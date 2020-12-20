<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>Basic information</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td><div class="cell">Redis version</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.redis_version }}</div></td>
                  <td><div class="cell">operating mode</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.redis_mode == "standalone"? "standalone": "cluster" }}</div></td>
                  <td><div class="cell">Port</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.tcp_port }}</div></td>
                  <td><div class="cell">Number of clients</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.connected_clients }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">Run time (days)</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.uptime_in_days }}</div></td>
                  <td><div class="cell">Using memory</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.used_memory_human }}</div></td>
                  <td><div class="cell">Use CPU</div></td>
                  <td><div v-if="cache.info" class="cell">{{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}</div></td>
                  <td><div class="cell">Memory configuration</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.maxmemory_human }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">Whether AOF is turned on</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.aof_enabled == "0"? "No": "Yes" }}</div></td>
                  <td><div class="cell">Whether the RDB is successful</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.rdb_last_bgsave_status }}</div></td>
                  <td><div class="cell">Key quantity</div></td>
                  <td><div v-if="cache.dbSize" class="cell">{{ cache.dbSize }} </div></td>
                  <td><div class="cell">Network entrance/exit</div></td>
                  <td><div v-if="cache.info" class="cell">{{ cache.info.instantaneous_input_kbps }}kps/{{ cache.info.instantaneous_output_kbps }}kps</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header"><span>Command statistics</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="commandstats" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>Memory information</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="usedmemory" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCache } from '@/api/monitor/cache'
import echarts from 'echarts'

export default {
  name: 'Server',
  data() {
    return {
      // Load layer information
      loading: [],
      // Statistics command information
      commandstats: null,
      // Use memory
      usedmemory: null,
      // cache information
      cache: []
    }
  },
  created() {
    this.getList()
    this.openLoading()
  },
  methods: {
    /** Check cache query information */
    getList() {
      getCache().then((response) => {
        this.cache = response.data
        this.loading.close()

        this.commandstats = echarts.init(this.$refs.commandstats, 'macarons')
        this.commandstats.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          series: [
            {
              name: 'Command',
              type: 'pie',
              roseType: 'radius',
              radius: [15, 95],
              center: ['50%', '38%'],
              data: response.data.commandStats,
              animationEasing: 'cubicInOut',
              animationDuration: 1000
            }
          ]
        })
        this.usedmemory = echarts.init(this.$refs.usedmemory, 'macarons')
        this.usedmemory.setOption({
          tooltip: {
            formatter: '{b} <br/>{a}: ' + this.cache.info.used_memory_human
          },
          series: [
            {
              name: 'Peak',
              type: 'gauge',
              min: 0,
              max: 1000,
              detail: {
                formatter: this.cache.info.used_memory_human
              },
              data: [
                {
                  value: parseFloat(this.cache.info.used_memory_human),
                  name: 'Memory consumption'
                }
              ]
            }
          ]
        })
      })
    },
    // Open the loading layer
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: 'Reading desperately',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
  }
}
</script>
