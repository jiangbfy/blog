<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import router from '@/router'
import http from "@/utils/request"
import { useRoute } from 'vue-router'
import { MdEditor, MdPreview, MdCatalog } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const state = reactive({
  theme: 'github' as any,
  text: "",
  id: 'editor',
  title: ''
})
const scrollElement = document.documentElement
const route = useRoute()

onMounted(() => {
  if(route.query.id != undefined) {
    const url = '/api/article/detail?id=' + route.query.id
    http.get(url).then((res: any) => {
      if(res.data.code == 200) {
        state.text = res.data.data.content
        state.title = res.data.data.title
      }
    })
  }
})
</script>
  
<template>
  <div class="container flex-row flex-justify-center flex-align-start">
    <div class="article-left flex-row flex-justify-start flex-align-center">
      <MdCatalog
        :editorId="state.id"
        :scrollElement="scrollElement"
        :theme="state.theme"
        class="md-catalog"
      />
    </div>
    <div class="article-right flex-column flex-justify-center flex-align-center">
      <div class="article-head">{{ state.title }}</div>
      <MdPreview
        :modelValue="state.text"
        :id="state.id"
        :theme="state.theme"
        class="md-preview"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  min-height: 100vh;
  background-color: #c7c9da;
  opacity: 0.8;
  .article-left {
    position: fixed;
    left: 0;
    margin-top: 140px;
    width: 20%;
    padding-left: 60px;
  }
  .article-right {
    margin-top: 140px;
    width: 70%;
    margin-left: 10%;
    .md-preview {
      --md-bk-color: rgb(255, 255, 255, 0.0) !important;
    }
    .article-head {
      font-size: 50px;
      font-family: '楷体';
      font-weight: bold;
    }
  }
}
</style>
