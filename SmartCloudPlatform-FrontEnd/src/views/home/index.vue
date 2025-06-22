<template>
  <div class="home-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">
          欢迎使用智能云平台管理系统
        </h1>
        <p class="welcome-subtitle">
          基于SpringBoot3 + MyBatis Plus的前后端分离管理系统
        </p>
        <div class="user-welcome" v-if="userInfo">
          <el-avatar :size="60" :src="userInfo.avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="user-info">
            <h3>{{ userInfo.userName || userInfo.account }}</h3>
            <p>{{ userInfo.email || '暂无邮箱' }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon user-icon">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.userCount }}</h3>
          <p>用户总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon role-icon">
          <el-icon><UserFilled /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.roleCount }}</h3>
          <p>角色总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon permission-icon">
          <el-icon><Key /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.permissionCount }}</h3>
          <p>权限总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon online-icon">
          <el-icon><Connection /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.onlineCount }}</h3>
          <p>在线用户</p>
        </div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <h2 class="section-title">快速操作</h2>
      <div class="action-grid">
        <div class="action-card" @click="navigateTo('/service-hub')">
          <div class="action-icon">
            <el-icon><Grid /></el-icon>
          </div>
          <h3>Service Hub</h3>
          <p>访问所有系统功能模块</p>
        </div>
        
        <div class="action-card" @click="navigateTo('/user')">
          <div class="action-icon">
            <el-icon><User /></el-icon>
          </div>
          <h3>用户管理</h3>
          <p>管理系统用户信息</p>
        </div>
        
        <div class="action-card" @click="navigateTo('/role')">
          <div class="action-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
          <h3>角色管理</h3>
          <p>配置用户角色权限</p>
        </div>
        
        <div class="action-card" @click="navigateTo('/permission')">
          <div class="action-icon">
            <el-icon><Key /></el-icon>
          </div>
          <h3>权限管理</h3>
          <p>设置系统访问权限</p>
        </div>
      </div>
    </div>

    <!-- 系统信息 -->
    <div class="system-info">
      <h2 class="section-title">系统信息</h2>
      <div class="info-grid">
        <div class="info-card">
          <h4>系统版本</h4>
          <p>v1.0.0</p>
        </div>
        <div class="info-card">
          <h4>技术栈</h4>
          <p>SpringBoot3 + Vue3 + TypeScript</p>
        </div>
        <div class="info-card">
          <h4>数据库</h4>
          <p>MySQL + MyBatis Plus</p>
        </div>
        <div class="info-card">
          <h4>最后更新</h4>
          <p>{{ new Date().toLocaleDateString() }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, UserFilled, Key, Connection, Grid } from '@element-plus/icons-vue'
import { getUserInfo as getAuthUserInfo } from '@/api/auth'
import { getUserInfo as getStoredUserInfo } from '@/utils/auth'

const router = useRouter()
const userInfo = ref<any>(null)

// 统计数据
const stats = ref({
  userCount: 156,
  roleCount: 8,
  permissionCount: 24,
  onlineCount: 12
})

// 导航到指定页面
const navigateTo = (path: string) => {
  router.push(path)
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    // 首先从本地存储获取
    const storedUser = getStoredUserInfo()
    if (storedUser) {
      userInfo.value = storedUser
    }
    
    // 然后从服务器获取最新信息
    const response = await getAuthUserInfo()
    if (response.success && response.data) {
      userInfo.value = response.data
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    // 如果获取失败，使用本地存储的信息
    const storedUser = getStoredUserInfo()
    if (storedUser) {
      userInfo.value = storedUser
    }
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 40px;
  color: white;
  margin-bottom: 30px;
}

.welcome-content {
  text-align: center;
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 10px 0;
}

.welcome-subtitle {
  font-size: 1.2rem;
  margin: 0 0 30px 0;
  opacity: 0.9;
}

.user-welcome {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.user-info h3 {
  margin: 0 0 5px 0;
  font-size: 1.3rem;
}

.user-info p {
  margin: 0;
  opacity: 0.8;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.user-icon {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.role-icon {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
}

.permission-icon {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
  color: white;
}

.online-icon {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
  color: white;
}

.stat-content h3 {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 5px 0;
  color: #1f2937;
}

.stat-content p {
  margin: 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0 0 20px 0;
  color: #1f2937;
}

.quick-actions {
  margin-bottom: 40px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.action-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
}

.action-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px auto;
  font-size: 24px;
}

.action-card h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #1f2937;
}

.action-card p {
  margin: 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.system-info {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.info-card {
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  text-align: center;
}

.info-card h4 {
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #374151;
}

.info-card p {
  margin: 0;
  color: #6b7280;
  font-size: 0.9rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-title {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .action-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style> 