<template>
  <div class="home-container" v-loading="loading">
    <div class="carousel-box">
      <el-carousel height="320px" :interval="5000" type="card">
        <el-carousel-item v-for="item in 3" :key="item">
          <div class="carousel-content" :class="`bg-${item}`">
            <h2>New Arrivals</h2>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="section-header">
      <h2>Featured Products</h2>
    </div>

    <el-row :gutter="20">
      <el-col 
        v-for="p in products" :key="p.productId" 
        :xs="24" :sm="12" :md="8" :lg="6" 
        style="margin-bottom: 20px;"
      >
        <ProductCard :product="p" @click-detail="goDetail(p)" />
      </el-col>
    </el-row>

    <el-empty v-if="!loading && products.length === 0" description="暂无商品" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductListApi } from '@/api/index'
import ProductCard from '../components/ProductCart.vue'
import { useProductStore } from '../stores/product'

const products = ref([])
const loading = ref(true)
const router = useRouter()
const productStore = useProductStore()

onMounted(async () => {
  try {
    const res = await getProductListApi({ page: 1, size: 100 })
    if (res.code === 200) {
      products.value = res.data.records || []
    }
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
})

const goDetail = (product) => {
  productStore.setProduct(product)
  
  router.push(`/product/${product.productId}`)
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.carousel-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  border-radius: 8px;
}

.bg-1 {
  background: linear-gradient(to right, #4facfe, #00f2fe);
}

.bg-2 {
  background: linear-gradient(to right, #43e97b, #38f9d7);
}

.bg-3 {
  background: linear-gradient(to right, #fa709a, #fee140);
}

.section-header {
  margin: 30px 0 20px;
  border-left: 5px solid #409EFF;
  padding-left: 10px;
}
</style>