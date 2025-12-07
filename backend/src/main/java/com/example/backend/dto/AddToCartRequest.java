package com.example.backend.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer userId;
    private Integer skuId;
    private Integer quantity;
}
