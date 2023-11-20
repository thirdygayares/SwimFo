package com.example.swimfo.unorganized.Model;

public class QuizModel {
    private String quizNumber;

    public QuizModel() {
        // Default constructor required for Firebase
    }

    public QuizModel(String quizNumber) {
        this.quizNumber = quizNumber;
    }

    public String getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(String quizNumber) {
        this.quizNumber = quizNumber;
    }
}
