package com.abc.jdbc.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.abc.jdbc.dto.MembersDTO;
import com.abc.jdbc.util.Animation;
import com.abc.jdbc.util.DatabaseConnection;
import com.abc.jdbc.dto.LikesDTO;
import com.abc.jdbc.dao.MembersDAO;

public class LikesDAO {
    // 연결
    private final Connection connection;
    private final MembersDAO LoginMembersDAO;

    public LikesDAO(MembersDAO membersDAO) {
        connection = DatabaseConnection.getConnection();
        this.LoginMembersDAO = membersDAO;
    }

    // 좋아요 추가
    public void addLike(LikesDTO likesDTO) throws IOException, InterruptedException {
        String sql = "INSERT INTO LIKES (POSTSID, MEMBERSID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, likesDTO.getPostsId());
            preparedStatement.setString(2, likesDTO.getMembersId());
            preparedStatement.executeUpdate();
            Animation.loading();
            System.out.println("좋아요를 눌렀습니다.");
            Animation.waitMoment();
        } catch (SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
            Animation.loading();
            System.out.println("이미 좋아요를 누르셨습니다.\n한 개의 게시글에는 한 개의 좋아요만 누르실 수 있습니다.");
            Animation.waitMoment();
        }
        catch (Exception e){
            System.out.println("LikesDAO addLike Error! : " + e);
        }
    }
}