<template>
  <div class="play-list-container">
    <yin-nav
      :styleList="songStyle"
      :activeName="activeName"
      @click="handleChangeView"
    ></yin-nav>
    <play-list :playList="data" path="song-sheet-detail"></play-list>
    <el-pagination
      class="pagination"
      background
      layout="total, prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="allPlayList.length"
      @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";
import YinNav from "@/components/layouts/YinNav.vue";
import PlayList from "@/components/PlayList.vue";
import { SONGSTYLE } from "@/enums";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    YinNav,
    PlayList,
  },
  setup() {
    const activeName = ref("All Playlists");
    const pageSize = ref(15); // Page size
    const currentPage = ref(1); // Current page
    const songStyle = ref(SONGSTYLE); // Playlist navigation categories
    const allPlayList = ref([]); // Playlists
    const data = computed(() =>
      allPlayList.value.slice(
        (currentPage.value - 1) * pageSize.value,
        currentPage.value * pageSize.value
      )
    );

    // Get all playlists
    async function getSongList() {
      allPlayList.value = (
        (await HttpManager.getSongList()) as ResponseBody
      ).data;
      currentPage.value = 1;
    }
    // Get playlists by category
    async function getSongListOfStyle(style) {
      allPlayList.value = (
        (await HttpManager.getSongListOfStyle(style)) as ResponseBody
      ).data;
      currentPage.value = 1;
    }

    try {
      getSongList();
    } catch (error) {
      console.error(error);
    }

    // Handle view change
    async function handleChangeView(item) {
      activeName.value = item.name;
      allPlayList.value = [];
      try {
        if (item.name === "All Playlists") {
          await getSongList();
        } else {
          await getSongListOfStyle(item.name);
        }
      } catch (error) {
        console.error(error);
      }
    }
    // Handle current page change
    function handleCurrentChange(val) {
      currentPage.value = val;
    }
    return {
      activeName,
      songStyle,
      pageSize,
      currentPage,
      allPlayList,
      data,
      handleChangeView,
      handleCurrentChange,
    };
  },
});
</script>
