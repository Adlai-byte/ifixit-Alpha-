package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses;

public class OngoingViewItem {
    String USERID;
    String NAME;
    String LOCATION;
    String JOBTYPE;
    String TOTALPRICE;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
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

    public OngoingViewItem(String NAME, String LOCATION, String JOBTYPE, String TOTALPRICE,String USERID) {
        this.NAME = NAME;
        this.LOCATION = LOCATION;
        this.JOBTYPE = JOBTYPE;
        this.TOTALPRICE = TOTALPRICE;
        this.USERID= USERID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}
