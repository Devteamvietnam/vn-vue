<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="Dictionary name" prop="dictName">
        <el-input
          v-model="queryParams.dictName"
          placeholder="Please enter the name of the dictionary"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Dictionary Type" prop="dictType">
        <el-input
          v-model="queryParams.dictType"
          placeholder="Please enter the dictionary type"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="Dictionary status"
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
      <el-form-item label="Created time">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="start date"
          end-placeholder="end date"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >New</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:edit']"
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:remove']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:export']"
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >Export</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:remove']"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          @click="handleClearCache"
        >Clean up cache</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Dictionary Number" align="center" prop="dictId" />
      <el-table-column label="Dictionary name" align="center" prop="dictName" :show-overflow-tooltip="true" />
      <el-table-column label="Dictionary type" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/dict/type/data/' + scope.row.dictId" class="link-type">
            <span>{{ scope.row.dictType }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="status" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="remarks" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="create time" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:dict:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >Edit</el-button>
          <el-button
            v-hasPermi="['system:dict:remove']"
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

    <!-- Add or modify parameter configuration dialog box -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="Dictionary name" prop="dictName">
          <el-input v-model="form.dictName" placeholder="Please enter the name of the dictionary" />
        </el-form-item>
        <el-form-item label="Dictionary Type" prop="dictType">
          <el-input v-model="form.dictType" placeholder="Please enter the dictionary type" />
        </el-form-item>
        <el-form-item label="status" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Remarks" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="Please enter content" />
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
import { listType, getType, delType, addType, updateType, exportType, clearCache } from '@/api/system/dict/type'

export default {
  name: 'Dict',
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
      // Dictionary table data
      typeList: [],
      // Pop-up layer title
      title: '',
      // Whether to display the pop-up layer
      open: false,
      // State data dictionary
      statusOptions: [],
      // date range
      dateRange: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      // form parameters
      form: {},
      // form validation
      rules: {
        dictName: [
          { required: true, message: 'The dictionary name cannot be empty', trigger: 'blur' }
        ],
        dictType: [
          { required: true, message: 'The dictionary type cannot be empty', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** Query the list of dictionary types */
    getList() {
      this.loading = true
      listType(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.typeList = response.rows
        this.total = response.total
        this.loading = false
      }
      )
    },
    // Dictionary status dictionary translation
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
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: '0',
        remark: undefined
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
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** New button operation */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = 'Add dictionary type'
    },
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset()
      const dictId = row.dictId || this.ids
      getType(dictId).then(response => {
        this.form = response.data
        this.open = true
        this.title = 'Modify Dictionary Type'
      })
    },
    /** Submit button */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.dictId !== undefined) {
            updateType(this.form).then(response => {
              this.msgSuccess('Modified successfully')
              this.open = false
              this.getList()
            })
          } else {
            addType(this.form).then(response => {
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
      const dictIds = row.dictId || this.ids
      this.$confirm('Are you sure to delete the data item with the dictionary number "' + dictIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return delType(dictIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('Delete successfully')
      })
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('Are you sure to export all types of data items?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return exportType(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    },
    /** Clear cache button operation */
    handleClearCache() {
      clearCache().then(response => {
        this.msgSuccess('Cleaning up successfully')
      })
    }
  }
}
</script>
