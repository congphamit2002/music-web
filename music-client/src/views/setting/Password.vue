<template>
  <el-form ref="passwordForm" label-width="70px" :model="form" :rules="rules">
    <el-form-item label="Old Password" prop="oldPassword">
      <el-input type="password" v-model="form.oldPassword" />
    </el-form-item>
    <el-form-item label="New Password" prop="newPassword">
      <el-input type="password" v-model="form.newPassword" />
    </el-form-item>
    <el-form-item label="Confirm Password" prop="confirmPassword">
      <el-input type="password" v-model="form.confirmPassword" />
    </el-form-item>
    <el-form-item>
      <el-button @click="clearData()">Reset</el-button>
      <el-button type="primary" @click="confirm()">Confirm Change</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, reactive } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { validatePassword } from "@/enums";

export default defineComponent({
  setup() {
    const store = useStore();
    const { proxy } = getCurrentInstance();
    const { goBack } = mixin();

    const form = reactive({
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    });
    const userId = computed(() => store.getters.userId);
    const userName = computed(() => store.getters.username);

    const validateCheck = (rule: any, value: any, callback: any) => {
      if (value === "") {
        callback(new Error("Password cannot be empty"));
      } else if (value !== form.newPassword) {
        callback(new Error("Please enter the correct password"));
      } else {
        callback();
      }
    };
    const rules = reactive({
      oldPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
      newPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
      confirmPassword: [{ validator: validateCheck, trigger: "blur", min: 3 }],
    });

    async function clearData() {
      form.oldPassword = "";
      form.newPassword = "";
      form.confirmPassword = "";
    }

    async function confirm() {
      let canRun = true;
      (proxy.$refs["passwordForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;

      const id = userId.value;
      const username = userName.value;
      const oldPassword = form.oldPassword;
      const password = form.newPassword;

      const result = (await HttpManager.updateUserPassword({
        id,
        username,
        oldPassword,
        password,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) goBack();
    }

    return {
      form,
      clearData,
      confirm,
      rules,
    };
  },
});
</script>

<style></style>
