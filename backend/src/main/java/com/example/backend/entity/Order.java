package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("Orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer userId;
    private Integer shippingAddressId;
    private Integer status; // 0:ready to pay, 1:paid, 2:sending, 3:done, 4:cancel
    private BigDecimal totalAmount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderDate;
}
