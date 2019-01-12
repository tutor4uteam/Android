package com.example.aljaz.tutor4u.Helpers;

import java.io.Serializable;

public class Subject  implements Serializable {
    String id_subject;
    String subject;

    public Subject(String id_subject, String subject) {
        this.id_subject = id_subject;
        this.subject = subject;
    }

    public String getId_subject() {
        return id_subject;
    }

    public void setId_subject(String id_subject) {
        this.id_subject = id_subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject;
    }
}
