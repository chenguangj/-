<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "courceRe",
  data() {
    return {
      name: '',
      tableData: [],
      total: 0,
      form: {},
      pageNum: 1,
      pageSize: 5,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
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
      request.put("/cource/getReTe?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize , this.user).then(res => {
        if (res.code === '0'){
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      })
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
      request.put("/cource/tuidingTe?id=" + id , this.user).then(res => {
        if (res.code === '0'){
          this.$message.success("不教了成功");
          localStorage.setItem("user" , JSON.stringify(res.data));
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    homework(cource){
      this.dialogFormVisible = true;
      this.form = cource;
      console.log(JSON.stringify (this.form));
    },

    successUpload(res){
      console.log(res);
      this.form.img = res.data;
    },

    insert(){
      request.put("/cource/upload" , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("上传作业成功");
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
    <div style="margin-bottom: 15px">
      <el-input v-model="name" style="width: 210px" placeholder="名称"></el-input>
      <el-button type="warning" style="margin-left: 10px" @click="table()">查询</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="reset()">重置</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" ref="table">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="capacity" label="当前学生人数"></el-table-column>
        <el-table-column prop="classroom" label="教学的教室"></el-table-column>
        <el-table-column prop="weekName" label="星期几"></el-table-column>
        <el-table-column prop="weekDay" label="持续几周"></el-table-column>
        <el-table-column prop="date" label="课程一天的时间"></el-table-column>
        <el-table-column label="作业">
          <template v-slot="scope">
            <el-image style="width: 70px; height: 70px; border-radius: 50%"
                      :src="'http://localhost:8888/chen/files/' + scope.row.img"
                      :preview-src-list="['http://localhost:8888/chen/files/' + scope.row.img]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-popconfirm title="确定不教了吗？" @confirm="tuiding(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px">不教了</el-button>
            </el-popconfirm>
            <el-button type="primary" @click="homework(scope.row)">作业</el-button>
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

    <div>
      <el-dialog title="请填写信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form">
          <el-form-item label="作业" label-width="15%">
            <el-upload action="http://localhost:8888/chen/files/upload" :on-success="successUpload">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="insert()">确 定</el-button>
        </div>
      </el-dialog>
    </div>

  </div>
</template>

<style scoped>

</style>