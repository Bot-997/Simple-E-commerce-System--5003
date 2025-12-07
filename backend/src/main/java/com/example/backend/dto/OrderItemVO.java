package com.example.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class OrderItemVO {
    private Integer skuId;
    private Integer quantity;
    private BigDecimal priceAtPurchase; // 购买时的单价

    // 需补充的商品信息
    private String productName;
    private String skuImage;
    private Map<String, Object> specs; // 规格 JSON
}