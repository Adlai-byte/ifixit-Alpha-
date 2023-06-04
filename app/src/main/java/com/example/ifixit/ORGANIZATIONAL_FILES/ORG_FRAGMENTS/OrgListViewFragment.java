package com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS;

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

import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.ListViewClasses.ListViewAdapter;
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.ListViewClasses.ListViewItem;
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

public class OrgListViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ListViewItem> listViewItems, originalList;
    private ListViewAdapter listViewAdapter;
    private SearchView listSearchView;
    private Query query;
    private Spinner spService;
    private Spinner spPrice;
    private Spinner spRating;


    private String mService = "";
    private String mPriceSort = "";
    private String mRatingSort = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_fragment_list_view, container, false);

        // Initialize views
        listSearchView = rootView.findViewById(R.id.listSearchView);
        recyclerView = rootView.findViewById(R.id.listRecyclerView);
        spService = rootView.findViewById(R.id.adminspinner);
        spPrice = rootView.findViewById(R.id.adminspinner2);
        spRating = rootView.findViewById(R.id.adminspinner3);


        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewItems = new ArrayList<>();
        originalList = new ArrayList<>();


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

        //Set up Rating spinner
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.rating_options, android.R.layout.simple_spinner_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRating.setAdapter(ratingAdapter);

        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRatingSort = spRating.getItemAtPosition(i).toString();

                sortListByRating(listViewItems, mRatingSort);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                listViewAdapter.setFilteredList(originalList);
                listViewAdapter.notifyDataSetChanged();
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

                filterList(mService);
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

                sortListByPrice(listViewItems, mPriceSort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listViewAdapter.setFilteredList(originalList);
                listViewAdapter.notifyDataSetChanged();
            }
        });


// Retrieve data from Firebase


        return rootView;
    }


    private void filterList(String service) {
        if (service.isEmpty() || service.equals("Service Provider")) {
            listViewAdapter.setFilteredList(originalList);
            listViewAdapter.notifyDataSetChanged();
            return;
        }

        List<ListViewItem> filteredList = new ArrayList<>();

        for (ListViewItem item : originalList) {
            if (item.getSERVICE() != null && item.getSERVICE().equalsIgnoreCase(service)) {
                filteredList.add(item);
            }
        }

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

        List<ListViewItem> filteredList = new ArrayList<>();

        for (ListViewItem item : items) {
            if (item.getSERVICE().equalsIgnoreCase(service)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }


    private void sortListByPrice(List<ListViewItem> items, String priceSortOption) {
        List<ListViewItem> sortedList = new ArrayList<>(items);

        if (priceSortOption.equals("Price-Ascending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item1.getMAXPRICE(), item2.getMAXPRICE());
                }
            });
        } else if (priceSortOption.equals("Price-Descending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item2.getMAXPRICE(), item1.getMAXPRICE());
                }
            });

        }
        listViewAdapter.setFilteredList(sortedList);

        listViewAdapter.notifyDataSetChanged();

    }

    private void sortListByRating(List<ListViewItem> items, String ratingSortOption) {
        List<ListViewItem> sortedList = new ArrayList<>(items);

        if (ratingSortOption.equals("Rating-Ascending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item1.getRATING(), item2.getRATING());
                }
            });
        } else if (ratingSortOption.equals("Rating-Descending")) {
            Collections.sort(sortedList, new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem item1, ListViewItem item2) {
                    return Float.compare(item2.getRATING(), item1.getRATING());
                }
            });

        }

        listViewAdapter.setFilteredList(sortedList);

        listViewAdapter.notifyDataSetChanged();
    }


    //Modified Part
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Adapter-------------------------------------------------------------
        listViewAdapter = new ListViewAdapter(getActivity(), listViewItems);
        recyclerView.setAdapter(listViewAdapter);
        //--------------------------------------------------------------------

        //Firebase
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
        //.................................................................................................................


    }
}

