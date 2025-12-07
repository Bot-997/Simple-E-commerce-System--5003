import { defineStore } from 'pinia'
import { ref } from 'vue'
import { loginApi, registerApi } from '@/api/index'
import { useCartStore } from './cart'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null) 
  const userId = ref(null)

  const isLoggedIn = () => !!userId.value

  // login
  const login = async (form) => {
    try {
      const res = await loginApi(form)
      userInfo.value = res.data
      userId.value = res.data.userId
      
      // request cart data after logined
      const cartStore = useCartStore()
      await cartStore.fetchCart() 
      
      return true
    } catch (e) {
      throw e
    }
  }

  // register
  const register = async (form) => {
    return registerApi(form)
  }

  // logout
  const logout = () => {
    userInfo.value = null
    userId.value = null
    const cartStore = useCartStore()
    cartStore.clearLocalCart()
  }

  return { userInfo, userId, isLoggedIn, login, register, logout }
})