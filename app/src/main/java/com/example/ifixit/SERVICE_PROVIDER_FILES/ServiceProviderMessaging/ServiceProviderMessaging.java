package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ifixit.R;
import com.google.firebase.FirebaseApp;

public class ServiceProviderMessaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_messaging);


        FirebaseApp.initializeApp(this);



    }
}