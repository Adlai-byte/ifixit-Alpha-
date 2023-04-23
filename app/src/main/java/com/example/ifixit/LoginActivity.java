package com.example.ifixit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ifixit.CUSTOMER_FILES.CustomerLoginActivity;
import com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderLoginActivity;

public class LoginActivity extends AppCompatActivity {


    //Variables
    private Button btnCustomer;
    private Button btnServiceProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Layout Connecting
        btnCustomer = (Button) findViewById(R.id.btnCustomer);
        btnServiceProvider = (Button) findViewById (R.id.btnServiceProvider);

        //On Click Listeners
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CustomerLoginActivity.class);
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

    }
}