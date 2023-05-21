package com.example.ifixit.ORGANIZATIONAL_FILES.JOB_POSTING;

public class JobPostingItemView {
    String userid;
    String name;
    String serviceType;
    String maxPrice;
    String minPrice;
    String orgName;
    String address;
    String duration;
    String description;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobPostingItemView(String userid, String name, String serviceType, String maxPrice, String minPrice, String address, String duration, String description,String orgName) {
        this.userid = userid;
        this.name = name;
        this.serviceType = serviceType;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.address = address;
        this.duration = duration;
        this.description = description;
        this.orgName = orgName;
    }



}
