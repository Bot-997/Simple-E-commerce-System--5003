package com.example.backend.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CreateOrderRequest {
    private Integer userId;
    private Integer addressId;
    // Key: skuId, Value: quantity
    private Map<Integer, Integer> items;
}
