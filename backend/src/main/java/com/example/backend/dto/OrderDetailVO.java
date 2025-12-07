package com.example.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    // 订单基础信息
    private Integer orderId;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;

    // 收货地址信息
    private Integer addressId;
    private String state;
    private String city;
    private String zipCode;

    // 购买的商品列表
    private List<OrderItemVO> items;
}
