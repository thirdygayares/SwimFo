package com.example.swimfo.Student.model;

import java.util.List;

public class QuestionQuizPreviewModel {
    public enum QuestionType { MULTIPLE_CHOICE, IDENTIFICATION }
    String questionId;
    QuestionType type;
    int questionNumber;
    String questionText;
    List<String> options;
    String answer;
    // Constructor, getters, and setters


    public QuestionQuizPreviewModel() {
    }

    public QuestionQuizPreviewModel(String questionId, QuestionType type, String questionText, List<String> options, String answer) {
        this.questionId = questionId;
        this.type = type;
        this.questionText = questionText;
        this.options = options;
        this.answer = answer;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
