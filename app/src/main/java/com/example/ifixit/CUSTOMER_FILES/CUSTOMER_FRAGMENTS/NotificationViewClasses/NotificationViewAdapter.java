package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.NotificationViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class NotificationViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private List<NotificationViewItem> notificationViewItems;
    private Context context;


    public NotificationViewAdapter(Context context, List<NotificationViewItem> notificationViewItems) {
        this.context = context;
        this.notificationViewItems = notificationViewItems;

    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_notification_list_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        NotificationViewItem notificationViewItem = notificationViewItems.get(position);

        holder.nameView.setText(notificationViewItem.getName());
        holder.serviceView.setText(notificationViewItem.getService());

        Glide.with(context).load(notificationViewItem.getProfileimageurl()).into(holder.imageView);


        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> reviews = new HashMap<>();

                String serviceProviderUserId = notificationViewItem.getUserid();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(serviceProviderUserId)
                        .child("reviews")
                        .child(currentUserId);

                DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(currentUserId);

                currentUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            String name = snapshot.child("name").getValue(String.class);
                            String imgUrl = snapshot.child("profileimageurl").getValue(String.class);

                            Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                            reviews.put("name", name);
                            reviews.put("profileimageurl", imgUrl);

                        reviewsRef.setValue(reviews);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                String review = holder.reviewComment.getText().toString();
                float rate = holder.ratingBar.getRating();




                reviews.put("comment", review);
                reviews.put("rating", String.valueOf(rate));
                reviewsRef.setValue(reviews);

                Toast.makeText(context, "Review Sent", Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationViewItems.size();
    }


    //----------------------------------------------------------------------------------

}
