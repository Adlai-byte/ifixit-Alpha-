package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
 import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ifixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ServiceProviderRegistrationActivity extends AppCompatActivity {

    // Variables

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    // EditTexts
    private EditText etName;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPassword;
    private Spinner spService;
    private EditText etConfirmPassword;
    private EditText etPhone;

    ArrayAdapter<CharSequence> adapter;

    // Buttons
    private Button btnRegister;

    // TextView
    private TextView tvAlreadyHave;

    // Strings
    private String mService;
    private String defaultProfilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_registration);

        defaultProfilePicUrl = "https://firebasestorage.googleapis.com/v0/b/ifixit-fac6e.appspot.com/o/profile_images%2F64_7.png?alt=media&token=f03436db-e228-4a0c-8a48-04bc480b8bca";

        // Layout Connecting
        // Spinner
        spService = findViewById(R.id.adminspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.service_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spService.setAdapter(adapter);
        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                mService = spService.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
                mService = "";
            }
        });

        etPhone = findViewById(R.id.SPetPhone);
        etConfirmPassword = findViewById(R.id.SPetConfirmPassword);
        etName = findViewById(R.id.adminetFullName);
        etAddress = findViewById(R.id.adminetAddress);
        etEmail = findViewById(R.id.adminetEmail);
        etPassword = findViewById(R.id.adminetPassword);
        btnRegister = findViewById(R.id.adminbtnRegister);
        tvAlreadyHave = findViewById(R.id.admintvAlreadyHaveAnAccount);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent intent = new Intent(ServiceProviderRegistrationActivity.this, ServiceProviderMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        // On Click Listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String address = etAddress.getText().toString();
                final String service = String.valueOf(spService.getSelectedItem());
                final String phone = etPhone.getText().toString();
                final String confirmPassword = etConfirmPassword.getText().toString();

                // Validations
                if (TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(address)
                        || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(confirmPassword)
                        || TextUtils.isEmpty(phone))
                {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }  else if (phone.length()!=11) {
                    Toast.makeText(ServiceProviderRegistrationActivity.this, "Phone number must be 11 digits exactly", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(ServiceProviderRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(ServiceProviderRegistrationActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Toast.makeText(ServiceProviderRegistrationActivity.this, "Malformed email", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(ServiceProviderRegistrationActivity.this, "Sign-up Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String userID = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                        .child("service-providers")
                                        .child("verified")
                                        .child(userID);
                                current_user_db.setValue(true);

                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("address", address);
                                userInfo.put("phone", phone);
                                userInfo.put("service", service);
                                userInfo.put("rating", 0.0);
                                userInfo.put("maxPrice", 0.0);
                                userInfo.put("minPrice", 0.0);
                                userInfo.put("service-schedules",true);
                                userInfo.put("profileimageurl", defaultProfilePicUrl);

                                current_user_db.updateChildren(userInfo);
                                Intent intent = new Intent(ServiceProviderRegistrationActivity.this, ServiceProviderMainMenuActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(ServiceProviderRegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        tvAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceProviderRegistrationActivity.this, ServiceProviderLoginActivity.class);
                startActivity(intent);
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

    public static boolean isPasswordValid(String password) {
        // Check if password length is at least 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Check if password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check if password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Password meets all the password standards
        return true;
    }
}
