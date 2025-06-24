<template>
  <div class="service-hub-container">
    <PageHeader />
    <ServiceCategories @navigate="navigateTo" />
    <QuickStats :stats="stats" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import PageHeader from '@/components/ServiceHub/PageHeader.vue'
import ServiceCategories from '@/components/ServiceHub/ServiceCategories.vue'
import QuickStats from '@/components/ServiceHub/QuickStats.vue'

const router = useRouter()

const stats = ref({
  totalServices: 6,
  activeServices: 2,
  comingSoonServices: 4,
  categories: 4
})

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
</style>
