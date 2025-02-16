import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from '@/store/index'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/index',
    name: 'index',
    component: () => import('@/views/index.vue'),
    children: [
      {
        path: '/home',
        name: '主页',
        meta: { title: '' },
        component: () => import('@/views/home.vue'),
      },
      {
        path: '/blog',
        name: '博客',
        meta: { title: '博客' },
        component: () => import('@/views/blog.vue'),
      },
      {
        path: '/search',
        name: '搜索',
        meta: { title: '搜索' },
        component: () => import('@/views/search.vue'),
      },
      {
        path: '/tools',
        name: '工具',
        meta: { title: '工具' },
        component: () => import('@/views/tools.vue'),
      },
      {
        path: '/article',
        name: '文章',
        meta: { title: '文章' },
        component: () => import('@/views/article.vue'),
      },
      {
        path: '/edit',
        name: '编辑',
        meta: { title: '编辑' },
        component: () => import('@/views/edit.vue'),
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  // const store = useStore()
  // if(store.token == '' && to.path != '/login') {
  //   next({
  //     path: '/login',
  //     query: {redirect: to.fullPath}
  //   })
  // }
  // else {
  //   next()
  // }
  next()
})
 
router.afterEach((to, from) => {
    const title = to.meta.title as string
    if (title) {
        window.document.title = title + '|江云帆的博客'
    }
})

export default router
