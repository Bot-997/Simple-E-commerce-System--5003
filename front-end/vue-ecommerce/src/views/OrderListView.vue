<template>
  <div class="orders-container">
    <h2>My Orders</h2>
    
    <el-table 
      :data="orderList" 
      v-loading="loading" 
      style="width: 100%" 
      empty-text="Empty"
    >
      <el-table-column prop="orderId" label="Order ID" width="100" />
      
      <el-table-column label="Order Date" width="180">
        <template #default="{ row }">
          {{ formatDate(row.orderDate) }}
        </template>
      </el-table-column>
      
      <el-table-column label="Total">
        <template #default="{ row }">
          <span style="font-weight: bold">¥{{ row.totalAmount }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="Status">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="Action" width="120" align="center">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row.orderId)">
            Detail
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrderListApi } from '@/api/index'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const orderList = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getOrderListApi(userStore.userId)
    if (res.code === 200) {
      orderList.value = res.data || []
    }
  } finally {
    loading.value = false
  }
})

const viewDetail = (id) => {
  router.push(`/order/${id}`)
}

// 状态映射 (根据 SQL 注释: 0: ready to pay, 1: payed, 2: sending, 3: done, 4: cancel)
const getStatusText = (status) => {
  const map = { 0: '待支付', 1: '已支付', 2: '发货中', 3: '已完成', 4: '已取消' }
  return map[status] || '未知状态'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'primary', 3: 'success', 4: 'info' }
  return map[status] || 'info'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString()
}
</script>

<style scoped>
.orders-container { max-width: 1000px; margin: 40px auto; padding: 0 20px; }
</style>