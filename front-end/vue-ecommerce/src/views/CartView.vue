<template>
  <div class="cart-container">
    <div class="header-row">
      <h2>Shopping Cart</h2>
      <el-button 
        v-if="cartStore.items.length > 0"
        type="danger" 
        link 
        icon="Delete" 
        @click="handleClear"
      >
        Clear Cart
      </el-button>
    </div>

    <el-card shadow="never" class="cart-card">
      <el-table 
        :data="cartStore.items" 
        style="width: 100%" 
        empty-text="Your cart is empty. Start shopping!"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column label="Product Info" min-width="300">
          <template #default="{ row }">
            <div class="product-cell">
              <div class="img-wrapper">
                <el-image 
                  :src="row.skuImage" 
                  fit="cover" 
                  class="sku-img"
                  :preview-src-list="[row.skuImage]" 
                  preview-teleported
                >
                  <template #error>
                    <div class="img-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              
              <div class="info-wrapper">
                <div class="p-name" :title="row.productName">{{ row.productName }}</div>
                <div class="specs-tag" v-if="row.specs">
                  <el-tag type="info" size="small" effect="plain">
                    {{ formatSpecs(row.specs) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="price" width="120" align="center">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price.toFixed(2) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="quantity" width="160" align="center">
          <template #default="{ row }">
            <el-input-number 
              v-model="row.quantity" 
              :min="1" 
              :max="99" 
              size="small" 
              @change="(val) => handleQuantityChange(row, val)"
            />
          </template>
        </el-table-column>

        <el-table-column label="subtotal" width="120" align="center">
          <template #default="{ row }">
            <span class="subtotal-text">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="action" width="100" align="center">
          <template #default="{ row }">
            <el-button 
              type="danger" 
              icon="Delete" 
              circle 
              plain
              @click="cartStore.removeFromCart(row.skuId)" 
            />
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer" v-if="cartStore.items.length > 0">
        <div class="footer-left">
          <span><b class="count">{{ cartStore.totalCount }}</b>  items in cart</span>
        </div>
        <div class="footer-right">
          <span class="label">Total Price:</span>
          <span class="total-price">¥{{ cartStore.totalPrice }}</span>
          <el-button type="primary" size="large" class="checkout-btn" @click="$router.push('/checkout')">Pay</el-button>
        </div>  
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useCartStore } from '@/stores/cart'
import { Picture, Delete } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const cartStore = useCartStore()

onMounted(() => {
  cartStore.fetchCart()
})

// formatter
const formatSpecs = (specs) => {
  if (!specs) return ''
  let specObj = specs
  if (typeof specs === 'string') {
    try {
      specObj = JSON.parse(specs)
    } catch (e) {
      return specs
    }
  }
  return Object.values(specObj).join(' / ')
}

const handleQuantityChange = async (row, newVal) => {
  cartStore.updateQuantity(row.skuId, newVal)
  // console.log(`Update SKU ${row.skuId} quantity to ${newVal}`)
}

const handleClear = () => {
  ElMessageBox.confirm(
    'Clear shopping cart?',
    'WARNING',
    { confirmButtonText: 'CLEAR', cancelButtonText: 'CANCEL', type: 'warning' }
  ).then(() => {
    cartStore.clearCart()
  }).catch(() => {})
}
</script>

<style scoped>
.cart-container {
  max-width: 1100px;
  margin: 40px auto;
  padding: 0 20px;
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.header-row h2 { margin: 0; color: #303133; }

.cart-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

/* 商品信息单元格布局 */
.product-cell {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 5px 0;
}

.img-wrapper {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #f0f0f0;
}

.sku-img {
  width: 100%;
  height: 100%;
}

.img-placeholder {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  font-size: 20px;
}

.info-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  gap: 8px;
}

.p-name {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.specs-tag .el-tag {
  color: #909399;
  background-color: #f4f4f5;
  border-color: #e9e9eb;
}

.price-text {
  color: #606266;
  font-weight: 500;
}

.subtotal-text {
  color: #f56c6c;
  font-weight: bold;
  font-family: Arial, sans-serif;
}

/* 底部结算栏 */
.cart-footer {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left {
  font-size: 14px;
  color: #606266;
}
.count {
  color: #f56c6c;
  font-size: 16px;
  margin: 0 2px;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.label {
  font-size: 14px;
  color: #606266;
}

.total-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  font-family: Arial, sans-serif;
  margin-right: 10px;
}

.checkout-btn {
  width: 120px;
  border-radius: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}
</style>