<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { useStore } from '@/store/index'

const props = defineProps<{
  tagtext: string
}>()

const store = useStore()
import router from '@/router'

const searchFun = () => {
  store.search = props.tagtext
  router.push({path: '/search', query: {search: props.tagtext}})
}
</script>

<template>
  <div class="tag flex-row flex-justify-center flex-align-center" @click="searchFun">
    <span class="tag-text">{{ tagtext }}</span>
  </div>
</template>

<style lang="scss" scoped>
.tag {
  padding: 8px;
  height: 20px;
  background-color: #801eff;
  margin-left: 10px;
  margin-top: 10px;
  border-radius: 10px;
  .tag-text {
    color: white;
    font-size: 18px;
    font-family: '楷体';
  }
}
.tag:hover {
  cursor: pointer;
}
</style>
