package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class CustomerProfileFragment extends Fragment {

    //Variables
    private TextView customerName;
    private TextView customerEmail;
    private TextView customerAddress;

    private LinearLayout editLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mPhone;
    private String mAddress;
    private ImageView mSettings;
    private Button saveBtn, uploadImage;
    private ImageView customerImage;
    private Uri resultUri;
    private String mProfileImageUrl;

    private EditText name, email, address;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_fragment_profile, container, false);

        customerName = (TextView) rootView.findViewById(R.id.customerName);
        customerEmail = (TextView) rootView.findViewById(R.id.customerEmail);
        customerAddress = (TextView) rootView.findViewById(R.id.customerAddress);
        mSettings = (ImageView) rootView.findViewById(R.id.customerSettingBtn);

        uploadImage = (Button)rootView.findViewById(R.id.uploadImage);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        customerImage = (ImageView) rootView.findViewById(R.id.customerProfileImage);

        name = (EditText) rootView.findViewById(R.id.NameET);
        email = (EditText) rootView.findViewById(R.id.PhoneET);
        address = (EditText) rootView.findViewById(R.id.AddressET);




        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        editLayout = (LinearLayout) rootView.findViewById(R.id.editLayout);

        DatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("customers")
                .child(userID);

        //Listener

        DatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getHeaderInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        uploadImage.setOnClickListener(new View.OnClickListener() {
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
                editLayout.setVisibility(View.GONE);
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
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
                            if(map.get("email")!=null){
                                mEmail = map.get("email") .toString();
                                email.setText(mEmail);
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                editLayout.setVisibility(View.VISIBLE);



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
                        customerName.setText(mUserName);
                    }

                    if (map.get("email") != null) {
                        mEmail = map.get("email").toString();
                        customerEmail.setText(mEmail);
                    }

                    if (map.get("address") != null) {
                        mAddress = map.get("address").toString();
                        customerAddress.setText(mAddress);
                    }

                    if (map.get("profileimageurl") != null) {
                        mProfileImageUrl = map.get("profileimageurl").toString();
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


        mUserName = name.getText().toString();
        mAddress = address.getText().toString();
        mPhone = email.getText().toString();


        Map <String,Object>userInfo = new HashMap();
        userInfo.put("name", mUserName);
        userInfo.put("phone", mPhone);
        userInfo.put("address", mAddress);
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

                            Map<String,Object>  newImage = new HashMap<>();
                            newImage.put("profileimageurl", downloadUrlStr);

                            DatabaseRef.updateChildren(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void Void) {

                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Profile Image Failed", Toast.LENGTH_SHORT).show();
                                    // Handle the error
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
