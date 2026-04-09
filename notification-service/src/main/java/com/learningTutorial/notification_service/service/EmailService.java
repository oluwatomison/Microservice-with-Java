package com.learningTutorial.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendOrderConfirmationEmail(String toEmail, String orderNumber){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ayoade.adeyemo@and.digital");
            message.setTo(toEmail);
            message.setSubject("Order Confirmation - " + orderNumber);
            message.setText("Great news! Your order " + orderNumber + " has been placed successfully.");

            javaMailSender.send(message);
            log.info("Sending order confirmation email");
        } catch (Exception e) {
            log.error("Error sending order confirmation email", e);
        }
    }

    public void sendProductConfirmationEmail(String toEmail, String productName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ayoade.adeyemo@and.digital");
        message.setTo(toEmail);
        message.setSubject("Product Confirmation");
        message.setText("Great news! Your product has been confirmed {} successfully " + productName);

        javaMailSender.send(message);
        log.info("Sending product confirmation email");
    }
}
