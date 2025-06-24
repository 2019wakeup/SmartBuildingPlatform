<template>
  <div style="margin-bottom: 20px">
    <el-button type="primary" @click="$emit('add')">新增</el-button>
    <el-button type="success" :disabled="selected.length !== 1" @click="$emit('edit')">修改</el-button>
    <el-button type="danger" :disabled="selected.length === 0" @click="$emit('delete')">删除</el-button>
  </div>
</template>

<script setup lang="ts">
defineProps<{ selected: any[] }>()
defineEmits(['add', 'edit', 'delete'])
</script>
