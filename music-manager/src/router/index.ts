import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
  {
    path: "/home",
    component: () => import("@/views/Home.vue"),
    children: [
      {
        path: "/song-list",
        component: () => import("@/views/SongListPage.vue"),
        meta: { title: "SongList" },
      },
      {
        path: "/list-song",
        component: () => import("@/views/ListSongPage.vue"),
        meta: { title: "ListSong" },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
