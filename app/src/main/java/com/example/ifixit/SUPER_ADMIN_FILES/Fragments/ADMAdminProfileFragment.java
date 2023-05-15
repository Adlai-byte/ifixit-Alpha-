package com.example.ifixit.SUPER_ADMIN_FILES.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ADMAdminProfileFragment extends Fragment {

    //Variables
    private TextView adminName;
    private TextView adminEmail;
    private TextView adminAddress;


    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private String userID;
    private String mUserName;
    private String mEmail;
    private String mAddress;
    private ImageView adminSettings;
//    private Button saveBtn;
    private ImageView adminProfileImage;
    private Uri resultUri;
    private String mProfileImageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_fragment_profile, container, false);

        adminName = (TextView) rootView.findViewById(R.id.adminName);
        adminEmail = (TextView) rootView.findViewById(R.id.adminEmail);
        adminAddress = (TextView) rootView.findViewById(R.id.adminAddress);
        adminSettings = (ImageView) rootView.findViewById(R.id.adminSettingBtn);

//        saveBtn = (Button) rootView.findViewById(R.id.CMsaveUserInfoBtn);
        adminProfileImage = (ImageView) rootView.findViewById(R.id.adminProfileImage);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("ADMIN").child(userID);
        DatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getHeaderInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adminProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveUserInformation();
//            }
//        });

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
                        adminName.setText(mUserName);
                    }
                    if (map.get("EMAIL") != null) {
                        mEmail = map.get("EMAIL").toString();
                        adminEmail.setText(mEmail);
                    }
                    if (map.get("ADDRESS") != null) {
                        mAddress = map.get("ADDRESS").toString();
                        adminAddress.setText(mAddress);
                    }
                    if (map.get("profileImageUrl") != null) {
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getContext().getApplicationContext()).load(mProfileImageUrl).into(adminProfileImage);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInformation() {
        mUserName = adminName.getText().toString();
//        mEmail = customerEmail.getText().toString();
        mAddress = adminAddress.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("NAME", mUserName);
        userInfo.put("ADDRESS", mAddress);
//      userInfo.put("services",mService);


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
                                public void onSuccess(Void Void) {
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
            adminProfileImage.setImageURI(resultUri);
        }
    }


}
