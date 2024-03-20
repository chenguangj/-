<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Layout",
  data() {
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
  },
  methods: {
    logout() {
      localStorage.removeItem("user");
      this.$router.push("/login");
    },

    geren(){
      this.$router.push("/seftCenter");
    }
  },

  mounted() {
    window.console.log(this.$router)
  },

  watch:{
    $route(){

    }
  }
})
</script>

<template>
  <div>
    <el-container>
      <el-header style="background-color: #4c535a">
        <img src="@/assets/logo.png" style="width: 40px; position: relative; top: 10px;">
        <span style="font-size: 20px; margin-left: 15px; color: white">软件项目管理实验</span>
        <el-dropdown style="float: right; height: 60px; line-height: 60px">
          <span class="el-dropdown-link" style="color: white; font-size: 17px">{{ user.name }}
            <i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item><div @click="geren()">个人中心</div></el-dropdown-item>
            <el-dropdown-item><div @click="logout()">退出登录</div></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
    </el-container>
    <el-container>
      <el-aside style="overflow: hidden; min-height: 100vh; background-color: #545c64; width: 250px">
        <el-menu :default-active="$route.path" router default-active="1"
                 style="border-right: none !important;"
                 class="el-menu-vertical-demo"
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-home"></i>
              <span>个人信息模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/seftCenter">个人中心</el-menu-item>
              <el-menu-item v-if="user.level !== 'admin'" index="/userNotice">用户公告</el-menu-item>
              <el-menu-item v-if="user.level === 'admin'" index="/notice">公告模块</el-menu-item>
              <el-menu-item v-if="user.level === 'teacher'" index="/courceRe">课程信息</el-menu-item>
              <el-menu-item v-if="user.level === 'student'" index="/schoolTimeable">课程表</el-menu-item>
              <el-menu-item v-if="user.level !== 'admin'" index="/classroomRe">教室信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2"  v-if="user.level === 'admin'">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>用户模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin">管理员信息</el-menu-item>
              <el-menu-item index="/teacher">老师信息</el-menu-item>
              <el-menu-item index="/student">学生信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3" v-if="user.level === 'admin'">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>专业模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/type">专业显示</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>课程模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/courceNotice">课程公告</el-menu-item>
              <el-menu-item index="/cource">课程显示</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>教室模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/classroomNotice">教室公告</el-menu-item>
              <el-menu-item index="/classroom">教室信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="6" v-if="user.level !== 'admin'">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>作业模块</span>
            </template>
            <el-menu-item-group>
              <el-menu-item v-if="user.level === 'student'" index="/homeworkSt">作业信息</el-menu-item>
              <el-menu-item v-if="user.level === 'teacher'" index="/homeworkTe">作业信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>

</style>