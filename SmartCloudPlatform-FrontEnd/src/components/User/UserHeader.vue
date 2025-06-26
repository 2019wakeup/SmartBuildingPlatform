<template>
  <div class="user-header">
    <div class="info">
      <span>NO.</span><el-input v-model="user.no" class="input" readonly />
      <span>ID</span><el-input v-model="user.id" class="input" readonly />
      <span>Name</span><el-input v-model="user.name" class="input" readonly />
    </div>
    <div class="buttons">
      <el-button type="primary" @click="$emit('add')"><el-icon><Plus /></el-icon>ADD</el-button>
      <el-button type="success" @click="$emit('edit')"><el-icon><Edit /></el-icon>EDIT</el-button>
      <el-button type="danger" @click="$emit('delete')"><el-icon><Delete /></el-icon>DELETE</el-button>
      <el-button type="info" @click="$emit('search')"><el-icon><Search /></el-icon>SEARCH</el-button>
      <el-button type="warning" @click="$emit('test-api')"><el-icon><Tools /></el-icon>Test API</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ currentUser: any; selectedUser: any }>()
const user = defineModel('currentUser')
</script>

<style scoped>
.user-header {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.info {
  display: flex;
  align-items: center;
  gap: 12px;
}
.input {
  width: 140px;
}
.buttons {
  display: flex;
  gap: 10px;
}
</style>
