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

import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest.PendingRequestAdapter;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest.PendingRequestItem;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CustomerPendingRequestsFragment extends Fragment {


    //------- Pending Request Variables
    private RecyclerView recyclerView;
    private List<PendingRequestItem> pendingRequestItemList;
    private PendingRequestAdapter pendingRequestAdapter;

    private Query query;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_pending_request, container, false);


        //---- Connecting the layouts
        recyclerView = rootView.findViewById(R.id.pendingRequestRecyclerView);
        pendingRequestItemList = new ArrayList<>();
        pendingRequestAdapter = new PendingRequestAdapter(pendingRequestItemList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(pendingRequestAdapter);


        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        query = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUid)
                .child("pending-request")
                .orderByChild("timestamp");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingRequestItemList.clear();
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "Snapshot exist", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                        String userId = childSnapshot.getKey();
                        String name = childSnapshot.child("name").getValue(String.class);
                        String service = childSnapshot.child("service").getValue(String.class);
                        String timestamp = childSnapshot.child("timestamp").getValue(String.class);
                        String total = childSnapshot.child("totalprice").getValue(String.class);
                        String status = childSnapshot.child("status").getValue(String.class);
                        String jobType = childSnapshot.child("jobtype").getValue(String.class);

                        PendingRequestItem item = new PendingRequestItem(userId, name, timestamp, service, total,status,jobType);
                        pendingRequestItemList.add(item);
                        pendingRequestAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "Doesn't exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}
