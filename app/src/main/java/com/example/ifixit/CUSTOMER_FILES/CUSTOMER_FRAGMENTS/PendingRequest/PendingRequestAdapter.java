package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

import java.util.List;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestViewHolder> {

    private List<PendingRequestItem> pendingRequestItemList;
    private Context context;

    public PendingRequestAdapter(List<PendingRequestItem> pendingRequestItemList, Context context) {
        this.pendingRequestItemList = pendingRequestItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.customer_pending_request_item, parent, false);

        return new PendingRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestViewHolder holder, int position) {

        PendingRequestItem pendingRequestItem = pendingRequestItemList.get(position);

        holder.status.setText(pendingRequestItem.getJobtype());
        holder.status.setText(pendingRequestItem.getStatus());
        holder.name.setText(pendingRequestItem.getName());
        holder.service.setText(pendingRequestItem.getService());
        holder.timestamp.setText(pendingRequestItem.getFormattedTimestamp());
        holder.total.setText(pendingRequestItem.getTotal());
        holder.jobtype.setText(pendingRequestItem.getJobtype());

    }

    @Override
    public int getItemCount() {
        return pendingRequestItemList.size();
    }
}
