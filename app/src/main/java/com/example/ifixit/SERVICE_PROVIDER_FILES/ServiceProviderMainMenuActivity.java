package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.app.Activity;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.ifixit.Messaging.ChatActivity;
import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.ServiceProviderJobPostingFragment;
import com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.ServiceProviderOngoingJob;
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
    private boolean backPressedOnce = false;
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

        mServiceProviderDatabase = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(userID);
        getHeaderInfo();

        drawer = findViewById(R.id.SPdrawer_layout);
        NavigationView navigationView = findViewById(R.id.SPnav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderOngoingJob()).commit();
            navigationView.setCheckedItem(R.id.SPnav_ongoing);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.SPnav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderProfileFragment()).commit();
                break;
            case R.id.SPnav_ongoing:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderOngoingJob()).commit();
                break;
            case R.id.SPnav_map:
                Intent intent = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.SPnav_message:
                Intent intent1 = new Intent(ServiceProviderMainMenuActivity.this, ChatActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.SPnav_jobposting:
                getSupportFragmentManager().beginTransaction().replace(R.id.SPfragment_container, new ServiceProviderJobPostingFragment()).commit();
                break;

//            case R.id.SPnav_notification:
//                Intent intent2 = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderNotification.class);
//                startActivity(intent2);
//                break;
            case R.id.SPnav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SPnav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(ServiceProviderMainMenuActivity.this, ServiceProviderLoginActivity.class);
                startActivity(intent2);
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
                    if (map.get("name") != null) {
                        mUserName = map.get("name").toString();
                        headerUserName.setText(mUserName);
                    }
                    if (map.get("email") != null) {
                        mEmail = map.get("email").toString();
                        headerEmail.setText(mEmail);
                    }

                    if(map.get("profileimageurl")!=null){
                        mProfileImageUrl = map.get("profileimageurl").toString();
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
            if (backPressedOnce) {
                // If back button is pressed again, exit the application
                super.onBackPressed();
            } else {
                backPressedOnce = true;
                // Show a dialog box to confirm exit
                new AlertDialog.Builder(this)
                        .setTitle("Exit Application")
                        .setMessage("Do you want to exit the application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Exit the application
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backPressedOnce = false;
                            }
                        })
                        .show();
            }
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




}

//RecyclerView
// Once you determine your layout, you need to implement your CMAdapter and UsersViewHolder.
// These two classes work together to define how your data is displayed. The UsersViewHolder is a wrapper around a View that contains the
// layout for an individual item in the list. The CMAdapter creates UsersViewHolder objects as needed and also sets the data for those views.
// The process of associating views to their data is called binding.

//When you define your adapter, you override three key methods:
//
//onCreateViewHolder(): RecyclerView calls this method whenever it needs to create a new UsersViewHolder.
// The method creates and initializes the UsersViewHolder and its associated View, but does not fill in the view's contents—the UsersViewHolder
// has not yet been bound to specific data.
//
//onBindViewHolder(): RecyclerView calls this method to associate a UsersViewHolder with data. The method fetches the appropriate data
// and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names, the method might
// find the appropriate name in the list and fill in the view holder's TextView widget.
//
//getItemCount(): RecyclerView calls this method to get the size of the dataset. For example, in an address book app,
// this might be the total number of addresses. RecyclerView uses this to determine when there are no more items that can be displayed.
//
//Here's a typical example of a simple adapter with a nested UsersViewHolder that displays a list of data. In this case,
// the RecyclerView displays a simple list of text elements. The adapter is passed an array of strings containing the text for the UsersViewHolder elements.