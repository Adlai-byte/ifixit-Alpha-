package com.example.ifixit.ORGANIZATIONAL_FILES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {


    Context context;
    List<ReviewItem> reviewItems;

    public ReviewAdapter (Context context, List<ReviewItem> reviewItems){
        this.context = context;
        this.reviewItems = reviewItems;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_reviews_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem reviewItem = reviewItems.get(holder.getAdapterPosition());
        Glide.with(context).load(reviewItem.getProfileImageUrl()).into(holder.imageView);
        holder.nameView.setText(reviewItem.getNAME());
        holder.reviewView.setText(reviewItem.getREVIEWS());



    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }
}
