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
        <el-table :data="dataList" style="width: 100%" 
          @row-click="handleRowClick" 
          :expand-row-keys="expandedRowKeys"
          :row-key="row => row.id">
          <!-- 展开列 -->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table :data="props.row.tokenBalan" style="width: 100%">
                <el-table-column label="代币名称" prop="name"></el-table-column>
                <el-table-column label="代币地址" prop="address"></el-table-column>
                <el-table-column label="coingeckoId" prop="coingeckoId"></el-table-column>
                <el-table-column label="代币余额" align="center">
                  <template slot-scope="scope">
                    <span style="font-size: 18px;font-weight: bold;">{{ scope.row.balance }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column label="所属网络" prop="network"></el-table-column>
          <el-table-column label="钱包地址" prop="walletAddress"></el-table-column>
          <el-table-column label="更新时间" prop="updatedAt"></el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { walletBalanceList} from "@/api/walletBalance/walletBalance";
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
      walletBalanceList(this.queryParams).then(res =>{
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

   // 处理行点击事件，点击行时展开或折叠
    handleRowClick(row, column, event) {
      const rowKey = row.id;  // 使用数据中的唯一字段 id 作为 row-key

      // 判断当前行是否已经在展开的行列表中
      const rowExpanded = this.expandedRowKeys.includes(rowKey);

      if (rowExpanded) {
        // 如果已展开，折叠
        this.expandedRowKeys = this.expandedRowKeys.filter(key => key !== rowKey);
      } else {
        // 如果未展开，展开
        this.expandedRowKeys.push(rowKey);
      }
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
