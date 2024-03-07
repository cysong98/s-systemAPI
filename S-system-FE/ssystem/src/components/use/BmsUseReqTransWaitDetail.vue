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
          <h5 class="h5">이관대기요청서</h5>
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
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailCondi.reqstatusnm == '승인' || selectUseReqViewDetailCondi.reqstatusnm == '1인결재'">
          <v-icon color="red">mdi-alert-box</v-icon>
          최종 결재승인이 되면 부서의 처리과 비밀관리자가 [비밀관리 > 예고문도래목록] 메뉴에서 내부이관 요청서를 작성하도록 합니다.
        </span>
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
          class="table-type-04"
          >
          <template v-slot:item.isopen="{ item }">
            <v-select v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist'"
              v-model="item.raw.isopen"
              :items="openData"
              @update:modelValue="updateIsOpen(item)"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            />
            <div class="box-flex justify-center" v-else>
              {{ item.raw.isopen }}
            </div>
          </template>
          <template v-slot:item.openrange="{ item }">
            <v-select 
              v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' && (item.raw.isopen == 'CLOSE' || item.raw.isopen == '비공개')"
              placeholder="비공개"
              :readonly="true"
              variant="outlined"
              hide-details="auto"
            />
            <v-select v-else-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' && !(item.raw.isopen == 'CLOSE' || item.raw.isopen == '비공개')"
              v-model="item.raw.openrange"
              :items="agencyData"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            />
            <div class="box-flex justify-center" v-else>
              {{ item.raw.openrange }}
            </div>
          </template>
          <template v-slot:item.openrestrict="{ item }">
            <v-select 
              v-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' && (item.raw.isopen == 'CLOSE' || item.raw.isopen == '비공개')"
              placeholder="비공개"
              :readonly="true"
              variant="outlined"
              hide-details="auto"
            />
            <v-select  v-else-if="selectUseReqViewDetailCondi.frompage == 'BmsUsereqlist' && !(item.raw.isopen == 'CLOSE' || item.raw.isopen == '비공개')"
              v-model="item.raw.openrestrict"
              :items="restrictData"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            />
            <div class="box-flex justify-center" v-else>
              {{ item.raw.openrestrict }}
            </div>
          </template>
          <template v-slot:item.secttl="{ item }">
            <div :class="{ 'text-left': item }">
              {{ item.raw.secttl }}
            </div>
          </template>
          <template v-slot:item.regirecvtype="{ item }">
            <span v-if="item.raw.regirecvtype == '1'">전자</span>
            <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
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
  import { storeToRefs } from 'pinia';
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = storeToRefs(loginStore)
  
  const name = ref('BmsUseReqTransWaitDetail')
/* ======================== 승인/반려 팝업 ======================== */
const bVisibleConfirmPopup = ref(false)
const bmsUseConfirmPopupReuturn = ref({})
const bmsUseConfirmPopupArgs = ref({type:""})
const bmsUseConfirmPopupToggleFunc = ref((val) => { 
  if(val == 'Y' && selectUseReqViewDetailViewList.value.some(item=> (item.isopen == 'OPEN' || item.isopen == 'PART') && (item.openrange == "" || item.openrestrict == ""))){
    alert("공개 설정을 완료해주세요")
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
})

// 공개여부
const openData = [
  {view: "비공개", key: "CLOSE"},
  {view: "공개", key: "OPEN"},
  {view: "부분공개", key: "PRAT"},
];

// 열람범위
const agencyData = [
  {view: "선택", key: ""},
  {view: "기관", key: "DDEP1"},
  {view: "실국", key: "DDEP2"},
  {view: "부서", key: "DDEP3"},
];

// 공개제한
const restrictData = [
  {view: "선택", key:""},
  {view: "1호", key:"1"},
  {view: "2호", key:"2"},
  {view: "3호", key:"3"},
  {view: "4호", key:"4"},
  {view: "5호", key:"5"},
  {view: "6호", key:"6"},
  {view: "7호", key:"7"},
  {view: "8호", key:"8"},
];

const updateIsOpen = (item) => {
  if(item.raw.isopen == 'CLOSE'){
    item.raw.openrange = '';
    item.raw.openrestrict = '';
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
const staticColumnsSelectUseReqViewList = [
  { key: "mgmtno", title: "관리번호", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "noti", title: "보존&보호기간", width: "150px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "isopen", title: "공개여부", width: "150px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "openrange", title: "열람범위", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "openrestrict", title: "공개제한", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];

// 조회
const selectUseReqViewDetail = async () => {
  selectUseReqViewDetailCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectUseReqViewDetail(selectUseReqViewDetailCondi.value, urlPaths.value);
    response.data.mapList = response.data.mapList.map((u) => {
      u = { ...u, noti: u.notiprsrvtermnm +" & "+ u.notiprottermnm };
      return { ...u};
    });
    selectUseReqViewDetailData.value = response.data;
    selectUseReqViewDetailPathList.value = response.data.pathList;
    selectUseReqViewDetailViewList.value = response.data.mapList;
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
  
  