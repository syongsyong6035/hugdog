package spring.hugme.domain.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "main";  // 루트 경로도 메인 페이지로
    }

    // 메인 페이지
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
}