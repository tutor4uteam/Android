package com.example.aljaz.tutor4u.listViewTutorTerms.freeTerms;

import java.io.Serializable;
import java.util.Date;

public class ModelFreeTerms implements Serializable {
    Date date;
    String idTermin;
    String price;
    String subject;

    public ModelFreeTerms(Date date, String idTermin, String price, String subject) {
        this.date = date;
        this.idTermin = idTermin;
        this.price = price;
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public String getIdTermin() {
        return idTermin;
    }

    public String getPrice() {
        return price;
    }

    public String getSubject() {
        return subject;
    }
}
