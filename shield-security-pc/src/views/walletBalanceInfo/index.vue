<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="walletAddress">
              <el-input v-model="queryParams.walletAddress" placeholder="请输入钱包地址"></el-input>
            </el-form-item>
            <el-form-item label="" prop="network">
              <el-input v-model="queryParams.networkName" placeholder="请输入网络名称"></el-input>
            </el-form-item>
            <el-form-item label="" prop="tokenName">
              <el-input v-model="queryParams.name" placeholder="请输入代币名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <span style="font-size: 24px;margin-right: 30px;font-weight: bold">钱包总额：${{ numAll }}</span>
        </div>

        <el-table :data="dataList" style="width: 100%" :row-key="row => row.id"  max-height="650">
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
          <el-table-column label="更新时间" align="center" prop="updatedAt"></el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { walletInfoList,walletInfoNunAll} from "@/api/walletBalance/walletBalance";

export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      numAll:"0",
      loading: true,
      dialogOpen: false,
      // 查询参数
      queryParams: {
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

    getNunAll(){
      walletInfoNunAll(this.queryParams).then(res => {
        this.numAll = res.data
      })
    },

    getList() {
      this.loading = true;
      walletInfoList(this.queryParams).then(res => {
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
        this.getNunAll();
      })

    },

    resetQuery() {
      this.queryParams = {}
      this.time = []
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
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
