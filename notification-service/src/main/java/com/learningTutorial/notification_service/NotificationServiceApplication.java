package com.learningTutorial.notification_service;

import com.learningTutorial.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
    // Inject the EmailService
    private final EmailService emailService;
    //    Listening to the order-topic
    @KafkaListener(topics = "notification-topic")
    public void handleNotification(@NonNull OrderPlacedEvent orderPlacedEvent){
        // Expand the logic of the service here
        // send out a push notification
        log.info("Order number {} has been placed", orderPlacedEvent.getOrderNumber());
        // send out an email
        emailService.sendOrderConfirmationEmail("tomiayoade2@gmail.com", orderPlacedEvent.getOrderNumber());
    }

    @KafkaListener(topics="notification-product-topic")
    public void handleProductNotification(@NonNull ProductPlacedEvent productPlacedEvent){
        log.info("Product {} {} {} has been placed",
                productPlacedEvent.getName(),
                productPlacedEvent.getPrice(),
                productPlacedEvent.getDescription()
        );
        emailService.sendProductConfirmationEmail("tomiayoade2@gmail.com", productPlacedEvent.getName());
    }
}