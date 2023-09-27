package com.abc.jdbc;

import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/members")
public class LoginController {
    private final MembersDAO membersDAO = new MembersDAO();

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 템플릿 이름
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        // 쿠키에서 사용자 정보를 추출
        String loggedInMember = getLoggedInMemberFromCookie(request);

        if (loggedInMember != null) {
            model.addAttribute("loggedInMember", loggedInMember);
            return "redirect:/main/post"; // 로그인되면 selectrst로 리다이렉트
        } else {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
    }


    private String getLoggedInMemberFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loggedInMember".equals(cookie.getName())) {
                    // 쿠키에서 사용자 정보 추출 (deserialize)
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MembersDTO membersDTO, HttpServletResponse response, Model model) {
        // 사용자 인증 로직 수행
        if (authenticateUser(membersDTO.getInputId(), membersDTO.getPassword())) {
            // 사용자 정보를 가져와서 쿠키 생성
            MembersDTO loggedInMember = membersDAO.login(membersDTO);
            if (loggedInMember != null) {
                Cookie cookie = new Cookie("loggedInMember", loggedInMember.getId());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return "redirect:/members/dashboard"; // 로그인 성공 시 이동할 페이지
        } else {
            return "redirect:/members/login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response ,Model model) {
        // 로그아웃 시 쿠키 삭제
        Cookie authCookie = new Cookie("loggedInMember", null); // 쿠키 이름을 "loggedInMember"로 변경
        authCookie.setMaxAge(0); // 쿠키 만료
        authCookie.setPath("/");
        response.addCookie(authCookie);
        CacheControl cacheControl =CacheControl.noStore().mustRevalidate();
        response.setHeader("Cache-Control", cacheControl.getHeaderValue());
        return "redirect:/main/post"; // 로그아웃 후 메인으로 이동
    }


    private boolean authenticateUser(String username, String password) {
        // 데이터베이스와 연동하여 사용자 정보를 조회
        MembersDTO userFromDatabase = membersDAO.findInputId(username);

        // 사용자 정보가 조회되지 않으면 인증 실패
        if (userFromDatabase == null) {
            return false;
        }

        // 입력한 비밀번호와 데이터베이스에서 조회한 비밀번호를 비교
        return password.equals(userFromDatabase.getPassword());
    }

    private MembersDTO getLoggedInMember(String username) {
        // 사용자 정보를 데이터베이스 또는 다른 저장소에서 가져옴
        MembersDTO loggedInMember = new MembersDTO();
        loggedInMember.setName(username);
        // 사용자의 다른 정보 설정
        return loggedInMember;
    }
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // 회원가입 페이지를 위한 템플릿 이름
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String inputId, @RequestParam String password, @RequestParam String name) {
        // 데이터베이스에 새 회원 정보를 추가
        MembersDTO newMember = new MembersDTO();
        newMember.setInputId(inputId);
        newMember.setPassword(password);
        newMember.setName(name);
        membersDAO.addMember(newMember);

        return "redirect:/members/login"; // 회원가입 후 로그인 페이지로 이동
    }


    @GetMapping("/list")
    public String listMembers(Model model) {
        List<MembersDTO> membersList = membersDAO.getAllMembers();
        model.addAttribute("membersList", membersList);
        return "memberlist"; // 회원 목록을 보여줄 Thymeleaf 템플릿 이름
    }
}
