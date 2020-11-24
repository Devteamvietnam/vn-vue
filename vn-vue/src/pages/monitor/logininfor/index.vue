<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="Address" prop="ipaddr">
        <el-input v-model="queryParams.ipaddr" placeholder="Please enter the login address" clearable style="width: 240px" size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Name" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="Please enter the user name" clearable style="width: 240px" size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="queryParams.status" placeholder="Login status" clearable size="small" style="width: 240px">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="Login time">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="start date" end-placeholder="end date"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['monitor:logininfor:remove']">Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleClean" v-hasPermi="['monitor:logininfor:remove']">Empty</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:logininfor:export']">Export</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Access number" align="center" prop="infoId" />
      <el-table-column label="user name" align="center" prop="userName" />
      <el-table-column label="Login address" align="center" prop="ipaddr" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="Login Location" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
      <el-table-column label="browser" align="center" prop="browser" />
      <el-table-column label="operating system" align="center" prop="os" />
      <el-table-column label="Login status" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="Operation information" align="center" prop="msg" />
      <el-table-column label="login date" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { list, delLogininfor, cleanLogininfor, exportLogininfor } from "@/services/api/monitor/logininfor";

export default {
  name: "Logininfor",
  data() {
    return {
      // Mask layer
      loading: true,
      // select the array
      ids: [],
      // not multiple disabled
      multiple: true,
      // Show search criteria
      showSearch: false,
      // Total number
      total: 0,
      // table data
      list: [],
      // State data dictionary
      statusOptions: [],
      // date range
      dateRange: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined,
        status: undefined,
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_common_status").then((response) => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** Query login log list */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then((response) => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // Login status dictionary translation
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.pageNum = 1;
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
      this.ids = selection.map((item) => item.infoId);
      this.multiple = !selection.length;
    },
    /** Delete button operation */
    handleDelete(row) {
      const infoIds = row.infoId || this.ids;
      this.$confirm('Are you sure to delete the data item with access number "' + infoIds + '"?', "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return delLogininfor(infoIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("Delete successfully");
        });
    },
    /** Clear button operation */
    handleClean() {
      this.$confirm("Are you sure to clear all login log data items?", "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return cleanLogininfor();
        })
        .then(() => {
          this.getList();
          this.msgSuccess("Empty successfully");
        });
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("Are you sure to export all operation log data items?", "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return exportLogininfor(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
