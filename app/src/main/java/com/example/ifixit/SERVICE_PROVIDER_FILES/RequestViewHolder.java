package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView nameView;
    public TextView location;
    public TextView jobType;
    public TextView totalPrice;

    public Button BtnAccept;
    public Button Btndecline;

    public RequestViewHolder(@NonNull View itemView) {

        super(itemView);
        imageView = itemView.findViewById(R.id.userProfile);
        nameView = itemView.findViewById(R.id.notificationName);
        location = itemView.findViewById(R.id.notificationLocation);
        jobType = itemView.findViewById(R.id.notificationJobType);
        totalPrice = itemView.findViewById(R.id.notificationTotalPrice);
        BtnAccept = itemView.findViewById(R.id.notificationAccept);
        Btndecline = itemView.findViewById(R.id.notificationDecline);


    }
}
