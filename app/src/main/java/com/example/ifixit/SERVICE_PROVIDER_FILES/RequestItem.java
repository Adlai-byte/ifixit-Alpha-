package com.example.ifixit.SERVICE_PROVIDER_FILES;

public class RequestItem {
    String NAME;
    String profileImageUrl;
    String ADDRESS;
    String USERID;
    String EMAIL;
    String JOBTYPE;
    String TOTALPRICE;
    String LOCATION;




    public RequestItem(String NAME, String address, String USERID, String profileImageUrl, String EMAIL, String JOBTYPE, String TOTALPRICE, String LOCATION) {
        this.NAME = NAME;
        this.ADDRESS = address;
        this.USERID = USERID;
        this.profileImageUrl = profileImageUrl;
        this.EMAIL = EMAIL;
        this.JOBTYPE = JOBTYPE;
        this.TOTALPRICE = TOTALPRICE;
        this.LOCATION = LOCATION;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
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
    public String getJOBTYPE() {
        return JOBTYPE;
    }

    public void setJOBTYPE(String JOBTYPE) {
        this.JOBTYPE = JOBTYPE;
    }

    public String getTOTALPRICE() {
        return TOTALPRICE;
    }

    public void setTOTALPRICE(String TOTALPRICE) {
        this.TOTALPRICE = TOTALPRICE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }
}
