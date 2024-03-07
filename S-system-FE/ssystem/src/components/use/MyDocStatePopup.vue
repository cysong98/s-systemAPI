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
  <div class="modal-body">
    <div class="content">
      <div class="row">
        <div class="col">
          <v-table class="table-type-03 narrow">
            <tbody>
              <tr>
                <th colspan="2" class="left-th" >분야</th>
                <th>조사항목</th>
                <th>현황</th>
              </tr>
              <tr>
                <th colspan="2" rowspan="4" class="left-th">비밀문서</th>
                <td>보유한 비밀</td>
                <td class="state">
                  <span :class="{ 'cnt': selectMgmtRegiListCnt>0}" @click="selectMgmtRegiListCnt > 0 ? moveListPage('BmsDctmgmtregilist', 'B', '비밀관리기록부') : null">{{ selectMgmtRegiListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>진행중인 비밀</td>
                <td class="state">
                  <span :class="{ 'cnt': selectIngListCnt>0}" @click="selectIngListCnt > 0 ? moveListPage('BmsDctinglist', 'B', '결재, 진행비밀함') : null">{{ selectIngListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>처리할 비밀</td>
                <td class="state">
                  <span :class="{ 'cnt': selectWaitListCnt>0}" @click="selectWaitListCnt > 0 ? moveListPage('BmsDctwaitlist', 'B', '결재, 결재대기함') : null">{{ selectWaitListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>발송대기함</td>
                <td class="state">
                  <span :class="{ 'cnt': selectSendWaitListCnt>0}" @click="selectSendWaitListCnt > 0 ? moveListPage('BmsDctsendwaitlist', 'B', '발송, 발송대기함') : null">{{ selectSendWaitListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <th rowspan="6" class="left-th">비밀<br>관리</th>
                <th rowspan="3" class="left-th">요청서</th>
                <td>진행중인 요청서</td>
                <td class="state">
                  <span :class="{ 'cnt': selectUseReqDraftCnt>0}" @click="selectUseReqDraftCnt > 0 ? moveListPage('BmsUsedraftlist', 'C', '비밀문서사용처리, 기안한 요청서') : null">{{ selectUseReqDraftCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>처리할 요청서</td>
                <td class="state">
                  <span :class="{ 'cnt': selectUseReqReqtCnt>0}" @click="selectUseReqReqtCnt > 0 ? moveListPage('BmsUsereqlist', 'C', '비밀문서사용처리, 처리할 요청서') : null">{{ selectUseReqReqtCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>요청서 완료처리</td>
                <td class="state">
                  <span :class="{ 'cnt': selectUseReqCompleteCnt>0}" @click="selectUseReqCompleteCnt > 0 ? moveListPage('BmsUsecompletelist', 'C', '비밀문서사용처리, 요청서 완료처리') : null">{{ selectUseReqCompleteCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <th rowspan="3" class="left-th">인계인수서</th>
                <td>진행중인 인계인수서</td>
                <td class="state">
                  <span :class="{ 'cnt': selectTrnReqListCnt>0}" @click="selectTrnReqListCnt > 0 ? moveListPage('BmsTrnreqlist', 'C', '인계인수, 요청한 인계인수서') : null">{{ selectTrnReqListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>처리할 인계인수서</td>
                <td class="state">
                  <span :class="{ 'cnt': selectTransProcToDoListCnt>0}" @click="selectTransProcToDoListCnt > 0 ? moveListPage('BmsComtransproctodolist', 'C', '인계인수, 처리할 인계인수서') : null">{{ selectTransProcToDoListCnt }}</span> 건
                </td>
              </tr>
              <tr>
                <td>인수현황함</td>
                <td class="state">
                  <span :class="{ 'cnt': bmsTrnCurrentListCnt>0}" @click="bmsTrnCurrentListCnt > 0 ? moveListPage('BmsTrncurrentlist', 'C', '인계인수, 인수현황함') : null">{{ bmsTrnCurrentListCnt }}</span> 건
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>
    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>

    <div v-if="isloading" class="overlay">
      <div class="spinner"></div>
    </div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, topMenu, breadcrumbs } = storeToRefs(mainStore)
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
import _ from 'lodash';

const name = ref('MyDocStatePopup')
const route = useRoute()
const router = useRouter()
const props = defineProps({
  toggleFunc: Function,
})
const urlPaths =  ref('')
const isloading = ref(false)

onMounted(async () => {
  try {
    isloading.value = true;
    await selectMgmtRegiList(selectMgmtRegiListCondi.value.pageNum);
    await selectIngList(selectIngListCondi.value.pageNum);
    await selectWaitList(selectWaitListCondi.value.pageNum);
    await selectSendWaitList(selectSendWaitListCondi.value.pageNum);
    await selectUseReqList(selectUseReqListCondi.value.pageNum, 'draft');
    await selectUseReqList(selectUseReqListCondi.value.pageNum, 'req');
    await selectUseReqList(selectUseReqListCondi.value.pageNum, 'complete');
    await selectTrnReqList(selectTrnReqListCondi.value.pageNum);
    await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum);
    await bmsTrnCurrentList(bmsTrnCurrentListCondi.value.pageNum);
  } finally {
    isloading.value = false;
  }
})

const moveListPage = (pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  router.push({
    name: pathname,
  });
  props.toggleFunc();
};

const closePopUp = () => {
  props.toggleFunc();
}

/* ======================= 1. 보유한 비밀 (비밀관리기록부) ======================= */
const selectMgmtRegiListCondi = ref({
  userreqtype: "개인",
  authorid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
});
const selectMgmtRegiListCnt = ref(0);
const selectMgmtRegiList = async (pageNum) => {
  selectMgmtRegiListCondi.value.pageNum = parseInt(pageNum);
  selectMgmtRegiListCondi.value.authorid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectMgmtRegiList({ ...selectMgmtRegiListCondi.value }, urlPaths.value);
    selectMgmtRegiListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 3. 진행중인 비밀 (결재-진행비밀함) ======================= */
const selectIngListCondi = ref({
  userid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "docid",
});
const selectIngListCnt = ref(0);
const selectIngList = async (pageNum) => {
  selectIngListCondi.value.pageNum = parseInt(pageNum);
  selectIngListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectIngList({ ...selectIngListCondi.value }, urlPaths.value);
    selectIngListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 4. 처리할 비밀 (결재-결재대기함) ======================= */
const selectWaitListCondi = ref({
  userid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "docid",
});
const selectWaitListCnt = ref(0);
const selectWaitList = async (pageNum) => {
  selectWaitListCondi.value.pageNum = parseInt(pageNum);
  selectWaitListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectWaitList({ ...selectWaitListCondi.value }, urlPaths.value);
    selectWaitListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 5. 발송대기함 (발송-발송대기함) ======================= */
const selectSendWaitListCondi = ref({
  userid: "",
  objid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "docid",
});
const selectSendWaitListCnt = ref(0);
const selectSendWaitList = async (pageNum) => {
  selectSendWaitListCondi.value.pageNum = parseInt(pageNum);
  selectSendWaitListCondi.value.userid = getUserLoginData.value.userid;
  selectSendWaitListCondi.value.objid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectSendWaitList({ ...selectSendWaitListCondi.value }, urlPaths.value);
    selectSendWaitListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 6. 진행중인 요청서 (사용처리-기안한요청서) ======================= */
/* ======================= 7. 처리할 요청서 (사용처리-처리할요청서) ======================= */
/* ======================= 8. 요청서 완료처리 (사용처리-요청서완료처리) ======================= */
const selectUseReqListCondi = ref({
  pagereqtype: "",
  reqid: "",
  apprid: "",
  reqstatus: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "DESC",
  sortItem: "reqdt",
});
const selectUseReqDraftCnt = ref(0);
const selectUseReqReqtCnt = ref(0);
const selectUseReqCompleteCnt = ref(0);
const selectUseReqList = async (pageNum, type) => {
  selectUseReqListCondi.value.pageNum = parseInt(pageNum);

  if(type == "draft"){ // (6)
    selectUseReqListCondi.value.pagereqtype = "draft";
    selectUseReqListCondi.value.reqid = getUserLoginData.value.userid;
    selectUseReqListCondi.value.apprid = "";
    selectUseReqListCondi.value.reqstatus = '1'; // 결재중
  }else if(type == "req"){ // (7)
    selectUseReqListCondi.value.pagereqtype = "req";
    selectUseReqListCondi.value.reqid = "";
    selectUseReqListCondi.value.apprid = getUserLoginData.value.userid;
  }else if(type == "complete"){ // (8)
    selectUseReqListCondi.value.pagereqtype = "complete";
    selectUseReqListCondi.value.reqid = getUserLoginData.value.userid;
    selectUseReqListCondi.value.apprid = "";
  }

  try {
    const response = await API.useAPI.selectUseReqList({ ...selectUseReqListCondi.value }, urlPaths.value);
    if(type == "draft"){ // (6)
      selectUseReqDraftCnt.value = response.data.total;
    }else if(type == "req"){ // (7)
      selectUseReqReqtCnt.value = response.data.total;
    }else if(type == "complete"){ // (8)
      selectUseReqCompleteCnt.value = response.data.total;
    }
    
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 9. 진행중인 인계인수서 (인계인수-요청한인계인수서) ======================= */
const selectTrnReqListCondi = ref({
  requserid: "",
  apprstatus: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "transferid"
});
const selectTrnReqListCnt = ref(0);
const selectTrnReqList = async (pageNum) => {
  selectTrnReqListCondi.value.pageNum = parseInt(pageNum);
  selectTrnReqListCondi.value.requserid = getUserLoginData.value.userid;
  selectTrnReqListCondi.value.apprstatus  = 'APP01'; // 상신
  try {
    const response = await API.trnAPI.selectTrnReqList({ ...selectTrnReqListCondi.value }, urlPaths.value);
    selectTrnReqListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 10. 처리할 인계인수서 (인계인수-처리할인계인수서) ======================= */
const selectTransProcToDoListCondi = ref({
  appruserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "transferid"
});
const selectTransProcToDoListCnt = ref(0);
const selectTransProcToDoList = async (pageNum) => {
  selectTransProcToDoListCondi.value.pageNum = parseInt(pageNum);
  selectTransProcToDoListCondi.value.appruserid = getUserLoginData.value.userid;
  try {
    const response = await API.trnAPI.selectTransProcToDoList({ ...selectTransProcToDoListCondi.value }, urlPaths.value);
    selectTransProcToDoListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

/* ======================= 11. 인수현황함 (인계인수-인수현황함) ======================= */
const bmsTrnCurrentListCondi = ref({
  appruserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 1000,
  sortDirection: "ASC",
  sortItem: "transferid"
});
const bmsTrnCurrentListCnt = ref(0);
const bmsTrnCurrentList = async (pageNum) => {
  bmsTrnCurrentListCondi.value.pageNum = parseInt(pageNum);
  bmsTrnCurrentListCondi.value.appruserid = getUserLoginData.value.userid;
  try {
    const response = await API.trnAPI.bmsTrnCurrentList({ ...bmsTrnCurrentListCondi.value }, urlPaths.value);
    bmsTrnCurrentListCnt.value = response.data.total;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
/* ====================================================================== */

</script>

<style scoped>
.state {
  text-align: center !important;
}
.cnt:hover {
  cursor: pointer;
  color: rgb(45, 7, 184);
  text-decoration: underline;
}
.left-th {
  width: 70px !important;
}
</style>