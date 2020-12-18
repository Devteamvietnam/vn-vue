<template>
  <div class="component-upload-image">
    <el-upload
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :on-error="handleUploadError"
      name="file"
      :show-file-list="false"
      :headers="headers"
      style="display: inline-block; vertical-align: top"
    >
      <img v-if="value" :src="value" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon" />
    </el-upload>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  components: {},
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      uploadImgUrl: process.env.VUE_APP_BASE_API + '/common/upload', // Uploaded image server address
      headers: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  watch: {},
  methods: {
    handleUploadSuccess(res) {
      this.$emit('input', res.url)
      this.loading.close()
    },
    handleBeforeUpload() {
      this.loading = this.$loading({
        lock: true,
        text: 'Uploading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    handleUploadError() {
      this.$message({
        type: 'error',
        message: 'Upload failed'
      })
      this.loading.close()
    }
  }
}
</script>

<style scoped lang="scss">
.avatar {
  width: 100%;
  height: 100%;
}
</style>
