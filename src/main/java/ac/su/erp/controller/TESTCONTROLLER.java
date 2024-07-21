package ac.su.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TESTCONTROLLER {

    @GetMapping
    public String ex01(Model model) {
        model.addAttribute("data","test data -jsc-");
        return "test";
    }

    @GetMapping("/layout")
    public String layoutPage(Model model) {
        return "layouts/layout";
    }

    @GetMapping("/work")
    public String workPage(Model model) {
        return "work";
    }
}
