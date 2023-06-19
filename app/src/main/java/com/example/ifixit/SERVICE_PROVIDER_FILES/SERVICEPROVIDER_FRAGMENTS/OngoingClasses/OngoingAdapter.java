package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses;

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

public class OngoingAdapter extends RecyclerView.Adapter<OngoingHolder> {

    private List<OngoingViewItem> ongoingViewItems;
    private Context context;

    public OngoingAdapter(Context context, List<OngoingViewItem> ongoingViewItems) {
        this.context = context;
        this.ongoingViewItems = ongoingViewItems;
    }

    @NonNull
    @Override
    public OngoingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OngoingHolder(LayoutInflater.from(context).inflate(R.layout.service_provider_ongoing_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingHolder holder, int position) {

        OngoingViewItem ongoingViewItem = ongoingViewItems.get(holder.getAdapterPosition());

        holder.nameView.setText(ongoingViewItem.getNAME());
        holder.totalPriceView.setText(ongoingViewItem.getTOTALPRICE());
        holder.locationView.setText(ongoingViewItem.getLOCATION());

        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerUserId = ongoingViewItem.getUSERID();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child(customerUserId)
                        .child("joboffers")
                        .child("ongoing")
                        .child(customerUserId);
                ongoingRef.removeValue();


                int itemPosition  = holder.getAdapterPosition();
                ongoingViewItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition,ongoingViewItems.size());

                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUserId)
                        .child("notification")
                        .child(currentUserId);
                customerRef.setValue("FINISHED");

            }





        });

    }

    @Override
    public int getItemCount() {
        return ongoingViewItems.size();
    }
}