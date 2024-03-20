<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "notice",
  data() {
    return {
      name: '',
      tableData: [{
        name: '用户信息模块',
      }, {
        name: '教室模块',
      }, {
        name: '课程模块',
      }],
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
    }
  },

  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
  },

  methods: {
    bianji(name){
      this.dialogFormVisible = true;
      request.get("/notice?name=" + name).then(res => {
        if (res.code === '0') {
          this.form = res.data;
        } else {
          this.$message.error(res.code);
        }
      })
    },

    update(){
      request.put("/notice" , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("编辑成功");
          this.dialogFormVisible = false;
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
    <div>
      <el-table
          :data="tableData"
          style="width: 100%">
        <el-table-column prop="name" label="什么模块"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button slot="reference" type="primary" @click="bianji(scope.row.name)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div>
      <el-dialog title="请填写信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form">
          <el-form-item label="标题" label-width="15%">
            <el-input v-model="form.title" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="内容" label-width="15%">
            <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="form.content" style="width: 90%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="update()">保 存</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<style scoped>

</style>