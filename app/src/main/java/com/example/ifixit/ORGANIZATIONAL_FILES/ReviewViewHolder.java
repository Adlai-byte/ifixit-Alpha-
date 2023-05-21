package com.example.ifixit.ORGANIZATIONAL_FILES;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView nameView;
    public TextView reviewView;

    public ReviewViewHolder(@NonNull View itemView){
        super(itemView);

        imageView = itemView.findViewById(R.id.reviewUserProfile);
        nameView = itemView.findViewById(R.id.reviewName);
        reviewView = itemView.findViewById(R.id.reviewComment);
    }


}
