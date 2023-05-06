package com.example.ifixit.CUSTOMER_FILES;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.Item;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class Customer_FILTERED_SERVICES extends AppCompatActivity {


    //Recycler View Variables
    private RecyclerView recyclerView;
    private List<Item> items;
    private MyAdapter adapter;
    //Recycler View


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_filtered_services);

        recyclerView = findViewById(R.id.recyclerview);

        items = new ArrayList<>();
        adapter = new MyAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //Recycler View

    }
}