package com.example.ifixit.Messaging;

public class User {
    private String userId;
    private String username;
    private String email;
    private String profilePictureUrl;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String username, String email, String profilePictureUrl) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
