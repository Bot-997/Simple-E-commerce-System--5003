<template>
  <div class="wishlist-container">
    <h2>My Wishlist</h2>
    
    <el-row :gutter="20" v-loading="loading">
      <el-col 
        v-for="item in wishlistStore.items" 
        :key="item.wishlistId" 
        :xs="24" :sm="12" :md="8" :lg="6"
        class="wishlist-col"
      >
        <el-card :body-style="{ padding: '0px' }" shadow="hover" class="wishlist-card">
          <div class="image-placeholder" @click="goToProduct(item)">
            <img v-if="item.imageUrl" :src="item.imageUrl" class="product-img" />
            <span v-else>Product Img</span>
          </div>
          
          <div class="card-body">
            <h3 class="title" @click="goToProduct(item)">{{ item.productName }}</h3>
            <div class="price">¥{{ item.basePrice }}</div>
            
            <div class="actions">
              <el-button type="primary" size="small" @click="goToProduct(item)">
                Start Shopping
              </el-button>
              <el-button 
                type="danger" 
                icon="Delete" 
                circle 
                size="small" 
                plain
                @click="wishlistStore.removeItem(item.productId)"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty 
      v-if="!loading && wishlistStore.items.length === 0" 
      description="Empty" 
    >
      <el-button type="primary" @click="$router.push('/')">Go to Shopping</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useWishlistStore } from '@/stores/wishlist'

const wishlistStore = useWishlistStore()
const router = useRouter()
const loading = ref(true)

onMounted(async () => {
  try {
    await wishlistStore.fetchWishlist()
  } finally {
    loading.value = false
  }
})

const goToProduct = (item) => {
  // 同样利用路由传参带过去基础信息，提升体验
  router.push({
    path: `/product/${item.productId}`,
    query: {
      name: item.productName,
      price: item.basePrice,
      img: item.imageUrl
    }
  })
}
</script>

<style scoped>
.wishlist-container { max-width: 1200px; margin: 20px auto; padding: 0 20px; }
.wishlist-col { margin-bottom: 20px; }
.wishlist-card { border-radius: 8px; overflow: hidden; }
.image-placeholder { height: 180px; background: #f5f7fa; display: flex; align-items: center; justify-content: center; cursor: pointer; }
.product-img { width: 100%; height: 100%; object-fit: contain; }
.card-body { padding: 14px; }
.title { font-size: 16px; margin: 0 0 10px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; cursor: pointer; }
.price { color: #f56c6c; font-weight: bold; margin-bottom: 10px; }
.actions { display: flex; justify-content: space-between; align-items: center; }
</style>