<template>
  <div class="role-management">
    <div class="page-title">角色管理</div>
    
    <!-- 搜索表单 -->
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
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增角色
      </el-button>
      <el-button 
        type="success" 
        @click="handleEdit" 
        :disabled="selectedRows.length !== 1"
      >
        <el-icon><Edit /></el-icon>
        修改
      </el-button>
      <el-button 
        type="danger" 
        @click="handleDelete" 
        :disabled="selectedRows.length === 0"
      >
        <el-icon><Delete /></el-icon>
        删除
      </el-button>
      <el-button type="warning" @click="testAPI">
        <el-icon><Tools /></el-icon>
        测试API
      </el-button>
    </div>

    <!-- 角色列表表格 -->
    <div class="table-container">
      <el-table
        :data="roleList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="roleId" label="角色ID" width="100" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" text @click="handleEdit(scope.row)">
              修改
            </el-button>
            <el-button type="danger" text @click="handleDelete(scope.row)">
              删除
            </el-button>
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

    <!-- 添加/编辑角色对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="roleForm.remark"
            type="textarea"
            :rows="4"
            placeholder="请输入备注"
          />
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
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/role'
import type { Role } from '@/api/types'

// 响应式数据
const roleList = ref<Role[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const selectedRows = ref<Role[]>([])

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: ''
})

// 角色表单
const roleForm = ref<Role>({
  roleId: undefined,
  roleName: '',
  remark: ''
})

// 表单验证规则
const roleRules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' }
  ]
}

const roleFormRef = ref()

// 计算属性
const dialogTitle = computed(() => {
  return roleForm.value.roleId ? '修改角色' : '添加角色'
})

// 获取角色列表
const getList = async () => {
  loading.value = true
  try {
    const response = await getRoleList(queryParams)
    console.log('角色列表响应:', response)
    
    if (response.success && response.data) {
      // 处理后端返回的分页数据
      if (response.data.rows) {
        // 如果是分页数据格式，映射蛇形命名到驼峰命名
        roleList.value = response.data.rows.map((role: any) => ({
          roleId: role.role_id || role.roleID || role.roleId,
          roleName: role.role_name || role.roleName,
          createTime: role.create_time || role.createTime,
          updateTime: role.update_time || role.updateTime,
          remark: role.remark
        }))
        total.value = response.data.total || 0
      } else if (Array.isArray(response.data)) {
        // 如果直接返回数组
        roleList.value = response.data.map((role: any) => ({
          roleId: role.role_id || role.roleID || role.roleId,
          roleName: role.role_name || role.roleName,
          createTime: role.create_time || role.createTime,
          updateTime: role.update_time || role.updateTime,
          remark: role.remark
        }))
        total.value = response.data.length
      } else {
        roleList.value = []
        total.value = 0
      }
    } else {
      roleList.value = []
      total.value = 0
      ElMessage.warning(response.msg || '获取角色列表失败')
    }
  } catch (error: any) {
    console.error('获取角色列表失败:', error)
    ElMessage.error(error.message || '获取角色列表失败')
    roleList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = (selection: Role[]) => {
  selectedRows.value = selection
}

// 处理搜索
const handleSearch = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置搜索
const resetSearch = () => {
  queryParams.roleName = ''
  queryParams.pageNum = 1
  getList()
}

// 处理添加
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row?: Role) => {
  if (row) {
    roleForm.value = { ...row }
  } else if (selectedRows.value.length === 1) {
    roleForm.value = { ...selectedRows.value[0] }
  } else {
    ElMessage.warning('请选择一条记录进行编辑')
    return
  }
  dialogVisible.value = true
}

// 处理删除
const handleDelete = async (row?: Role) => {
  const roles = row ? [row] : selectedRows.value
  if (roles.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除选中的角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const roleIds = roles.map(role => role.roleId!).filter(id => id !== undefined)
    await deleteRole(roleIds)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
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
  if (!roleFormRef.value) return

  await roleFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        console.log('提交角色数据:', roleForm.value)
        
        if (roleForm.value.roleId) {
          const response = await updateRole(roleForm.value)
          console.log('修改角色响应:', response)
          ElMessage.success('修改成功')
        } else {
          const response = await addRole(roleForm.value)
          console.log('添加角色响应:', response)
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
  roleForm.value = {
    roleId: undefined,
    roleName: '',
    remark: ''
  }
  if (roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
}

// 初始化
onMounted(() => {
  getList()
})

// 测试API连接
const testAPI = async () => {
  console.log('开始测试角色API...')
  
  try {
    // 测试角色列表API
    console.log('测试角色列表API...')
    const roleResponse = await getRoleList({ pageNum: 1, pageSize: 10 })
    console.log('角色列表API响应:', roleResponse)
    
    // 测试角色选项API
    console.log('测试角色选项API...')
    const { getRoleOptionSelect } = await import('@/api/role')
    const optionResponse = await getRoleOptionSelect()
    console.log('角色选项API响应:', optionResponse)
    
    ElMessage.success('角色API测试完成，请查看控制台日志')
  } catch (error: any) {
    console.error('角色API测试失败:', error)
    ElMessage.error(`角色API测试失败: ${error.message}`)
  }
}
</script>

<style scoped>
.role-management {
  height: 100%;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 20px;
}

.search-form {
  background: #ffffff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.table-container {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
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