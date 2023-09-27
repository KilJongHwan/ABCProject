package com.abc.jdbc.dao;

import com.abc.jdbc.dto.CommentsDTO;
import com.abc.jdbc.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommentsDAO {
    Connection conn = null;
    Statement st = null;
    PreparedStatement pst = null;
    Scanner sc = new Scanner(System.in);

    public List<CommentsDTO> whatsPost(int postId) {
        List<CommentsDTO> comments = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT C.ID AS COMMENT_ID, C.COMMENTSTEXT, C.COMMENTSTIME FROM COMMENTS C WHERE C.POSTSID = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, postId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int commentId = rs.getInt("COMMENT_ID");
                String commentText = rs.getString("COMMENTSTEXT");
                Date commentTime = rs.getDate("COMMENTSTIME");

                CommentsDTO comment = new CommentsDTO(commentId, commentText, commentTime);
                comments.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }

    public void printCommentsByPostId(int postId, int memberId) {
        List<CommentsDTO> comments = whatsPost(postId);
        String name = "";
        if (comments.isEmpty()) {
            System.out.println("해당 게시글에 댓글이 없습니다.");
        } else {
            try {
                conn = DatabaseConnection.getConnection();
                String sql = "SELECT NAME FROM MEMBERS WHERE ID = ?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, memberId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    name = rs.getString("NAME");
                }
                DatabaseConnection.close(conn);
                DatabaseConnection.close(pst);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("게시글 #" + postId + "의 댓글 목록:");
            for (CommentsDTO comment : comments) {
                System.out.println("댓글 닉네임: " + name);
                System.out.println("댓글 번호: " + comment.getId());
                System.out.println("댓글 내용: " + comment.getCommentsText());
                System.out.println("댓글 작성 시간: " + comment.getCurrentTime());
                System.out.println("-------------");
            }
        }
    }

    //    public void commentModify() {
//        String sql = "UPDATE COMMENTS SET COMMENTSTEXT = ? WHERE ID = ?";
//        try {
//            conn = DatabaseConnection.getConnection();
//            pst = conn.prepareStatement(sql);
//
//            System.out.print("수정할 댓글 번호를 선택하세요: ");
//            int modCommentId = sc.nextInt();
//            System.out.print("수정할 내용을 입력하세요: ");
//            String modComment = sc.next();
//            sc.nextLine();
//            pst.setString(1, modComment);
//            pst.setInt(2, modCommentId);
//            int rowsUpdated = pst.executeUpdate();
//
//            if (rowsUpdated > 0) {
//                System.out.println("댓글이 성공적으로 수정되었습니다.");
//            } else {
//                System.out.println("댓글 수정에 실패했습니다.");
//            }
//        } catch (Exception e) {
//            System.out.println("댓글 수정 오류: " + e);
//        } finally {
//            DatabaseConnection.close(pst);
//            DatabaseConnection.close(conn);
//        }
//    }
    public void commentModify() {
        String sql = "UPDATE COMMENTS SET COMMENTSTEXT = ? WHERE ID = ?";
        conn = DatabaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            System.out.print("수정할 댓글 번호를 선택하세요: ");
            int modCommentId = sc.nextInt();
            sc.nextLine(); // 줄 바꿈 문자 처리

            System.out.println("수정할 내용을 입력하세요 (입력 완료 후 Enter를 누르세요): ");
            StringBuilder modCommentBuilder = new StringBuilder();
            String line;

            while (true) {
                line = sc.nextLine();
                if (line.isEmpty()) {
                    break; // Enter 키 입력 시 루프 탈출
                }
                modCommentBuilder.append(line).append("\n");
            }

            String modComment = modCommentBuilder.toString().trim(); // 입력 내용을 가져오고 공백 제거

            preparedStatement.setString(1, modComment);
            preparedStatement.setInt(2, modCommentId);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("댓글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("댓글 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            System.out.println("CommentDAO commentModify: " + e);
        }
    }



    public List<CommentsDTO> postList() {
        List<CommentsDTO> list = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            st = conn.createStatement();

            // 댓글 데이터 조회 쿼리 작성
            String sql = "SELECT ID, POSTSID, MEMBERSID, COMMENTSTEXT, COMMENTSTIME FROM COMMENTS";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                int postId = rs.getInt("POSTSID");
                int memberId = rs.getInt("MEMBERSID");
                String commentText = rs.getString("COMMENTSTEXT");
                Date commentsTime = rs.getDate("COMMENTSTIME");

                // CommentsDTO 객체 생성 후 리스트에 추가
                CommentsDTO comment = new CommentsDTO(id, postId, memberId, commentText, commentsTime);
                list.add(comment);

                // 콘솔 출력
                System.out.println("ID: " + id + ", PostID: " + postId + ", MemberID: " + memberId + ", Comment: " + commentText + "Time: " + commentsTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(conn);
            DatabaseConnection.close(st);
        }
        return list;
    }

    public void commentWrite(int pid, int mid) {
        try {
            System.out.print("작성할 댓글을 입력하세요: ");
            String comment = sc.nextLine(); // 댓글을 공백을 포함한 전체 라인으로 입력받음

            String w = "INSERT INTO COMMENTS(POSTSID, MEMBERSID, COMMENTSTEXT) VALUES (?, ?, ?)";
            conn = DatabaseConnection.getConnection();
            pst = conn.prepareStatement(w);
            pst.setInt(1, pid);
            pst.setInt(2, mid);
            pst.setString(3, comment);

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("댓글이 성공적으로 작성되었습니다.");
            } else {
                System.out.println("댓글 작성에 실패했습니다.");
            }
        } catch (Exception e) {
            System.err.println("댓글 작성 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(pst);
            DatabaseConnection.close(conn);
        }
    }
}
