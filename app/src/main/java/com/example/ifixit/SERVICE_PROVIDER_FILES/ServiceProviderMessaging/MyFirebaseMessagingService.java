package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG,"From: "+message.getFrom());
        if(message.getData().size()>0){
            Log.d(TAG,"Message data payload: "+message.getData());
        }
        if(message.getNotification()!=null){
            Log.d(TAG, "Message Notification Body: "+message.getNotification().getBody());
        }
    }
}
