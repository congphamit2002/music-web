<template>
  <el-container>
    <el-aside class="album-slide">
      <el-image
        class="album-img"
        fit="contain"
        :src="attachImageUrl(songDetails.pic)"
      />
      <h3 class="album-info">{{ songDetails.title }}</h3>
    </el-aside>
    <el-main class="album-main">
      <h1>Description</h1>
      <p>{{ songDetails.introduction }}</p>
      <!-- Rating -->
      <div class="album-score">
        <div>
          <h3>Song List Rating</h3>
          <el-rate v-model="rank" allow-half disabled></el-rate>
        </div>
        <span>{{ rank * 2 }}</span>
        <div>
          <h3>{{ assistText }} {{ score * 2 }}</h3>
          <el-rate
            allow-half
            v-model="score"
            :disabled="disabledRank"
            @click="pushValue()"
          ></el-rate>
        </div>
      </div>
      <!-- Songs -->
      <song-list class="album-body" :songList="currentSongList"></song-list>
      <comment :playId="songListId" :type="1"></comment>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { defineComponent, ref, computed, getCurrentInstance } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import SongList from "@/components/SongList.vue";
import Comment from "@/components/Comment.vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    SongList,
    Comment,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { checkStatus } = mixin();

    const currentSongList = ref([]); // Songs in the song list
    const nowSongListId = ref(""); // Song list ID
    const nowScore = ref(0);
    const nowRank = ref(0);
    const disabledRank = ref(false);
    const assistText = ref("Rate");
    // const evaluateList = ref(["Very Poor", "Poor", "Average", "Good", "Excellent"]);
    const songDetails = computed(() => store.getters.songDetails); // Single song list information
    const nowUserId = computed(() => store.getters.userId);

    nowSongListId.value = songDetails.value.id; // Assign song list ID

    // Collect songs in the song list
    async function getSongId(id) {
      const result = (await HttpManager.getListSongOfSongId(
        id
      )) as ResponseBody;
      // Fetch information about the songs in the list
      for (const item of result.data) {
        // Get songs in the list
        const resultSong = (await HttpManager.getSongOfId(
          item.songId
        )) as ResponseBody;
        currentSongList.value.push(resultSong.data[0]);
      }
    }

    // Get the rating
    async function getRank(id) {
      const result = (await HttpManager.getRankOfSongListId(
        id
      )) as ResponseBody;
      nowRank.value = result.data / 2;
    }

    async function getUserRank(userId, songListId) {
      const result = (await HttpManager.getUserRank(
        userId,
        songListId
      )) as ResponseBody;
      nowScore.value = result.data / 2;
      disabledRank.value = true;
      assistText.value = "Rated";
    }

    // Submit rating
    async function pushValue() {
      if (disabledRank.value || !checkStatus()) return;

      const songListId = nowSongListId.value;
      const consumerId = nowUserId.value;
      const score = nowScore.value * 2;
      try {
        const result = (await HttpManager.setRank({
          songListId,
          consumerId,
          score,
        })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          getRank(nowSongListId.value);
          disabledRank.value = true;
          assistText.value = "Rated";
        }
      } catch (error) {
        console.error(error);
      }
    }

    getUserRank(nowUserId.value, nowSongListId.value);
    getRank(nowSongListId.value); // Get the rating
    getSongId(nowSongListId.value); // Get the song IDs in the song list

    return {
      songDetails,
      rank: nowRank,
      score: nowScore,
      disabledRank,
      assistText,
      currentSongList,
      songListId: nowSongListId,
      attachImageUrl: HttpManager.attachImageUrl,
      pushValue,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;

  .album-img {
    height: 250px;
    width: 250px;
    border-radius: 10%;
  }

  .album-info {
    width: 70%;
    padding-top: 2rem;
  }
}

.album-main {
  h1 {
    font-size: 22px;
  }

  p {
    color: rgba(0, 0, 0, 0.5);
    margin: 10px 0 20px 0px;
  }
  /* Song list rating */
  .album-score {
    display: flex;
    align-items: center;
    margin: 1vw;

    h3 {
      margin: 10px 0;
    }
    span {
      font-size: 60px;
    }
    & > div:last-child {
      margin-left: 10%;
    }
  }

  .album-body {
    margin: 20px 0 20px 0px;
  }
}

@media screen and (min-width: $sm) {
  .album-slide {
    position: fixed;
    width: 400px;
  }
  .album-main {
    min-width: 600px;
    padding-right: 10vw;
    margin-left: 400px;
  }
}

@media screen and (max-width: $sm) {
  .album-slide {
    display: none;
  }
}
</style>
