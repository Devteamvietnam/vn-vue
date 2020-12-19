<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true">
      <el-form-item label="menu name" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          placeholder="Please enter the name of the menu"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select v-model="queryParams.status" placeholder="menu status" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
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
          v-hasPermi="['system:menu:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >New</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :tree-props="{children:'children', hasChildren:'hasChildren'}"
    >
      <el-table-column prop="menuName" label="Name" :show-overflow-tooltip="true" width="160" />
      <el-table-column prop="icon" label="Icon" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="Sort" width="60" />
      <el-table-column prop="perms" label="Authority identification" :show-overflow-tooltip="true" />
      <el-table-column prop="component" label="Component path" :show-overflow-tooltip="true" />
      <el-table-column prop="status" label="Status" :formatter="statusFormat" width="80" />
      <el-table-column label="Create time" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:menu:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >Edit</el-button>
          <el-button
            v-hasPermi="['system:menu:add']"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >New</el-button>
          <el-button
            v-hasPermi="['system:menu:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Add or modify menu dialog box -->
    <el-dialog v-el-drag-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="Upper menu">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="Select the upper menu"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Menu Type" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">Catalog</el-radio>
                <el-radio label="C">Menu</el-radio>
                <el-radio label="F">Button</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType !='F'" label="menu icon">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.icon" placeholder="click to select icon" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px;width: 16px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="menu name" prop="menuName">
              <el-input v-model="form.menuName" placeholder="Please enter the menu name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Display order" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !='F'" label="Whether it is outside the chain">
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">Yes</el-radio>
                <el-radio label="1">No</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !='F'" label="route address" prop="path">
              <el-input v-model="form.path" placeholder="Please enter the routing address" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType =='C'" :span="12">
            <el-form-item label="Component path" prop="component">
              <el-input v-model="form.component" placeholder="Please enter the component path" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !='M'" label="Permission ID">
              <el-input v-model="form.perms" placeholder="Please authorize identification" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !='F'" label="Display status">
              <el-radio-group v-model="form.visible">
                <el-radio
                  v-for="dict in visibleOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !='F'" label="Menu Status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType =='C'" label="Cache or not">
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">Cache</el-radio>
                <el-radio label="1">Do not cache</el-radio>
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
  </div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu } from '@/api/system/menu'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect'
import elDragDialog from '@/components/el-drag-dialog'

export default {
  name: 'Menu',
  components: { Treeselect, IconSelect },
  directives: { elDragDialog },
  data() {
    return {
      // Mask layer
      loading: true,
      // Show search criteria
      showSearch: false,
      // Menu table tree data
      menuList: [],
      // Menu tree options
      menuOptions: [],
      // Pop-up layer title
      title: '',
      // Whether to display the pop-up layer
      open: false,
      // Display status data dictionary
      visibleOptions: [],
      // Menu status data dictionary
      statusOptions: [],
      // query parameters
      queryParams: {
        menuName: undefined,
        visible: undefined
      },
      // form parameters
      form: {},
      // form validation
      rules: {
        menuName: [
          { required: true, message: 'Menu name cannot be empty', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: 'Menu order cannot be empty', trigger: 'blur' }
        ],
        path: [
          { required: true, message: 'Routing address cannot be empty', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_show_hide').then(response => {
      this.visibleOptions = response.data
    })
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    // select icon
    selected(name) {
      this.form.icon = name
    },
    /** Query menu list */
    getList() {
      this.loading = true
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, 'menuId')
        this.loading = false
      })
    },
    /** Convert menu data structure */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    /** Query menu drop-down tree structure */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = []
        const menu = { menuId: 0, menuName: 'Main category', children: [] }
        menu.children = this.handleTree(response.data, 'menuId')
        this.menuOptions.push(menu)
      })
    },
    // Show status dictionary translation
    visibleFormat(row, column) {
      if (row.menuType === 'F') {
        return ''
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    // Menu status dictionary translation
    statusFormat(row, column) {
      if (row.menuType === 'F') {
        return ''
      }
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
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      }
      this.resetForm('form')
    },
    /** Search button operation */
    handleQuery() {
      this.getList()
    },
    /** Reset button operation */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** New button operation */
    handleAdd(row) {
      this.reset()
      this.getTreeselect()
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.title = 'Add Menu'
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      getMenu(row.menuId).then(response => {
        this.form = response.data
        this.open = true
        this.title = 'Modify Menu'
      })
    },
    /** Submit button */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.menuId !== undefined) {
            updateMenu(this.form).then(response => {
              this.msgSuccess('Modified successfully')
              this.open = false
              this.getList()
            })
          } else {
            addMenu(this.form).then(response => {
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
      this.$confirm('Are you sure to delete the data item named "' + row.menuName + '"?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(function() {
        return delMenu(row.menuId)
      }).then(() => {
        this.getList()
        this.msgSuccess('Delete successfully')
      })
    }
  }
}
</script>
