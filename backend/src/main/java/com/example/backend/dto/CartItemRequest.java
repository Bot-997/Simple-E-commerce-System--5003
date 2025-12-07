package com.example.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * object for showing cartItem
 */
@Data
public class CartItemRequest {
    // 购物车条目信息
    private Integer cartItemId;
    private Integer quantity;

    // 商品 SKU 信息
    private Integer skuId;
    private Integer productId;
    private String productName;
    private String skuImage;
    private String skuCode;
    private BigDecimal price;
    private Map<String, Object> specs;
    private BigDecimal subTotal;
}