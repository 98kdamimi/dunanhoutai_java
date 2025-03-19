<template>
  <div class="u-padding-20">
    <div class="app-container white-bg u-padding-30">
      <div class="flex_sb">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch">
          <el-form-item prop="language">
            <el-input v-model="queryParams.ipSite" placeholder="请输入ip地址"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['system:menu:add']">新增</el-button>
          </el-col>
        </el-row>
      </div>

      <el-table :data="dataList" max-height="600" v-loading="loading">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="ip地址" align="center" prop="ipSite" :show-overflow-tooltip="true" />
        <el-table-column label="创建时间" align="center" prop="setTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
        :limit.sync="queryParams.pageSize" @pagination="getList" />

      <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body :close-on-click-modal="false">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="ip地址" prop="ipSite">
                <el-input v-model="form.ipSite" placeholder="请输入ip地址" style="width: 90%;" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { ipWhitelistList, ipWhitelistAdd, ipWhitelistDelete } from "@/api/ipWhitelist/ipWhitelist";

export default {
  name: "Menu",
  components: {

  },
  data() {
    return {
      userInfo: {},
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: "菜单名称不能为空", trigger: "blur" },
        ],

      },
    };
  },
  created() {
    this.getList();
  },
  methods: {

    getList() {
      this.loading = true;
      ipWhitelistList(this.queryParams).then(res => {
        this.dataList = res.data.list
        this.total = res.data.total
        this.loading = false
      })
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        icon: undefined,
        iconImg: undefined,
        language: undefined,
        menuName: undefined,
        toUrl: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.open = true;
      this.title = "新增";
    },


    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          ipWhitelistAdd(this.form).then((response) => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal
        .confirm('是否确认删除？')
        .then(function () {
          return ipWhitelistDelete(row.id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
    },
  },
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
