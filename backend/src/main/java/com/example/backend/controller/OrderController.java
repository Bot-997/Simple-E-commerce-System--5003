package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.CreateOrderRequest;
import com.example.backend.entity.OrderItem;
import com.example.backend.service.OrderService;
import com.example.backend.entity.Order;
import com.example.backend.dto.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/create")
    public Result<Integer> createOrder(@RequestBody CreateOrderRequest req) {
        try {
            Integer orderId = orderService.createOrder(
                    req.getUserId(),
                    req.getAddressId(),
                    req.getItems()
            );
            return Result.success(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Failed Order: " + e.getMessage());
        }
    }

    // order list
    @GetMapping("/my")
    public Result<List<Order>> myOrders(@RequestParam Integer userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    @GetMapping("/detail")
    public Result<OrderDetailVO> orderDetail(@RequestParam Integer orderId) {
        try {
            OrderDetailVO detail = orderService.getOrderDetail(orderId);
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}