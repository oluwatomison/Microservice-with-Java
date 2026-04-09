package com.learningTutorial.product_service.service;

import com.learningTutorial.product_service.dto.ProductRequest;
import com.learningTutorial.product_service.dto.ProductResponse;
import com.learningTutorial.product_service.event.ProductPlacedEvent;
import com.learningTutorial.product_service.model.Product;
import com.learningTutorial.product_service.repository.ProductRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation to inject the dependencies
@Slf4j // Lombok annotation to log the messages
public class ProductService {
    // Accessing the product repository by injecting it to the Product service class
    // Constructor injection
    private  final ProductRespository productRespository;
    // Injecting the KafkaTemplate to send the product placed event
    private final KafkaTemplate<Object, ProductPlacedEvent> kafkaTemplate;
   public void createProduct(ProductRequest productRequest){
        // Building the product object
        Product product = Product.builder()
               .name(productRequest.getName())
               .description(productRequest.getDescription())
               .price(productRequest.getPrice())
               .build();
        // Sending the product to the Kafka topic
        kafkaTemplate.send("notification-product-topic", new ProductPlacedEvent(product.getName(), product.getDescription(), product.getPrice()));
        productRespository.save(product); // Saving the product to the database
        log.info("Product created successfully {}", product.getId()); // Logging the success message
   }

   // Creating a method to get all products
   public List<ProductResponse> getAllProducts(){
       List<Product> products = productRespository.findAll();
       return products.stream().map(this::mapToProductResponse).toList();
   }

   // Creating a method to map the product object to the product response object
   private ProductResponse mapToProductResponse (Product product){
       return ProductResponse.builder()
               .id(product.getId())
               .name(product.getName())
               .description(product.getDescription())
               .price(product.getPrice())
               .build();
    }
}
