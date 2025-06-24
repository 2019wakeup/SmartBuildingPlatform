<template>
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
</template>

<script setup lang="ts">
defineProps<{ stats: any }>()
</script>

<style scoped>
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
</style>
