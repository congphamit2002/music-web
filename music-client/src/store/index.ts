import { createStore } from "vuex";
import configure from "./configure";
import song from "./song";
import user from "./user";

export default createStore({
  modules: {
    configure,
    song,
    user
  },
});
