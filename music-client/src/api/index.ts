import { getBaseURL, get, post, deletes } from "./request";

const HttpManager = {
  attachImageUrl: (url) =>
    url
      ? `${getBaseURL()}/${url}`
      : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
  signIn: ({ username, password }) =>
    post(`user/login/status`, { username, password }),
  signInByemail: ({ email, password }) =>
    post(`user/email/status`, { email, password }),
  // 注册
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
  // Add a song to the collection. Type: 0 represents a song, 1 represents a playlist
  setCollection: ({ userId, type, songId }) =>
    post(`collection/add`, { userId, type, songId }),

  // Delete a song from the collection
  deleteCollection: (userId, songId) =>
    deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

  // Check if a song is in the collection
  isCollection: ({ userId, type, songId }) =>
    post(`collection/status`, { userId, type, songId }),
};

export { HttpManager };
