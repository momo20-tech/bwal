import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from '../views/Login.vue'
import User from '../views/User.vue'
// 导入请假审批组件（如果文件已创建，直接导入；也可以用懒加载方式）
import LeaveApproval from '../views/LeaveApproval.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    redirect: '/login',
    component: HomeView,
    children:[
      {
        path: 'user', // 子路由的 path 通常不需要以 / 开头
        name:"User",
        component:User
      },
      {
        path: 'home',
        name: 'Home',
        component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue')
      },
      // 新增请假审批子路由（和user同级，属于HomeView的子路由）
      {
        path: 'leave-approval',
        name: 'LeaveApproval',
        // 两种导入方式选一种即可：
        // 方式1：静态导入（已在顶部import）
        component: LeaveApproval,
        // 方式2：懒加载（推荐，减少首屏加载体积）
        // component: () => import(/* webpackChunkName: "leave" */ '../views/LeaveApproval.vue')
      }
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component:Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router