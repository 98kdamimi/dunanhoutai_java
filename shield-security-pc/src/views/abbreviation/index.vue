<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <el-form-item label="语言" prop="language">
              <el-select v-model="queryParams.language" filterable  placeholder="请选择语言"  style="width: 100%;" >
                <el-option
                  v-for="item in languageList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
                </el-option>
                  </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
                添加
              </el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="联系方式" align="center" prop="linkName" :show-overflow-tooltip="true" width="550"/>
          <el-table-column label="语言" align="center" prop="language" :show-overflow-tooltip="true"/>
          <el-table-column label="跳转地址" align="center" prop="toUrl" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
              <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />


        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="25%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="语言" prop="language">
                  <el-select v-model="formData.language" filterable  placeholder="请选择语言"  style="width: 90%;" >
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

            <el-row style="margin-top: 15px;">
              <el-col :span="24">
                <el-form-item label="联系方式" prop="linkName">
                    <el-input v-model="formData.linkName" placeholder="请输入联系方式" style="width: 90%;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row style="margin-top: 15px;">
              <el-col :span="24">
                <el-form-item label="跳转地址" prop="toUrl">
                    <el-input v-model="formData.toUrl" placeholder="请输入跳转地址" style="width: 90%;"></el-input>
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
import {carouseGetList,carouseAdd,carouseDelete,carouseUpdate} from "@/api/carouse/carouse";
import {abbreviationAdd,abbreviationUpdate,abbreviationlist,abbreviationDelete} from "@/api/abbreviation/abbreviation";
import {getLanguage} from "@/api/dic/dic";
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
      languageList:[],
      sortTypeList:[
        {
          "id":"1",
          "name":"轮播",
        },{
          "id":"2",
          "name":"广告位",
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
    this.getLanguageList()
    this.getList();
  },
  methods: {
    
    getLanguageList(){
      getLanguage().then(res =>{
        this.languageList = res.data
      })
    },
   
    getList() {
      this.loading = true;
      abbreviationlist(this.queryParams).then(res =>{
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
      this.dialogOpen = true;
      this.title = "编辑";
    },

   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          if(this.formData.id){//修改
            abbreviationUpdate(this.formData).then(res => {
                this.$modal.msgSuccess("编辑成功");
                this.dialogOpen = false;
                this.getList();
            });
          }else{//新增
            abbreviationAdd(this.formData).then(res => {
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
        return abbreviationDelete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },


  }
};
</script>
<style scoped>
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

::v-deep.hide .el-upload--picture-card {
  display: none;
}

::v-deep .el-upload-list--picture-card .el-upload-list__item {
  width: 90% !important;
}
</style>
