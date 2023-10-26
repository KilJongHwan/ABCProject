package com.abc.jdbc.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.abc.jdbc.dao.LikesDAO;
import com.abc.jdbc.dao.MembersDAO;
import com.abc.jdbc.dao.PostsDAO;
import com.abc.jdbc.dto.LikesDTO;
import com.abc.jdbc.dto.MembersDTO;
import com.abc.jdbc.dto.PostsDTO;
import com.abc.jdbc.dao.CommentsDAO;
import com.abc.jdbc.dto.CommentsDTO;
import com.abc.jdbc.util.Animation;
import oracle.jdbc.driver.json.tree.JsonpObjectImpl;

import static java.lang.Integer.parseInt;


// 터미널 실행
// cd D:\dev\work_java\Java_Oracle_Mini\out\artifacts\Java_Oracle_Mini_jar
// java -jar Java_Oracle_Mini.jar

public class MainApplication {
    public static void main(String[] args) throws InterruptedException, IOException {
        clearScreen();
        Animation.loading();
        Scanner sc = new Scanner(System.in);
        MembersDAO membersDAO = new MembersDAO();
        MembersDTO loginMember = null; // 현재 로그인한 사용자
        PostsDTO postsDTO = new PostsDTO();
        PostsDAO postsDAO = new PostsDAO();
        LikesDAO likesDAO = new LikesDAO(membersDAO);
        CommentsDAO commentsDAO = new CommentsDAO();

        while (true) {
            if (loginMember == null) { // 첫 화면
                printFirstMenu();
            } else { // 로그인한 경우
                printSecondMenu();
            }
            int choice = -1; // 초기값 설정
            try {
                choice = sc.nextInt(); // 정수 입력 시 처리
                clearScreen();
            } catch (InputMismatchException e) {
                // switch-case문의 default에서 예외 처리를 하였으나,
                // case * 이외의 숫자가 입력됐을 때의 예외는 처리가 되었으나,
                // 문자열 등이 입력됐을 때의 예외처리가 되지 않아 이 곳에서 처리.
                sc.nextLine(); // 버퍼 비우기
                System.out.println("잘못된 입력입니다. 올바른 숫자를 입력해주세요.");
            }
            System.out.println();
            if (loginMember == null) { // 로그인하지 않은 경우
                switch (choice) {
                    case 1: // 로그인
                        MembersDTO inputMember = new MembersDTO();
                        System.out.print("아이디 입력: ");
                        String inputId = sc.next();
                        sc.nextLine(); // 버퍼 비우기
                        System.out.print("비밀번호 입력: ");
                        String password = sc.next();
                        // 새로운 회원 객체, 입력된 아이디와 비밀번호를 새롭게 생성된 회원 객체에 설정
                        inputMember.setInputId(inputId);
                        inputMember.setPassword(password);
                        loginMember = membersDAO.login(inputMember);
                        break; // loginMember = null → 다시 첫화면으로
                    case 2: // 회원 가입
                        membersDAO.addMember(new MembersDTO());
                        break;
                    case 3: // 종료
                        Animation.loading();
                        System.out.println("프로그램을 종료합니다...");
                        return;
                    default:
                        System.out.println("올바른 숫자를 입력해주세요.");
                        break;
                }
            } else { // 로그인 성공 시
                switch (choice) {
                    case 1: // 게시글 작성
                        System.out.print("글 제목 입력: ");
                        sc.nextLine(); // 버퍼 지우기
                        String title = sc.nextLine();
                        System.out.print("글 내용 입력: ");
                        String content = sc.nextLine();
                        PostsDTO myPostsDTO = new PostsDTO(title, content, loginMember.getId());
                        postsDAO.addPost(myPostsDTO);
                        // break; 자동으로 다음 case로 넘어가게 하기 위해 주석 처리
                    case 2: // 게시글 목록 출력
                        List<PostsDTO> postsList = postsDAO.getAllPosts(); // 모든 게시글의 정보를 리스트에 저장
                        if (!postsList.isEmpty()) {
                            System.out.println("<모든 글 목록>");
                            for (PostsDTO post : postsList) { // 향상된 for 문
                                String author = postsDAO.postAuthorName(parseInt(post.getMembersID()), parseInt(post.getId()));
                                System.out.println("글 번호 : " + post.getId());
                                System.out.println("글 제목 : " + post.getTitle());
                                System.out.println("작성자 : " + author);
                                System.out.println("추천수 : " + post.getLikesCounts());
                                System.out.println("-".repeat(100));
                            }
                            // 댓글 작성 및 좋아요 누르기 위해 글 선택
                            System.out.print("몇번 게시글에 들어가시겠습니까? : ");
                            int selectedPostID = sc.nextInt();
                            clearScreen();
                            boolean isPost = true;
                            while (isPost) {
                                List<PostsDTO> enter = postsDAO.enterPost(selectedPostID);
                                System.out.println(selectedPostID + "번 게시글에 들어왔습니다.");
                                if (enter.isEmpty()) {
                                    System.out.println(selectedPostID + "번 게시글은 존재하지 않습니다.");
                                    System.out.println("다시 이전 메뉴로 돌아가시겠습니까?");
                                    Animation.waitMoment();
                                    break;
                                }

                                for (PostsDTO e : enter) { // 향상된 for 문으로 모든 게시글의 정보가 저장되있는 리스트 순회 출력
                                    System.out.println("<" + selectedPostID + "번 글>");
                                    System.out.println("글 번호 : " + e.getId());
                                    System.out.println("글 제목 : " + e.getTitle());
                                    System.out.println("글 작성 시간 : " + e.getCurrentTime());
                                    System.out.println("글 내용 : " + e.getContent());
                                    System.out.println("작성자 : " + e.getName());
                                    System.out.println("추천수 : " + e.getLikesCounts());
                                    System.out.println("-".repeat(100));
                                }
                                System.out.print("[1] 글 수정\n[2] 글 삭제\n[3] 댓글 보기\n[4] 댓글 작성\n[5] 댓글 수정\n[6] 댓글 삭제\n[7] 좋아요\n[8] 나가기\n입력: ");
                                int action = sc.nextInt();
                                switch (action) {
                                    case 1: // 글 수정
                                        postsDAO.modifyPost(new PostsDTO(), selectedPostID, loginMember.getId());
                                        break;
                                    case 2: // 글 삭제
                                        postsDAO.deletePost(selectedPostID, loginMember.getId());
                                        break;
                                    case 3: // 댓글 보기
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        clearScreen();
                                        break;
                                    case 4: // 댓글 작성
                                        System.out.print("댓글을 입력해주세요 : ");
                                        sc.nextLine();
                                        String comment = sc.nextLine();
                                        commentsDAO.addComment(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId(), comment, loginMember.getName()));
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        break;
                                    case 5: // 댓글 수정
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        commentsDAO.commentModify(loginMember, selectedPostID);
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        break;
                                    case 6: // 댓글 삭제
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        commentsDAO.commentDelete(new CommentsDTO());
                                        commentsDAO.getCommentsByPostId(new CommentsDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        break;
                                    case 7: // 좋아요
                                        likesDAO.addLike(new LikesDTO(Integer.toString(selectedPostID), loginMember.getId()));
                                        break;
                                    case 8: // 나가기
                                        isPost = false;
                                        clearScreen();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            System.out.println("등록된 글이 존재하지 않습니다.");
                        }
                        break;
                    case 3:
                        loginMember = null; // 로그아웃
                        break;
                    case 4: // 회원 탈퇴
                        membersDAO.deleteMember(loginMember);
                        loginMember = null;
                        break;
                    default:
                        System.out.println("올바른 숫자를 입력해주세요.");
                        break;
                }
            }
        }
    }


    public static void clearScreen() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    public static void printFirstMenu() throws IOException, InterruptedException {
        Animation.logo();
        System.out.println("[1] 로그인");
        System.out.println("[2] 회원가입");
        System.out.println("[3] 종료");
        System.out.println();
        System.out.print("메뉴를 선택하세요 : ");
    }

    public static void printSecondMenu() throws IOException, InterruptedException {
        Animation.logo();
        System.out.println("[1] 글쓰기");
        System.out.println("[2] 글 목록보기");
        System.out.println("[3] 로그아웃");
        System.out.println("[4] 회원 탈퇴");
        System.out.println();
        System.out.print("메뉴를 선택하세요 : ");
    }
}