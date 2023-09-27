package com.abc.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;


@Component
public class LikesDAO {
    private final Connection connection;
    private final MembersDAO LoginMembersDAO;

    @Autowired
    public LikesDAO(MembersDAO membersDAO) {
        connection = Common.getConnection();
        this.LoginMembersDAO = membersDAO;
    }

    // 좋아요 추가
    public void addLike(LikesDTO likesDTO) {
        String sql = "INSERT INTO LIKES (POSTSID, MEMBERSID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, likesDTO.getPostsId());
            preparedStatement.setString(2, likesDTO.getMembersId());
            preparedStatement.executeUpdate();
            System.out.println("좋아요를 눌렀습니다.");
        } catch (SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
            System.out.println("이미 좋아요를 누르셨습니다!");
        }
        catch (Exception e){
            System.out.println("LikesDAO addLike Error! : " + e);
        }
    }
    // 좋아요 누른 회원 인지 확인
    public boolean hasUserLikedPost(String memberId, String postId) {
        String sql = "SELECT COUNT(*) FROM LIKES WHERE MEMBERSID = ? AND POSTSID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, memberId);
            preparedStatement.setString(2, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // 사용자가 해당 포스트를 이미 좋아요를 눌럿다면 true를 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 좋아요 안눌렀다면 false
    }
}