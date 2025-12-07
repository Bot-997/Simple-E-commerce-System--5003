package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.dto.ChangePasswordRequest;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String email, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null || !user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password or email");
        }
        return user;
    }

    public void register(RegisterRequest req) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", req.getEmail());
        if (userMapper.selectCount(query) > 0) {
            throw new RuntimeException("Email Exists");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(req.getPassword());
        user.setIsAdmin(0);

        userMapper.insert(user);
    }

    public void changePassword(ChangePasswordRequest req) {
        User user = userMapper.selectById(req.getUserId());
        if (user == null) throw new RuntimeException("User doesn't exist");

        // check old password
        if (!user.getPasswordHash().equals(req.getOldPassword())) {
            throw new RuntimeException("Old Password Doesn't Match");
        }

        // change password
        user.setPasswordHash(req.getNewPassword());
        userMapper.updateById(user);
    }
}
