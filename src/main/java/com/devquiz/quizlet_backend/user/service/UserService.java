package com.devquiz.quizlet_backend.user.service;

import com.devquiz.quizlet_backend.user.dto.request.UserRegisterRequest;
import com.devquiz.quizlet_backend.user.dto.request.UserSignInRequest;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.dto.response.SignInResponse;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.entity.User;

import java.util.List;

public interface UserService {
 UserResponse register(UserRegisterRequest request);
 List<UserResponse> getAllUsers();
//    void verifyUser(String token);
    ApiResponse<SignInResponse> signIn(UserSignInRequest request);
    String verifyOTP(String email, String otp);
//void logOut(String token);
    String resendOTP(String email);
 User processOAuthPostLogin(String email, String name);
  void forgotPassword(String email);
  String updatePassword(String email, String newPassword , String oldPassword);
  User findUserByEmail(String email);

  List<User> getAllOtherUsers(String userEmail);


}
