<template>
  <!-- Import table -->
  <el-dialog title="Import table" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true">
      <el-form-item label="table name" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="Please enter the table name"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="table description" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          placeholder="Please enter a table description"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table ref="table" :data="dbTableList" height="260px" @row-click="clickRow" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="tableName" label="table name" :show-overflow-tooltip="true" />
        <el-table-column prop="tableComment" label="table description" :show-overflow-tooltip="true" />
        <el-table-column prop="createTime" label="create time" />
        <el-table-column prop="updateTime" label="update time" />
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">OK</el-button>
      <el-button @click="visible = false">Cancel</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listDbTable, importTable } from '@/api/tool/gen'
export default {
  data() {
    return {
      // Mask layer
      visible: false,
      // select the array value
      tables: [],
      // Total number
      total: 0,
      // table data
      dbTableList: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      }
    }
  },
  methods: {
    // Show the bullet box
    show() {
      this.getList()
      this.visible = true
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.tables = selection.map(item => item.tableName)
    },
    // Query table data
    getList() {
      listDbTable(this.queryParams).then(res => {
        if (res.code === 200) {
          this.dbTableList = res.rows
          this.total = res.total
        }
      })
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
    /** Import button operation */
    handleImportTable() {
      importTable({ tables: this.tables.join(',') }).then(res => {
        this.msgSuccess(res.msg)
        if (res.code === 200) {
          this.visible = false
          this.$emit('ok')
        }
      })
    }
  }
}
</script>
