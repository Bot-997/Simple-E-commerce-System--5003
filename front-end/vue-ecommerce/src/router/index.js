import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: () => import('../views/HomeView.vue') },
    { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
    { path: '/product/:id', name: 'product', component: () => import('../views/ProductDetail.vue') },
    
    // login required
    { 
      path: '/cart', 
      name: 'cart', 
      component: () => import('../views/CartView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/checkout', 
      name: 'checkout', 
      component: () => import('../views/CheckoutView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/orders', 
      name: 'orders', 
      component: () => import('../views/OrderListView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/order/:id', 
      name: 'orderDetail', 
      component: () => import('../views/OrderDetailView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/profile', 
      name: 'profile', 
      component: () => import('../views/UserProfileView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/wishlist', 
      name: 'wishlist', 
      component: () => import('../views/WishlistView.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.isLoggedIn()) {
    next('/login')
  } else {
    next()
  }
})

export default router