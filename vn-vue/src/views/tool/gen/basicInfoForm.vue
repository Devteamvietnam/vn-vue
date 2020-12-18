<template>
  <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="table name" prop="tableName">
          <el-input v-model="info.tableName" placeholder="Please enter the warehouse name" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="table description" prop="tableComment">
          <el-input v-model="info.tableComment" placeholder="Please enter" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="Entity class name" prop="className">
          <el-input v-model="info.className" placeholder="Please enter" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Author" prop="functionAuthor">
          <el-input v-model="info.functionAuthor" placeholder="Please enter" />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="Remarks" prop="remark">
          <el-input v-model="info.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
export default {
  name: 'BasicInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      rules: {
        tableName: [
          { required: true, message: 'Please enter the table name', trigger: 'blur' }
        ],
        tableComment: [
          { required: true, message: 'Please enter a table description', trigger: 'blur' }
        ],
        className: [
          { required: true, message: 'Please enter the entity class name', trigger: 'blur' }
        ],
        functionAuthor: [
          { required: true, message: 'Please enter the author', trigger: 'blur' }
        ]
      }
    }
  }
}
</script>
