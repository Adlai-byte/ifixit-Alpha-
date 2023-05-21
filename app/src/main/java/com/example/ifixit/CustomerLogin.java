package com.example.ifixit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ifixit.CUSTOMER_FILES.CustomerLoginActivity;
import com.example.ifixit.ORGANIZATIONAL_FILES.OrgLoginActivity;

public class CustomerLogin extends AppCompatActivity {


    private CardView btnIndividual;
    private CardView btnOrganizational;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);


        btnIndividual = (CardView) findViewById(R.id.btnIndividual);
        btnOrganizational = (CardView) findViewById(R.id.btnOrganization);



        btnOrganizational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerLogin.this, OrgLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerLogin.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}