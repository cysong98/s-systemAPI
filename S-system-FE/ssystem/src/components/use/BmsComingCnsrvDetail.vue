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
          <h5 class="h5">존안 요청서</h5>
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
                  v-model="insertComingCnsrvApproveBIAData.reqttl"
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
                  v-model="insertComingCnsrvApproveBIAData.reqcontents"
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
          :headers="getDctMgmtRegiHeaders"
          :items="getDctMgmtRegiList"
          :items-length="getDctMgmtRegiList.length"
          item-value="mgmtid"
          class="table-type-02"
        >
        <template v-slot:item.reqmaxcnsrvdate="{item}">
          <v-text-field class="mx-auto" type="date" style="margin-left: 30px;" :min="today" :max="item.raw.maxcnsrvdate" v-model="item.raw.reqmaxcnsrvdate" @update:modelValue="updateMaxCnsrvDate(item)" variant="outlined" hide-details="auto"/>
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
        :insertFunc="insertComingCnsrvApproveBIA"
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
import { findChiefId, findChiefInfo, isChiefMe, insertUsePathList2, getDefaultPathUsers2, checkPathList2 } from "@/utils/OrganizationInfo.js"; 
import _ from 'lodash';
import { API } from '@/api'; 
import UsePathPopup from "./UsePathPopup.vue";
import UseApprovalPopup from "./UseApprovalPopup.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('BmsComingCnsrvDetail')
/* ======================== 결재선지정 팝업 ======================== */
const bVisibleUsePathPopup = ref(false)
const usePathPopupReuturn = ref({})
const usePathPopupArgs = ref({})
const usePathPopupToggleFunc = ref(() => { 
  const pathList = [...insertComingCnsrvApproveBIAData.value.pathList];
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
  usePathPopupArgs.value.cnsrv = 'cnsrv';
  usePathPopupArgs.value.initialDeptid = getUserLoginData.value.deptid;
  usePathPopupArgs.value.initialDeptname = getUserLoginData.value.deptname;
  bVisibleUsePathPopup.value = !bVisibleUsePathPopup.value;
})
const usePathPopupReturnFunc = ref((retValue) => {
  usePathPopupToggleFunc.value()
  if(retValue != ""){
    const filteredList = insertComingCnsrvApproveBIAData.value.pathList.slice(1);
    const existingIdentifiers = new Set(filteredList.map(path => path.identifier));
    const deletedPath = Array.from(existingIdentifiers).filter(identifier => !retValue.some(item => item.identifier === identifier));
    if(deletedPath.length > 0){
      deletedPath.forEach(identifier=>{
        const indexToRemove = insertComingCnsrvApproveBIAData.value.pathList.findIndex(path => path.identifier === identifier);
        if (indexToRemove !== -1) {
          insertComingCnsrvApproveBIAData.value.pathList.splice(indexToRemove, 1);
        }
      })
    }
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
      path.identifier = retValue[i].identifier;

      if (!existingIdentifiers.has(path.identifier)) {
        insertComingCnsrvApproveBIAData.value.pathList.push(path);
      }
    }

    const retValueIdentifiers = retValue.map(item => item.identifier);
    insertComingCnsrvApproveBIAData.value.pathList.sort((a, b) => {
      const indexA = retValueIdentifiers.indexOf(a.identifier);
      const indexB = retValueIdentifiers.indexOf(b.identifier);
      return indexA - indexB;
    });
    sortedPathList.value;
  }
});

const sortedPathList = computed(() => {
  const requests = [];
  const reviewers = [];
  const approvers = [];

  // '요청', '검토', '승인'에 따라 재정렬
  insertComingCnsrvApproveBIAData.value.pathList.forEach((path) => {
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

  insertComingCnsrvApproveBIAData.value.pathList = requests.concat(reviewers, approvers);
  updateApprreqSeq();

  return insertComingCnsrvApproveBIAData.value.pathList
});

const updateApprreqSeq = () => {
  insertComingCnsrvApproveBIAData.value.pathList.forEach((path, index) => {
      path.apprreqseq = index + 1;
  });
};

/* ============================================================ */

/* ======================== 승인요청 팝업 ======================== */
const bVisibleUseApprovalPopup = ref(false)
const useApprovalPopupReuturn = ref({})
const useApprovalPopupArgs = ref({})
const useApprovalPopupToggleFunc = ref(() => { 

  if(!insertComingCnsrvApproveBIAData.value.reqttl){
    alert("제목을 작성해주세요")
    return
  }

  if(!insertComingCnsrvApproveBIAData.value.reqcontents){
    alert("사유를 작성해주세요")
    return
  }

  if(insertComingCnsrvApproveBIAData.value.pathList.length == 1 && isChiefMe.value == false){
    alert("결재선지정을 해주세요.")
    return
  }

  if(!checkPathList2(insertComingCnsrvApproveBIAData.value) && isChiefMe.value == false){
    alert("존안관리담당자 및 존안관리부서장이 최소1명씩 승인자에 존재해야합니다.")
    return;
  }

  let dateNull = 0;
  insertComingCnsrvApproveBIAData.value.cnsrvList.forEach(item => {
    if (!item.reqcnsrvdt) {
      dateNull++;
      return;
    }
  });
  if(dateNull>0){
    alert("요청존안기간을 선택해주세요")
    return
  }

  if(bVisibleUseApprovalPopup.value == false && isChiefMe.value == true){
    if(!confirm("1인결재를 진행합니다.")){
      return
    }
  }

  if(isChiefMe.value == true){
    insertComingCnsrvApproveBIAData.value.reqstatus = '6';
    insertComingCnsrvApproveBIAData.value.grantflag = 'Y';
    insertComingCnsrvApproveBIAData.value.cnsrvList.forEach(data => {
      data.grantcnsrvdt = data.reqcnsrvdt.replace(/[-.]/g, '')+'235959';
      data.grantflag = 'Y';
    });
  }

  useApprovalPopupArgs.value = insertComingCnsrvApproveBIAData.value;
  bVisibleUseApprovalPopup.value = !bVisibleUseApprovalPopup.value;
})
/* ============================================================ */
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const params = ref({});
const query = ref({});
const today = ref("")

// for insertComingCnsrvApproveBIA
const insertComingCnsrvApproveBIAData = ref({

  // 1) bms_dct_apprreq table
  apprreqid: "", // 결재요청 ID
  reqtype: "08", // 요청타입 (01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출, 06: 재분류, 07: 파기, 08: 존안, 09: 이관, 10: 이관대기, 11: 인관연기, 12: 내부이관, 13: 비전자, 14: 오프라인열람, 15: 인쇄용프린터, 16: 추가발송)
  reqttl: "[존안요청서] 존안을 요청합니다.", // 요청제목
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


  // 3) bms_dct_apprreq_cnsrv table
  cnsrvList: [
    {
      apprreqid: "", // 요청ID
      mgmtid: "", // 비밀관리 ID
      reqcnsrvdt: "", // 요청존안기간
    }
  ],

  docid: [],
  dutyname: "",

  requester: [], // 요청자 정보
  managers: [], // 존안관리담당자 정보
  headers: [], // 존안관리 부서장 정보 
})

const rules = {
  required: value => !!value || '필수 입력값입니다.',
  counter: value => value.length < 110 || '최대 110자 까지 입력하실 수 있습니다.',
  reqcounter: value => value.length < 500 || '최대 500자 까지 입력하실 수 있습니다.',
};

// for getDctMgmtRegi
const getDctMgmtRegiList = ref([])
const getDctMgmtRegiDefCondi = {
  mgmtid: []
}
const getDctMgmtRegiCondi = ref({ ...getDctMgmtRegiDefCondi })
const isloading = ref(false)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  const mgmtidArray = query.value.mgmtid.replace(/'/g, '').split(','); 
  getDctMgmtRegiCondi.value.mgmtid = mgmtidArray;
  insertComingCnsrvApproveBIAData.value.cnsrvList = mgmtidArray.map(mgmtid => ({
    apprreqid: "",
    mgmtid: mgmtid,
    reqcnsrvdt: "",
  }));
})

onMounted(async () => {
  await getDctMgmtRegi();
  await selectFindManager();
  insertComingCnsrvApproveBIAData.value.reqid = getUserLoginData.value.userid;
  insertComingCnsrvApproveBIAData.value.reqname = getUserLoginData.value.username;
  insertComingCnsrvApproveBIAData.value.reqdeptid = getUserLoginData.value.deptid;
  insertComingCnsrvApproveBIAData.value.reqdeptname = getUserLoginData.value.deptname;
  insertComingCnsrvApproveBIAData.value.vgroupid = getUserLoginData.value.vgroupid;
  today.value = dayjs().format('YYYY-MM-DD');
  await insertUsePathList2(insertComingCnsrvApproveBIAData.value, selectFindManagerList.value, getUserLoginData.value);
  await getDefaultPathUsers2(insertComingCnsrvApproveBIAData.value);
  getDctMgmtRegiList.value.forEach((mgmtRegiItem) => {
    const mgmtid = mgmtRegiItem.mgmtid;
    const maxcnsrvdate = mgmtRegiItem.maxcnsrvdate;
    insertComingCnsrvApproveBIAData.value.cnsrvList.forEach((cnsrvItem) => {
      if (cnsrvItem.mgmtid === mgmtid) {
        cnsrvItem.reqcnsrvdt = maxcnsrvdate;
      }
    });
  });
  insertComingCnsrvApproveBIAData.value.dutyname = getUserLoginData.value.grade;
  insertComingCnsrvApproveBIAData.value.docid = getDctMgmtRegiList.value.map(item => item.docid);
})

const updateMaxCnsrvDate = (item) => {
  const targetIndex = insertComingCnsrvApproveBIAData.value.cnsrvList.findIndex(i => i.mgmtid === item.key);
  const max = dayjs(item.raw.maxcnsrvdate, 'YYYY.MM.DD').format('YYYY-MM-DD');
  const req = item.raw.reqmaxcnsrvdate
  if(max < req){
    alert(`요청존안기간은 최대존안가능기간(${item.raw.maxcnsrvdate}) 보다 클 수 없습니다.`);
    item.raw.reqmaxcnsrvdate = max;
  }
  if(targetIndex !== -1){
    insertComingCnsrvApproveBIAData.value.cnsrvList[targetIndex].reqcnsrvdt = item.raw.reqmaxcnsrvdate.replace(/-/g, '.');
  }
}

const getDctMgmtRegiHeaders = [
  { key: "mgmtno", title: "관리번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "maxcnsrvdate", title: "최대존안가능기간", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqmaxcnsrvdate", title: "요청존안기간", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


const selectFindManagerList = ref([]);
const selectFindManagerCondi = ref({
  vgroupid: ['8010','8015']
}) // 8010 : 존안관리담당자, 8015: 존안관리부서장

const selectFindManager = async () => {

  try {
    const response = await API.comAPI.selectFindManager(selectFindManagerCondi.value, urlPaths.value);
    selectFindManagerList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// eslint-disable-next-line
const getDctMgmtRegi = async () => {
  getDctMgmtRegiCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.getDctMgmtRegi(getDctMgmtRegiCondi.value, urlPaths.value);
    response.data = response.data.map((u) => {
        u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
        u = { ...u, maxcnsrvdate: parseInt(u.regirecvyear) + parseInt(u.notiprsrvterm) +".12.31"}; // 생산년도(yyyy) + 예고문보존기간
        u = { ...u, reqmaxcnsrvdate: parseInt(u.regirecvyear) + parseInt(u.notiprsrvterm) +"-12-31"}; // 생산년도(yyyy) + 예고문보존기간
        return { ...u};
    });
    getDctMgmtRegiList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const insertComingCnsrvApproveBIA = async (param) => {
  isloading.value = true;

  param.pathList[0].appropinion = param.appropiniontemp;

  API.useAPI
    .insertComingCnsrvApproveBIA(param, urlPaths.value)
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
