package com.example.backend.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private Integer userId;
    private Integer addressId; // 修改/删除时必填
    private String state;
    private String city;
    private String zipCode;
    private Integer isDefault; // 0或1
}