<template>
  <div>
    <!-- Delete confirmation dialog -->
    <el-dialog
      title="Prompt"
      v-model="centerDialogVisible"
      width="300px"
      center
    >
      <div class="del-dialog-cnt" align="center">
        Deletion is irreversible, are you sure you want to delete?
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelRow">Cancel</el-button>
          <el-button type="primary" @click="confirm">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, toRefs, watch, ref } from "vue";

export default defineComponent({
  props: {
    delVisible: Boolean,
  },
  emits: ["cancelRow", "confirm"],
  setup(props) {
    const { proxy } = getCurrentInstance();

    const { delVisible } = toRefs(props);
    const centerDialogVisible = ref(delVisible.value);

    watch(delVisible, (value) => {
      centerDialogVisible.value = value;
    });

    function cancelRow() {
      proxy.$emit("cancelRow", false);
    }
    function confirm() {
      proxy.$emit("confirm", null);
    }
    return {
      centerDialogVisible,
      cancelRow,
      confirm,
    };
  },
});
</script>
