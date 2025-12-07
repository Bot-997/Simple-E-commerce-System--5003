<template>
  <div class="checkout-container">
    <el-steps :active="1" finish-status="success" simple style="margin-bottom: 20px">
      <el-step title="购物车" />
      <el-step title="填写订单" />
      <el-step title="完成支付" />
    </el-steps>

    <div class="content-wrapper" v-loading="loading">
      <el-card class="section-card">
        <template #header>
          <div class="card-header">
            <span>收货地址 / Shipping Address</span>
            <el-button type="primary" link @click="$router.push('/profile')">管理地址</el-button>
          </div>
        </template>
        
        <div v-if="addressList.length > 0">
          <el-radio-group v-model="selectedAddressId" class="address-group">
            <el-radio 
              v-for="addr in addressList" 
              :key="addr.addressId" 
              :label="addr.addressId" 
              border 
              class="address-item"
            >
              <div class="addr-content">
                <span class="tag" v-if="addr.isDefault">默认</span>
                <span class="text">{{ addr.state }} {{ addr.city }} (邮编: {{ addr.zipCode }})</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
        
        <el-empty v-else description="No Address">
          <el-button type="primary" @click="$router.push('/profile')">Add an Address</el-button>
        </el-empty>
      </el-card>

      <el-card class="section-card" header="Order Items">
        <el-table :data="cartStore.items" stripe>
          <el-table-column prop="productName" label="product" />
          <el-table-column prop="quantity" label="quantity" width="80" />
          <el-table-column label="subtotal" width="120" align="right">
            <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
          </el-table-column>
        </el-table>
      </el-card>

      <div class="submit-bar">
        <div class="price-info">
          Total: <span class="final-price">¥{{ cartStore.totalPrice }}</span>
        </div>
        <el-button 
          type="primary" 
          size="large" 
          :loading="submitting" 
          :disabled="!selectedAddressId"
          @click="handleSubmitOrder"
        >
          {{ selectedAddressId ? 'Create Order' : 'Select Address' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { getAddressListApi, createOrderApi } from '@/api/index'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(true)
const submitting = ref(false)
const addressList = ref([])
const selectedAddressId = ref(null)

onMounted(async () => {
  if (cartStore.items.length === 0) {
    ElMessage.warning('Cart is Empty!')
    router.replace('/cart')
    return
  }
  
  // 获取地址列表
  try {
    const res = await getAddressListApi(userStore.userId)
    if (res.code === 200) {
      addressList.value = res.data || []
      const defaultAddr = addressList.value.find(a => a.isDefault === 1)
      if (defaultAddr) {
        selectedAddressId.value = defaultAddr.addressId
      } else if (addressList.value.length > 0) {
        selectedAddressId.value = addressList.value[0].addressId
      }
    }
  } finally {
    loading.value = false
  }
})

const handleSubmitOrder = async () => {
  if (!selectedAddressId.value) return ElMessage.warning('Please Select Address')
  
  submitting.value = true
  try {
    const itemsMap = {}
    cartStore.items.forEach(item => {
      itemsMap[item.skuId] = item.quantity
    })

    const res = await createOrderApi({
      userId: userStore.userId,
      addressId: selectedAddressId.value,
      items: itemsMap
    })
    
    if (res.code === 200) {
      ElMessage.success('Orders Create')
      await cartStore.clearCart(true)
      router.replace('/orders')
    }
  } catch (e) {
    ElMessage.error(e.msg || 'Fail to Create Orders')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.checkout-container {
    max-width: 1000px;
    margin: 30px auto;
    padding: 0 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.section-card {
    margin-bottom: 20px;
}

.address-group {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.address-item {
    width: 100%;
    margin-left: 0 !important;
    height: auto;
    padding: 10px;
}

.addr-content {
    display: flex;
    align-items: center;
    gap: 10px;
}

.tag {
    background: #f0f9eb;
    color: #67c23a;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
}

.submit-bar {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    background: #fff;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.final-price {
    font-size: 28px;
    color: #f56c6c;
    font-weight: bold;
    margin-left: 10px;
}
</style>