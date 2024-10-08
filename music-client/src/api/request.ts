import axios from "axios";
import router from "../router";
import store from "@/store";
import { RouterName } from "@/enums";

const BASE_URL = process.env.NODE_HOST;

axios.defaults.timeout = 5000; // Set timeout
axios.defaults.withCredentials = true; // Allow cross-domain requests
axios.defaults.baseURL = BASE_URL;
// Set Content-Type in response headers
axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded;charset=UTF-8";

axios.interceptors.request.use(
  async (config) => {
    const token = store.getters.token;
    console.log("Check token ", token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
axios.interceptors.response.use(
  (response) => {
    // If response status is 200, the API request was successful, data can be retrieved
    // Otherwise, throw an error
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // Case when server status is not starting with a 2
  (error) => {
    if (error.response.status) {
      switch (error.response.status) {
        // 401: Not logged in
        case 401:
          router.replace({
            path: RouterName.SignIn,
          });
          break;
        case 403:
          // console.log('Admin privileges have changed, please log in again')
          // Redirect to the login page and pass the fullPath of the page you want to view, after logging in successfully, it will redirect to the desired page
          setTimeout(() => {
            router.replace({
              path: RouterName.Error403,
            });
          }, 1000);
          break;

        // 404: Request does not exist
        case 404:
          router.replace({
            path: RouterName.Error404,
          });
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
    axios.get(url, { params }).then(
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
    axios.delete(url, { data }).then(
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
