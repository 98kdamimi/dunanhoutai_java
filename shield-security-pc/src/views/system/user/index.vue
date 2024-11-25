<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <div class="flex_sb">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="" prop="username">
              <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable style="width: 240px"
                @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="" prop="tel">
              <el-input v-model="queryParams.tel" placeholder="请输入手机号码" clearable style="width: 240px"
                @keyup.enter.native="handleQuery" @input="queryParams.tel = queryParams.tel.replace(/\D/g, '')" />
            </el-form-item>
            <el-form-item label="" prop="roleId">
              <el-select v-model="queryParams.roleId" collapse-tags placeholder="请选择角色" clearable>
                <el-option v-for="item in sxroleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
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
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                v-hasPermi="['system:user:add']">新增</el-button>
            </el-col>
            <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar> -->
          </el-row>
        </div>
        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="序号" align="center" />
          <el-table-column label="用户账号" align="center" key="acctive" prop="acctive" v-if="columns[1].visible"
            :show-overflow-tooltip="true" />
          <el-table-column label="用户名称" align="center" key="username" prop="username" v-if="columns[2].visible"
            :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" key="tel" prop="tel" v-if="columns[4].visible" />
          <el-table-column prop="创建人" label="角色" align="center" width="150">
            <template slot-scope="{ row, $index }">
              <span v-for="(item, index) in row.roleList" :key="index">
                {{ item.roleName + "," }}
              </span>
            </template>
          </el-table-column>
         
          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']">删除</el-button>
              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                    v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户账号" prop="acctive">
              <el-input v-model="form.acctive" placeholder="请输入用户账号" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="tel">
              <el-input v-model="form.tel" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="roleId">
              <el-select v-model="form.roleId" size="medium" collapse-tags placeholder="请选择角色" multiple>
                <el-option v-for="item in sxroleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
                </el-option>
              </el-select>
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
</template>

<script>
// import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  resetUserPwd,
  changeUserStatus,
  findBiggestUpLevel,
  updateUserState
} from "@/api/system/user";
import {
  findNormalRole, findLowerRole, listRole
} from "@/api/system/role";
import { getToken } from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import uploadImg from '@/components/uploadImg/uploadImg.vue'
// import { listShop} from "@/api/system/kucun/shop";

export default {
  name: "User",
  // dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { Treeselect, uploadImg },
  data() {
    return {
      baseUrl: process.env.VUE_APP_IMAGE_API,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      roleIds: null,
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        username: undefined,
        tel: undefined,
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户名称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true },
        { key: 7, label: `店铺名称`, visible: true },
        { key: 8, label: `角色`, visible: true },


      ],
      // 表单校验
      rules: {
        username: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        acctive: [
          { required: true, message: "用户账号不能为空", trigger: "blur" }
        ],
        roleId: [{ required: true, message: "用户账号不能为空", trigger: "blur" }],
        tel: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      roleList: [],
      sxroleList: [],
      filesList: [],
      userInfo: null,
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  beforeDestroy() {
    // 在组件销毁前清除定时器
    // console.log("121221ddddd");
  },
  created() {
    this.userInfo = this.$store.state.user.userInfo;
    this.getList();
    this.findRoleListAll()
  },
  methods: {
    findRoleListAll() {
      findNormalRole().then((res) => {
        this.sxroleList = res.data;
      })
    },
    addrole(val) {
      this.form.roleIds = []
      this.form.roleIds.push(val)
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      let obj = Object.assign({}, this.queryParams)
      if (obj.roleId) {
        obj.roleId = [obj.roleId]
      } else {
        obj.roleId = undefined
      }
      for (let key in obj) {
        if (obj[key] == '') {
          obj[key] = undefined
        }
      }
      listUser(obj).then(response => {
        this.userList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      }
      );
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function () {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        username: undefined,
        acctive: undefined,
        tel: undefined,
        roleId: undefined
      };
      this.filesList = []
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      /* this.queryParams.deptId = undefined;
      this.$refs.tree.setCurrentKey(null); */
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户";
      // getUser().then(response => {
      //   this.postOptions = response.posts;
      //   this.roleOptions = response.roles;
      //   this.open = true;
      //   this.title = "添加用户";
      //   this.form.password = this.initPassword;
      // });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // console.log("121211");
      this.reset();
      this.filesList = row.headPic
      const userId = row.id || this.ids;
      // console.log(userId,"userIduserId");
      // getUser(userId).then(response => {
      // this.form = response.data;
      // this.postOptions = response.posts;
      // this.roleOptions = response.roles;
      // this.$set(this.form, "postIds", response.postIds);
      // this.$set(this.form, "roleIds", response.roleIds);
      // if (response.roleIds) {
      //   this.roleIds = response.roleIds[0]
      //   this.form.roleId = response.roleIds[0]
      // }
      // this.open = true;
      // this.title = "修改用户";
      // this.form.password = "";
      // });
      row.roleId = row.roleList.map((item) => item.roleId)
      this.form = JSON.parse(JSON.stringify(row));
      this.form.roleId = row.roleId
      this.open = true;
      this.title = "修改用户";
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      // this.$prompt('请输入"' + row.username + '"的新密码', "提示", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      //   closeOnClickModal: false,
      //   inputPattern: /^.{5,20}$/,
      //   inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      // }).then(({ value }) => {
      //   resetUserPwd(row.userId, value).then(response => {
      //     this.$modal.msgSuccess("修改成功，新密码是：" + value);
      //   });
      // }).catch(() => { });
      this.$confirm("此操作将重置" + row.username + "的密码是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          resetUserPwd(row.id).then((response) => {
            if (response.rtncode == 200) {
              this.$message({
                type: "success",
                message: response.msg,
              });
            }
          });

        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // this.form.roleIds[0] = this.form.roleId
          let fordata = new FormData()
          if (this.filesList && this.filesList.length > 0 && this.filesList[0].raw) {
            this.filesList.forEach((val, index) => {
              fordata.append("file", val.raw);
            });
          }
          this.form.createUser = this.userInfo.id;
          fordata.append("user", JSON.stringify(this.form))
          if (this.form.id != undefined) {
            updateUser(fordata).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(fordata).then(response => {
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
      const userIds = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除用户编号为"' + userIds + '"的数据项？')
        .then(function () {
          return delUser(userIds);
        })
        .then(() => {
          // this.queryParams.pageNumberber = 1;
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    dictSet(value) {
      // console.log("1",value,this.shopList);
      return this.shopList.filter(item => item.shopId == value)[0].shopName
    }
  }
};
</script>
<style>
.flex_sb {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
