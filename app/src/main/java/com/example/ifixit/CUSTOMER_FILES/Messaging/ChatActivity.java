package com.example.ifixit.CUSTOMER_FILES.Messaging;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.CUSTOMER_FILES.CustomerMainMenuActivity;
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


    private ImageView chatImage;
    private TextView chatName;
    private ImageView backButton;

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private String serviceProviderId;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private List<String> contactIds;
    private DatabaseReference messagesDatabaseRef;
    private Uri resultUri;
    private ChildEventListener messageChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = (ImageView) findViewById(R.id.backArrow);

        // -- Contacts ArrayList --

        contactIds = new ArrayList<>();

        // -- Chat Header --
        chatImage = findViewById(R.id.chatProfileImage);
        chatName =findViewById(R.id.chatName);
        // ---------------------------------------------
        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        //Fetch the data from the ChatList Activity
        String chatKey = intent.getStringExtra("chatKey");
        String name = intent.getStringExtra("name");
        String service = intent.getStringExtra("service");
        String profileimageurl = intent.getStringExtra("profileimageurl");



        //Setting the information
        Glide.with(this).load(profileimageurl).into(chatImage);
        chatName.setText(name);

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId);



        // -- Getting the Details from your database

        DatabaseReference customerChatThreadList = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId)
                .child("chat-thread-list");



        messagesDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("chat-rooms")
                .child(chatKey);




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(currentUserRef,currentUserId);
            }
        });


        retrieveMessages();





        //Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, CustomerMainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    //Send Message Function
    private void sendMessage(DatabaseReference currentUserRef,String currentUserId) {

        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());




            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String senderName = snapshot.child("name").getValue(String.class);
                    boolean isOutgoing = true; // Assume all messages sent by the current user are outgoing
                    ChatMessage chatMessage = new ChatMessage(message, timestamp, isOutgoing,senderName,currentUserId);
                    messagesDatabaseRef.push().setValue(chatMessage);
                    messageEditText.setText("");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    }

    private void retrieveMessages() {
        messageChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                if (chatMessage != null) {
                    messageList.add(chatMessage);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        messagesDatabaseRef.addChildEventListener(messageChildEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesDatabaseRef != null && messageChildEventListener != null) {
            messagesDatabaseRef.removeEventListener(messageChildEventListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            chatImage.setImageURI(resultUri);
        }
    }



}
