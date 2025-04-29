<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="stakingAddress">
              <el-input v-model="queryParams.stakingAddress" placeholder="请输入质押账户地址"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row>
            <span style="font-weight: bold;">委托TRX总量：{{ numData.totalAmount }}</span>
            <span style="font-weight: bold;margin-left: 30px;margin-right: 30px;">委托资源总量：{{numData.totalResourceAmount}}</span>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="质押账户地址" align="center" key="id" prop="id" />
          <el-table-column label="委托TRX总量" align="center" key="amount" prop="amount" />
          <el-table-column label="委托资源总量" align="center" key="resourceAmount" prop="resourceAmount">
            <template slot-scope="scope">
              <span >{{"≈"+ scope.row.resourceAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="委托类型" align="center" prop="resourceType">
            <template slot-scope="scope">
              <span v-if="scope.row.resourceType === 'BANDWIDTH'">带宽</span>
              <span v-else>能量</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="findInfoList(scope.row)">委托记录</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>

    <el-dialog title="访问记录" :visible.sync="dialogOpen" width="70%" append-to-body>
      <div class="flex_sb">
          <el-form :model="infoQueryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="content">
              <el-input v-model="infoQueryParams.receiverAddress" placeholder="请输入申请地址"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="infoHandleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="infoResetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      <el-table :data="dataInfoList" max-height="600">
        <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="质押账户地址" align="center" key="stakingAddress" prop="stakingAddress" />
          <el-table-column label="申请账户地址" align="center" key="receiverAddress" prop="receiverAddress" />
          <el-table-column label="委托TRX量" align="center" key="amount" prop="amount" />
          <el-table-column label="委托资源量" align="center" key="resourceAmount" prop="resourceAmount">
            <template slot-scope="scope">
              <span >{{"≈"+ scope.row.resourceAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="委托类型" align="center" prop="resourceType">
            <template slot-scope="scope">
              <span v-if="scope.row.resourceType === 'BANDWIDTH'">带宽</span>
              <span v-else>能量</span>
            </template>
          </el-table-column>
          <el-table-column label="交易id" align="center" key="transactionId" prop="transactionId" />
          <el-table-column label="创建时间" align="center" prop="timestamp"></el-table-column>
      </el-table>
      <pagination v-show="infoTotal > 0" :total="infoTotal" :page.sync="infoQueryParams.pageNumber"
        :limit.sync="infoQueryParams.pageSize" @pagination="getInfoList" />
    </el-dialog>

  </div>
</template>

<script>
import { findStatistics,findStatisticsNum,findDelegatelist } from "@/api/delegateList/delegateList";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      loading: true,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      numData:{},
      dataInfoList:[],
      dialogOpen: false,
      infoQueryParams:{
        pageNumber: 1,
        pageSize: 10,
      },
      infoTotal:0,
      stakingAddress:null,
     
    };
  },
  watch: {
  },
  created() {
    this.getList();
    this.getNum()
  },
  methods: {
    getNum(){
      findStatisticsNum(this.queryParams).then(res => {
        this.numData = res.data
      })
    },

    getList() {
      this.loading = true;
      findStatistics(this.queryParams).then(res => {
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
      })

    },
    resetQuery() {
      this.queryParams = {}
      this.getList();
      this.getNum()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
      this.getNum()
    },

    //打开详情框
    findInfoList(row){
      this.infoQueryParams.stakingAddress = row.id
      this.stakingAddress = row.id
      this.getInfoList()
      this.dialogOpen =true
    },
    //查询详情记录
    getInfoList(){
      findDelegatelist(this.infoQueryParams).then(res => {
        this.dataInfoList = res.data.list
        this.infoTotal = res.data.total
      })
    },

    //详情搜索
    infoHandleQuery(){
      this.infoQueryParams.pageNumber = 1;
      this.infoQueryParams.pageSize = 10;
      this.getInfoList();
    },

    //详情重置
    infoResetQuery(){
      this.infoQueryParams ={}
      this.infoQueryParams.pageNumber = 1;
      this.infoQueryParams.pageSize = 10;
      this.infoQueryParams.stakingAddress =this.stakingAddress;
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
