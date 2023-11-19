package com.example.swimfo.Teacher.model;

public class CoTeacherListModel {
    String name,uid, imgUrl, emailId, tempPassword;

    public CoTeacherListModel() {
    }

    public CoTeacherListModel(String name, String uid, String imgUrl, String emailId, String tempPassword) {
        this.name = name;
        this.uid = uid;
        this.imgUrl = imgUrl;
        this.emailId = emailId;
        this.tempPassword = tempPassword;
    }

    public CoTeacherListModel(String name, String uid) {
        this.name = name;
        this.uid = uid;
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
