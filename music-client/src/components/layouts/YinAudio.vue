<template>
  <audio :src="attachImageUrl(songUrl)" controls="controls" :ref="player" preload="true" @canplay="canplay" @timeupdate="timeupdate" @ended="ended">
    <!-- (1) Attributes: controls, preload (2) Events: canplay, timeupdate, ended (3) Methods: play(), pause() -->
    <!-- controls: Displays audio controls (play/pause/progress bar/volume) to the user -->
    <!-- preload: Specifies whether to load the audio when the page loads -->
    <!-- canplay: Event that occurs when the audio/video is ready to start playing -->
    <!-- timeupdate: Event that occurs when the current playback position changes -->
    <!-- ended: Event that occurs when the audio has finished playing -->
  </audio>
</template>

<script lang="ts">
import { defineComponent, ref, getCurrentInstance, computed, watch } from "vue";
import { useStore } from "vuex";
import { HttpManager } from "@/api";
import { onMounted } from 'vue';

export default defineComponent({
  setup() {

    const { proxy } = getCurrentInstance();
    const store = useStore();
    const divRef = ref<HTMLAudioElement>();
    const player = (el) => {
      divRef.value = el;
    };

    const muted = ref(true); // Add a reactive muted property

    const audioDom = document.querySelector('audio');
    if (audioDom) {
      // Set to muted and try to autoplay
      audioDom.muted = true;
      audioDom.play()
        .then(() => {
          // Autoplay succeeded
        })
        .catch(error => {
          // Autoplay failed, possibly due to no user interaction
          console.error('Autoplay failed, user interaction required.', error);
        });
    }

    const songUrl = computed(() => store.getters.songUrl); // Song URL
    const isPlay = computed(() => store.getters.isPlay); // Play status
    const volume = computed(() => store.getters.volume); // Volume
    const changeTime = computed(() => store.getters.changeTime); // Specified playback time
    const autoNext = computed(() => store.getters.autoNext); // Trigger auto-play next song

    // Watch for play or pause
    watch(isPlay, () => togglePlay());
    // Jump to specified time to play
    watch(changeTime, () => (divRef.value.currentTime = changeTime.value));
    watch(volume, (value) => (divRef.value.volume = value));

    // Start / Pause
    function togglePlay() {
      isPlay.value ? divRef.value.play() : divRef.value.pause();
    }
    // Prepare to play after getting the song URL
    function canplay() {
      // Record music duration
      proxy.$store.commit("setDuration", divRef.value.duration);
      // Start playing
      if (muted.value) {
        divRef.value.muted = false;
        muted.value = false;
      }
      proxy.$store.commit("setIsPlay", true);
    }
    // Record the current playback position when the music is playing
    function timeupdate() {
      proxy.$store.commit("setCurTime", divRef.value.currentTime);
    }
    // Triggered when the music finishes playing
    function ended() {
      proxy.$store.commit("setIsPlay", false);
      proxy.$store.commit("setCurTime", 0);
      proxy.$store.commit("setAutoNext", !autoNext.value);
    }

    return {
      songUrl,
      player,
      canplay,
      timeupdate,
      ended,
      muted,
      attachImageUrl: HttpManager.attachImageUrl,
    };
  },
});
</script>

<style scoped>
audio {
  display: none;
}
</style>
