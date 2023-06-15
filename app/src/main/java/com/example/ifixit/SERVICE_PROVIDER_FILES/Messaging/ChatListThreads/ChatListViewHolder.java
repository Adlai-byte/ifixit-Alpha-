package com.example.ifixit.SERVICE_PROVIDER_FILES.Messaging.ChatListThreads;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public ImageView chatProfilePic;
    public TextView chatName;
    public TextView chatService;
    public LinearLayout layout;

    public ChatListViewHolder(@NonNull View itemView) {
        super(itemView);

        layout = itemView.findViewById(R.id.listLayout);
        chatProfilePic = itemView.findViewById(R.id.profilePic);
        chatName = itemView.findViewById(R.id.name);
        chatService = itemView.findViewById(R.id.service);




    }
}
