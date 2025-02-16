<script setup lang="ts">
import BlogCard from '@/components/BlogCard.vue'
import ToolsCard from '@/components/ToolsCard.vue'
import ServerState from '@/components/ServerState.vue'
import { onMounted, reactive, ref } from 'vue'
import { useStore } from '@/store/index'
import http from "@/utils/request"

const headText = reactive({
  value: '',
  sub: 0,
  str: '循声莫问路 功到自然成',
  len: 0
})
const blogSummary = reactive({
  list: [] as any,
  total: 0,
  page: 0,
  pageSize: 0
})
const intervalId = reactive({
  value: 0
})
const showHeadText = () => {
  headText.value += headText.str[headText.sub]
  headText.sub++
  if(headText.sub == headText.str.length) {
    clearInterval(intervalId.value)
  }
}
const store = useStore()

const loadBlogSummary = () => {
  const param = {
    page: 1,
    pageSize: 6
  }
  http.post('/api/article/summary', param).then((res: any) => {
    if(res.data.code == 200) {
      blogSummary.list = res.data.data.list
      blogSummary.total = res.data.data.total
      blogSummary.page = res.data.data.page
      blogSummary.pageSize = res.data.data.pageSize
    }
  })
}

onMounted(() => {
  headText.len = headText.str.length
  intervalId.value = setInterval(showHeadText, 200) as any
  const now = Math.floor(Date.now() / 1000)
  const timeInterval = now - store.lastTime
  if(timeInterval > 7200) {
    store.lastTime = now
    http.get('/api/server/state').then((res: any) => {
      if(res.data.code == 200) {
        store.server.memUse = res.data.data.memUse
        store.server.delay = res.data.data.delay
        store.server.bw = res.data.data.bw
        store.server.spaceUse = res.data.data.spaceUse
        store.server.cpuUse = res.data.data.cpuUse
      } else {
        store.server.memUse = '--'
        store.server.delay = '--'
        store.server.bw = '--'
        store.server.spaceUse = '--'
        store.server.cpuUse = '--'
      }
    })
    http.get('/api/weather/weather').then((res: any) => {
      if(res.data.code == 200) {
        store.weather.city = res.data.data.city
        store.weather.weather = res.data.data.weather
        store.weather.temperature_float = res.data.data.temperature_float
        store.weather.humidity_float = res.data.data.humidity_float
        store.weather.winddirection = res.data.data.winddirection
        store.weather.windpower = res.data.data.windpower
      } else {
        store.weather.city = '--'
        store.weather.weather = '--'
        store.weather.temperature_float = '--'
        store.weather.humidity_float = '--'
        store.weather.winddirection = '--'
        store.weather.windpower = '--'
      }
    })
  }
  loadBlogSummary()
})

</script>

<template>
  <div class="container flex-column flex-justify-center flex-align-center">
    <div class="header flex-row flex-justify-start flex-align-center">
      <span class="header-input">{{ headText.value }}</span>
      <span class="header-blink">|</span>
    </div>
    <div class="main flex-column flex-justify-start flex-align-center">
      <div class="server flex-column flex-justify-center flex-align-center">
        <div class="server-head flex-row flex-justify-start flex-align-center">
          <el-icon class="server-head-icon"><Cpu /></el-icon>
          <span class="server-head-span">Server</span>
        </div>
        <div class="server-body">
          <ServerState></ServerState>
        </div>
      </div>
      <div class="server flex-column flex-justify-center flex-align-center">
        <div class="server-head flex-row flex-justify-start flex-align-center">
          <el-icon class="server-head-icon"><MessageBox /></el-icon>
          <span class="server-head-span">最新博客</span>
        </div>
        <div class="server-body flex-wrap flex-justify-center flex-align-center">
          <BlogCard :id="item.id" :blogtags="item.tags" :blogtitle="item.title" :blogtip="item.title" :updatetime="item.updateTime" :cover="item.cover" v-for="(item, index) in blogSummary.list" :key="index"></BlogCard>
        </div>
      </div>
      <!-- <div class="server flex-column flex-justify-center flex-align-center">
        <div class="server-head flex-row flex-justify-start flex-align-center">
          <el-icon class="server-head-icon"><Tools /></el-icon>
          <span class="server-head-span">最新工具</span>
        </div>
        <div class="server-body flex-wrap flex-justify-center flex-align-center">
          <ToolsCard :blogtags="['工具','Markdown']" blogtitle="Markdown编辑器" blogtip="Markdown编辑器" updatetime="2025/1/1" blogroute="/edit"></ToolsCard>
        </div>
      </div> -->
      <div class="record flex-row flex-justify-center flex-align-center">
        <a href="https://beian.miit.gov.cn/"><span class="record-text">津ICP备2024026493号-1</span></a>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  .header {
    width: 100%;
    height: 60px;
    margin-top: 100px;
    margin-bottom: 20px;
    .header-input  {
      margin-left: 15px;
      font-size: 40px;
      font-family: '楷体';
      color: white;
    }
    .header-blink {
      margin-left: 5px;
      font-size: 60px;
      font-weight: 100;
      color: white;
      animation: an-header-blink 1s infinite;
    }
    @keyframes an-header-blink {
      0% { opacity: 0.0; }
      70% { opacity: 1.0; }
      100% { opacity: 0.0; }
    }
  }
  .main {
    width: 100%;
    min-height: 100vh;
    background-color: #c7c9da;
    box-shadow: 0px 0px 5px 0px rgba(255, 255, 255);
    opacity: 0.8;
    .server {
      width: 100%;
      .server-head {
        width: 90%;
        margin-left: 10%;
        margin-top: 20px;
        margin-bottom: 40px;
        .server-head-icon {
          font-size: 40px;
          background-color: #801eff;
          color: white;
          box-shadow: 0px 0px 5px 0px rgba(0, 0, 0);
          border-radius: 10px;
          padding: 5px;
        }
        .server-head-span {
          margin-left: 20px;
          font-size: 40px;
          font-family: '楷体';
        }
      }
      .server-body {
        width: 90%;
        margin-bottom: 40px;
      }
    }
    .record {
      margin-top: 100px;
      width: 100%;
      .record-text {
        font-size: 20px;
        font-family: '楷体';
        color: gray;
      }
      .record-text:hover {
        color: black;
      }
    }
  }
}
</style>
