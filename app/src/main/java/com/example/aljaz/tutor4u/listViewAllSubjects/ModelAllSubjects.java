package com.example.aljaz.tutor4u.listViewAllSubjects;

public class ModelAllSubjects {
    String subjectName;
    String tutorNum;

    public ModelAllSubjects(String subjectName, String tutorNum) {
        this.subjectName = subjectName;
        this.tutorNum = tutorNum;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public String getTutorNum() {
        return this.tutorNum;
    }

}
