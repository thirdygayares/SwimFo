package com.example.swimfo;

public class Modelclass {

    String Question;
    String C1;
    String C2;
    String C3;
    String C4;
    String category;
    String ans;
    String key;


    public Modelclass(){

    }

    public Modelclass(String question, String c1, String c2, String c3, String c4, String category, String ans, String key) {
        Question = question;
        C1 = c1;
        C2 = c2;
        C3 = c3;
        C4 = c4;
        this.category = category;
        this.ans = ans;
        this.key = key;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getC1() {
        return C1;
    }

    public void setC1(String c1) {
        C1 = c1;
    }

    public String getC2() {
        return C2;
    }

    public void setC2(String c2) {
        C2 = c2;
    }

    public String getC3() {
        return C3;
    }

    public void setC3(String c3) {
        C3 = c3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getC4() {
        return C4;
    }

    public void setC4(String c4) {
        C4 = c4;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
