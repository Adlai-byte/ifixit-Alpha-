package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.Item;
import com.example.ifixit.SERVICE_PROVIDER_FILES.MyViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


    Context context;
    List<Item> items;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = items.get(holder.getAdapterPosition());
        holder.nameView.setText(item.getName());
        holder.template.setText(item.getTemplate());

        holder.BtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = item.getUserId();
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("USERS")
                        .child("SERVICE-PROVIDERS")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("JOB-OFFERS")
                        .child("ONGOING")
                        .child(userId);
                ongoingRef.setValue(item);

                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("USERS")
                        .child("SERVICE-PROVIDERS")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("JOB-OFFERS")
                        .child("PENDING")
                        .child(userId);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                items.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, items.size());
            }
        });

        holder.Btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = item.getUserId();
                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("USERS")
                        .child("SERVICE-PROVIDERS")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("JOB-OFFERS")
                        .child("PENDING")
                        .child(userId);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                items.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, items.size());
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}

