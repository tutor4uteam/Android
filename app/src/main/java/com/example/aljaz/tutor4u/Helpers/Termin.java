package com.example.aljaz.tutor4u.Helpers;

public class Termin {
    String id_termin;
    String id_student;
    String id_tutor;
    String id_subject;
    String reserved;
    String grade;
    String date;
    String price;

    public Termin(String id_termin, String id_student, String id_tutor, String id_subject, String reserved, String grade, String date, String price) {
        this.id_termin = id_termin;
        this.id_student = id_student;
        this.id_tutor = id_tutor;
        this.id_subject = id_subject;
        this.reserved = reserved;
        this.grade = grade;
        this.date = date;
        this.price = price;
    }

    public String getId_termin() {
        return id_termin;
    }

    public void setId_termin(String id_termin) {
        this.id_termin = id_termin;
    }

    public String getId_student() {
        return id_student;
    }

    public void setId_student(String id_student) {
        this.id_student = id_student;
    }

    public String getId_tutor() {
        return id_tutor;
    }

    public void setId_tutor(String id_tutor) {
        this.id_tutor = id_tutor;
    }

    public String getId_subject() {
        return id_subject;
    }

    public void setId_subject(String id_subject) {
        this.id_subject = id_subject;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
