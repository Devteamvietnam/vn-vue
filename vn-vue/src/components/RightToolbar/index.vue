<!-- @author Jihoon -->
<template>
  <div class="top-right-btn">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch?'Hide search':'Show search'" placement="top">
        <el-button size="mini" circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="refresh" placement="top">
        <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
      </el-tooltip>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'RightToolbar',
  props: {
    showSearch: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {}
  },

  methods: {
    // search for
    toggleSearch() {
      this.$emit('update:showSearch', !this.showSearch)
    },
    // refresh
    refresh() {
      this.$emit('queryTable')
    }
  }
}
</script>
