import { defineStore } from 'pinia';

export const useStore = defineStore('store', {
  state: () => ({
    token: '',
    isLogin: false,
    user: {
      name: '',
      headUrl: ''
    },
    lastTime: 0,
    server: {
      memUse: '',
      delay: '',
      bw: '',
      spaceUse: '',
      cpuUse: ''
    },
    weather: {
      city: '',
      weather: '',
      temperature_float: '',
      humidity_float: '',
      winddirection: '',
      windpower: '',
    },
    articleEdit: {
      id: 0,
      content: '',
      title: '',
      cover: '',
      tags: [] as string[],
    },
    search: '',
    searchHistory: [] as string[],
  }),
  getters: {
    getToken: (state) => state.token,
  },
  actions: {
    setToken(token: string) {
      this.token = token
    },
  },
  persist: true
});