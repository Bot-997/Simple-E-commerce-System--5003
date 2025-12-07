<template>
  <el-header class="nav-header">
    <div class="nav-content">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🛍️</span> MyShop
      </div>
      
      <div class="menu">
        <router-link to="/" class="nav-link">HOME</router-link>
        
        <template v-if="userStore.isLoggedIn()">
          <router-link to="/wishlist" class="nav-link">
            WISHLIST ({{ wishlistStore.items.length }})
          </router-link>

          <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link nav-link user-link">
              Hi, {{ userStore.userInfo?.email || 'User' }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">Profile</el-dropdown-item>
                <el-dropdown-item command="orders">My Orders</el-dropdown-item>
                <el-dropdown-item command="wishlist">Wishlist</el-dropdown-item>
                <el-dropdown-item divided command="logout">Logout</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        
        <router-link v-else to="/login" class="nav-link login-btn">Login</router-link>
      </div>

      <div class="cart-btn-wrapper" @click="handleGoCart">
        <el-badge :value="cartStore.totalCount" :max="99" :hidden="cartStore.totalCount === 0" class="badge-item">
          <el-button circle size="large">
            <el-icon :size="20"><ShoppingCart /></el-icon>
          </el-button>
        </el-badge>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { useWishlistStore } from '@/stores/wishlist'
import { ArrowDown, ShoppingCart } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const wishlistStore = useWishlistStore()

const fetchAllData = () => {
  if (userStore.isLoggedIn()) {
    cartStore.fetchCart()
    wishlistStore.fetchWishlist()
  }
}

onMounted(() => {
  fetchAllData()
})

watch(
  () => userStore.userId,
  (newId) => {
    if (newId) {
      console.log(1234)
      fetchAllData()
    } else {
      console.log(4321)
      cartStore.items = []
      wishlistStore.items = []
    }
  }
)

const handleGoCart = () => {
  router.push('/cart')
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (['profile', 'orders', 'wishlist'].includes(command)) {
    router.push(`/${command}`)
  }
}
</script>

<style scoped>
.nav-header {
  background: #fff;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  height: 70px;
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 999;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 24px;
  font-weight: 800;
  color: #333;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu { display: flex; align-items: center; flex: 1; justify-content: flex-end; margin-right: 30px; }

.nav-link { 
  margin: 0 15px; text-decoration: none; color: #555; font-weight: 500; cursor: pointer; transition: color 0.2s;
}
.nav-link:hover { color: #409EFF; }
.user-link { display: flex; align-items: center; }
.login-btn { color: #409EFF; }

.cart-btn-wrapper { cursor: pointer; display: flex; align-items: center; }
:deep(.el-badge__content.is-fixed) { top: 5px; right: 5px; }
</style>