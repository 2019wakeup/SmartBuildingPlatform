<template>
  <div class="home-container">
    <Welcome :userInfo="userInfo" />
    <StatsCards :stats="stats" />
    <QuickActions @navigate="navigateTo" />
    <SystemInfo />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfo as getAuthUserInfo } from '@/api/auth'
import { getUserInfo as getStoredUserInfo } from '@/utils/auth'

import Welcome from '@/components/Home/Welcome.vue'
import StatsCards from '@/components/Home/StatsCards.vue'
import QuickActions from '@/components/Home/QuickActions.vue'
import SystemInfo from '@/components/Home/SystemInfo.vue'

const router = useRouter()
const userInfo = ref<any>(null)

const stats = ref({
  userCount: 156,
  roleCount: 8,
  permissionCount: 24,
  onlineCount: 12
})

const navigateTo = (path: string) => {
  router.push(path)
}

const fetchUserInfo = async () => {
  const storedUser = getStoredUserInfo()
  if (storedUser) userInfo.value = storedUser

  try {
    const response = await getAuthUserInfo()
    if (response.success) {
      userInfo.value = response.data
    }
  } catch (error) {
    console.warn('用户信息获取失败', error)
  }
}

onMounted(fetchUserInfo)
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
