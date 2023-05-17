package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

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
    private Query query;
    private Spinner spService;
    private String mService = "";

    private String Ascending="Ascending";
    private String Descending ="Descending";

    private Spinner spPrice;
    //------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customer_fragment_list_view, container, false);

        //-------Recycler View-----------
        listSearchView = rootView.findViewById(R.id.listSearchView);
        recyclerView = rootView.findViewById(R.id.listRecyclerView);
        listSearchView.clearFocus();

        listViewItems = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getActivity(), listViewItems);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listViewAdapter);


        spService = rootView.findViewById(R.id.adminspinner);
        spPrice = rootView.findViewById(R.id.adminspinner2);

        ArrayAdapter<CharSequence>adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.price_options, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.service_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spPrice.setAdapter(adapter1);
        spPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Ascending.equals(spPrice.getItemAtPosition(i).toString())){
                    listViewAdapter.sortItemsByPriceAscending();
                }else {
                    listViewAdapter.sortItemsByPriceDescending();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spService.setAdapter(adapter);
        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // handle item selection
                mService = spService.getItemAtPosition(position).toString();
                filterList(mService);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mService = "";
              filterList(mService);

            }
        });


        //----------------Search ------------------------
        listSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        query = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listViewItems.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    String name = childSnapshot.child("name").getValue(String.class);
                    String address = childSnapshot.child("address").getValue(String.class);
                    String service = childSnapshot.child("service").getValue(String.class);
                    String imgURL = childSnapshot.child("profileimageurl").getValue(String.class);
                    float rating = childSnapshot.child("rating").getValue(Float.class);
                    float maxPrice = childSnapshot.child("maxPrice").getValue(Float.class);

                    ListViewItem item = new ListViewItem(name, imgURL, service, address, rating, userId,maxPrice);

                    listViewItems.add(item);

                }

                listViewAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }


    private void filterList(String text) {
        List<ListViewItem> filteredList = new ArrayList<>();
        for (ListViewItem item : listViewItems) {
            if (item.getNAME() != null &&
                    item.getNAME().toLowerCase().contains(text.toLowerCase())
            ) {
                filteredList.add(item);
            }
            if (item.getADDRESS() != null && item.getADDRESS().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            if (item.getSERVICE() != null && item.getSERVICE().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        listViewAdapter.setFilteredList(filteredList);
    }


}



