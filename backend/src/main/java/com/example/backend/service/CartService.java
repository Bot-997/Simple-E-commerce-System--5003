package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Cart;
import com.example.backend.entity.CartItem;
import com.example.backend.mapper.CartItemMapper;
import com.example.backend.mapper.CartMapper;
import com.example.backend.dto.CartItemRequest;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductSku;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.mapper.ProductSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired private CartMapper cartMapper;
    @Autowired private CartItemMapper cartItemMapper;
    @Autowired private ProductSkuMapper skuMapper;
    @Autowired private ProductMapper productMapper;

    /**
     * get or create cart
     */
    public Cart getOrCreateCart(Integer userId) {
        Cart cart = cartMapper.selectOne(new QueryWrapper<Cart>().eq("user_id", userId));
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartMapper.insert(cart);
        }
        return cart;
    }

    /**
     * add to cart
     */
    public void addToCart(Integer userId, Integer skuId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);

        // check if it exists
        QueryWrapper<CartItem> query = new QueryWrapper<CartItem>()
                .eq("cart_id", cart.getCartId())
                .eq("sku_id", skuId);
        CartItem item = cartItemMapper.selectOne(query);

        if (item != null) {
            // exist
            item.setQuantity(item.getQuantity() + quantity);
            cartItemMapper.updateById(item);
        } else {
            // not exist
            item = new CartItem();
            item.setCartId(cart.getCartId());
            item.setSkuId(skuId);
            item.setQuantity(quantity);
            LocalDateTime now = LocalDateTime.now();
            item.setCreatedAt(now);
            cartItemMapper.insert(item);
        }
    }

    /**
     * update quantity
     */
    public void updateQuantity(Integer userId, Integer skuId, Integer quantity) {
        if (quantity <= 0) {
            // 如果数量小于等于0，视为删除
            removeCartItem(userId, skuId);
            return;
        }

        Cart cart = getOrCreateCart(userId);
        QueryWrapper<CartItem> query = new QueryWrapper<CartItem>()
                .eq("cart_id", cart.getCartId())
                .eq("sku_id", skuId);

        CartItem item = cartItemMapper.selectOne(query);
        if (item != null) {
            item.setQuantity(quantity);
            cartItemMapper.updateById(item);
        }
    }

    /**
     * remove from cart
     */
    public void removeCartItem(Integer userId, Integer skuId) {
        Cart cart = getOrCreateCart(userId);
        QueryWrapper<CartItem> query = new QueryWrapper<CartItem>()
                .eq("cart_id", cart.getCartId())
                .eq("sku_id", skuId);
        cartItemMapper.delete(query);
    }

    /**
     * clean the cart
     */
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        QueryWrapper<CartItem> query = new QueryWrapper<CartItem>()
                .eq("cart_id", cart.getCartId());
        cartItemMapper.delete(query);
    }

    /**
     * cart list
     */
    public List<CartItem> listCartItems(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        return cartItemMapper.selectList(new QueryWrapper<CartItem>().eq("cart_id", cart.getCartId()));
    }

    /**
     * cart list V2
     */
    public List<CartItemRequest> listCartItemsV2(Integer userId) {
        Cart cart = getOrCreateCart(userId);

        List<CartItem> cartItems = cartItemMapper.selectList(
                new QueryWrapper<CartItem>().eq("cart_id", cart.getCartId())
        );

        if (cartItems.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> skuIds = cartItems.stream()
                .map(CartItem::getSkuId)
                .collect(Collectors.toList());


        List<ProductSku> skus = skuMapper.selectBatchIds(skuIds);
        // Key=skuId, Value=ProductSku
        Map<Integer, ProductSku> skuMap = skus.stream()
                .collect(Collectors.toMap(ProductSku::getSkuId, sku -> sku));

        // get all productIds
        List<Integer> productIds = skus.stream()
                .map(ProductSku::getProductId)
                .distinct()
                .collect(Collectors.toList());

        // query Products info by productIds
        List<Product> products = productMapper.selectBatchIds(productIds);
        // Map: Key=productId, Value=Product
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        List<CartItemRequest> result = new ArrayList<>();

        for (CartItem item : cartItems) {
            CartItemRequest vo = new CartItemRequest();

            vo.setCartItemId(item.getCartItemId());
            vo.setQuantity(item.getQuantity());
            vo.setSkuId(item.getSkuId());

            ProductSku sku = skuMap.get(item.getSkuId());
            if (sku != null) {
                vo.setPrice(sku.getPrice());
                vo.setSkuImage(sku.getSkuImage());
                vo.setSpecs(sku.getSpecs());
                vo.setProductId(sku.getProductId());
                vo.setSkuCode(sku.getSkuCode());

                if (sku.getPrice() != null) {
                    vo.setSubTotal(sku.getPrice().multiply(new BigDecimal(item.getQuantity())));
                }

                Product product = productMap.get(sku.getProductId());
                if (product != null) {
                    vo.setProductName(product.getName());
                    if (vo.getSkuImage() == null) {
                        vo.setSkuImage(product.getImageUrl());
                    }
                }
            }

            result.add(vo);
        }

        return result;
    }
}
