<template>
  <div class="service-hub-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">Service Hub</h1>
      <p class="page-description">系统功能服务中心 - 快速访问所有管理功能</p>
    </div>

    <!-- 功能分类 -->
    <div class="service-categories">
      <!-- 用户管理模块 -->
      <div class="category-section">
        <h2 class="category-title">
          <el-icon><User /></el-icon>
          用户管理
        </h2>
        <div class="service-grid">
          <div class="service-card" @click="navigateTo('/user')">
            <div class="service-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="service-content">
              <h3>用户管理</h3>
              <p>管理系统用户信息，包括用户的增删改查操作</p>
              <div class="service-features">
                <span class="feature-tag">用户列表</span>
                <span class="feature-tag">用户详情</span>
                <span class="feature-tag">状态管理</span>
              </div>
            </div>
            <div class="service-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>

      <!-- 角色权限模块 -->
      <div class="category-section">
        <h2 class="category-title">
          <el-icon><Key /></el-icon>
          角色权限管理
        </h2>
        <div class="service-grid">
          <div class="service-card" @click="navigateTo('/role')">
            <div class="service-icon role-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="service-content">
              <h3>角色管理</h3>
              <p>配置系统角色信息，管理角色权限分配</p>
              <div class="service-features">
                <span class="feature-tag">角色配置</span>
                <span class="feature-tag">权限分配</span>
                <span class="feature-tag">角色授权</span>
              </div>
            </div>
            <div class="service-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>

          <div class="service-card" @click="navigateTo('/permission')">
            <div class="service-icon permission-icon">
              <el-icon><Key /></el-icon>
            </div>
            <div class="service-content">
              <h3>权限管理</h3>
              <p>设置系统访问权限，控制功能模块访问</p>
              <div class="service-features">
                <span class="feature-tag">权限配置</span>
                <span class="feature-tag">访问控制</span>
                <span class="feature-tag">权限审计</span>
              </div>
            </div>
            <div class="service-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>

      <!-- 系统管理模块 -->
      <div class="category-section">
        <h2 class="category-title">
          <el-icon><Setting /></el-icon>
          系统管理
        </h2>
        <div class="service-grid">
          <div class="service-card coming-soon">
            <div class="service-icon system-icon">
              <el-icon><Monitor /></el-icon>
            </div>
            <div class="service-content">
              <h3>系统监控</h3>
              <p>监控系统运行状态，查看性能指标</p>
              <div class="service-features">
                <span class="feature-tag">性能监控</span>
                <span class="feature-tag">日志管理</span>
                <span class="feature-tag">告警通知</span>
              </div>
            </div>
            <div class="coming-soon-badge">
              即将上线
            </div>
          </div>

          <div class="service-card coming-soon">
            <div class="service-icon config-icon">
              <el-icon><Tools /></el-icon>
            </div>
            <div class="service-content">
              <h3>系统配置</h3>
              <p>管理系统参数配置，自定义系统设置</p>
              <div class="service-features">
                <span class="feature-tag">参数配置</span>
                <span class="feature-tag">系统设置</span>
                <span class="feature-tag">配置备份</span>
              </div>
            </div>
            <div class="coming-soon-badge">
              即将上线
            </div>
          </div>
        </div>
      </div>

      <!-- 数据管理模块 -->
      <div class="category-section">
        <h2 class="category-title">
          <el-icon><DataBoard /></el-icon>
          数据管理
        </h2>
        <div class="service-grid">
          <div class="service-card coming-soon">
            <div class="service-icon data-icon">
              <el-icon><PieChart /></el-icon>
            </div>
            <div class="service-content">
              <h3>数据统计</h3>
              <p>查看系统数据统计，生成分析报表</p>
              <div class="service-features">
                <span class="feature-tag">数据统计</span>
                <span class="feature-tag">报表生成</span>
                <span class="feature-tag">数据导出</span>
              </div>
            </div>
            <div class="coming-soon-badge">
              即将上线
            </div>
          </div>

          <div class="service-card coming-soon">
            <div class="service-icon backup-icon">
              <el-icon><FolderOpened /></el-icon>
            </div>
            <div class="service-content">
              <h3>数据备份</h3>
              <p>管理数据备份任务，确保数据安全</p>
              <div class="service-features">
                <span class="feature-tag">自动备份</span>
                <span class="feature-tag">数据恢复</span>
                <span class="feature-tag">备份策略</span>
              </div>
            </div>
            <div class="coming-soon-badge">
              即将上线
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速统计 -->
    <div class="quick-stats">
      <h2 class="section-title">系统概览</h2>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-number">{{ stats.totalServices }}</div>
          <div class="stat-label">总服务数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.activeServices }}</div>
          <div class="stat-label">可用服务</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.comingSoonServices }}</div>
          <div class="stat-label">即将上线</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.categories }}</div>
          <div class="stat-label">功能分类</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { 
  User, 
  UserFilled, 
  Key, 
  Setting, 
  Monitor, 
  Tools, 
  DataBoard, 
  PieChart, 
  FolderOpened,
  ArrowRight
} from '@element-plus/icons-vue'

const router = useRouter()

// 统计数据
const stats = ref({
  totalServices: 6,
  activeServices: 2,
  comingSoonServices: 4,
  categories: 4
})

// 导航到指定页面
const navigateTo = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.service-hub-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 10px 0;
  background: linear-gradient(135deg, #667eea, #764ba2);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-description {
  font-size: 1.1rem;
  color: #6b7280;
  margin: 0;
}

.service-categories {
  margin-bottom: 40px;
}

.category-section {
  margin-bottom: 40px;
}

.category-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-title .el-icon {
  font-size: 20px;
  color: #667eea;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.service-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  position: relative;
  overflow: hidden;
}

.service-card:hover:not(.coming-soon) {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
  border-color: #667eea;
}

.service-card.coming-soon {
  opacity: 0.7;
  cursor: not-allowed;
}

.service-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
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

.system-icon {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
  color: white;
}

.config-icon {
  background: linear-gradient(135deg, #fa709a, #fee140);
  color: white;
}

.data-icon {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
  color: #374151;
}

.backup-icon {
  background: linear-gradient(135deg, #ffecd2, #fcb69f);
  color: #374151;
}

.service-content {
  flex: 1;
}

.service-content h3 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.service-content p {
  color: #6b7280;
  margin: 0 0 12px 0;
  line-height: 1.5;
}

.service-features {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.feature-tag {
  background: #f3f4f6;
  color: #374151;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

.service-arrow {
  color: #9ca3af;
  font-size: 20px;
  transition: all 0.3s;
}

.service-card:hover:not(.coming-soon) .service-arrow {
  color: #667eea;
  transform: translateX(4px);
}

.coming-soon-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, #fbbf24, #f59e0b);
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 600;
}

.quick-stats {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 20px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  transition: all 0.3s;
}

.stat-item:hover {
  border-color: #667eea;
  transform: translateY(-2px);
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-label {
  color: #6b7280;
  font-size: 0.9rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .service-grid {
    grid-template-columns: 1fr;
  }
  
  .service-card {
    flex-direction: column;
    text-align: center;
  }
  
  .service-arrow {
    display: none;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style> 