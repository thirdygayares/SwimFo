package com.example.swimfo.unorganized.Model;

public class SectionModel {
    String id, sectionName;

    public SectionModel() {
    }

    public SectionModel(String id, String sectionName) {
        this.id = id;
        this.sectionName = sectionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
