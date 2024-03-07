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
  <div class="page" :style="{width: contentWidth}">
    <div class="box-title">
      <h4 class="h4">예고문도래목록</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">재분류 - 예고문 변경요청서</h5>
          <div class="action-buttons">
            <v-btn class="button" :disabled="isChiefMe" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn class="button" @click="useApprovalPopupToggleFunc()">승인요청</v-btn>
            <v-btn class="button" @click="moveToBmsCominglist()">취소</v-btn>
          </div>
        </div>
      </section>
      
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청정보</h6>
        </div>
        <v-table class="table-type-03">
          <colgroup>
            <col width="240">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th><span class="required">제목</span></th>
              <td>
                <v-text-field
                  v-model="insertComingReclassApproveBIAData.reqttl"
                  :rules="[rules.required, rules.counter]"
                  maxlength="110"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th><span class="required">사유</span></th>
              <td>
                <v-text-field
                  v-model="insertComingReclassApproveBIAData.reqcontents"
                  :rules="[rules.required, rules.reqcounter]"
                  maxlength="500"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
      
      <section class="section">
        <div class="box-title">
          <h6 class="h6">결재선정보</h6>
          <div class="action-buttons">
            <v-btn class="button" :disabled="isChiefMe" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
          </div>
        </div>
        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 10%;">구분</th>
              <th style="width: 15%;">부서</th>
              <th style="width: 20%;">직위/성명</th>
              <th style="width: 20%;">의견</th>
              <th style="width: 10%;">서명</th>
              <th style="width: 25%;">처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pathData, idx in sortedPathList" :key="idx">
              <td>{{ pathData.apprtemp }}</td>
              <td>{{ pathData.apprdeptname }}</td>
              <td class="text-left">{{ pathData.apprnametemp }}</td>
              <td class="text-left">{{ pathData.appropinion }}</td>
              <td>{{ pathData.electsign }}</td>
              <td>{{ pathData.apprgubuntemp }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>
      
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>
        <v-data-table-server
          :headers="getDctMgmtRegiAttrHeaders"
          :items="getDctMgmtRegiAttrList"
          :items-length="getDctMgmtRegiAttrList.length"
          item-value="mgmtid"
          :expanded="expandedItems"
          class="table-type-02"
        >
        <template v-slot:expanded-row="{ item }">
          <tr>
            <td colspan="99">
              <div class="box-flex">
                <v-label>등급</v-label>
                <v-select
                  v-model="item.raw.getseclevel"
                  :items="secLevelData"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>보존기간</v-label>
                <v-select
                  v-model="item.raw.getprsrvterm"
                  :items="prsRvTermData"
                  @update:modelValue="updatePrsrvterm(item)"
                  item-title="key"
                  item-value="value"
                  required
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>보호기간</v-label>
                <v-select
                  v-model="item.raw.protdtSelected"
                  :items="protdtData"
                  variant="outlined"
                  hide-details="auto"></v-select>
                <v-text-field
                  v-if="item.raw.protdtSelected == '직접입력'"
                  v-model="item.raw.userDate"
                  @update:modelValue="updateUserDate(item)"
                  type="date"
                  variant="outlined"
                  hide-details="auto">
                </v-text-field>
                <template v-else>
                  <v-select
                    v-model="item.raw.protdtPeriodSelected"
                    :items="protdtPeriodData"
                    @update:modelValue="updateDate(item)"
                    item-title="key"
                    item-value="value"
                    variant="outlined"
                    hide-details="auto">
                  </v-select>
                  <v-label>년</v-label>
                  <v-text-field
                    v-model="item.raw.year"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                  <v-label>년</v-label>
                  <v-text-field
                    v-model="item.raw.month"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                  <v-label>월</v-label>
                  <v-text-field
                    v-model="item.raw.day"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                    <v-label>일</v-label>
                  </template>
                  <v-select
                    v-model="item.raw.reclassSelected"
                    :items="reclass"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto">
                  </v-select>
                  <v-label>(으)로</v-label>
                  <v-select
                    v-model="item.raw.getreclass"
                    :items="reclassData(item.raw.copyno)"
                    @update:modelValue="selectedReclass(item)"
                    item-title="key"
                    item-value="value"
                    variant="outlined"
                    hide-details="auto">
                  </v-select>
              <v-btn @click="clear(item)">초기화</v-btn>  
              </div>
            </td>
          </tr>
        </template>
        <template v-slot:item.secttl="{ item }">
          <div :class="{ 'text-left': item }">
            {{ item.raw.secttl }}
          </div>
        </template>
        <template v-slot:item.seclevel="{ item }">
          <span>{{transformSeclevel(item.raw.seclevel)}}</span>
        </template>
        <template v-slot:item.notiprsrvterm="{ item }">
          <span>{{transformNotiprsrvterm(item.raw.notiprsrvterm)}}</span>
        </template>
        <template v-slot:item.orgnprotdt="{ item }">
          <span>{{dayjs(item.raw.orgnprotdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')}}</span>
        </template>
        <template v-slot:item.regirecvtype="{ item }">
          <span v-if="item.raw.regirecvtype == '1'">전자</span>
          <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
        </template>
        <template v-slot:bottom></template>
        </v-data-table-server>
      </section> 
    </div>
  </div>

  <v-dialog v-model="bVisibleUsePathPopup" style="width: 850px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUsePathPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UsePathPopup
        v-if="bVisibleUsePathPopup"
        :args="usePathPopupArgs"
        :toggleFunc="usePathPopupToggleFunc"
        :returnFunc="usePathPopupReturnFunc"
      ></UsePathPopup>
    </v-card>
  </v-dialog>


    <v-dialog v-model="bVisibleUseApprovalPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>승인요청</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUseApprovalPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UseApprovalPopup
        v-if="bVisibleUseApprovalPopup"
        :args="useApprovalPopupArgs"
        :toggleFunc="useApprovalPopupToggleFunc"
        :insertFunc="insertComingReclassApproveBIA"
      ></UseApprovalPopup>
    </v-card>
  </v-dialog>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
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
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api'; 
import UsePathPopup from "./UsePathPopup.vue";
import UseApprovalPopup from "./UseApprovalPopup.vue";
import { transformStatus, transformSeclevel, transformState, transformAsktype, transformDate, transformNotiprsrvterm } from "@/utils/TransFormLabelDataUtil.js"
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { findChiefId, findChiefInfo } from "@/utils/OrganizationInfo.js"; 
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('BmsComingReclassDetail')
/* ======================== 결재선지정 팝업 ======================== */
const bVisibleUsePathPopup = ref(false)
const usePathPopupReuturn = ref({})
const usePathPopupArgs = ref({})
const usePathPopupToggleFunc = ref(() => { 
  const pathList = [...insertComingReclassApproveBIAData.value.pathList];
  const modifiedPathList = pathList.slice(1).map(item => {
    return {
      orgid: item.apprdeptid,
      userid: item.apprid,
      username: item.apprname,
      orgname: item.apprdeptname,
      grade: item.apprdutyname,
      askTemp: item.apprtemp,
      asktype: item.apprtype,
      identifier: `${item.apprdeptid}-${item.apprid}`
    };
  });
  usePathPopupArgs.value.path = modifiedPathList;
  usePathPopupArgs.value.managerids = [];
  usePathPopupArgs.value.initialDeptid = getUserLoginData.value.deptid;
  usePathPopupArgs.value.initialDeptname = getUserLoginData.value.deptname;
  bVisibleUsePathPopup.value = !bVisibleUsePathPopup.value;
})
const usePathPopupReturnFunc = ref((retValue) => {
  usePathPopupToggleFunc.value()
  if(retValue != ""){
    insertInitialPath();
    for(let i = 0; i < retValue.length; i++){
      const path = {};
      path.apprreqseq = i+2;
      path.apprtemp = retValue[i].askTemp;
      path.apprtype = retValue[i].asktype;
      path.apprgubun = "2";
      path.apprstatus = "APP10";
      path.apprid = retValue[i].userid;
      path.apprname = retValue[i].username;
      path.apprdeptid = retValue[i].orgid;
      path.apprdeptname = retValue[i].orgname;
      path.apprdutyname = retValue[i].grade;
      path.apprnametemp = retValue[i].grade+"/"+retValue[i].username;
      path.rgstid = getUserLoginData.value.userid;
      path.rgstname = getUserLoginData.value.username;
      insertComingReclassApproveBIAData.value.pathList.push(path);
    };
    sortedPathList.value;
  }else{
    insertInitialPath();
  }
});

const createPath = (apprreqseq, apprtemp, apprtype, apprgubun, apprstatus, item) => {
  return {
    apprreqseq,
    apprtemp,
    apprtype,
    apprgubun,
    apprstatus,
    apprid: item.userid,
    apprname: item.username,
    apprdeptid: item.deptid,
    apprdeptname: item.deptname,
    apprdutyname: item.grade,
    apprnametemp: `${item.grade}/${item.username}`,
    rgstid: getUserLoginData.value.userid,
    rgstname: getUserLoginData.value.username,
  }
};

const insertInitialPath = () => {
  insertComingReclassApproveBIAData.value.pathList = [];
  const initialPath = createPath("1", "요청", "1", "1", "APP00", getUserLoginData.value);
  insertComingReclassApproveBIAData.value.pathList.push(initialPath);
};

const insertChiefPath = (info) => {
  const approver = createPath("2", "승인", "3", "2", "APP10", info);
  insertComingReclassApproveBIAData.value.pathList.push(approver);
};

const insertChiefMePath = (info) => {
  insertComingReclassApproveBIAData.value.pathList = [];
  const iAmApprover = createPath("1", "1인결재", "4", "1", "APP30", info);
  insertComingReclassApproveBIAData.value.pathList.push(iAmApprover);
};

const sortedPathList = computed(() => {
  const requests = [];
  const reviewers = [];
  const approvers = [];

  // '요청', '검토', '승인'에 따라 재정렬
  insertComingReclassApproveBIAData.value.pathList.forEach((path) => {
    switch (path.apprtemp) {
      case '요청':
      case '1인결재':
        requests.push(path);
        break;
      case '검토':
        reviewers.push(path);
        break;
      case '승인':
        approvers.push(path);
        break;
      default:
        break;
    }
  });
  
  insertComingReclassApproveBIAData.value.pathList = requests.concat(reviewers, approvers);
  updateApprreqSeq();

  return insertComingReclassApproveBIAData.value.pathList
});

const updateApprreqSeq = () => {
  insertComingReclassApproveBIAData.value.pathList.forEach((path, index) => {
    path.apprreqseq = index + 1;
  });
};

/* ============================================================ */

/* ======================== 승인요청 팝업 ======================== */
const bVisibleUseApprovalPopup = ref(false)
const useApprovalPopupReuturn = ref({})
const useApprovalPopupArgs = ref({})
const useApprovalPopupToggleFunc = ref(() => { 

  if(!insertComingReclassApproveBIAData.value.reqttl){
    alert("제목을 작성해주세요")
    return
  }

  if(!insertComingReclassApproveBIAData.value.reqcontents){
    alert("사유를 작성해주세요")
    return
  }

  if(insertComingReclassApproveBIAData.value.pathList.length == 1 && isChiefMe.value == false){
    alert("결재선지정을 해주세요.")
    return
  }

  if(!insertComingReclassApproveBIAData.value.pathList.some(path=>path.apprid == chiefid.value) && isChiefMe.value == false){
    alert("부서장을 승인자에 추가하여 주십시오.")
    return;
  }

  let dataNull = 0;
  let reclassData = [];
  getDctMgmtRegiAttrList.value.forEach(item => {
    if (!item.getreclass || !item.getseclevel || !item.protDate || !item.getprsrvterm) {
      dataNull++;
    } else {
      reclassData.push({
      mgmtid: item.mgmtid,
      reclassgubun: item.getreclass,
      reqseclevel: item.getseclevel,
      reqnotiprotterm: item.protDate,
      reqnotiprotlevel: item.getseclevel,
      reqnotiact: item.getreclass,
      reqprsrvterm: item.getprsrvterm,
      });
    }
  });

  if (dataNull > 0) {
    alert("요청내용을 확인해주세요");
    return;
  }

  if(dataNull === 0){
    insertComingReclassApproveBIAData.value.reclassList = reclassData;
  }

  if(bVisibleUseApprovalPopup.value == false && isChiefMe.value == true){
    if(!confirm("1인결재를 진행합니다.")){
      return
    }
  }
  
  if(isChiefMe.value == true){
    insertComingReclassApproveBIAData.value.reqstatus = '6';
    insertComingReclassApproveBIAData.value.grantflag = 'Y';
    insertComingReclassApproveBIAData.value.reclassList.forEach(data => {
      data.grantseclevel = data.reqseclevel;
      data.grantnotiprotterm = data.reqnotiprotterm.replace(/-/g, '')+'235959';
      data.grantnotiprotlevel = data.reqnotiprotlevel;
      data.grantnotiact = data.reqnotiact;
      data.grantnotiprsrvterm = data.reqprsrvterm;
      data.grantflag = 'Y';
    });
  }
  useApprovalPopupArgs.value = insertComingReclassApproveBIAData.value;
  bVisibleUseApprovalPopup.value = !bVisibleUseApprovalPopup.value;
})
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const params = ref({});
const query = ref({});
const isloading = ref(false)

// for insertComingReclassApproveBIA
const insertComingReclassApproveBIAData = ref({

  // 1) bms_dct_apprreq table
  apprreqid: "", // 결재요청 ID
  reqtype: "06", // 요청타입 (01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출, 06: 재분류, 07: 파기, 08: 존안, 09: 이관, 10: 이관대기, 11: 인관연기, 12: 내부이관, 13: 비전자, 14: 오프라인열람, 15: 인쇄용프린터, 16: 추가발송)
  reqttl: "[재분류요청서] 재분류를 요청합니다.", // 요청제목
  reqcontents: "", // 요청사유
  reqid: "", // 요청자 ID
  reqname: "", // 요청자명
  reqdeptid: "", // 요청자 부서ID
  reqdeptname: "", // 요청자 부서명
  reqstatus: "1", // 진행상태 (0: 임시저장, 1: 결재중, 2: 결재완료, 3: 최종처리, 4: 권한회수, 5: 요청서회수, 6: 1인결재)
  grantflag: "N", // 요청승인여부


  // 2) bms_dct_apprreq_path table
  pathList: [
    {
      apprreqseq: "", // 요청경로순번
      apprtemp: "", // apprtype 해당하는 viewing 값 임시 저장
      apprtype: "", // 결재유형 (1: 요청, 2: 검토, 3: 결재, 4: 1인결재)
      apprgubuntemp: "", // apprgubun 해당하는 viewing 값 임시 저장
      apprgubun: "", // 결재처리구분 (1: 승인, 2: 비승인)
      apprstatus: "", // 결재요청상태 (APP00: - , APP10: 결재대기, APP20: 결재완료, APP30: 1인결재)
      apprid: "", // 결재자 ID
      apprname: "", // 결재자명
      apprdeptid: "", // 결재자 부서ID
      apprdeptname: "", // 결재자 부서명
      apprdutyname: "", // 결재자 직위직급명
      apprnametemp: "", // 직위/직급에 해당하는 viewing 값 임시 저장
      appropinion: "", // 결재자의견
      electsign: "", // 전자서명
      rgstid: "", // 등록자 ID
      rgstname: "", // 등록자명
    }
  ],
  appropiniontemp: "", // pathList의 appropinion에 넣을 값 임시 저장


  // 3) bms_dct_apprreq_reclass table
  reclassList: [
    {
      apprreqid: "", // 요청ID
      mgmtid: "", // 비밀관리 ID
      reclassgubun: "", // 재분류구분 (20: 존안, 21: 재분류, 22: 이관, 23: 파기)
      reqseclevel: "", // 요청비밀등급 (2: 2급, 3: 3급, 5: 일반)
      reqnotiprotterm: "", // 요청예고문보호기간
      reqnotiprotlevel: "", // 요청예고문비밀등급 (2: 2급, 3: 3급, 5: 일반)
      reqnotiact: "", // 요청예고문처리 (20: 존안, 21: 재분류, 22: 이관, 23: 파기)
      reqprsrvterm: "", // 요청예고문보존기간 (001: 1년, 003: 3년, 005: 5년, 030: 30년, 040: 준영구, 050: 영구)
    }
  ],
  docid: [],
  dutyname: "",

})

const rules = {
  required: value => !!value || '필수 입력값입니다.',
  counter: value => value.length < 110 || '최대 110자 까지 입력하실 수 있습니다.',
  reqcounter: value => value.length < 500 || '최대 500자 까지 입력하실 수 있습니다.',
};


// for getDctMgmtRegiAttr
const getDctMgmtRegiAttrList = ref([])
const getDctMgmtRegiAttrDefCondi = {
  mgmtid: []
}
const getDctMgmtRegiAttrCondi = ref({ ...getDctMgmtRegiAttrDefCondi })

const chiefid = ref("")
const chiefInfo = ref({})
const isChiefMe = ref(false)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  const mgmtidArray = query.value.mgmtid.replace(/'/g, '').split(','); 
  getDctMgmtRegiAttrCondi.value.mgmtid = mgmtidArray;
  insertComingReclassApproveBIAData.value.reclassList = mgmtidArray.map(mgmtid => ({
    apprreqid: "",
    mgmtid: mgmtid,
    reclassgubun: "",
    reqseclevel: "",
    reqnotiprotterm: "",
    reqnotiprotlevel: "",
    reqnotiact: "",
    reqprsrvterm: "",
  }));
})

onMounted(async () => {
  await getDctMgmtRegiAttr();
  insertComingReclassApproveBIAData.value.reqid = getUserLoginData.value.userid;
  insertComingReclassApproveBIAData.value.reqname = getUserLoginData.value.username;
  insertComingReclassApproveBIAData.value.reqdeptid = getUserLoginData.value.deptid;
  insertComingReclassApproveBIAData.value.reqdeptname = getUserLoginData.value.deptname;
  insertComingReclassApproveBIAData.value.dutyname = getUserLoginData.value.grade;
  insertComingReclassApproveBIAData.value.docid = getDctMgmtRegiAttrList.value.map(item => item.docid);
  insertInitialPath();
  chiefid.value = await findChiefId(getUserLoginData.value.deptid);
  if(chiefid.value != undefined){
    chiefInfo.value = await findChiefInfo(chiefid.value);
    if(chiefid.value == getUserLoginData.value.userid){
      isChiefMe.value = true;
      insertChiefMePath(chiefInfo.value);
    }else{
      insertChiefPath(chiefInfo.value);
    }
  }
})

const secLevelData = [
  { key: '선택', value: ''},
  { key: 'II급', value: '2'},
  { key: 'III급', value: '3'},
  { key: '일반', value: '5'},
];
const prsRvTermData = [
  {key: '선택', value: ''},
  {key: '1년', value: '001'},
  {key: '3년', value: '003'},
  {key: '5년', value: '005'},
  {key: '10년', value: '010'},
  {key: '30년', value: '030'},
  {key: '준영구', value: '040'},
  {key: '영구', value: '050'},
];
const protdtData = ["날짜지정", "직접입력"];
const protdtPeriodData = [
  {key: '선택', value: ''},
  {key: '1년', value:'0'},
  {key: '2년', value:'1'},
  {key: '3년', value:'2'},
  {key: '4년', value:'3'},
  {key: '5년', value:'4'},
  {key: '6년', value:'5'},
  {key: '7년', value:'6'},
  {key: '8년', value:'7'},
  {key: '9년', value:'8'},
  {key: '10년', value:'9'},
];

const updatePrsrvterm = (item) => { // 보존기간
  if(parseInt(item.raw.getprsrvterm) < parseInt(item.raw.protdtPeriodSelected)+1){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.getprsrvterm = "";
    return
  }
  if(parseInt(item.raw.getprsrvterm) < parseInt(item.raw.userDate.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.getprsrvterm = "";
    return
  }
};

const updateDate = (item) => { // 날짜지정
  if(parseInt(item.raw.getprsrvterm) < parseInt(item.raw.protdtPeriodSelected)+1){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.protdtPeriodSelected = "";
    return
  }
  const currentDate = new Date();
  const currentYear = currentDate.getFullYear();
  item.raw.year = currentYear + parseInt(item.raw.protdtPeriodSelected);
  item.raw.month = '12';
  item.raw.day = '31';
  item.raw.protDate = item.raw.year+item.raw.month+item.raw.day;
};

const updateUserDate = (item) => { // 직접입력
  if(parseInt(item.raw.getprsrvterm) < parseInt(item.raw.userDate.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.userDate = "";
    return
  }
  item.raw.protDate = item.raw.userDate.replace(/-/g, '');
};

const reclass = ['선택','일반문서'];
const orgnReclassData = [
  { key: '선택', value: ''},
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'},
];
const copyReclassData = [
  { key: '선택', value: ''},
  { key: '재분류', value: '21'},
  { key: '존안', value: '20'},
  { key: '파기', value: '23'},
];

const reclassData = computed(() => (copyno) => {
  if (copyno.includes('원')) {
    return orgnReclassData;
  } else {
    return copyReclassData;
  }
});

const selectedReclass = (item) => {
  if(item.raw.getreclass == '21'){
    item.raw.reclassSelected = "일반문서"
  }else{
    item.raw.reclassSelected = "선택"
  }
}

const clear = (item) => {
  if(!confirm("초기화하시겠습니까?")){
    return;
  }
  item.raw.getseclevel = '',
  item.raw.getprsrvterm = '',
  item.raw.protdtSelected = '날짜지정',
  item.raw.userDate = '',
  item.raw.protdtPeriodSelected = '',
  item.raw.year = '',
  item.raw.month = '',
  item.raw.day = '',
  item.raw.reclassSelected = '선택',
  item.raw.getreclass = ''
}

const getDctMgmtRegiAttrHeaders = [
  { key: "mgmtno", title: "관리번호", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "비밀등급", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "notiprsrvterm", title: "보존기간", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "protdt", title: "보호기간", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

const expandedItems = ref([]);

// eslint-disable-next-line
const getDctMgmtRegiAttr = async () => {
  getDctMgmtRegiAttrCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.getDctMgmtRegiAttr(getDctMgmtRegiAttrCondi.value, urlPaths.value);
      response.data = response.data.map((u) => {
        u.orgnprotdt = dayjs(u.orgnprotdt, 'YYYYMMDD').format('YYYY.MM.DD');
        u.copyprotdt = u.copyprotdt != null ? dayjs(u.copyprotdt, 'YYYYMMDD').format('YYYY.MM.DD') : '';
        u.protdt = u.copyno.includes("원") ? u.orgnprotdt + transReclassInfo(u.orgnreclass) : u.copyprotdt + transReclassInfo(u.copyreclass);
        return { ...u};
      });
    
    getDctMgmtRegiAttrList.value = response.data.map(item => ({
        ...item, getseclevel: '', getprsrvterm: '', protdtSelected: '날짜지정', protdtPeriodSelected: '', year: '', month: '', day: '', reclassSelected: '선택', getreclass:'', userDate: '', protDate: ''
      }));
    expandedItems.value = getDctMgmtRegiAttrList.value.map(item => item.mgmtid);

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const transReclassInfo = (value) => {
  switch (value) {
    case '20':
      return ' 존안'
    case '21':
      return ' 재분류'
    case '22':
      return ' 이관'
    case '23':
      return ' 파기'
    default:
      return ''
  }
};

const insertComingReclassApproveBIA = async (param) => {
  isloading.value = true;

  param.pathList[0].appropinion = param.appropiniontemp;

  API.useAPI
    .insertComingReclassApproveBIA(param, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("정상 등록되었습니다");
        moveToBmsUsedraftlist();
      }
    })
    .catch(error => {
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });
}

const moveToBmsCominglist = () => {
  router.push({
    name: "BmsCominglist"
    });
};

const moveToBmsUsedraftlist = () => {
  let arr = ['비밀관리', '비밀문서사용처리', '기안한 요청서'];

  breadcrumbs.value.activeLink = arr;
  
  router.push({
    name: "BmsUsedraftlist",
  });
};
</script>
