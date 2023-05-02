package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private ListView messageListView;
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


        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
        jobOffersRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(senderId).child("JOB-OFFERS");
        customerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS");

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        messageListView = findViewById(R.id.message_list);

        ArrayList<Message> messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

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
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                customerUserId = dataSnapshot.getKey();
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
        });

        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.getKey() != null) {
                    String messageKey = dataSnapshot.getKey();
                    Message message = dataSnapshot.getValue(Message.class);

                    // Check if the message is for the current user
                    if (message.getReceiverUid() != null && message.getReceiverUid().equals(customerUserId)) {
                        // Add the message to the list
                        messageList.add(message);

                        // Notify the adapter that the data has changed
                        messageAdapter.notifyDataSetChanged();
                    }
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
        });
    }

    private void sendMessage(String senderId, String receiverId, String messageText) {
        long timestamp = System.currentTimeMillis();
        String messageId = messagesRef.push().getKey();

        // Get the reference to the CUSTOMERS directory
        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference().child("USERS")
                .child("CUSTOMERS")
                .child(receiverId)
                .child("Messages");

        // Create a new message object and set its values
        Message message = new Message(messageId, senderId, receiverId, messageText, timestamp);

        // Use the customerUserId as the key in the customer's Messages directory
        customersRef.child(senderId).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Message sent successfully
                } else {
                    // Error sending message
                }
            }
        });

        // Add the message to the Service Provider's Messages directory in the Job-Offer/Ongoing directory
        DatabaseReference serviceProviderRef = FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("SERVICE-PROVIDERS")
                .child(senderId)
                .child("JOB-OFFERS")
                .child("ONGOING")
                .child(receiverId)
                .child("Messages");

        serviceProviderRef.child(messageId).setValue(message);
    }
}