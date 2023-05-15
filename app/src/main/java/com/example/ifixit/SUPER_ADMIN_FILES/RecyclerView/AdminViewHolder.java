package com.example.ifixit.SUPER_ADMIN_FILES.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class AdminViewHolder extends RecyclerView.ViewHolder {

    public TextView serviceProviderName;
    public TextView serviceProviderAddress;
    public TextView serviceProviderJob;
    public TextView serviceProviderRate;
    public ImageView serviceProviderImageView;
    public Button acceptButton;
    public Button declineButton;


    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);
        serviceProviderName = itemView.findViewById(R.id.adminserviceProviderName);
        serviceProviderAddress = itemView.findViewById(R.id.adminserviceProviderAddress);
        serviceProviderJob = itemView.findViewById(R.id.adminserviceProviderJob);
        serviceProviderRate = itemView.findViewById(R.id.adminservicerProviderRate);
        acceptButton = itemView.findViewById(R.id.adminbtnAccept);
        declineButton = itemView.findViewById(R.id.adminbtnDecline);
        serviceProviderImageView = itemView.findViewById(R.id.serviceProviderProfileImage);



    }
}
