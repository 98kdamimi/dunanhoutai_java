<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
      @toggleClick="toggleSideBar" />
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav" />
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav" />

    <div class="right-menu">
      <div style="position: relative; display: inline-block;cursor: pointer;" @click="openMsg">
        <img src="../../assets/images/xiaoxi.png" style="width: 40px;height: 40px;margin-right: 25px;">
        <div
          style="position: absolute; top: 0; right: 12px; background-color: red; color: white; border-radius: 50%; width: 20px; height: 20px; text-align: center; line-height: 20px; font-size: 12px;">
          {{ msgNum }}
        </div>
      </div>
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <span>欢迎用户：{{ userInfo.username }}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <!-- <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>  -->
          <!-- divided -->
          <el-dropdown-item>
            <div @click="openUpGuge()">
              <span>修改谷歌密钥</span>
            </div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="openUpPassword()">
              <span>修改密码</span>
            </div>
          </el-dropdown-item>
          <el-dropdown-item @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog title="版本更新提醒" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-row>
          <el-col :span="24">
            <h2>您有新版本需要操作更新!!!</h2>
          </el-col>
        </el-row>
        <el-row style="font-size: 16px;line-height: 15px;margin-bottom: 15px;">
          <el-col :span="12">
            <span>IOS新版本：{{ iosVersion }}</span>
          </el-col>
          <el-col :span="12">
            <span>安卓新版本：{{ androidVersion }}</span>
          </el-col>
        </el-row>
        <el-row style="font-size: 16px;">
          <el-col :span="12">
            <span>google新版本：{{ googleVersion }}</span>
          </el-col>
          <el-col :span="12">
            <span>设备新版本：{{ firmwareVersion }}</span>
          </el-col>
        </el-row>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="closeMsg">取 消</el-button>
        <el-button type="primary" @click="toUpVersion">去更新</el-button>
      </span>
    </el-dialog>


    <el-dialog title="修改谷歌密钥" :visible.sync="gugeDialogVisible" width="30%">
      <el-form ref="gugeData" :model="gugeData" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="原谷歌密钥" prop="googleSecretkey">
              <el-input v-model="userInfo.googleSecretkey" style="width: 85%;" readonly ref="copyLodKey"/>
              <a href="#" style="margin-left: 20px;color: cornflowerblue;" @click="lodCopy(userInfo.googleSecretkey)">复制</a>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="新谷歌密钥" prop="googleSecretkey">
              <el-input v-model="googleSecretkey" style="width: 85%;" readonly ref="copyNewKey"/>
              <a href="#" style="margin-left: 20px; color: cornflowerblue;" @click="newCopy">复制</a>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="gugeCancel">取 消</el-button>
        <el-button type="primary" @click="gugeSubmitForm" v-debounce>确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="30%">
      <el-form ref="passwordData" :model="passwordData" :rules="passwordRules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordData.oldPassword" style="width: 85%;" type="password"  />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordData.newPassword" style="width: 85%;" type="password"  />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="passwordData.confirmPassword" style="width: 85%;" type="password"  />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="passwordCancel">取 消</el-button>
        <el-button type="primary" @click="passwordSubmitForm" v-debounce>确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import ImportFile from '@/components/ImportFile'
import { msgWarn, deleteMsg } from "@/api/version/version";
import Clipboard from "clipboard";
import { IssueGoogleSecretkey, upGoogleSecretkey,verifyPwd,resetPwd } from "@/api/system/user";


export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    ImportFile
  },
  data() {
    return {
      userInfo: null,
      msgNum: 0,
      dialogVisible: false,
      gugeDialogVisible: false,
      passwordDialogVisible:false,
      gugeData: {},
      passwordData:{},
      googleSecretkey: "",
      iosVersion: "",
      androidVersion: "",
      googleVersion: "",
      firmwareVersion: "",
      timer: null,
      passwordRules: {
        oldPassword: [
          { required: true, message: "请输入原密码", trigger: "blur" },
          { validator: this.validateOldPassword, trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请确认新密码", trigger: "blur" },
          { validator: this.validateConfirmPassword, trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  beforeDestroy() {
    // 组件销毁时清除定时器，防止内存泄漏
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  created() {
    this.userInfo = this.$store.state.user.userInfo;
    this.getMsgWarn()
    this.startMsgWarnTimer();
  },
  methods: {

    startMsgWarnTimer() {
      // 每隔 60 秒执行一次 getMsgWarn 方法
      this.timer = setInterval(() => {
        this.getMsgWarn();
      }, 60000); // 60000 毫秒 = 60 秒
    },

    getMsgWarn() {
      msgWarn().then(res => {
        if (res.data != null) {
          this.iosVersion = res.data.ios.version.join('.')
          this.androidVersion = res.data.android.version.join('.')
          this.googleVersion = res.data.android.google.version.join('.')
          this.firmwareVersion = res.data.digtalshield.firmware[0].version.join('.')
          this.msgNum = 1
        }
      })
    },

    closeMsg() {
      deleteMsg().then(res => {
        this.msgNum = 0
        this.dialogVisible = false
      })
    },

    openMsg() {
      if (this.msgNum > 0) {
        this.dialogVisible = true
      } else {
        this.$modal.msgError("没有新消息");
      }
    },

    toUpVersion() {
      const currentPath = this.$route.path;
      if (currentPath === '/version/software' || currentPath === '/version/hardware') {
        window.location.reload();  // 刷新当前页面
      } else {
        // 正常跳转
        this.$router.push({ path: "version/software" }).catch(() => { });
      }
      this.closeMsg()
      this.dialogVisible = false
    },

    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => { });
    },

    openUpGuge() {
      IssueGoogleSecretkey(this.userInfo.acctive).then(res => {
        this.googleSecretkey = res.data
      })
      this.gugeDialogVisible = true
    },

    gugeSubmitForm() {
      this.$modal.confirm('是否确认修改谷歌密钥？').then(() => {
        return upGoogleSecretkey(this.userInfo.id, this.googleSecretkey);
      }).then(() => {
        this.gugeDialogVisible = false
        this.$modal.msgSuccess("修改成功");
      }).catch(() => {
        // 这里可以捕获到错误并处理
      });
    },

    gugeCancel() {
      this.gugeDialogVisible = false
    },

    lodCopy(data){
      const clipboard = new Clipboard(this.$refs.copyLodKey.$el, {
        text: () => this.userInfo.googleSecretkey
      });
      clipboard.on("success", () => {
        this.$modal.msgSuccess("复制成功！");
      });

      clipboard.on("error", () => {
        this.$modal.msgError("复制失败，请手动复制！");
      });

      clipboard.onClick({ currentTarget: this.$refs.copyLodKey.$el });
      // try {
      //   // 使用 Clipboard API 进行复制
      //   navigator.clipboard.writeText(data);
      //   this.$modal.msgSuccess("复制成功");
      // } catch (err) {
      //   this.$modal.msgError("复制失败");
      // }
    },

    newCopy(data){
      const clipboard = new Clipboard(this.$refs.copyNewKey.$el, {
        text: () => this.googleSecretkey
      });
      clipboard.on("success", () => {
        this.$modal.msgSuccess("复制成功！");
      });

      clipboard.on("error", () => {
        this.$modal.msgError("复制失败，请手动复制！");
      });

      clipboard.onClick({ currentTarget: this.$refs.copyNewKey.$el });
      // try {
      //   // 使用 Clipboard API 进行复制
      //   navigator.clipboard.writeText(data);
      //   this.$modal.msgSuccess("复制成功");
      // } catch (err) {
      //   this.$modal.msgError("复制失败");
      // }
    },

    openUpPassword(){
      this.passwordData={}
      this.passwordDialogVisible = true
    },
    passwordSubmitForm(){
      this.$modal.confirm('是否确认修改密码？').then(() => {
        return resetPwd(this.userInfo.id, this.passwordData.newPassword);
      }).then(() => {
        this.passwordDialogVisible = false
        this.$modal.msgSuccess("修改成功");
      }).catch(() => {
        // 这里可以捕获到错误并处理
      });
    },
    passwordCancel(){
      this.passwordDialogVisible = false
    },

    validateOldPassword(rule, value, callback){
      verifyPwd(this.userInfo.id,value).then(res => {
        if(res.rtncode != 200){
          callback(new Error(res.msg));
        }else{
          callback();
        }
      })
    },

    validateConfirmPassword(rule, value, callback) {
      if (value !== this.passwordData.newPassword) {
        callback(new Error("两次输入的新密码不一致"));
      } else {
        callback();
      }
    },


  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

.topSearch {
  // position: absolute;
  // left: 50%;
  // transform: translateX(-50%);
  float: left;
  line-height: 50px;
  margin: 0 15px;
  margin-left: 100px;

}

.import {
  float: left;
  margin-left: 15px;
  line-height: 50px;
}
</style>
