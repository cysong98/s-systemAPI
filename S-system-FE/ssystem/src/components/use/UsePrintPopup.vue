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
    <div class="content">
      <div class="row">
        <div class="col">
          <v-table class="table-type-04">

            <!-- 인쇄/파쇄 실행 헤더 -->
            <thead v-if="isLend != 'Y'">
              <tr>
                <th>관리번호</th>
                <th>제목</th>

                <template v-if="isPrint == 'Y'">
                  <th>승인파쇄예정일</th>
                  <th>승인인쇄부수</th>
                  <th>인쇄일</th>
                </template>

                <template v-else>
                  <th>인쇄실행일</th>
                  <th>인쇄실행부수</th>
                  <th>승인파쇄예정일</th>
                  <th>파쇄일</th>
                  <th>파쇄부수</th>
                </template>
              </tr>
            </thead>

            <!-- 인쇄반출 실행 헤더 -->
            <thead v-else>
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
              <template v-for="(listData, idx) in reqDocInfo" :key="idx">

                <!-- 인쇄/파쇄 실행 본문 -->
                <tr v-if="isLend != 'Y'">
                  <td>{{ listData.mgmtno }}</td>
                  <td style="text-align: left;">{{ listData.secttl }}</td>

                  <!-- 인쇄 실행 -->
                  <template v-if="isPrint == 'Y'">
                    <td>{{ listData.grantdestructdt }}</td>
                    <td>{{ listData.grantprtcnt }}</td>
                    <td>
                      <v-text-field readonly v-model="listData.prtdt" variant="outlined" hide-details="auto" />
                    </td>
                  </template>

                  <!-- 파쇄 실행 -->
                  <template v-else>
                    <td>{{ listData.prtdt }}</td>
                    <td>{{ listData.prtcnt }}</td>
                    <td>{{ listData.grantdestructdt }}</td>
                    <td>
                      <v-text-field readonly v-model="listData.destructdt" variant="outlined" hide-details="auto" />
                    </td>
                    <td>
                      <v-text-field :rules="[rules.prtCnt]" v-model="listData.destructcnt" @update:modelValue="handleDestructCnt(idx)" variant="outlined" hide-details="auto"></v-text-field>
                    </td>
                  </template>
                </tr>

                <!-- 인쇄반출 실행 본문 -->
                <tr v-else>
                  <td>{{ listData.mgmtno }}</td>
                  <td class="text-left">{{ listData.secttl }}</td>
                  <td>{{ listData.authorname }}</td>
                  <td>{{ listData.authordutyname }}</td>
                  <td>{{ listData.deptname }}</td>
                  <td>{{ listData.regirecvtype == "1" ? "전자" : "비전자" }}</td>
                </tr>

                <tr>
                  <td colspan="8">
                    <ul class="attach-list ml-16">
                      <li v-for="(file, i) in listData.fileInfoList" :key="i">
                        <span class="branch-down"></span>
                        <v-chip v-if="file.gubun == 'DFT01'" label color="#9C9AD9" class="density" text="기안문" variant="flat"></v-chip>
                        <v-chip v-else-if="file.gubun == 'DFT02'" label color="#30A54A" class="density" text="붙임" variant="flat"></v-chip>
                        <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                        <span v-if="isPrint == 'Y' || isLend == 'Y'" @click="toggleViewerPopup(file, getUserLoginData.userid, listData)" style="font-weight: 600; cursor: pointer;">{{ file.sfilename }}</span>
                        <span v-else >{{ file.sfilename }}</span>
                        <span v-if="isPrint == 'Y'" style="margin-left: 10px;">( 인쇄부수 : <input type="text" style="width: 15px;" readonly v-model="file.prtcnt"/> )</span>
                      </li>
                    </ul>
                  </td>
                </tr>

              </template>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn v-if="isPrint == 'Y' && allPrinted == 'N'" variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if="isPrint != 'Y'" variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if="isLend != 'Y' && isPrint == 'Y'" :disabled="allNotPrinted == 'Y'" variant="flat" color="indigo-darken-3" rounded="xl" @click="useReqStatusSave()">인쇄완료</v-btn>
      <v-btn v-if="isLend != 'Y' && isPrint == 'N'" variant="flat" color="indigo-darken-3" rounded="xl" @click="useReqStatusSave()">파쇄실행</v-btn>
      <v-btn v-if="isLend == 'Y'" variant="flat" color="indigo-darken-3" rounded="xl" @click="useReqStatusSave()">반출실행</v-btn>
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

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { getValueByKey, getExtension, getFileIcon, viewDocument, url } from "@/utils/Utils.js";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('UsePrintPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
})
const urlPaths =  ref('')
const route = useRoute()
const router = useRouter()
const args = ref({})
const isPrint = ref("")
const isLend = ref("")
const reqtype = ref("")
const reqid = ref("")
const apprreqid = ref("") 
const today = ref("")
const isloading = ref(false)
const rules = {
  prtCnt: value => /^[1-9][0-9]{0,2}$/.test(value) || '3자리 이내의 정수만 입력하실 수 있습니다.'
};

onBeforeMount(() => {
})

onMounted(async () => {
  isPrint.value = props.args.isPrint;
  isLend.value = props.args.isLend;
  reqtype.value = props.args.reqtype;
  reqid.value = props.args.reqid;
  apprreqid.value = props.args.apprreqid;
  await selectUseReqViewDetail();
  today.value = dayjs().format('YYYY-MM-DD');
})

// 조회
const selectUseReqViewDetailCondi = ref({
  apprreqid: "",
  reqtype: "",
  reqstatus: "",
  type:"",
});
const reqDocInfo = ref([]);
const selectUseReqViewDetail = async () => {
  selectUseReqViewDetailCondi.value.userid = getUserLoginData.value.userid;
  selectUseReqViewDetailCondi.value.apprreqid = props.args.apprreqid;
  selectUseReqViewDetailCondi.value.reqtype = props.args.reqtype;
  try {
    const response = await API.useAPI.selectUseReqViewDetail(selectUseReqViewDetailCondi.value, urlPaths.value);
    response.data.mapList = response.data.mapList.map(item => {
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

    reqDocInfo.value = response.data.mapList.map(item => {
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
    console.log(error);
    alert("Server Error");
  }
};


// 실행
const useReqStatusSave = async () => {

  if(isLend.value != 'Y'){
    if (isPrint.value === 'Y' && allPrinted.value === 'N') {
      if(!confirm("인쇄가 미완료된 건이 존재합니다. 인쇄완료하시겠습니까?")){
        return;
      }
    } else if (isPrint.value === 'N' && reqDocInfo.value.some(item => item.destructcnt === 0)) {
      alert("파쇄부수를 입력해주세요");
      return;
    }
  }

  const updateUseReqViewAppStatusBIAData = {};
  if(isLend.value != 'Y'){
    updateUseReqViewAppStatusBIAData.reqstatus = '3'; // 인쇄실행
    if(isPrint.value === 'Y'){
      updateUseReqViewAppStatusBIAData.isprintreq = "printReq";
      updMaxPrtcnt();
    }else{
      updateUseReqViewAppStatusBIAData.isprintreq = "destructReq";
    }
  }else{
    updateUseReqViewAppStatusBIAData.reqstatus = '7'; // 반출실행
    updateUseReqViewAppStatusBIAData.savetype = 'lendcomplete';
  }
  isloading.value = true;
  updateUseReqViewAppStatusBIAData.reqtype = reqtype.value; 
  updateUseReqViewAppStatusBIAData.apprid = getUserLoginData.value.userid;
  updateUseReqViewAppStatusBIAData.reqid = reqid.value; 
  updateUseReqViewAppStatusBIAData.apprreqid = apprreqid.value;
  updateUseReqViewAppStatusBIAData.prtlist = reqDocInfo.value;
  updateUseReqViewAppStatusBIAData.maplist = reqDocInfo.value;

  API.useAPI
  .updateUseReqViewAppStatusBIA(updateUseReqViewAppStatusBIAData, urlPaths.value)
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

const setDate = () => {
  reqDocInfo.value.forEach(data=>{
    if(isPrint.value == 'Y'){
      data.prtdt = dayjs().format('YYYY-MM-DD')
    }else if(isPrint.value == 'N'){
      data.destructdt =  dayjs().format('YYYY-MM-DD')
      data.destructcnt = data.prtcnt
    }
  });
};

const allPrinted = ref('N');
const isAllPrinted = () => {
  allPrinted.value = reqDocInfo.value.every(item =>
    item.fileInfoList.every(file => file.prtcnt == item.grantprtcnt)
  ) ? 'Y' : 'N';
};

const allNotPrinted = ref('N');
const isAllNotPrinted = () => {
  allNotPrinted.value = reqDocInfo.value.every(item =>
    item.fileInfoList.every(file => file.prtcnt == 0)
  ) ? 'Y' : 'N';
};

watch(() => reqDocInfo.value, () => {
  setDate();
  isAllPrinted();
  isAllNotPrinted();
});

const updMaxPrtcnt = () => {
  reqDocInfo.value.forEach(item => {
    const maxPrtcnt = item.fileInfoList.reduce((max, file) => {
      return Math.max(max, file.prtcnt || 0);
    }, 0);
    item.prtcnt = maxPrtcnt;
  });
};

const handleDestructCnt = (idx) => {
  const prtcnt = parseInt(reqDocInfo.value[idx].prtcnt);
  const destructcnt = parseInt(reqDocInfo.value[idx].destructcnt);
  if (!/^[1-9][0-9]{0,2}$/.test(reqDocInfo.value[idx].destructcnt)) {
    reqDocInfo.value[idx].destructcnt = '';
    return;
  }
  if(prtcnt < destructcnt){
    alert("파쇄부수는 인쇄실행부수보다 클 수 없습니다.");
    reqDocInfo.value[idx].destructcnt = prtcnt;
  }else if(prtcnt > destructcnt){
    alert("인쇄실행부수보다 파쇄부수가 적습니다.\n파쇄부수 확인 후 다시 파쇄실행하여주십시오.");
    reqDocInfo.value[idx].destructcnt = prtcnt;
  }
};


// 인쇄부수 확인
const updatePrtCntCondi = ref({
  apprreqid: "",
  mgmtid: "",
  gubun: "",
  fleorder: 0
});
const checkPrtCntResult = ref({})
const checkPrtCnt = async () => {
  try {
    const response = await API.useAPI.checkPrtCnt({...updatePrtCntCondi.value}, urlPaths.value);
    checkPrtCntResult.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 인쇄부수 업데이트
const updatePrtCnt = async () => {
  API.useAPI
  .updatePrtCnt(updatePrtCntCondi.value, urlPaths.value)
  .then(response => {
    if (response.status == 200) {
    }
  })
  .catch(error => {
    console.log(error);
    alert("Server Error");
  });
};

const moveToBmsUsereqlist = () => {
  router.push({
    name: 'BmsUsecompletelist'
  });
};

const closePopUp = () => {
  props.toggleFunc();
};

const confirmPopUp = () => {
  props.returnFunc();
};


// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid, row) => {
  if (isPrint.value === 'Y') {
    updatePrtCntCondi.value.apprreqid = apprreqid.value;
    updatePrtCntCondi.value.mgmtid = row.mgmtid;
    updatePrtCntCondi.value.gubun = file.gubun;
    updatePrtCntCondi.value.fleorder = file.fleorder;
    await checkPrtCnt();
    if (parseInt(checkPrtCntResult.value.prtcnt) < parseInt(row.grantprtcnt)) {
      await updatePrtCnt();
      await selectUseReqViewDetail();
      await openViewerPopup(file, userid);
    } else {
      alert("승인인쇄부수를 초과하실 수 없습니다.");
    }
  } else {
    await openViewerPopup(file, userid);
  }
};

const openViewerPopup = async (file, userid) => {
  try {
    docloading.value = true;
    visibleViewerPopup.value = !visibleViewerPopup.value;
    await viewDocument(file, userid, 'N,N', '1');
  } finally {
    document.querySelector('iframe').addEventListener('load', function() {
      docloading.value = false;
    });
  }
};
</script>

<style scoped>
.density {
  padding: 0px 11px;
  height: 21px;
}
</style>