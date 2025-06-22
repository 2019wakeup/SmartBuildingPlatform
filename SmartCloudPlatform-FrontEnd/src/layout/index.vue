<template>
  <div class="main-container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="logo-container">
        <h2>智能云平台<br/>管理系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :router="true"
        unique-opened
      >
        <el-menu-item index="/" class="menu-item" :class="{ active: activeMenu === '/' }">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/service-hub" class="menu-item" :class="{ active: activeMenu === '/service-hub' }">
          <el-icon><Grid /></el-icon>
          <span>Service Hub</span>
        </el-menu-item>
        <el-sub-menu index="user-management">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user" class="sub-menu-item">
            <el-icon><User /></el-icon>
            <span>用户列表</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="role-management">
          <template #title>
            <el-icon><Key /></el-icon>
            <span>角色权限</span>
          </template>
          <el-menu-item index="/role" class="sub-menu-item">
            <el-icon><UserFilled /></el-icon>
            <span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/permission" class="sub-menu-item">
            <el-icon><Key /></el-icon>
            <span>权限管理</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>

    <!-- 主内容区域 -->
    <div class="content-area">
      <!-- 顶部导航 -->
      <div class="header">
        <div class="header-left">
          <div class="breadcrumb">
            <span class="current-page">{{ currentPageTitle }}</span>
          </div>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="userInfo?.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ userInfo?.userName || userInfo?.account || 'User' }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="changePassword">
                  <el-icon><Lock /></el-icon>
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 主内容 -->
      <div class="main-content">
        <router-view />
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="changePasswordVisible"
      title="修改密码"
      width="400px"
      @close="resetPasswordForm"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="80px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="changePasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { 
  User, 
  UserFilled, 
  Key, 
  HomeFilled, 
  Grid, 
  ArrowDown, 
  Lock, 
  SwitchButton 
} from '@element-plus/icons-vue'
import { logout, changePassword } from '@/api/auth'
import { getUserInfo as getStoredUserInfo, clearAuth } from '@/utils/auth'

const route = useRoute()
const router = useRouter()

const userInfo = ref<any>(null)
const changePasswordVisible = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref<FormInstance>()

// 修改密码表单
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const activeMenu = computed(() => {
  return route.path
})

const currentPageTitle = computed(() => {
  const routeMap: { [key: string]: string } = {
    '/': '首页',
    '/service-hub': 'Service Hub',
    '/user': '用户管理',
    '/role': '角色管理',
    '/permission': '权限管理'
  }
  return routeMap[route.path] || '系统管理'
})

// 处理用户下拉菜单命令
const handleUserCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人中心功能开发中')
      break
    case 'changePassword':
      changePasswordVisible.value = true
      break
    case 'logout':
      await handleLogout()
      break
  }
}

// 处理登出
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    try {
      await logout()
    } catch (error) {
      // 即使登出API失败，也要清除本地认证信息
      console.error('登出API调用失败:', error)
    }
    
    clearAuth()
    ElMessage.success('已成功退出登录')
    router.push('/login')
  } catch (error) {
    // 用户取消登出
  }
}

// 处理修改密码
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        const response = await changePassword({
          oldPassword: passwordForm.value.oldPassword,
          newPassword: passwordForm.value.newPassword
        })
        
        if (response.success) {
          ElMessage.success('密码修改成功，请重新登录')
          changePasswordVisible.value = false
          clearAuth()
          router.push('/login')
        } else {
          ElMessage.error(response.msg || '密码修改失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '密码修改失败')
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields()
  }
}

// 获取用户信息
const fetchUserInfo = () => {
  const storedUser = getStoredUserInfo()
  if (storedUser) {
    userInfo.value = storedUser
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.main-container {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 260px;
  background: #ffffff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  display: flex;
  flex-direction: column;
}

.logo-container {
  padding: 30px 20px;
  border-bottom: 1px solid #f0f0f0;
  text-align: center;
}

.logo-container h2 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.4;
  margin: 0;
  background: linear-gradient(135deg, #667eea, #764ba2);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.sidebar-menu {
  flex: 1;
  border: none;
  padding-top: 20px;
}

.menu-item {
  margin: 0 20px 8px 20px;
  border-radius: 8px;
  height: 48px;
  line-height: 48px;
}

.menu-item:hover {
  background-color: #f3f4f6;
}

.menu-item.active {
  background-color: #4f46e5;
  color: #ffffff;
}

.menu-item.active .el-icon {
  color: #ffffff;
}

.sub-menu-item {
  margin: 0 10px 4px 10px;
  border-radius: 6px;
  height: 40px;
  line-height: 40px;
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: 60px;
  background: #ffffff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.breadcrumb {
  display: flex;
  align-items: center;
}

.current-page {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f3f4f6;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.dropdown-icon {
  font-size: 12px;
  color: #9ca3af;
  transition: transform 0.2s;
}

.user-info:hover .dropdown-icon {
  transform: rotate(180deg);
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f5f5;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 深度选择器修改Element Plus样式 */
:deep(.el-sub-menu__title) {
  margin: 0 20px 4px 20px;
  border-radius: 8px;
  height: 48px;
  line-height: 48px;
}

:deep(.el-sub-menu__title:hover) {
  background-color: #f3f4f6;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title) {
  background-color: #4f46e5;
  color: #ffffff;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title .el-icon) {
  color: #ffffff;
}

:deep(.el-menu--collapse .el-sub-menu__title) {
  margin: 0 10px 4px 10px;
}
</style> 