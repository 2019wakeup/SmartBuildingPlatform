<template>
  <div class="main-container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="logo-container">
        <h2>Unified Identity<br/>& Access<br/>Management</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :router="true"
        unique-opened
      >
        <el-menu-item index="/user" class="menu-item" :class="{ active: activeMenu === '/user' }">
          <el-icon><User /></el-icon>
          <span>User Management</span>
        </el-menu-item>
        <el-menu-item index="/role" class="menu-item" :class="{ active: activeMenu === '/role' }">
          <el-icon><UserFilled /></el-icon>
          <span>Role Management</span>
        </el-menu-item>
        <el-menu-item index="/permission" class="menu-item" :class="{ active: activeMenu === '/permission' }">
          <el-icon><Key /></el-icon>
          <span>Permission Management</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区域 -->
    <div class="content-area">
      <!-- 顶部导航 -->
      <div class="header">
        <div class="header-left">
          <!-- 可以添加面包屑导航等 -->
        </div>
        <div class="header-right">
          <el-dropdown>
            <div class="user-info">
              <el-avatar :size="32" src="/avatar.jpg">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">Manager</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
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
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => {
  return route.path
})
</script>

<style scoped>
.main-container {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 250px;
  background: #ffffff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  display: flex;
  flex-direction: column;
}

.logo-container {
  padding: 30px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.logo-container h2 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.4;
  margin: 0;
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

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
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

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f5f5;
}
</style> 