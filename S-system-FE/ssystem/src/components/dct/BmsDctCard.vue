<!-- BmsDctCard.vue -->

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
              :items="secLevelData"
              item-title="secLevelDataKey"
              item-value="secLevelDataValue"
              variant="outlined"
              :readonly="true"
              hide-details="auto"></v-select>
          </td>
          <th>관리번호</th>
          <td>
            <v-text-field
              v-model="receivedData.mgmtno"
              variant="solo"
              hide-details="auto"
              :readonly="true"></v-text-field>
          </td>
          <th>사본번호</th>
          <td>
            <v-text-field
              v-model="receivedData.copyno"
              variant="solo"
              hide-details="auto"
              :readonly="true"></v-text-field>
          </td>
        </tr>
        <tr>
          <th rowspan="3"><span class="required">예고문</span></th>
          <td colspan="5">
            <!-- 예고문 - 보존기간 -->
            <div class="box-flex">
              <v-label>보존기간:</v-label>
              <v-select
                v-model="receivedData.prsrvterm"
                :items="prsRvTermData"
                item-title="prsRvTermDataKey"
                item-value="prsRvTermDataValue"
                variant="outlined"
                hide-details="auto"
                :readonly="true"></v-select>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="5">
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
                  v-model="receivedData.orgnreclass"
                  :items="orgnReclassData"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"
                  :readonly="true">
                </v-select>
            </div>
          </td>
        </tr>
        <tr>
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
                hide-details="auto"></v-select>
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
                v-model="receivedData.copyreclass"
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
              v-model="props.data.docttl"
              variant="solo"
              hide-details="auto"
              :readonly="true"></v-text-field>
          </td>
          <th>긴급도</th>
          <td>
            <v-select
              v-model="receivedData.emergency"
              :items="emergencyData"
              item-title="emergencyDataKey"
              item-value="emergencyDataValue"
              variant="outlined"
              hide-details="auto"
              :readonly="true"></v-select>
          </td>
        </tr>
        <tr>
          <th>
            <div>
              관련근거
            </div>
          </th>
          <td>
            <div v-for="refData, idx in responseData.refList" :key="idx">
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
                v-model="receivedData.keyword"
                variant="solo"
                hide-details="auto"
                :readonly="true"></v-text-field>
            </td>
        </tr>
        <tr>
          <th>문서취지</th>
          <td colspan="3">
            <span>{{ receivedData.summary }}</span>
          </td>
        </tr>
        <tr>
          <th>기안문</th>
          <td colspan="3">
              <div class="box-flex" v-if="receivedData.draftfile!=undefined && receivedData.draftfile[0] ">
                <v-icon v-if="receivedData.draftfile && receivedData.draftfile.length > 0" :color="getFileIcon(getExtension(receivedData.draftfile[receivedData.draftfile.length-1].sfilename)).color">{{ getFileIcon(getExtension(receivedData.draftfile[receivedData.draftfile.length-1].sfilename)).icon }}</v-icon>
                <span v-if="receivedData.draftfile && receivedData.draftfile.length > 0" @click="toggleDraft()" style="font-weight: 600; cursor: pointer;">{{ receivedData.draftfile[receivedData.draftfile.length-1].sfilename }}</span>
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
        <tr v-for="pathData, idx in receivedData.pathList" :key="idx">
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
            {{ transformDate(pathData.reportdt) }}
          </td>
          <td>
            <span v-if="pathData.reportdt" @click="hwpCtrlPopupToggleFunc(pathData)" style="cursor: pointer;">v{{ pathData.mainver }}.0</span>
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
              v-model="receivedData.sendOrgNameData"
              :readonly="true"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </td>
          <th>발신명의</th>
          <td>
            <v-text-field
              class="w100"
              v-model="receivedData.sendNameData"
              :readonly="true"
              variant="outlined"
              hide-details="auto"
            ></v-text-field>
          </td>
        </tr>
        <tr>
          <th>수신자</th>
          <td colspan="3">
            <v-text-field
              v-model="formatRecvList"
              variant="solo"
              hide-details="auto"
              :readonly="true"></v-text-field>
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
              v-model="receivedData.docno"
              variant="solo"
              hide-details="auto"
              :readonly="true"></v-text-field>
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

import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
  defineEmits
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { API } from "@/api";
import { getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import dayjs from "dayjs";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel, transformSecstatus, transformObjStatus, transformEnfHisActgubun, transformPathState } from "@/utils/TransFormLabelDataUtil.js";

/*============ 기안문 관련 ===============*/
import HwpCtrlPopup from "./HwpCtrlPopup.vue";
const emit = defineEmits(['updateToggle']);
const toggleDraft = () => {
  if (receivedData && receivedData.drafturl !== "") {
    const newValue = props.draftToggle.data === 'Y' ? 'N' : 'Y';
    emit('updateToggle', newValue);
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
};
/*============ 기안문 관련 ===============*/

const name = ref("BmsDctCard");
const props = defineProps({
  data: Object,
  draftToggle: Object //부모창에 전달할 emit
});
const data = ref({});
const args = ref({});
const route = useRoute();
const router = useRouter();
const receivedData = ref({});
// for popup
const urlPaths = ref("");
const isReceivedDoc = ref(false);
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)

const responseData = ref([]);
const docidData = ref({docid: ""});
const reportdtConverted = ref("");
//예고문 - 원본보호기간
const bmsDctAttrOrgnProtDt = ref({
  totalStr: "",
  year: "",
  month: "",
  day: "",
});
//예고문 - 사본보호기간
const bmsDctAttrCopyProtDt = ref({
  totalStr: "",
  year: "",
  month: "",
  day: "",
});

// bmsDctAttrDateSetting 참조
// 예고문 원본보호기간 및 사본보호기간 날짜 설정(원본보호기간 : reportdt날짜 + 2년, 사본보호기간 : reportdt날짜 + 1년)
const bmsDctAttrDateGetting = async () => {
  reportdtConverted.value = dayjs(receivedData.orgnprotdt).format("YYYY-MM-DD");

  bmsDctAttrOrgnProtDt.value.totalStr = dayjs(reportdtConverted.value)
    .add(2, "year")
    .format("YYYY-MM-DD");

  const date = ref("");

  date.value = dayjs(bmsDctAttrOrgnProtDt.value.totalStr);

  bmsDctAttrOrgnProtDt.value.year = date.value.format("YYYY");
  bmsDctAttrOrgnProtDt.value.month = date.value.format("MM");
  bmsDctAttrOrgnProtDt.value.day = date.value.format("DD");

  bmsDctAttrCopyProtDt.value.totalStr = dayjs(reportdtConverted.value)
    .add(1, "year")
    .format("YYYY-MM-DD");

  date.value = dayjs(bmsDctAttrCopyProtDt.value.totalStr);

  bmsDctAttrCopyProtDt.value.year = date.value.format("YYYY");
  bmsDctAttrCopyProtDt.value.month = date.value.format("MM");
  bmsDctAttrCopyProtDt.value.day = date.value.format("DD");
};

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

const downloadFileAll = async(files) => {
  try {
    for (const file of files) {
      var fileInfo = {
        uploadPath: file.flepath,
        orgFilename: file.sfilename,
      };

      const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
      SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
    }
  } catch (error) {
    console.error(error);
    alert("Server Error");
  }
};
/* ============================================================ */


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
      receivedData.value.emergency = data.emergency;
      receivedData.value.keyword = data.keyword;
      receivedData.value.authorname = data.authorname;
      receivedData.value.authordutyname = data.authordutyname;
      receivedData.value.authordeptname = data.authordeptname;
      receivedData.value.tempflag = data.tempflag;
      receivedData.value.indt = data.indt;
    } catch (error) {
      console.log(error);
      alert("Server Error");
    }
  }
};

// 3) bms_dct_attr table
const getDctAttr = async () => {
  const response = await API.dctAPI.getDctAttr({
      docid: props.data.docid},
      urlPaths.value
    );
    const data = response.data;

    receivedData.value.secgubun = data.secgubun;
    receivedData.value.seclevel = data.seclevel;
    receivedData.value.prsrvterm = data.prsrvterm ? (data.prsrvterm.trim() != "" ? data.prsrvterm : '') : '';
    receivedData.value.orgnprotdt = data.orgnprotdt;
    receivedData.value.orgnnextlevel = data.orgnnextlevel ;
    receivedData.value.orgnreclass = data.orgnreclass ? (data.orgnreclass.trim() != "" ? data.orgnreclass : '') : '';
    receivedData.value.copyprotdt = data.copyprotdt;
    receivedData.value.copynextlevel = data.copynextlevel;
    receivedData.value.copyreclass = data.copyreclass ? (data.copyreclass.trim() != "" ? data.copyreclass : '') : '';
    receivedData.value.docno = data.docno;
    receivedData.value.mgmtno = data.mgmtno;
    receivedData.value.docno = data.docno;
    isReceivedDoc.value = receivedData.value.copyno != null && !receivedData.value.copyno.includes("원") ? true : false;
  if(receivedData.value.orgnprotdt){
    sliceDates(receivedData.value.orgnprotdt, orgnDate.value);
  }
  if(receivedData.value.copyprotdt){
    sliceDates(receivedData.value.copyprotdt, copyDate.value);
  }
  if(receivedData.value.orgnprotdt){
    sliceDates(receivedData.value.orgnprotdt, orgnDate.value);
    orgnProtDtPeriodSelected.value = (parseInt(orgnDate.value.year) - parseInt(receivedData.value.indt.slice(0,4))).toString();
  };
  if(receivedData.value.copyprotdt){
    sliceDates(receivedData.value.copyprotdt, copyDate.value);
    copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(receivedData.value.indt.slice(0,4))).toString();
  };
};
const sliceDates = (dateString, target) =>{
  const year = parseInt(dateString.slice(0, 4));
  const month = parseInt(dateString.slice(4, 6));
  const day = parseInt(dateString.slice(6, 8));
  target.year = year;
  target.month = month;
  target.day = day;
};

// 4) bms_dct_path table
const selectDctPath  = async () => {
  receivedData.value.pathList = [];
  if(receivedData.value.pathlist){
    if(receivedData.value.pathlist.length >0){
    for(let i=0; i<receivedData.value.pathlist.length; i++){
        const upd = {};
        upd.askTemp = transformAsktype(receivedData.value.pathlist[i].asktype);
        upd.asktype = receivedData.value.pathlist[i].asktype;
        upd.deptname = receivedData.value.pathlist[i].deptname;
        upd.dutyname = receivedData.value.pathlist[i].dutyname;
        upd.dutynamedesc = receivedData.value.pathlist[i].dutynamedesc;
        upd.electsignid = receivedData.value.pathlist[i].electsignid;
        upd.reportdt = receivedData.value.pathlist[i].reportdt;
        upd.mainver = receivedData.value.pathlist[i].mainver;
        upd.pathorder = receivedData.value.pathlist[i].pathorder;
        upd.objid = receivedData.value.pathlist[i].objid;
        upd.objname = receivedData.value.pathlist[i].reportdt != null ? receivedData.value.pathlist[i].objname : "";
        upd.opinion = receivedData.value.pathlist[i].opinion;
        upd.pathstate = receivedData.value.pathlist[i].pathstate;

        // 처리결과 
        upd.acttype = receivedData.value.pathlist[i].acttype;
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

        receivedData.value.pathList.push(upd);
      }
    }
  }
}

// 6) bms_dct_appr_recv
const getDctRecv = async () => {
  receivedData.value.recvList = [];
  if(receivedData.value.recvlist){
    for(let i=0; i<receivedData.value.recvlist.length; i++){
      const upd = {};
      upd.receiverid = receivedData.value.recvlist[i].receiverid;
      upd.apprrecvtempflag = receivedData.value.recvlist[i].empflag;
      upd.apprrecvseq = receivedData.value.recvlist[i].seq;
      upd.apprrecvdeptname = receivedData.value.recvlist[i].deptname;
      upd.apprrecvrefdeptname = receivedData.value.recvlist[i].refdeptname;
      upd.relaytype = receivedData.value.recvlist[i].relaytype;
      receivedData.value.recvList.push(upd);
      receivedData.value.sendOrgNameData = receivedData.value.recvlist[i].distdeptname;
      receivedData.value.sendNameData = receivedData.value.recvlist[i].distname;
    }
  }
}
// 수신자
const formatRecvList = computed(() => {
  if(receivedData.value.recvList){
    const formattedRecvList = receivedData.value.recvList.map((item) => {
      const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
      return `${item.apprrecvdeptname}${refDeptName}`;
    });
    return formattedRecvList.join(', ');
  }
});

// 7) bms_dct_rdoc_rcvs
const selectDctReader = async () => {
  receivedData.value.rcvsList = [];
  if(receivedData.value.rcvslist){
    for(let i=0; i<receivedData.value.rcvslist.length; i++){
      const upd = {};
      upd.rcvsuserid = receivedData.value.rcvslist[i].userid;
      upd.rcvsusername = receivedData.value.rcvslist[i].username;
      upd.rcvsdeptid = receivedData.value.rcvslist[i].deptid;
      upd.rcvdeptname = receivedData.value.rcvslist[i].deptname;
      upd.rcvdutyname = receivedData.value.rcvslist[i].dutyname;
      upd.rcvsdeptnamedesc = receivedData.value.rcvslist[i].deptnamedesc;
      upd.rcvsinseq = receivedData.value.rcvslist[i].inseq;
      receivedData.value.rcvsList.push(upd);
    }
  }
}

// 열람권자
const formatRcvsList = computed(() => {
  if(receivedData.value.rcvsList){
    const formattedRcvsList = receivedData.value.rcvsList.map((item) => {
      return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
    });
    return formattedRcvsList.join(', ');
  }
});

// 8) bms_task_infomation table
const selectTaskInfo  = async () => {
  try {
    const response = await API.dctAPI.selectTaskInfo ({
      docid: props.data.docid },
      urlPaths.value
    );
    responseData.value.refList = [];
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
      responseData.value.refList.push(upd);
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


onBeforeMount(() => {});
onBeforeMount(async () => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
})

onMounted(async () => {
  receivedData.value = props.data;
  await bmsDctAttrDateGetting();
});

watch(() => props.draftToggle.data, (newValue) => {
  emit('updateToggle', newValue);
});
watchEffect(async() => {
  receivedData.value = props.data;
  docidData.value.docid = props.data.docid;
  receivedData.value.vgroupid = '5210'; //com_vgroup.vgroupid 를 참조하여 com_vgroup_user.vgroupid 에 넣을 값임.
  await bmsDctAttrDateGetting(); 
  if((props.data.docid || props.data.docid != undefined) && props.data.docid.value !== ""){
    await getDctRdoc();
    await getDctAttr();
    await selectDctPath();
    await getDctRecv();
    await selectDctReader();
    await selectTaskInfo();
    await selectDctFile();
  }
});

// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: "II급", secLevelDataValue: "2" },
  { secLevelDataKey: "III급", secLevelDataValue: "3" },
  { secLevelDataKey: "일반", secLevelDataValue: "5" },
];

// 보존기간 Select-Box
const prsRvTermData = [
  { prsRvTermDataKey: "1년", prsRvTermDataValue: "001" },
  { prsRvTermDataKey: "3년", prsRvTermDataValue: "003" },
  { prsRvTermDataKey: "5년", prsRvTermDataValue: "005" },
  { prsRvTermDataKey: "10년", prsRvTermDataValue: "010" },
  { prsRvTermDataKey: "30년", prsRvTermDataValue: "030" },
  { prsRvTermDataKey: "준영구", prsRvTermDataValue: "040" },
  { prsRvTermDataKey: "영구", prsRvTermDataValue: "050" },
  { prsRvTermDataKey: "직접입력", prsRvTermDataValue: "099" },
];

/* ======================== 원본보호기간 ======================== */
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

const orgnReclassSelected = computed(() => {
  if(receivedData.value.orgnreclass == '21'){
    return '일반문서';
  }else{
    return '선택'
  }
}); 

const orgnReclassData = [
  { key: '선택', value: '0'},
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'}, // 임시 설정
];
/* ============================================================ */

/* ======================== 사본보호기간 ======================== */
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

const copyDate = ref({
  year: "",
  month: "",
  day: "",
})

const copyReclassSelected = computed(() => {
  if(receivedData.value.copyreclass == '21'){
    return '일반문서';
  }else{
    return '선택'
  }
}); 
const copyReclassData = [
  { key: '선택', value: '0'},
  { key: '존안', value: '20'},
  { key: '재분류', value: '21'},
  { key: '파기', value: '23'},
];
/* ============================================================ */

// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: "일반", emergencyDataValue: "N" },
  { emergencyDataKey: "지급", emergencyDataValue: "Z" },
  { emergencyDataKey: "긴급", emergencyDataValue: "Y" },
  { emergencyDataKey: "초긴급", emergencyDataValue: "X" },
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
      for (let i = 0; i < receivedData.value.pathList.length; i++) {
        let object = {};
        object.id = receivedData.value.pathList[i].objid;

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
</script>