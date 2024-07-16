package ac.su.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage"; // mypage.html 템플릿을 반환
    }
}
