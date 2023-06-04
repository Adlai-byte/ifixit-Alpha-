package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PendingRequestItem {
    String serviceProviderUid;
    String name;
    String timestamp;
    String service;
    String total;
    String status;
    String jobtype;

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceProviderUid() {
        return serviceProviderUid;
    }

    public void setServiceProviderUid(String serviceProviderUid) {
        this.serviceProviderUid = serviceProviderUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public PendingRequestItem(String serviceProviderUid, String name, String timestamp, String service, String total,String status,String jobtype) {
        this.jobtype = jobtype;
        this.serviceProviderUid = serviceProviderUid;
        this.name = name;
        this.timestamp = timestamp;
        this.service = service;
        this.total = total;
        this.status = status;
    }
    public String getFormattedTimestamp() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM:dd:yyyy HH:mm:ss", Locale.getDefault());
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
