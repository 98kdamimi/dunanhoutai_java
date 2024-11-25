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
         <!-- <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:user:add']">添加</el-button>
            </el-col>
          </el-row>-->
        </div>

        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="地址" align="path" prop="path" />
          <el-table-column label="状态" align="status" prop="status" />
          <el-table-column label="版本" align="version" prop="version" />
          <el-table-column label="描述" :show-overflow-tooltip="true" align="remark" prop="remark" />
          <el-table-column label="创建时间" align="createdAt" prop="createdAt" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text" style="color: lawngreen;" v-if="scope.row.status == 'OFFLINE'" @click="online(scope.row)">上线</el-button>
              <el-button type="text" style="color: red;" v-if="scope.row.status == 'ONLINE'" @click="offline(scope.row)">下线</el-button>
              <el-button type="text" @click="handleUpdate(scope.row)">编辑</el-button>
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
                <el-form-item label="地址" prop="path">
                  <el-input v-model="formData.path" placeholder="请输入中文标题" style="width: 85%;" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="formData.status" placeholder="请选择状态"  style="width: 85%;" >
                    <el-option
                      v-for="item in statusList"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
             
              <el-col :span="24">
                <el-form-item label="版本" prop="version">
                  <el-input v-model="formData.version" placeholder="请输入英文标题" style="width: 85%;" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="描述" prop="remark">
                  <el-input type="textarea" :rows="4" v-model="formData.remark" placeholder="请输入英文内容" style="width: 85%;" />
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
import { dappList,dappUpdata,dappOnline,dappOffline} from "@/api/dapp/dapp";
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
      statusList:[
        {
          "id":"ONLINE",
          "value":"ONLINE"
        },
        {
          "id":"OFFLINE",
          "value":"OFFLINE"
        },
      ],
      rules:{

      }
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
      dappList(this.queryParams).then(res =>{
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
   
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["formData"].validate(valid => {
        if (valid) {
          console.log(this.formData)
          dappUpdata(this.formData).then(res => {
            console.log(res)
            this.$modal.msgSuccess("新增成功");
            this.dialogOpen = false;
            this.getList();
          });
        }
      });
    },

    //上线
    online(row){
      this.$modal.confirm('是否确认上线？').then(function () {
        return dappOnline(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上线成功");
      }).catch(() => { });
    },

    //下线
    offline(row){
      this.$modal.confirm('是否确认下线此版本？').then(function () {
        return dappOffline(row.id);
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
