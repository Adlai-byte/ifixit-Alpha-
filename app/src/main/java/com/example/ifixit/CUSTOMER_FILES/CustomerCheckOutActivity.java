package com.example.ifixit.CUSTOMER_FILES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerCheckOutActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView job;
    private TextView address;
    private NumberPicker picker1;
    private TextView total;
    private Button placeOrder;
    private Spinner serviceType;

    //Use this as the key in the Hashmap

    //*
    private String service = "Installation";
    private String comment;
    double days = 1.0;
    double finalPrice;
    //*


    double initialPrice;


    // Variables from customer maps
    long timestamp = System.currentTimeMillis();
    DatabaseReference mServiceProviderRef;
    DatabaseReference mCustomerRef;
    DatabaseReference mServiceProviderRefForHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_check_out);
        //------Spinner
        Intent intent = getIntent();
        HashMap<String, Double> servicePriceDictionary = new HashMap<>();

        String installation = "Installation";
        String maintenance = "Maintenance";
        String repair = "Repair";

        servicePriceDictionary.put(installation, 500.0);
        servicePriceDictionary.put(maintenance, 600.0);
        servicePriceDictionary.put(repair, 450.0);


        serviceType = findViewById(R.id.adminspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.service_type, android.R.layout.simple_spinner_item);
        serviceType.setAdapter(adapter);


        serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                service = serviceType.getItemAtPosition(i).toString();
//                Toast.makeText(OrgCheckOutActivity.this, "Changed Here" +service, Toast.LENGTH_SHORT).show();

                if (servicePriceDictionary.containsKey(service)) {

                    initialPrice = servicePriceDictionary.get(service);



                } else {
                    Toast.makeText(CustomerCheckOutActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String customerUserId = intent.getStringExtra("customeruserid");
        String serviceProviderUserId = intent.getStringExtra("serviceprovideruserid");


        //Database References
        mServiceProviderRefForHeader = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId);

        mCustomerRef = FirebaseDatabase.getInstance().getReference()
                .child("customers");

        mServiceProviderRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId)
                .child("joboffers")
                .child("pending")
                .child(customerUserId);
        //------------------


        //Layputs
        profileImage = (ImageView) findViewById(R.id.serviceProviderProfileImage);
        name = (TextView) findViewById(R.id.serviceProviderName);
        job = (TextView) findViewById(R.id.serviceProviderJob);
        address = (TextView) findViewById(R.id.serviceProviderAddress);
        total = (TextView) findViewById(R.id.tvTotal);
        picker1 = (NumberPicker) findViewById(R.id.numberPicker1);
        placeOrder = (Button) findViewById(R.id.placeOrderButton);

        //Number Picker
        picker1.setMinValue(0);
        picker1.setMaxValue(31);


        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                days = picker1.getValue();
                finalPrice = days * initialPrice;
                total.setText(String.valueOf(finalPrice));

            }
        });


        getHeaderInfo();


        //-------------------------------------------------------------------------------------------
        //Variables From Customer Maps

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCustomerRef = FirebaseDatabase.getInstance().getReference()
                        .child("customers");
                mServiceProviderRef = FirebaseDatabase.getInstance().getReference()
                        .child("service-providers")
                        .child("verified")
                        .child(serviceProviderUserId)
                        .child("joboffers")
                        .child("pending")
                        .child(customerUserId);


                mCustomerRef.child(customerUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String imgUrl = snapshot.child("profileimageurl").getValue(String.class);

                            String jobType = service;
                            String commentReview = comment;
                            String daysOfWork = String.valueOf(days);
                            String totalPrice = String.valueOf(finalPrice);

                            // Create a new job offer HashMap with the customer's data
                            HashMap<String, String> jobOffer = new HashMap<>();
                            jobOffer.put("name", name);
                            jobOffer.put("address", address);
                            jobOffer.put("email", email);
                            jobOffer.put("timestamp", String.valueOf(timestamp));
                            jobOffer.put("profileimageurl", imgUrl);
                            jobOffer.put("jobtype", jobType);
                            jobOffer.put("comment", commentReview);
                            jobOffer.put("duration", daysOfWork);
                            jobOffer.put("totalprice",totalPrice);

                            mServiceProviderRef.setValue(jobOffer);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(CustomerCheckOutActivity.this, "Requested Succesfully", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(CustomerCheckOutActivity.this,CustomerMapsActivity.class);
                startActivity(intent1);


            }
        });

        //-------------------------------------------------------------------------------------------
    }

    public void getHeaderInfo() {
        mServiceProviderRefForHeader.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("name") != null) {
                        String mUserName;
                        mUserName = map.get("name").toString();
                        name.setText(mUserName);
                    }
                    if (map.get("address") != null) {
                        String mAddress;
                        mAddress = map.get("address").toString();
                        address.setText(mAddress);
                    }

                    if (map.get("service") != null) {
                        String mService;
                        mService = map.get("service").toString();
                        job.setText(mService);

                    }
                    if (map.get("profileimageurl") != null) {
                        String mProfileImageUrl;
                        mProfileImageUrl = map.get("profileimageurl").toString();
                        Glide.with(getApplication().getApplicationContext()).load(mProfileImageUrl).into(profileImage);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}