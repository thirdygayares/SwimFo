package com.example.swimfo.Teacher.topic;

public class TopicModel {
    String topicId, topicTitle;
    String topicDesc, imageUrl;
    Boolean topicHidden = true;

    public TopicModel() {
    }

    public TopicModel(String topicId, String topicTitle, String topicDesc, String imageUrl, Boolean topicHidden) {
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.topicDesc = topicDesc;
        this.imageUrl = imageUrl;
        this.topicHidden = topicHidden;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getTopicHidden() {
        return topicHidden;
    }

    public void setTopicHidden(Boolean topicHidden) {
        this.topicHidden = topicHidden;
    }
}
