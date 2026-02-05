package com.devquiz.quizlet_backend.user.controllers;

import com.devquiz.quizlet_backend.user.dto.request.UserRegisterRequest;
import com.devquiz.quizlet_backend.user.dto.request.UserSignInRequest;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.service.JwtService.JwtService;
import com.devquiz.quizlet_backend.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    // Hằng số cho Cookie - Nên đưa vào một class Constants riêng
    private static final String COOKIE_NAME = "my_quizz_token";
    private static final int COOKIE_MAX_AGE = 3600; // 1 hour

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRegisterRequest user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody UserSignInRequest userRequest, HttpServletResponse response) {
        String token = userService.signIn(userRequest);
        addTokenToCookie(response, token); // Sử dụng hàm dùng chung

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Sign-in successfully")
                .data(token) // Vẫn trả về token nếu FE cần dùng cho mục đích khác
                .build());
    }

    @GetMapping("/google-success")
    public ResponseEntity<ApiResponse<String>> googleSuccess(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            HttpServletResponse response) {

        if (oAuth2User == null) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "OAuth2User is null");
        }

        try {
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            User user = userService.processOAuthPostLogin(email, name);
            String token = jwtService.generateJwtToken(user.getEmail());

            addTokenToCookie(response, token);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code(1000)
                    .message("Google Sign-in successfully")
                    .data(token)
                    .build());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Hàm helper để tái sử dụng logic tạo Cookie
     */
    private void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Đổi thành true khi deploy lên HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        // cookie.setAttribute("SameSite", "Lax"); // Quan trọng nếu FE và BE khác domain
        response.addCookie(cookie);
    }

    /**
     * Hàm helper để tạo Response lỗi nhanh
     */
    private ResponseEntity<ApiResponse<String>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiResponse.<String>builder()
                .code(status.value())
                .message(message)
                .build());
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
