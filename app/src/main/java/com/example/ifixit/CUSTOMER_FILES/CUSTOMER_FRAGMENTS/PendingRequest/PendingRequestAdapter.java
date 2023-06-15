package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest;

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

        String currentuserid = FirebaseAuth.getInstance().getUid();
        holder.status.setText(pendingRequestItem.getStatus());
        holder.name.setText(pendingRequestItem.getName());
        holder.service.setText(pendingRequestItem.getService());
        holder.timestamp.setText(pendingRequestItem.getTimestamp());
        holder.total.setText(pendingRequestItem.getTotal());
        holder.jobtype.setText(pendingRequestItem.getJobtype());





        //Cancel the request
        holder.cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pendingRequestRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(currentuserid)
                        .child("pending-request")
                        .child(pendingRequestItem.getKey());
                pendingRequestRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                pendingRequestItemList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition,pendingRequestItemList.size());


            }
        });


    }

    @Override
    public int getItemCount() {
        return pendingRequestItemList.size();
    }
}
