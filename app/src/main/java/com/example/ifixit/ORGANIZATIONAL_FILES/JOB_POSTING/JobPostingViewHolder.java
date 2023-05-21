package com.example.ifixit.ORGANIZATIONAL_FILES.JOB_POSTING;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class JobPostingViewHolder extends RecyclerView.ViewHolder {

    public TextView orgName,serviceType,name,maxPrice,minPrice,address,decription;
    public Button acceptButton;

    public JobPostingViewHolder(@NonNull View itemView) {
        super(itemView);

        orgName = itemView.findViewById(R.id.job_list_org);
        serviceType  = itemView.findViewById(R.id.job_list_service);
        name = itemView.findViewById(R.id.job_list_name);
        maxPrice = itemView.findViewById(R.id.job_list_maxPrice);
        minPrice = itemView.findViewById(R.id.job_list_minPrice);
        address = itemView.findViewById(R.id.job_list_address);
        decription = itemView.findViewById(R.id.job_list_description);

        acceptButton = itemView.findViewById(R.id.job_list_accept_btn);

    }
}
