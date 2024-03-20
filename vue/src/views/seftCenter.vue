<script>
import {defineComponent} from 'vue'
import {left} from "core-js/internals/array-reduce";
import request from "@/utils/request";

export default defineComponent({
  name: "seftCenter",
  data() {
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      form: {},
      left: 'left',
      reverse: true,
      log12: {},
      activities: [],
    }
  },
  created() {
    this.table();
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    if (this.user.level !== 'admin') {
      this.getLog();
    }
  },
  methods: {
    table(){
      request.put("/selftCenter" , this.user).then(res => {
        if (res.code === '0'){
          this.form = res.data;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    updateSelf(){
      request.post("/updateSelf" , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("修改成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    getLog(){
      request.put("/log?id=" + this.user.id + "&level=" + this.user.level).then(res => {
        if (res.code === '0'){
          this.log12 = res.data;
          console.log(JSON.stringify(res.data))
          this.fen(res.data);
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    fen(log12){
      request.put("/log/fen" , log12).then(res => {
        if (res.code === '0') {
          this.activities = res.data;
          console.log(JSON.stringify(res.data))
        } else {
          this.$message.error(res.msg);
        }
      })
    }

  }
})
</script>

<template>
  <div>
    <el-form :label-position="left" label-width="80px" :model="form" style="margin-bottom: 50px">
      <el-form-item label="名称">
        <el-input v-model="form.name" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input show-password v-model="form.password" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input :disabled="true" v-model="form.sex" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="权限">
        <el-input :disabled="true" v-model="form.level" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="专业" v-if="form.level != 'admin'">
        <el-input :disabled="true" v-model="form.type" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="学号" v-if="form.level == 'student'">
        <el-input :disabled="true" v-model="form.studentId" style="width: 30%"></el-input>
      </el-form-item>
      <el-form-item label="职工号" v-if="form.level == 'teacher'">
        <el-input :disabled="true" v-model="form.teacherId" style="width: 30%"></el-input>
      </el-form-item>
      <el-popconfirm title="确定修改个人信息吗？" @confirm="updateSelf()">
        <el-button slot="reference" type="primary" style="margin-left: 5px">修改</el-button>
      </el-popconfirm>
    </el-form>

    <el-timeline :reverse="reverse">
      <el-timeline-item
          v-for="(activity, index) in activities"
          :key="index"
          :timestamp="activity.time">
        {{activity.content}}
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<style scoped>

</style>