package com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VSPAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private List<UsersItemView> usersItemViewList;
    private Context context;

    public VSPAdapter(List<UsersItemView> usersItemViewList, Context context) {

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



holder.userAcceptButton.setVisibility(View.GONE);


        holder.usersDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userUid = usersItemView.getUserid();
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
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
