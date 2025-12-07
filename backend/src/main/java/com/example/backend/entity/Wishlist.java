package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("Wishlists")
public class Wishlist {
    @TableId(type = IdType.AUTO)
    private Integer wishlistId;
    private Integer userId;
    private Integer productId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}