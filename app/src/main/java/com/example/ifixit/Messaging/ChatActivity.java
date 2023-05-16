package com.example.ifixit.Messaging;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messagesRef;
    private String currentUserId;
    String name = null;
    String profileUrl = null;

    private ChatAdapter chatSentAdapter;

    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        messagesRef = firebaseDatabase.getReference("messages");
        chatMessages = new ArrayList<>();

        chatSentAdapter = new ChatAdapter(chatMessages,ChatActivity.this);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(chatSentAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        retrieveMessages();
    }

    private void sendMessage() {

        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("customers").child(currentUserId);
        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference().child("service-providers").child("verified").child(currentUserId);



        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    name = snapshot.child("name").getValue(String.class);
                    profileUrl = snapshot.child("profileimageurl").getValue(String.class);

                }else {
                    serviceRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                name = snapshot.child("name").getValue(String.class);
                                profileUrl = snapshot.child("profileimageurl").getValue(String.class);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String messageText = messageEditText.getText().toString().trim();
//        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            ChatMessage chatMessage = new ChatMessage(name, messageText, timestamp,profileUrl);
            messagesRef.push().setValue(chatMessage);
            messageEditText.setText("");
        }
    }

    private void retrieveMessages() {


        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildKey) {

                String userid = snapshot.child("sender").getValue(String.class);


                ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                chatSentAdapter.addMessage(chatMessage);
                recyclerView.smoothScrollToPosition(chatMessages.size() - 1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            // Other methods of ChildEventListener are not implemented here, but you may need to handle them based on your requirements
        });
    }
}
