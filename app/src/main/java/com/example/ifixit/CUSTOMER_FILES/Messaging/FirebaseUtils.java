package com.example.ifixit.CUSTOMER_FILES.Messaging;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static final String DATABASE_URL = "YOUR_DATABASE_URL";
    private static final String GOOGLE_SERVICES_JSON = "google-services.json";

    public static void initializeFirebase(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl(DATABASE_URL)
                .setProjectId(getFirebaseProjectId())
                .setApiKey(getFirebaseApiKey())
                .setApplicationId(getFirebaseApplicationId())
                .build();

        FirebaseApp.initializeApp(context, options);
    }

    private static String getFirebaseProjectId() {
        // Return your Firebase project ID
        return "YOUR_FIREBASE_PROJECT_ID";
    }

    private static String getFirebaseApiKey() {
        // Return your Firebase API key
        return "YOUR_FIREBASE_API_KEY";
    }

    private static String getFirebaseApplicationId() {
        // Return your Firebase application ID
        return "YOUR_FIREBASE_APPLICATION_ID";
    }

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }
}
