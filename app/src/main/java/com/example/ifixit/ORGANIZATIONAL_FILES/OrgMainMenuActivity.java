package com.example.ifixit.ORGANIZATIONAL_FILES;

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
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.OrgJobPostingFragment;
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.OrgListViewFragment;
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.OrgNotificationFragment;
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.OrgProfileFragment;
import com.example.ifixit.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class OrgMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //Variables
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DrawerLayout drawer;
    private TextView headerUserName;
    private TextView headerEmail;
    private View headerInfo;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mProfileImageUrl;
    private ImageView customerImage;
    private boolean backPressedOnce = false;
    //Profile Fragment
    //Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.org_main_menu);
        Toolbar toolbar = findViewById(R.id.ORGtoolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationViews = findViewById(R.id.ORGnav_view);
        headerInfo = navigationViews.getHeaderView(0);
        navigationViews.setNavigationItemSelectedListener(this);
        customerImage = headerInfo.findViewById(R.id.CMheaderImageView);
        headerEmail = headerInfo.findViewById(R.id.CMemailTV);
        headerUserName = headerInfo.findViewById(R.id.CMuserNameTV);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        mCustomerDatabase = FirebaseDatabase.getInstance().getReference()
                .child("organizational")
                .child(userID);
        getHeaderInfo();


        drawer = findViewById(R.id.ORGdrawer_layout);
        NavigationView navigationView = findViewById(R.id.ORGnav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ORGfragment_container, new OrgListViewFragment()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.CMnav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.ORGfragment_container, new OrgProfileFragment()).commit();
                break;
            case R.id.CMnav_listview:
                getSupportFragmentManager().beginTransaction().replace(R.id.ORGfragment_container, new OrgListViewFragment()).commit();
                break;
            case R.id.CMnav_map:
                Intent intent = new Intent(OrgMainMenuActivity.this, OrgMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.CMnav_message:
               Intent intent2 = new Intent(OrgMainMenuActivity.this, ChatActivity.class);
                break;
            case R.id.CMnav_jobposting:
                getSupportFragmentManager().beginTransaction().replace(R.id.ORGfragment_container,new OrgJobPostingFragment()).commit();

                break;
            case R.id.CMnav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.ORGfragment_container, new OrgNotificationFragment()).commit();
                break;
            case R.id.CMnav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.CMnav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(OrgMainMenuActivity.this, OrgLoginActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getHeaderInfo() {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
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
                        Glide.with(getApplication()).load(mProfileImageUrl).into(customerImage);

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
            customerImage.setImageURI(resultUri);
        }
    }


}