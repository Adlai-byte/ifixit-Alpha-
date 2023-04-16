package com.example.ifixit.CUSTOMER_FILES;

import static com.example.ifixit.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ifixit.R;
import com.example.ifixit.databinding.ActivityCustomerMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class CustomerMapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private ActivityCustomerMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(id.drawer_layout);
        NavigationView navigationView = findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, string.navigation_drawer_open , string.navigation_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        if(savedInstanceState==null){
//            getSupportFragmentManager().beginTransaction().replace(id.fragment_container,new MessageFragment()).commit();
//            navigationView.setCheckedItem(id.nav_message);
//        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container,new ProfileFragment()).commit();
                break;
            case id.nav_map:
                Intent intent = new Intent(CustomerMapsActivity.this,CustomerMapsActivity.class);
                startActivity(intent);
                break;
            case id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container,new MessageFragment()).commit();
                break;
            case id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container,new NotificationFragment()).commit();
                break;
            case id.nav_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}