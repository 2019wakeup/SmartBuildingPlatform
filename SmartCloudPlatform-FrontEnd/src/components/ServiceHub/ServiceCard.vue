<template>
  <div class="service-card" :class="{ 'coming-soon': card.comingSoon }">
    <div class="service-icon" :class="card.icon + '-icon'">
      <el-icon><component :is="card.icon" /></el-icon>
    </div>
    <div class="service-content">
      <h3>{{ card.title }}</h3>
      <p>{{ card.desc }}</p>
      <div class="service-features">
        <span v-for="feature in card.features" :key="feature" class="feature-tag">{{ feature }}</span>
      </div>
    </div>
    <div class="service-arrow" v-if="!card.comingSoon">
      <el-icon><ArrowRight /></el-icon>
    </div>
    <div class="coming-soon-badge" v-if="card.comingSoon">
      即将上线
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowRight } from '@element-plus/icons-vue'

defineProps<{ card: any }>()
</script>

<style scoped>
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
</style>
