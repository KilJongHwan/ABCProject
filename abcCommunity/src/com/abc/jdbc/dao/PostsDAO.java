package com.abc.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.abc.jdbc.main.MainApplication;
import com.abc.jdbc.util.Animation;
import com.abc.jdbc.util.DatabaseConnection;
import com.abc.jdbc.dto.PostsDTO;

public class PostsDAO {
    private final Connection connection;

    public PostsDAO() {
        connection = DatabaseConnection.getConnection();
    }

    Scanner sc = new Scanner(System.in);

    // 게시글 작성
    public void addPost(PostsDTO postsDTO) {
        String sql = "INSERT INTO Posts (TITLE, CONTENT, MEMBERSID) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, postsDTO.getTitle());
            preparedStatement.setString(2, postsDTO.getContent());
            preparedStatement.setString(3, postsDTO.getMembersID());
            preparedStatement.executeUpdate();
            Animation.loading();
            System.out.println("새로운 게시글이 작성되었습니다.");
            Animation.waitMoment();
        } catch (Exception e) {
            System.out.println("PostsDAO addPost Error! : " + e);
        }
    }

    // 게시글 수정
    public void modifyPost(PostsDTO postsDTO, int postID, String memberID) {
        System.out.print("[1] 제목 수정, [2] 내용 수정 : ");
        int choice = sc.nextInt();
        if (choice == 1) { // 제목 수정
            String sql = "update posts set title = ? WHERE id = ? and membersid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                postsDTO.setId(String.valueOf(postID)); // 현재 선택된 게시글
                postsDTO.setMembersID(memberID); // 현재 로그인한 회원
                System.out.print("새로운 게시글 제목을 입력해주세요 : ");
                sc.nextLine();
                String newTitle = sc.nextLine();
                postsDTO.setTitle(newTitle);
                preparedStatement.setString(1, postsDTO.getTitle());
                preparedStatement.setString(2, postsDTO.getId());
                preparedStatement.setString(3, postsDTO.getMembersID());
                preparedStatement.executeUpdate();
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    Animation.loading();
                    System.out.println("게시글 제목이 성공적으로 수정되었습니다.");
                    Animation.waitMoment();
                } else {
                    Animation.loading();
                    System.out.println("게시글 제목 수정에 실패했습니다.\n작성자를 확인해주세요.\n본인이 작성하신 글만을 수정할 수 있습니다.");
                    Animation.waitMoment();
                }
            } catch (Exception e) {
                System.out.println("PostsDAO modifyTitle: " + e);
            }
        } else if (choice == 2) { // 내용 수정
            String sql = "update posts set content = ? WHERE id = ? and membersid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                postsDTO.setId(String.valueOf(postID)); // 현재 선택된 게시글
                postsDTO.setMembersID(memberID); // 현재 로그인한 회원
                System.out.print("새로운 게시글 내용을 입력해주세요 : ");
                sc.nextLine();
                String newContent = sc.nextLine();
                postsDTO.setContent(newContent);
                preparedStatement.setString(1, postsDTO.getContent());
                preparedStatement.setString(2, postsDTO.getId());
                preparedStatement.setString(3, postsDTO.getMembersID());
                preparedStatement.executeUpdate();
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    Animation.loading();
                    System.out.println("게시글 내용이 성공적으로 수정되었습니다.");
                    Animation.waitMoment();
                } else {
                    Animation.loading();
                    System.out.println("게시글 제목 내용 수정에 실패했습니다.\n작성자를 확인해주세요.\n본인이 작성하신 글만을 수정할 수 있습니다.");
                    Animation.waitMoment();
                }
            } catch (Exception e) {
                System.out.println("PostsDAO modifyTitle: " + e);
            }

        } else {
            System.out.println("올바른 번호를 입력해주세요.");
        }
    }

    // 게시글 삭제
    public void deletePost(int postID, String memberID) {
        String sql = "DELETE FROM POSTS WHERE ID = ? AND MEMBERSID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postID); // 현재 선택된 게시글
            preparedStatement.setString(2, memberID); // 현재 로그인한 회원
            int rowsDeleted = preparedStatement.executeUpdate();
            //삭제된 행의 개수를 출력
            if (rowsDeleted == 0) {
                Animation.loading();
                System.out.println("게시글이 삭제되지 않았습니다.\n본인이 작성하신 게시글이 맞는지 확인해주세요.\n");
                Animation.waitMoment();
            } else {
                Animation.loading();
                System.out.println("게시글 삭제에 성공하셨습니다");
                Animation.waitMoment();
            }
        } catch (Exception e) {
            System.out.println("PostsDAO deletePost : " + e);
        }
    }


    // 모든 게시글 조회
    public List<PostsDTO> getAllPosts() {
        List<PostsDTO> postsList = new ArrayList<>();
        String sql = "SELECT * FROM POSTS ORDER BY CURRENTTIME ASC";
        //String sql = "SELECT * FROM POSTS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PostsDTO post = new PostsDTO();
                post.setId(resultSet.getString("ID"));
                post.setTitle(resultSet.getString("TITLE"));
                post.setCurrentTime(String.valueOf(resultSet.getTimestamp("CURRENTTIME")));
                post.setContent(resultSet.getString("CONTENT"));
                post.setMembersID(String.valueOf(resultSet.getInt("MEMBERSID")));
                //post.setName(String.valueOf(resultSet.getInt("NAME")));
                post.setLikesCounts(String.valueOf(resultSet.getInt("LIKESCOUNTS")));
                postsList.add(post);
            }
        } catch (Exception e) {
            System.out.println("PostsDAO getAllPosts Error! : " + e);
        }
        return postsList;
    }


    // 게시글의 작성자만 확인하는 함수
    public String postAuthorName(int memberId, int postsId) {
        String authorName = null; // 결과를 저장할 변수

        String sql = "SELECT NAME FROM MEMBERS WHERE ID = (SELECT MEMBERSID FROM POSTS WHERE ID = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postsId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과가 하나의 행만 반환되는 경우 (예: 이름 하나)
            if (resultSet.next()) {
                authorName = resultSet.getString("NAME");
            }
        } catch (Exception e) {
            System.out.println("PostsDAO postAuthorName Error! : " + e);
        }

        // authorName에 결과가 없는 경우 "dd"를 반환하고, 결과가 있는 경우 결과값 반환
        return (authorName != null) ? authorName : "회원 탈퇴";
    }

    // 게시글 들어가기
    public List<PostsDTO> enterPost(int postId) {
        List<PostsDTO> postsList = new ArrayList<>();
        String sql = "SELECT * FROM POSTS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PostsDTO post = new PostsDTO();
                post.setId(resultSet.getString("ID"));
                post.setTitle(resultSet.getString("TITLE"));
                post.setName(resultSet.getString("NAME"));
                post.setCurrentTime(String.valueOf(resultSet.getTimestamp("CURRENTTIME")));
                post.setContent(resultSet.getString("CONTENT"));
                post.setMembersID(String.valueOf(resultSet.getInt("MEMBERSID")));
                post.setLikesCounts(String.valueOf(resultSet.getInt("LIKESCOUNTS")));
                postsList.add(post);
            }
        } catch (Exception e) {
            System.out.println("PostsDAO enterPost Error! : " + e);
        }
        return postsList;
    }
}