<template>
  <div class="detail-container" v-loading="loading">
    <div class="header">
      <el-button link @click="$router.back()">← Back</el-button>
      <span class="title">Order Details</span>
    </div>

    <div v-if="order" class="content">
      <el-card class="box-card">
        <el-steps :active="order.status + 1" align-center finish-status="success">
          <el-step title="Order Created" :description="formatDate(order.orderDate)" />
          <el-step title="Paid " />
          <el-step title="Sending" />
          <el-step title="Done" />
        </el-steps>
      </el-card>

      <el-descriptions title="Info" bordered :column="2" class="desc-box">
        <el-descriptions-item label="Order Id">
          {{ order.orderId }}
        </el-descriptions-item>
        
        <el-descriptions-item label="Status">
           <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
        </el-descriptions-item>
        
        <el-descriptions-item label="Total">
          <span class="total-price">¥{{ order.totalAmount }}</span>
        </el-descriptions-item>
        
        <el-descriptions-item label="Address">
          <el-icon><LocationInformation /></el-icon> 
          {{ order.state }} {{ order.city }} ({{ order.zipCode }})
        </el-descriptions-item>
        
        <el-descriptions-item label="Create Time">
          {{ formatDate(order.orderDate) }}
        </el-descriptions-item>
      </el-descriptions>

      <el-table :data="order.items" border style="margin-top: 20px">
        <el-table-column label="Product Info" min-width="300">
          <template #default="{ row }">
            <div class="product-info">
              <el-image 
                :src="row.skuImage" 
                class="thumb" 
                fit="contain"
                :preview-src-list="[row.skuImage]" 
                preview-teleported
              >
                <template #error>
                  <div class="img-placeholder"><el-icon><Picture /></el-icon></div>
                </template>
              </el-image>
              
              <div class="info-text">
                <div class="p-name">{{ row.productName }}</div>
                <div class="p-specs">
                  <el-tag size="small" type="info" effect="plain">
                    {{ formatSpecs(row.specs) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="Price" width="150" align="center">
          <template #default="{ row }">
            ¥{{ row.priceAtPurchase.toFixed(2) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="quantity" label="Quantity" width="100" align="center" />
        
        <el-table-column label="Total" width="150" align="right">
          <template #default="{ row }">
            <b style="color: #f56c6c">¥{{ (row.priceAtPurchase * row.quantity).toFixed(2) }}</b>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-empty v-else-if="!loading" description="Cannot Find the Order" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetailApi } from '@/api/index'
import { LocationInformation, Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const orderId = route.params.id
const loading = ref(true)
const order = ref(null)

onMounted(async () => {
  if (!orderId) return
  try {
    const res = await getOrderDetailApi(orderId)
    if (res.code === 200) {
      order.value = res.data
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Fail to load the order detail.')
  } finally {
    loading.value = false
  }
})

// === 辅助函数 ===

// 状态映射
const getStatusText = (status) => {
  const map = { 0: 'Ready to Pay', 1: 'Paid', 2: 'Sending', 3: 'Done', 4: 'Canceled' }
  return map[status] || 'Unkown Status'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'primary', 3: 'success', 4: 'info' }
  return map[status] || 'info'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString()
}

const formatSpecs = (specs) => {
  if (!specs) return 'Default Spec'
  try {
    const obj = typeof specs === 'string' ? JSON.parse(specs) : specs
    return Object.values(obj).join(' / ')
  } catch (e) {
    return String(specs)
  }
}
</script>

<style scoped>
.detail-container {
    max-width: 1000px;
    margin: 40px auto;
    padding: 0 20px;
}

.header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    gap: 20px;
}

.title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
}

.box-card {
    margin-bottom: 20px;
    padding: 20px 0;
}

.desc-box {
    background: #fff;
    padding: 20px;
    border-radius: 4px;
    border: 1px solid #ebeef5;
}

.total-price {
    color: #f56c6c;
    font-weight: bold;
    font-size: 18px;
}

/* 商品列表样式 */
.product-info {
    display: flex;
    gap: 15px;
    align-items: flex-start;
}

.thumb {
    width: 80px;
    height: 80px;
    border: 1px solid #eee;
    border-radius: 6px;
    flex-shrink: 0;
}

.img-placeholder {
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #909399;
    font-size: 20px;
}

.info-text {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.p-name {
    font-weight: 500;
    font-size: 14px;
    color: #303133;
    line-height: 1.4;
}
</style>