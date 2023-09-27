package com.abc.jdbc.main;

import java.util.List;
import java.util.Scanner;

import com.abc.jdbc.dao.CommentsDAO;
import com.abc.jdbc.dao.LikesDAO;
import com.abc.jdbc.dao.MembersDAO;
import com.abc.jdbc.dao.PostsDAO;
import com.abc.jdbc.dto.LikesDTO;
import com.abc.jdbc.dto.MembersDTO;
import com.abc.jdbc.dto.PostsDTO;

public class MainApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MembersDAO membersDAO = new MembersDAO();
        MembersDTO loggedInUser = null; // 현재 로그인한 사용자
        PostsDTO postsDTO = new PostsDTO();
        PostsDAO postsDAO = new PostsDAO();
        LikesDTO likesDTO = new LikesDTO();
        LikesDAO likesDAO = new LikesDAO();
        CommentsDAO commentsDAO = new CommentsDAO();

        while (true) {
            if (loggedInUser == null) { // 로그인하지 않은 경우
                System.out.println("<회원 관리 프로그램>");
                System.out.println("[1] 로그인");
                System.out.println("[2] 회원가입");
                System.out.println("[3] 종료");
            } else { // 로그인한 경우
                System.out.println("<글 관리 프로그램>");
                System.out.println("[1] 글쓰기");
                System.out.println("[2] 글 목록보기");
                System.out.println("[3] 로그아웃");
            }

            System.out.print("메뉴를 선택하세요 : ");
            int choice = sc.nextInt();

            if (loggedInUser == null) { // 로그인하지 않은 경우
                switch (choice) {
                    case 1: // 로그인
                        // 입력
                        MembersDTO inputMember = new MembersDTO();
                        System.out.print("아이디 입력: ");
                        String inputId = sc.next();
                        System.out.print("비밀번호 입력: ");
                        String password = sc.next();

                        // 새로운 회원 객체, 입력된 아이디와 비밀번호를 새롭게 생성된 회원 객체에 설정
                        inputMember.setInputId(inputId);
                        inputMember.setPassword(password);
                        loggedInUser = membersDAO.login(inputMember);
                        if (loggedInUser != null) {
                            System.out.println("로그인 성공!");
                        } else {
                            System.out.println("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
                        }
                        break;
                    case 2: // 회원 가입
                        System.out.print("아이디 입력: ");
                        inputId = sc.next();
                        System.out.print("비밀번호 입력: ");
                        password = sc.next();
                        System.out.print("이름 입력: ");
                        String name = sc.next();
                        MembersDTO membersDTO = new MembersDTO(inputId, password, name);
                        membersDAO.addMember(membersDTO);
                        System.out.println("회원이 추가되었습니다.");
                        break;
                    case 3: // 종료
                        membersDAO.close();
                        sc.close(); // 스캐너를 먼저 닫습니다.
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("올바른 숫자를 입력해주세요.");
                        break;
                }
            } else { // 로그인한 경우
                switch (choice) {
                    case 1:
                        System.out.print("글 제목 입력: ");
                        String title = sc.next();
                        System.out.print("글 내용 입력: ");
                        String content = sc.next();
                        PostsDTO postsDTO1 = new PostsDTO(title, content, loggedInUser.getId());
                        postsDAO.addPost(postsDTO1);
                        System.out.println("글이 작성되었습니다.");
                        break;
                    case 2: // 글 목록 보기
                        List<PostsDTO> postsList = postsDAO.getAllPosts();
                        if (!postsList.isEmpty()) {
                            System.out.println("<모든 글 목록>");
                            for (PostsDTO post : postsList) { // 향상된 for 문
                                System.out.println("글 번호 : " + post.getId());
                                System.out.println("글 제목 : " + post.getTitle());
                                System.out.println("글 작성 시간 : " + post.getCurrentTime());
                                System.out.println("글 내용 : " + post.getContent());
                                System.out.println("작성자 : " + post.getMembersID());
                                System.out.println("추천수 : " + post.getLikesCounts());
                                System.out.println("-".repeat(20));
                            }
                            System.out.print("몇번 글에 들어갈까요? : ");
                            int post = sc.nextInt();
                            boolean isPost = true;
                            while (isPost){
//                                List<PostsDTO>enter = postsDAO.enterPost(post);
                                PostsDTO enter = postsDAO.enterPost(post);
//                                for (PostsDTO e : enter) { // 향상된 for 문
//                                    System.out.println("글 번호 : " + e.getId());
//                                    System.out.println("글 제목 : " + e.getTitle());
//                                    System.out.println("글 작성 시간 : " + e.getCurrentTime());
//                                    System.out.println("글 내용 : " + e.getContent());
//                                    System.out.println("작성자 : " + e.getMembersID());
//                                    System.out.println("추천수 : " + e.getLikesCounts());
//                                    System.out.println("-".repeat(20));
//                                }
                                    System.out.println("글 번호 : " + enter.getId());
                                    System.out.println("글 제목 : " + enter.getTitle());
                                    System.out.println("글 작성 시간 : " + enter.getCurrentTime());
                                    System.out.println("글 내용 : " + enter.getContent());
                                    System.out.println("작성자 : " + enter.getMembersID());
                                    System.out.println("추천수 : " + enter.getLikesCounts());
                                    System.out.println("-".repeat(20));

                                System.out.println(post + "번방에 들어 왔습니다.");
                                System.out.print("[1]댓글 작성 [2]댓글 수정 [3]댓글 보기 [4] 좋아요 누르기 [5]나가기 : ");
                                int action = sc.nextInt();
                                switch (action){
                                    case 1:
                                        commentsDAO.commentWrite(post,Integer.parseInt(loggedInUser.getId()));
                                        break;
                                    case 2:
                                        commentsDAO.commentModify();
                                        break;
                                    case 3:
                                        commentsDAO.printCommentsByPostId(post,Integer.parseInt(loggedInUser.getId()));
                                        break;
                                    case 4:
                                        System.out.println("좋아요를 누르시겠습니까? (1 : 누른다 / 2 : 안누른다 / 3 : 뒤로 가기) : ");
                                        int input = sc.nextInt();
                                        if (input == 1) { // 좋아요
                                        } else if (input == 2) { // 안누른다
                                        } else if (input == 3) { // 뒤로 가기
                                        } else {
                                            System.out.println("올바른 숫자를 입력해주세요.");
                                        }
                                        break;
                                    case 5:
                                        isPost = false;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            System.out.println("등록된 글이 없습니다.");
                        }
                        break;
                    case 3:
                        loggedInUser = null; // 로그아웃
                        break;
                    default:
                        System.out.println("올바른 숫자를 입력해주세요.");
                        break;
                }
            }
        }
    }
}