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
      <el-button @click="deleteAll">Bulk Delete</el-button>
      <el-input v-model="searchWord" placeholder="Filter Keywords"></el-input>
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
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column
        prop="username"
        label="User"
        width="80"
      ></el-table-column>
      <el-table-column prop="content" label="Comment Content"></el-table-column>
      <el-table-column label="Action" width="100" align="center">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteRow(scope.row.id)"
            >Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- Delete confirmation dialog -->
  <yin-del-dialog
    :delVisible="delVisible"
    @confirm="confirm"
    @cancelRow="delVisible = $event"
  ></yin-del-dialog>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, watch, ref, computed } from "vue";
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

    const tableData = ref([]); // Records comments for display
    const tempData = ref([]); // Temporarily records comments for searching
    const breadcrumbList = computed(() => store.getters.breadcrumbList);

    const searchWord = ref(""); // Records the content entered in the input box
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempData.value;
      } else {
        tableData.value = [];
        for (let item of tempData.value) {
          if (item.username.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    getData();

    // Fetch comments
    function getData() {
      tableData.value = [];
      tempData.value = [];
      let promise = null;
      if (route.query.type == "0") {
        promise = HttpManager.getCommentOfSongId(route.query.id);
      } else if (route.query.type == "1") {
        promise = HttpManager.getCommentOfSongListId(route.query.id);
      }

      promise.then((res) => {
        console.log("check item ", res);
        for (let item of (res as ResponseBody).data) {
          getUsers(item.userId, item);
        }
      });
    }
    async function getUsers(id, item) {
      const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
      console.log("Check result ", result)
      item.username = result.data.username;
      console.log("check item ", item);
      tableData.value.push(item);
      tempData.value.push(item);
    }

    /**
     * Delete
     */
    const idx = ref(-1); // Records the ID of the row to be deleted
    const multipleSelection = ref([]); // Records the list of items to be deleted
    const delVisible = ref(false); // Controls the visibility of the delete confirmation dialog

    async function confirm() {
      const result = (await HttpManager.deleteComment(
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
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      deleteRow,
      confirm,
    };
  },
});
</script>

<style scoped></style>
