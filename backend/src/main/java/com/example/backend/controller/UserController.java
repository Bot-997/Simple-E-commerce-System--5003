package com.example.backend.controller;

import com.example.backend.dto.AddressRequest;
import com.example.backend.dto.ChangePasswordRequest;
import com.example.backend.entity.Address;
import com.example.backend.service.AddressService;
import com.example.backend.common.Result;
import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.example.backend.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private AddressService addressService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest req) {
        try {
            User user = userService.login(req.getEmail(), req.getPassword());
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest req) {
        try {
            userService.register(req);
            return Result.success("Register Success, Please Login");
        } catch (Exception e) {
            return Result.error("Register Failed: " + e.getMessage());
        }
    }

    @PostMapping("/password/change")
    public Result<String> changePassword(@RequestBody ChangePasswordRequest req) {
        try {
            userService.changePassword(req);
            return Result.success("Change Password Success, Please Login");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/address/list")
    public Result<List<Address>> listAddress(@RequestParam Integer userId) {
        return Result.success(addressService.getUserAddresses(userId));
    }

    @PostMapping("/address/add")
    public Result<String> addAddress(@RequestBody AddressRequest req) {
        addressService.addAddress(req);
        return Result.success("Add Address Success");
    }


    @PostMapping("/address/update")
    public Result<String> updateAddress(@RequestBody AddressRequest req) {
        addressService.updateAddress(req);
        return Result.success("Address Update Success");
    }

    @PostMapping("/address/remove")
    public Result<String> removeAddress(@RequestBody AddressRequest req) {
        // 前端只需传 addressId
        addressService.deleteAddress(req.getAddressId());
        return Result.success("Address Delete Success");
    }
}
