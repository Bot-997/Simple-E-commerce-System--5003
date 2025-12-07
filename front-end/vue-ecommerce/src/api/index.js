import request from '@/utils/request'

// === 用户模块 ===

// data: { email, password }
// 登录
export const loginApi = (data) => {
  return request.post('/user/login', data)
}

// 注册 
export const registerApi = (data) => {
  return request.post('/user/register', data)
}

export const changePasswordApi = (data) => {
  // data: { userId, oldPassword, newPassword }
  return request.post('/user/password/change', data)
}

//  获取地址列表
export const getAddressListApi = (userId) => {
  return request.get('/user/address/list', { params: { userId } })
}

// 添加地址
export const addAddressApi = (data) => {
  // data: { userId, state, city, zipCode, isDefault }
  return request.post('/user/address/add', data)
}

// 删除地址 (假设接口，文档未显式列出但功能要求"删"，按惯例补充)
export const deleteAddressApi = (data) => {
  // data: { userId, addressId }
  return request.post('/user/address/delete', data)
}

// === 商品模块 ===

// 获取分页商品列表
export const getProductListApi = (params) => {
  // params: { page, size, categoryId(可选) }
  return request.get('/product/list', { params })
}

// 获取商品 SKU 详情
export const getProductSkusApi = (productId) => {
  return request.get(`/product/${productId}/skus`)
}


// === 购物车模块 ===

// 获取购物车列表
export const getCartListApi = (userId) => {
  return request.get('/cart/list', { params: { userId } })
}

// 添加到购物车
export const addToCartApi = (data) => {
  // data: { userId, skuId, quantity }
  return request.post('/cart/add', data)
}

// 移除购物车商品
export const removeCartItemApi = (data) => {
  // data: { userId, skuId }
  return request.post('/cart/remove', data)
}

// 修改物品数量
export const updateCartItemApi = (data) => {
  // data: { userId, skuId, quantity }
  return request.post('/cart/update', data)
}

// 清空购物车
export const clearCartApi = (data) => {
  // data: { userId }
  return request.post('/cart/clear', data)
}

// === 订单模块 ===

// 创建订单
export const createOrderApi = (data) => {
  // data: { userId, addressId, ... }
  return request.post('/order/create', data)
}

// 获取订单列表
export const getOrderListApi = (userId) => {
  return request.get('/order/my', { params: { userId } })
}

// 获取订单详情
export const getOrderDetailApi = (orderId) => {
  return request.get(`/order/detail`, { params: { orderId } })
}

// === 愿望单模块 ===

// 添加到愿望单
export const addToWishlistApi = (data) => {
  // data: { userId, productId }
  return request.post('/wishlist/add', data)
}

//  获取愿望单列表
export const getWishlistApi = (userId) => {
  return request.get('/wishlist/list', { params: { userId } })
}

// 移除愿望单
export const removeWishlistApi = (data) => {
  return request.post('/wishlist/remove', data)
}