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
  <div class="box-title">
    <h4 class="h4">재지정 상세</h4>
  </div>

  <div class="box-content">
    <!-- 비밀조회 -->
    <section class="section sticky">
      <div class="box-title">
        <h5 class="h5">비밀조회</h5>
        <div class="action-buttons">
          <v-btn class="button" @click="dctReviewPrintPopupToggleFunc()">검토용인쇄</v-btn>
          <v-btn class="button" @click="writeDraftDoc()">기안문조회</v-btn>
          <v-btn class="button" @click="dctReaderPopupToggleFunc()">담당변경</v-btn>
          <v-btn class="button" @click="moveToBmsReprocesslist()">닫기</v-btn>
        </div>
      </div>
      <!-- {{insertCreateCardData}} -->
    </section>

    <!-- 비밀속성 -->
    <section class="section">    
      <div class="box-title">
        <h6 class="h6">비밀속성</h6>
      </div>
      <v-table class="table-type-03">
        <colgroup>
          <col width="120">
          <col width="">
          <col width="120">
          <col width="">
          <col width="120">
          <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th><span class="required">비밀등급</span></th>
            <td>
              <v-select
                v-model="receivedData.seclevel"
                label=""
                :items="secLevelData"
                item-title="secLevelDataKey"
                item-value="secLevelDataValue"
                :readonly="true"
                variant="outlined"
                hide-details="auto"></v-select>
            </td>
            <th>관리번호</th>
            <td>
              <v-text-field
                v-model="receivedData.mgmtno"
                :readonly="true"
                variant="outlined"
                hide-details="auto"></v-text-field>
            </td>
            <th>사본번호</th>
            <td>
              <v-text-field
                v-model="receivedData.copyno"
                :readonly="true"
                variant="outlined"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
          <tr>
            <th><span class="required">예고문</span></th>
            <td colspan="5">
              <!-- 예고문 - 사본보호기간 -->
              <div class="box-flex">
                <v-label>사본보호기간:</v-label>
                <v-label>{{copyDate.year}}년</v-label>
                <v-label>{{copyDate.month}}월</v-label>
                <v-label>{{copyDate.day}}일</v-label>
                <v-label>{{secLevelDataDisplayValue}}(으)로</v-label>
                <v-label>{{copyReclassDataDisplayValue}}</v-label>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </section>

    <!-- 표제부 -->
    <section class="section">
      <div class="box-title">
        <h6 class="h6">표제부</h6>
      </div>
      <v-table class="table-type-03">
        <colgroup>
          <col width="120">
          <col width="">
          <col width="120">
          <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th><span class="required">제목</span></th>
            <td>
              <v-text-field
                v-model="receivedData.docttl"
                label=""
                :readonly="true"
                variant="outlined"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
          <tr>
            <th>관련근거</th>
            <td>
              <div v-for="refData, idx in insertCreateCardData.refList" :key="idx">
                <div class="box-flex">
                  <span>{{ refData.gubunTemp }}</span>
                  <v-text-field
                    v-model="refData.infotitle"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto">
                  </v-text-field>
                </div>
                <div v-if="refData.sfilename!= undefined && refData.sfilename != ''">
                  <ul class="attach-list ml-16">
                    <li>
                      <span class="branch-down"></span>
                      <v-icon v-if="getExtension(refData.sfilename) == 'pdf'" color="#b30c00">mdi-file-pdf-box</v-icon>
                      <v-icon v-if="getExtension(refData.sfilename) == 'hwp' || getExtension(refData.sfilename) == 'hwpx'" color="#35aee7">mdi-syllabary-hangul</v-icon>
                      <v-icon v-if="getExtension(refData.sfilename) == 'xls' || getExtension(refData.sfilename) == 'xlsx'" color="#1f7246">mdi-file-excel-box</v-icon>
                      <v-icon v-if="getExtension(refData.sfilename) == 'ppt'" color="#b74829">mdi-file-powerpoint-box</v-icon>
                      <v-icon v-if="getExtension(refData.sfilename) == 'doc'" color="#1079bf">mdi-file-word-box</v-icon>
                      <span>{{ refData.sfilename }}</span>
                    </li>
                  </ul>
                </div>
                <div v-else-if="refData.file != undefined && refData.file != ''">
                  <ul class="attach-list ml-16">
                    <li>
                      <span class="branch-down"></span>
                      <v-icon v-if="getExtension(refData.file.name) == 'pdf'" color="#b30c00">mdi-file-pdf-box</v-icon>
                      <v-icon v-if="getExtension(refData.file.name) == 'hwp' || getExtension(refData.file.name) == 'hwpx'" color="#35aee7">mdi-syllabary-hangul</v-icon>
                      <v-icon v-if="getExtension(refData.file.name) == 'xls' || getExtension(refData.file.name) == 'xlsx'" color="#1f7246">mdi-file-excel-box</v-icon>
                      <v-icon v-if="getExtension(refData.file.name) == 'ppt'" color="#b74829">mdi-file-powerpoint-box</v-icon>
                      <v-icon v-if="getExtension(refData.file.name) == 'doc'" color="#1079bf">mdi-file-word-box</v-icon>
                      <span>{{ refData.file.name }}</span>
                    </li>
                  </ul>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th>문서취지</th>
            <td>
              <v-text-field
                v-model="receivedData.summary"
                label=""
                :readonly="true"
                variant="outlined"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
          <tr>
            <th>기안문</th>
            <td colspan="3">
              <v-text-field
                v-model="draftDocInputData"
                :readonly="true"
                variant="solo"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
          <tr>
            <th>붙임</th>
            <td colspan="3">
              <div v-if="insertCreateCardData.fileList != []">
                <div class="box-flex" v-for="(fileData,idx) in insertCreateCardData.fileList" :key="idx">
                  <v-text-field
                    v-model="fileData.sfilename"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto">
                  </v-text-field>
                  <v-btn class="btn" disabled @click="delExistingAttach(idx)">삭제</v-btn>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </section>


    <!-- 담당정보 -->
    <section class="section">
      <div class="box-title">
        <h6 class="h6">담당정보</h6>
        <div class="action-buttons">
          <v-btn v-if="!receivedData.chargerid" class="button" @click="dctChargerPopupToggleFunc()">담당지정</v-btn>
        </div>
      </div>
      <v-table class="table-type-02"
        :no-data-text="noDataText">
        <thead>
          <tr>
            <th>구분</th>
            <th>부서</th>
            <th>직위/성명</th>
            <th>의견/지시</th>
            <th>서명</th>
            <th>처리결과</th>
            <th>이력</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="(!insertCreateCardData.chargerList || insertCreateCardData.chargerList.length === 0)">
            <td :colspan="7" text @click="dctChargerPopupToggleFunc" class="temp-cursor-pointer">{{ noDataText.noChargerText }}</td>
          </tr>
            
        </tbody>
      </v-table>
    </section>


    <!-- 시행정보 -->
    <section class="section">
      <div class="box-title">
        <h6 class="h6">시행정보</h6>
      </div>
      <v-table class="table-type-03">
        <colgroup>
          <col width="120">
          <col width="">
          <col width="120">
          <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th>발신기관명</th>
            <td>
              <v-select
                class="w100"
                v-model="sendOrgNameData"
                :readonly="true"
                variant="solo"
                hide-details="auto"
                ></v-select>
            </td>
            <th>발신명의</th>
            <td>
              <v-select
                class="w100"
                v-model="sendNameData"
                :readonly="true"
                variant="solo"
                hide-details="auto"
                ></v-select>
            </td>
          </tr>
          <tr>
            <th>생산문서번호</th>
            <td>
              <v-text-field
                v-model="sendOrgNameData"
                :readonly="true"
                variant="solo"
                hide-details="auto">
              </v-text-field>
            </td>
            <th>접수번호</th>
            <td>
              <v-text-field
                v-model="sendOrgNameData"
                :readonly="true"
                variant="solo"
                hide-details="auto">
              </v-text-field>
            </td>
          </tr>
          <tr>
            <th>생산기관경로</th>
            <td colspan="3">
              <v-text-field
                v-model="displayPathList"
                :readonly="true"
                variant="solo"
                hide-details="auto">
              </v-text-field>
            </td>
          </tr>
          <tr>
            <th>수신자</th>
            <td colspan="3">
              <v-text-field
                v-model="formatRecvList"
                :readonly="true"
                variant="solo"
                hide-details="auto">
              </v-text-field>
            </td>
          </tr>
          <tr>
            <th>배포처</th>
            <td colspan="3">
              <v-text-field
                v-model="sendOrgNameData"
                :readonly="true"
                variant="solo"
                hide-details="auto">
              </v-text-field>
            </td>
          </tr>
        </tbody>
      </v-table>
    </section>

    <!-- 관리정보 -->
    <section class="section">
      <div class="box-title">
        <h6 class="h6">관리정보</h6>
      </div>
      <v-table class="table-type-03">
        <colgroup>
          <col width="120">
          <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th>문서번호</th>
            <td>
              <v-text-field
                v-model="docNameInputData"
                :readonly="true"
                variant="solo"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
          <tr>
            <th>열람권자</th>
            <td>
              <v-text-field 
                :readonly="true"
                v-model="displayRcvsList"
                variant="solo"
                hide-details="auto"></v-text-field>
            </td>
          </tr>
        </tbody>
      </v-table>
    </section>
  </div>
  
  <!-- 검토용인쇄 팝업 -->
  <v-dialog v-model="bVisibleDctReviewPrintPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>검토용 인쇄 DctReviewPrintPopup</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctReviewPrintPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctReviewPrintPopup
        v-if="bVisibleDctReviewPrintPopup"
        :args="dctReviewPrintPopupArgs"
        :toggleFunc="dctReviewPrintPopupToggleFunc"
        :returnFunc="dctReviewPrintPopupReturnFunc"
      ></DctReviewPrintPopup>
    </v-card>
  </v-dialog>

  <!-- 문서처리 팝업 -->
  <v-dialog v-model="bVisibleDocumentProcPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>문서처리 DocumentProcPopup</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDocumentProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DocumentProcPopup
        v-if="bVisibleDocumentProcPopup"
        :args="documentProcPopupArgs"
        :toggleFunc="documentProcPopupToggleFunc"
        :returnFunc="documentProcPopupReturnFunc"
      ></DocumentProcPopup>
    </v-card>
  </v-dialog>

  <!-- 담당변경 팝업 -->
  <v-dialog v-model="bVisibleDctChargerPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀담당자 변경</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctChargerPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctChargerPopup
        v-if="bVisibleDctChargerPopup"
        :args="dctChargerPopupArgs"
        :toggleFunc="dctChargerPopupToggleFunc"
        :returnFunc="dctChargerPopupReturnFunc"
      ></DctChargerPopup>
    </v-card>
  </v-dialog>

  <!-- 결재선지정 팝업 -->
  <v-dialog v-model="bVisibleDctPathPopup" style="width: 850px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctPathPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctPathPopup
        v-if="bVisibleDctPathPopup"
        :args="dctPathPopupArgs"
        :toggleFunc="dctPathPopupToggleFunc"
        :returnFunc="dctPathPopupReturnFunc"
      ></DctPathPopup>
    </v-card>
  </v-dialog>

  <!-- 로고/심볼 팝업 -->
  <v-dialog v-model="bVisibleDctSymbolPopup" style="width: 1000px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>로고/심볼 지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctSymbolPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctSymbolPopup
        v-if="bVisibleDctSymbolPopup"
        :args="dctSymbolPopupArgs"
        :toggleFunc="dctSymbolPopupToggleFunc"
        :returnFunc="dctSymbolPopupReturnFunc"
      ></DctSymbolPopup>
    </v-card>
  </v-dialog>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import DctPathPopup from "./DctPathPopup.vue";
import DctSymbolPopup from "./DctSymbolPopup.vue";
import DctReviewPrintPopup from "./DctReviewPrintPopup.vue";
import DocumentProcPopup from "./DocumentProcPopup.vue";
import DctChargerPopup from "./DctChargerPopup.vue";
import { API } from "@/api";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";

import dayjs from 'dayjs';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { transformStatus,transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"
const name = ref('BmsDctCreateCardTemp')

const props = defineProps({
  data: Object
  // data: String,
  // args: Object,
  // toggleFunc: Function,
  // returnFunc: Function,
});
const data = ref({});
const receivedData = ref({});
const args = ref({});
const docidData = ref({docid: ""});

const noDataText = ref({
  type: String,
  noPathText: '데이터가 없습니다. 경로를 지정해주세요.',
  noChargerText: '데이터가 없습니다. 담당자를 지정해주세요.'
});

/* ======================== 검토용인쇄 팝업 ======================== */
const bVisibleDctReviewPrintPopup = ref(false)
const dctReviewPrintPopupReuturn = ref({})
const dctReviewPrintPopupArgs = ref({})
const dctReviewPrintPopupToggleFunc = ref(() => { 
  // dctReviewPrintPopupArgs.value = receivedData;
  dctReviewPrintPopupArgs.value = {
    ...insertCreateCardData.value,
    ...receivedData.value,
  };
  bVisibleDctReviewPrintPopup.value = !bVisibleDctReviewPrintPopup.value; 
  togglePopup('DctReviewPrintPopup'); 
})
const dctReviewPrintPopupReturnFunc = ref((retValue) => {
  dctReviewPrintPopupToggleFunc.value()
  dctReviewPrintPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 문서처리 팝업 ======================== */
const bVisibleDocumentProcPopup = ref(false)
const documentProcPopupReuturn = ref({})
const documentProcPopupArgs = ref({});
const documentProcPopupToggleFunc = ref(() => { 
  documentProcPopupArgs.value = {
    ...insertCreateCardData.value,
    ...receivedData.value,
  }
  // documentProcPopupArgs.value = receivedData;
  bVisibleDocumentProcPopup.value = !bVisibleDocumentProcPopup.value; 
  togglePopup('DocumentProcPopup'); 
})
const documentProcPopupReturnFunc = ref((retValue) => {
  documentProcPopupToggleFunc.value()
  documentProcPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 담당변경 팝업 ======================== */
const bVisibleDctChargerPopup = ref(false)
const dctChargerPopupReuturn = ref({})
const dctChargerPopupArgs = ref({})
const dctChargerPopupToggleFunc = ref(() => { 
  const chargerList = [...insertCreateCardData.value.chargerList];
  // const modifiedChargerList = chargerList.slice(1).map(item => {
  const modifiedChargerList = chargerList.map(item => {
    return {
      orgid: item.deptid,
      userid: item.objid,
      username: item.actername,
      orgname: item.deptname,
      grade: item.dutyname,
      askTemp: item.askTemp,
      asktype: item.asktype,
      identifier: `${item.deptid}-${item.objid}`
    };
  });
  dctChargerPopupArgs.value = modifiedChargerList;
  bVisibleDctChargerPopup.value = !bVisibleDctChargerPopup.value;
})
const dctChargerPopupReturnFunc = ref((retValue) => {
  dctChargerPopupToggleFunc.value()
  if(retValue != ""){
    // insertInitialCharger();
    insertCreateCardData.value.chargerList = [];
    for(let i = 0; i < retValue.length; i++){
      const charger = {};
      charger.askTemp = retValue[i].askTemp;
      charger.asktype = retValue[i].asktype;
      charger.objid = retValue[i].userid;
      charger.actername = retValue[i].username;
      charger.deptid = retValue[i].orgid;
      charger.deptname = retValue[i].orgname;
      charger.dutyname = retValue[i].grade;
      charger.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      charger.mainver = 1;
      charger.chargerorder = i+1;
      charger.chargerstate = 'DCST1';
      insertCreateCardData.value.chargerList.push(charger);
      // receivedData.value.chargerList.push(charger);
    };
    // updateChargerOrder();
  }else{
    // insertInitialCharger();
  }
});

const insertInitialCharger = () => {
  insertCreateCardData.value.chargerList = [];
  const initialCharger = {};
  initialCharger.askTemp = "기안";
  initialCharger.asktype = "DEA11";
  initialCharger.acttype = "DEA41";
  initialCharger.objid = getUserLoginData.value.userid;
  initialCharger.objname = getUserLoginData.value.username;
  initialCharger.deptid = getUserLoginData.value.deptid;
  initialCharger.deptname = getUserLoginData.value.deptname;
  initialCharger.dutyname = getUserLoginData.value.grade;
  initialCharger.dutynamedesc = getUserLoginData.value.grade+"/"+getUserLoginData.value.username;
  initialCharger.mainver = "1";
  initialCharger.chargerorder = "1";
  initialCharger.chargerstate = 'DCST1';
  insertCreateCardData.value.chargerList.push(initialCharger);
};

const sortedChargerList = computed(() => {
  return insertCreateCardData.value.chargerList.sort((a, b) => {
    return a.chargerorder - b.chargerorder;
  });
});

const updateChargerOrder = () => {
  insertCreateCardData.value.chargerList.forEach((charger, index) => {
      charger.chargerorder = index + 1;
  });
};
/* ============================================================ */

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref(() => { 
  insertCreateCardData.value.pathList.splice(0); // 기본설정(초기값)데이터 제거
  bVisibleDctPathPopup.value = !bVisibleDctPathPopup.value;
  
})
const dctPathPopupReturnFunc = ref((retValue) => {
  dctPathPopupToggleFunc.value()
  dctPathPopupReuturn.value = retValue;
  for(let idx = 0; idx < retValue.length; idx++){
    insertCreateCardData.value.pathList.push({
    askTemp: retValue[idx].askTemp,
    asktype: retValue[idx].asktype,
    deptname: retValue[idx].orgname,
    dutyname: retValue[idx].grade,
    dutynamedesc: retValue[idx].grade + "/" + retValue[idx].username,
    pathorder: idx+1,
    pathstate: 'DCST1', // 처리대기 코드
    })
  }
})
/* ============================================================ */

/* ======================== 담당변경 팝업 ======================== */
const bVisibleDctRecvPopup = ref(false)
const dctRecvPopupReuturn = ref({})
const dctRecvPopupArgs = ref({})
const dctRecvPopupToggleFunc = ref(() => { 
  bVisibleDctRecvPopup.value = !bVisibleDctRecvPopup.value;
  receiver.value = "";
})
const receiver = ref("")
const dctRecvPopupReturnFunc = ref((retValue) => {
  dctRecvPopupToggleFunc.value()
  dctRecvPopupReuturn.value = retValue;
  insertCreateCardData.value.apprrecvdeptname = "";
  for(let idx = 0; idx < retValue.length; idx++){
    receiver.value += (retValue[idx].orgname + "("+ retValue[idx].chamjo +")")
    if (idx < retValue.length - 1) {
      receiver.value += ', ';
    }
  }
  insertCreateCardData.value.apprrecvdeptname = receiver.value;
})
/* ============================================================ */

/* ======================== 열람지정 팝업 ======================== */
const bVisibleDctReaderPopup = ref(false)
const dctReaderPopupReuturn = ref({})
const dctReaderPopupArgs = ref({})
const dctReaderPopupToggleFunc = ref(() => { 
  const rcvsList = [...insertCreateCardData.value.rcvsList];
  const modifiedRcvsList = rcvsList.slice(1).map(item => {
    return {
      orgid: item.rcvdeptid,
      userid: item.rcvsuserid,
      username: item.rcvsusername,
      orgname: item.rcvdeptname,
      grade: item.rcvdutyname,
      identifier: `${item.rcvdeptid}-${item.rcvsuserid}`
    };
  });
  dctReaderPopupArgs.value.rcvs = modifiedRcvsList;
  dctReaderPopupArgs.value.myid = getUserLoginData.value.userid;
  bVisibleDctReaderPopup.value = !bVisibleDctReaderPopup.value;
  reader.value = "";
})
const reader = ref("");
const dctReaderPopupReturnFunc = ref((retValue) => {
  dctReaderPopupToggleFunc.value()
  if(retValue != ""){
    rcvsMe();
    for(let i = 0; i < retValue.length; i++){
      const rcvs = {};
      rcvs.rcvsuserid = retValue[i].userid;
      rcvs.rcvsusername = retValue[i].username;
      rcvs.rcvsinseq = i+1;
      rcvs.rcvdeptid = retValue[i].orgid;
      rcvs.rcvdeptname = retValue[i].orgname;
      rcvs.rcvdutyname = retValue[i].grade;
      insertCreateCardData.value.rcvsList.push(rcvs);
    }
  }else{
    rcvsMe();
  }
})

const rcvsMe = () => {
  insertCreateCardData.value.rcvsList = [];
  const initialRcvs = {};
  initialRcvs.rcvsuserid = getUserLoginData.value.userid;
  initialRcvs.rcvsusername = getUserLoginData.value.username;
  initialRcvs.rcvsinseq = 0;
  initialRcvs.rcvdeptid = getUserLoginData.value.deptid;
  initialRcvs.rcvdeptname = getUserLoginData.value.deptname;
  initialRcvs.rcvdutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.rcvsList.push(initialRcvs);
}
/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');

const today = ref("")
const insertCreateCardData = ref({

// 1) bms_dct_rdoc table
docttl: "", // 비밀명
gubun: "", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
state: "", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
emergency: "", // 긴급도 (대내: N, 대외: Y, 대내외: T)
keyword: "", // 키워드
authorid: "", // 작성자 ID
authorname: "", // 담당자명
authordutyname: "", // 담당자 직위직급
authordeptname: "", // 담당자 부서이름
tempflag: "", // 임시 flag (임시: Y, 처리요청: N)

// 2) bms_dct_summary table
docgubun: "", // 비밀구분 (발송: A, 시행: E)
summary: "", // 요약

// 3) bms_dct_attr table
secgubun: "", // 비밀구분 (일반행정: 00, 군사: 01, 군사훈련: 02, 전시: 03, 한미연합: 04, 한미연합연습: 05, 전시(행정기관): 06)
seclevel: "", // 비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
prsrvterm: "", // 보존기간 (1년: 001, 3년: 003, 5년: 005, 10년: 010, 30년: 030, 준영구: 040, 영구: 050)
orgnprotdt: "", // 원본보호기간
orgnnextlevel: "", // 원본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
orgnreclass: "", // 원본재분류 (재분류: 21)
copyprotdt: "", // 사본보호기간
copynextlevel: "", // 사본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4)
copyreclass: "", // 사본재분류 (재분류: 21. 파기: 23, 존안: 20)

// 4) bms_dct_path table
pathList:[
  {
    askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
    asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
    deptname: "", // 부서이름
    dutyname: "", // 직위직급이름
    dutynamedesc: "", // 직위직급이름 상세
    electsignid: "", // 전자서명ID
    reportdt: "", // 보고일자
    mainver: "", // 메인버전
    pathorder: "", //  경로순서
    pathstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
    opinion: "",
  }
],

// 5) bms_dct_opinion table (문서처리시에만. 임시저장에는X)
opinionpathorder: "", //  경로순서 (실제로는 pathorder 컬럼)
opinion: "",  // 의견

// 6) bms_dct_appr_recv table
recvList:[
  {
    receiverid: "",    // 수신처ID
    apprrecvtempflag: "", // 임시FLAG (실제로는 tempflag 컬럼)
    apprrecvseq: "", // 일련번호 (실제로는 seq 컬럼)
    apprrecvgubun: "",  // 구분 (실제로는 gubun 컬럼)
    apprrecvdeptname: "",  // 부서이름 (실제로는 deptname 컬럼)
    apprrecvrefdeptname: "",  // 참조부서이름 (실제로는 refdeptname 컬럼)
  }
],

// 7) bms_dct_rdoc_rcvs table
rcvsList:[
  {
    rcvsuserid: "",    // 사용자ID (실제로는 userid 컬럼)
    rcvsusername: "",      // 사용자이름 (실제로는 username 컬럼)
    rcvdeptid: "",      // 부서이름 (실제로는 deptid 컬럼)
    rcvdeptname: "",      // 부서이름 (실제로는 deptname 컬럼)
    rcvdutyname: "",      // 직위직급이름 (실제로는 dutyname 컬럼)
    rcvsinseq: "",      // 등록일련번호 (실제로는 inseq 컬럼)
  }
],

// 8) bms_task_infomation_temp table (문서처리 일때는 bms_task_infomation table)
refList: [
  {
    infomationid: "", // 정보출처ID
    resultid: "", // 실적ID
    infotext: "", // 정보출처제목 (비밀문서는 docid, 직접입력은 입력받은 근거)
    infotitle: "", // 정보출처제목 (비밀문서는 docttl, 직접입력은 입력받은 근거)
    infotype: "", // 정보출처유형 (비밀문서는 T, 직접입력은 P)
  }
],

// 9) bms_dct_hist_card table (문서처리시에만. 임시저장에는X)
apprreqid: "", // 결재요청 ID
histgubun: "", // 이력구분 (기안: 01)
deptid: "", // 소속코드
deptname: "", // 소속명
dutyname: "", // 직위직급명
username: "", // 사용자명
histstatus: "", // 이력상태 (결재 요청: 010)
etc: "", // 기타
electsignid: "", // 전자서명 ID

// 10) bms_dct_file table
fileList: [
  {
    fleid: "", // 첨부파일 ID
    sfilename: "", // 파일고유이름
    gubun: "", // 구분
    flettl: "", // 첨부명
    flesize: "", // 첨부크기
    fleorder: "", // 첨부순서
    isviewtype: "", // 비전자 첨부구분
    fleurl: "", // 첨부 url
    flepath: "", // 첨부 경로
  }
],

// 공통
resdocid: "",
resreceiverid: "",
resuserid: "",
});

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted( async () => {
  insertCreateCardData.value.authorid = getUserLoginData.value.userid;
  insertCreateCardData.value.authorname = getUserLoginData.value.username;
  insertCreateCardData.value.authordutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.authordeptname = getUserLoginData.value.deptname;

  insertCreateCardData.value.deptid = getUserLoginData.value.deptid;
  insertCreateCardData.value.deptname = getUserLoginData.value.deptname;
  insertCreateCardData.value.dutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.username = getUserLoginData.value.username;

  insertCreateCardData.value.resuserid = getUserLoginData.value.userid;
  sendOrgNameData.value = getUserLoginData.value.deptname;
  sendNameData.value = getUserLoginData.value.deptname+' 부서장';
  today.value = dayjs().format('YYYY-MM-DD');
  makeIds();
})
watch(route, async (route) => {
  receivedData.value = props.data;
});
// watchEffect(() => {
// })
watchEffect(async() => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
  if(props.data.docid && props.data.docid.value !== ""){
  await getCopyDate();
  await selectDctPath();
  await getDctRecv();
  await selectDctReader();
  await selectTaskInfo();
  await selectDctFile();
  }
})



// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: 'II급', secLevelDataValue: '2'},
  { secLevelDataKey: 'III급', secLevelDataValue: '3'},
  { secLevelDataKey: '대외비', secLevelDataValue: '4'},
  { secLevelDataKey: '일반', secLevelDataValue: '5'},
];

// 보존기간 Select-Box
const prsRvTermData = [
  {prsRvTermDataKey: '1년', prsRvTermDataValue: '001'},
  {prsRvTermDataKey: '3년', prsRvTermDataValue: '003'},
  {prsRvTermDataKey: '5년', prsRvTermDataValue: '005'},
  {prsRvTermDataKey: '10년', prsRvTermDataValue: '010'},
  {prsRvTermDataKey: '30년', prsRvTermDataValue: '030'},
  {prsRvTermDataKey: '준영구', prsRvTermDataValue: '040'},
  {prsRvTermDataKey: '영구', prsRvTermDataValue: '050'},
];


/* ======================== 원본보호기간 ======================== */
const orgnProtDtSelected = ref("날짜지정");
const orgnProtDtData = ["날짜지정", "직접입력"];

const orgnProtDtPeriodSelected = ref("");
const orgnProtDtPeriodData = [
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

// 날짜 지정
const orgnDate = ref({
  year: "",
  month: "",
  day: "",
})
const getOrgnDate = () => {
  const currentDate = new Date();
  const currentYear = currentDate.getFullYear();
  orgnDate.value.year = currentYear + parseInt(orgnProtDtPeriodSelected.value);
  orgnDate.value.month = '12';
  orgnDate.value.day = '31';
  insertCreateCardData.value.orgnprotdt = orgnDate.value.year+orgnDate.value.month+orgnDate.value.day;
}

// 직접엽력
const orgnUserDate = ref("");
const getOrgnUserDate = () => {
  insertCreateCardData.value.orgnprotdt = orgnUserDate.value.replace(/-/g, '');
}

const orgnReclassSelected = ref("");
const orgnReclass = ['선택','일반문서'];
const orgnReclassData = [
  { key: '선택', value: '0'},
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'}, // 임시 설정
];

const selectedOrgnReClass = (key) => {
  if(key == '21'){
    orgnReclassSelected.value = "일반문서"
  }else{
    orgnReclassSelected.value = "선택"
  }
}

/* ============================================================ */

/* ======================== 사본보호기간 ======================== */
const copyProtDtSelected = ref("날짜지정");
const copyProtDtData = ["날짜지정", "직접입력"];

const copyProtDtPeriodSelected = ref("");
const copyProtDtPeriodData = [
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

// 날짜지정
const copyDate = ref({
  year: "",
  month: "",
  day: "",
})

const getCopyDate = async () => {
  if(props.data.copyprotdt){
    const copyDatetotal = props.data.copyprotdt;
    copyDate.value.year = copyDatetotal.slice(0,4);
    copyDate.value.month = copyDatetotal.slice(4,6);
    copyDate.value.day = copyDatetotal.slice(6,8);
    insertCreateCardData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
  }
}

// 직접엽력
const copyUserDate = ref("");
const getCopyUserDate = () => {
  insertCreateCardData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
}

const copyReclassSelected = ref("");
const copyReclass = ['선택','일반문서'];
const copyReclassData = [
  { key: '선택', value: '0'},
  { key: '존안', value: '20'},
  { key: '재분류', value: '21'},
  { key: '파기', value: '23'},
];

const selectedCopyReClass = (key) => {
  if(key == '21'){
    copyReclassSelected.value = "일반문서"
  }else{
    copyReclassSelected.value = "선택"
  }
}
/* ============================================================ */


// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];


// 표제부 기안문 Text-Field
const draftDocInputData = ref("2023년도 확정 예산안 계획보고.hwp");

// 표제부 붙임 Text-Field
const attachFileInputData = ref("규정지침.xlsx");

// 시행정보 발신기관명 Select-Box
const sendOrgNameData = ["한국원자력통제기술원"];

// 시행정보 발신명의 Select-Box
const sendNameData = ["한국원자력통제기술원 장"];

// 관리정보 문서번호 Text-Field
const docNameInputData = ref("");

// 기안문 조회 버튼 (임시)
const writeDraftDoc = ref(() => {
  alert('기안문조회 Popup');
});


const makeAlert = () => {
  if (insertCreateCardData.value.seclevel == "") {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if(insertCreateCardData.value.prsrvterm == ''){
    alert('예고문 보호기간을 선택해주세요.');
    return false;
  }
  if(orgnProtDtPeriodSelected.value == ''){
    alert('원본 보호기간을 선택해주세요.');
    return false;
  }
  if(insertCreateCardData.value.orgnreclass == ''){
    alert('원본 보호기간을 조건을 선택해주세요.');
    return false;
  }
  if(copyProtDtPeriodSelected.value == ''){
    alert('사본 보호기간을 선택해주세요.');
    return false;
  }
  if(insertCreateCardData.value.copyreclass == ''){
    alert('사본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)){
    alert('예고문 보호기간은 원본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)){
    alert('예고문 보호기간은 사본 보호기간보다 작을 수 없습니다.');
    return false;
  }

  if(parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    return false;
  }

  if (insertCreateCardData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
};


const makeIds = () => {
  const now = dayjs();
  const formattedDate = now.format('YYYYMMDDHHmmss');
  insertCreateCardData.value.resdocid = "DCT"+formattedDate;
  insertCreateCardData.value.resreceiverid = "RECV"+formattedDate;
  insertCreateCardData.value.resuserid = "USER"+formattedDate;
}


  // 4) bms_dct_path table
 const displayPathList = ref("");
 const selectDctPath  = async () => {
  try {
    const response = await API.dctAPI.selectDctPath ({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.pathList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.askTemp = transformAsktype(response.data[i].asktype);
      upd.asktype = response.data[i].asktype;
      upd.deptname = response.data[i].deptname;
      upd.dutyname = response.data[i].dutyname;
      upd.dutynamedesc = response.data[i].dutynamedesc;
      upd.electsignid = response.data[i].electsignid;
      upd.reportdt = response.data[i].reportdt;
      if(upd.reportdt){
        upd.transformedReportdt = transformDate(response.data[i].reportdt);
      }
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.pathstate = response.data[i].state;
      insertCreateCardData.value.pathList.push(upd);
    }
    displayPathList.value = formaPathList.value; 
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
const formaPathList = computed(() => {
  const formattedPathList = insertCreateCardData.value.pathList.map((item) => {
    return `${item.dutynamedesc}(${item.askTemp} ${item.transformedReportdt})`;
  });
  return formattedPathList.join(', ');
});


 // 6) bms_dct_appr_recv
 const getDctRecv = async () => {
  try {
    const response = await API.dctAPI.getDctRecv({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.recvList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].tempflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvgubun = response.data[i].gubun;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      insertCreateCardData.value.recvList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
const formatRecvList = computed(() => {
  const formattedRecvList = insertCreateCardData.value.recvList.map((item) => {
    const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
    return `${item.apprrecvdeptname}${refDeptName}`;
  });
  return formattedRecvList.join(', ');
});


 // 7) bms_dct_rdoc_rcvs
 const selectDctReader = async () => {
  try {
    const response = await API.dctAPI.selectDctReader({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.rcvsList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsdeptnamedesc = response.data[i].deptnamedesc;
      upd.rcvsinseq = response.data[i].inseq;
      insertCreateCardData.value.rcvsList.push(upd);
    }
    displayRcvsList.value = formatRcvsList.value; 
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const formatRcvsList = computed(() => {
  const formattedRcvsList = insertCreateCardData.value.rcvsList.map((item) => {
    return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
  });
  return formattedRcvsList.join(', ');
});

// 8) bms_task_infomation table
const selectTaskInfo  = async () => {
  try {
    const response = await API.dctAPI.selectTaskInfo ({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.refList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.infomationid = response.data[i].infomationid;
      upd.resultid = response.data[i].resultid;
      upd.infotext = response.data[i].infotext;
      upd.infotitle = response.data[i].infotitle;
      upd.infotype = response.data[i].infotype;
      upd.sfilename = response.data[i].sfilename;
      if(response.data[i].infotype == 'P'){
        upd.gubunTemp = '직접입력'
      }else if(response.data[i].infotype == 'T'){
        upd.gubunTemp = '비밀문서'
      }
      insertCreateCardData.value.refList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 10) bms_dct_file table
const selectDctFile = async () => {
  try {
    const response = await API.dctAPI.selectDctFile ({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.fileList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.fleid = response.data[i].fleid;
      upd.sfilename = response.data[i].sfilename;
      upd.gubun = response.data[i].gubun;
      upd.flettl = response.data[i].flettl;
      upd.flesize = response.data[i].flesize;
      upd.fleorder = response.data[i].fleorder;
      upd.isviewtype = response.data[i].isviewtype;
      upd.fleurl = response.data[i].fleurl;
      upd.flepath = response.data[i].flepath;
      insertCreateCardData.value.fileList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 취소 버튼 Move FN (재지정함 페이지로 이동)
const moveToBmsReprocesslist = () => {
  router.push({
    name: "BmsReprocesslist",
  });
};

</script>