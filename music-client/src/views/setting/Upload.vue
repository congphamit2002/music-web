<template>
  <div class="upload">
    <el-upload
      drag
      :action="uploadUrl()"
      :headers="uploadHeaders"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">Drag files here or click to upload</div>
      <template #tip>
        <p class="el-upload__tip">
          You can only upload {{ uploadTypes.join("、") }} files, and the size
          should not exceed 10MB
        </p>
      </template>
    </el-upload>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, getCurrentInstance } from "vue";
import { useStore } from "vuex";
import { UploadFilled } from "@element-plus/icons-vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    UploadFilled,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const uploadTypes = ref(["jpg", "jpeg", "png", "gif"]);
    const userId = computed(() => store.getters.userId);

     const uploadHeaders = computed(() => ({
      Authorization: `Bearer ${store.getters.token}`, // Set Bearer token
    }))

    function uploadUrl() {
      return HttpManager.uploadAvatarUrl(userId.value);
    }

    function beforeAvatarUpload(file) {
      const ltCode = 2;
      const isLt10M = file.size / 1024 / 1024;
      const isExistFileType = uploadTypes.value.includes(
        file.type.replace(/image\//, "")
      );

      if (isLt10M > ltCode || isLt10M <= 0) {
        (proxy as any).$message.error(`Image size range is 0~${ltCode}MB!`);
      }
      if (!isExistFileType) {
        (proxy as any).$message.error(
          `Image supports only ${uploadTypes.value.join("、")} formats!`
        );
      }

      return isLt10M && isExistFileType;
    }

    function handleAvatarSuccess(response, file) {
      (proxy as any).$message({
        message: response.message,
        type: response.type,
      });

      if (response.code === 200) proxy.$store.commit("setUserPic", response.data);
    }

    return {
      uploadTypes,
      uploadUrl,
      beforeAvatarUpload,
      handleAvatarSuccess,
      uploadHeaders
    };
  },
});
</script>

<style scoped>
.upload {
  width: 100%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
