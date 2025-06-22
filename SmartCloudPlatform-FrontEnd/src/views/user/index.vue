<template>
  <div class="user-management">
    <!-- 页面标题和操作区域 -->
    <div class="page-header">
      <div class="header-info">
        <div class="user-details">
          <span class="label">NO.</span>
          <el-input v-model="currentUser.no" class="detail-input" readonly />
          <span class="label">ID</span>
          <el-input v-model="currentUser.id" class="detail-input" readonly />
          <span class="label">Name</span>
          <el-input v-model="currentUser.name" class="detail-input" readonly />
        </div>
        <div class="action-buttons">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            ADD
          </el-button>
          <el-button type="success" @click="handleEdit" :disabled="!selectedUser">
            <el-icon><Edit /></el-icon>
            EDIT
          </el-button>
          <el-button type="danger" @click="handleDelete" :disabled="!selectedUser">
            <el-icon><Delete /></el-icon>
            DELETE
          </el-button>
          <el-button type="info" @click="handleSearch">
            <el-icon><Search /></el-icon>
            SEARCH
          </el-button>
        </div>
      </div>
    </div>

    <!-- 用户分类标签 -->
    <div class="user-tabs">
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'STUDENT' }"
        @click="activeTab = 'STUDENT'"
      >
        STUDENT
      </div>
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'TEACHER' }"
        @click="activeTab = 'TEACHER'"
      >
        TEACHER
      </div>
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'STAFF' }"
        @click="activeTab = 'STAFF'"
      >
        STAFF
      </div>
    </div>

    <!-- 用户列表表格 -->
    <div class="table-container">
      <el-table
        :data="userList"
        style="width: 100%"
        @row-click="handleRowClick"
        :row-class-name="getRowClassName"
        height="500"
      >
        <el-table-column prop="no" label="NO." width="80" align="center" />
        <el-table-column prop="userId" label="ID" width="150" align="center" />
        <el-table-column prop="userName" label="Name" width="120" align="center" />
        <el-table-column prop="gender" label="Gender" width="80" align="center" />
        <el-table-column prop="age" label="Age" width="80" align="center" />
        <el-table-column prop="birth" label="Birth" width="120" align="center" />
        <el-table-column prop="city" label="City" width="100" align="center" />
        <el-table-column label="More" align="center">
          <template #default="scope">
            <el-button type="primary" text @click="handleMore(scope.row)">More</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="userForm.userName" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="用户账号" prop="account">
          <el-input v-model="userForm.account" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userForm.gender">
            <el-radio label="Male">男</el-radio>
            <el-radio label="Female">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="userForm.age" :min="1" :max="120" />
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker
            v-model="userForm.birth"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="userForm.city" placeholder="请输入城市" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'
import type { User } from '@/api/types'

// 响应式数据
const userList = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const activeTab = ref('STUDENT')
const selectedUser = ref<any>(null)

// 当前显示的用户信息
const currentUser = reactive({
  no: '001',
  id: '202231123002001',
  name: 'One'
})

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  account: ''
})

// 用户表单
const userForm = ref<any>({
  userId: undefined,
  userName: '',
  account: '',
  email: '',
  phone: '',
  gender: 'Male',
  age: 20,
  birth: '2004-01-01',
  city: 'Wuhan'
})

// 表单验证规则
const userRules = {
  userName: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' }
  ],
  account: [
    { required: true, message: '用户账号不能为空', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

const userFormRef = ref()

// 计算属性
const dialogTitle = computed(() => {
  return userForm.value.userId ? '修改用户' : '添加用户'
})

// 获取用户列表
const getList = async () => {
  loading.value = true
  try {
    const response = await getUserList(queryParams)
    userList.value = [
      {
        no: '001',
        userId: '202231123002001',
        userName: 'Zhang San',
        gender: 'Male',
        age: 20,
        birth: '2004.01.01',
        city: 'Wuhan'
      }
    ]
    total.value = 1
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理行点击
const handleRowClick = (row: any) => {
  selectedUser.value = row
  currentUser.no = row.no
  currentUser.id = row.userId
  currentUser.name = row.userName
}

// 获取行类名
const getRowClassName = ({ row }: { row: any }) => {
  return selectedUser.value === row ? 'selected-row' : ''
}

// 处理添加
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = () => {
  if (!selectedUser.value) {
    ElMessage.warning('请先选择要编辑的用户')
    return
  }
  userForm.value = { ...selectedUser.value }
  dialogVisible.value = true
}

// 处理删除
const handleDelete = async () => {
  if (!selectedUser.value) {
    ElMessage.warning('请先选择要删除的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteUser([selectedUser.value.userId])
    ElMessage.success('删除成功')
    getList()
    selectedUser.value = null
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 处理搜索
const handleSearch = () => {
  queryParams.pageNum = 1
  getList()
}

// 处理更多操作
const handleMore = (row: any) => {
  ElMessage.info(`查看用户 ${row.userName} 的更多信息`)
}

// 分页处理
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

// 提交表单
const submitForm = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (userForm.value.userId) {
          await updateUser(userForm.value)
          ElMessage.success('修改成功')
        } else {
          await addUser(userForm.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  userForm.value = {
    userId: undefined,
    userName: '',
    account: '',
    email: '',
    phone: '',
    gender: 'Male',
    age: 20,
    birth: '2004-01-01',
    city: 'Wuhan'
  }
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.user-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-details {
  display: flex;
  align-items: center;
  gap: 15px;
}

.label {
  font-weight: 600;
  color: #374151;
  min-width: 40px;
}

.detail-input {
  width: 150px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.user-tabs {
  display: flex;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  overflow: hidden;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 15px 20px;
  background: #f8fafc;
  color: #6b7280;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
}

.tab-item.active {
  background: #4f46e5;
  color: #ffffff;
}

.tab-item:hover:not(.active) {
  background: #e5e7eb;
}

.table-container {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

:deep(.selected-row) {
  background-color: #eff6ff !important;
}

:deep(.selected-row:hover) {
  background-color: #dbeafe !important;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 