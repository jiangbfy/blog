<script setup lang="ts">
import Login from '@/components/Login.vue'
import { ref, computed, onMounted, reactive } from 'vue'
import { useStore } from '@/store/index'
import http from "@/utils/request"
import { ElMessage } from 'element-plus'
import Images from '@/utils/images'
import Tag from '@/components/Tag.vue'
import router from '@/router'
import { useRoute } from 'vue-router'

const selection = reactive({
  active: 0
})
const search = reactive({
  active: -1
})
const loginPage = reactive({
  show: false
})
const loginToken = reactive({
  token: ''
})
const store = useStore()
const headImg = reactive({
  value: ''
})
const loginImg = computed(() => {
  return store.isLogin && store.user.headUrl != null && store.user.headUrl != ''
})
const loginText = computed(() => {
  return store.isLogin
})
const loginButtonText = computed(() => {
  return store.user.name.substring(0, 1)
})
const route = useRoute()
const searchText = ref('')

const showImg = () => {
  if(store.isLogin) {
    const url = store.user.headUrl
    http.download(url).then((res: any) => {
      let blob = new Blob([res.data], { type: "image/jpeg" })
      headImg.value = URL.createObjectURL(blob)
    })
  }
}
onMounted(() => {
  if (route.path == '/home') selection.active = 0
  if (route.path == '/blog') selection.active = 1
  if (route.path == '/tools' || route.path == '/edit') selection.active = 2
  if(store.token !== '') {
    loginToken.token = store.token
    http.post("/api/login/login", loginToken).then((res: any) => {
      if(res.data.code == 200) {
        ElMessage.success('登录成功')
        store.isLogin = true
        store.user.name = res.data.data.name
        store.user.headUrl = res.data.data.headUrl
      } else {
        ElMessage.error('登录失败')
        store.setToken('')
        store.isLogin = false
        store.user.name = ''
        store.user.headUrl = ''
      }
    })
  }
  showImg()
})
const selectMenu = (index: number) => {
  selection.active = index
  if (index == 0) router.push({path: '/home'})
  if (index == 1) router.push({path: '/blog'})
  if (index == 2) router.push({path: '/tools'})
}
const searchFun = () => {
  if(searchText.value == "") {
    ElMessage.error("搜索栏不能为空")
    return
  }
  store.search = searchText.value
  store.searchHistory.push(searchText.value)
  const size = store.searchHistory.length
  if (size > 10) store.searchHistory.splice(0, 1)
  router.push({path: '/search', query: {search: searchText.value}})
}
const deleteHistory = () => {
  store.searchHistory = []
}

</script>

<template>
  <div class="app">
    <Login @return="loginPage.show = false" @show="showImg" v-if="loginPage.show == true"></Login>
    <div class="container flex-row flex-justify-center">
      <div class="header flex-row">
        <div class="header-left flex-row flex-justify-start flex-align-center">
          <div class="logo flex-row flex-justify-start flex-align-center">
            <img src="/favicon.png" alt="logo" class="logo-img">
            <span class="logo-text">江云帆</span>
          </div>
          <div class="flex-column flex-justify-center flex-align-center" :class="[selection.active == 0 ? 'menu-active': 'menu']" @click="selectMenu(0)">
            <el-icon size="20"><HomeFilled /></el-icon>
            <div class="menu-text">首页</div>
          </div>
          <div class="flex-column flex-justify-center flex-align-center" :class="[selection.active == 1 ? 'menu-active': 'menu']" @click="selectMenu(1)">
            <el-icon size="20"><MessageBox /></el-icon>
            <div class="menu-text">博客</div>
          </div>
          <div class="flex-column flex-justify-center flex-align-center" :class="[selection.active == 2 ? 'menu-active': 'menu']" @click="selectMenu(2)">
            <el-icon size="20"><Tools /></el-icon>
            <div class="menu-text">工具</div>
          </div>
          <!-- <div class="flex-column flex-justify-center flex-align-center" :class="[selection.active == 3 ? 'menu-active': 'menu']" @click="selectMenu(3)">
            <el-icon size="20"><User /></el-icon>
            <div class="menu-text">关于我</div>
          </div> -->
        </div>
        <div class="header-right flex-row flex-justify-end flex-align-center">
          <div class="search flex-row flex-justify-end flex-align-center" :class="[{'search-a': search.active == 1}, {'search-d': search.active == 0}]">
            <el-icon class="search-icon2"><MessageBox /></el-icon>
            <span class="search-span">|</span>
            <input class="search-input" v-model="searchText" @focus="search.active = 1" @focusout="search.active = 0"></input>
            <img :src="Images.Search" alt="" class="search-icon1" @click="searchFun">
          </div>
          <div class="dropdown-container flex-row flex-justify-end flex-align-start">
            <div class="search-dropdown flex-column flex-justify-start flex-align-center" :class="[{'search-dropdown-a': search.active == 1}, {'search-dropdown-d': search.active == 0}]">
              <div class="dropdown-head flex-row flex-justify-start flex-align-center">
                <div class="dropdown-head-left flex-row flex-justify-start flex-align-center">
                  <img :src="Images.History" alt="" class="dropdown-head-img">
                  <span class="dropdown-head-text">历史搜索</span>
                </div>
                <div class="dropdown-head-right flex-row flex-justify-end flex-align-center">
                  <img :src="Images.Delete" alt="" class="dropdown-head-rimg" @click="deleteHistory">
                </div>
              </div>
              <div class="dropdown-wrap flex-wrap flex-justify-start flex-align-center">
                <Tag :tagtext="item" v-for="(item, index) in store.searchHistory" :key="index"></Tag>
              </div>
            </div>
          </div>
          <div class="head-login flex-row flex-justify-center flex-align-center" @click="loginPage.show = true" v-if="loginImg">
            <img :src="headImg.value" alt="logo" class="head-login-img">
          </div>
          <div class="login flex-row flex-justify-center flex-align-center" @click="loginPage.show = true" v-else-if="loginText">
            <span>{{ loginButtonText }}</span>
          </div>
          <div class="login flex-row flex-justify-center flex-align-center" @click="loginPage.show = true" v-else>
            <span>登录</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.app {
  width: 100%;
  height: 100%;
  .container {
    position: fixed;
    width: 100%;
    z-index: 1;
    .header {
      background-color: rgb(255,255,255,0.6);
      height: 70px;
      width: 80%;
      border-radius: 15px;
      box-shadow: 0px 0px 5px 0px rgba(255, 255, 255, 0.5);
      .header-left {
        height: 100%;
        width: 50%;
        .logo {
          height: 70%;
          margin-left: 15px;
          margin-right: 20px;
          border-radius: 15px;
          transition: background-color 0.5s ease;
          .logo-img {
            max-height: 100%;
            border-radius: 50%;
          }
          .logo-text {
            margin-left: 5px;
            font-family: "楷体";
            font-size: 32px;
            font-weight: bold
          }
        }
        .logo:hover {
          animation: logo-animate 0.5s forwards;
          cursor: pointer;
        }
        @keyframes logo-animate {
          0% { background: linear-gradient(to right, rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          10% { background: linear-gradient(to right, rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          20% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          30% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          40% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          50% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
          60% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,0.4)); }
          70% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1), rgb(255,255,255,1)); }
          80% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1), rgb(255,255,255,1)); }
          90% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,1)); }
          100% { background: linear-gradient(to right, rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4), rgb(255,255,255,0.4)); }
        }
        .menu {
          height: 90%;
          width: 63px;
          border-radius: 50%;
          .menu-text {
            margin-top: 5px;
            font-family: "楷体";
            font-size: 18px;
          }
        }
        .menu-active {
          height: 90%;
          width: 63px;
          border-radius: 50%;
          color: white;
          background-color: #801eff;
          box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.5);
          animation: active-animate 0.5s forwards;
          .menu-text {
            margin-top: 5px;
            font-family: "楷体";
            font-size: 18px;
          }
        }
        @keyframes active-animate {
          0% { transform: translate(0px, 0px); background-color: rgb(255,255,255,0.0); }
          100% { transform: translate(0px, 10px);  background-color: #801eff; }
        }
        .menu:hover {
          color: #801eff;
          cursor: pointer;
          animation: menu-shake 0.5s forwards;
        }
        @keyframes menu-shake {
          0% { transform: translateY(0); }
          25% { transform: translateY(-5px); }
          50% { transform: translateY(0px); }
          75% { transform: translateY(-2px); }
          100% { transform: translateY(0px); }
        }
      }
      .header-right {
        height: 100%;
        width: 50%;
        .dropdown-container {
          height: 100%;
          .search-dropdown {
            position: absolute;
            visibility: hidden;
            margin-top: 70px;
            margin-right: 35px;
            width: 260px;
            border-radius: 20px;
            background-color: rgb(255,255,255,0.8);
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
            .dropdown-head {
              margin-top: 20px;
              width: 100%;
              .dropdown-head-left {
                width: 50%;
                .dropdown-head-img {
                  max-width: 20px;
                  max-height: 20px;
                  margin-left: 20px;
                }
                .dropdown-head-text {
                  margin-left: 10px;
                  font-size: 20px;
                  font-family: '楷体';
                  font-weight: bold;
                }
              }
              .dropdown-head-right {
                width: 50%;
                .dropdown-head-rimg {
                  cursor: pointer;
                  max-width: 20px;
                  max-height: 20px;
                  margin-right: 20px;
                }
              }
            }
            .dropdown-wrap {
              width: 90%;
              margin-top: 20px;
              margin-bottom: 20px;
            }
          }
          .search-dropdown-a {
            animation: search-dropdown-active 0.5s forwards;
          }
          @keyframes search-dropdown-active {
            0% { width: 260px; opacity: 0; visibility: hidden; }
            100% { width: 360px; opacity: 1; visibility: visible; }
          }
          .search-dropdown-d {
            animation: search-dropdown-deactive 0.5s forwards;
          }
          @keyframes search-dropdown-deactive {
            0% { width: 360px; opacity: 1; visibility: visible; }
            100% { width: 260px; opacity: 0; visibility: hidden; }
          }
        }
        .login {
          height: 50px;
          width: 50px;
          margin-right: 15px;
          background-color: #801eff;
          color: white;
          border-radius: 50%;
          font-family: "楷体";
          font-size: 22px;
        }
        .login:hover {
          font-size: 24px;
          cursor: pointer;
          box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
        }
        .head-login {
          max-height: 50px;
          max-width: 50px;
          margin-right: 15px;
          border-radius: 50%;
          .head-login-img {
            max-width: 100%;
            max-height: 100%;
            border-radius: 50%;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
          }
          .head-login-img:hover {
            cursor: pointer;
            max-width: 110%;
            max-height: 110%;
          }
        }
        .search {
          width: 300px;
          height: 70%;
          margin-right: 15px;
          border-radius: 30px;
          border: 2px solid #801eff;
          background-color: rgb(255,255,255,0.4);
          .search-icon1 {
            margin-left: 5px;
            margin-right: 5px;
            padding: 5px;
            font-size: 20px;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            font-weight: bold;
            color: white;
            background-color: #801eff;
          }
          .search-icon1:hover {
            cursor: pointer;
          }
          .search-input {
            width: calc(100% - 110px);
            background-color: rgb(255,255,255,0.0);
            border: none;
            outline: none;
            font-size: 20px;
            color: #801eff;
          }
          .search-span {
            font-size: 20px;
            margin-right: 10px;
            margin-left: 10px;
          }
          .search-icon2 {
            font-size: 22px;
            font-weight: bold;
          }
        }
        .search:hover {
            background-color: rgb(255,255,255,0.6);
        }
        .search-a {
          animation: search-active 0.5s forwards;
        }
        @keyframes search-active {
          0% { width: 300px; }
          100% { width: 400px; }
        }
        .search-d {
          animation: search-deactive 0.5s forwards;
        }
        @keyframes search-deactive {
          0% { width: 400px; }
          100% { width: 300px; }
        }
      }
    }
  }
}
</style>
