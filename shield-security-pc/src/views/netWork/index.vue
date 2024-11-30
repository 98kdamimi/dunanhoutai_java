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

        <el-table :data="dataList" v-loading="loading" max-height="600">
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
          <el-input v-model="tokenQueryParams.name" placeholder="请输入代币符号"></el-input>
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
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope" >
              <el-button size="mini" type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="tokenTotal > 0" :total="tokenTotal" :page.sync="tokenQueryParams.pageNumber"
          :limit.sync="tokenQueryParams.pageSize" @pagination="getTokenList" />
    </el-dialog>


    <el-dialog title="第三方代币" :visible.sync="obtainTokenOpen" width="80%" >
      <el-form :model="obtainTokenQueryParams"  size="small" :inline="true" label-width="68px">
        <el-form-item label="" prop="name">
          <el-input v-model="tokenQueryParams.name" placeholder="请输入代币符号"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="tokenHandleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="tokenResetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="coingeckoDataList" max-height="550">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="logo" align="center" width="180">
            <template slot-scope="scope">
              <div style="width: 90%; height: 80px; overflow: hidden; cursor: pointer;" >
                <img :src="scope.row.data.attributes.image_url" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" key="name" prop="data.attributes.name"/>
          <el-table-column label="代币符号" align="center" key="symbol" prop="data.attributes.symbol"/>
          <el-table-column label="代币地址" align="center" key="address" prop="data.attributes.address" 
          :show-overflow-tooltip="true" width="350"/>
          <el-table-column label="总值" align="center" key="market_cap_usd" prop="data.attributes.market_cap_usd"/>
          <el-table-column label="精度" align="center" key="decimals" prop="data.attributes.decimals"/>
          <el-table-column label="平台id" align="center" key="coingecko_coin_id" prop="data.attributes.coingecko_coin_id"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180"> 
            <template slot-scope="scope">
              <el-button type="text" @click = "addToken(scope.row)">添加至主链</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="coingeckoTotal > 0" :total="coingeckoTotal" :page.sync="obtainTokenQueryParams.pageNumber"
          :limit.sync="obtainTokenQueryParams.pageSize" @pagination="getTokenList" />
    </el-dialog>

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

        <!-- 添加至主链 -->
        <el-dialog title="添加代币" :visible.sync="coingeckoDialogOpen" width="70%" append-to-body>
          <el-form ref="coingeckoFormData" :model="coingeckoFormData" :rules="coingeckoRules" label-width="150px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="logo" prop="logoURI">
                  <uploadImg v-model="coingeckoLogoURI" :limit="1"></uploadImg>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="名称" prop="name">
                  <el-input v-model="coingeckoFormData.name" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="addToIndex" prop="addToIndex">
                  <el-select v-model="coingeckoFormData.addToIndex"  placeholder="请选择"  style="width: 100%;" >
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
                  <el-input v-model="coingeckoFormData.address" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="区块链的 ID" prop="chainId">
                  <el-input v-model="coingeckoFormData.chainId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否通过验证" prop="checked">
                  <el-select v-model="coingeckoFormData.checked" placeholder="请选择"  style="width: 100%;" >
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
                  <el-input v-model="coingeckoFormData.cmcId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="CoinGecko标识" prop="coingeckoId">
                  <el-input v-model="coingeckoFormData.coingeckoId" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="代币的精度" prop="decimals">
                  <el-input v-model="coingeckoFormData.decimals" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实现方式" prop="impl">
                  <el-input v-model="coingeckoFormData.impl" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否原生" prop="isNative">
                  <el-select v-model="coingeckoFormData.isNative" placeholder="请选择"  style="width: 100%;" >
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
                  <el-input v-model="coingeckoFormData.marketCap" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级" prop="riskLevel">
                  <el-input v-model="coingeckoFormData.riskLevel" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="安全保障" prop="security">
                  <el-select v-model="coingeckoFormData.security" placeholder="请选择"  style="width: 100%;" >
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
                  <el-select v-model="coingeckoFormData.status" placeholder="请选择状态"  style="width: 100%;" >
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
                  <el-input v-model="coingeckoFormData.symbol" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否验证" prop="verified">
                  <el-select v-model="coingeckoFormData.verified" placeholder="请选择"  style="width: 100%;" >
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
                  <el-select v-model="coingeckoFormData.source" multiple placeholder="请选择"  style="width: 100%;" >
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
            <el-button type="primary" @click="coingeckoSubmitForm" v-debounce>确 定</el-button>
            <el-button @click="closeCoingecko">取 消</el-button>
          </div>
        </el-dialog>

  </div>

  
</template>

<script>
import uploadImg from '@/components/uploadImg/uploadImg.vue'
import { networkList,networkObtainList} from "@/api/network/network";
import {tokenList,findTokenSource,tokenUpdata,tokenAdd} from "@/api/token/token";
import {findCoingeckoToken} from "@/api/coingecko/coingecko";
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
      coingeckoDataList:[],
      coingeckoTotal:0,
      loading :true,
      logoURI:[],
      sourceList:[],
      coingeckoDialogOpen:false,
      coingeckoFormData:{},
      coingeckoLogoURI:[],
      acctiveNetWork:{},
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
      rules:{

      },
      coingeckoRules:{
        name: [
          { required: true, message: "代币名称不能为空", trigger: "change" },
        ],
        addToIndex: [
          { required: true, message: "addToIndex不能为空", trigger: "change" },
        ],
        address: [
          { required: true, message: "合约不能为空", trigger: "change" },
        ],
        chainId: [
          { required: true, message: "所属网络不能为空", trigger: "change" },
        ],
        checked: [
          { required: true, message: "请选择是否验证", trigger: "change" },
        ],
        coingeckoId: [
          { required: true, message: "coingecko标识不能为空", trigger: "change" },
        ],
        decimals: [
          { required: true, message: "代币精度不能为空", trigger: "change" },
        ],
        impl: [
          { required: true, message: "实现方式不能为空", trigger: "change" },
        ],
        isNative: [
          { required: true, message: "请选择是否原生", trigger: "change" },
        ],
        marketCap: [
          { required: true, message: "市场总值不能为空", trigger: "change" },
        ],
        riskLevel: [
          { required: true, message: "风险等级不能为空", trigger: "change" },
        ],
        security: [
          { required: true, message: "请选择安全保障", trigger: "change" },
        ],
        status: [
          { required: true, message: "请选择发布状态", trigger: "change" },
        ],
        symbol: [
          { required: true, message: "代币符号不能为空", trigger: "change" },
        ],
        verified: [
          { required: true, message: "请选择是否验证", trigger: "change" },
        ],
        source: [
          { required: true, message: "请选择信息来源", trigger: "change" },
        ],
      }
    };
  },
  watch: {
  },
  created() {
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
      this.tokenQueryParams.pageNumber = 1
      this.tokenQueryParams.pageSize = 10
      this.tokenQueryParams.chainId = row.chainId
      this.tokenQueryParams.impl = row.impl
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
    obtainTokenList(row){
      this.acctiveNetWork = row
      findCoingeckoToken(row).then(res =>{
        this.coingeckoDataList = res.data
        this.obtainTokenOpen = true
      })
    },

    //初始化新增代币
    addToken(row){
      this.coingeckoFormData = {}
      this.coingeckoLogoURI = []
      this.coingeckoLogoURI = row.data.attributes.image_url
      this.coingeckoFormData.logoURI = row.data.attributes.image_url
      this.coingeckoFormData.name = row.data.attributes.name
      this.coingeckoFormData.address = row.data.attributes.address
      this.coingeckoFormData.chainId = this.acctiveNetWork.chainId
      this.coingeckoFormData.impl = this.acctiveNetWork.impl
      this.coingeckoFormData.coingeckoId = row.data.attributes.coingecko_coin_id
      this.coingeckoFormData.decimals = row.data.attributes.decimals
      this.coingeckoFormData.marketCap = row.data.attributes.market_cap_usd
      this.coingeckoFormData.symbol = row.data.attributes.symbol
      this.coingeckoFormData.status = this.statusList[0].id
      this.coingeckoFormData.source = ["Coingecko"]
      this.coingeckoFormData.riskLevel = 1
      this.coingeckoFormData.addToIndex = this.lableTypeList[0].id
      this.coingeckoFormData.checked = this.lableTypeList[0].id
      this.coingeckoFormData.isNative = this.lableTypeList[0].id
      this.coingeckoFormData.security = this.lableTypeList[0].id
      this.coingeckoFormData.verified = this.lableTypeList[0].id
      this.coingeckoDialogOpen = true
    },
    //关闭新增代币
    closeCoingecko(){
      this.coingeckoDialogOpen = false
    },
    //将代币添加至主链
    coingeckoSubmitForm(){
      this.$refs["coingeckoFormData"].validate(valid => {
        if (valid) {
          let fordata = new FormData()
          if (this.coingeckoLogoURI && this.coingeckoLogoURI.length > 0 && this.coingeckoLogoURI[0].raw) {
              this.coingeckoLogoURI.forEach((val, index) => {
                fordata.append("file", val.raw);
              });
          }
          fordata.append("dataStr", JSON.stringify(this.coingeckoFormData))
          tokenAdd(fordata).then(res => {
            this.$modal.msgSuccess("新增成功");
            this.coingeckoDialogOpen = false;
            this.obtainTokenList(this.acctiveNetWork);
          });
        }
      });
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
            this.getTokenList();
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
