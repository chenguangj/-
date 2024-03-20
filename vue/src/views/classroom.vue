<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default defineComponent({
  name: "classroom",
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
      reName: '未被预约'
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
      request.get("/classroom?name=" + this.name + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize).then(res => {
        if (res.code === '0'){
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    edit(classroom) {
      this.form = classroom;
      this.dialogFormVisible = true;
    },
    del(id) {
      request.delete("/classroom?id=" + id).then(res => {
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
        request.put("/classroom" , this.form).then(res => {
          if (res.code === '0'){
            this.dialogFormVisible = false;
            this.$message.success("编辑成功");
            this.table();
          } else {
            this.$message.error(res.msg);
          }
        })
      } else {
        request.post("/classroom" , this.form).then(res => {
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
      request.put("/classroom/delSelected" , this.multipleSelection).then(res => {
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

    openDia(classroom){
      this.dialogFormVisible2 = true;
      this.form = classroom;
    },

    re() {
      request.post("/classroom/re?userId=" + this.user.id + "&level=" + this.user.level , this.form).then(res => {
        if (res.code === '0'){
          this.$message.success("预约成功");
          this.dialogFormVisible2 = false;
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    quxiao2() {
      this.dialogFormVisible2 = false;
      this.form = {};
    },


    agree(classroom) {
      request.put("/classroom/agree" , classroom).then(res => {
        if (res.code === '0'){
          this.$message.success("同意当前用户预约成功");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },


    disagree(classroom) {
      request.put("/classroom/disagree" , classroom).then(res => {
        if (res.code === '0'){
          this.$message.warning("不同意当前用户预约这个教室");
          this.table();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    forcere(classroom){
      request.put("/classroom/forcere" , classroom).then(res => {
        if (res.code === '0') {
          this.$message.success("成功强制删除");
          this.table();
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
      <el-button type="primary" style="margin-left: 10px" @click="add()" v-if="user.level === 'admin'">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="delSelected()">
        <el-button slot="reference" type="danger" style="margin-left: 5px" v-if="user.level === 'admin'">批量删除</el-button>
      </el-popconfirm>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" ref="table" @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column ref="table" type="selection" width="55" align="center" :reserve-selection="true"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="capacity" label="容量"></el-table-column>
        <el-table-column prop="reName" label="预约的角色"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)" v-if="user.level === 'admin' & scope.row.state === null">编辑</el-button>
            <el-button type="primary" @click="openDia(scope.row)" v-if="user.level !== 'admin' & scope.row.state === null">预约</el-button>
            <el-button type="danger" @click="forcere(scope.row)" v-if="user.level === 'admin' & scope.row.isTrue !== null">强制撤回教室</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px" v-if="user.level === 'admin' & scope.row.state === null">删除</el-button>
            </el-popconfirm>
            <el-button v-if="user.level === 'admin' & scope.row.state !== null & scope.row.isTrue === null" type="primary" @click="agree(scope.row)">同意</el-button>
            <el-button v-if="user.level === 'admin' & scope.row.state !== null & scope.row.isTrue === null" type="danger" @click="disagree(scope.row)">不同意</el-button>
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
          <el-form-item label="名称" label-width="15%">
            <el-input v-model="form.name" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="容量" label-width="15%">
            <el-input v-model="form.capacity" autocomplete="off" style="width: 90%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="quxiao()">取 消</el-button>
          <el-button type="primary" @click="insert()">确 定</el-button>
        </div>
      </el-dialog>


      <el-dialog title="请填写你预约的教室的开始时间和结束时间" :visible.sync="dialogFormVisible2" width="30%">
        <el-form :model="form">
          <el-form-item label="开始时间" label-width="15%">
            <el-date-picker
                v-model="form.initTime"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd-HH-mm-ss"
                default-time="00:00:00">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" label-width="15%">
            <el-date-picker
                v-model="form.destroyTime"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd-HH-mm-ss"
                default-time="00:00:00">
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="quxiao2()">取 消</el-button>
          <el-button type="primary" @click="re()">确 定</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<style scoped>

</style>