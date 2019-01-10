package com.example.aljaz.tutor4u.Helpers;

import java.io.Serializable;

public class Student  implements Serializable {
        String id_student;
        String firsname;
        String lastname;
        String street;
        String houseNum;
        String postNum;
        String mail;
        String phoneNum;

    public Student(String id_student, String firsname, String lastname, String street, String houseNum, String postNum, String mail, String phoneNum) {
        this.id_student = id_student;
        this.firsname = firsname;
        this.lastname = lastname;
        this.street = street;
        this.houseNum = houseNum;
        this.postNum = postNum;
        this.mail = mail;
        this.phoneNum = phoneNum;
    }

    public String getId_student() {
        return id_student;
    }

    public void setId_student(String id_student) {
        this.id_student = id_student;
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
