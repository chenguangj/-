<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "userNotice",
  data() {
    return {
      name: '',
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
    }
  },

  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    this.getUserNotice();
  },

  methods: {

    getUserNotice(){
      request.get("/notice/getAllNotice?module=" + "用户信息模块").then(res => {
        if (res.code === '0'){
          this.form = res.data;
        }else {
          this.$message.error(res.msg);
        }
      })
    }

  }
})
</script>

<template>
  <div>
    <h1 style="text-align: center;font-size: 70px">{{form.title}}</h1>
    <div style="text-align: center;word-break: break-all;width: 600px;margin-left: 310px;margin-top: 100px;font-size: 30px;font-weight: bolder">
      {{form.content}}
    </div>
  </div>
</template>

<style scoped>

</style>