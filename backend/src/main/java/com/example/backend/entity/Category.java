package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Categories")
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer categoryId;
    private String name;
    private Integer parentId;
}