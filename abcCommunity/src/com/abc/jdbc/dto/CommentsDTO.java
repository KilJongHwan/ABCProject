package com.abc.jdbc.dto;

public class CommentsDTO {
    private String id;
    private String postsId;
    private String membersId;
    private String commentsText;
    private String commentsTime;
    private String name;

    public CommentsDTO() {
    }


    public CommentsDTO(String postsId, String membersId, String commentsText, String name) {
        this.postsId = postsId;
        this.membersId = membersId;
        this.commentsText = commentsText;
        this.name = name;
    }

    public CommentsDTO(String postsId, String membersId) {
        this.postsId = postsId;
        this.membersId = membersId;
    }

    public String getId() {
        return id;
    }

    public String getPostsId() {
        return postsId;
    }

    public String getMembersId() {
        return membersId;
    }

    public String getCommentsText() {
        return commentsText;
    }

    public String getCommentsTime() {
        return commentsTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }

    public void setCommentsTime(String commentsTime) {
        this.commentsTime = commentsTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}