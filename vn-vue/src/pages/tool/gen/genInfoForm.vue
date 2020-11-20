<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">Generate template</span>
          <el-select v-model="info.tplCategory">
            <el-option label="Single table (addition, deletion, modification, check)" value="crud" />
            <el-option label="Tree table (addition, deletion, modification, check)" value="tree" />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            Generate package path
            <el-tooltip content="Under which java package is generated, for example, com.ruoyi.system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            Generate module name
            <el-tooltip content=" can be understood as the name of the subsystem, such as system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            Generate business name
            <el-tooltip content="can be understood as the English name of the function, such as user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            Generate function name
            <el-tooltip content="Used as a class description, such as user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            Upper menu
            <el-tooltip content="Assign to the designated menu, such as system management" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <treeselect :append-to-body="true" v-model="info.parentMenuId" :options="menus" :normalizer="normalizer" :show-count="true" placeholder="Please select the system menu" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="genType">
          <span slot="label">
            Code generation method
            <el-tooltip content="The default is zip compression package download, you can also customize the generation path" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio v-model="info.genType" label="0">zip compressed package</el-radio>
          <el-radio v-model="info.genType" label="1">Customized path</el-radio>
        </el-form-item>
      </el-col>

      <el-col :span="24" v-if="info.genType == '1'">
        <el-form-item prop="genPath">
          <span slot="label">
            Custom path
            <el-tooltip content="Fill in the absolute path of the disk. If not, it will be generated under the current Web project" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.genPath">
            <el-dropdown slot="append">
              <el-button type="primary">
                Quick selection of nearest path
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="info.genPath = '/'">Restore the default generation base path</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="info.tplCategory == 'tree'">
      <h4 class="form-header">Other information</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            Tree encoding field
            <el-tooltip content="The name of the coding field displayed in the tree, such as: dept_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" placeholder="Please select">
            <el-option v-for="column in info.columns" :key="column.columnName" :label="column.columnName + ':' + column.columnComment" :value="column.columnName"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            Tree parent encoding field
            <el-tooltip content="The name of the parent code field displayed in the tree, such as: parent_Id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" placeholder="Please select">
            <el-option v-for="column in info.columns" :key="column.columnName" :label="column.columnName + ':' + column.columnComment" :value="column.columnName"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            Tree name field
            <el-tooltip content="The display name field name of the tree node, such as: dept_name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" placeholder="Please select">
            <el-option v-for="column in info.columns" :key="column.columnName" :label="column.columnName + ':' + column.columnComment" :value="column.columnName"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "BasicInfoForm",
  components: { Treeselect },
  props: {
    info: {
      type: Object,
      default: null,
    },
    menus: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      rules: {
        tplCategory: [{ required: true, message: "Please select a template to generate", trigger: "blur" }],
        packageName: [{ required: true, message: "Please enter the path of the generated package", trigger: "blur" }],
        moduleName: [{ required: true, message: "Please enter the name of the generated module", trigger: "blur" }],
        businessName: [{ required: true, message: "Please enter the generated business name", trigger: "blur" }],
        functionName: [{ required: true, message: "Please enter the name of the generated function", trigger: "blur" }],
      },
    };
  },
  created() {},
  methods: {
    /** Convert menu data structure */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children,
      };
    },
  },
};
</script>
