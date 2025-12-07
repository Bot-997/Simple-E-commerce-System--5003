import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useProductStore = defineStore('product', () => {
  const currentProduct = ref(null)

  const setProduct = (product) => {
    currentProduct.value = product
  }

  return { currentProduct, setProduct }
})