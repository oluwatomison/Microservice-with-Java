package com.learningTutorial.product_service;

import com.learningTutorial.product_service.dto.ProductRequest;
import com.learningTutorial.product_service.dto.ProductResponse;
import com.learningTutorial.product_service.model.Product;
import com.learningTutorial.product_service.repository.ProductRespository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    // Writing integration tests for the product service
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.10");
    @Autowired
    private MockMvc mockMvc; // Injecting the MockMvc object to test the REST API
    @Autowired
    private ObjectMapper objectMapper; // Injecting the ObjectMapper to convert the ProductRequest to JSON
    @Autowired
    private ProductRespository productRepository;

//    @AfterEach
//    void tearDown() {
//        productRepository.deleteAll(); // Wipes the MongoDB collection clean!
//    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

	@Test
	void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        // Converting the product request to JSON using jackson
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        // URLS have a separate class with a list of urls.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString) // Accepts string
        ).andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

    private ProductRequest getProductRequest(){
        return ProductRequest.builder()
                .name("iphone 13")
                .description("iphone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    @Test
    void ShouldGetAllProducts() throws Exception {
        // 1. ARRANGE: Actually save the product into your test database so the API has something to find
        Product product = Product.builder()
                .name("iphone 13")
                .description("iphone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
        productRepository.save(product);
        // 2. ACT & ASSERT: Call the endpoint and inspect the JSON it returns
       mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
               .contentType(MediaType.APPLICATION_JSON)
                       // 3 Expect a 200 OK
       ).andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("iphone 13"))
               .andExpect(jsonPath("$[0].description").value("iphone 13"))
               .andExpect(jsonPath("$[0].price").value(1200));

    }
}
