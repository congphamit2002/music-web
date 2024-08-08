import { deletes, get, getBaseURL, post } from "./request";

const HttpManager = {
  // Get image URL
  attachImageUrl: (url) => `${getBaseURL()}/${url}`,

  // =======================> Admin API Complete
  // Check login status
  getLoginStatus: ({ username, password }) =>
    post(`admin/login/status`, { username, password }),

  // =======================> User API Complete
  // Get all users
  getAllUser: () => get(`user`),
  // Get user by ID
  getUserOfId: (id) => get(`user/detail?id=${id}`),
  // Delete user
  deleteUser: (id) => get(`user/delete?id=${id}`),

  // =======================> Collection API Complete
  // Get collection list of specified user ID
  getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),
  // Delete a collected song
  deleteCollection: (userId, songId) =>
    deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

  // =======================> Comment API Complete
  // Get comments for a specific song ID
  getCommentOfSongId: (songId) => get(`comment/song/detail?songId=${songId}`),
  // Get comments for a specific playlist ID
  getCommentOfSongListId: (songListId) =>
    get(`comment/songList/detail?songListId=${songListId}`),
  // Delete a comment
  deleteComment: (id) => get(`comment/delete?id=${id}`),

  // =======================> Singer API Complete
  // Get all singers
  getAllSinger: () => get(`singer`),
  // Add a singer
  setSinger: ({ name, sex, birth, location, introduction }) =>
    post(`singer/add`, {
      name,
      sex,
      birth,
      location,
      introduction,
    }),
  // Update singer information
  updateSingerMsg: ({ id, name, sex, birth, location, introduction }) =>
    post(`singer/update`, {
      id,
      name,
      sex,
      birth,
      location,
      introduction,
    }),
  // Delete a singer
  deleteSinger: (id) => deletes(`singer/delete?id=${id}`),

  // =======================> Song API Complete
  // Get all songs
  getAllSong: () => get(`song`),
  // Get songs by singer ID
  getSongOfSingerId: (id) => get(`song/singer/detail?singerId=${id}`),
  // Get song by ID
  getSongOfId: (id) => get(`song/detail?id=${id}`),
  // Get songs by singer name
  getSongOfSingerName: (id) => get(`song/singerName/detail?name=${id}`),
  // Update song information
  updateSongMsg: ({ id, singerId, name, introduction, lyric }) =>
    post(`song/update`, {
      id,
      singerId,
      name,
      introduction,
      lyric,
    }),
  updateSongUrl: (id) => `${getBaseURL()}/song/url/update?id=${id}`,
  updateSongImg: (id) => `${getBaseURL()}/song/img/update?id=${id}`,
  updateSongLrc: (id) => `${getBaseURL()}/song/lrc/update?id=${id}`,
  // Delete a song
  deleteSong: (id) => deletes(`song/delete?id=${id}`),

  // =======================> Playlist API Complete
  // Add a playlist
  setSongList: ({ title, introduction, style }) =>
    post(`songList/add`, { title, introduction, style }),
  // Get all playlists
  getSongList: () => get(`songList`),
  // Update playlist information
  updateSongListMsg: ({ id, title, introduction, style }) =>
    post(`songList/update`, { id, title, introduction, style }),
  // Delete a playlist
  deleteSongList: (id) => get(`songList/delete?id=${id}`),

  // =======================> Playlist Song API Complete
  // Add a song to a playlist
  setListSong: ({ songId, songListId }) =>
    post(`listSong/add`, { songId, songListId }),
  // Get songs in a playlist by playlist ID
  getListSongOfSongId: (songListId) =>
    get(`listSong/detail?songListId=${songListId}`),
  // Delete a song from a playlist
  deleteListSong: (songId) => get(`listSong/delete?songId=${songId}`),
};

export { HttpManager };
