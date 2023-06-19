package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {


    Context context;
    List<RequestItem> requestItems;

    public RequestAdapter(Context context, List<RequestItem> requestItems) {
        this.context = context;
        this.requestItems = requestItems;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(context).inflate(R.layout.service_provider_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestItem requestItem = requestItems.get(holder.getAdapterPosition());

        //Variables for creating a chat room
        String customerUid = requestItem.getUSERID();
        String currentUserUid = FirebaseAuth.getInstance().getUid();

        holder.totalPrice.setText(requestItem.getTOTALPRICE());
        holder.location.setText(requestItem.getLOCATION());;
        holder.jobType.setText(requestItem.getJOBTYPE());
        holder.dateView.setText(requestItem.getDate());

        Glide.with(context).load(requestItem.getProfileImageUrl())
                .into(holder.imageView);


        holder.nameView.setText(requestItem.getNAME());

        holder.BtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = requestItem.getKey();
                DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("ongoing")
                        .child(key);
                ongoingRef.setValue(requestItem);

                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("pending")
                        .child(key);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                requestItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, requestItems.size());



                //Create a chat-room Key = (service-providerUid + Customer-Uid)
                DatabaseReference newChatRoom = FirebaseDatabase.getInstance().getReference()
                        .child("chat-rooms")
                        .child(currentUserUid+customerUid);
                newChatRoom.setValue(true);




                /*
                Add the chat room key to service-provider chat-thread-list
                *Necessary Details
                For the Service Provider  Side
                - Customer name
                - profile image url
                - service ("Customer")
                */

                HashMap <String,Object> chatThreadValue = new HashMap<>();

                DatabaseReference serviceProviderChatThread = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserUid)
                        .child("chat-thread-list")
                        .child(currentUserUid+customerUid);

                //Customer Reference
                DatabaseReference customerReference = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUid);

                customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue(String.class);
                        String imgurl = snapshot.child("profileimageurl").getValue(String.class);
                        String service ="customer";

                        chatThreadValue.put("name",name);
                        chatThreadValue.put("profileimageurl",imgurl);
                        chatThreadValue.put("service",service);

                        serviceProviderChatThread.setValue(chatThreadValue);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //-------------------------------------------------------


                /*
                Add the chat room key to customer chat-thread-list
                *Necessary Details
                For the Customer Side
                - Service provider name
                - profile image url
                - service

                */

                HashMap <String,Object> customerChatThreadValue = new HashMap<>();

                DatabaseReference customerChatThread = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(customerUid)
                        .child("chat-thread-list")
                        .child(currentUserUid+customerUid);

                //Customer Reference
                DatabaseReference serviceProviderReference = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(currentUserUid);

                serviceProviderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue(String.class);
                        String imgurl = snapshot.child("profileimageurl").getValue(String.class);
                        String service =snapshot.child("service").getValue(String.class);

                        customerChatThreadValue.put("name",name);
                        customerChatThreadValue.put("profileimageurl",imgurl);
                        customerChatThreadValue.put("service",service);

                        customerChatThread.setValue(customerChatThreadValue);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //-------------------------------------------------------

            }
        });
        holder.Btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = requestItem.getKey();
                DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("joboffers")
                        .child("pending")
                        .child(key);
                pendingRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                requestItems.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, requestItems.size());
            }
        });
    }



    @Override
    public int getItemCount() {
        return requestItems.size();
    }


}