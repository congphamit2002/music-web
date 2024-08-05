<template>
  <yin-login-logo></yin-login-logo>
  <div class="sign">
    <div class="sign-head">
      <span>Registration</span>
    </div>
    <el-form
      ref="signUpForm"
      label-width="70px"
      status-icon
      :model="registerForm"
      :rules="SignUpRules"
    >
      <el-form-item prop="username" label="Username">
        <el-input
          v-model="registerForm.username"
          placeholder="Username"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password" label="Password">
        <el-input
          type="password"
          placeholder="Password"
          v-model="registerForm.password"
        ></el-input>
      </el-form-item>
      <el-form-item prop="sex" label="Gender">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :label="0">Female</el-radio>
          <el-radio :label="1">Male</el-radio>
          <el-radio :label="2">Other</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="Phone">
        <el-input
          placeholder="Phone"
          v-model="registerForm.phoneNum"
        ></el-input>
      </el-form-item>
      <el-form-item prop="email" label="Email">
        <el-input v-model="registerForm.email" placeholder="Email"></el-input>
      </el-form-item>
      <el-form-item prop="birth" label="Birthday">
        <el-date-picker
          type="date"
          placeholder="Select Date"
          v-model="registerForm.birth"
          style="width: 100%"
        ></el-date-picker>
      </el-form-item>
      <el-form-item prop="introduction" label="Introduction">
        <el-input
          type="textarea"
          placeholder="Introduction"
          v-model="registerForm.introduction"
        ></el-input>
      </el-form-item>
      <el-form-item prop="location" label="Location">
        <el-select
          v-model="registerForm.location"
          placeholder="Location"
          style="width: 100%"
        >
          <el-option
            v-for="item in AREA"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="sign-btn">
        <el-button @click="goBackRegist()">Cancel</el-button>
        <el-button type="primary" @click="handleSignUp(formRef)"
          >Confirm</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, getCurrentInstance } from "vue";
import mixin from "@/mixins/mixin";
import YinLoginLogo from "@/components/layouts/YinLoginLogo.vue";
import { HttpManager } from "@/api";
import { getBirth } from "@/utils";
import { AREA, RouterName, NavName, SignUpRules } from "@/enums";

export default defineComponent({
  components: {
    YinLoginLogo,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin();

    const registerForm = reactive({
      username: "",
      password: "",
      sex: "",
      phoneNum: "",
      email: "",
      birth: new Date(),
      introduction: "",
      location: "",
    });

    async function goBackRegist() {
      routerManager(RouterName.SignIn, { path: RouterName.SignIn });
    }

    async function handleSignUp() {
      let canRun = true;
      (proxy.$refs["signUpForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;

      try {
        const username = registerForm.username;
        const password = registerForm.password;
        const sex = registerForm.sex;
        const phoneNum = registerForm.phoneNum;
        const email = registerForm.email;
        const birth = registerForm.birth;
        const introduction = registerForm.introduction;
        const location = registerForm.location;
        const result = (await HttpManager.SignUp({
          username,
          password,
          sex,
          phoneNum,
          email,
          birth,
          introduction,
          location,
        })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          changeIndex(NavName.SignIn);
          routerManager(RouterName.SignIn, { path: RouterName.SignIn });
        }
      } catch (error) {
        console.error(error);
      }
    }

    return {
      SignUpRules,
      AREA,
      registerForm,
      handleSignUp,
      goBackRegist,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/sign.scss";
</style>
