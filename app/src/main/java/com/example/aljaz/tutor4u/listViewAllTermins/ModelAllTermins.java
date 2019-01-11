package com.example.aljaz.tutor4u.listViewAllTermins;

public class ModelAllTermins {
    String tutorName;
    String date;
    String price;
    String idTermin;

    public ModelAllTermins(String tutorName, String date, String price, String idTermin) {
        this.tutorName = tutorName;
        this.date = date;
        this.price = price;
        this.idTermin = idTermin;
    }

    public String getIdTermin() {
        return idTermin;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
