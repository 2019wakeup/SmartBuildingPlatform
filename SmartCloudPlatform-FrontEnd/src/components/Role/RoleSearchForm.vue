<template>
  <div class="search-form">
    <el-form :model="queryParams" :inline="true">
      <el-form-item label="角色名称">
        <el-input
            v-model="queryParams.roleName"
            placeholder="请输入角色名称"
            clearable
            style="width: 200px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="$emit('search')">
          <el-icon><Search /></el-icon>搜索
        </el-button>
        <el-button @click="reset">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{ queryParams: any }>()
const emit = defineEmits(['search'])

const reset = () => {
  props.queryParams.roleName = ''
  props.queryParams.pageNum = 1
  emit('search')
}
</script>

<style scoped>
.search-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
