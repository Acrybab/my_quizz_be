package com.devquiz.quizlet_backend.user.service.Email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        // Logic to send email
        String verificationUrl = "http://localhost:8080/api/v1/users/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Quizlet Dev <your-email@gmail.com>");
        message.setTo(toEmail);
        message.setSubject("[Quizlet] Xác nhận địa chỉ email của bạn");
        message.setText("Chào bạn,\n\n" +
                "Cảm ơn bạn đã đăng ký tài khoản tại Quizlet Backend. " +
                "Vui lòng nhấn vào đường dẫn bên dưới để xác thực tài khoản của mình:\n" +
                verificationUrl + "\n\n" +
                "Link này sẽ hết hạn sau 24 giờ.\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.");

        mailSender.send(message);
    }
}
