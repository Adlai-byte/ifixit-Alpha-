package com.example.ifixit.CUSTOMER_FILES;

public class Item {
    String name;
    String address;
    String service;

    public Item(String name, String address, String service) {
        this.name = name;
        this.address = address;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
