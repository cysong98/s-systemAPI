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
      <h4 class="h4">기안</h4>
    </div>


    <div class="box-content">
      <!-- 비밀작성 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비밀작성</h5>
          <div class="action-buttons">
            <v-btn class="button" disabled @click="writeDraftDoc()">기안문작성</v-btn>
            <v-btn class="button" disabled @click="dctReqProcPopupToggleFunc()">문서처리</v-btn>
            <v-btn class="button" disabled @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn class="button" disabled @click="dctRecvPopupToggleFunc()">수신지정</v-btn>
            <v-btn class="button" disabled @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
            <v-btn class="button" disabled @click="dctSymbolPopupToggleFunc()">로고/심볼</v-btn>
            <v-btn class="button" disabled @click="tempSave()">임시저장</v-btn>
            <v-btn class="button" @click="moveToBmsDcttodolistread()">취소</v-btn>
          </div>
        </div>
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
                  v-model="showCreateCardData.seclevel"
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
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
              <th>사본번호</th>
              <td>
                <v-text-field
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th rowspan="3"><span class="required">예고문</span></th>
              <td colspan="5">
                <!-- 예고문 - 보존기간 -->
                <div class="box-flex">
                  <v-label>보존기간:</v-label>
                  <v-select
                    v-model="showCreateCardData.prsrvterm"
                    label=""
                    :items="prsRvTermData"
                    item-title="prsRvTermDataKey"
                    item-value="prsRvTermDataValue"
                    required
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </div>
              </td>
            </tr>
            <tr>
              <td colspan="5">
                <!-- 예고문 - 원본보호기간 -->
                <div class="box-flex">
                  <v-label>원본보호기간:</v-label>
                  <v-select
                    v-model="orgnProtDtSelected"
                    label=""
                    :items="orgnProtDtData"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-select>
                  <v-text-field
                    v-if="orgnProtDtSelected == '직접입력'"
                    v-model="orgnUserDate"
                    type="date"
                    @update:modelValue="getOrgnUserDate"
                    variant="outlined"
                    hide-details="auto">
                  </v-text-field>
                  <template v-else>
                    <v-select
                      v-model="orgnProtDtPeriodSelected"
                      label=""
                      :items="orgnProtDtPeriodData"
                      @update:modelValue="getOrgnDate"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                    <v-text-field
                      v-model="orgnDate.year"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto"
                      ></v-text-field>
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
                    <v-select
                      v-model="orgnReclassSelected"
                      label=""
                      :items="orgnReclass"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                    <v-label>(으)로</v-label>
                    <v-select
                      v-model="showCreateCardData.orgnreclass"
                      label=""
                      :items="orgnReclassData"
                      @update:modelValue="selectedOrgnReClass"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                  </template>
                </div>
              </td>
            </tr>
            <tr>
              <td colspan="5">
                <div class="box-flex">
                  <v-label>사본보호기간:</v-label>
                  <v-select
                    v-model="copyProtDtSelected"
                    label=""
                    :items="copyProtDtData"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-select>
                  <v-text-field
                    v-if="copyProtDtSelected == '직접입력'"
                    v-model="copyUserDate"
                    type="date"
                    @update:modelValue="getCopyUserDate"
                    variant="outlined"
                    hide-details="auto">
                  </v-text-field>
                  <template v-else>
                    <v-select
                      v-model="copyProtDtPeriodSelected"
                      label=""
                      @update:modelValue="getCopyDate"
                      :items="copyProtDtPeriodData"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
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
                    <v-select
                      v-model="copyReclassSelected"
                      label=""
                      :items="copyReclass"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                    <v-label>(으)로</v-label>
                    <v-select
                      v-model="showCreateCardData.copyreclass"
                      label=""
                      :items="copyReclassData"
                      @update:modelValue="selectedCopyReClass"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                  </template>
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
                  v-model="showCreateCardData.docttl"
                  label=""
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
              <th>긴급도</th>
              <td>
                <v-select
                  v-model="showCreateCardData.emergency"
                  label=""
                  :items="emergencyData"
                  item-title="emergencyDataKey"
                  item-value="emergencyDataValue"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-select>
              </td>
            </tr>
            <tr>
              <th>
                <div class=box-flex>
                  관련근거
                  <v-btn size="xsmall" class="magnify" disabled @click.stop="dctReferencePopupToggleFunc">
                    <v-icon></v-icon>
                  </v-btn>
                </div>
              </th>
              <td>
                <div v-for="refData, idx in showCreateCardData.refList" :key="idx">
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
                    <span style="margin-left: 80px">ㄴ-- {{refData.sfilename}}</span>
                  </div>
                </div>
              </td>
              <th>검색어</th>
              <td>
                <v-text-field
                  v-model="showCreateCardData.keyword"
                  label=""
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>문서취지</th>
              <td colspan="3">
                <v-text-field
                  v-model="showCreateCardData.summary"
                  label=""
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>기안문</th>
              <td colspan="3">
                <div class="box-flex">
                  <v-text-field
                    v-model="draftDocInputData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                    <v-btn class="btn" disabled text-color="black" @click="writeDraftDoc()">작성</v-btn>
                    <v-btn class="btn" disabled text-color="black" @click="deleteDraftDoc()">삭제</v-btn>
                </div>
              </td>
            </tr>
            <tr>
              <th>
                <div class="box-flex">
                  붙임<v-btn disabled @click="addAttach()">추가</v-btn>
                </div>
              </th>
              <td colspan="3">
                <div v-if="showCreateCardData.fileList != []">
                  <div class="box-flex" v-for="(fileData,idx) in showCreateCardData.fileList" :key="idx">
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

      <!-- 결재선정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">결재선정보</h6>
          <div class="action-buttons">
            <v-btn class="button" disabled @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
          </div>
        </div>
        <v-table class="table-type-02">
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
            <tr v-for="pathData, idx in showCreateCardData.pathList" :key="idx">
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
                {{ pathData.mainver }}
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 시행정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">시행정보</h6>
          <div class="action-buttons">
            <v-btn class="button" disabled @click="dctRecvPopupToggleFunc()">수신지정</v-btn>
          </div>
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
                  variant="outlined"
                  hide-details="auto"
                  ></v-select>
              </td>
              <th>발신명의</th>
              <td>
                <v-select
                  class="w100"
                  v-model="sendNameData"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                  ></v-select>
              </td>
            </tr>
            <tr>
              <th>수신자</th>
              <td colspan="3">
                <v-text-field
                  v-model="formatRecvList"
                  label="부서명"
                  :readonly="true"
                  variant="solo"
                  hide-details="auto"
                >
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
            <v-btn class="button" disabled @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
          </div>
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
                  v-model="formatRcvsList"
                  variant="solo"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>

    <!-- 문서처리 팝업 -->
    <v-dialog v-model="bVisibleDctReqProcPopup" style="width: 800px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>문서처리</v-toolbar-title>
          <v-btn class="close" @click="bVisibleDctReqProcPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctReqProcPopup
          v-if="bVisibleDctReqProcPopup"
          :args="dctReqProcPopupArgs"
          :toggleFunc="dctReqProcPopupToggleFunc"
          :returnFunc="dctReqProcPopupReturnFunc"
        ></DctReqProcPopup>
      </v-card>
    </v-dialog>

    <!-- 결재선지정 팝업 -->
    <v-dialog v-model="bVisibleDctPathPopup" style="width: 850px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>결재선지정</v-toolbar-title>
          <v-btn class="close" @click="bVisibleDctPathPopup = false">
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
          <v-toolbar-title>수신처지정</v-toolbar-title>
          <v-btn class="close" @click="bVisibleDctRecvPopup = false">
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
          <v-btn class="close" @click="bVisibleDctReaderPopup = false">
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

    <!-- 로고/심볼 팝업 -->
    <v-dialog v-model="bVisibleDctSymbolPopup" style="width: 1000px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>로고/심볼 지정</v-toolbar-title>
          <v-btn class="close" @click="bVisibleDctSymbolPopup = false">
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

      <!-- 관련근거 팝업 -->
      <v-dialog v-model="bVisibleDctReferencePopup" style="width: 1000px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>관련근거</v-toolbar-title>
          <v-btn class="close" @click="bVisibleDctReferencePopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctReferencePopup
          v-if="bVisibleDctReferencePopup"
          :args="dctReferencePopupArgs"
          :toggleFunc="dctReferencePopupToggleFunc"
          :returnFunc="dctReferencePopupReturnFunc"
        ></DctReferencePopup>
      </v-card>
    </v-dialog>
  </div>
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
import DctReqProcPopup from "./DctReqProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctRecvPopup from "./DctRecvPopup.vue";
import DctReferencePopup from "./DctReferencePopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctSymbolPopup from "./DctSymbolPopup.vue";
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
import { transformStatus,transformState, transformAsktype } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('BmsDctrdocdetailcard')

/* ======================== 문서처리 팝업 ======================== */
const bVisibleDctReqProcPopup = ref(false)
const dctReqProcPopupReuturn = ref({})
const dctReqProcPopupArgs = ref({})
const dctReqProcPopupToggleFunc = ref(() => {

  if (makeAlert() == false) {
    return;
  }

  dctReqProcPopupArgs.value = updateCreateCardData.value;
  bVisibleDctReqProcPopup.value = !bVisibleDctReqProcPopup.value; 
})
const dctReqProcPopupReturnFunc = ref((retValue) => {
  dctReqProcPopupToggleFunc.value()
  dctReqProcPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref(() => { 
  bVisibleDctPathPopup.value = !bVisibleDctPathPopup.value;
})
const dctPathPopupReturnFunc = ref((retValue) => {
  dctPathPopupToggleFunc.value()
  if(retValue != ""){
    updateCreateCardData.value.pathList = [];

    const initialPath = {};
    initialPath.askTemp = "기안";
    initialPath.asktype = "DEA11";
    initialPath.deptname = getUserLoginData.value.deptname;
    initialPath.dutyname = getUserLoginData.value.username;
    initialPath.dutynamedesc = getUserLoginData.value.grade+"/"+getUserLoginData.value.username;
    initialPath.mainver = "1";
    initialPath.pathorder = "1";
    initialPath.pathstate = 'DCST1';
    updateCreateCardData.value.pathList.push(initialPath);

    for(let i = 0; i < retValue.length; i++){
      const path = {};
      path.askTemp = retValue[i].askTemp;
      path.asktype = retValue[i].asktype;
      path.deptname = retValue[i].orgname;
      path.dutyname = retValue[i].grade;
      path.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      path.pathorder = i+2;
      path.pathstate = 'DCST1';
      updateCreateCardData.value.pathList.push(path);
    }
  }
})
/* ============================================================ */

/* ======================== 수신지정 팝업 ======================== */
const bVisibleDctRecvPopup = ref(false)
const dctRecvPopupReuturn = ref({})
const dctRecvPopupArgs = ref({})
const dctRecvPopupToggleFunc = ref(() => { 
  bVisibleDctRecvPopup.value = !bVisibleDctRecvPopup.value;
})
const dctRecvPopupReturnFunc = ref((retValue) => {
  dctRecvPopupToggleFunc.value()
  if(retValue != ""){
    updateCreateCardData.value.recvList = [];
    for(let i = 0; i < retValue.length; i++){
      const receiver = {};
      receiver.receiverid = retValue[i].receiverid;
      receiver.apprrecvtempflag = retValue[i].apprrecvtempflag;
      receiver.apprrecvseq = i+1;
      receiver.apprrecvgubun = retValue[i].apprrecvgubun;
      receiver.apprrecvdeptname = retValue[i].apprrecvdeptname;
      receiver.apprrecvrefdeptname = retValue[i].apprrecvrefdeptname;
      updateCreateCardData.value.recvList.push(receiver);
    }
  }
});
/* ============================================================ */

/* ======================== 열람지정 팝업 ======================== */
const bVisibleDctReaderPopup = ref(false)
const dctReaderPopupReuturn = ref({})
const dctReaderPopupArgs = ref({})
const dctReaderPopupToggleFunc = ref(() => { 
  bVisibleDctReaderPopup.value = !bVisibleDctReaderPopup.value;
  reader.value = "";
})
const reader = ref("");
const dctReaderPopupReturnFunc = ref((retValue) => {
  dctReaderPopupToggleFunc.value()
  if(retValue != ""){
    updateCreateCardData.value.rcvsList = [];
    for(let i = 0; i < retValue.length; i++){
      const rcvs = {};
      rcvs.rcvsuserid = retValue[i].userid;
      rcvs.rcvsusername = retValue[i].username;
      rcvs.rcvsinseq = i+1;
      rcvs.rcvdeptid = retValue[i].orgid;
      rcvs.rcvdeptname = retValue[i].deptname;
      rcvs.rcvdutyname = retValue[i].grade;
      updateCreateCardData.value.rcvsList.push(rcvs);
    }
  }
})
/* ============================================================ */

/* ======================== 로고/심볼 팝업 ======================== */
const bVisibleDctSymbolPopup = ref(false)
const dctSymbolPopupReuturn = ref({})
const dctSymbolPopupArgs = ref({})
const dctSymbolPopupToggleFunc = ref(() => { 
  bVisibleDctSymbolPopup.value = !bVisibleDctSymbolPopup.value;
})
const dctSymbolPopupReturnFunc = ref((retValue) => {
  dctSymbolPopupToggleFunc.value()
  dctSymbolPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 관련근거 팝업 ======================== */
const bVisibleDctReferencePopup = ref(false)
const dctReferencePopupReuturn = ref({})
const dctReferencePopupArgs = ref({})
const dctReferencePopupToggleFunc = ref(() => { 
  bVisibleDctReferencePopup.value = !bVisibleDctReferencePopup.value;
})
const dctReferencePopupReturnFunc = ref((retValue) => {
  dctReferencePopupToggleFunc.value()
  updateCreateCardData.value.refList = [];
  dctReferencePopupReuturn.value = retValue;
  for(let idx = 0; idx < retValue.length; idx++){
    const self = {};
    const choice= {}; 
    if(retValue[idx].gubunTemp == "직접입력"){
      self.gubunTemp = retValue[idx].gubunTemp,
      self.infotext = retValue[idx].docttl,
      self.infotitle = retValue[idx].docttl,
      self.infotype = "P",
      self.infomationid = "INFO" + dayjs().format('YYYYMMDDHHmmss')+"_"+(idx+1),
      self.resultid = "RESULT" + dayjs().format('YYYYMMDDHHmmss')+"_"+(idx+1),
      updateCreateCardData.value.refList.push(self);
    }else{
      choice.gubunTemp = retValue[idx].gubunTemp,
      choice.infotext = retValue[idx].docid,
      choice.infotitle = retValue[idx].docttl,
      choice.infotype = "T",
      choice.infomationid = "INFO" + dayjs().format('YYYYMMDDHHmmss')+"_"+(idx+1),
      choice.resultid = "RESULT" + dayjs().format('YYYYMMDDHHmmss')+"_"+(idx+1),
      updateCreateCardData.value.refList.push(choice);
    }
  }
})
/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');
const params = ref({});
const query = ref({});
const docidData = ref({docid: ""});

const showCreateCardData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
  open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
  readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
  emergency: "", // 긴급도 (대내: N, 대외: Y, 대내외: T)
  keyword: "", // 키워드
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
  resuserid: "",

});

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  docidData.value.docid = query.value.docid;
  showCreateCardData.value.resdocid = query.value.docid;
});

onMounted( async () => {
  await getDctRdoc();
  await getDctSummary();
  await getDctAttr();
  await selectDctPath();
  await getDctRecv();
  await selectDctReader();
  await selectTaskInfo();
  await selectDctFile();
})

watchEffect(() => {
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
  showCreateCardData.value.orgnprotdt = orgnDate.value.year+orgnDate.value.month+orgnDate.value.day;
}

// 직접엽력
const orgnUserDate = ref("");
const getOrgnUserDate = () => {
  showCreateCardData.value.orgnprotdt = orgnUserDate.value.replace(/-/g, '');
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
const getCopyDate = () => {
  const currentDate = new Date();
  const currentYear = currentDate.getFullYear();
  copyDate.value.year = currentYear + parseInt(copyProtDtPeriodSelected.value);
  copyDate.value.month = '12';
  copyDate.value.day = '31';
  showCreateCardData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
}

// 직접엽력
const copyUserDate = ref("");
const getCopyUserDate = () => {
  showCreateCardData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
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
const sendOrgNameData = ref([]);

// 시행정보 발신명의 Select-Box
const sendNameData = ref([]);

// 관리정보 문서번호 Text-Field
const docNameInputData = ref("");

// 기안문 작성 버튼 (임시)
const writeDraftDoc = ref(() => {
  alert('기안문작성 Popup');
});


const makeAlert = () => {
  if (showCreateCardData.value.seclevel == "") {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if(showCreateCardData.value.prsrvterm == ''){
    alert('예고문 보호기간을 선택해주세요.');
    return false;
  }
  if(orgnProtDtPeriodSelected.value == ''){
    alert('원본 보호기간을 선택해주세요.');
    return false;
  }
  if(showCreateCardData.value.orgnreclass == ''){
    alert('원본 보호기간을 조건을 선택해주세요.');
    return false;
  }
  if(copyProtDtPeriodSelected.value == ''){
    alert('사본 보호기간을 선택해주세요.');
    return false;
  }
  if(showCreateCardData.value.copyreclass == ''){
    alert('사본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if(parseInt(showCreateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)){
    alert('예고문 보호기간은 원본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if(parseInt(showCreateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)){
    alert('예고문 보호기간은 사본 보호기간보다 작을 수 없습니다.');
    return false;
  }

  if(parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    return false;
  }

  if (showCreateCardData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
};


const tempSave = ref(() => {
  
  // 필수 입력 값 > 비밀등급 선택
  if (showCreateCardData.value.seclevel == "") {
    alert('비밀등급을 선택해주세요.');
    return;
  }

  // 필수 입력 값 > 제목 입력
  if (showCreateCardData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return;
  }

  API.dctAPI
  .insertTempList(showCreateCardData.value, urlPaths.value)
  .then(response => {
    if (response.status == 200) {
      alert("임시 저장되었습니다");
      window.location.reload();
    }
  })
  .catch(error => {
    console.log(error);
  });

});

 // 1) bms_dct_rdoc table
const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    showCreateCardData.value.docttl = data.docttl;
    showCreateCardData.value.gubun = data.gubun;
    showCreateCardData.value.state = data.state;
    showCreateCardData.value.open = data.open;
    showCreateCardData.value.readrangetype = data.readrangetype;
    showCreateCardData.value.emergency = data.emergency;
    showCreateCardData.value.keyword = data.keyword;
    showCreateCardData.value.authorname = data.authorname;
    showCreateCardData.value.authordutyname = data.authordutyname;
    showCreateCardData.value.authordeptname = data.authordeptname;
    showCreateCardData.value.tempflag = data.tempflag;
    sendOrgNameData.value = data.authordeptname;
    sendNameData.value = data.authordeptname+" 부서장";
    
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

 // 2) bms_dct_summary table
 const getDctSummary  = async () => {
  try {
    const response = await API.dctAPI.getDctSummary({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    showCreateCardData.value.docgubun = data.docgubun;
    showCreateCardData.value.summary = data.summary;
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
    showCreateCardData.value.secgubun = data.secgubun;
    showCreateCardData.value.seclevel = data.seclevel;
    showCreateCardData.value.prsrvterm = data.prsrvterm;
    showCreateCardData.value.orgnprotdt = data.orgnprotdt;
    showCreateCardData.value.orgnnextlevel = data.orgnnextlevel;
    showCreateCardData.value.orgnreclass = data.orgnreclass;
    showCreateCardData.value.copyprotdt = data.copyprotdt;
    showCreateCardData.value.copynextlevel = data.copynextlevel;
    showCreateCardData.value.copyreclass = data.copyreclass;

    sliceDates(showCreateCardData.value.orgnprotdt, orgnDate.value);
    sliceDates(showCreateCardData.value.copyprotdt, copyDate.value);

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
}

 // 4) bms_dct_path table
 const selectDctPath  = async () => {
  try {
    const response = await API.dctAPI.selectDctPath ({
      ...docidData.value },
      urlPaths.value
    );
    showCreateCardData.value.pathList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.askTemp = transformAsktype(response.data[i].asktype);
      upd.asktype = response.data[i].asktype;
      upd.deptname = response.data[i].deptname;
      upd.dutyname = response.data[i].dutyname;
      upd.dutynamedesc = response.data[i].dutynamedesc;
      upd.electsignid = response.data[i].electsignid;
      upd.reportdt = dayjs(response.data[i].reportdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD');
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.opinion = response.data[i].opinion;
      upd.pathstate = response.data[i].pathstate;
      showCreateCardData.value.pathList.push(upd);
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
    showCreateCardData.value.recvList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].empflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      showCreateCardData.value.recvList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const formatRecvList = computed(() => {
  const formattedRecvList = showCreateCardData.value.recvList.map((item) => {
    return `${item.apprrecvdeptname}(${item.apprrecvrefdeptname})`;
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
    showCreateCardData.value.rcvsList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsdeptnamedesc = response.data[i].deptnamedesc;
      upd.rcvsinseq = response.data[i].inseq;
      showCreateCardData.value.rcvsList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const formatRcvsList = computed(() => {
  const formattedRcvsList = showCreateCardData.value.rcvsList.map((item) => {
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
    showCreateCardData.value.refList = [];
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
      showCreateCardData.value.refList.push(upd);
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
    showCreateCardData.value.fileList = [];
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
      showCreateCardData.value.fileList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 취소 버튼 Move FN (비밀열람함 페이지로 이동)
const moveToBmsDcttodolistread = () => {
  router.push({
    name: "BmsDcttodolistread",
  });
};
</script>

