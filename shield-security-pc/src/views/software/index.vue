<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams"  size="small" :inline="true" label-width="68px">
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
          <el-table-column label="IOS版本" align="center">
            <template slot-scope="scope">
             {{ scope.row.software.ios.version }}
            </template>
          </el-table-column>
          <el-table-column label="IOS地址" align="center">
            <template slot-scope="scope">
              {{ scope.row.software.ios.url }}
            </template>
          </el-table-column>
          <el-table-column label="安卓版本" align="center">
            <template slot-scope="scope">
              {{ scope.row.software.android.version }}
            </template>
          </el-table-column>
          <el-table-column label="安卓地址" align="center" >
            <template slot-scope="scope">
              {{ scope.row.software.android.url }}
            </template>
          </el-table-column>
          <el-table-column label="谷歌版本" align="center">
            <template slot-scope="scope">
              {{ scope.row.software.android.google.version }}
            </template>
          </el-table-column>
          <el-table-column label="谷歌地址" align="center" prop="releaseState">
            <template slot-scope="scope">
              {{ scope.row.software.android.google.url }}
            </template>
          </el-table-column>
          <el-table-column label="发行状态" align="center" prop="releaseState">
            <template slot-scope="scope">
              <span v-if="scope.row.software.onlineState == 1" style="color: red;">下线</span>
              <span v-if="scope.row.software.onlineState == 2" style="color: green;">上线</span>
              <span v-if="scope.row.software.onlineState == 0" style="color: blue;">待上线</span>
            </template>
          </el-table-column>
          <el-table-column label="是否强制更新" align="center" prop="releaseState">
            <template slot-scope="scope">
              <span v-if="scope.row.forceUpdateLable == 1" style="color: red;">是</span>
              <span v-else style="color: blue;">否</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="320">
            <template slot-scope="scope">
              <el-button type="primary" v-if="scope.row.software.onlineState == 0" @click="forceOnline(scope.row)">强制更新上线</el-button>
              <el-button type="success" v-if="scope.row.software.onlineState == 0" @click="online(scope.row)">普通更新上线</el-button>
            </template>
          </el-table-column>

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
      <!-- 添加或修改用户配置对话框 -->
      <el-dialog :title="title" :visible.sync="dialogOpen" width="700px" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="140px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="IOS版本号" prop="iosVersion">
                  <el-input v-model="formData.iosVersion" placeholder="请输入IOS版本号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="IOS下载地址" prop="iosUrl">
                  <el-input v-model="formData.iosUrl" placeholder="请输入IOS地址"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="googlePlay地址" prop="googlePlayUrl">
                  <el-input v-model="formData.googlePlayUrl" placeholder="请输入googlePlay地址"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="安卓版本号" prop="androidVersion">
                  <el-input v-model="formData.androidVersion" placeholder="请输入安卓版本号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="安卓apk上传" prop="fileList">
                  <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
                    :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho" :on-remove="handleRemoveEcho"
                    accept=".apk" :file-list="fileList">
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
import { versionList,versionOffline,versionOnline,releaseVersion} from "@/api/version/version";
export default {
  name: "typesOfPoints",
  data() {
    return {
      baseImageUrl: process.env.VUE_APP_IMAGE_API,
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      loading:true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogOpen: false,
      formData: {
        iosVersion:"",
        iosUrl:"",
        androidVersion:"",
        googlePlayUrl:""
      },
      fileList:[],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      statusList:[
        {
          "id":0,
          "value":"待上线"
        },
        {
          "id":1,
          "value":"下线"
        },
        {
          "id":2,
          "value":"上线"
        },
      ],
      rules:{
        iosVersion: [
          { required: true, message: "请填写ios版本号", trigger: "change" },
          {
            pattern: /^\d+\.\d+\.\d+$/,  // 版本号的正则表达式
            message: "版本号格式不正确",
            trigger: "blur"  // 当输入框失去焦点时触发验证
          }
        ],
        iosUrl: [
          { required: true, message: "请填写ios下载地址", trigger: "change" },
        ],
        androidVersion: [
          { required: true, message: "请填写安卓版本号", trigger: "change" },
          {
            pattern: /^\d+\.\d+\.\d+$/,  // 版本号的正则表达式
            message: "版本号格式不正确",
            trigger: "blur"  // 当输入框失去焦点时触发验证
          }
        ],
        googlePlayUrl: [
          { required: true, message: "请填写谷歌下载地址", trigger: "change" },
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
    
    getList() {
      this.loading = true;
      versionList(this.queryParams).then(res =>{
        console.log(res)
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
   

    //强制更新上线
    forceOnline(row){
      this.$modal.confirm('是否确认上线？').then(function () {
        return versionOnline(row.id,1);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上线成功");
      }).catch(() => { });
    },

    //普通上线
    online(row){
      this.$modal.confirm('是否确认上线？').then(function () {
        return versionOnline(row.id,2);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上线成功");
      }).catch(() => { });
    },

    //下线
    offline(row){
      this.$modal.confirm('是否确认下线此版本？').then(function () {
        return versionOffline(row.id);
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
          if(this.fileList.length>0){
            this.fileList.forEach((val, index) => {
              dataaInfo.append("file", val.raw);
            });
          }else{
            loadingOpen.close()
            this.$message.error('请上传文件');
            return
          }
          dataaInfo.append("dataStr",JSON.stringify(this.formData))
          releaseVersion(dataaInfo).then(res => {
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
