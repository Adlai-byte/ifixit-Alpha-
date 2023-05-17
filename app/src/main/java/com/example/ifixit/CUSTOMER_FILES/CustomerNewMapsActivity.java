package com.example.ifixit.CUSTOMER_FILES;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.CUSTOMER_FILES.NewMapClasses.Adapter;
import com.example.ifixit.CUSTOMER_FILES.NewMapClasses.ItemView;
import com.example.ifixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerNewMapsActivity extends AppCompatActivity {

    //------ Recycler Variables-------
    private RecyclerView recyclerView;
    private List<ItemView> itemViewList;
    private Adapter adapter;
    private Query query;
    //--------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_maps);


        //--------------Reference------------
        recyclerView = findViewById(R.id.cardRecycler);
        itemViewList = new ArrayList<>();
        adapter = new Adapter(itemViewList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setAdapter(adapter);

        query = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .orderByChild("name");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String userid = childSnapshot.getKey();
                        String name = childSnapshot.child("name").getValue(String.class);
                        String service = childSnapshot.child("service").getValue(String.class);
                        String imgURL = childSnapshot.child("profileimageurl").getValue(String.class);
                        String ratings = childSnapshot.child("ratings").getValue(String.class);


                        ItemView item = new ItemView(userid, name, imgURL, service, ratings);
                        itemViewList.add(item);


                    }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}