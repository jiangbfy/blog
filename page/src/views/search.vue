<script setup lang="ts">
import { useStore } from '@/store/index'
import { ref, reactive, computed, onMounted } from 'vue'
import BlogCard from '@/components/BlogCard.vue'
import http from "@/utils/request"

const store = useStore()
const state = reactive({
  totalPage: 1,
  currentPage: 1,
  middleIndex: [1],
  startIndex: [] as number[]
})
const blogSummary = reactive({
  list: [] as any,
  total: 0,
  page: 0,
  pageSize: 0
})
const identorState = computed(() => {
  if(state.totalPage < 6) {
    state.startIndex = []
    for(let i = 1;i <= state.totalPage;i++) {
      state.startIndex.push(i)
    }
    return 0
  }
  if(state.currentPage >= 4 && (state.totalPage - state.currentPage) >= 3) {
    state.middleIndex[0] = state.currentPage - 2
    state.middleIndex[1] = state.currentPage - 1
    state.middleIndex[2] = state.currentPage
    state.middleIndex[3] = state.currentPage + 1
    state.middleIndex[4] = state.currentPage + 2
    return 1
  }
  if(state.currentPage < 4) {
    state.middleIndex[0] = 1
    state.middleIndex[1] = 2
    state.middleIndex[2] = 3
    state.middleIndex[3] = 4
    state.middleIndex[4] = 5
    return 2
  }
  if((state.totalPage - state.currentPage) < 3) {
    state.middleIndex[0] = state.totalPage - 4
    state.middleIndex[1] = state.totalPage - 3
    state.middleIndex[2] = state.totalPage - 2
    state.middleIndex[3] = state.totalPage - 1
    state.middleIndex[4] = state.totalPage
    return 3
  }
})
const switchPage = (page: number) => {
  state.currentPage = page
  loadBlogSummary(page, 9)
}
const switchPrev = () => {
  if (state.currentPage > 1) state.currentPage--
  loadBlogSummary(state.currentPage, 9)
}
const switchNext = () => {
  if (state.currentPage < state.totalPage) state.currentPage++
  loadBlogSummary(state.currentPage, 9)
}
const loadBlogSummary = (page: number, pageSize: number) => {
  const param = {
    page: page,
    pageSize: pageSize,
    title: store.search
  }
  http.post('/api/article/search', param).then((res: any) => {
    if(res.data.code == 200) {
      blogSummary.list = res.data.data.list
      blogSummary.total = res.data.data.total
      blogSummary.page = res.data.data.page
      blogSummary.pageSize = res.data.data.pageSize
      if((blogSummary.total % blogSummary.pageSize) > 0) state.totalPage = Math.floor(blogSummary.total / blogSummary.pageSize) + 1
      else state.totalPage = blogSummary.total / blogSummary.pageSize
      state.currentPage = blogSummary.page
    }
  })
}
onMounted(() => {
  loadBlogSummary(1, 9)
})
</script>

<template>
  <div class="container flex-column flex-justify-start flex-align-center">
    <div class="search-head flex-row flex-justify-start flex-align-center">
      <div class="head-k flex-row flex-justify-center flex-align-center">T</div>
      <span class="head-text">搜索内容为</span>
      <div class="head-search flex-row flex-justify-center flex-align-center">{{ store.search }}</div>
      <span class="head-text">的结果({{ blogSummary.total }})</span>
    </div>
    <div class="search-body flex-wrap flex-justify-center flex-align-center">
      <BlogCard :id="item.id" :blogtags="item.tags" :blogtitle="item.title" :blogtip="item.title" :updatetime="item.updateTime" :cover="item.cover" v-for="(item, index) in blogSummary.list" :key="index"></BlogCard>
    </div>
    <div class="blog-identor flex-row flex-justify-center flex-align-center" v-if="identorState == 0">
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchPrev">前一页</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" v-for="(item, index) in state.startIndex" :key="index" :class="{'identor-page-select': item == state.currentPage}" @click="switchPage(item)">{{ item }}</div>
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchNext">后一页</div>
    </div>
    <div class="blog-identor flex-row flex-justify-center flex-align-center" v-if="identorState == 1">
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchPrev">前一页</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" @click="switchPage(1)">1</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center">...</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" v-for="(item, index) in state.middleIndex" :key="index" :class="{'identor-page-select': item == state.currentPage}" @click="switchPage(item)">{{ item }}</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center">...</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" @click="switchPage(state.totalPage)">{{ state.totalPage }}</div>
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchNext">后一页</div>
    </div>
    <div class="blog-identor flex-row flex-justify-center flex-align-center" v-if="identorState == 2">
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchPrev">前一页</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" v-for="(item, index) in state.middleIndex" :key="index" :class="{'identor-page-select': item == state.currentPage}" @click="switchPage(item)">{{ item }}</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center">...</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" @click="switchPage(state.totalPage)">{{ state.totalPage }}</div>
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchNext">后一页</div>
    </div>
    <div class="blog-identor flex-row flex-justify-center flex-align-center" v-if="identorState == 3">
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchPrev">前一页</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" @click="switchPage(1)">1</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center">...</div>
      <div class="identor-page flex-row flex-justify-center flex-align-center" v-for="(item, index) in state.middleIndex" :key="index" :class="{'identor-page-select': item == state.currentPage}" @click="switchPage(item)">{{ item }}</div>
      <div class="identor-button flex-row flex-justify-center flex-align-center" @click="switchNext">后一页</div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.container {
  width: 100%;
  min-height: 100vh;
  height: 100%;
  background-color: #c7c9da;
  opacity: 0.8;
  .search-head {
    width: 80%;
    margin-top: 140px;
    .head-k {
      padding: 10px;
      height: 20px;
      border-radius: 10px;
      font-size: 20px;
      font-weight: 200;
      font-family: 'Times New Roman', Times, serif;
      background-color: #801eff;
      color: white;
    }
    .head-text {
      margin-left: 10px;
      font-size: 26px;
      font-family: '楷体';
    }
    .head-search {
      margin-left: 10px;
      padding: 10px;
      height: 20px;
      border-radius: 10px;
      font-size: 20px;
      font-weight: 200;
      font-family: '楷体';
      background-color: #dc7c06;
      color: white;
    }
  }
  .search-body {
    width: 90%;
    margin-top: 50px;
  }
  .blog-identor {
    width: 90%;
    .identor-button {
      font-size: 20px;
      font-family: '楷体';
      cursor: pointer;
      border: 2px solid #801eff;
      color: #801eff;
      border-radius: 15px;
      padding: 10px;
      margin: 10px;
    }
    .identor-button:hover {
      background-color: rgb(0, 0, 0, 0.1);
    }
    .identor-page {
      font-size: 20px;
      font-family: '楷体';
      cursor: pointer;
      border-radius: 10px;
      margin: 10px;
      width: 40px;
      height: 40px;
      color: #801eff;
    }
    .identor-page:hover {
      background-color: #79bbff;
      color: white;
    }
    .identor-page-select {
      background-color: #801eff;
      color: white;
    }
  }
}
</style>
