<template>
  <div class="role-management">
    <div class="page-title">角色管理</div>

    <RoleSearchForm v-model:queryParams="queryParams" @search="getList" />
    <RoleActionButtons
        :selected-rows="selectedRows"
        @add="handleAdd"
        @edit="handleEdit"
        @delete="handleDelete"
        @test="testAPI"
    />
    <RoleTable
        :data="roleList"
        :total="total"
        :loading="loading"
        v-model:selected-rows="selectedRows"
        v-model:query-params="queryParams"
        @refresh="getList"
        @edit="handleEdit"
        @delete="handleDelete"
    />
    <RoleDialog
        v-model:visible="dialogVisible"
        :role="roleForm"
        :is-edit="!!roleForm.roleId"
        @submit="submitForm"
        @cancel="resetForm"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/role'
import type { Role } from '@/api/types'

import RoleSearchForm from '@/components/Role/RoleSearchForm.vue'
import RoleActionButtons from '@/components/Role/RoleActionButtons.vue'
import RoleTable from '@/components/Role/RoleTable.vue'
import RoleDialog from '@/components/Role/RoleDialog.vue'

const roleList = ref<Role[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const selectedRows = ref<Role[]>([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: ''
})

const roleForm = ref<Role>({
  roleId: undefined,
  roleName: '',
  remark: ''
})

const getList = async () => {
  loading.value = true
  try {
    const response = await getRoleList(queryParams)
    if (response.success && response.data) {
      const rows = Array.isArray(response.data.rows)
          ? response.data.rows
          : response.data
      roleList.value = rows.map((role: any) => ({
        roleId: role.role_id ?? role.roleId,
        roleName: role.role_name ?? role.roleName,
        createTime: role.create_time ?? role.createTime,
        updateTime: role.update_time ?? role.updateTime,
        remark: role.remark
      }))
      total.value = response.data.total ?? rows.length
    } else {
      roleList.value = []
      total.value = 0
    }
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  roleForm.value = { roleId: undefined, roleName: '', remark: '' }
  dialogVisible.value = true
}

const handleEdit = (row?: Role) => {
  if (row) {
    roleForm.value = { ...row }
    dialogVisible.value = true
  }
}

const handleDelete = async (row?: Role) => {
  const roles = row ? [row] : selectedRows.value
  if (!roles.length) return
  await deleteRole(roles.map(r => r.roleId!))
  ElMessage.success('删除成功')
  getList()
}

const submitForm = async () => {
  if (roleForm.value.roleId) {
    await updateRole(roleForm.value)
    ElMessage.success('修改成功')
  } else {
    await addRole(roleForm.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  getList()
}

const resetForm = () => {
  roleForm.value = { roleId: undefined, roleName: '', remark: '' }
}

const testAPI = async () => {
  const res = await getRoleList({ pageNum: 1, pageSize: 10 })
  console.log('测试API响应:', res)
  ElMessage.success('API测试完成')
}

onMounted(getList)
</script>

<style scoped>
.role-management {
  height: 100%;
}
.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
}
</style>
