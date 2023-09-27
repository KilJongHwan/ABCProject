package com.abc.jdbc.dto;

import java.sql.Clob;
import java.sql.Date;

public class CommentsDTO {
    private int id;
    private int postId;
    private int memberId;
    private String commentsText;
    private Date currentTime;

    public  CommentsDTO(){}
    public CommentsDTO(int id, int postId, int memberId, String commentsText, Date currentTime) {
        this.id = id;
        this.postId = postId;
        this.memberId = memberId;
        this.commentsText = commentsText;
        this.currentTime = currentTime;
    }

    public CommentsDTO(int commentId, String commentText, Date currentTime) {
        this.id = commentId;
        this.commentsText = commentText;
        this.currentTime = currentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCommentsText() {
        return commentsText;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
