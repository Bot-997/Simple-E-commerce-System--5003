package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Addresses")
public class Address {
    @TableId(type = IdType.AUTO)
    private Integer addressId;
    private Integer userId;
    private String state;
    private String city;
    private String zipCode;
    private Integer isDefault;
}