package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class PendingRequestViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView service;
    public TextView timestamp;
    public TextView total;
    public TextView status;
    public TextView jobtype;



     public PendingRequestViewHolder(@NonNull View itemView) {
        super(itemView);


        name = itemView.findViewById(R.id.cpSPname);
        service = itemView.findViewById(R.id.cpSPservice);
        timestamp = itemView.findViewById(R.id.cpSPtimestamp);
        total =itemView.findViewById(R.id.cpSPtotal);
        status = itemView.findViewById(R.id.cpSPstatus);
        jobtype = itemView.findViewById(R.id.cpSPjobtype);

    }
}
