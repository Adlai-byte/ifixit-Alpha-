package com.example.ifixit.SUPER_ADMIN_FILES;

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

public class AdminRegistrationActivity extends AppCompatActivity {

    //Variables

    //------Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    //------EditTexts
    private EditText etName;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPassword;


    //------Buttons
    private Button btnRegister;

    //------TextView
    private TextView tvAlreadyHave;

    //------String



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registration);

        //---Layout Connecting


        etName = (EditText) findViewById(R.id.etadminUserName);
        etEmail = (EditText) findViewById(R.id.etadminEmail);
        etPassword = (EditText) findViewById(R.id.etadminPassword);

        btnRegister = (Button) findViewById(R.id.adminbtnRegister);
        tvAlreadyHave = (TextView) findViewById(R.id.admintvAlreadyHaveAnAccount);


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(AdminRegistrationActivity.this, AdminMainMenuActivity.class);
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

                    //Validations
                if (TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password)

                ) {
                    Toast.makeText(AdminRegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                
                else {


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(AdminRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(AdminRegistrationActivity.this, "Sign-up Error", Toast.LENGTH_SHORT).show();
                            } else {
                                String userID = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("admins").child(userID);
                                current_user_db.setValue(true);

                                Map userInfo = new HashMap();
                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("password", password);

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
                Intent intent = new Intent(AdminRegistrationActivity.this, AdminLoginActivity.class);
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