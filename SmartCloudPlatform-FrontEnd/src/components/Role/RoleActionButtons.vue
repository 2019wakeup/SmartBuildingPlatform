<template>
  <div class="action-buttons">
    <el-button type="primary" @click="$emit('add')">
      <el-icon><Plus /></el-icon>新增角色
    </el-button>
    <el-button type="success" @click="$emit('edit')" :disabled="selectedRows.length !== 1">
      <el-icon><Edit /></el-icon>修改
    </el-button>
    <el-button type="danger" @click="$emit('delete')" :disabled="selectedRows.length === 0">
      <el-icon><Delete /></el-icon>删除
    </el-button>
    <el-button type="warning" @click="$emit('test')">
      <el-icon><Tools /></el-icon>测试API
    </el-button>
  </div>
</template>

<script setup lang="ts">
defineProps<{ selectedRows: any[] }>()
defineEmits(['add', 'edit', 'delete', 'test'])
</script>

<style scoped>
.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
