import { Icon } from "@/enums";

export default {
  state: {
    /** Music Information */
    songId: "", // Music ID
    songTitle: "", // Song Title
    songUrl: "", // Music URL
    songPic: `/img/songPic/tubiao.jpg`, // Song Image
    singerName: "", // Singer Name
    lyric: [], // Processed Lyrics Data

    /** Music Playback Information */
    isPlay: false, // Playback Status
    playBtnIcon: Icon.BOFANG, // Playback Status Icon
    volume: 0, // Volume
    duration: 0, // Music Duration
    curTime: 0, // Current Music Playback Position
    changeTime: 0, // Specified Playback Time
    autoNext: true, // Trigger Auto Play Next Track

    /** Music List Information */
    currentPlayList: [], // Current Playlist
    songDetails: null, // Single Playlist Information
    currentPlayIndex: -1, // Current Song Position in Playlist
  },
  getters: {
    songId: (state) => state.songId,
    songTitle: (state) => state.songTitle,
    songUrl: (state) => state.songUrl,
    songPic: (state) => state.songPic,
    singerName: (state) => state.singerName,
    lyric: (state) => state.lyric,

    isPlay: (state) => state.isPlay,
    playBtnIcon: (state) => state.playBtnIcon,
    volume: (state) => state.volume,
    duration: (state) => state.duration,
    curTime: (state) => state.curTime,
    changeTime: (state) => state.changeTime,
    autoNext: (state) => state.autoNext,

    currentPlayList: (state) => state.currentPlayList,
    songDetails: (state) => state.songDetails,
    currentPlayIndex: (state) => state.currentPlayIndex,
  },
  mutations: {
    setSongId: (state, songId) => {
      state.songId = songId;
    },
    setSongTitle: (state, songTitle) => {
      state.songTitle = songTitle;
    },
    setSongUrl: (state, songUrl) => {
      state.songUrl = songUrl;
    },
    setSongPic: (state, songPic) => {
      state.songPic = songPic;
    },
    setSingerName: (state, singerName) => {
      state.singerName = singerName;
    },
    setAutoNext: (state, autoNext) => {
      state.autoNext = autoNext;
    },
    setLyric: (state, lyric) => {
      state.lyric = lyric;
    },

    setIsPlay: (state, isPlay) => {
      state.isPlay = isPlay;
    },
    setPlayBtnIcon: (state, playBtnIcon) => {
      state.playBtnIcon = playBtnIcon;
    },
    setVolume: (state, volume) => {
      state.volume = volume;
    },
    setDuration: (state, duration) => {
      state.duration = duration;
    },
    setCurTime: (state, curTime) => {
      state.curTime = curTime;
    },
    setChangeTime: (state, changeTime) => {
      state.changeTime = changeTime;
    },

    setCurrentPlayList: (state, currentPlayList) => {
      state.currentPlayList = currentPlayList;
    },
    setSongDetails: (state, songDetails) => {
      state.songDetails = songDetails;
    },
    setCurrentPlayIndex: (state, currentPlayIndex) => {
      state.currentPlayIndex = currentPlayIndex;
    },
  },
  actions: {
    playMusic: (
      { commit },
      { id, url, pic, index, songTitle, singerName, lyric, currentSongList }
    ) => {
      commit("setSongId", id);
      commit("setSongUrl", url);
      commit("setSongPic", pic);
      commit("setCurrentPlayIndex", index);
      commit("setSongTitle", songTitle);
      commit("setSingerName", singerName);
      commit("setLyric", lyric);
      commit("setCurrentPlayList", currentSongList);
    },
  },
};
