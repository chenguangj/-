<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "cource",
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
      options: [],
      types: [],
      times: [{
          label: '8:10 - 9:45',
          value: '08-10-00and09-45-00'
        },
        {
          label: '10:15 - 11:50',
          value: '10-15-00and11-50-00'
        },
        {
          label: '14:30 - 16:05',
          value: '14-30-00and16-05-00'
        },
        {
          label: '16:30 - 18:05',
          value: '16-30-00and18-05-00'
        }],
      weeks: [{
        label: '星期一',
        value: 1
      },
        {
          label: '星期二',
          value: 2
        },
        {
          label: '星期三',
          value: 3
        },
        {
          label: '星期四',
          value: 4
        },
        {
          label: '星期五',
          value: 5
        },
        {
          label: '星期六',
          value: 6
        },
        {
          label: '星期日',
          value: 7
        }]
    }
  },

  created() {
    this.table();
    this.pageNum = 1;
    this.pageSize = 5;
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    this.getClassroom();
    this.getTypes();
  },

  methods: {
    table() {
      request.get("/cource?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize).then(res => {
        if (res.code === '0'){
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    edit(cource) {
      this.form = cource;
      this.dialogFormVisible = true;
    },
    del(id) {
      request.delete("/cource?id=" + id).then(res => {
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
    },

    insert(){
      if (this.form.id != null) {
        request.put("/cource" , this.form).then(res => {
          if (res.code === '0'){
            this.dialogFormVisible = false;
            this.$message.success("编辑成功");
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      } else {
        request.post("/cource" , this.form).then(res => {
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
      request.put("/cource/delSelected" , this.multipleSelection).then(res => {
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

    getClassroom(){
      request.get("/classroom/cource").then(res => {
        if (res.code === '0'){
          this.options = res.data;
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    getTypes(){
      request.get("/type").then(res => {
        if (res.code === '0'){
          this.types = res.data;
        } else {
          this.$message.error(res.msg);
        }
      })
    },


    xuanjiao(cource){
      request.put("/cource/xuanjiao?teacherId=" + this.user.id , cource).then(res => {
        if (res.code === '0'){
          this.$message.success("选教成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    agree(cource) {
      request.put("/cource/agree" , cource).then(res => {
        if (res.code === '0'){
          this.$message.success("同意当前老师选教这个课程");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },


    disagree(cource) {
      request.put("/cource/disagree" , cource).then(res => {
        if (res.code === '0'){
          this.$message.warning("不同意当前老师选教这个课程");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },


    force(cource){
      request.put("/cource/force" , cource).then(res => {
        if (res.code === '0') {
          this.$message.success("成功强制删除");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    xuanke(cource){
      request.put("/cource/xuanke?studentId=" + this.user.id , cource).then(res => {
        if (res.code === '0'){
          this.$message.success("选课成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    tuixuan(cource){
      request.put("/cource/tuixuan?studentId=" + this.user.id , cource).then(res => {
        if (res.code === '0'){
          this.$message.success("退选成功");
          this.table();
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
      <el-button type="primary" style="margin-left: 10px" @click="add()" v-if="user.level === 'admin'">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="delSelected()">
        <el-button slot="reference" type="danger" style="margin-left: 5px" v-if="user.level === 'admin'">批量删除</el-button>
      </el-popconfirm>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" ref="table" @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column ref="table" type="selection" width="55" align="center" :reserve-selection="true"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="capacity" label="选择这门课程的学生"></el-table-column>
        <el-table-column prop="type" label="选择课程的专业要求"></el-table-column>
        <el-table-column prop="teacherName" label="执教的老师"></el-table-column>
        <el-table-column prop="classroom" label="上课的所在教室"></el-table-column>
        <el-table-column prop="date" label="上课的时间"></el-table-column>
        <el-table-column prop="weekName" label="上课的星期"></el-table-column>
        <el-table-column prop="weekDay" label="持续星期"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)" v-if="user.level === 'admin'& scope.row.state === 0">编辑</el-button>
            <el-button type="primary" @click="xuanjiao(scope.row)" v-if="user.level === 'teacher' & scope.row.state === 0 & user.courceId.indexOf(scope.id) === -1">选教</el-button>
            <el-button type="primary" @click="xuanke(scope.row)" v-if="user.level === 'student' & scope.row.state === 1">选课</el-button>
            <el-button type="danger" @click="tuixuan(scope.row)" v-if="user.level === 'student' & scope.row.state === 1">退选</el-button>
            <el-button type="danger" @click="force(scope.row)" v-if="user.level === 'admin' & scope.row.isTrue !== null">强制删除当前课程</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px" v-if="user.level === 'admin' & scope.row.state === 0">删除</el-button>
            </el-popconfirm>
            <el-button v-if="user.level === 'admin' & scope.row.state === 1 & scope.row.isTrue === null" type="primary" @click="agree(scope.row)">同意</el-button>
            <el-button v-if="user.level === 'admin' & scope.row.state === 1 & scope.row.isTrue === null" type="danger" @click="disagree(scope.row)">不同意</el-button>
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
      <el-dialog title="请填写信息" :visible.sync="dialogFormVisible" width="40%">
        <el-form :model="form">
          <el-form-item label="名称" label-width="15%">
            <el-input v-model="form.name" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="教室" label-width="15%">
            <el-select v-model="form.classroom" placeholder="选择预约的教室" style="width: 90%">
              <el-option
                  v-for="item in options"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业选择" label-width="15%">
            <el-select v-model="form.formType" multiple placeholder="请选择专业" style="width: 90%">
              <el-option
                  v-for="item in types"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上课时间" label-width="15%">
            <el-select v-model="form.date" placeholder="请选择" style="width: 90%">
              <el-option
                  v-for="item in times"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上课的星期" label-width="15%">
            <el-select v-model="form.week" placeholder="请选择" style="width: 90%">
              <el-option
                  v-for="item in weeks"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="持续星期数" label-width="15%">
            <el-input v-model="form.weekDay" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="quxiao()">取 消</el-button>
          <el-button type="primary" @click="insert()">确 定</el-button>
        </div>
      </el-dialog>



    </div>
  </div>
</template>

<style scoped>

</style>