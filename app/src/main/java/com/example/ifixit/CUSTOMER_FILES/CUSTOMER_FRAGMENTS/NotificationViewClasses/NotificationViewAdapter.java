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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        String review = holder.reviewComment.getText().toString();
        float rate = holder.ratingBar.getRating();

        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serviceProviderUserId = notificationViewItem.getUserid();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(currentUserId)
                        .child("notification")
                        .child(serviceProviderUserId);

                DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(serviceProviderUserId)
                        .child("reviews")
                        .child(currentUserId);

                DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(serviceProviderUserId);

                ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            float currentRating = snapshot.child("rating").getValue(Float.class);
                            float newRating = (currentRating + rate) / 2;

                            // Update the rating value
                            ratingRef.child("rating").setValue(newRating)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // The update operation was successful
                                            Toast.makeText(context, "Rating updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error that occurred during the update operation
                                            Toast.makeText(context, "Failed to update rating", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Failed to get current rating", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });

                DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers")
                        .child(currentUserId);

                currentUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue(String.class);
                            String imgUrl = snapshot.child("profileimageurl").getValue(String.class);

                            HashMap<String, Object> reviews = new HashMap<>();
                            reviews.put("comment", review);
                            reviews.put("name", name);
                            reviews.put("profileimageurl", imgUrl);

                            // Set the review data for the service provider
                            reviewsRef.setValue(reviews)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // The review was successfully sent
                                            Toast.makeText(context, "Review Sent", Toast.LENGTH_SHORT).show();

                                            // Remove the notification from the database
                                            notificationRef.removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // The notification was successfully removed
                                                            int itemPosition = holder.getAdapterPosition();
                                                            if (itemPosition != RecyclerView.NO_POSITION) {
                                                                notificationViewItems.remove(itemPosition);
                                                                notifyItemRemoved(itemPosition);
                                                                notifyItemRangeChanged(itemPosition, notificationViewItems.size());
                                                            } else {
                                                                // Handle the case when the item position is invalid
                                                                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error that occurred while sending the review
                                            Toast.makeText(context, "Failed to send review", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });
            }
        });





















                                                        }

    @Override
    public int getItemCount() {
        return notificationViewItems.size();
    }
}

