package com.example.ifixit.CUSTOMER_FILES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CustomerCheckOutActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView job;
    private TextView address;
    private TextView total;
    private Button placeOrder;
    private Spinner serviceType;

    private TextView statusTextView;
    private TextView dateTextView;


    //Calendar
    private CalendarView calendarView;
    private String selecteddate;

    //Calendar View
    private CalendarView calendar;


    private ArrayAdapter<CharSequence> adapter;


    //Use this as the key in the Hashmap

    //*
    private String serviceProviderService = "";
    private String service = "";
    String comment = "";
    double days = 1.0;
    double finalPrice;
    //*
    Calendar selectedDate;

    double initialPrice;


    // Variables from customer maps
    long timestamp = System.currentTimeMillis();
    DatabaseReference serviceProviderServiceSchedules;
    DatabaseReference serviceProviderPendingRef;
    DatabaseReference mCustomerRef;
    DatabaseReference mServiceProviderRefForHeader;

    ArrayList<String>ListOfUnvailableDates = new ArrayList<>();

    private HashMap<String, Double> servicePriceDictionary = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_check_out);
        //------Spinner
        Intent intent = getIntent();


        //Variables
        String customerUserId = intent.getStringExtra("customeruserid");
        String serviceProviderUserId = intent.getStringExtra("serviceprovideruserid");


        serviceProviderServiceSchedules = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId)
                .child("service-schedules");
        //Database References
        mServiceProviderRefForHeader = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId);

        //Adding all the service data and it's price
        //Carpentry Services---------------------------------------------------------------
        servicePriceDictionary.put("Plywood Works", 1500.00);
        servicePriceDictionary.put("Wood Works", 2000.00);
        servicePriceDictionary.put("Wood Design Works", 3000.0);
        servicePriceDictionary.put("Door Lock Repair and Install", 800.0);
        servicePriceDictionary.put("Furniture Repair", 1500.0);
        servicePriceDictionary.put("Blinds and Curtain install", 600.0);
        servicePriceDictionary.put("Varnish Wood", 500.0);
        servicePriceDictionary.put("Modular Kitchen Works", 5000.0);
        //----------------------------------------------------------------------------------
        //Plumbing Services-----------------------------------------------------------------
        servicePriceDictionary.put("Underground and Above Ground Pipe installation", 1000.0);
        servicePriceDictionary.put("Flush Tank Service", 800.0);
        servicePriceDictionary.put("Wash Basin Service", 500.0);
        servicePriceDictionary.put("Hot and Cold Water Mixed Repair and Installation", 1500.0);
        servicePriceDictionary.put("Underground Tank Cleaning", 1500.0);
        servicePriceDictionary.put("Water Meter Installation", 800.0);
        servicePriceDictionary.put("Motor Installation", 1500.0);
        //---------------------------------------------------------------------------------
        //Electrical Services--------------------------------------------------------------
        servicePriceDictionary.put("Internal House Wiring (under wall)", 1000.0);
        servicePriceDictionary.put("Casing House Wiring (above wall)", 2000.0);
        servicePriceDictionary.put("Fan Repair and Installation", 600.0);
        servicePriceDictionary.put("Inverter and Stabilizer Repair and Installation", 1500.0);
        servicePriceDictionary.put("Motor Repair", 800.0);
        servicePriceDictionary.put("Solar Panel Installation", 5000.0);
        //---------------------------------------------------------------------------------
        //Masonry Services-----------------------------------------------------------------
        servicePriceDictionary.put("Stone Walls Construction", 1500.0);
        servicePriceDictionary.put("Brick walls construction", 1200.0);
        servicePriceDictionary.put("Concrete block walls", 1000.0);
        servicePriceDictionary.put("Concrete foundations", 1000.0);
        servicePriceDictionary.put("Repairing cracked or damaged bricks/stone", 300.0);
        servicePriceDictionary.put("Masonry columns and pillars", 1000.0);
        servicePriceDictionary.put("Masonry garden sculptures<", 1500.0);
        //---------------------------------------------------------------------------------
        //Computer Services ---------------------------------------------------------------
        servicePriceDictionary.put("Computer Assembly and Installation", 500.0);
        servicePriceDictionary.put("Hardware Troubleshooting and Repair", 300.0);
        servicePriceDictionary.put("System Upgrades and Expansion", 500.0);
        servicePriceDictionary.put("Data Backup and Recovery", 300.0);
        servicePriceDictionary.put("Virus and Malware Removal", 200.0);
        servicePriceDictionary.put("System Maintenance and Optimization", 300.0);
        servicePriceDictionary.put("Network Setup and Troubleshooting", 500.0);
        servicePriceDictionary.put("Peripheral Setup and Configuration", 200.0);
        //---------------------------------------------------------------------------------
        //Gardening Services---------------------------------------------------------------
        servicePriceDictionary.put("Lawn Care", 500.0);
        servicePriceDictionary.put("Planting and Bed Maintenance", 300.0);
        servicePriceDictionary.put("Garden Design and Landscaping", 5000.0);
        servicePriceDictionary.put("Seasonal Plantings and Container Gardens", 300.0);
        servicePriceDictionary.put("Vegetable and Herb Gardens", 500.0);
        servicePriceDictionary.put("Tree and Shrub Care", 500.0);
        servicePriceDictionary.put("Garden Cleanup and Waste Removal", 200.0);
        servicePriceDictionary.put("Consultation and Maintenance Planning", 500.0);
        //--------------------------------------------------------------------------------

        //Get the service provider service type
        mServiceProviderRefForHeader.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    serviceProviderService = snapshot.child("service").getValue(String.class);
                    setupSpinnerAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mCustomerRef = FirebaseDatabase.getInstance().getReference()
                .child("customers");

        serviceProviderPendingRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId)
                .child("joboffers")
                .child("pending")
                .child(customerUserId);
        //-------------Database References------------
        mCustomerRef = FirebaseDatabase.getInstance().getReference()
                .child("customers");

        //Customer Pending request reference
        DatabaseReference customerPendingRequestRef = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("pending-request");

        //Service Providers Service Schedules


        //Pending Jobs directory reference
        serviceProviderPendingRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId)
                .child("joboffers")
                .child("pending");

        //Service Provider reference
        DatabaseReference ServiceProviderRefData = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderUserId);
        //-----------------------------------------------------------


        //Layouts
        profileImage = (ImageView) findViewById(R.id.serviceProviderProfileImage);
        name = (TextView) findViewById(R.id.serviceProviderName);
        job = (TextView) findViewById(R.id.serviceProviderJob);
        address = (TextView) findViewById(R.id.serviceProviderAddress);
        total = (TextView) findViewById(R.id.tvTotal);
        serviceType = (Spinner) findViewById(R.id.adminspinner);
        placeOrder = (Button) findViewById(R.id.placeOrderButton);


        //Service schedule reference



        //Calendar
        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                // Create a Calendar instance for the selected date
                selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Create a Calendar instance for the current date
                Calendar currentDate = Calendar.getInstance();

                // Compare the selected date with the current date
                if (selectedDate.before(currentDate)) {
                    Toast.makeText(CustomerCheckOutActivity.this, "Can't select a date before the current date", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Example format 05/02/2001
                selecteddate = (month + 1) + "/" + dayOfMonth + "/" + year;


                serviceProviderServiceSchedules.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        StringBuilder statusBuilder = new StringBuilder();
                        Boolean isFound = false;
                        if (snapshot.exists()) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                String serviceType = childSnapshot.child("service").getValue(String.class);
                                String dateOfService = childSnapshot.child("dateofservice").getValue(String.class);
                                String newSelectedDate = selecteddate;

                                if(newSelectedDate.contains(dateOfService)){
                                    Toast.makeText(CustomerCheckOutActivity.this, "This date is not available", Toast.LENGTH_SHORT).show();
                                    ListOfUnvailableDates.add(dateOfService);
                                }else {
                                    Toast.makeText(CustomerCheckOutActivity.this,"This date is available",Toast.LENGTH_SHORT).show();
                                }

                            }

                        }else {
                            Toast.makeText(CustomerCheckOutActivity.this, "No current bookings in the database", Toast.LENGTH_SHORT).show();
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database read error
                    }
                });

            }
        });


        getHeaderInfo();


        //-------------------------------------------------------------------------------------------
        //Variables From Customer Maps

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean dateAvaible = true;
                comment = "None for the moment";


                for (String notAvaiableDates: ListOfUnvailableDates){
                    Calendar currentDate = Calendar.getInstance();
                    if(notAvaiableDates.contains(selecteddate)||selectedDate.before(currentDate)){
                        Toast.makeText(CustomerCheckOutActivity.this, "Please Select Another Date", Toast.LENGTH_SHORT).show();
                        dateAvaible = false;
                    }
                }

                if(dateAvaible){
                    Toast.makeText(CustomerCheckOutActivity.this, "Sheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeehhhhhh", Toast.LENGTH_SHORT).show();
                    mCustomerRef.child(customerUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String name = snapshot.child("name").getValue(String.class);
                                String address = snapshot.child("address").getValue(String.class);
                                String email = snapshot.child("email").getValue(String.class);
                                String imgUrl = snapshot.child("profileimageurl").getValue(String.class);

                                String jobType = service;
//                            String daysOfWork = String.valueOf(days);
                                String totalPrice = String.valueOf(finalPrice);
//                            String description = comment;

                                // Create a new job offer HashMap with the customer's data
                                HashMap<String, String> jobOffer = new HashMap<>();
                                HashMap<String, String> datesofservices = new HashMap<>();

                                jobOffer.put("userid", customerUserId);
                                jobOffer.put("service", service);
                                jobOffer.put("name", name);
                                jobOffer.put("address", address);
                                jobOffer.put("email", email);
                                jobOffer.put("timestamp", String.valueOf(timestamp));
                                jobOffer.put("profileimageurl", imgUrl);
                                jobOffer.put("jobtype", jobType);
//                            jobOffer.put("duration", daysOfWork);
                                jobOffer.put("totalprice", totalPrice);
//                            jobOffer.put("decription", description);
                                jobOffer.put("dateofservice", selecteddate);


                                // Hashmap for to send to pending request
                                HashMap<String, String> pendingReqData = new HashMap<>();
                                //Service Provider Data
                                ServiceProviderRefData.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String spName = snapshot.child("name").getValue(String.class);
                                        String spService = snapshot.child("service").getValue(String.class);
                                        pendingReqData.put("userid", serviceProviderUserId);
                                        pendingReqData.put("name", spName);
                                        pendingReqData.put("service", spService);
                                        pendingReqData.put("totalprice", totalPrice);
                                        pendingReqData.put("timestamp", String.valueOf(timestamp));
                                        pendingReqData.put("jobtype", jobType);
                                        pendingReqData.put("status", "PENDING");
                                        pendingReqData.put("dateofservice", selecteddate);

                                        //Service dates------------------------------------
                                        datesofservices.put("dateofservice", selecteddate);
                                        datesofservices.put("service", spService);
                                        //--------------------------------------------------




                                        //Sending Data to the customers pending request
                                        customerPendingRequestRef.push().setValue(pendingReqData);
                                        serviceProviderServiceSchedules.push().setValue(datesofservices);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                                //Sending the data to the service provider pending directory
                                serviceProviderPendingRef.push().setValue(jobOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data successfully added to the database
                                            Toast.makeText(CustomerCheckOutActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Failed to add data to the database
                                            Toast.makeText(CustomerCheckOutActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // ...
                        }
                    });

                    Intent intent1 = new Intent(CustomerCheckOutActivity.this, CustomerMapsActivity.class);
                    startActivity(intent1);
                }


            }
        });
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

    private void setupSpinnerAdapter() {
        switch (serviceProviderService) {
            case "Plumber":
                adapter = ArrayAdapter.createFromResource(this, R.array.plumbing_services, android.R.layout.simple_spinner_item);
                break;
            case "Electrician":
                adapter = ArrayAdapter.createFromResource(this, R.array.electrical_services, android.R.layout.simple_spinner_item);
                break;
            case "Masonry":
                adapter = ArrayAdapter.createFromResource(this, R.array.masonry_services, android.R.layout.simple_spinner_item);
                break;
            case "Computer Repair":
                adapter = ArrayAdapter.createFromResource(this, R.array.computer_services, android.R.layout.simple_spinner_item);
                break;
            case "Gardener":
                adapter = ArrayAdapter.createFromResource(this, R.array.gardening_services, android.R.layout.simple_spinner_item);
                break;
        }

        serviceType.setAdapter(adapter);

        serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                service = serviceType.getItemAtPosition(i).toString();

                if (servicePriceDictionary.containsKey(service)) {
                    finalPrice = servicePriceDictionary.get(service);
                    total.setText(String.valueOf(finalPrice));

                } else {
                    Toast.makeText(CustomerCheckOutActivity.this, String.valueOf(servicePriceDictionary.get(service)), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(CustomerCheckOutActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the selection when nothing is selected
            }
        });
    }
}