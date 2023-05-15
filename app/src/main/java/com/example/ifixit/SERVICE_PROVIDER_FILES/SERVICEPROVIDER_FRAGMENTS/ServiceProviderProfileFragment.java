package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
import com.example.ifixit.SERVICE_PROVIDER_FILES.RequestAdapter;
import com.example.ifixit.SERVICE_PROVIDER_FILES.RequestItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProviderProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Variables
    private EditText serviceProviderName;
    private TextView customerEmail;
    private EditText serviceProviderAddress;
    private ImageView serviceProviderImage;



    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;

    private String userID;
    private String mUserName;
    private String mEmail;
    private String mAddress;
    private String mPhone;
    private Button saveBtn;

    private Uri resultUri;
    private String mProfileImageUrl;
    private Spinner serviceSpinner;
    private String mService;


    //------------------------
    //TextViews
    private TextView SPname;
    private TextView SPservice;
    private TextView SPemail;
    //ImageView
    private ImageView SPnewimage;
    private ImageView settingsImg;
    //------------------------

    //------Request Variables----
    private RecyclerView recyclerView;
    private List<RequestItem> requestItems;
    private RequestAdapter requestAdapter;
    //------------------------

    //------Profile Edit Variables
    private EditText name,phone,address;
    private Button saveBtnNew;
    private LinearLayout SPeditLayout;

    ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.service_provider_fragment_profile, container, false);

        //------------------------


        settingsImg = (ImageView) rootView.findViewById(R.id.settingBtn);

        serviceProviderImage = (ImageView) rootView.findViewById(R.id.SPNewImage);

        //Drop Down
        serviceSpinner = rootView.findViewById(R.id.adminspinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.service_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);
        serviceSpinner.setOnItemSelectedListener(this);
        //------------------------


        //--------Header-----------
        SPname = (TextView)rootView.findViewById(R.id.tvSPname);
        SPemail = (TextView)rootView.findViewById(R.id.tvSPemail);
        SPservice = (TextView)rootView.findViewById(R.id.tvSPservice);
        SPnewimage = (ImageView)rootView.findViewById(R.id.SPNewImage) ;
        //------------------------

        //------Edit Details Reference--------
        name = (EditText) rootView.findViewById(R.id.SPNameET);
        phone = (EditText) rootView.findViewById(R.id.SPPhoneET);
        address = (EditText)rootView.findViewById(R.id.SPAddressET);
        saveBtnNew=(Button)rootView.findViewById(R.id.SPsaveBtnNew);
        SPeditLayout = (LinearLayout)rootView.findViewById(R.id.SPeditLayout);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        //---Request References-----
        recyclerView = rootView.findViewById(R.id.recyclerview);
        requestItems = new ArrayList<>();
        requestAdapter = new RequestAdapter(getActivity(), requestItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(requestAdapter);

        String serviceProviderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference serviceProviderRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(serviceProviderID)
                .child("joboffers")
                .child("pending");

        serviceProviderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestItems.clear();
                for (DataSnapshot customerSnapShot : snapshot.getChildren()) {
                    String customerID = customerSnapShot.getKey();
                    DatabaseReference customerRef = serviceProviderRef.child(customerID);

                    customerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userId = snapshot.getKey();
                                String name = snapshot.child("name").getValue(String.class);
                                String email = snapshot.child("email").getValue(String.class);
                                String address = snapshot.child("address").getValue(String.class);
                                String imgURL = snapshot.child("profileimageurl").getValue(String.class);

                                String jobtype = snapshot.child("jobtype").getValue(String.class);
                                String totalPrice = snapshot.child("totalprice").getValue(String.class);
                                requestItems.add(new RequestItem(name, address,userId,imgURL,email,jobtype,totalPrice,address));
                                requestAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ServiceProviderNotif", "Error: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ServiceProviderNotif", "Error: " + error.getMessage());
            }
        });


        //------------------------



        DatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("service-providers")
                .child("verified")
                .child(userID);
            DatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getHeaderInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        serviceProviderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        settingsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                            if (map.get("name") != null) {
                                mUserName = map.get("name").toString();
                                name.setText(mUserName);
                            }

                            if (map.get("address") != null) {
                                mAddress = map.get("address").toString();
                                address.setText(mAddress);
                            }
                            if(map.get("phone")!=null){
                                mPhone = map.get("phone") .toString();
                                phone.setText(mPhone);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                SPeditLayout.setVisibility(View.VISIBLE);

            }
        });

        saveBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
                SPeditLayout.setVisibility(View.GONE);
            }
        });





        return rootView;

    }


    public void getHeaderInfo() {
        DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("name") != null) {
                        mUserName = map.get("name").toString();
                        SPname.setText(mUserName);
                    }
                    if (map.get("email") != null) {
                        mEmail = map.get("email").toString();
                        SPemail.setText(mEmail);
                    }

                    if (map.get("service") != null) {
                        mService = map.get("service").toString();
                        SPservice.setText(mService);

                    }
                    if (map.get("profileimageurl") != null) {
                        mProfileImageUrl = map.get("profileimageurl").toString();
                        Glide.with(getContext().getApplicationContext()).load(mProfileImageUrl).into(SPnewimage);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }








    private void saveUserInformation() {
        mUserName = name.getText().toString();
        mPhone = phone.getText().toString();
        mAddress = address.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("name", mUserName);
        userInfo.put("address", mAddress);
        userInfo.put("service", mService);


        DatabaseRef.updateChildren(userInfo);
        if (resultUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), resultUri);

//                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            String downloadUrlStr = downloadUrl.toString();

                            Map<String, Object> newImage = new HashMap<>();
                            newImage.put("profileimageurl", downloadUrlStr);
                            DatabaseRef.updateChildren(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the error
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                }
            });
        } else {
//            finish();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            SPnewimage.setImageURI(resultUri);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mService = serviceSpinner.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mService = "";
    }
}
