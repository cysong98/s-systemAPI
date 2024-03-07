<!--para
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
      <h4 class="h4">인계인수</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 v-if="insertApprreqData.parentPage == 'BmsComtransproctodolist'" class="h5">처리할 인계인수서 상세</h5>
          <h5 v-if="insertApprreqData.parentPage == 'BmsTrnreqlist'" class="h5">요청한 인계인수서 상세</h5>
          <h5 v-if="insertApprreqData.parentPage == 'BmsTrncompletelist'" class="h5">처리한 인계인수서 상세</h5>
          <h5 v-if="insertApprreqData.parentPage == 'BmsTrncurrentlist'" class="h5">인수현황 상세</h5>
          <div class="action-buttons">
            <!-- <v-btn v-if="insertApprreqData.parentPage == 'BmsTrntempsavelist'" class="button" @click="insertTrnStep()">승인요청</v-btn>
            <v-btn v-if="insertApprreqData.parentPage == 'BmsTrntempsavelist'" class="button" @click="insertApprreq()">임시저장</v-btn> -->
            <v-btn v-if="insertApprreqData.parentPage == 'BmsComtransproctodolist' || insertApprreqData.parentPage == 'BmsComtransprocdepttodolist' || insertApprreqData.parentPage == 'Home'" class="button" outlined @click="trnApprovalPopupToggleFunc(1)">{{namingGubun()}}</v-btn>
            <v-btn v-if="insertApprreqData.parentPage == 'BmsComtransproctodolist' || insertApprreqData.parentPage == 'BmsComtransprocdepttodolist' || insertApprreqData.parentPage == 'Home'" class="button" outlined @click="trnApprovalPopupToggleFunc(2)">반려</v-btn>
            <!-- <v-btn v-if="insertApprreqData.parentPage == 'BmsTrncurrentlist' && insertApprreqData.status != 'TRS04'                                " class="button" outlined @click="trnApprovalPopupToggleFunc(3)">인수</v-btn> -->
            <v-btn v-if="insertApprreqData.parentPage == 'BmsComtransproctodolist'"        class="button" outlined @click="moveToBmsComtransproctodolist()">닫기</v-btn> <!--처리할(개인)-->
            <v-btn v-else-if="insertApprreqData.parentPage == 'BmsComtransprocdepttodolist'"    class="button" outlined @click="moveToBmsComtransprocdepttodolist()">닫기</v-btn> <!--처리할(부서)-->
            <v-btn v-else-if="insertApprreqData.parentPage == 'BmsTrnreqlist'"                  class="button" outlined @click="moveToBmsTrnreqlist()">닫기</v-btn> <!--요청한-->
            <v-btn v-else-if="insertApprreqData.parentPage == 'BmsTrncompletelist'"             class="button" outlined @click="moveToBmsTrncompletelist()">닫기</v-btn> <!--처리한-->
            <!-- <v-btn v-else-if="insertApprreqData.parentPage == 'BmsTrntempsavelist'"             class="button" outlined @click="moveToBmsTrntempsavelist()">닫기</v-btn> -->
            <v-btn v-else-if="insertApprreqData.parentPage == 'BmsTrncurrentlist'"              class="button" outlined @click="moveToBmsTrncurrentlist()">닫기</v-btn> <!--인수현황-->
            <v-btn v-else class="btn" outlined @click="moveToBmsComtransproctodolist()">닫기</v-btn>
          </div>
        </div>
      </section>


      <!-- 기본정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">기본정보</h6>
        </div>

        <v-table class="table-type-03">
          <colgroup>
            <col width="120">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th><span class="required">제목</span></th>
              <td>
                <v-text-field
                  :readonly="isReadOnly"
                  v-model="insertApprreqData.reqttl"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th><span class="required">사유</span></th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="insertApprreqData.reqcontents"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 인계정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인계정보</h6>
          <div class="action-buttons">
            <v-btn v-if="insertApprreqData.parentPage != 'BmsTrntempsavelist'" class="button" :disabled="true" @click="dctPathPopupToggleFunc(2)">인계결재선 지정</v-btn>
            <v-btn v-else class="button" @click="dctPathPopupToggleFunc(1)">인계결재선 지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 20%">부서</th>
              <th style="width: 10%">직위(직급)</th>
              <th style="width: 10%">성명</th>
              <th style="width: 10%">구분</th>
              <th style="width: 35%">의견</th>
              <th style="width: 15%">처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="handover, idx in insertApprreqData.handoverList" :key="idx">
              <td>{{ handover.apprdeptname }}</td>
              <td>{{ handover.apprposition }}</td>
              <td>{{ handover.apprusername }}</td>
              <td>{{ transformedHandoverGubun[idx] || '' }}</td>
              <td>{{ handover.apprreason }}</td>
              <td>{{ transformApprstatus(handover.apprstatus, handover.apprcode) }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 인수정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인수정보</h6>
          <div class="action-buttons">
            <v-btn v-if="insertApprreqData.parentPage != 'BmsTrntempsavelist'" class="button" :disabled="true" @click="dctPathPopupToggleFunc(2)">인수결재선 지정</v-btn>
            <v-btn v-else class="button" @click="dctPathPopupToggleFunc(2)">인수결재선 지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 20%">부서</th>
              <th style="width: 10%">직위(직급)</th>
              <th style="width: 10%">성명</th>
              <th style="width: 10%">구분</th>
              <th style="width: 35%">의견</th>
              <th style="width: 15%">처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="takeover, idx in insertApprreqData.takeoverList" :key="idx">
              <td>{{ takeover.apprdeptname }}</td>
              <td>{{ takeover.apprposition }}</td>
              <td>{{ takeover.apprusername }}</td>
              <td>{{ transformedTakeoverGubun[idx] || '' }}</td>
              <td>{{ takeover.apprreason }}</td>
              <td>{{ transformApprstatus(takeover.apprstatus, takeover.apprcode) }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 인계인수목록 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인계인수목록</h6>
          <div class="action-buttons">
            <v-btn v-if="insertApprreqData.parentPage != 'BmsTrntempsavelist'" class="button" :disabled="true" @click="trnObjectPopupToggleFunc()">대상선택</v-btn>
            <v-btn v-else class="button" @click="trnObjectPopupToggleFunc()">대상선택</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 15%;">등록일</th>
              <th style="width: 15%;">관리번호</th>
              <th style="width: 20%;">문서번호</th>
              <th style="width: 35%;">제목</th>
              <th style="width: 15%;">구분</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="select, idx in insertApprreqData.selectList" :key="idx">
              <td>{{ select.reportdt }}</td>
              <td>{{ select.mgmtno }}</td>
              <td>{{ select.docno }}</td>
              <td>{{ select.objectname }}</td>
              <td>{{ select.paperflag === 'Y' ? '전자' : (select.paperflag === 'N' ? '비전자' : select.paperflag) }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>

  </div>

  <v-dialog v-model="bVisibleDctPathPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctPathPopup = false">
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
        <v-btn class="close" icon @click="bVisibleTrnObjectPopup = false">
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

  <v-dialog v-model="bVisibleTrnApprovalPopup" style="width: 800px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>
          {{
            (approveType === 1) && (getUserLoginData.username !== insertApprreqData.finalTakeOver) ? '승인'
            : (approveType === 1) && (getUserLoginData.username === insertApprreqData.finalTakeOver) ? '인수'
            : approveType === 2 ? '반려'
            : ''
          }}
        </v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleTrnApprovalPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <TrnApprovalPopup
        v-if="bVisibleTrnApprovalPopup"
        :args="trnApprovalPopupArgs"
        :toggleFunc="trnApprovalPopupToggleFunc"
      ></TrnApprovalPopup>
    </v-card>
  </v-dialog>
  
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import { ref, onBeforeMount, onMounted, computed, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { storeToRefs } from 'pinia';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import DctPathTrnPopup from "@/components/dct/DctPathTrnPopup.vue";
import TrnObjectPopup from "./TrnObjectPopup.vue";
import TrnApprovalPopup from "./TrnApprovalPopup.vue";
import { transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { togglePopup } from "@/utils/Popup.js";

const name = ref('BmsTrnDetailCard')
const params = ref({});
const query = ref({});
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isReadOnly = ref(true);

const namingGubun = () => {
  if(insertApprreqData.value.apprcode === "ARC05")
    return '인수';
  else 
    return '승인';
}

const transformApprstatus = (apprstatus, apprcode) => {
  if(apprcode === 'ARC01') return '요청';
  if(apprstatus === 'APR01') {
    return '처리대기';
  }
  else if(apprstatus === 'APR02'){
    if(apprcode === 'ARC05') return '인수완료';
    return '승인';
  }
  else
    return '반려';
};

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
        handover.apprdeptid = retValue[i].deptid;
        handover.apprdeptname = retValue[i].deptname;
        handover.apprposition = retValue[i].grade;
        handover.appruserid = retValue[i].userid;
        handover.apprusername = retValue[i].username;
        handover.handovergubun = retValue[i].askTemp;
        handover.apprcode = retValue[i].asktype;
        handover.apprseq = i+1;
        handover.apprstatus = "APR01" //(요청)bms_trn_step.apprstatus
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
        takeover.apprdeptid = retValue[i].deptid;
        takeover.apprdeptname = retValue[i].deptname;
        takeover.apprposition = retValue[i].grade;
        takeover.appruserid = retValue[i].userid;
        takeover.apprusername = retValue[i].username;
        takeover.takeovergubun = retValue[i].askTemp;
        takeover.apprcode = retValue[i].asktype;
        takeover.apprseq = i+1+insertApprreqData.value.handoverList.length;
        takeover.apprstatus = "APR01" //(요청)bms_trn_step.apprstatus
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
      select.objectid = retValue[i].docid;
      select.docno = retValue[i].docno;
      select.objectname = retValue[i].secttl;
      select.mgmtid = retValue[i].mgmtid;
      if(retValue[i].regirecvtype == '1'){
        select.paperflag = 'Y'
      }else{
        select.paperflag = 'N'
      }
      select.seclevel = retValue[i].seclevel;
      select.mgmtno = retValue[i].mgmtno;
      select.reportdt = transformDate(retValue[i].indt);
      select.copyno = retValue[i].copyno;
      select.selectauthorname = retValue[i].authorname;
      select.selectdeptname = retValue[i].deptname;
      select.secstatus = retValue[i].secstatus;
      select.docnoseq = i+1;
      select.status = "TRS01" //(결재중) bms_trn_object.status 진행상태
      select.secstatus = "MGT50" //(인수인계중) bms_dct_mgmt_regi.secstatus 비밀상태
      insertApprreqData.value.selectList.push(select);
    }
  }
  trnObjectPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 승인/반려 팝업 ======================== */
const bVisibleTrnApprovalPopup = ref(false)
const trnApprovalPopupArgs = ref({})
const approveType = ref(null);
const trnApprovalPopupToggleFunc = ref((type) => { 
  trnApprovalPopupArgs.value = insertApprreqData.value;
  trnApprovalPopupArgs.value.type = type;
  approveType.value = type;
  bVisibleTrnApprovalPopup.value = !bVisibleTrnApprovalPopup.value;
})
/* ============================================================ */


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
  apprstatus: "",

  // 인계정보
  handoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      appruserid: "",
      apprusername: "",
      handovergubun: "",
      apprcode: "",
      apprstatus: "",
      apprseq: "",
    }
  ],

  // 인수정보
  takeoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      appruserid: "",
      apprusername: "",
      takeovergubun: "",
      apprcode: "",
      apprstatus: "",
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
  apprstatus: "",

  // 인계정보
  handoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      appruserid: "",
      apprusername: "",
      handovergubun: "",
      apprcode:"",
      apprstatus: "",
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
      apprstatus: "",
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

onBeforeMount(async() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  insertApprreqData.value = query.value
  insertApprreqData.value.reqttl = query.value.title
  insertApprreqData.value.reqcontents = query.value.reason
  // await selectTrnReqList();
  if(query.value.transferid && query.value.transferid.value !== ""){
    await selectTrnOverList();
    await selectTrnObjectList();
    if(insertApprreqData.value.proc == 'proc'){
      await updateTrnRepotBIA0();
    }
  }
});

watchEffect(() => {
  isReadOnly.value = insertApprreqData.value.parentPage !== 'BmsTrntempsavelist';
});

 
const updateTrnRepotBIA0 = async () => {

  insertApprreqData.value.transferid = query.value.transferid;
  insertApprreqData.value.apprstatus = 'APP02' //결재중 //BMS_TRN_REPORT.apprstatus(최종결재완료여부)
  insertApprreqData.value.gojudge = 'stop'
  insertApprreqData.value.appruserid = getUserLoginData.value.userid;

  API.trnAPI
    .updateTrnRepotBIA1(insertApprreqData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("결재중 처리 되었습니다");
        // moveToBmsComtransproctodolist();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}

const selectTrnReqListList = ref([])
const selectTrnObjectListList = ref([])
const selectTrnReqListDefCondi = {
  title: "",
  reqdeptname: "",
  requsername: "",
  apprdeptname: "",
  apprusername: "",
  transferid: "",
  status: "",
  apprstatus: "",
  appruserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "transferid"
}
// selectTrnReqListDefCondi.value.transferid = query.value.transferid;
const selectTrnReqListCondi = ref({ ...selectTrnReqListDefCondi })
// const selectTrnReqList = async (/*pageNum*/) => {
const selectTrnOverList = async () => {
try {
  selectTrnReqListCondi.value.transferid = query.value.transferid;
  // selectTrnReqListCondi.value.appruserid = getUserLoginData.value.userid;
  const response = await API.trnAPI.selectTrnOverList({ ...selectTrnReqListCondi.value }, urlPaths.value);

  selectTrnReqListList.value = response.data;

  /* ======================== 인계정보 ======================== */
  // const validDocSeq = ["1", ""];
  const validHandOverApprCodes = ["ARC01", "ARC02", "ARC03", "ARC04"]; 
  //인계정보코드 BMS_TRN_STEP.apprcode // "ARC01"(인계담당), "ARC02"(인계관리), "ARC03"(인계결재), "ARC04"(보안통제)

  insertApprreqData.value.handoverList = selectTrnReqListList.value
  .filter(item => 
  // validHandOverApprCodes.includes(item.apprcode) && validDocSeq.includes(item.docnoseq)
  validHandOverApprCodes.includes(item.apprcode)
  )
  .map(item => {
    return {
      apprdeptid: item.apprdeptid,
      apprdeptname: item.apprdeptname,
      apprposition: item.apprposition,
      appruserid: item.appruserid,
      apprusername: item.apprusername,
      apprreason: item.apprreason,
      // handovergubun: "",
      handovergubun: item.apprcode,
      apprcode: item.apprcode,
      apprstatus: item.apprstatus,
      apprseq: item.apprseq,
      state: item.state
    };
  });
  /* ============================================================ */

  /* ======================== 인수정보 ======================== */
  const validTakeOverApprCodes = ["ARC05", "ARC06", "ARC07", "ARC08"]; 
  //인수정보코드 BMS_TRN_STEP.apprcode // "ARC05"(인수담당), "ARC06"(인수관리), "ARC07"(인수결재), "ARC08"(보안통제)

  insertApprreqData.value.takeoverList = selectTrnReqListList.value
    .filter(item => 
    validTakeOverApprCodes.includes(item.apprcode)
    )
    .map(item => {
      return {
        apprdeptid: item.apprdeptid,
        apprdeptname: item.apprdeptname,
        apprposition: item.apprposition,
        appruserid: item.appruserid,
        apprusername: item.apprusername,
        apprreason: item.apprreason,
        takeovergubun: item.apprcode,
        apprcode: item.apprcode,
        apprstatus: item.apprstatus,
        apprseq: item.apprseq,
        state: item.state
      };
    });
  /* ============================================================ */ 
  } catch (error) {
    console.log(error);
  }
}

const selectTrnObjectList = async () => {
try {
    selectTrnReqListCondi.value.transferid = query.value.transferid;
    const response = await API.trnAPI.selectTrnObjectList({ transferid: selectTrnReqListCondi.value.transferid }, urlPaths.value);
    selectTrnObjectListList.value = response.data;
    /* ======================== 인계인수목록 정보 ======================== */
    insertApprreqData.value.selectList = selectTrnObjectListList.value;
    insertApprreqData.value.selectList.forEach(item => {
      item.reportdt = transformDate(item.reportdt);
    });
    /* ================================================================== */
  }catch (error) {
    console.log(error);
  }
}

//승인요청
const insertTrnStep = async () => {
  // if (checkRVInsertTrnStep() == false) {
  //   return;
  // }
  insertApprreqData.value.transferid = query.value.transferid;
  insertApprreqData.value.requserid = getUserLoginData.value.userid;
  insertApprreqData.value.apprstatus = 'APP01' //상신 //BMS_TRN_REPORT.apprstatus(최종결재완료여부) APP09 임시저장
  insertApprreqData.value.parentPage = 'BmsTrntempsavelist';
  API.trnAPI
    .insertTrnStep(insertApprreqData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("정상 등록되었습니다");
        moveToBmsTrnreqlist();
      }else{
        alert("실패하였습니다");
      }
    })
    .catch(error => {
      console.log(error);
    });
}

//임시저장
const insertApprreq = async () => {
  insertApprreqData.value.requserid = getUserLoginData.value.userid;
  insertApprreqData.value.apprstatus = 'APP09' //임시저장 //BMS_TRN_REPORT.apprstatus(최종결재완료여부) APP01 상신
  API.trnAPI
    .insertApprreq(insertApprreqData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("임시저장 되었습니다");
        moveToBmsTrntempsavelist();
      }else{
        alert("실패하였습니다");
      }
    })
    .catch(error => {
      console.log(error);
    });
}

// Move Function
const moveToBmsComtransproctodolist = () => {
  router.push({
    name: "BmsComtransproctodolist"
    });
}

// Move Function
const moveToBmsComtransprocdepttodolist = () => {
  router.push({
    name: "BmsComtransprocdepttodolist"
    });
}
// Move Function
const moveToBmsTrnreqlist = () => {
  router.push({
    name: "BmsTrnreqlist"
    });
}
// Move Function
const moveToBmsTrncompletelist = () => {
  router.push({
    name: "BmsTrncompletelist"
    });
}
// Move Function
const moveToBmsTrntempsavelist = () => {
  router.push({
    name: "BmsTrntempsavelist"
    });
}
// Move Function
const moveToBmsTrncurrentlist = () => {
  router.push({
    name: "BmsTrncurrentlist"
    });
}

const transformedHandoverGubun = computed(() => {
    if (!insertApprreqData.value || !insertApprreqData.value.handoverList || insertApprreqData.value.handoverList == undefined ) {
      return [];
    }

    if(insertApprreqData.value.handoverList != undefined){
      return insertApprreqData.value.handoverList.map(handover => {
        switch (handover.handovergubun) {
          case 'ARC01':
            return '담당자';
          case 'ARC02':
            return '검토자';
          case 'ARC03':
            return '승인자';
          case 'ARC04':
            return '보안통제';
          default:
            return handover.handovergubun; // 기본값은 그대로 반환
        }
      });
    }
  });

  const transformedTakeoverGubun = computed(() => {
    if (!insertApprreqData.value || !insertApprreqData.value.takeoverList || insertApprreqData.value.takeoverList == undefined ) {
      return [];
    }

    if(insertApprreqData.value.takeoverList != undefined){
      return insertApprreqData.value.takeoverList.map(takeover => {
        switch (takeover.takeovergubun) {
          case 'ARC05':
            return '담당자';
          case 'ARC06':
            return '검토자';
          case 'ARC07':
            return '승인자';
          case 'ARC08':
            return '보안통제';
          default:
            return takeover.takeovergubun; // 기본값은 그대로 반환
        }
      });
    }
  });
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
