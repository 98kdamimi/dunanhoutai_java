<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="content">
              <el-input v-model="queryParams.content" placeholder="请输入归属地"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="设备id" align="center" key="deviceId" prop="deviceId" />
          <el-table-column label="设备类型" align="center" key="deviceModel" prop="deviceModel" />
          <el-table-column label="首次登录ip" align="center" key="userIp" prop="userIp" />
          <el-table-column label="归属地" align="center" key="ipAddress" prop="ipAddress" />
          <el-table-column label="首次登录时间" align="center" prop="createdAt"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="findInfoList(scope.row)">访问记录</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>

    <el-dialog title="访问记录" :visible.sync="dialogOpen" width="70%" append-to-body>
      <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="content">
              <el-input v-model="infoQueryParams.content" placeholder="请输入归属地"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="infoHandleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="infoResetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      <el-table :data="dataInfoList" max-height="600">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="设备id" align="center" key="deviceId" prop="deviceId" />
        <el-table-column label="设备类型" align="center" key="deviceModel" prop="deviceModel" />
        <el-table-column label="首次登录ip" align="center" key="userIp" prop="userIp" />
        <el-table-column label="归属地" align="center" key="ipAddress" prop="ipAddress" />
        <el-table-column label="登录时间" align="center" prop="createdAt"></el-table-column>
      </el-table>
      <pagination v-show="infoTotal > 0" :total="infoTotal" :page.sync="infoQueryParams.pageNumber"
        :limit.sync="infoQueryParams.pageSize" @pagination="getInfoList" />
    </el-dialog>

  </div>
</template>

<script>
import { findAppUser,findInfoAppUser } from "@/api/appuser/appuser";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      dataInfoList:[],
      infoTotal:0,
      loading: true,
      infoLoading: true,
      dialogOpen: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      infoQueryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
    };
  },
  watch: {
  },
  created() {
    this.getList();
  },
  methods: {

    getList() {
      this.loading = true;
      findAppUser(this.queryParams).then(res => {
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
      })

    },

    resetQuery() {
      this.queryParams = {}
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    findInfoList(row) {
      this.infoQueryParams.deviceId = row.deviceId
      this.infoQueryParams.pageNumber =1
      this.infoQueryParams.pageSize=10
      this.getInfoList()
      this.dialogOpen = true
    },

    getInfoList(){
      this.infoLoading = true;
      findInfoAppUser(this.infoQueryParams).then(res => {
        this.dataInfoList = res.data.list
        this.infoTotal = res.data.total
        this.infoLoading = false
      })
    },
    infoHandleQuery(){
      this.infoQueryParams.pageNumber = 1
      this.getInfoList();
    },
    infoResetQuery(){
      this.infoQueryParams.content = null
      this.infoQueryParams.pageNumber =1
      this.infoQueryParams.pageSize=10
      this.getInfoList();
    },
    formatDate(isoString) {
      const date = new Date(isoString);
      const formattedDate = new Intl.DateTimeFormat("zh-CN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        timeZone: "Asia/Shanghai"
      }).format(date);
      return formattedDate;
    }

  }
};
</script>
<style>
.mainBox {
  height: calc(100vh - 84px);
  overflow-y: auto;
  background-color: #efefef;

}

.flex_sb {
  display: flex;
  justify-content: space-between;
}

.app-container {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.conetntBox {
  background-color: #fff;
  box-sizing: border-box;
  padding: 20px;
}
</style>
