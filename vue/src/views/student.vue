<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "student",
  data() {
    return {
      name: '',
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      multipleSelection: [],
      pageNum: 1,
      pageSize: 5,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible2: false,
      options: []
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
      request.get("/studentInfo?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize).then(res => {
        if (res.code === '0'){
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    edit(student) {
      this.form = student;
      this.dialogFormVisible = true;
      this.getType();
    },
    del(id) {
      request.delete("/studentInfo?id=" + id).then(res => {
        if (res.code === '0'){
          this.$message.success("删除成功");
          this.table();
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
    add() {
      this.form = {};
      this.dialogFormVisible = true;
      this.getType();
    },

    insert(){
      if (this.form.id != null) {
        request.put("/studentInfo" , this.form).then(res => {
          if (res.code === '0'){
            this.dialogFormVisible = false;
            this.$message.success("编辑成功");
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      } else {
        request.post("/studentInfo" , this.form).then(res => {
          if (res.code === '0') {
            this.dialogFormVisible = false;
            this.$message.success("新增成功");
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      }
    },

    quxiao(){
      this.dialogFormVisible = false;
      this.table()
    },

    reset(){
      this.name = '';
      this.table();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    delSelected() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("请先勾选你要删除的项")
        return
      }
      request.put("/studentInfo/delSelected" , this.multipleSelection).then(res => {
        if (res.code === '0'){
          this.$message.success("批量删除成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    getRowKeys(row) {
      return row.id;
    },

    exp(){
      location.href = 'http://localhost:8888/chen/studentInfo/export';
    },

    successUpload(res) {
      if (res.code === '0') {
        this.$message.success("批量导入成功");
        this.table();
      } else {
        this.$message.error(res.msg)
      }
    },

    dialogOpen(student){
      this.dialogFormVisible2 = true;
      this.form = student;
    },

    agree() {
      request.put("/studentInfo/agree" , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("同意当前学生信息成功");
          this.dialogFormVisible2 = false;
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    disagree(student) {
      request.put("/studentInfo/disagree" , student).then(res => {
        if (res.code === '0'){
          this.$message.warning("不同意当前学生信息成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    getType(){
      request.get("/type").then(res => {
        if (res.code === '0'){
          this.options = res.data;
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
      <el-input v-model="name" style="width: 210px" placeholder="姓名"></el-input>
      <el-button type="warning" style="margin-left: 10px" @click="table()">查询</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="reset()">重置</el-button>
      <el-button type="primary" style="margin-left: 10px" @click="add()">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="delSelected()">
        <el-button slot="reference" type="danger" style="margin-left: 5px">批量删除</el-button>
      </el-popconfirm>
      <el-button type="success" style="margin-left: 10px" @click="exp()">导出报表</el-button>
      <el-upload action="http://localhost:8888/chen/studentInfo/upload" style="display: inline-block; margin-left: 10px" :show-file-list="false" :on-success="successUpload">
        <el-button size="small" type="primary">批量导入</el-button>
      </el-upload>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" ref="table" @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column ref="table" type="selection" width="55" align="center" :reserve-selection="true"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="studentId" label="学号"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="type" label="专业"></el-table-column>
        <el-table-column prop="level" label="权限"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)" v-if="scope.row.isTrue === 1">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px" v-if="scope.row.isTrue === 1">删除</el-button>
            </el-popconfirm>
            <el-button v-if="user.level === 'admin' & scope.row.isTrue === 0" type="primary" @click="dialogOpen(scope.row)">同意</el-button>
            <el-button v-if="user.level === 'admin' & scope.row.isTrue === 0" type="danger" @click="disagree(scope.row)">不同意</el-button>
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
          <el-form-item label="姓名" label-width="15%">
            <el-input v-model="form.name" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="学号" label-width="15%">
            <el-input v-model="form.studentId" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="年龄" label-width="15%">
            <el-input v-model="form.age" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="性别" label-width="15%">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
          </el-form-item>
          <el-form-item label="专业" label-width="15%">
            <el-select v-model="form.type" style="width: 80%">
              <el-option
                  v-for="item in options"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="quxiao()">取 消</el-button>
          <el-button type="primary" @click="insert()">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="请给当前同意的学生一个学号" :visible.sync="dialogFormVisible2" width="30%">
        <el-form :model="form">
          <el-form-item label="学号" label-width="15%">
            <el-input v-model="form.studentId" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="agree()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<style scoped>

</style>