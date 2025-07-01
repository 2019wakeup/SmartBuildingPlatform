<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <el-upload
                class="avatar-uploader"
                action="http://localhost:8080/api/system/user/avatar"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :headers="uploadHeaders"
                name="avatarfile"
              >
                <img v-if="user.avatar" :src="user.avatar" class="avatar">
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <el-icon><User /></el-icon>用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Phone /></el-icon>手机号码
                <div class="pull-right">{{ user.phone }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Message /></el-icon>用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <el-form ref="userRef" :model="user" :rules="rules" label-width="80px">
                <el-form-item label="用户昵称" prop="userName">
                  <el-input v-model="user.userName" maxlength="30" />
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="user.phone" maxlength="11" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="user.email" maxlength="50" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submit">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="健康报告" name="healthreport">
              <div v-if="showHealthReportHistory">
                <HealthReportHistory :stu-id="user.user_id || user.userId || 0" />
              </div>
              <div v-else>
                <HealthReportCard 
                  :stu-id="user.user_id || user.userId || 0" 
                  @view-history="showHealthHistory"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { getUserProfile, updateUserProfile, uploadAvatar } from '@/api/user';
import { getToken } from '@/utils/auth';
import type { UploadProps } from 'element-plus'
import HealthReportCard from '@/components/HealthReport/HealthReportCard.vue'
import HealthReportHistory from '@/components/HealthReport/HealthReportHistory.vue'
import axios from 'axios'

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 添加请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  }
)

// 添加响应拦截器，直接返回data
axiosInstance.interceptors.response.use(
  (response) => {
    return response.data
  }
)

const user = ref({
  userName: '',
  phone: '',
  email: '',
  avatar: '',
  userId: 0,
  user_id: 0
});
const activeTab = ref('userinfo');
const userRef = ref();
const showHealthReportHistory = ref(false);

const uploadHeaders = {
  Authorization: 'Bearer ' + getToken()
};

const rules = reactive({
  userName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
  email: [
    { required: true, message: "邮箱地址不能为空", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
  ],
  phone: [
    { required: true, message: "手机号码不能为空", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ]
});

const getProfile = async () => {
  try {
    // 直接调用axios获取原始响应
    const response = await axiosInstance.get('/system/user/profile');
    console.log('getProfile raw API response:', response);
    
    if (response.code === 200 && response.user) {
      user.value = response.user;
      console.log('User data set to:', user.value);
      console.log('User ID (user_id):', user.value.user_id);
      
      if (user.value.avatar && !user.value.avatar.startsWith('http')) {
        user.value.avatar = `http://localhost:8080/api${user.value.avatar}`;
      }
    } else {
      console.error('Failed to get user profile:', response);
    }
  } catch (error) {
    console.error('Error getting user profile:', error);
  }
};

const submit = () => {
  userRef.value.validate(async (valid: boolean) => {
    if (valid) {
      await updateUserProfile(user.value);
      ElMessage.success('修改成功');
      getProfile();
    }
  });
};

const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  if (response.code === 200) {
    user.value.avatar = `http://localhost:8080/api${response.imgUrl}`;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(response.msg || '上传失败');
  }
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type.indexOf("image") == -1) {
    ElMessage.error('Avatar picture must be image format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}

const showHealthHistory = () => {
  showHealthReportHistory.value = true
}

onMounted(() => {
  getProfile();
});
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
.list-group {
  padding-left: 0;
  list-style: none;
}
.list-group-item {
  border-bottom: 1px solid #e7eaec;
  padding: 10px 0;
  font-size: 14px;
}
.pull-right {
  float: right !important;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    text-align: center;
}
.text-center {
  text-align: center;
  padding-bottom: 20px;
}
</style> 