package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingHolder> {

    private String paymentStatus;
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
        holder.dateView.setText(ongoingViewItem.getDate());
        holder.jobTypeView.setText(ongoingViewItem.getJOBTYPE());


        holder.ongoingCancelledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String key = ongoingViewItem.getKey();
                String customerUserId = ongoingViewItem.getUSERID();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference customerTransactionHistory = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUserId)
                        .child("transaction-history");

                //Transaction History Reference (Service Provider)
                DatabaseReference serviceProviderTransactionHistory = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("transaction-history");

                //Transaction Item HashMap
                Map<String,Object> transactionHistory = new HashMap();
                transactionHistory.put("service",ongoingViewItem.getJOBTYPE());
                transactionHistory.put("name",ongoingViewItem.getNAME());
                transactionHistory.put("total",ongoingViewItem.getTOTALPRICE());
                transactionHistory.put("date",ongoingViewItem.getDate());
                transactionHistory.put("status","Cancelled");
                transactionHistory.put("userid",currentUserId);

                //Sending data to the transaction History
                serviceProviderTransactionHistory.push().setValue(transactionHistory);
                customerTransactionHistory.push().setValue(transactionHistory);


                //Deleting the chat room
                DatabaseReference currentChatRoom = FirebaseDatabase.getInstance().getReference()
                        .child("chat-rooms")
                        .child(currentUserId+customerUserId);
                currentChatRoom.removeValue();
                //Deleting the chat room in the respected threads
                DatabaseReference spThread = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("chat-thread-list")
                        .child(currentUserId+customerUserId);
                spThread.removeValue();

                DatabaseReference custThread = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUserId)
                        .child("chat-thread-list")
                        .child(currentUserId+customerUserId);
                custThread.removeValue();
                //----------------------------------------------------------------------------------

                //Removing the values
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("joboffers")
                        .child("ongoing")
                        .child(key);
                ongoingRef.removeValue();

                int itemPosition  = holder.getAdapterPosition();
                ongoingViewItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition,ongoingViewItems.size());



            }
        });
        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shows the validation layout
                holder.validation.setVisibility(View.VISIBLE);
                //Pag click ang confirm button

            }

        });

        holder.completeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String key = ongoingViewItem.getKey();
                String customerUserId = ongoingViewItem.getUSERID();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



                //Customer Notification Reference
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUserId)
                        .child("notification");


                //Transaction History Reference (Service Provider)
                DatabaseReference serviceProviderTransactionHistory = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("transaction-history");

                //Transaction Item HashMap
                Map<String,Object> transactionHistory = new HashMap();
                transactionHistory.put("service",ongoingViewItem.getJOBTYPE());
                transactionHistory.put("name",ongoingViewItem.getNAME());
                transactionHistory.put("total",ongoingViewItem.getTOTALPRICE());
                transactionHistory.put("date",ongoingViewItem.getDate());
                transactionHistory.put("status",paymentStatus);
                transactionHistory.put("userid",currentUserId);

                //Sending data to the transaction History
                serviceProviderTransactionHistory.push().setValue(transactionHistory);
                //Sending data to the customers notification
                customerRef.push().setValue(transactionHistory);


                //Deleting the chat room
                DatabaseReference currentChatRoom = FirebaseDatabase.getInstance().getReference()
                        .child("chat-rooms")
                        .child(currentUserId+customerUserId);
                currentChatRoom.removeValue();
                //Deleting the chat room in the respected threads
                DatabaseReference spThread = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("chat-thread-list")
                        .child(currentUserId+customerUserId);
                spThread.removeValue();

                DatabaseReference custThread = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUserId)
                        .child("chat-thread-list")
                        .child(currentUserId+customerUserId);
                custThread.removeValue();
                //----------------------------------------------------------------------------------



                //Removing the values
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserId)
                        .child("joboffers")
                        .child("ongoing")
                        .child(key);
                ongoingRef.removeValue();

                int itemPosition  = holder.getAdapterPosition();
                ongoingViewItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition,ongoingViewItems.size());




            }
        });


        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioButton = radioGroup.findViewById(i);
                if(radioButton!=null&radioButton.isChecked()){
                    paymentStatus  = radioButton.getText().toString();
                    Toast.makeText(context, paymentStatus, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return ongoingViewItems.size();
    }
}