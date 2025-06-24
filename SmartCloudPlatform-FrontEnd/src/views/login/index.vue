<template>
  <div class="login-container">
    <div class="login-wrapper">
      <BrandIntro />
      <LoginForm @loginSuccess="handleSuccess" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import BrandIntro from '@/components/Login/BrandIntro.vue'
import LoginForm from '@/components/Login/LoginForm.vue'

const router = useRouter()
const handleSuccess = () => {
  router.push('/')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}
.login-wrapper {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}
</style>
