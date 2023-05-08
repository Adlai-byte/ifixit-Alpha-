package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder> {
    Context context;
    List<ListViewItem> listViewItems;

    public ListViewAdapter (Context context,List<ListViewItem> listViewItems){
        this.context = context;
        this.listViewItems = listViewItems  ;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_list_view_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        ListViewItem listViewItem = listViewItems.get(holder.getAdapterPosition());
        Glide.with(context).load(listViewItem.getProfileImageUrl()).into(holder.imageView);
        holder.nameView.setText(listViewItem.getNAME());
        holder.addressView.setText(listViewItem.getADDRESS());;
        holder.serviceView.setText(listViewItem.getSERVICE());
        holder.ratingBar.setRating(listViewItem.getRATING());

    }

    @Override
    public int getItemCount() {
        return listViewItems.size();
    }
}
