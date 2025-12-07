package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("Carts")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer cartId;
    private Integer userId;
}