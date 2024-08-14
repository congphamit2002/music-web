<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item
      v-for="item in breadcrumbList"
      :key="item.name"
      :to="{ path: item.path, query: item.query }"
    >
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">Batch Delete</el-button>
      <el-input v-model="searchWord" placeholder="Filter Keywords"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true"
        >Add Song</el-button
      >
    </div>
    <el-table
      height="550px"
      border
      size="small"
      :data="data"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column
        label="ID"
        prop="id"
        width="50"
        align="center"
      ></el-table-column>
      <el-table-column label="Singer Image" width="110" align="center">
        <template v-slot="scope">
          <div style="width: 80px; height: 80px; overflow: hidden">
            <img :src="attachImageUrl(scope.row.pic)" style="width: 100%" />
          </div>
          <div class="play" @click="setSongUrl(scope.row)">
            <svg class="icon" aria-hidden="true">
              <use
                :xlink:href="toggle === scope.row.name ? playIcon : BOFANG"
              ></use>
            </svg>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="Song Name"
        prop="name"
        width="150"
      ></el-table-column>
      <el-table-column
        label="Album"
        prop="introduction"
        width="150"
      ></el-table-column>
      <el-table-column label="Lyrics" align="center">
        <template v-slot="scope">
          <ul style="height: 100px; overflow: scroll">
            <li
              v-for="(item, index) in parseLyric(scope.row.lyric)"
              :key="index"
            >
              {{ item }}
            </li>
          </ul>
        </template>
      </el-table-column>
      <el-table-column label="Resource Update" width="120" align="center">
        <template v-slot="scope">
          <el-upload
            :action="updateSongImg(scope.row.id)"
            :show-file-list="false"
            :on-success="handleImgSuccess"
            :before-upload="beforeImgUpload"
          >
            <el-button>Update Image</el-button>
          </el-upload>
          <br />
          <el-upload
            :action="updateSongUrl(scope.row.id)"
            :show-file-list="false"
            :on-success="handleSongSuccess"
            :before-upload="beforeSongUpload"
          >
            <el-button>Update Song</el-button>
          </el-upload>
          <br />
          <el-upload
            :action="updateSongLrc(scope.row.id)"
            :show-file-list="false"
            :on-success="handleSongSuccess"
            :before-upload="beforeSongUpload"
          >
            <el-button>Update Lyrics</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column label="Comments" width="90" align="center">
        <template v-slot="scope">
          <el-button @click="goCommentPage(scope.row.id)">Comments</el-button>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="160" align="center">
        <template v-slot="scope">
          <el-button @click="editRow(scope.row)">Edit</el-button>
          <el-button type="danger" @click="deleteRow(scope.row.id)"
            >Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
      background
      layout="total, prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="tableData.length"
      @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>

  <!-- Add Song Dialog -->
  <el-dialog title="Add Song" v-model="centerDialogVisible">
    <el-form id="add-song" label-width="120px" :model="registerForm">
      <el-form-item label="Song Name">
        <el-input
          type="text"
          name="name"
          v-model="registerForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="Album">
        <el-input
          type="text"
          name="introduction"
          v-model="registerForm.introduction"
        ></el-input>
      </el-form-item>
      <el-form-item label="Lyrics (Upload lrc file if available)">
        <el-input
          type="textarea"
          name="lyric"
          v-model="registerForm.lyric"
        ></el-input>
      </el-form-item>
      <el-form-item label="Lyrics lrc Upload">
        <input type="file" name="lrcfile" />
      </el-form-item>
      <el-form-item label="Song Upload">
        <input type="file" name="file" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="addSong">Confirm</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- Edit Dialog -->
  <el-dialog title="Edit" v-model="editVisible">
    <el-form :model="editForm">
      <el-form-item label="Song">
        <el-input v-model="editForm.name"></el-input>
      </el-form-item>
      <el-form-item label="Introduction">
        <el-input v-model="editForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="Lyrics">
        <el-input type="textarea" v-model="editForm.lyric"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveEdit">Confirm</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- Delete Confirmation Dialog -->
  <yin-del-dialog
    :delVisible="delVisible"
    @confirm="confirm"
    @cancelRow="delVisible = $event"
  ></yin-del-dialog>
</template>

<script lang="ts">
import {
  defineComponent,
  getCurrentInstance,
  watch,
  ref,
  reactive,
  computed,
} from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { Icon, RouterName } from "@/enums";
import { HttpManager } from "@/api";
import { parseLyric } from "@/utils";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";
import { useRoute } from "vue-router";

export default defineComponent({
  components: {
    YinDelDialog,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, beforeImgUpload, beforeSongUpload } = mixin();
    const store = useStore();
    const route = useRoute();

    const tableData = ref([]); // Records songs for display
    const tempDate = ref([]); // Records songs for temporary search
    const pageSize = ref(5); // Number of items per page
    const currentPage = ref(1); // Current page number
    const singerId = ref("");
    const singerName = ref("");
    const toggle = ref(false); // Controls play icon status
    const BOFANG = ref(Icon.BOFANG);
    const ZANTING = ref(Icon.ZANTING);
    const breadcrumbList = computed(() => store.getters.breadcrumbList);

    const isPlay = computed(() => store.getters.isPlay); // Play status
    const playIcon = computed(() =>
      isPlay.value ? ZANTING.value : BOFANG.value
    ); // Play status

    // Computes the current table data
    const data = computed(() => {
      return tableData.value.slice(
        (currentPage.value - 1) * pageSize.value,
        currentPage.value * pageSize.value
      );
    });

    const searchWord = ref(""); // Records input text for search
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempDate.value;
      } else {
        tableData.value = [];
        for (let item of tempDate.value) {
          if (item.name.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    singerId.value = route.query.id as string;
    singerName.value = route.query.name as string;
    proxy.$store.commit("setIsPlay", false);
    getData();

    // Fetch songs
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await HttpManager.getSongOfSingerId(
        singerId.value
      )) as ResponseBody;
      tableData.value = result.data;
      tempDate.value = result.data;
      currentPage.value = 1;
    }

    function setSongUrl(row) {
      proxy.$store.commit("setUrl", row.url);
      toggle.value = row.name;
      if (isPlay.value) {
        proxy.$store.commit("setIsPlay", false);
      } else {
        proxy.$store.commit("setIsPlay", true);
      }
    }

    // Update song image
    function updateSongImg(id) {
      return HttpManager.updateSongImg(id);
    }
    function updateSongUrl(id) {
      return HttpManager.updateSongUrl(id);
    }
    function updateSongLrc(id) {
      return HttpManager.updateSongLrc(id);
    }

    // Handle page change
    function handleCurrentChange(val) {
      currentPage.value = val;
    }

    function handleSongSuccess(res) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    function handleLyricsSuccess(res) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    // Handle image update
    function handleImgSuccess(res, file) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    /**
     * Routing
     */
    function goCommentPage(id) {
      const breadcrumbList = reactive([
        {
          path: RouterName.Singer,
          name: "Singer Management",
        },
        {
          path: RouterName.Song,
          query: {
            id: singerId.value,
            name: singerName.value,
          },
          name: "Song Information",
        },
        {
          path: "",
          name: "Comment Details",
        },
      ]);
      proxy.$store.commit("setBreadcrumbList", breadcrumbList);
      routerManager(RouterName.Comment, {
        path: RouterName.Comment,
        query: { id, type: 0 },
      });
    }

    /**
     * Add Song
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      name: "",
      singerName: "",
      introduction: "",
      lyric: "",
    });

    function addSong() {
      const addSongForm = new FormData(
        document.getElementById("add-song") as HTMLFormElement
      );
      addSongForm.append("singerId", singerId.value);
      addSongForm.set("name", singerName.value + "-" + addSongForm.get("name"));
      if (!addSongForm.get("lyric"))
        addSongForm.set("lyric", "[00:00:00] No Lyrics");

      const req = new XMLHttpRequest();
      req.onreadystatechange = () => {
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response);
          (proxy as any).$message({
            message: res.message,
            type: res.type,
          });
          if (res.success) {
            getData();
            registerForm.name = "";
            registerForm.singerName = "";
            registerForm.introduction = "";
            registerForm.lyric = "";
          }
        }
      };
      req.open("post", HttpManager.attachImageUrl(`/song/add`), false);
      req.send(addSongForm);
      centerDialogVisible.value = false;
    }

    /**
     * Edit Song
     */
    const editVisible = ref(false);
    const editForm = reactive({
      id: "",
      singerId: "",
      name: "",
      introduction: "",
      createTime: "",
      updateTime: "",
      pic: "",
      lyric: "",
      url: "",
    });

    function editRow(row) {
      idx.value = row.id;
      editForm.id = row.id;
      editForm.singerId = row.singerId;
      editForm.name = row.name;
      editForm.introduction = row.introduction;
      editForm.createTime = row.createTime;
      editForm.updateTime = row.updateTime;
      editForm.pic = row.pic;
      editForm.lyric = row.lyric;
      editForm.url = row.url;
      editVisible.value = true;
    }

    async function saveEdit() {
      let id = editForm.id;
      let singerId = editForm.singerId;
      let name = editForm.name;
      let introduction = editForm.introduction;
      let lyric = editForm.lyric;
      const result = (await HttpManager.updateSongMsg({
        id,
        singerId,
        name,
        introduction,
        lyric,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      editVisible.value = false;
    }

    /**
     * Delete Song
     */
    const idx = ref(-1); // Record the current row to delete
    const multipleSelection = ref([]); // Record the current list to delete
    const delVisible = ref(false); // Show delete dialog

    async function confirm() {
      const result = (await HttpManager.deleteSong(idx.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      delVisible.value = false;
    }

    function deleteRow(id) {
      idx.value = id;
      delVisible.value = true;
    }

    function handleSelectionChange(val) {
      multipleSelection.value = val;
    }

    function deleteAll() {
      for (let item of multipleSelection.value) {
        deleteRow(item.id);
        confirm();
      }
      multipleSelection.value = [];
    }

    return {
      playIcon,
      toggle,
      searchWord,
      data,
      editForm,
      registerForm,
      tableData,
      centerDialogVisible,
      editVisible,
      delVisible,
      pageSize,
      currentPage,
      ZANTING,
      BOFANG,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      handleCurrentChange,
      handleImgSuccess,
      beforeImgUpload,
      parseLyric,
      saveEdit,
      updateSongImg,
      updateSongUrl,
      updateSongLrc,
      deleteRow,
      confirm,
      attachImageUrl: HttpManager.attachImageUrl,
      addSong,
      editRow,
      handleSongSuccess,
      setSongUrl,
      handleLyricsSuccess,
      goCommentPage,
    };
  },
});
</script>

<style scoped>
.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  top: 18px;
  left: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}
</style>
