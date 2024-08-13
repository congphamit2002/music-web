<template>
  <div class="search-song">
    <song-list :songList="currentSongList"></song-list>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  computed,
  watch,
  onMounted,
  getCurrentInstance,
} from "vue";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import SongList from "@/components/SongList.vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    SongList,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const route = useRoute();

    const currentSongList = ref([]);
    const searchWord = computed(() => store.getters.searchWord);
    watch(searchWord, (value) => {
      searchSong(value);
    });

    async function searchSong(value) {
      if (!value) {
        currentSongList.value = [];
        return;
      }
      const result = (await HttpManager.getSongOfSingerName(
        value
      )) as ResponseBody;
      if (!result.data.length) {
        currentSongList.value = [];
        (proxy as any).$message({
          message: "No data for this keyword",
          type: "warning",
        });
      } else {
        currentSongList.value = result.data;
      }
    }

    onMounted(() => {
      searchSong(route.query.keywords);
    });

    return {
      currentSongList,
    };
  },
});
</script>
