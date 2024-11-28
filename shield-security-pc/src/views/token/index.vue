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
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope" >
              <el-button size="mini" type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>

     <!-- 添加或修改用户配置对话框 -->
     <el-dialog :title="title" :visible.sync="dialogOpen" width="70%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="150px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="logo" prop="logoURI">
                  <uploadImg v-model="logoURI" :limit="1"></uploadImg>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="名称" prop="name">
                  <el-input v-model="formData.name" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="addToIndex" prop="addToIndex">
                  <el-select v-model="formData.addToIndex"  placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in lableTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="合约地址" prop="address">
                  <el-input v-model="formData.address" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="区块链的 ID" prop="chainId">
                  <el-input v-model="formData.chainId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否通过验证" prop="checked">
                  <el-select v-model="formData.checked" placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in lableTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="CoinMarketCap标识" prop="cmcId">
                  <el-input v-model="formData.cmcId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="CoinGecko标识" prop="coingeckoId">
                  <el-input v-model="formData.coingeckoId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="代币的精度" prop="decimals">
                  <el-input v-model="formData.decimals" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实现方式" prop="impl">
                  <el-input v-model="formData.impl" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否原生" prop="isNative">
                  <el-select v-model="formData.isNative" placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in lableTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="市场总值" prop="marketCap">
                  <el-input v-model="formData.marketCap" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级" prop="riskLevel">
                  <el-input v-model="formData.riskLevel" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="安全保障" prop="security">
                  <el-select v-model="formData.security" placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in lableTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="formData.status" placeholder="请选择状态"  style="width: 100%;" >
                    <el-option
                      v-for="item in statusList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="代币符号" prop="symbol">
                  <el-input v-model="formData.symbol" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否验证" prop="verified">
                  <el-select v-model="formData.verified" placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in lableTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信息来源" prop="verified">
                  <el-select v-model="formData.source" multiple placeholder="请选择"  style="width: 100%;" >
                    <el-option
                      v-for="item in sourceList"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm" v-debounce>确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
  </div>
</template>

<script>
import uploadImg from '@/components/uploadImg/uploadImg.vue'
import { tokenList,findTokenSource,tokenUpdata} from "@/api/token/token";
import { networkAll} from "@/api/network/network";
export default {
  name: "typesOfPoints",
  components: {
      uploadImg
  },
  data() {
    return {
      baseImageUrl: process.env.VUE_APP_IMAGE_API,
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 是否显示弹出层
      dialogOpen: false,
      formData:{},
      title:"",
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      networkList:[],
      logoURI:[],
      sourceList:[],
      statusList:[
        {
          "id":"LISTED",
          "value":"LISTED"
        },
        {
          "id":"DRAFT",
          "value":"DRAFT"
        },
      ],
      lableTypeList:[
        {
          "id":true,
          "name":"是"
        },
        {
          "id":false,
          "name":"否"
        }
      ],
      rules:{}
    };
  },
  watch: {
  },
  created() {
    this.getNetworkList()
    this.getTokenSource()
    this.getList();
  },
  methods: {

    //代币来源
    getTokenSource(){
      findTokenSource().then(res =>{
        this.sourceList = res.data
      })
    },
   
    //网络列表
    getNetworkList(){
      networkAll().then(res =>{
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
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },

    handleUpdate(row){
      this.dialogOpen = true
      this.formData = row 
      this.logoURI = row.logoURI
    },

    cancel(){
      this.dialogOpen = false
      this.formData ={}
      this.logoURI =[]
    },
    
     /** 提交按钮 */
     submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          let fordata = new FormData()
          if (this.logoURI && this.logoURI.length > 0 && this.logoURI[0].raw) {
              this.logoURI.forEach((val, index) => {
                fordata.append("file", val.raw);
              });
          }
          fordata.append("dataStr", JSON.stringify(this.formData))
          tokenUpdata(fordata).then(res => {
            this.$modal.msgSuccess("新增成功");
            this.dialogOpen = false;
            this.getList();
          });
        }
      });
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
