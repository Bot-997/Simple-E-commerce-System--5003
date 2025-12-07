package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
@TableName(value = "Product_SKUs", autoResultMap = true)
public class ProductSku {
    @TableId(type = IdType.AUTO)
    private Integer skuId;
    private Integer productId;
    private String skuCode;
    private BigDecimal price;
    private Integer stockQuantity;
    private String skuImage;

    // 将数据库的 specs 字段 (JSON) 映射为 Map<String, Object>
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> specs;
}