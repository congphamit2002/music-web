<template>
  <el-row :gutter="20">
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="card-left">
            <el-icon><user /></el-icon>
          </div>
          <div class="card-right">
            <div class="card-num">{{ userCount }}</div>
            <div>Total Users</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="card-left">
            <el-icon><headset /></el-icon>
          </div>
          <div class="card-right">
            <div class="card-num">{{ songCount }}</div>
            <div>Total Songs</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="card-left">
            <el-icon><mic /></el-icon>
          </div>
          <div class="card-right">
            <div class="card-num">{{ singerCount }}</div>
            <div>Number of Singers</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="card-left">
            <el-icon><document /></el-icon>
          </div>
          <div class="card-right">
            <div class="card-num">{{ songListCount }}</div>
            <div>Number of Playlists</div>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12">
      <h3>User Gender Ratio</h3>
      <el-card
        class="cav-info"
        shadow="hover"
        :body-style="{ padding: '0px' }"
        id="userSex"
      ></el-card>
    </el-col>
    <el-col :span="12">
      <h3>Song Genres</h3>
      <el-card
        class="cav-info"
        shadow="hover"
        :body-style="{ padding: '0px' }"
        id="songStyle"
      ></el-card>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12">
      <h3>Singer Gender Ratio</h3>
      <el-card
        class="cav-info"
        shadow="hover"
        :body-style="{ padding: '0px' }"
        id="singerSex"
      ></el-card>
    </el-col>
    <el-col :span="12">
      <h3>Singer Nationalities</h3>
      <el-card
        class="cav-info"
        shadow="hover"
        :body-style="{ padding: '0px' }"
        id="country"
      ></el-card>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
// import { ref, reactive, getCurrentInstance } from "vue";
import { ref, reactive } from "vue";
import * as echarts from "echarts";
import { Mic, Document, User, Headset } from "@element-plus/icons-vue";
import { HttpManager } from "@/api/index";

// const { proxy } = getCurrentInstance();
const userCount = ref(0);
const songCount = ref(0);
const singerCount = ref(0);
const songListCount = ref(0);
const userSex = reactive({
  series: [
    {
      type: "pie",
      data: [
        {
          value: 0,
          name: "Male",
        },
        {
          value: 0,
          name: "Female",
        },
      ],
    },
  ],
});
const songStyle = reactive({
  xAxis: {
    type: "category",
    data: [
      "Mandarin",
      "Cantonese",
      "Western",
      "Japanese and Korean",
      "BGM",
      "Light Music",
      "Instrumental",
    ],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [0, 0, 0, 0, 0, 0, 0],
      type: "bar",
      barWidth: "20%",
    },
  ],
});
const singerSex = reactive({
  series: [
    {
      type: "pie",
      data: [
        {
          value: 0,
          name: "Male",
        },
        {
          value: 0,
          name: "Female",
        },
      ],
    },
  ],
});
const country = reactive({
  xAxis: {
    type: "category",
    data: [
      "China",
      "South Korea",
      "Italy",
      "Singapore",
      "USA",
      // "Malaysia",
      "Spain",
      "Japan",
    ],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [0, 0, 0, 0, 0, 0, 0, 0],
      type: "bar",
      barWidth: "20%",
    },
  ],
});

function setSex(sex, arr) {
  let value = 0;
  const name = sex === 0 ? "Male" : "Female";
  for (let item of arr) {
    if (sex === item.sex) {
      value++;
    }
  }
  return { value, name };
}

// TODO: fix them
// HttpManager.getAllUser().then((res) => {
//   const result = res as ResponseBody;
//   userCount.value = result.data.length;
//   userSex.series[0].data.push(setSex(0, result.data));
//   userSex.series[0].data.push(setSex(1, result.data));

//   // const userSexChart = echarts.init(proxy.$refs.userSex);
//   const userSexChart = echarts.init(document.getElementById("userSex"));
//   userSexChart.setOption(userSex);
// });

HttpManager.getAllSong().then((res) => {
  songCount.value = (res as ResponseBody).data.length;
});
HttpManager.getSongList().then((res) => {
  const result = res as ResponseBody;
  songListCount.value = result.data.length;
  for (let item of result.data) {
    for (let i = 0; i < songStyle.xAxis.data.length; i++) {
      if (item.style.includes(songStyle.xAxis.data[i])) {
        songStyle.series[0].data[i]++;
      }
    }
  }
  // const songStyleChart = echarts.init(proxy.$refs.songStyle);
  const songStyleChart = echarts.init(document.getElementById("songStyle"));
  songStyleChart.setOption(songStyle);
});

HttpManager.getAllSinger().then((res) => {
  const result = res as ResponseBody;
  singerCount.value = result.data.length;
  singerSex.series[0].data.push(setSex(0, result.data));
  singerSex.series[0].data.push(setSex(1, result.data));
  const singerSexChart = echarts.init(document.getElementById("singerSex"));
  singerSexChart.setOption(singerSex);

  for (let item of result.data) {
    for (let i = 0; i < country.xAxis.data.length; i++) {
      if (item.location.includes(country.xAxis.data[i])) {
        country.series[0].data[i]++;
      }
    }
  }
  const countryChart = echarts.init(document.getElementById("country"));
  countryChart.setOption(country);
});
</script>

<style scoped>
.card-content {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 100px;
  padding-left: 20%;
  text-align: center;
}

.card-left {
  display: flex;
  font-size: 3rem;
}

.card-right {
  flex: 1;
  font-size: 14px;
}

.card-num {
  font-size: 30px;
  font-weight: bold;
}

h3 {
  margin: 10px 0;
  text-align: center;
}
.cav-info {
  border-radius: 6px;
  overflow: hidden;
  height: 250px;
  background-color: white;
}
</style>
