package com.example.ifixit.CUSTOMER_FILES;

public class ReviewItem {

    String NAME;
    String profileImageUrl;
    String REVIEWS;


    public ReviewItem(String NAME, String profileImageUrl, String REVIEWS) {
        this.NAME = NAME;
        this.profileImageUrl = profileImageUrl;
        this.REVIEWS = REVIEWS;
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

    public String getREVIEWS() {
        return REVIEWS;
    }

    public void setREVIEWS(String REVIEWS) {
        this.REVIEWS = REVIEWS;
    }
}
