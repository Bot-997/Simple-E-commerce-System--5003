package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.dto.AddressRequest;
import com.example.backend.entity.Address;
import com.example.backend.mapper.AddressMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired private AddressMapper addressMapper;

    // 获取用户所有地址
    public List<Address> getUserAddresses(Integer userId) {
        return addressMapper.selectList(new QueryWrapper<Address>().eq("user_id", userId));
    }

    // 添加地址
    @Transactional
    public void addAddress(AddressRequest req) {
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefaultAddress(req.getUserId());
        }
        Address address = new Address();
        BeanUtils.copyProperties(req, address); // 属性拷贝
        addressMapper.insert(address);
    }

    // 修改地址
    @Transactional
    public void updateAddress(AddressRequest req) {
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefaultAddress(req.getUserId());
        }
        Address address = new Address();
        BeanUtils.copyProperties(req, address);
        addressMapper.updateById(address);
    }

    public void deleteAddress(Integer addressId) {
        addressMapper.deleteById(addressId);
    }

    private void clearDefaultAddress(Integer userId) {
        Address update = new Address();
        update.setIsDefault(0);
        addressMapper.update(update, new QueryWrapper<Address>().eq("user_id", userId));
    }
}