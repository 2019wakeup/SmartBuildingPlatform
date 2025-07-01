<template>
  <div class="main-container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="logo-container">
        <img src="@/LOGO.jpg" alt="Logo" class="logo-image" />
        <h2>Smart Cloud Platform<br />Management System</h2>
      </div>
      <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          :router="true"
          unique-opened
      >
        <el-menu-item index="/home" class="menu-item" :class="{ active: activeMenu === '/home' }">
          <el-icon><HomeFilled /></el-icon>
          <span>Home</span>
        </el-menu-item>
        <el-menu-item index="/service-hub" class="menu-item" :class="{ active: activeMenu === '/service-hub' }">
          <el-icon><Grid /></el-icon>
          <span>Service Hub</span>
        </el-menu-item>
        <el-menu-item index="video-replay" class="menu-item">
          <el-icon><VideoCamera /></el-icon>
          <span>
            <a
              href="/recording.html"
              style="color: inherit; text-decoration: none; display: inline-block; inline-size: 100%;"
              target="_blank"
            >
              Video Recording
            </a>
          </span>
        </el-menu-item>
        <el-sub-menu index="iot-management">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>IoT Network</span>
          </template>
          <el-menu-item index="/iot-dashboard" class="sub-menu-item">
            <el-icon><TrendCharts /></el-icon>
            <span>Data Monitoring</span>
          </el-menu-item>
          <el-menu-item index="/iot-device" class="sub-menu-item">
            <el-icon><Connection /></el-icon>
            <span>Device Management</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user-management">
          <template #title>
            <el-icon><User /></el-icon>
            <span>User Management</span>
          </template>
          <el-menu-item index="/user" class="sub-menu-item">
            <el-icon><User /></el-icon>
            <span>User List</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="role-management">
          <template #title>
            <el-icon><Key /></el-icon>
            <span>Role & Permission</span>
          </template>
          <el-menu-item index="/role" class="sub-menu-item">
            <el-icon><UserFilled /></el-icon>
            <span>Role Management</span>
          </el-menu-item>
          <el-menu-item index="/permission" class="sub-menu-item">
            <el-icon><Key /></el-icon>
            <span>Permission Management</span>
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
                  Profile
                </el-dropdown-item>
                <el-dropdown-item command="changePassword">
                  <el-icon><Lock /></el-icon>
                  Change Password
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  Logout
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

    <!-- Change Password Dialog -->
    <el-dialog
        v-model="changePasswordVisible"
        title="Change Password"
        width="400px"
        @close="resetPasswordForm"
    >
      <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="120px"
      >
        <el-form-item label="Old Password" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="Enter old password" show-password />
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="Enter new password" show-password />
        </el-form-item>
        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="Confirm new password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="changePasswordVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">Confirm</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Logout Confirmation Dialog -->
    <el-dialog
        v-model="logoutConfirmVisible"
        title="Confirm Logout"
        width="360px"
        center
    >
      <div class="logout-dialog-body">
        <el-avatar :size="48" :src="userInfo?.avatar">
          <el-icon><User /></el-icon>
        </el-avatar>
        <p class="logout-message">
          Are you sure you want to logout from account <strong>{{ userInfo?.userName || userInfo?.account || 'User' }}</strong>?
        </p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="logoutConfirmVisible = false">Cancel</el-button>
          <el-button type="danger" @click="confirmLogout">Logout</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import {
  User,
  UserFilled,
  Key,
  HomeFilled,
  Grid,
  Monitor,
  TrendCharts,
  Connection,
  ArrowDown,
  Lock,
  SwitchButton,
  VideoCamera
} from '@element-plus/icons-vue'
import { logout, changePassword } from '@/api/auth'
import { getUserInfo as getStoredUserInfo, clearAuth } from '@/utils/auth'

// 状态变量
const route = useRoute()
const router = useRouter()

const userInfo = ref<any>(null)
const changePasswordVisible = ref(false)
const logoutConfirmVisible = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref<FormInstance>()

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [{ required: true, message: 'Please enter old password', trigger: 'blur' }],
  newPassword: [
    { required: true, message: 'Please enter new password', trigger: 'blur' },
    { min: 6, max: 20, message: 'Password length should be between 6-20 characters', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: 'Please confirm new password', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('Passwords do not match'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const activeMenu = computed(() => route.path)

const currentPageTitle = computed(() => {
  const map: Record<string, string> = {
    '/home': 'Home',
    '/service-hub': 'Service Hub',
    '/iot-dashboard': 'IoT Data Monitoring',
    '/iot-device': 'IoT Device Management',
    '/user': 'User Management',
    '/role': 'Role Management',
    '/permission': 'Permission Management',
    '/profile': 'Profile'
  }
  return map[route.path] || 'System Management'
})

// 用户下拉菜单
const handleUserCommand = (command: string) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'changePassword') {
    changePasswordVisible.value = true
  } else if (command === 'logout') {
    logoutConfirmVisible.value = true
  }
}

const confirmLogout = async () => {
  logoutConfirmVisible.value = false
  try {
    await logout()
  } catch (e) {
    console.warn('Logout API error', e)
  }
  clearAuth()
  ElMessage.success('Successfully logged out')
  router.push('/login')
}

const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        const res = await changePassword({
          oldPassword: passwordForm.value.oldPassword,
          newPassword: passwordForm.value.newPassword
        })
        if (res.success) {
          ElMessage.success('Password changed successfully, please login again')
          changePasswordVisible.value = false
          clearAuth()
          router.push('/login')
        } else {
          ElMessage.error(res.msg || 'Password change failed')
        }
      } catch (e: any) {
        ElMessage.error(e.message || 'Password change failed')
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

const resetPasswordForm = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  passwordFormRef.value?.resetFields()
}

const fetchUserInfo = () => {
  const user = getStoredUserInfo()
  if (user) userInfo.value = user
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.main-container {
  display: flex;
  block-size: 100vh;
}
.sidebar {
  inline-size: 260px;
  background: #fff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  display: flex;
  flex-direction: column;
}
.logo-container {
  padding: 30px 20px;
  text-align: center;
  border-block-end: 1px solid #f0f0f0;
}
.logo-image {
  width: 60px;
  height: 60px;
  object-fit: contain;
  margin-bottom: 15px;
  border-radius: 8px;
}
.logo-container h2 {
  background: linear-gradient(135deg, #667eea, #764ba2);
  background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 18px;
  margin: 0;
  font-weight: 600;
}
.sidebar-menu {
  flex: 1;
  padding-block-start: 20px;
  border: none;
}
.menu-item,
.sub-menu-item {
  margin: 0 20px 8px 20px;
  border-radius: 8px;
  block-size: 48px;
  line-height: 48px;
}
.menu-item:hover,
.sub-menu-item:hover {
  background: #f3f4f6;
}
.menu-item.active,
.sub-menu-item.active {
  background: #4f46e5;
  color: #fff;
}
.menu-item.active .el-icon,
.sub-menu-item.active .el-icon {
  color: #fff;
}
.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.header {
  block-size: 60px;
  background: #fff;
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.header-left {
  display: flex;
  align-items: center;
}
.current-page {
  font-size: 16px;
  font-weight: 600;
}
.header-right {
  display: flex;
  align-items: center;
}
.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  transition: 0.2s;
}
.user-info:hover {
  background: #f3f4f6;
}
.username {
  font-size: 14px;
  font-weight: 500;
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
  overflow-y: auto;
  background: #f5f5f5;
  padding: 20px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.logout-dialog-body {
  text-align: center;
  padding: 10px;
}
.logout-message {
  margin-block-start: 10px;
  font-size: 14px;
  color: #374151;
}
</style>
