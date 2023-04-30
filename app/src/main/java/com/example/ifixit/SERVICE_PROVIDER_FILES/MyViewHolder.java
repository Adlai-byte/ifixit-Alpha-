package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView nameView;
    public TextView template;
    public Button BtnAccept;
    public Button Btndecline;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.userProfile);
        nameView = itemView.findViewById(R.id.notificationName);
        template = itemView.findViewById(R.id.template);
        BtnAccept = itemView.findViewById(R.id.notificationAccept);
        Btndecline = itemView.findViewById(R.id.notificationDecline);

    }
}
