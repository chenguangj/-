import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import Layout from "@/views/Layout.vue";
import admin from "@/views/admin.vue";
import teacher from "@/views/teacher.vue";
import student from "@/views/student.vue";
import seftCenter from "@/views/seftCenter.vue";
import classroom from "@/views/classroom.vue";
import classroomRe from "@/views/classroomRe.vue";
import type from "@/views/type.vue";
import cource from "@/views/cource.vue";
import courceRe from "@/views/courceRe.vue";
import notice from "@/views/notice.vue";
import schoolTimeable from "@/views/SchoolTimeable.vue";
import userNotice from "@/views/userNotice.vue";
import classroomNotice from "@/views/classroomNotice.vue";
import courceNotice from "@/views/courceNotice.vue";
import homeworkSt from "@/views/homeworkSt.vue";
import homeworkTe from "@/views/homeworkTe.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/layout',
    name: 'layout',
    component: Layout,
    children: [
      {
        path: '/admin',
        name: 'admin',
        component: admin,
      },
      {
        path: '/teacher',
        name: 'teacher',
        component: teacher,
      },
      {
        path: '/student',
        name: 'student',
        component: student,
      },
      {
        path: '/seftCenter',
        name: 'seftCenter',
        component: seftCenter,
      },
      {
        path: '/classroom',
        name: 'classroom',
        component: classroom
      },
      {
        path: '/classroomRe',
        name: 'classroomRe',
        component: classroomRe
      },
      {
        path: '/type',
        name: 'type',
        component: type
      },
      {
        path: '/cource',
        name: 'cource',
        component: cource
      },
      {
        path: '/courceRe',
        name: 'courceRe',
        component: courceRe
      },
      {
        path: '/schoolTimeable',
        name: 'schoolTimeable',
        component: schoolTimeable
      },
      {
        path: '/notice',
        name: 'notice',
        component: notice
      },
      {
        path: '/userNotice',
        name: 'userNotice',
        component: userNotice
      },
      {
        path: "/classroomNotice",
        name: 'classroomNotice',
        component: classroomNotice
      },
      {
        path: '/courceNotice',
        name: 'courceNotice',
        component: courceNotice
      },
      {
        path: '/homeworkSt',
        name: 'homeworkSt',
        component: homeworkSt
      },
      {
        path: '/homeworkTe',
        name: 'homeworkTe',
        component: homeworkTe
      }
    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to , from , next) => {
  const user = localStorage.getItem("user");
  if (!user && to.path !== '/login' && to.path !== '/register'){
    return next("/login");
  }
  next();
})

export default router
