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
      title: 'Login',
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
          title: 'Home',
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
          title: 'User Management',
          icon: 'User'
        }
      },
      {
        path: '/role',
        name: 'RoleManagement',
        component: () => import('@/views/role/index.vue'),
        meta: {
          title: 'Role Management',
          icon: 'UserFilled'
        }
      },
      {
        path: '/permission',
        name: 'PermissionManagement',
        component: () => import('@/views/permission/index.vue'),
        meta: {
          title: 'Permission Management',
          icon: 'Key'
        }
      },
      {
        path: '/iot-dashboard',
        name: 'IoTDashboard',
        component: () => import('@/views/iot-dashboard/index.vue'),
        meta: {
          title: 'IoT Data Monitoring',
          icon: 'Monitor'
        }
      },
      {
        path: '/iot-device',
        name: 'IoTDevice',
        component: () => import('@/views/iot-device/index.vue'),
        meta: {
          title: 'IoT Device Management',
          icon: 'Connection'
        }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: {
          title: 'Profile',
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
  // Set page title
  if (to.meta?.title) {
    document.title = `${to.meta.title} - Smart Cloud Platform Management System`
  } else {
    document.title = 'Smart Cloud Platform Management System'
  }

  // Check if authentication is required
  if (to.meta?.requiresAuth !== false) {
    // Pages that require authentication
    if (isLoggedIn()) {
      next()
    } else {
      ElMessage.warning('Please login first')
      next('/login')
    }
  } else {
    // Pages that don't require authentication (like login page)
    if (to.path === '/login' && isLoggedIn()) {
      // Redirect logged-in users from login page to home
      next('/')
    } else {
      next()
    }
  }
})

export default router 