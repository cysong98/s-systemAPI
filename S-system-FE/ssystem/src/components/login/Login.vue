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
  <div v-if="loginLoading" class="overlay">
    <div class="spinner"></div>
  </div>
<!-- 임시태그 시작: 런칭시 삭제 -->
  <div class="members">
    <div>[기안-결재]</div>
    <div>1. 기안: 없음, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'song'; getUserLogin();">송채연(song)</v-btn></div>
    <div>2. 검토: 처리과 비밀관리자, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'kim3'; getUserLogin();">김검토(kim3)</v-btn></div>
    <div>3. 협조: 없음, 감사실 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'won'; getUserLogin();">원감사(won)</v-btn></div>
    <div>4. 결재: 시스템관리자, 전산팀 부서장 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'kim'; getUserLogin();">김전산(kim)</v-btn></div>
    <br>
    <div>[발송-접수]</div>
    <div>1. 발송: 없음, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'song'; getUserLogin();">송채연(song)</v-btn></div>
    <div>2. 접수 및 담당지정: 처리과 비밀관리자, 수출입통제실 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'an'; getUserLogin();">안수출(an)</v-btn></div>
    <div>3. 접수문서 기안: 없음, 수출입통제실 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'su'; getUserLogin();">수담당(su)</v-btn></div>
    <br>
    <div>[요청-사용]</div>
    <div>1. 요청서 작성: 없음, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'song'; getUserLogin();">송채연(song)</v-btn></div>
    <div>2. 이관 결재: 이관관리담당자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'kim'; getUserLogin();">김전산(kim)</v-btn></div>
    <div>3. 이관 결재: 이관관리부서장 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'kim3'; getUserLogin();">김검토(kim3)</v-btn></div>
    <div>4. 존안 결재: 존안관리담당자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'lee3'; getUserLogin();">이확산(lee3)</v-btn></div>
    <!-- <div>5. 존안 결재: 존안관리담당자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'na'; getUserLogin();">나지원(na)</v-btn></div> -->
    <div>5. 존안 결재: 존안관리부서장 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'yu'; getUserLogin();">유확산(yu)</v-btn></div>
    <div>6. 반출 결재: 파일반출담당자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'lee3'; getUserLogin();">이확산(lee3)</v-btn></div>
    <div>문서과 비밀관리자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'won'; getUserLogin();">원감사(won)</v-btn></div>
    <div>기관보안관리자 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'na2'; getUserLogin();">나보안(na2)</v-btn></div>
    <br>
    <div>[인계인수]</div>
    <div>1. 인계: 없음, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'song'; getUserLogin();">송채연(song)</v-btn></div>
    <div>2. 인수: 처리과 비밀관리자, 전산팀 <v-btn style="float: right;" @click="getUserLoginCondi.userid = 'kim3'; getUserLogin();">김검토(kim3)</v-btn></div>
  </div>
  
  <div class="loginMain">
    <h1>로그인</h1>
    <div>
      <v-text-field
        v-model="getUserLoginCondi.userid"
        label="아이디"
        style="width:150px;"
        hide-details="auto"
        @keyup.enter="getUserLogin()"
        autofocus
        ></v-text-field>
    </div>
    <br><br>
    <div style="display: flex; align-items: center; gap: 10px">
      <v-btn outlined @click="getUserLogin()">로그인</v-btn>
    </div>
  </div>
<!-- 임시태그 끝: 런칭시 삭제 -->
</template>

<script setup>

import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkLoginSession, parseJwt, goLoginPage } from "@/components/login/ShieldgateLogin.js";

import { storeToRefs } from 'pinia'; 
import { useLoginStore } from '/src/store/Login';


const name = ref('Login')
const route = useRoute()
const router = useRouter()
const query = ref({
  code: "",
});
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore

const loginLoading = ref(false);
/* IdGP Login */
// onMounted(async () => {
//   loginLoading.value = true;

//   query.value = route.query;
//   await checkLoginSession(query.value.code);
  
//   const jwt = sessionStorage.getItem("Authorization");
//   if(jwt !== null) {
//     const decodeJwt = parseJwt(jwt);
//     getUserLoginCondi.value.userid = decodeJwt.user_email;
//     getUserLoginCondi.value.jwt = sessionStorage.getItem("Authorization");

//     await getUserLogin();
//     loginLoading.value = false;

//     router.push({
//       name: "Home"
//     });
//   }
//   else goLoginPage();
// })

</script>


<!-- 임시스타일 시작: 런칭시 삭제 -->
<style lang="scss" scoped>
.loginMain {
    width: 250px;
    height: 300px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    border: 1px solid lightgrey;
    border-radius: 5px;
    /* 상하좌우 정중앙 정렬하기 */
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

.members {
  position: fixed;
  top: 50px;
  right: 50px;
  display: flex;
  gap: 10px;
  flex-direction: column;
}
</style>
<!-- 임시스타일 끝: 런칭시 삭제 -->