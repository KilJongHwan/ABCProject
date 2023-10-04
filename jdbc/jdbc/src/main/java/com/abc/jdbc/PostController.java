package com.abc.jdbc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/main")
public class PostController {
    PostsDAO postsDAO = new PostsDAO();
    CommentsDAO commentsDAO = new CommentsDAO();

    MembersDAO membersDAO = new MembersDAO();
    LikesDAO likesDAO = new LikesDAO(membersDAO);

    @GetMapping("/post")
    public String selectPost(HttpServletRequest request, Model model) {
        String loggedInMember = getLoggedInMemberFromCookie(request);
        model.addAttribute("loggedInMember", loggedInMember);
        List<PostsDTO> posts = postsDAO.getAllPosts();
        model.addAttribute("posts", posts);
        return "selectrst";
    }

    private String getLoggedInMemberFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loggedInMember".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/post/{postId}") // URL 패턴 변경
    public String enterPost(@PathVariable("postId") Integer postId, Model model) {
        PostsDTO post = postsDAO.enterPost(postId);
        model.addAttribute("post", post);
        List<CommentsDTO> comments = commentsDAO.whatsPost(postId);
        if (comments.isEmpty()) {
            model.addAttribute("message", "해당 게시글에 댓글이 없습니다.");
        } else {
            model.addAttribute("comments", comments);
            model.addAttribute("postId", postId);
        }
        return "postroom"; // postroom.html 페이지로 이동
    }

    // 게시물 수정 페이지로 이동
    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable int postId, Model model, HttpServletRequest request) {
        String loggedInMember = getLoggedInMemberFromCookie(request);
        PostsDTO post = postsDAO.getPostById(postId);

        // 게시물 작성자와 로그인한 사용자가 같은 경우에만 수정 페이지로 이동
        if (post != null && loggedInMember != null && loggedInMember.equals(post.getMembersID())) {
            model.addAttribute("post", post);
            return "modifypost";
        } else {
            return "redirect:/main/post/" + postId;
        }
    }
    @PostMapping("/edit/{postId}/complete")
    public String editComplete(@RequestParam("pid") int postId,
                               @RequestParam("title") String title,
                               @RequestParam("content") String content,
                               HttpServletRequest request, Model model) {
        String loggedInMember = getLoggedInMemberFromCookie(request);

        postsDAO.modifyTitleAndContent(postId, loggedInMember, title, content);

        PostsDTO updatedPost = postsDAO.getPostById(postId);

        model.addAttribute("post", updatedPost);
        model.addAttribute("title", updatedPost.getTitle());
        model.addAttribute("content", updatedPost.getContent());

        return "selectrst";
    }

    @PostMapping("/add/comment")
    public String addComment(@RequestParam("pid") int postId,
                             @RequestParam("content") String content,
                             HttpServletRequest request, Model model) {
        String loggedInMember = getLoggedInMemberFromCookie(request);
        if (loggedInMember != null){
            int writeId = Integer.parseInt(loggedInMember);
            commentsDAO.commentWrite(postId, writeId, content);
            model.addAttribute("content", content);
            model.addAttribute("memberId",writeId);
        }
        // 댓글 작성 후 다시 해당 게시글 페이지로 리다이렉트
        return "redirect:/main/post/" + postId;
    }
    @PostMapping("/add/like")
    public String addLike(@RequestParam String postId, HttpServletRequest request) {
        // 로그인한 아이디 정보를 가져오는 데 필요한 코드
        String loggedInMember = getLoggedInMemberFromCookie(request);

        if (loggedInMember != null) {
            // 로그인한 아이디가 이미 해당 포스트를 눌렀는지 확인
            boolean hasLiked = likesDAO.hasUserLikedPost(loggedInMember, postId);

            if (!hasLiked) {
                // 좋아요를 추가하는 데 필요한 데이터를 생성
                LikesDTO likesDTO = new LikesDTO();
                likesDTO.setPostsId(postId);

                likesDTO.setMembersId(loggedInMember);

                // 좋아요를 추가
                likesDAO.addLike(likesDTO);

                // 게시물 상세 페이지 또는 리디렉션할 페이지로 이동
                return "redirect:/main/post/" + postId;
            } else {
                // 이미 좋아요를 누른 경우에 대한 처리 (예: 오류 메시지 표시 또는 다른 페이지로 리디렉션)
                return "redirect:/main/post/" + postId;
            }
        } else {
            // 로그인되지 않은 경우 로그인 페이지로 리디렉션
            return "redirect:/members/login";
        }
    }
}