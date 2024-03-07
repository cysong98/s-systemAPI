<!--
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
-->

<template>
  <div :key="renderingKey">
    <div v-if="loginLoading" class="overlay">
      <div class="spinner"></div>
    </div>
    <master-layout>
      <router-view></router-view>
    </master-layout>
  </div>
</template>

<script setup>
import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, onBeforeUnmount, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import MasterLayout  from '@/layout/MasterLayout.vue';
import { API } from '@/api';

import { tokenExpiredCheck, parseJwt, goLoginPage, checkAndInsertRealtimeLog } from "@/components/login/ShieldgateLogin.js";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { useMainStore } from '/src/store/Main';

const name = ref('App')
const route = useRoute()
const router = useRouter()
const renderingKey = ref('load');
const loginLoading = ref(false);

const loginStore = useLoginStore();
const mainStore = useMainStore()
const { getUserLoginData, getUserLoginCondi, getUserLogin } = storeToRefs(loginStore);
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const InsertRealtimeLogData = ref({
  rtid: "RT" + dayjs(new Date()).format('YYYYMMDDHHmmSSS'),
  rtseq: "",
  indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
  userid: getUserLoginData.value.userid,
  username: "",
  deptid: "",
  deptname: "",
  flag: "",
  token: "",
});

/* ========================= IdGP 파트 시작 ========================= */
// watch(route, async (route) => {
//   loginLoading.value = true;
//   if(route.path !== '/login/Login') {
//     const jToken = sessionStorage.getItem('Authorization');
//     const rToken = sessionStorage.getItem('Refresh-Authorization');
//     const aToken = sessionStorage.getItem('Access-Token');

//     if (jToken !== null && rToken !== null && aToken !== null) {
//       /* ==================== 페이지가 새로고침됨 ==================== */
//       if(InsertRealtimeLogData.value.token === "") {
//         await API.comAPI.deleteSystemLoginLog({
//           userid: parseJwt(jToken).user_email,
//           token: rToken
//         }, '');
//       }
//       InsertRealtimeLogData.value.token = rToken;
//       /* =========================================================== */ 

//       await checkAndInsertRealtimeLog(parseJwt(jToken).user_email, rToken);
//       await tokenExpiredCheck(rToken);
//       if(getUserLoginData.value.userid === undefined || getUserLoginData.value.userid === null || getUserLoginData.value.userid === '') {
//         // for HomeTop, DetailLeft
//         topMenu.value.data = sessionStorage.getItem("topmenu");
//         breadcrumbs.value.topMenu = sessionStorage.getItem("breadcrumbs.topMenu");
//         breadcrumbs.value.activeLink = sessionStorage.getItem("breadcrumbs.activeLink");

//         // for getUserLoginData
//         getUserLoginCondi.value.jwt = sessionStorage.getItem("Authorization");
//         const decodeJwt = parseJwt(getUserLoginCondi.value.jwt);

//         getUserLoginCondi.value.userid = decodeJwt.user_email;
//         await loginStore.getUserLogin();
//         loginLoading.value = false;
//         renderingKey.value = "reload";
//       }
//     }
//     else goLoginPage();
//   }
//   loginLoading.value = false;
// })

// // 강제종료 전 데이터 저장
// const InsertSystemLoginLogData = ref({
//   opid: "",
//   userid: "",
//   logtime: dayjs(new Date()).format('YYYYMMDDHHmmss'),
//   gubun: "로그아웃",
//   ip: "",
//   kdnid: ""
// });
// const updateRealtimeLogData = ref({
//   userid: "",
//   flag: "N",
//   token: ""
// });

// window.addEventListener('unload', function() {
//   const decodeJwt = parseJwt(sessionStorage.getItem("Authorization"));

//   InsertSystemLoginLogData.value.logtime = dayjs(new Date()).format('YYYYMMDDHHmmss');
//   InsertSystemLoginLogData.value.userid = decodeJwt.user_email;
//   InsertSystemLoginLogData.value.ip = decodeJwt.ipAddress;
//   updateRealtimeLogData.value.userid = decodeJwt.user_email;
//   updateRealtimeLogData.value.token = sessionStorage.getItem("Refresh-Authorization");

//   API.comAPI.insertSystemLoginLog(InsertSystemLoginLogData.value, '');
//   API.comAPI.updateRealtimeLog(updateRealtimeLogData.value, '');
// });
/* =========================== IdGP 파트 끝 =========================== */

</script>

<style lang="scss">
table {
    width: 100%;
    border: 1px solid #444444;
    border-collapse: collapse;
  }
  th {
    border: 1px solid #444444;
    padding: 10px;
    background-color: lightgray;
  }
  td {
    border: 1px solid #444444;
    padding: 10px;
  }
</style>
