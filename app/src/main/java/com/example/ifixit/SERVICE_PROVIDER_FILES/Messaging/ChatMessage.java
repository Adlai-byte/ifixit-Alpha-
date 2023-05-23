package com.example.ifixit.SERVICE_PROVIDER_FILES.Messaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    public static final int TYPE_OUTGOING = 1;
    public static final int TYPE_INCOMING = 2;


    private String currentUserUid;
    private String message;
    private String timestamp;
    private boolean isOutgoing;

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    private String senderName;

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public ChatMessage() {
    }

    public ChatMessage(String message, String timestamp, boolean isOutgoing,String senderName,String currentUserUid) {
        this.message = message;
        this.timestamp = timestamp;
        this.isOutgoing = isOutgoing;
        this.senderName = senderName;
        this.currentUserUid = currentUserUid;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public String getFormattedTimestamp() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
