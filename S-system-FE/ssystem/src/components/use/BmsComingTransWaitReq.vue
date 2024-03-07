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
          <h5 class="h5">이관대기 요청서</h5>
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
                  v-model="insertComingTransWaitData.reqttl"
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
                  v-model="insertComingTransWaitData.reqcontents"
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
          class="table-type-02"
        >
        <template v-slot:item.isopen="{ item }">
          <v-select
            v-model="item.raw.open"
            :items="openData"
            @update:modelValue="updateIsOpen(item)"
            item-title="view"
            item-value="key"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </template>
        <template v-slot:item.openrange="{ item }">
          <v-select 
            v-if="item.raw.open == 'CLOSE'"
            placeholder="비공개"
            :readonly="true"
            variant="outlined"
            hide-details="auto"
          ></v-select>
          <v-select v-else
            v-model="item.raw.agency"
            :items="agencyData"
            item-title="view"
            item-value="key"
            variant="outlined"
            hide-details="auto">
          </v-select>
        </template>
        <template v-slot:item.openrestrict="{ item }">
          <v-select 
            v-if="item.raw.open == 'CLOSE'"
            placeholder="비공개"
            :readonly="true"
            variant="outlined"
            hide-details="auto"
          ></v-select>
          <v-select v-else
            v-model="item.raw.restrict"
            :items="restrictData"
            item-title="view"
            item-value="key"
            variant="outlined"
            hide-details="auto">
          </v-select>
        </template>
        <template v-slot:item.secttl="{ item }">
          <div :class="{ 'text-left': item }">
            {{ item.raw.secttl }}
          </div>
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
        :insertFunc="insertTransWait"
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
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { findChiefId, findChiefInfo } from "@/utils/OrganizationInfo.js"; 
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore) 
import { transformStatus,transformState, transformAsktype, transformDate, transformNotiprsrvterm } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('BmsComingTransWaitReq')
/* ======================== 결재선지정 팝업 ======================== */
const bVisibleUsePathPopup = ref(false)
const usePathPopupReuturn = ref({})
const usePathPopupArgs = ref({})
const usePathPopupToggleFunc = ref(() => {
  const pathList = [...insertComingTransWaitData.value.pathList];
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
    let startIdx = 1;
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
      insertComingTransWaitData.value.pathList.splice(startIdx,0,path);
      startIdx++;
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
  insertComingTransWaitData.value.pathList = [];
  const initialPath = createPath("1", "요청", "1", "1", "APP00", getUserLoginData.value);
  insertComingTransWaitData.value.pathList.push(initialPath);
};

const insertChiefPath = (info) => {
  const approver = createPath("2", "승인", "3", "2", "APP10", info);
  insertComingTransWaitData.value.pathList.push(approver);
};

const insertChiefMePath = (info) => {
  insertComingTransWaitData.value.pathList = [];
  const iAmApprover = createPath("1", "1인결재", "4", "1", "APP30", info);
  insertComingTransWaitData.value.pathList.push(iAmApprover);
};

const sortedPathList = computed(() => {
  const requests = [];
  const reviewers = [];
  const approvers = [];

  // '요청', '검토', '승인'에 따라 재정렬
  insertComingTransWaitData.value.pathList.forEach((path) => {
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

  insertComingTransWaitData.value.pathList = requests.concat(reviewers, approvers);
  updateApprreqSeq();

  return insertComingTransWaitData.value.pathList
});

const updateApprreqSeq = () => {
  insertComingTransWaitData.value.pathList.forEach((path, index) => {
    path.apprreqseq = index + 1;
  });
};
/* ============================================================ */


/* ======================== 승인요청 팝업 ======================== */
const bVisibleUseApprovalPopup = ref(false)
const useApprovalPopupReuturn = ref({})
const useApprovalPopupArgs = ref({})
const useApprovalPopupToggleFunc = ref(() => { 

  if(!insertComingTransWaitData.value.reqttl){
    alert("제목을 작성해주세요")
    return
  }

  if(!insertComingTransWaitData.value.reqcontents){
    alert("사유를 작성해주세요")
    return
  }

  if(getDctMgmtRegiAttrList.value.some(item=> item.open != 'CLOSE' && (item.agency == "" || item.restrict == ""))){
    alert("공개 설정을 완료해주세요")
    return
  }

  if(insertComingTransWaitData.value.pathList.length == 1 && isChiefMe.value == false){
    alert("결재선지정을 해주세요.")
    return
  }

  if(!insertComingTransWaitData.value.pathList.some(path=>path.apprid == chiefid.value) && isChiefMe.value == false){
    alert("부서장을 승인자에 추가하여 주십시오.")
    return;
  }

  if(bVisibleUseApprovalPopup.value == false && isChiefMe.value == true){
    if(!confirm("1인결재를 진행합니다.")){
      return
    }
  }

  insertComingTransWaitData.value.rdocList = [];
  getDctMgmtRegiAttrList.value.forEach(item=>{
    const rdocData = {};
    rdocData.r_docid = item.docid;
    rdocData.r_open = item.open;
    rdocData.r_readrangetype = item.agency;
    rdocData.r_readlimittype = item.restrict;
    rdocData.r_transflag = 'N';
    insertComingTransWaitData.value.rdocList.push(rdocData);
  })

  if(isChiefMe.value == true){
    insertComingTransWaitData.value.reqstatus = '6';
    insertComingTransWaitData.value.grantflag = 'Y';
  }

  useApprovalPopupArgs.value = insertComingTransWaitData.value;
  bVisibleUseApprovalPopup.value = !bVisibleUseApprovalPopup.value;
})
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const params = ref({});
const query = ref({});
const isloading = ref(false)

const insertComingTransWaitData = ref({

  // 1) bms_dct_apprreq table
  apprreqid: "", // 결재요청 ID
  reqtype: "10", // 요청타입 (01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출, 06: 재분류, 07: 파기, 08: 존안, 09: 이관, 10: 이관대기, 11: 이관연기, 12: 내부이관, 13: 비전자, 14: 오프라인열람, 15: 인쇄용프린터, 16: 추가발송)
  reqttl: "[이관대기요청서] 이관대기를 요청합니다.", // 요청제목
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


  // 3) bms_dct_apprreq_trans table
  transList: [
    {
      apprreqid: "", // 요청ID
      mgmtid: "", // 비밀관리 ID
    }
  ],

  docid: [],
  dutyname: "",

  // 4) bms_dct_rdoc table
  rdocList: [
    {
      r_docid: "",
      r_open: "",
      r_readrangetype: "",
      r_readlimittype: "",
      r_transflag: "",
    }
  ]

})

const rules = {
  required: value => !!value || '제목을 입력해주세요.',
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
  insertComingTransWaitData.value.transList = mgmtidArray.map(mgmtid => ({
    apprreqid: "",
    mgmtid: mgmtid,
  }));
})

onMounted(async () => {
  await getDctMgmtRegiAttr();
  insertComingTransWaitData.value.reqid = getUserLoginData.value.userid;
  insertComingTransWaitData.value.reqname = getUserLoginData.value.username;
  insertComingTransWaitData.value.reqdeptid = getUserLoginData.value.deptid;
  insertComingTransWaitData.value.reqdeptname = getUserLoginData.value.deptname;
  insertComingTransWaitData.value.dutyname = getUserLoginData.value.grade;
  insertComingTransWaitData.value.docid = getDctMgmtRegiAttrList.value.map(item => item.docid);
  chiefid.value = await findChiefId(getUserLoginData.value.deptid);
  if(chiefid.value != undefined){
    chiefInfo.value = await findChiefInfo(chiefid.value);
    if(chiefid.value == getUserLoginData.value.userid){
      isChiefMe.value = true;
      insertChiefMePath(chiefInfo.value);
    }else{
      insertInitialPath();
      insertChiefPath(chiefInfo.value);
    }
  }
})


// 공개여부
const openData = [
  {view: "비공개", key: "CLOSE"},
  {view: "공개", key: "OPEN"},
  {view: "부분공개", key: "PRAT"},
];

// 열람범위
const agencyData = [
  {view: "선택", key: ""},
  {view: "기관", key: "DDEP1"},
  {view: "실국", key: "DDEP2"},
  {view: "부서", key: "DDEP3"},
];

// 공개제한
const restrictData = [
  {view: "선택", key:""},
  {view: "1호", key:"1"},
  {view: "2호", key:"2"},
  {view: "3호", key:"3"},
  {view: "4호", key:"4"},
  {view: "5호", key:"5"},
  {view: "6호", key:"6"},
  {view: "7호", key:"7"},
  {view: "8호", key:"8"},
];

const updateIsOpen = (item) => {
  if(item.raw.open == 'CLOSE'){
    item.raw.agency = '';
    item.raw.restrict = '';
  }
};

const getDctMgmtRegiAttrHeaders = [
  { key: "mgmtno", title: "관리번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "noti", title: "보존&보호기간", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "isopen", title: "공개여부", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "openrange", title: "열람범위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "openrestrict", title: "공개제한", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


// eslint-disable-next-line
const getDctMgmtRegiAttr = async () => {
  getDctMgmtRegiAttrCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.getDctMgmtRegiAttr(getDctMgmtRegiAttrCondi.value, urlPaths.value);
    response.data = response.data.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      u = { ...u, noti: transformNotiprsrvterm(u.notiprsrvterm) +" & "+ dayjs(u.notiprotterm, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      u = { ...u, open: 'CLOSE', agency: '', restrict: ''};
      return { ...u};
    });
    getDctMgmtRegiAttrList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const insertTransWait = async (param) => {
  isloading.value = true;

  param.pathList[0].appropinion = param.appropiniontemp;

  API.useAPI
    .insertTransWait(param, urlPaths.value)
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