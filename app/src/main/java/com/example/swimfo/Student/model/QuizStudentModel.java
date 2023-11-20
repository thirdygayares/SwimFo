package com.example.swimfo.Student.model;

import com.example.swimfo.Teacher.model.QuestionModel;

import java.util.Map;

public class QuizStudentModel {
    public Object  created;
    public String  quizname;

    public int items;
    public String deadline;
    public Map<String, QuestionModel> questions;

    public QuizStudentModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
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
