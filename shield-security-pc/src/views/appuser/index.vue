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
              <el-button type="text" @click="findWalletList(scope.row)">代币明细</el-button>
              <el-button type="text" @click="findWalletAll(scope.row)">钱包余额</el-button>
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
        <el-table-column label="登录ip" align="center" key="userIp" prop="userIp" />
        <el-table-column label="归属地" align="center" key="ipAddress" prop="ipAddress" />
        <el-table-column label="登录时间" align="center" prop="createdAt"></el-table-column>
      </el-table>
      <pagination v-show="infoTotal > 0" :total="infoTotal" :page.sync="infoQueryParams.pageNumber"
        :limit.sync="infoQueryParams.pageSize" @pagination="getInfoList" />
    </el-dialog>
    
    <el-dialog title="代币明细" :visible.sync="walletDialog" width="70%" append-to-body>
      <div class="flex_sb">
          <!-- <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="content">
              <el-input v-model="infoQueryParams.content" placeholder="请输入归属地"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="infoHandleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="infoResetQuery">重置</el-button>
            </el-form-item>
          </el-form> -->
        </div>
        <el-table :data="walletDataList" style="width: 100%" :row-key="row => row.id"  max-height="650">
          <el-table-column label="账户id" align="center" prop="accountId"></el-table-column>
          <el-table-column label="钱包地址" align="center" prop="walletAddress"></el-table-column>
          <el-table-column label="所属网络" align="center" prop="networkName"></el-table-column>
          <el-table-column label="代币名称" align="center" prop="name"></el-table-column>
          <el-table-column label="合约地址" align="center" prop="address">
            <template slot-scope="scope" >
              <span v-if="scope.row.address ">{{"$"+ scope.row.address }}</span>
              <span v-else>/</span>
            </template>
          </el-table-column>
          <el-table-column label="代币数量" align="center" prop="balance"></el-table-column>
          <el-table-column label="代币金额" align="center" prop="usdValue">
            <template slot-scope="scope">
              <span>{{"$"+ scope.row.usdValue }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="更新时间" align="center" prop="updatedAt"></el-table-column> -->
        </el-table>
        <pagination v-show="walletTotal > 0" :total="walletTotal" :page.sync="walletQueryParams.pageNumber"
          :limit.sync="walletQueryParams.pageSize" @pagination="getWalletList" />
    </el-dialog>

    <el-dialog title="钱包余额" :visible.sync="walletAllDialog" width="70%" append-to-body>
      <div class="flex_sb">
          <!-- <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="content">
              <el-input v-model="infoQueryParams.content" placeholder="请输入归属地"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="infoHandleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="infoResetQuery">重置</el-button>
            </el-form-item>
          </el-form> -->
        </div>
        <el-table :data="walletDataAllList" style="width: 100%" :row-key="row => row.id"  max-height="650">
          <!-- <el-table-column label="账户id" align="center" prop="accountId"></el-table-column> -->
          <el-table-column label="钱包地址" align="center" prop="walletAddress"></el-table-column>
          <el-table-column label="钱包余额" align="center" prop="totalUsdValue">
            <template slot-scope="scope">
              <span>{{"$"+ scope.row.totalUsdValue }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="更新时间" align="center" prop="updatedAt"></el-table-column> -->
        </el-table>
        <pagination v-show="walletAllTotal > 0" :total="walletTotal" :page.sync="walletAllQueryParams.pageNumber"
          :limit.sync="walletAllQueryParams.pageSize" @pagination="getWalletAllList" />
    </el-dialog>
  </div>
</template>

<script>
import { findAppUser,findInfoAppUser,findWalletList,findWalletAll } from "@/api/appuser/appuser";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      dataInfoList:[],
      walletDataList:[],
      walletDataAllList:[],
      walletTotal:0,
      infoTotal:0,
      walletAllTotal:0,
      loading: true,
      infoLoading: true,
      dialogOpen: false,
      walletDialog:false,
      walletAllDialog:false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      infoQueryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      walletQueryParams: {
        pageNumber: 1,
        pageSize: 10,
        instanceId:null,
      },
      walletAllQueryParams: {
        pageNumber: 1,
        pageSize: 10,
        instanceId:null,
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

    findWalletList(data){
      this.walletQueryParams.instanceId = data.instanceId
      this.walletQueryParams.pageNumber =1
      this.walletQueryParams.pageSize=10
      this.getWalletList()
      this.walletDialog = true
    },
    getWalletList(){
      console.log(this.walletQueryParams)
      findWalletList(this.walletQueryParams).then(res => {
        this.walletDataList = res.data.list
        this.walletTotal = res.data.total
      })
    },

    findWalletAll(data){
      this.walletAllQueryParams.instanceId = data.instanceId
      this.walletAllQueryParams.pageNumber =1
      this.walletAllQueryParams.pageSize=10
      this.getWalletAllList()
      this.walletAllDialog = true
    },
    getWalletAllList(){
      findWalletAll(this.walletAllQueryParams).then(res => {
        this.walletDataAllList = res.data.list
        this.walletAllTotal = res.data.total
      })
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
