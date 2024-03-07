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
      <h4 v-if="getFromPage == 'BmsDcttodolistread'" class="h4">비밀열람함</h4>
      <h4 v-if="getFromPage == 'BmsDctmgmtregilist'" class="h4">비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsUseConserveMgmtRegiList'" class="h4">존안비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsUseTransMgmtRegiList'" class="h4">이관비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsCominglist'" class="h4">예고문도래목록</h4>
      <h4 v-if="['BmsUseViewReqList', 'BmsReUseReqList', 'BmsUsePrintReqList', 'BmsUseTakeOutReqList'].includes(getFromPage)" class="h4">비밀문서 사용요청</h4>
      <div class="action-buttons3">
        <v-btn class="button" @click="bVisibleDctHistoryCardPopup = true">비밀이력카드</v-btn>
        <v-btn class="button" @click="bVisibleDctAuthManageCardPopup = true">비밀권한관리카드</v-btn>
        <v-btn class="button" @click="checkConditionAndTogglePopup">비밀배포카드</v-btn>
        <v-btn class="button" @click="bVisibleDctManageRecordPopup = true">비밀관리기록부</v-btn>
      </div>
    </div>

    <div class="box-content">
      <!-- 비밀작성 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비밀문서</h5>
          <div class="action-buttons">
            <v-btn class="button" v-if="checkIsReusableResult.result == '재사용가능'" @click="moveToBmsUsetempcard(docidData.docid)">재사용</v-btn>
            <v-btn class="button" v-if="draftToggle.data == 'N'" @click="readDraftDoc()">기안문조회</v-btn>
            <v-btn class="button" v-else @click="readDraftDoc()">비밀카드</v-btn>
            <v-btn class="button" @click="moveToList()">목록</v-btn>
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
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">비밀등급</span></th>
                <td>
                  <v-select
                    v-model="showCreateCardData.seclevel"
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
                    v-model="showCreateCardData.mgmtno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>사본번호</th>
                <td>
                  <v-text-field
                    v-model="showCreateCardData.copyno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>내부생성<br>(사본없음)</th>
                <td><v-checkbox class="v-checkbox-square-btn" v-model="chkbox" hide-details="auto" readonly></v-checkbox></td>
              </tr>
              <tr>
                <th rowspan="3"><span class="required">예고문</span></th>
                <td colspan="7" v-if="!isReceivedDoc">
                  <!-- 예고문 - 보존기간 -->
                  <div class="box-flex">
                    <v-label>보존기간:</v-label>
                    <v-select
                      v-model="showCreateCardData.prsrvterm"
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
              <tr v-if="!isReceivedDoc">
                <td colspan="7">
                  <!-- 예고문 - 원본보호기간 -->
                  <div class="box-flex">
                    <v-label>원본보호기간:</v-label>
                    <v-select
                      v-model="orgnProtDtPeriodSelected"
                      :items="orgnProtDtPeriodData"
                      item-title="key"
                      item-value="value"
                      variant="outlined"
                      :readonly="true"
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
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                    <v-label>(으)로</v-label>
                    <v-select
                      v-model="showCreateCardData.orgnreclass"
                      :items="orgnReclassData"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                  </div>
                </td>
              </tr>
              <tr v-if="chkbox == false || isReceivedDoc == true">
                <td colspan="7">
                  <div class="box-flex">
                    <v-label>사본보호기간:</v-label>
                    <v-select
                      v-model="copyProtDtPeriodSelected"
                      :items="copyProtDtPeriodData"
                      item-title="key"
                      item-value="value"
                      variant="outlined"
                      :readonly="true"
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
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto">
                    </v-select>
                    <v-label>(으)로</v-label>
                    <v-select
                      v-model="showCreateCardData.copyreclass"
                      :items="copyReclassData"
                      item-title="key"
                      item-value="value"
                      :readonly="true"
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
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
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
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    v-model="showCreateCardData.emergency"
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
                  <!-- <div class="box-flex"> -->
                    관련근거
                  <!-- </div> -->
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
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.sfilename)).color">{{ getFileIcon(getExtension(refData.sfilename)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.sfilename }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-model="showCreateCardData.keyword"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr v-if="!isReceivedDoc">
                <th>문서취지</th>
                <td colspan="3">
                  <span>{{ showCreateCardData.summary }}</span>
                </td>
              </tr>
              <tr>
                <th>기안문</th>
                <td colspan="3">
                  <div class="box-flex" v-if="showCreateCardData.draftdocttl">
                    <v-icon :color="getFileIcon(getExtension(showCreateCardData.draftdocttl)).color">{{ getFileIcon(getExtension(showCreateCardData.draftdocttl)).icon }}</v-icon>
                    <span @click="readDraftDoc()" style="font-weight: 600; cursor: pointer;">{{ showCreateCardData.draftdocttl }}</span>
                  </div>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="3">
                  <div v-if="showCreateCardData.fileList != []">
                    <div class="box-flex" v-for="(fileData,idx) in showCreateCardData.fileList" :key="idx">
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
          </div>
          <v-table :class="{ 'table-type-04-copydoc': isReceivedDoc, 'table-type-04': !isReceivedDoc }">
            <thead>
              <tr>
                <th>구분</th>
                <th>부서</th>
                <th>직위/성명</th>
                <th>의견/지시</th>
                <th>서명</th>
                <th>처리결과</th>
                <th>처리일시</th>
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
                <td style="width: 800px;" class="text-left">
                  {{ pathData.opinion }}
                </td>
                <td class="signature">
                  {{ pathData.objname }}
                </td>          
                <td>
                  {{ pathData.reportresult }}
                </td>
                <td>
                  {{ pathData.reportdt }}
                </td>
                <td>
                  <span v-if="pathData.reportdt" @click="hwpCtrlPopupToggleFunc(pathData)" style="cursor: pointer;">v{{ pathData.mainver }}.0</span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 시행정보 -->
        <div v-if="!isReceivedDoc">
          <section class="section">
            <div class="box-title">
              <h6 class="h6">시행정보</h6>
            </div>
            <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
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
                      v-model="showCreateCardData.sendOrgNameData"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto"
                      ></v-select>
                  </td>
                  <th>발신명의</th>
                  <td>
                    <v-select
                      class="w100"
                      v-model="showCreateCardData.sendNameData"
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
        </div>
        <div v-else>
          <section class="section">
            <div class="box-title">
              <h6 class="h6">시행정보</h6>
            </div>
            <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
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
                    <v-text-field
                      v-model="oriRecvData.distdeptname"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                  <th>발신명의</th>
                  <td>
                    <v-text-field
                      v-model="oriRecvData.distname"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                </tr>
                <tr>
                  <th>생산문서번호</th>
                  <td>
                    <v-text-field
                      v-model="oriAttrData.docno"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                  <th>접수번호</th>
                  <td>
                    <v-text-field
                      v-model="showCreateCardData.docno"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                </tr>
                <tr>
                  <th>생산기관경로</th>
                  <td colspan="3">
                    <v-text-field
                      v-model="oriPathData"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                </tr>
                <tr>
                  <th>수신자</th>
                  <td colspan="3">
                    <v-text-field
                      v-model="oriRecvData.recvdata"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                </tr>
                <tr>
                  <th>배포처</th>
                  <td colspan="3">
                    <v-text-field
                      v-model="oriAttrData.docnosplit"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </td>
                </tr>
              </tbody>
            </v-table>
          </section>
        </div>

        <!-- 관리정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">관리정보</h6>
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
                  <v-text-field
                    v-model="showCreateCardData.docno"
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

      <HwpCtrl v-if="draftToggle.data == 'Y'" :data="showCreateCardData" :toggle="draftToggle" :view="draftView" :received="isReceivedDoc" ref="childRef" :key="compKey"/>
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

  <!-- 비밀이력카드 팝업 -->
  <v-dialog v-model="bVisibleDctHistoryCardPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀이력카드</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctHistoryCardPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctHistoryCardPopup
      v-if="bVisibleDctHistoryCardPopup"
      :args="dctHistoryCardPopupArgs"
      :toggleFunc="dctHistoryCardPopupToggleFunc"
      :returnFunc="dctHistoryCardPopupReturnFunc"
      ></DctHistoryCardPopup>
    </v-card>
  </v-dialog>

  <!-- 비밀권한관리카드 팝업 -->
  <v-dialog v-model="bVisibleDctAuthManageCardPopup" max-width="1500px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀권한관리카드</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctAuthManageCardPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctAuthManageCardPopup
        v-if="bVisibleDctAuthManageCardPopup"
        :args="dctAuthManageCardPopupArgs"
        :toggleFunc="dctAuthManageCardPopupToggleFunc"
        :returnFunc="dctAuthManageCardPopupReturnFunc"
      ></DctAuthManageCardPopup>
    </v-card>
  </v-dialog>

  <!-- 비밀배포카드 팝업 -->
  <v-dialog v-model="state.bVisibleDctDistributeCardPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀배포카드</v-toolbar-title>
        <v-btn class="close" icon @click="state.bVisibleDctDistributeCardPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctDistributeCardPopup
         v-if="state.bVisibleDctDistributeCardPopup"
        :args="state.dctDistributeCardPopupArgs"
        :toggleFunc="dctDistributeCardPopupToggleFunc"
        :returnFunc="dctDistributeCardPopupReturnFunc"
      ></BmsDctDistributeCardPopup>
    </v-card>
  </v-dialog>

  <!-- 비밀관리기록부 팝업 -->
  <v-dialog v-model="bVisibleDctManageRecordPopup" max-width="1000px" max-height="850px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀관리기록부</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctManageRecordPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctManageRecordPopup
        v-if="bVisibleDctManageRecordPopup"
        :args="dctManageRecordPopupArgs"
        :toggleFunc="dctManageRecordPopupToggleFunc"
        :returnFunc="dctManageRecordPopupReturnFunc"
      ></DctManageRecordPopup>
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
      <HwpCtrlPopup
        v-if="bVisibleHwpCtrlPopup"
        :args="hwpCtrlPopupArgs"
        :toggleFunc="hwpCtrlPopupToggleFunc"
        :returnFunc="hwpCtrlPopupReturnFunc"
        :received="isReceivedDoc"
      ></HwpCtrlPopup>
    </v-card>
  </v-dialog>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore)

import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import DctReqProcPopup from "./DctReqProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctRecvPopup from "./DctRecvPopup.vue";
import DctReferencePopup from "./DctReferencePopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctSymbolPopup from "./DctSymbolPopup.vue";
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";
import DctAuthManageCardPopup from "./DctAuthManageCardPopup.vue";
import BmsDctDistributeCardPopup from "./BmsDctDistributeCardPopup.vue";
import DctManageRecordPopup from "./DctManageRecordPopup.vue";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import { API } from "@/api";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import dayjs from 'dayjs';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { transformStatus,transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('BmsDctMgmtRegiDetailCard')

/* ======================== 비밀이력카드 팝업 ======================== */
const bVisibleDctHistoryCardPopup = ref(false);
const dctHistoryCardPopupArgs = ref({});
const dctHistoryCardPopupToggleFunc = ref(() => {
  bVisibleDctHistoryCardPopup.value = !bVisibleDctHistoryCardPopup.value;
});
const dctHistoryCardPopupReturnFunc = ref((retValue) => {
  dctHistoryCardPopupToggleFunc.value();
});
/* ============================================================ */

/* ======================== 비밀권한관리카드 팝업 ======================== */
const bVisibleDctAuthManageCardPopup = ref(false)
const dctAuthManageCardPopupArgs = ref({});
const dctAuthManageCardPopupToggleFunc = ref(() => {
  bVisibleDctAuthManageCardPopup.value = !bVisibleDctAuthManageCardPopup.value;
});
const dctAuthManageCardPopupReturnFunc = ref((retValue) => {
  dctAuthManageCardPopupToggleFunc.value();
});
/* ============================================================ */

/* ======================== 비밀관리기록부 팝업 ======================== */
const bVisibleDctManageRecordPopup = ref(false)
const dctManageRecordPopupArgs = ref({})
const dctManageRecordPopupToggleFunc = ref(() => {
  bVisibleDctManageRecordPopup.value = !bVisibleDctManageRecordPopup.value; 
})
const dctManageRecordPopupReturnFunc = ref((retValue) => {
  dctManageRecordPopupToggleFunc.value()
})
/* ============================================================ */

/* ======================== 비밀배포카드 팝업 ======================== */
const bVisibleDctDistributeCardPopup = ref(false)
const dctDistributeCardPopupArgs = ref({})

const state = reactive({
  bVisibleDctDistributeCardPopup: false,
  dctDistributeCardPopupArgs: {}
});
const checkConditionAndTogglePopup = () => {
  //발송대기,종료 상태시엔 비밀배포카드 대신 알럿.
  if (['DRDS3', 'DRDS4'].includes(state.dctDistributeCardPopupArgs.stateCheck)) {
    alert('비밀배포카드가 존재하지 않습니다.');
  } else {
    state.bVisibleDctDistributeCardPopup = !state.bVisibleDctDistributeCardPopup;
  }
};

const dctDistributeCardPopupToggleFunc = ref(() => {
  state.bVisibleDctDistributeCardPopup = !state.bVisibleDctDistributeCardPopup;
})
const dctDistributeCardPopupReturnFunc = ref((retValue) => {
  dctDistributeCardPopupToggleFunc.value();
})
/* ============================================================ */

/* ======================== 검토용인쇄 팝업 ======================== */
const bVisibleDctReviewPrintPopup = ref(false);
const dctReviewPrintPopupReturn = ref({});
const dctReviewPrintPopupArgs = ref({});
const dctReviewPrintPopupToggleFunc = ref(() => {
  dctReviewPrintPopupArgs.value = updateCardData.value;
  bVisibleDctReviewPrintPopup.value = !bVisibleDctReviewPrintPopup.value;
});
const dctReviewPrintPopupReturnFunc = ref((retValue) => {
  dctReviewPrintPopupToggleFunc.value();
  dctReviewPrintPopupReturn.value = retValue;
});
/* ============================================================ */

/* ======================== 기안문조회 ======================== */
import HwpCtrlPopup from "./HwpCtrlPopup.vue";
import HwpCtrl from "./HwpCtrl.vue";
const childRef = ref(null);

const draftToggle = ref({ data: "N" });
const draftView = ref({ data: "Y" });
const readDraftDoc = ref(() => {
  if (showCreateCardData.value.drafturl != "") {
    if (draftToggle.value.data == 'Y') {
      draftToggle.value.data = "N";
    } else {
      draftToggle.value.data = "Y";
    }
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
});
/* ============================================================ */

/* ======================== 기안문 버전별 조회 팝업 ======================== */
const bVisibleHwpCtrlPopup = ref(false)
const hwpCtrlPopupArgs = ref({})
const hwpCtrlPopupToggleFunc = ref((pathData) => {
  if (showCreateCardData.value.draftfile.length > 0) {
    if (pathData != null && pathData != undefined) {
      // 전체 조회 데이터 넘겨줌
      hwpCtrlPopupArgs.value.data = showCreateCardData.value;

      // asktype 넘겨줌
      hwpCtrlPopupArgs.value.asktype = pathData.asktype;

      // userid 넘겨줌
      hwpCtrlPopupArgs.value.objid = pathData.objid;

      // url 넘겨줌
      let arr = [];
      for (let i = 0; i < showCreateCardData.value.pathList.length; i++) {
        let object = {};
        object.id = showCreateCardData.value.pathList[i].objid;

        for (let j = 0; j < showCreateCardData.value.draftfile.length; j++) {
          if (object.id == showCreateCardData.value.draftfile[j].inid) {
            object.url = showCreateCardData.value.draftfile[j].fleurl;
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
            let urlString = '';
            for(let j= arr.length-1; j>=0; j--) {
              if(arr[j].url != '') {
                urlString = arr[j].url;
                break;
              }
            }
            hwpCtrlPopupArgs.value.url = urlString;
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
  authorid: "", // 담당자ID
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
  mgmtno: "",
  copyno: "",
  docno: "",

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
      objid: "",
      objname: "",
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

  // 기안문 url
  drafturl: "",
  draftdocttl: "",

});
const getMgmtidByDocidCondi = ref({docid: ""})
const getMgmtidByDocidList = ref([])

const getFromPage = ref('')
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const fetchDataBefore = () => {
  getFromPage.value = query.value.frompage;
  docidData.value.docid = query.value.docid;
  showCreateCardData.value.resdocid = query.value.docid;
  getMgmtidByDocidCondi.value.docid = query.value.docid;
  dctHistoryCardPopupArgs.value.docid = query.value.docid;
  dctManageRecordPopupArgs.value.docid = query.value.docid;
  dctAuthManageCardPopupArgs.value.docid = query.value.docid;
  dctDistributeCardPopupArgs.value.docid = query.value.docid;
  state.dctDistributeCardPopupArgs.docid = query.value.docid;
};

const fetchData = async() => {
  await getDctRdoc();
  await getDctSummary();
  await getDctAttr();
  await selectDctPath();
  await getDctRecv();
  await selectDctReader();
  await selectTaskInfo();
  await selectDctFile();
  await getMgmtidByDocid();
  await checkIsReusable();
  if(showCreateCardData.value.copyprotdt == null){
    chkbox.value = true;
  }

  if (isReceivedDoc.value == true) {
    recvDocidData.value.docid = showCreateCardData.value.oridocid;
    if(recvDocidData.value.docid != undefined && recvDocidData.value.docid != null && recvDocidData.value.docid != "") {
      await getDctAttrOridocid();
      await getDctRecvOridocid();
      await selectDctPathOridocid();
    }
  }
}

onBeforeMount(() => {
  params.value = route.params;
  query.value = route.query;
  fetchDataBefore();
});

onMounted( async () => {
  fetchData();
})

watch(() => route.query, () => {
  params.value = route.params;
  query.value = route.query;
  fetchDataBefore();
  fetchData();
});

const chkbox = ref(false);

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


/* ======================== 원본보호기간 ======================== */
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

const orgnReclassSelected = computed(() => {
  if(showCreateCardData.value.orgnreclass == '21'){
    return '일반문서';
  }else{
    return '선택'
  }
}); 

const orgnReclassData = [
  { key: '선택', value: ''},
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'}, // 임시 설정
];

/* ============================================================ */

/* ======================== 사본보호기간 ======================== */
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

const copyReclassSelected = computed(() => {
  if(showCreateCardData.value.copyreclass == '21'){
    return '일반문서';
  }else{
    return '선택'
  }
}); 

const copyReclassData = [
  { key: '선택', value: ''},
  { key: '존안', value: '20'},
  { key: '재분류', value: '21'},
  { key: '파기', value: '23'},
];
/* ============================================================ */


// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N'},
  { emergencyDataKey: '지급', emergencyDataValue: 'Z'},
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y'},
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X'},
];

/* ======================== 첨부파일 ======================== */
const downloadFile = async(file) => {
  try {
    var fileInfo = {
      uploadPath: file.flepath,
      orgFilename: file.sfilename,
    };

    const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
    SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
/* ============================================================ */


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
    showCreateCardData.value.authorid = data.authorid;
    showCreateCardData.value.authordutyname = data.authordutyname;
    showCreateCardData.value.authordeptname = data.authordeptname;
    showCreateCardData.value.tempflag = data.tempflag;
    showCreateCardData.value.indt = data.indt;
    showCreateCardData.value.oridocid = data.oridocid;
    state.dctDistributeCardPopupArgs.stateCheck = showCreateCardData.value.state;
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

const isReceivedDoc = ref(false);

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
    showCreateCardData.value.prsrvterm = data.prsrvterm ? (data.prsrvterm.trim() != "" ? data.prsrvterm : '') : '';
    showCreateCardData.value.orgnprotdt = data.orgnprotdt;
    showCreateCardData.value.orgnnextlevel = data.orgnnextlevel ;
    showCreateCardData.value.orgnreclass = data.orgnreclass ? (data.orgnreclass.trim() != "" ? data.orgnreclass : '') : '';
    showCreateCardData.value.copyprotdt = data.copyprotdt;
    showCreateCardData.value.copynextlevel = data.copynextlevel;
    showCreateCardData.value.copyreclass = data.copyreclass ? (data.copyreclass.trim() != "" ? data.copyreclass : '') : '';
    showCreateCardData.value.docno = data.docno;
    showCreateCardData.value.mgmtno = data.mgmtno;
    showCreateCardData.value.copyno = data.copyno;
    isReceivedDoc.value = showCreateCardData.value.copyno != null && !showCreateCardData.value.copyno.includes("원") ? true : false;
    if(showCreateCardData.value.orgnprotdt){
      sliceDates(showCreateCardData.value.orgnprotdt, orgnDate.value);
      orgnProtDtPeriodSelected.value = (parseInt(orgnDate.value.year) - parseInt(showCreateCardData.value.indt.slice(0,4))).toString();
    }
    if(showCreateCardData.value.copyprotdt){
      sliceDates(showCreateCardData.value.copyprotdt, copyDate.value);
      copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(showCreateCardData.value.indt.slice(0,4))).toString();
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
      upd.reportdt = response.data[i].reportdt != null ? dayjs(response.data[i].reportdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm') : "";
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.objid = response.data[i].objid;
      upd.objname = response.data[i].reportdt != null ? response.data[i].objname : "";
      upd.opinion = response.data[i].opinion;
      upd.pathstate = response.data[i].pathstate;

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
      if(response.data[0].distdeptname != null){
        showCreateCardData.value.sendOrgNameData = response.data[0].distdeptname;
        showCreateCardData.value.sendNameData = showCreateCardData.value.sendOrgNameData == '원자력통제기술원' ? '원자력통제기술원장' : showCreateCardData.value.sendOrgNameData+ '부서장';
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 수신자
const formatRecvList = computed(() => {
  const formattedRecvList = showCreateCardData.value.recvList.map((item) => {
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

// 열람권자
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
      upd.flepath = response.data[i].flepath;
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
    showCreateCardData.value.draftfile = [];

    // 붙임파일 전체 (이력용)
    showCreateCardData.value.fileListAll = [];
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
        upd.inid = response.data[i].inid;
        upd.indt = response.data[i].indt;
        showCreateCardData.value.fileListAll.push(upd);

        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT02') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          showCreateCardData.value.fileList.push(upd);
        }
      } else {
        showCreateCardData.value.draftfile.push(response.data[i]);
      }
    }

    // 관인 붙어있는 데이터 삭제
    let indtArr = [];
    let arrIdx = [];
    for (let j = 0; j < showCreateCardData.value.draftfile.length; j++) {
      indtArr.push(Number(showCreateCardData.value.draftfile[j].indt));
      if (showCreateCardData.value.authorid == showCreateCardData.value.draftfile[j].inid) {
        arrIdx.push(j);
      }
    }
    if (arrIdx.length > 1) {
      showCreateCardData.value.draftfile.splice(arrIdx[0], 1);
    }

    let maxIndt = Math.max(...indtArr);
    for (let k = 0; k < showCreateCardData.value.draftfile.length; k++) {
      if (Number(showCreateCardData.value.draftfile[k].indt) == Number(maxIndt)) {
        showCreateCardData.value.draftdocttl = showCreateCardData.value.draftfile[k].sfilename;
        showCreateCardData.value.drafturl = showCreateCardData.value.draftfile[k].fleurl;
      } else if (showCreateCardData.value.draftfile.length == 1) {
        showCreateCardData.value.draftdocttl = showCreateCardData.value.draftfile[0].sfilename;
        showCreateCardData.value.drafturl = showCreateCardData.value.draftfile[0].fleurl;
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getMgmtidByDocid = async () => {
  try {
    const response = await API.dctAPI.getMgmtidByDocid(getMgmtidByDocidCondi.value, urlPaths.value);
    getMgmtidByDocidList.value = response.data;
    dctManageRecordPopupArgs.value.mgmtid = getMgmtidByDocidList.value.mgmtid;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const checkIsReusableCondi = ref({});
const checkIsReusableResult = ref([]);
const checkIsReusable = async () => {
  checkIsReusableCondi.value.mgmtid = getMgmtidByDocidList.value.mgmtid;
  try {
    const response = await API.useAPI.checkIsReusable(checkIsReusableCondi.value, urlPaths.value);
    checkIsReusableResult.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const moveToBmsUsetempcard = (docid) => {
  const param = {docid: docid, frompage: getFromPage.value}
  router.push({
    name: 'BmsUsetempcard',
    query: {
      ...param
    }
  });
};

// Move Function
const moveToList = () => {
  const page = getFromPage.value || "BmsDctmgmtregilist";
  router.push({
    name: page,
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


// 접수문서 시행정보 조회
const recvDocidData = ref({ docid: "" });

const oriAttrData = ref({});
const oriPathList = ref([]);
const oriPathData = ref("");
const oriRecvList = ref([]);
const oriRecvData = ref({});

const getDctAttrOridocid = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriAttrData.value = response.data;
    let docnoArr = oriAttrData.value.docno.split('-');
    oriAttrData.value.docnosplit = docnoArr[0];

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectDctPathOridocid = async () => {
  try {
    const response = await API.dctAPI.selectDctPath({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriPathList.value = response.data;

    oriPathData.value = formatPathList.value;

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }

}

const getDctRecvOridocid = async () => {
  try {
    const response = await API.dctAPI.getDctRecv({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriRecvList.value = response.data;

    for (let i = 0; i < oriRecvList.value.length; i++) {
      // if (oriRecvList.value[i].deptid == getUserLoginData.value.deptid) {
      //   oriRecvData.value = oriRecvList.value[i];
      // }
      oriRecvData.value = oriRecvList.value[0];
    }

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const formatPathList = computed(() => {
  const formattedPathList = oriPathList.value.map((item) => {
    return `${item.dutynamedesc}(${transformAsktype(item.asktype)} ${transformDate(item.reportdt)})`;
  });
  return formattedPathList.join(', ');
});
</script>

