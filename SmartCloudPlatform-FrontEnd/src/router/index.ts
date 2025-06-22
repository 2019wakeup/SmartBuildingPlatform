import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/user',
    children: [
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
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 