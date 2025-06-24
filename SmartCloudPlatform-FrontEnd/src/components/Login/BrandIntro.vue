<template>
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
</template>

<script setup lang="ts">
import { Check } from '@element-plus/icons-vue'
</script>

<style scoped>
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
  margin-bottom: 10px;
  background: linear-gradient(45deg, #fff, #e0e7ff);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.brand-subtitle {
  font-size: 1.2rem;
  font-weight: 300;
  margin-bottom: 20px;
  opacity: 0.9;
}
.brand-description {
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 40px;
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
</style>
