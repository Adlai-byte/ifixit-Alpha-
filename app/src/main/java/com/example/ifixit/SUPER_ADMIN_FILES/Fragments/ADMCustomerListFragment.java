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
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.CMAdapter;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.UsersItemView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ADMCustomerListFragment extends Fragment {

    //------Recycler Variables----
    private RecyclerView recyclerView;
    private List<UsersItemView> usersItemViewList;
    private CMAdapter cmAdapter;
    //------------------------


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adm_customer_list_fragment, container, false);


        //------References--------
        recyclerView = rootView.findViewById(R.id.adm_customer_list_recyclerView);
        usersItemViewList = new ArrayList<>();
        cmAdapter = new CMAdapter(usersItemViewList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cmAdapter);
        //-----------------------


        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("CUSTOMERS");

        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersItemViewList.clear();

                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {

                    String userid = customerSnapshot.getKey();
                    String name = customerSnapshot.child("NAME").getValue(String.class);
                    String email = customerSnapshot.child("EMAIL").getValue(String.class);
                    String address = customerSnapshot.child("ADDRESS").getValue(String.class);
                    String phone = customerSnapshot.child("PHONE").getValue(String.class);
                    String userprofileimage = customerSnapshot.child("profileImageUrl").getValue(String.class);

                    usersItemViewList.add(new UsersItemView(userid,name,address,userprofileimage,phone,email));
                    cmAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}
