<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <blockquote class="text-warning" style="font-size: 14px">
           Receive CTN Management System
           <br />
           <el-link
             href="https://github.com/Devteamvietnam/vue-press-page"
             type="primary"
             target="_blank"
             >https://github.com/Devteamvietnam/vue-press-page</el-link
           >
           <br />
            Receive CTN Management System
           <br />
           <el-link
             href="https://github.com/Devteamvietnam/vue-press-page"
             type="primary"
             target="_blank"
             >https://github.com/Devteamvietnam/vue-press-page</el-link
           >
           <br />
           Devteam Cloud Server Discount Area
          <el-link href="https://devteamvietnam.com" type="primary" target="_blank">
            >☛☛Click me to enter ☚☚
            </el-link>
          <br />
          Devteam Cloud Server spike area
          <el-link href="https://devteamvietnam.com" type="primary" target="_blank"
            >>☛☛Click me to enter ☚☚</el-link
          ><br />
          <h4 class="text-danger">
            General red envelopes for cloud products can be used with regular discounts on the official website. (For new users only)
          </h4>
        </blockquote>

        <hr />
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>CTN background management framework</h2>
        <p>
          I have always wanted to build a back-end management system. I read a lot of excellent open source projects but found that there is no suitable one for me. So I started to write a back-end system in my free time. So with Ruoyi management system. , It can be used for all web applications, such as website management backend, website membership center, CMS, CRM, OA, etc. Of course, you can also customize her deeply to make a stronger system. All front-end and back-end codes are packaged and easy to use, with low error probability. It also supports mobile client access. The system will continue to update some useful functions.
        </p>
        <p>
          <b>Current version:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">free and open source</el-tag>
        </p>
        <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTarget('https://github.com/Devteamvietnam/vn-vue')"
            >Visit Git</el-button
          >
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('https://github.com/Devteamvietnam/vue-press-page')"
            >Visit homepage</el-button
          >
        </p>
      </el-col>

    <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>Technology selection</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>Back-end technology</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>Front-end technology</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>Contact information</span>
          </div>

          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> Official website: <el-link
                href="https://devteamvietnam.com"
                target="_blank"
                >https://devteamvietnam.com</el-link
              >
            </p>
             <p>
               <i class="el-icon-user-solid"></i> Gmail : <p>devteamvietnam@gmail.com</p>
               <p>Over jungjihoonkr97@gmail.com</p> <p> Over 0337303666</p>
               <a href="https://devteamvietnam.com" target="_blank"
                 > jungjihoonkr97</a
               >
           <p>
              <i class="el-icon-chat-dot-round"></i> Facebook: <a
                href="javascript:;"
                >/ *Ivan</a
              >
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
                <li>Added menu and data permissions (expand/collapse select all/unselect all parent-child linkage)</li>
                <li>Allow users to assign to department parent node</li>
                <li>The menu adds whether to cache keep-alive or not</li>
                <li>Table operation column spacing adjustment</li>
                <li>Restricted system built-in parameters are not allowed to be deleted</li>
                <li>Optimized rich text components, supports custom height and image conflict issues</li>
                <li>Rich text toolbar style alignment</li>
                <li>Import excel shaping value verification optimization</li>
                <li>Fixed the issue of fixed tab routing not refreshing when tabs are closed all</li>
              </ol>
            </el-collapse-item>



          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      // Version number
      version: "1.0.0",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
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
</style>

