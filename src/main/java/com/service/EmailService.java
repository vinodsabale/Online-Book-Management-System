package com.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String userName, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email - Book Management System");
        message.setText(
            "Hello " + userName + ",\n\n" +
            "Please click the link below to verify your email:\n" +
            verificationLink + "\n\n" +
            "This link will confirm your account.\n\n" +
            "Regards,\nBook Management System"
        );
        mailSender.send(message);
    }
}