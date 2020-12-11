import Vue from'vue'
import VueI18n from 'vue-i18n'
import Cookies from'js-cookie'
import Element from'element-ui'
import'./assets/styles/element-variables.scss'
import'@/assets/styles/index.scss' // global css
import'@/assets/styles/dev.scss' // ruoyi css
import App from'./App'
import store from'./store'
import router from'./router'
import permission from'./Security/permission'
import'./assets/icons' // icon
import'./permission' // permission control
import {getDicts} from "@/services/api/system/dict/data";
import {getConfigKey} from "@/services/api/system/config";
import {parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, download, handleTree} from "@/utils/devteam";
import Pagination from "@/components/Pagination";
//Custom table tool extension
import RightToolbar from "@/components/RightToolbar"
// fix languages 
import enLang from'element-ui/lib/locale/lang/en'


// global method mount
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// Global component mounting
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.use(permission)

Vue.use(Element, {
  size: Cookies.get('size') ||'medium', // set element-ui default size
  locale: enLang
})
Vue.use(VueI18n)

Vue.config.productionTip = false

new Vue({
  el:'#app',
  router,
  store,
  render: h => h(App)
})