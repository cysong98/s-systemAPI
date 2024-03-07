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
    <v-text class="text-h4">요청한 인계인수서</v-text>
    <div class="btns">
      <v-btn class="btn" text-color="black" @click="moveToBmsTrnreqlist()">닫기</v-btn>
    </div>

    <h3>기본정보</h3>
    <v-table>
      <tbody>
        <tr>
          <th>제목*</th>
          <td>
            <v-text-field :readonly="true" v-model="insertApprreqData.reqttl"></v-text-field>
          </td>
        </tr>
        <tr>
          <th>사유</th>
          <td>
            <v-text-field :readonly="true" v-model="insertApprreqData.reqcontents"></v-text-field>
          </td>
        </tr>
        <tr>
          <th>처리의견</th>
          <td>
            <v-text-field :readonly="true"></v-text-field>
          </td>
        </tr>
      </tbody>
    </v-table>

    <br><br>

    <h3>인계정보*</h3>
    <v-table>
      <thead>
        <tr>
          <th>부서</th>
          <th>직위(직급)</th>
          <th>성명</th>
          <th>구분</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="handover, idx in insertApprreqData.handoverList" :key="idx">
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="handover.apprdeptname"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="handover.apprposition"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="handover.apprusername"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="handover.handovergubun"
              :readonly="true">
            </v-text-field>
          </td>
        </tr>
      </tbody>
    </v-table>

    <br><br>

    <h3>인수정보*</h3>
    <v-table>
      <thead>
        <tr>
          <th>부서</th>
          <th>직위(직급)</th>
          <th>성명</th>
          <th>구분</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="takeover, idx in insertApprreqData.takeoverList" :key="idx">
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="takeover.apprdeptname"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="takeover.apprposition"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="takeover.apprusername"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="takeover.takeovergubun"
              :readonly="true">
            </v-text-field>
          </td>
        </tr>
      </tbody>
    </v-table>

    <br><br>

    <br><br>

    <h3>인계인수목록*</h3>
    <v-table>
      <thead>
        <tr>
          <th>ID</th>
          <th>구분</th>
          <th>제목</th>
          <th>등록일</th>
          <th>관리번호</th>
          <th>문서번호</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="select, idx in insertApprreqData.selectList" :key="idx">
          <td>
            <v-text-field
             class="h-75 w-75 ma-1"
             v-model="select.selectdocid"
             :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              v-model="select.selectdocttl"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              :readonly="true">
            </v-text-field>
          </td>
          <td>
            <v-text-field
              class="h-75 w-75 ma-1"
              :readonly="true">
            </v-text-field>
          </td>
        </tr>
      </tbody>
    </v-table>
  </div>

  <v-dialog v-model="bVisibleDctPathPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" @click="bVisibleDctPathPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      
      <DctPathTrnPopup
        v-if="bVisibleDctPathPopup"
        :args="dctPathPopupArgs"
        :toggleFunc="dctPathPopupToggleFunc"
        :returnFunc="dctPathPopupReturnFunc"
      ></DctPathTrnPopup>
    </v-card>
  </v-dialog>


  <v-dialog v-model="bVisibleTrnObjectPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>인계인수 대상선택</v-toolbar-title>
        <v-btn class="close" @click="bVisibleTrnObjectPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      
      <TrnObjectPopup
        v-if="bVisibleTrnObjectPopup"
        :args="trnObjectPopupArgs"
        :toggleFunc="trnObjectPopupToggleFunc"
        :returnFunc="trnObjectPopupReturnFunc"
      ></TrnObjectPopup>
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
import DctPathTrnPopup from "@/components/dct/DctPathTrnPopup.vue";
import TrnObjectPopup from "./TrnObjectPopup.vue";
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";

const name = ref('BmsDctReqAddROnly')
const params = ref({}); //리스트에서 디테일카드로 넘겨받기위한 선언
const query = ref({}); //리스트에서 디테일카드로 넘겨받기위한 선언

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref((val) => { 
  dctPathPopupArgs.value = val;
  bVisibleDctPathPopup.value = !bVisibleDctPathPopup.value;
 })
const dctPathPopupReturnFunc = ref((retValue) => {
  dctPathPopupToggleFunc.value()
  if(retValue != ""){
    if(retValue.temp == 1){
      insertApprreqData.value.handoverList = [];
      for(let i = 0; i < retValue.length; i++){
        const handover = {};
        handover.apprdeptname = retValue[i].deptname;
        handover.apprposition = retValue[i].grade;
        handover.apprusername = retValue[i].username;
        handover.handovergubun = retValue[i].askTemp;
        handover.apprcode = retValue[i].asktype;
        handover.apprseq = i+1;
        handover.appstatus = "APR01" //(요청)bms_trn_step.appstatus
        handover.state = "DCST1" //(경로에 있음)bms_trn_step.state
        insertApprreqData.value.handoverList.push(handover);
      }
      for(let i = 0; i < insertApprreqData.value.takeoverList.length; i++){
        insertApprreqData.value.takeoverList[i].apprseq = i+1+insertApprreqData.value.handoverList.length;
      }
    }else if(retValue.temp == 2){
      insertApprreqData.value.takeoverList = [];
      for(let i = 0; i < retValue.length; i++){
        const takeover = {};
        takeover.apprdeptname = retValue[i].deptname;
        takeover.apprposition = retValue[i].grade;
        takeover.apprusername = retValue[i].username;
        takeover.takeovergubun = retValue[i].askTemp;
        takeover.apprcode = retValue[i].asktype;
        takeover.apprseq = i+1+insertApprreqData.value.handoverList.length;
        takeover.appstatus = "APR01" //(요청)bms_trn_step.appstatus
        takeover.state = "DCST1" //(경로에 있음)bms_trn_step.state
        insertApprreqData.value.takeoverList.push(takeover);
      }
    }
  }
})
/* ============================================================ */

/* ======================== 대상선택 팝업 ======================== */
const bVisibleTrnObjectPopup = ref(false)
const trnObjectPopupReuturn = ref({})
const trnObjectPopupArgs = ref({})
const trnObjectPopupToggleFunc = ref(() => { bVisibleTrnObjectPopup.value = !bVisibleTrnObjectPopup.value; togglePopup('TrnObjectPopup'); })
const trnObjectPopupReturnFunc = ref((retValue) => {
  trnObjectPopupToggleFunc.value()
  if(retValue != ""){
    insertApprreqData.value.selectList = [];
    for(let i = 0; i < retValue.length; i++){
      const select = {};
      select.selectdocid = retValue[i].docid;
      select.selectdocttl = retValue[i].docttl;
      select.docnoseq = i+1;
      select.status = "TRS01" //(결재중) bms_trn_object.status 진행상태
      select.secstatus = "MGT50" //(인수인계중) bms_dct_mgmt_regi.secstatus 비밀상태
      insertApprreqData.value.selectList.push(select);
    }
  }
  trnObjectPopupReuturn.value = retValue;
})
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')

// 승인요청
const insertTrnStepData = ref({
  transferid: "",
  apprseq: "",
  apprusername: "",
  appruserid: "",
  apprdeptname: "",
  apprdeptid: "",
  apprposition: "",
  apprcode: "",
  apprstatus: "",
  apprdate: "",
  apprreason: "",
  inid: "",
  indt: "",
  state: "",
  electsignid: "",
  appstatus: "",

  // 인계정보
  handoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      apprusername: "",
      handovergubun: "",
      apprcode: "",
      appstatus: "",
      apprseq: "",
    }
  ],

  // 인수정보
  takeoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      apprusername: "",
      takeovergubun: "",
      apprcode: "",
      appstatus: "",
      apprseq: "",
    }
  ],

  // 인계인수목록 대상선택
  selectList: [
    {
      selectdocid: "",
      selectdocttl: "",
      selectorder: "",
    }
  ]
})

// 임시저장
const insertApprreqData = ref({
  apprreqid: "",
  reqtype: "",
  reqdt: "",
  // reqttl: "",
  reqttl: query.value.title,
  reqregdt: "",
  reqcontents: "",
  reqid: "",
  reqname: "",
  reqdeptid: "",
  reqdeptname: "",
  reqstatus: "",
  apprenddt: "",
  adminconfirmdt: "",
  grantflag: "",
  reqrfidcheck: "",
  reqrfidcheckdt: "",
  finalrfidcheck: "",
  finalrfidcheckdt: "",
  reservedate: "",
  destructreserve: "",
  isnewrule: "",
  appstatus: "",

  // 인계정보
  handoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      apprusername: "",
      handovergubun: "",
      apprcode:"",
      appstatus: "",
      apprseq: "",
    }
  ],

  // 인수정보
  takeoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      apprusername: "",
      takeovergubun: "",
      apprcode: "",
      appstatus: "",
      apprseq: "",
    }
  ],

  // 인계인수목록 대상선택
  selectList: [
    {
      selectdocid: "",
      selectdocttl: "",
      selectorder: "",
    }
  ]
})

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})
onBeforeMount(async() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  insertApprreqData.value.reqttl = query.value.title
  insertApprreqData.value.reqcontents = query.value.reason
  await selectTrnReqList();
});

onMounted(() => {
})

watchEffect(() => {
})
watchEffect(async() => {
  
})

const selectTrnReqListList = ref([])
const selectTrnReqListDefCondi = {
  title: "",
  reqdeptname: "",
  requsername: "",
  apprdeptname: "",
  apprusername: "",
  transferid: "",
  status: "",
  apprstatus: "",
  appuserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "transferid"
}
const selectTrnReqListCondi = ref({ ...selectTrnReqListDefCondi })
const selectTrnReqList = async (pageNum) => {
try {
  selectTrnReqListCondi.value.transferid = query.value.transferid;
  
  const response = await API.trnAPI.selectTrnReqList({ ...selectTrnReqListCondi.value }, urlPaths.value);

  selectTrnReqListList.value = response.data.list;
  
  /* ======================== 인계정보 ======================== */
  const validDocSeq = ["1", ""];
  const validHandOverApprCodes = ["ARC01", "ARC02", "ARC03", "ARC04"]; 
  //인계정보코드 BMS_TRN_STEP.apprcode // "ARC01"(인계담당), "ARC02"(인계관리), "ARC03"(인계결재), "ARC04"(보안통제)

  insertApprreqData.value.handoverList = selectTrnReqListList.value
  .filter(item => 
  validHandOverApprCodes.includes(item.apprcode) && validDocSeq.includes(item.docnoseq)
  )
  .map(item => {
    return {
      apprdeptname: item.apprdeptname,
      apprposition: item.apprposition,
      apprusername: item.apprusername,
      handovergubun: "",
      apprcode: item.apprcode,
      appstatus: item.apprstatus,
      apprseq: item.apprseq
    };
  });
  /* ============================================================ */

  /* ======================== 인계정보 ======================== */
  const validTakeOverApprCodes = ["ARC05", "ARC06", "ARC07", "ARC08"]; 
  //인수정보코드 BMS_TRN_STEP.apprcode // "ARC05"(인수담당), "ARC06"(인수관리), "ARC07"(인수결재), "ARC08"(보안통제)

  insertApprreqData.value.takeoverList = selectTrnReqListList.value
    .filter(item => 
    validTakeOverApprCodes.includes(item.apprcode) && validDocSeq.includes(item.docnoseq)
    )
    .map(item => {
      return {
        apprdeptname: item.apprdeptname,
        apprposition: item.apprposition,
        apprusername: item.apprusername,
        handovergubun: "",
        apprcode: item.apprcode,
        appstatus: item.apprstatus,
        apprseq: item.apprseq
      };
    });
  /* ============================================================ */

  /* ======================== 인계인수목록 정보 ======================== */
  // const uniqueSelectTrnReqListListValues = [...new Set(selectTrnReqListList)];
  const validDocnoseq = ref(["1"]);
  const validApprseq = ref([1]);
  const tempDocnoseq = selectTrnReqListList.value.map(selectTrnReqListListData => selectTrnReqListListData.docnoseq);
  validDocnoseq.value = [...new Set(tempDocnoseq)];

  insertApprreqData.value.selectList = selectTrnReqListList.value
    .filter(item => validDocnoseq.value.includes(item.docnoseq) && (item.apprseq === 1))
    .map(item => {
      return {
        transferid: item.transferid,
        selectdocid: item.objectid,
        selectdocttl: item.objectname,
        docnoseq: item.docnoseq,
        status: item.status,
        secstatus: item.secstatus,
      };
    });
  /* ================================================================== */
} catch (error) {
  console.log(error);
}

}

// Move Function
const moveToBmsTrnreqlist = () => {
  router.push({
    name: "BmsTrnreqlist"
    });
}

// 닫기버튼
const moveToBmsTrntempsavelist = () => {
  router.push({
    name: "BmsTrntempsavelist"
  })
}

</script>

<style lang="scss" scoped>
.btns{
  display: flex;
  justify-content: end;
}
.btn{
  border: 1px solid blue;
  font-weight: 600;
  margin-right: 10px;
}
</style>
