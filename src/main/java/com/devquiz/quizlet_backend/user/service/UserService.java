package com.devquiz.quizlet_backend.user.service;

import com.devquiz.quizlet_backend.user.dto.request.UserRegisterRequest;
import com.devquiz.quizlet_backend.user.dto.request.UserSignInRequest;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.entity.User;

import java.util.List;

public interface UserService {
 UserResponse register(UserRegisterRequest request);
 List<UserResponse> getAllUsers();
//    void verifyUser(String token);
    String signIn(UserSignInRequest request);
    String verifyOTP(String email, String otp);
//void logOut(String token);
 User processOAuthPostLogin(String email, String name);

}
