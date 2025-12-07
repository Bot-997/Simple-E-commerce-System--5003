package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Wishlist;
import com.example.backend.mapper.WishlistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.dto.WishlistVO;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductSku;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.mapper.ProductSkuMapper;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class WishlistService {

    @Autowired private WishlistMapper wishlistMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private ProductSkuMapper skuMapper;

    /**
     * add
     */
    public void addWishlist(Integer userId, Integer productId) {
        QueryWrapper<Wishlist> query = new QueryWrapper<>();
        query.eq("user_id", userId).eq("product_id", productId);

        Long count = wishlistMapper.selectCount(query);
        if (count > 0) {
            throw new RuntimeException("productId is exist");
        }

        LocalDateTime now = LocalDateTime.now();

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setCreatedAt(now);
        wishlistMapper.insert(wishlist);
    }

    /**
     * remove
     */
    public void removeWishlist(Integer userId, Integer productId) {
        QueryWrapper<Wishlist> query = new QueryWrapper<>();
        query.eq("user_id", userId).eq("product_id", productId);
        wishlistMapper.delete(query);
    }

    /**
     * list
     */
    public List<Wishlist> getUserWishlist(Integer userId) {
        QueryWrapper<Wishlist> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.orderByDesc("created_at");
        return wishlistMapper.selectList(query);
    }

    /**
     * list v2
     */
    public List<WishlistVO> getUserWishlistV2(Integer userId) {
        QueryWrapper<Wishlist> query = new QueryWrapper<>();
        query.eq("user_id", userId).orderByDesc("created_at");
        List<Wishlist> list = wishlistMapper.selectList(query);

        if (list.isEmpty()) return new ArrayList<>();

        List<Integer> productIds = list.stream()
                .map(Wishlist::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productMapper.selectBatchIds(productIds);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        List<ProductSku> allSkus = skuMapper.selectList(
                new QueryWrapper<ProductSku>().in("product_id", productIds)
        );

        Map<Integer, List<ProductSku>> skuMap = allSkus.stream()
                .collect(Collectors.groupingBy(ProductSku::getProductId));


        List<WishlistVO> result = new ArrayList<>();
        for (Wishlist w : list) {
            WishlistVO vo = new WishlistVO();
            BeanUtils.copyProperties(w, vo);

            Product p = productMap.get(w.getProductId());
            if (p != null) {
                vo.setProductName(p.getName());
                vo.setBasePrice(p.getBasePrice());
                vo.setImageUrl(p.getImageUrl());
                vo.setDescription(p.getDescription());
            }

            vo.setSkus(skuMap.get(w.getProductId()));

            result.add(vo);
        }

        return result;
    }
}