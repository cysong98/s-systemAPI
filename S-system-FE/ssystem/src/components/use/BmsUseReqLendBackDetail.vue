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
          <h5 class="h5">반납요청서</h5>
          <div class="action-buttons" >
            <v-btn class="button" v-if="selectUseReqViewDetailData.lendcharge == getUserLoginData.userid" @click="useReqStatusSave('9')">반납실행</v-btn>
            <v-btn class="button" v-else @click="useReqStatusSave('8')">요청</v-btn>
            <v-btn class="button" @click="moveToBmsUsereqlist()">취소</v-btn>
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
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '4'">* 권한이 회수되었습니다.</span>
        <span style="color:red; margin: 10px;" v-if="selectUseReqViewDetailData.reqstatus == '5'">* 요청서가 회수되었습니다.</span>
        <div style="display: flex; margin: 10px; color: darkblue; font-weight: 500;" v-if="selectUseReqViewDetailData.lendcharge == getUserLoginData.userid">
          <v-checkbox  v-model="chk" hide-details class="flex-0-1" style="margin-right: 10px;" density="comfort" />
          <span>인쇄반출된 문서의 파쇄를 완료하였습니다.</span>
        </div>
      </section>

      <!-- 요청내용 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">요청내용</h6>
        </div>

        <v-table class="table-type-02">
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
                <td>{{ listData.regirecvtype == 1 ? '전자' : '비전자' }}</td>
              </tr>
              <tr>
                <td colspan="6">
                  <ul class="attach-list ml-16" v-if="listData.fileInfoList">
                    <li v-for="(file, i) in listData.fileInfoList" :key="i">
                      <span class="branch-down"></span>
                      <v-chip  v-if="file.gubun == 'DFT01'" label color="#9C9AD9" class="density" text="기안문" variant="flat"></v-chip>
                      <v-chip  v-else-if="file.gubun == 'DFT02'" label color="#30A54A" class="density" text="붙임" variant="flat"></v-chip>
                      <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                      <span>{{ file.sfilename }}</span>
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
  import { getValueByKey, getExtension, getFileIcon } from "@/utils/Utils.js";
  import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
  import _ from 'lodash';
  import BmsUseConfirmPopup from "./BmsUseConfirmPopup.vue";
  import { API } from '@/api'; 
  import { useLoginStore } from '/src/store/Login';
  import { storeToRefs } from 'pinia';
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = storeToRefs(loginStore)
  
  const name = ref('BmsUseReqLendBackDetail')
/* ======================== 승인/반려 팝업 ======================== */
const bVisibleConfirmPopup = ref(false)
const bmsUseConfirmPopupReuturn = ref({})
const bmsUseConfirmPopupArgs = ref({type:""})
const bmsUseConfirmPopupToggleFunc = ref((val) => { 
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

const chk = ref(false);

// 결재선정보
const selectUseReqViewDetailPathList = ref([])
const fileManagerInfo = ref([])
const staticColumnsSelectUseReqPathList = [
  { key: "apprtypenm", title: "구분", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprdeptname", title: "부서", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "positionnm", title: "직위/성명", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "appropinion", title: "의견", width:"100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "electsign", title: "서명", width:"50px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
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
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });    
};


//상태변경
const useReqStatusSave = async (status) => {

  if(status == '9' && chk.value == false){
    alert("파쇄 여부를 체크해주세요.");
    return;
  }

  let message = '';
  if (status == '8') {
    message = "반납요청하시겠습니까?";
  } else if (status == '9') {
    message = "실행하시겠습니까?";
  }

  if (!confirm(message)) {
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
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });
};


// Move Function
const moveToBmsUsereqlist = (page) => {
  router.push({
    name: 'BmsUsecompletelist'
    });
};  
  </script>