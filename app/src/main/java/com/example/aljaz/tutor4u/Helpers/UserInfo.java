package com.example.aljaz.tutor4u.Helpers;

import java.io.Serializable;

public class UserInfo implements Serializable {

    String user_id;
    String firsname;
    String lastname;
    String street;
    String houseNum;
    String postNum;
    String mail;
    String phoneNum;
    String role;

    public UserInfo(String user_id, String firsname, String lastname, String street, String houseNum, String postNum, String mail, String phoneNum, String role) {
        this.user_id = user_id;
        this.firsname = firsname;
        this.lastname = lastname;
        this.street = street;
        this.houseNum = houseNum;
        this.postNum = postNum;
        this.mail = mail;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirsname() {
        return firsname;
    }

    public void setFirsname(String firsname) {
        this.firsname = firsname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
