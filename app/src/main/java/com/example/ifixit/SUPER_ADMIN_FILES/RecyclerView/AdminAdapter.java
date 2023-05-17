package com.example.ifixit.SUPER_ADMIN_FILES.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminViewHolder> {
    private List<AdminItemView> adminItemViewList;
    private Context context;

    public AdminAdapter(List<AdminItemView> adminItemViewList, Context context) {
        this.adminItemViewList = adminItemViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        AdminItemView adminItemView = adminItemViewList.get(position);

        holder.serviceProviderName.setText(adminItemView.getNAME());
        holder.serviceProviderAddress.setText(adminItemView.getADDRESS());
        holder.serviceProviderJob.setText(adminItemView.getJOBTYPE());
        holder.serviceProviderRate.setText(adminItemView.getRATE());
        Glide.with(context).load(adminItemView.getProfileImageUrl()).into(holder.serviceProviderImageView);
        //------Button Shit--------
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serviceProviderId = adminItemView.getUSERID();
                DatabaseReference verifiedServiceProvider = FirebaseDatabase.getInstance().getReference()

                        .child("service-providers")
                        .child("verified")
                        .child(serviceProviderId);
                verifiedServiceProvider.setValue(adminItemView);

                DatabaseReference unverifiedServiceProvider = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("unverified")
                        .child(serviceProviderId);
                unverifiedServiceProvider.removeValue();

                int itemPosition = holder.getAdapterPosition();
                adminItemViewList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, adminItemViewList.size());

            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serviceProviderId = adminItemView.getUSERID();
                DatabaseReference unverifiedServiceProvider = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("unverified")
                        .child(serviceProviderId);
                unverifiedServiceProvider.removeValue();

                int itemPosition = holder.getAdapterPosition();
                adminItemViewList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, adminItemViewList.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return adminItemViewList.size();
    }
}
