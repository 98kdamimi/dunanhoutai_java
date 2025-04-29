<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
          <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
            <el-form-item label="" prop="address">
              <el-input v-model="queryParams.address" placeholder="请输入钱包地址"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-show="this.addState">
                添加
             </el-button>
            </el-col>
          </el-row>
        </div>

        <el-table :data="dataList" style="width: 100%" :row-key="row => row.id"  max-height="650">
          <el-table-column label="账户地址" align="center" prop="address"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createdAt"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="dialogOpen" width="30%" append-to-body>
          <el-form ref="formData" :model="formData" :rules="rules" label-width="130px">
            <el-row>
              <el-form-item label="账户地址" prop="address">
                <el-input v-model="formData.address"  placeholder="请输入账户地址" style="width: 90%;"></el-input>
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
</template>

<script>
import { addTronStaking,findTronStaking,deleteTronStaking} from "@/api/tronStaking/tronStaking";

export default {
  name: "typesOfPoints",
  data() {
    return {
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogOpen: false,
      addState: true,
      loading: true,
      formData: {},
      // 表单校验
      rules: {
        address: [
          { required: true, message: "请输入质押账户地址", trigger: "change" },
        ],
      },
      loading: true,
      dialogOpen: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
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
      findTronStaking(this.queryParams).then(res => {
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
          addTronStaking(this.formData).then(res => {
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
        return deleteTronStaking(row.id);
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
