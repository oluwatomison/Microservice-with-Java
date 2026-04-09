package com.learningTutorial.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// DATA TRANSFER OBJECT, request body
public class OrderRequest {
    private List<OrderLineItemsDTO> orderLineItemsDTOList;
}
