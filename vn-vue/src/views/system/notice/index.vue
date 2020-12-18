<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="Announcement Title" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="Please enter the title of the announcement"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="operator" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="Please enter the operator"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="type" prop="noticeType">
        <el-select v-model="queryParams.noticeType" placeholder="Announcement Type" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:notice:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >New</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:notice:edit']"
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:notice:remove']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >Delete</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="serial number" align="center" prop="noticeId" width="100" />
      <el-table-column
        label="Announcement Title"
        align="center"
        prop="noticeTitle"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="Announcement Type"
        align="center"
        prop="noticeType"
        :formatter="typeFormat"
        width="100"
      />
      <el-table-column
        label="Status"
        align="center"
        prop="status"
        :formatter="statusFormat"
        width="100"
      />
      <el-table-column label="creator" align="center" prop="createBy" width="100" />
      <el-table-column label="create time" align="center" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime,'{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:notice:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >Edit</el-button>
          <el-button
            v-hasPermi="['system:notice:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- Add or modify announcement dialog box -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Announcement Title" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="Please enter the title of the announcement" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Notice Type" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="Please select">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Content">
              <editor v-model="form.noticeContent" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">OK</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotice, getNotice, delNotice, addNotice, updateNotice, exportNotice } from '@/api/system/notice'
import Editor from '@/components/Editor'

export default {
  name: 'Notice',
  components: {
    Editor
  },
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
      showSearch: true,
      // Total number
      total: 0,
      // Announcement form data
      noticeList: [],
      // Pop-up layer title
      title: '',
      // Whether to display the pop-up layer
      open: false,
      // Type data dictionary
      statusOptions: [],
      // State data dictionary
      typeOptions: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      // form parameters
      form: {},
      // form validation
      rules: {
        noticeTitle: [
          { required: true, message: 'Announcement title cannot be empty', trigger: 'blur' }
        ],
        noticeType: [
          { required: true, message: 'Announcement type cannot be empty', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_notice_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_notice_type').then(response => {
      this.typeOptions = response.data
    })
  },
  methods: {
    /** Query announcement list */
    getList() {
      this.loading = true
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // Announcement status dictionary translation
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // Announcement status dictionary translation
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.noticeType)
    },
    // Cancel button
    cancel() {
      this.open = false
      this.reset()
    },
    // form reset
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
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
      this.ids = selection.map(item => item.noticeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** New button operation */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = 'Add Announcement'
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset()
      const noticeId = row.noticeId || this.ids
      getNotice(noticeId).then(response => {
        this.form = response.data
        this.open = true
        this.title = 'Modification Notice'
      })
    },
    /** Submit button */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.noticeId !== undefined) {
            updateNotice(this.form).then(response => {
              this.msgSuccess('Modified successfully')
              this.open = false
              this.getList()
            })
          } else {
            addNotice(this.form).then(response => {
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
      const noticeIds = row.noticeId || this.ids
      this.$confirm('Are you sure to delete the data item with the notice number "' + noticeIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return delNotice(noticeIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('Delete successfully')
      })
    }
  }
}
</script>
