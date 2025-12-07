import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getWishlistApi, addToWishlistApi, removeWishlistApi } from '@/api/index'
import { useUserStore } from './user'
import { ElMessage } from 'element-plus'

export const useWishlistStore = defineStore('wishlist', () => {
    const items = ref([])

    // 获取愿望单
    const fetchWishlist = async () => {
        const userStore = useUserStore()
        if (!userStore.userId) return
        try {
            const res = await getWishlistApi(userStore.userId)
            if (res.code === 200) {
                items.value = res.data || []
            }
        } catch (e) {
            console.error(e)
        }
    }

    // 添加收藏
    const toggleWishlist = async (product) => {
        const userStore = useUserStore()
        if (!userStore.isLoggedIn()) {
            ElMessage.warning('Pleas Login')
            return
        }

        const existingItem = items.value.find(i => i.productId == product.productId)

        if (existingItem) {
            await removeItem(existingItem.productId)
        } else {
            try {
                const res = await addToWishlistApi({
                    userId: userStore.userId,
                    productId: product.productId
                })
                if (res.code === 200) {
                    ElMessage.success('Add Success')
                    fetchWishlist()
                }
            } catch (e) {
                ElMessage.error(e.msg || 'Add Fail')
            }
        }
    }

    // 移除收藏
    const removeItem = async (productId) => {
        const userStore = useUserStore()
        try {
            await removeWishlistApi({ userId: userStore.userId, productId })
            ElMessage.success('Remove Success')
            fetchWishlist()
        } catch (e) {
            items.value = items.value.filter(i => i.productId !== productId)
        }
    }

    return { items, fetchWishlist, toggleWishlist, removeItem }
})