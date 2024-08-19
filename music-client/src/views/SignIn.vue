<template>
  <yin-login-logo></yin-login-logo>
  <div class="sign">
    <div class="sign-head">
      <span>Login</span>
    </div>
    <el-form
      ref="signInForm"
      status-icon
      :model="registerForm"
      :rules="SignInRules"
    >
      <el-form-item prop="username">
        <el-input
          placeholder="Username"
          v-model="registerForm.username"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          placeholder="Password"
          v-model="registerForm.password"
          @keyup.enter="handleLoginIn"
        ></el-input>
      </el-form-item>
      <el-form-item class="sign-btn">
        <el-button @click="handleSignUp">Sign Up</el-button>
        <el-button type="primary" @click="handleLoginIn">Login</el-button>
        <el-button @click="handleForgotPassword">Forgot Password</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, getCurrentInstance } from "vue";
import mixin from "@/mixins/mixin";
import YinLoginLogo from "@/components/layouts/YinLoginLogo.vue";
import { HttpManager } from "@/api";
import { NavName, RouterName, SignInRules } from "@/enums";
import store from "@/store";

export default defineComponent({
  components: {
    YinLoginLogo,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin();

    // Login username and password
    const registerForm = reactive({
      username: "",
      password: "",
    });

    async function handleLoginIn() {
      let canRun = true;
      (proxy.$refs["signInForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;

      try {
        const username = registerForm.username;
        const password = registerForm.password;
        const result = (await HttpManager.signIn({
          username,
          password,
        })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });


        if (result.code === 200) {
          store.commit("setUserId", result.data.userId);
          store.commit("setUsername", result.data.userName);
          store.commit("setUserPic", result.data.avatar);
          store.commit("setToken", result.data.accessToken);
          changeIndex(NavName.Home);
          routerManager(RouterName.Home, { path: RouterName.Home });
        }
      } catch (error) {
        console.error(error);
      }
    }

    function handleSignUp() {
      routerManager(RouterName.SignUp, { path: RouterName.SignUp });
    }

    function handleForgotPassword() {
      routerManager(RouterName.ForgotPassword, {
        path: RouterName.ForgotPassword,
      });
    }

    return {
      registerForm,
      SignInRules,
      handleLoginIn,
      handleForgotPassword,
      handleSignUp,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/sign.scss";
</style>
