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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerListViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ListViewItem> listViewItems, originalList;
    private ListViewAdapter listViewAdapter;
    private SearchView listSearchView;
    private Query query;
    private Spinner spService;
    private Spinner spPrice;

    private String mService = "";
    private String mPriceSort = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_fragment_list_view, container, false);

        // Initialize views
        listSearchView = rootView.findViewById(R.id.listSearchView);
        recyclerView = rootView.findViewById(R.id.listRecyclerView);
        spService = rootView.findViewById(R.id.adminspinner);
        spPrice = rootView.findViewById(R.id.adminspinner2);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewItems = new ArrayList<>();
        originalList = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getActivity(), listViewItems);
        recyclerView.setAdapter(listViewAdapter);

        // Set up search functionality
        listSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterByText(originalList, newText);

                return false;
            }
        });


        // Set up service spinner
        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.service_options, android.R.layout.simple_spinner_item);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spService.setAdapter(serviceAdapter);

        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mService = spService.getItemAtPosition(position).toString();
                filterList(mService, mPriceSort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mService = "";
            }
        });


        // Set up price spinner
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.price_options, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spPrice.setAdapter(priceAdapter);
        spPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriceSort = spPrice.getItemAtPosition(position).toString();
                filterList(mService, mPriceSort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriceSort = "";
                filterList(mService, mPriceSort);
            }
        });

// Retrieve data from Firebase
        query = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                originalList.clear();  // Clear the original list before adding items
                listViewItems.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    String name = childSnapshot.child("name").getValue(String.class);
                    String address = childSnapshot.child("address").getValue(String.class);
                    String service = childSnapshot.child("service").getValue(String.class);
                    String imgURL = childSnapshot.child("profileimageurl").getValue(String.class);
                    Float ratingFloat = childSnapshot.child("rating").getValue(Float.class);
                    Float maxPriceFloat = childSnapshot.child("maxPrice").getValue(Float.class);
                    // Check for null values before using them
                    float rating = ratingFloat != null ? ratingFloat : 0.0f;
                    float maxPrice = maxPriceFloat != null ? maxPriceFloat : 0.0f;

                    ListViewItem item = new ListViewItem(name, imgURL, service, address, rating, userId, maxPrice);
                    originalList.add(item);
                    listViewItems.add(item);
                }

                listViewAdapter.setFilteredList(originalList);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }

    private void filterList(String service, String priceSortOption) {

        List<ListViewItem> filteredList = new ArrayList<>(originalList);

        // Apply the filtering and sorting operations

        filteredList = filterByService(filteredList, service);
        filteredList = sortListByPrice(filteredList, priceSortOption);

        // Update the adapter with the filtered and sorted list
        listViewAdapter.setFilteredList(filteredList);
        listViewAdapter.notifyDataSetChanged();
    }

    private void filterByText(List<ListViewItem> items, String text) {
        if (text.isEmpty()) {
            listViewAdapter.setFilteredList(items);
        }

        List<ListViewItem> filteredList = new ArrayList<>();

        for (ListViewItem item : items) {

            if (item.getNAME() != null && item.getNAME().toLowerCase().contains(text.toLowerCase())
                || item.getADDRESS() != null && item.getADDRESS().toLowerCase().contains(text.toLowerCase())
            ) {
                filteredList.add(item);


            }
            listViewAdapter.setFilteredList(filteredList);

        }
        listViewAdapter.notifyDataSetChanged();

    }

    private List<ListViewItem> filterByService(List<ListViewItem> items, String service) {


        if (service.equalsIgnoreCase("None")) {
            return originalList;
        }

        List<ListViewItem> filteredList = new ArrayList<>();

        for (ListViewItem item : items) {
            if (item.getSERVICE().equalsIgnoreCase(service)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }


    private List<ListViewItem> sortListByPrice(List<ListViewItem> items, String priceSortOption) {
        List<ListViewItem> sortedList = new ArrayList<>(items);

        if (priceSortOption.equalsIgnoreCase("Ascending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item1.getMAXPRICE(), item2.getMAXPRICE());
                }
            });
        } else if (priceSortOption.equalsIgnoreCase("Descending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item2.getMAXPRICE(), item1.getMAXPRICE());
                }
            });
        }

        return sortedList;
    }


}