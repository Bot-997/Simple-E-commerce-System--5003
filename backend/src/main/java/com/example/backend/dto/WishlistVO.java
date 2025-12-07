package com.example.backend.dto;

import com.example.backend.entity.ProductSku;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WishlistVO {
    private Integer wishlistId;
    private Integer productId;
    private LocalDateTime createdAt;

    // product SPU
    private String productName;
    private String description;
    private BigDecimal basePrice;
    private String imageUrl;

    // SKU list
    private List<ProductSku> skus;
}