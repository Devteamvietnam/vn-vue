<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="Name" prop="jobName">
        <el-input v-model="queryParams.jobName" placeholder="Please enter the name of the job" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Task" prop="jobGroup">
        <el-select v-model="queryParams.jobGroup" placeholder="Please select the name of the task group" clearable size="small">
          <el-option v-for="dict in jobGroupOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="queryParams.status" placeholder="Please select the task status" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:add']" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">Add</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:edit']" type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:remove']" type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:export']" type="warning" icon="el-icon-download" size="mini" @click="handleExport">Export</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['monitor:job:query']" type="info" icon="el-icon-s-operation" size="mini" @click="handleJobLog">Log</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="jobList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Id" align="center" prop="jobId" />
      <el-table-column label="Name" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="Task" align="center" prop="jobGroup" :formatter="jobGroupFormat" />
      <el-table-column label="Invoke" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="Cron" align="center" prop="cronExpression" :show-overflow-tooltip="true" />
      <el-table-column label="Status" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPermi="['monitor:job:changeStatus']" size="mini" type="text" icon="el-icon-caret-right" @click="handleRun(scope.row)">Execute once</el-button>
          <el-button v-hasPermi="['monitor:job:query']" size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"> Detailed</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- Add or modify timed task dialog box -->
    <el-dialog v-el-drag-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Job name" prop="jobName">
              <el-input v-model="form.jobName" placeholder="Please enter the name of the job" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Task Group" prop="jobGroup">
              <el-select v-model="form.jobGroup" placeholder="Please select">
                <el-option v-for="dict in jobGroupOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                Call method
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean call example: ryTask.ryParams('ry')
                    <br>Class class call example: com.ruoyi.quartz.task.RyTask.ryParams('ry') <br>Parameter description: support string, boolean, long integer, floating point, integer
                  </div>
                  <i class="el-icon-question" />
                </el-tooltip>
              </span>
              <el-input v-model="form.invokeTarget" placeholder="Please enter the call target string" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cron expression" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="Please enter cron expression" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Is it concurrent?" prop="concurrent">
              <el-radio-group v-model="form.concurrent" size="small">
                <el-radio-button label="0">Allow</el-radio-button>
                <el-radio-button label="1">Prohibited</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Wrong policy" prop="misfirePolicy">
              <el-radio-group v-model="form.misfirePolicy" size="small">
                <el-radio-button label="1">Execute now</el-radio-button>
                <el-radio-button label="2">Execute once</el-radio-button>
                <el-radio-button label="3">Abandon execution</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">OK</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>

    <!-- Detailed task log -->
    <el-dialog v-el-drag-dialog title="Task details" :visible.sync="openView" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="ID:">{{ form.jobId }}</el-form-item>
            <el-form-item label="Name:">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Grouping:">{{ jobGroupFormat(form) }}</el-form-item>
            <el-form-item label="Create Time:">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cron:">{{ form.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Validtime:">{{ parseTime(form.nextValidTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Invoke target:">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Status:">
              <div v-if="form.status == 0">Normal</div>
              <div v-else-if="form.status == 1">Failed</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Whether concurrent:">
              <div v-if="form.concurrent == 0">allowed</div>
              <div v-else-if="form.concurrent == 1">prohibited</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Execution Strategy:">
              <div v-if="form.misfirePolicy == 0">default policy</div>
              <div v-else-if="form.misfirePolicy == 1">Execute immediately</div>
              <div v-else-if="form.misfirePolicy == 2">Execute once</div>
              <div v-else-if="form.misfirePolicy == 3">Abandon execution</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">Closed</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listJob, getJob, delJob, addJob, updateJob, exportJob, runJob, changeJobStatus } from '@/api/monitor/job'
import elDragDialog from '@/components/el-drag-dialog'

export default {
  name: 'Job',
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
      // Scheduled task table data
      jobList: [],
      // Pop-up layer title
      title: '',
      // Whether to display the pop-up layer
      open: false,
      // Whether to display the detailed pop-up layer
      openView: false,
      // Task group name dictionary
      jobGroupOptions: [],
      // state dictionary
      statusOptions: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // form parameters
      form: {},
      // form validation
      rules: {
        jobName: [{ required: true, message: 'Task name cannot be empty', trigger: 'blur' }],
        invokeTarget: [{ required: true, message: 'The call target string cannot be empty', trigger: 'blur' }],
        cronExpression: [{ required: true, message: 'cron execution expression cannot be empty', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_job_group').then((response) => {
      this.jobGroupOptions = response.data
    })
    this.getDicts('sys_job_status').then((response) => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** Query the list of scheduled tasks */
    getList() {
      this.loading = true
      listJob(this.queryParams).then((response) => {
        this.jobList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // Dictionary translation of task group name
    jobGroupFormat(row, column) {
      return this.selectDictLabel(this.jobGroupOptions, row.jobGroup)
    },
    // State dictionary translation
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // Cancel button
    cancel() {
      this.open = false
      this.reset()
    },
    // form reset
    reset() {
      this.form = {
        jobId: undefined,
        jobName: undefined,
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: 1,
        concurrent: 1,
        status: '0'
      }
      this.resetForm('form')
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** Reset button operation */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.jobId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // Task status modification
    handleStatusChange(row) {
      const text = row.status === '0' ? 'Enable' : 'Disable'
      this.$confirm('Confirm to "' + text + '""' + row.jobName + '"task?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return changeJobStatus(row.jobId, row.status)
        })
        .then(() => {
          this.msgSuccess(text + 'success')
        })
        .catch(function() {
          row.status = row.status === '0' ? '1' : '0'
        })
    },
    /* Execute once immediately */
    handleRun(row) {
      this.$confirm('Are you sure you want to execute the task immediately "' + row.jobName + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return runJob(row.jobId, row.jobGroup)
        })
        .then(() => {
          this.msgSuccess('Successful execution')
        })
    },
    /** Task details */
    handleView(row) {
      getJob(row.jobId).then((response) => {
        this.form = response.data
        this.openView = true
      })
    },
    /** Task log list query */
    handleJobLog() {
      this.$router.push('/job/log')
    },
    /** New button operation */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = 'Add Task'
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset()
      const jobId = row.jobId || this.ids
      getJob(jobId).then((response) => {
        this.form = response.data
        this.open = true
        this.title = 'Modify Task'
      })
    },
    /** Submit button */
    submitForm: function() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.jobId !== undefined) {
            updateJob(this.form).then((response) => {
              this.msgSuccess('Modified successfully')
              this.open = false
              this.getList()
            })
          } else {
            addJob(this.form).then((response) => {
              this.msgSuccess('Add success')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** Delete button operation */
    handleDelete(row) {
      const jobIds = row.jobId || this.ids
      this.$confirm('Are you sure to delete the data item with the scheduled task number "' + jobIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return delJob(jobIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('Delete successfully')
        })
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('Are you sure to export all timed task data items?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return exportJob(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
    }
  }
}
</script>
