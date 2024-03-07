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
          <h5 v-if="draftToggle.data == 'N'" class="h5">비밀작성</h5>
          <h5 v-else class="h5">기안문작성</h5>
          <div class="action-buttons">
            <v-btn v-if="draftToggle.data == 'N'" class="button" @click="writeDraftDoc()">기안문작성</v-btn>
            <v-btn class="button" @click="dctReqProcPopupToggleFunc()">문서처리</v-btn>
            <v-btn class="button" @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn :disabled="chkbox == true" class="button" @click="dctRecvPopupToggleFunc()">수신지정</v-btn>
            <v-btn class="button" @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
            <v-btn class="button" @click="dctSymbolPopupToggleFunc()">로고/심볼</v-btn>
            <v-btn class="button" @click="dctTempPopupToggleFunc()">임시저장</v-btn>
            <v-btn class="button" @click="moveToBmsTodolist()">목록</v-btn>
          </div>
        </div>
      </section>

      <div v-show="draftToggle.data == 'N'">
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
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">비밀등급</span></th>
                <td>
                  <v-select
                    v-model="insertCreateCardData.seclevel"
                    :items="secLevelData"
                    item-title="secLevelDataKey"
                    item-value="secLevelDataValue"
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
                <th>내부생성<br>(사본없음)</th>
                <td><v-checkbox class="v-checkbox-square-btn" v-model="chkbox" hide-details="auto" @update:modelValue="noCopy"></v-checkbox></td>
              </tr>
              <tr>
                <th rowspan="3"><span class="required">예고문</span></th>
                <td colspan="7">
                  <!-- 예고문 - 보존기간 -->
                  <div class="box-flex">
                    <v-label>보존기간:</v-label>
                    <v-select
                      v-model="insertCreateCardData.prsrvterm"
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
                        v-model="insertCreateCardData.orgnreclass"
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
              <tr v-if="chkbox == false">
                <td colspan="7">
                  <!-- 예고문 - 사본보호기간 -->
                  <div class="box-flex">
                    <v-label>사본보호기간:</v-label>
                    <v-select
                      v-model="copyProtDtSelected"
                      @update:modelValue="updateCopyDt"
                      :items="copyProtDtData"
                      variant="outlined"
                      hide-details="auto"></v-select>
                    <v-text-field
                      v-if="copyProtDtSelected == '직접입력'"
                      v-model="copyUserDate"
                      type="date"
                      :min="today"
                      @update:modelValue="getCopyUserDate"
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
                        v-model="insertCreateCardData.copyreclass"
                        :items="copyReclassData"
                        @update:modelValue="selectedCopyReClass"
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
              <col width="">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">제목</span></th>
                <td>
                  <v-text-field
                    v-model="insertCreateCardData.docttl"
                    :rules="[rules.required, rules.counter]"
                    maxlength="500"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    v-model="insertCreateCardData.emergency"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
              </tr>
              <tr>
                <th>
                  관련근거
                  <v-btn size="xsmall" class="magnify" @click.stop="dctReferencePopupToggleFunc">
                    <v-icon></v-icon>
                  </v-btn>
                </th>
                <td>
                  <div v-for="refData, idx in insertCreateCardData.refList" :key="idx">
                    <div class="box-flex">
                      <span v-if="refData.gubunTemp">{{ refData.gubunTemp }}</span>
                      <v-text-field
                        v-model="refData.infotitle"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto">
                      </v-text-field>
                      <v-btn @click="deleteRef(idx)" class="del-btn"><v-icon>mdi-close</v-icon></v-btn>
                    </div>
                    <div v-if="refData.file != undefined && refData.file != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.file.name)).color">{{ getFileIcon(getExtension(refData.file.name)).icon }}</v-icon>
                          <span  @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.file.name }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-model="insertCreateCardData.keyword"
                    variant="outlined"
                    :rules="[rules.counter]"
                    maxlength="500"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>문서취지</th>
                <td colspan="3">
                  <v-text-field
                    v-model="insertCreateCardData.summary"
                    :rules="[rules.summarycounter]"
                    maxlength="4000"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>기안문</th>
                <td colspan="3">
                  <div class="box-flex" v-if="insertCreateCardData.draftdocttl">
                    <v-icon :color="getFileIcon(getExtension(insertCreateCardData.draftdocttl)).color">{{ getFileIcon(getExtension(insertCreateCardData.draftdocttl)).icon }}</v-icon>
                    <span @click="writeDraftDoc()" style="font-weight: 600; cursor: pointer;">{{ insertCreateCardData.draftdocttl }}</span>
                      <v-btn @click="deleteDraftDoc()" class="del-btn"><v-icon>mdi-close</v-icon></v-btn>
                  </div>
                  <div class="box-flex justify-end" v-else>
                    <v-btn class="btn" text-color="black" @click="writeDraftDoc()">작성</v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="3">
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
      
        <!-- 시행정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">시행정보</h6>
            <div class="action-buttons">
              <v-btn v-if="chkbox == false" class="button" @click="dctRecvPopupToggleFunc()">수신지정</v-btn>
              <v-btn v-else-if="chkbox == true" class="button" disabled @click="dctRecvPopupToggleFunc()">수신지정</v-btn>
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
                    v-model="insertCreateCardData.sendOrgNameData"
                    :items="sendOrgNameData"
                    @update:modelValue="changeSendName"
                    item-title="key"
                    item-value="value"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th>
                  발신명의
                </th>
                <td>
                  <v-text-field
                    class="w100"
                    v-model="insertCreateCardData.sendNameData"
                    variant="outlined"
                    maxlength="100"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>
                  수신자
                </th>
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
              <v-btn class="button" @click="dctReaderPopupToggleFunc()">열람지정</v-btn>
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
      
      <HwpCtrl v-if="draftToggle.data == 'Y'" :data="insertCreateCardData" :toggle="draftToggle" :view="draftView" ref="childRef" :key="compKey"/>
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

    <!-- 문서처리 팝업 -->
    <v-dialog v-if="!insertCreateCardData.saveLoading" v-model="bVisibleDctReqProcPopup" style="width: 800px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>문서처리</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctReqProcPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctReqProcPopup
          v-if="bVisibleDctReqProcPopup"
          :args="dctReqProcPopupArgs"
          :toggleFunc="dctReqProcPopupToggleFunc"
          :returnFunc="dctReqProcPopupReturnFunc"
          :saveFunc="callDraftToggle"
        ></DctReqProcPopup>
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
          <v-toolbar-title>수신처지정</v-toolbar-title>
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

      <!-- 관련근거 팝업 -->
      <v-dialog v-model="bVisibleDctReferencePopup" style="width: 1000px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>관련근거</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctReferencePopup = false">
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

    <!-- 임시저장 팝업 -->
    <v-dialog v-if="!insertCreateCardData.saveLoading" v-model="bVisibleDctTempPopup" style="width: 650px;">
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
  </div>

  <div v-if="insertCreateCardData.saveLoading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight, topMenu, breadcrumbs } = storeToRefs(mainStore)

import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import DctReqProcPopup from "./DctReqProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctRecvPopup from "./DctRecvPopup.vue";
import DctReferencePopup from "./DctReferencePopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctSymbolPopup from "./DctSymbolPopup.vue";
import DctTempPopup from "./DctTempPopup.vue";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
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

// 기안문 관련
import HwpCtrl from "./HwpCtrl.vue";
const childRef = ref(null);

const callDraftToggle = (data, apprSave) => {
  if (childRef.value) {
    childRef.value.draftToggle(data, apprSave);
  }
}

const name = ref('BmsDctCreateCard')

/* ======================== 문서처리 팝업 ======================== */
const bVisibleDctReqProcPopup = ref(false)
const dctReqProcPopupReuturn = ref({})
const dctReqProcPopupArgs = ref({})
const dctReqProcPopupToggleFunc = ref(() => {
  if (makeAlert() == false) {
    return;
  }

  // 저장로딩 true
  insertCreateCardData.value.saveLoading = true;

  if (draftToggle.value.data == 'N') {
    if (!insertCreateCardData.value.draftcontent) {
      // 저장로딩 false
      insertCreateCardData.value.saveLoading = false;
      alert('본문을 작성해주세요.');
      return;
    }
    draftToggle.value.data = 'Y';
  } else {
    callDraftToggle('Y');
    
    if (!insertCreateCardData.value.draftcontent) {
      // 저장로딩 false
      insertCreateCardData.value.saveLoading = false;
      alert('본문을 작성해주세요.');
      return;
    }
  }

  if(chkbox.value == true){
    insertCreateCardData.value.gubun = 'DOPT1'
  }else{
    insertCreateCardData.value.gubun = 'DOPT2'
  }
  
  dctReqProcPopupArgs.value = insertCreateCardData.value;
  dctReqProcPopupArgs.value.files = newAttachFileArray.value;
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
  const pathList = [...insertCreateCardData.value.pathList];
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
      path.objid = retValue[i].userid;
      path.objname = retValue[i].username;
      path.deptid = retValue[i].orgid;
      path.deptname = retValue[i].orgname;
      path.dutyname = retValue[i].grade;
      path.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      path.mainver = 1;
      path.pathorder = i+2;
      path.pathstate = 'DCST1';
      insertCreateCardData.value.pathList.push(path);
    };

    const requests = []; // 기안
    const reviewers = []; // 검토, 협조
    const approvers = []; // 전결, 결재

    // '기안','검토', ... , '결재'에 따라 재정렬
    insertCreateCardData.value.pathList.forEach((path) => {
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

    insertCreateCardData.value.pathList = requests.concat(reviewers,approvers);
    updatePathOrder();
  }else{
    insertInitialPath();
  }

  callDraftToggle("Y");
});

const insertInitialPath = () => {
  insertCreateCardData.value.pathList = [];
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
  initialPath.pathstate = 'DCST1';
  insertCreateCardData.value.pathList.push(initialPath);
};

const sortedPathList = computed(() => {
  return insertCreateCardData.value.pathList.sort((a, b) => {
    return a.pathorder - b.pathorder;
  });
});

const updatePathOrder = () => {
  insertCreateCardData.value.pathList.forEach((path, index) => {
      path.pathorder = index + 1;
  });
};

/* ============================================================ */

/* ======================== 수신지정 팝업 ======================== */
const bVisibleDctRecvPopup = ref(false)
const dctRecvPopupReuturn = ref({})
const dctRecvPopupArgs = ref({})
const dctRecvPopupToggleFunc = ref(() => { 
  if(chkbox.value == false) {
    insertCreateCardData.value.gubun = 'DOPT2'
  } else {
    insertCreateCardData.value.gubun = 'DOPT1'
  }

  const recvList = [...insertCreateCardData.value.recvList];
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
      relaytype: item.relaytype, //유통망 정보
    };
  });
  dctRecvPopupArgs.value = modifiedRecvList;
  bVisibleDctRecvPopup.value = !bVisibleDctRecvPopup.value;
})
const dctRecvPopupReturnFunc = ref((retValue) => {
  dctRecvPopupToggleFunc.value()
  if(retValue != ""){
    insertCreateCardData.value.recvList = [];
    for(let i = 0; i < retValue.length; i++){
      const receiver = {};
      receiver.receiverid = retValue[i].receiverid;
      receiver.apprrecvtempflag = retValue[i].apprrecvtempflag;
      receiver.apprrecvseq = i+1;
      receiver.apprrecvgubun = retValue[i].apprrecvgubun;
      receiver.apprrecvgubun = retValue[i].apprrecvgubun;
      receiver.apprrecvdeptname = retValue[i].apprrecvdeptname;
      receiver.apprrecvrefdeptname = retValue[i].apprrecvrefdeptname;
      receiver.relaytype = retValue[i].relaytype; //유통망 정보
      receiver.recsymbol = retValue[i].receiverSymbol;
      insertCreateCardData.value.recvList.push(receiver);
    }
  }else{
    insertCreateCardData.value.recvList = [];
  }
  callDraftToggle("Y");
});

const sendOrgNameData = ref([
  { key: '원자력통제기술원', value: '원자력통제기술원' },
]);

const updateSendOrgName = () => {
  let obj = {};
  obj.key = getUserLoginData.value.deptname;
  obj.value = getUserLoginData.value.deptname;
  sendOrgNameData.value.push(obj);
}

const changeSendName = () => {
  if(insertCreateCardData.value.sendOrgNameData == '원자력통제기술원') {
    insertCreateCardData.value.sendNameData = insertCreateCardData.value.sendOrgNameData + '장'
  } else {
    insertCreateCardData.value.sendNameData = insertCreateCardData.value.sendOrgNameData + '부서장'
  }
}
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

  insertCreateCardData.value.logourl = dctSymbolPopupReuturn.value.logourl;
  insertCreateCardData.value.logopath = dctSymbolPopupReuturn.value.logopath;
  insertCreateCardData.value.symbolurl = dctSymbolPopupReuturn.value.symbolurl;
  insertCreateCardData.value.symbolpath = dctSymbolPopupReuturn.value.symbolpath;

  callDraftToggle("Y");
})
/* ============================================================ */

/* ======================== 관련근거 팝업 ======================== */
const bVisibleDctReferencePopup = ref(false)
const dctReferencePopupReuturn = ref({})
const dctReferencePopupArgs = ref({})
const dctReferencePopupToggleFunc = ref(() => { 
  bVisibleDctReferencePopup.value = !bVisibleDctReferencePopup.value;
  dctReferencePopupArgs.value = insertCreateCardData.value.refList;
})
const dctReferencePopupReturnFunc = ref((retValue) => {
  if (retValue.ref.length == 0) {
    insertCreateCardData.value.refList = [];
  }

  dctReferencePopupToggleFunc.value()
  if(retValue.ref != ""){
    if(insertCreateCardData.value.refList[0] != undefined){
        insertCreateCardData.value.refList = [];
    }
    for(let idx = 0; idx < retValue.ref.length; idx++){
      const self = {};
      const choice= {}; 
      if(retValue.ref[idx].gubunTemp == "직접입력"){
        self.gubunTemp = retValue.ref[idx].gubunTemp,
        self.infotext = retValue.ref[idx].docttl,
        self.infotitle = retValue.ref[idx].docttl,
        self.infotype = "P",
        self.file = retValue.ref[idx].file,
        self.fileexist = retValue.ref[idx].file != '' ? 'Y' : 'N',
        insertCreateCardData.value.refList.push(self);
      }else{
        choice.gubunTemp = retValue.ref[idx].gubunTemp,
        choice.infotext = retValue.ref[idx].docid,
        choice.infotitle = retValue.ref[idx].docttl,
        choice.infotype = "T",
        choice.fileexist = 'N',
        insertCreateCardData.value.refList.push(choice);
      }
    }
  }
});

const deleteRef = (idx) => {
  insertCreateCardData.value.refList.splice(idx,1);
};

// 관련근거 첨부 다운로드
const downloadRefFile = async(idx) => {
  const file = insertCreateCardData.value.refList[idx].file;
  if (file) {
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(file);
    link.download = file.name;
    link.click();
  } else {
    alert('File content not found.');
  }
};
/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');

const today = ref("")
const insertCreateCardData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "DOPT1", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "DRDS1", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
  open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
  readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
  emergency: "N", // 긴급도
  keyword: "", // 키워드
  authorid: "", // 작성자 ID
  authorname: "", // 담당자명
  authordutyname: "", // 담당자 직위직급
  authordeptid: "", // 담당자 부서ID
  authordeptname: "", // 담당자 부서이름
  tempflag: "Y", // 임시 flag (임시: Y, 처리요청: N)

  // 2) bms_dct_summary table
  docgubun: "A", // 비밀구분 (발송: A, 시행: E)
  summary: "", // 요약

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
      apprrecvtempflag: "", // 임시FLAG (실제로는 tempflag 컬럼)
      apprrecvseq: "", // 일련번호 (실제로는 seq 컬럼)
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

  // 8) bms_task_infomation_temp table (문서처리 일때는 bms_task_infomation table)
  refList: [
    {
      infomationid: "", // 정보출처ID
      resultid: "", // 실적ID
      infotext: "", // 정보출처제목 (비밀문서는 docid, 직접입력은 입력받은 근거)
      infotitle: "", // 정보출처제목 (비밀문서는 docttl, 직접입력은 입력받은 근거)
      infotype: "", // 정보출처유형 (비밀문서는 T, 직접입력은 P)
      file: "", // FILE 객체 저장용
      fileexist: "", // 파일여부 체크용
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

  // 기안문 url
  drafturl: "",
  draftdocttl: "",
  draftcontent: "",

  // 로고/심볼 url
  logourl: "",
  logopath: "",
  symbolurl: "",
  symbolpath: "",

  // 시행정보
  sendOrgNameData: "원자력통제기술원",
  sendNameData: "",

  // 붙임파일 관련
  newFileList: [],
  delFileList: [],

  // 웹한글기안기 저장 로딩
  saveLoading: false,

  // 이메일, 전화번호 초기값 세팅용
  email: "",
  phone: "",

  // 비전자문서 flag
  paperflag: "N",
  
});

const rules = {
  required: value => !!value || '제목을 입력해주세요.',
  counter: value => value.length < 500 || '최대 500자 까지 입력하실 수 있습니다.',
  summarycounter: value => value.length < 4000 || '최대 4000자 까지 입력하실 수 있습니다.',
};

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted( async () => {
  window.sessionStorage.setItem('waitFlag', 'N');

  insertCreateCardData.value.authorid = getUserLoginData.value.userid;
  insertCreateCardData.value.authorname = getUserLoginData.value.username;
  insertCreateCardData.value.authordutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.authordeptid = getUserLoginData.value.deptid;
  insertCreateCardData.value.authordeptname = getUserLoginData.value.deptname;

  insertCreateCardData.value.deptid = getUserLoginData.value.deptid;
  insertCreateCardData.value.deptname = getUserLoginData.value.deptname;
  insertCreateCardData.value.dutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.username = getUserLoginData.value.username;

  insertCreateCardData.value.resuserid = getUserLoginData.value.userid;
  today.value = dayjs().format('YYYY-MM-DD');
  insertInitialPath();
  rcvsMe();
  updateSendOrgName();
  changeSendName();
  getUserDetail();
})

watchEffect(() => {
})

const chkbox = ref(false);
const noCopy = () => {
  // 초기화
  if(chkbox.value == true){
    insertCreateCardData.value.copyprotdt = "";
    insertCreateCardData.value.copyreclass = "";
    copyProtDtPeriodSelected.value = "";
    copyDate.value.year = "";
    copyDate.value.month = "";
    copyDate.value.day = "";
  }
};

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

// 보존기간 validation check
const updatePrsrvterm = () => {
  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    insertCreateCardData.value.prsrvterm = "";
    return;
  }
  if((parseInt(insertCreateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)+1) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    insertCreateCardData.value.prsrvterm = "";
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

  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return
  }
  if((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return;
  }
  if(comparisonDate.isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return;
  }
  orgnDate.value.year = futureYear;
  orgnDate.value.month = '12';
  orgnDate.value.day = '31';
  insertCreateCardData.value.orgnprotdt = orgnDate.value.year+orgnDate.value.month+orgnDate.value.day;
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

  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(orgnUserDate.value.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(comparisonDate) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return;
  }
  insertCreateCardData.value.orgnprotdt = orgnUserDate.value.replace(/-/g, '');
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
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if((parseInt(insertCreateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)+1) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  if((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(comparisonDate) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  copyDate.value.year = futureYear;
  copyDate.value.month = '12';
  copyDate.value.day = '31';
  insertCreateCardData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
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
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(orgnProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if((parseInt(insertCreateCardData.value.prsrvterm) < parseInt(copyUserDate.value.slice(0,4))+1 - new Date().getFullYear()) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  if(comparisonDate.isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  insertCreateCardData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
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

// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];

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
    insertCreateCardData.value.newFileList.push({ file: data.file, id: data.id });
  }
};

const handleFileRemove = (error, data) => {
  const index = attachFileData.value.findIndex(attach => attach.id === data.id);
  if (index !== -1) {
    attachFileData.value.splice(index, 1);
    insertCreateCardData.value.newFileList.splice(index, 1);
    insertCreateCardData.value.delFileList.push(index);
  }
};

const newAttachFileArray = computed(() => {
  return attachFileData.value.map(item => item.file);
});

const attachFileData = ref([]);
/* ============================================================ */


// 관리정보 문서번호 Text-Field
const docNameInputData = ref("");

// 기안문
const draftToggle = ref({data:"N"});
const draftView = ref({ data: "N" });
const writeDraftDoc = ref(() => {
  draftToggle.value.data = "Y";
});
const deleteDraftDoc = ref(() => {
  insertCreateCardData.value.drafturl = "";
  insertCreateCardData.value.draftdocttl = "";
})

// 수신자
const formatRecvList = computed(() => {
  if(insertCreateCardData.value.recvList.length == 0 || insertCreateCardData.value.recvList[0].apprrecvdeptname == ""){
    return
  }else{
    const formattedRecvList = insertCreateCardData.value.recvList.map((item) => {
      const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
      return `${item.apprrecvdeptname}${refDeptName}`;
    });
    return formattedRecvList.join(', ');
  }
});

// 열람권자
const formatRcvsList = computed(() => {
  if(insertCreateCardData.value.rcvsList[0].rcvsusername == ""){
    return
  }else{
    const formattedRcvsList = insertCreateCardData.value.rcvsList.map((item) => {
      return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
    });
    return formattedRcvsList.join(', ');
  }
});


const makeAlert = () => {
  if (!insertCreateCardData.value.seclevel) {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if(!insertCreateCardData.value.prsrvterm){
    alert('예고문 보존기간을 선택해주세요.');
    return false;
  }
  if(!insertCreateCardData.value.orgnprotdt){
    alert('원본 보호기간을 선택해주세요.');
    return false;
  }
  if(!insertCreateCardData.value.copyprotdt && chkbox.value == false){
    alert('사본 보호기간을 선택해주세요.');
    return false;
  }
  if(parseInt(insertCreateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if((parseInt(insertCreateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)+1) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    return false;
  }
  if(!insertCreateCardData.value.orgnreclass){
    alert('원본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if(!insertCreateCardData.value.copyreclass && chkbox.value == false){
    alert('사본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if (insertCreateCardData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
  if (chkbox.value == false && insertCreateCardData.value.recvList[0].receiverid == "") {
    alert('수신지정을 해주세요');
    return false;
  } 
};


// 임시저장 팝업
const bVisibleDctTempPopup = ref(false)
const dctTempPopupReuturn = ref({})
const dctTempPopupArgs = ref({})
const dctTempPopupToggleFunc = ref(() => {
  // 필수 입력 값 > 비밀등급 선택
  if (insertCreateCardData.value.seclevel == "") {
    alert('비밀등급을 선택해주세요.');
    return;
  }
  // 필수 입력 값 > 제목 입력
  if (insertCreateCardData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return;
  }

  // 저장로딩 true
  insertCreateCardData.value.saveLoading = true;

  if(draftToggle.value.data == 'N') {
    draftToggle.value.data = 'Y';
  } else {
    callDraftToggle('Y');
  }

  if (chkbox.value == true) {
    insertCreateCardData.value.gubun = 'DOPT1'
  } else {
    insertCreateCardData.value.gubun = 'DOPT2'
  }

  dctTempPopupArgs.value.data = insertCreateCardData.value;
  dctTempPopupArgs.value.file = newAttachFileArray.value;
  dctTempPopupArgs.value.flag = '0'; // 0: 기안 임시저장, 1: 결재 임시저장
  bVisibleDctTempPopup.value = !bVisibleDctTempPopup.value;
})
const dctTempPopupReturnFunc = ref((retValue) => {
  dctTempPopupToggleFunc.value()
  dctTempPopupReuturn.value = retValue;
})


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

// 사용자정보 select
const userCondi = ref({userid:""});
const getUserDetail = async () => {
  userCondi.value.userid = getUserLoginData.value.userid;

  try {
    const response = await API.comAPI.getUserDetail({
      ...userCondi.value
    },
      urlPaths.value
    );
    const data = response.data;
    insertCreateCardData.value.email = data[0].email;
    insertCreateCardData.value.phone = data[0].mobilephone;

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};


</script>