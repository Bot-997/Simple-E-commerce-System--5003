package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductSku;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.mapper.ProductSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired private ProductMapper productMapper;
    @Autowired private ProductSkuMapper skuMapper;

    /**
     * get product list by pages
     * @param page page number，1 as default
     * @param size page size，10 as default
     * @param categoryId (optional)
     */
    @GetMapping("/list")
    public Result<IPage<Product>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer categoryId) {

        Page<Product> pageParam = new Page<>(page, size);

        QueryWrapper<Product> query = new QueryWrapper<>();
        if (categoryId != null) {
            query.eq("category_id", categoryId);
        }

        // ordered by is_recommended
        query.orderByDesc("is_recommended");

        IPage<Product> result = productMapper.selectPage(pageParam, query);

        return Result.success(result);
    }

    // 获取商品详情（包含 SKU 列表）
    @GetMapping("/{id}/skus")
    public Result<List<ProductSku>> getProductSkus(@PathVariable Integer id) {
        return Result.success(skuMapper.selectList(
                new QueryWrapper<ProductSku>().eq("product_id", id)
        ));
    }
}
