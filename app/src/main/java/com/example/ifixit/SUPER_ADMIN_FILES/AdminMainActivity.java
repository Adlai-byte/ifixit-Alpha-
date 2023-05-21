package com.example.ifixit.SUPER_ADMIN_FILES;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SUPER_ADMIN_FILES.RecyclerView.AdminAdapter;
import com.example.ifixit.SUPER_ADMIN_FILES.RecyclerView.AdminItemView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    //----Recycler View Variables-----
    private RecyclerView recyclerView;
    private List<AdminItemView> adminItemViewList;
    private AdminAdapter adminAdapter;
    //--------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        //------Recycler View ------
        recyclerView = (RecyclerView)findViewById(R.id.adminRecyclerView);
        adminItemViewList = new ArrayList<>();
        adminAdapter = new AdminAdapter(adminItemViewList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminAdapter);
        //--------------------------

        DatabaseReference unverifiedServiceProviders= FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("SERVICE-PROVIDERS")
                .child("UNVERIFIED");

        unverifiedServiceProviders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Clear the ArrayList

                for(DataSnapshot serviceProviderSnapshot : snapshot.getChildren()){
                    if(snapshot.exists()){
                        String serviceProviderUid= serviceProviderSnapshot.getKey();
                        String name  = serviceProviderSnapshot.child("NAME").getValue(String.class);
                        String address = serviceProviderSnapshot.child("ADDRESS").getValue(String.class);
                        String email = serviceProviderSnapshot.child("EMAIL").getValue(String.class);
                        String service = serviceProviderSnapshot.child("SERVICE").getValue(String.class);
                        String rate = serviceProviderSnapshot.child("RATE").getValue(String.class);
                        String profileImageUrl = serviceProviderSnapshot.child("profileImageUrl").getValue(String.class);

                        adminItemViewList.add(new AdminItemView(serviceProviderUid,name,address,email,service,profileImageUrl,rate));

                    }
                    adminAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }
}