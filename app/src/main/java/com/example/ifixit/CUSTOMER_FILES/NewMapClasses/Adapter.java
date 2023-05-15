package com.example.ifixit.CUSTOMER_FILES.NewMapClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private List<ItemView> itemViewList;
    private Context context;

    public Adapter(List<ItemView> itemViewList, Context context) {
        this.itemViewList = itemViewList;
        this.context = context;
    }


    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.service_provider_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemView itemView = itemViewList.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        String serviceproviderid = itemView.getUserid();
        holder.name.setText(itemView.getName());
        holder.service.setText(itemView.getService());
        holder.ratings.setRating((float)3.5);
        Glide.with(context).load(itemView.getProfileimageurl()).into(holder.profilepic);

        //Later
        holder.hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return itemViewList.size();
    }
}
