<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title" style="margin-bottom: 10px">欢迎登录</h3>
      <h3 class="title" style="">盾安项目管理平台</h3>
      <div class="inputBox">
        <el-form-item prop="username" style="padding-bottom: 30px;">
          <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="请输入登录账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password" style="padding-bottom: 30px;">
          <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="请输入密码"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="googleCode">
          <el-input v-model="loginForm.googleCode" type="text" auto-complete="off" placeholder="请输入谷歌验证码"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix"  icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
      </div>
      <!-- <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item> -->
      <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox> -->
      <el-form-item style="width:100%;padding: 0 51px;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;height: 60px;font-size: 18px;"
          @click.native.prevent="handleLogin">
          <span v-if="!loading">立 即 登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <!-- <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div> -->
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span></span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        googleCode: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        googleCode: [
          { required: true, trigger: "blur", message: "请输入谷歌验证码" }
        ],
        // code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: false,
      // 注册开关
      register: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    // this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      const googleCode = Cookies.get('googleCode')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        password: googleCode === undefined ? this.loginForm.googleCode : googleCode,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set("googleCode", encrypt(this.loginForm.googleCode), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
            Cookies.remove('googleCode');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { });
            this.loading = false;

          }).catch(() => {
            this.loading = false;

            if (this.captchaEnabled) {
              // this.getCode();
            }
          });
          setTimeout(()=>{
            this.loading = false;
          },3000)
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/pcbg.png");
  background-size: 100% 100%;
}

.title {
  margin: 0px auto 60px auto;
  text-align: center;
  font-size: 30px;
  font-family: PingFang SC;
  font-weight: bold;
  color: #303133;
}

.login-form {
  position: fixed;
  right: 170px;
  border-radius: 6px;
  background: #ffffff;
  width: 500px;
  height: 620px;
  // padding: 81px 51px 115px 51px;
  padding: 81px 0;

  box-shadow: 0 0 20px 1px rgba($color: #000000, $alpha: 0.3);
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}
</style>
<style >
 .login-form .el-input__inner {
  border: unset !important;
  border-bottom: 1px solid #D7D8D9!important;
  background:#ffffff!important;
  border-radius: 0!important;
  font-size: 16px!important;
}
.login-form .inputBox{
  padding: 0 51px;
  flex: 1;
}
.login-form .el-form-item{
  /* margin-bottom: 50px!important; */
}
</style>
