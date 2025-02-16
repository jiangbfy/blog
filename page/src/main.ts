import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import { createPinia } from "pinia"
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import * as Icons from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import './style.css'

const app = createApp(App)

for (const name in Icons) {
    const component = (Icons as any)[name]
    app.component(name, component)
}
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(ElementPlus)
app.use(router)
app.mount('#app')
