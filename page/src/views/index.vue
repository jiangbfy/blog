<script setup lang="ts">
import Sakura from '@/components/Sakura.vue'
import Header from '@/components/Header.vue'

</script>

<template>
  <div class="container">
    <Sakura></Sakura>
    <Header></Header>
    <router-view></router-view>
  </div>
</template>

<style lang="scss" scoped>
.container{
  width: 100%; 
  height: 100%;
}
</style>
