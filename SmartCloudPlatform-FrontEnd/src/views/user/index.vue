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
          <el-button type="warning" @click="testAPI">
            <el-icon><Tools /></el-icon>
            测试API
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
        <el-table-column prop="account" label="Account" width="120" align="center" />
        <el-table-column prop="email" label="Email" width="180" align="center" />
        <el-table-column prop="phone" label="Phone" width="120" align="center" />
        <el-table-column prop="roleId" label="Role ID" width="80" align="center" />
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
        <el-form-item label="默认密码" v-if="!userForm.userId">
          <el-input v-model="userForm.password" placeholder="新用户默认密码" readonly />
        </el-form-item>
        <el-form-item label="角色ID">
          <el-input-number v-model="userForm.roleId" :min="1" :max="10" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="debugFormData" type="info" plain>调试数据</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
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
  password: '123456', // 新用户默认密码
  email: '',
  phone: '',
  roleId: 2 // 默认普通用户角色
})

// 表单验证规则
const userRules = {
  userName: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' },
    { min: 2, max: 50, message: '用户名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  account: [
    { required: true, message: '用户账号不能为空', trigger: 'blur' },
    { min: 3, max: 50, message: '用户账号长度在 3 到 50 个字符', trigger: 'blur' }
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
    console.log('用户列表响应:', response)
    
    if (response.success && response.data) {
      // 处理后端返回的分页数据
      if (response.data.rows) {
        // 如果是分页数据格式
        userList.value = response.data.rows.map((user: any, index: number) => ({
          no: String(index + 1).padStart(3, '0'),
          userId: user.user_id || user.userID || user.userId,
          userName: user.user_name || user.userName,
          account: user.account,
          email: user.email,
          phone: user.phone,
          roleId: user.role_id || user.roleID || user.roleId
        }))
        total.value = response.data.total || 0
      } else if (Array.isArray(response.data)) {
        // 如果直接返回数组
        userList.value = response.data.map((user: any, index: number) => ({
          no: String(index + 1).padStart(3, '0'),
          userId: user.user_id || user.userID || user.userId,
          userName: user.user_name || user.userName,
          account: user.account,
          email: user.email,
          phone: user.phone,
          roleId: user.role_id || user.roleID || user.roleId
        }))
        total.value = response.data.length
      } else {
        userList.value = []
        total.value = 0
      }
    } else {
      userList.value = []
      total.value = 0
      ElMessage.warning(response.msg || '获取用户列表失败')
    }
  } catch (error: any) {
    console.error('获取用户列表失败:', error)
    ElMessage.error(error.message || '获取用户列表失败')
    userList.value = []
    total.value = 0
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
  
  // 将后端的蛇形命名数据转换为前端表单的驼峰命名
  userForm.value = {
    userId: selectedUser.value.userId,
    userName: selectedUser.value.userName,
    account: selectedUser.value.account,
    password: '123456', // 编辑时不显示原密码
    email: selectedUser.value.email || '',
    phone: selectedUser.value.phone || '',
    roleId: selectedUser.value.roleId || 2
  }
  
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
        // 确保所有必需字段都有值
        const formData = {
          ...userForm.value,
          userName: userForm.value.userName?.trim() || '',
          account: userForm.value.account?.trim() || '',
          email: userForm.value.email?.trim() || '',
          phone: userForm.value.phone?.trim() || '',
          password: userForm.value.password || '123456',
          roleId: userForm.value.roleId || 2
        }
        
        // 转换为蛇形命名法以匹配后端配置
        const submitData = {
          user_id: formData.userId,
          user_name: formData.userName,
          account: formData.account,
          password: formData.password,
          email: formData.email,
          phone: formData.phone,
          role_id: formData.roleId
        }
        
        console.log('提交用户数据 (蛇形命名):', submitData)
        
        // 验证必需字段
        if (!submitData.user_name) {
          ElMessage.error('用户名称不能为空')
          return
        }
        if (!submitData.account) {
          ElMessage.error('用户账号不能为空')
          return
        }
        
        if (submitData.user_id) {
          const response = await updateUser(submitData)
          console.log('修改用户响应:', response)
          ElMessage.success('修改成功')
        } else {
          const response = await addUser(submitData)
          console.log('添加用户响应:', response)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error: any) {
        console.error('操作失败:', error)
        ElMessage.error(error.message || '操作失败')
      }
    } else {
      console.log('表单验证失败')
      ElMessage.error('请检查表单填写是否正确')
    }
  })
}

// 重置表单
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
  
  // 等待下一个tick再重置表单验证
  nextTick(() => {
    if (userFormRef.value) {
      userFormRef.value.clearValidate()
    }
  })
}

// 初始化
onMounted(() => {
  getList()
})

// 测试API连接
const testAPI = async () => {
  console.log('开始测试API...')
  
  try {
    // 测试用户列表API
    console.log('测试用户列表API...')
    const userResponse = await getUserList({ pageNum: 1, pageSize: 10 })
    console.log('用户列表API响应:', userResponse)
    
    // 测试角色列表API
    console.log('测试角色列表API...')
    const { getRoleList } = await import('@/api/role')
    const roleResponse = await getRoleList({ pageNum: 1, pageSize: 10 })
    console.log('角色列表API响应:', roleResponse)
    
    ElMessage.success('API测试完成，请查看控制台日志')
  } catch (error: any) {
    console.error('API测试失败:', error)
    ElMessage.error(`API测试失败: ${error.message}`)
  }
}

// 添加调试按钮
const debugFormData = () => {
  console.log('调试表单数据:', userForm.value)
}
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