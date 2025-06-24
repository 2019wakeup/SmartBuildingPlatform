<template>
  <div class="table-container">
    <el-table
        :data="data"
        style="width: 100%"
        @selection-change="val => $emit('update:selectedRows', val)"
        v-loading="loading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="roleId" label="角色ID" width="100" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" text @click="$emit('edit', row)">修改</el-button>
          <el-button type="danger" text @click="$emit('delete', row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="$emit('refresh')"
          @current-change="$emit('refresh')"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  data: any[]
  total: number
  loading: boolean
  selectedRows: any[]
  queryParams: any
}>()
defineEmits(['update:selectedRows', 'update:queryParams', 'edit', 'delete', 'refresh'])
</script>

<style scoped>
.table-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
