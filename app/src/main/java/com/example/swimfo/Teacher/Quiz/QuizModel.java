package com.example.swimfo.Teacher.Quiz;

import java.util.Map;

public class QuizModel {
    public String createdby;
    public String created;
    public int items;
    public String deadline;
    public Map<String, QuestionModel> questions;

    public QuizModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
    }

    // Getters and setters for each field


    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
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

class QuestionModel {
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
