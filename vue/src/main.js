import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/global.css'

// 在下面的ElementUI的后面加上第二个参数来表示全局的设置，如下面这个表示将所有的标签都设置小，这里不管是字体还是按钮都变小
Vue.use(ElementUI , {size: "small"});

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
