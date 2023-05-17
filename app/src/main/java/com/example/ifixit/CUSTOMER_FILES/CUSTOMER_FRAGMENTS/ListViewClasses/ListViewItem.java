package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses;

public class ListViewItem {
    String USERID;
    String NAME;
    String profileImageUrl;
    String SERVICE;
    String ADDRESS;

    public float getMAXPRICE() {
        return MAXPRICE;
    }

    public void setMAXPRICE(float MAXPRICE) {
        this.MAXPRICE = MAXPRICE;
    }

    float MAXPRICE;
    float RATING;

    public ListViewItem(String NAME, String profileImageUrl, String SERVICE, String ADDRESS, float RATING, String USERID,float MAXPRICE) {
        this.NAME = NAME;
        this.MAXPRICE = MAXPRICE;
        this.profileImageUrl = profileImageUrl;
        this.SERVICE = SERVICE;
        this.ADDRESS = ADDRESS;
        this.RATING = RATING;
        this.USERID = USERID;
    }



    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERID() {
        return USERID;
    }

    public float getRATING() {
        return RATING;
    }

    public void setRATING(float RATING) {
        this.RATING = RATING;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public void setSERVICE(String SERVICE) {
        this.SERVICE = SERVICE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }



}
