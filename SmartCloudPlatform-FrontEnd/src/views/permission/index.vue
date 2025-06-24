<template>
  <div class="permission-management">
    <div class="page-title">权限管理</div>

    <SearchForm :query="queryParams" @search="handleSearch" @reset="resetSearch" />

    <ActionButtons
        :selected="selectedRows"
        @add="handleAdd"
        @edit="handleEdit"
        @delete="handleDelete"
    />

    <PermissionTable
        :loading="loading"
        :data="permissionList"
        :total="total"
        :query="queryParams"
        @update:query="updateQuery"
        @selection-change="handleSelectionChange"
        @edit="handleEdit"
        @delete="handleDelete"
    />

    <PermissionDialog
        v-model:visible="dialogVisible"
        :form="permissionForm"
        :rules="permissionRules"
        @submit="submitForm"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import SearchForm from '@/components/Permission/SearchForm.vue'
import ActionButtons from '@/components/Permission/ActionButtons.vue'
import PermissionTable from '@/components/Permission/PermissionTable.vue'
import PermissionDialog from '@/components/Permission/PermissionDialog.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Permission } from '@/api/types'

// 状态变量
const permissionList = ref<Permission[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const selectedRows = ref<Permission[]>([])

const queryParams = reactive({ pageNum: 1, pageSize: 10, code: '' })
const permissionForm = ref<Permission>({ permissionId: undefined, code: '', remark: '' })
const permissionRules = {
  code: [
    { required: true, message: '权限代码不能为空', trigger: 'blur' },
    { max: 50, message: '权限代码长度不能超过50个字符', trigger: 'blur' }
  ]
}

// 初始化
onMounted(() => getList())

const getList = async () => {
  loading.value = true
  await new Promise(resolve => setTimeout(resolve, 500))
  permissionList.value = [
    { permissionId: 1, code: 'user:list', remark: '用户列表查看权限', createTime: '', updateTime: '' },
    { permissionId: 2, code: 'user:add', remark: '用户添加权限', createTime: '', updateTime: '' }
  ]
  total.value = permissionList.value.length
  loading.value = false
}

const updateQuery = (params: any) => {
  Object.assign(queryParams, params)
  getList()
}
const handleSelectionChange = (rows: Permission[]) => selectedRows.value = rows
const handleSearch = () => { queryParams.pageNum = 1; getList() }
const resetSearch = () => { queryParams.code = ''; getList() }

const handleAdd = () => {
  permissionForm.value = { permissionId: undefined, code: '', remark: '' }
  dialogVisible.value = true
}
const handleEdit = (row?: Permission) => {
  const data = row || selectedRows.value[0]
  if (!data) return ElMessage.warning('请选择一条记录进行编辑')
  permissionForm.value = { ...data }
  dialogVisible.value = true
}
const handleDelete = async (row?: Permission) => {
  const list = row ? [row] : selectedRows.value
  if (!list.length) return ElMessage.warning('请选择要删除的记录')
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    getList()
  } catch {}
}

const submitForm = () => {
  ElMessage.success(permissionForm.value.permissionId ? '修改成功' : '添加成功')
  dialogVisible.value = false
  getList()
}
</script>

<style scoped>
.permission-management {
  padding: 20px;
}
.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
}
</style>
