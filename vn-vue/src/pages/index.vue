<template>
  <div class="dashboard-editor-container">
    <panel-group @handleSetLineChartData="handleSetLineChartData" />
    <el-row style="background: #fff; padding: 16px 16px 0; margin-bottom: 32px">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
    </el-row>
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
              <i class="el-icon-s-promotion"></i> Official website:
              <el-link href="https://devteamvietnam.com" target="_blank"
                >https://devteamvietnam.com</el-link
              >
            </p>
            <i class="el-icon-user-solid"></i> Gmail : devteamvietnam@gmail.com
            <p class="el-icon-user-solid">Mail dev: jungjihoonkr97@gmail.com</p>
            <p class="el-icon-mobile-phone">Phone 0337303666</p>
            <a href="https://devteamvietnam.com" target="_blank"></a>
            <p>
              <i class="el-icon-chat-dot-round"></i> Facebook:
              <a href="javascript:;">/ *Ivan</a>
            </p>
            <p>
              <i class="el-icon-money"></i> Github:<a
                href="javascript:;"
                class="Alipay Information"
                >/ *Ivan</a
              >
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
        <todo-list />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from "./dashboard/PanelGroup";
import LineChart from "./dashboard/LineChart";
import RaddarChart from "./dashboard/RaddarChart";
import PieChart from "./dashboard/PieChart";
import BarChart from "./dashboard/BarChart";
import TodoList from "./dashboard/TodoList";

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145],
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130],
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130],
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130],
  },
};

export default {
  name: "Index",
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    TodoList,
  },
  data() {
    return {
      // Version number
      version: "1.0.0",
      lineChartData: lineChartData.newVisitis,
    };
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type];
    },
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
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
