package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageTextView.setText(message.getMessageText());

        if (message.getSenderUid().equals(currentUserId)) {
            // Set sender's message view properties
            holder.messageTextView.setBackgroundResource(R.drawable.incoming_message_bg);
            holder.messageTextView.setTextColor(Color.WHITE);
            holder.messageLinearLayout.setGravity(Gravity.END);
        } else {
            // Set receiver's message view properties
            holder.messageTextView.setBackgroundResource(R.drawable.message_bubble);
            holder.messageTextView.setTextColor(Color.BLACK);
            holder.messageLinearLayout.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderUid().equals(currentUserId)) {
            return R.layout.sender_message_item;
        } else {
            return R.layout.receiver_message_item;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public LinearLayout messageLinearLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            messageLinearLayout = itemView.findViewById(R.id.message_text_view);
        }
    }
}
