import axios from "axios";
import router from "../router";

const BASE_URL = process.env.NODE_HOST;

axios.defaults.timeout = 5000; // Thiết lập thời gian chờ
axios.defaults.withCredentials = true; // true cho phép cross-domain
axios.defaults.baseURL = BASE_URL;
// Content-Type trong header phản hồi
axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded;charset=UTF-8";

// Bộ chặn phản hồi
axios.interceptors.response.use(
  (response) => {
    // Nếu trạng thái phản hồi là 200, tức là yêu cầu API thành công, có thể lấy được dữ liệu
    // Ngược lại, ném ra lỗi
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // Tình huống trạng thái máy chủ không phải là số bắt đầu bằng 2
  (error) => {
    if (error.response.status) {
      switch (error.response.status) {
        // 401: Chưa đăng nhập
        case 401:
          router.replace({
            path: "/",
            query: {
              // redirect: router.currentRoute.fullPath
            },
          });
          break;
        case 403:
          // console.log('Quyền quản trị viên đã thay đổi, vui lòng đăng nhập lại')
          // Chuyển hướng đến trang đăng nhập và chuyển trang fullPath muốn xem qua, sau khi đăng nhập thành công sẽ chuyển hướng đến trang cần truy cập
          setTimeout(() => {
            router.replace({
              path: "/",
              query: {
                // redirect: router.currentRoute.fullPath
              },
            });
          }, 1000);
          break;

        // 404 yêu cầu không tồn tại
        case 404:
          // console.log('Trang yêu cầu đã bay lên sao Hỏa')
          break;
      }
      return Promise.reject(error.response);
    }
  }
);

export function getBaseURL() {
  return BASE_URL;
}

export function get(url, params?: object) {
  return new Promise((resolve, reject) => {
    axios.get(url, params).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

export function post(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

export function deletes(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

export function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}
