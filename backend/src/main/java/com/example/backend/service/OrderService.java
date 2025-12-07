package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.dto.OrderDetailVO;
import com.example.backend.dto.OrderItemVO;
import com.example.backend.entity.Address;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductSku;
import com.example.backend.mapper.AddressMapper;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.entity.ProductSku;
import com.example.backend.mapper.OrderItemMapper;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.mapper.ProductSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired private ProductSkuMapper skuMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private OrderItemMapper orderItemMapper;
    @Autowired private AddressMapper addressMapper;
    @Autowired private ProductMapper productMapper;

    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(Integer userId, Integer addressId, Map<Integer, Integer> buyList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // traverse cart and sum price
        for (Map.Entry<Integer, Integer> entry : buyList.entrySet()) {
            Integer skuId = entry.getKey();
            Integer quantity = entry.getValue();

            ProductSku sku = skuMapper.selectById(skuId);
            if (sku == null) throw new RuntimeException("商品不存在: " + skuId);
            if (sku.getStockQuantity() < quantity) {
                throw new RuntimeException("商品 " + sku.getSkuCode() + " 库存不足");
            }

            sku.setStockQuantity(sku.getStockQuantity() - quantity);
            skuMapper.updateById(sku);


            BigDecimal itemTotal = sku.getPrice().multiply(new BigDecimal(quantity));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setSkuId(skuId);
            item.setQuantity(quantity);
            item.setPriceAtPurchase(sku.getPrice());
            orderItems.add(item);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 0: ready to pay
        order.setOrderDate(LocalDateTime.now());
        orderMapper.insert(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
            orderItemMapper.insert(item);
        }

        return order.getOrderId();
    }

    public List<Order> getUserOrders(Integer userId) {
        QueryWrapper<Order> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.orderByDesc("order_date");
        return orderMapper.selectList(query);
    }


    public List<OrderItem> getOrderItems(Integer orderId) {
        QueryWrapper<OrderItem> query = new QueryWrapper<>();
        query.eq("order_id", orderId);
        return orderItemMapper.selectList(query);
    }

    public OrderDetailVO getOrderDetail(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }

        Address address = addressMapper.selectById(order.getShippingAddressId());

        List<OrderItem> items = orderItemMapper.selectList(
                new QueryWrapper<OrderItem>().eq("order_id", orderId)
        );

        OrderDetailVO detailVO = new OrderDetailVO();
        BeanUtils.copyProperties(order, detailVO);

        if (address != null) {
            BeanUtils.copyProperties(address, detailVO);
        }

        if (!items.isEmpty()) {
            List<Integer> skuIds = items.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
            List<ProductSku> skus = skuMapper.selectBatchIds(skuIds);
            Map<Integer, ProductSku> skuMap = skus.stream().collect(Collectors.toMap(ProductSku::getSkuId, s -> s));

            List<Integer> productIds = skus.stream().map(ProductSku::getProductId).distinct().collect(Collectors.toList());
            List<Product> products = productMapper.selectBatchIds(productIds);
            Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, p -> p));

            List<OrderItemVO> itemVOs = new ArrayList<>();
            for (OrderItem item : items) {
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item, itemVO);

                ProductSku sku = skuMap.get(item.getSkuId());
                if (sku != null) {
                    itemVO.setSpecs(sku.getSpecs());
                    itemVO.setSkuImage(sku.getSkuImage());

                    Product p = productMap.get(sku.getProductId());
                    if (p != null) {
                        itemVO.setProductName(p.getName());
                        if (itemVO.getSkuImage() == null) {
                            itemVO.setSkuImage(p.getImageUrl());
                        }
                    }
                }
                itemVOs.add(itemVO);
            }
            detailVO.setItems(itemVOs);
        } else {
            detailVO.setItems(new ArrayList<>());
        }

        return detailVO;
    }
}
