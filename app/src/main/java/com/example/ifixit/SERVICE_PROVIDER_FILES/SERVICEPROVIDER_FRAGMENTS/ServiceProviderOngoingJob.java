package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses.OngoingAdapter;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses.OngoingViewItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderOngoingJob extends Fragment {

    //-------Ongoing Fragment Variables------
    private RecyclerView recyclerView;
    private List<OngoingViewItem> ongoingViewItems;
    private OngoingAdapter ongoingAdapter;
    private Button completeBtn;
    private Query query;
    private String currentUserId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.service_provider_fragment_ongoing, container, false);

        //------Recycler View------
        recyclerView = rootView.findViewById(R.id.ongoingRecyclerView);
        ongoingViewItems = new ArrayList<>();
        ongoingAdapter = new OngoingAdapter(getActivity(), ongoingViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(ongoingAdapter);
        //------Button---------
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        query = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(currentUserId)
                .child("joboffers")
                .child("ongoing")
                .orderByChild("name");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ongoingViewItems.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        String name = childSnapshot.child("name").getValue(String.class);
                        String address = childSnapshot.child("address").getValue(String.class);
                        String jobtype = childSnapshot.child("jobtype").getValue(String.class);
                        String totalPrice = childSnapshot.child("totalprice").getValue(String.class);

                        OngoingViewItem item = new OngoingViewItem(name, address, jobtype, totalPrice, userId);
                        ongoingViewItems.add(item);
                        ongoingAdapter.notifyDataSetChanged();

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
