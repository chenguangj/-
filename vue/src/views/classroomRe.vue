<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "classroomRe",
  data() {
    return {
      name: '',
      tableData: [],
      total: 0,
      form: {},
      pageNum: 1,
      pageSize: 5,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },

  created() {
    this.table();
    this.pageNum = 1;
    this.pageSize = 5;
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
  },

  methods: {
    table() {
      if (this.user.studentId != null) {
        request.put("/classroom/getReSt?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize , this.user).then(res => {
          if (res.code === '0'){
            this.tableData = res.data.list;
            this.total = res.data.total;
          } else {
            this.$message.error(res.msg);
          }
        })
      } else {
        request.put("/classroom/getReTe?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize , this.user).then(res => {
          if (res.code === '0'){
            this.tableData = res.data.list;
            this.total = res.data.total;
          } else {
            this.$message.error(res.msg);
          }
        })
      }
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize;
      this.table();
    },

    handleCurrentChange(pageNum){
      this.pageNum = pageNum;
      this.table();
    },


    reset(){
      this.name = '';
      this.table();
    },

    tuiding(id){
      if(this.user.studentId != null){
        request.put("/classroom/tuidingSt?id=" + id , this.user).then(res => {
          if (res.code === '0'){
            this.$message.success("退订成功");
            localStorage.setItem("user" , JSON.stringify(res.data));
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      } else if (this.user.teacherId != null) {
        request.put("/classroom/tuidingTe?id=" + id , this.user).then(res => {
          if (res.code === '0'){
            this.$message.success("退订成功");
            localStorage.setItem("user" , JSON.stringify(res.data));
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      }
    },



  }
})
</script>

<template>
  <div>
    <div style="margin-bottom: 15px">
      <el-input v-model="name" style="width: 210px" placeholder="名称"></el-input>
      <el-button type="warning" style="margin-left: 10px" @click="table()">查询</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="reset()">重置</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" ref="table">
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="capacity" label="容量"></el-table-column>
        <el-table-column prop="initTime" label="开始时间"></el-table-column>
        <el-table-column prop="destroyTime" label="结束时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-popconfirm title="确定退订吗？" @confirm="tuiding(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px">退订</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 10px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>

</style>