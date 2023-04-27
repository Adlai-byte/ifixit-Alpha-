package com.example.ifixit.GLOBAL_ACTIVITIES;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class ProfileFragment extends Fragment {

    //Variables
    private EditText customerName;
    private EditText customerEmail;
    private EditText customerAddress;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mAddress;
    private Button saveBtn;
    private ImageView customerImage;
    private Uri resultUri;
    private String mProfileImageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        customerName = (EditText) rootView.findViewById(R.id.customersName);
//        customerEmail = (EditText) rootView.findViewById(R.id.customersEmail);
        customerAddress = (EditText) rootView.findViewById(R.id.customerAddress);
        saveBtn = (Button) rootView.findViewById(R.id.saveUserInfoBtn);
        customerImage = (ImageView) rootView.findViewById(R.id.customerImage);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child("CUSTOMERS").child(userID);
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getHeaderInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        customerImage.setOnClickListener(new View.OnClickListener() {
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
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("NAME") != null) {
                        mUserName = map.get("NAME").toString();
                        customerName.setText(mUserName);
                    }
//                    if (map.get("EMAIL") != null) {
//                        mEmail = map.get("EMAIL").toString();
//                        customerEmail.setText(mEmail);
//                    }
                    if (map.get("ADDRESS") != null) {
                        mAddress = map.get("ADDRESS").toString();
                        customerAddress.setText(mAddress);
                    }
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
//                        Glide.with(getApplication()).load(mProfileImageUrl).into(customerImage);
                        Glide.with(getContext().getApplicationContext()).load(mProfileImageUrl).into(customerImage);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInformation() {
        mUserName = customerName.getText().toString();
//        mEmail = customerEmail.getText().toString();
        mAddress = customerAddress.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("NAME", mUserName);
        userInfo.put("ADDRESS", mAddress);
//      userInfo.put("services",mService);


        mCustomerDatabase.updateChildren(userInfo);
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
                            mCustomerDatabase.updateChildren(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            customerImage.setImageURI(resultUri);
        }
    }


}
