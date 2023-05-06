package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ServiceProviderMessaging;

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

public class ServiceProviderMessageAdapter extends RecyclerView.Adapter<ServiceProviderMessageAdapter.MessageViewHolder> {
    public List<ServiceProviderMessage> getMessageList() {
        return serviceProviderMessageList;
    }

    public void setMessageList(List<ServiceProviderMessage> serviceProviderMessageList) {
        this.serviceProviderMessageList = serviceProviderMessageList;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private List<ServiceProviderMessage> serviceProviderMessageList;
    private String currentUserId;

    public ServiceProviderMessageAdapter(List<ServiceProviderMessage> serviceProviderMessageList, String currentUserId) {
        this.serviceProviderMessageList = serviceProviderMessageList;
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
        ServiceProviderMessage serviceProviderMessage = serviceProviderMessageList.get(position);
        holder.messageTextView.setText(serviceProviderMessage.getMessageText());

        if (serviceProviderMessage.getSenderUid().equals(currentUserId)) {
            // Set sender's serviceProviderMessage view properties
            holder.messageTextView.setBackgroundResource(R.drawable.incoming_message_bg);
            holder.messageTextView.setTextColor(Color.WHITE);
            holder.messageLinearLayout.setGravity(Gravity.END);
        } else {
            // Set receiver's serviceProviderMessage view properties
            holder.messageTextView.setBackgroundResource(R.drawable.message_bubble);
            holder.messageTextView.setTextColor(Color.BLACK);
            holder.messageLinearLayout.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return serviceProviderMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ServiceProviderMessage serviceProviderMessage = serviceProviderMessageList.get(position);
        if (serviceProviderMessage.getSenderUid().equals(currentUserId)) {
            return R.layout.service_provider_sender_message_item;
        } else {
            return R.layout.service_provider_receiver_message_item;
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
