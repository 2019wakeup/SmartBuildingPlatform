<template>
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
</template>

<script setup lang="ts">
// 静态展示，无需 props
</script>

<style scoped>
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
  margin-bottom: 8px;
  color: #374151;
}
.info-card p {
  margin: 0;
  color: #6b7280;
  font-size: 0.9rem;
}
</style>
