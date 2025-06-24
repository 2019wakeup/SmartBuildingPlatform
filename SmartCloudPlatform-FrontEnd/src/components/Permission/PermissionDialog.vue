<template>
  <el-dialog :model-value="visible" title="权限配置" width="500px" @close="$emit('update:visible', false)">
    <el-form :model="form" :rules="rules" label-width="100px" ref="formRef">
      <el-form-item label="权限代码" prop="code">
        <el-input v-model="form.code" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remark" :rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" @click="onSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
defineProps<{ visible: boolean; form: any; rules: any }>()
const emit = defineEmits(['update:visible', 'submit'])

const formRef = ref()
const onSubmit = () => {
  if (formRef.value) {
    formRef.value.validate((valid: boolean) => {
      if (valid) emit('submit')
    })
  }
}
</script>
