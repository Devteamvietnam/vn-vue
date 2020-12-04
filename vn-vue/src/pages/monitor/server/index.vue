<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>CPU</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf"><div class="cell">attributes</div></th>
                  <th class="is-leaf"><div class="cell">value</div></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td><div class="cell">Number of cores</div></td>
                  <td>
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.cpuNum }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">User usage rate</div></td>
                  <td>
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.used }}%
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">System utilization rate</div></td>
                  <td>
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.sys }}%
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Current idle rate</div></td>
                  <td>
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.free }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>Memory</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf"><div class="cell">attributes</div></th>
                  <th class="is-leaf"><div class="cell">Memory</div></th>
                  <th class="is-leaf"><div class="cell">JVM</div></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td><div class="cell">Total memory</div></td>
                  <td>
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.total }}G
                    </div>
                  </td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.total }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Used memory</div></td>
                  <td>
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.used }}G
                    </div>
                  </td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.used }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Remaining memory</div></td>
                  <td>
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.free }}G
                    </div>
                  </td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.free }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Utilization rate</div></td>
                  <td>
                    <div
                      class="cell"
                      v-if="server.mem"
                      :class="{ 'text-danger': server.mem.usage > 80 }"
                    >
                      {{ server.mem.usage }}%
                    </div>
                  </td>
                  <td>
                    <div
                      class="cell"
                      v-if="server.jvm"
                      :class="{ 'text-danger': server.jvm.usage > 80 }"
                    >
                      {{ server.jvm.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>Server information</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td><div class="cell">server name</div></td>
                  <td>
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.computerName }}
                    </div>
                  </td>
                  <td><div class="cell">operating system</div></td>
                  <td>
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.osName }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Server IP</div></td>
                  <td>
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.computerIp }}
                    </div>
                  </td>
                  <td><div class="cell">System architecture</div></td>
                  <td>
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.osArch }}
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>Java virtual machine information</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td><div class="cell">Java name</div></td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.name }}
                    </div>
                  </td>
                  <td><div class="cell">Java version</div></td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.version }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td><div class="cell">Startup time</div></td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.startTime }}
                    </div>
                  </td>
                  <td><div class="cell">Run time</div></td>
                  <td>
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.runTime }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1"><div class="cell">Installation path</div></td>
                  <td colspan="3">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.home }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1"><div class="cell">Project path</div></td>
                  <td colspan="3">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.userDir }}
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>Disk status</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf">
                    <div class="cell">drive letter path</div>
                  </th>
                  <th class="is-leaf"><div class="cell">File system</div></th>
                  <th class="is-leaf">
                    <div class="cell">Drive letter type</div>
                  </th>
                  <th class="is-leaf"><div class="cell">Total size</div></th>
                  <th class="is-leaf">
                    <div class="cell">Available size</div>
                  </th>
                  <th class="is-leaf"><div class="cell">Used size</div></th>
                  <th class="is-leaf">
                    <div class="cell">Used percentage</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="server.sysFiles">
                <tr v-for="sysFile in server.sysFiles" :key="sysFile">
                  <td>
                    <div class="cell">{{ sysFile.dirName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.sysTypeName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.typeName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.total }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.free }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.used }}</div>
                  </td>
                  <td>
                    <div
                      class="cell"
                      :class="{ 'text-danger': sysFile.usage > 80 }"
                    >
                      {{ sysFile.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getServer } from "@/services/api/monitor/server";

export default {
  name: "Server",
  data() {
    return {
      // Load layer information
      loading: [],
      // server information
      server: [],
    };
  },
  created() {
    this.getList();
    this.openLoading();
  },
  methods: {
    /** Query server information */
    getList() {
      getServer().then((response) => {
        this.server = response.data;
        this.loading.close();
      });
    },
    // Open the loading layer
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: "Reading desperately",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
  },
};
</script>
