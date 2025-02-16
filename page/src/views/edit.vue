<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { MdEditor, ModalToolbar, NormalFooterToolbar, NormalToolbar, Themes, ToolbarNames } from 'md-editor-v3'
import { ExportPDF } from '@vavt/v3-extension'
import { ElMessage } from 'element-plus'
import http from "@/utils/request"
import Config from '@/utils/config'
import { useStore } from '@/store/index'
import { useRoute } from 'vue-router'
import 'md-editor-v3/lib/style.css'
import '@vavt/v3-extension/lib/asset/ExportPDF.css'
import router from '@/router'

const route = useRoute()
const state = reactive({
  theme: 'github' as any,
  content: "",
  id: 'editor',
  status: false
})
const store = useStore()
const showAdd = ref(false)
const tagText = ref('')
const selectOptions = reactive({
  value: ['web', 'nginx', '流媒体']
})
const toolbars = ref<ToolbarNames[]>([
  'revoke', 'next', '-', 'bold', 'underline', 'italic', 'strikeThrough', '-', 'title', 'sub', 
  'sup', 'quote', 'unorderedList', 'orderedList', 'task', 'codeRow', 'code', '-', 'link', 
  'image', 'table', 'mermaid', 'katex', '-', 0, 1, 2, '=', 
  'catalog', 'preview', 'previewOnly', 'htmlPreview', 'save', 
  // 'prettier', 'pageFullscreen', 'github'
])

const onSave = (value: string) => {
  store.articleEdit.content = state.content
}
const tagAdd = () => {
  showAdd.value = false
  if(tagText.value == '') return
  store.articleEdit.tags.push(tagText.value)
  tagText.value = ''
}
const tagRemove = (index: number) => {
  store.articleEdit.tags.splice(index, 1)
}
const onUploadImg = async (files: File[], callback: (url: { alt: string; title: string; url: any }[]) => void) => {
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((resolve, reject) => {
        const form = new FormData()
        form.append('file', file)
        http.upload('/api/file/uploadImage', form)
          .then((res) => resolve(res))
          .catch((error) => reject(error))
      })
    })
  )
  callback(
    res.map((item: any) => ({
      url:  Config.baseURL + item.data.url,
      alt: item.data.fileName,
      title: item.data.fileName
    }))
  )
}
const onSuccess = () => {
  ElMessage.success('PDF导出成功！')
}
const customizePdf = (pdfIns: any) => {
  const totalPages = pdfIns.internal.getNumberOfPages()
  const pageWidth = pdfIns.internal.pageSize.getWidth()
  const pageHeight = pdfIns.internal.pageSize.getHeight()

  for (let i = 1; i <= totalPages; i++) {
    pdfIns.setPage(i)
    pdfIns.setFontSize(10)
    pdfIns.text(`${i} / ${totalPages}`, pageWidth / 2, pageHeight - 1, {
      align: 'center'
    })
  }
}
const fileUpload = (options: any) => {
  const url = "/api/file/uploadCover"
  const form = new FormData()
  form.append('file', options.file)
  http.upload(url, form).then((res: any) => {
    store.articleEdit.cover = Config.baseURL + res.data.url
  })
}
onMounted(() => {
  if (route.query.id != undefined && store.isLogin == true) {
    state.status = true
    const url = "/api/article/detail?id=" + route.query.id
    http.get(url).then((res: any) => {
      if(res.data.code == 200) {
        store.articleEdit.content = res.data.data.content
        store.articleEdit.cover = res.data.data.cover
        store.articleEdit.tags = res.data.data.tags
        store.articleEdit.title = res.data.data.title
        store.articleEdit.id = res.data.data.id
        state.content = store.articleEdit.content
      }
    })
  }
  state.content = store.articleEdit.content
})
const articlePublish = () => {
  if (state.status == true) {
    const param = {
      id: store.articleEdit.id,
      content: store.articleEdit.content,
      cover: store.articleEdit.cover,
      tags: store.articleEdit.tags,
      title: store.articleEdit.title
    }
    http.post("/api/article/update", param).then((res: any) => {
      if(res.data.code == 200) {
        store.articleEdit.content = ''
        store.articleEdit.cover = ''
        store.articleEdit.tags = []
        store.articleEdit.title = ''
        store.articleEdit.id = 0
        ElMessage.success('文章更新成功')
      }
    })
  } else {
    const param = {
      content: store.articleEdit.content,
      cover: store.articleEdit.cover,
      tags: store.articleEdit.tags,
      title: store.articleEdit.title
    }
    http.post("/api/article/publish", param).then((res: any) => {
      if(res.data.code == 200) {
        store.articleEdit.content = ''
        store.articleEdit.cover = ''
        store.articleEdit.tags = []
        store.articleEdit.title = ''
        store.articleEdit.id = 0
        ElMessage.success('文章发布成功')
      }
    })
  }
}
</script>
  
<template>
  <div class="container flex-column flex-justify-start flex-align-center">
    <div class="edit-header flex-row flex-justify-center flex-align-center">
      <div class="header-left flex-row flex-justify-start flex-align-center">
        <el-icon class="header-icon"><Edit /></el-icon>
        <span class="header-span">文章编辑</span>
      </div>
      <div class="header-right flex-row flex-justify-end flex-align-center">
        <span class="header-span" @click="articlePublish" v-if="store.isLogin">发布文章</span>
      </div>
    </div>
    <div class="edit-editor">
      <MdEditor v-model="state.content" :theme="state.theme" :toolbars="toolbars" class="md-editor" @on-save="onSave" @on-upload-img="onUploadImg">
        <template #defToolbars>
          <ExportPDF :model-value="state.content" @on-success="onSuccess" :customize="customizePdf" />
        </template>
      </MdEditor>
    </div>
    <div class="edit-tag flex-wrap flex-justify-start flex-align-center" v-if="store.isLogin">
      <div class="tag-explain">文章标签: </div>
      <div class="tag-tag flex-row flex-justify-start flex-align-center" v-for="(item, index) in store.articleEdit.tags" :key="index">
        <span class="tag-item">{{ item }}</span>
        <el-icon class="tag-icon" @click="tagRemove"><CloseBold /></el-icon>
      </div>
      <div class="tag-add" @click="showAdd = true">添加标签</div>
      <div class="pull-container" v-if="showAdd">
        <div class="pull-up flex-row flex-justify-center flex-align-center">
          <!-- <el-select v-model="tagText" class="pull-input" placeholder="选择标签">
            <el-option
              v-for="item in selectOptions.value"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select> -->
          <input type="text" v-model="tagText" class="pull-input" placeholder="添加标签">
          <div class="pull-button" @click="tagAdd">添加</div>
          <div class="pull-button" @click="showAdd = false">关闭</div>
        </div>
      </div>
    </div>
    <div class="edit-cover flex-row flex-justify-start flex-align-start" v-if="store.isLogin">
      <div class="cover-explain">文章封面: </div>
      <el-upload class="cover-upload flex-row flex-justify-center flex-align-center" action="#" :http-request="fileUpload" :show-file-list="false">
        <img v-if="store.articleEdit.cover" :src="store.articleEdit.cover" alt="" class="img-upload flex-row flex-justify-center flex-align-center">
        <el-icon v-else class="img-icon"><Plus /></el-icon>
      </el-upload>
    </div>
    <div class="edit-title flex-row flex-justify-start flex-align-center" v-if="store.isLogin">
      <div class="title-explain">文章标题: </div>
      <input type="text" v-model="store.articleEdit.title" class="title-text">
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
  .edit-header {
    width: 90%;
    margin-top: 140px;
    .header-left {
      width: 50%;
      .header-icon {
        font-size: 40px;
        background-color: #801eff;
        color: white;
        box-shadow: 0px 0px 10px 0px rgba(255, 255, 255);
        border-radius: 10px;
        padding: 5px;
      }
      .header-span {
        margin-left: 20px;
        font-size: 40px;
        font-family: '楷体';
        font-weight: bold;
      }
    }
    .header-right {
      width: 50%;
      .header-span {
        margin-left: 20px;
        font-size: 28px;
        padding: 10px;
        font-family: '楷体';
        font-weight: bold;
        background-color: #801eff;
        color: white;
        cursor: pointer;
        border-radius: 15px;
        padding: 8px;
      }
      .header-span:hover {
        box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
      }
    }
  }
  .edit-editor {
    margin-top: 20px;
    width: 90%;
    height: 100%;
    border-radius: 20px;
    box-shadow: 0px 0px 10px 0px rgba(255, 255, 255, 0.7);
    .md-editor {
      height: 800px;
      border-radius: 20px;
      --md-bk-color: rgb(255, 255, 255, 0.5) !important;
    }
  }
  .edit-tag {
    width: 90%;
    margin-top: 20px;
    .tag-explain {
      font-size: 22px;
      font-family: '楷体';
      font-weight: bold;
    }
    .tag-tag {
      margin-left: 10px;
      font-size: 20px;
      font-family: '楷体';
      border: 1px solid rgb(0, 0, 0, 0.2);
      padding: 2px;
      border-radius: 5px;
      background-color: white;
      .tag-item {
        margin-right: 5px;
      }
      .tag-item:hover {
        cursor: default;
      }
      .tag-icon {
        font-size: 15px;
        border-radius: 50%;
        padding: 2px;
      }
      .tag-icon:hover {
        cursor: pointer;
        background-color: #64aaef;
        color: white;
      }
    }
    .tag-add {
      margin-left: 10px;
      font-size: 20px;
      font-family: '楷体';
      border: 1px solid rgb(0, 0, 0, 0.2);
      padding: 2px;
      border-radius: 5px;
      background-color: white;
    }
    .tag-add:hover {
      cursor: pointer;
      background-color: #64aaef;
      color: white;
    }
    .pull-container {
      width: 100%;
      width: 0;
      .pull-up {
        position: absolute;
        background-color: white;
        transform: translate(-160px, -50px);
        border-radius: 5px;
        .pull-input {
          width: 150px;
          border: none;
          outline: none;
          font-family: '楷体';
          font-size: 20px;
          border-radius: 5px;
        }
        .pull-button {
          color: black;
          font-size: 20px;
          font-family: '楷体';
          margin-right: 5px;
        }
        .pull-button:hover {
          cursor: pointer;
          background-color: #64aaef;
          color: white;
          border-radius: 5px;
        }
      }
      .pull-up::after {
        top: 100%;
        left: 50%;
        margin-left: -5px;
        border-color: #fff transparent transparent transparent;
        content: "";
        position: absolute;
        border-width: 5px;
        border-style: solid;
      }
    }
  }
  .edit-cover {
    width: 90%;
    margin-top: 20px;
    .cover-explain {
      font-size: 22px;
      font-family: '楷体';
      font-weight: bold;
    }
    .cover-upload {
      margin-left: 10px;
      width: 300px;
      height: 200px;
      border: 1px dashed black;
      border-radius: 6px;
      cursor: pointer;
      overflow: hidden;
      background-color: rgb(255, 255, 255, 0);
      .img-upload {
        max-height: 100%;
        max-width: 100%;
      }
      .img-icon {
        width: 100%;
        height: 100%;
        font-size: 28px;
        color: #8c939d;
        text-align: center;
      }
    }
  }
  .edit-title {
    width: 90%;
    margin-top: 20px;
    margin-bottom: 20px;
    .title-explain {
      font-size: 22px;
      font-family: '楷体';
      font-weight: bold;
    }
    .title-text {
      margin-left: 10px;
      height: 32px;
      width: 50%;
      border: none;
      font-size: 22px;
      font-family: '楷体';
      outline: none;
    }
  }
}
</style>