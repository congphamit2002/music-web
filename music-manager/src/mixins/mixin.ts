import { getCurrentInstance, ref } from "vue";
import { LocationQueryRaw } from "vue-router";
import { RouterName } from "@/enums";
import router from "@/router";

interface RouterOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export default function () {
  const { proxy } = getCurrentInstance();
  const uploadTypes = ref(["jpg", "jpeg", "png", "gif"]);

  // Convert numeric or string gender value to its corresponding label
  function changeSex(value) {
    if (value === 0) {
      return "Female";
    } else if (value === 1) {
      return "Male";
    } else if (value === 2) {
      return "Group";
    } else if (value === 3) {
      return "Unknown";
    } else if (value === "Male" || value === "Female") {
      return value;
    }
  }

  // Validate image file before upload
  function beforeImgUpload(file) {
    const maxSizeMB = 2;
    const isLt2M = file.size / 1024 / 1024 < maxSizeMB;
    const isExistFileType = uploadTypes.value.includes(
      file.type.replace(/image\//, "")
    );

    if (!isExistFileType) {
      (proxy as any).$message.error(
        `Images are only supported in ${uploadTypes.value.join("、")} formats!`
      );
    }
    if (!isLt2M) {
      (proxy as any).$message.error(
        `Uploaded avatar image size cannot exceed ${maxSizeMB}MB!`
      );
    }

    return isExistFileType && isLt2M;
  }

  // Validate song file before upload
  function beforeSongUpload(file) {
    const maxSizeMB = 10;
    const isLt10M = file.size / 1024 / 1024 < maxSizeMB;
    const fileExtension = file.name.substring(file.name.lastIndexOf(".") + 1);
    const isMp3 = fileExtension === "mp3";

    if (!isMp3) {
      (proxy as any).$message({
        message: "Only mp3 format files are allowed!",
        type: "error",
      });
    }
    if (!isLt10M) {
      (proxy as any).$message.error(
        `Uploaded song file size cannot exceed ${maxSizeMB}MB!`
      );
    }

    return isMp3 && isLt10M;
  }

  // Router management
  function routerManager(routerName: string | number, options: RouterOptions) {
    switch (routerName) {
      case RouterName.Song:
      case RouterName.ListSong:
      case RouterName.Comment:
      case RouterName.Consumer:
      case RouterName.Collect:
        router.push({ path: options.path, query: options.query });
        break;
      case RouterName.Home:
      case RouterName.SignIn:
      case RouterName.SignOut:
      case RouterName.Info:
      case RouterName.Singer:
      case RouterName.SongList:
      case RouterName.Error:
      default:
        router.push({ path: options.path });
        break;
    }
  }

  // Navigate back in the router history
  function goBack(step = -1) {
    router.go(step);
  }

  return {
    changeSex,
    routerManager,
    goBack,
    beforeImgUpload,
    beforeSongUpload,
  };
}
