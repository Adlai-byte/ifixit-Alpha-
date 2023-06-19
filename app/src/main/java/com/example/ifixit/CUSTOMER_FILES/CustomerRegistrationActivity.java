package com.example.ifixit.CUSTOMER_FILES;

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

public class CustomerRegistrationActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    // EditTexts
    private EditText etName;
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
        setContentView(R.layout.customer_registration);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(CustomerRegistrationActivity.this, CustomerMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        // Initialize Views
        etName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etadminEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etadminPassword);
        etConfirmPassword = findViewById(R.id.etadminPassword2);
        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHave = findViewById(R.id.tvAlreadyHaveAnAccount);

        // Register Button Click Listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Already Have an Account TextView Click Listener
        tvAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerRegistrationActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Input Validations
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
                || TextUtils.isEmpty(phone)) {
            showToast("Please fill in all fields");
        } else if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
        } else if (phone.length() != 11) {
            showToast("Phone number must be 11 digits exactly");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userID = firebaseUser.getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                    .child("customers")
                                    .child(userID);
                            current_user_db.setValue(true);

                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("email", email);
                            userInfo.put("address", address);
                            userInfo.put("phone", phone);
                            userInfo.put("notificationcount",0);
                            current_user_db.updateChildren(userInfo);

                            showToast("User registered successfully");
                            Intent intent = new Intent(CustomerRegistrationActivity.this, CustomerMainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Registration failed");
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(CustomerRegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
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
