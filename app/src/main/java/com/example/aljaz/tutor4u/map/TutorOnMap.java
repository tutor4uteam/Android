package com.example.aljaz.tutor4u.map;

public class TutorOnMap {
    String id, name, surname, address, mail, phone, grade;

    public TutorOnMap(String id, String name, String surname, String address, String mail, String phone, String grade) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.mail = mail;
        this.phone = phone;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getGrade() {
        return grade;
    }
}
