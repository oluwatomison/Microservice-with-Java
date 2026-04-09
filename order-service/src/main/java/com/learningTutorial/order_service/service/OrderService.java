package com.learningTutorial.order_service.service;

import com.learningTutorial.order_service.dto.InventoryResponse;
import com.learningTutorial.order_service.dto.OrderLineItemsDTO;
import com.learningTutorial.order_service.dto.OrderRequest;
import com.learningTutorial.order_service.event.OrderPlacedEvent;
import com.learningTutorial.order_service.model.Order;
import com.learningTutorial.order_service.model.OrderLineItems;
import com.learningTutorial.order_service.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Constructor injection for OrderRepository
@Transactional
public class OrderService {
    // Injecting the OrderRepository to save the order
    private final OrderRepository orderRepository;
    // Injecting the WebClient to call the InventoryService
    private final WebClient.Builder webClientBuilder;
    // Creating a tracer class
    private final Tracer tracer;

    // Injecting kafka template class
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    // Accepts OrderRequest from the controller
    public String placeOrder(OrderRequest orderRequest){
        // Create an order object
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsDTOList().stream()
                .map(this::mapToEntity)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        // Collect the skuCodes from the orderLineItemsList
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        Span inventoryServiceLookup =  tracer.nextSpan().name("inventoryServiceLookup");
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            // Calling the InventoryService, place the order if the order is in stock using the webclient
            // bodyToMono() is used to convert the response to Mono<Boolean>
            // block() is used to make Async request
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock =  Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock){
                orderRepository.save(order);
                // Sending the order number to the kafka topic where the topic name is order-topic
                // Created a POJO class OrderPlacedEvent to send the order number
                // You can also make the topic a default topic by adding it to the application.yml file
                kafkaTemplate.send("notification-topic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order placed successfully";
            }else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        } finally {
            inventoryServiceLookup.end();
        }
    }
// Change this to builder
    private OrderLineItems mapToEntity(OrderLineItemsDTO orderLineItemsDTO){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
