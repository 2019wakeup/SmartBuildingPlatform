<template>
  <div class="permission-management">
    <div class="page-title">权限管理</div>
    
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="queryParams" :inline="true">
        <el-form-item label="权限代码">
          <el-input
            v-model="queryParams.code"
            placeholder="请输入权限代码"
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
        新增权限
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
    </div>

    <!-- 权限列表表格 -->
    <div class="table-container">
      <el-table
        :data="permissionList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="permissionId" label="权限ID" width="100" />
        <el-table-column prop="code" label="权限代码" />
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

    <!-- 添加/编辑权限对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="100px"
      >
        <el-form-item label="权限代码" prop="code">
          <el-input v-model="permissionForm.code" placeholder="请输入权限代码，如：user:list" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="permissionForm.remark"
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
import type { Permission } from '@/api/types'

// 响应式数据
const permissionList = ref<Permission[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const selectedRows = ref<Permission[]>([])

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  code: ''
})

// 权限表单
const permissionForm = ref<Permission>({
  permissionId: undefined,
  code: '',
  remark: ''
})

// 表单验证规则
const permissionRules = {
  code: [
    { required: true, message: '权限代码不能为空', trigger: 'blur' },
    { max: 50, message: '权限代码长度不能超过50个字符', trigger: 'blur' }
  ]
}

const permissionFormRef = ref()

// 计算属性
const dialogTitle = computed(() => {
  return permissionForm.value.permissionId ? '修改权限' : '添加权限'
})

// 获取权限列表（模拟数据，因为后端API文档中没有权限管理的接口）
const getList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    permissionList.value = [
      {
        permissionId: 1,
        code: 'user:list',
        createTime: '2023-01-01 10:00:00',
        updateTime: '2023-01-01 10:00:00',
        remark: '用户列表查看权限'
      },
      {
        permissionId: 2,
        code: 'user:add',
        createTime: '2023-01-01 10:00:00',
        updateTime: '2023-01-01 10:00:00',
        remark: '用户添加权限'
      },
      {
        permissionId: 3,
        code: 'user:edit',
        createTime: '2023-01-01 10:00:00',
        updateTime: '2023-01-01 10:00:00',
        remark: '用户编辑权限'
      },
      {
        permissionId: 4,
        code: 'user:delete',
        createTime: '2023-01-01 10:00:00',
        updateTime: '2023-01-01 10:00:00',
        remark: '用户删除权限'
      },
      {
        permissionId: 5,
        code: 'role:list',
        createTime: '2023-01-01 10:00:00',
        updateTime: '2023-01-01 10:00:00',
        remark: '角色列表查看权限'
      }
    ]
    total.value = 5
  } catch (error) {
    console.error('获取权限列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = (selection: Permission[]) => {
  selectedRows.value = selection
}

// 处理搜索
const handleSearch = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置搜索
const resetSearch = () => {
  queryParams.code = ''
  queryParams.pageNum = 1
  getList()
}

// 处理添加
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row?: Permission) => {
  if (row) {
    permissionForm.value = { ...row }
  } else if (selectedRows.value.length === 1) {
    permissionForm.value = { ...selectedRows.value[0] }
  } else {
    ElMessage.warning('请选择一条记录进行编辑')
    return
  }
  dialogVisible.value = true
}

// 处理删除
const handleDelete = async (row?: Permission) => {
  const permissions = row ? [row] : selectedRows.value
  if (permissions.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除选中的权限吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // 模拟删除操作
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
  if (!permissionFormRef.value) return

  await permissionFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        // 模拟API调用
        if (permissionForm.value.permissionId) {
          ElMessage.success('修改成功')
        } else {
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
  permissionForm.value = {
    permissionId: undefined,
    code: '',
    remark: ''
  }
  if (permissionFormRef.value) {
    permissionFormRef.value.resetFields()
  }
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.permission-management {
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