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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private ImageView chatImage;
    private TextView chatName;

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

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId);

        // -- Getting the Details from your database

        DatabaseReference currentUserRefContacts = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId)
                .child("contacts");




        currentUserRefContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactIds.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        contactIds.add(userId);
                    }

                    if (!contactIds.isEmpty()) {
                        String serviceProviderId = contactIds.get(0);
                        String chatId = currentUserId + serviceProviderId; // Replace with your own chat ID

                        DatabaseReference serviceProviderRef = FirebaseDatabase.getInstance().getReference()
                                .child("service-providers")
                                .child("verified")
                                .child(serviceProviderId);

                        serviceProviderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()&& snapshot.getChildrenCount()>0){
                                    Map<String,Object>map =(Map<String, Object>)snapshot.getValue();
                                    if(map.get("name")!=null){
                                        String name =  map.get("name").toString();
                                        chatName.setText(name);
                                    }
                                    if(map.get("profileimageurl")!=null){
                                        String url = map.get("profileimageurl").toString();
                                        Glide.with(getApplication()).load(url).into(chatImage);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        messagesDatabaseRef = FirebaseDatabase.getInstance().getReference().child("messages").child(chatId);
                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendMessage(currentUserRef,currentUserId);
                            }
                        });

                        retrieveMessages();
                    } else {
                        // Handle case when contactIds is empty
                        Toast.makeText(ChatActivity.this, "No contacts found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "Snapshot doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });


    }

    private void sendMessage(DatabaseReference currentUserRef,String currentUserId) {
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());




            currentUserRef.addValueEventListener(new ValueEventListener() {
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
