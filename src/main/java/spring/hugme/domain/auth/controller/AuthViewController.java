package spring.hugme.domain.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.hugme.global.controller.BaseViewController;

@Slf4j
@Controller
@RequestMapping(BaseViewController.VIEW_PREFIX)
public class AuthViewController extends BaseViewController {

    private static final String ATTRIBUTE_NAME = "devDomain";
    @Value("${dev.domain}")
    private String devDomain;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute(ATTRIBUTE_NAME, devDomain);
        return "auth/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute(ATTRIBUTE_NAME, devDomain);
        return "auth/signup";
    }

    @GetMapping("/home")
    public String userPage(Model model) {
        model.addAttribute(ATTRIBUTE_NAME, devDomain);
        return "auth/home";
    }


}