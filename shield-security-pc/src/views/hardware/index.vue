<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="messageTitleZh">
              <el-select v-model="queryParams.status" placeholder="请选择状态">
                <el-option v-for="item in statusList" :key="item.id" :label="item.value" :value="item.id">
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
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">发布</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="加载程序版本" align="center" prop="releaseState">
            <template slot-scope="scope">
              {{ scope.row.hardware.bootloaders[0].version }}
            </template>
          </el-table-column>
          <el-table-column label="加载程序下载地址" align="center" prop="releaseState">
            <template slot-scope="scope">
              {{ scope.row.hardware.bootloaders[0].url }}
            </template>
          </el-table-column>
          <el-table-column label="固件版本" align="center" prop="releaseState">
            <template slot-scope="scope">
              {{ scope.row.hardware.firmwares[0].version }}
            </template>
          </el-table-column>
          <el-table-column label="固件下载地址" align="center" prop="releaseState">
            <template slot-scope="scope">
              {{ scope.row.hardware.firmwares[0].url }}
            </template>
          </el-table-column>

          <el-table-column label="发行状态" align="center" prop="releaseState">
            <template slot-scope="scope">
              <span v-if="scope.row.hardware.onlineState == 1" style="color: red;">下线</span>
              <span v-if="scope.row.hardware.onlineState == 2" style="color: green;">上线</span>
              <span v-if="scope.row.hardware.onlineState == 0" style="color: blue;">待上线</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="primary" v-if="scope.row.hardware.onlineState == 0"
                @click="online(scope.row)">上线</el-button>
            </template>
          </el-table-column>

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="dialogOpen" width="600px" append-to-body>
      <el-form ref="formData" :model="formData" :rules="rules" label-width="140px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="版本号" prop="hardwareVersion">
              <el-input v-model="formData.hardwareVersion" placeholder="请输入安卓版本号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="固件资源上传" prop="fileList">
              <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
                :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho" :on-remove="handleRemoveEcho"
                accept=".zip" :file-list="fileList">
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="引导程序上传" prop="fileTwoList">
              <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
                :on-change="handleChangeEchoTwo" :on-preview="handlePictureCardPreviewEchoTwo"
                :on-remove="handleRemoveEchoTwo" accept=".zip" :file-list="fileTwoList">
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm" v-debounce>确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { hardwareFindList, versionOffline, onlineHardware, releaseHardwareVersion } from "@/api/version/version";
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
      loading: true,
      fileList: [],
      fileTwoList: [],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      statusList: [
        {
          "id": 0,
          "value": "待上线"
        },
        {
          "id": 1,
          "value": "下线"
        },
        {
          "id": 2,
          "value": "上线"
        },
      ],
      rules: {
        hardwareVersion: [
          {
            required: true,
            message: "请填写版本号",
            trigger: "change"
          },
          {
            pattern: /^\d+\.\d+\.\d+$/,  // 版本号的正则表达式
            message: "版本号格式不正确",
            trigger: "blur"  // 当输入框失去焦点时触发验证
          }
        ]
      }
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
      hardwareFindList(this.queryParams).then(res => {
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
    handleUpdate(row) {
      this.reset();
      this.formData = JSON.parse(JSON.stringify(row));
      this.dialogOpen = true;
      this.title = "编辑";
    },


    //上线
    online(row) {
      this.$modal.confirm('是否确认上线？').then(function () {
        return onlineHardware(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上线成功");
      }).catch(() => { });
    },

    //下线
    offline(row) {
      this.$modal.confirm('是否确认下线此版本？').then(function () {
        return versionOffline(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("下线成功");
      }).catch(() => { });
    },

    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.dialogOpen = true;
      this.title = "上传资源";
    },

    /** 提交按钮 */
    submitForm: function () {
      const loadingOpen = this.$loading({
        lock: true,
        text: '资源上传中，请稍后.....',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.$refs["formData"].validate(valid => {
        if (valid) {
          let dataaInfo = new FormData()
          if (this.fileList.length > 0) {
            this.fileList.forEach((val, index) => {
              dataaInfo.append("firmwareFile", val.raw);
            });
          } else {
            loadingOpen.close()
            this.$message.error('请上传固件资源');
            return
          }
          if (this.fileTwoList.length > 0) {
            this.fileTwoList.forEach((val, index) => {
              dataaInfo.append("bootloaderFile", val.raw);
            });
          } else {
            loadingOpen.close()
            this.$message.error('请上传引导程序');
            return
          }
          dataaInfo.append("dataStr", JSON.stringify(this.formData))
          releaseHardwareVersion(dataaInfo).then(res => {
            this.$modal.msgSuccess("上传成功");
            loadingOpen.close()
            this.dialogOpen = false;
            this.getList();
          }).catch(error => {
            // 错误时关闭 loading
            this.$message.error('上传失败');
            loadingOpen.close();
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

    handleRemoveEchoTwo(file, fileTwoList) {
      this.fileTwoList = fileTwoList;
    },
    handlePictureCardPreviewEchoTwo(file) {

    },
    handleChangeEchoTwo(file, fileTwoList) {
      this.fileTwoList = fileTwoList;
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
