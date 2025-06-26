<template>
  <div class="stats-grid">
    <div class="stat-card" v-for="item in items" :key="item.label">
      <div class="stat-icon" :class="item.iconClass">
        <el-icon><component :is="item.icon" /></el-icon>
      </div>
      <div class="stat-content">
        <h3>{{ item.count }}</h3>
        <p>{{ item.label }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { User, UserFilled, Key, Connection } from '@element-plus/icons-vue'
const props = defineProps<{ stats: { userCount: number; roleCount: number; permissionCount: number; onlineCount: number } }>()

const items = [
  { icon: User, label: 'Total Users', count: props.stats.userCount, iconClass: 'user-icon' },
  { icon: UserFilled, label: 'Total Roles', count: props.stats.roleCount, iconClass: 'role-icon' },
  { icon: Key, label: 'Total Permissions', count: props.stats.permissionCount, iconClass: 'permission-icon' },
  { icon: Connection, label: 'Online Users', count: props.stats.onlineCount, iconClass: 'online-icon' }
]
</script>

<style scoped>
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
.user-icon { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
.role-icon { background: linear-gradient(135deg, #f093fb, #f5576c); color: white; }
.permission-icon { background: linear-gradient(135deg, #4facfe, #00f2fe); color: white; }
.online-icon { background: linear-gradient(135deg, #43e97b, #38f9d7); color: white; }
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
</style>