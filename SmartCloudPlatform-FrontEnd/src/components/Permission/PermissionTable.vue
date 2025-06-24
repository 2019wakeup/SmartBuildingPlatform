<template>
  <el-table :data="data" @selection-change="$emit('selection-change', $event)" v-loading="loading">
    <el-table-column type="selection" width="55" />
    <el-table-column prop="permissionId" label="ID" width="80" />
    <el-table-column prop="code" label="权限代码" />
    <el-table-column prop="remark" label="备注" />
    <el-table-column label="操作" width="160">
      <template #default="scope">
        <el-button size="small" @click="$emit('edit', scope.row)">修改</el-button>
        <el-button size="small" type="danger" @click="$emit('delete', scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <div style="text-align: center; margin-top: 20px">
    <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50]"
        @size-change="onSizeChange"
        @current-change="onPageChange"
    />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  data: any[]
  total: number
  loading: boolean
  query: { pageNum: number; pageSize: number }
}>()
defineEmits(['selection-change', 'edit', 'delete', 'update:query'])

const onSizeChange = (val: number) => {
  emit('update:query', { pageSize: val, pageNum: 1 })
}
const onPageChange = (val: number) => {
  emit('update:query', { pageNum: val })
}
</script>
