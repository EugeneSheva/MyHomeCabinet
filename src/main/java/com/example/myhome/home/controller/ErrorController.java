package com.example.myhome.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/cabinet/400")
    public String showBadRequestPage() {return "error/400";}

    @GetMapping("/cabinet/403")
    public String showForbiddenPage() {
        return "error/403";
    }

    @GetMapping("/cabinet/500")
    public String showServerErrorPage() {return "error/500";}

}
