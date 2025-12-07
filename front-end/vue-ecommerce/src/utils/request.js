import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
  headers: { 'Content-Type': 'application/json' }
})

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.msg || 'Error')
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  (error) => {
    console.error('Request err: ' + error)
    ElMessage.error(error.message || 'Request Failed')
    return Promise.reject(error)
  }
)

export default service