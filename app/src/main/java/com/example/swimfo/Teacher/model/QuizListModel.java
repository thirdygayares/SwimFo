package com.example.swimfo.Teacher.model;

public class QuizListModel {


    public String quizname;
    public Object created;
    public int items;
    public String deadline;


    public QuizListModel() {
    }

    public QuizListModel(String quizname, int items, String deadline) {
        this.quizname = quizname;
        this.items = items;
        this.deadline = deadline;
    }

    public String getName() {
        return quizname;
    }

    public Object getCreated() {
        return created;
    }

    public int getItems() {
        return items;
    }

    public String getDeadline() {
        return deadline;
    }
}
