package com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView;

public class UsersItemView {
    String name;
    String address;
    String profileimageurl;
    String phone;
    String email;
    String userid;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsersItemView(String userid,String name, String address, String profileimageurl, String phone, String email) {
        this.name = name;
        this.address = address;
        this.profileimageurl = profileimageurl;
        this.phone = phone;
        this.email = email;
        this.userid = userid;
    }
}
