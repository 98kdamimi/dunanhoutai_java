<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入网络名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-refresh-left" @click="rpcList">获取网络</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="logo" align="center" width="180">
            <template slot-scope="scope">
              <div style="width: 90%; height: 80px; overflow: hidden; cursor: pointer;" >
                <img :src="scope.row.logoURI" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" key="name" prop="name"/>
          <el-table-column label="网络代码" align="center" key="code" prop="code"/>
          <el-table-column label="代币精度" align="center" key="decimals" prop="decimals"/>
          <el-table-column label="类型" align="center" key="impl" prop="impl"/>
          <el-table-column label="标识符" align="center" key="shortcode" prop="shortcode"/>
          <el-table-column label="简称" align="center" key="shortname" prop="shortname"/>
          <el-table-column label="状态" align="center" key="status" prop="status"/>
          <el-table-column label="代币符号" align="center" key="symbol" prop="symbol"/>
          <el-table-column label="更新时间" align="center" key="updatedAt" prop="updatedAt" width="200"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180"> 
            <template slot-scope="scope">
              <!-- <el-button type="text" @click="handleInfo(scope.row)">详情</el-button> -->
              <el-button type="text" @click="tokenList(scope.row)">代币列表</el-button>
              <el-button type="text" style="color: limegreen;" @click="obtainTokenList(scope.row)">获取代币</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>

    <el-dialog title="代币列表" :visible.sync="tokenOpen" width="80%" >
      <el-form :model="tokenQueryParams"  size="small" :inline="true" label-width="68px">
        <el-form-item label="" prop="name">
          <el-input v-model="tokenQueryParams.name" placeholder="请输入代币符合"></el-input>
          </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="tokenHandleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="tokenResetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tokenDataList" max-height="550">
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
        <pagination v-show="tokenTotal > 0" :total="tokenTotal" :page.sync="tokenQueryParams.pageNumber"
          :limit.sync="tokenQueryParams.pageSize" @pagination="getTokenList" />
    </el-dialog>


    <el-dialog title="第三方代币" :visible.sync="obtainTokenOpen" width="80%" >
      <el-form :model="obtainTokenQueryParams"  size="small" :inline="true" label-width="68px">
        <el-form-item label="" prop="name">
          <el-input v-model="tokenQueryParams.name" placeholder="请输入代币符合"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="tokenHandleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="tokenResetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tokenDataList" max-height="550">
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
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180"> 
            <template slot-scope="scope">
              <el-button type="text">添加至主链</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="tokenTotal > 0" :total="tokenTotal" :page.sync="tokenQueryParams.pageNumber"
          :limit.sync="tokenQueryParams.pageSize" @pagination="getTokenList" />
    </el-dialog>

  </div>

  
</template>

<script>
import { networkList,networkObtainList} from "@/api/network/network";
import {tokenList} from "@/api/token/token";
export default {
  name: "typesOfPoints",
  data() {
    return {
      baseImageUrl: process.env.VUE_APP_IMAGE_API,
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogOpen: false,
      tokenOpen:false,
      formData: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      tokenDataList: null,
      tokenTotal:0,
      tokenQueryParams:{
        pageNumber: 1,
        pageSize: 10,
      },
      obtainTokenOpen:false,
      obtainTokenQueryParams:{
        pageNumber: 1,
        pageSize: 10,
      },
      loading :true,
      rules:{

      }
    };
  },
  watch: {
  },
  created() {
    this.getList();
  },
  methods: {
    changeTime(e) {
      if (e) {
        this.queryParams.begTime = e[0]
        this.queryParams.endTime = e[1]
      } else {
        this.queryParams.begTime = ''
        this.queryParams.endTime = ''
      }
    },

    getList() {
      this.loading = true;
      networkList(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
      })
      
    },

    // 取消按钮
    cancel() {
      this.dialogOpen = false;
      this.reset();
    },

    // 表单重置
    reset() {
      this.formData = {};
      this.time = []
      this.fileList = []
      this.resetForm("form");
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

    //调用远程接口获取最新列表
    rpcList(){
      this.loading = true
      networkObtainList().then(res =>{
        this.getList()
      })
    },
    
    //详情页面
    handleInfo(row) {
      this.reset();
      this.formData = JSON.parse(JSON.stringify(row));
      this.dialogOpen = true;
      this.title = "详情";
    },
    //代币列表
    tokenList(row){
      this.tokenQueryParams.chainId = row.chainId
      this.getTokenList()
    },

    getTokenList(){
      tokenList(this.tokenQueryParams).then(res =>{
        this.tokenDataList = res.data.list
        this.tokenTotal = res.data.total;
      })
      this.tokenOpen = true
    },

    //搜索代币
    tokenHandleQuery(){
      this.getTokenList()
    },

    tokenResetQuery(){
      this.tokenQueryParams = {
        pageNumber: 1,
        pageSize: 10,
      }
      this.getTokenList();
    },
   
    //获取第三方代币列表
    obtainTokenList(){
      this.obtainTokenOpen = true
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
</style>
