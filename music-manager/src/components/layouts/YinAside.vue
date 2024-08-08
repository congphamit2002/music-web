<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      background-color="#ffffff"
      active-text-color="#30a4fc"
      default-active="2"
      router
      :collapse="collapse"
    >
      <el-menu-item index="info">
        <el-icon><pie-chart /></el-icon>
        <span>System Home</span>
      </el-menu-item>
      <el-menu-item index="consumer">
        <el-icon><User /></el-icon>
        <span>User Management</span>
      </el-menu-item>
      <el-menu-item index="singer">
        <el-icon><mic /></el-icon>
        <span>Artist Management</span>
      </el-menu-item>
      <el-menu-item index="songList">
        <el-icon><Document /></el-icon>
        <span>Playlist Management</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { PieChart, Mic, Document, User } from "@element-plus/icons-vue";
import emitter from "@/utils/emitter";

const collapse = ref(false);
emitter.on("collapse", (msg) => {
  collapse.value = msg as boolean;
});
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
  width: 10%;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar > ul {
  height: 100%;
}
</style>
