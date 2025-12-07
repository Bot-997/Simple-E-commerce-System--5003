// 模拟数据库 / Mock Database
const MOCK_PRODUCTS = [
  { id: 1, name: '机械键盘 Pro / Mech Keyboard', price: 299, desc: '全键无冲，RGB背光 / RGB Backlit', img: '' },
  { id: 2, name: '无线鼠标 / Wireless Mouse', price: 89, desc: '人体工学设计 / Ergonomic', img: '' },
  { id: 3, name: '4K 显示器 / 4K Monitor', price: 1599, desc: 'IPS 面板，超清画质 / Ultra HD', img: '' },
  { id: 4, name: '降噪耳机 / NC Headphones', price: 499, desc: '静享音乐世界 / Silence', img: '' },
  { id: 5, name: '智能音箱 / Smart Speaker', price: 199, desc: '语音助手 / Voice Assistant', img: '' },
  { id: 6, name: '游戏手柄 / Gamepad', price: 150, desc: '精准操控 / Precision control', img: '' },
]

// 模拟请求延迟 / Simulate network latency
const mockRequest = (data, time = 300) => {
  return new Promise((resolve) => setTimeout(() => resolve(data), time))
}

// === API 导出 ===

export const getProductsApi = () => {
  return mockRequest({ code: 200, data: MOCK_PRODUCTS })
}

export const getProductDetailApi = (id) => {
  const product = MOCK_PRODUCTS.find(p => p.id === Number(id))
  return mockRequest({ code: 200, data: product })
}

export const loginApi = (username, password) => {
  if (username === 'admin' && password === '123456') {
    return mockRequest({ 
      code: 200, 
      token: 'mock-token-xyz', 
      userInfo: { name: 'Admin User', role: 'admin' }
    })
  }
  return Promise.reject({ msg: '用户名或密码错误 / Invalid credentials' })
}