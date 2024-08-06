import { getBaseURL, get, post, deletes } from "./request";

const HttpManager = {
  // Get image information
  attachImageUrl: (url) =>
    url
      ? `${getBaseURL()}/${url}`
      : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",

  // =======================> User API Completed
  // Login
  signIn: ({ username, password }) =>
    post(`user/login/status`, { username, password }),
  signInByemail: ({ email, password }) =>
    post(`user/email/status`, { email, password }),

  // Register
  SignUp: ({
    username,
    password,
    sex,
    phoneNum,
    email,
    birth,
    introduction,
    location,
  }) =>
    post(`user/add`, {
      username,
      password,
      sex,
      phoneNum,
      email,
      birth,
      introduction,
      location,
    }),

  // Delete user
  deleteUser: (id) => get(`user/delete?id=${id}`),

  // Update user information
  updateUserMsg: ({
    id,
    username,
    sex,
    phoneNum,
    email,
    birth,
    introduction,
    location,
  }) =>
    post(`user/update`, {
      id,
      username,
      sex,
      phoneNum,
      email,
      birth,
      introduction,
      location,
    }),
  updateUserPassword: ({ id, username, oldPassword, password }) =>
    post(`user/updatePassword`, { id, username, oldPassword, password }),

  // Return user by ID
  getUserOfId: (id) => get(`user/detail?id=${id}`),

  // Update user avatar
  uploadUrl: (userId) => `${getBaseURL()}/user/avatar/update?id=${userId}`,

  // =======================> Playlist API Completed
  // Get all playlists
  getSongList: () => get("songList"),

  // Get playlists by genre
  getSongListOfStyle: (style) => get(`songList/style/detail?style=${style}`),

  // Return playlists with titles containing specified keywords
  getSongListOfLikeTitle: (keywords) =>
    get(`songList/likeTitle/detail?title=${keywords}`),

  // Return songs from a playlist by playlist ID
  getListSongOfSongId: (songListId) =>
    get(`listSong/detail?songListId=${songListId}`),

  // =======================> Singer API Completed
  // Return all singers
  getAllSinger: () => get("singer"),

  // Categorize singers by gender
  getSingerOfSex: (sex) => get(`singer/sex/detail?sex=${sex}`),

  // =======================> Collection API Completed
  // Return the collection list for a specific user ID
  getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),

  // Add a song to the collection; type: 0 for songs, 1 for playlists
  setCollection: ({ userId, type, songId }) =>
    post(`collection/add`, { userId, type, songId }),

  deleteCollection: (userId, songId) =>
    deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

  isCollection: ({ userId, type, songId }) =>
    post(`collection/status`, { userId, type, songId }),

  // =======================> Rating API Completed
  // Submit rating
  setRank: ({ songListId, consumerId, score }) =>
    post(`rankList/add`, { songListId, consumerId, score }),

  // Get ratings for a specific playlist
  getRankOfSongListId: (songListId) => get(`rankList?songListId=${songListId}`),

  // Get user's ratings for playlists
  getUserRank: (consumerId, songListId) =>
    get(`/rankList/user?consumerId=${consumerId}&songListId=${songListId}`),

  // =======================> Comment API Completed
  // Add comment
  setComment: ({ userId, content, songId, songListId, nowType }) =>
    post(`comment/add`, { userId, content, songId, songListId, nowType }),

  // Delete comment
  deleteComment: (id) => get(`comment/delete?id=${id}`),

  // Like a comment
  setSupport: ({ id, up }) => post(`comment/like`, { id, up }),

  // Return all comments
  getAllComment: (type, id) => {
    let url = "";
    if (type === 1) {
      url = `comment/songList/detail?songListId=${id}`;
    } else if (type === 0) {
      url = `comment/song/detail?songId=${id}`;
    }
    return get(url);
  },

  // =======================> Song API
  // Return song by song ID
  getSongOfId: (id) => get(`song/detail?id=${id}`),

  // Return songs by singer ID
  getSongOfSingerId: (id) => get(`song/singer/detail?singerId=${id}`),

  // Return songs by singer name
  getSongOfSingerName: (keywords) =>
    get(`song/singerName/detail?name=${keywords}`),

  // Download music
  downloadMusic: (url) => get(url, { responseType: "blob" }),

  //======================> Optimize the like API to avoid duplicate likes; a new data table is required
  testAlreadySupport: ({ commentId, userId }) =>
    post(`userSupport/test`, { commentId, userId }),

  deleteUserSupport: ({ commentId, userId }) =>
    post(`userSupport/delete`, { commentId, userId }),

  insertUserSupport: ({ commentId, userId }) =>
    post(`userSupport/insert`, { commentId, userId }),

  // Get all posters
  getBannerList: () => get("banner/getAllBanner"),
};

export { HttpManager };
