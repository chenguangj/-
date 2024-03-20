
<template>
  <div class="course-table">
    <span style="margin-left:10px"><i
        class="el-icon-date"
        style="margin-right:5px"
      ></i>课表查询</span>

    <table style="margin-top:10px">
      <thead>
        <tr>
          <th>时间</th>
          <th>周一</th>
          <th>周二</th>
          <th>周三</th>
          <th>周四</th>
          <th>周五</th>
          <th>周六</th>
          <th>周日</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row, rowIndex) in timetable" :key="rowIndex">
          <td>{{ getTime(rowIndex) }}</td>
          <td v-for="(col, colIndex) in row" :key="colIndex">
            <div v-if="col.course" v-html="col.course"></div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import request from "@/utils/request";

export default {
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      courses: [],

      timetable: [
        [
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
        ],
        [
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
        ],
        [
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
        ],
        [
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
          { course: "" },
        ],
      ],
    };
  },
  methods: {


    getTime(index) {
      const times = [
        "第一大节 8:10 - 9:45",
        "第二大节 10:15 - 11:50",
        "第三大节 14:30 - 16:05",
        "第四大节 16:30 - 18:05",
      ];

      return times[index];
    },

    updateUser (){
      request.post("/schoolTime" , this.user).then(res => {
        localStorage.setItem("user" , JSON.stringify(res.data));
      })
    },

    getget(){
      request.put("/cource/schoolTime", this.user).then((res) => {
        console.log(res);
        this.courses = res.data.map((item) => ({
          name: item.name,
          location: item.classroom,
          time: item.date,
          teacheridd: item.teacherid,
          week: item.week,
          weekDay: item.weekDay,
          teacherNa: item.teacherName
        }));

        this.courses.forEach((course) => {
          const time = course.time;
          const location = course.location;
          const week = course.week;
          const weekDay = course.weekDay;
          // 查询教师信息，获取教师姓名
          request.put("/teacherInfo/getname?id=" +  course.teacheridd).then((res) => {
            const teachername = course.teacherNa;

            const timeMap = {
              '08-10-00': 0,
              '10-15-00': 1,
              '14-30-00': 2,
              '16-30-00': 3,
            };
            const timeIndex = timeMap[time.split("and")[0]];

            const dayOfWeekIndex = [
              "1",
              "2",
              "3",
              "4",
              "5",
              "6",
              "7",
            ].indexOf(week);

            const courseInfo = `课程名称是${course.name}<br>由${teachername}老师教学<br>在${location}教室<br>持续${weekDay}周`;

            this.timetable[timeIndex][dayOfWeekIndex].course = courseInfo;
          });
        });
      });
    }

  },

  created() {
    this.getget();
    this.updateUser();
  },
};
</script>

<style scoped>
.course-table {
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 5px;
}
table {
  border-collapse: collapse;
  width: 100%;
}

th {
  background-color: #3ba6f2;
  color: #fff;
  font-weight: bold;
  padding: 8px;
  text-align: center;
  border: 1px solid #fff;
}

td {
  border: 1px solid #fff;
  padding: 8px;
  text-align: center;
  background-color: #bcebff;
  height: 80px;
}

tbody tr:nth-child(even) td {
  background-color: #bcebff;
}

.course-cell {
  background-color: #bcebff;
}
</style>