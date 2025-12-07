package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("OrderItems")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer orderItemId;
    private Integer orderId;
    private Integer skuId;
    private BigDecimal priceAtPurchase;
    private Integer quantity;
}
