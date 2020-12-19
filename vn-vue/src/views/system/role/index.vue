<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true">
      <el-form-item label="role name" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="Please enter the role name"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Authorization character" prop="roleKey">
        <el-input
          v-model="queryParams.roleKey"
          placeholder="Please enter permission characters"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="role status"
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
          v-hasPermi="['system:role:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >New</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:role:edit']"
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:role:remove']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:role:export']"
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >Export</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="100" align="center" />
      <el-table-column label="Name" prop="roleName" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="Permission" prop="roleKey" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="Sort" prop="roleSort" width="150" />
      <el-table-column label="Status" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="Create time" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            v-hasPermi="['system:role:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >Edit</el-button> -->
          <el-button
            v-hasPermi="['system:role:edit']"
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleDataScope(scope.row)"
          >Data permission</el-button>
          <!-- <el-button
            v-hasPermi="['system:role:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >Delete</el-button> -->
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

    <!-- Add or modify role configuration dialog box -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="role name" prop="roleName">
          <el-input v-model="form.roleName" placeholder="Please enter the role name" />
        </el-form-item>
        <el-form-item label="Authorization character" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="Please enter the authority characters" />
        </el-form-item>
        <el-form-item label="role order" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="Status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Menu permissions">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event,'menu')">Expand/collapse</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event,'menu')">Select all/Unselect all</el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event,'menu')">Father-son linkage</el-checkbox>
          <el-tree
            ref="menu"
            class="tree-border"
            :data="menuOptions"
            show-checkbox
            node-key="id"
            :check-strictly="!form.menuCheckStrictly"
            empty-text="Loading, please wait"
            :props="defaultProps"
          />
        </el-form-item>
        <el-form-item label="Remarks">
          <el-input v-model="form.remark" type="textarea" placeholder="Please enter content" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">OK</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>

    <!-- Assign role data permissions dialog box -->
    <el-dialog v-el-drag-dialog :title="title" :visible.sync="openDataScope" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="Name">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="Permission">
          <el-input v-model="form.roleKey" :disabled="true" />
        </el-form-item>
        <el-form-item label="Scope">
          <el-select v-model="form.dataScope">
            <el-option
              v-for="item in dataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-show="form.dataScope == 2" label="Data Permission">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event,'dept')">Expand/Collapse</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event,'dept')">Select all/Unselect all</el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event,'dept')">Father-son linkage</el-checkbox>
          <el-tree
            ref="dept"
            class="tree-border"
            :data="deptOptions"
            show-checkbox
            default-expand-all
            node-key="id"
            :check-strictly="!form.deptCheckStrictly"
            empty-text="Loading, please wait"
            :props="defaultProps"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">OK</el-button>
        <el-button @click="cancelDataScope">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from '@/api/system/role'
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu'
import { treeselect as deptTreeselect, roleDeptTreeselect } from '@/api/system/dept'
import elDragDialog from '@/components/el-drag-dialog'

export default {
  name: 'Role',
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
      // role table data
      roleList: [],
      // Pop-up layer title
      title: '',
      // Whether to display the pop-up layer
      open: false,
      // Whether to display the pop-up layer (data permission)
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // date range
      dateRange: [],
      // State data dictionary
      statusOptions: [],
      // Data range options
      dataScopeOptions: [
        {
          value: '1',
          label: 'All data permissions'
        },
        {
          value: '2',
          label: 'Custom data permissions'
        },
        {
          value: '3',
          label: 'Data permission of this department'
        },
        {
          value: '4',
          label: 'Data authority of this department and below'
        },
        {
          value: '5',
          label: 'Only personal data permission'
        }
      ],
      // Menu list
      menuOptions: [],
      // Department list
      deptOptions: [],
      // query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        roleKey: undefined,
        status: undefined
      },
      // form parameters
      form: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // form validation
      rules: {
        roleName: [
          { required: true, message: 'The role name cannot be empty', trigger: 'blur' }
        ],
        roleKey: [
          { required: true, message: 'Permission character cannot be empty', trigger: 'blur' }
        ],
        roleSort: [
          { required: true, message: 'The role sequence cannot be empty', trigger: 'blur' }
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
    /** Query role list */
    getList() {
      this.loading = true
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.roleList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    /** Query menu tree structure */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data
      })
    },
    /** Query department tree structure */
    getDeptTreeselect() {
      deptTreeselect().then(response => {
        this.deptOptions = response.data
      })
    },
    // All menu node data
    getMenuAllCheckedKeys() {
      // currently selected menu node
      const checkedKeys = this.$refs.menu.getCheckedKeys()
      // Half-selected menu node
      const halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    // All department node data
    getDeptAllCheckedKeys() {
      // currently selected department node
      const checkedKeys = this.$refs.dept.getCheckedKeys()
      // Half-selected department node
      const halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    /** Query the menu tree structure according to the role ID */
    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus
        return response
      })
    },
    /** Query the department tree structure according to the role ID */
    getRoleDeptTreeselect(roleId) {
      return roleDeptTreeselect(roleId).then(response => {
        this.deptOptions = response.depts
        return response
      })
    },
    // role status modification
    handleStatusChange(row) {
      const text = row.status === '0' ? 'Enable' : 'Disable'
      this.$confirm('Confirm to "' + text + '""' + row.roleName + '"role?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return changeRoleStatus(row.roleId, row.status)
      }).then(() => {
        this.msgSuccess(text + 'success')
      }).catch(function() {
        row.status = row.status === '0' ? '1' : '0'
      })
    },
    // Cancel button
    cancel() {
      this.open = false
      this.reset()
    },
    // Cancel button (data permission)
    cancelDataScope() {
      this.openDataScope = false
      this.reset()
    },
    // form reset
    reset() {
      if (this.$refs.menu !== undefined) {
        this.$refs.menu.setCheckedKeys([])
      }
      this.menuExpand = false
      this.menuNodeAll = false
      this.deptExpand = true
      this.deptNodeAll = false
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: '0',
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true,
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
    // Multiple selection box to select data
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // Tree permissions (expand/collapse)
    handleCheckedTreeExpand(value, type) {
      if (type === 'menu') {
        const treeList = this.menuOptions
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value
        }
      } else if (type === 'dept') {
        const treeList = this.deptOptions
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value
        }
      }
    },
    // Tree permissions (select all/unselect all)
    handleCheckedTreeNodeAll(value, type) {
      if (type === 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : [])
      } else if (type === 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions : [])
      }
    },
    // Tree permissions (father-child linkage)
    handleCheckedTreeConnect(value, type) {
      if (type === 'menu') {
        this.form.menuCheckStrictly = !!value
      } else if (type === 'dept') {
        this.form.deptCheckStrictly = !!value
      }
    },
    /** New button operation */
    handleAdd() {
      this.reset()
      this.getMenuTreeselect()
      this.open = true
      this.title = 'Add Role'
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset()
      const roleId = row.roleId || this.ids
      const roleMenu = this.getRoleMenuTreeselect(roleId)
      getRole(roleId).then(response => {
        this.form = response.data
        this.open = true
        this.$nextTick(() => {
          roleMenu.then(res => {
            this.$refs.menu.setCheckedKeys(res.checkedKeys)
          })
        })
        this.title = 'Modify Role'
      })
    },
    /** Assign data permission operation */
    handleDataScope(row) {
      this.reset()
      const roleDeptTreeselect = this.getRoleDeptTreeselect(row.roleId)
      getRole(row.roleId).then(response => {
        this.form = response.data
        this.openDataScope = true
        this.$nextTick(() => {
          roleDeptTreeselect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys)
          })
        })
        this.title = 'Assign Data Permission'
      })
    },
    /** Submit button */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.roleId !== undefined) {
            this.form.menuIds = this.getMenuAllCheckedKeys()
            updateRole(this.form).then(response => {
              this.msgSuccess('Modified successfully')
              this.open = false
              this.getList()
            })
          } else {
            this.form.menuIds = this.getMenuAllCheckedKeys()
            addRole(this.form).then(response => {
              this.msgSuccess('Add success')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** Submit button (data permission) */
    submitDataScope: function() {
      if (this.form.roleId !== undefined) {
        this.form.deptIds = this.getDeptAllCheckedKeys()
        dataScope(this.form).then(response => {
          this.msgSuccess('Modified successfully')
          this.openDataScope = false
          this.getList()
        })
      }
    },
    /** Delete button operation */
    handleDelete(row) {
      const roleIds = row.roleId || this.ids
      this.$confirm('Are you sure to delete the data item with the role number "' + roleIds + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return delRole(roleIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('Delete successfully')
      })
    },
    /** Export button operation */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('Are you sure to export all role data items?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return exportRole(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    }
  }
}
</script>
