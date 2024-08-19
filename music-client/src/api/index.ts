import { getBaseURL, get, put, post, deletes } from "./request";

const HttpManager = {
  // Get image information
  attachImageUrl: (url) =>
    url
      ? `${getBaseURL()}/file?fileName=${url}`
      : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",

  // =======================> User API Completed
  // Login
  signIn: ({ username, password }) =>
    post(`api/v1/login`, { username, password }),
  signInByemail: ({ email, password }) =>
    post(`user/email/status`, { email, password }),

  logOut: () => post(`/api/v1/logout`),
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
    post(`users`, {
      username,
      password,
      sex,
      phoneNum,
      email,
      birth,
      introduction,
      location,
      role: "ROLE_USER",
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
    put(`users/${id}`, {
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
    put(`users/${id}/password`, { id, username, oldPassword, password }),

  // Return user by ID
  getUserOfId: (id) => get(`users/${id}`),

  // Update user avatar
  uploadAvatarUrl: (userId) => `${getBaseURL()}/users/${userId}/avatar`,

  // =======================> Playlist API Completed
  // Get all playlists
  getSongList: () => get("songLists"),

  // Get playlists by genre
  getSongListOfStyle: (style) => get(`songLists/style?style=${style}`),

  // Return playlists with titles containing specified keywords
  getSongListOfLikeTitle: (keywords) =>
    get(`songLists/title?title=${keywords}`),

  // Return songs from a playlist by playlist ID
  getListSongOfSongId: (songListId) => get(`listSongs/songList/${songListId}`),

  // =======================> Singer API Completed
  // Return all singers
  getAllSinger: () => get("singers"),

  // Categorize singers by gender
  getSingerOfSex: (sex) => get(`singers/sex?sex=${sex}`),

  // =======================> Collection API Completed
  // Return the collection list for a specific user ID
  getCollectionOfUser: (userId) => get(`collections?userId=${userId}`),

  // Add a song to the collection; type: 0 for songs, 1 for playlists
  setCollection: ({ userId, type, songId }) =>
    post(`collections`, { userId, type, songId }),

  deleteCollection: (userId, songId) =>
    deletes(`collections?userId=${userId}&&songId=${songId}`),

  isCollection: ({ userId, type, songId }) =>
    get(`collections/status?userId=${userId}&&songId=${songId}`),

  // =======================> Rating API Completed
  // Submit rating
  setRank: ({ songListId, consumerId, score }) =>
    post(`rankLists`, { songListId, consumerId, score }),

  // Get ratings for a specific playlist
  getRankOfSongListId: (songListId) => get(`rankLists/songLists/${songListId}`),

  // Get user's ratings for playlists
  getUserRank: (userId, songListId) =>
    get(`/rankLists/user?userId=${userId}&songListId=${songListId}`),

  // =======================> Comment API Completed
  // Add comment
  setComment: ({ userId, content, songId, songListId, nowType }) =>
    post(`comments`, { userId, content, songId, songListId, nowType }),

  // Delete comment
  deleteComment: (id) => deletes(`comments/${id}`),

  // Like a comment
  setSupport: ({ id, up }) => post(`comments/${id}/like`, { id, up }),

  // Return all comments
  getAllComment: (type, id) => {
    let url = "";
    if (type === 1) {
      url = `comments/song-lists/${id}`;
    } else if (type === 0) {
      url = `comments/songs/${id}`;
    }
    return get(url);
  },

  // =======================> Song API
  // Return song by song ID
  getSongOfId: (id) => get(`songs/${id}`),

  // Return songs by singer ID
  getSongOfSingerId: (id) => get(`songs/singers/${id}`),

  // Return songs by singer name
  // TODO: CHECK
  getSongOfSingerName: (keywords) => get(`songs/name?name=${keywords}`),

  // Download music
  downloadMusic: (url) => get(url, { responseType: "blob" }),

  //======================> Optimize the like API to avoid duplicate likes; a new data table is required
  testAlreadySupport: ({ commentId, userId }) =>
    get(`userSupports/exist`, { commentId, userId }),

  deleteUserSupport: ({ commentId, userId }) =>
    post(`userSupports/unlike`, { commentId, userId }),

  insertUserSupport: ({ commentId, userId }) =>
    post(`userSupports/like`, { commentId, userId }),

  // Get all posters
  getBannerList: () => get("banners"),
};

export { HttpManager };
