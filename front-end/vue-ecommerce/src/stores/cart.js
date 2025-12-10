import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCartListApi, addToCartApi, removeCartItemApi, clearCartApi, updateCartItemApi } from '@/api/index'
import { useUserStore } from './user'
import { ElMessage } from 'element-plus'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  // list
  const fetchCart = async () => {
    const userStore = useUserStore()
    if (!userStore.userId) return
    try {
      const res = await getCartListApi(userStore.userId)
      if (res.code === 200) {
        items.value = res.data || []
      }
    } catch (e) {
      console.error('Fetch cart failed', e)
    }
  }

  // add to cart
  const addToCart = async (skuId, quantity = 1) => {
    const userStore = useUserStore()
    if (!userStore.isLoggedIn()) {
      ElMessage.warning('Please Login')
      return false
    }

    try {
      await addToCartApi({
        userId: userStore.userId,
        skuId: skuId,
        quantity: quantity
      })
      // refresh
      await fetchCart()
      return true
    } catch (e) {
      return false
    }
  }

  // remove
  const removeFromCart = async (skuId) => {
    const userStore = useUserStore()
    try {
      await removeCartItemApi({ userId: userStore.userId, skuId })
      items.value = items.value.filter(item => item.skuId !== skuId)
    } catch (e) {
      ElMessage.error('Remove Failed')
    }
  }

  // clear
  const clearCart = async (silent = false) => {
    const userStore = useUserStore()
    try {
      await clearCartApi({ userId: userStore.userId })
      items.value = []
      if (!silent) {
        ElMessage.success('Clear Cart')
      }
    } catch (e) {
      if (!silent) ElMessage.error('Clear Failed')
    }
  }

  // update
  const updateQuantity = async (skuId, quantity) => {
    const userStore = useUserStore()
    const item = items.value.find(i => i.skuId === skuId)
    if (item) item.quantity = quantity

    try {
      await updateCartItemApi({
        userId: userStore.userId,
        skuIdL skuId,
        quantity: quantity
      })
    } catch (e) {
      ElMessage.error('Update Failed')
      fetchCart()
    }
  }


  const clearLocalCart = () => {
    items.value = []
  }

  
  const totalCount = computed(() => items.value.reduce((sum, item) => sum + item.quantity, 0))
  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)
  })

  return { items, fetchCart, addToCart, removeFromCart, clearCart, updateQuantity, clearLocalCart, totalCount, totalPrice }
})
