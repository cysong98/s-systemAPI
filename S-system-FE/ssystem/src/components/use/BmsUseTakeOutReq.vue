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
      <h4 v-if="getFromPage == 'BmsUseConserveMgmtRegiList'" class="h4">존안비밀관리기록부</h4>
      <h4 v-if="getFromPage == 'BmsUseTransMgmtRegiList'" class="h4">이관비밀관리기록부</h4>
      <h4 v-else class="h4">비밀문서 요청</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">전자 반출 요청서</h5>
          <div class="action-buttons">
            <v-btn class="button" :disabled="isChiefMe" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
            <!-- <v-btn class="button" text-color="black" @click="useManagerChoicePopupToggleFunc()">관리자지정</v-btn> -->
            <v-btn class="button" @click="useApprovalPopupToggleFunc()">{{ isChiefMe ? '승인' : '승인요청' }}</v-btn>
            <v-btn class="button" @click="moveToBmsUseTakeOutReqList()">닫기</v-btn>
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
          </colgroup>
          <tbody>
            <tr>
              <th>반출구분</th>
              <td>
                <v-radio-group v-model="selectedAsktype" hide-details="auto" inline>
                  <div v-for="(data,idx) in radioData" :key="idx">
                    <v-radio :label="data.view" color="indigo" :value="data.key"></v-radio>
                  </div>
                </v-radio-group>
              </td>
              <th>반출형태</th>
              <td>인쇄반출</td>            
            </tr>          
            <tr>
              <th><span class="required">제목</span></th>
              <td colspan="3">
                <v-text-field
                  v-model="insertUseTakeOutData.reqttl"
                  :rules="[rules.required, rules.counter]"
                  maxlength="110"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th><span class="required">요청반출기간</span></th>
              <td colspan="3">
                <div class="box-flex">
                  <v-text-field type="date" :min="today" v-model="insertUseTakeOutData.startdt" variant="outlined" hide-details="auto"></v-text-field>
                  ~
                  <v-text-field type="date" :min="insertUseTakeOutData.startdt" v-model="insertUseTakeOutData.enddt"  @update:modelValue="updateEndDt" variant="outlined" hide-details="auto"></v-text-field>
                </div>
              </td>           
            </tr>                    
            <tr>
              <th><span class="required">사유</span></th>
              <td colspan="3">
                <v-text-field
                  v-model="insertUseTakeOutData.reqcontents"
                  :rules="[rules.required, rules.reqcounter]"
                  maxlength="500"
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
          <div class="action-buttons">
            <v-btn class="button" :disabled="isChiefMe" @click="usePathPopupToggleFunc()">결재선지정</v-btn>
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 10%;">구분</th>
              <th style="width: 15%;">부서</th>
              <th style="width: 20%;">직위/성명</th>
              <th style="width: 20%;">의견</th>
              <th style="width: 10%;">서명</th>
              <th style="width: 25%;">처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pathData, idx in sortedPathList" :key="idx">
              <td>{{ pathData.apprtemp }}</td>
              <td>{{ pathData.apprdeptname }}</td>
              <td class="text-left">{{ pathData.apprnametemp }}</td>
              <td class="text-left">{{ pathData.appropinion }}</td>
              <td>{{ pathData.electsign }}</td>
              <td>{{ pathData.apprgubuntemp }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 파일반출 담당자정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">파일반출 담당자정보</h6>
          <div class="action-buttons">
            <!-- <v-btn class="button" @click="useManagerChoicePopupToggleFunc()">관리자지정</v-btn> -->
          </div>
        </div>

        <v-table class="table-type-04">
          <thead>
            <tr>
              <th style="width: 10%;">구분</th>
              <th style="width: 15%;">부서</th>
              <th style="width: 20%;">직위/성명</th>
              <th style="width: 20%;">의견</th>
              <th style="width: 10%;">서명</th>
              <th style="width: 25%;">처리결과</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="manager, idx in insertUseTakeOutData.managertemp" :key="idx">
              <td>{{ manager.apprtemp }}</td>
              <td>{{ manager.apprdeptname }}</td>
              <td class="text-left">{{ manager.apprnametemp }}</td>
              <td class="text-left">{{ manager.appropinion }}</td>
              <td>{{ manager.electsign }}</td>
              <td>{{ manager.apprgubuntemp }}</td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 요청내용 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>

        <v-data-table-server
          :headers="getDctMgmtRegiHeaders"
          :items="getDctMgmtRegiList"
          :items-length="getDctMgmtRegiList.length"
          item-value="mgmtid"
          :expanded="expandedItems"
          class="table-type-04"
        >
        <template v-slot:item.secttl="{ item }">
          <div :class="{ 'text-left': item }">
            {{ item.raw.secttl }}
          </div>
        </template>
        <template v-slot:expanded-row="{ columns, item }">
          <tr>
            <td :colspan="columns.length">
              <div v-for="file in item.raw.attachfileList" :key="file.id">
                <ul class="attach-list ml-16" v-if="file.sfilename != undefined">
                  <li>
                    <span class="branch-down"></span>
                    <v-checkbox v-model="file.isSelected" hide-details class="flex-0-1 density" style="margin-right: 10px;"></v-checkbox>
                    <v-chip  v-if="file.gubun == 'DFT01'" label color="#9C9AD9" class="density" text="기안문" variant="flat"></v-chip>
                    <v-chip  v-else-if="file.gubun == 'DFT02'" label color="#30A54A" class="density" text="붙임" variant="flat"></v-chip>
                    <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                    <span>{{ file.sfilename }}</span>
                  </li>
                </ul>
              </div>
            </td>
          </tr>
        </template>
        <template v-slot:item.regirecvtype="{ item }">
          <span v-if="item.raw.regirecvtype == '1'">전자</span>
          <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
        </template>
        <template v-slot:bottom></template>
        </v-data-table-server>
      </section>
    </div>
  </div>

  <v-dialog v-model="bVisibleUseManagerChoicePopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>관리자지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUseManagerChoicePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UseManagerChoicePopup
        v-if="bVisibleUseManagerChoicePopup"
        :args="useManagerChoicePopupArgs"
        :toggleFunc="useManagerChoicePopupToggleFunc"
        :returnFunc="useManagerChoicePopupReturnFunc"
      ></UseManagerChoicePopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="bVisibleUsePathPopup" style="width: 850px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUsePathPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UsePathPopup
        v-if="bVisibleUsePathPopup"
        :args="usePathPopupArgs"
        :toggleFunc="usePathPopupToggleFunc"
        :returnFunc="usePathPopupReturnFunc"
      ></UsePathPopup>
    </v-card>
  </v-dialog>


    <v-dialog v-model="bVisibleUseApprovalPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>승인요청</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleUseApprovalPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <UseApprovalPopup
        v-if="bVisibleUseApprovalPopup"
        :args="useApprovalPopupArgs"
        :toggleFunc="useApprovalPopupToggleFunc"
        :insertFunc="insertUseTakeOut"
        :addManagerPath="addManagerPath"
      ></UseApprovalPopup>
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
import UseManagerChoicePopup from "./UseManagerChoicePopup.vue";
import { storeToRefs } from 'pinia';
import UsePathPopup from "./UsePathPopup.vue";
import UseApprovalPopup from "./UseApprovalPopup.vue";
import { getValueByKey, getExtension, getFileIcon } from "@/utils/Utils.js";
import { API } from '@/api'; 
import { useLoginStore } from '/src/store/Login';
import { findChiefId, findChiefInfo, isChiefMe, insertUsePathList, getDeptInfo, reqContentDeptid, reqContentDeptname, getDefaultPathUsers, checkPathList } from "@/utils/OrganizationInfo.js"; 

const name = ref('BmsUseTakeOutReq')
/* ======================== 관리자지정 팝업 ======================== */
const bVisibleUseManagerChoicePopup = ref(false)
const useManagerChoicePopupReuturn = ref({})
const useManagerChoicePopupArgs = ref({})
const useManagerChoicePopupToggleFunc = ref(() => {  
  const mangerList = [...insertUseTakeOutData.value.managertemp];
  const modifiedManagerList = mangerList.map(item => {
    return {
      orgid: item.apprdeptid,
      userid: item.apprid,
      username: item.apprname,
      orgname: item.apprdeptname,
      grade: item.apprdutyname,
      askTemp: item.apprtemp,
      asktype: item.apprtype,
      identifier: `${item.apprdeptid}-${item.apprid}`
    };
  });
  useManagerChoicePopupArgs.value = modifiedManagerList; 
  bVisibleUseManagerChoicePopup.value = !bVisibleUseManagerChoicePopup.value;
})
const useManagerChoicePopupReturnFunc = ref((retValue) => {
  useManagerChoicePopupToggleFunc.value()
  if(retValue != undefined && retValue != ""){
    insertUseTakeOutData.value.managertemp = [];
    const manager = {};
    manager.apprreqseq = 0;
    manager.apprtemp = retValue.askTemp;
    manager.apprtype = retValue.asktype;
    manager.apprgubun = "2";
    manager.apprstatus = "APP10";
    manager.apprid = retValue.userid;
    manager.apprname = retValue.username;
    manager.apprdeptid = retValue.orgid;
    manager.apprdeptname = retValue.orgname;
    manager.apprdutyname = retValue.grade;
    manager.apprnametemp = retValue.grade+"/"+retValue.username;
    manager.rgstid = getUserLoginData.value.userid;
    manager.rgstname = getUserLoginData.value.username;
    insertUseTakeOutData.value.managertemp.push(manager);
  }
})
/* ============================================================ */

/* ======================== 결재선지정 팝업 ======================== */
const bVisibleUsePathPopup = ref(false)
const usePathPopupReuturn = ref({})
const usePathPopupArgs = ref({})
const usePathPopupToggleFunc = ref(() => { 
  const pathList = [...insertUseTakeOutData.value.pathList];
  const modifiedPathList = pathList.slice(1).map(item => {
    return {
      orgid: item.apprdeptid,
      userid: item.apprid,
      username: item.apprname,
      orgname: item.apprdeptname,
      grade: item.apprdutyname,
      askTemp: item.apprtemp,
      asktype: item.apprtype,
      identifier: `${item.apprdeptid}-${item.apprid}`
    };
  });
  usePathPopupArgs.value.path = modifiedPathList;
  usePathPopupArgs.value.managerids = [];
  usePathPopupArgs.value.initialDeptid = reqContentDeptid.value;
  usePathPopupArgs.value.initialDeptname = reqContentDeptname.value;
  bVisibleUsePathPopup.value = !bVisibleUsePathPopup.value;
})
const usePathPopupReturnFunc = ref((retValue) => {
  usePathPopupToggleFunc.value();

  if (retValue !== "") {
    const filteredList = insertUseTakeOutData.value.pathList.slice(1);
    const existingIdentifiers = new Set(filteredList.map(path => path.identifier));
    const deletedPath = Array.from(existingIdentifiers).filter(identifier => !retValue.some(item => item.identifier === identifier));
    if(deletedPath.length > 0){
      deletedPath.forEach(identifier=>{
        const indexToRemove = insertUseTakeOutData.value.pathList.findIndex(path => path.identifier === identifier);
        if (indexToRemove !== -1) {
          insertUseTakeOutData.value.pathList.splice(indexToRemove, 1);
        }
      })
    }

    for(let i = 0; i < retValue.length; i++){
      const path = {};
      path.apprreqseq = i+2;
      path.apprtemp = retValue[i].askTemp;
      path.apprtype = retValue[i].asktype;
      path.apprgubun = "2";
      path.apprstatus = "APP10";
      path.apprid = retValue[i].userid;
      path.apprname = retValue[i].username;
      path.apprdeptid = retValue[i].orgid;
      path.apprdeptname = retValue[i].orgname;
      path.apprdutyname = retValue[i].grade;
      path.apprnametemp = retValue[i].grade + "/" + retValue[i].username;
      path.rgstid = getUserLoginData.value.userid;
      path.rgstname = getUserLoginData.value.username;
      path.identifier = retValue[i].identifier;

      if (!existingIdentifiers.has(path.identifier)) {
        insertUseTakeOutData.value.pathList.push(path);
      }
    }

    const retValueIdentifiers = retValue.map(item => item.identifier);
    insertUseTakeOutData.value.pathList.sort((a, b) => {
      const indexA = retValueIdentifiers.indexOf(a.identifier);
      const indexB = retValueIdentifiers.indexOf(b.identifier);
      return indexA - indexB;
    });
    sortedPathList.value;
  }
});

const sortedPathList = computed(() => {
  const requests = [];
  const reviewers = [];
  const approvers = [];

  // '요청', '검토', '승인'에 따라 재정렬
  insertUseTakeOutData.value.pathList.forEach((path) => {
    switch (path.apprtemp) {
      case '요청':
        requests.push(path);
        break;
      case '검토':
        reviewers.push(path);
        break;
      case '승인':
        approvers.push(path);
        break;
      default:
        approvers.push(path);
        break;
    }
  });

  insertUseTakeOutData.value.pathList = requests.concat(reviewers, approvers);
  updateApprreqSeq();

  return insertUseTakeOutData.value.pathList
});

const updateApprreqSeq = () => {
  insertUseTakeOutData.value.pathList.forEach((path, index) => {
    path.apprreqseq = index + 1;
  });
};

/* ============================================================ */

/* ======================== 승인요청 팝업 ======================== */
const bVisibleUseApprovalPopup = ref(false)
const useApprovalPopupReuturn = ref({})
const useApprovalPopupArgs = ref({})
const useApprovalPopupToggleFunc = ref(() => { 

  if(!insertUseTakeOutData.value.reqttl){
    alert("제목을 작성해주세요")
    return
  }

  if(!insertUseTakeOutData.value.reqcontents){
    alert("사유를 작성해주세요")
    return
  }

  if(!insertUseTakeOutData.value.startdt || !insertUseTakeOutData.value.enddt){
    alert("요청반출기간을 선택해주세요.")
    return
  }

  if(insertUseTakeOutData.value.pathList.length == 1 && isChiefMe.value == false){
    alert("결재선지정을 해주세요.")
    return
  }

  if(!checkPathList(insertUseTakeOutData.value) && isChiefMe.value == false){
    alert("요청내용에 속한 부서의 비밀관리자/부서장 중 승인자가 존재해야 합니다. \n비밀관리자 또는 부서장을 추가하여주십시오.")
    return;
  }

  if(insertUseTakeOutData.value.managertemp.length >1 ){
    alert("파일반출담당자를 1명만 지정해주세요.")
    return
  }

  if(isAllFilesNotChecked() == false){
    insertSelectedFiles();
  }else{
    return
  }

  if(bVisibleUseApprovalPopup.value == false && isChiefMe.value == true){
    if(!confirm("1인결재를 진행합니다.")){
      return
    }
  }

  if(isChiefMe.value == true){
    insertUseTakeOutData.value.reqstatus = '6';
    insertUseTakeOutData.value.grantflag = 'Y';
  }

  useApprovalPopupArgs.value = insertUseTakeOutData.value;
  useApprovalPopupArgs.value.addManager = 'Y';
  bVisibleUseApprovalPopup.value = !bVisibleUseApprovalPopup.value;
});

const addManagerPath = () => {
  insertUseTakeOutData.value.managertemp.map(data=>{
    const { appropinion, electsign, apprgubuntemp, ...others } = data;
    insertUseTakeOutData.value.pathList.push(others)
  });
  updateApprreqSeq();
};
/* ============================================================ */


const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const params = ref({});
const query = ref({});

const today = ref("");
const isloading = ref(false)
const getFromPage = ref('')

// for insertUseTakeOut
const insertUseTakeOutData = ref({

  // 1) bms_dct_apprreq table
  apprreqid: "", // 결재요청 ID
  reqtype: "04", // 요청타입 (01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출, 06: 재분류, 07: 파기, 08: 존안, 09: 이관, 10: 이관대기, 11: 인관연기, 12: 내부이관, 13: 비전자, 14: 오프라인열람, 15: 인쇄용프린터, 16: 추가발송)
  reqttl: "[전자반출요청서] 반출을 요청합니다.", // 요청제목
  reqcontents: "", // 요청사유
  reqid: "", // 요청자 ID
  reqname: "", // 요청자명
  reqdeptid: "", // 요청자 부서ID
  reqdeptname: "", // 요청자 부서명
  reqstatus: "1", // 진행상태 (0: 임시저장, 1: 결재중, 2: 결재완료, 3: 최종처리, 4: 권한회수, 5: 요청서회수, 6: 1인결재)
  grantflag: "N", // 요청승인여부


  // 2) bms_dct_apprreq_path table
  pathList: [
    {
      apprreqseq: "", // 요청경로순번
      apprtemp: "", // apprtype 해당하는 viewing 값 임시 저장
      apprtype: "", // 결재유형 (1: 요청, 2: 검토, 3: 결재, 4: 1인결재)
      apprgubuntemp: "", // apprgubun 해당하는 viewing 값 임시 저장
      apprgubun: "", // 결재처리구분 (1: 승인, 2: 비승인)
      apprstatus: "", // 결재요청상태 (APP00: - , APP10: 결재대기, APP20: 결재완료, APP30: 1인결재)
      apprid: "", // 결재자 ID
      apprname: "", // 결재자명
      apprdeptid: "", // 결재자 부서ID
      apprdeptname: "", // 결재자 부서명
      apprdutyname: "", // 결재자 직위직급명
      apprnametemp: "", // 직위/직급에 해당하는 viewing 값 임시 저장
      appropinion: "", // 결재자의견
      electsign: "", // 전자서명
      rgstid: "", // 등록자 ID
      rgstname: "", // 등록자명
    }
  ],
  appropiniontemp: "", // pathList의 appropinion에 넣을 값 임시 저장

  // 파일반출담당자 정보 viewing 용도
  managertemp: [],

  // 요청반출기간 temp
  startdt: dayjs().format("YYYY-MM-DD"),
  enddt: "",

  // 3) bms_dct_apprreq_lend table
  lendList: [
    {
      apprreqid: "", // 요청ID
      mgmtid: "", // 비밀관리 ID
      lendgubun: "", // 반출구분 (1: 지출, 2: 대출)
      reqlenddt: "", // 요청반출일
      reqbackdt: "", // 요청반입예정일
      grantflag: "", // 승인여부
      lendtype: "", // 반출형태 (null: 비전자, 1: 인쇄반출, 2: 파일반출)
      lendcharge: "", // 파일반출담당자
      carrypcid: "", // 반출PC KEY
      isorgdoc: "", // 원본 반출 여부

      // 4) bms_dct_apprreq_lend_file table
      fileList: [
        {
          apprreqid: "", // 요청ID
          mgmtid: "", // 비밀관리 ID
          fleorder: "", // 첨부순서
          gubun: "", // 구분
          fleid: "", // 첨부ID
          sfilename: "", // 파일 고유이름
          flettl: "", // 첨부명
          flesize: "", // 첨부크기
          grantflag: "", // 승인여부
          prtcnt: "", // 인쇄부수
        }
      ],
    }
  ],

  docid: [],
  dutyname: "",

  requester: [], // 요청자 정보
  draftmanagers: [], // 비밀담당자 정보
  draftmanagersheads: [], // 비밀담당자의 부서장 정보 
})

const rules = {
  required: value => !!value || '필수 입력값입니다.',
  counter: value => value.length < 110 || '최대 110자 까지 입력하실 수 있습니다.',
  reqcounter: value => value.length < 500 || '최대 500자 까지 입력하실 수 있습니다.',
};

// for getDctMgmtRegi
const getDctMgmtRegiList = ref([])
const getDctMgmtRegiDefCondi = {}
const getDctMgmtRegiCondi = ref({ ...getDctMgmtRegiDefCondi })

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  getFromPage.value = query.value.frompage;
  const mgmtidArray = query.value.mgmtid.replace(/'/g, '').split(','); 
  getDctMgmtRegiCondi.value.mgmtid = mgmtidArray;
  insertUseTakeOutData.value.lendList = mgmtidArray.map(mgmtid => ({
    apprreqid: "", 
    mgmtid: mgmtid, 
    lendgubun: "",
    reqlenddt: "",
    reqbackdt: "",
    grantflag: "N",
    lendtype: "1",
    lendcharge: "",
    carrypcid: "",
    isorgdoc: "N",
  }));
})

// 구분 라디오 버튼
const selectedAsktype = ref("2");
const radioData = [
  {view: "대출(내부)", key:"2"},
  {view: "지출(외부)", key:"1"},
]

onMounted(async () => {
  await getDctMgmtRegiPrint();
  await selectFindManager();
  await getFileManager();
  insertUseTakeOutData.value.reqid = getUserLoginData.value.userid;
  insertUseTakeOutData.value.reqname = getUserLoginData.value.username;
  insertUseTakeOutData.value.reqdeptid = getUserLoginData.value.deptid;
  insertUseTakeOutData.value.reqdeptname = getUserLoginData.value.deptname;
  today.value = dayjs().format('YYYY-MM-DD');
  insertUseTakeOutData.value.dutyname = getUserLoginData.value.grade;
  insertUseTakeOutData.value.docid = getDctMgmtRegiList.value.map(item => item.docid);
  await insertUsePathList(insertUseTakeOutData.value, getDctMgmtRegiList.value, getUserLoginData.value);
  await getDeptInfo(insertUseTakeOutData.value);
  await getDefaultPathUsers(insertUseTakeOutData.value);
})

watchEffect(() => {
})

const updateEndDt = () => {
  if(minReadAuthExpiredt.value < insertUseTakeOutData.value.enddt){
      alert(`반출만료일은 열람만료일(${minReadAuthExpiredt.value}) 보다 클 수 없습니다.`);
      insertUseTakeOutData.value.enddt = "";
  }
};

const getDctMgmtRegiHeaders = [
  { key: "mgmtno", title: "관리번호", width: "150px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "부서", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "readauthexpiredt", title: "열람만료일", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "구분", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];


const expandedItems = ref([]);
const minReadAuthExpiredt = ref('');

// eslint-disable-next-line
const getDctMgmtRegiPrint = async () => {
  getDctMgmtRegiCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.getDctMgmtRegiPrint(getDctMgmtRegiCondi.value, urlPaths.value);
    let minDate = dayjs(response.data[0].readauthexpiredt);
    response.data.forEach(item => {
      const expiredt = dayjs(item.readauthexpiredt);
      if (expiredt.isBefore(minDate)) {
        minDate = expiredt;
      }
      item.attachfileList = item.attachfileList
        .filter(file => file.gubun === 'DFT01')
        .concat(
          item.attachfileList
            .filter(file => file.gubun === 'DFT02')
            .sort((a, b) => a.fleorder - b.fleorder)
        )
        .map(file => ({
          ...file,
          isSelected: true
        }));

    });
    getDctMgmtRegiList.value = response.data;
    minReadAuthExpiredt.value = minDate.format('YYYY-MM-DD');
    expandedItems.value = getDctMgmtRegiList.value.map(item => item.mgmtid);
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectFindManagerList = ref([]);
const selectFindManagerCondi = ref({
  vgroupid: ['6010']
}) // 6010 : 파일반출담당자

const selectFindManager = async () => {
  try {
    const response = await API.comAPI.selectFindManager(selectFindManagerCondi.value, urlPaths.value);
    selectFindManagerList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const getFileManager = async() => {
  insertUseTakeOutData.value.managertemp = [];
  selectFindManagerList.value.forEach(data=>{
    const manager = {
      apprreqseq: 0,
      apprtemp: "승인",
      apprtype: "3",
      apprgubun: "2",
      apprstatus: "APP10",
      apprid: "", 
      apprname: "", 
      apprdeptid: "",
      apprdeptname: "",
      apprdutyname: "",
      apprnametemp: "",
      appropinion: "",
      electsign: "",
      apprgubuntemp: "",
      rgstid: getUserLoginData.value.userid,
      rgstname: getUserLoginData.value.username,
    }
    manager.apprid = data.userid;
    manager.apprname = data.username;
    manager.apprdeptid = data.deptid;
    manager.apprdeptname = data.deptname;
    manager.apprdutyname = data.grade;
    manager.apprnametemp = data.grade+"/"+data.username;
    insertUseTakeOutData.value.managertemp.push(manager);
  })
}

const isAllFilesNotChecked = () => {
  let anyFileSelected = false;

  getDctMgmtRegiList.value.forEach(data => {
    const noFileSelected  = data.attachfileList.every(fileData => fileData.isSelected === false);
    if (noFileSelected ) {
      anyFileSelected = true;
      return; 
    }
  });

  if (anyFileSelected) {
    alert("한개 이상의 파일을 선택해주세요.");
  }
  return anyFileSelected;
};

const insertSelectedFiles = () => {
  insertUseTakeOutData.value.lendList.forEach(item => {
    const targetData = getDctMgmtRegiList.value.find(data => data.mgmtid === item.mgmtid);
    if (targetData) {
      const selectedFiles = targetData.attachfileList.filter(fileData => fileData.isSelected);
      item.fileList = selectedFiles.map((fileData, idx) => ({
        apprreqid: "",
        mgmtid: targetData.mgmtid,
        fleorder: idx++,
        gubun: fileData.gubun,
        fleid: fileData.fleid,
        sfilename: fileData.sfilename,
        flettl: fileData.flettl,
        flesize: fileData.flesize,
        grantflag: 'N',
        prtcnt: 0
      }));
    }
  });
};

const updateLendGubun = () => {
  insertUseTakeOutData.value.lendList.forEach((lend) => {
    lend.lendgubun = selectedAsktype.value;
    if (selectedAsktype.value == '2') {
      insertUseTakeOutData.value.reqtype = '04';
    } else if (selectedAsktype.value == '1') {
      insertUseTakeOutData.value.reqtype = '05';
    }
  });
}

const insertUseTakeOut = async (param) => {
  isloading.value = true;
  updateLendGubun();
  param.pathList[0].appropinion = param.appropiniontemp;

  param.lendList.forEach(lend => {
    lend.lendcharge = param.managertemp[0].apprid;
    lend.reqlenddt = param.startdt;
    lend.reqbackdt = param.enddt;
    
    if(isChiefMe.value == true){
      lend.grantlenddt = lend.reqlenddt.replace(/-/g, '')+'000000';
      lend.grantbackdt = lend.reqbackdt.replace(/-/g, '')+'235959';
      lend.grantflag = 'Y';
    }

  });

  API.useAPI
    .insertUseTakeOut(param, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("정상 등록되었습니다");
        moveToBmsUsedraftlist();
      }
    })
    .catch(error => {
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });
}

const moveToBmsUseTakeOutReqList = () => {
  router.push({
    name: getFromPage.value ||  "BmsUseTakeOutReqList",
  });
};

const moveToBmsUsedraftlist = () => {
  let arr = ['비밀관리', '비밀문서사용처리', '기안한 요청서'];

  breadcrumbs.value.activeLink = arr;
  
  router.push({
    name: "BmsUsedraftlist",
  });
};
</script>


<style scoped>
.density {
  padding: 0px 11px;
  height: 21px;
}
</style>