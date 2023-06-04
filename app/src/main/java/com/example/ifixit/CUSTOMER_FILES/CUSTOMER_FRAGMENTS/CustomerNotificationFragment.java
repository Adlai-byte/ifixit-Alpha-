package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.NotificationViewClasses.NotificationViewAdapter;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.NotificationViewClasses.NotificationViewItem;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerNotificationFragment extends Fragment {

    //---------Recycler View Adapters--------
    private RecyclerView recyclerView;
    private List<NotificationViewItem> notificationViewItemList;
    private NotificationViewAdapter notificationViewAdapter;
    //--------------------------------------

    DatabaseReference customerNotificationRef;
    DatabaseReference transactionHistoryRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment_list_view, container, false);

        //-----Recycler View and Friends------
        recyclerView = rootView.findViewById(R.id.notificationRecyclerView);
        notificationViewItemList = new ArrayList<>();
        notificationViewAdapter = new NotificationViewAdapter(getActivity(), notificationViewItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notificationViewAdapter);
        //------------------------------------

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Notification Reference
        customerNotificationRef = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId)
                .child("notification");




        customerNotificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapShot : snapshot.getChildren()){

                            String serviceProviderId = childSnapShot.child("userid").getValue(String.class);
                            String key = childSnapShot.getKey();
                            Toast.makeText(getContext(), serviceProviderId, Toast.LENGTH_SHORT).show();
                            DatabaseReference serviceProviderRef = FirebaseDatabase.getInstance().getReference()
                                    .child("service-providers")
                                    .child("verified")
                                    .child(serviceProviderId);

                            serviceProviderRef.addValueEventListener(new ValueEventListener() {
                                @Override

                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    notificationViewItemList.clear();
                                    if (snapshot.exists()) {
                                        String name = snapshot.child("name").getValue(String.class);
                                        String service = snapshot.child("service").getValue(String.class);
                                        String imgurl = snapshot.child("profileimageurl").getValue(String.class);


                                        NotificationViewItem item = new NotificationViewItem(serviceProviderId, name, imgurl, service,key);
                                        notificationViewItemList.add(item);
                                        notificationViewAdapter.notifyDataSetChanged();





                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }




                    }else {

                    }


                }






            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        return rootView;
    }
}
