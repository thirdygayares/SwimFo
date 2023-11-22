package com.example.swimfo.Teacher.quiz;

public class StudentLeaderBoardModel {

    String displayName;
    String sectionId;
    int score;
    String date;
    String time;

    public StudentLeaderBoardModel() {
    }

    public StudentLeaderBoardModel(String displayName, String sectionId, int score, String date, String time) {
        this.displayName = displayName;
        this.sectionId = sectionId;
        this.score = score;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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
