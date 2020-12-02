<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--Department Data-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" placeholder="The department name" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px" />
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick" />
        </div>
      </el-col>
      <!--wareHouse Data-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
          <el-form-item label="Code" prop="wareCode">
              <el-input v-model="queryParams.wareCode" placeholder="Please enter the ware code" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="Name" prop="wareName">
            <el-input v-model="queryParams.wareName" placeholder="Please enter the name of the ware" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="Status" prop="status">
            <el-select v-model="queryParams.status" placeholder="Ware status" clearable size="small" style="width: 240px">
              <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="Time">
            <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="Start date" end-placeholder="End date"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
          </el-form-item>
        </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:ware:add']">New</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:ware:edit']">Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:ware:remove']">Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:ware:export']">Export</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

        <el-table v-loading="loading" :data="wareList" @selection-change="handleSelectionChange">
          <el-table-column  type="selection" width="55" align="center" />
          <el-table-column sortable label="Number" align="center" prop="wareId" />
          <el-table-column label="Code" align="center" prop="wareCode" :show-overflow-tooltip="true" />
          <el-table-column label="Name" align="center" prop="wareName" :show-overflow-tooltip="true" />
        <!--   <el-table-column label="Department" align="center" prop="dept.deptName" :show-overflow-tooltip="true" /> -->
          <el-table-column label="Sorting" align="center" prop="wareSort" width="120" />
          <el-table-column label="Status" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="Createtime" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <!--  <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:post:edit']">Edit</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:post:remove']">Delete</el-button>
        </template>
      </el-table-column> -->
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- Add or modify post dialog box -->
    <el-dialog v-el-drag-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Name" prop="wareName">
          <el-input v-model="form.wareName" placeholder="Please enter the ware name" />
        </el-form-item>
        <el-form-item label="Code" prop="wareCode">
          <el-input v-model="form.wareCode" placeholder="Please enter the code name" />
        </el-form-item>
        <el-form-item label="Order" prop="wareSort">
          <el-input-number v-model="form.wareSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Remarks" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="Please enter the content" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">OK</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWare, getWare, delWare, addWare, updateWare, exportWare } from "@/services/api/system/ware"
import elDragDialog from '@/components/el-drag-dialog'
import { treeselectWare } from "@/services/api/system/deptWare"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "Ware",
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
      // Pop-up layer title
      title: "",
      // Department tree options
      deptOptions: undefined,
      // Whether to display the pop-up layer
      open: false,
      // Department name
      deptName: undefined,
      // date range
      dateRange: [],
      // Ware table data
      wareList: [],
      // State data dictionary
      statusOptions: [],
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
        wareName: [{ required: true, message: "Ware name cannot be empty", trigger: "blur" }],
        wareCode: [{ required: true, message: "Ware code cannot be empty", trigger: "blur" }],
        wareSort: [{ required: true, message: "The ware sequence cannot be empty", trigger: "blur" }],
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
  },

  methods: {
    /** Query user list */
    getList() {
      this.loading = true;
      listWare(this.addDateRange(this.queryParams, this.dateRange)).then((response) => {
        this.wareList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** Query the department drop-down tree structure */
    getTreeselect() {
      treeselectWare().then((response) => {
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
      this.$confirm('Confirm to "' + text + '" "' + row.userName + '"User?', "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return changeUserStatus(row.userId, row.status);
        })
        .then(() => {
          this.msgSuccess(text  +  "Success");
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
        wareId: undefined,
        wareCode: undefined,
        wareName: undefined,
        wareSort: 0,
        status: "0",
        remark: undefined,
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
      this.open = true;
      this.title = "Add Ware";
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const wareId = row.wareId || this.ids;
      getWare(wareId).then((response) => {
        this.form = response.data;
        this.form = response.rolesId;
        this.open = true;
        this.title = "Modify Post";
      });
    },
    /** Submit button */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.wareId != undefined) {
            updateWare(this.form).then((response) => {
              this.msgSuccess("Modified successfully");
              this.open = false;
              this.getList();
            });
          } else {
            addWare(this.form).then((response) => {
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
      const wareIds = row.wareId || this.ids;
      this.$confirm('Are you sure to delete the data item with the ware number "' + wareIds + '"?', "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return delWare(wareIds);
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
          return exportWare(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
    // Submit upload file
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>
