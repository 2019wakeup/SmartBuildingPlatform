<template>
  <div class="category-section">
    <h2 class="category-title">
      <el-icon><component :is="icon" /></el-icon>
      {{ title }}
    </h2>
    <div class="service-grid">
      <ServiceCard
          v-for="card in cards"
          :key="card.title"
          :card="card"
          @click="card.comingSoon ? null : $emit('navigate', card.path)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import ServiceCard from './ServiceCard.vue'

defineProps<{ icon: string; title: string; cards: any[] }>()
</script>

<style scoped>
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
</style>
