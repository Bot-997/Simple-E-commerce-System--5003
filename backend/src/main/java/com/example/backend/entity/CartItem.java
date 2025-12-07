package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("CartItems")
public class CartItem {
    @TableId(type = IdType.AUTO)
    private Integer cartItemId;
    private Integer cartId;
    private Integer skuId;
    private Integer quantity;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}