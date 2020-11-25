<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="Name" prop="configName">
        <el-input v-model="queryParams.configName" placeholder="Please enter the parameter name" clearable size="small" style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Key" prop="configKey">
        <el-input v-model="queryParams.configKey" placeholder="Please enter the parameter key name" clearable size="small" style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Built-in" prop="configType">
        <el-select v-model="queryParams.configType" placeholder="System built-in" clearable size="small">
          <el-option v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="Time">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="start date" end-placeholder="end date"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:config:add']">New</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:config:edit']">Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:remove']">Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:config:export']">Export</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-refresh" size="mini" @click="handleClearCache" v-hasPermi="['system:config:remove']">Clean up cache</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="60" align="center" />
      <el-table-column label="id" align="center" prop="configId" width="80" />
      <el-table-column label="Name" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column label="Key name" align="center" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column label="Key value" align="center" prop="configValue" />
      <el-table-column label="System built-in" align="center" prop="configType" :formatter="typeFormat" />
      <el-table-column label="Remarks" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="Create time" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
     <!-- <el-table-column label="operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']">Edit</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:config:remove']">Delete</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- Add or modify parameter configuration dialog box -->
    <el-dialog v-el-drag-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="Name" prop="configName">
          <el-input v-model="form.configName" placeholder="Please enter the parameter name" />
        </el-form-item>
        <el-form-item label="Key Name" prop="configKey">
          <el-input v-model="form.configKey" placeholder="Please enter the parameter key name" />
        </el-form-item>
        <el-form-item label="Key value" prop="configValue">
          <el-input v-model="form.configValue" placeholder="Please enter the parameter key value" />
        </el-form-item>
        <el-form-item label="Built-in"  prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
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
import { listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig, clearCache } from "@/services/api/system/config"
import elDragDialog from '@/components/el-drag-dialog'
export default {
  name: "Config",
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
      // Parameter table data
      configList: [],
      // Pop-up layer title
      title: "",
      // Whether to display the pop-up layer
      open: false,
      // Type data dictionary
      typeOptions: [],
      // date range
      dateRange: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined,
      },
      // form parameters
      form: {},
      // form validation
      rules: {
        configName: [{ required: true, message: "Parameter name cannot be empty", trigger: "blur" }],
        configKey: [{ required: true, message: "Parameter key name cannot be empty", trigger: "blur" }],
        configValue: [{ required: true, message: "Parameter key value cannot be empty", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_yes_no").then((response) => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** Query parameter list */
    getList() {
      this.loading = true;
      listConfig(this.addDateRange(this.queryParams, this.dateRange)).then((response) => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // Parameter system built-in dictionary translation
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.configType);
    },
    // Cancel button
    cancel() {
      this.open = false;
      this.reset();
    },
    // form reset
    reset() {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: "Y",
        remark: undefined,
      };
      this.resetForm("form");
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
    /** New button operation */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "Add parameter";
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.configId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset();
      const configId = row.configId || this.ids;
      getConfig(configId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "Modify Parameters";
      });
    },
    /** Submit button */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.configId != undefined) {
            updateConfig(this.form).then((response) => {
              this.msgSuccess("Modified successfully");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then((response) => {
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
      const configIds = row.configId || this.ids;
      this.$confirm('Are you sure to delete the data item with the parameter number "' + configIds + '"?', "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return delConfig(configIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("Delete successfully");
        });
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("Are you sure to export all parameter data items?", "Warning", {
        confirmButtonText: "OK",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(function () {
          return exportConfig(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
    /** Clear cache button operation */
    handleClearCache() {
      clearCache().then((response) => {
        this.msgSuccess("Cleaning up successfully");
      });
    },
  },
};
</script>
