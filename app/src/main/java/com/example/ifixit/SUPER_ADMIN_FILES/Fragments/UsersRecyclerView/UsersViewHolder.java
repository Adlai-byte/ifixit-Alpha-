package com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    public TextView usersName;
    public TextView usersAddress;
    public TextView usersEmail;
    public ImageView usersProfile;
    public TextView usersPhone;
    public Button userEditButton;
    public Button usersDeleteButton;


    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        usersName = itemView.findViewById(R.id.usersName);
        usersAddress = itemView.findViewById(R.id.usersAddress);
        usersEmail =itemView.findViewById(R.id.usersEmail);
        usersPhone = itemView.findViewById(R.id.usersPhone);
        userEditButton = itemView.findViewById(R.id.usersEditButton);
        usersDeleteButton = itemView.findViewById(R.id.usersDeleteButton);
        usersProfile = itemView.findViewById(R.id.usersProfileImage);



    }
}
