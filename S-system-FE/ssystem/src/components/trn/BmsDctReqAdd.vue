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
      <h4 class="h4">인계인수</h4>
    </div>

    <div class="box-content">
      <!-- 인계인수서작성 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">인계인수서 작성</h5>
          <div class="action-buttons">
            <v-btn class="button" @click="trnApprovalPopupToggleFunc()">승인요청</v-btn>
            <v-btn class="button" @click="moveToBmstrncntlist()">닫기</v-btn>
          </div>
        </div>
      </section>

      <!-- 기본정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">기본정보</h6>
        </div>

        <v-table class="table-type-03">
          <colgroup>
            <col width="120">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th><span class="required">제목</span></th>
              <td>
                <v-text-field
                  v-model="insertApprreqData.reqttl"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th><span class="required">사유</span></th>
              <td>
                <v-text-field
                  v-model="insertApprreqData.reqcontents"
                  variant="outlined"
                  hide-details="auto"></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 인계정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인계정보</h6>
          <div class="action-buttons">
            <v-btn class="button" @click="dctPathPopupToggleFunc(1)">인계결재선 지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 25%;">부서</th>
              <th style="width: 25%;">직위(직급)</th>
              <th style="width: 25%;">성명</th>
              <th style="width: 25%;">구분</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="handover, idx in insertApprreqData.handoverList" :key="idx">
              <td>
                {{ handover.apprdeptname }}
              </td>
              <td>
                {{ handover.apprposition }}
              </td>
              <td>
                {{ handover.apprusername }}
              </td>
              <td>
                {{ handover.handovergubun }}
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 인수정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인수정보</h6>
          <div class="action-buttons">
            <v-btn class="button" @click="dctPathPopupToggleFunc(2)">인수결재선 지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 25%;">부서</th>
              <th style="width: 25%;">직위(직급)</th>
              <th style="width: 25%;">성명</th>
              <th style="width: 25%;">구분</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!insertApprreqData.takeoverList || insertApprreqData.takeoverList[0].apprusername === ''">
              <td :colspan="4" text @click="dctPathPopupToggleFunc(2)" class="temp-cursor-pointer">{{ noObjectDataText2 }}</td>
            </tr>
            <template v-if="insertApprreqData.takeoverList && insertApprreqData.takeoverList[0].apprusername !== ''">
              <tr v-for="takeover, idx in insertApprreqData.takeoverList" :key="idx">
                <td>
                  {{ takeover.apprdeptname }}
                </td>
                <td>
                  {{ takeover.apprposition }}
                </td>
                <td>
                  {{ takeover.apprusername }}
                </td>
                <td>
                  {{ takeover.takeovergubun }}
                </td>
              </tr>
            </template>
          </tbody>
        </v-table>
      </section>

      <!-- 인계인수목록 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">인계인수목록</h6>
          <div class="action-buttons">
            <v-btn v-if="dataObject.selectedType === 'partial'" class="button" @click="trnObjectPopupToggleFunc()">대상선택</v-btn>
          </div>
        </div>

        <v-table v-if="dataObject.selectedType === 'partial'" class="table-type-04" :no-data-text="noObjectDataText">
          <thead>
            <tr>
              <th style="width: 120px;">등록일</th>
              <th style="width: 120px;">관리번호</th>
              <th style="width: 120px;">문서번호</th>
              <th style="width: 300px;">제목</th>
              <th style="width: 50px;">종류</th>
              <th style="width: 50px;">구분</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!insertApprreqData.selectList || insertApprreqData.selectList.length ===0">
              <td :colspan="6" text @click="trnObjectPopupToggleFunc" class="temp-cursor-pointer">{{ noObjectDataText }}</td>
            </tr>
            <tr v-for="select, idx in insertApprreqData.selectList" :key="idx">
              <td>
                 {{ transformDate(select.reportdt) }}
              </td>
              <td>
                {{ select.mgmtno }}
              </td>
              <td>
                {{ select.docno }}
              </td>
              <td class="text-left">
                {{ select.objectname }}
              </td>
              <td>
                {{ select.paperflag === 'Y' ? '전자' : (select.paperflag === 'N' ? '비전자' : '') }}
              </td>
              <td>
                {{ select.regirecvgubun }}
              </td>
            </tr>
          </tbody>
        </v-table>

        <v-table v-if="dataObject.selectedType === 'all'" class="table-type-04">
          <thead>
            <tr>
              <th style="width: 25%;">총계</th>
              <th style="width: 25%;">생산비밀</th>
              <th style="width: 25%;">접수비밀</th>
              <th style="width: 25%;">재분류(일반)</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{dataObject.responseData.totalCount}}</td>
              <td>{{dataObject.responseData.createOtherCount}}</td>
              <td>{{dataObject.responseData.receiptCount}}</td>
              <td>{{dataObject.responseData.create5LevelCount}}</td>
            </tr>
          </tbody>
        </v-table>
      </section>
      <br/><br/>
    </div>
  </div>

  <v-dialog v-model="bVisibleDctPathPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>{{btnNo === 1 ? '인계 ' : '인수 '}}결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctPathPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctPathTrnPopup
        v-if="bVisibleDctPathPopup"
        :args="dctPathPopupArgs"
        :toggleFunc="dctPathPopupToggleFunc"
        :returnFunc="dctPathPopupReturnFunc"
      ></DctPathTrnPopup>
    </v-card>
  </v-dialog>


  <v-dialog v-model="bVisibleTrnObjectPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>인계인수 대상선택</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleTrnObjectPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <TrnObjectPopup
        v-if="bVisibleTrnObjectPopup"
        :args="trnObjectPopupArgs"
        :toggleFunc="trnObjectPopupToggleFunc"
        :returnFunc="trnObjectPopupReturnFunc"
      ></TrnObjectPopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="bVisibleTrnApprovalPopup" style="width: 800px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>승인요청</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleTrnApprovalPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <TrnApprovalPopup
        v-if="bVisibleTrnApprovalPopup"
        :args="trnApprovalPopupArgs"
        :toggleFunc="trnApprovalPopupToggleFunc"
        :returnFunc="trnApprovalPopupReturnFunc"
      ></TrnApprovalPopup>
    </v-card>
  </v-dialog>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import DctPathTrnPopup from "@/components/dct/DctPathTrnPopup.vue";
import TrnApprovalPopup from "./TrnApprovalPopup.vue";
import TrnObjectPopup from "./TrnObjectPopup.vue";
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import { transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { togglePopup } from "@/utils/Popup.js";

const name = ref('BmsDctReqAdd')
const params = ref({}); //리스트에서 디테일카드로 넘겨받기위한 선언
const query = ref({}); //리스트에서 디테일카드로 넘겨받기위한 선언
const noObjectDataText = "데이터가 없습니다. 인계할 대상을 선택해 주세요.";
const noObjectDataText2 = "데이터가 없습니다. 인수 담당을 선택해 주세요.";


const selectMgmtRegiListDefCondi = {
  userreqtype: "개인",
  // regirecvgubun: '1', //생산, 접수 구분용도
  seclevel: ['2','3','4','5'],
}
const selectMgmtRegiListCondi = ref({ ...selectMgmtRegiListDefCondi })
const selectMgmtRegiListLoader = ref(true)

const selectMgmtRegiNonPageList = async (pageNum) => {
  selectMgmtRegiListLoader.value = true;
  selectMgmtRegiListCondi.value.pageNum = parseInt(pageNum);
  selectMgmtRegiListCondi.value.authorid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectMgmtRegiNonPageList({ ...selectMgmtRegiListCondi.value }, urlPaths.value);
    selectMgmtRegiListLoader.value = false;
    return response.data;
  } catch (error) {
    alert("Server Error")
    return null;
  }
}

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleDctPathPopup = ref(false)
const dctPathPopupReuturn = ref({})
const dctPathPopupArgs = ref({})
const modifiedtakeoverList = ref([]);
const btnNo = ref(0);
const dctPathPopupToggleFunc = ref((val) => {
  btnNo.value = val;
  const handoverList = [...insertApprreqData.value.handoverList];
  const takeoverList = [...insertApprreqData.value.takeoverList];
  
  const modifiedhandoverList = handoverList.map(item => {
    return {
      deptid: item.apprdeptid,
      deptname: item.apprdeptname,
      grade: item.apprposition,
      userid: item.appruserid,
      username: item.apprusername,
      askTemp: item.handovergubun, //hand
      asktype: item.apprcode,
      identifier: `${item.apprdeptid}-${item.appruserid}`
    }
  })

  
  if(takeoverList[0].appruserid === ''){
    modifiedtakeoverList.value = [];
  }else{
    modifiedtakeoverList.value = takeoverList.map(item => {
      return {
        deptid: item.apprdeptid,
        deptname: item.apprdeptname,
        grade: item.apprposition,
        userid: item.appruserid,
        username: item.apprusername,
        askTemp: item.takeovergubun, //take
        asktype: item.apprcode,
        identifier: `${item.apprdeptid}-${item.appruserid}`
      }
    })
  }
  
  dctPathPopupArgs.value.handover = modifiedhandoverList;
  dctPathPopupArgs.value.takeover = modifiedtakeoverList;
  dctPathPopupArgs.value.myid = getUserLoginData.value.userid;
  dctPathPopupArgs.value.btnNo = btnNo.value;
  bVisibleDctPathPopup.value = !bVisibleDctPathPopup.value;
})

const dctPathPopupReturnFunc = ref((retValue) => {
  const charger = []; // 담당자
  const secretManager = []; // 비밀관리자
  const approver = []; // 승인자(결재자)

  dctPathPopupToggleFunc.value(retValue.temp.btnNo)
  if(retValue != ""){
    if(retValue.temp.btnNo == 1){
      insertInitialHandover();
      insertApprreqData.value.handoverList = [];
      for(let i = 0; i < retValue.length; i++){
        const handover = {};
        handover.apprdeptid = retValue[i].deptid;
        handover.apprdeptname = retValue[i].deptname;
        handover.apprposition = retValue[i].grade;
        handover.appruserid = retValue[i].userid;
        handover.apprusername = retValue[i].username;
        handover.handovergubun = retValue[i].askTemp;
        handover.apprcode = retValue[i].asktype;
        handover.apprseq = i+2;
        handover.apprstatus = "APR01" //(요청)bms_trn_step.apprstatus
        handover.state = "DCST1" //(경로에 있음)bms_trn_step.state
        insertApprreqData.value.handoverList.push(handover);
      }
      for(let i = 0; i < insertApprreqData.value.takeoverList.length; i++){ //apprseq (순서 정렬용)
        insertApprreqData.value.takeoverList[i].apprseq = i+1+insertApprreqData.value.handoverList.length;
      }
      
      // '담당자','검토자', '승인자'에 따라 재정렬
      insertApprreqData.value.handoverList.forEach((handover) => {
        switch (handover.handovergubun) {
          case '담당':
            charger.push(handover);
            break;
          case '검토':
            secretManager.push(handover);
            break;
          case '승인':
            approver.push(handover);
            break;
          default: break;
        }
      })
      insertApprreqData.value.handoverList = charger.concat(secretManager,approver);
      updateHandoverOrder();

    }else if(retValue.temp.btnNo == 2){
      insertApprreqData.value.takeoverList = [];
      for(let i = 0; i < retValue.length; i++){
        const takeover = {};
        takeover.apprdeptid = retValue[i].deptid;
        takeover.apprdeptname = retValue[i].deptname;
        takeover.apprposition = retValue[i].grade;
        takeover.appruserid = retValue[i].userid;
        takeover.apprusername = retValue[i].username;
        takeover.takeovergubun = retValue[i].askTemp;
        takeover.apprcode = retValue[i].asktype;
        takeover.apprseq = i+1+insertApprreqData.value.handoverList.length;
        takeover.apprstatus = "APR01" //(요청)bms_trn_step.apprstatus
        takeover.state = "DCST1" //(경로에 있음)bms_trn_step.state
        insertApprreqData.value.takeoverList.push(takeover);
      }
      for(let i = 0; i < insertApprreqData.value.takeoverList.length; i++) { //apprseq (순서 정렬용)
        insertApprreqData.value.takeoverList[i].apprseq = i+1+insertApprreqData.value.handoverList.length;
      }

      // '승인자','검토자', '담당자' 에 따라 재정렬
      insertApprreqData.value.takeoverList.forEach((takeover) => {
        switch (takeover.takeovergubun) {
          case '담당':
            charger.push(takeover);
            break;
          case '검토':
            secretManager.push(takeover);
            break;
          case '승인':
            approver.push(takeover);
            break;
          default:
            break;
        }
      })
      insertApprreqData.value.takeoverList = secretManager.concat(approver,charger);
      updateHandoverOrder();
      for(let i = 0; i < insertApprreqData.value.takeoverList.length; i++) {
        insertApprreqData.value.takeoverList[i].apprseq = i+1+insertApprreqData.value.handoverList.length;
      }
    }
  }else{
    insertInitialHandover();
  }
});

let initialClick = false;
const insertInitialHandover = () => {
  if(initialClick === false) {
    insertApprreqData.value.handoverList = [];
    const initialHandover = {};
    initialHandover.handovergubun = "담당";
    initialHandover.apprcode = "ARC01"; //bms_tnr_step 테이블. 인계담당 //ARC05 는 인수담당
    initialHandover.apprstatus = 'APR02';
    initialHandover.appruserid = getUserLoginData.value.userid;
    initialHandover.apprusername = getUserLoginData.value.username;
    initialHandover.apprposition = getUserLoginData.value.grade
    initialHandover.apprdeptid = getUserLoginData.value.deptid;
    initialHandover.apprdeptname = getUserLoginData.value.deptname;
    initialHandover.apprseq = "1";
    initialHandover.state = "DCST3";
    
    insertApprreqData.value.handoverList.push(initialHandover);
    initialClick = true;
  }
};

const updateHandoverOrder = () => {
  insertApprreqData.value.handoverList.forEach((handover, index) => {
    handover.apprseq = index + 1;
  });
};
/* ============================================================ */

/* ======================== 대상선택 팝업 ======================== */
const bVisibleTrnObjectPopup = ref(false)
const trnObjectPopupReuturn = ref({})
const trnObjectPopupArgs = ref({})
const trnObjectPopupToggleFunc = ref(() => {
  const selectList = [...insertApprreqData.value.selectList];
  trnObjectPopupArgs.value = {
    authorid: insertApprreqData.value.requserid,
    dataObject: dataObject.value.responseData
  };
  const modifiedSelectList = selectList.map(item => {
    return {
      docid: item.objectid,
      secttl: item.objectname,
      mgmtid: item.mgmtid,
      // regirecvtype: item.paperflag,
      regirecvtype: item.paperflag === 'Y' ? '1' : item.paperflag === 'N' ? '2' : undefined,
      // regirecvgubun: item.regirecvgubun === '생산' ? '1' : item.regirecvgubun === '접수' ? '2' : undefined,
      regirecvgubun: item.regirecvgubun,
      seclevel: item.seclevel,
      mgmtno: item.mgmtno,
      indt: item.reportdt,
      copyno: item.copyno,
      authorname: item.selectauthorname,
      deptname: item.selectdeptname,
      secstatus: item.secstatus,
      docnoseq: item.docnoseq,
      status: item.status,
      docno: item.docno,
    }
  })
  trnObjectPopupArgs.value.selectObject = modifiedSelectList;
  bVisibleTrnObjectPopup.value = !bVisibleTrnObjectPopup.value; 
  togglePopup('TrnObjectPopup'); 
});

const trnObjectPopupReturnFunc = ref((retValue) => {
  trnObjectPopupToggleFunc.value()
  if(retValue != ""){
    insertApprreqData.value.selectList = [];
    for(let i = 0; i < retValue.length; i++){
      const select = {};
      select.objectid = retValue[i].docid;
      select.docno = retValue[i].docno;
      select.objectname = retValue[i].secttl;
      select.mgmtid = retValue[i].mgmtid;
      if(retValue[i].regirecvtype == '1'){
        select.paperflag = 'Y'
      }else{
        select.paperflag = 'N'
      }
      select.seclevel = retValue[i].seclevel;
      select.mgmtno = retValue[i].mgmtno;
      select.reportdt = retValue[i].indt;
      select.copyno = retValue[i].copyno;
      select.selectauthorname = retValue[i].authorname;
      select.selectdeptname = retValue[i].deptname;
      select.secstatus = retValue[i].secstatus;
      select.docnoseq = i+1;
      select.status = "TRS01" //(결재중) bms_trn_object.status 진행상태
      select.secstatus = "MGT50" //(인수인계중) bms_dct_mgmt_regi.secstatus 비밀상태
      // select.regirecvgubun = retValue[i].regirecvgubun,
      if(retValue[i].regirecvgubun == '1'){
        select.regirecvgubun = '생산'
      }else{
        select.regirecvgubun = '접수'
      }
      insertApprreqData.value.selectList.push(select);
    }
  }
  trnObjectPopupReuturn.value = retValue;
})
/* ============================================================ */

/* ======================== 승인요청 팝업 ======================== */
const bVisibleTrnApprovalPopup = ref(false)
const trnApprovalPopupArgs = ref({});
const trnApprovalPopupToggleFunc = ref(() => {
  if (checkRVInsertTrnStep() == false) {
    return;
  }
  trnApprovalPopupArgs.value = insertApprreqData.value;
  bVisibleTrnApprovalPopup.value = !bVisibleTrnApprovalPopup.value;
})
const trnApprovalPopupReturnFunc = ref((retValue) => {
  bVisibleTrnApprovalPopup.value = !bVisibleTrnApprovalPopup.value;
  insertApprreqData.value.apprreason = retValue;
  insertTrnStep();
})
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')

// 임시저장
const insertApprreqData = ref({
  apprreqid: "",
  reqtype: "",
  reqdt: "",
  reqttl: "[인계인수서 요청서] 인계인수를 요청합니다.",
  reqregdt: "",
  reqcontents: "",
  reqid: "",
  reqname: "",
  reqdeptid: "",
  reqdeptname: "",
  reqstatus: "",
  apprenddt: "",
  adminconfirmdt: "",
  grantflag: "",
  reqrfidcheck: "",
  reqrfidcheckdt: "",
  finalrfidcheck: "",
  finalrfidcheckdt: "",
  reservedate: "",
  destructreserve: "",
  isnewrule: "",
  apprstatus: "",

  // 인계정보
  handoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      appruserid: "",
      apprusername: "",
      handovergubun: "",
      apprcode:"",
      apprstatus: "",
      apprseq: "",
    }
  ],

  // 인수정보
  takeoverList: [
    {
      apprdeptname: "",
      apprposition: "",
      appruserid: "",
      apprusername: "",
      takeovergubun: "",
      apprcode: "",
      apprstatus: "",
      apprseq: "",
    }
  ],

  // 인계인수목록 대상선택
  selectList: [
    // {
    //   selectdocid: "",
    //   selectdocttl: "",
    //   selectorder: "",
    // }
  ]
})

const loginStore = useLoginStore()
const { getUserLoginData } = storeToRefs(loginStore)

const receivedData = ref({})
const dataObject = computed(() => {
  if (typeof receivedData.value === 'string') {
      return JSON.parse(receivedData.value);
  } else {
    return receivedData.value;
  }
});

onBeforeMount(async() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  receivedData.value = query.value.data;
});

onMounted(() => {
  insertApprreqData.value.requserid = getUserLoginData.value.userid;
  insertInitialHandover();
})


watch(() => dataObject.value.selectedType, (newVal) => {
  const handleDataChange = async () => {
    if (newVal === 'all') {
      try {
        const responseData = await selectMgmtRegiNonPageList(selectMgmtRegiListCondi.value);
        insertApprreqData.value.selectListTemp = responseData;
        if(insertApprreqData.value.selectListTemp != ""){
          for(let i = 0; i < insertApprreqData.value.selectListTemp.length; i++){
            const select = {};
            select.objectid = insertApprreqData.value.selectListTemp[i].docid;
            select.docno = insertApprreqData.value.selectListTemp[i].docno;
            select.objectname = insertApprreqData.value.selectListTemp[i].secttl;
            select.mgmtid = insertApprreqData.value.selectListTemp[i].mgmtid;
            if(insertApprreqData.value.selectListTemp[i].regirecvtype == '1'){
              select.paperflag = 'Y'
            }else{
              select.paperflag = 'N'
            }
            select.seclevel = insertApprreqData.value.selectListTemp[i].seclevel;
            select.mgmtno = insertApprreqData.value.selectListTemp[i].mgmtno;
            select.reportdt = insertApprreqData.value.selectListTemp[i].indt;
            select.copyno = insertApprreqData.value.selectListTemp[i].copyno;
            select.selectauthorname = insertApprreqData.value.selectListTemp[i].authorname;
            select.selectdeptname = insertApprreqData.value.selectListTemp[i].deptname;
            select.secstatus = insertApprreqData.value.selectListTemp[i].secstatus;
            select.docnoseq = i+1;
            select.status = "TRS01" //(결재중) bms_trn_object.status 진행상태
            select.secstatus = "MGT50" //(인수인계중) bms_dct_mgmt_regi.secstatus 비밀상태
            insertApprreqData.value.selectList.push(select);
          }
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        alert("Server Error")
        insertApprreqData.value.selectList = [];
      }
    } else {
      insertApprreqData.value.selectList = [];
    }
  };

  handleDataChange();
});


//승인요청
const insertTrnStep = async () => {
  insertApprreqData.value.transferid = query.value.transferid;
  insertApprreqData.value.requserid = getUserLoginData.value.userid;
  insertApprreqData.value.requsername = getUserLoginData.value.username;
  insertApprreqData.value.apprstatus = 'APP01' //상신 //BMS_TRN_REPORT.apprstatus(최종결재완료여부) APP09 임시저장
  API.trnAPI
    .insertTrnStep(insertApprreqData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        if(response.data === -1) {
          alert("이미 인계인수서가 존재하는 문서가 포함되어 있습니다.");
          return;
        }
        else
          moveToBmsTrnreqlist();
      }
    })
    .catch(error => {
      console.log(error);
      alert("실패하였습니다");
    });
}


// Move Function
const moveToBmsTrnreqlist = () => {
  router.push({
    name: "BmsTrnreqlist"
    });
}

// 닫기버튼
const moveToBmstrncntlist = () => {
  router.push({
    name: "Bmstrncntlist"
  })
}

// Check Validation
const checkRVInsertTrnStep = () => {
  if(!insertApprreqData.value.reqttl){
    alert("제목을 입력하세요.");
    return false;
  }
  if(!insertApprreqData.value.reqcontents){
    alert("사유를 입력하세요.");
    return false;
  }
  if(insertApprreqData.value.handoverList.length < 2){
    alert("인계결재선을 확인하세요.");
    return false;
  }
  if(insertApprreqData.value.takeoverList.length < 2){
    alert("인수결재선을 확인하세요.");
    return false;
  }
  if(dataObject.value.selectedType ==='partial' && insertApprreqData.value.selectList.length == 0){
    alert("인계인수목록을 확인하세요.");
    return false;
  }
  
  return true;
}

</script>

<style lang="scss" scoped>
.btns{
  display: flex;
  justify-content: end;
}
.btn{
  border: 1px solid blue;
  font-weight: 600;
  margin-right: 10px;
}
.temp-cursor-pointer {
  cursor: pointer;
}
</style>
