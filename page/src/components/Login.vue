<script setup lang="ts">
import { reactive, onMounted, computed } from 'vue'
import http from "@/utils/request"
import { useStore } from '@/store/index'
import { ElMessage } from 'element-plus'

const loginInfo = reactive({
  name: '',
  password: ''
})
const loginConfirm = reactive({
  name: '',
  password: '',
  rePassword: ''
})
const store = useStore()
const emits = defineEmits(['return', 'show'])
const loginActive = reactive({
  active: [false, false, false, false]
})
const loginState = (index: number, state: boolean) => {
  loginActive.active[index] = state
}
const fadeOut = reactive({
  active: false
})
const loginButtonText = computed(() => {
  return store.user.name.substring(0, 1)
})
const headUrl = store.user.headUrl
const headImg = reactive({
  value: ''
})
const isLogin = store.isLogin
const loginText = computed(() => {
  return store.isLogin && store.user.headUrl == null || store.user.headUrl == ''
})

onMounted(() => {
  if(headUrl != '') {
    const url = store.user.headUrl
    http.download(url).then((res: any) => {
      let blob = new Blob([res.data], { type: "image/jpeg" })
      headImg.value = URL.createObjectURL(blob)
    })
  }
  loginConfirm.name = store.user.name
})

const Login = () => {
  if (loginInfo.name == '') {
    ElMessage.error('用户名不能为空')
    return
  }
  if (loginInfo.password == '') {
    ElMessage.error('密码不能为空')
    return
  }
  http.post("/api/login/login", loginInfo).then((res: any) => {
    console.log(res)
    if(res.data.code == 200) {
      ElMessage.success('登录成功')
      store.setToken(res.data.data.token)
      store.isLogin = true
      store.user.name = res.data.data.name
      store.user.headUrl = res.data.data.headUrl
      closePage()
      emits('show')
    } else {
      ElMessage.error('登录失败')
      store.setToken('')
      store.isLogin = false
      store.user.name = ''
      store.user.headUrl = ''
    }
  })
}
const logout = () => {
  store.setToken('')
  store.isLogin = false
  store.user.name = ''
  store.user.headUrl = ''
  closePage()
}
const closePage = () => {
  fadeOut.active = true
  setTimeout(() => {
    fadeOut.active = false
    emits('return')
  }, 500);
}
const editUser = () => {
  if(loginConfirm.password == '') {
    ElMessage.error('密码不能为空')
    return
  }
  if(loginConfirm.password != loginConfirm.rePassword) {
    ElMessage.error('密码不一致')
    return
  }

  const data = {
    name: loginConfirm.name,
    password: loginConfirm.password
  }
  http.post("/api/login/password", data).then((res: any) => {
    ElMessage.success('密码修改成功')
    logout()
  })
}
</script>

<template>
  <div class="container flex-row flex-justify-center flex-align-center" :class="{'fade-out': fadeOut.active}">
    <div class="login flex-row" v-if="isLogin == false">
      <div class="login-left flex-column flex-justify-center flex-align-center">
        <div class="login-line flex-row flex-justify-center flex-align-center">
          <el-icon class="login-icon"><Avatar /></el-icon>
          <span class="login-text"> 登录账号</span>
        </div>
        <div class="login-line flex-row flex-justify-center flex-align-center">
          <div class="login-input" :class="[loginActive.active[0] ? 'login-input-a' : 'login-input-d']">
            <el-input v-model="loginInfo.name" prefix-icon="User" class="login-el-input" placeholder="用户名" @focus="loginState(0, true)" @focusout="loginState(0, false)"></el-input>
          </div>
        </div>
        <div class="login-line flex-row flex-justify-center flex-align-center">
          <div class="login-input" :class="[loginActive.active[1] ? 'login-input-a' : 'login-input-d']">
            <el-input v-model="loginInfo.password" prefix-icon="Lock" class="login-el-input" placeholder="密码" show-password @focus="loginState(1, true)" @focusout="loginState(1, false)"></el-input>
          </div>
        </div>
        <div class="login-line flex-row flex-justify-center flex-align-center">
          <div class="login-button flex-row flex-justify-center flex-align-center" @click="Login">
            登录
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-right-container flex-column flex-justify-center flex-align-center">
          <img src="/favicon.png" alt="logo" class="logo-img">
          <div class="login-title">欢迎来到江云帆</div>
          <div class="login-saying">循声莫问路 功到自然成</div>
          <div class="login-return flex-row flex-justify-center flex-align-center" @click="closePage">返回</div>
        </div>
      </div>
    </div>
    <div class="logined flex-column" v-else>
      <div class="logined-head flex-row flex-justify-start flex-align-start">
        <div class="logined-head-left flex-row flex-justify-start flex-align-start">
          <el-icon class="logined-icon"><Avatar /></el-icon>
          <span class="logined-text"> 用户信息</span>
        </div>
        <div class="logined-head-right flex-row flex-justify-end flex-align-start">
          <div class="logined-return flex-column flex-justify-center flex-align-center" @click="closePage">
            <el-icon><Close /></el-icon>
          </div>
        </div>
      </div>
      <div class="logined-body flex-row flex-justify-center flex-align-start">
        <div class="logined-body-left flex-row flex-justify-center flex-align-start">
          <div class="login-head-img flex-row flex-justify-center flex-align-center" v-if="loginText">
            <span>{{ loginButtonText }}</span>
          </div>
          <div class="login-head-img flex-row flex-justify-center flex-align-start" v-else>
            <img :src="headImg.value" alt="logo" class="head-img">
          </div>
        </div>
        <div class="logined-body-right flex-column flex-justify-start flex-align-center">
          <div class="login-body-line flex-column flex-justify-center flex-align-center">
            <div class="login-body-input">
              <el-input v-model="loginConfirm.name" prefix-icon="User" class="login-body-el-input" placeholder="用户名" disabled></el-input>
            </div>
          </div>
          <div class="login-body-line flex-column flex-justify-center flex-align-center">
            <div class="login-body-input" :class="[loginActive.active[2] ? 'login-body-input-a' : 'login-body-input-d']">
              <el-input v-model="loginConfirm.password" prefix-icon="Lock" class="login-body-el-input" placeholder="密码" show-password @focus="loginState(2, true)" @focusout="loginState(2, false)"></el-input>
            </div>
          </div>
          <div class="login-body-line flex-column flex-justify-center flex-align-center">
            <div class="login-body-input" :class="[loginActive.active[3] ? 'login-body-input-a' : 'login-body-input-d']">
              <el-input v-model="loginConfirm.rePassword" prefix-icon="Lock" class="login-body-el-input" placeholder="确认密码" show-password @focus="loginState(3, true)" @focusout="loginState(3, false)"></el-input>
            </div>
          </div>
          <div class="login-body-line flex-row flex-justify-center flex-align-center">
            <div class="login-body-button flex-row flex-justify-center flex-align-center" @click="editUser">
              修改
            </div>
            <div class="login-body-return flex-row flex-justify-center flex-align-center" @click="logout">
              登出
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.container {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: rgb(255,255,255,0.2);
  animation: container-animation 0.5s forwards;
  .logined {
    width: 1000px;
    height: 600px;
    background-color: rgb(255,255,255,0.6);
    border-radius: 30px;
    .logined-body {
      width: 100%;
      margin-top: 30px;
      animation: login-body-load 1s forwards;
      .logined-body-left {
        width: 35%;
        .login-head-img {
          width: 200px;
          height: 200px;
          // transform: translateY(-50px);
          background-color: rgb(127,127,127,1);
          color: white;
          border-radius: 50%;
          font-family: "楷体";
          font-size: 40px;
          .head-img{
            max-width: 100%;
            max-height: 100%;
            border-radius: 50%;
          }
        }
      }
      .logined-body-right {
        width: 65%;
        .login-body-line {
          margin-bottom: 30px;
          .login-body-button {
            height: 50px;
            width: 150px;
            margin-top: 10px;
            margin-right: 20px;
            background-color: #801eff;
            color: white;
            border-radius: 15px;
            font-family: "楷体";
            font-size: 22px;
          }
          .login-body-button:hover {
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
          }
          .login-body-return {
            height: 46px;
            width: 146px;
            margin-top: 10px;
            border: 2px solid #801eff;
            background-color: rgba(255, 255, 255, 0);
            color: #801eff;
            border-radius: 15px;
            font-family: "楷体";
            font-size: 22px;
          }
          .login-body-return:hover {
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
            background-color: rgba(127, 127, 127, 0.1);
          }
          .login-body-input {
            background-color: rgba(255, 255, 255, 0.6);
            border-radius: 25px;
            height: 50px;
            width: 400px;
            .login-body-el-input {
              width: 100%;
              height: 100%;
              --el-input-border-color: rgba(255, 255, 255, 0);
              --el-input-focus-border-color: rgba(255, 255, 255, 0);
              --el-input-hover-border-color: rgba(255, 255, 255, 0);
              :deep(.el-input__wrapper) {
                border-radius: 25px;
                background-color: rgba(255, 255, 255, 0);
                font-size: 20px;
                font-weight: bold;
              }
              :deep(.el-input__inner) {
                color: #801eff;
                font-size: 20px;
                font-weight: bold;
              }
            }
          }
          .login-body-input-a {
            animation: login-body-input-active 0.5s forwards;
          }
          @keyframes login-body-input-active {
            0% { border: 2px solid rgba(255, 255, 255, 0); }
            100% { border: 2px solid #801eff; }
          }
          .login-body-input-d {
            animation: login-body-input-deactive 0.5s forwards;
          }
          @keyframes login-body-input-deactive {
            0% { border: 2px solid #801eff; }
            100% { border: 2px solid rgba(255, 255, 255, 0);}
          }
        }
      }
    }
    @keyframes login-body-load {
      0% { transform: translateY(0); }
      70% { transform: translateY(50px); }
      100% { transform: translateY(0px); }
    }
    .logined-head{
      margin-bottom: 30px;
      margin-top: 30px;
      width: 100%;
      .logined-head-left{
        width: 50%;
        .logined-icon {
          font-size: 32px;
          margin-left: 30px;
          margin-right: 20px;
        }
        .logined-text {
          font-family: "楷体";
          font-size: 32px;
          font-weight: bold;
          cursor: default;
        }
      }
      .logined-head-right{
        width: 50%;
        .logined-return {
          height: 30px;
          width: 30px;
          margin-right: 30px;
          background-color: white;
          color: #801eff;
          font-size: 16px;
          border: 2px solid #801eff;
          border-radius: 8px;
        }
        .logined-return:hover {
          cursor: pointer;
          box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
          animation: logined-return 0.5s forwards;
        }
        @keyframes logined-return {
          0% { background-color: white; color: #801eff; }
          100% { background-color: #801eff; color: white; }
        }
      }
    }
  }
  .login {
    width: 1000px;
    height: 600px;
    .login-left {
      height: 100%;
      width: 61%;
      border-radius: 30px 0px 0px 30px;
      background-color: rgb(255,255,255,0.6);
      box-shadow: 5px 0px 20px 0px rgba(0, 0, 0, 0.4);
      .login-line {
        margin-bottom: 30px;
        .login-icon {
          font-size: 32px;
          margin-right: 20px;
        }
        .login-text {
          font-family: "楷体";
          font-size: 32px;
          font-weight: bold;
          cursor: default;
        }
        .login-button {
          height: 50px;
          width: 150px;
          margin-top: 10px;
          background-color: #801eff;
          color: white;
          border-radius: 15px;
          font-family: "楷体";
          font-size: 22px;
        }
        .login-button:hover {
          font-size: 24px;
          cursor: pointer;
          box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
        }
        .login-input {
          background-color: rgba(255, 255, 255, 0.6);
          border-radius: 25px;
          height: 50px;
          width: 400px;
          .login-el-input {
            width: 100%;
            height: 100%;
            --el-input-border-color: rgba(255, 255, 255, 0);
            --el-input-focus-border-color: rgba(255, 255, 255, 0);
            --el-input-hover-border-color: rgba(255, 255, 255, 0);
            :deep(.el-input__wrapper) {
              border-radius: 25px;
              background-color: rgba(255, 255, 255, 0);
              font-size: 20px;
              font-weight: bold;
            }
            :deep(.el-input__inner) {
              color: #801eff;
              font-size: 20px;
              font-weight: bold;
            }
          }
        }
        .login-input-a {
          animation: login-input-active 0.5s forwards;
        }
        @keyframes login-input-active {
          0% { border: 2px solid rgba(255, 255, 255, 0); }
          100% { border: 2px solid #801eff; }
        }
        .login-input-d {
          animation: login-input-deactive 0.5s forwards;
        }
        @keyframes login-input-deactive {
          0% { border: 2px solid #801eff; }
          100% { border: 2px solid rgba(255, 255, 255, 0);}
        }
      }
    }
    .login-right {
      height: 100%;
      width: 41%;
      border-radius: 0px 30px 30px 0px;
      background-color: rgb(255,255,255,0.6);
      box-shadow: -5px 0px 20px 0px rgba(0, 0, 0, 0.4);
      .login-right-container {
        width: 100%;
        height: 100%;
        animation: login-right-load 1s forwards;
        .logo-img {
          max-height: 160px;
          max-width: 160px;
          border-radius: 50%;
          margin-bottom: 30px;
        }
        .login-title {
          font-family: "楷体";
          font-size: 40px;
          font-weight: bold;
          cursor: default;
        }
        .login-saying {
          margin-top: 20px;
          font-family: "楷体";
          font-size: 20px;
          font-weight: bold;
          cursor: default;
        }
        .login-return {
          height: 40px;
          width: 140px;
          margin-top: 20px;
          border: 2px solid #801eff;
          background-color: rgba(255, 255, 255, 0);
          color: #801eff;
          border-radius: 15px;
          font-family: "楷体";
          font-size: 22px;
        }
        .login-return:hover {
          font-size: 24px;
          cursor: pointer;
          box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
          background-color: rgba(127, 127, 127, 0.1);
        }
      }
      @keyframes login-right-load {
        0% { transform: translateX(0); }
        70% { transform: translateX(50px); }
        100% { transform: translateY(0px); }
      }
    }
  }
}
@keyframes container-animation {
  0% { opacity: 0; }
  100% { opacity: 100; }
}
.fade-out {
  animation: fade-out-animation 0.5s forwards;
}
@keyframes fade-out-animation {
  0% { opacity: 100; }
  100% { opacity: 0; }
}
</style>