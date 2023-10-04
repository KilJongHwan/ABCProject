package com.abc.jdbc;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PostsDAO {
    private final Connection connection;

    public PostsDAO() {
        connection = Common.getConnection();
    }

    // 게시글 작성
    public void addPost(PostsDTO postsDTO) {
        String sql = "INSERT INTO Posts (TITLE, CONTENT, MEMBERSID) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, postsDTO.getTitle());
            preparedStatement.setString(2, postsDTO.getContent());
            preparedStatement.setString(3, postsDTO.getMembersID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("PostsDAO addPost Error! : " + e);
        }
    }

    // 게시글 들어가기
    public PostsDTO enterPost(int postId) {
        String sql = "SELECT * FROM POSTS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                PostsDTO post = new PostsDTO();
                post.setId(resultSet.getString("ID"));
                post.setTitle(resultSet.getString("TITLE"));
                post.setCurrentTime(String.valueOf(resultSet.getTimestamp("CURRENTTIME")));
                post.setContent(resultSet.getString("CONTENT"));
                post.setMembersID(String.valueOf(resultSet.getInt("MEMBERSID")));
                post.setLikesCounts(String.valueOf(resultSet.getInt("LIKESCOUNTS")));
                return post;
            }
        } catch (Exception e) {
            System.out.println("PostsDAO enterPost Error! : " + e);
        }
        return null; // 해당 postId에 해당하는 게시글을 찾지 못한 경우 null 반환
    }



    // 모든 글 조회
    public List<PostsDTO> getAllPosts() {
        List<PostsDTO> postsList = new ArrayList<>();
        String sql = "SELECT * FROM POSTS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PostsDTO post = new PostsDTO();
                post.setId(resultSet.getString("ID"));
                post.setTitle(resultSet.getString("TITLE"));
                post.setMembersID(String.valueOf(resultSet.getInt("MEMBERSID")));
                post.setLikesCounts(String.valueOf(resultSet.getInt("LIKESCOUNTS")));
                postsList.add(post);
            }
        } catch (Exception e) {
            System.out.println("PostsDAO getAllPosts Error! : " + e);
        }
        return postsList;
    }

    // 연결 해제
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("PostDAO close Error! : " + e);
        }
    }

    public MembersDTO getMemberById(String memberId) {
        MembersDTO membersDTO = null;
        String sql = "SELECT * FROM Members WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, memberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 데이터베이스에서 조회한 정보를 MembersDTO 객체에 설정
                    membersDTO = new MembersDTO();
                    membersDTO.setId(resultSet.getString("id"));
                    membersDTO.setPassword(resultSet.getString("password"));
                    membersDTO.setName(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return membersDTO;
    }
    // 게시글 수정
    public void modifyTitle(PostsDTO postsDTO, int postID, String memberID, String newTitle) {
        String sql = "update posts set title = ? WHERE id = ? and membersid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            postsDTO.setId(String.valueOf(postID)); // 현재 선택된 게시글
            postsDTO.setMembersID(memberID); // 현재 로그인한 회원
            postsDTO.setTitle(newTitle);
            preparedStatement.setString(1, postsDTO.getTitle());
            preparedStatement.setString(2, postsDTO.getId());
            preparedStatement.setString(3, postsDTO.getMembersID());
            preparedStatement.executeUpdate();
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("게시글 제목이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("게시글 제목 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            System.out.println("PostsDAO modifyTitle: " + e);
        }
    }
    public void modifyContent(PostsDTO postsDTO, int postID, String memberID, String newContent){
        String sql = "update posts set content = ? WHERE id = ? and membersid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            postsDTO.setId(String.valueOf(postID)); // 현재 선택된 게시글
            postsDTO.setMembersID(memberID); // 현재 로그인한 회원
            postsDTO.setContent(newContent);
            preparedStatement.setString(1, postsDTO.getContent());
            preparedStatement.setString(2, postsDTO.getId());
            preparedStatement.setString(3, postsDTO.getMembersID());
            preparedStatement.executeUpdate();
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("게시글 내용이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("게시글 내용 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            System.out.println("PostsDAO modifyTitle: " + e);
        }
    }
    public PostsDTO getPostById(int postId) {
        String sql = "SELECT * FROM POSTS WHERE ID = ?";
        try (Connection connection = Common.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    PostsDTO post = new PostsDTO();
                    post.setId(resultSet.getString("ID"));
                    post.setTitle(resultSet.getString("TITLE"));
                    post.setContent(resultSet.getString("CONTENT"));
                    post.setMembersID(resultSet.getString("MEMBERSID"));
                    post.setLikesCounts(resultSet.getString("LIKESCOUNTS"));
                    return post;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 게시물이 없는 경우
    }

}