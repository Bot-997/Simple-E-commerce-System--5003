package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.WishlistVO;
import com.example.backend.entity.Wishlist;
import com.example.backend.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // add
    // POST /api/wishlist/add
    // Body: { "userId": 2, "productId": 1 }
    @PostMapping("/add")
    public Result<String> add(@RequestBody Map<String, Integer> req) {
        try {
            Integer userId = req.get("userId");
            Integer productId = req.get("productId");
            wishlistService.addWishlist(userId, productId);
            return Result.success("收藏成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // remove
    // POST /api/wishlist/remove
    // Body: { "userId": 2, "productId": 1 }
    @PostMapping("/remove")
    public Result<String> remove(@RequestBody Map<String, Integer> req) {
        Integer userId = req.get("userId");
        Integer productId = req.get("productId");
        wishlistService.removeWishlist(userId, productId);
        return Result.success("已取消收藏");
    }

    // list
    // GET /api/wishlist/list?userId=2
    @GetMapping("/list")
    public Result<List<WishlistVO>> list(@RequestParam Integer userId) {
        // 调用增强版的 Service 方法，返回带有商品名、价格、SKU规格的数据
        List<WishlistVO> list = wishlistService.getUserWishlistV2(userId);
        return Result.success(list);
    }
}