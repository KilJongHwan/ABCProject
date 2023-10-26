package com.abc.jdbc.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.abc.jdbc.dto.MembersDTO;
import com.abc.jdbc.main.MainApplication;
import com.abc.jdbc.util.Animation;
import com.abc.jdbc.util.DatabaseConnection;
import com.abc.jdbc.dto.CommentsDTO;

public class CommentsDAO {
    // 연결
    private final Connection connection;
    Scanner sc = new Scanner(System.in);

    public CommentsDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // 댓글 달기
    public void addComment(CommentsDTO commentsDTO) {
        String sql = "INSERT INTO COMMENTS (POSTSID, MEMBERSID, COMMENTSTEXT) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, commentsDTO.getPostsId());
            preparedStatement.setString(2, commentsDTO.getMembersId());
            preparedStatement.setString(3, commentsDTO.getCommentsText());
            preparedStatement.executeUpdate();
            Animation.loading();
            System.out.println("댓글을 작성했습니다.");
            Animation.waitMoment();
        } catch (Exception e) {
            System.out.println("CommentsDAO addComment Error! : " + e);
        }
    }

    // 댓글 보기
    public List<CommentsDTO> getCommentsByPostId(CommentsDTO commentsDTO) {
        List<CommentsDTO> commentsList = new ArrayList<>();
        String sql = "SELECT ID, NAME, COMMENTSTEXT, COMMENTSTIME FROM COMMENTS WHERE POSTSID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, commentsDTO.getPostsId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CommentsDTO comment = new CommentsDTO();
                comment.setId(resultSet.getString("ID"));
                comment.setName(resultSet.getString("NAME"));
                comment.setCommentsText(resultSet.getString("COMMENTSTEXT"));
                comment.setCommentsTime(String.valueOf(resultSet.getTimestamp("COMMENTSTIME")));
                commentsList.add(comment);
            }

            if (commentsList.isEmpty()) {
                System.out.println(commentsDTO.getPostsId() + "번 게시글에 댓글이 없습니다.");
                Animation.waitMoment();
            } else {
                System.out.println("<" + commentsDTO.getPostsId() + "번 게시글의 댓글 목록>");
                for (CommentsDTO comment : commentsList) {
                    System.out.println("댓글 번호 : " + comment.getId());
                    System.out.println("댓글 작성자 : " + comment.getName());
                    System.out.println("댓글 내용: " + comment.getCommentsText());
                    System.out.println("댓글 시간: " + comment.getCommentsTime());
                    System.out.println();
                }
                Animation.waitMoment();
            }
        } catch (SQLException e) {
            System.out.println("SQL 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
        return commentsList;
    }

    // 댓글 수정
    public void commentModify(MembersDTO membersDTO, int postid) {
        String sql = "UPDATE COMMENTS SET COMMENTSTEXT = ? WHERE ID = ? AND MEMBERSID = ? AND POSTSID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            System.out.print("수정할 댓글 번호를 선택하세요: ");
            int modCommentId;
            try {
                modCommentId = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("올바른 숫자를 입력해주세요.\n해당 번호의 댓글은 존재하지 않습니다.");

                return;
            }
            sc.nextLine(); // 버퍼 비우기

            // 댓글이 존재하는지 확인
            if (!isCommentExists(modCommentId)) {
                System.out.println("해당 댓글이 존재하지 않습니다.");
                return;
            }

            // 댓글 작성자의 MembersID를 호출
            int commentAuthorId = getCommentAuthorId(modCommentId);
            // 댓글 작성자와 현재 로그인한 사용자를 비교
            if (commentAuthorId != Integer.parseInt(membersDTO.getId())) {
                System.out.println("본인이 작성한 댓글만 수정할 수 있습니다.");
                return; // 다른 작성자의 댓글을 수정하려고 시도한 경우
            }

            int commentPostId = getCommentPostId(modCommentId);
            if (commentPostId != postid) {
                System.out.println("해당 게시글에는 해당 댓글이 존재하지 않습니다.");
                Animation.waitMoment();
                return;
            }

            System.out.print("수정할 내용을 입력하세요: ");
            String modComment = sc.nextLine();
            preparedStatement.setString(1, modComment);
            preparedStatement.setInt(2, modCommentId);
            preparedStatement.setInt(3, commentAuthorId);
            preparedStatement.setInt(4, commentPostId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Animation.loading();
                System.out.println("댓글이 성공적으로 수정되었습니다.");
                Animation.waitMoment();
            } else {
                Animation.loading();
                System.out.println("댓글 수정에 실패했습니다.");
                Animation.waitMoment();
            }
        } catch (Exception e) {
            System.out.println("CommentDAO commentModify: " + e);
        }
    }


    // 댓글이 존재하는지 확인하는 메서드
    private boolean isCommentExists(int commentId) {
        String sql = "SELECT 1 FROM COMMENTS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println("Error in isCommentExists: " + e);
            return false;
        }
    }


    // 댓글 작성자의 MembersID를 호출하는 함수(댓글 수정 함수에서 사용)
    private int getCommentAuthorId(int commentId) {
        String sql = "SELECT MEMBERSID FROM COMMENTS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("MEMBERSID");
            }
        } catch (Exception e) {
            System.out.println("Error in getCommentAuthorId: " + e);
        }
        return -1; // 작성자 ID를 찾지 못한 경우 -1을 반환
    }

    // 댓글이 달린 게시글의 PostsId를 호출하는 함수(댓글 수정 함수에서 사용)
    private int getCommentPostId(int commentId){
        String sql = "SELECT POSTSID FROM COMMENTS WHERE ID=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("POSTSID");
            }
        } catch (Exception e) {
            System.out.println("Error in getCommentPostId: " + e);
        }
        return -1; // 작성자 ID를 찾지 못한 경우 -1을 반환
    }

    // 댓글 삭제
    public void commentDelete(CommentsDTO commentsDTO){
        String sql = "DELETE FROM COMMENTS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            System.out.print("삭제할 댓글 번호를 입력해주세요 : ");
            int id =  sc.nextInt();
            commentsDTO.setId(String.valueOf(id));
            preparedStatement.setString(1, commentsDTO.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("해당 번호의 댓글이 존재하지 않습니다.");
            } else {
                Animation.loading();
                System.out.println("댓글이 성공적으로 삭제되었습니다.");
                Animation.waitMoment();
            }
        } catch (Exception e) {
            System.out.println("CommentDAO commentDelete : " + e);
        }
    }

    // 출력문을 호출하는 메서드 추가
    public void printComments(List<CommentsDTO> commentsList) throws IOException, InterruptedException {
        if (commentsList.isEmpty()) {
            System.out.println("댓글이 없습니다.");
            Animation.waitMoment();

        } else {
            System.out.println("<댓글 목록>");
            for (CommentsDTO comment : commentsList) {
                System.out.println("댓글 번호 : " + comment.getId());
                System.out.println("댓글 작성자 : " + comment.getName());
                System.out.println("댓글 내용: " + comment.getCommentsText());
                System.out.println("댓글 시간: " + comment.getCommentsTime());
                System.out.println();
            }
        }
    }
}