package com.example.aljaz.tutor4u.listViewStudentTerms;

import java.io.Serializable;
import java.util.Date;

public class ModelStudentTerm implements Serializable {
    String tutorId;
    String terminId;
    String tutorName;
    String tutorAddress;
    String subject;
    Date date;

    public ModelStudentTerm(String tutorId, String terminId, String tutorName, String tutorAddress, String subject, Date date) {
        this.tutorId = tutorId;
        this.terminId = terminId;
        this.tutorName = tutorName;
        this.tutorAddress = tutorAddress;
        this.subject = subject;
        this.date = date;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getTerminId() {
        return terminId;
    }

    public void setTerminId(String terminId) {
        this.terminId = terminId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorAddress() {
        return tutorAddress;
    }

    public void setTutorAddress(String tutorAddress) {
        this.tutorAddress = tutorAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
