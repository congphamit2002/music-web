<template>
  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">Batch Delete</el-button>
      <el-input v-model="searchWord" placeholder="Filter Keywords"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true"
        >Add Playlist</el-button
      >
      <el-button type="primary" @click="exportPlaylist"
        >Export Playlist</el-button
      >
    </div>
    <el-table
      height="550px"
      border
      size="small"
      :data="data"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="40"
        align="center"
      ></el-table-column>
      <el-table-column
        label="ID"
        prop="id"
        width="50"
        align="center"
      ></el-table-column>
      <el-table-column label="Playlist Image" width="110" align="center">
        <template v-slot="scope">
          <img :src="attachImageUrl(scope.row.pic)" style="width: 80px" />
          <el-upload
            :action="uploadUrl(scope.row.id)"
            :show-file-list="false"
            :on-success="handleImgSuccess"
            :before-upload="beforeImgUpload"
          >
            <el-button>Update Image</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="Title" width="200"></el-table-column>
      <el-table-column label="Introduction">
        <template v-slot="scope">
          <p style="height: 100px; overflow: scroll">
            {{ scope.row.introduction }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="Style" prop="style" width="100"></el-table-column>
      <el-table-column label="Content" width="90" align="center">
        <template v-slot="scope">
          <el-button @click="goContentPage(scope.row.id)">Content</el-button>
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

  <!-- Add Playlist -->
  <el-dialog title="Add Playlist" v-model="centerDialogVisible">
    <el-form label-width="70px" :model="registerForm">
      <el-form-item label="Playlist Name" prop="title">
        <el-input v-model="registerForm.title"></el-input>
      </el-form-item>
      <el-form-item label="Playlist Introduction" prop="introduction">
        <el-input v-model="registerForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="Style" prop="style">
        <el-input v-model="registerForm.style"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="addsongList">Confirm</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- Edit Dialog -->
  <el-dialog title="Edit" v-model="editVisible">
    <el-form :model="editForm">
      <el-form-item label="Title">
        <el-input v-model="editForm.title"></el-input>
      </el-form-item>
      <el-form-item label="Introduction">
        <el-input type="textarea" v-model="editForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="Style">
        <el-input v-model="editForm.style"></el-input>
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
  computed,
  defineComponent,
  getCurrentInstance,
  reactive,
  ref,
  watch,
} from "vue";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api/index";
import { RouterName } from "@/enums";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";
import axios from "axios";

export default defineComponent({
  components: {
    YinDelDialog,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, beforeImgUpload } = mixin();

    const tableData = ref([]); // Records playlists for display
    const tempDate = ref([]); // Records playlists for temporary storage during search
    const pageSize = ref(5); // Number of items per page
    const currentPage = ref(1); // Current page

    // Compute the data for the current page
    const data = computed(() => {
      return tableData.value.slice(
        (currentPage.value - 1) * pageSize.value,
        currentPage.value * pageSize.value
      );
    });

    const searchWord = ref(""); // Stores the content of the search input
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempDate.value;
      } else {
        tableData.value = [];
        for (let item of tempDate.value) {
          if (item.title.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    getData();

    // Fetch playlist data
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await HttpManager.getSongList()) as ResponseBody;
      tableData.value = result.data;
      tempDate.value = result.data;
      currentPage.value = 1;
    }

    function exportPlaylist() {
      axios({
        method: "get",
        url: "http://localhost:8888/excle",
        responseType: "blob", // Set response type to blob
      })
        .then((response) => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", "SongList.xlsx"); // Set the download filename
          document.body.appendChild(link);
          link.click();
          link.remove();
        })
        .catch((error) => {
          console.error("Failed to export playlist:", error);
        });
    }

    // Handle page change
    function handleCurrentChange(val) {
      currentPage.value = val;
    }

    function uploadUrl(id) {
      return HttpManager.updateSongListImg(id);
    }

    // Handle image upload success
    function handleImgSuccess(response, file) {
      (proxy as any).$message({
        message: response.message,
        type: response.type,
      });
      if (response.success) getData();
    }

    /**
     * Routing
     */
    function goContentPage(id) {
      const breadcrumbList = reactive([
        {
          path: RouterName.SongList,
          name: "Playlist Management",
        },
        {
          path: "",
          name: "Playlist Content",
        },
      ]);
      proxy.$store.commit("setBreadcrumbList", breadcrumbList);
      routerManager(RouterName.ListSong, {
        path: RouterName.ListSong,
        query: { id },
      });
    }

    function goCommentPage(id) {
      const breadcrumbList = reactive([
        {
          path: RouterName.SongList,
          name: "Playlist Management",
        },
        {
          path: "",
          name: "Comment Details",
        },
      ]);
      proxy.$store.commit("setBreadcrumbList", breadcrumbList);
      routerManager(RouterName.Comment, {
        path: RouterName.Comment,
        query: { id, type: 1 },
      });
    }

    /**
     * Add Playlist
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      title: "",
      introduction: "",
      style: "",
    });

    async function addsongList() {
      let title = registerForm.title;
      let introduction = registerForm.introduction;
      let style = registerForm.style;
      const result = (await HttpManager.setSongList({
        title,
        introduction,
        style,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) {
        getData();
        registerForm.title = "";
        registerForm.introduction = "";
        registerForm.style = "";
      }
      centerDialogVisible.value = false;
    }

    /**
     * Edit
     */
    const editVisible = ref(false);
    const editForm = reactive({
      id: "",
      title: "",
      pic: "",
      introduction: "",
      style: "",
    });

    function editRow(row) {
      idx.value = row.id;
      editForm.id = row.id;
      editForm.title = row.title;
      editForm.pic = row.pic;
      editForm.introduction = row.introduction;
      editForm.style = row.style;
      editVisible.value = true;
    }

    async function saveEdit() {
      let id = editForm.id;
      let title = editForm.title;
      let introduction = editForm.introduction;
      let style = editForm.style;

      const result = (await HttpManager.updateSongListMsg({
        id,
        title,
        introduction,
        style,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) getData();
      editVisible.value = false;
    }

    /**
     * Delete
     */
    const idx = ref(-1); // Records the row currently to be deleted
    const multipleSelection = ref([]); // Records the list currently to be deleted
    const delVisible = ref(false); // Show delete confirmation dialog

    async function confirm() {
      const result = (await HttpManager.deleteSongList(
        idx.value
      )) as ResponseBody;
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
      searchWord,
      data,
      tableData,
      centerDialogVisible,
      editVisible,
      delVisible,
      pageSize,
      currentPage,
      registerForm,
      editForm,
      addsongList,
      deleteAll,
      handleSelectionChange,
      handleImgSuccess,
      beforeImgUpload,
      saveEdit,
      attachImageUrl: HttpManager.attachImageUrl,
      uploadUrl,
      editRow,
      handleCurrentChange,
      exportPlaylist,
      confirm,
      deleteRow,
      goContentPage,
      goCommentPage,
    };
  },
});
</script>

<style scoped></style>
