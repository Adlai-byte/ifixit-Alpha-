package com.example.ifixit.ORGANIZATIONAL_FILES.JOB_POSTING;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ifixit.R;

import java.util.List;

public class JobPostingAdapter extends RecyclerView.Adapter<JobPostingViewHolder> {

    private List<JobPostingItemView> itemViewList;
    private Context context;

    public JobPostingAdapter(List<JobPostingItemView> itemViewList, Context context) {
        this.itemViewList = itemViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public JobPostingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobPostingViewHolder(LayoutInflater.from(context).inflate(R.layout.org_job_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostingViewHolder holder, int position) {

        JobPostingItemView itemView = itemViewList.get(position);
        holder.orgName.setText(itemView.getOrgName());
        holder.name.setText(itemView.getName());;
        holder.address.setText(itemView.getAddress());
        holder.decription.setText(itemView.getDescription());
        holder.minPrice.setText(itemView.getMinPrice());
        holder.maxPrice.setText(itemView.getMaxPrice());
        holder.serviceType.setText(itemView.getServiceType());



        //Later
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
