<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <el-row style="margin-bottom: 20px;">
          <el-col :span="24" style="text-align: right;">
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
              添加
            </el-button>
          </el-col>
        </el-row>
        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="版本号" align="center" prop="version" />
          <el-table-column label="配置网络" align="center" prop="networks" width="850" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span  v-for="(item, index) in scope.row.app.networks" :key="index">
                {{ item.name+"，" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope" >
            <el-button  type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />


        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="45%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-form-item label="版本号" prop="networkIds">
                <el-input v-model="formData.version" placeholder="请输入版本号" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="网络" prop="networkIds">
                <el-select v-model="formData.networkIds" multiple placeholder="请选择网络"style="width: 90%;">
                    <el-option
                      v-for="item in networkList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
                    </el-option>
                </el-select>
              </el-form-item>
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
import { appConfigAdd,appConfigUpdate,appConfiglist} from "@/api/appconfig/appconfig";
import { networkAll} from "@/api/network/network";
import editor from '@/components/Editor/index.vue'
export default {
  name: "typesOfPoints",
  components: {
    editor
  },
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogOpen: false,
      loading:true,
      formData: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      networkList:[],
      networkIdAll:[],
      networkIds:[],
      // 表单校验
      rules: {
        networkIds: [
          { required: true, message: "请选择至少一个网络", trigger: "blur" },
        ],
        version: [
          { required: true, message: "请输入版本号", trigger: "blur" },
        ],
      },
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
        this.networkList = res.data
      })
    },

   
    getList() {
      this.loading = true;
      appConfiglist(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
      })
      
    },
    // 取消按钮
    cancel() {
      this.dialogOpen = false;
      this.getList()
      this.reset();
    },
    // 表单重置
    reset() {
      this.formData = {};
      this.time = []
      this.fileList = []
      this.networkAll = []
      this.networkIds = []
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.dialogOpen = true;
      this.title = "添加";
    },

     /** 编辑按钮操作 */
     handleUpdate(row) {
      this.reset();
      this.formData = row;
      this.networkIdAll = row.app.networks
      this.formData.networkIds = this.networkIdAll.map(networkIdAll => networkIdAll.id);
      this.dialogOpen = true;
      this.title = "编辑";
    },
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        console.log(this.formData)
        if (valid) {
          if(this.formData.id){
            appConfigUpdate(this.formData).then(res => {
              this.$modal.msgSuccess("编辑成功");
              this.dialogOpen = false;
              this.getList();
            });
          }else{
            appConfigAdd(this.formData).then(res => {
              this.$modal.msgSuccess("新增成功");
              this.dialogOpen = false;
              this.getList();
            });
          }
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
