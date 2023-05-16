package com.example.ifixit.Messaging;

public class ChatMessage {
    private String imageurl;
    private String sender;
    private String message;
    private long timestamp;

    public String getImageurl() {
        return imageurl;
    }

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String sender, String message, long timestamp, String imageurl) {
        this.imageurl =imageurl;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
