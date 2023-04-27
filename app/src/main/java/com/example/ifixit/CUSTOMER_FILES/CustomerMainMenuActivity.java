package com.example.ifixit.CUSTOMER_FILES;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.ifixit.GLOBAL_ACTIVITIES.DashboardFragment;
import com.example.ifixit.GLOBAL_ACTIVITIES.MessageFragment;
import com.example.ifixit.GLOBAL_ACTIVITIES.NotificationFragment;
import com.example.ifixit.GLOBAL_ACTIVITIES.ProfileFragment;
import com.example.ifixit.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CustomerMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //Variables
    private DrawerLayout drawer;

    private TextView headerUserName;
    private TextView headerEmail;

    private View headerInfo;





    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mProfileImageUrl;
    private ImageView customerImage;
    //Profile Fragment
    //Variables






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Profile Fragment

//
//        customerName = (EditText) rootView.findViewById(R.id.customersName);
//        customerEmail = (EditText) rootView.findViewById(R.id.customersEmail);
//        customerAddress = (EditText) rootView.findViewById(R.id.customerAddress);


        NavigationView navigationViews = findViewById(R.id.nav_view);
        headerInfo = navigationViews.getHeaderView(0);

        navigationViews.setNavigationItemSelectedListener(this);

        customerImage = headerInfo.findViewById(R.id.headerImageView);
        headerEmail =  headerInfo.findViewById(R.id.emailTV);
        headerUserName = headerInfo.findViewById(R.id.userNameTV);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS").child(userID);
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getHeaderInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

                break;
            case R.id.nav_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                break;
            case R.id.nav_map:
                Intent intent = new Intent(CustomerMainMenuActivity.this, CustomerMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(CustomerMainMenuActivity.this, CustomerLoginActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    public void getHeaderInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("NAME") != null) {
                        mUserName = map.get("NAME").toString();
                        headerUserName.setText(mUserName);
                    }
                    if (map.get("EMAIL") != null) {
                        mEmail = map.get("EMAIL").toString();
                        headerEmail.setText(mEmail);
                    }

                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(mProfileImageUrl).into(customerImage);

                    }



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}