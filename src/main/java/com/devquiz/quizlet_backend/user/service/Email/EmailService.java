package com.devquiz.quizlet_backend.user.service.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("Quizlet Dev <your-email@gmail.com>");
            helper.setTo(toEmail);
            helper.setSubject("[Quizlet] Verify your email address");

            // Generate the English HTML content
            String htmlContent = buildHtmlEmail(otp);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtmlEmail(String otp) {
        return "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;\">" +
                "  <div style=\"background-color: #4255ff; padding: 20px; text-align: center;\">" +
                "    <h1 style=\"color: white; margin: 0; font-size: 28px;\">Quizlet</h1>" +
                "  </div>" +
                "  <div style=\"padding: 30px; line-height: 1.6; color: #333;\">" +
                "    <h2 style=\"color: #1a1a1a;\">Verify your account</h2>" +
                "    <p>Hi there,</p>" +
                "    <p>Thank you for joining our learning community! To complete your registration, please use the following One-Time Password (OTP) to verify your account:</p>" +
                "    <div style=\"text-align: center; margin: 40px 0;\">" +
                "      <div style=\"margin-bottom: 10px; font-size: 14px; color: #666; font-weight: bold; text-transform: uppercase;\">Your Verification Code</div>" +
                "      <span style=\"display: inline-block; padding: 15px 35px; background-color: #f0f2ff; color: #4255ff; font-size: 36px; font-weight: bold; letter-spacing: 8px; border-radius: 8px; border: 2px solid #4255ff;\">" + otp + "</span>" +
                "    </div>" +
                "    <p style=\"font-size: 14px; color: #666; border-top: 1px solid #eee; pt: 20px; margin-top: 30px;\">" +
                "       This code will <b>expire in 5 minutes</b> for security reasons.<br>" +
                "       If you did not request this code, please ignore this email." +
                "    </p>" +
                "  </div>" +
                "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eeeeee;\">" +
                "    &copy; 2026 Quizlet Backend Team. All rights reserved." +
                "  </div>" +
                "</div>";
    }
}