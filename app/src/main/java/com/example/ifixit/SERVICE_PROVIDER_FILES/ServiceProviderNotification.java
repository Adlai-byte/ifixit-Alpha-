package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ServiceProviderNotification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Item> items;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_notification);

        recyclerView = findViewById(R.id.recyclerview);
        items = new ArrayList<>();
        adapter = new MyAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String serviceProviderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference serviceProviderRef = FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("SERVICE-PROVIDERS")
                .child(serviceProviderID)
                .child("JOB-OFFERS")
                .child("PENDING");

        serviceProviderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot customerSnapShot : snapshot.getChildren()) {
                    String customerID = customerSnapShot.getKey();
                    DatabaseReference customerRef = serviceProviderRef.child(customerID);

                    customerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userId = snapshot.getKey();
                                String name = snapshot.child("NAME").getValue(String.class);
                                String email = snapshot.child("EMAIL").getValue(String.class);
                                String address = snapshot.child("ADDRESS").getValue(String.class);

                                items.add(new Item(name, email, address,userId));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ServiceProviderNotif", "Error: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ServiceProviderNotif", "Error: " + error.getMessage());
            }
        });
    }
}

//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//        holder.nameView.setText(items.get(position).getName());
//        holder.template.setText(items.get(position).getTemplate());
////            holder.imageView.setImageResource(items.get(position).getImage());
//
//
//    }