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
      <h4 class="h4">비밀문서 요청(비밀관리기록부)</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비전자 인쇄 요청서</h5>
          <div class="action-buttons">
            <v-btn class="button" @click="useManagerChoicePopupToggleFunc()">관리자지정</v-btn>
            <v-btn class="button" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn class="button" @click="useApprovalPopupToggleFunc()">승인요청</v-btn>
            <v-btn class="button" @click="moveToBmsUsePrintReqList()">닫기</v-btn>
          </div>
        </div>
      </section>

      <!-- 요청정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청정보</h6>
        </div>

        <v-table class="table-type-03">
          <colgroup>
            <col width="240">
            <col width="">
            <col width="240">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th><span class="required">제목</span></th>
              <td>
                <v-text-field v-model="insertUseNonDigitalPrintData.reqttl" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>사유</th>
              <td>
                <v-text-field v-model="insertUseNonDigitalPrintData.reqcontents" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 결재선정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">결재선정보</h6>
          <div class="action-buttons">
            <v-btn class="button" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-02">
          <thead>
            <tr>
              <th>구분</th>
              <th>부서</th>
              <th>직위/성명</th>
              <th>의견</th>
              <th>서명</th>
              <th>처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pathData, idx in sortedPathList" :key="idx">
              <td>
                <v-text-field
                  v-model="pathData.apprtemp"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="pathData.apprdeptname"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="pathData.apprnametemp"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="pathData.appropinion"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="pathData.electsign"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="pathData.apprgubuntemp"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 기안보안관리자정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">기안보안관리자정보</h6>
          <div class="action-buttons">
          <v-btn class="button" @click="useManagerChoicePopupToggleFunc()">관리자지정</v-btn>
        </div>
        </div>

        <v-table class="table-type-02">
          <thead>
            <tr>
              <th>구분</th>
              <th>부서</th>
              <th>직위(직급)</th>
              <th>성명</th>
              <th>서명</th>
              <th>처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.vgroupname"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.deptname"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.grade"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.username"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.electsign"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
              <td>
                <v-text-field
                  v-model="insertUseNonDigitalPrintData.managertemp.result"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                >
                </v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 요청내용 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>

        <v-data-table-server
          :headers="getDctMgmtRegiHeaders"
          :items="getDctMgmtRegiList"
          :items-length="getDctMgmtRegiList.length"
          item-value="mgmtid"
          :expanded="expandedItems"
          class="table-type-02"
        >
        <template v-slot:expanded-row="{ columns, item }">
          <tr>
            <td :colspan="columns.length">
              <ul class="attach-list ml-16">
                <li>
                  <span class="branch-down"></span>
                  <span class="spacing">보관함: </span>{{item.raw.locker}} <span class="spacing">, 단번호: </span>{{item.raw.unitnum}}
                </li>
                <li>
                  <span class="branch-down"></span>
                  <span class="spacing">자료유형: </span>{{item.raw.doctype}} <span class="spacing">, 매체유형: </span>{{item.raw.mediatype}}
                </li>
              </ul>
            </td>
          </tr>
        </template>
        <template v-slot:item.reqdestructdt="{ item }">
          <v-text-field type="date" :min="today" v-model="item.raw.reqdestructdt" @update:modelValue="updateDestructDt(item)" variant="outlined" hide-details="auto"/>
        </template>
        <template v-slot:item.reqprtcnt="{ item }">
          <v-text-field v-model="item.raw.reqprtcnt" @update:modelValue="updateReqPrtCnt(item)" variant="outlined" hide-details="auto"/>
        </template>
        <template v-slot:bottom></template>
        </v-data-table-server>
      </section>
    </div>
  </div>

  <v-dialog v-model="bVisibleUseManagerChoicePopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>관리자지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUseManagerChoicePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UseManagerChoicePopup
        v-if="bVisibleUseManagerChoicePopup"
        :args="useManagerChoicePopupArgs"
        :toggleFunc="useManagerChoicePopupToggleFunc"
        :returnFunc="useManagerChoicePopupReturnFunc"
      ></UseManagerChoicePopup>
    </v-card>
  </v-dialog>

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
        :insertFunc="insertUseNonDigitalPrint"
      ></UseApprovalPopup>
    </v-card>
  </v-dialog>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { storeToRefs } from 'pinia';
import UseManagerChoicePopup from "./UseManagerChoicePopup.vue";
import UsePathPopup from "./UsePathPopup.vue";
import UseApprovalPopup from "./UseApprovalPopup.vue";
import { API } from '@/api'; 
import { useLoginStore } from '/src/store/Login';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js"; 

const name = ref('BmsUseDigitalPrintReq')
/* ======================== 관리자지정 팝업 ======================== */
const bVisibleUseManagerChoicePopup = ref(false)
const useManagerChoicePopupReuturn = ref({})
const useManagerChoicePopupArgs = ref({})
const useManagerChoicePopupToggleFunc = ref(() => { 
  bVisibleUseManagerChoicePopup.value = !bVisibleUseManagerChoicePopup.value;
})
const useManagerChoicePopupReturnFunc = ref((retValue) => {
  useManagerChoicePopupToggleFunc.value()
  if(retValue != ""){
    insertUseNonDigitalPrintData.value.managertemp.vgroupname = retValue[0].vgroupname;
    insertUseNonDigitalPrintData.value.managertemp.deptname = retValue[0].deptname;
    insertUseNonDigitalPrintData.value.managertemp.grade = retValue[0].grade;
    insertUseNonDigitalPrintData.value.managertemp.username = retValue[0].username;
    insertUseNonDigitalPrintData.value.managertemp.electsign = '';
    insertUseNonDigitalPrintData.value.managertemp.result = '';
    insertUseNonDigitalPrintData.value.managertemp.id = retValue[0].userid;
  }
})
/* ============================================================ */

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleUsePathPopup = ref(false)
const usePathPopupReuturn = ref({})
const usePathPopupArgs = ref({})
const usePathPopupToggleFunc = ref(() => { 
  const pathList = [...insertUseNonDigitalPrintData.value.pathList];
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
  usePathPopupArgs.value = modifiedPathList;
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
      insertUseNonDigitalPrintData.value.pathList.push(path);
    };
    sortedPathList.value;
  }else{
    insertInitialPath();
  }
});

const insertInitialPath = () => {
  insertUseNonDigitalPrintData.value.pathList = [];
  const initialPath = {};
  initialPath.apprreqseq = "1";
  initialPath.apprtemp = "요청";
  initialPath.apprtype = "1";
  initialPath.apprgubun = "1";
  initialPath.apprstatus = "APP00";
  initialPath.apprid = getUserLoginData.value.userid;
  initialPath.apprname = getUserLoginData.value.username;
  initialPath.apprdeptid = getUserLoginData.value.deptid;
  initialPath.apprdeptname = getUserLoginData.value.deptname;
  initialPath.apprdutyname = getUserLoginData.value.grade;
  initialPath.apprnametemp = getUserLoginData.value.grade+"/"+getUserLoginData.value.username;
  initialPath.rgstid = getUserLoginData.value.userid;
  initialPath.rgstname = getUserLoginData.value.username;
  insertUseNonDigitalPrintData.value.pathList.push(initialPath);
};

const sortedPathList = computed(() => {
  const requests = [];
  const reviewers = [];
  const approvers = [];

  // '요청', '검토', '승인'에 따라 재정렬
  insertUseNonDigitalPrintData.value.pathList.forEach((path) => {
    switch (path.apprtemp) {
      case '요청':
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

  insertUseNonDigitalPrintData.value.pathList = requests.concat(reviewers, approvers);
  updateApprreqSeq();

  return insertUseNonDigitalPrintData.value.pathList
});

const updateApprreqSeq = () => {
  insertUseNonDigitalPrintData.value.pathList.forEach((path, index) => {
      path.apprreqseq = index + 1;
  });
};
/* ============================================================ */

/* ======================== 승인요청` 팝업 ======================== */
const bVisibleUseApprovalPopup = ref(false)
const useApprovalPopupReuturn = ref({})
const useApprovalPopupArgs = ref({})
const useApprovalPopupToggleFunc = ref(() => { 

  if(!insertUseNonDigitalPrintData.value.reqttl){
    alert("제목을 작성해주세요")
    return
  }

  if(insertUseNonDigitalPrintData.value.pathList.length == 1){
    alert("결재선지정을 해주세요.")
    return
  }

  if(insertUseNonDigitalPrintData.value.managertemp.vgroupname == ""){
    alert("관리자를 지정해주세요.")
    return
  }

  let dateNull = 0;
  let prtNull = 0;
  insertUseNonDigitalPrintData.value.printList.forEach(item => {
    if (!item.reqdestructdt) {
      dateNull++;
      return;
    }
    if (!item.reqprtcnt) {
      prtNull++;
      return;
    }
  });
  if(dateNull>0){
    alert("파쇄예정일을 선택해주세요")
    return
  }
  if(prtNull>0){
    alert("인쇄부수를 입력해주세요")
    return
  }

  useApprovalPopupArgs.value = insertUseNonDigitalPrintData.value;
  bVisibleUseApprovalPopup.value = !bVisibleUseApprovalPopup.value;
})
/* ============================================================ */
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const params = ref({});
const query = ref({});

const today = ref("");

// for insertUseNonDigitalPrint
const insertUseNonDigitalPrintData = ref({

  // 1) bms_dct_apprreq table
  apprreqid: "", // 결재요청 ID
  reqtype: "03", // 요청타입 (01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출, 06: 재분류, 07: 파기, 08: 존안, 09: 이관, 10: 이관대기, 11: 인관연기, 12: 내부이관, 13: 비전자, 14: 오프라인열람, 15: 인쇄용프린터, 16: 추가발송)
  reqttl: "", // 요청제목
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

  // 관리리자 정보 viewing 용도
  managertemp: {
  vgroupname: "",
  deptname: "",
  grade: "",
  username: "",
  electsign: "",
  result: "",
  id: "",
  },

  // 3) bms_dct_apprreq_prt table
  printList: [
    {
      apprreqid: "", // 요청ID
      mgmtid: "", // 비밀관리 ID
      reqprtcnt: "", // 요청인쇄부수
      reqdestructdt: "", // 요청파기예정일
      grantflag: "", // 승인여부
      prtcharge: "", // 기관보안관리자
    }
  ],

  docid: [],
  dutyname: "",

})
// for getDctMgmtRegi
const getDctMgmtRegiList = ref([])
const getDctMgmtRegiDefCondi = {
  mgmtid: []
}
const getDctMgmtRegiCondi = ref({ ...getDctMgmtRegiDefCondi })

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  const mgmtidArray = query.value.mgmtid.replace(/'/g, '').split(','); 
  getDctMgmtRegiCondi.value.mgmtid = mgmtidArray;
  insertUseNonDigitalPrintData.value.printList = mgmtidArray.map(mgmtid => ({
    apprreqid: "", 
    mgmtid: mgmtid, 
    reqprtcnt: "",
    reqdestructdt: "",
    grantflag: "N",
    prtcharge: "",
  }));
})

onMounted(async () => {
  await getDctMgmtRegiPrint();
  insertUseNonDigitalPrintData.value.reqid = getUserLoginData.value.userid;
  insertUseNonDigitalPrintData.value.reqname = getUserLoginData.value.username;
  insertUseNonDigitalPrintData.value.reqdeptid = getUserLoginData.value.deptid;
  insertUseNonDigitalPrintData.value.reqdeptname = getUserLoginData.value.deptname;
  insertInitialPath();
  today.value = dayjs().format('YYYY-MM-DD');
  insertUseNonDigitalPrintData.value.dutyname = getUserLoginData.value.grade;
  insertUseNonDigitalPrintData.value.docid = getDctMgmtRegiList.value.map(item => item.docid);
})

watchEffect(() => {
})

const updateDestructDt = (item) => {
  const targetIndex = insertUseNonDigitalPrintData.value.printList.findIndex(i => i.mgmtid === item.key);
  if (targetIndex !== -1) {
    insertUseNonDigitalPrintData.value.printList[targetIndex].reqdestructdt = item.raw.reqdestructdt;
  }
};

const updateReqPrtCnt = (item) => {
  const targetIndex = insertUseNonDigitalPrintData.value.printList.findIndex(i => i.mgmtid === item.key);
  if (targetIndex !== -1) {
    insertUseNonDigitalPrintData.value.printList[targetIndex].reqprtcnt = item.raw.reqprtcnt;
  }
};

const getDctMgmtRegiHeaders = [
  { key: "mgmtno", title: "관리번호", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "부서", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqdestructdt", title: "파쇄예정일", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqprtcnt", title: "인쇄부수", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

const expandedItems = ref([]);

// eslint-disable-next-line
const getDctMgmtRegiPrint = async () => {
  getDctMgmtRegiCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.getDctMgmtRegiPrint(getDctMgmtRegiCondi.value, urlPaths.value);
    getDctMgmtRegiList.value = response.data.map(item => ({
      ...item, reqdestructdt: '', reqprtcnt: ''
    }));

    expandedItems.value = getDctMgmtRegiList.value.map(item => item.mgmtid);

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}


const insertUseNonDigitalPrint = async (param) => {
  // if (checkRVBmsDctApprreqPrt() == false) {
  //   return;
  // }

  param.pathList[0].appropinion = param.appropiniontemp;
  param.printList.map(item=>item.prtcharge = param.managertemp.id);

  API.useAPI
    .insertUseNonDigitalPrint(param, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("정상 등록되었습니다");
        moveToBmsUsePrintReqList();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}

// Check Validation
const checkRVBmsDctApprreqPrt = () => {
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.apprreqid,
      true,
      "checkStrLen",
      "요청 ID 형식을 확인하세요.",
      35
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.mgmtid,
      true,
      "checkStrLen",
      "관리번호 형식을 확인하세요.",
      35
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.reqprtcnt,
      true,
      "checkReal",
      "요청인쇄부수 형식을 확인하세요."
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.reqdestructdt,
      true,
      "checkStrLen",
      "요청파기예정일 형식을 확인하세요.",
      14
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.grantprtcnt,
      false,
      "checkReal",
      "승인인쇄부수 형식을 확인하세요."
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.grantdestructdt,
      false,
      "checkStrLen",
      "승인파기예정일 형식을 확인하세요.",
      14
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.prtcnt,
      false,
      "checkReal",
      "인쇄부수 형식을 확인하세요."
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.prtdt,
      false,
      "checkStrLen",
      "인쇄일시 형식을 확인하세요.",
      14
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.destructcnt,
      false,
      "checkReal",
      "파기부수 형식을 확인하세요."
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.destructdt,
      false,
      "checkStrLen",
      "파기일시 형식을 확인하세요.",
      14
    )
  )
    return false;
  if (
    !checkRV.checkRV(
      insertUseNonDigitalPrintData.value.grantflag,
      false,
      "checkStrLen",
      "승인여부 형식을 확인하세요.",
      1
    )
  )
    return false;

  return true;
}


const moveToBmsUsePrintReqList = (item) => {
  router.push({
    name: "BmsUsePrintReqList",
  });
};
</script>

<style lang="scss">
.v-data-table .v-data-table__expandable .v-data-table__expandable__content {
  display: block;
}
</style>
