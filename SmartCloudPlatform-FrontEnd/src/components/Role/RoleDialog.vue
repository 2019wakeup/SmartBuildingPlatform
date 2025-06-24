<template>
  <el-dialog
      :model-value="visible"
      @update:model-value="emit('update:visible', $event)"
      :title="isEdit ? '修改角色' : '新增角色'"
      width="600px"
      @close="emit('cancel')"
  >
    <el-form :model="role" ref="formRef" :rules="rules" label-width="100px">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="role.roleName" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="role.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="emit('cancel')">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  visible: boolean
  role: any
  isEdit: boolean
}>()

const emit = defineEmits(['submit', 'cancel', 'update:visible'])

const rules = {
  roleName: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }]
}

const formRef = ref()

const onSubmit = async () => {
  if (formRef.value) {
    await formRef.value.validate()
    emit('submit')
  }
}
</script>
