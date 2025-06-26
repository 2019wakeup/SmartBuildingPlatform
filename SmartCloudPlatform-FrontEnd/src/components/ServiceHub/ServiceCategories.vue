<template>
  <div class="service-categories">
    <CategorySection
        icon="User"
        title="User Management"
        :cards="[
        {
          icon: 'User',
          title: 'User Management',
          desc: 'Manage system user information, including user CRUD operations',
          features: ['User List', 'User Details', 'Status Management'],
          path: '/user'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Key"
        title="Role & Permission Management"
        :cards="[
        {
          icon: 'UserFilled',
          title: 'Role Management',
          desc: 'Configure system role information and manage role permission assignment',
          features: ['Role Configuration', 'Permission Assignment', 'Role Authorization'],
          path: '/role'
        },
        {
          icon: 'Key',
          title: 'Permission Management',
          desc: 'Set system access permissions and control module access',
          features: ['Permission Configuration', 'Access Control', 'Permission Audit'],
          path: '/permission'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Connection"
        title="IoT Network Management"
        :cards="[
        {
          icon: 'Monitor',
          title: 'IoT Data Monitoring',
          desc: 'Real-time monitoring of IoT device data, including CO₂, TVOC, temperature and humidity sensor data',
          features: ['Real-time Monitoring', 'Data Visualization', 'Status Alerts'],
          path: '/iot-dashboard'
        },
        {
          icon: 'Connection',
          title: 'IoT Device Management',
          desc: 'Manage IoT device registration, status monitoring and configuration information',
          features: ['Device Registration', 'Status Monitoring', 'Device Configuration'],
          path: '/iot-device'
        }
      ]"
        @navigate="$emit('navigate', $event)"
    />

    <CategorySection
        icon="Setting"
        title="System Management"
        :cards="[
        {
          icon: 'DataLine',
          title: 'System Monitoring',
          desc: 'Monitor system operation status and view performance metrics',
          features: ['Performance Monitoring', 'Log Management', 'Alert Notifications'],
          comingSoon: true
        },
        {
          icon: 'Tools',
          title: 'System Configuration',
          desc: 'Manage system parameter configuration and customize system settings',
          features: ['Parameter Configuration', 'System Settings', 'Configuration Backup'],
          comingSoon: true
        }
      ]"
    />

    <CategorySection
        icon="DataBoard"
        title="Data Management"
        :cards="[
        {
          icon: 'PieChart',
          title: 'Data Statistics',
          desc: 'View system data statistics and generate analytical reports',
          features: ['Data Statistics', 'Report Generation', 'Data Export'],
          comingSoon: true
        },
        {
          icon: 'FolderOpened',
          title: 'Data Backup',
          desc: 'Manage data backup tasks to ensure data security',
          features: ['Automatic Backup', 'Data Recovery', 'Backup Strategy'],
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