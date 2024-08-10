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
      {
        path: "/singer",
        component: () => import("@/views/SingerPage.vue"),
        meta: { title: "Singer" },
      },
      {
        path: "/song",
        component: () => import("@/views/SongPage.vue"),
        meta: { title: "Song" },
      },
      {
        path: "/Comment",
        component: () => import("@/views/CommentPage.vue"),
        meta: { title: "Comment" },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
