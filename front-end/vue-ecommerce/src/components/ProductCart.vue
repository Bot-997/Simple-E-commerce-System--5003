<template>
  <el-card :body-style="{ padding: '0px' }" shadow="hover" class="product-card">
    <div class="image-placeholder" @click="emit('click-detail')">
      <img v-if="product.imageUrl" :src="product.imageUrl" class="real-img" loading="lazy" />
      <span v-else>No Image</span>
    </div>
    
    <div class="card-body">
      <h3 class="title" @click="emit('click-detail')">
        {{ product.name }}
      </h3>
      <div class="bottom">
        <span class="price"><small>From</small> ¥{{ product.basePrice }}</span>
        <el-button type="primary" size="small" plain @click.stop="emit('click-detail')">
          Buy
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
defineProps({
  product: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click-detail'])
</script>

<style scoped>
.product-card {
  transition: all 0.3s ease; /* 平滑过渡 */
  border-radius: 12px;
  overflow: hidden;
  height: 100%;
  border: 1px solid #f0f0f0;
}

/* 悬浮效果：上浮 + 投影加深 / Hover Effect */
.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
}

.image-placeholder {
  width: 100%;
  height: 200px; /* 固定高度 */
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden; /* 防止溢出 */
  position: relative; /* 为内部定位做准备 */
}

.real-img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 关键：保持比例缩放，不拉伸，留白 */
  /* object-fit: cover; // 如果你希望填满格子允许裁剪，可以用这个 */
  transition: transform 0.3s;
}

.card-body { padding: 16px; }
.title {
  font-size: 16px;
  margin: 0 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  color: #333;
}

.title:hover {
  color: #409EFF;
}

.desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 15px;
  height: 16px;
  /* 固定高度防止错位 */
  overflow: hidden;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
  font-family: 'Verdana', sans-serif;
}
</style>