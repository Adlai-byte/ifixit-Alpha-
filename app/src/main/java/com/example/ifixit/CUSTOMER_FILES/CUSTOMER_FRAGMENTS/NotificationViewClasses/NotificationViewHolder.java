package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.NotificationViewClasses;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class NotificationViewHolder extends  RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView nameView;
    public TextView serviceView;
    public RatingBar ratingBar;
    public EditText reviewComment;
    public Button completeButton;


    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.notification_view_imageView);
        nameView = itemView.findViewById(R.id.notification_view_name);
        reviewComment = itemView.findViewById(R.id.notificationReviewComment);
        serviceView = itemView.findViewById(R.id.notification_view_service);
        ratingBar = itemView.findViewById(R.id.notification_view_rating);
        completeButton = itemView.findViewById(R.id.notificationConfirmButton);

    }
}
