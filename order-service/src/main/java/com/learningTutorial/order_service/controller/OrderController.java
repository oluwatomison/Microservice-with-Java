package com.learningTutorial.order_service.controller;

import com.learningTutorial.order_service.dto.OrderRequest;
import com.learningTutorial.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor // It creates a constructor parameter for the OrderService
public class OrderController {
    // Injecting the OrderService
    private final OrderService orderService;
    @PostMapping  // Accepts OrderRequest from the controller
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod") // Circuit breaker for the inventory service
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        // Returning a CompletableFuture to indicate that the order is being processed asynchronously
       return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Order placement failed due to inventory service issues");
    }
}
