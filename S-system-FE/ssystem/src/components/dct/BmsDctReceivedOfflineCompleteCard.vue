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
      <h4 v-if="getFromPage == 'BmsDctreqcompletelist'" class="h4">기안완료함</h4>
      <h4 v-if="['BmsDctapprovalcompletelist','BmsDctinglist'].includes(getFromPage)" class="h4">결재</h4>
      <h4 v-if="getFromPage == 'BmsDcttodolistread'" class="h4">비밀열람함</h4>
      <h4 v-if="getFromPage == 'BmsDctmgmtregilist'" class="h4">비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsUseConserveMgmtRegiList'" class="h4">존안비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsUseTransMgmtRegiList'" class="h4">이관비밀관리기록부</h4>
      <h4 v-if="['BmsUseViewReqList','BmsReUseReqList','BmsUsePrintReqList','BmsUseTakeOutReqList'].includes(getFromPage)" class="h4">비밀문서 사용요청</h4>
      <h4 v-if="getFromPage == 'BmsCominglist'" class="h4">예고문도래목록</h4>
      <div class="action-buttons3">
        <v-btn class="button" v-if="!['BmsDctreqcompletelist','BmsDctinglist'].includes(getFromPage)" @click="bVisibleDctHistoryCardPopup = true">비밀이력카드</v-btn>
        <v-btn class="button" v-if="!['BmsDctreqcompletelist','BmsDctinglist'].includes(getFromPage)" @click="bVisibleDctAuthManageCardPopup = true">비밀권한관리카드</v-btn>
        <v-btn class="button" v-if="!['BmsDctreqcompletelist','BmsDctinglist'].includes(getFromPage)" @click="checkConditionAndTogglePopup">비밀배포카드</v-btn>
        <v-btn class="button" v-if="!['BmsDctreqcompletelist','BmsDctinglist'].includes(getFromPage)" @click="bVisibleDctManageRecordPopup = true">비밀관리기록부</v-btn>
      </div>
    </div>

    <div class="box-content">
      <!-- 비전자비밀접수 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비전자 비밀문서</h5>
          <div class="action-buttons">
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
                    v-model="showOfflineData.seclevel"
                    :readonly="true"
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
                      v-model="showOfflineData.copyno"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"></v-text-field>
                  </div>
                </td>
                <th>관리번호</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="showOfflineData.mgmtno"
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
                      :items="copyReclass"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="true">
                    </v-select>
                    <v-label>(으)로</v-label>
                    <v-select
                      v-model="showOfflineData.copyreclass"
                      :readonly="true"
                      :items="copyReclassData"
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
                    v-model="showOfflineData.docttl"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"
                    ></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="showOfflineData.emergency"
                    :readonly="true"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"></v-select>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-model="showOfflineData.keyword"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>붙임</th>
                <td colspan="5">
                  <div v-if="showOfflineData.fileList != []">
                    <div class="box-flex" v-for="(fileData,idx) in showOfflineData.fileList" :key="idx">
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
              <tr v-for="pathData, idx in showOfflineData.pathList" :key="idx">
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
                    v-model="showOfflineData.sendOrgNameData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>발신기관명의</th>
                <td>
                  <v-text-field
                    v-model="showOfflineData.sendNameData"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>생산문서번호</th>
                <td>
                  <v-text-field
                    v-model="showOfflineData.proddocnum"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>접수번호</th>
                <td>
                  <v-text-field 
                    v-model="showOfflineData.acceptdocnum"
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
                    v-model="showOfflineData.senddate"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>시행범위</th>
                <td>
                  <v-select
                    class="w100"
                    v-model="showOfflineData.sendrange"
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
                    v-model="showOfflineData.reciveddt"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>등재일자</th>
                <td>
                  <v-text-field
                    v-model="showOfflineData.reciveddt"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>접수부서</th>
                <td>
                  <v-text-field 
                    v-model="showOfflineData.authordeptname"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>자료량<br>(쪽수,용량)</th>
                <td>
                  <v-text-field 
                    v-model=showOfflineData.docpagenum
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>자료유형</th>
                <td colspan="3">
                  <v-radio-group v-model="showOfflineData.doctype" inline hide-details="auto" class="radio-group-spacing">
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
                  <v-radio-group v-model="showOfflineData.mediatype" inline hide-details="auto" class="radio-group-spacing">
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
                    v-model="showOfflineData.docno"
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
                    v-model=showOfflineData.locker
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>단번호</th>
                <td>
                  <v-text-field 
                    v-model=showOfflineData.unitnum
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
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";
import DctAuthManageCardPopup from "./DctAuthManageCardPopup.vue";
import BmsDctDistributeCardPopup from "./BmsDctDistributeCardPopup.vue";
import DctManageRecordPopup from "./DctManageRecordPopup.vue";
const name = ref('BmsDctReceivedOfflinecompletecard')

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
const route = useRoute();
const router = useRouter();
const urlPaths =  ref('');
const getFromPage = ref('')
const today = ref("")
const showOfflineData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "DOPT2", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "DRDS2", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
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

  // 접수정보
  sendOrgNameData: "", // 발신기관명
  sendNameData: "", // 발신기관명의
  senddate: "", // 시행일자
  proddocnum: "", // 생산등록번호
  sendrange: "RDR01", // 시행범위

  // 관리정보
  regideptname: "", // 접수부서
  docpagenum: "", // 자료량(쪽수)
  doctype: "문서", // 자료유형
  mediatype: "전자파일", // 매체유형

  // 보관함 정보
  locker: "", // 보관함
  unitnum: "", // 단번호
});

const isloading = ref(false);
const params = ref({});
const query = ref({});
const docidData = ref({docid: ""});
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const fetchDataBefore = () => {
  getFromPage.value = query.value.frompage;
  docidData.value.docid = query.value.docid;
  showOfflineData.value.resdocid = query.value.docid;
  getMgmtidByDocidCondi.value.docid = query.value.docid;
  dctHistoryCardPopupArgs.value.docid = query.value.docid;
  dctManageRecordPopupArgs.value.docid = query.value.docid;
  dctAuthManageCardPopupArgs.value.docid = query.value.docid;
  dctDistributeCardPopupArgs.value.docid = query.value.docid;
  state.dctDistributeCardPopupArgs.docid = query.value.docid;
};

const fetchData = async() => {
  await getDctRdoc();
  await getDctAttr();
  await getDctAttrAdd();
  await selectDctPath();
  await getDctRecv();
  await selectDctReader();
  await selectDctFile();
  await getReprocess();
  await getMgmtidByDocid();
};

onBeforeMount(() => {
  params.value = route.params;
  query.value = route.query;
  fetchDataBefore();
})

onMounted( async () => {
  fetchData();
})

watch(() => route.query, () => {
  params.value = route.params;
  query.value = route.query;
  fetchDataBefore();
  fetchData();
});

watchEffect(() => {
})


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
  showOfflineData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
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
  showOfflineData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
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
    showOfflineData.value.newFileList.push({ file: data.file, id: data.id });
  }
};

const handleFileRemove = (error, data) => {
  const index = attachFileData.value.findIndex(attach => attach.id === data.id);
  if (index !== -1) {
    attachFileData.value.splice(index, 1);
    showOfflineData.value.newFileList.splice(index, 1);
    showOfflineData.value.delFileList.push(index);
  }
};

const newAttachFileArray = computed(() => {
  return attachFileData.value.map(item => item.file);
});

const attachFileData = ref([]);

/* ============================================================ */

 // 1) bms_dct_rdoc table
 const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({
      ...docidData.value },
      urlPaths.value
    );
    const data = response.data;
    showOfflineData.value.docttl = data.docttl;
    showOfflineData.value.gubun = data.gubun;
    showOfflineData.value.state = data.state;
    showOfflineData.value.open = data.open;
    showOfflineData.value.readrangetype = data.readrangetype;
    showOfflineData.value.emergency = data.emergency;
    showOfflineData.value.keyword = data.keyword;
    showOfflineData.value.authorname = data.authorname;
    showOfflineData.value.authorid = data.authorid;
    showOfflineData.value.authordutyname = data.authordutyname;
    showOfflineData.value.authordeptname = data.authordeptname;
    showOfflineData.value.tempflag = data.tempflag;
    showOfflineData.value.indt = data.indt;
    state.dctDistributeCardPopupArgs.stateCheck = showOfflineData.value.state;
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
    showOfflineData.value.secgubun = data.secgubun;
    showOfflineData.value.seclevel = data.seclevel;
    showOfflineData.value.copyprotdt = data.copyprotdt;
    showOfflineData.value.copynextlevel = data.copynextlevel;
    showOfflineData.value.copyreclass = data.copyreclass ? (data.copyreclass.trim() != "" ? data.copyreclass : '') : '';
    showOfflineData.value.docno = data.docno;
    showOfflineData.value.mgmtno = data.mgmtno;
    showOfflineData.value.copyno = data.copyno;
    showOfflineData.value.docpagenum = data.docpagenum;
    if(showOfflineData.value.copyprotdt){
      sliceDates(showOfflineData.value.copyprotdt, copyDate.value);
      copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(showOfflineData.value.indt.slice(0,4))).toString();
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
    showOfflineData.value.mediatype = data.mediatype;
    showOfflineData.value.doctype = data.doctype;
    showOfflineData.value.locker = data.locker;
    showOfflineData.value.unitnum = data.unitnum;
    showOfflineData.value.proddocnum = data.proddocnum;
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
    showOfflineData.value.pathList = [];
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

      showOfflineData.value.pathList.push(upd);
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
    showOfflineData.value.recvList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].empflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      showOfflineData.value.recvList.push(upd);
    }
    showOfflineData.value.senddate = dayjs(response.data[0].actdt).format('YYYY-MM-DD');
    showOfflineData.value.sendOrgNameData = response.data[0].distdeptname;
    showOfflineData.value.sendNameData = response.data[0].distname
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 수신자
const formatRecvList = computed(() => {
  const formattedRecvList = showOfflineData.value.recvList.map((item) => {
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
    showOfflineData.value.rcvsList = [];
    for(let i=0; i<response.data.length; i++){
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsdeptnamedesc = response.data[i].deptnamedesc;
      upd.rcvsinseq = response.data[i].inseq;
      showOfflineData.value.rcvsList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 열람권자
const formatRcvsList = computed(() => {
  const formattedRcvsList = showOfflineData.value.rcvsList.map((item) => {
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
    showOfflineData.value.fileList = [];
    showOfflineData.value.draftfile = [];
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
        // showOfflineData.value.fileList.push(upd);

        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT02') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          showOfflineData.value.fileList.push(upd);
        }
      } else {
        showOfflineData.value.draftfile.push(response.data[i]);
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
    showOfflineData.value.acceptdocnum = data.acceptdeptname + '-S'+data.acceptdocseq;
    showOfflineData.value.reciveddt = dayjs(data.indt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getMgmtidByDocidCondi = ref({docid: ""})
const getMgmtidByDocidList = ref([])
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