<template>
  <!-- Carousel -->
  <el-carousel
    v-if="swiperList.length"
    class="swiper-container"
    type="card"
    height="20vw"
    :interval="4000"
  >
    <el-carousel-item v-for="(item, index) in swiperList" :key="index">
      <img :src="HttpManager.attachImageUrl(item.pic)" />
    </el-carousel-item>
  </el-carousel>
  <!-- Hot Playlists -->
  <play-list
    class="play-list-container"
    title="Playlists"
    path="song-sheet-detail"
    :playList="songList"
  ></play-list>
  <!-- Hot Singers -->
  <play-list
    class="play-list-container"
    title="Singers"
    path="singer-detail"
    :playList="singerList"
  ></play-list>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";

import PlayList from "@/components/PlayList.vue";
import { NavName } from "@/enums";
import { HttpManager } from "@/api";
import mixin from "@/mixins/mixin";

const songList = ref([]); // Playlist list
const singerList = ref([]); // Singer list
const swiperList = ref([]); // Carousel images are always being queried
const { changeIndex } = mixin();
try {
  HttpManager.getBannerList().then((res) => {
    swiperList.value = (res as ResponseBody).data.sort();
  });

  HttpManager.getSongList().then((res) => {
    songList.value = (res as ResponseBody).data.sort().slice(0, 10);
  });

  HttpManager.getAllSinger().then((res) => {
    singerList.value = (res as ResponseBody).data.sort().slice(0, 10);
  });

  onMounted(() => {
    changeIndex(NavName.Home);
  });
} catch (error) {
  console.error(error);
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

/* Carousel */
.swiper-container {
  width: 90%;
  margin: auto;
  padding-top: 20px;
  img {
    width: 100%;
  }
}

.swiper-container:deep(
    .el-carousel__indicators.el-carousel__indicators--outside
  ) {
  display: inline-block;
  transform: translateX(30vw);
}

.el-slider__runway {
  background-color: $color-blue;
}
</style>
