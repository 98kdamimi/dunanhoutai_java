<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <el-row style="margin-bottom: 20px;">
          <el-col :span="24" style="text-align: right;">
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-show="this.addState">
              添加
            </el-button>
          </el-col>
        </el-row>
        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="节点地址" align="center" prop="tronFullhost" />
          <el-table-column label="节点APIKEY" align="center" prop="tronApikey" />
          <el-table-column label="质押钱包私钥" align="center" prop="tronPrivatekey" />
          <el-table-column label="质押钱包地址" align="center" prop="tronContractaddress" />
          <el-table-column label="创建时间" align="center" prop="createdAt" />
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="handleUpdate(scope.row)">编辑</el-button>
              <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>


        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="30%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="130px">
            <el-row>
              <el-form-item label="节点地址" prop="tronFullhost">
                <el-input v-model="formData.tronFullhost"  placeholder="请输入节点地址" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="节点APIKEY" prop="tronApikey">
                <el-input v-model="formData.tronApikey" placeholder="请输入节点APIKEY" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="质押钱包私钥" prop="tronPrivatekey">
                <el-input v-model="formData.tronPrivatekey"  placeholder="请输入质押钱包私钥" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="质押钱包地址" prop="tronContractaddress">
                <el-input v-model="formData.tronContractaddress" placeholder="请输入质押钱包地址" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import { addTronWebconfig, findTronWebconfig,deleteTronWebconfig } from "@/api/tronWebConfig/tronWebConfig";

import editor from '@/components/Editor/index.vue'
export default {
  name: "typesOfPoints",
  components: {
    editor
  },
  data() {
    return {
      // 用户表格数据
      dataList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogOpen: false,
      addState: true,
      loading: true,
      formData: {},
     
      // 表单校验
      rules: {
        freeNum: [
          { required: true, message: "请输入每日免费次数", trigger: "change" },
        ],
        invalidationTime: [
          { required: true, message: "请输入有效时长", trigger: "blur" },
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
      findTronWebconfig(this.queryParams).then(res => {
        this.dataList = res.data
        if (this.dataList.length > 0) {
          this.addState = false
        }
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
      row.invalidationTime = row.invalidationTime / 60000
      this.formData = row;
      this.dialogOpen = true;
      this.title = "编辑";
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          addTronWebconfig(this.formData).then(res => {
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
        return deleteTronWebconfig(row.id);
      }).then(() => {
        this.getList();
        window.location.reload(); // 刷新当前页面
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
