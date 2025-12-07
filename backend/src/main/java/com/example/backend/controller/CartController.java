package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.UpdateCartRequest;
import com.example.backend.dto.AddToCartRequest;
import com.example.backend.service.CartService;
import com.example.backend.dto.CartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;

    // add to cart
    @PostMapping("/add")
    public Result<String> addToCart(@RequestBody AddToCartRequest req) {
        cartService.addToCart(req.getUserId(), req.getSkuId(), req.getQuantity());
        return Result.success("Added Successfully");
    }

    // cart list
    @GetMapping("/list")
    public Result<List<CartItemRequest>> list(@RequestParam Integer userId) {
        List<CartItemRequest> list = cartService.listCartItemsV2(userId);
        return Result.success(list);
    }

    // remove from cart
    @PostMapping("/remove")
    public Result<String> remove(@RequestBody Map<String, Integer> req) {
        // example: { "userId": 2, "skuId": 105 }
        Integer userId = req.get("userId");
        Integer skuId = req.get("skuId");
        cartService.removeCartItem(userId, skuId);
        return Result.success("Delete Successfully");
    }

    // Clear the cart
    @PostMapping("/clear")
    public Result<String> clear(@RequestBody Map<String, Integer> req) {
        // example: { "userId": 2 }
        Integer userId = req.get("userId");
        cartService.clearCart(userId);
        return Result.success("Clear Successfully");
    }

    // update quantity
    @PostMapping("/update")
    public Result<String> updateQuantity(@RequestBody UpdateCartRequest req) {
        cartService.updateQuantity(req.getUserId(), req.getSkuId(), req.getQuantity());
        return Result.success("Updated Successfully");
    }
}