package com.example.ifixit.CUSTOMER_FILES.Messaging;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.CUSTOMER_FILES.Messaging.ChatListThreads.ChatListAdapter;
import com.example.ifixit.CUSTOMER_FILES.Messaging.ChatListThreads.ChatListItem;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ChatListItem> chatListItemList;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.chatListRecyclerView);
        chatListItemList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatListItemList,this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(chatListAdapter);
        String currentID = FirebaseAuth.getInstance().getUid();

        //Database
        DatabaseReference query = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentID)
                .child("chat-thread-list");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatListItemList.clear();
                if(snapshot.exists()){
                    Toast.makeText(ChatListActivity.this, "snapshop", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot childSnapshot :snapshot.getChildren()){
                        String chatKey = childSnapshot.getKey();
                        String name =childSnapshot.child("name").getValue(String.class);
                        String service = childSnapshot.child("service").getValue(String.class);
                        String profileimageurl=childSnapshot.child("profileimageurl").getValue(String.class);

                        ChatListItem item = new ChatListItem(chatKey,name,service,profileimageurl);
                        chatListItemList.add(item);
                    }
                    chatListAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(ChatListActivity.this, "Chat List is Empty", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }



}