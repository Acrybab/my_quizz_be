package com.devquiz.quizlet_backend.user.service;

import com.devquiz.quizlet_backend.user.dto.request.UserRegisterRequest;
import com.devquiz.quizlet_backend.user.dto.request.UserSignInRequest;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.dto.response.SignInResponse;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import com.devquiz.quizlet_backend.user.service.Email.EmailService;
import com.devquiz.quizlet_backend.user.service.JwtService.JwtService;
import com.devquiz.quizlet_backend.user.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final EmailService   emailService;
   private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;
   @Override
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.findByUserName((request.getUserName())).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
//        String verificationToken = jwtService.generateJwtToken(request.getEmail());
        String OTP = String.valueOf(new Random().nextInt(899999) + 100000);
       redisTemplate.opsForValue().set("otp:" + request.getEmail(), String.valueOf(OTP), 5, TimeUnit.MINUTES);
       User user = User.builder().userName(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isEnabled(false)
                .password(encodedPassword).role(Role.RoleType.USER)
                .build();
    User savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(savedUser.getEmail() , OTP );
        return mapToUserResponse(savedUser);
    }
//    @Override
//    public void verifyUser(String token) {
//        try {
//            String userEmail = jwtService.extractEmail(token);
//            User user = userRepository.findByEmail(userEmail)
//                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
//            user.setIsEnabled(true);
//            userRepository.save(user);
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid verification token");
//        }
//    }




    @Override
    public String verifyOTP(String email, String otpEntered) {
        String storedOtp = (String) redisTemplate.opsForValue().get("otp:" + email);
        System.out.println(storedOtp + "storedOtp" +otpEntered);
        if (storedOtp == null) {
            throw new RuntimeException("Mã OTP đã hết hạn hoặc không tồn tại");
        }

        if (!storedOtp.equals(otpEntered)) {
            throw new RuntimeException("Mã OTP không chính xác");
        }

        // Nếu khớp: Active user và xóa OTP trong Redis
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.setIsEnabled(true);
        userRepository.save(user);

        redisTemplate.delete("otp:" + email);

        return "Xác thực thành công!";
    }

    @Override
    public String resendOTP(String email) {
        System.out.println("Resending OTP to email: " + email);
           User user = userRepository.findByEmail(email).orElseThrow(
                     () -> new RuntimeException("User không tồn tại")
           );
              String newOTP = String.valueOf(new Random().nextInt(899999) + 100000);
                redisTemplate.opsForValue().set("otp:" + email, String.valueOf(newOTP), 5, TimeUnit.MINUTES);
                emailService.sendVerificationEmail(user.getEmail(), newOTP);
        return "Đã gửi lại mã OTP mới đến email của bạn.";

     }


    @Override
    public ApiResponse<SignInResponse> signIn(UserSignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        if (!user.getIsEnabled()) {
            throw new RuntimeException("User account is not verified");
        }

        return ApiResponse.<SignInResponse>builder()
                .code(200)
                .message("Sign in successful")
                .data(new SignInResponse(
                        jwtService.generateJwtToken(user.getEmail()),
                        user.getEmail()
                ))
                .build();
    }

//    @Override
//    public void logOut(String token) {
//       if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//       }
//       try {
//     Date expirationDate = jwtService.extractExpiration(token);
//        long ttl = expirationDate.getTime() - new Date().getTime();
//        if (ttl > 0) {
//            // Lưu token vào kho lưu trữ tạm thời với thời gian sống tương ứng
//            // Ví dụ: Sử dụng Redis hoặc một cấu trúc dữ liệu trong bộ nhớ
//            // tokenBlacklistStore.put(token, System.currentTimeMillis() + ttl);
//            // Đây chỉ là một ví dụ minh họa, bạn cần triển khai kho lưu trữ thực tế
//            redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMillis(ttl));        }
//
//       } catch (Exception e) {
//           throw new RuntimeException("Token invalid or already expired");
//       }
//
//    }

    @Override
    public User processOAuthPostLogin(String email, String name) {
        Optional<User> existUser = userRepository.findByEmail(email);
         if(existUser.isPresent()) {
             User user = existUser.get();
             user.setUserName(name);
             return userRepository.save(user);

         }else {
                User newUser = new User();
                String[] nameParts = name.split(" ");
                if (nameParts.length > 1) {
                    newUser.setFirstName(nameParts[0]);
                    newUser.setLastName(String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length)));
                } else {
                    newUser.setFirstName(name);
                    newUser.setLastName("");
                }
                newUser.setUserName(name);
                newUser.setEmail(email);
                newUser.setIsEnabled(true);
                newUser.setRole(Role.RoleType.USER);
                newUser.setPassword("");
                return userRepository.save(newUser);
         }

    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }
    private UserResponse mapToUserResponse(User user){
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedUserAt()
        );
    }
}
