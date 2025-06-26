<template>
  <div class="login-left">
    <div class="brand-info">
      <h1 class="brand-title">Smart Cloud Platform</h1>
      <h2 class="brand-subtitle">Intelligent Management System</h2>
      <p class="brand-description">
        Full-stack separation management system based on SpringBoot3 + MyBatis Plus
      </p>
      <div class="feature-list">
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>Unified Authentication</span>
        </div>
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>Role & Permission Management</span>
        </div>
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>Secure Access Control</span>
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
