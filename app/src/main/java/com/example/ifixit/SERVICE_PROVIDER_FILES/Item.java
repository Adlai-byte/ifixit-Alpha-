package com.example.ifixit.SERVICE_PROVIDER_FILES;

public class Item {
    String name;
    String template;
    String address;
    String userId;


    public Item(String name, String template, String address, String userId) {
        this.name = name;
        this.template = template;
        this.address = address;
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
        ;
    }


}
