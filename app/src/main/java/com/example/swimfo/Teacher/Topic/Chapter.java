package com.example.swimfo.Teacher.Topic;

public class Chapter {
    private String chapterId;
    private String chapterTitle;
    private String videoUrl;
    private String chapterDemo;
    private String chapterDesc;

    public Chapter() {
    }

    public Chapter(String chapterId, String chapterTitle, String videoUrl, String chapterDemo, String chapterDesc) {
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.videoUrl = videoUrl;
        this.chapterDemo = chapterDemo;
        this.chapterDesc = chapterDesc;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getChapterDemo() {
        return chapterDemo;
    }

    public void setChapterDemo(String chapterDemo) {
        this.chapterDemo = chapterDemo;
    }

    public String getChapterDesc() {
        return chapterDesc;
    }

    public void setChapterDesc(String chapterDesc) {
        this.chapterDesc = chapterDesc;
    }
}

