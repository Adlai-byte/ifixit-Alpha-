package com.example.ifixit.CUSTOMER_FILES.NewMapClasses;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView profilepic;
    public TextView name;
    public TextView service;
    public RatingBar ratings;
    public Button hire;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        profilepic = itemView.findViewById(R.id.spCardProfile);
        name = itemView.findViewById(R.id.spCardName);
        service = itemView.findViewById(R.id.spCardService);
        ratings = itemView.findViewById(R.id.spCardRating);
        hire = itemView.findViewById(R.id.spCardHireButton);


    }
}
