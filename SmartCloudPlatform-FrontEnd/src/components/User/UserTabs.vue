<template>
  <div class="tabs">
    <div
        v-for="tab in ['STUDENT', 'TEACHER', 'STAFF']"
        :key="tab"
        :class="['tab', { active: modelValue === tab }]"
        @click="$emit('update:activeTab', tab)"
    >
      {{ tab }}
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ modelValue: string }>()
defineEmits(['update:activeTab'])
</script>

<style scoped>
.tabs {
  display: flex;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}
.tab {
  flex: 1;
  text-align: center;
  padding: 15px;
  cursor: pointer;
  transition: 0.3s;
  font-weight: bold;
}
.tab.active {
  background: #4f46e5;
  color: #fff;
}
.tab:not(.active):hover {
  background: #f3f4f6;
}
</style>
