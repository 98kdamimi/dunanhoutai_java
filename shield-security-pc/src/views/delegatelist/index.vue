<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="stakingAddress">
              <el-input v-model="queryParams.stakingAddress" placeholder="请输入质押账户地址"></el-input>
            </el-form-item>
            <el-form-item label="" prop="receiverAddress">
              <el-input v-model="queryParams.receiverAddress" placeholder="请输入申请地址"></el-input>
            </el-form-item>
            <!-- <el-form-item label="" prop="tradeState">
              <el-select v-model="queryParams.tradeState" filterable placeholder="请选择交易类型" style="width: 90%;">
                <el-option v-for="item in tradeStateList" :key="item.name" :label="item.name" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="" prop="resourceType">
              <el-select v-model="queryParams.resourceType" filterable placeholder="请选择委托类型" style="width: 90%;">
                <el-option v-for="item in energyTypeList" :key="item.name" :label="item.name" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="dataList" max-height="600">
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
          <!-- <el-table-column label="交易类型" align="center" prop="tradeState">
            <template slot-scope="scope">
              <span v-if="scope.row.tradeState === 'send'">委托</span>
              <span v-else>取消委托</span>
            </template>
          </el-table-column> -->
          <el-table-column label="交易id" align="center" key="transactionId" prop="transactionId" />
          <el-table-column label="创建时间" align="center" prop="timestamp"></el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>



  </div>
</template>

<script>
import { findDelegatelist } from "@/api/delegateList/delegateList";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      loading: true,
      dialogOpen: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      energyTypeList:[
        {
          "name":"带宽",
          "value":"BANDWIDTH"
        },
        {
          "name":"能量",
          "value":"ENERGY"
        }
      ],
      tradeStateList:[
        {
          "name":"委托",
          "value":"send"
        },
        {
          "name":"取消委托",
          "value":"return"
        }
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
      findDelegatelist(this.queryParams).then(res => {
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
