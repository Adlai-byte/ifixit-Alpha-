package com.example.ifixit.SERVICE_PROVIDER_FILES.JOB_POSTING;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
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

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

                HashMap<String, Object> jobPostingItem = new HashMap<>();

                String jobPostingKey = itemView.getOrderid();
                DatabaseReference ongoingRef  = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("joboffers")
                        .child("ongoing")
                        .child(itemView.getOrderid());


                jobPostingItem.put("address",itemView.getAddress());
                jobPostingItem.put("email","");
                jobPostingItem.put("jobtype",itemView.getServiceType());
                jobPostingItem.put("name",itemView.getOrgName());
                jobPostingItem.put("totalprice",itemView.getMaxPrice());

                ongoingRef.updateChildren(jobPostingItem);



                DatabaseReference jobPosting = FirebaseDatabase.getInstance().getReference()
                        .child("job-posting")
                        .child(jobPostingKey);



            }
        });


    }

    @Override
    public int getItemCount() {
       return   itemViewList.size();
    }
}
