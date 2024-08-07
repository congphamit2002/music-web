import { createStore } from "vuex";
import configure from "./configure";
import song from "./song";

export default createStore({
  modules: {
    configure,
    song,
  },
});
