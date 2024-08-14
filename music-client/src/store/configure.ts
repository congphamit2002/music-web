export default {
  state: {
    token: false, // User login status
    showAside: false, // Whether to display the sidebar
    searchWord: "", // Search keyword
    activeNavName: "", // Name of the active navigation
  },
  getters: {
    token: (state) => state.token,
    activeNavName: (state) => state.activeNavName,
    showAside: (state) => state.showAside,
    searchWord: (state) => state.searchWord,
  },
  mutations: {
    setToken: (state, token) => {
      state.token = token;
    },
    setActiveNavName: (state, activeNavName) => {
      state.activeNavName = activeNavName;
    },
    setShowAside: (state, showAside) => {
      state.showAside = showAside;
    },
    setSearchWord: (state, searchWord) => {
      state.searchWord = searchWord;
    },
  },
};
