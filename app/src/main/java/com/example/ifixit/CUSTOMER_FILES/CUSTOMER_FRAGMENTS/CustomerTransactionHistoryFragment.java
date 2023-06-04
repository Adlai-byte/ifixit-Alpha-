package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.TransactionHistory.TransactionAdapter;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.TransactionHistory.TransactionItem;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerTransactionHistoryFragment extends Fragment {
    //--------- Transaction History -------------------
    private RecyclerView recyclerView;
    private List<TransactionItem> transactionItemList;
    private TransactionAdapter transactionAdapter;
    //-------------------------------------------------

    private Query query;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.service_provider_fragment_transaction_history,container,false);


        //-----Connecting the layouts--------
        recyclerView = rootView.findViewById(R.id.transactionRecyclerView);
        transactionItemList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(getActivity(),transactionItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transactionAdapter);
        //-----------------------------------


        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        query = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(currentUserId)
                .child("transaction-history")
                .orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionItemList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot chilSnapShot : snapshot.getChildren()){
                        String userId = chilSnapShot.getKey();
                        String name = chilSnapShot.child("name").getValue(String.class);
                        String service = chilSnapShot.child("service").getValue(String.class);
                        String timestamp = chilSnapShot.child("timestamp").getValue(String.class);
                        String total = chilSnapShot.child("total").getValue(String.class);
                        String status = chilSnapShot.child("status").getValue(String.class);

                        TransactionItem item = new TransactionItem(userId,name,timestamp,total,status,service);
                        transactionItemList.add(item);
                        transactionAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootView;
    }
}
