import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import Dashboard from '@/views/Dashboard.vue'
import User from '@/views/system/User.vue'
import Warehouse from '@/views/warehouse/Warehouse.vue'
import Category from '@/views/product/Category.vue'
import Product from '@/views/product/Product.vue'
import Stock from '@/views/stock/Stock.vue'
import Inbound from '@/views/order/Inbound.vue'
import Outbound from '@/views/order/Outbound.vue'
import Supplier from '@/views/partner/Supplier.vue'
import Customer from '@/views/partner/Customer.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { title: '首页' }
      },
      {
        path: 'user',
        name: 'User',
        component: User,
        meta: { title: '用户管理' }
      },
      {
        path: 'warehouse',
        name: 'Warehouse',
        component: Warehouse,
        meta: { title: '仓库管理' }
      },
      {
        path: 'category',
        name: 'Category',
        component: Category,
        meta: { title: '分类管理' }
      },
      {
        path: 'product',
        name: 'Product',
        component: Product,
        meta: { title: '商品管理' }
      },
      {
        path: 'stock',
        name: 'Stock',
        component: Stock,
        meta: { title: '库存管理' }
      },
      {
        path: 'inbound',
        name: 'Inbound',
        component: Inbound,
        meta: { title: '入库管理' }
      },
      {
        path: 'outbound',
        name: 'Outbound',
        component: Outbound,
        meta: { title: '出库管理' }
      },
      {
        path: 'supplier',
        name: 'Supplier',
        component: Supplier,
        meta: { title: '供应商管理' }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: Customer,
        meta: { title: '客户管理' }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('user')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
