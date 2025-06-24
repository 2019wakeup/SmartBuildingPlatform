<template>
  <div class="table">
    <el-table
        :data="userList"
        style="width: 100%"
        height="500"
        @row-click="row => $emit('row-click', row)"
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
          <el-button type="primary" text @click="$emit('more', scope.row)">More</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="val => $emit('size-change', val)"
          @current-change="val => $emit('current-change', val)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  userList: any[]
  total: number
  loading: boolean
  selectedUser: any
  queryParams: { pageNum: number; pageSize: number }
}>()
defineEmits(['row-click', 'size-change', 'current-change', 'more'])
</script>

<style scoped>
.table {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
