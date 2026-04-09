package com.learningTutorial.inventory_service.controller;

import com.learningTutorial.inventory_service.dto.InventoryResponse;
import com.learningTutorial.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    public final InventoryService inventoryService;

    //http://localhost:8080/api/inventory?skuCode=123456&skuCode=654321
    @GetMapping // Accepts skuCode from the controller as a path variable
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode  ){
        return inventoryService.isInStock(skuCode);
    }
}
