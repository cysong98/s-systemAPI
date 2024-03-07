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
      <h4 v-if="['BmsDctwaitlist','BmsDctinglist','BmsDctapprovalcompletelist'].includes(getFromPage)" class="h4">결재</h4>
    </div>

    <div class="box-content">
      <!-- 비전자비밀접수 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비전자 비밀조회</h5>
          <div class="action-buttons">
            <v-btn class="button" @click="dctProcPopupToggleFunc()">문서처리</v-btn>
            <v-btn class="button" @click="dctPathPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || me.asktype == 'DEA41' || me.asktype == 'DEA51' || me.asktype == 'DEA61' || me.asktype == 'DEA71'">결재선지정</v-btn>
            <v-btn class="button" @click="dctReaderPopupToggleFunc()" :disabled="me.asktype == 'DEA31'">열람지정</v-btn>
            <v-btn class="button" @click="dctTempPopupToggleFunc()" :disabled="me.asktype == 'DEA31'">임시저장</v-btn>
            <v-btn class="button" @click="moveToList()">목록</v-btn>
          </div>
        </div>
      </section>

      <div>
        <!-- 비밀속성 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">비밀속성</h6>
          </div>
          <v-table class="table-type-03-offline">
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
                <th>비밀등급</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="updateOfflineData.seclevel"
                    :readonly="me.asktype == 'DEA31'"
                    :items="secLevelData"
                    item-title="secLevelDataKey"
                    item-value="secLevelDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th>사본번호</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="updateOfflineData.copyno"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </div>
                </td>
                <th>관리번호</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="updateOfflineData.mgmtno"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </div>
                </td>
              </tr>
              <tr>
                <th>예고문</th>
                <td colspan="5">
                  <!-- 예고문 - 사본보호기간 -->
                  <div class="box-flex">
                    <v-label>사본보호기간:</v-label>
                    <v-select
                      v-model="copyProtDtSelected"
                      @update:modelValue="updateCopyDt"
                      :items="copyProtDtData"
                      :readonly="me.asktype == 'DEA31'"
                      variant="outlined"
                      hide-details="auto"></v-select>
                    <v-text-field
                      v-if="copyProtDtSelected == '직접입력'"
                      v-model="copyUserDate"
                      type="date"
                      :min="today"
                      @update:modelValue="getCopyUserDate"
                      :readonly="me.asktype == 'DEA31'"
                      variant="outlined"
                      hide-details="auto">
                    </v-text-field>
                    <template v-else>
                      <v-select
                        v-model="copyProtDtPeriodSelected"
                        @update:modelValue="getCopyDate"
                        :items="copyProtDtPeriodData"
                        item-title="key"
                        item-value="value"
                        :readonly="me.asktype == 'DEA31'"
                        variant="outlined"
                        hide-details="auto">
                      </v-select>
                      <v-text-field
                        v-model="copyDate.year"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>년</v-label>
                      <v-text-field
                        v-model="copyDate.month"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>월</v-label>
                      <v-text-field
                        v-model="copyDate.day"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>일</v-label>
                    </template>
                      <v-select
                        v-model="copyReclassSelected"
                        :items="copyReclass"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="true">
                      </v-select>
                      <v-label>(으)로</v-label>
                      <v-select
                        v-model="updateOfflineData.copyreclass"
                        :items="copyReclassData"
                        @update:modelValue="selectedCopyReClass"
                        item-title="key"
                        item-value="value"
                        :readonly="me.asktype == 'DEA31'"
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
          <v-table class="table-type-03-offline">
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
                <th>제목</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.docttl"
                    :readonly="me.asktype == 'DEA31'"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="updateOfflineData.emergency"
                    :readonly="me.asktype == 'DEA31'"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.keyword"
                    :readonly="me.asktype == 'DEA31'"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="5">
                  <div>
                    <FilePond
                      v-if="me.asktype != 'DEA31'"
                      ref="pondRef"
                      label-idle="Drop files here or Browse"
                      allow-multiple="true"
                      v-on:addfile="handleFileAdd"
                      v-on:removefile="handleFileRemove"
                      v-on:activatefile="clickFile"
                    />
                  </div>
                  <div v-if="updateOfflineData.fileList != []">
                    <div class="box-flex" v-for="(fileData,idx) in updateOfflineData.fileList" :key="idx">
                      <v-icon :color="getFileIcon(getExtension(fileData.sfilename)).color">{{ getFileIcon(getExtension(fileData.sfilename)).icon }}</v-icon>
                      <span @click="toggleViewerPopup(fileData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ fileData.sfilename }}</span>
                    </div>
                  </div>
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
              <v-btn class="button" @click="dctPathPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || me.asktype == 'DEA41' || me.asktype == 'DEA51' || me.asktype == 'DEA61' || me.asktype == 'DEA71'">결재선지정</v-btn>
            </div>
          </div>
          <v-table class="table-type-04-offline">
            <thead>
              <tr>
                <th>구분</th>
                <th>부서</th>
                <th>직위/성명</th>
                <th>의견/지시</th>
                <th>서명</th>
                <th>처리결과</th>
                <th>처리일시</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="pathData, idx in updateOfflineData.pathList" :key="idx">
                <td>
                  {{ pathData.askTemp }}
                </td>
                <td>
                  {{ pathData.deptname }}
                </td>
                <td>
                  {{ pathData.dutynamedesc }}
                </td>
                <td style="width: 800px;" class="text-left">
                  {{ pathData.opinion }}
                </td>
                <td class="signature">
                  {{ pathData.sign }}
                </td>
                <td>
                  {{ pathData.reportresult }}
                </td>          
                <td>
                  {{ pathData.reportdt }}
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 시행정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">시행정보</h6>
          </div>
          <v-table class="table-type-03-offline">
            <colgroup>
              <col width="120">
              <col width="600">
              <col width="120">
              <col width="600">
            </colgroup>
            <tbody>
              <tr>
                <th>발신기관명</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.sendOrgNameData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>발신기관명의</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.sendNameData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>생산문서번호</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.proddocnum"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>접수번호</th>
                <td>
                  <v-text-field 
                    v-model="updateOfflineData.acceptdocnum"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>시행일자</th>
                <td>
                  <v-text-field
                    class="w100"
                    v-model="updateOfflineData.senddate"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>시행범위</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="updateOfflineData.sendrange"
                    :readonly="true"
                    :items="sendRangeData"
                    item-title="key"
                    item-value="value"
                    variant="outlined"
                    hide-details="auto"></v-select>
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
            </tbody>
          </v-table>
        </section>
      
      
        <!-- 관리정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">관리정보</h6>
            <div class="action-buttons">
              <v-btn class="button" @click="dctReaderPopupToggleFunc()" :disabled="me.asktype == 'DEA31'">열람지정</v-btn>
            </div>
          </div>
          <v-table class="table-type-03-offline">
            <colgroup>
              <col width="120">
              <col width="600">
              <col width="120">
              <col width="600">
            </colgroup>
            <tbody>
              <tr>
                <th>접수등록일자</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.reciveddt"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>등재일자</th>
                <td>
                  <v-text-field
                    v-model="updateOfflineData.reciveddt"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>접수부서</th>
                <td>
                  <v-text-field 
                    v-model="updateOfflineData.authordeptname"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>자료량<br>(쪽수,용량)</th>
                <td>
                  <v-text-field 
                    v-model=updateOfflineData.docpagenum
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>자료유형</th>
                <td colspan="3">
                  <v-radio-group v-model="updateOfflineData.doctype" inline hide-details="auto" class="radio-group-spacing">
                    <v-radio disabled v-for="(data,idx) in doctypeData" :key="idx" :label="data.view" :value="data.key" color="indigo" class="custom-radio-label">
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
                  <v-radio-group v-model="updateOfflineData.mediatype" inline hide-details="auto" class="radio-group-spacing">
                    <v-radio disabled v-for="(data,idx) in mediatypeData" :key="idx" :label="data.view" :value="data.key" color="indigo" class="custom-radio-label">
                      <template v-slot:label>
                        <span style="color: black; font-weight: bold; font-size: 14px">{{ data.view }}</span>
                      </template>
                    </v-radio>
                </v-radio-group>
                </td>
              </tr>
              <tr>
                <th>문서번호</th>
                <td colspan="3">
                  <v-text-field
                    v-model="updateOfflineData.docno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>열람권자</th>
                <td>
                  <v-text-field 
                    v-model="formatRcvsList"
                    :readonly="true"
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
          <v-table class="table-type-03-offline">
            <colgroup>
              <col width="120">
              <col width="600">
              <col width="120">
              <col width="600">
            </colgroup>
            <tbody>
              <tr>
                <th>보관함</th>
                <td>
                  <v-text-field 
                    v-model=updateOfflineData.locker
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>단번호</th>
                <td>
                  <v-text-field 
                    v-model=updateOfflineData.unitnum
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
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

  <!-- 임시저장 팝업 -->
  <v-dialog v-model="bVisibleDctTempPopup" style="width: 650px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctTempPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctTempPopup
        v-if="bVisibleDctTempPopup"
        :args="dctTempPopupArgs"
        :toggleFunc="dctTempPopupToggleFunc"
        :returnFunc="dctTempPopupReturnFunc"
      ></DctTempPopup>
    </v-card>
  </v-dialog>

  <!-- 문서처리 팝업 -->
  <v-dialog v-model="bVisibleDctProcPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>문서처리</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctApprovalProcPopup
       v-if="bVisibleDctProcPopup"
        :args="dctProcPopupArgs"
        :toggleFunc="dctProcPopupToggleFunc"
        :returnFunc="dctProcPopupReturnFunc"
      ></DctApprovalProcPopup>
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

import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import { transformStatus,transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { API } from "@/api";
import dayjs from 'dayjs';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import DctPathPopup from "./DctPathPopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctTempPopup from "./DctTempPopup.vue";
import DctApprovalProcPopup from "./DctApprovalProcPopup.vue";
const name = ref('BmsDctOfflineUpdateCard')

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref(() => { 
  const pathList = [...updateOfflineData.value.pathList];
  //이미 결재된거 + 본인 안넘겨주도록
  let pathList2 = [];
  for(let i=0; i<pathList.length; i++) {
    if(pathList[i].sign == ""){
      pathList2.push(pathList[i]);
    }
  }
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
  if (retValue != "") {
    // 서명이 없는 항목 삭제 (본인 제외)
    let count = 0;
    for (let j = updateOfflineData.value.pathList.length - 1; j > 0; j--) {
      if (updateOfflineData.value.pathList[j].sign == "") {
        if(updateOfflineData.value.pathList[j].objid != me.value.objid) {
          updateOfflineData.value.pathList.splice(j, 1);
        }
      } else {
        count++;
      }
    }

    // 팝업 리턴값 세팅
    let pathList = [];
    for(let i = 0; i < retValue.length; i++){
      const path = {
        askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
        asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
        acttype: "", // 처리유형 (결재선지정 한 경우 - 결재: DEA41, 1인결재: DEA45)
        objid: "", // 객체ID
        objname: "", // 객체이름
        deptid: "", // 부서ID
        deptname: "", // 부서이름
        dutyname: "", // 직위직급이름
        dutynamedesc: "", // 직위직급이름 상세
        electsignid: "", // 전자서명ID
        reportdt: "", // 보고일자
        mainver: "", // 메인버전
        pathorder: "", //  경로순서
        pathstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
        sign: "",
      };
      path.askTemp = retValue[i].askTemp;
      path.asktype = retValue[i].asktype;
      path.objid = retValue[i].userid;
      path.objname = retValue[i].username;
      path.deptid = retValue[i].orgid;
      path.deptname = retValue[i].orgname;
      path.dutyname = retValue[i].grade;
      path.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      path.mainver = 1;
      path.pathorder = i+2;
      path.pathstate = 'DCST1';
      updateOfflineData.value.pathList.push(path);
    };

    const requests = []; // 기안
    const reviewers = []; // 검토, 협조
    const approvers = []; // 전결, 결재

    // '기안','검토', ... , '결재'에 따라 재정렬
    updateOfflineData.value.pathList.forEach((path) => {
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

    updateOfflineData.value.pathList = requests.concat(reviewers,approvers);
    updatePathOrder();
  }
});

const updatePathOrder = () => {
  updateOfflineData.value.pathList.forEach((path, index) => {
      path.pathorder = index + 1;
  });
};

/* ============================================================ */

/* ======================== 열람지정 팝업 ======================== */
const bVisibleDctReaderPopup = ref(false)
const dctReaderPopupReuturn = ref({})
const dctReaderPopupArgs = ref({})
const dctReaderPopupToggleFunc = ref(() => { 
  const rcvsList = [...updateOfflineData.value.rcvsList];
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
      updateOfflineData.value.rcvsList.push(rcvs);
    }
  }else{
    rcvsMe();
  }
})

const rcvsMe = () => {
  const initialRcvs = updateOfflineData.value.rcvsList[0];
  updateOfflineData.value.rcvsList = [];
  updateOfflineData.value.rcvsList.push(initialRcvs);
}
/* ============================================================ */

/* ======================== 임시저장 팝업 ======================== */
const bVisibleDctTempPopup = ref(false)
const dctTempPopupReuturn = ref({})
const dctTempPopupArgs = ref({})
const dctTempPopupToggleFunc = ref(() => {
  if (makeAlert() == false) {
    return;
  }
  dctTempPopupArgs.value.data = updateOfflineData.value;
  dctTempPopupArgs.value.file = newAttachFileArray.value;
  dctTempPopupArgs.value.flag = '1'; // 0: 기안 임시저장, 1: 결재 임시저장
  bVisibleDctTempPopup.value = !bVisibleDctTempPopup.value;
})
const dctTempPopupReturnFunc = ref((retValue) => {
  dctTempPopupToggleFunc.value()
  dctTempPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 문서처리 팝업 ======================== */
const bVisibleDctProcPopup = ref(false);
const dctProcPopupReuturn = ref({});
const dctProcPopupArgs = ref({});
const dctProcPopupToggleFunc = ref(() => {
  if (makeAlert() == false) {
    return;
  }
  dctProcPopupArgs.value = updateOfflineData.value;
  dctProcPopupArgs.value.files = newAttachFileArray.value;
  dctProcPopupArgs.value.judgeParam = 'offApprove';
  bVisibleDctProcPopup.value = !bVisibleDctProcPopup.value;
});
const dctProcPopupReturnFunc = ref((retValue) => {
  dctProcPopupToggleFunc.value();
  dctProcPopupReuturn.value = retValue;
});
/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');
const getFromPage = ref('')
const today = ref("")
const updateOfflineData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
  open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
  readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
  emergency: "", // 긴급도
  keyword: "", // 키워드
  authorid: "", // 작성자 ID
  authorname: "", // 담당자명
  authordutyname: "", // 담당자 직위직급
  authordeptid: "", // 담당자 부서ID
  authordeptname: "", // 담당자 부서이름
  tempflag: "", // 임시 flag (임시: Y, 처리요청: N)

  // 2) bms_dct_summary table
  docgubun: "A", // 비밀구분 (발송: A, 시행: E)
  summary: "", // 요약

  // 3) bms_dct_attr table
  secgubun: "00", // 비밀구분 (일반행정: 00, 군사: 01, 군사훈련: 02, 전시: 03, 한미연합: 04, 한미연합연습: 05, 전시(행정기관): 06)
  seclevel: "", // 비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
  copyprotdt: "", // 사본보호기간
  copynextlevel: "", // 사본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4)
  copyreclass: "", // 사본재분류 (재분류: 21. 파기: 23, 존안: 20)
  copynostart: "",
  copynoend: "",

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
      sign: "",
    }
  ],
  
  // 5) bms_dct_opinion table (문서처리시에만. 임시저장에는X)
  opinionpathorder: "1", //  경로순서 (실제로는 pathorder 컬럼)
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
  resuserid: "",
  
  // 붙임파일 관련
  newFileList: [],
  delFileList: [],
  refList: [],

  // 접수정보
  sendOrgNameData: "", // 발신기관명
  sendNameData: "", // 발신기관명의
  senddate: "", // 시행일자
  proddocnum: "", // 생산등록번호
  sendrange: "대내", // 시행범위

  // 관리정보
  regideptname: "", // 접수부서
  docpagenum: "", // 자료량(쪽수)
  doctype: "", // 자료유형
  mediatype: "", // 매체유형

  // 보관함 정보
  locker: "", // 보관함
  unitnum: "", // 단번호

  // 검토용인쇄확인 flag
  printflag: "N",

  // 오프라인확인 flag
  offlineflag: "Y",

  // 비전자문서 flag
  paperflag: "",
});

const isloading = ref(false);
const query = ref({});
const docidData = ref({docid: ""});
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  query.value = route.query;
  getFromPage.value = query.value.frompage;
  docidData.value.docid = query.value.docid;
})

onMounted( async () => {
  await checkOrder();
  if(checkYN == "Y") {
    updateOfflineData.value.resuserid = getUserLoginData.value.userid;
    updateOfflineData.value.deptid = getUserLoginData.value.deptid;
    updateOfflineData.value.deptname = getUserLoginData.value.deptname;
    updateOfflineData.value.dutyname = getUserLoginData.value.grade;
    updateOfflineData.value.username = getUserLoginData.value.username;
    await getDctRdoc();
    await getDctAttr();
    await getDctAttrAdd();
    await selectDctPath();
    await getDctRecv();
    await selectDctReader();
    await selectDctFile();
    await getReprocess();
  }else {
    alert("이전 결재자가 결재 진행중입니다.");
    moveToBmsDctwaitlist();
  }
})

watchEffect(() => {
})

const moveToBmsDctwaitlist = () => {
  router.push({
    name: "BmsDctwaitlist",
  });
};

// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: '선택', secLevelDataValue: ''},
  { secLevelDataKey: 'II급', secLevelDataValue: '2'},
  { secLevelDataKey: 'III급', secLevelDataValue: '3'},
  { secLevelDataKey: '일반', secLevelDataValue: '5'},
];

// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];

// 시행범위
const sendRangeData = ref([
  { key: '원내', value: 'RDR08' },
  { key: '대내', value: 'RDR01' },
  { key: '외부', value: 'RDR02' },
]);

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

/* ======================== 사본보호기간 ======================== */
const copyProtDtSelected = ref("날짜지정");
const copyProtDtData = ["날짜지정", "직접입력"];

const copyProtDtPeriodSelected = ref("");
const copyProtDtPeriodData = [
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

const copyDate = ref({
  year: "",
  month: "",
  day: "",
})

// 날짜지정
const getCopyDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(copyProtDtPeriodSelected.value);
  copyDate.value.year = futureYear;
  copyDate.value.month = '12';
  copyDate.value.day = '31';
  updateOfflineData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
}

const updateCopyDt = () => {
  copyProtDtPeriodSelected.value = "";
  copyUserDate.value = "";
  copyDate.value.year = "";
  copyDate.value.month = "";
  copyDate.value.day = "";
}
const copyUserDate = ref("");

// 직접입력
const getCopyUserDate = () => {
  updateOfflineData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
}

const copyReclassSelected = ref("선택");
const copyReclass = ['선택','일반문서'];
const copyReclassData = [
  { key: '선택', value: ''},
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
    updateOfflineData.value.newFileList.push({ file: data.file, id: data.id });
  }
};

const handleFileRemove = (error, data) => {
  const index = attachFileData.value.findIndex(attach => attach.id === data.id);
  if (index !== -1) {
    attachFileData.value.splice(index, 1);
    updateOfflineData.value.newFileList.splice(index, 1);
    updateOfflineData.value.delFileList.push(index);
  }
};

const newAttachFileArray = computed(() => {
  return attachFileData.value.map(item => item.file);
});

const attachFileData = ref([]);

/* ============================================================ */

const makeAlert = () => {
  if(updateOfflineData.value.seclevel == "") {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if(!updateOfflineData.value.copyprotdt){
    alert('사본 보호기간을 선택해주세요.');
    return false;
  }
  if(updateOfflineData.value.copyreclass == ""){
    alert('사본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if(updateOfflineData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
};

 // 1) bms_dct_rdoc table
 const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    updateOfflineData.value.docttl = data.docttl;
    updateOfflineData.value.gubun = data.gubun;
    updateOfflineData.value.state = data.state;
    updateOfflineData.value.open = data.open;
    updateOfflineData.value.readrangetype = data.readrangetype;
    updateOfflineData.value.emergency = data.emergency;
    updateOfflineData.value.keyword = data.keyword;
    updateOfflineData.value.authorname = data.authorname;
    updateOfflineData.value.authorid = data.authorid;
    updateOfflineData.value.authordutyname = data.authordutyname;
    updateOfflineData.value.authordeptname = data.authordeptname;
    updateOfflineData.value.tempflag = data.tempflag;
    updateOfflineData.value.indt = data.indt;
    updateOfflineData.value.resdocid = data.docid;
    updateOfflineData.value.paperflag = data.paperflag;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

 // 3) bms_dct_attr table
 const getDctAttr = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    updateOfflineData.value.secgubun = data.secgubun;
    updateOfflineData.value.seclevel = data.seclevel;
    updateOfflineData.value.copyprotdt = data.copyprotdt;
    updateOfflineData.value.copynextlevel = data.copynextlevel;
    updateOfflineData.value.copyreclass = data.copyreclass ? (data.copyreclass.trim() != "" ? data.copyreclass : '') : '';
    updateOfflineData.value.docno = data.docno;
    updateOfflineData.value.mgmtno = data.mgmtno;
    updateOfflineData.value.copyno = data.copyno;
    updateOfflineData.value.docpagenum = data.docpagenum;
    if(updateOfflineData.value.copyprotdt){
      sliceDates(updateOfflineData.value.copyprotdt, copyDate.value);
      copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(updateOfflineData.value.indt.slice(0,4))).toString();
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const sliceDates = (dateString, target) =>{
  const year = parseInt(dateString.slice(0, 4));
  const month = parseInt(dateString.slice(4, 6));
  const day = parseInt(dateString.slice(6, 8));
  target.year = year;
  target.month = month;
  target.day = day;
};

 // 3-1) bms_dct_attr_add table
 const getDctAttrAdd = async () => {
  try {
    const response = await API.dctAPI.getDctAttrAdd({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    updateOfflineData.value.mediatype = data.mediatype;
    updateOfflineData.value.doctype = data.doctype;
    updateOfflineData.value.locker = data.locker;
    updateOfflineData.value.unitnum = data.unitnum;
    updateOfflineData.value.proddocnum = data.proddocnum;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

 // 4) bms_dct_path table
 const selectDctPath  = async () => {
  try {
    const response = await API.dctAPI.selectDctPath ({
      ...docidData.value },
      urlPaths.value
    );
    updateOfflineData.value.pathList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.askTemp = transformAsktype(response.data[i].asktype);
      upd.asktype = response.data[i].asktype;
      upd.deptid = response.data[i].deptid;
      upd.deptname = response.data[i].deptname;
      upd.dutyname = response.data[i].dutyname;
      upd.dutynamedesc = response.data[i].dutynamedesc;
      upd.electsignid = response.data[i].electsignid;
      upd.reportdt = response.data[i].reportdt != null ? dayjs(response.data[i].reportdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm') : "";
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.objid = response.data[i].objid;
      upd.objname = response.data[i].objname;
      upd.sign = response.data[i].reportdt != null ? response.data[i].objname : "";
      upd.opinion = response.data[i].opinion;
      upd.pathstate = response.data[i].state;

      // 처리결과 
      upd.acttype = response.data[i].acttype;
      if (upd.reportdt != "" && upd.reportdt != null && upd.reportdt != undefined) {
        if (upd.asktype == "DEA11") {
          upd.reportresult = "기안";
        } else {
          if (upd.acttype == "DEA43" && (upd.asktype == "DEA41" || upd.asktype == "DEA51" || upd.asktype == "DEA61" || upd.asktype == "DEA71")) {
            upd.reportresult = "반려"
          } else {
            upd.reportresult = "승인";
          }
        }
      } else {
        upd.reportresult = "";
      }

      updateOfflineData.value.pathList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

 // 6) bms_dct_appr_recv
 const getDctRecv = async () => {
  try {
    const response = await API.dctAPI.getDctRecv({
      ...docidData.value },
      urlPaths.value
    );
    updateOfflineData.value.recvList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].empflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      upd.apprrecvgubun = response.data[i].gubun;
      upd.gubun = response.data[i].gubun;
      upd.relaytype = response.data[i].relaytype;
      updateOfflineData.value.recvList.push(upd);
    }
    updateOfflineData.value.senddate = dayjs(response.data[0].actdt).format('YYYY-MM-DD');
    updateOfflineData.value.sendOrgNameData = response.data[0].distdeptname;
    updateOfflineData.value.sendNameData = response.data[0].distname
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 수신자
const formatRecvList = computed(() => {
  const formattedRecvList = updateOfflineData.value.recvList.map((item) => {
    const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
    return `${item.apprrecvdeptname}${refDeptName}`;
  });
  const uniqueFormattedRecvList = [...new Set(formattedRecvList)];
  return uniqueFormattedRecvList.join(', ');
});


 // 7) bms_dct_rdoc_rcvs
 const selectDctReader = async () => {
  try {
    const response = await API.dctAPI.selectDctReader({
      ...docidData.value },
      urlPaths.value
    );
    updateOfflineData.value.rcvsList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptid = response.data[i].deptid;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsdeptnamedesc = response.data[i].deptnamedesc;
      upd.rcvsinseq = response.data[i].inseq;
      updateOfflineData.value.rcvsList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 열람권자
const formatRcvsList = computed(() => {
  const formattedRcvsList = updateOfflineData.value.rcvsList.map((item) => {
    return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
  });
  return formattedRcvsList.join(', ');
});


 // 10) bms_dct_file table
 const selectDctFile = async () => {
  try {
    const response = await API.dctAPI.selectDctFile ({
      ...docidData.value },
      urlPaths.value
    );
    updateOfflineData.value.fileList = [];
    updateOfflineData.value.draftfile = [];
    for(let i=0; i<response.data.length; i++){
      if(response.data[i].gubun == 'DFT02') { // 붙임파일만
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
        // updateOfflineData.value.fileList.push(upd);

        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT02') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          updateOfflineData.value.fileList.push(upd);
        }
      } else {
        updateOfflineData.value.draftfile.push(response.data[i]);
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

 // 11) bms_dct_enf_doc table
 const getReprocess = async () => {
  try {
    const response = await API.dctAPI.getReprocess({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    updateOfflineData.value.acceptdocnum = data.acceptdeptname + '-S'+data.acceptdocseq;
    updateOfflineData.value.reciveddt = dayjs(data.indt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

let checkYN = "Y"; // 수정필요
const me = ref({asktype: "", pathorder: "", state: "", objid: ""});
const checkOrder = async() => {
  // 내가 결재순번인지 체크
  try {
    const response = await API.dctAPI.selectDctPath({
      ...docidData.value
    },
      urlPaths.value
    );

    for (let i = 0; i < response.data.length; i++) {
      if (response.data[i].objid == getUserLoginData.value.userid) {
        me.value.asktype = response.data[i].asktype;
        me.value.pathorder = response.data[i].pathorder;
        me.value.state = response.data[i].state;
        me.value.objid = response.data[i].objid;
        updateOfflineData.value.opinionpathorder = me.value.pathorder;
      }
    }

    for (let i = 0; i < response.data.length; i++) {
      if(response.data[i].pathorder < me.value.pathorder) {
        if (response.data[i].asktype == 'DEA11') {
          checkYN = "Y";
        } else {
          checkYN = "N";
          if (response.data[i].state == 'DCST5') {
            checkYN = "Y";
          }
        }
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

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


// 목록
const moveToList = () => {
  const page = getFromPage.value || "BmsDctmgmtregilist";
  router.push({
    name: page,
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