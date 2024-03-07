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
          <h5 class="h5">반출요청서</h5>
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
            <v-btn class="button" :disabled="hasViewingPermissionResult.result == 'false'" v-if="selectUseReqViewDetailData.lendcharge == getUserLoginData.userid"  @click="bmsUsePrintPopupToggleFunc()">반출실행</v-btn>
            <v-btn class="button" v-if="hasViewingPermissionResult.result == 'false' && selectUseReqViewDetailData.lendcharge == getUserLoginData.userid" @click="useReqStatusSave('4')">권한회수</v-btn>
            <v-btn class="button" v-if="selectUseReqViewDetailData.lendcharge != getUserLoginData.userid && selectUseReqViewDetailData.lendstatus == '2'" @click="moveToBmsUsereqlendbackdetail()" >반납요청</v-btn>
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
            <col width="120">
            <col width="">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th>반출구분</th>
              <td>
                <v-text-field
                  v-model="selectUseReqViewDetailData.lendgubun"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
              <th>반출형태</th>
              <td>
                <v-text-field
                  v-model="selectUseReqViewDetailData.lendtype"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>            
            </tr>          
            <tr>
              <th>제목</th>
              <td colspan="3">
                <v-text-field
                  v-model="selectUseReqViewDetailData.reqttl"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>요청반출기간</th>
              <td>
                <div class="box-flex">
                  <v-text-field
                    v-model="selectUseReqViewDetailData.reqlenddt"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  ~
                  <v-text-field
                    v-model="selectUseReqViewDetailData.reqbackdt"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </div>
              </td>
              <th>승인반출기간</th>
              <td>
                <div class="box-flex" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
                  <v-text-field
                    v-model="selectUseReqViewDetailData.grantlenddt"
                    type="date"
                    :min=selectUseReqViewDetailData.reqlenddt
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  ~
                  <v-text-field
                    v-model="selectUseReqViewDetailData.grantbackdt"
                    @update:modelValue="updateEndDt"
                    type="date"
                    :min=selectUseReqViewDetailData.grantlenddt
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </div>
                <div class="box-flex" v-else>
                  <v-text-field
                    v-model="selectUseReqViewDetailData.grantlenddt"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  ~
                  <v-text-field
                    v-model="selectUseReqViewDetailData.grantbackdt"
                    :readonly="true"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </div>
              </td>            
            </tr>  
            <tr>
              <th>요청자</th>
              <td colspan="3">
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
              <td colspan="3">
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
      </section>

        <!-- 파일반출담당자정보 -->
        <section class="section">
        <div class="box-title">
          <h6 class="h6">파일반출 담당자정보</h6>
        </div>

        <v-data-table
          :headers="staticColumnsSelectUseReqPathList"
          :items="fileManagerInfo"
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
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist' && hasViewingPermissionResult.result == 'false'">
          <v-icon color="red">mdi-alert-box</v-icon>
          열람권한이 회수된 비밀문서가 존재하여 반출이 불가능합니다.
        </span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '4'">* 권한이 회수되었습니다.</span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '5'">* 요청서가 회수되었습니다.</span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist' && hasViewingPermissionResult.result == 'true' && selectUseReqViewDetailData.lendcharge != getUserLoginData.userid">{{ selectUseReqViewDetailData.lendstatus == '1' ? '* 담당자의 반출 실행 대기중입니다.' : '* 반출 완료되었습니다. 반납요청을 해주세요' }}</span>
      </section>

      <!-- 요청내용 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>

        <v-table class="table-type-04">
          <colgroup>
            <col width="200">
            <col width="">
            <col width="">
            <col width="">
            <col width="">
            <col width="">
          </colgroup>
          <thead>
            <tr>
              <th>관리번호</th>
              <th>제목</th>
              <th>담당자</th>
              <th>직위</th>
              <th>부서</th>
              <th>열람만료일</th>
              <th>구분</th>
            </tr>
          </thead>
          <tbody>
            <template v-for="(listData, idx) in selectUseReqViewDetailViewList" :key="idx">
              <tr>
                <td>{{ listData.mgmtno }}</td>
                <td class="text-left">{{ listData.secttl }}</td>
                <td>{{ listData.authorname }}</td>
                <td>{{ listData.authordutyname }}</td>
                <td>{{ listData.deptname }}</td>
                <td>{{ listData.readauthexpiredt }}</td>
                <td>{{ listData.regirecvtype == "1" ? "전자" : "비전자" }}</td>
              </tr>
              <tr>
                <td colspan="6">
                  <ul class="attach-list ml-16" v-if="listData.fileInfoList">
                    <li v-for="(file, i) in listData.fileInfoList" :key="i">
                      <span class="branch-down"></span>
                      <v-chip  v-if="file.gubun == 'DFT01'" label color="#9C9AD9" class="density" text="기안문" variant="flat"></v-chip>
                      <v-chip  v-else-if="file.gubun == 'DFT02'" label color="#30A54A" class="density" text="붙임" variant="flat"></v-chip>
                      <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                      <span v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' || selectUseReqViewDetailCondi.frompage == 'BmsUseproclist' || selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist' && selectUseReqViewDetailData.lendcharge == getUserLoginData.userid" color="white" @click="toggleViewerPopup(file, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ file.sfilename }}</span>
                      <span v-else>{{ file.sfilename }}</span>
                    </li>
                  </ul>
                  <ul class="attach-list ml-16" v-else>
                    <li>
                      <span class="branch-down"></span>
                      <span class="spacing">보관함: </span>{{listData.locker}} <span class="spacing">, 단번호: </span>{{listData.unitnum}}
                    </li>
                    <li>
                      <span class="branch-down"></span>
                      <span class="spacing">자료유형: </span>{{listData.doctype}} <span class="spacing">, 매체유형: </span>{{listData.mediatype}}
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
        <v-btn class="close" @click="bVisibleConfirmPopup = false">
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
  
  <!-- 인쇄반출실행 팝업 -->
  <v-dialog v-model="bVisibleUsePrintPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>
          <span>인쇄반출실행</span>
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
  import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
  import { getValueByKey, getExtension, getFileIcon, viewDocument, url } from "@/utils/Utils.js";
  import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
  import _ from 'lodash';
  import BmsUseConfirmPopup from "./BmsUseConfirmPopup.vue";
  import UsePrintPopup from "./UsePrintPopup.vue";
  import { API } from '@/api'; 
  import { useLoginStore } from '/src/store/Login';
  import { storeToRefs } from 'pinia';
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = storeToRefs(loginStore)
  
  const name = ref('BmsUseReqLendDetail')
/* ======================== 승인/반려 팝업 ======================== */
const bVisibleConfirmPopup = ref(false)
const bmsUseConfirmPopupReuturn = ref({})
const bmsUseConfirmPopupArgs = ref({type:""})
const bmsUseConfirmPopupToggleFunc = ref((val) => { 
  if (selectUseReqViewDetailData.value.grantlenddt === '' || selectUseReqViewDetailData.value.grantbackdt === '' ||
    selectUseReqViewDetailData.value.grantlenddt > selectUseReqViewDetailData.value.grantbackdt) {
    alert("승인반출기간 설정을 완료해주세요")
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

/* ======================== 인쇄반출실행 팝업 ======================== */
const bVisibleUsePrintPopup = ref(false)
const bmsUsePrintPopupArgs = ref({type:""})
const bmsUsePrintPopupToggleFunc = ref((val) => { 
  if(selectUseReqViewDetailViewList.value[0].regirecvtype == '1'){
    bmsUsePrintPopupArgs.value.isLend = 'Y';
    bmsUsePrintPopupArgs.value.data = selectUseReqViewDetailViewList.value;
    bmsUsePrintPopupArgs.value.apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
    bmsUsePrintPopupArgs.value.reqtype = selectUseReqViewDetailCondi.value.reqtype;
    bmsUsePrintPopupArgs.value.reqid = selectUseReqViewDetailData.value.reqid;
    bVisibleUsePrintPopup.value = !bVisibleUsePrintPopup.value; 
  }else{
    useReqStatusSave('7');
  }
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
const isloading = ref(false)

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

const minReadAuthExpiredt = ref('');

const updateEndDt = () => {
  if(minReadAuthExpiredt.value < selectUseReqViewDetailData.value.grantbackdt){
    alert(`반출만료일은 열람만료일(${minReadAuthExpiredt.value}) 보다 클 수 없습니다.`);
    selectUseReqViewDetailData.value.grantbackdt = dayjs(selectUseReqViewDetailData.value.reqbackdt).format('YYYY-MM-DD');
  }
};

// 결재선정보
const selectUseReqViewDetailPathList = ref([])
const fileManagerInfo = ref([])
const staticColumnsSelectUseReqPathList = [
  { key: "apprtypenm", title: "구분", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprdeptname", title: "부서", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "positionnm", title: "직위/성명", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "appropinion", title: "의견", width:"100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprname", title: "서명", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprstatusnm", title: "처리결과", width:"200px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];

// 요청내용
const selectUseReqViewDetailViewList = ref([])

// 조회
const selectUseReqViewDetail = async () => {
  selectUseReqViewDetailCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectUseReqViewDetail(selectUseReqViewDetailCondi.value, urlPaths.value);
    selectUseReqViewDetailData.value = response.data;

    selectUseReqViewDetailPathList.value = response.data.pathList.slice(0, -1);
    fileManagerInfo.value = response.data.pathList.slice(-1);

    const mapListWithoutSlashes = response.data.mapList.map(item => {
      if(item.fileInfoList){
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
      }else{
        return {...item}
      }
    });

    selectUseReqViewDetailViewList.value = mapListWithoutSlashes.map(item => {
      if (item.fileInfoList) {
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
      } else {
        return item;
      }
    });


    let minDate = dayjs(selectUseReqViewDetailViewList.value[0].readauthexpiredt);
    selectUseReqViewDetailViewList.value.forEach(item => {
      const expiredt = dayjs(item.readauthexpiredt);
      if (expiredt.isBefore(minDate)) {
        minDate = expiredt;
      }
    });
    minReadAuthExpiredt.value = minDate.format('YYYY-MM-DD');
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
  
  updateUseReqViewAppPathApproveBIAData.value.grantlenddt = selectUseReqViewDetailData.value.grantlenddt;
  updateUseReqViewAppPathApproveBIAData.value.grantbackdt = selectUseReqViewDetailData.value.grantbackdt;
  
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
  }else if(status == '4' || selectUseReqViewDetailCondi.value.frompage == "BmsUseproclist"){
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

  if(status == '7'){ // 반출실행
    updateUseReqViewAppStatusBIAData.value.savetype = 'lendcomplete';
  }else if(status == '9'){ // 반납실행
    updateUseReqViewAppStatusBIAData.value.savetype = 'lendbackcomplete';
  }

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
  isloading.value = true;
  deleteUseReqListCondi.value[0].apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  deleteUseReqListCondi.value[0].reqtype = selectUseReqViewDetailCondi.value.reqtype;

  await API.useAPI
    .deleteUseReqList(deleteUseReqListCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("정상 삭제되었습니다");
        moveToBmsUsereqlist();
      }
    })
    .catch(error => {
      isloading.value = false;
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

// 반납요청서
const moveToBmsUsereqlendbackdetail = () => {
  // if(!confirm("반납요청서를 작성하시겠습니까?")){
  //   return;
  // }

  const param = {apprreqid: "",reqtype:""};
  param.apprreqid = selectUseReqViewDetailCondi.value.apprreqid;
  param.reqtype = selectUseReqViewDetailCondi.value.reqtype;

  router.push({
    name: 'BmsUsereqlendbackdetail',
    params: {
        ...param
      },
      query: {
        ...param
      }
    });
};

</script>

<style scoped>
.density {
  padding: 0px 11px;
  height: 21px;
}
</style>