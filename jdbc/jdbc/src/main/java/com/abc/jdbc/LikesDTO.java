package com.abc.jdbc;

import lombok.Getter;

@Getter
public class LikesDTO {
    private String id;
    private String postsId;
    private String membersId;

    public  LikesDTO(){}

    public LikesDTO(String postsId, String membersId) {
        this.postsId = postsId;
        this.membersId = membersId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPostsId(String postId) {
        this.postsId = postId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }
}