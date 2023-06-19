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
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class CustomerLoginActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    // Views
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnLoginGoogle;
    private TextView tvNoAccount;

    // Google
//    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login);

        // Initialization
        btnLoginGoogle = findViewById(R.id.adminbtnSignInWithGoogle);
        etEmail = findViewById(R.id.etadminEmail);
        etPassword = findViewById(R.id.etadminPassword);
        btnLogin = findViewById(R.id.adminbtnLogin);
        tvNoAccount = findViewById(R.id.tvadminNoAccout);

        // Configure Google Sign-In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(this, gso);



        btnLoginGoogle.setVisibility(View.GONE);
//        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signInWithGoogle();
//            }
//        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }



//                if (user != null && user.isEmailVerified()) {
//                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainMenuActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        };

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // No account text click listener
        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerLoginActivity.this, CustomerRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
//
//    private void signInWithGoogle() {
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }



    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(CustomerLoginActivity.this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainMenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CustomerLoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(CustomerLoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(CustomerLoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(CustomerLoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//                FirebaseAuth.getInstance().signInWithCredential(credential)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMapsActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(CustomerLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            } catch (ApiException e) {
//                e.printStackTrace();
//                Toast.makeText(CustomerLoginActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
//                // Show the Google Sign-In options again
//                signInWithGoogle();
//            }
//        }
//    }

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
