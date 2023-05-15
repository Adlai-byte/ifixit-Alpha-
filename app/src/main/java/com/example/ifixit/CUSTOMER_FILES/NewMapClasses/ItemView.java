package com.example.ifixit.CUSTOMER_FILES.NewMapClasses;

public class ItemView {

    String userid;
    String name;
    String profileimageurl;
    String service;

    String rating;
    public ItemView(String userid, String name, String profileimageurl, String service, String rating) {
        this.userid = userid;
        this.name = name;
        this.profileimageurl = profileimageurl;
        this.service = service;
        this.rating = rating;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
