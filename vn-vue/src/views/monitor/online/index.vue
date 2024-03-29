<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px">
      <el-form-item label="Address" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          placeholder="Please enter the login address"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="User" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="Please enter the user name"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>

    </el-form>
    <el-table
      v-loading="loading"
      :data="list.slice((pageNum-1)*pageSize,pageNum*pageSize)"
      style="width: 100%;"
    >
      <el-table-column label="id" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ (pageNum-1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Session" align="center" prop="tokenId" :show-overflow-tooltip="true" />
      <el-table-column label="Login name" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="Dept name" align="center" prop="deptName" />
      <el-table-column label="Host" align="center" prop="ipaddr" :show-overflow-tooltip="true" />
      <el-table-column label="Login Location" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
      <el-table-column label="Browser" align="center" prop="browser" />
      <el-table-column label="OS" align="center" prop="os" />
      <el-table-column label="Login time" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['monitor:online:forceLogout']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
          >Forced back</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />
  </div>
</template>

<script>
import { list, forceLogout } from '@/api/monitor/online'

export default {
  name: 'Online',
  data() {
    return {
      // Mask layer
      loading: true,
      // Total number
      total: 0,
      // table data
      list: [],
      pageNum: 1,
      pageSize: 10,
      // query parameters
      queryParams: {
        ipaddr: undefined,
        userName: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** Query login log list */
    getList() {
      this.loading = true
      list(this.queryParams).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** Search button operation */
    handleQuery() {
      this.pageNum = 1
      this.getList()
    },
    /** Reset button operation */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** Force back button operation */
    handleForceLogout(row) {
      this.$confirm('Are you sure to force back the data item whose name is "' + row.userName + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return forceLogout(row.tokenId)
      }).then(() => {
        this.getList()
        this.msgSuccess('Forced to retreat successfully')
      })
    }
  }
}
</script>

