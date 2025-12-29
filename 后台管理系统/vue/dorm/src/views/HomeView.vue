<template>
  <div class="home" style="height: 100%;">
    <el-container style="height: 100%;border: 1px solid #eee;">
      <el-aside width="250px">
        <Aside></Aside>
      </el-aside>
      <el-container>
        <el-header style="text-align: right;font-size: 12px;border-bottom: 1px solid red;line-height: 60px;">
         <Header name="个人中心"></Header>
        </el-header>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Aside from '@/components/Aside.vue'
import Header from '@/components/Header.vue'
import axios from 'axios'
export default {
  name: 'HomeView',
  components: {
    Aside,
    Header
  }
}
</script>