package com.example.swimfo.Teacher.Topic;

import com.example.swimfo.Chapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lesson {
    private String lessonId;
    private String topicId;
    private String lessonName;
//    private HashMap<String,Chapter> chapters;
    private List<Chapter> chapters;

//    public Lesson() {
//        // Default constructor required for Firebase
//    }
public Lesson() {
    chapters = new ArrayList<>(); // Initialize the chapters list
}

//    public Lesson(String lessonId, String topicId, String lessonName, HashMap<String, Chapter> chapters) {
//        this.lessonId = lessonId;
//        this.topicId = topicId;
//        this.lessonName = lessonName;
//        this.chapters = chapters;
//    }


    public Lesson(String lessonId, String topicId, String lessonName, List<Chapter> chapters) {
        this.lessonId = lessonId;
        this.topicId = topicId;
        this.lessonName = lessonName;
        this.chapters = chapters;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

//    public HashMap<String, Chapter> getChapters() {
//        return chapters;
//    }
//
//    public void setChapters(HashMap<String, Chapter> chapters) {
//        this.chapters = chapters;
//    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}

