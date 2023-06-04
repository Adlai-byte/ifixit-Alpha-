package com.example.ifixit.SUPER_ADMIN_FILES;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.ADMAdminProfileFragment;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.ADMCustomerListFragment;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.ADMUnverifiedSPListFragment;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.ADMVerifiedSPListFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AdminMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private DrawerLayout drawer;
    private TextView headerUserName;
    private TextView headerEmail;
    private View headerInfo;
    private View notificationFragment;
    private FirebaseAuth mAuth;
    private DatabaseReference mAdminRef;
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

        setContentView(R.layout.admin_main_menu);
        Toolbar toolbar = findViewById(R.id.ADMtoolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationViews = findViewById(R.id.ADMnav_view);


        headerInfo = navigationViews.getHeaderView(0);
        navigationViews.setNavigationItemSelectedListener(this);

        serviceProviderImage = headerInfo.findViewById(R.id.ADMheaderImageView);
        headerEmail = headerInfo.findViewById(R.id.ADMemailTV);
        headerUserName = headerInfo.findViewById(R.id.ADMuserNameTV);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();




        mAdminRef = FirebaseDatabase.getInstance().getReference()
                .child("admins")
                .child(userID);

        getHeaderInfo();



        drawer = findViewById(R.id.ADMdrawer_layout);
        NavigationView navigationView = findViewById(R.id.ADMnav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMUnverifiedSPListFragment()).commit();
            navigationView.setCheckedItem(R.id.SPnav_ongoing);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //Ciao
            case R.id.adminProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMAdminProfileFragment()).commit();
                break;

            case R.id.adminCMtList:
                getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMCustomerListFragment()).commit();
                break;

            case R.id.adminVerifiedSPList:
                getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMVerifiedSPListFragment()).commit();
                break;

            case R.id.adminUnverifiedSPList:
                getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMUnverifiedSPListFragment()).commit();
                break;
//            case R.id.adminMessaging:
//
//                Intent intent = new Intent(AdminMainMenuActivity.this, ChatActivity.class);
//                startActivity(intent);
//                finish();
////                getSupportFragmentManager().beginTransaction().replace(R.id.ADMfragment_container, new ADMMessagingFragment()).commit();
//                break;

            case R.id.SPnav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(AdminMainMenuActivity.this, AdminLoginActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getHeaderInfo() {
        mAdminRef.addValueEventListener(new ValueEventListener() {
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