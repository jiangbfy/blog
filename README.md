## 一、前言
&emsp;&emsp;本博客网站是前后端分离项目，前端是vue3，后端是springboot，是本人学习完Web开发后的练手项目。<br>
&emsp;&emsp;开源地址为：https://github.com/jiangbfy/blog。<br>
&emsp;&emsp;线上预览地址为：https://jiangbfy.com。
## 二、后端
- 新建数据库`blog`，运行`blog.sql`文件
- 用`Idea`打开`pom.xml`，用`Maven`导入依赖包
- 打开`src/main/resources/application.yml`文件，修改`mysql`配置
- 点击运行
## 三、前端
- 打开文件`src/utils/config.ts`，修改`baseURL`为`http://localhost:8000`
- 打开终端，执行以下命令
```
npm install
npm run dev
```
## 四、页面展示
- 博客列表页面
<div style="text-align: center;"><img src="https://jiangbfy.com/static/image/2025-2/1739695887591.blog.jpg"/></div>

- 博客详情页面
<div style="text-align: center;"><img src="https://jiangbfy.com/static/image/2025-2/1739695942766.detail.jpg"/></div>

- 博客编辑界面
<div style="text-align: center;"><img src="https://jiangbfy.com/static/image/2025-2/1739696056554.edit.jpg"/></div>

- 工具栏从左到右功能为：
  - 撤销，取消撤销，加粗，下划线，斜体，删除线
  - 标题，下标，上标，引用，无序列表，有序列表，任务列表，行内代码，块级代码
  - 添加链接，添加图片，添加表格，流程图，公式
  - 导出pdf
  - 目录，预览，仅预览，预览代码，保存