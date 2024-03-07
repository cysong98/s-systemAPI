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
      <h4 class="h4">접수</h4>
    </div>

    <div class="box-content">
      <!-- 비밀조회 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비밀조회</h5>
          <div class="action-buttons">
            <v-btn v-if="draftToggle.data == 'N'" class="button" @click="readDraftDoc()">기안문조회</v-btn>
            <v-btn v-if="draftToggle.data == 'Y'" class="button" @click="readDraftDoc()">비밀카드</v-btn>
            <!-- 담당자 지정이 안돼있으면(receivedData.chargerid 값 없으면) 문서처리하여 담당지정로직을 따름 -->
            <v-btn v-if="(!receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (!receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="documentProcPopupToggleFunc(0)">반송</v-btn>
            <v-btn v-if="(!receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (!receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="documentProcPopupToggleFunc(1)">접수</v-btn>
            <v-btn v-if="(!receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (!receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="dctChargerPopupToggleFunc()">담당지정</v-btn>

            <v-btn v-if="(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="documentProcPopupToggleFunc(2)">재지정요청</v-btn>
            <v-btn v-if="(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="dctReqProcPopupToggleFunc(1)">문서처리</v-btn>
            <v-btn v-if="(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            <v-btn v-if="receivedData.page === 'BmsDctreceiptlist' || receivedData.page === 'Home'" class="button" @click="moveToBmsDctreceiptlist()">닫기</v-btn>

            <v-btn v-if="receivedData.page === 'BmsReprocesslist'" class="button" @click="documentProcPopupToggleFunc(1)">접수</v-btn>
            <v-btn v-if="receivedData.page === 'BmsReprocesslist'" class="button" @click="dctChargerPopupToggleFunc()">담당지정</v-btn>
            <v-btn v-if="receivedData.page === 'BmsReprocesslist'"  class="button" @click="moveToBmsReprocesslist()">닫기</v-btn>
            
            <v-btn v-if="receivedData.page === 'BmsDctReceiptCurrentList' && (route.query && route.query.docstatus && route.query.docstatus !=='EDS11')" class="button" @click="documentProcPopupToggleFunc(3)">접수</v-btn>
            <v-btn v-if="receivedData.page === 'BmsDctReceiptCurrentList' && (route.query && route.query.docstatus && route.query.docstatus !=='EDS11')" class="button" @click="dctChargerPopupToggleFunc()">담당변경</v-btn>
            <v-btn v-if="receivedData.page === 'BmsDctReceiptCurrentList'"  class="button" @click="moveToBmsDctreceiptcurrentlist()">닫기</v-btn>
          </div>
        </div>
      </section>

      <div v-if="draftToggle.data == 'N'">
        <!-- 비밀속성 -->
        <section class="section">  
          <div class="box-title">
            <h6 class="h6">비밀속성</h6>
          </div>
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
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
                <th rowspan="3"><span class="required">예고문</span></th>
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
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="700">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">제목</span></th>
                <td>
                  <v-text-field
                    class="w100"
                    v-model="receivedData.docttl"
                    label=""
                    variant="solo"
                    :readonly="true"
                    hide-details="auto"></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    :readonly="!(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist')"
                    v-model="receivedData.emergency"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
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
                          <v-icon v-if="refData" :color="getFileIcon(getExtension(refData.sfilename)).color">{{ getFileIcon(getExtension(refData.sfilename)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.sfilename }}</span>
                        </li>
                      </ul>
                    </div>
                    <div v-else-if="refData.file != undefined && refData.file != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon v-if="refData" :color="getFileIcon(getExtension(refData.file.name)).color">{{ getFileIcon(getExtension(refData.file.name)).icon }}</v-icon>
                          <span>{{ refData.file.name }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    :readonly="!(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist')"
                    v-model="receivedData.keyword"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>기안문</th>
                <td colspan="3">
                  <div class="box-flex">
                    <div class="box-flex" v-if="receivedData.draftfile">
                      <v-icon v-if="receivedData.draftfile && receivedData.draftfile.length > 0" :color="getFileIcon(getExtension(receivedData.draftfile[receivedData.draftfile.length-1].sfilename)).color">{{ getFileIcon(getExtension(receivedData.draftfile[receivedData.draftfile.length-1].sfilename)).icon }}</v-icon>
                      <span v-if="receivedData.draftfile && receivedData.draftfile.length > 0" @click="readDraftDoc()" style="font-weight: 600; cursor: pointer;">{{ receivedData.draftfile[receivedData.draftfile.length-1].sfilename }}</span>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="3">
                  <div v-if="receivedData.fileList != []">
                    <div class="box-flex" v-for="(fileData,idx) in receivedData.fileList" :key="idx">
                      <v-icon :color="getFileIcon(getExtension(fileData.sfilename)).color">{{ getFileIcon(getExtension(fileData.sfilename)).icon }}</v-icon>
                      <span @click="toggleViewerPopup(fileData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ fileData.sfilename }}</span>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 결재선정보  (접수대기함에서 기안상태이거나 접수현황함에서 종료 상태일때만 출력.)-->
        <section class="section" v-if="(receivedData.page === 'BmsDctReceiptCurrentList' && route.query && route.query.docstatus !== 'EDS05')
           || (receivedData.page === 'BmsDctreceiptlist' && route.query && route.query.docstatus === 'EDS05')">
          <div class="box-title">
            <h6 class="h6">결재선정보</h6>
            <div class="action-buttons">
              <v-btn v-if="(receivedData.chargerid && receivedData.page === 'BmsDctreceiptlist') || (receivedData.chargerid && receivedData.page === 'Home')" class="button" @click="dctPathPopupToggleFunc()">결재선지정</v-btn>
            </div>
          </div>
          <v-table :class="{ 'table-type-04-copydoc': isReceivedDoc, 'table-type-04': !isReceivedDoc }"
            :no-data-text="noDataText">
            <thead>
              <tr>
                <th>구분</th>
                <th>부서</th>
                <th>직위/성명</th>
                <th>의견/지시</th>
                <th>서명</th>
                <th>처리결과</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!insertCreateCardData.pathList || insertCreateCardData.pathList2.length === 0">
                <td :colspan="7" text @click="dctPathPopupToggleFunc" class="temp-cursor-pointer">{{ noDataText.noPathText }}</td>
              </tr>
              <tr v-else v-for="pathData, idx in insertCreateCardData.pathList2" :key="idx">
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
                  {{ transformDate(pathData.reportdt) }}
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
          <v-table :class="{ 'table-type-04-copydoc': isReceivedDoc, 'table-type-04': !isReceivedDoc }"
            :no-data-text="noDataText">
            <thead>
              <tr>
                <th>구분</th>
                <th>부서</th>
                <th>직위/성명</th>
                <th>의견/지시</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="(!insertCreateCardData.chargerList || insertCreateCardData.chargerList.length === 0)">
                <td :colspan="5" text @click="dctChargerPopupToggleFunc" class="temp-cursor-pointer">{{ noDataText.noChargerText }}</td>
              </tr>
              <tr v-else v-for="(chargerData, idx) in insertCreateCardData.chargerList" :key="idx">
                <td>
                  <span style="text-decoration: none; color: red; margin: 10px;" v-if="idx === 0 && receivedData.page == 'BmsReprocesslist'">*담당 재지정 요청</span>
                  <span v-else>담당</span>
                </td>
                <!--부서-->
                <td v-if="receivedData.page == 'BmsReprocesslist'" :class="{ 'line-through': idx === 0, 'no-line': idx !== 0 }">
                  {{ chargerData.deptname }}
                </td>
                <td v-else>
                  {{ chargerData.deptname }}
                </td>
                <!-- 직위/성명 -->
                <td v-if="receivedData.page =='BmsReprocesslist'&& (receivedData.chargerList || receivedData.chargerList.length >= 1)" :class="{ 'line-through': idx === 0, 'no-line': idx !== 0 }">
                    {{ chargerData.dutyname }}/{{ chargerData.actername }}
                </td>
                <td v-else>
                  {{ chargerData.dutyname }}/{{ chargerData.actername }}
                </td>
                <!--의견-->
                <td v-if = "receivedData.page == 'BmsReprocesslist'&& (receivedData.chargerList || receivedData.chargerList.length >= 1)" :class="{ 'line-through': idx === 0, 'no-line': idx !== 0 }">
                  {{ chargerData.actopinion }}
                </td>
                <td v-else>
                  {{ chargerData.actopinion }}
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
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="700">
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
                    hide-details="auto"></v-select>
                </td>
                <th>발신명의</th>
                <td>
                  <v-text-field
                    class="w100"
                    v-model="sendNameData"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>생산문서번호</th>
                <td>
                  <v-text-field
                    v-model="receivedData.docno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>접수번호</th>
                <td>
                  <v-text-field
                    v-model="receiptNo"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>생산기관경로</th>
                <td colspan="3">
                  <v-text-field
                    v-model="displayPathList"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>수신자</th>
                <td colspan="3">
                  <v-text-field
                    :value="formatOriRecvList"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>배포처</th>
                <td colspan="3">
                  <v-text-field
                    v-model="sendOrgNameData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
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
            </div>
          </div>
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>문서번호</th>
                <td>
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
      <HwpCtrl v-if="draftToggle.data == 'Y'" :data="receivedData" :toggle="draftToggle" :view="draftView" :received="isReceivedDoc" ref="childRef" :key="compKey"/>
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

  <!-- 접수 팝업 -->
  <v-dialog v-model="bVisibleDocumentProcPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>{{ toolbarTitle }}</v-toolbar-title>
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

  <!-- 담당변경 팝업 -->
  <v-dialog v-model="bVisibleDctChargerPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀담당자 지정</v-toolbar-title>
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

  <!-- 문서처리 팝업 -->
  <v-dialog v-model="bVisibleDctReqProcPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>{{ toolbarTitle }}</v-toolbar-title>
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
        :received="isReceivedDoc"
      ></DctReqProcPopup>
    </v-card>
  </v-dialog>

  <!-- 기안문 버전별 조회 팝업 -->
  <v-dialog v-model="bVisibleHwpCtrlPopup" style="width: 1000px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>기안문 버전별 조회</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleHwpCtrlPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <HwpCtrlPopup if="bVisibleHwpCtrlPopup" :args="hwpCtrlPopupArgs"
        :toggleFunc="hwpCtrlPopupToggleFunc" :returnFunc="hwpCtrlPopupReturnFunc" :received="isReceivedDoc"></HwpCtrlPopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore)

import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import DctReqProcPopup from "./DctReqProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctReviewPrintPopup from "./DctReviewPrintPopup.vue";
import DocumentProcPopup from "./DocumentProcPopup.vue";
import DctChargerPopup from "./DctChargerPopup.vue";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";

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

// 기안문 관련
import HwpCtrlPopup from "./HwpCtrlPopup.vue";
import HwpCtrl from "./HwpCtrl.vue";
const childRef = ref(null);

const callDraftToggle = (data, apprSave) => {
  if (childRef.value) {
    childRef.value.draftToggle(data, apprSave);
  }
}

const draftToggle = ref({ data: "N" });
const draftView = ref({ data: "Y" });
const readDraftDoc = ref(() => {
  if (receivedData.value.drafturl != "") {
    if (draftToggle.value.data == 'Y') {
      draftToggle.value.data = "N";
    } else {
      draftToggle.value.data = "Y";
    }
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
});

const name = ref('BmsDctCreateCardTemp')
const data = ref({});
const props = defineProps({
  data: Object
});

const receivedData = ref({});
const args = ref({});
const docidData = ref({docid: ""});
const noDataText = ref({
  type: String,
  noPathText: '데이터가 없습니다. 경로를 지정해주세요.',
  noChargerText: '데이터가 없습니다. 담당자를 지정해주세요.'
});

/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');
const isReceivedDoc = ref(false);
const today = ref("")
const insertCreateCardData = ref({

// 1) bms_dct_rdoc table
docttl: "", // 비밀명
gubun: "", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
state: "", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
emergency: "N", // 긴급도 (대내: N, 대외: Y, 대내외: T)
keyword: "", // 키워드
authorid: "", // 작성자 ID
authorname: "", // 담당자명
authordutyname: "", // 담당자 직위직급
authordeptname: "", // 담당자 부서이름
tempflag: "", // 임시 flag (임시: Y, 처리요청: N)
oridocid: "",

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
recvOriList:[
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

// 11) bms_dct_enf_doc table (담당자 확인용)
chargerList: [
  // {
  //   askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
  //   asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
  //   deptname: "", // 부서이름
  //   dutyname: "", // 직위직급이름
  //   dutynamedesc: "", // 직위직급이름 상세
  //   electsignid: "", // 전자서명ID
  //   reportdt: "", // 보고일자
  //   mainver: "", // 메인버전
  //   chargerorder: "", //  담당순서
  //   chargerstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
  //   opinion: "",
  // }
],

// 12) bms_dct_path table
pathList2:[
  // {
  //   askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
  //   asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
  //   deptname: "", // 부서이름
  //   dutyname: "", // 직위직급이름
  //   dutynamedesc: "", // 직위직급이름 상세
  //   electsignid: "", // 전자서명ID
  //   reportdt: "", // 보고일자
  //   mainver: "", // 메인버전
  //   pathorder: "", //  경로순서
  //   pathstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
  //   opinion: "",
  // }
],

// 공통
resdocid: "",
resreceiverid: "",
resuserid: "",

  // 비전자문서 flag
  paperflag: "",
});



onBeforeMount(() => {
  // receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
})

onBeforeMount(async() => {
  receivedData.value = props.data;
  docidData.value.docid = route.query.docid;
})

onMounted( async () => {
  window.sessionStorage.setItem('waitFlag', 'N');

  receivedData.value = route.query;  
  docidData.value.docid = route.query.docid;
  isReceivedDoc.value = receivedData.value.copyno != null && !receivedData.value.copyno.includes("원") ? true : false;
  insertCreateCardData.value.authorid = getUserLoginData.value.userid;
  insertCreateCardData.value.authorname = getUserLoginData.value.username;
  insertCreateCardData.value.authordutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.authordeptname = getUserLoginData.value.deptname;

  insertCreateCardData.value.deptid = getUserLoginData.value.deptid;
  insertCreateCardData.value.deptname = getUserLoginData.value.deptname;
  insertCreateCardData.value.dutyname = getUserLoginData.value.grade;
  insertCreateCardData.value.username = getUserLoginData.value.username;

  insertCreateCardData.value.resuserid = getUserLoginData.value.userid;

  
  today.value = dayjs().format('YYYY-MM-DD');
  insertInitialPath();
})
watch(route, async (route) => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
});
watchEffect(() => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
  receivedData.value.oriEmergency = props.data.emergency;
  receivedData.value.oriKeyword = props.data.keyword;
})
watchEffect(async() => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
  insertCreateCardData.value.chargerid = receivedData.value.chargerid;
  if(!receivedData.value.chargerid){
    receivedData.value.chargerList =[];
  }
  let chargerListLength = receivedData.value.chargerList.length;
  insertCreateCardData.value.chargerList = receivedData.value.chargerList;
  if(receivedData && receivedData.value.chargerList && receivedData.value.chargerList.length > 1){
    insertCreateCardData.value.chargerList = [ receivedData.value.chargerList[chargerListLength-1] ];
    insertCreateCardData.value.chargerListOld = receivedData.value.chargerList[0];
    insertCreateCardData.value.newChargerList = receivedData.value.newChargerList;
  }
  if((props.data.docid || props.data.docid != undefined) && props.data.docid !== ""){
    await getDctRdoc();
    await getCopyDate();
    await selectDctPath();
    await getDctRecv();
    await getDctOriRecv();
    if(!route.query.chargerid){
      insertCreateCardData.value.rcvsList = [];
    }else{
      await selectDctReader();
    }
    await selectTaskInfo();
    await selectDctFile();
  } 
  if (receivedData.value && receivedData.value.chargerList && receivedData.value.chargerList.length > 0) {
    receiptNo.value = receivedData.value.chargerList[0].deptname + '-S' + receivedData.value.chargerList[0].actseq;
  }
})
const toolbarTitle = ref(''); //팝업창 타이틀(제목)

/* ======================== 접수 팝업 ======================== */
const bVisibleDocumentProcPopup = ref(false)
const documentProcPopupReuturn = ref({})
const documentProcPopupArgs = ref({});
const documentProcPopupToggleFunc = (buttonValue) => { 
  
  if(receivedData.value.page === 'BmsReprocesslist' && insertCreateCardData.value.chargerList.length == 1){
    bVisibleDocumentProcPopup.value = false;
    alert('담당자를 지정해주세요.');
    return false;
  }

  const getUserLoginData = ref({getUserLoginData: loginStore.getUserLoginData});
  const loginuserid = ref({loginuserid: loginStore.getUserLoginData.userid});
  documentProcPopupArgs.value = {
    ...insertCreateCardData.value,
    ...receivedData.value,
    chargerList: insertCreateCardData.value.chargerList,
    pathList2: insertCreateCardData.value.pathList2,
    docstatus: "",
    //// ...getUserLoginData.value.value,
    ...loginuserid.value,
  }
  if (buttonValue === 0) { //반송, 버튼 값 0
    toolbarTitle.value = '반송';
    documentProcPopupArgs.value.docstatus = "EDS06"; //반송 //bms_dct_enf_doc.docstatus 비밀상태
    documentProcPopupArgs.value.actgubun = "EPA05" //반송 //bms_dct_enf_history.actgubun 처리구분
    documentProcPopupArgs.value.state = "DRDS6" //반송 //bms_dct_rdoc.state 상황
  } else if (buttonValue === 1) { //접수, 버튼 값 1
    toolbarTitle.value = '접수';
      if (makeAlert() == false) {
      return;
    }
    // documentProcPopupArgs.value.docstatus = "EDS09"; //접수대기 //bms_dct_enf_doc.docstatus 비밀상태
    documentProcPopupArgs.value.docstatus = "EDS05"; //접수 //bms_dct_enf_doc.docstatus 비밀상태
    documentProcPopupArgs.value.actgubun = "EPA04" //접수 //bms_dct_enf_history.actgubun 처리구분
  } else if (buttonValue === 2) { //재지정요청
    toolbarTitle.value = '재지정요청';
    documentProcPopupArgs.value.docstatus = "EDS10"; //재지정요청 //bms_dct_enf_doc.docstatus 비밀상태
    documentProcPopupArgs.value.actgubun = "EPA17"; //재지정요청 //bms_dct_enf_history.actgubun 처리구분
  } else if (buttonValue === 3) { //담당변경접수 (접수현황함에서의 접수)
    toolbarTitle.value = '접수';
    documentProcPopupArgs.value.docstatus = "EDS12"; //담당변경 //bms_dct_enf_doc.docstatus 비밀상태
    documentProcPopupArgs.value.actgubun = "EPA04"; //접수 //bms_dct_enf_history.actgubun 처리구분
  }

  bVisibleDocumentProcPopup.value = !bVisibleDocumentProcPopup.value; 
  togglePopup('DocumentProcPopup'); 
}

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
  const modifiedChargerList = chargerList.map(item => {
    return {
      orgid: item.deptid,
      userid: item.objid,
      username: item.actername,
      orgname: item.deptname,
      grade: item.dutyname,
      askTemp: item.askTemp,
      asktype: item.asktype,
      actgubun: item.asktype,
      actopinion: item.actopinion,
      indt: item.indt,
      actseq: item.actseq,
      identifier: `${item.deptid}-${item.objid}`
    };
  });
  dctChargerPopupArgs.value = modifiedChargerList;

  if(receivedData.value.page === 'BmsReprocesslist'){
    dctChargerPopupArgs.value.page = 'BmsReprocesslist'
  }else{
    dctChargerPopupArgs.value.page = 'BmsDctreceiptlist'
  }

  dctChargerPopupArgs.value.deptid = getUserLoginData.value.deptid;

  bVisibleDctChargerPopup.value = !bVisibleDctChargerPopup.value;
});

const dctChargerPopupReturnFunc = ref((retValue) => {
  dctChargerPopupToggleFunc.value()
  if(retValue != ""){
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
      charger.actgubun = retValue[i].asktype;
      charger.actopinion = retValue[i].actopinion;
      charger.indt = retValue[i].indt;
      charger.actseq = retValue[i].actseq;
      insertCreateCardData.value.chargerList.push(charger);
    };
    rcvsCharger();
  }else{
    insertCreateCardData.value.chargerList = [];
    rcvsCharger();
  }
});
/* ============================================================ */

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const dctPathPopupToggleFunc = ref(() => { 
  const pathList2 = [...insertCreateCardData.value.pathList2];
  const modifiedPathList2 = pathList2.slice(1).map(item => {
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
  dctPathPopupArgs.value.path = modifiedPathList2;
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
      insertCreateCardData.value.pathList2.push(path);
    };

    const requests = []; // 기안
    const reviewers = []; // 검토
    const collaborator = []; // 협조
    const d_approvers = []; // 대결
    const j_approvers = []; // 전결
    const jd_approvers = []; // 전대결
    const approvers = []; // 결재

    // '기안','검토', ... , '결재'에 따라 재정렬
    insertCreateCardData.value.pathList2.forEach((path) => {
      switch (path.askTemp) {
        case '기안':
          requests.push(path);
          break;
        case '검토':
          reviewers.push(path);
          break;
        case '협조':
          collaborator.push(path);
          break;
        case '대결':
          d_approvers.push(path);
          break;
        case '전결':
          j_approvers.push(path);
          break;
        case '전대결':
          jd_approvers.push(path);
          break;
        case '결재':
          approvers.push(path);
          break;
        default:
          break;
      }
    });

    insertCreateCardData.value.pathList2 = requests.concat(reviewers,collaborator,d_approvers,j_approvers,jd_approvers,approvers);
    updatePathOrder();
  }else{
    insertInitialPath();
  }
});

const insertInitialPath = () => {
  insertCreateCardData.value.pathList2 = [];
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
  insertCreateCardData.value.pathList2.push(initialPath);
};

const updatePathOrder = () => {
  insertCreateCardData.value.pathList2.forEach((path, index) => {
      path.pathorder = index + 1;
  });
};
/* ============================================================ */

/* ======================== 열람지정 팝업 ======================== */
const bVisibleDctReaderPopup = ref(false)
const dctReaderPopupReuturn = ref({})
const dctReaderPopupArgs = ref({})
const dctReaderPopupToggleFunc = ref(() => { 
  const rcvsList = [...insertCreateCardData.value.rcvsList];
  const modifiedRcvsList = rcvsList.map(item => {
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
});

const reader = ref("");
const dctReaderPopupReturnFunc = ref((retValue) => {
  dctReaderPopupToggleFunc.value()
  if(retValue != ""){
    insertCreateCardData.value.rcvsList = [];
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
  }
})

const rcvsCharger = () => {
  insertCreateCardData.value.rcvsList = [];
    const initialRcvs = {};
    if(insertCreateCardData.value.chargerList && insertCreateCardData.value.chargerList.length === 1){
      initialRcvs.rcvsuserid = insertCreateCardData.value.chargerList[0].objid;
      initialRcvs.rcvsusername = insertCreateCardData.value.chargerList[0].actername;
      initialRcvs.rcvsinseq = 1;
      initialRcvs.rcvdeptid = insertCreateCardData.value.chargerList[0].deptid;
      initialRcvs.rcvdeptname = insertCreateCardData.value.chargerList[0].deptname;
      initialRcvs.rcvdutyname = insertCreateCardData.value.chargerList[0].dutyname;
    }else{ //담당 재지정한경우로서 열람권자 바뀜.
      initialRcvs.rcvsuserid = insertCreateCardData.value.chargerList[1].objid;
      initialRcvs.rcvsusername = insertCreateCardData.value.chargerList[1].actername;
      initialRcvs.rcvsinseq = 1;
      initialRcvs.rcvdeptid = insertCreateCardData.value.chargerList[1].deptid;
      initialRcvs.rcvdeptname = insertCreateCardData.value.chargerList[1].deptname;
      initialRcvs.rcvdutyname = insertCreateCardData.value.chargerList[1].dutyname;
    }
    
    insertCreateCardData.value.rcvsList.push(initialRcvs);
}
/* ============================================================ */

/* ======================== 문서처리 팝업 ======================== */
const bVisibleDctReqProcPopup = ref(false)
const dctReqProcPopupReuturn = ref({})
const dctReqProcPopupArgs = ref({})
const dctReqProcPopupToggleFunc = ref((buttonValue) => {

  receivedData.value.opinion = "";
  
  const getUserLoginData = ref({getUserLoginData: loginStore.getUserLoginData});
  const loginuserid = ref({loginuserid: loginStore.getUserLoginData.userid});
  dctReqProcPopupArgs.value = {
    ...insertCreateCardData.value,
    ...receivedData.value,
    docstatus: "", //접수
    judgeParam: "receipt", // 접수시 빽단에서 기안의 문서처리인지, 접수의 문서처리인지 구분용도.
    ...loginuserid.value,
    getUserLoginData: getUserLoginData.value.getUserLoginData,
    authordeptid: getUserLoginData.value.getUserLoginData.deptid,
    authorname: getUserLoginData.value.getUserLoginData.username,
    authordutyname: getUserLoginData.value.getUserLoginData.grade,
    authordeptname: getUserLoginData.value.getUserLoginData.orgname,
  }
  if (buttonValue === 1) { //문서처리 버튼 클릭시.
    toolbarTitle.value = '문서처리';
    if (makeAlert('pathCheck') == false) {
      return;
    }
  }

  dctReqProcPopupArgs.value.pathList = insertCreateCardData.value.pathList2;
  dctReqProcPopupArgs.value.opinionpathorder = insertCreateCardData.value.opinionpathorder;
  bVisibleDctReqProcPopup.value = !bVisibleDctReqProcPopup.value; 
})
const dctReqProcPopupReturnFunc = ref((retValue) => {
  dctReqProcPopupToggleFunc.value()
  dctReqProcPopupReuturn.value = retValue;
})
/* ============================================================ */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

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

// 직접입력
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
const copyReclassDataDisplayValue = computed(() => {
  const item = copyReclassData.find(item => item.value === receivedData.value.copyreclass);
  return item ? item.key : '';
});
const selectedCopyReClass = (key) => {
  if(key == '21'){
    copyReclassSelected.value = "일반문서"
  }else{
    copyReclassSelected.value = "선택"
  }
}
/* ============================================================ */
const displayPathList = ref("");

// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: 'II급', secLevelDataValue: '2'},
  { secLevelDataKey: 'III급', secLevelDataValue: '3'},
  { secLevelDataKey: '대외비', secLevelDataValue: '4'},
  { secLevelDataKey: '일반', secLevelDataValue: '5'},
];
const secLevelDataDisplayValue = computed(() => {
  const item = secLevelData.find(item => item.secLevelDataValue === receivedData.value.seclevel);
  return item ? item.secLevelDataKey : '';
});

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

// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];


// 표제부 기안문 Text-Field
const draftDocInputData = ref("");

// 기안문 버전별 조회 팝업 관련
const bVisibleHwpCtrlPopup = ref(false)
const hwpCtrlPopupArgs = ref({})
const hwpCtrlPopupToggleFunc = ref((pathData) => {
  if (receivedData.value.draftfile.length > 0) {
    if (pathData != null && pathData != undefined) {
      // 전체 조회 데이터 넘겨줌
      hwpCtrlPopupArgs.value.data = receivedData.value;

      // asktype 넘겨줌
      hwpCtrlPopupArgs.value.asktype = pathData.asktype;

      // userid 넘겨줌
      hwpCtrlPopupArgs.value.objid = pathData.objid;

      // url 넘겨줌
      let arr = [];
      for (let i = 0; i < receivedData.value.pathlist.length; i++) {
        let object = {};
        object.id = receivedData.value.pathlist[i].objid;

        for (let j = 0; j < receivedData.value.draftfile.length; j++) {
          if (object.id == receivedData.value.draftfile[j].inid) {
            object.url = receivedData.value.draftfile[j].fleurl;
            break;
          } else {
            object.url = "";
          }
        }
        
        arr.push(object);
      }

      for (let i = 0; i < arr.length; i++) {
        if (arr[i].id == pathData.objid) {
          if (arr[i].url != '') {
            hwpCtrlPopupArgs.value.url = arr[i].url;
          } else {
            if (i != 0) {
              let count = 1;
              while (true) {
                if (count == arr.length) {
                  break;
                }
                hwpCtrlPopupArgs.value.url = arr[i - count].url;

                if (hwpCtrlPopupArgs.value.url == '') {
                  count++;
                } else {
                  break;
                }
              }
            }
          }
        }
      }
    }
    bVisibleHwpCtrlPopup.value = !bVisibleHwpCtrlPopup.value;
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
})
const hwpCtrlPopupReturnFunc = ref((retValue) => {
  hwpCtrlPopupToggleFunc.value();
})


// 접수번호
const receiptNo = ref("");

// 시행정보 발신기관명 Select-Box
const sendOrgNameData = ref([]);

// 시행정보 발신명의 Select-Box
const sendNameData = ref([]);

const makeAlert = (checkType) => {
  if (receivedData.value.docttl == "") {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }

  if(!insertCreateCardData.value.chargerList || insertCreateCardData.value.chargerList.length === 0) {
    alert('담당자를 지정해주세요.');
    return false;
  }
};


// 1) bms_dct_rdoc table
const getDctRdoc = async () => {
  if(props.data.docid){
    try {
      const response = await API.dctAPI.getDctRdoc({
        docid: props.data.docid},
        urlPaths.value
      );
      const data = response.data;
      receivedData.value.docttl = data.docttl;
      receivedData.value.gubun = data.gubun;
      receivedData.value.state = data.state;
      receivedData.value.open = data.open;
      receivedData.value.readrangetype = data.readrangetype;
      receivedData.value.emergency = 'N';
      receivedData.value.keyword = '';
      receivedData.value.authorname = data.authorname;
      receivedData.value.authordutyname = data.authordutyname;
      receivedData.value.authordeptname = data.authordeptname;
      receivedData.value.tempflag = data.tempflag;
      receivedData.value.oridocid = data.oridocid;
      receivedData.value.paperflag = data.paperflag;
    } catch (error) {
      console.log(error);
      alert("Server Error");
    }
  }
};

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
      sendOrgNameData.value = response.data[i].distdeptname;
      sendNameData.value = response.data[i].distname;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};

// 6-2) bms_dct_appr_recv
const getDctOriRecv = async () => {
  try {
    const response = await API.dctAPI.getDctOriRecv({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.recvOriList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].tempflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvgubun = response.data[i].gubun;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      insertCreateCardData.value.recvOriList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};

// 수신자
const formatOriRecvList = computed(() => {
  const formattedOriRecvList = insertCreateCardData.value.recvOriList.map((item) => {
    const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
    return `${item.apprrecvdeptname}${refDeptName}`;
  });
    return formattedOriRecvList.join(', ') || getUserLoginData.value.deptname;
});


 // 7) bms_dct_rdoc_rcvs
 const selectDctReader = async () => {
  try {
    const response = await API.dctAPI.selectDctReader({
      ...docidData.value },
      urlPaths.value
    );
    insertCreateCardData.value.rcvsList = [];
    const initialRcvs = {};
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptid = response.data[i].deptid;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsdeptnamedesc = response.data[i].deptnamedesc;
      upd.rcvsinseq = response.data[i].inseq;
      insertCreateCardData.value.rcvsList.push(upd);
    }
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
      upd.flepath = response.data[i].flepath;
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
    const response = await API.dctAPI.selectDctFile({
      ...docidData.value
    },
      urlPaths.value
    );
    receivedData.value.fileList = [];
    receivedData.value.draftfile = [];

    // 붙임파일 전체 (이력용)
    receivedData.value.fileListAll = [];
    for (let i = 0; i < response.data.length; i++) {
      if (response.data[i].gubun == 'DFT02') { // 붙임파일만
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
        upd.inid = response.data[i].inid;
        upd.indt = response.data[i].indt;
        receivedData.value.fileListAll.push(upd);

        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT02') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          receivedData.value.fileList.push(upd);
        }
      } else {
        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT01') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if(Number(response.data[i].indt) == maxIndt) {
          draftDocInputData.value = response.data[i].sfilename;
          receivedData.value.drafturl = response.data[i].fleurl;
        }

        receivedData.value.draftfile.push(response.data[i]);
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};


// 4) bms_dct_path table
 const selectDctPath  = async () => {
  try {
    const response = await API.dctAPI.selectDctPath ({
        docid: props.data.docid}, 
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
      }else{
        upd.transformedReportdt = transformDate(response.data[i].docid.slice(3,15));
      }
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.pathstate = response.data[i].state;
      insertCreateCardData.value.pathList.push(upd);
    }
    displayPathList.value = formaPathList.value; 
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};
const formaPathList = computed(() => {
  const formattedPathList = insertCreateCardData.value.pathList.map((item) => {
    return `${item.dutynamedesc}(${item.askTemp} ${item.transformedReportdt})`;
  });
  return formattedPathList.join(', ');
});


// 취소 버튼 Move FN (처리할 일 페이지로 이동)
const moveToBmsDctreceiptlist = () => {
  router.push({
    name: "BmsDctreceiptlist",
  });
};

const moveToBmsDctreceiptcurrentlist = () => {
  router.push({
    name: "BmsDctreceiptcurrentlist",
  });
};

const moveToBmsReprocesslist = () => {
  router.push({
    name: "BmsReprocesslist",
  });
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


<style lang="scss" scoped>
.required label::after {
  content: "*";
  color: red;
}
.btns{
  display: flex;
  justify-content: end;
}
.btn{
  border: 1px solid blue;
  font-weight: 600;
  margin-right: 10px;
}
.td-row {
  display: flex;
}
.custom-v-select {
  min-width: 150px;
  max-width: 200px;
  margin-left: 10px;
  margin-right: 10px;
}
.custom-field {
  min-width: 150px;
  max-width: 200px;
  margin-left: 10px;
  margin-right: 10px;
}
.temp-cursor-pointer {
  cursor: pointer;
}
.line-through {
    text-decoration: line-through red;
}

.no-line {
    text-decoration: none;
}
</style>
