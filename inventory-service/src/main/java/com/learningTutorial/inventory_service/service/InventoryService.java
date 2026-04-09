package com.learningTutorial.inventory_service.service;

import com.learningTutorial.inventory_service.dto.InventoryResponse;
import com.learningTutorial.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    // Inject the InventoryRepository to access the database
    public final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows //  Don't use in production code
    public List<InventoryResponse> isInStock(List<String> skuCode){
//        log.info("Wait started");
//        Thread.sleep(1000); // Simulate a delay
//        log.info("Wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .inStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
