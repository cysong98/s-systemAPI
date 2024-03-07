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
    <div class="header-wrap" :class="{'hidden': topVisible}">
      <div id="header">
        <h1 class="h1"><RouterLink to="/home/Home"><img src="@/assets/images/logo.png" alt=""></RouterLink></h1>
        <nav class="gnb">
          <RouterLink to="/home/Home" active-class="active" @click="clickTop('A')">HOME</RouterLink>
          <RouterLink to="/dct/BmsTodolist" active-class="active" @click="clickTop('B')">비밀문서</RouterLink>
          <RouterLink to="/dct/ManageTodolist" active-class="active" @click="clickTop('C')">비밀관리</RouterLink>
          <RouterLink v-if="manageflag" to="/com/BmsComnoticelist" active-class="active" @click="clickTop('D')">관리자</RouterLink>
        </nav>
        <div class="user-menu">
          <button type="button" class="button-icon i05" @click="toggleOrgViewPopup">
            <strong class="title">조직 조회</strong>
          </button>
          <button type="button" class="button-icon i01" @click="toggleDctKeywordPopup">
            <strong class="title">키워드 검색</strong>
          </button>
          <button type="button" class="button-icon i02" @click="toggleAlert">
            <strong class="title2">예고문도래 알림</strong>
            <span v-if="isNeedBadge" class="badge"></span>  
          </button>
          <button type="button" class="button-icon i03">
            <strong class="title">개인정보</strong>  
            <v-menu activator="parent" location="bottom" class="manage">
              <v-list>
                <v-list-item
                  v-for="(item, index) in manageItems"
                  :key="index"
                  :value="index"
                  @click="handleMenuClick(index)"
                >
                  <v-list-item-title>{{ item.title }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </button>
          <button type="button" class="button-icon i04" @click="logout">
            <strong class="title">로그아웃</strong>  
          </button>
          <button type="button" class="button-profile">
            <!-- <img src="https://t1.daumcdn.net/cfile/tistory/24283C3858F778CA2E?original" alt="" class="image"> -->
            <strong class="name">{{ getUserLoginData.deptname+' / '+getUserLoginData.username }}님</strong>
          </button>

          <!-- 알림 오버레이 -->
          <AlertList
            v-if="visibleAlert"
            :toggleFunc="toggleAlert"
          ></AlertList>

        </div>
      </div>
    </div>

    <!-- 조직 조회 팝업 -->
    <v-dialog v-model="visibleBmsComOrgViewPopup">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>부서/사용자 조회</v-toolbar-title>
          <v-btn class="close" icon @click="visibleBmsComOrgViewPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <BmsComOrgViewPopup
          v-if="visibleBmsComOrgViewPopup"
          :toggleFunc="toggleOrgViewPopup"
        ></BmsComOrgViewPopup>
      </v-card>
    </v-dialog>

    <!-- 키워드 조회 팝업 -->
    <v-dialog v-model="visibleDctKeywordPopup">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>키워드 검색</v-toolbar-title>
          <v-btn class="close" icon @click="visibleDctKeywordPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctKeywordPopup
          v-if="visibleDctKeywordPopup"
          :toggleFunc="toggleDctKeywordPopup"
        ></DctKeywordPopup>
      </v-card>
    </v-dialog>

    <!-- 개인문서소유현황 팝업 -->
    <v-dialog v-model="visibleManagePopup" width="750px">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>개인소유문서 현황</v-toolbar-title>
          <v-btn class="close" icon @click="visibleManagePopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <MyDocStatePopup
          v-if="visibleManagePopup"
          :toggleFunc="toggleManagePopup"
        ></MyDocStatePopup>
      </v-card>
    </v-dialog>

  <!-- 개인정보 변경 -->
  <v-dialog v-model="visibleBmsComUserUpdatePopup" width="800px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>개인정보 변경</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComUserUpdatePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComUserUpdate
        v-if="visibleBmsComUserUpdatePopup"
        :args="userArgs"
        :isMyPage="'true'"
        :toggleFunc="toggleUserUpdatePopup"
        :returnFunc="returnUserUpdatePopup"
      ></BmsComUserUpdate>
    </v-card>
  </v-dialog>

  <!-- 부재 설정 -->
  <v-dialog v-model="visibleAbsencePopup" width="800px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부재 설정</v-toolbar-title>
        <v-btn class="close" icon @click="visibleAbsencePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <AbsencePopup
        v-if="visibleAbsencePopup"
        :toggleFunc="toggleAbsencePopup"
      ></AbsencePopup>
    </v-card>
  </v-dialog>

</template>

<script setup>
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

import { useMainStore } from '/src/store/Main';
import BmsComOrgViewPopup from '../com/BmsComOrgViewPopup.vue';
import DctKeywordPopup from '../dct/DctKeywordPopup.vue';
import { tokenExpiredCheck, parseJwt, clearStorage } from "@/components/login/ShieldgateLogin.js";
import AlertList from '../use/AlertList.vue';
import MyDocStatePopup from '../use/MyDocStatePopup.vue';
import AbsencePopup from '../use/AbsencePopup.vue';
import BmsComUserUpdate from "../com/BmsComUserUpdate.vue";
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('HomeTop')
const route = useRoute()
const router = useRouter()

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const manageflag = ref(false);

onMounted(async() => {
  // 로그인 정보가 없는 경우
  if(getUserLoginData.value.username == undefined || getUserLoginData.value.username == null || getUserLoginData.value.username == "") {
    /* 개발용 */
    router.push({
      name: "Login",
    });
  }
  else if (getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')) {
    manageflag.value = true;
  }
  await selectAlert();
});

const logout = async() => {
  /* ====================== IdGP Login ======================= */
  // tokenExpiredCheck(sessionStorage.getItem('Refresh-Authorization'));
  // const logoutCondi = {
  //   accessToken: sessionStorage.getItem("Access-Token"),
  //   userAccount: getUserLoginData.value.userid
  // };

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
  //   token: sessionStorage.getItem('Refresh-Authorization'),
  //   flag: "N"
  // });

  // props.returnFunc(true);
  // await API.loginAPI.logout(logoutCondi, '')
  //   .then(async (res) => {
  //     const decodeJwt = parseJwt(sessionStorage.getItem("Authorization"));
  //     InsertSystemLoginLogData.value.userid = decodeJwt.user_email;
  //     InsertSystemLoginLogData.value.ip = decodeJwt.ipAddress;
  //     updateRealtimeLogData.value.userid = decodeJwt.user_email;
  //     await API.comAPI.insertSystemLoginLog(InsertSystemLoginLogData.value, '');
  //     await API.comAPI.updateRealtimeLog(updateRealtimeLogData.value, '');
  //   });

  //   clearStorage();
  // props.returnFunc(false);
  /* ========================================================== */

  router.push({
    name: "Login",
  });
};

const clickTop = (data) => {
  topMenu.value.data = data;
  sessionStorage.setItem("topmenu", topMenu.value.data);
  if(data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
    breadcrumbs.value.activeLink = ['비밀문서', '처리할일'];
    sessionStorage.setItem("breadcrumbs.topMenu", breadcrumbs.value.topMenu);
    sessionStorage.setItem("breadcrumbs.activeLink", breadcrumbs.value.activeLink);
  } else if (data == 'C'){
    breadcrumbs.value.topMenu = '비밀관리';
    breadcrumbs.value.activeLink = ['비밀관리', '처리할일'];
    sessionStorage.setItem("breadcrumbs.topMenu", breadcrumbs.value.topMenu);
    sessionStorage.setItem("breadcrumbs.activeLink", breadcrumbs.value.activeLink);
  } else if (data == 'D'){
    breadcrumbs.value.topMenu = '관리자';
    breadcrumbs.value.activeLink = ['관리자', '공지사항 관리'];
    sessionStorage.setItem("breadcrumbs.topMenu", breadcrumbs.value.topMenu);
    sessionStorage.setItem("breadcrumbs.activeLink", breadcrumbs.value.activeLink);
  } 
}

// toggleInsertPopup
const visibleBmsComOrgViewPopup = ref(false);
const toggleOrgViewPopup = ref(async () => {
  visibleBmsComOrgViewPopup.value = !visibleBmsComOrgViewPopup.value;
});

// toggleKeywordPopup DctKeywordPopup
const visibleDctKeywordPopup = ref(false);
const toggleDctKeywordPopup = ref(async () => {
  visibleDctKeywordPopup.value = !visibleDctKeywordPopup.value;
});

// toggleAlert
const visibleAlert = ref(false);
const toggleAlert = ref(async () => {
  visibleAlert.value = !visibleAlert.value;
});

const selectAlertCondi = ref({
  loginuserid: "",
});
const isNeedBadge = ref(false);
const selectAlert = async () => {
  selectAlertCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectAlert({...selectAlertCondi.value}, '');
    isNeedBadge.value = !response.data.every(data => data==null);
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// toggleManage
const visibleManagePopup = ref(false);
const toggleManagePopup = ref(async () => {
  visibleManagePopup.value = !visibleManagePopup.value;
});

// toggleUserUpdate
const visibleBmsComUserUpdatePopup = ref(false);
const userArgs = ref({});
const toggleUserUpdatePopup = ref(() => {
  userArgs.value.userid = getUserLoginData.value.userid;
  visibleBmsComUserUpdatePopup.value = !visibleBmsComUserUpdatePopup.value;
});
const returnUserUpdatePopup = ref(async() => {
  toggleUserUpdatePopup.value();
});

// toggleAbsence
const visibleAbsencePopup = ref(false);
const toggleAbsencePopup = ref(async () => {
  visibleAbsencePopup.value = !visibleAbsencePopup.value;
});

const manageItems = ref([
  {title : "개인정보 변경", key: '1'},
  {title : "부재 설정", key: '3' },
  {title : "개인소유문서 현황", key: '2'},
]);
const handleMenuClick = (index) => {
  const key = manageItems.value[index].key;
  if(key=='1'){
    toggleUserUpdatePopup.value();
  }else if(key=='2'){
    toggleManagePopup.value();
  }else{
    toggleAbsencePopup.value();
  }
};


const props = defineProps({
  topVisible: Boolean,
  returnFunc: Function,
})

</script>

<style lang="scss" scoped>
.hidden {
  height: 0;
  overflow: hidden;
}
</style>
