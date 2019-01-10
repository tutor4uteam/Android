package com.example.aljaz.tutor4u.listViewAllTutors;

public class ModelAllTutors {
    String profile_img;
    String profile_name;
    String profile_surname;
    String profile_address;
    String profile_mail;
    String profile_phone;
    String profile_grade;

    public ModelAllTutors(String profile_img, String profile_name, String profile_surname, String profile_address, String profile_mail, String profile_phone, String profile_grade) {
        this.profile_img = profile_img;
        this.profile_name = profile_name;
        this.profile_surname = profile_surname;
        this.profile_address = profile_address;
        this.profile_mail = profile_mail;
        this.profile_phone = profile_phone;
        this.profile_grade = profile_grade;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public String getProfile_surname() {
        return profile_surname;
    }

    public String getProfile_address() {
        return profile_address;
    }

    public String getProfile_mail() {
        return profile_mail;
    }

    public String getProfile_phone() {
        return profile_phone;
    }

    public String getProfile_grade() {
        return profile_grade;
    }
}
