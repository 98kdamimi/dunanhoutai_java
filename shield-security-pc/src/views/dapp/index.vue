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
              <el-select v-model="queryParams.type" placeholder="请选择类型">
                    <el-option
                      v-for="item in typeList"
                      :key="item.id"
                      :label="item.name+' —— '+item.type"
                      :value="item.id">
                    </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" prop="messageTitleZh">
              <el-select v-model="queryParams.status" placeholder="请选择状态">
                    <el-option
                      v-for="item in statusList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
                    </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="" prop="messageTitleZh">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 240px"
                @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-refresh-left" @click="rpcRest">获取dapp</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" icon="el-icon-plus" @click="handleAdd">添加</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="logo" align="center" width="180">
            <template slot-scope="scope">
              <div style="width: 90%; height: 80px; overflow: hidden; cursor: pointer;" >
                <img :src="scope.row.logoURL" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" prop="name" />
          <el-table-column label="标题" align="center" prop="subtitle" :show-overflow-tooltip="true"/>
          <el-table-column label="地址" align="center" prop="url" :show-overflow-tooltip="true"/>
          <el-table-column label="状态"  align="center" prop="status" />
          <el-table-column label="创建时间" align="center" prop="createdAt" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text" style="color: lawngreen;" v-if="scope.row.status == 'DRAFT'" @click="online(scope.row)">上线</el-button>
              <el-button type="text" style="color: red;" v-if="scope.row.status == 'LISTED'" @click="offline(scope.row)">下线</el-button>
              <el-button type="text" @click="handleUpdate(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="70%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="logo" prop="logoURL">
                  <uploadImg v-model="logoURL" :limit="1"></uploadImg>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="名称" prop="name">
                  <el-input v-model="formData.name" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="标题" prop="subtitle">
                  <el-input v-model="formData.subtitle" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="url地址" prop="url">
                  <el-input v-model="formData.url" placeholder="请输入名称" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="所属网络" prop="networkIds">
                  <el-select v-model="formData.networkIds" multiple filterable  placeholder="请选择网络"  style="width: 100%;" >
                    <el-option
                      v-for="item in networkList"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col> 
              <el-col :span="12">
                <el-form-item label="所属分类" prop="categoryIds">
                  <el-select v-model="formData.categoryIds" multiple  placeholder="请选择分类"  style="width: 100%;" >
                    <el-option
                      v-for="item in typeList"
                      :key="item.id"
                      :label="item.name+' —— '+item.type"
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
              <el-col :span="24">
                <el-divider content-position="center">不同语言标题</el-divider>
              </el-col>
              <el-col :span="12">
                <el-form-item label="ar" prop="ar">
                  <el-input v-model="localization['ar']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="bn" prop="bn">
                  <el-input v-model="localization['bn']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="de" prop="de">
                  <el-input v-model="localization['de']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="en-US" prop="en-US">
                  <el-input v-model="localization['en-US']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="es" prop="es">
                  <el-input v-model="localization['es']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="fil" prop="fil">
                  <el-input v-model="localization['fil']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="fr-FR" prop="fr-FR">
                  <el-input v-model="localization['fr-FR']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="hi-IN" prop="hi-IN">
                  <el-input v-model="localization['hi-IN']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="it-IT" prop="it-IT">
                  <el-input v-model="localization['it-IT']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="ja-JP" prop="ja-JP">
                  <el-input v-model="localization['ja-JP']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="ko-KR" prop="ko-KR">
                  <el-input v-model="localization['ko-KR']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="mn-MN" prop="mn-MN">
                  <el-input v-model="localization['mn-MN']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="pt" prop="pt">
                  <el-input v-model="localization['pt']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="ru" prop="ru">
                  <el-input v-model="localization['ru']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="th-TH" prop="th-TH">
                  <el-input v-model="localization['th-TH']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="uk-UA" prop="uk-UA">
                  <el-input v-model="localization['uk-UA']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="vi" prop="vi">
                  <el-input v-model="localization['vi']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="zh-CN" prop="zh-CN">
                  <el-input v-model="localization['zh-CN']" placeholder="请输入内容" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="zh-HK" prop="zh-HK">
                  <el-input v-model="localization['zh-HK']" placeholder="请输入内容" style="width: 100%;" />
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
    </div>
  </div>
</template>

<script>
import uploadImg from '@/components/uploadImg/uploadImg.vue'
import { dappList,dappUpdata,dappOnline,dappOffline,findTypeList,dappAdd,dappRpcList} from "@/api/dapp/dapp";
import { findNetWorkIdList} from "@/api/network/network";
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
      formData: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      loading :true,
      typeList:[],
      networkList:[],
      logoURL:[],
      localization:{
        "_id":"",
        "ar": "",
        "bn": "",
        "de": "",
        "en-US": "",
        "es": "",
        "fil": "",
        "fr-FR": "",
        "hi-IN": "",
        "it-IT": "",
        "ja-JP": "",
        "ko-KR": "",
        "mn-MN": "",
        "pt": "",
        "ru": "",
        "th-TH": "",
        "uk-UA": "",
        "vi": "",
        "zh-CN": "",
        "zh-HK": ""
      },
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
      rules:{

      }
    };
  },
  watch: {
  },
  created() {
    this.getFindTypeList();
    this.getNetworkList()
    this.getList();

  },
  methods: {

    //获取远程更新
    rpcRest(){
      this.loading = true
      dappRpcList().then(res =>{
        this.getFindTypeList()
        this.getList()
      })
    },
   
    getFindTypeList(){
      findTypeList().then(res =>{
        this.typeList =res.data
      })
    },

    //网络列表
    getNetworkList(){
      findNetWorkIdList().then(res =>{
        this.networkList = res.data
      })
    },

    getList() {
      this.loading = true;
      dappList(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false;
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
      this.logoURL = [];
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
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.logoURL = row.logoURL
      this.formData = row;
      this.localization = row.localization
      this.dialogOpen = true;
      this.title = "编辑";
    },
    handleAdd(){
      this.reset();
      this.logoURL = []
      this.localization ={
        "_id":"",
        "ar": "",
        "bn": "",
        "de": "",
        "en-US": "",
        "es": "",
        "fil": "",
        "fr-FR": "",
        "hi-IN": "",
        "it-IT": "",
        "ja-JP": "",
        "ko-KR": "",
        "mn-MN": "",
        "pt": "",
        "ru": "",
        "th-TH": "",
        "uk-UA": "",
        "vi": "",
        "zh-CN": "",
        "zh-HK": ""
      }
      this.dialogOpen = true;
      this.title = "新增";
    },
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          let fordata = new FormData()
          if (this.logoURL && this.logoURL.length > 0 && this.logoURL[0].raw) {
              this.logoURL.forEach((val, index) => {
                fordata.append("file", val.raw);
              });
          }
          this.formData.localization = this.localization
          fordata.append("dataStr", JSON.stringify(this.formData))
          if(this.formData.id){
            dappUpdata (fordata).then(res =>{
              this.$modal.msgSuccess("新增成功");
              this.dialogOpen = false;
              this.getList();
            })
          }else{
             dappAdd(fordata).then(res => {
              this.$modal.msgSuccess("编辑成功");
              this.dialogOpen = false;
              this.getList();
            });
          }
        }
      });
    },

    //上线
    online(row){
      this.$modal.confirm('是否确认上线？').then(function () {
        return dappOnline(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上线成功");
      }).catch(() => { });
    },

    //下线
    offline(row){
      this.$modal.confirm('是否确认下线此版本？').then(function () {
        return dappOffline(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("下线成功");
      }).catch(() => { });
    } , 

    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除？').then(function () {
        return messageDelete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
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
