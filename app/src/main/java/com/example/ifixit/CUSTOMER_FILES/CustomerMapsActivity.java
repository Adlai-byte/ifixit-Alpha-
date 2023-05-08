package com.example.ifixit.CUSTOMER_FILES;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private TextView serviceProviderJob;
    private TextView serviceProviderAddress;
    private LinearLayout serviceProviderLayout;
    private ImageView serviceProviderImage;

    private Uri resultUri;
    private String mProfileImage;

    Marker markerzy;

    String serviceProviderUserId;

    //------ Customer Reviews Variables----
    private RecyclerView recyclerView;
    private List<ReviewItem> reviewItems;
    private ReviewAdapter reviewAdapter;
    //------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.CMmap);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //-------Review References------
        recyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewItems = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);
        //------------------------------

        hireButton = (Button) findViewById(R.id.serviceProviderRequest);
        serviceProviderName = (TextView) findViewById(R.id.serviceProviderName);
        serviceProviderAddress = (TextView) findViewById(R.id.serviceProviderAddress);
        serviceProviderJob = (TextView) findViewById(R.id.serviceProviderJob);
        serviceProviderLayout = (LinearLayout) findViewById(R.id.serviceProviderInfo);
        serviceProviderImage = (ImageView) findViewById(R.id.serviceProviderProfileImage);


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
                        String service = childSnapshot.child("SERVICE").getValue(String.class);
                        if (service == null) {
                            service = "None";
                        }
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

                            switch (service) {
                                case "Plumber":
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_plumber_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);

                                    break;
                                case "Electrician":
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_electrician_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);
                                    break;
                                case "Carpenter":
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_carpentry_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);
                                    break;
                                case "Computer Repair":
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_computer_repair_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);
                                    break;
                                case "Gardener":
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_gardener_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);
                                    break;
                                default:
                                    markerzy = Mmap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(userId)
                                            .snippet(service)
                                            .icon(vectorToBitmap(R.mipmap.ic_app_logo)));
                                    mMarkers.put(name, markerzy);
                                    markerzy.setTag(userId);
                                    break;
                            }

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

                    Intent intent =new Intent(CustomerMapsActivity.this, CustomerCheckOutActivity.class);
                    intent.putExtra("customerUserId",customerUserId );
                    intent.putExtra("serviceProviderUserId",serviceProviderUserId);

                    startActivity(intent);


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
                    serviceProviderJob.setText("");
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
                            // Hide sa nato ang atong Map Marker
//                            Mmap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
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

        //------------REVIEW  FUNCTION-----------------
        //Clear the Array
        reviewItems.clear();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child("SERVICE-PROVIDERS")
                .child(providerId)
                .child("REVIEWS");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    String name = childSnapshot.child("NAME").getValue(String.class);
                    String imgUrl = childSnapshot.child("profileImageUrl").getValue(String.class);
                    String reviews = childSnapshot.child("COMMENT").getValue(String.class);
                        reviewItems.add(new ReviewItem(name,imgUrl,reviews));

                }
                reviewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ServiceProviderNotif", "Error: " + error.getMessage());
            }
        });

        //------------------------------

        DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(providerId);
        providerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("NAME").getValue(String.class);
                String job = snapshot.child("SERVICE").getValue(String.class);
                String address = snapshot.child("ADDRESS").getValue(String.class);
                String serviceProviderImageURL = snapshot.child("profileImageUrl").getValue(String.class);
                serviceProviderName.setText(name);
                serviceProviderJob.setText(job);
                serviceProviderAddress.setText(address);
                serviceProviderLayout.setVisibility(View.VISIBLE);
                Glide.with(getApplication()).load(serviceProviderImageURL).into(serviceProviderImage);

                // display the data in TextViews
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //* Converts a vector asset icon to a BitmapDescriptor object for use as a marker icon.

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        if (vectorDrawable == null) {
            Log.e(TAG, "vectorToBitmap: vector drawable is null.");
            return BitmapDescriptorFactory.defaultMarker();
        }

        int width = vectorDrawable.getIntrinsicWidth();
        int height = vectorDrawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
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