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
      <h4 class="h4">비밀관리기록부</h4>
    </div>

    <div class="box-content">
      <!-- 비전자 비밀등록 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">생산 비전자비밀 등록</h5>
          <div class="action-buttons">
            <v-btn class="button" @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn class="button" @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
            <v-btn class="button" @click="dctOfflineProcPopupToggleFunc()">등록</v-btn>
            <v-btn class="button" @click="moveToBmsTodolist()">닫기</v-btn>
          </div>
        </div>
      </section>

      <div>
        <!-- 비밀속성 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">비밀속성</h6>
          </div>
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="620">
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
                    class="w100"
                    v-model="insertOfflineData.seclevel"
                    @update:modelValue="updateMgmtno"
                    :items="secLevelData"
                    item-title="secLevelDataKey"
                    item-value="secLevelDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th><span class="required">사본번호</span></th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="insertOfflineData.copynostart"
                      readonly="true"
                      maxlength="3"
                      variant="outlined"
                      hide-details="auto"></v-text-field>
                      /
                      <v-text-field
                      v-model="insertOfflineData.copynoend"
                      readonly="true"
                      maxlength="3"
                      variant="outlined"
                      hide-details="auto"></v-text-field>
                  </div>
                </td>
                <th><span class="required">관리번호</span></th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="insertOfflineData.mgmtnoStart"
                      readonly="true"
                      variant="outlined"
                      hide-details="auto"></v-text-field>
                      -
                      <v-text-field
                      v-model="insertOfflineData.mgmtnoCenter"
                      @update:modelValue="checkMgmtno(insertOfflineData.mgmtnoCenter, 'mgmtnoCenter')"
                      :rules="[rules.mgmtnoCenter]"
                      maxlength="4"
                      variant="outlined"
                      hide-details="auto"></v-text-field>
                      -
                      <v-text-field
                      v-model="insertOfflineData.mgmtnoEnd"
                      @update:modelValue="checkMgmtno(insertOfflineData.mgmtnoEnd, 'mgmtnoEnd')"
                      :rules="[rules.mgmtnoEnd]"
                      variant="outlined"
                      hide-details="auto"></v-text-field>
                  </div>
                </td>
              </tr>
              <tr>
                <th rowspan="3"><span class="required">예고문</span></th>
                <td colspan="7">
                  <!-- 예고문 - 보존기간 -->
                  <div class="box-flex">
                    <v-label>보존기간:</v-label>
                    <v-select
                      v-model="insertOfflineData.prsrvterm"
                      @update:modelValue="updatePrsrvterm"
                      :items="prsRvTermData"
                      item-title="prsRvTermDataKey"
                      item-value="prsRvTermDataValue"
                      required
                      variant="outlined"
                      hide-details="auto"></v-select>
                  </div>
                </td>
              </tr>
              <tr>
                <td colspan="7">
                  <!-- 예고문 - 원본보호기간 -->
                  <div class="box-flex">
                    <v-label>원본보호기간:</v-label>
                    <v-select
                      v-model="orgnProtDtSelected"
                      @update:modelValue="updateOrgnDt"
                      :items="orgnProtDtData"
                      variant="outlined"
                      hide-details="auto"></v-select>
                    <v-text-field
                      v-if="orgnProtDtSelected == '직접입력'"
                      v-model="orgnUserDate"
                      type="date"
                      :min="today"
                      @update:modelValue="getOrgnUserDate"
                      variant="outlined"
                      hide-details="auto">
                    </v-text-field>
                  
                    <template v-else>
                      <v-select
                        v-model="orgnProtDtPeriodSelected"
                        :items="orgnProtDtPeriodData"
                        @update:modelValue="getOrgnDate"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto">
                      </v-select>
                      <v-text-field
                        v-model="orgnDate.year"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>년</v-label>
                      <v-text-field
                        v-model="orgnDate.month"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>월</v-label>
                      <v-text-field
                        v-model="orgnDate.day"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>일</v-label>
                    </template>
                      <v-select
                        v-model="orgnReclassSelected"
                        :items="orgnReclass"
                        variant="outlined"
                        :readonly="true"
                        hide-details="auto">
                      </v-select>
                      <v-label>(으)로</v-label>
                      <v-select
                        v-model="insertOfflineData.orgnreclass"
                        :items="orgnReclassData"
                        @update:modelValue="selectedOrgnReClass"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto">
                      </v-select>
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
              <col width="620">
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
                    v-model="insertOfflineData.docttl"
                    :rules="[rules.required, rules.counter]"
                    maxlength="500"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="insertOfflineData.emergency"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-model="insertOfflineData.keyword"
                    variant="outlined"
                    :rules="[rules.counter]"
                    maxlength="500"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="5">
                  <FilePond
                    ref="pondRef"
                    label-idle="Drop files here or Browse"
                    allow-multiple="true"
                    v-on:addfile="handleFileAdd"
                    v-on:removefile="handleFileRemove"
                    v-on:activatefile="clickFile"
                  />
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
              <v-btn class="button" @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            </div>
          </div>
          <v-table class="table-type-04">
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
              <tr v-for="pathData, idx in sortedPathList" :key="idx">
                <td>
                  {{ pathData.askTemp }}
                </td>
                <td>
                  {{ pathData.deptname }}
                </td>
                <td>
                  {{ pathData.dutynamedesc }}
                </td>
                <td>
                  {{ pathData.opinion }}
                </td>
                <td>
                  {{ pathData.electsignid }}
                </td>          
                <td>
                  {{ pathData.reportdt }}
                </td>
                <td>
                  <span v-if="pathData.reportdt">v{{ pathData.mainver }}.0</span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>
      
        <!-- 관리정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">관리정보</h6>
            <div class="action-buttons">
              <v-btn class="button" @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
            </div>
          </div>
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="600">
              <col width="120">
              <col width="600">
            </colgroup>
            <tbody>
              <tr>
                <th>등재일자</th>
                <td>
                  <v-text-field
                    v-model="today"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th><span class="required">자료량</span><br>(쪽수,용량)</th>
                <td>
                  <v-text-field 
                    v-model=insertOfflineData.docpagenum
                    variant="outlined"
                    maxlength="9"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>자료유형</th>
                <td colspan="3">
                  <v-radio-group v-model="insertOfflineData.doctype" inline hide-details="auto" class="radio-group-spacing">
                    <v-radio v-for="(data,idx) in doctypeData" :key="idx" :label="data.view" :value="data.key" color="indigo" class="custom-radio-label">
                      <template v-slot:label>
                        <span style="color: black; font-weight: bold; font-size: 14px">{{ data.view }}</span>
                      </template>
                    </v-radio>
                  </v-radio-group>
                </td>
              </tr>
              <tr>
                <th>매체유형</th>
                <td colspan="3">
                  <v-radio-group v-model="insertOfflineData.mediatype" inline hide-details="auto" class="radio-group-spacing">
                    <v-radio v-for="(data,idx) in mediatypeData" :key="idx" :label="data.view" :value="data.key" color="indigo" class="custom-radio-label">
                      <template v-slot:label>
                        <span style="color: black; font-weight: bold; font-size: 14px">{{ data.view }}</span>
                      </template>
                    </v-radio>
                </v-radio-group>
                </td>
              </tr>
              <tr>
                <th><span class="required">문서번호</span></th>
                <td colspan="3">
                  <v-text-field
                    class="w45"
                    v-model="insertOfflineData.proddocnum"
                    @update:modelValue="updateDocno"
                    variant="outlined"
                    maxlength="50"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>열람권자</th>
                <td colspan="3">
                  <v-text-field 
                    :readonly="true"
                    v-model="formatRcvsList"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 보관함정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">보관함정보</h6>
          </div>
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="600">
              <col width="120">
              <col width="600">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">보관함</span></th>
                <td>
                  <v-select
                    class="w100"
                    v-model=insertOfflineData.locker
                    :items="cabinetList"
                    variant="outlined"
                    hide-details="auto"
                  ></v-select>
                </td>
                <th><span class="required">단번호</span></th>
                <td>
                  <v-select
                    class="w100"
                    v-model=insertOfflineData.unitnum
                    :items="danList"
                    variant="outlined"
                    hide-details="auto"
                  ></v-select>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

      </div>
    </div>
  </div>
  
  <!-- 문서 조회 팝업 -->
  <v-dialog v-model="visibleViewerPopup">
      <v-card>
        <div v-if="docloading" class="overlay_02">
          <div class="spinner_02"></div>
          <div class="progress_02"></div>
        </div>
        <v-toolbar>
          <v-toolbar-title>문서 조회</v-toolbar-title>
          <v-btn class="close" icon @click="visibleViewerPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <iframe :src="url" width="1200" height="840" style="padding: 10px;"></iframe>
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

    <!-- 수신지정 팝업 -->
    <v-dialog v-model="bVisibleDctRecvPopup" style="width: 650px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>수신지정</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctRecvPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctRecvPopup
          v-if="bVisibleDctRecvPopup"
          :args="dctRecvPopupArgs"
          :toggleFunc="dctRecvPopupToggleFunc"
          :returnFunc="dctRecvPopupReturnFunc"
        ></DctRecvPopup>
      </v-card>
    </v-dialog>

    <!-- 열람지정 팝업 -->
    <v-dialog v-model="bVisibleDctReaderPopup" style="width: 1000px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>열람자지정</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctReaderPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctReaderPopup
          v-if="bVisibleDctReaderPopup"
          :args="dctReaderPopupArgs"
          :toggleFunc="dctReaderPopupToggleFunc"
          :returnFunc="dctReaderPopupReturnFunc"
        ></DctReaderPopup>
      </v-card>
    </v-dialog>

    <!-- 등록 팝업 -->
    <v-dialog v-model="bVisibleDctOfflineProcPopup" style="width: 800px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>등록</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctOfflineProcPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctOfflineProcPopup
          v-if="bVisibleDctOfflineProcPopup"
          :args="dctOfflineProcPopupArgs"
          :toggleFunc="dctOfflineProcPopupToggleFunc"
        ></DctOfflineProcPopup>
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
const { contentWidth, storeContentHeight, topMenu, breadcrumbs } = storeToRefs(mainStore)

import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import { API } from "@/api";
import dayjs from 'dayjs';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import DctOfflineProcPopup from "./DctOfflineProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctRecvPopup from "./DctRecvPopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
const name = ref('BmsDctProductOfflineCreateCard')

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref(() => { 
  const pathList = [...insertOfflineData.value.pathList];
  const modifiedPathList = pathList.slice(1).map(item => {
    return {
      orgid: item.deptid,
      userid: item.objid,
      username: item.objname,
      orgname: item.deptname,
      grade: item.dutyname,
      askTemp: item.askTemp,
      asktype: item.asktype,
      identifier: `${item.deptid}-${item.objid}`
    };
  });
  dctPathPopupArgs.value.path = modifiedPathList;
  dctPathPopupArgs.value.myid = getUserLoginData.value.userid;
  bVisibleDctPathPopup.value = !bVisibleDctPathPopup.value;
})
const dctPathPopupReturnFunc = ref((retValue) => {
  dctPathPopupToggleFunc.value()
  if(retValue != ""){
    insertInitialPath();
    for(let i = 0; i < retValue.length; i++){
      const path = {};
      path.askTemp = retValue[i].askTemp;
      path.asktype = retValue[i].asktype;
      path.acttype = retValue[i].asktype;
      path.objid = retValue[i].userid;
      path.objname = retValue[i].username;
      path.deptid = retValue[i].orgid;
      path.deptname = retValue[i].orgname;
      path.dutyname = retValue[i].grade;
      path.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      path.mainver = 1;
      path.pathorder = i+2;
      path.pathstate = 'DCST5';
      insertOfflineData.value.pathList.push(path);
    };

    const requests = []; // 기안
    const reviewers = []; // 검토, 협조
    const approvers = []; // 전결, 결재

    // '기안','검토', ... , '결재'에 따라 재정렬
    insertOfflineData.value.pathList.forEach((path) => {
      switch (path.askTemp) {
        case '기안':
          requests.push(path);
          break;
        case '검토':
        case '협조':
          reviewers.push(path);
          break;
        case '전대결':
        case '대결':
        case '전결':
        case '결재':
          approvers.push(path);
          break;
        default:
          break;
      }
    });

    insertOfflineData.value.pathList = requests.concat(reviewers,approvers);
    updatePathOrder();
  }else{
    insertInitialPath();
  }

});

const insertInitialPath = () => {
  insertOfflineData.value.pathList = [];
  const initialPath = {};
  initialPath.askTemp = "기안";
  initialPath.asktype = "DEA11";
  initialPath.acttype = "DEA41";
  initialPath.objid = getUserLoginData.value.userid;
  initialPath.objname = getUserLoginData.value.username;
  initialPath.deptid = getUserLoginData.value.deptid;
  initialPath.deptname = getUserLoginData.value.deptname;
  initialPath.dutyname = getUserLoginData.value.grade;
  initialPath.dutynamedesc = getUserLoginData.value.grade+"/"+getUserLoginData.value.username;
  initialPath.mainver = "1";
  initialPath.pathorder = "1";
  initialPath.pathstate = 'DCST5';
  insertOfflineData.value.pathList.push(initialPath);
};

const sortedPathList = computed(() => {
  return insertOfflineData.value.pathList.sort((a, b) => {
    return a.pathorder - b.pathorder;
  });
});

const updatePathOrder = () => {
  insertOfflineData.value.pathList.forEach((path, index) => {
      path.pathorder = index + 1;
  });
};

/* ============================================================ */

/* ======================== 수신지정 팝업 ======================== */
const bVisibleDctRecvPopup = ref(false)
const dctRecvPopupReuturn = ref({})
const dctRecvPopupArgs = ref({})
const dctRecvPopupToggleFunc = ref(() => { 
  insertOfflineData.value.gubun = 'DOPT1'
  const recvList = [...insertOfflineData.value.recvList];
  const modifiedRecvList = recvList.map(item => {
    const gubun = item.apprrecvgubun === "RDR08" ? '원내' : (item.apprrecvgubun === "RDR02" ? '외부' : '대내');
    return {
      receiverid: item.receiverid,
      apprrecvtempflag: item.apprrecvtempflag,
      receiverSymbol: item.recsymbol,
      apprrecvgubun: item.apprrecvgubun,
      gubun,
      apprrecvdeptname: item.apprrecvdeptname,
      apprrecvrefdeptname: item.apprrecvrefdeptname,
    };
  });
  dctRecvPopupArgs.value = modifiedRecvList;
  bVisibleDctRecvPopup.value = !bVisibleDctRecvPopup.value;
})
const dctRecvPopupReturnFunc = ref((retValue) => {
  dctRecvPopupToggleFunc.value()
  if(retValue != ""){
    insertOfflineData.value.recvList = [];
    for(let i = 0; i < retValue.length; i++){
      const receiver = {};
      receiver.receiverid = retValue[i].receiverid;
      receiver.apprrecvtempflag = retValue[i].apprrecvtempflag;
      receiver.apprrecvseq = i+1;
      receiver.apprrecvgubun = retValue[i].apprrecvgubun;
      receiver.apprrecvdeptname = retValue[i].apprrecvdeptname;
      receiver.apprrecvrefdeptname = retValue[i].apprrecvrefdeptname;
      receiver.recsymbol = retValue[i].receiverSymbol;
      insertOfflineData.value.recvList.push(receiver);
    }
  }else{
    insertOfflineData.value.recvList = [];
  }
});
/* ============================================================ */
/* ======================== 열람지정 팝업 ======================== */
const bVisibleDctReaderPopup = ref(false)
const dctReaderPopupReuturn = ref({})
const dctReaderPopupArgs = ref({})
const dctReaderPopupToggleFunc = ref(() => { 
  const rcvsList = [...insertOfflineData.value.rcvsList];
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
      insertOfflineData.value.rcvsList.push(rcvs);
    }
  }else{
    rcvsMe();
  }
})

const rcvsMe = () => {
  insertOfflineData.value.rcvsList = [];
  const initialRcvs = {};
  initialRcvs.rcvsuserid = getUserLoginData.value.userid;
  initialRcvs.rcvsusername = getUserLoginData.value.username;
  initialRcvs.rcvsinseq = 0;
  initialRcvs.rcvdeptid = getUserLoginData.value.deptid;
  initialRcvs.rcvdeptname = getUserLoginData.value.deptname;
  initialRcvs.rcvdutyname = getUserLoginData.value.grade;
  insertOfflineData.value.rcvsList.push(initialRcvs);
}
/* ============================================================ */
/* ======================== 등록 팝업 ======================== */
const bVisibleDctOfflineProcPopup = ref(false)
const dctOfflineProcPopupArgs = ref({})
const dctOfflineProcPopupToggleFunc = ref(() => {
  if (makeAlert() == false) {
    return;
  }
  insertOfflineData.value.mgmtno = insertOfflineData.value.mgmtnoStart+'-'+insertOfflineData.value.mgmtnoCenter+'-'+insertOfflineData.value.mgmtnoEnd;
  dctOfflineProcPopupArgs.value = insertOfflineData.value;
  dctOfflineProcPopupArgs.value.files = newAttachFileArray.value;
  bVisibleDctOfflineProcPopup.value = !bVisibleDctOfflineProcPopup.value; 
})
/* ============================================================ */
const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');

const today = ref("")
const insertOfflineData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "DOPT2", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "DRDS3", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
  open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
  readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
  emergency: "N", // 긴급도
  keyword: "", // 키워드
  authorid: "", // 작성자 ID
  authorname: "", // 담당자명
  authordutyname: "", // 담당자 직위직급
  authordeptid: "", // 담당자 부서ID
  authordeptname: "", // 담당자 부서이름
  tempflag: "N", // 임시 flag (임시: Y, 처리요청: N)

  // 3) bms_dct_attr table
  secgubun: "00", // 비밀구분 (일반행정: 00, 군사: 01, 군사훈련: 02, 전시: 03, 한미연합: 04, 한미연합연습: 05, 전시(행정기관): 06)
  seclevel: "", // 비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
  prsrvterm: "", // 보존기간 (1년: 001, 3년: 003, 5년: 005, 10년: 010, 30년: 030, 준영구: 040, 영구: 050)
  orgnprotdt: "", // 원본보호기간
  orgnnextlevel: "", // 원본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
  orgnreclass: "", // 원본재분류 (재분류: 21)
  copyprotdt: "", // 사본보호기간
  copynextlevel: "", // 사본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4)
  copyreclass: "", // 사본재분류 (재분류: 21. 파기: 23, 존안: 20)
  copynostart: "원",
  copynoend: "본",

 // 4) bms_dct_path table
 pathList:[
    {
      askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
      asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
      acttype: "", // 처리유형 (결재선지정 한 경우 - 결재: DEA41, 1인결재: DEA45)
      objid: "", // 객체ID
      objname:"", // 객체이름
      deptid: "", // 부서ID
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
  opinionpathorder: "1", //  경로순서 (실제로는 pathorder 컬럼)
  opinion: "",  // 의견
  
  // 6) bms_dct_appr_recv table
  recvList:[
    {
      receiverid: "",    // 수신처ID
      apprrecvtempflag: "N", // 임시FLAG (실제로는 tempflag 컬럼)
      apprrecvseq: "1", // 일련번호 (실제로는 seq 컬럼)
      apprrecvgubun: "",  // 구분 (실제로는 gubun 컬럼)
      apprrecvdeptname: "",  // 부서이름 (실제로는 deptname 컬럼)
      apprrecvrefdeptname: "",  // 참조부서이름 (실제로는 refdeptname 컬럼)
      recsymbol: "", // 외부 수신처 기호
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


  // 9) bms_dct_hist_card table (문서처리시에만. 임시저장에는X)
  apprreqid: "", // 결재요청 ID
  histgubun: "01", // 이력구분 (기안: 01)
  deptid: "", // 소속코드
  deptname: "", // 소속명
  dutyname: "", // 직위직급명
  username: "", // 사용자명
  histstatus: "010", // 이력상태 (결재 요청: 010)
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
  resuserid: "",
  
  // 붙임파일 관련
  newFileList: [],
  delFileList: [],


  // 관리정보
  docpagenum: "", // 자료량(쪽수)
  doctype: "문서", // 자료유형
  mediatype: "전자파일", // 매체유형

  // 보관함 정보
  locker: "선택", // 보관함
  unitnum: "선택", // 단번호

  proddocnum: "", // 생산문서번호
  docno: "", // 문서번호
  mgmtnoStart: "", // 관리번호 - 등급
  mgmtnoCenter: "", // 관리번호 - 년도
  mgmtnoEnd: "", // 관리번호 - 번호
  mgmtno: "", // 관리번호
  paperrecgubun: "생산", // 비전자문서 생산/접수 구분
});

const isloading = ref(false);

const rules = {
  required: value => !!value || '제목을 입력해주세요.',
  counter: value => value.length < 500 || '최대 500자 까지 입력하실 수 있습니다.',
  summarycounter: value => value.length < 4000 || '최대 4000자 까지 입력하실 수 있습니다.',
  copyno: value => /^[1-9][0-9]{0,2}$/.test(value) || '3자리 이내의 정수만 입력하실 수 있습니다.',
  mgmtnoCenter: value => /^[1-9][0-9]{0,3}$/.test(value) || '4자리 이내의 정수만 입력하실 수 있습니다.',
  mgmtnoEnd: value => /^[0-9][0-9]*$/.test(value) || '정수만 입력하실 수 있습니다.',
};

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted( async () => {
  insertOfflineData.value.authorid = getUserLoginData.value.userid;
  insertOfflineData.value.authorname = getUserLoginData.value.username;
  insertOfflineData.value.authordutyname = getUserLoginData.value.grade;
  insertOfflineData.value.authordeptid = getUserLoginData.value.deptid;
  insertOfflineData.value.authordeptname = getUserLoginData.value.deptname;

  insertOfflineData.value.deptid = getUserLoginData.value.deptid;
  insertOfflineData.value.deptname = getUserLoginData.value.deptname;
  insertOfflineData.value.dutyname = getUserLoginData.value.grade;
  insertOfflineData.value.username = getUserLoginData.value.username;

  insertOfflineData.value.resuserid = getUserLoginData.value.userid;
  today.value = dayjs().format('YYYY-MM-DD');
  insertInitialPath();
  rcvsMe();
  await selectCabinetInfo();
  await selectDanInfo();
})

watchEffect(() => {
})


// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: '선택', secLevelDataValue: ''},
  { secLevelDataKey: 'II급', secLevelDataValue: '2'},
  { secLevelDataKey: 'III급', secLevelDataValue: '3'},
  { secLevelDataKey: '일반', secLevelDataValue: '5'},
];

// 보존기간 Select-Box
const prsRvTermData = [
  {prsRvTermDataKey: '선택', prsRvTermDataValue: ''},
  {prsRvTermDataKey: '1년', prsRvTermDataValue: '001'},
  {prsRvTermDataKey: '3년', prsRvTermDataValue: '003'},
  {prsRvTermDataKey: '5년', prsRvTermDataValue: '005'},
  {prsRvTermDataKey: '10년', prsRvTermDataValue: '010'},
  {prsRvTermDataKey: '30년', prsRvTermDataValue: '030'},
  {prsRvTermDataKey: '준영구', prsRvTermDataValue: '040'},
  {prsRvTermDataKey: '영구', prsRvTermDataValue: '050'},
];

// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];

// 자료유형
const doctypeData = [
  {view: "문서", key:"문서"},
  {view: "보고서", key:"보고서"},
  {view: "그림", key:"그림"},
  {view: "사진", key:"사진"},
  {view: "간행물", key:"간행물"},
  {view: "도면", key:"도면"},
  {view: "SW", key:"SW"},
  {view: "기타", key:"기타"},
];

// 매체유형
const mediatypeData = [
  {view: "전자파일", key:"전자파일"},
  {view: "책자", key:"책자"},
  {view: "CD", key:"CD"},
  {view: "바인더", key:"바인더"},
  {view: "HDD", key:"HDD"},
  {view: "AP카드", key:"AP카드"},
  {view: "테이프", key:"테이프"},
  {view: "기타", key:"기타"},
];

// 보존기간 validation check
const updatePrsrvterm = () => {
  if(parseInt(insertOfflineData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    insertOfflineData.value.prsrvterm = "";
    return;
  }
}

/* ======================== 원본보호기간 ======================== */
const orgnProtDtSelected = ref("날짜지정");
const orgnProtDtData = ["날짜지정", "직접입력"];

const orgnProtDtPeriodSelected = ref("");
const orgnProtDtPeriodData = [
  {key: '선택', value:''},
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

const orgnDate = ref({
  year: "",
  month: "",
  day: "",
})

// 날짜지정
const getOrgnDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(orgnProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if(parseInt(insertOfflineData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return
  }
  orgnDate.value.year = futureYear;
  orgnDate.value.month = '12';
  orgnDate.value.day = '31';
  insertOfflineData.value.orgnprotdt = orgnDate.value.year+orgnDate.value.month+orgnDate.value.day;
}

const updateOrgnDt = () => {
  orgnProtDtPeriodSelected.value = "";
  orgnUserDate.value = "";
  orgnDate.value.year = "";
  orgnDate.value.month = "";
  orgnDate.value.day = "";
}
const orgnUserDate = ref("");

// 직접입력
const getOrgnUserDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(copyProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if(parseInt(insertOfflineData.value.prsrvterm) < parseInt(orgnUserDate.value.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return
  }
  insertOfflineData.value.orgnprotdt = orgnUserDate.value.replace(/-/g, '');
}

const orgnReclassSelected = ref("선택");
const orgnReclass = ['선택','일반문서'];
const orgnReclassData = [
  { key: '선택', value: ''},
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


/* ======================== 첨부파일 ======================== */
const pondRef = ref(null);
const removePond = (id) => {
  pondRef.value.removeFile(id);
};

const clickFile = (event) => {
  toggleViewerPopup(event, getUserLoginData.value.userid);
};

const handleFileAdd = (error, data) => {
  const size = data.fileSize;
  const dupliNameIdx = attachFileData.value.findIndex(attach => attach.file.name === data.filename);
  if(size == 0){
    alert("크기가 0인 파일은 등록이 불가능합니다.");
    removePond(data.id);
  }else if(size >= 2000000000){
    alert("붙임 파일은 2GB까지 등록 가능합니다.");
    removePond(data.id);
  }else if(dupliNameIdx != -1){
    alert("이미 등록된 파일입니다.");
    removePond(data.id);
  }else{
    attachFileData.value.push({file: data.file, id: data.id});
    insertOfflineData.value.newFileList.push({ file: data.file, id: data.id });
  }
};

const handleFileRemove = (error, data) => {
  const index = attachFileData.value.findIndex(attach => attach.id === data.id);
  if (index !== -1) {
    attachFileData.value.splice(index, 1);
    insertOfflineData.value.newFileList.splice(index, 1);
    insertOfflineData.value.delFileList.push(index);
  }
};

const newAttachFileArray = computed(() => {
  return attachFileData.value.map(item => item.file);
});

const attachFileData = ref([]);

/* ============================================================ */

const updateDocno = () => {
  insertOfflineData.value.docno = insertOfflineData.value.proddocnum;
};

const updateMgmtno = () => {
  switch(insertOfflineData.value.seclevel){
    case '2':
      insertOfflineData.value.mgmtnoStart = 'II';
      break;
    case '3':
      insertOfflineData.value.mgmtnoStart = 'III';
    break;
    case '5':
      insertOfflineData.value.mgmtnoStart = '일반';
      break;
    default:
      insertOfflineData.value.mgmtnoStart = '';
  }
}

const checkMgmtno = (item, val) => {
  if(val == 'mgmtnoCenter'){
      if(rules.mgmtnoCenter(item) == '4자리 이내의 정수만 입력하실 수 있습니다.'){
        insertOfflineData.value.mgmtnoCenter = "";
      }
    }else{
      if(rules.mgmtnoEnd(item) == '정수만 입력하실 수 있습니다.'){
        insertOfflineData.value.mgmtnoEnd = "";
      }
    }
};

// 열람권자
const formatRcvsList = computed(() => {
  if(insertOfflineData.value.rcvsList[0].rcvsusername == ""){
    return
  }else{
    const formattedRcvsList = insertOfflineData.value.rcvsList.map((item) => {
      return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
    });
    return formattedRcvsList.join(', ');
  }
});

const makeAlert = () => {
  if (!insertOfflineData.value.seclevel) {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if (insertOfflineData.value.mgmtnoCenter == "" || insertOfflineData.value.mgmtnoEnd == "") {
    alert('관리번호를 입력해주세요.');
    return false;
  }
  if(!insertOfflineData.value.prsrvterm){
    alert('예고문 보존기간을 선택해주세요.');
    return false;
  }
  if(!insertOfflineData.value.orgnprotdt){
    alert('원본 보호기간을 선택해주세요.');
    return false;
  }
  if(parseInt(insertOfflineData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if(!insertOfflineData.value.orgnreclass){
    alert('원본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if (insertOfflineData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
  if (insertOfflineData.value.docpagenum == "") {
    alert('자료량(쪽수)을 입력해주세요.');
    return false;
  }
  if (insertOfflineData.value.docno == "") {
    alert('문서번호를 입력해주세요.');
    return false;
  }
  if (insertOfflineData.value.locker == "선택") {
    alert('보관함 정보를 선택해주세요.');
    return false;
  }
  if (insertOfflineData.value.unitnum == "선택") {
    alert('단번호를 선택해주세요.');
    return false;
  }
};

const cabinetInfoCondi = ref({tcodeid: ""})
const cabinetList = ref(["선택"]);
const selectCabinetInfo = async () => {
  cabinetInfoCondi.value.tcodeid = 'OFFCAB';
  try {
    const response = await API.comAPI.selectOfflineCabinetInfo({...cabinetInfoCondi.value}, urlPaths.value);
    cabinetList.value = [...cabinetList.value, ...response.data.map(item => item.codename)];
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
const danInfoCondi = ref({tcodeid: ""})
const danList = ref(["선택"]);
const selectDanInfo = async () => {
  danInfoCondi.value.tcodeid = 'OFFDAN';
  try {
    const response = await API.comAPI.selectOfflineCabinetInfo({...danInfoCondi.value}, urlPaths.value);
    danList.value = [...danList.value, ...response.data.map(item => item.codename)];
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  docloading.value = true;
  visibleViewerPopup.value = !visibleViewerPopup.value;
  await viewDocument(file, userid, 'N,N', '0');
  document.querySelector('iframe').addEventListener('load', function() {
    docloading.value = false;
  });
}


// 취소 버튼 Move FN (처리할 일 페이지로 이동)
const moveToBmsTodolist = () => {
  let arr = ['비밀문서', '처리할일']

  breadcrumbs.value.activeLink = arr;
  
  router.push({
    name: "BmsTodolist",
  });
};


// left, top 숨김/펼침 버튼 클릭 시
// 기안문 width, height 변경되도록
const compKey = ref(0);

watch(() => contentWidth.value, (newWidth) => {
  compKey.value++;
});

watch(() => storeContentHeight.value, (newHeight) => {
  compKey.value++;
});

</script>
<style scoped>
.custom-radio-label {
  margin-right: 30px !important;
}
</style>