package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private String senderId;
    private String receiverId;
    private DatabaseReference messagesRef;
    private DatabaseReference senderRef;
    private DatabaseReference jobOffersRef;
    private DatabaseReference customerRef;
    private String customerUserId;
    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_chat);

        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverId = "";
        DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(senderId).child("JOB-OFFERS").child("ONGOING");
        ongoingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        receiverId = child.getKey();
                        break;
                    }
                }
                // Do something with the first node here...
                Log.d("TAG", "First node: " + receiverId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }
        });


        messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        jobOffersRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(senderId).child("JOB-OFFERS");
        customerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS");

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        messageRecyclerView = findViewById(R.id.message_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        List<Message> messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList,senderId);
        messageRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(messageText)) {
                    sendMessage(senderId, receiverId, messageText);
                    messageEditText.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        jobOffersRef.child("ONGOING").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    receiverId = snapshot.getKey();
                    DatabaseReference customerUserIdRef = jobOffersRef.child("ONGOING").child(receiverId).child("customerId");
                    customerUserIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                customerUserId = dataSnapshot.getValue().toString();
                                String messagePushId = snapshot.getKey();
                                if (messagePushId != null && !messagePushId.isEmpty()) {
                                    jobOffersRef.child("ONGOING").child(receiverId).child("Messages").child(messagePushId).setValue(true);
                                    customerRef.child(customerUserId).child("JOB-OFFERS").child("ONGOING").child(senderId).child("Messages").child(messagePushId).setValue(true);
                                } else {
                                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("TAG", "Error: " + error.getMessage());
                        }
                    });

                }
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
        });
    }

    private void sendMessage(String senderId, String receiverId, String messageText) {
        String messagePushId = messagesRef.child(senderId).child(receiverId).push().getKey();
        if (messagePushId != null && !messagePushId.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            Message message = new Message(messagePushId, messageText, senderId, receiverId, timestamp);
            messagesRef.child(senderId).child(receiverId).child(messagePushId).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        messagesRef.child(receiverId).child(senderId).child(messagePushId).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChatActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadMessages(String senderId, String receiverId, String customerUserId) {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Message> messageList = new ArrayList<>();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        if ((message.getSenderUid().equals(senderId) && message.getReceiverUid().equals(receiverId)) || (message.getSenderUid().equals(receiverId) && message.getReceiverUid().equals(senderId))) {
                            messageList.add(message);
                            // Mark the message as read by the receiver
                            if (message.getReceiverUid().equals(senderId) && !message.isRead()) {
                                message.setRead(true);
                                messageSnapshot.getRef().setValue(message);
                            }
                        }
                    }
                    messageAdapter.setMessageList(messageList);
                    messageRecyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }
        });
    }

}
