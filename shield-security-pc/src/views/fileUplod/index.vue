<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="typeId">
              <el-select v-model="queryParams.typeId" placeholder="请选择分类">
                    <el-option
                      v-for="item in typeList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
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
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:user:add']">上传资源</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="资源" align="center" width="180">
            <template slot-scope="scope">
              <div style="width: 90%; height: 80px; overflow: hidden; cursor: pointer;" @click="downloadFile(scope.row.filePath)">
                <img v-if="scope.row.imageLable" :src="scope.row.filePath" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
                <img v-else src="../../assets/images/wenjian.png" alt="图片" style="width: 100%; height: 100%; object-fit: contain;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="资源地址" :show-overflow-tooltip="true" align="center" prop="filePath"/>
          <el-table-column label="资源类型" align="center" prop="typeName" width = "180"/>
          <el-table-column label="资源目录" align="center" prop="fileCatalogue" width = "180"/>
          <el-table-column label="资源格式" align="center" prop="fileType"width = "180" />
          <el-table-column label="资源大小" align="center" prop="fileSize" width = "180"/>
          <el-table-column label="上传时间" align="center" prop="setTime" width = "180"/>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="600px" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="资源分类" prop="typeId">
                  <el-select v-model="formData.typeId" placeholder="请选择分类" style="width: 100%;">
                    <el-option
                      v-for="item in typeList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="文件" prop="fileList">
                  <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
                    :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho" :on-remove="handleRemoveEcho"
                    :file-list="fileList">
                    <el-button size="small" type="primary">点击上传</el-button>
                  </el-upload>
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
import { uploadList,uploadTypeList,fileUpload} from "@/api/fileUpload/fileUpload";
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
      formData: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      typeList:[],
      fileList:[],
      rules:{
        typeId: [
          { required: true, message: "请选择资源分类", trigger: "change" },
        ],
      }
    };
  },
  watch: {
  },
  created() {
    this.getList();
    this.getTypeList()
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
    //获取数据列表
    getList() {
      this.loading = true;
      uploadList(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
      })
    },
    //获取类型列表
    getTypeList(){
      uploadTypeList().then(res =>{
        this.typeList = res.data
      })
    },
    downloadFile(data){
      window.open(data)
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
    //重置按钮
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
    handleAdd(row) {
      this.reset();
      this.dialogOpen = true;
      this.title = "上传资源";
    },
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          let dataaInfo = new FormData()
          if(this.fileList.length>0){
            this.fileList.forEach((val, index) => {
              dataaInfo.append("file", val.raw);
            });
          }else{
            this.$message.error('请上传文件');
            return
          }
          dataaInfo.append("typeId",this.formData.typeId)
          fileUpload(dataaInfo).then(res => {
            this.$modal.msgSuccess("上传成功");
            this.dialogOpen = false;
            this.getList();
          });
        }
      });
    },

    handleRemoveEcho(file, filesList) {
      this.fileList = filesList;
    },
    handlePictureCardPreviewEcho(file) {

    },
    handleChangeEcho(file, filesList) {
      this.fileList = filesList;
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
