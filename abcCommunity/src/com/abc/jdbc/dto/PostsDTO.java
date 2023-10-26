package com.abc.jdbc.dto;

public class PostsDTO {
    private String id;
    private String title;
    private String currentTime;
    private String content;
    private String membersID;
    private String likesCounts;
    private String name;

    public PostsDTO(){

    }

    public PostsDTO(String title, String content, String membersID) {
        this.title = title;
        this.content = content;
        this.membersID = membersID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getContent() {
        return content;
    }

    public String getMembersID() {
        return membersID;
    }

    public String getLikesCounts() {
        return likesCounts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMembersID(String membersID) {
        this.membersID = membersID;
    }

    public void setLikesCounts(String likesCounts) {
        this.likesCounts = likesCounts;
    }
}