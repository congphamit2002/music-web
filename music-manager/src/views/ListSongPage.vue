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
      <el-button type="primary" @click="centerDialogVisible = true">
        Add Song
      </el-button>
    </div>
    <el-table
      height="550px"
      border
      size="small"
      :data="tableData"
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
      <el-table-column label="Artist-Song" prop="name"></el-table-column>
      <el-table-column label="Actions" width="90" align="center">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteRow(scope.row.id)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- Add Song -->
  <el-dialog title="Add Song" v-model="centerDialogVisible">
    <el-form label-width="80px" :model="registerForm">
      <el-form-item prop="singerName" label="Artist Name">
        <el-input v-model="registerForm.singerName"></el-input>
      </el-form-item>
      <el-form-item prop="songName" label="Song Name">
        <el-input v-model="registerForm.songName"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveSong()">Confirm</el-button>
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
import { HttpManager } from "@/api";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";
import { useRoute } from "vue-router";

export default defineComponent({
  components: {
    YinDelDialog,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const route = useRoute();

    const tableData = ref([]); // Records songs for display
    const tempDate = ref([]); // Records songs to temporarily store a copy for search
    const breadcrumbList = computed(() => store.getters.breadcrumbList);

    const searchWord = ref(""); // Records the content entered in the input box
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

    getData();

    // Fetch song list
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await HttpManager.getListSongOfSongListId(
        route.query.id
      )) as ResponseBody;
      for (let item of result.data) {
        const result = (await HttpManager.getSongOfId(
          item.songId
        )) as ResponseBody;
        tableData.value.push(result.data);
        tempDate.value.push(result.data);
      }
    }

    /**
     * Add Song
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      singerName: "",
      songName: "",
    });

    // Get the ID of the song to be added
    async function saveSong() {
      const id = `${registerForm.singerName}-${registerForm.songName}`;
      const result = (await HttpManager.getSongOfSingerName(
        id
      )) as ResponseBody;

      if (result.success) {
        addSong(result.data[0].id);
      } else {
        alert(result.message);
        centerDialogVisible.value = false;
      }
    }
    async function addSong(id) {
      let songId = id;
      let songListId = route.query.id as string;

      const result = (await HttpManager.setListSong({
        songId,
        songListId,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) {
        getData();
      }

      centerDialogVisible.value = false;
    }

    /**
     * Delete
     */
    const idx = ref(-1); // Records the current row to be deleted
    const multipleSelection = ref([]); // Records the current list to be deleted
    const delVisible = ref(false); // Shows the delete dialog

    async function confirm() {
      const result = (await HttpManager.deleteListSong(
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
      tableData,
      delVisible,
      centerDialogVisible,
      registerForm,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      deleteRow,
      confirm,
      saveSong,
    };
  },
});
</script>

<style scoped></style>
