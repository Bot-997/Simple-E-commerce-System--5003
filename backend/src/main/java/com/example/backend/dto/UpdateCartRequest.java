package com.example.backend.dto;

import lombok.Data;

@Data
public class UpdateCartRequest {
    private Integer userId;
    private Integer skuId;
    private Integer quantity; // 新的数量
}
