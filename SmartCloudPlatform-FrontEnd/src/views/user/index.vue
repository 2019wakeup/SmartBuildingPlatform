<template>
  <div class="user-management">
    <UserHeader
        :current-user="currentUser"
        :selected-user="selectedUser"
        @add="handleAdd"
        @edit="handleEdit"
        @delete="handleDelete"
        @search="handleSearch"
        @test-api="testAPI"
    />

    <UserTabs v-model:activeTab="activeTab" />

    <UserTable
        :user-list="userList"
        :query-params="queryParams"
        :total="total"
        :loading="loading"
        :selected-user="selectedUser"
        @row-click="handleRowClick"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        @more="handleMore"
    />

    <UserDialog
        v-model:visible="dialogVisible"
        :form-data="userForm"
        :is-edit="!!userForm.userId"
        @submit="submitForm"
        @reset="resetForm"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import UserHeader from '@/components/User/UserHeader.vue'
import UserTabs from '@/components/User/UserTabs.vue'
import UserTable from '@/components/User/UserTable.vue'
import UserDialog from '@/components/User/UserDialog.vue'
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'

const userList = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const selectedUser = ref<any>(null)
const activeTab = ref('STUDENT')

const currentUser = reactive({
  no: '001',
  id: '202231123002001',
  name: 'One'
})

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  account: ''
})

const userForm = ref<any>({
  userId: undefined,
  userName: '',
  account: '',
  password: '123456',
  email: '',
  phone: '',
  roleId: 2
})

const getList = async () => {
  loading.value = true
  try {
    const response = await getUserList(queryParams)
    userList.value = response.data?.rows?.map((u: any, i: number) => ({
      no: String(i + 1).padStart(3, '0'),
      userId: u.user_id,
      userName: u.user_name,
      account: u.account,
      email: u.email,
      phone: u.phone,
      roleId: u.role_id
    })) || []
    total.value = response.data?.total || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleRowClick = (row: any) => {
  selectedUser.value = row
  currentUser.no = row.no
  currentUser.id = row.userId
  currentUser.name = row.userName
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = () => {
  if (!selectedUser.value) return ElMessage.warning('请先选择用户')
  userForm.value = { ...selectedUser.value, password: '123456' }
  dialogVisible.value = true
}

const handleDelete = async () => {
  if (!selectedUser.value) return
  await ElMessageBox.confirm('确定要删除该用户吗？', '提示')
  await deleteUser([selectedUser.value.userId])
  ElMessage.success('删除成功')
  getList()
}

const handleSearch = () => {
  queryParams.pageNum = 1
  getList()
}

const handleMore = (row: any) => {
  ElMessage.info(`查看用户 ${row.userName} 的更多信息`)
}

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

const submitForm = async () => {
  const data = {
    user_id: userForm.value.userId,
    user_name: userForm.value.userName,
    account: userForm.value.account,
    password: userForm.value.password,
    email: userForm.value.email,
    phone: userForm.value.phone,
    role_id: userForm.value.roleId
  }

  const res = userForm.value.userId ? await updateUser(data) : await addUser(data)
  ElMessage.success(userForm.value.userId ? '修改成功' : '添加成功')
  dialogVisible.value = false
  getList()
}

const resetForm = () => {
  userForm.value = {
    userId: undefined,
    userName: '',
    account: '',
    password: '123456',
    email: '',
    phone: '',
    roleId: 2
  }
}

const testAPI = async () => {
  const res = await getUserList({ pageNum: 1, pageSize: 10 })
  console.log('用户API响应：', res)
  ElMessage.success('API 测试成功')
}

onMounted(getList)
</script>

<style scoped>
.user-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
