package com.example.ifixit.CUSTOMER_FILES;

public class ServiceProvider {
    private String userId;
    private String name;
    private String email;
    private String address;
    private String lat;
    private String lng;

    public ServiceProvider(String userId, String name, String email, String address, String lat, String lng) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}





