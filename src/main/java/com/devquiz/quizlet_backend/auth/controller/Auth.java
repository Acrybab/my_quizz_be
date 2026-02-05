package com.devquiz.quizlet_backend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor

public class Auth {
    @GetMapping("/oauth2/code/google")
    public String googleLogin() {
        return "Google login successful!";
    }
}
