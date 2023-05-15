package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.NotificationViewClasses;

public class NotificationViewItem {
    String userid;
    String name;
    String profileimageurl;
    String service;
    float rating;

    public float getRating() {
        return rating;
    }

    public String getReviews() {
        return reviews;
    }

    String reviews;

    public NotificationViewItem(String user, String name, String profileimageurl, String service) {
        this.userid = user;
        this.name = name;
        this.profileimageurl = profileimageurl;
        this.service = service;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }




}
