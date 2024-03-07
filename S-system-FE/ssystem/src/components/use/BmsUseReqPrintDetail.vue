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
      <h4 class="h4">비밀문서사용</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">인쇄요청서</h5>
          <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
            <!-- 처리할요청서 -->
            <v-btn class="button" :disabled="hasViewingPermissionResult.result == 'false'" @click="bmsUseConfirmPopupToggleFunc('Y')">승인</v-btn>
            <v-btn class="button" @click="bmsUseConfirmPopupToggleFunc('N')">반려</v-btn>
            <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
          </div>
          <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsedraftlist'">
            <!-- 기안한요청서 -->
            <v-btn class="button" v-if="selectUseReqViewDetailData.reqstatus == '1' && isPossibleRevoke() || hasViewingPermissionResult.result == 'false'" @click="useReqStatusSave('5')">요청서회수</v-btn>
            <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
          </div>  
          <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUseproclist'">
            <!-- 처리한요청서 -->
            <v-btn class="button" v-if="selectUseReqViewDetailCondi.reqstatusnm === '승인' || selectUseReqViewDetailCondi.reqstatusnm === '실행완료'" @click="useReqStatusSave('4')">권한회수</v-btn>
            <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
          </div>    
          <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist'">
            <!-- 요청서완료처리 -->
            <v-btn class="button" @click="bmsUsePrintPopupToggleFunc(printReq)">{{ printReq == true ? "인쇄실행" : "파쇄실행" }}</v-btn>
            <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
          </div>  
        </div>
      </section>

      <!-- 요청정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청정보</h6>
        </div>

        <v-table class="table-type-03">
          <colgroup>
            <col width="120">
            <col width="">
            <col width="">
          </colgroup>
          <tbody>     
            <tr>
              <th>제목</th>
              <td>
                <v-text-field
                  v-model="selectUseReqViewDetailData.reqttl"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>요청자</th>
              <td>
                <v-text-field
                  v-model="selectUseReqViewDetailData.reqname"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>              
            <tr>
              <th>사유</th>
              <td>
                <v-text-field
                  v-model="selectUseReqViewDetailData.reqcontents"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
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

        <!-- :item-value="selectUseReqPathListIdentifier" -->
        <v-data-table
          :headers="staticColumnsSelectUseReqPathList"
          :items="selectUseReqViewDetailPathList"
          class="table-type-04"
          >
          <template v-slot:item="{ item }">
            <tr :style="{ 'text-decoration': selectUseReqViewDetailData.reqstatus == '4' || selectUseReqViewDetailData.reqstatus == '5' ? 'line-through red' : 'none' }">
              <td>{{ item.raw.apprtypenm }}</td>
              <td>{{ item.raw.apprdeptname }}</td>
              <td>{{ item.raw.positionnm }}</td>
              <td style="width: 600px;" class="text-left">{{ item.raw.appropinion }}</td>
              <td class="signature">
                {{
                  item.raw.apprstatusnm.includes("요청") ||
                  item.raw.apprstatusnm.includes("승인") ||
                  item.raw.apprstatusnm.includes("반려") ||
                  item.raw.apprstatusnm.includes("1인결재")
                    ? item.raw.apprstatusnm.includes("반려로 인한 결재취소")
                      ? ""
                      : item.raw.apprname
                    : ""
                }}
              </td>
              <td>{{ item.raw.apprstatusnm }}</td>
            </tr>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsedraftlist' && hasViewingPermissionResult.result == 'false'">
          <v-icon color="red">mdi-alert-box</v-icon>
          열람권한이 회수된 비밀문서가 존재합니다. 요청서를 회수해주세요.
        </span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' && selectUseReqViewDetailData.reqstatus == '1' && hasViewingPermissionResult.result == 'false'">
          <v-icon color="red">mdi-alert-box</v-icon>
          열람권한이 회수된 비밀문서가 존재합니다. 반려를 해주세요.
        </span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '4'">* 권한이 회수되었습니다.</span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '5'">* 요청서가 회수되었습니다.</span>
      </section>

      <!-- 요청내용 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>
        
        <v-table class="table-type-04">
          <thead>
            <tr>
              <th>관리번호</th>
              <th>제목</th>
              <th>담당자</th>
              <th v-if="printReq == true">요청파쇄예정일</th>
              <th v-if="printReq == true">요청인쇄부수</th>
              <th>승인파쇄예정일</th>
              <th>승인인쇄부수</th>
              <th v-if="printReq == false">인쇄일</th>
              <th v-if="printReq == false">인쇄부수</th>
              <th v-if="isDestructed == true">파쇄일</th>
              <th v-if="isDestructed == true">파쇄부수</th>
            </tr>
          </thead>
          <tbody>
            <template v-for="(listData, idx) in selectUseReqViewDetailViewList" :key="idx">
              <tr>
                <td>{{ listData.mgmtno }}</td>
                <td class="text-left">{{ listData.secttl }}</td>
                <td>{{ listData.authorname }}</td>
                <td v-if="printReq == true">{{ listData.reqdestructdt }}</td>
                <td v-if="printReq == true">{{ listData.reqprtcnt }}</td>
                
                <td v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
                  <v-text-field class="h-75 w-75 ma-1 mx-auto" v-model="listData.grantdestructdt" :min="dayjs(selectUseReqViewDetailData.reqdt).format('YYYY-MM-DD')" type="date" variant="outlined" hide-details="auto"></v-text-field>
                </td>
                <td v-else>{{ dayjs(listData.grantdestructdt).format('YYYY.MM.DD') }}</td>
                
                <td v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
                  <v-text-field class="h-75 w-75 ma-1 mx-auto" v-model="listData.grantprtcnt" :rules="[rules.prtCnt]" @update:modelValue="handlePrtdt(idx)" variant="outlined" hide-details="auto"></v-text-field>
                </td>
                <td v-else>{{ listData.grantprtcnt }}</td>

                <td v-if="printReq == false">{{ dayjs(listData.prtdt).format('YYYY.MM.DD') }}</td>
                <td v-if="printReq == false">{{ listData.prtcnt }}</td>
                <td v-if="isDestructed == true">{{ dayjs(listData.destructdt).format('YYYY.MM.DD') }}</td>
                <td v-if="isDestructed == true">{{ listData.destructcnt }}</td>

              </tr>
              <tr>
                <td colspan="8">
                  <ul class="attach-list ml-16">
                    <li v-for="(file, i) in listData.fileInfoList" :key="i">
                      <span class="branch-down"></span>
                      <v-chip  v-if="file.gubun == 'DFT01'" label color="#9C9AD9" class="density" text="기안문" variant="flat"></v-chip>
                      <v-chip  v-else-if="file.gubun == 'DFT02'" label color="#30A54A" class="density" text="붙임" variant="flat"></v-chip>
                      <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                      <span  v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' || selectUseReqViewDetailCondi.frompage == 'BmsUseproclist'" @click="toggleViewerPopup(file, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ file.sfilename }}</span>
                      <span v-else>{{ file.sfilename }}</span>
                    </li>
                  </ul>
                </td>
              </tr>
            </template>
          </tbody>
        </v-table> 
      </section>
    </div>
  </div>
  
  <!-- 승인/반려 팝업 -->
    <v-dialog v-model="bVisibleConfirmPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>
          <span v-if="selectUseReqViewDetailCondi.type == 'Y'">승인</span>
          <span v-else-if="selectUseReqViewDetailCondi.type == 'N'">반려</span>
        </v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleConfirmPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsUseConfirmPopup
        v-if="bVisibleConfirmPopup"
        :args="bmsUseConfirmPopupArgs"
        :toggleFunc="bmsUseConfirmPopupToggleFunc"
        :returnFunc="bmsUseConfirmPopupReturnFunc"
      ></BmsUseConfirmPopup>
    </v-card>
  </v-dialog>

  <!-- 인쇄/파기실행 팝업 -->
  <v-dialog v-model="bVisibleUsePrintPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>
          <span v-if="printReq == true">인쇄실행</span>
          <span v-else>파쇄실행</span>
        </v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUsePrintPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UsePrintPopup
        v-if="bVisibleUsePrintPopup"
        :args="bmsUsePrintPopupArgs"
        :toggleFunc="bmsUsePrintPopupToggleFunc"
        :returnFunc="bmsUsePrintPopupReturnFunc"
      ></UsePrintPopup>
    </v-card>
  </v-dialog>

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
  
  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, topMenu, breadcrumbs } = storeToRefs(mainStore)
  
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import BmsUseConfirmPopup from "./BmsUseConfirmPopup.vue";
import UsePrintPopup from "./UsePrintPopup.vue";
import { API } from '@/api'; 
import { useLoginStore } from '/src/store/Login';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { getValueByKey, getExtension, getFileIcon, viewDocument, url } from "@/utils/Utils.js";
import { storeToRefs } from 'pinia';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const isloading = ref(false)
const name = ref('BmsUseReqPrintDetail')
/* ======================== 승인/반려 팝업 ======================== */
const bVisibleConfirmPopup = ref(false)
const bmsUseConfirmPopupReuturn = ref({})
const bmsUseConfirmPopupArgs = ref({type:""})
const bmsUseConfirmPopupToggleFunc = ref((val) => { 
  if (selectUseReqViewDetailViewList.value.some(item => item.grantdestructdt === '')) {
    alert("승인파쇄예정일 설정을 완료해주세요")
    return
  }
  if (selectUseReqViewDetailViewList.value.some(item => item.grantprtcnt === '')) {
    alert("승인인쇄부수를 입력해주세요")
    return
  }
  bmsUseConfirmPopupArgs.value.type = val;
  bmsUseConfirmPopupArgs.value.title = selectUseReqViewDetailData.value.reqttl;
  selectUseReqViewDetailCondi.value.type = val;
  bVisibleConfirmPopup.value = !bVisibleConfirmPopup.value; 
});
const bmsUseConfirmPopupReturnFunc = ref((retValue) => {
  bmsUseConfirmPopupToggleFunc.value(retValue.type);
  bmsUseConfirmPopupReuturn.value = retValue;
  useReqSave();
});
/* ============================================================ */

/* ======================== 인쇄/파기실행 팝업 ======================== */
const printReq = ref(true);
const isDestructed = ref(false);
const bVisibleUsePrintPopup = ref(false)
const bmsUsePrintPopupArgs = ref({type:""})
const bmsUsePrintPopupToggleFunc = ref((val) => { 
  if(hasViewingPermissionResult.value.result == "false"){
    const revokedMsg = confirm("열람권한이 회수된 비밀문서가 존재합니다. \n요청서를 삭제하시겠습니까?");
    if(revokedMsg){
      deleteUseReqList();
    }
    return;
  }
  
  if(val){
    bmsUsePrintPopupArgs.value.isPrint = 'Y';
  }else{
    bmsUsePrintPopupArgs.value.isPrint = 'N';
  }
  bmsUsePrintPopupArgs.value.apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  bmsUsePrintPopupArgs.value.reqtype = selectUseReqViewDetailCondi.value.reqtype
  bmsUsePrintPopupArgs.value.reqid = selectUseReqViewDetailData.value.reqid;
  bVisibleUsePrintPopup.value = !bVisibleUsePrintPopup.value; 
});
const bmsUsePrintPopupReturnFunc = ref((retValue) => {
  bmsUsePrintPopupToggleFunc.value(retValue.type);
});
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const params = ref({});
const query = ref({});
const urlPaths =  ref('')
const today = ref("")

const updateUseReqViewAppPathApproveBIAData = ref({})
const updateUseReqViewAppStatusBIAData = ref({})
const selectUseReqViewDetailData = ref({})
const selectUseReqViewDetailDefCondi = {
  apprreqid: "",
  reqtype: "",
  reqstatusnm: "",
  reqstatus: "",
  type:"",
  frompage:""
}
const selectUseReqViewDetailCondi = ref({ ...selectUseReqViewDetailDefCondi })


onBeforeMount(() => {
  query.value = route.query;
  if (Array.isArray(route.query.apprreqid) && route.query.apprreqid.length > 0) {
      selectUseReqViewDetailCondi.value.apprreqid = route.query.apprreqid[0];
      selectUseReqViewDetailCondi.value.reqtype = route.query.reqtype[0];
      selectUseReqViewDetailCondi.value.reqstatusnm = route.query.reqstatusnm[0];
      selectUseReqViewDetailCondi.value.frompage = route.query.frompage[0];
      updateUseReqViewAppStatusBIAData.value.apprreqid = route.query.apprreqid[0];
    } else {
      selectUseReqViewDetailCondi.value.apprreqid = route.query.apprreqid;
      selectUseReqViewDetailCondi.value.reqtype = route.query.reqtype;
      selectUseReqViewDetailCondi.value.reqstatusnm = route.query.reqstatusnm;
      selectUseReqViewDetailCondi.value.frompage = route.query.frompage;
      updateUseReqViewAppStatusBIAData.value.apprreqid = route.query.apprreqid;
    }
})

onMounted(async () => {
  await selectUseReqViewDetail();
  await makeCondis();
  await hasViewingPermission();
  today.value = dayjs().format('YYYY-MM-DD');
})

const rules = {
  prtCnt: value => /^[1-9][0-9]{0,2}$/.test(value) || '3자리 이내의 정수만 입력하실 수 있습니다.'
};

const handlePrtdt = (idx) =>{
  if (!/^[1-9][0-9]{0,2}$/.test(selectUseReqViewDetailViewList.value[idx].grantprtcnt)) {
    selectUseReqViewDetailViewList.value[idx].grantprtcnt = '';
  }
};

// 결재선정보
const selectUseReqViewDetailPathList = ref([])
const staticColumnsSelectUseReqPathList = [
  { key: "apprtypenm", title: "구분", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprdeptname", title: "부서", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "positionnm", title: "직위/성명", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "appropinion", title: "의견", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprname", title: "서명", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprstatusnm", title: "처리결과", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];

// 요청내용
const selectUseReqViewDetailViewList = ref([])


// 조회
const selectUseReqViewDetail = async () => {
  selectUseReqViewDetailCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectUseReqViewDetail(selectUseReqViewDetailCondi.value, urlPaths.value);
    selectUseReqViewDetailData.value = response.data;
    selectUseReqViewDetailPathList.value = response.data.pathList;
    const mapListWithoutSlashes = response.data.mapList.map(item => {
      return {
        ...item,
        fileInfoList: JSON.parse(`[${item.fileInfoList.join(',')}]`).map(fileInfo => {
          return {
            ...fileInfo,
            gubun: fileInfo.gubun.replace(/\\/g, ''),
            flepath: fileInfo.flepath.replace(/\\/g, ''),
            sfilename: fileInfo.sfilename.replace(/\\/g, '')
          };
        }),
      };
    });

    selectUseReqViewDetailViewList.value = mapListWithoutSlashes.map(item => {
      return {
        ...item,
        fileInfoList: item.fileInfoList
          .filter(fileInfo => fileInfo.gubun === 'DFT01')
          .concat(
            item.fileInfoList
              .filter(fileInfo => fileInfo.gubun === 'DFT02')
              .sort((a, b) => a.fleorder - b.fleorder)
          )
      };
    });

    if(selectUseReqViewDetailData.value.destructreserve == ""){
      printReq.value = true;
    }else if(selectUseReqViewDetailData.value.destructreserve == "prtComplete"){
      printReq.value = false;
    }else if(selectUseReqViewDetailData.value.destructreserve == "dstComplete"){
      isDestructed.value = true;
    }

  } catch (error) {
    alert("Server Error");
    console.log(error);
  }
};

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

//승인,반려처리
const useReqSave = async (type) => {
  isloading.value = true;

  updateUseReqViewAppPathApproveBIAData.value.apprid = getUserLoginData.value.userid;
  updateUseReqViewAppPathApproveBIAData.value.updtname = getUserLoginData.value.username;
  updateUseReqViewAppPathApproveBIAData.value.grantflag = bmsUseConfirmPopupReuturn.value.type;
  updateUseReqViewAppPathApproveBIAData.value.reqtype = selectUseReqViewDetailCondi.value.reqtype;
  updateUseReqViewAppPathApproveBIAData.value.appropinion = bmsUseConfirmPopupReuturn.value.appropinion;
  updateUseReqViewAppPathApproveBIAData.value.apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  updateUseReqViewAppPathApproveBIAData.value.maplist = selectUseReqViewDetailViewList.value;
  updateUseReqViewAppPathApproveBIAData.value.reqid = selectUseReqViewDetailData.value.reqid;

  API.useAPI
    .updateUseReqViewAppPathApproveBIA(updateUseReqViewAppPathApproveBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("정상 갱신되었습니다");
        selectUseReqViewDetailCondi.value.frompage = 'BmsUseproclist';

        let arr = ['비밀관리', '비밀문서사용처리', '처리한 요청서']
        breadcrumbs.value.activeLink = arr;
        
        moveToBmsUsereqlist();
      }
    })
    .catch(error => {
      console.log(error);
      isloading.value = false;
      alert("Server Error");
    });    
};


//상태변경
const useReqStatusSave = async (status) => {
  let msg = "실행하시겠습니까?";
  if(selectUseReqViewDetailCondi.value.frompage == "BmsUsedraftlist"){
    msg = "요청서회수를 하시겠습니까?";
  }else if(selectUseReqViewDetailCondi.value.frompage == "BmsUseproclist"){
    msg = "권한회수를 하시겠습니까?";
  }else if(selectUseReqViewDetailCondi.value.frompage == "BmsUsetemplist"){
    msg = "승인요청을 하시겠습니까?";
  }
  
  const confirmStatus = confirm(msg);
  if (confirmStatus == false) {
    return;
  }
  isloading.value = true;
  updateUseReqViewAppStatusBIAData.value.reqstatus = status;
  updateUseReqViewAppStatusBIAData.value.reqtype = selectUseReqViewDetailCondi.value.reqtype;
  updateUseReqViewAppStatusBIAData.value.apprid = getUserLoginData.value.userid;
  updateUseReqViewAppStatusBIAData.value.apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  updateUseReqViewAppStatusBIAData.value.maplist = selectUseReqViewDetailViewList.value;
  updateUseReqViewAppStatusBIAData.value.reqid = selectUseReqViewDetailData.value.reqid;

  API.useAPI
    .updateUseReqViewAppStatusBIA(updateUseReqViewAppStatusBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("정상 갱신되었습니다");
        moveToBmsUsereqlist();
      }
    })
    .catch(error => {
      console.log(error);
      isloading.value = false;
      alert("Server Error");
    });
};

const hasViewingPermissionCondi = ref({
  userid: "",
  docids: [],
});
const hasViewingPermissionResult = ref([]);
const makeCondis = async() => {
  const docids = selectUseReqViewDetailViewList.value
    .map(item => item.docid);
  hasViewingPermissionCondi.value.docids = docids;
  hasViewingPermissionCondi.value.userid = selectUseReqViewDetailData.value.reqid;
};
const hasViewingPermission = async() => {
  try {
    const response = await API.dctAPI.hasViewingPermission(hasViewingPermissionCondi.value, urlPaths.value);
    hasViewingPermissionResult.value = response.data;
  } catch (error) {
    alert("Server Error");
    console.log(error);
  }
};
const deleteUseReqListCondi = ref([{ apprreqid: "", reqtype: "" }]);
const deleteUseReqList = async() => {

  deleteUseReqListCondi.value[0].apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  deleteUseReqListCondi.value[0].reqtype = selectUseReqViewDetailCondi.value.reqtype;

  await API.useAPI
    .deleteUseReqList(deleteUseReqListCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("정상 삭제되었습니다");
        moveToBmsUsereqlist();
      }
    })
    .catch(error => {
      console.log(error)
      alert("Server Error");
    });
};

// 요청서 회수 가능한 상태인지 체크
const isPossibleRevoke = () => {
  return !selectUseReqViewDetailPathList.value.some(item=>item.apprstatus == 'APP20')
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

// Move Function
const moveToBmsUsereqlist = (page) => {
  router.push({
    name: selectUseReqViewDetailCondi.value.frompage
    });
};
  </script>

<style scoped>
.density {
  padding: 0px 11px;
  height: 21px;
}
</style>