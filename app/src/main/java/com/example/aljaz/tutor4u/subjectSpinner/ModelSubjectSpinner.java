package com.example.aljaz.tutor4u.subjectSpinner;

public class ModelSubjectSpinner {
    String id;
    String subjectName;
    String price;

    public ModelSubjectSpinner(String id, String subjectName, String price) {
        this.id = id;
        this.subjectName = subjectName;
        this.price = price;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return subjectName + " (" + price + "€)";
    }
}
