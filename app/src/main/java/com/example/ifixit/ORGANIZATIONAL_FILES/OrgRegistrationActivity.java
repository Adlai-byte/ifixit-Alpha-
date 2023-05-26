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

    // Variables

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    // EditTexts
    private EditText etName, etOrgname;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;

    // Buttons
    private Button btnRegister;

    // TextView
    private TextView tvAlreadyHave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_registration);

        // Layout Connecting
        etConfirmPassword = findViewById(R.id.etadminPassword2);
        etName = findViewById(R.id.etFullName);
        etOrgname = findViewById(R.id.etOrgName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etadminEmail);
        etPassword = findViewById(R.id.etadminPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHave = findViewById(R.id.tvAlreadyHaveAnAccount);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(OrgRegistrationActivity.this, OrgMapsActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        // On Click Listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerOrganization();
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

    private void registerOrganization() {
        final String orgname = etOrgname.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(orgname)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword)
                || TextUtils.isEmpty(phone)
        )  {
            showToast("Please fill in all fields");
        } else if (!password.equals(confirmPassword)) {
            showToast("Password does not match");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(OrgRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        showToast("Sign-up Error: " + task.getException().getMessage());
                    } else {
                        String userID = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                .child("organizational")
                                .child(userID);
                        current_user_db.setValue(true);
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("orgname", orgname);
                        userInfo.put("name", name);
                        userInfo.put("email", email);
                        userInfo.put("address", address);
                        userInfo.put("phone", phone);

                        current_user_db.updateChildren(userInfo);

                        showToast("Registration successful");
                    }
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(OrgRegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
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
