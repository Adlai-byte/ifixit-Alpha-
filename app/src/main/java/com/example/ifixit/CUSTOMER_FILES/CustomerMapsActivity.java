package com.example.ifixit.CUSTOMER_FILES;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ifixit.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap Mmap;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest mLocationRequest;
    LatLng latLng;
    private HashMap<String, Marker> mMarkers;

    private Button hireButton;
    private TextView serviceProviderName;
    private TextView serviceProviderEmail;
    private TextView serviceProviderAddress;
    private LinearLayout serviceProviderLayout;

    Marker markerzy;
    DatabaseReference mDatabaseRef;
    String serviceProviderUserId;

    //Nofication



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.CMmap);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        hireButton = (Button) findViewById(R.id.serviceProviderRequest);
        serviceProviderName = (TextView) findViewById(R.id.serviceProviderName);
        serviceProviderAddress = (TextView) findViewById(R.id.serviceProviderAddress);
        serviceProviderEmail = (TextView) findViewById(R.id.serviceProviderEmail);
        serviceProviderLayout = (LinearLayout) findViewById(R.id.serviceProviderInfo);


//        pushDataToDatabase();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        Mmap.getUiSettings().setZoomControlsEnabled(true);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {


            getCurrentLocation();


            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("USERS")
                    .child("SERVICE-PROVIDERS");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        String name = childSnapshot.child("NAME").getValue(String.class);
                        String email = childSnapshot.child("EMAIL").getValue(String.class);
                        String latStr = childSnapshot.child("LOCATION").child("lat").getValue(String.class);
                        String lngStr = childSnapshot.child("LOCATION").child("lng").getValue(String.class);
                        double lat = latStr != null ? Double.parseDouble(latStr.trim()) : 0.0;
                        double lng = lngStr != null ? Double.parseDouble(lngStr.trim()) : 0.0;

                        LatLng location = new LatLng(lat, lng);

                        // Add a marker for each location
                        mMarkers = new HashMap<>();
                        markerzy = mMarkers.get(name);
                        if (markerzy != null) {
                            markerzy.setPosition(location);
                        } else {

                            markerzy = Mmap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(userId)
                                    .snippet(email)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            mMarkers.put(name, markerzy);


                            //Mao ni ang changes
                            markerzy.setTag(userId);
                            //Pa load sa ko piste
                        }
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });



            //Hire Button Functionlitye
            hireButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String customerUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("USERS")
                            .child("SERVICE-PROVIDERS").child(serviceProviderUserId).child("JOB-OFFERS");

                    Toast.makeText(getApplicationContext(), serviceProviderUserId, Toast.LENGTH_LONG).show();

                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap map = new HashMap();
                            //Adding Fields
                            map.put("PENDING",customerUserId);
                            mDatabaseRef.updateChildren(map);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });

            Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {


                    displayToTextView(marker.getTag().toString());
                    serviceProviderUserId = marker.getTag().toString();
                    return true;
                }
            });

            Mmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    serviceProviderName.setText("");
                    serviceProviderAddress.setText("");
                    serviceProviderEmail.setText("");
                    serviceProviderLayout.setVisibility(View.GONE);
                }
            });
        }


    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            Mmap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                            Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

                            pushDataToDatabase();
                        }
                    });
        }
    }


    private DatabaseReference customerRef;
    private ValueEventListener customerLocationRefListener;

    private void pushDataToDatabase() {
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        customerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS").child(customerId).child("LOCATION");
        customerLocationRefListener = customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap map = new HashMap();
                //Adding fields
                map.put("lat", String.valueOf(latLng.latitude));
                map.put("lng", String.valueOf(latLng.longitude));

                customerRef.updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void displayToTextView(String providerId) {
        DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(providerId);
        providerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("NAME").getValue(String.class);
                String email = snapshot.child("EMAIL").getValue(String.class);
                String address = snapshot.child("ADDRESS").getValue(String.class);


                serviceProviderName.setText(name);
                serviceProviderEmail.setText(email);
                serviceProviderAddress.setText(address);

                serviceProviderLayout.setVisibility(View.VISIBLE);

                // display the data in TextViews
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}