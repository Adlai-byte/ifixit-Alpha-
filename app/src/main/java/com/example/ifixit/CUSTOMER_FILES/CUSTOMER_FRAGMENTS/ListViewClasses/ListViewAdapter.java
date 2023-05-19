package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.CUSTOMER_FILES.CustomerCheckOutActivity;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<ListViewItem> listViewItems;
    private List<ListViewItem> originalList;
    private Context context;

    public ListViewAdapter(Context context, List<ListViewItem> listViewItems) {
        this.context = context;
        this.listViewItems = listViewItems;
        this.originalList = new ArrayList<>(listViewItems);
    }

    public void setFilteredList(List<ListViewItem> newFilteredList) {
        this.listViewItems.clear();
        this.listViewItems.addAll(newFilteredList);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.customer_list_view_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListViewItem listViewItem = listViewItems.get(position);

        holder.nameView.setText(listViewItem.getNAME());
        holder.serviceView.setText(listViewItem.getSERVICE());
        holder.addressView.setText(listViewItem.getADDRESS());
        holder.ratingBar.setRating(listViewItem.getRATING());
        holder.maxPrice.setText(String.valueOf(listViewItem.getMAXPRICE()));

        Glide.with(context).load(listViewItem.getProfileImageUrl()).into(holder.imageView);

        holder.hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serviceProviderUserId = listViewItem.getUSERID();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(context, CustomerCheckOutActivity.class);
                intent.putExtra("customerUserId", currentUserId);
                intent.putExtra("serviceProviderUserId", serviceProviderUserId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listViewItems.size();
    }
}
