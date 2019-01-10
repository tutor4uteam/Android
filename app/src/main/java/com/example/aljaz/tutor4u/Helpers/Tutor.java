package com.example.aljaz.tutor4u.Helpers;

import java.io.Serializable;

public class Tutor  implements Serializable {
    String id_tutor;
    String firsname;
    String lastname;
    String street;
    String houseNum;
    String postNum;
    String mail;
    String phoneNum;

    public Tutor(String id_tutor, String firsname, String lastname, String street, String houseNum, String postNum, String mail, String phoneNum) {
        this.id_tutor = id_tutor;
        this.firsname = firsname;
        this.lastname = lastname;
        this.street = street;
        this.houseNum = houseNum;
        this.postNum = postNum;
        this.mail = mail;
        this.phoneNum = phoneNum;
    }

    public String getId_tutor() {
        return id_tutor;
    }

    public void setId_tutor(String id_tutor) {
        this.id_tutor = id_tutor;
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
