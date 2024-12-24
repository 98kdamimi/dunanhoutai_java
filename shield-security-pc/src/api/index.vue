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
          <el-table-column label="类型" align="center" prop="typeName" />
          <el-table-column label="语言" align="center" prop="languageType" />
          <el-table-column label="内容" align="center" prop="contentInfo" :show-overflow-tooltip="true"/>
          <el-table-column label="访问地址" align="center" prop="htmlSite" width="660"/>
          <el-table-column label="创建时间" align="setTime" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope" v-if="scope.row.roleId !== 1">
            <el-button size="mini" type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete"  @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />


        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="60%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="语言" prop="languageType">
                  <el-select v-model="formData.languageType" filterable  placeholder="请选择语言"  style="width: 100%;" >
                    <el-option
                      v-for="item in languageList"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <editor v-model="formData.contentInfo":min-height="340"/>
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
import { agreementFindType,agreementAdd,agreementUpdate,agreementDelete} from "@/api/agreement/agreement";
import editor from '@/components/Editor/index.vue'
export default {
  name: "typesOfPoints",
  components: {
    editor
  },
  data() {
    return {
      typeId:2,
      baseImageUrl: process.env.VUE_APP_IMAGE_API,
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
      languageList:[
        {
          "name":"zh-CN",
        },
        {
          "name":"zh-HK",
        },
        {
          "name":"fil",
        },
        {
          "name":"pt-BR",
        },
      ],
      // 表单校验
      rules: {
        languageType: [
          { required: true, message: "请选择语言", trigger: "change" },
        ],
        contentInfo: [
          { required: true, message: "协议内容不能为空", trigger: "blur" },
        ],
      },
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
      agreementFindType(this.typeId).then(res =>{
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
      this.time = []
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
      this.dialogOpen = true;
      this.title = "编辑";
    },
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          if(this.formData.id){
            agreementUpdate(this.formData).then(res => {
              this.$modal.msgSuccess("编辑成功");
              this.dialogOpen = false;
              this.getList();
            });
          }else{
            this.formData.typeId = 2
            agreementAdd(this.formData).then(res => {
              this.$modal.msgSuccess("新增成功");
              this.dialogOpen = false;
              this.getList();
            });
          }
        }
      });
    },
  
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除？').then(function () {
        return agreementDelete(row.id);
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
