<template>
  <el-form :model="query" :inline="true">
    <el-form-item label="权限代码">
      <el-input v-model="query.code" placeholder="请输入权限代码" style="width: 200px" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="$emit('search')">搜索</el-button>
      <el-button @click="$emit('reset')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
defineProps<{ query: { code: string } }>()
defineEmits(['search', 'reset'])
</script>
