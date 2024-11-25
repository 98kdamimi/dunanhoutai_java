<template>
  <div class="u-padding-20">
    <div class="app-container u-padding-30">
      <div>
        <el-row>
          <el-col :span="12">
            <div class="yemiantitle">用户管理</div>
          </el-col>
          <el-col :span="12">
            <div style="float: right">
              <el-button class="xinzengwz" type="primary" icon="el-icon-plus" size="medium"
                style="width: 100px; font-size: 14px" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="0">
          <div>
            <div style="display: flex; justify-content: space-between">
              <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
                <el-form-item>
                  <el-select v-model="deptName" placeholder="请选择店铺名称" clearable style="width: 150px"
                    @change="handleNodeClick" @clear="dianclear">
                    <el-option v-for="item in storeList" :key="item.id" :label="item.storeName" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item prop="role">
                  <el-select  v-model="queryParams.roleId" size="medium" collapse-tags multiple placeholder="请选择角色" style="width: 300px">
                    <el-option v-for="item in sxroleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item prop="role">
                  <el-select clearable v-model="queryParams.userState" size="medium"   placeholder="请选择工作状态" style="width: 300px">
                    <el-option v-for="item in [{value:1,title:'在职'},{value:2,title:'离职'}]" :key="item.value" :label="item.title" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label-width="120px">
                  <el-input v-model="usernameTel" placeholder="请输入用户名/手机号" clearable size="medium" style="width: 300px"
                    @keyup.enter.native="handleQuery" @clear="inputclear" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" size="medium" style="width: 100px; font-size: 14px"
                    @click="handleQuery">搜索</el-button>
                </el-form-item>
              </el-form>
            </div>

            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <span class="totalming">共有{{ total }}名员工</span>
              </el-col>

              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </el-row>
            <el-table v-tableHeight="{ bottomOffset: 50 }" v-loading="loading" :data="userList"
              @selection-change="handleSelectionChange" :cell-style="tableCellStyle"
              :header-cell-style="tableHeaderStyle">
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column prop="username" width="150" label="用户名" align="center">
              </el-table-column>
              <el-table-column prop="acctive" width="150" label="账号" align="center">
              </el-table-column>
              <el-table-column prop="创建人" label="角色" align="center" width="150">
                <template slot-scope="{ row, $index }">
                  <span v-for="(item, index) in row.roleList" :key="index">
                    {{ item.roleName + "," }}
                  </span>
                </template>
              </el-table-column>

              <el-table-column label="店铺" align="center" width="350">
                <template slot-scope="{ row, $index }">
                  <span v-for="(item, index) in row.storeList" :key="index">
                    {{ item.storeName + "," }}
                  </span>
                </template>
              </el-table-column>

              <el-table-column prop="tel" width="150" label="手机号" align="center">
              </el-table-column>
              <el-table-column prop="userStateNmae" label="用户状态" align="center" width="150">
              </el-table-column>
              <el-table-column prop="createTime" width="250" label="创建时间" align="center">
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right" width="200">
                <template slot-scope="scope" v-if="scope.row.userId !== 1">
                  <el-button size="mini" type="text" :disabled="scope.row.userState != 1" icon="el-icon-edit"
                    @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']">修改</el-button>

                  <el-button size="mini" type="text" icon="el-icon-turn-off"
                    @click="handleAuthRole(scope.row)" v-hasPermi="['system:user:edit']"> {{ scope.row.userState == 1 ? "离职" : "正常" }}</el-button>

                  <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                    v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                    <span class="el-dropdown-link">
                      <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                        v-hasPermi="['system:user:resetPwd']" :disabled="scope.row.userState != 1">
                        重置密码</el-dropdown-item>

                      <el-dropdown-item command="handleDelete" icon="el-icon-delete"
                        v-hasPermi="['system:user:remove']">
                        删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber"
              :limit.sync="queryParams.pageSize" @pagination="getList" />
          </div>
        </el-row>

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户昵称" prop="username">
                  <el-input v-model="form.username" placeholder="请输入用户昵称" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用户账户" prop="acctive">
                  <el-input v-model="form.acctive" placeholder="请输入用户账户" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所属店铺" prop="storeId">
                  <el-select v-model="form.storeId" multiple placeholder="请选择所属店铺" @change="dianpuchange" collapse-tags>
                    <el-option label='全部店铺' :value='0' @click.native='addSelectAll'></el-option>
                    <el-option v-for="item in storeList" :key="item.id" :label="item.storeName" :value="item.id"
                      :disabled="item.status == 1"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码" prop="tel">
                  <el-input v-model="form.tel" placeholder="请输入手机号码" maxlength="11" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="职位" prop="roleId">
                  <el-select v-model="form.roleId" multiple :disabled="position" placeholder="请选择职位">
                    <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId"
                      :disabled="item.status == 1"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>


        <!-- 用户导入对话框 -->
        <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body :close-on-click-modal="false">
          <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
            :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
            :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip text-center" slot="tip">
              <div class="el-upload__tip" slot="tip">
                <el-checkbox v-model="upload.updateSupport" />
                是否更新已经存在的用户数据
              </div>
              <span>仅允许导入xls、xlsx格式文件。</span>
              <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                @click="importTemplate">下载模板</el-link>
            </div>
          </el-upload>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitFileForm">确 定</el-button>
            <el-button @click="upload.open = false">取 消</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
    <el-dialog  title="选择离职时间"  :visible.sync="dialogTime"  width="20%"  class="timeDialog" destroy-on-close @close="lzTime=''">
      <el-date-picker  v-model="lzTime" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTime = false;">取 消</el-button>
        <el-button type="primary" @click="submitTime">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
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
    findNormalRole,findLowerRole
  } from "@/api/system/role";
  import {
    getToken
  } from "@/utils/auth";
  import {
    treeselect
  } from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  // import {
  //   ServerUser,
  //   initColumns
  // } from "@/utils/serverData/dic";
  import auth from "@/plugins/auth";
  // import {
  //   getStoreList
  // } from "@/api/system/store";
  // import {
  //   dataURLtoFile,
  //   dataURLtoBlob
  // } from "@/utils/fileUtil";
  // import {
  //   faceVerifyOnlyFile
  // } from "@/api/member/member";
  export default {
    name: "User",
    components: {
      Treeselect,
    },
    data() {
      return {
        memberFace: null,
        imgUrl: process.env.VUE_APP_IMAGE_API,
        dialogVisible: false,
        humanFace: null,
        userInfo: {},
        roleList: [],
        //店铺id
        storeIds: [],
        storeList: [],
        // 上级选择框
        superior: false,
        // 职位
        position: true,
        // 上级领导列表
        leaderList: [],
        // 离职按钮显示
        dimission: "",
        // 和数据库字段对应的用户数据
        ServerUser,
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
        // 日期范围
        dateRange: [],
        faceState:false,
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label",
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
          headers: {
            Authorization: "Bearer " + getToken(),
          },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/system/user/importData",
        },
        // 查询参数
        queryParams: {
          pageNumber: 1,
          pageSize: 10,
        },
        // 搜索绑定数据
        usernameTel: "",
        // 状态
        statusList: [{
            id: 1,
            typename: "正常",
          },
          {
            id: 2,
            typename: "离职",
          },
        ],
        // 表单校验
        rules: {
          username: [{
              required: true,
              message: "用户名称不能为空",
              trigger: "blur",
            },
            {
              min: 2,
              max: 20,
              message: "用户名称长度必须介于 2 和 20 之间",
              trigger: "blur",
            },
          ],
          acctive: [{
            required: true,
            message: "请输入用户账户",
            trigger: "blur",
          },   {
            validator: (rule, value, callback) => {
              var re = new RegExp("[\\u4E00-\\u9FFF]+","g");
              if (re.test(value)){
                callback(new Error("用户账户名不可存在中文字符"));
              }else{
                callback()
              }
            }
          }],
          storeId: [{
            required: true,
            message: "请选择所属店铺",
            trigger: "blur",
          }, ],
          tel: [
            {
              required: true,
              message: "请输入手机号码",
              trigger: "blur",
            },
            {
              validator: (rule, value, callback) => {
                if (value === "") {
                  callback(new Error("请输入手机号码"));
                } else {
                  if (!/^1[3-9]\d{9}$/.test(value)) {
                    callback(new Error("请输入正确的手机号码"));
                  } else {
                    callback();
                  }
                }
              },
            },
          ],
          roleId: [{
            required: true,
            message: "请选择职位",
            trigger: "blur",
          }, ],
          leaderId: [{
            required: true,
            message: "请选择上级领导",
            trigger: "blur",
          }, ],
        },
        dpIds:[],
        dialogTime:false,
        lzTime:'',
        nowData:null,
        dqRoleList:[],
        sxroleList:[]
      };
    },
    watch: {
      "form.storeId": {
        handler(newName, oldName) {
          if (this.form.storeId != "") {
            this.position = false;
            this.storeIds = newName;
          } else {
            this.position = true;
          }
        },
      },
      "form.roleId": {
        handler(newName, oldName) {
          if (this.form.roleId != "") {
            const obj = {
              roleList: newName,
              storeIdList: this.storeIds,
            };
            // this.findBiggestUpLevel(obj);
          } else {
            this.superior = false;
          }
        },
      },
    },
    created() {
      this.userInfo = this.$store.state.user.userInfo;
      this.dqRoleList = this.$store.state.user.userInfo.roleList.map(item=>item.roleId)
      this.userInfo.storeList.map((item) => {
        this.dpIds.push(item.id);
      });
      this.storeList = this.userInfo.storeList
      this.columns = initColumns(this.ServerUser);
      // this.findRoleList();
      this.findRoleListAll()
      // this.getStoreList();
      let root = auth.hasRoleAdmin("root");
      let admin = auth.hasRoleAdmin("admin");
      if (root || admin) {
        this.getList(1);
      } else {
        this.getList(2);
      }
    },
    mounted() {
      // 处理滚动条不一致
      const fixedWrapper = document.getElementsByClassName(
        "el-table__fixed-body-wrapper"
      );
      const bodyWrapper = document.getElementsByClassName(
        "el-table__body-wrapper"
      );
      if (fixedWrapper && bodyWrapper && fixedWrapper[0] && bodyWrapper[0]) {
        fixedWrapper[0].onscroll = function(e) {
          bodyWrapper[0].scrollTop = fixedWrapper[0].scrollTop;
        };
      }
    },
    methods: {
      //查询所有店铺
      getStoreList() {
        getStoreList().then((res) => {
          if (res.rtncode == 200) {

            this.storeList = res.data;
          }
        });
      },
      saveFile(val) {
        this.memberFace = val;
        this.faceState = true;
      },

      findRoleListAll() {
        findNormalRole().then((res) => {
          this.sxroleList = res.data;
        }).then(()=>{
          if(auth.hasRoleAdmin("admin")){
            this.roleList = this.sxroleList
          }else{
            findLowerRole({roleIdList:this.dqRoleList}).then((res) => {
              this.roleList = res.data;
            })
          }
        })
      },
      findRoleList() {

      },
      // 查找某最高职位上级人员列表
      findBiggestUpLevel(e) {
        findBiggestUpLevel(JSON.stringify(e)).then((res) => {
          if (res.data.length > 0) {
            this.superior = true;
            this.leaderList = res.data;
          } else {
            this.superior = false;
          }
        });
      },
      /** 查询用户列表 */
      getList(data) {
        this.loading = true;
        if (data == 2) {
          this.queryParams.leaderId = this.userInfo.id;
        }
        this.queryParams.userId = this.userInfo.id
        listUser(this.queryParams).then((response) => {
          this.userList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.storeId = [data];
        this.getList();
      },
      handleNodeClicks(data) {
        this.queryParams.userState = data;
        this.getList();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal
          .confirm('确认要"' + text + '""' + row.userName + '"用户吗？')
          .then(function() {
            return changeUserStatus(row.userId, row.status);
          })
          .then(() => {
            this.$modal.msgSuccess(text + "成功");
          })
          .catch(function() {
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
          tel: undefined,
          storeId: [],
          roleId: [],
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        if (this.usernameTel != "") {
          if (this.usernameTel.match(/^[0-9]{11}$/)) {
            this.queryParams.tel = this.usernameTel;
          } else {
            this.queryParams.username = this.usernameTel;
          }
        }
        this.queryParams.pageNumber = 1;
        this.getList();
      },
      // 输入框的点击叉号清空
      inputclear() {
        delete this.queryParams.tel;
        delete this.queryParams.username;
        this.getList();
      },
      // 店铺叉号清空
      dianclear() {
        delete this.queryParams.storeId;
        this.deptName = undefined;
        this.getList();
      },
      // 用户状态清空
      dianclears() {
        delete this.queryParams.userState;
        this.userState = undefined;
        this.getList();
      },

      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map((item) => item.id);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      // 更多操作触发
      handleCommand(command, row) {
        switch (command) {
          case "handleResetPwd":
            this.handleResetPwd(row);
            break;

          case "handleDelete":
            this.handleDelete(row);
            break;
          default:
            break;
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.memberFace = null;
        this.faceState = false;
        this.reset();
        this.open = true;
        this.title = "添加用户";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.faceState = false;
        const userId = row.userId || this.ids;
        this.open = true;
        this.title = "修改用户";
        this.memberFace = null;
        if (row.headPic) {
          this.memberFace = this.imgUrl + row.headPic;
        }
        row.storeId = row.storeList.map((item) => item.id);
        row.roleId = row.roleList.map((item) => item.roleId);
        this.form = JSON.parse(JSON.stringify(row));
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
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
      // 员工离职
      handleAuthRole(row) {
        if(row.userState == 1){//员工离职
          this.dialogTime = true
          this.nowData = row
        }else{
          this.nowData = row
          this.submitTime()
        }

      },
      submitTime(){
        let row = this.nowData
        if(row.userState == 1&&this.lzTime==''){
          this.$message.error('请选择离职时间')
          return
        }

        this.$modal
          .confirm('是否确认对"' + row.username + '"用户进行操作？')
          .then(()=> {
            updateUserState(row.id,row.userState == 1 ? "2" : "1",this.lzTime).then(res =>{
              this.$modal.msgSuccess("修改成功");
              this.getList();
              this.dialogTime = false
            })
          })
          .catch(() => {});

      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            if (this.form.id !== undefined) {
              let fordata = new FormData()
              if (this.faceState && this.memberFace) {
                let file = dataURLtoFile(this.memberFace, "face.jpg");
                fordata.append("file", file)
              }
              this.form.leaderId = this.userInfo.id;
              this.form.createUser = this.userInfo.id;
              fordata.append("user", JSON.stringify(this.form))
              window.openLoading.open();
              updateUser(fordata).then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
                if(window.openLoading.loading){window.openLoading.close()}
              });
            } else {
              let fordata = new FormData()
              if (this.faceState &&this.memberFace) {
                let file = dataURLtoFile(this.memberFace, "face.jpg");
                fordata.append("file", file)
              }
              this.form.leaderId = this.userInfo.id;
              this.form.createUser = this.userInfo.id;
              fordata.append("user", JSON.stringify(this.form))
              window.openLoading.open();
              addUser(fordata).then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                if(window.openLoading.loading){window.openLoading.close()}
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
          .then(function() {
            return delUser(userIds);
          })
          .then(() => {
            // this.queryParams.pageNumber = 1;
            this.getList();
            this.$modal.msgSuccess("删除成功");
          })
          .catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download(
          "system/user/export", {
            ...this.queryParams,
          },
          `user_${new Date().getTime()}.xlsx`
        );
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "用户导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download(
          "system/user/importTemplate", {},
          `user_template_${new Date().getTime()}.xlsx`
        );
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
        this.$alert(response.msg, "导入结果", {
          dangerouslyUseHTMLString: true,
        });
        this.getList();
      },
      // 店铺下拉
      addSelectAll(){
        if (this.form.storeId.length < this.$store.state.user.userInfo.storeList.length) {
          this.form.storeId = this.dpIds
          this.form.storeId[this.form.storeId.length] = 0
        } else {
          this.form.storeId = []
        }
        this.dianpuchange()
      },
      // 店铺下拉数据判断
      dianpuchange() {
        if(this.form.storeId.length == this.$store.state.user.userInfo.storeList.length&&!this.form.storeId.includes(0)){
          this.form.storeId[this.form.storeId.length] = 0
        }else if(this.form.storeId.includes(0)&&this.form.storeId.length!=this.$store.state.user.userInfo.storeList.length+1){
          let index = this.form.storeId.indexOf(0)
          this.form.storeId.splice(index,1)
        }
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
      // 表格样式
      tableCellStyle() {
        return `text-align:center;
                border-bottom: 1px solid #eef1f5;
                padding: 14px 0;`;
      },
      tableHeaderStyle({
        rowIndex
      }) {
        if (rowIndex === 0) {
          return `
              background-color: #fafafa;
              color: #000;
              font-weight: bold;
              font-size: 14px;
              text-align:center;
              border-bottom: 2px solid #ededed;
              `;
        }
      },
    },
  };
</script>

<style lang="scss" scoped>
  ::v-deep {
    .el-table__body-wrapper {
      overflow-y: auto;
      height: calc(100vh - 375px); // 表格固定了高度
    }

    .el-table__fixed-body-wrapper {
      overflow-y: auto; // fixed的列也要超出滚动
      height: calc(100vh - 385px); // 出现滚动条需要比表格短10px
    }
  }

  .app-container {
    background: #fff;
  }

  .yemiantitle {
    font-size: 24px;
    padding-bottom: 30px;
    font-weight: 500;
    color: #000000;
  }

  .totalming {
    font-size: 16px;
    color: #383838;
  }

  // .xinzengwz {
  // }
  /* ::-webkit-scrollbar-thumb {
  background-color: #a1a3a9;
  border-radius: 3px;
  width: 20px;
} */
</style>
