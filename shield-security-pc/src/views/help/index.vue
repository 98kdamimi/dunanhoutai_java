<template>
  <div class="u-padding-20">
    <div class="app-container white-bg u-padding-30">
      <div class="flex_sb">
      <el-form :model="queryParams" ref="queryForm"  size="mini" :inline="true" v-show="showSearch">
        <el-form-item label="语言" prop="language">
          <el-select v-model="queryParams.language" filterable  placeholder="请选择语言"  style="width: 100%;" >
            <el-option
                  v-for="item in languageList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
            </el-option>
          </el-select>
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
          <el-table-column prop="icon" label="图标" align="center" width="100">
            <template slot-scope="scope">
              <svg-icon :icon-class="scope.row.iconImg" />
            </template>
          </el-table-column>
          <el-table-column label="菜单名称" align="center" prop="menuName" :show-overflow-tooltip="true"/>
          <el-table-column label="语言" align="center" prop="language" :show-overflow-tooltip="true"/>
          <el-table-column label="跳转地址" align="center" prop="toUrl" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="setTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text"  @click="handleUpdate(scope.row)">编辑</el-button>
              <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />

      <!-- 添加或修改菜单对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body :close-on-click-modal="false">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-row>
            <el-col :span="24">
                <el-form-item label="语言" prop="language">
                  <el-select v-model="form.language" filterable  placeholder="请选择语言"  style="width: 90%;" >
                    <el-option
                      v-for="item in languageList"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="24" v-if="form.menuType != 'F'">
              <el-form-item label="菜单图标" prop="icon">
                <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()" >
                  <IconSelect ref="iconSelect" @selected="selected" :active-icon="form.iconImg" />
                  <el-input slot="reference" v-model="form.iconImg" placeholder="点击选择图标" readonly style="width: 90%;" >
                    <svg-icon v-if="form.iconImg" slot="prefix" :icon-class="form.iconImg" style="width: 25px" />
                    <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                  </el-input>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单名称" prop="menuName">
                <el-input v-model="form.menuName" placeholder="请输入菜单名称" style="width: 90%;"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="跳转地址" prop="toUrl">
                <el-input v-model="form.toUrl" placeholder="请输入跳转地址" style="width: 90%;"/>
              </el-form-item>
            </el-col>
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
import {helpAdd,helpUpdate,helplist,helpDelete} from "@/api/help/help";
import {getLanguage} from "@/api/dic/dic";
import IconSelect from "@/components/IconSelect";

export default {
  name: "Menu",
  components: {  IconSelect },
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
      form: {
        icon:undefined,
        iconProps:undefined,
        language:undefined,
        menuName:undefined,
        toUrl:undefined
      },
       // 总条数
       total: 0,
      // 用户表格数据
      dataList: null,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
      },
      languageList:[],
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: "菜单名称不能为空", trigger: "blur" },
        ],
        orderNum: [
          { required: true, message: "菜单顺序不能为空", trigger: "blur" },
        ],
        path: [
          { required: true, message: "路由地址不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getLanguageList()
    this.getList();
  },
  methods: {
    // 选择图标
    selected(name) {
      console.log("*************************************")
      console.log(name)
      this.form.iconImg = name;
    },
    getLanguageList(){
      getLanguage().then(res =>{
        this.languageList = res.data
      })
    },
    getList() {
      this.loading = true;
      helplist(this.queryParams).then(res =>{
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
        icon:undefined,
        iconImg:undefined,
        language:undefined,
        menuName:undefined,
        toUrl:undefined
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
   
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.form = row;
      this.open = true;
      this.title = "编辑";
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            helpUpdate(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            helpAdd(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal
        .confirm('是否确认删除？')
        .then(function () {
          return helpDelete(row.id);
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
.flex_sb {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
