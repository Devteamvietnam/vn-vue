<template>
  <div class="dashboard-editor-container">
    <div class="title">{{ timeFix }}，{{ name }}，{{ welcome }}</div>
    <br>

    <!-- Introduce -->
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>Contact information</span>
          </div>

          <div class="body">
            <p>
              <i class="el-icon-s-promotion" /> Official website:
              <el-link
                href="https://devteamvietnam.com"
                target="_blank"
              >https://devteamvietnam.com</el-link>
            </p>
            <i class="el-icon-user-solid" /> Gmail : devteamvietnam@gmail.com
            <p class="el-icon-user-solid">Mail dev: jungjihoonkr97@gmail.com</p>
            <p class="el-icon-mobile-phone">Phone 0337303666</p>
            <a href="https://devteamvietnam.com" target="_blank" />
            <p>
              <i class="el-icon-chat-dot-round" /> Facebook:
              <a href="javascript:;">/ *Ivan</a>
            </p>
            <p>
              <i class="el-icon-money" /> Github:<a
                href="javascript:;"
                class="Alipay Information"
              >/ *Ivan</a>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>Update log</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v1.0.0-2020-11-17">
              <ol>
                <li>
                  Added menu and data permissions (expand/collapse select
                  all/unselect all parent-child linkage)
                </li>
                <li>Allow users to assign to department parent node</li>
                <li>The menu adds whether to cache keep-alive or not</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v1.3.0-2020-11-24">
              <ol>
                <li>
                  Added menu and data permissions (expand/collapse select
                  all/unselect all parent-child linkage)
                </li>
                <li>Allow users to assign to department parent node</li>
                <li>The menu adds whether to cache keep-alive or not</li>
                <li>Table operation column spacing adjustment</li>
                <li>
                  Restricted system built-in parameters are not allowed to be
                  deleted
                </li>
                <li>
                  Optimized rich text components, supports custom height and
                  image conflict issues
                </li>
                <li>Rich text toolbar style alignment</li>
                <li>Import excel shaping value verification optimization</li>
                <li>
                  Fixed the issue of fixed tab routing not refreshing when tabs
                  are closed all
                </li>
                <li>Fixed error Java > 9</li>
                <li>Fixed query Database dept error user permissions</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <div class="App">
          <header class="App-header">
            <img src="@/assets/images/avatar.png" class="App-logo" alt="logo">
          </header>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { timeFix } from '@/utils/util'
import { welcome } from '@/utils/util'
import RaddarChart from './dashboard/RaddarChart'
import { mapState } from 'vuex'
import { mapGetters } from 'vuex'

export default {
  name: 'Index',
  components: {
    RaddarChart
  },
  data() {
    return {
      // Version number
      version: '1.0.0',
      timeFix: timeFix(),
      welcome: welcome()
    }
  },
  computed: {
    ...mapGetters(['name', 'avatar', 'device'])
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    goTarget(href) {
      window.open(href, '_blank')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
.App {
  text-align: center;
}

@media only  screen and (min-width: 768px) {
  .App-logo {
  height: 30vmin;
  pointer-events: none;
  }
}

.App-logo {
  height: 25vmin;
  pointer-events: none;
}

@media (prefers-reduced-motion: no-preference) {
  .App-logo {
    animation: App-logo-spin infinite 20s linear;
  }
}

.App-header {
  min-height: 25vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}

.App-link {
  color: #61dafb;
}

@keyframes App-logo-spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
.title {
  font: 1em sans-serif;
  font-size: 100%;
  text-align: center;
}
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
