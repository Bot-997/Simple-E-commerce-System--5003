<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>User Profile</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="Addresses" name="address">
          <div class="address-actions">
            <el-button type="primary" icon="Plus" @click="showAddDialog = true">Add New</el-button>
          </div>
          
          <el-table :data="addresses" style="width: 100%" v-loading="loading">
            <el-table-column prop="state" label="State" width="120" />
            <el-table-column prop="city" label="City" width="120" />
            <el-table-column prop="zipCode" label="zipCode" width="100" />
            <el-table-column label="Default" width="80">
              <template #default="{ row }">
                <el-tag v-if="row.isDefault === 1" type="success" size="small">Default</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Action" align="right">
              <template #default="{ row }">
                <el-button type="danger" link size="small" @click="handleDeleteAddress(row)">Del</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="Security" name="security">
          <el-form 
            ref="pwdFormRef" 
            :model="pwdForm" 
            :rules="pwdRules" 
            label-width="120px" 
            style="max-width: 500px; margin-top: 20px"
          >
            <el-form-item label="Old Password" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="New Password" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="Confirm" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="pwdLoading">Confirm</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="showAddDialog" title="Add New Address" width="500px">
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="State">
          <el-input v-model="addressForm.state" placeholder="例如：广东省" />
        </el-form-item>
        <el-form-item label="City">
          <el-input v-model="addressForm.city" placeholder="例如：深圳市" />
        </el-form-item>
        <el-form-item label="zipCode">
          <el-input v-model="addressForm.zipCode" placeholder="例如：518000" />
        </el-form-item>
        <el-form-item label="As Default">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">Cancel</el-button>
          <el-button type="primary" @click="handleSaveAddress">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getAddressListApi, addAddressApi, deleteAddressApi, changePasswordApi } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('address')
const loading = ref(false)
const addresses = ref([])
const showAddDialog = ref(false)

// === 地址管理逻辑 ===
const addressForm = reactive({
  state: '',
  city: '',
  zipCode: '',
  isDefault: 0
})

const fetchAddresses = async () => {
  loading.value = true
  try {
    const res = await getAddressListApi(userStore.userId)
    if (res.code === 200) {
      addresses.value = res.data || []
    }
  } finally {
    loading.value = false
  }
}

const handleSaveAddress = async () => {
  if (!addressForm.state || !addressForm.city) return ElMessage.warning('请填写完整地址信息')
  
  try {
    await addAddressApi({
      userId: userStore.userId,
      ...addressForm
    })
    ElMessage.success('Add Success')
    showAddDialog.value = false
    // 重置表单
    addressForm.state = ''
    addressForm.city = ''
    addressForm.zipCode = ''
    addressForm.isDefault = 0
    fetchAddresses()
  } catch (e) {
    ElMessage.error(e.msg || 'Add Fail')
  }
}

const handleDeleteAddress = (row) => {
  ElMessageBox.confirm('Confirm to Delete?', 'WANRING', { type: 'warning' })
    .then(async () => {
      try {
        await deleteAddressApi({ userId: userStore.userId, addressId: row.addressId })
        ElMessage.success('Delete Success')
        fetchAddresses()
      } catch(e) { 
        // 演示用：如果后端还没做删除接口，前端临时移除
        addresses.value = addresses.value.filter(a => a.addressId !== row.addressId)
      }
    })
    .catch(() => {})
}

// === 修改密码逻辑 ===
const pwdFormRef = ref(null)
const pwdLoading = ref(false)
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPwd = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('Passwords do not match!'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: 'Input old password', trigger: 'blur' }],
  newPassword: [{ required: true, message: 'Input new password', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: 'Confirm new password', trigger: 'blur' },
    { validator: validateConfirmPwd, trigger: 'blur' }
  ]
}

const handleChangePassword = async () => {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (valid) {
      pwdLoading.value = true
      try {
        const res = await changePasswordApi({
          userId: userStore.userId,
          oldPassword: pwdForm.oldPassword,
          newPassword: pwdForm.newPassword
        })
        if (res.code === 200) {
          ElMessage.success('Password reset, please login again.')
          userStore.logout()
          // 跳转回登录页通常在 App.vue 或 Router 守卫中处理，或者直接刷新
          window.location.reload() 
        }
      } catch (e) {
        ElMessage.error(e.msg || '修改失败')
      } finally {
        pwdLoading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchAddresses()
})
</script>

<style scoped>
.profile-container { max-width: 1000px; margin: 40px auto; padding: 0 20px; }
.address-actions { margin-bottom: 20px; }
</style>