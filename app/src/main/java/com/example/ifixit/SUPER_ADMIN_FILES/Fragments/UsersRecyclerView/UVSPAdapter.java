package com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UVSPAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private List<UsersItemView> usersItemViewList;
    private Context context;

    public UVSPAdapter(List<UsersItemView> usersItemViewList, Context context) {

        this.usersItemViewList = usersItemViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_users_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UsersItemView usersItemView = usersItemViewList.get(position);



        holder.usersName.setText(usersItemView.getName());
        holder.usersAddress.setText(usersItemView.getAddress());
        holder.usersEmail.setText(usersItemView.getEmail());
        holder.usersPhone.setText(usersItemView.getPhone());

        Glide.with(context).load(usersItemView.getProfileimageurl()).into(holder.usersProfile);

        Map<String, Object> serviceProvideItem = new HashMap<>();

        //------Button Shit--------
        holder.userAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userUid = usersItemView.getUserid();

                DatabaseReference serviceProviderRefVerified = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(userUid);

                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("unverified")
                        .child(userUid);

                customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String userid = snapshot.getKey();
                            String name = snapshot.child("name").getValue(String.class);
                            String service = snapshot.child("service").getValue(String.class);
                            Float maxprice =snapshot.child("maxPrice").getValue(Float.class);
                            Float minprice = snapshot.child("minPrice").getValue(Float.class);
                            String rating = snapshot.child("rating").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String profileimageurl = snapshot.child("profileimageurl").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);


                            serviceProvideItem.put("address",address);
                            serviceProvideItem.put("name",name);
                            serviceProvideItem.put("service",service);
                            serviceProvideItem.put("maxPrice",maxprice);
                            serviceProvideItem.put("minPrice",minprice);
                            serviceProvideItem.put("rating",rating);
                            serviceProvideItem.put("email",email);
                            serviceProvideItem.put("profileimageurl",profileimageurl);

                            serviceProviderRefVerified.updateChildren(serviceProvideItem);
                            Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Does not exist", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                customerRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                usersItemViewList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, usersItemViewList.size());




            }
        });


        holder.usersDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userUid = usersItemView.getUserid();
                Toast.makeText(context, userUid, Toast.LENGTH_SHORT).show();
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("unverified")
                        .child(userUid);

                customerRef.removeValue();

                int itemPosition = holder.getAdapterPosition();
                usersItemViewList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, usersItemViewList.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersItemViewList.size();
    }
}
