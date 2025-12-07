package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("Products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private Integer categoryId;
    private String name;
    private BigDecimal basePrice;
    private String manufacturer;
    private String description;
    private String imageUrl;
    private Integer isRecommended;
    private Integer isAd;
}
