package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class ListViewHolder extends  RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView nameView;
    public TextView addressView;
    public TextView serviceView;
    public RatingBar ratingBar;
    public Button hireButton;


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.list_view_imageView);
        nameView = itemView.findViewById(R.id.list_view_name);
        addressView = itemView.findViewById(R.id.list_view_address);
        serviceView = itemView.findViewById(R.id.list_view_service);
        ratingBar = itemView.findViewById(R.id.list_view_rating);
        hireButton = itemView.findViewById(R.id.hireButton);

    }
}
