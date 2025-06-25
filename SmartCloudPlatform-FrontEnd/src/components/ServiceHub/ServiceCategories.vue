<template>
  <div class="service-categories">
    <CategorySection
        icon="User"
        title="用户管理"
        :cards="[
        {
          icon: 'User',
          title: '用户管理',
          desc: '管理系统用户信息，包括用户的增删改查操作',
          features: ['用户列表', '用户详情', '状态管理'],
          path: '/user'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Key"
        title="角色权限管理"
        :cards="[
        {
          icon: 'UserFilled',
          title: '角色管理',
          desc: '配置系统角色信息，管理角色权限分配',
          features: ['角色配置', '权限分配', '角色授权'],
          path: '/role'
        },
        {
          icon: 'Key',
          title: '权限管理',
          desc: '设置系统访问权限，控制功能模块访问',
          features: ['权限配置', '访问控制', '权限审计'],
          path: '/permission'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Connection"
        title="IoT物联网管理"
        :cards="[
        {
          icon: 'Monitor',
          title: 'IoT数据监控',
          desc: '实时监控IoT设备数据，包括CO₂、TVOC、温湿度等传感器数据',
          features: ['实时监控', '数据可视化', '状态告警'],
          path: '/iot-dashboard'
        },
        {
          icon: 'Connection',
          title: 'IoT设备管理',
          desc: '管理IoT设备的注册、状态监控和配置信息',
          features: ['设备注册', '状态监控', '设备配置'],
          path: '/iot-device'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Setting"
        title="系统管理"
        :cards="[
        {
          icon: 'DataLine',
          title: '系统监控',
          desc: '监控系统运行状态，查看性能指标',
          features: ['性能监控', '日志管理', '告警通知'],
          comingSoon: true
        },
        {
          icon: 'Tools',
          title: '系统配置',
          desc: '管理系统参数配置，自定义系统设置',
          features: ['参数配置', '系统设置', '配置备份'],
          comingSoon: true
        }
      ]"
    />

    <CategorySection
        icon="DataBoard"
        title="数据管理"
        :cards="[
        {
          icon: 'PieChart',
          title: '数据统计',
          desc: '查看系统数据统计，生成分析报表',
          features: ['数据统计', '报表生成', '数据导出'],
          comingSoon: true
        },
        {
          icon: 'FolderOpened',
          title: '数据备份',
          desc: '管理数据备份任务，确保数据安全',
          features: ['自动备份', '数据恢复', '备份策略'],
          comingSoon: true
        }
      ]"
    />
  </div>
</template>

<script setup lang="ts">
import CategorySection from './CategorySection.vue'
</script>

<style scoped>
.service-categories {
  margin-bottom: 40px;
}
</style>