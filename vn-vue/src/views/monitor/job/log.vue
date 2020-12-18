<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="Name" prop="jobName">
        <el-input v-model="queryParams.jobName" placeholder="Please enter the task name" clearable size="small" style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Task" prop="jobGroup">
        <el-select v-model="queryParams.jobGroup" placeholder="Please name of task group" clearable size="small" style="width: 240px">
          <el-option v-for="dict in jobGroupOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="queryParams.status" placeholder="Please select the execution status" clearable size="small" style="width: 240px">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="Time">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="start date" end-placeholder="end date" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:remove']" type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:remove']" type="danger" icon="el-icon-delete" size="mini" @click="handleClean">Empty</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:export']" type="warning" icon="el-icon-download" size="mini" @click="handleExport">Export</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="jobLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Log" width="80" align="center" prop="jobLogId" />
      <el-table-column label="Name" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="Task" align="center" prop="jobGroup" :formatter="jobGroupFormat" :show-overflow-tooltip="true" />
      <el-table-column label="Invoke" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="Information" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
      <el-table-column label="Status" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="Time" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPermi="['monitor:job:query']" size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">Detailed</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- Detailed schedule log -->
    <el-dialog v-el-drag-dialog title="Schedule log details" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="LogId:">{{ form.jobLogId }}</el-form-item>
            <el-form-item label="Task name:">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Task:">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="Execution time:">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Invoke:">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Message:">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Status:">
              <div v-if="form.status == 0">Normal</div>
              <div v-else-if="form.status == 1">Failed</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.status == 1" label="Status:">{{ form.exceptionInfo }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">Closed</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listJobLog, delJobLog, exportJobLog, cleanJobLog } from '@/api/monitor/jobLog'
import elDragDialog from '@/components/el-drag-dialog'
export default {
  name: 'JobLog',
  directives: { elDragDialog },
  data() {
    return {
      // Mask layer
      loading: true,
      // select the array
      ids: [],
      // not multiple disabled
      multiple: true,
      // Show search criteria
      showSearch: true,
      // Total number
      total: 0,
      // Schedule log table data
      jobLogList: [],
      // Whether to display the pop-up layer
      open: false,
      // date range
      dateRange: [],
      // form parameters
      form: {},
      // Execution status dictionary
      statusOptions: [],
      // Task group name dictionary
      jobGroupOptions: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_job_status').then((response) => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_job_group').then((response) => {
      this.jobGroupOptions = response.data
    })
  },
  methods: {
    /** Query scheduling log list */
    getList() {
      this.loading = true
      listJobLog(this.addDateRange(this.queryParams, this.dateRange)).then((response) => {
        this.jobLogList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // Perform status dictionary translation
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // Dictionary translation of task group name
    jobGroupFormat(row, column) {
      return this.selectDictLabel(this.jobGroupOptions, row.jobGroup)
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** Reset button operation */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.jobLogId)
      this.multiple = !selection.length
    },
    /** Detailed button operation */
    handleView(row) {
      this.open = true
      this.form = row
    },
    /** Delete button operation */
    handleDelete(row) {
      const jobLogIds = this.ids
      this.$confirm('Are you sure to delete the data item whose schedule log number is "' + jobLogIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return delJobLog(jobLogIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('Delete successfully')
        })
    },
    /** Clear button operation */
    handleClean() {
      this.$confirm('Are you sure to clear all schedule log data items?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return cleanJobLog()
        })
        .then(() => {
          this.getList()
          this.msgSuccess('Empty successfully')
        })
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('Are you sure to export all scheduling log data items?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return exportJobLog(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
    }
  }
}
</script>
