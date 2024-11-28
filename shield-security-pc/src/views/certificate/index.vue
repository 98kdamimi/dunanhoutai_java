<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="typeId">
              <el-input v-model="queryParams.serialNumber" placeholder="请输入设备序列号"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">导入证书</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" icon="el-icon-plus" @click="handleOpen">批量导入</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="设备序列号" align="center" prop="serialNumber"/>
          <el-table-column label="证书名称" align="center" prop="fileName"/>
          <el-table-column label="证书大小" align="center" prop="fileSize" />
          <el-table-column label="上传时间" align="center" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180"> 
            <template slot-scope="scope">
              <el-button type="text" @click="downloadFile(scope.row)">下载证书</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="600px" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备序列号" prop="typeId">
                  <el-input v-model="formData.serialNumber" placeholder="请输入设备序列号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="文件" prop="fileList">
                  <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
                    :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho" :on-remove="handleRemoveEcho"
                    accept=".pem" :file-list="fileList">
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

    <el-dialog title="导入数据" :visible.sync="fileOpen" width="20%" @close="handleClose">
      <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
        :on-change="handleChangeEchoEx"  :on-remove="handleRemoveEcho"
        accept=".xlsx,.xls" :file-list="excleFileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传.xlsx文件</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="fileOpen = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { 
  certificateList,
  certificateUpload,
  certificateDownload,
  certificateUploadExcle,
  certificateDelete
} from "@/api/certificate/certificate";
export default {
  name: "certificate",
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
      fileOpen: false,
      excleFileList:[],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      typeList:[],
      fileList:[],
      rules:{
        serialNumber: [
          { required: true, message: "请填写设备序列号", trigger: "change" },
        ],
      }
    };
  },
  watch: {
  },
  created() {
    this.getList();
  },
  methods: {
    //获取数据列表
    getList() {
      certificateList(this.queryParams).then(res =>{
        this.dataList = res.data.list
        this.total = res.data.total
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
    //重置按钮
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
              dataaInfo.append("pemCertificateFile", val.raw);
            });
          }else{
            this.$message.error('请上传文件');
            return
          }
          dataaInfo.append("serialNumber",this.formData.serialNumber)
          certificateUpload(dataaInfo).then(res => {
            this.$modal.msgSuccess("上传成功");
            this.dialogOpen = false;
            this.getList();
          });
        }
      });
    },

    //下载证书
    downloadFile(data) {
        certificateDownload(data.id).then((res) => {
					const blob = new Blob([res]);
					const link = document.createElement("a");
					link.href = window.URL.createObjectURL(blob);
					link.download = data.fileName;
					link.click();
					loading.close();
				}).catch((error) => {
          
				});
    },

      /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除？').then(function () {
        return certificateDelete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },

    //批量导入打开
    handleOpen(){
      this.fileOpen = true
    },

    //导入框关闭
    handleClose(){
      this.fileOpen = false
      this.excleFileList = []
    },

    handleRemoveEchoEx(file, excleFileList) {
      this.excleFileList = excleFileList;
    },

    handleChangeEchoEx(file, excleFileList) {
        this.excleFileList = excleFileList;
    },
    //导入上传
    handleSubmit() {
      console.log(this.excleFileList)
        if (!this.excleFileList.length) {
          this.$message.error('请上传文件');
          return
        }
        let formData = new FormData()
        formData.append('file', this.excleFileList[0].raw)
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(83, 83, 83, 0.7)'
        });
        certificateUploadExcle(formData).then(res => {
          loading.close();
          this.fileOpen = false
          this.getList();
          this.$message({
            type: 'success',
            message: '操作成功'
          });
        }).catch(error => {
          loading.close();
        })
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
