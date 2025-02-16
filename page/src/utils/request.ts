import axios from 'axios'
import { useStore } from '@/store/index'
import { ElMessage } from 'element-plus'
import Config from '@/utils/config'

const store = useStore()
const service = axios.create({
  baseURL: Config.baseURL,
  headers: {
    'Access-Control-Allow-Headers': 'token',
    'token': ''
  }
})

service.interceptors.request.use(
  config => {
    const token = store.getToken
    if (token) {
      config.headers.token = token
    }
    return config
  }
)

service.interceptors.response.use(
  response => {
    return response
  },
  err => {
    if (err.response?.status === 400) {
      ElMessage.error('登录过期，请重新登录')
      store.setToken('')
      store.isLogin = false
      store.user.name = ''
      store.user.headUrl = ''
    }
  }
)

export default {
  get (url: string) {
    return service.get(url)
  },
  download (url: string) {
    return service.get(url, {
      responseType: 'blob'
    })
  },
  post (url: string, data: any) {
    return service.post(url, data)
  },
  upload (url: string, data: any) {
    return service.post(url, data, {
      headers: {
        'Content-Type': 'multipart/form-data;',
      }
    })
  }
}
