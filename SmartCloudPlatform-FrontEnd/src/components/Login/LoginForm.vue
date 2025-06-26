<template>
  <div class="login-right">
    <div class="login-form-container">
      <div class="login-header">
        <h3>Welcome</h3>
        <p>Please enter your account and password</p>
      </div>
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="account">
          <el-input
              v-model="loginForm.account"
              placeholder="Enter account"
              size="large"
              :prefix-icon="User"
              clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="Enter password"
              size="large"
              :prefix-icon="Lock"
              show-password
              clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              size="large"
              :loading="loading"
              @click="handleLogin"
              class="login-button"
          >
            {{ loading ? 'Logging in...' : 'Login' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p class="demo-account">Demo Account: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/auth'
import { setToken, setUserInfo } from '@/utils/auth'
import type { LoginRequest } from '@/api/auth'

const emit = defineEmits(['loginSuccess'])
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive<LoginRequest>({
  account: '',
  password: ''
})

const loginRules = {
  account: [{ required: true, message: 'Please enter account', trigger: 'blur' }],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, message: 'Password length must be at least 6 characters', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await login(loginForm)
        if (response.success) {
          const { token, user } = response.data
          setToken(token)
          setUserInfo(user)
          ElMessage.success('Login successful')
          emit('loginSuccess')
        } else {
          ElMessage.error(response.msg || 'Login failed')
        }
      } catch (error: any) {
        ElMessage.error(error.message || 'Login failed, please check network connection')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 40px;
}
.login-form-container {
  width: 100%;
  max-width: 400px;
}
.login-header {
  text-align: center;
  margin-bottom: 40px;
}
.login-header h3 {
  font-size: 2rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 10px;
}
.login-header p {
  color: #6b7280;
  font-size: 1rem;
}
.login-form .el-form-item {
  margin-bottom: 24px;
}
.login-button {
  width: 100%;
  height: 48px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}
.login-button:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}
.login-footer {
  text-align: center;
  margin-top: 30px;
}
.demo-account {
  color: #6b7280;
  font-size: 0.9rem;
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}
</style>