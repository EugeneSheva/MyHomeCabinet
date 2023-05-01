package com.example.myhome.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/admin/403")
    public String showForbiddenPage() {
        return "error/403";
    }
}
