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
            <el-form-item label="" prop="title">
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
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:user:add']">添加</el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="中文消息标题" align="messageTitleZh" prop="messageTitleZh" />
          <el-table-column label="中文消息内容" align="messageContentZh" prop="messageContentZh" />
          <el-table-column label="英文消息标题" align="messageTitleEn" prop="messageTitleEn" />
          <el-table-column label="英文消息内容" align="messageContentEn" prop="messageContentEn" />
          <el-table-column label="发送时间" align="setTime" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope" v-if="scope.row.roleId !== 1">
            <el-button size="mini" type="text"  @click="pushZh(scope.row)">发送中文消息</el-button>
            <el-button size="mini" type="text"  @click="pushEl(scope.row)">发送英文消息</el-button>
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
                <el-form-item label="中文标题" prop="messageTitleZh">
                  <el-input v-model="formData.messageTitleZh" placeholder="请输入中文标题" style="width: 85%;" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="中文内容" prop="messageContentZh">
                  <el-input type="textarea" :rows="4" v-model="formData.messageContentZh" placeholder="请输入中文内容" style="width: 85%;" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="英文标题" prop="messageTitleEn">
                  <el-input v-model="formData.messageTitleEn" placeholder="请输入英文标题" style="width: 85%;" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="英文内容" prop="messageContentEn">
                  <el-input type="textarea" :rows="4" v-model="formData.messageContentEn" placeholder="请输入英文内容" style="width: 85%;" />
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
import { messageList,messageAdd,messageDelete,messagePushZh,messagePushEl} from "@/api/message/message";
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
      loading:true,
      formData: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      // 表单校验
      rules: {
        messageTitleZh: [
          { required: true, message: "中文标题不能为空", trigger: "change" },
        ],
        messageTitleEn: [
          { required: true, message: "英文标题不能为空", trigger: "blur" },
        ],
        messageContentZh: [
          { required: true, message: "中文内容不能为空", trigger: "blur" },
        ],
        messageContentEn: [
          { required: true, message: "英文内容不能为空", trigger: "change" },
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
    changeTime(e) {
      if (e) {
        this.queryParams.begTime = e[0]
        this.queryParams.endTime = e[1]
      } else {
        this.queryParams.begTime = ''
        this.queryParams.endTime = ''
      }
    },
    getList() {
      this.loading = true;
      messageList(this.queryParams).then(res =>{
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
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          messageAdd(this.formData).then(res => {
            console.log(res)
            this.$modal.msgSuccess("新增成功");
            this.dialogOpen = false;
            this.getList();
          });
        }
      });
    },
  
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除？').then(function () {
        return messageDelete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },

    //发送消息
    pushZh(row){
      this.$modal.confirm('是否确认发送？').then(function () {
        return messagePushZh(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("发送成功");
      }).catch(() => { });
    },

    pushEl(row){
      this.$modal.confirm('是否确认发送？').then(function () {
        return messagePushEl(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("发送成功");
      }).catch(() => { });
    }

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
