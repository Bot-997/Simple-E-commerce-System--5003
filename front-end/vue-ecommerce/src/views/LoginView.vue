<template>
  <div class="login-wrapper">
    <el-card class="login-card">
      <div class="header">
        <span :class="{ active: isLogin }" @click="isLogin = true">Login</span>
        <span class="divider">|</span>
        <span :class="{ active: !isLogin }" @click="isLogin = false">Register</span>
      </div>

      <el-form :model="form" ref="formRef" label-position="top">
        <el-form-item label="Email" prop="email" :rules="[{ required: true, message: 'Required' }]">
          <el-input v-model="form.email" placeholder="student@demo.com" prefix-icon="Message" />
        </el-form-item>
        
        <el-form-item label="Password" prop="password" :rules="[{ required: true, message: 'Required' }]">
          <el-input v-model="form.password" type="password" show-password prefix-icon="Lock" />
        </el-form-item>

        <el-form-item v-if="!isLogin" label="Confirm" prop="confirmPwd">
          <el-input v-model="form.confirmPwd" type="password" prefix-icon="Lock" />
        </el-form-item>

        <el-button type="primary" class="submit-btn" :loading="loading" @click="handleSubmit">
          {{ isLogin ? 'Login Now' : 'Register Now' }}
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const isLogin = ref(true) // true=登录, false=注册
const loading = ref(false)
const userStore = useUserStore()
const router = useRouter()
const formRef = ref(null)

const form = reactive({
  email: '',
  password: '',
  confirmPwd: ''
})

const handleSubmit = async () => {
  if (!form.email || !form.password) return ElMessage.warning('Please Fill Tables')
  if (!isLogin.value && form.password !== form.confirmPwd) return ElMessage.error('Passwords do not match.')

  loading.value = true
  try {
    if (isLogin.value) {
      // === 登录流程 ===
      await userStore.login({ email: form.email, password: form.password })
      ElMessage.success('Login Success')
      router.push('/')
    } else {
      // === 注册流程 ===
      const res = await userStore.register({ email: form.email, password: form.password })
      if (res.code === 200) {
        ElMessage.success('Register Success, Please login.')
        isLogin.value = true
        form.password = '' 
      }
    }
  } catch (err) {
    
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper { display: flex; justify-content: center; align-items: center; height: 100vh; background: #f0f2f5; }
.login-card { width: 400px; padding: 20px; }
.header { text-align: center; font-size: 18px; margin-bottom: 30px; cursor: pointer; color: #999; }
.header span.active { color: #409EFF; font-weight: bold; font-size: 20px; }
.header .divider { margin: 0 15px; color: #eee; cursor: default; }
.submit-btn { width: 100%; margin-top: 10px; padding: 20px; font-size: 16px; }
</style>