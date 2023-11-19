package com.example.swimfo.Teacher.model;

import java.sql.Timestamp;
import java.util.Map;

public class QuizModel {
    public String createdby;
    public Object  created;
    public String  quizname;

    public int items;
    public String deadline;
    public Map<String, QuestionModel> questions;

    public QuizModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
    }

    // Getters and setters for each field

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Map<String, QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, QuestionModel> questions) {
        this.questions = questions;
    }
}

