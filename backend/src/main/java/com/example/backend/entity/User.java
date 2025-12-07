package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("Users")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String email;
    private String passwordHash;
    private Integer isAdmin;    // 0:no, 1:yes
    private LocalDateTime createdAt;
}
