package com.example.ifixit.SUPER_ADMIN_FILES.RecyclerView;

public class AdminItemView {
    String USERID, NAME,ADDRESS,JOBTYPE,profileImageUrl,EMAIL;
    String RATE;

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public AdminItemView(String USERID, String NAME, String ADDRESS, String EMAIL, String JOBTYPE, String profileImageUrl, String RATE) {
        this.USERID = USERID;
        this.NAME = NAME;
        this.ADDRESS = ADDRESS;
        this.JOBTYPE = JOBTYPE;
        this.profileImageUrl = profileImageUrl;
        this.RATE = RATE;
        this.EMAIL = EMAIL;
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

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getJOBTYPE() {
        return JOBTYPE;
    }

    public void setJOBTYPE(String JOBTYPE) {
        this.JOBTYPE = JOBTYPE;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRATE() {
        return RATE;
    }

    public void setRATE(String RATE) {
        this.RATE = RATE;
    }
}
