package com.example.ifixit.SERVICE_PROVIDER_FILES;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ServiceProviderRegistrationActivity extends AppCompatActivity {

    //Variables

    //------Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    //------EditTexts
    private EditText etName;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPassword;
    private Spinner spService;
    ArrayAdapter<CharSequence> adapter;

    //------Buttons
    private Button btnRegister;

    //------TextView
    private TextView tvAlreadyHave;

    //------String
    private String mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_registration);

        //---Layout Connecting

        //Spinner
        spService = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.service_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spService.setAdapter(adapter);
        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // handle item selection
                mService = spService.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // handle nothing selected

                mService = "";
            }
        });


        etName = (EditText) findViewById(R.id.SPetFullName);
        etAddress =(EditText) findViewById(R.id.SPetAddress);
        etEmail = (EditText) findViewById(R.id.SPetEmail);
        etPassword = (EditText)findViewById(R.id.SPetPassword);
        btnRegister = (Button) findViewById(R.id.SPbtnRegister);
        tvAlreadyHave = (TextView) findViewById(R.id.SPtvAlreadyHaveAnAccount);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(ServiceProviderRegistrationActivity.this, ServiceProviderMainMenuActivity.class);
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

                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String address = etAddress.getText().toString();
                final String service = String.valueOf(spService.getSelectedItem());

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(ServiceProviderRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(ServiceProviderRegistrationActivity.this, "Sign-up Error", Toast.LENGTH_SHORT).show();
                        }else {
                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE-PROVIDERS").child(userID);
                            current_user_db.setValue(true);

                            Map userInfo = new HashMap();
                            userInfo.put("NAME", name);
                            userInfo.put("EMAIL", email);
                            userInfo.put("ADDRESS", address);
                            userInfo.put("PASSWORD",password);
                            userInfo.put("SERVICE",service);

                            current_user_db.updateChildren(userInfo);

                        }
                    }
                });

            }
        });
        tvAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceProviderRegistrationActivity.this, ServiceProviderLoginActivity.class);
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