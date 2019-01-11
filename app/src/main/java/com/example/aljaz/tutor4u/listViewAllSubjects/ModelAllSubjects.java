package com.example.aljaz.tutor4u.listViewAllSubjects;

public class ModelAllSubjects {
    String id_subject;
    String subjectName;
    String tutorNum;

    public ModelAllSubjects(String id_subject, String subjectName, String tutorNum) {
        this.id_subject = id_subject;
        this.subjectName = subjectName;
        this.tutorNum = tutorNum;
    }

    public String getId_subject() {
        return id_subject;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public String getTutorNum() {
        return this.tutorNum;
    }

}
