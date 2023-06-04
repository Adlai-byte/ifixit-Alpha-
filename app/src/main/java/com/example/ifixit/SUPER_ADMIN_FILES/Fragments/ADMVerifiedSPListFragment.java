package com.example.ifixit.SUPER_ADMIN_FILES.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.UsersItemView;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.VSPAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ADMVerifiedSPListFragment extends Fragment {

    //------Recycler Variables----
    private RecyclerView recyclerView;
    private List<UsersItemView> usersItemViewList;
    private VSPAdapter vspAdapter;
    //------------------------


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adm_customer_list_fragment, container, false);


        //------References--------
        recyclerView = rootView.findViewById(R.id.adm_customer_list_recyclerView);
        usersItemViewList = new ArrayList<>();
        vspAdapter = new VSPAdapter(usersItemViewList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(vspAdapter);
        //-----------------------


        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified");

        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersItemViewList.clear();

                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {

                    String userid = customerSnapshot.getKey();
                    String name = customerSnapshot.child("name").getValue(String.class);
                    String email = customerSnapshot.child("email").getValue(String.class);
                    String address = customerSnapshot.child("address").getValue(String.class);
                    String phone = customerSnapshot.child("phone").getValue(String.class);
                    String userprofileimage = customerSnapshot.child("profileimageurl").getValue(String.class);

                    usersItemViewList.add(new UsersItemView(userid,name,address,userprofileimage,phone,email));
                    vspAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}
