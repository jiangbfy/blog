import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      /** @ 符号指向 src 目录 */
      "@": path.resolve(__dirname, 'src')
    }
  },
  plugins: [
    vue()
  ],
  server: {
    host: true,// host: "0.0.0.0"
    port: 2346,
    /** 是否自动打开浏览器 */
    open: false,
    /** 跨域设置允许 */
    cors: true,
    /** 端口被占用时，是否直接退出 */
    strictPort: false
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/scss/index.scss" as *;`
      }
    }
  }
})
