<template>
  <div class="detail-wrapper" v-loading="loading">
    <el-button link @click="$router.back()" style="margin-bottom: 20px;">← Back</el-button>
    
    <div class="content" v-if="productInfo.name || skus.length > 0">
      <div class="img-box">
        <img v-if="displayImage" :src="displayImage" class="detail-img" />
        <div v-else class="placeholder-text">Product Image</div>
      </div>
      
      <div class="info-box">
        <h1 class="product-title">{{ productInfo.name || 'Loading...' }}</h1>
        
        <div class="price-area">
           ¥ <span class="price-num">{{ currentSku ? currentSku.price : displayPriceRange }}</span>
        </div>

        <div v-for="(options, key) in specMap" :key="key" class="spec-row">
          <div class="spec-label">{{ key }}:</div>
          <div class="spec-options">
            <el-button 
              v-for="opt in options" 
              :key="opt" 
              :type="selectedSpecs[key] === opt ? 'primary' : ''"
              :disabled="checkOptionDisabled(key, opt)" 
              size="small"
              @click="selectSpec(key, opt)"
            >
              {{ opt }}
            </el-button>
          </div>
        </div>

        <div class="actions">
          <el-input-number v-model="quantity" :min="1" :max="99" />
          <el-button 
            type="primary" 
            size="large" 
            :disabled="!currentSku"
            @click="handleAddToCart"
            style="margin-left: 20px; width: 200px;"
          >
            {{ currentSku ? 'Add to Cart' : 'Select Specs' }}
          </el-button>

          <el-button 
            :type="isFav ? 'warning' : 'default'" 
            size="large" 
            circle
            @click="handleToggleWishlist"
            title="Add to Whishlist"
          >
            <el-icon size="20">
              <StarFilled v-if="isFav" />
              <Star v-else />
            </el-icon>
          </el-button>
        </div>
      </div>
    </div>
    
    <el-empty v-else description="No info about this product." />
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { ref, computed, onMounted, reactive } from 'vue'
import { getProductSkusApi, getProductListApi } from '@/api/index'
import { useCartStore } from '@/stores/cart'
import { useWishlistStore } from '@/stores/wishlist'
import { useProductStore } from '@/stores/product'
import { ElMessage } from 'element-plus'

const route = useRoute()
const cartStore = useCartStore()
const productStore = useProductStore()
const wishlistStore = useWishlistStore()
const productId = route.params.id
const loading = ref(true)

const skus = ref([])
const selectedSpecs = ref({})
const quantity = ref(1)

const productInfo = reactive({
  name: '',
  price: '',
  img: ''
})

onMounted(async () => {
  try {
    // Initial
    if (productStore.currentProduct && productStore.currentProduct.productId == productId) {
      const p = productStore.currentProduct
      productInfo.name = p.name
      productInfo.price = p.basePrice
      productInfo.img = p.imageUrl
    } else {
      await fetchProductInfoFallback()
    }

    const res = await getProductSkusApi(productId)
    if (res.code === 200) skus.value = res.data || []
    
    await wishlistStore.fetchWishlist()
    
  } catch(e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})

const fetchProductInfoFallback = async () => {
  try {
    const res = await getProductListApi({ page: 1, size: 100 })
    if (res.code === 200 && res.data.records) {
      const target = res.data.records.find(p => p.productId == productId)
      if (target) {
        productInfo.name = target.name
        productInfo.price = target.basePrice
        productInfo.img = target.imageUrl
      }
    }
  } catch (err) {}
}

// 计算当前商品是否在愿望单中
const isFav = computed(() => {
  if (!wishlistStore.items) return false
  return wishlistStore.items.some(item => item.productId == productId)
})

const handleToggleWishlist = async () => {
  const productPayload = {
    productId: productId,
    productName: productInfo.name,
    basePrice: productInfo.price,
    imageUrl: productInfo.img
  }
  
  await wishlistStore.toggleWishlist(productPayload)
}

// === 计算属性 ===

// 解析所有可用规格
const specMap = computed(() => {
  const map = {}
  skus.value.forEach(sku => {
    if (!sku.specs) return
    const specs = typeof sku.specs === 'string' ? JSON.parse(sku.specs) : sku.specs
    Object.keys(specs).forEach(key => {
      if (!map[key]) map[key] = new Set()
      map[key].add(specs[key])
    })
  })
  const result = {}
  for (const k in map) {
    result[k] = Array.from(map[k])
  }
  return result
})

// 计算当前选中的完整 SKU
const currentSku = computed(() => {
  const keys = Object.keys(specMap.value)
  if (Object.keys(selectedSpecs.value).length < keys.length) return null
  
  return skus.value.find(sku => {
    const specs = typeof sku.specs === 'string' ? JSON.parse(sku.specs) : sku.specs
    return keys.every(k => specs[k] === selectedSpecs.value[k])
  })
})


const displayImage = computed(() => {
  if (currentSku.value && currentSku.value.skuImage) {
    return currentSku.value.skuImage
  }
  return productInfo.img
})

const displayPriceRange = computed(() => {
  if (skus.value.length === 0) return productInfo.price || '0.00'
  const prices = skus.value.map(s => s.price)
  const min = Math.min(...prices)
  const max = Math.max(...prices)
  return min === max ? min : `${min} - ${max}`
})


const checkOptionDisabled = (key, value) => {
  const nextSpecs = { ...selectedSpecs.value }
  nextSpecs[key] = value
  
  const isValid = skus.value.some(sku => {
    const specs = typeof sku.specs === 'string' ? JSON.parse(sku.specs) : sku.specs
    
    for (const k in nextSpecs) {
      if (nextSpecs[k] && specs[k] !== nextSpecs[k]) {
        return false
      }
    }
    return true
  })

  return !isValid
}

const selectSpec = (key, value) => {
  if (selectedSpecs.value[key] === value) {
    delete selectedSpecs.value[key]
  } else {
    selectedSpecs.value[key] = value
  }
}

const handleAddToCart = async () => {
  if (!currentSku.value) return
  const success = await cartStore.addToCart(currentSku.value.skuId, quantity.value)
  if (success) ElMessage.success('Add to Cart')
}
</script>

<style scoped>
.detail-wrapper {
  max-width: 1000px;
  margin: 40px auto;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
}

.content {
  display: flex;
  gap: 40px;
}

.img-box {
  width: 400px;
  height: 400px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #eee;
}

/* 详情页图片也加上 contain，防止变形 */
.detail-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.info-box {
  flex: 1;
}

.product-title {
  font-size: 28px;
  margin-bottom: 5px;
  color: #333;
}

.product-id {
  font-size: 13px;
  color: #999;
  margin-bottom: 15px;
}

.price-area {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  margin: 20px 0;
  background: #fdf6ec;
  padding: 15px;
  border-radius: 4px;
}

.spec-row {
  margin-bottom: 20px;
}

.spec-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.spec-options {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.actions {
  margin-top: 40px;
  display: flex;
  align-items: center;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
</style>