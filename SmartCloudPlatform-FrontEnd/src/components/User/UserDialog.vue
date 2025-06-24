<template>
  <el-dialog :model-value="visible" :title="isEdit ? '修改用户' : '添加用户'" @close="$emit('reset')" width="600px">
    <el-form :model="formData" ref="formRef" label-width="100px">
      <el-form-item label="用户名"><el-input v-model="formData.userName" /></el-form-item>
      <el-form-item label="账号"><el-input v-model="formData.account" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="formData.email" /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="formData.phone" /></el-form-item>
      <el-form-item label="角色ID"><el-input-number v-model="formData.roleId" :min="1" :max="10" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('reset')">取消</el-button>
      <el-button type="primary" @click="$emit('submit')">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
defineProps<{
  visible: boolean
  formData: any
  isEdit: boolean
}>()
defineEmits(['submit', 'reset'])
</script>

<style scoped>
</style>
