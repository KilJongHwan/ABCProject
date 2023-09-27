package com.abc.jdbc;

import lombok.Getter;

import java.sql.Clob;
import java.sql.Date;

@Getter
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

    public void setId(int id) {
        this.id = id;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}