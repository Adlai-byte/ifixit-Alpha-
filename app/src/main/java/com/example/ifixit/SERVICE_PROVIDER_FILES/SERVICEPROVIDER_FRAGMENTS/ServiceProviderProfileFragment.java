package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ifixit.R;
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
import java.util.HashMap;
import java.util.Map;

public class ServiceProviderProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Variables
    private EditText serviceProviderName;
    private TextView customerEmail;
    private EditText serviceProviderAddress;

    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mAddress;
    private Button saveBtn;
    private ImageView serviceProviderImage;
    private Uri resultUri;
    private String mProfileImageUrl;
    private Spinner serviceSpinner;
    private String mService;
    ArrayAdapter<CharSequence> adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.service_provider_fragment_profile, container, false);

        serviceProviderName = (EditText) rootView.findViewById(R.id.SPName);
        customerEmail = (TextView) rootView.findViewById(R.id.customersEmail);
        serviceProviderAddress = (EditText) rootView.findViewById(R.id.SPAddress);
        saveBtn = (Button) rootView.findViewById(R.id.SPsaveUserInfoBtn);
        serviceProviderImage = (ImageView) rootView.findViewById(R.id.SPImage);
        mAuth = FirebaseAuth.getInstance();
        serviceSpinner = rootView.findViewById(R.id.spinner);
         adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.service_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);
        serviceSpinner.setOnItemSelectedListener(this);





        userID = mAuth.getCurrentUser().getUid();


        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(userID);
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
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
                    if (map.get("NAME") != null) {
                        mUserName = map.get("NAME").toString();
                        serviceProviderName.setText(mUserName);
                    }
                    if (map.get("EMAIL") != null) {
                        mEmail = map.get("EMAIL").toString();
                        customerEmail.setText(mEmail);
                    }
                    if (map.get("ADDRESS") != null) {
                        mAddress = map.get("ADDRESS").toString();
                        serviceProviderAddress.setText(mAddress);
                    }
                    if(map.get("SERVICE")!=null) {
                        mService = map.get("SERVICE").toString();
                        serviceSpinner.setSelection(adapter.getPosition(mService));

                    }
                    if (map.get("profileImageUrl") != null) {
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getContext().getApplicationContext()).load(mProfileImageUrl).into(serviceProviderImage);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInformation() {
        mUserName = serviceProviderName.getText().toString();
        mEmail = customerEmail.getText().toString();
        mAddress = serviceProviderAddress.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("NAME", mUserName);
        userInfo.put("ADDRESS", mAddress);
      userInfo.put("SERVICE",mService);


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
                            newImage.put("profileImageUrl", downloadUrlStr);
                            DatabaseRef.updateChildren(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    finish();
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
            serviceProviderImage.setImageURI(resultUri);
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
