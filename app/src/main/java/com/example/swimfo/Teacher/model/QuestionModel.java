package com.example.swimfo.Teacher.model;

import java.util.Map;

public class QuestionModel {
    public String type;
    public String question;
    public Map<String, String> options;
    public String correct;

    public QuestionModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Question.class)
    }

    // Getters and setters for each field


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }
}
