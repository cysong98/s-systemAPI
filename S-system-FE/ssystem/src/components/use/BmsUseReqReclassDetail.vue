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
        <h5 class="h5">재분류 - 예고문변경 요청서</h5>
        <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
          <!-- 처리할요청서 -->
          <v-btn class="button" @click="bmsUseConfirmPopupToggleFunc('Y')">승인</v-btn>
          <v-btn class="button" @click="bmsUseConfirmPopupToggleFunc('N')">반려</v-btn>
          <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
        </div>
        <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsedraftlist'">
          <!-- 기안한요청서 -->
          <v-btn class="button" v-if="selectUseReqViewDetailData.reqstatus == '1' && isPossibleRevoke()" @click="useReqStatusSave('5')">요청서회수</v-btn>
          <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
        </div>  
        <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUseproclist'">
          <!-- 처리한요청서 -->
          <v-btn class="button" v-if="selectUseReqViewDetailCondi.reqstatusnm === '승인' || selectUseReqViewDetailCondi.reqstatusnm === '실행완료'" @click="useReqStatusSave('4')">권한회수</v-btn>
          <v-btn class="button" @click="moveToBmsUsereqlist()">목록</v-btn>
        </div>    
        <div class="action-buttons" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist'">
          <!-- 요청서완료처리 -->
          <v-btn class="button" @click="useReqStatusSave('3')">재분류실행</v-btn>
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

        <v-data-table
          :headers="staticColumnsSelectUseReqPathList"
          :items="selectUseReqViewDetailPathList"
          item-value="apprreqid"
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
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '4'">* 권한이 회수되었습니다.</span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '5'">* 요청서가 회수되었습니다.</span>
      </section>

    <!-- 요청내용 -->
    <section class="section">
      <div class="box-title">
        <h6 class="h6">요청내용</h6>
      </div>
      
      <v-data-table
        :headers="staticColumnsSelectUseReqViewList"
        :items="selectUseReqViewDetailViewList"
        item-value="docid"
        :expanded="expandedItems"
        class="table-type-04"
        >
        <template v-slot:item.secttl="{ item }">
          <div :class="{ 'text-left': item }">
            {{ item.raw.secttl }}
          </div>
        </template>
        <template v-slot:item.regirecvtype="{ item }">
          <span v-if="item.raw.regirecvtype == '1'">전자</span>
          <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
        </template>
        <template v-slot:expanded-row="{ item }" v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'">
          <tr>
            <td colspan="99">
              <div class="box-flex">
                <v-label>비밀등급</v-label>
                <v-select
                  v-model="item.raw.grantseclevel"
                  :items="secLevelData"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>보존기간</v-label>
                <v-select
                  v-model="item.raw.grantnotiprsrvterm"
                  :items="prsRvTermData"
                  @update:modelValue="updatePrsrvterm(item)"
                  item-title="key"
                  item-value="value"
                  required
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>보호기간</v-label>
                <v-select
                  v-model="item.raw.protdtSelected"
                  :items="protdtData"
                  variant="outlined"
                  hide-details="auto"></v-select>
                <v-text-field
                  v-if="item.raw.protdtSelected == '직접입력'"
                  v-model="item.raw.userDate"
                  @update:modelValue="updateUserDate(item)"
                  type="date"
                  variant="outlined"
                  hide-details="auto">
                </v-text-field>
                <template v-else>
                <v-select
                  v-model="item.raw.protdtPeriodSelected"
                  :items="protdtPeriodData"
                  @update:modelValue="updateDate(item)"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto">
                </v-select>
                <v-label>년</v-label>
                <v-text-field   
                  v-model="item.raw.year"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>년</v-label>
                <v-text-field
                  v-model="item.raw.month"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>월</v-label>
                <v-text-field
                  v-model="item.raw.day"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>일</v-label>
              </template>
                <v-label>(으)로</v-label>
                <v-select
                  v-model="item.raw.grantnotiact"
                  :items="reclassData(item.raw.copyno)"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto">
                </v-select>
              </div>
            </td>
          </tr>
        </template>
        <template v-slot:expanded-row="{ item }" v-else-if="selectUseReqViewDetailCondi.frompage == 'BmsUseproclist' ||
         selectUseReqViewDetailCondi.frompage == 'BmsUsedraftlist' && selectUseReqViewDetailCondi.reqstatusnm !== '결재중' || selectUseReqViewDetailCondi.frompage == 'BmsUsecompletelist'">
          <tr>
            <td colspan="99">
              <div class="box-flex">
                <v-label>승인 비밀등급</v-label>
                <v-select
                  v-model="item.raw.grantseclevel"
                  :items="secLevelData"
                  :readonly="true"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>승인 보존기간</v-label>
                <v-select
                  v-model="item.raw.grantnotiprsrvterm"
                  :items="prsRvTermData"
                  :readonly="true"
                  item-title="key"
                  item-value="value"
                  required
                  variant="outlined"
                  hide-details="auto"/>
                <v-label>승인 보호기간</v-label>
                <v-text-field   
                  v-model="item.raw.year"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>년</v-label>
                <v-text-field
                  v-model="item.raw.month"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>월</v-label>
                <v-text-field
                  v-model="item.raw.day"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
                <v-label>일</v-label>
                <v-label>(으)로</v-label>
                <v-select
                  v-model="item.raw.grantnotiact"
                  :items="reclassData(item.raw.copyno)"
                  :readonly="true"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto">
                </v-select>
              </div>
            </td>
          </tr>
        </template>
        <template v-slot:bottom></template>
      </v-data-table>   
    </section>
  </div>
</div>

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
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import BmsUseConfirmPopup from "./BmsUseConfirmPopup.vue";
import { API } from '@/api'; 
import { useLoginStore } from '/src/store/Login';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";

import { storeToRefs } from 'pinia';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('BmsUseReqReclassDetail')
/* ======================== 승인/반려 팝업 ======================== */
const bVisibleConfirmPopup = ref(false)
const bmsUseConfirmPopupReuturn = ref({})
const bmsUseConfirmPopupArgs = ref({type:""})
const bmsUseConfirmPopupToggleFunc = ref((val) => { 
  if (selectUseReqViewDetailViewList.value.some(item => item.grantnotiprsrvterm === '' || item.grantnotiprotterm === '')) {
    alert("승인 예고문 설정을 완료해주세요")
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

const route = useRoute()
const router = useRouter()
const params = ref({});
const query = ref({});
const urlPaths =  ref('')
const today = ref("")
const isloading = ref(false)

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
const expandedItems = ref([]);

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
  today.value = dayjs().format('YYYY-MM-DD');
})


const secLevelData = [
  { key: 'II급', value: '2'},
  { key: 'III급', value: '3'},
  { key: '일반', value: '5'},
];
const prsRvTermData = [
  {key: '1년', value: '001'},
  {key: '3년', value: '003'},
  {key: '5년', value: '005'},
  {key: '10년', value: '010'},
  {key: '30년', value: '030'},
  {key: '준영구', value: '040'},
  {key: '영구', value: '050'},
];
const protdtData = ["날짜지정", "직접입력"];
const protdtPeriodData = [
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
const orgnReclassData = [
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'},
];
const copyReclassData = [
  { key: '재분류', value: '21'},
  { key: '존안', value: '20'},
  { key: '파기', value: '23'},
];

const reclassData = computed(() => (copyno) => {
  if (copyno.includes('원')) {
    return orgnReclassData;
  } else {
    return copyReclassData;
  }
});

const updatePrsrvterm = (item) => { // 보존기간
  if(parseInt(item.raw.grantnotiprsrvterm) < parseInt(item.raw.protdtPeriodSelected)+1){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.grantnotiprsrvterm = "";
    return
  }
  if(parseInt(item.raw.grantnotiprsrvterm) < parseInt(item.raw.userDate.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.grantnotiprsrvterm = "";
    return
  }
};

const updateDate = (item) => { // 날짜지정
  if(parseInt(item.raw.grantnotiprsrvterm) < parseInt(item.raw.protdtPeriodSelected)+1){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.protdtPeriodSelected = "";
    item.raw.year = "";
    item.raw.month = "";
    item.raw.day = "";
    item.raw.grantnotiprotterm = "";
    return
  }
  const currentDate = new Date();
  const currentYear = currentDate.getFullYear();
  item.raw.year = currentYear + parseInt(item.raw.protdtPeriodSelected);
  item.raw.month = '12';
  item.raw.day = '31';
  item.raw.grantnotiprotterm = item.raw.year+item.raw.month+item.raw.day;
};

const updateUserDate = (item) => { // 직접입력
  if(parseInt(item.raw.grantnotiprsrvterm) < parseInt(item.raw.userDate.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 보호기간보다 작을 수 없습니다.');
    item.raw.userDate = "";
    item.raw.grantnotiprotterm = "";
    return
  }
  item.raw.grantnotiprotterm = item.raw.userDate.replace(/-/g, '');
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
const staticColumnsSelectUseReqViewList = [
  { key: "mgmtno", title: "관리번호", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "250px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "80px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "80px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "orgnattrinfo", title: "원본예고문", width: "200px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqattrinfo", title: "요청예고문", width: "200px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "80px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];

// 조회
const selectUseReqViewDetail = async () => {
  selectUseReqViewDetailCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectUseReqViewDetail(selectUseReqViewDetailCondi.value, urlPaths.value);
    selectUseReqViewDetailData.value = response.data;
    selectUseReqViewDetailPathList.value = response.data.pathList;
    selectUseReqViewDetailViewList.value = response.data.mapList;

    selectUseReqViewDetailViewList.value = response.data.mapList.map(item => ({
      ...item, 
      protdtPeriodSelected: (item.grantnotiprotterm.slice(0, 4) - new Date().getFullYear()).toString(),
      year: item.grantnotiprotterm.slice(0, 4),
      month: item.grantnotiprotterm.slice(4, 6),
      day: item.grantnotiprotterm.slice(6, 8),
      protdtSelected: '날짜지정',
      userDate: '',
    }));

    expandedItems.value = selectUseReqViewDetailViewList.value.map(item => item.docid);

  } catch (error) {
    alert("Server Error");
    console.log(error);
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
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });    
};


//상태변경
const useReqStatusSave = async (status) => {

  if(status == '4' && selectUseReqViewDetailCondi.value.reqstatusnm === '실행완료'){
    alert("이미 재분류되어 권한회수를 하실 수 없습니다.");
    return;
  }

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

  if(status == '4' || status == '5'){ // 권한회수, 요청서회수
    updateUseReqViewAppStatusBIAData.value.savetype = 'MGT00'; //초기화
    updateUseReqViewAppStatusBIAData.value.list = selectUseReqViewDetailViewList.value;
  }else if(status == '3'){ // 실행
    updateUseReqViewAppStatusBIAData.value.savetype = 'reclasscomplete'
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
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });
};

// 요청서 회수 가능한 상태인지 체크
const isPossibleRevoke = () => {
  return !selectUseReqViewDetailPathList.value.some(item=>item.apprstatus == 'APP20')
};

// Move Function
const moveToBmsUsereqlist = (page) => {
  router.push({
    name: selectUseReqViewDetailCondi.value.frompage
    });
};

</script>