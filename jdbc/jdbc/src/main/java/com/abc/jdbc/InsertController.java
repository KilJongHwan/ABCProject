package com.abc.jdbc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/insert")
public class InsertController {
    PostsDAO dao = new PostsDAO();
    @GetMapping("/post")
    public String insertForm(HttpServletRequest request, Model model ) {
        String loggedInMember = getLoggedInMemberFromCookie(request);
        if (loggedInMember != null) {
            // 사용자가 로그인한 경우
            model.addAttribute("loggedInMember", loggedInMember);
            model.addAttribute("post", new PostsDTO());
            return "insert"; // 게시물 작성 폼 템플릿 이름
        } else {
            // 사용자가 로그인하지 않은 경우
            return "redirect:/members/login"; // 로그인 페이지로 리다이렉트
        }
    }

    @PostMapping("/post")
    public String savePost(HttpServletRequest request, @ModelAttribute("post") PostsDTO postsDTO) {
        String loggedInMember = getLoggedInMemberFromCookie(request);
        if (loggedInMember != null) {
            MembersDTO membersDTO = dao.getMemberById(loggedInMember); // MembersDTO에서 id를 가져옴
            if (membersDTO != null) {
                postsDTO.setMembersID(membersDTO.getId()); // PostsDTO의 MembersID 필드에 설정
                dao.addPost(postsDTO);
                return "redirect:/main/post";
            }
        }
        // 사용자가 로그인하지 않은 경우 처리
        return "redirect:/members/login";
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
}