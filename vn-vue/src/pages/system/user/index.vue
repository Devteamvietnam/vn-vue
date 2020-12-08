<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--Department Data-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="The department name"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--User Data-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          v-show="showSearch"
          label-width="100px"
        >
          <el-form-item label="Name" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="Please enter the user name"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="Phone" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="Please enter your mobile phone number"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="Status" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="User status"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Time">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="Start date"
              end-placeholder="End date"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              type="cyan"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >Search</el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >Reset</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
              >New</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:user:edit']"
              >Edit</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
              >Delete</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['system:user:import']"
              >Import</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
              >Export</el-button
            >
          </el-col>
          <right-toolbar
            :showSearch.sync="showSearch"
            @queryTable="getList"
          ></right-toolbar>
        </el-row>

        <el-table
          v-loading="loading"
          :data="userList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column sortable label="id" align="center" prop="userId" />
          <el-table-column
            label="User Login"
            align="center"
            prop="userName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="Name"
            align="center"
            prop="nickName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="Department"
            align="center"
            prop="dept.deptName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="Phone"
            align="center"
            prop="phonenumber"
            width="120"
          />
          <el-table-column label="Status" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="Createtime"
            align="center"
            prop="createTime"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="Action"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <!-- <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']">Edit</el-button> -->
              <!-- <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']">Delete</el-button> -->
              <el-button
                size="mini"
                type="text"
                icon="el-icon-key"
                @click="handleResetPwd(scope.row)"
                v-hasPermi="['system:user:resetPwd']"
                >Reset</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- Add or modify parameter configuration dialog box -->
    <el-dialog
      v-el-drag-dialog
      :title="title"
      :visible.sync="open"
      width="700px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Name" prop="nickName">
              <el-input
                v-model="form.nickName"
                placeholder="Please enter the user's nickname"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Attribution" prop="deptId">
              <treeselect
                v-model="form.deptId"
                :options="deptOptions"
                :show-count="true"
                placeholder="Please select the department attribution"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Phone" prop="phonenumber">
              <el-input
                v-model="form.phonenumber"
                placeholder="Please enter the phone number"
                maxlength="11"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Email" prop="email">
              <el-input
                v-model="form.email"
                placeholder="Please enter the email"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              v-if="form.userId == undefined"
              label="User Login"
              prop="userName"
            >
              <el-input
                v-model="form.userName"
                placeholder="Please enter the user name"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="form.userId == undefined"
              label="User password"
              prop="password"
            >
              <el-input
                show-password
                v-model="form.password"
                placeholder="Please enter the user password"
                type="password"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="Gender">
              <el-select v-model="form.sex" placeholder="Please select">
                <el-option
                  v-for="dict in sexOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  >{{ dict.dictLabel }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Post">
              <el-select
                v-model="form.postIds"
                multiple
                placeholder="Please select"
              >
                <el-option
                  v-for="item in postOptions"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Role">
              <el-select
                v-model="form.roleIds"
                multiple
                placeholder="Please select"
              >
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Remarks">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="Please enter content"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">OK</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>

    <!-- User import dialog box -->
    <el-dialog
      v-el-drag-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          Drag the file here, or
          <em>Click to upload</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />Whether to update
          existing user data
          <el-link type="info" style="font-size: 12px" @click="importTemplate"
            >Download template</el-link
          >
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          Tip: Only "xls" or "xlsx" format files are allowed to be imported!
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">OK</el-button>
        <el-button @click="upload.open = false">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  exportUser,
  resetUserPwd,
  changeUserStatus,
  importTemplate,
} from "@/services/api/system/user";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/services/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import elDragDialog from "@/components/el-drag-dialog";

export default {
  name: "User",
  components: { Treeselect },
  directives: { elDragDialog },
  data() {
    return {
      // Mask layer
      loading: true,
      // select the array
      ids: [],
      // not individually disabled
      single: true,
      // not multiple disabled
      multiple: true,
      // Show search criteria
      showSearch: false,
      // Total number
      total: 0,
      // User table data
      userList: null,
      // Pop-up layer title
      title: "",
      // Department tree options
      deptOptions: undefined,
      // Whether to display the pop-up layer
      open: false,
      // Department name
      deptName: undefined,
      // default password
      initPassword: undefined,
      // date range
      dateRange: [],
      // State data dictionary
      statusOptions: [],
      // Gender status dictionary
      sexOptions: [],
      // Position options
      postOptions: [],
      // role options
      roleOptions: [],
      // form parameters
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // User import parameters
      upload: {
        // Whether to display the pop-up layer (user import)
        open: false,
        // Pop-up layer title (user import)
        title: "",
        // Whether to disable upload
        isUploading: false,
        // Whether to update existing user data
        updateSupport: 0,
        // Set the upload request header
        headers: { Authorization: "Bearer " + getToken() },
        // Uploaded address
        url: process.env.VUE_APP_BASE_API + "/system/user/importData",
      },
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
      },
      // form validation
      rules: {
        userName: [
          {
            required: true,
            message: "User name cannot be empty",
            trigger: "blur",
          },
        ],
        nickName: [
          {
            required: true,
            message: "User nickname cannot be empty",
            trigger: "blur",
          },
        ],
        deptId: [
          {
            required: true,
            message: "Attribution department cannot be empty",
            trigger: "change",
          },
        ],
        password: [
          {
            required: true,
            message: "User password cannot be empty",
            trigger: "blur",
          },
        ],
        email: [
          {
            required: true,
            message: "Email address cannot be empty",
            trigger: "blur",
          },
          {
            type: "email",
            message: "'Please enter the correct email address",
            trigger: ["blur", "change"],
          },
        ],
        phonenumber: [
          {
            required: true,
            message: "Mobile phone number cannot be empty",
            trigger: "blur",
          },
          {
            //pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "Please enter the correct mobile phone number",
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
    // Filter department tree by name
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then((response) => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then((response) => {
      this.sexOptions = response.data;
    });
    this.getConfigKey("sys.user.initPassword").then((response) => {
      this.initPassword = response.msg;
    });
  },

  methods: {
    /** Query user list */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** Query the department drop-down tree structure */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    // Filter nodes
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // node click event
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // User status modification
    handleStatusChange(row) {
      let text = row.status === "0" ? "Enable" : "Disable";
      this.$confirm(
        'Confirm to "' + text + '" "' + row.userName + '"User?',
        "Warning",
        {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      )
        .then(function () {
          return changeUserStatus(row.userId, row.status);
        })
        .then(() => {
          this.msgSuccess(text + "Success");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // Cancel button
    cancel() {
      this.open = false;
      this.reset();
    },
    // form reset
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: [],
      };
      this.resetForm("form");
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** Reset button operation */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** New button operation */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      getUser().then((response) => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "Add Personnel";
        this.form.password = this.initPassword;
      });
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const userId = row.userId || this.ids;
      getUser(userId).then((response) => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.form.postIds = response.postIds;
        this.form.roleIds = response.roleIds;
        this.open = true;
        this.title = "Modify User";
        this.form.password = "";
      });
    },
    /** Reset password button operation */
    handleResetPwd(row) {
      this.$prompt(
        'Please enter "' + row.userName + '" new password',
        "Prompt",
        {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
        }
      )
        .then(({ value }) => {
          resetUserPwd(row.userId, value).then((response) => {
            this.msgSuccess(
              "Modified successfully, the new password is:" + value
            );
          });
        })
        .catch(() => {});
    },
    /** Submit button */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then((response) => {
              this.msgSuccess("Modified successfully");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then((response) => {
              this.msgSuccess("Add success");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** Delete button operation */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$confirm(
        'Are you sure to delete the data item with user ID "' + userIds + '"?',
        "Warning",
        {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      )
        .then(function () {
          return delUser(userIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("Delete successfully");
        });
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("Are you sure to export all user data items?", "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return exportUser(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
    /** Import button operation */
    handleImport() {
      this.upload.title = "User Import";
      this.upload.open = true;
    },
    /** Download template operation */
    importTemplate() {
      importTemplate().then((response) => {
        this.download(response.msg);
      });
    },
    // Processing during file upload
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // File upload successfully processed
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "Import result", {
        dangerouslyUseHTMLString: true,
      });
      this.getList();
    },
    // Submit upload file
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>
