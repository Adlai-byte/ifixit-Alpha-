package com.example.ifixit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderLoginActivity;
import com.example.ifixit.SUPER_ADMIN_FILES.AdminRegistrationActivity;

public class LoginActivity extends AppCompatActivity {


    //Variables
    private CardView btnCustomer;
    private CardView btnServiceProvider;
    private Button btnAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Layout Connecting
        btnCustomer = (CardView) findViewById(R.id.btnCustomer);
        btnServiceProvider = (CardView) findViewById (R.id.btnServiceProvider);
        btnAdmin =(Button)findViewById(R.id.btnAdmin);


        //On Click Listeners
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CustomerLogin.class);
                startActivity(intent);
                return;

            }
        });

        btnServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ServiceProviderLoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        btnAdmin.setVisibility(View.GONE);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this, AdminRegistrationActivity.class);
                startActivity(intent);
                return;
            }
        });



    }
}