<script setup lang="ts">
import Images from '@/utils/images'
import ToolTip from '@/components/ToolTip.vue'
import router from '@/router'

defineProps<{
  blogtags: string[],
  blogtitle: string,
  blogtip: string,
  blogroute: string
}>()

</script>

<template>
  <div class="card-container flex-column flex-justify-center flex-align-center" @click="router.push({path: blogroute})">
    <div class="card flex-column flex-justify-start flex-align-center">
      <div class="card-pic flex-row flex-justify-center flex-align-center">
        <img :src="Images.ChuYin" alt="" class="card-pic-img">
      </div>
      <div class="card-line flex-row flex-justify-center flex-align-center">
        <div class="card-line-k flex-row flex-justify-center flex-align-center">T</div>
        <div class="card-line-text flex-row flex-justify-center flex-align-center"><ToolTip :text="blogtitle" :tip="blogtip"></ToolTip></div>
      </div>
      <div class="card-line flex-row flex-justify-center flex-align-center">
        <div class="card-icon flex-row flex-justify-center flex-align-center">
          <img :src="Images.Tag" alt="" class="card-icon-img">
        </div>
        <div class="card-tag flex-row flex-justify-center flex-align-center" v-for="(item, index) in blogtags" :key="index">
          {{ item }}
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.card-container {
  width: 500px;
  height: 300px;
  margin-left: 20px;
  .card {
    width: 480px;
    height: 280px;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.7);
    border-radius: 30px;
    animation: an-card 0.1s forwards;
    .card-line {
      width: 100%;
      margin-top: 20px;
      .card-line-k {
        width: 30px;
        height: 30px;
        border-radius: 5px;
        font-size: 20px;
        font-weight: 200;
        font-family: 'Times New Roman', Times, serif;
        background-color: #801eff;
        color: white;
      }
      .card-line-text {
        margin-left: 10px;
        font-size: 24px;
        font-family: '楷体';
      }
      .card-icon {
        width: 26px;
        height: 26px;
        .card-icon-img {
          min-width: 100%;
          min-height: 100%;
        }
      }
      .card-tag {
        height: 32px;
        margin-left: 10px;
        border-radius: 10px;
        padding-left: 10px;
        padding-right: 10px;
        background-color: #801eff;
        color: white;
        font-size: 18px;
        font-family: '楷体';
      }
    }
    .card-pic {
      width: 100%;
      height: 55%;
      overflow: hidden;
      border-radius: 30px 30px 0 0;
      .card-pic-img {
        min-width: 100%;
        min-height: 100%;
      }
    }
  }
  @keyframes an-card {
    0% { width: 490px; height: 290px; }
    100% { width: 480px; height: 280px; }
  }
  .card:hover {
    cursor: pointer;
    animation: an-card-hover 0.1s forwards;
  }
  @keyframes an-card-hover {
    0% { width: 480px; height: 280px; }
    100% { width: 490px; height: 290px; }
  }
}
</style>
