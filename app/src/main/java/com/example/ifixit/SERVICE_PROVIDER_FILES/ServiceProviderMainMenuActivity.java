package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.ServiceProviderDashboardFragment;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.ServiceProviderMessageFragment;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.ServiceProviderProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ServiceProviderMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private DrawerLayout drawer;
    private TextView headerUserName;
    private TextView headerEmail;
    private View headerInfo;
    private View notificationFragment;
    private FirebaseAuth mAuth;
    private DatabaseReference mServiceProviderDatabase;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mProfileImageUrl;
    private ImageView serviceProviderImage;
    private Uri resultUri;

    //Notification

    ArrayList<String> requestUserID = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_provider_main_menu);
        Toolbar toolbar = findViewById(R.id.SPtoolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationViews = findViewById(R.id.SPnav_view);
        headerInfo = navigationViews.getHeaderView(0);
        navigationViews.setNavigationItemSelectedListener(this);
        serviceProviderImage = headerInfo.findViewById(R.id.SPheaderImageView);
        headerEmail = headerInfo.findViewById(R.id.SPemailTV);
        headerUserName = headerInfo.findViewById(R.id.SPuserNameTV);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mServiceProviderDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(userID);
        getHeaderInfo();

        drawer = findViewById(R.id.SPdrawer_layout);
        NavigationView navigationView = findViewById(R.id.SPnav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.SPnav_dashboard);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.SPnav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderProfileFragment()).commit();
                break;
            case R.id.SPnav_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderDashboardFragment()).commit();
                break;
            case R.id.SPnav_map:
                Intent intent = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.SPnav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderMessageFragment()).commit();
                break;
            case R.id.SPnav_notification:
                Intent intent2 = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderNotification.class);
                startActivity(intent2);
                break;
            case R.id.SPnav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SPnav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderLoginActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getHeaderInfo() {
        mServiceProviderDatabase.addValueEventListener(new ValueEventListener() {
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
                        Glide.with(getApplication()).load(mProfileImageUrl).into(serviceProviderImage);

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            serviceProviderImage.setImageURI(resultUri);
        }
    }

    //Notification
    private void setUpNotification() {

        String serviceProviderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mServiceProviderDatabase = FirebaseDatabase.getInstance().getReference().child("USERS")
                .child("SERVICE-PROVIDERS")
                .child(serviceProviderID)
                .child("JOB-OFFERS")
                .child("PENDING");

        mServiceProviderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String requestID = snapshot.getKey();
                    requestUserID.add(requestID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}