<template>
  <div class="welcome-section">
    <div class="welcome-content">
      <h1 class="welcome-title">欢迎使用智能云平台管理系统</h1>
      <p class="welcome-subtitle">基于SpringBoot3 + MyBatis Plus的前后端分离管理系统</p>
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
</template>

<script setup lang="ts">
import { User } from '@element-plus/icons-vue'
defineProps<{ userInfo: any }>()
</script>

<style scoped>
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
  margin-bottom: 10px;
}
.welcome-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-bottom: 30px;
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
</style>
