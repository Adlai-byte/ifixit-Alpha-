package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses.ListViewAdapter;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses.ListViewItem;
import com.example.ifixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerListViewFragment extends Fragment {


    //---------List View Variables--------
    private RecyclerView recyclerView;
    private List<ListViewItem> listViewItems;
    private ListViewAdapter listViewAdapter;
    private SearchView listSearchView;


    //------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customer_fragment_list_view, container, false);

        //-------Recycler View-----------
        listSearchView = rootView.findViewById(R.id.listSearchView);
        recyclerView = rootView.findViewById(R.id.listRecyclerView);
        listViewItems = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getActivity(), listViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listViewAdapter);


        listSearchView.clearFocus();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("SERVICE-PROVIDERS");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    String userId = childSnapshot.getKey();
                    String name = childSnapshot.child("NAME").getValue(String.class);
                    String address = childSnapshot.child("ADDRESS").getValue(String.class);
                    String service = childSnapshot.child("SERVICE").getValue(String.class);
                    String imgURL = childSnapshot.child("profileImageUrl").getValue(String.class);
                    float rating = 5.0f;


                    listViewItems.add(new ListViewItem(name, imgURL, service, address, rating));
                    listViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;


    }



}
