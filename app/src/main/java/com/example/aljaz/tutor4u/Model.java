package com.example.aljaz.tutor4u;

public class Model {
    String subjectName;
    String tutorNum;

    public Model(String subjectName, String tutorNum) {
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
