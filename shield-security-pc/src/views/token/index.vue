<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <!-- <el-form-item label="" prop="status">
              <el-date-picker v-model="time" type="daterange" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="changeTime">
              </el-date-picker>
            </el-form-item> -->
            <el-form-item label="" prop="messageTitleZh">
              <el-select v-model="queryParams.chainId" placeholder="请选择主链">
                    <el-option
                      v-for="item in networkList"
                      :key="item.name"
                      :label="item.value"
                      :value="item.name">
                    </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入代币符合"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
         <!-- <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:user:add']">添加</el-button>
            </el-col>
          </el-row>-->
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="logo" align="center" width="180">
            <template slot-scope="scope">
              <div style="width: 90%; height: 80px; overflow: hidden; cursor: pointer;" >
                <img :src="scope.row.logoURI" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" key="name" prop="name"/>
          <el-table-column label="代币状态" align="center" key="status" prop="status"/>
          <el-table-column label="代币符号" align="center" key="symbol" prop="symbol"/>
          <el-table-column label="代币来源" align="center" key="source" prop="source"/>
          <el-table-column label="实现平台" align="center" key="impl" prop="impl"/>
          <el-table-column label="合约地址" align="center" key="address" prop="address"/>
          <el-table-column label="市场总值" align="center" key="marketCap" prop="marketCap"/>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />

        
      </div>
    </div>
  </div>
</template>

<script>
import { tokenList} from "@/api/token/token";
import { networkAll} from "@/api/network/network";
export default {
  name: "typesOfPoints",
  data() {
    return {
      baseImageUrl: process.env.VUE_APP_IMAGE_API,
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 是否显示弹出层
      dialogOpen: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      networkList:[],
     
    };
  },
  watch: {
  },
  created() {
    this.getNetworkList()
    this.getList();
  },
  methods: {
   
    //网络列表
    getNetworkList(){
      networkAll().then(res =>{
        console.log(res)
        this.networkList = res.data
      })
    },

    //列表
    getList() {
      tokenList(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
      })
      
    },
    //重置
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
