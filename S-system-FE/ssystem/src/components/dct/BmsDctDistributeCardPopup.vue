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
  <div class="modal-body">
    <div class="content" v-if="selectDistributeListList.length > 0 && selectsFindManagerData.length > 0">
      <div class="row">
        <div class="col">
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
              <tr >
                <th>비밀등급</th>
                <td>{{transformSeclevel(selectDistributeListList[0].seclevel)}}</td>
                <th>관리번호</th>
                <td>{{selectDistributeListList[0].mgmtno}}</td>
                <th>사본번호</th>
                <td>{{selectDistributeListList[0].copyno}}</td>
              </tr>
              <tr>
                <th>문서 제목</th>
                <td colspan="5">{{selectDistributeListList[0].docttl}}
                  <!--테스트 끌나면 지워야함!!!-->
                  <v-btn v-if="isloading == false && selectDistributeListList[0] && transformState(displayState(selectDistributeListList[0])) == '발송실패'"
                    color="white" style="border: none;" @click="toggleexstatusText()" class="test-button">(테스트스위치)송수신 상태 : {{ exstatusText }}</v-btn>
                  <v-btn v-if="isloading == false && selectDistributeListList[0] && transformState(displayState(selectDistributeListList[0])) == '반송종료'"
                    color="white" style="border: none;" @click="toggleexstatusText()" class="test-button">(테스트스위치)송수신 상태 : {{ exstatusText }}</v-btn>
                  <!--테스트 끌나면 지워야함!!!-->
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-table class="table-type-04 narrow">
            <thead>
              <tr>
                <th rowspan="2"></th>
                <th rowspan="2">구분</th>
                <th rowspan="2">시행일자</th>
                <th rowspan="2">사본번호</th>
                <th rowspan="2">발신기관명</th>
                <th colspan="3">수신자</th>
                <th colspan="3">배포자</th>
                <th rowspan="2">상태</th>
              </tr>
              <tr>
                <th>수신처</th>
                <th>성명</th>
                <th>서명</th>
                <th>소속</th>
                <th>성명</th>
                <th>서명</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in selectDistributeListListF" :key="index">
                <td></td>
                <td>{{ transformRdocGubun(item.gubun) }}</td>
                <td>{{ transformDate(item.reportdt) }}</td>
                <td>{{ item.copyno }}</td>
                <td>{{ item.authordeptname }}</td>
                <td v-if="isOridoc !== 'oriDoc'">{{ props.args.recvdeptname }}</td>
                <td v-else>{{ tempRecvDeptNameList[index] }}</td>
            
                <td v-if="isOridoc !== 'oriDoc' && props.args"></td>
                <td v-if="isOridoc === 'oriDoc' && selectsFindManagerData[index] && selectsFindManagerData[index].length > 0">{{ selectsFindManagerData[index][0].username }}</td>

                <td v-if="getDctRdocData && getDctRdocData.copyparams && getDctRdocData.copyparams[0].docstatus == 'EDS05' && isOridoc !== 'oriDoc'" class="signature">{{ selectsFindManagerData[0][0].username }}</td>
                <td v-if="getDctRdocData && getDctRdocData.copyparams && getDctRdocData.copyparams[0].docstatus !== 'EDS05' && isOridoc !== 'oriDoc'" class="signature"></td>
                <td v-if="isOridoc === 'oriDoc' && selectsFindManagerData[index] && selectsFindManagerData[index].length > 0 && selectDistributeListList && selectDistributeListList[index+1].docstatus === 'EDS05'" class="signature">{{ selectsFindManagerData[index][0].username }}</td>
                <td v-if="isOridoc === 'oriDoc' && selectsFindManagerData[index] && selectsFindManagerData[index].length > 0 && selectDistributeListList && selectDistributeListList[index+1].docstatus !== 'EDS05'" class="signature"></td>
                
                <td>{{ item.authordeptname }}</td>
                <td>{{ item.authorname }}</td>
                <td v-if="props.args && props.args.fromPage && props.args.fromPage === 'BmsDctSendCurrentList'" class="signature">{{ item.authorname }}</td>
                <td v-else class="signature">{{ item.authorname }}</td>
                <td v-if="selectRelayHisData[0] && selectRelayHisData[0].retry">{{ transformState(displayState(item)) }}{{'('+ selectRelayHisData[0].retry + ')'}}</td>
                <td v-else>{{ transformState(displayState(item)) }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn v-if="stateValue =='DRDS8'" variant="flat" color="grey-lighten-3" rounded="xl" @click="confirmPopUp">닫기</v-btn>
      <v-btn v-else variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if="isloading == false && selectDistributeListList[0] && transformState(displayState(selectDistributeListList[0])) == '발송실패' && stateValue !='DRDS8' && allNotPrinted == 'Y'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA()">재발송</v-btn>
      <v-btn v-if="isloading == false && selectRelayHisData[0] && selectRelayHisData[0].retry === 3
        && selectDistributeListList[0] && transformState(displayState(selectDistributeListList[0])) == '발송실패' && stateValue !='DRDS8'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="bmsUsePrintPopupToggleFunc()">오프라인인쇄</v-btn>
      <v-btn v-if="isloading == false && selectDistributeListList[0] && selectDistributeListList[0].state =='DRDS8' && !selectDistributeListList[0].docstatus"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="bmsDctDetailPopupToggleFunc(props, 1)">송증인쇄</v-btn><!--오프라인발송완료시-->
      <v-btn v-if="isloading == false && selectDistributeListList[0] && selectDistributeListList[0].state =='DRDS8' && !selectDistributeListList[0].docstatus"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="bmsDctDetailPopupToggleFunc(props, 2)">접수증입력</v-btn><!--오프라인발송완료시-->
      <v-btn v-if="isloading == false && selectRelayHisData[0] && selectRelayHisData[0].retry === 3
        && selectDistributeListList[0] && transformState(displayState(selectDistributeListList[0])) == '발송실패' && stateValue !='DRDS8' && allNotPrinted == 'Y'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="notSend()">발송안함</v-btn>

      <!-- 반송함에서만 열리는 비밀배포카드 버튼 -->
        <v-btn v-if="isloading == false && selectDistributeListList[0] && selectDistributeListList[0].docstatus ==='EDS40' && ['반송종료'].includes(transformState(displayState(selectDistributeListList[0])) )"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA()">재발송</v-btn>
        <v-btn v-if="isloading == false && selectDistributeListList[0] && ['반송종료'].includes(transformState(displayState(selectDistributeListList[0])))"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="notSend()">발송안함</v-btn>
    </div>

  </div>

  <!-- 오프라인인쇄 실행 팝업 -->
  <v-dialog v-model="bVisibleUsePrintPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>
          <span>오프라인인쇄 실행</span>
        </v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUsePrintPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctPrintSendPopup
        v-if="bVisibleUsePrintPopup"
        :args="bmsUsePrintPopupArgs"
        :toggleFunc="bmsUsePrintPopupToggleFunc"
        :returnFunc="bmsUsePrintPopupReturnFunc"
      ></BmsDctPrintSendPopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="vReceiptPopupDialog" width="700px" height="900px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title> 송증/영수증</v-toolbar-title>
        <v-btn class="close" icon @click="vReceiptPopupDialog = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctReceipt
        v-if="vReceiptPopupDialog"
        :args="bmsDctDetailPopupArgs"
        :toggleFunc="bmsDctDetailPopupToggleFunc"
        :returnFunc="
          BmsDctDistributeCardPopupReturnFunc
        "></BmsDctReceipt>
    </v-card>
  </v-dialog>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from "dayjs";
import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { VDatePicker } from "vuetify/labs/VDatePicker";
import { API } from "@/api";
import {
  popups,
  togglePopup,
  bringPopupToFront,
  getPopupSize,
  centerPopup,
} from "@/utils/Popup.js";
import { getValueByKey, splitSlash } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import BmsDctPrintSendPopup from "./BmsDctPrintSendPopup.vue";
import BmsDctReceipt from "./BmsDctReceipt.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref("BmsDctDistributeCardPopup");
const props = defineProps({
  args: Object,
  // args: String,
  toggleFunc: Function,
  returnFunc: Function,
});
const args = ref({});

const getHistCardInfoList = ref([])
const reqDocInfo = ref([]);

const selectRelayHisData = ref([]);
const selectsFindManagerData = ref([]);

const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// for selectDistributeList
const checkedListSelectDistributeList = ref([]);
const selectDistributeListList = ref([]);
const selectDistributeListListF = ref([]);
const selectDistributeListDefCondi = {
  docid: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
};
const selectDistributeListCondi = ref({ ...selectDistributeListDefCondi });
const selectDistributeListCondiCheck = ref({
  value: { ...selectDistributeListDefCondi },
  flag: { ...selectDistributeListDefCondi },
});
/* ================ pagination ================ */
const selectDistributeListLoader = ref(true);
const totalPagesSelectDistributeList = ref(0);
const currentPageSelectDistributeList = ref(1);
const pageSizeSelectDistributeList = ref(10);
const pageSizesSelectDistributeList = ref([
  { view: "10개씩 보기", key: 10 },
  { view: "25개씩 보기", key: 25 },
  { view: "50개씩 보기", key: 50 },
]);

const handlePageSizeChangeSelectDistributeList = () => {
  selectDistributeListCondi.value.pageSize = pageSizeSelectDistributeList.value;
  currentPageSelectDistributeList.value = 1;
  selectDistributeList(currentPageSelectDistributeList.value);
};
const handlePageChangeSelectDistributeList = () => {
  selectDistributeList(currentPageSelectDistributeList.value);
};
/* ============================================== */

onBeforeMount(async() => {
  args.value = props.args;
});

onMounted(async () => {
  args.value = props.args;
  try {
    isloading.value = true;
    await getDctOridoc();
    if(args.value && args.value.docstatus === 'EDS06' && args.value.isReturned === true){ //반송인경우
      updateEnfDocRepath(); //접수한 비밀문서를 '문서처리'시 상태값을 업데이트하는 로직 : bms_dct_enf_doc 테이블 docstatus 컬럼을 EDS40 (반송종료) 처리
    }
    selectDistributeListCondi.value.docid = args.value.docid;
    await selectDistributeList(selectDistributeListCondi.value);
    await selectTaskInfo();
    await selectDctRdoc();
    await getDctRdoc();
    await getDctAttr();
    await selectSendinfo();
    await selectDctPath();
    await getDctRecv();
    await selectRelayHis();
    await selectDctReader();
    await selectDctFile();
    await selectFindManager();
    await selectSendFileList();
  }finally {
    isloading.value = false;
  }
});
onBeforeUnmount(() => {});

const allNotPrinted = ref('N');
const isAllNotPrinted = () => {
  allNotPrinted.value = reqDocInfo.value.every(item => item.prtCnt == 0) ? 'Y' : 'N';
};
watch(() => reqDocInfo.value, async () => {
  await addHistoryToFiles();
  isAllNotPrinted();
  await addHistoryToFiles();
});

watch(route, async (route) => {
  setQueries(
    route,
    selectDistributeListCondi.value,
    selectDistributeListDefCondi
  );
  await selectDistributeList(selectDistributeListCondi.value.pageNum);
});

watch(
  () => _.cloneDeep(selectDistributeListCondi.value),
  (newVal, oldVal) => {
    setCondiChanged(selectDistributeListCondiCheck.value, newVal, oldVal);
  }
);

watchEffect(() => {
  args.value = props.args;
});

// const exstatusText = ref('arrive');
const exstatusText = ref('fail');
function toggleexstatusText() {
  exstatusText.value = exstatusText.value === 'arrive' ? 'fail' : 'arrive';
}

const isloading = ref(false);

const orgnProtDtPeriodSelected = ref("");
const copyProtDtPeriodSelected = ref("");

// 날짜 지정
const orgnDate = ref({
  year: "",
  month: "",
  day: "",
})
const copyDate = ref({
  year: "",
  month: "",
  day: "",
})

const selectSendinfoList = ref([])
const selectSendinfoDefCondi = {
  docid: props.args.docid,
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectSendinfoCondi = ref({ ...selectSendinfoDefCondi })

const selectDistributeListRouterPush = (pageNum) => {
  selectDistributeListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectDistributeListCondi.value,
    })
    .catch((error) => {
      console.log(error);
    });
};


// const paramValue = ref({});
const selectDistributeList = async (paramValue) => {
  selectDistributeListLoader.value = true;
  // selectDistributeListCondi.value.pageNum = parseInt(1);
  try {
    const response = await API.dctAPI.selectDistributeList(
      { ...selectDistributeListCondi.value },
      urlPaths.value
    );
    selectDistributeListList.value = response.data.list;
    selectDistributeListListF.value = [...response.data.list];
    if(isOridoc.value === 'oriDoc'){
      selectDistributeListListF.value.splice(0,1);
    }
    totalPagesSelectDistributeList.value = response.data.pages;
    const allOption = pageSizesSelectDistributeList.value.find(
      (option) => option.view === "ALL"
    );
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectDistributeListLoader.value = false;

    setCondiClear(
      selectDistributeListCondiCheck.value,
      selectDistributeListCondi.value
    );
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};

function displayState(selectDistributeListList) {
  //EDS09 접수대기 
  if (selectDistributeListList.state === 'DRDS8' && selectDistributeListList.docstatus === 'EDS06') { //EDS06 반송대기
    return 'DRDS6'; //반송
  }
  if (selectDistributeListList.state === 'DRDS8' && selectDistributeListList.docstatus === 'EDS40') { //EDS40 반송종료
    return '반송종료';
  }
  if (selectDistributeListList.state === 'DRDS8' &&  selectDistributeListList.docstatus === 'EDS05') { //EDS05 접수
    return '접수완료';
  }
  return selectDistributeListList.state;
}
let isOridoc = ref('');
const tempRecvDeptNameList = ref([]);
// 원본 docid 찾기
const getDctOridoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc(
      {docid: props.args.docid}, // 사본 docid
      urlPaths.value
    );
    const data = response.data;
    if(data.oridocid){
      args.value.oridocid = data.oridocid;
    }else{
      args.value.oridocid = props.args.docid;
      isOridoc.value = 'oriDoc';
    }
  } catch (error) {
    console.log(error);
  }
};
const responseData = ref([]);
const selectTaskInfo  = async () => {
  try {
    const response = await API.dctAPI.selectTaskInfo ({
      docid: args.value.oridocid },
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
      upd.fileexist = response.data[i].attachid != null? 'Y':'N';
      upd.sfilename = response.data[i].sfilename;
      upd.attachid = response.data[i].attachid;
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

const getDctRdocData = ref({});
const selectDctRdoc = async () => {
  try {
    const response = await API.dctAPI.selectTempCreateCardList(
      {docid: args.value.oridocid,
        loginuserid: loginStore.getUserLoginData.userid
      },
      urlPaths.value
    );
    getDctRdocData.value = response.data;
    getDctRdocData.value.refList = responseData.value.refList;

    getDctRdocData.value.loginUserId =loginStore.getUserLoginData.userid;
    getDctRdocData.value.loginUserName =loginStore.getUserLoginData.username;
    getDctRdocData.value.copyparams = [];
    getDctRdocData.value.copyparams.push(props.args);
    if(getDctRdocData.value.copyparams[0].copyno){
      getDctRdocData.value.copyOrder = splitSlash(getDctRdocData.value.copyparams[0].copyno)[0];
    }else{
      getDctRdocData.value.copyOrder = '0';
    }
    getDctRdocData.value.isresend = 'resend';
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 1) bms_dct_rdoc table
const getDctRdoc = async () => {
  if(args.value.oridocid){
    try {
      const response = await API.dctAPI.getDctRdoc({
        docid: args.value.oridocid},
        urlPaths.value
      );
      const data = response.data;
      getDctRdocData.value.docttl = data.docttl;
      getDctRdocData.value.gubun = data.gubun;
      getDctRdocData.value.state = data.state;
      getDctRdocData.value.open = data.open;
      getDctRdocData.value.readrangetype = data.readrangetype;
      getDctRdocData.value.emergency = data.emergency;
      getDctRdocData.value.keyword = data.keyword;
      getDctRdocData.value.authorname = data.authorname;
      getDctRdocData.value.authordutyname = data.authordutyname;
      getDctRdocData.value.authordeptname = data.authordeptname;
      getDctRdocData.value.tempflag = data.tempflag;
      getDctRdocData.value.indt = data.indt;
    } catch (error) {
      console.log(error);
      alert("Server Error");
    }
  }
};

// 3) bms_dct_attr table
const getDctAttr = async () => {
  const response = await API.dctAPI.getDctAttr({
      docid: args.value.oridocid},
      urlPaths.value
    );
    const data = response.data;

    getDctRdocData.value.secgubun = data.secgubun;
    getDctRdocData.value.seclevel = data.seclevel;
    getDctRdocData.value.prsrvterm = data.prsrvterm ? (data.prsrvterm.trim() != "" ? data.prsrvterm : '') : '';
    getDctRdocData.value.orgnprotdt = data.orgnprotdt;
    getDctRdocData.value.orgnnextlevel = data.orgnnextlevel ;
    getDctRdocData.value.orgnreclass = data.orgnreclass ? (data.orgnreclass.trim() != "" ? data.orgnreclass : '') : '';
    getDctRdocData.value.copyprotdt = data.copyprotdt;
    getDctRdocData.value.copynextlevel = data.copynextlevel;
    getDctRdocData.value.copyreclass = data.copyreclass ? (data.copyreclass.trim() != "" ? data.copyreclass : '') : '';
    getDctRdocData.value.docno = data.docno;
    getDctRdocData.value.mgmtno = data.mgmtno;
    getDctRdocData.value.docno = data.docno;

  if(getDctRdocData.value.orgnprotdt){
    sliceDates(getDctRdocData.value.orgnprotdt, orgnDate.value);
  }
  if(getDctRdocData.value.copyprotdt){
    sliceDates(getDctRdocData.value.copyprotdt, copyDate.value);
  }
  if(getDctRdocData.value.orgnprotdt){
    sliceDates(getDctRdocData.value.orgnprotdt, orgnDate.value);
    orgnProtDtPeriodSelected.value = (parseInt(orgnDate.value.year) - parseInt(getDctRdocData.value.indt.slice(0,4))).toString();
    getDctRdocData.value.orgnProtDtPeriodSelected = orgnProtDtPeriodSelected.value
  };
  if(getDctRdocData.value.copyprotdt){
    sliceDates(getDctRdocData.value.copyprotdt, copyDate.value);
    copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(getDctRdocData.value.indt.slice(0,4))).toString();
    getDctRdocData.value.copyProtDtPeriodSelected = copyProtDtPeriodSelected.value
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

const selectSendinfo = async (pageNum) => {
  selectSendinfoCondi.value.pageNum = parseInt(1);
  selectSendinfoCondi.value.docid = props.args.docid;

  try {
    const response = await API.dctAPI.selectSendinfo({ ...selectSendinfoCondi.value }, urlPaths.value);
    selectSendinfoList.value = response.data.list;
    getDctRdocData.value.sendinfolist = [];
    if(response.data.length>0){
      getDctRdocData.value.sendinfolist.push(response.data[0]);
      getDctRdocData.value.sendinfolist[0].docid = args.value.oridocid;
    }
  } catch (error) {
    console.log(error);
  }
};

// 4) bms_dct_path table
const selectDctPath  = async () => {
  getDctRdocData.value.pathList = [];
  if(getDctRdocData.value.pathlist){
    if(getDctRdocData.value.pathlist.length >0){
    for(let i=0; i<getDctRdocData.value.pathlist.length; i++){
        const upd = {};
        upd.askTemp = transformAsktype(getDctRdocData.value.pathlist[i].asktype);
        upd.asktype = getDctRdocData.value.pathlist[i].asktype;
        upd.deptname = getDctRdocData.value.pathlist[i].deptname;
        upd.dutyname = getDctRdocData.value.pathlist[i].dutyname;
        upd.dutynamedesc = getDctRdocData.value.pathlist[i].dutynamedesc;
        upd.electsignid = getDctRdocData.value.pathlist[i].electsignid;
        upd.reportdt = getDctRdocData.value.pathlist[i].reportdt;
        upd.mainver = getDctRdocData.value.pathlist[i].mainver;
        upd.pathorder = getDctRdocData.value.pathlist[i].pathorder;
        upd.objid = getDctRdocData.value.pathlist[i].objid;
        upd.objname = getDctRdocData.value.pathlist[i].reportdt != null ? getDctRdocData.value.pathlist[i].objname : "";
        upd.opinion = getDctRdocData.value.pathlist[i].opinion;
        upd.pathstate = getDctRdocData.value.pathlist[i].pathstate;
        getDctRdocData.value.pathList.push(upd);
      }
    }
  }
}

// 6) bms_dct_appr_recv
const getDctRecv = async () => {
  getDctRdocData.value.recvList = [];
  const docidData = ref({docid: ''});
  if(isOridoc.value === 'oriDoc'){
    docidData.value.docid = props.args.docid;
  }else{
    docidData.value.docid = props.args.docid;
  }
  const response = await API.dctAPI.getDctRecv({
    ...docidData.value },
    urlPaths.value
  );
  response.data.sort((a, b) => a.seq - b.seq);
   getDctRdocData.value.recvlistOri = [];
   getDctRdocData.value.recvlistOri = [...getDctRdocData.value.recvlist];
  getDctRdocData.value.recvlist = [];
  getDctRdocData.value.recvlist = [...response.data];
  if(getDctRdocData.value.recvlist){
    for(let i=0; i<getDctRdocData.value.recvlist.length; i++){
      const upd = {};
      upd.receiverid = getDctRdocData.value.recvlist[i].receiverid;
      upd.apprrecvtempflag = getDctRdocData.value.recvlist[i].tempflag;
      upd.apprrecvgubun = getDctRdocData.value.recvlist[i].gubun;
      upd.apprrecvseq = getDctRdocData.value.recvlist[i].seq;
      upd.apprrecvdeptname = getDctRdocData.value.recvlist[i].deptname;
      upd.apprrecvrefdeptname = getDctRdocData.value.recvlist[i].refdeptname;
      getDctRdocData.value.recvList.push(upd);
      getDctRdocData.value.sendOrgNameData = getDctRdocData.value.recvlist[i].distdeptname;
      getDctRdocData.value.sendNameData = getDctRdocData.value.sendOrgNameData == '원자력통제기술원' ? '원자력통제기술원장' : getDctRdocData.value.sendOrgNameData+ '부서장';
    }

    if(getDctRdocData.value.copyOrder && getDctRdocData.value.copyOrder !== '0' && isOridoc.value !== 'oriDoc'){
      props.args.recvdeptname = getDctRdocData.value.recvList[0].apprrecvdeptname;
    }else{
      props.args.recvdeptname = getDctRdocData.value.recvList[0].apprrecvdeptname;
    }

    for(let i=0; i<getDctRdocData.value.recvlistOri.length; i++){
      tempRecvDeptNameList.value.push(getDctRdocData.value.recvlistOri[i].deptname)
    }
  }
  updateCardBIAData.value.recvMultiList = [...getDctRdocData.value.recvList]

  getDctRdocData.value.recvMultiList = [];
  getDctRdocData.value.recvMultiList.push(updateCardBIAData.value.recvMultiList[0]);

}
const formatRecvList = computed(() => {
  if(getDctRdocData.value.recvList){
    const formattedRecvList = getDctRdocData.value.recvList.map((item) => {
      return `${item.apprrecvdeptname}(${item.apprrecvrefdeptname})`;
    });
    return formattedRecvList.join(', ');
  }
});

// 7) bms_dct_rdoc_rcvs
const selectDctReader = async () => {
  getDctRdocData.value.rcvsList = [];
  if(getDctRdocData.value.rcvslist){
    for(let i=0; i<getDctRdocData.value.rcvslist.length; i++){
      const upd = {};
      upd.rcvsuserid = getDctRdocData.value.rcvslist[i].userid;
      upd.rcvsusername = getDctRdocData.value.rcvslist[i].username;
      upd.rcvsdeptid = getDctRdocData.value.rcvslist[i].deptid;
      upd.rcvdeptname = getDctRdocData.value.rcvslist[i].deptname;
      upd.rcvdutyname = getDctRdocData.value.rcvslist[i].dutyname;
      upd.rcvsdeptnamedesc = getDctRdocData.value.rcvslist[i].deptnamedesc;
      upd.rcvsinseq = getDctRdocData.value.rcvslist[i].inseq;
      getDctRdocData.value.rcvsList.push(upd);
    }
  }
}
const formatRcvsList = computed(() => {
  if(getDctRdocData.value.rcvsList){
    const formattedRcvsList = getDctRdocData.value.rcvsList.map((item) => {
      return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
    });
    return formattedRcvsList.join(', ');
  }
});

// 10) bms_dct_file table
const draftDocInputData = ref("");
const selectDctFile = async () => {
  try {
    const response = await API.dctAPI.selectDctFile({
      docid: args.value.oridocid
    },
      urlPaths.value
    );
    getDctRdocData.value.fileList = [];
    getDctRdocData.value.draftfile = [];
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
        getDctRdocData.value.fileList.push(upd);
      } else {
        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          indtArr.push(Number(response.data[i].indt));
        }

        let maxIndt = Math.max(...indtArr);
        if(Number(response.data[i].indt) == maxIndt) {
          draftDocInputData.value = response.data[i].sfilename;
          getDctRdocData.value.drafturl = response.data[i].fleurl;
        }

        getDctRdocData.value.draftfile.push(response.data[i]);
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const updateCardBIAData = ref({});
const updateCardBIA = async () => {
  // 1) bms_dct_rdoc table
  args.value.tempflag = "N";
  args.value.state = "DRDS8"; // 발송완료
  // 4) bms_dct_path table
  getDctRdocData.value.pathList.map(item=>
    {
      if(item.pathorder=="1"){
        item.pathstate = "DCST5" //처리완료. // DCST4: 처리중
      }else{
      }
    }
  ); 
  // 6) bms_dct_appr_recv table
  getDctRdocData.value.recvList.map(item=>item.apprrecvtempflag = "N");
  getDctRdocData.value.recvList.map(item=>item.apprrecvgubun = "RDR08"); //원내

  updateCardBIAData.value = args.value;
  updateCardBIAData.value = getDctRdocData.value;
  updateCardBIAData.value.refList = responseData.value.refList;
  updateCardBIAData.value.recvMultiList = getDctRdocData.value.recvList
  updateCardBIAData.value.sendopinion = getDctRdocData.value.sendopinion;
  updateCardBIAData.value.loginuserid = loginStore.getUserLoginData.userid;
  updateCardBIAData.value.authorid = getUserLoginData.value.userid;
  updateCardBIAData.value.authorname = getUserLoginData.value.username;
  updateCardBIAData.value.authordutyname = getUserLoginData.value.grade;
  updateCardBIAData.value.authordeptid = getUserLoginData.value.deptid;
  updateCardBIAData.value.authordeptname = getUserLoginData.value.deptname;
  updateCardBIAData.value.enfdocid = props.args.enfdocid;

  updateCardBIAData.value.draftfile = getDctRdocData.value.draftfile;

  updateCardBIAData.value.actgubun = "EPA09"; //EPA09 업무관리지정 //bms_dct_enf_history.actgubun 처리구분
  updateCardBIAData.value.isresend = "resend";
  updateCardBIAData.value.docstatus = "EDS41";//재발송 bms_dct_enf_doc 테이블 docstatus 컬럼

  // ############ 송수신 테스트용 임시 파라미터 ############ //
  // updateCardBIAData.value.exstatus = "arrive";
  // updateCardBIAData.value.exstatus = "fail";
  updateCardBIAData.value.exstatus = exstatusText.value;
  // ############ 송수신 테스트용 임시 파라미터 ############ //

  var insertCardBIAFormData = new FormData(); 
  getDctRdocData.value.fileList.forEach((sfilename) => {
    insertCardBIAFormData.append('draftAttach', sfilename);
  });

  getDctRdocData.value.refList.forEach((ref)=>{
    if(ref.sfilename){
      insertCardBIAFormData.append('refAttach', ref.sfilename);
    }
  });     
  const {files, ...rest} = updateCardBIAData.value;
  const textData = Object.assign({}, rest);
  insertCardBIAFormData.append(
    "reqUpdateCardBIA",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  for (let [key, value] of insertCardBIAFormData.entries()) {
  }

  if(!confirm("처리하시겠습니까?")){
    isloading.value = false;
    return;
  }

  try {
    const response = await API.dctAPI.updateCardBIA(insertCardBIAFormData, urlPaths.value);
    if (response.status == 200) {
      await selectRelayHis();
      if (selectRelayHisData.value[0].exstatus === 'fail') {
        alert("요청하신 문서가 재발송되었으나 수신이 불량하여 발송실패하였습니다. 다시 시도해주세요." + " " + "실패횟수 : " + selectRelayHisData.value[0].retry + "/3" + " 3회 실패마다 오프라인인쇄, 발송안함 기능이 활성화 됩니다.");
        if (selectRelayHisData.value[0].retry == 3) {
          if(props.args.fromPage && props.args.fromPage === 'BmsDctSendCurrentList'){
            confirmPopUp();
          }else{
            // moveToBmsDctreturnlist();
            confirmPopUp();
          }
        }else{
          confirmPopUp();
          // moveToBmsDctsendCurrentList();
        }
      } else {
        alert("요청하신 문서가 재발송되었습니다.");
        if(props.args.fromPage && props.args.fromPage === 'BmsDctSendCurrentList'){
          confirmPopUp();
        }else{
          // moveToBmsDctsendCurrentList();
          confirmPopUp();
        }
      }
      isloading.value = false;
      // confirmPopUp();
    }
  } catch (error) {
    isloading.value = false;
    console.log(error);
    alert("Server Error");
  }
};

const selectRelayHis = async () => {
  try {
    const response = await API.dctAPI
    .selectRelayHis(
      {
        pageNum: 1,
        pageSize: 99999999,

        docid : getDctRdocData.value.copyparams[0].docid,
        receiverid : getDctRdocData.value.recvList[0].receiverid,
        docorder : getDctRdocData.value.recvList[0].apprrecvseq,
      },
      urlPaths.value
    );
    selectRelayHisData.value = response.data.list;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const selectFindManager = async () => {
if(isOridoc.value !== 'oriDoc'){
  try {
    const response = await API.comAPI.selectFindManager({
      vgroupid: ['5220'],
      orgid: getDctRdocData.value.recvlistOri[parseInt(getDctRdocData.value.copyOrder)-1].receiverid
    },
    urlPaths.value);
    selectsFindManagerData.value.push(response.data);
  }catch (error) {
    console.log(error);
    alert("Server Error");
  }
}else{
  for (let i = 0; i < getDctRdocData.value.recvlistOri.length; i++) {
    try {
      const response = await API.comAPI.selectFindManager({
        vgroupid: ['5220'],
        orgid: getDctRdocData.value.recvlistOri[i].receiverid
        },
        urlPaths.value);
      selectsFindManagerData.value.push(response.data);
    } catch (error) {
      console.log(error);
      alert("Server Error");
    }
  }
}
}

/* ======================== 오프라인인쇄 실행 팝업 ======================== */
let stateValue = "";
const bVisibleUsePrintPopup = ref(false)
const bmsUsePrintPopupArgs = ref({type:""})
const bmsUsePrintPopupToggleFunc = ref((val) => { 
  bmsUsePrintPopupArgs.value.isLend = 'Y';
  bmsUsePrintPopupArgs.value.docid = selectDistributeListList.value[0].docid;
  bmsUsePrintPopupArgs.value.mgmtno = getDctRdocData.value.mgmtno;
  bmsUsePrintPopupArgs.value.secttl = selectDistributeListList.value[0].docttl;
  bmsUsePrintPopupArgs.value.authorname = getDctRdocData.value.authorname;
  bmsUsePrintPopupArgs.value.authordutyname = getDctRdocData.value.authordutyname;
  bmsUsePrintPopupArgs.value.deptname = getDctRdocData.value.deptname;
  bmsUsePrintPopupArgs.value.regirecvtype = getDctRdocData.value.regirecvtype;
  bmsUsePrintPopupArgs.value.recvdeptname = props.args.recvdeptname;
  bmsUsePrintPopupArgs.value.copyno = selectDistributeListListF.value[0].copyno;
  bmsUsePrintPopupArgs.value.oridocid = args.value.oridocid;
  bVisibleUsePrintPopup.value = !bVisibleUsePrintPopup.value; 
});
const bmsUsePrintPopupReturnFunc = ref((retValue) => {
  if(retValue){
    bmsUsePrintPopupToggleFunc.value(retValue);
    stateValue = retValue.value.state;
    if(stateValue === 'DRDS8'){
      selectDistributeList({docid: args.value.oridocid});
    }
    confirmPopUp();
  }
});
/* ============================================================ */

const notSend = async() => {
  // 1) bms_dct_rdoc table
  args.value.tempflag = "N";
  args.value.state = "DRDS5"; // 발송안함
  // 4) bms_dct_path table
  getDctRdocData.value.pathList.map(item=>
    {if(item.pathorder=="1"){
      item.pathstate = "DCST5" //처리완료. // DCST4: 처리중
    }}); 
  // 6) bms_dct_appr_recv table
  getDctRdocData.value.recvList.map(item=>item.apprrecvtempflag = "N");
  getDctRdocData.value.recvList.map(item=>item.apprrecvgubun = "RDR08"); //원내
  updateCardBIAData.value = args.value;
  updateCardBIAData.value = getDctRdocData.value;
  updateCardBIAData.value.exstatus = exstatusText.value;
  updateCardBIAData.value.refList = responseData.value.refList;
  updateCardBIAData.value.recvMultiList = getDctRdocData.value.recvList
  updateCardBIAData.value.sendopinion = getDctRdocData.value.sendopinion;
  updateCardBIAData.value.loginuserid = loginStore.getUserLoginData.userid;
  updateCardBIAData.value.authorid = getUserLoginData.value.userid;
  updateCardBIAData.value.authorname = getUserLoginData.value.username;
  updateCardBIAData.value.authordutyname = getUserLoginData.value.grade;
  updateCardBIAData.value.authordeptid = getUserLoginData.value.deptid;
  updateCardBIAData.value.authordeptname = getUserLoginData.value.deptname;

  updateCardBIAData.value.draftfile = getDctRdocData.value.draftfile;

  updateCardBIAData.value.actgubun = "EPA09"; //EPA09 업무관리지정 //bms_dct_enf_history.actgubun 처리구분
  updateCardBIAData.value.isresend = "resend";

  updateCardBIAData.value.isNotSend = 'NotSend';

  var insertCardBIAFormData = new FormData(); 
  getDctRdocData.value.fileList.forEach((sfilename) => {
    insertCardBIAFormData.append('draftAttach', sfilename);
  });

  getDctRdocData.value.refList.forEach((ref)=>{
    if(ref.sfilename){
      insertCardBIAFormData.append('refAttach', ref.sfilename);
    }
  });     
  const {files, ...rest} = updateCardBIAData.value;
  // const {files, ...rest} = args.value;
  const textData = Object.assign({}, rest);

  insertCardBIAFormData.append(
    "reqUpdateCardBIA",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  for (let [key, value] of insertCardBIAFormData.entries()) {
  }

  if(!confirm("처리하시겠습니까?")){
    isloading.value = false;
    return;
  }

  try {
    const response = await API.dctAPI.updateCardBIA(insertCardBIAFormData, urlPaths.value);
    if (response.status == 200) {
      alert("발송안함 처리 되었습니다.")
      isloading.value = false;
      confirmPopUp();
    }
  }catch (error) {
    isloading.value = false;
    console.log(error);
    alert("Server Error");
  }
};

const selectSendDocInfoList = ref([]);
const selectSendFileList = async () => {
  try {
    const response = await API.dctAPI.selectSendFileList(
      {docid: props.args.docid},
      urlPaths.value
    );
    selectSendDocInfoList.value = response.data;
    reqDocInfo.value = response.data;
    
    await addHistoryToFiles();
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
const getHistCardInfo = async () => {
  try {
    const response = await API.dctAPI.getHistCardInfo({
        docid:props.args.docid,
        histstatus: '057',
      }, urlPaths.value);
    getHistCardInfoList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// 이력 정보를 조회하는 함수
async function fetchHistoryData(file) {
  try {
    const response = await API.dctAPI.getHistCardInfo({
        docid:file.docid,
        histstatus: '057',
        etcFileOrder: (file.rownum).toString(),
      }, urlPaths.value);
    getHistCardInfoList.value = response.data;
    return { prtCnt: response.data.length }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// 파일 리스트에 이력 정보(인쇄부수)를 추가하는 함수
async function addHistoryToFiles() {
  for (const file of reqDocInfo.value) {
    const historyData = await fetchHistoryData(file);
    file.prtCnt = historyData.prtCnt;
  }
}

const updateEnfDocRepath = async () => {
  try {
    const response = await API.dctAPI.updateEnfDocRepath(
      {enfdocid: props.args.enfdocid, docstatus: 'EDS40'},
      urlPaths.value
    );
    const data = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};


/*-------------송증팝업-------------- */
const vReceiptPopupDialog = ref(false);
const bVisibleBmsDctDistributeCardPopup = ref(false);
const BmsDctDistributeCardPopupReuturn = ref({});
const bmsDctDetailPopupArgs = ref({});

const bmsDctDetailPopupToggleFunc = ref((props, actionType) => {
  vReceiptPopupDialog.value = !vReceiptPopupDialog.value;//송증/영수증 출력
  if(props){
    props.args.fromPage = 'BmsDctDistributeCardPopup';
    props.args.actionType = actionType;
    bmsDctDetailPopupArgs.value = props.args;
  }
});

const BmsDctDistributeCardPopupReturnFunc = ref((retValue) => {
  confirmPopUp();
});
/*-------------송증팝업-------------- */

const selectSendCurrentListList = ref([]);
const currentPageSelectSendCurrentList = ref(1);
const totalPagesSelectSendCurrentList = ref(0);
const totalPagesSelectSendCurrentListCnt = ref(0);
const selectSendCurrentListLoader = ref(true);
const selectSendCurrentListDefCondi = {
  docid: "",
  docttl: "",
  authorname: "",
  orgname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
};
const selectSendCurrentListCondi = ref({ ...selectSendCurrentListDefCondi });
const selectSendCurrentList = async (pageNum) => {
  vReceiptPopupDialog.value = false;
  selectSendCurrentListLoader.value = true;
  selectSendCurrentListCondi.value.pageNum = parseInt(pageNum);
  selectSendCurrentListCondi.value.userid = getUserLoginData.value.userid;

  try {
    const response = await API.dctAPI.selectSendCurrentList(
      { ...selectSendCurrentListCondi.value },
      urlPaths.value
    );
    selectSendCurrentListList.value = response.data.list;
    responseData.value = response.data;
    totalPagesSelectSendCurrentList.value = response.data.pages;
    totalPagesSelectSendCurrentListCnt.value = response.data.total

    selectSendCurrentListList.value.forEach(item=>{
      if(['EDS05','EDS11', 'EDS10'].includes(item.docstatus)){ //[접수,종료,재지정]
        item.displayDocstatus = "○" //접수증 있음표기
      }else{
        item.displayDocstatus = ""
      }
    });

    // 오프라인접수자 등록한 이름.
    selectSendCurrentListList.value.forEach(item=>{
      if(item.acceptname){
        item.username = item.acceptname
      }
    })

    selectSendCurrentListLoader.value = false;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};



// // Move Function
const moveToBmsDctsendCurrentList = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};

// // Move Function
const moveToBmsDctreturnlist = () => {
  router.push({
    name: "BmsDctreturnlist"
    });
};

// Popup
const closePopUp = () => {
  props.toggleFunc();
};

const confirmPopUp = () => {
  props.returnFunc();
}
</script>

<style scoped>
  .test-button {
    color: white;
    border: none;
    opacity: 0; /* 기본 상태에서는 완전히 투명 */
    transition: opacity 0.3s;
  }
  
  .test-button:hover {
    opacity: 0.5; /* 마우스를 올렸을 때 반투명하게 보이도록 설정 */
  }
</style>