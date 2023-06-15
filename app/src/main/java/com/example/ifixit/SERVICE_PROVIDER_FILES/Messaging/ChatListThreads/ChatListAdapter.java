package com.example.ifixit.SERVICE_PROVIDER_FILES.Messaging.ChatListThreads;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.Messaging.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {

    private List<ChatListItem> chatListItemList;
    private Context context;

    public ChatListAdapter(List<ChatListItem> chatListItemList, Context context) {
        this.chatListItemList = chatListItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.chat_list_item,parent,false);
        return new ChatListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {

        ChatListItem chatListItem = chatListItemList.get(position);

        String currentUserid = FirebaseAuth.getInstance().getUid();
        holder.chatName.setText(chatListItem.getName());
        holder.chatService.setText(chatListItem.getService());
        Glide.with(context).load(chatListItem.getProfileimagurl()).into(holder.chatProfilePic);



        //If Clicked -> Chat Activity
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);

                intent.putExtra("chatKey", chatListItem.getChatKey());
                intent.putExtra("name", chatListItem.getName());
                intent.putExtra("service", chatListItem.service);
                intent.putExtra("profileimageurl", chatListItem.profileimagurl);

                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        return chatListItemList.size();
    }
}
