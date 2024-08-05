import { getCurrentInstance, computed } from "vue";
import { useStore } from "vuex";
import { LocationQueryRaw } from "vue-router";
import { RouterName } from "@/enums";
import { HttpManager } from "@/api";
import axios from "axios";
import router from "@/router";
interface routerOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export default function () {
  const { proxy } = getCurrentInstance();

  const store = useStore();
  const token = computed(() => store.getters.token);

  function getUserSex(sex) {
    if (sex === 0) {
      return "Nữ";
    } else if (sex === 1) {
      return "Nam";
    }
  }

  // Lấy tên bài hát
  function getSongTitle(str) {
    return str.split("-")[1];
  }

  // Lấy tên ca sĩ
  function getSingerName(str) {
    return str.split("-")[0];
  }

  // Kiểm tra trạng thái đăng nhập
  function checkStatus(status?: boolean) {
    if (!token.value) {
      if (status !== false)
        (proxy as any).$message({
          message: "Vui lòng đăng nhập trước",
          type: "warning",
        });
      return false;
    }
    return true;
  }

  // Phát nhạc
  function playMusic({ id, url, pic, index, name, lyric, currentSongList }) {
    const songTitle = getSongTitle(name);
    const singerName = getSingerName(name);
    proxy.$store.dispatch("playMusic", {
      id,
      url,
      pic,
      index,
      songTitle,
      singerName,
      lyric,
      currentSongList,
    });
  }

  function getFileName(path) {
    const parts = path.split("/");
    return parts[parts.length - 1];
  }

  // Tải nhạc
  async function downloadMusic({ songUrl, songName }) {
    if (!songUrl) {
      (proxy as any).$message({
        message: "Liên kết tải về trống!",
        type: "error",
      });
      console.error("Liên kết tải về trống!");
      return;
    }
    const fileName = getFileName(songUrl);
    const downUrl = "/download/" + fileName;

    const response = await axios.get(downUrl, {
      responseType: "blob", // Chỉ định loại phản hồi là dữ liệu nhị phân
    });

    // Tạo một URL Blob để tải xuống tệp
    const blob = new Blob([response.data], {
      type: "application/octet-stream",
    });
    const url = window.URL.createObjectURL(blob);

    // Tạo một thẻ <a> ẩn để tải xuống tệp
    const link = document.createElement("a");
    link.href = url;
    link.download = fileName;
    document.body.appendChild(link);
    link.click();

    // Giải phóng đối tượng URL
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
  }

  // Chỉ mục điều hướng
  function changeIndex(value) {
    proxy.$store.commit("setActiveNavName", value);
  }

  // Quản lý điều hướng
  // Note use router !=== proxy.$router
  function routerManager(routerName: string | number, options: routerOptions) {
    switch (routerName) {
      case RouterName.Search:
        router.push({ path: options.path, query: options.query });
        break;
      case RouterName.Home:
      case RouterName.SongSheet:
      case RouterName.SongSheetDetail:
      case RouterName.Singer:
      case RouterName.SingerDetail:
      case RouterName.Personal:
      case RouterName.PersonalData:
      case RouterName.Setting:
      case RouterName.SignIn:
      case RouterName.SignUp:
      case RouterName.SignOut:
      case RouterName.Lyric:
      case RouterName.Error:
      default:
        router.push({ path: options.path });
        break;
    }
  }

  function goBack(step = -1) {
    router.go(step);
  }

  return {
    getUserSex,
    getSongTitle,
    getSingerName,
    changeIndex,
    checkStatus,
    playMusic,
    routerManager,
    goBack,
    downloadMusic,
  };
}
