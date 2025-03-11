<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="network">
              <el-input v-model="queryParams.network" placeholder="请输入网络名称"></el-input>
            </el-form-item>
            <el-form-item label="" prop="tokenName">
              <el-input v-model="queryParams.tokenName" placeholder="请输入代币名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
          
          <!-- 展开行的子表格 -->
          <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="交易哈希" align="center" key="txid" prop="txid"/>
          <el-table-column label="账户id" align="center" key="accountId" prop="accountId"/>
          <el-table-column label="发送地址" align="center" key="fromAddress" prop="fromAddress"/>
          <el-table-column label="接收地址" align="center" key="toAddress" prop="toAddress"/>
          <el-table-column label="网络名称" align="center" key="networkName" prop="networkName"/>
          <el-table-column label="代币名称" align="center" key="tokenName" prop="tokenName"/>
          <el-table-column label="交易数量" align="center" key="amount" prop="amount"/>
          <el-table-column label="交易类型" align="center" key="type" prop="type"/>
          <el-table-column label="交易方向" align="center" key="direction" prop="direction"/>
          <el-table-column label="交易时间" align="center" key="createdAt" prop="createdAt"/>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { transactionlist} from "@/api/transaction/transaction";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      loading:true,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      expandedRowKeys: [],  // 用于存储展开的行的 key
      statusList:[
        {
          "id":0,
          "value":"待上线"
        },
        {
          "id":1,
          "value":"下线"
        },
        {
          "id":2,
          "value":"上线"
        },
      ],
     
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
      transactionlist(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
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
