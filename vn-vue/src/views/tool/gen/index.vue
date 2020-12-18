<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item label="Name" prop="tableName">
        <el-input v-model="queryParams.tableName" placeholder="Please enter the table name" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Description" prop="tableComment">
        <el-input v-model="queryParams.tableComment" placeholder="Please enter a table description" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Created time">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="start date" end-placeholder="end date" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:code']" type="primary" icon="el-icon-download" size="mini" @click="handleGenTable">Generate</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:import']" type="info" icon="el-icon-upload" size="mini" @click="openImportTable">Import</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:edit']" type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleEditTable">Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:remove']" type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">Delete</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="Id" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum-1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" align="center" prop="tableName" :show-overflow-tooltip="true" width="130" />
      <el-table-column label="Description" align="center" prop="tableComment" :show-overflow-tooltip="true" width="130" />
      <el-table-column label="Entity" align="center" prop="className" :show-overflow-tooltip="true" width="130" />
      <el-table-column label="Create time" align="center" prop="createTime" width="160" />
      <el-table-column label="Update time" align="center" prop="updateTime" width="160" />
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPermi="['tool:gen:preview']" type="text" size="small" icon="el-icon-view" @click="handlePreview(scope.row)"> Preview</el-button>
          <el-button v-hasPermi="['tool:gen:edit']" type="text" size="small" icon="el-icon-edit" @click="handleEditTable(scope.row)"> Edit</el-button>
          <el-button v-hasPermi="['tool:gen:remove']" type="text" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)"> Delete</el-button>
          <el-button v-hasPermi="['tool:gen:edit']" type="text" size="small" icon="el-icon-refresh" @click="handleSynchDb(scope.row)"> Sync</el-button>
          <el-button v-hasPermi="['tool:gen:code']" type="text" size="small" icon="el-icon-download" @click="handleGenTable(scope.row)"> Generate code</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total> 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <!-- Preview interface -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(value, key) in preview.data" :key="key" :label="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))" :name="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))">
          <pre>{{ value }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table ref="import" @ok="handleQuery" />
  </div>
</template>

<script>
import { listTable, previewTable, delTable, genCode, synchDb } from '@/api/tool/gen'
import importTable from './importTable'
import { downLoadZip } from '@/utils/zipdownload'
export default {
  name: 'Gen',
  components: { importTable },
  data() {
    return {
      // Mask layer
      loading: true,
      // unique identifier
      uniqueId: '',
      // select the array
      ids: [],
      // selected table array
      tableNames: [],
      // not individually disabled
      single: true,
      // not multiple disabled
      multiple: true,
      // Show search criteria
      showSearch: true,
      // Total number
      total: 0,
      // table data
      tableList: [],
      // date range
      dateRange: '',
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      },
      // Preview parameters
      preview: {
        open: false,
        title: 'Code Preview',
        data: {},
        activeName: 'domain.java'
      }
    }
  },
  created() {
    this.getList()
  },
  activated() {
    const time = this.$route.query.t
    if (time != null && time != this.uniqueId) {
      this.uniqueId = time
      this.resetQuery()
    }
  },
  methods: {
    /** Query table collection */
    getList() {
      this.loading = true
      listTable(this.addDateRange(this.queryParams, this.dateRange)).then((response) => {
        this.tableList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** Generate code operation */
    handleGenTable(row) {
      const tableNames = row.tableName || this.tableNames
      if (tableNames == '') {
        this.msgError('Please select the data to be generated')
        return
      }
      if (row.genType === '1') {
        genCode(row.tableName).then((response) => {
          this.msgSuccess('Successfully generated to custom path:' + row.genPath)
        })
      } else {
        downLoadZip('/tool/gen/batchGenCode?tables=' + tableNames, 'ruoyi')
      }
    },
    /** Synchronize database operations */
    handleSynchDb(row) {
      const tableName = row.tableName
      this.$confirm('Are you sure you want to force synchronization "' + tableName + '"table structure?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return synchDb(tableName)
        })
        .then(() => {
          this.msgSuccess('Sync success')
        })
    },
    /** Open the import table popup */
    openImportTable() {
      this.$refs.import.show()
    },
    /** Reset button operation */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** Preview button */
    handlePreview(row) {
      previewTable(row.tableId).then((response) => {
        this.preview.data = response.data
        this.preview.open = true
      })
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.tableId)
      this.tableNames = selection.map((item) => item.tableName)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** Modify button operation */
    handleEditTable(row) {
      const tableId = row.tableId || this.ids[0]
      this.$router.push('/gen/edit/' + tableId)
    },
    /** Delete button operation */
    handleDelete(row) {
      const tableIds = row.tableId || this.ids
      this.$confirm('Are you sure to delete the data item whose table number is "' + tableIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(function() {
          return delTable(tableIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('Delete successfully')
        })
    }
  }
}
</script>
