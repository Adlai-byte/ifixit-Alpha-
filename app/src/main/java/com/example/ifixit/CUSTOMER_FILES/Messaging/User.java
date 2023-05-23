package com.example.ifixit.CUSTOMER_FILES.Messaging;

public class User {
    private String username;
    private String profilePictureUrl;

    public User() {
        // Required default constructor for Firebase
    }

    public User(String username, String profilePictureUrl) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
