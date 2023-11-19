package com.example.swimfo.Model;

public class StudentModel {
    String name,uid,section,sectionId, imgUrl, emailId, tempPassword;

    public StudentModel() {
    }

    public StudentModel(String name, String uid, String section, String sectionId, String imgUrl, String emailId, String tempPassword) {
        this.name = name;
        this.uid = uid;
        this.section = section;
        this.sectionId = sectionId;
        this.imgUrl = imgUrl;
        this.emailId = emailId;
        this.tempPassword = tempPassword;
    }

    public StudentModel(String name, String uid, String section, String sectionId) {
        this.name = name;
        this.uid = uid;
        this.section = section;
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }
}
