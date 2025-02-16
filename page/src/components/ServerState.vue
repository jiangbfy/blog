<script setup lang="ts">
import { useStore } from '@/store/index'
import Images from '@/utils/images'
import { computed, onMounted, reactive } from 'vue'

const tagStyle = reactive({
  text: [' ', 'health', 'running', 'full'],
  color: ['gray', '#6dc45f', '#E6A23C', '#f56c6c']
})
const store = useStore()
const cpuUsed = computed(() => {
  if(store.server.cpuUse == '--') {
    return '--'
  }
  const str = store.server.cpuUse.split('%')[0]
  return str
})
const cpuUsedValue = computed(() => {
  if(cpuUsed.value == '--') {
    return 0.0
  }
  return parseFloat(cpuUsed.value)
})
const cpuUseId = computed(() => {
  if(cpuUsed.value == '--') {
    return 0
  }
  const num = parseFloat(cpuUsed.value)
  if (num < 30) {
    return 1
  }
  else if(num < 70) {
    return 2
  }
  else {
    return 3
  }
})
const memUsed = computed(() => {
  if(store.server.memUse == '--') {
    return '--'
  }
  const str = store.server.memUse.split('%')[0]
  return str
})
const memUsedValue = computed(() => {
  if(memUsed.value == '--') {
    return 0.0
  }
  return parseFloat(memUsed.value)
})
const memUseId = computed(() => {
  if(memUsed.value == '--') {
    return 0
  }
  const num = parseFloat(memUsed.value)
  if (num < 30) {
    return 1
  }
  else if(num < 70) {
    return 2
  }
  else {
    return 3
  }
})
const spaceUsed = computed(() => {
  if(store.server.spaceUse == '--') {
    return '--'
  }
  const str = store.server.spaceUse.split('%')[0]
  return str
})
const spaceUsedValue = computed(() => {
  if(spaceUsed.value == '--') {
    return 0.0
  }
  return parseFloat(spaceUsed.value)
})
const spaceUseId = computed(() => {
  if(spaceUsed.value == '--') {
    return 0
  }
  const num = parseFloat(spaceUsed.value)
  if (num < 30) {
    return 1
  }
  else if(num < 70) {
    return 2
  }
  else {
    return 3
  }
})
const locate = store.weather.city
const tempture = computed(() => {
  return store.weather.temperature_float + '℃ ' + store.weather.weather
})
const wind = computed(() => {
  return store.weather.winddirection + '风 ' + store.weather.windpower + '级'
})
const humidity = computed(() => {
  return store.weather.humidity_float + '%'
})

// onMounted(() => {
//   console.log(store.weather)
//   console.log(store.server)
// })
</script>

<template>
  <div class="state flex-row flex-justify-center flex-align-center">
    <div class="state-server flex-row flex-justify-center flex-align-center">
      <div class="state-server-card flex-column flex-justify-center flex-align-center">
        <el-progress :text-inside="true" :stroke-width="30" :percentage="cpuUsedValue" color="#b352ff"/>
        <div class="server-tag flex-row flex-justify-start flex-align-center">
          <div class="server-tag1">{{ 'CPU ' + cpuUsed + '%' }}</div>
          <div class="server-tag2" :style="{ background: tagStyle.color[cpuUseId] }">{{ tagStyle.text[cpuUseId] }}</div>
        </div>
        <el-progress :text-inside="true" :stroke-width="30" :percentage="memUsedValue" color="#b352ff"/>
        <div class="server-tag flex-row flex-justify-start flex-align-center">
          <div class="server-tag1">{{ 'MEM ' + memUsed + '%' }}</div>
          <div class="server-tag2" :style="{ background: tagStyle.color[memUseId] }">{{ tagStyle.text[memUseId] }}</div>
        </div>
        <el-progress :text-inside="true" :stroke-width="30" :percentage="spaceUsedValue" color="#b352ff"/>
        <div class="server-tag flex-row flex-justify-start flex-align-center">
          <div class="server-tag1">{{ 'DISK ' + spaceUsed + '%' }}</div>
          <div class="server-tag2" :style="{ background: tagStyle.color[spaceUseId] }">{{ tagStyle.text[spaceUseId] }}</div>
        </div>
      </div>
    </div>
    <div class="state-weather flex-row flex-justify-center flex-align-center">
        <div class="state-weather-card flex-column flex-justify-center flex-align-start">
          <div class="weather-line1 flex-row flex-justify-start flex-align-center">
            <img :src="Images.Locate" alt="" class="locate-img">
            <span class="locate-text">{{ locate }}</span>
          </div>
          <div class="weather-line2 flex-row flex-justify-start flex-align-center">
            <span class="tempture-text">{{ tempture }}</span>
          </div>
          <div class="weather-line3 flex-row flex-justify-start flex-align-center">
            <img :src="Images.Wind" alt="" class="weather-img">
            <span class="weather-text">{{ wind }}</span>
          </div>
          <div class="weather-line3 flex-row flex-justify-start flex-align-center">
            <img :src="Images.Humidity" alt="" class="weather-img">
            <span class="weather-text">{{ humidity }}</span>
          </div>
        </div>
      </div>
  </div>
</template>

<style lang="scss" scoped>
.state {
  width: 100%;
  .state-server {
    width: 700px;
    height: 350px;
    .state-server-card {
      padding-top: 25px;
      width: 680px;
      height: 340px;
      background-color: #d6b7e2;
      box-shadow: 0px 0px 20px 0px rgba(0, 0, 0, 0.5);
      border-radius: 25px;
      animation: an-card 0.1s forwards;
      .el-progress--line {
        margin-bottom: 10px;
        width: 90%;
      }
      .server-tag {
        width: 100%;
        margin-bottom: 25px;
        .server-tag1 {
          background-color: #801eff;
          color: white;
          padding: 8px;
          border-radius: 10px;
          margin-left: 5%;
          font-size: 18px;
          font-family: 'Times New Roman', Times, serif;
        }
        .server-tag2 {
          color: white;
          padding: 8px;
          border-radius: 10px;
          margin-left: 15px;
          font-size: 18px;
          font-family: 'Times New Roman', Times, serif;
        }
      }
    }
    @keyframes an-card {
      0% { width: 690px; height: 350px; }
      100% { width: 680px; height: 340px; }
    }
    .state-server-card:hover {
      cursor: pointer;
      animation: an-server-hover 0.1s forwards;
    }
    @keyframes an-server-hover {
      0% { width: 680px; height: 340px; }
      100% { width: 690px; height: 350px; }
    }
  }
  .state-weather {
    margin-left: 30px;
    width: 520px;
    height: 350px;
    .state-weather-card {
      padding-top: 25px;
      width: 510px;
      height: 340px;
      background: linear-gradient(to bottom, #801fff, #68b0fa);
      box-shadow: 0px 0px 20px 0px rgba(0, 0, 0, 0.5);
      border-radius: 25px;
      animation: an-card1 0.1s forwards;
      .weather-line1 {
        transform: translateY(-50px);
        .locate-img {
          margin-left: 40px;
          max-width: 30px;
          max-height: 30px;
        }
        .locate-text {
          margin-left: 10px;
          color: white;
          font-family: '楷体';
          font-size: 24px;
        }
      }
      .weather-line2 {
        transform: translateY(-30px);
        margin-top: 30px;
        margin-bottom: 10px;
        .tempture-text {
          margin-left: 50px;
          color: white;
          font-family: '楷体';
          font-size: 48px;
        }
      }
      .weather-line3 {
        margin-top: 30px;
        .weather-img {
          margin-left: 50px;
          max-width: 24px;
          max-height: 24px;
        }
        .weather-text {
          margin-left: 10px;
          color: white;
          font-family: '楷体';
          font-size: 20px;
        }
      }
    }
    @keyframes an-card1 {
      0% { width: 520px; height: 350px; }
      100% { width: 510px; height: 340px; }
    }
    .state-weather-card:hover {
      cursor: pointer;
      animation: an-card1-hover 0.1s forwards;
    }
    @keyframes an-card1-hover {
      0% { width: 510px; height: 340px; }
      100% { width: 520px; height: 350px; }
    }
  }
}
</style>
