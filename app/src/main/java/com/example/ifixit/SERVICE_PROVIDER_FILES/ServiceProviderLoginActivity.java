package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.content.Intent;
import android.os.Bundle;
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

public class ServiceProviderLoginActivity extends AppCompatActivity {


    //---Variables

    //------Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    //------EditTexts
    private EditText etEmail;
    private EditText etPassword;

    //------Buttons
    private Button btnLogin;

    //------TextViews
    private TextView tvNoAccout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_login);

        //-----Referencing
        etEmail = (EditText) findViewById(R.id.etadminEmail);
        etPassword = (EditText) findViewById(R.id.etadminPassword);

        btnLogin = (Button) findViewById(R.id.adminbtnLogin);

        tvNoAccout = (TextView) findViewById(R.id.tvadminNoAccout);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(ServiceProviderLoginActivity.this, ServiceProviderMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //-----On Click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(ServiceProviderLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful() ){
                            Toast.makeText(ServiceProviderLoginActivity.this, "Sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tvNoAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceProviderLoginActivity.this, ServiceProviderRegistrationActivity.class);
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