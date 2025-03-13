<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <el-row style="margin-bottom: 20px;">
          <el-col :span="24" style="text-align: right;">
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-if="this.addState">
              添加
            </el-button>
          </el-col>
        </el-row>
        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <!-- <el-table-column label="每日免费次数" align="center" prop="freeNum" /> -->
          <el-table-column label="委托类型" align="center" prop="energyType">
            <template slot-scope="scope">
              <span v-if="scope.row.energyType === 'BANDWIDTH'">带宽</span>
              <span v-else>能量</span>
            </template>
          </el-table-column>
          <el-table-column label="每笔能量" align="center" prop="everyTrxNum" />
          <el-table-column label="每日上限" align="center" prop="maxTrxNum" />
          <el-table-column label="有效时长" align="center" prop="invalidationTime">
            <template slot-scope="scope">
              {{ scope.row.invalidationTime / 60000 }} 分钟
            </template>
          </el-table-column>

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
            <!-- <el-row>
              <el-form-item label="每日免费次数" prop="freeNum">
                <el-input v-model="formData.freeNum" type="number" placeholder="请输入免费次数" style="width: 90%;"></el-input>
                <span style="margin-left: 10px;">次</span>
              </el-form-item>
            </el-row> -->
            <el-row>
              <el-form-item label="委托类型" prop="energyType">
                <el-select v-model="formData.energyType" filterable  placeholder="请选择委托类型"  style="width: 90%;" >
                    <el-option
                      v-for="item in energyTypeList"
                      :key="item.name"
                      :label="item.name"
                      :value="item.value">
                    </el-option>
                  </el-select>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="每笔委托能量" prop="everyTrxNum">
                <el-input v-model="formData.everyTrxNum" type="number" placeholder="请输入每笔委托能量" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="能量上限" prop="maxTrxNum">
                <el-input v-model="formData.maxTrxNum" type="number" placeholder="请输入能量上限" style="width: 90%;"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="委托有效时长" prop="invalidationTime">
                <el-input v-model="formData.invalidationTime" type="number" placeholder="请输入有效时长"
                  style="width: 90%;"></el-input>
                <span style="margin-left: 10px;">分钟</span>
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
import { addDelegateConfig, findDelegateConfig,deleteDelegateConfig } from "@/api/delegateConfig/delegateConfig";

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
      energyTypeList:[
        {
          "name":"带宽",
          "value":"BANDWIDTH"
        },
        {
          "name":"能量",
          "value":"ENERGY"
        }
      ],
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
      findDelegateConfig(this.queryParams).then(res => {
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
          addDelegateConfig(this.formData).then(res => {
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
        return deleteDelegateConfig(row.id);
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
