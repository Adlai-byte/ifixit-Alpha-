package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ServiceProviderMessaging;

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

public class ServiceProviderChatActivity extends AppCompatActivity {

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
    private ServiceProviderMessageAdapter serviceProviderMessageAdapter;

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

                if (!TextUtils.isEmpty(receiverId)) {
                    loadMessages(senderId, receiverId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }
        });

        messagesRef = FirebaseDatabase.getInstance().getReference().child("MESSAGES");
        jobOffersRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(senderId).child("JOB-OFFERS");
        customerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS");

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        messageRecyclerView = findViewById(R.id.message_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        List<ServiceProviderMessage> serviceProviderMessageList = new ArrayList<>();
        serviceProviderMessageAdapter = new ServiceProviderMessageAdapter(serviceProviderMessageList, senderId);
        messageRecyclerView.setAdapter(serviceProviderMessageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!TextUtils.isEmpty(messageText) && !TextUtils.isEmpty(receiverId)) {
                    sendMessage(senderId, receiverId, messageText);

                    messageEditText.setText("");
                } else {
                    Toast.makeText(ServiceProviderChatActivity.this, "ServiceProviderMessage cannot be empty", Toast.LENGTH_SHORT).show();
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

                                    // Load the messages between sender and receiver
                                    loadMessages(senderId, receiverId);
                                } else {
                                    Toast.makeText(ServiceProviderChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
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
            ServiceProviderMessage serviceProviderMessage = new ServiceProviderMessage(messagePushId, messageText, senderId, receiverId, timestamp);
            messagesRef.child(senderId).child(receiverId).child(messagePushId).setValue(serviceProviderMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        messagesRef.child(receiverId).child(senderId).child(messagePushId).setValue(serviceProviderMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ServiceProviderChatActivity.this, "ServiceProviderMessage sent successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceProviderChatActivity.this, "Failed to send serviceProviderMessage", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ServiceProviderChatActivity.this, "Failed to send serviceProviderMessage", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ServiceProviderChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages(String senderId, String receiverId) {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    List<ServiceProviderMessage> serviceProviderMessageList = new ArrayList<>();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {


                        ServiceProviderMessage serviceProviderMessage = messageSnapshot.getValue(ServiceProviderMessage.class);

                        if (serviceProviderMessage != null) {
                            String messageSenderId = serviceProviderMessage.getSenderUid();
                            String messageReceiverId = serviceProviderMessage.getReceiverUid();

                            if ((senderId != null && messageSenderId != null && senderId.equals(messageSenderId) && receiverId != null && messageReceiverId != null && receiverId.equals(messageReceiverId)) ||
                                    (senderId != null && messageReceiverId != null && senderId.equals(messageReceiverId) && receiverId != null && messageSenderId != null && receiverId.equals(messageSenderId))) {
                                serviceProviderMessageList.add(serviceProviderMessage);

                                // Mark the serviceProviderMessage as read by the receiver
                                if (messageReceiverId != null && senderId != null && messageReceiverId.equals(senderId) && !serviceProviderMessage.isRead()) {
                                    serviceProviderMessage.setRead(true);
                                    messageSnapshot.getRef().setValue(serviceProviderMessage);
                                }
                            }
                        }
                    }
                    serviceProviderMessageAdapter.setMessageList(serviceProviderMessageList);
                    messageRecyclerView.scrollToPosition(serviceProviderMessageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }
        });
    }



}


