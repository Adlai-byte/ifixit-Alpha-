package com.example.ifixit.ORGANIZATIONAL_FILES;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ifixit.CUSTOMER_FILES.CustomerLoginActivity;
import com.example.ifixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrgRegistrationActivity extends AppCompatActivity {

    //Variables

    //------Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    //------EditTexts
    private EditText etName, etOrgname;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword, etConfirmPassword;

    //------Buttons
    private Button btnRegister;

    //------TextView
    private TextView tvAlreadyHave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_registration);

        //---Layout Connecting
        etConfirmPassword = (EditText)findViewById(R.id.etadminPassword2);
        etName = (EditText) findViewById(R.id.etFullName);
        etOrgname = (EditText)findViewById(R.id.etOrgName);
        etAddress =(EditText) findViewById(R.id.etAddress);
        etEmail = (EditText) findViewById(R.id.etadminEmail);
        etPassword = (EditText)findViewById(R.id.etadminPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        tvAlreadyHave = (TextView) findViewById(R.id.tvAlreadyHaveAnAccount);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(OrgRegistrationActivity.this, OrgMapsActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //---On Click Listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String orgname = etOrgname.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String address = etAddress.getText().toString();
                final String phone = etPhone.getText().toString();
                final String confirmPassword = etConfirmPassword.getText().toString();


                if (TextUtils.isEmpty(name)
                        ||TextUtils.isEmpty(orgname)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(address)
                        || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(confirmPassword)
                        || TextUtils.isEmpty(phone)
                        || !password.equals(confirmPassword)

                ) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields or password doesn't match", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(OrgRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(OrgRegistrationActivity.this, "Sign-up Error", Toast.LENGTH_SHORT).show();
                            }else {
                                String userID = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                        .child("organizational")
                                        .child(userID);
                                current_user_db.setValue(true);

                                Map userInfo = new HashMap();
                                userInfo.put("orgname",orgname);
                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("address", address);
                                userInfo.put("phone",phone);

                                current_user_db.updateChildren(userInfo);
                            }
                        }
                    });

                }


            }
        });
        tvAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrgRegistrationActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                return;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}