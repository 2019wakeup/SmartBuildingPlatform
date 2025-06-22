<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="brand-info">
          <h1 class="brand-title">智能云平台</h1>
          <h2 class="brand-subtitle">Smart Cloud Platform</h2>
          <p class="brand-description">
            基于SpringBoot3 + MyBatis Plus的前后端分离管理系统
          </p>
          <div class="feature-list">
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>统一身份认证</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>角色权限管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>安全访问控制</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="login-right">
        <div class="login-form-container">
          <div class="login-header">
            <h3>欢迎登录</h3>
            <p>请输入您的账号和密码</p>
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
                placeholder="请输入账号"
                size="large"
                :prefix-icon="User"
                clearable
              />
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
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
                {{ loading ? '登录中...' : '登录' }}
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="login-footer">
            <p class="demo-account">
              演示账号: admin / 123456
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock, Check } from '@element-plus/icons-vue'
import { login } from '@/api/auth'
import { setToken, setUserInfo } from '@/utils/auth'
import type { LoginRequest } from '@/api/auth'

const router = useRouter()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

// 登录表单数据
const loginForm = reactive<LoginRequest>({
  account: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await login(loginForm)
        
        if (response.success) {
          // 保存token和用户信息
          const { token, user } = response.data
          setToken(token)
          setUserInfo(user)
          
          ElMessage.success('登录成功')
          
          // 跳转到首页
          router.push('/')
        } else {
          ElMessage.error(response.msg || '登录失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-wrapper {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 40px;
  color: white;
}

.brand-info {
  text-align: center;
  max-width: 400px;
}

.brand-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 10px 0;
  background: linear-gradient(45deg, #fff, #e0e7ff);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.brand-subtitle {
  font-size: 1.2rem;
  font-weight: 300;
  margin: 0 0 20px 0;
  opacity: 0.9;
}

.brand-description {
  font-size: 1rem;
  line-height: 1.6;
  margin: 0 0 40px 0;
  opacity: 0.8;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1rem;
}

.feature-item .el-icon {
  font-size: 18px;
  color: #4ade80;
}

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
  margin: 0 0 10px 0;
}

.login-header p {
  color: #6b7280;
  margin: 0;
  font-size: 1rem;
}

.login-form {
  width: 100%;
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
  margin: 0;
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    max-width: 400px;
  }
  
  .login-left {
    padding: 40px 20px;
  }
  
  .login-right {
    padding: 40px 20px;
  }
  
  .brand-title {
    font-size: 2rem;
  }
}
</style> 