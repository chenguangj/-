<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "LoginView",
  data() {
    return{
      user: {},
      key: '',
      captchaUrl: ''
    }
  },
  created() {
    this.clickImg();
  },
  methods: {

    login (){
      request.post("/login?key=" + this.key , this.user).then(res => {
        if (res.code === '0') {
          localStorage.setItem("user" , JSON.stringify(res.data));
          this.$router.push("/seftCenter")
        } else {
          this.$message.error(res.msg);
          this.key = Math.random();
          this.captchaUrl = "http://localhost:8888/chen/captcha?key=" + this.key;
          this.user.verCode = '';
        }
      })
    },

    navLogin(){
      this.$router.push("/register")
    },

    clickImg(){
      this.key = Math.random();
      this.captchaUrl = "http://localhost:8888/chen/captcha?key=" + this.key;
    }

  }
})
</script>

<template>
  <div>
    <div style="width: 400px; height: 450px; margin: 150px auto; background-color:rgba(107,149,224,0.5); border-radius: 10px">
      <div style="width: 100%; height: 100px; font-size: 30px; line-height: 100px; text-align: center; color: #4a5ed0">欢迎登录</div>
      <div style="margin-top: 25px; text-align: center; height: 320px;">
        <el-form :model="user">
          <el-form-item>
            <el-input v-model="user.name" prefix-icon="el-icon-user" style="width: 80%" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="user.password" show-password prefix-icon="el-icon-lock" style="width: 80%" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="user.level" placeholder="请选择权限" style="width: 80%">
              <el-option label="管理员" value="admin"></el-option>
              <el-option label="老师" value="teacher"></el-option>
              <el-option label="学生" value="student"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <div style="display: flex; justify-content: center">
              <el-input v-model="user.verCode" prefix-icon="el-icon-user" style="width: 42%;margin-right: 10px" placeholder="请输入验证码"></el-input>
              <img :src="captchaUrl" @click="clickImg()" width="140px" height="33px">
            </div>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 80%; margin-top: 10px" type="primary" @click="login()">登录</el-button>
          </el-form-item>
          <div style="text-align: center">
            未有账号？<a href="javascript:void(0)" style="text-decoration: none" @click="navLogin">点击注册</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>