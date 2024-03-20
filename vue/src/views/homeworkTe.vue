<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "homeworkTe",
  data() {
    return {
      name: '',
      tableData: [],
      total: 0,
      form: {},
      pageNum: 1,
      pageSize: 5,
      homeSt: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
      dialogFormVisible2: false,
      score: ''
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
      request.put("/cource/getReSt?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize , this.user).then(res => {
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

    homework(cource){
      if (cource.img == null){
        this.$message.error("你还没有设置作业，不能查看作业相关内容");
        return;
      }
      this.dialogFormVisible = true;
      this.getHomeSt(cource);
    },

    successUpload(res){
      console.log(res);
      this.form.img = res.data;
    },

    getHomeSt(cource){
      request.put("/cource/getHomeSt" , cource).then(res => {
        if (res.code === '0'){
          this.homeSt = res.data;
          console.log(JSON.stringify (res.data));
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    getScore(score){
      this.dialogFormVisible2 = true;
      this.form = score;
      console.log(JSON.stringify (this.form));
    },

    insert(){
      request.put("/cource/getScore?score=" + this.score , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("分数上传成功");
          this.dialogFormVisible2 = false;
          this.score = '';
        } else {
          this.$message.error(res.msg);
        }
      })
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
        <el-table-column prop="name" label="课程名称"></el-table-column>
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
            <el-button type="primary" @click="homework(scope.row)">查看作业</el-button>
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
      <el-dialog title="作业相关信息" :visible.sync="dialogFormVisible" width="30%">
        <el-table :data="homeSt" style="width: 100%" ref="table">
          <el-table-column prop="name" label="学生"></el-table-column>
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
              <el-button type="primary" @click="getScore(scope.row)">打分</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
        </div>
      </el-dialog>
    </div>

    <div>
      <el-dialog title="作业相关信息" :visible.sync="dialogFormVisible2" width="20%">
        <el-form :model="form">
          <el-form-item label="分数" label-width="15%">
            <el-input v-model="score" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="insert()">确 定</el-button>
        </div>
      </el-dialog>
    </div>

  </div>
</template>

<style scoped>

</style>