<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";
import {options} from "axios";

export default defineComponent({
  name: "RegisterView",
  data() {
    return{
      user: {},
      options: []
    }
  },
  created() {
    this.getType();
  },
  methods: {

    getType(){
      request.get("/type").then(res => {
        if (res.code === '0'){
          this.options = res.data;
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    register (){
      request.post("/register" , this.user).then(res => {
        if (res.code === '0'){
          this.$message.success("注册成功，请登录");
          this.$router.push("/login")
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    navLogin(){
      this.$router.push("/login")
    },

  }
})
</script>

<template>
  <div>
    <div style="width: 400px; height: 530px; margin: 150px auto; background-color:rgba(107,149,224,0.5); border-radius: 10px">
      <div style="width: 100%; height: 100px; font-size: 30px; line-height: 100px; text-align: center; color: #4a5ed0">欢迎注册</div>
      <div style="margin-top: 25px; text-align: center; height: 320px;">
        <el-form :model="user">
          <el-form-item>
            <el-input v-model="user.name" prefix-icon="el-icon-user" style="width: 80%" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="user.password" show-password prefix-icon="el-icon-lock" style="width: 80%" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="user.age" prefix-icon="el-icon-lock" style="width: 80%" placeholder="请输入年龄"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="user.type" placeholder="请选择专业" style="width: 80%">
              <el-option
                  v-for="item in options"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="user.sex" placeholder="请选择性别" style="width: 80%;">
              <el-option label="男" value="男"></el-option>
              <el-option label="女" value="女"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="user.level" placeholder="请选择你所要创建的权限" style="width: 80%">
              <el-option label="老师" value="teacher"></el-option>
              <el-option label="学生" value="student"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 80%; margin-top: 10px" type="primary" @click="register()">注册</el-button>
          </el-form-item>
          <div style="text-align: center">
            已有账号？<a href="javascript:void(0)" style="text-decoration: none" @click="navLogin">点击登录</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>