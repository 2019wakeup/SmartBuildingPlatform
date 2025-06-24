<template>
  <div class="quick-actions">
    <h2 class="section-title">快速操作</h2>
    <div class="action-grid">
      <div class="action-card" v-for="action in actions" :key="action.path" @click="$emit('navigate', action.path)">
        <div class="action-icon">
          <el-icon><component :is="action.icon" /></el-icon>
        </div>
        <h3>{{ action.title }}</h3>
        <p>{{ action.desc }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Grid, User, UserFilled, Key } from '@element-plus/icons-vue'
const actions = [
  { path: '/service-hub', icon: Grid, title: 'Service Hub', desc: '访问所有系统功能模块' },
  { path: '/user', icon: User, title: '用户管理', desc: '管理系统用户信息' },
  { path: '/role', icon: UserFilled, title: '角色管理', desc: '配置用户角色权限' },
  { path: '/permission', icon: Key, title: '权限管理', desc: '设置系统访问权限' }
]
</script>

<style scoped>
.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #1f2937;
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
</style>
