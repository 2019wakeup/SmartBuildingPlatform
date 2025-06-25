import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '@/utils/auth'
import { ElMessage } from 'element-plus'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: {
          title: '首页',
          icon: 'HomeFilled'
        }
      },
      {
        path: '/service-hub',
        name: 'ServiceHub',
        component: () => import('@/views/service-hub/index.vue'),
        meta: {
          title: 'Service Hub',
          icon: 'Grid'
        }
      },
      {
        path: '/user',
        name: 'UserManagement',
        component: () => import('@/views/user/index.vue'),
        meta: {
          title: '用户管理',
          icon: 'User'
        }
      },
      {
        path: '/role',
        name: 'RoleManagement',
        component: () => import('@/views/role/index.vue'),
        meta: {
          title: '角色管理',
          icon: 'UserFilled'
        }
      },
      {
        path: '/permission',
        name: 'PermissionManagement',
        component: () => import('@/views/permission/index.vue'),
        meta: {
          title: '权限管理',
          icon: 'Key'
        }
      },
      {
        path: '/iot-dashboard',
        name: 'IoTDashboard',
        component: () => import('@/views/iot-dashboard/index.vue'),
        meta: {
          title: 'IoT数据监控',
          icon: 'Monitor'
        }
      },
      {
        path: '/iot-device',
        name: 'IoTDevice',
        component: () => import('@/views/iot-device/index.vue'),
        meta: {
          title: 'IoT设备管理',
          icon: 'Connection'
        }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: {
          title: '个人中心',
          hidden: true
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - 智能云平台管理系统`
  } else {
    document.title = '智能云平台管理系统'
  }

  // 检查是否需要认证
  if (to.meta?.requiresAuth !== false) {
    // 需要认证的页面
    if (isLoggedIn()) {
      next()
    } else {
      ElMessage.warning('请先登录')
      next('/login')
    }
  } else {
    // 不需要认证的页面（如登录页）
    if (to.path === '/login' && isLoggedIn()) {
      // 已登录用户访问登录页，重定向到首页
      next('/')
    } else {
      next()
    }
  }
})

export default router 