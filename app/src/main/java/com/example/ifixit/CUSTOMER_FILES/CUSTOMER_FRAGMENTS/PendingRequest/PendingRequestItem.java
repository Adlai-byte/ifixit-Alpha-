package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.PendingRequest;

public class PendingRequestItem {

    String key;
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

    public String getKey() {
        return key;
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

    public PendingRequestItem(String serviceProviderUid, String name, String timestamp, String service, String total,String status,String jobtype,String key) {
        this.key= key;
        this.jobtype = jobtype;
        this.serviceProviderUid = serviceProviderUid;
        this.name = name;
        this.timestamp = timestamp;
        this.service = service;
        this.total = total;
        this.status = status;
    }

}
