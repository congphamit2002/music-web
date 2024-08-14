<template>
  <transition name="aside-fade">
    <div class="yin-current-play" v-if="showAside">
      <h2 class="title">Currently Playing</h2>
      <div class="control">
        Total {{ (currentPlayList && currentPlayList.length) || 0 }} songs
      </div>
      <ul class="menus">
        <li
          v-for="(item, index) in currentPlayList"
          :class="{ 'is-play': songId === item.id }"
          :key="index"
          @click="
            playMusic({
              id: item.id,
              url: item.url,
              pic: item.pic,
              index: index,
              name: item.name,
              lyric: item.lyric,
              currentSongList: currentPlayList,
            })
          "
        >
          {{ getSongTitle(item.name) }}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, onMounted } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";

export default defineComponent({
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { getSongTitle, playMusic } = mixin();

    const songId = computed(() => store.getters.songId); // Music ID
    const currentPlayList = computed(() => store.getters.currentPlayList); // Currently Playing List
    const showAside = computed(() => store.getters.showAside); // Whether to show the sidebar

    onMounted(() => {
      document.addEventListener(
        "click",
        () => {
          proxy.$store.commit("setShowAside", false);
        },
        true
      );
    });

    return {
      songId,
      currentPlayList,
      showAside,
      getSongTitle,
      playMusic,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/yin-current-play.scss";
</style>
