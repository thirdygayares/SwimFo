package com.example.swimfo.unorganized.Model;

public class LeaderBoardModel {

    String displayName;
    String sectionId;
    String score;
    String date;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(String displayName, String sectionId, String score, String date) {
        this.displayName = displayName;
        this.sectionId = sectionId;
        this.score = score;
        this.date = date;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
