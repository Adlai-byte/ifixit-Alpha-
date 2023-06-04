package com.example.ifixit.CUSTOMER_FILES;

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
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.CustomerListViewFragment;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.CustomerNotificationFragment;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.CustomerPendingRequestsFragment;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.CustomerProfileFragment;
import com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.CustomerTransactionHistoryFragment;
import com.example.ifixit.CUSTOMER_FILES.Messaging.ChatActivity;
import com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS.OrgJobPostingFragment;
import com.example.ifixit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class CustomerMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private StorageReference mProfileImageStorage;

    private DrawerLayout drawer;
    private TextView headerUserName;
    private TextView headerEmail;
    private View headerInfo;
    private String userID;
    private ImageView customerImage;
    private Uri resultUri;
    private boolean backPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_menu);
        Toolbar toolbar = findViewById(R.id.CMtoolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationViews = findViewById(R.id.CMnav_view);
        headerInfo = navigationViews.getHeaderView(0);
        navigationViews.setNavigationItemSelectedListener(this);

        customerImage = headerInfo.findViewById(R.id.CMheaderImageView);
        headerEmail = headerInfo.findViewById(R.id.CMemailTV);
        headerUserName = headerInfo.findViewById(R.id.CMuserNameTV);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mCustomerDatabase = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(userID);
        mProfileImageStorage = FirebaseStorage.getInstance().getReference().child("profile_images");

        getHeaderInfo();

        drawer = findViewById(R.id.CMdrawer_layout);
        NavigationView navigationView = findViewById(R.id.CMnav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new CustomerListViewFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CMnav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new CustomerProfileFragment()).commit();
                break;
            case R.id.CMnav_listview:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new CustomerListViewFragment()).commit();
                break;
            case R.id.CMnav_map:
                Intent intent = new Intent(CustomerMainMenuActivity.this, CustomerMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.CMnav_message:
                Intent intent2 = new Intent(CustomerMainMenuActivity.this, ChatActivity.class);
                startActivity(intent2);
                break;
            case R.id.CMnav_jobposting:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new OrgJobPostingFragment()).commit();
                break;
            case R.id.CMnav_requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container,new CustomerPendingRequestsFragment()).commit();
                break;
            case R.id.CMnav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new CustomerNotificationFragment()).commit();
                break;
            case R.id.CMnav_trasaction_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.CMfragment_container, new CustomerTransactionHistoryFragment()).commit();
                break;
            case R.id.CMnav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.CMnav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(CustomerMainMenuActivity.this, CustomerLoginActivity.class);
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
                        String mUserName = map.get("name").toString();
                        headerUserName.setText(mUserName);
                    }
                    if (map.get("email") != null) {
                        String mEmail = map.get("email").toString();
                        headerEmail.setText(mEmail);
                    }
                    if (map.get("profileimageurl") != null) {
                        String mProfileImageUrl = map.get("profileimageurl").toString();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            customerImage.setImageURI(resultUri);

            if (resultUri != null) {
                final StorageReference imageRef = mProfileImageStorage.child(userID + ".jpg");
                UploadTask uploadTask = imageRef.putFile(resultUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                String imageUrl = downloadUrl.toString();
                                mCustomerDatabase.child("profileimageurl").setValue(imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CustomerMainMenuActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CustomerMainMenuActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerMainMenuActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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
}
