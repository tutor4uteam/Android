package com.example.aljaz.tutor4u.listViewAllTermins;

import java.util.Date;

public class ModelAllTermins {
    String tutorName;
    Date date;
    String price;
    String idTermin;

    public ModelAllTermins(String tutorName, Date date, String price, String idTermin) {
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

    public Date getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }


}
