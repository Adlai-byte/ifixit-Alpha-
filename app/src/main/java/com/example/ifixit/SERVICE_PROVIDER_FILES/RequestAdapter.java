package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {


    Context context;
    List<RequestItem> requestItems;

    public RequestAdapter(Context context, List<RequestItem> requestItems) {
        this.context = context;
        this.requestItems = requestItems;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(context).inflate(R.layout.service_provider_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestItem requestItem = requestItems.get(holder.getAdapterPosition());
        holder.totalPrice.setText(requestItem.getTOTALPRICE());
        holder.location.setText(requestItem.getLOCATION());;
        holder.jobType.setText(requestItem.getJOBTYPE());

        Glide.with(context).load(requestItem.getProfileImageUrl())
                        .into(holder.imageView);


        holder.nameView.setText(requestItem.getNAME());

        holder.BtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = requestItem.getUSERID();
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("ongoing")
                        .child(userId);
                ongoingRef.setValue(requestItem);

                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("pending")
                        .child(userId);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                requestItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, requestItems.size());
            }
        });
        holder.Btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = requestItem.getUSERID();
                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("pending")
                        .child(userId);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                requestItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, requestItems.size());
            }
        });
    }



    @Override
    public int getItemCount() {
        return requestItems.size();
    }


}

