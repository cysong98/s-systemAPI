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
    <!-- {{props}} -->
    <!-- {{getUserLoginData}} -->
    <!-- {{props.args.docid}} -->
    <!-- {{updateCardBIAData}} -->
    <div class="content">
      <div class="row">
        <div class="col">
          <v-table class="table-type-04">
            <!-- 인쇄반출 실행 헤더 -->
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
                <!-- 인쇄반출 실행 본문 -->
                <tr>
                  <td>{{ props.args.mgmtno }}</td>
                  <td class="text-left">{{ props.args.secttl }}</td>
                  <td>{{ props.args.authorname }}</td>
                  <td>{{ props.args.authordutyname }}</td>
                  <td>{{ props.args.deptname }}</td>
                  <td>{{ props.args.regirecvtype == "1" ? "전자" : "비전자" }}</td><!--수정예정-->
                </tr>
                <tr v-if="reqDocInfo && reqDocInfo.length > 0" v-for="(file, idx) in reqDocInfo" :key="idx">
                  <td colspan="8">
                    <ul class="attach-list ml-16">
                      <li>
                        <span class="branch-down"></span>
                        <v-chip v-if="file.gubun == 'DFT01'" label color="#9C9AD9" density="comfort" text="본문" variant="flat"></v-chip>
                        <v-chip v-else-if="file.gubun == 'DFT02'" label color="#30A54A" density="comfort" text="붙임" variant="flat"></v-chip>
                        <v-icon :color="getFileIcon(getExtension(file.sfilename)).color">{{ getFileIcon(getExtension(file.sfilename)).icon }}</v-icon>
                        <span v-if="isPrint == 'Y' || isLend == 'Y'" @click="toggleViewerPopup(file, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ file.sfilename }}</span>
                        <span v-else >{{ file.sfilename }}</span>
                        <span style="margin-left: 10px;">( 인쇄부수 : <input type="text" style="width: 15px;" readonly v-model="file.prtCnt"/> )</span>
                      </li>
                    </ul>
                  </td>
                </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <!-- <v-btn v-if="isPrint != 'Y'" variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn> -->
      <v-btn v-if="isPrint != 'Y'" variant="flat" color="grey-lighten-3" rounded="xl" @click="confirmPopUp">닫기</v-btn>
      <v-btn v-if="isPrint != 'Y' && allPrinted == 'N'" :disabled="allNotPrinted == 'Y'" variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA()">인쇄완료</v-btn>
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

const getHistCardInfoList = ref([])
const updateCardBIAData = ref({});

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
  // await selectUseReqViewDetail();
  // await selectDctFile();
  await selectSendFileList();
  // await getDctOridoc();
  today.value = dayjs().format('YYYY-MM-DD');
  args.value = props.args;
  updateCardBIAData.value = args.value;
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
      if (response.data.mapList && response.data.mapList.length > 0) {
      response.data.mapList = response.data.mapList.map(item => {
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
    }
    reqDocInfo.value = response.data.mapList;
  } catch (error) {
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

const getDctRdocData = ref({});
const responseData = ref([]);
// 10) bms_dct_file table
const draftDocInputData = ref("");
const selectDctFile = async () => {
  try {
    const response = await API.dctAPI.selectDctFile({
      docid: props.args.docid
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


const updateCardBIA = async () => {
 
  if(allPrinted.value === 'N') {
    if(!confirm("인쇄가 미완료된 건이 존재합니다. 인쇄완료하시겠습니까?")){
      return;
    }
  }

  
  isloading.value = true;

  // 1) bms_dct_rdoc table
  updateCardBIAData.value.loginuserid = getUserLoginData.value.userid;
  updateCardBIAData.value.authorid = getUserLoginData.value.userid;
  updateCardBIAData.value.authorname = getUserLoginData.value.username;
  updateCardBIAData.value.authordutyname = getUserLoginData.value.grade;
  updateCardBIAData.value.authordeptid = getUserLoginData.value.deptid;
  updateCardBIAData.value.authordeptname = getUserLoginData.value.deptname;

  updateCardBIAData.value.judgeParam = 'offSend';

  var insertCardBIAFormData = new FormData();
  const {files, ...rest} = updateCardBIAData.value;
  const textData = Object.assign({}, rest);
  
  insertCardBIAFormData.append(
    "reqUpdateCardBIA",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  API.dctAPI
    .updateCardBIA(insertCardBIAFormData, urlPaths.value)
    .then((response) => {
      if (response.status == 200) {
        isloading.value = false;
        if(allPrinted.value === 'N'){
          alert("오프라인발송 처리되었습니다.");
          confirmPopUp();//모두출력이 아닐때만 컨펌태우고/ 모두출력일때는 리턴펑크
        }else{
          alert("모든 인쇄를 완료하여, 오프라인발송 처리되었습니다.");
        }
      }
    })
    .catch((error) => {
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });
}



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
  allPrinted.value = reqDocInfo._value.every(item => item.prtCnt >= 1) ? 'Y' : 'N';
};


const allNotPrinted = ref('N');
const isAllNotPrinted = () => {
  allNotPrinted.value = reqDocInfo.value.every(item => item.prtCnt == 0) ? 'Y' : 'N';
};

watch(() => reqDocInfo.value, async () => {
  setDate();
  await addHistoryToFiles();
  isAllPrinted();
  isAllNotPrinted();
  await addHistoryToFiles();
  if(allPrinted.value === 'Y'){
    updateCardBIA();

    // alert('전체 출력 완료.')
  }
  // await updatePrtCnt();
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

// let isOridoc = ref('')
// /* 원본 docid 찾기 */
// const getDctOridoc = async () => {
//   try {
//     const response = await API.dctAPI.getDctRdoc(
//       {docid: props.args.docid}, // 사본 docid
//       urlPaths.value
//     );
//     const data = response.data;
//     if(data.oridocid){
//       args.value.oridocid = data.oridocid;
//     }else{
//       args.value.oridocid = props.args.docid;
//       isOridoc.value = 'oriDoc';
//     }
//   } catch (error) {
//     console.log(error);
//   }
// };

const insertHistoryCard = async (file) => {
  updatePrtCntCondi.value = {
    docid: props.args.docid,
    // oridocid: props.args.oridocid,
    resuserid: getUserLoginData.value.userid,
    deptid: getUserLoginData.value.deptid,
    deptname: getUserLoginData.value.deptname,
    dutyname: getUserLoginData.value.orgname,
    username: getUserLoginData.value.username,
    electsignid: getUserLoginData.value.username,
    recvdeptname: props.args.recvdeptname,
    copyno: props.args.copyno,
    // file: file,
    // etc: props.args.recvdeptname + ' ' + props.args.copyno,
    etc: (file.rownum) + '번 파일:' + file.sfilename,
  }
  API.dctAPI
  .insertHistoryCard(updatePrtCntCondi.value, urlPaths.value)
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
  const confirmItem  = ref({});
  confirmItem.value.state = 'DRDS8';
  props.returnFunc(confirmItem);
};


// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  updatePrtCntCondi.value.gubun = file.gubun;
  updatePrtCntCondi.value.fleorder = file.fleorder;
  if(file.prtCnt && file.prtCnt >= 1){
    alert("인쇄부수는 1을 초과할 수 없습니다. \n추가 인쇄가 필요할 경우,\n비밀문서사용요청의 인쇄요청으로 진행해주세요.");
  }else{
    docloading.value = true;
    visibleViewerPopup.value = !visibleViewerPopup.value;
    // await insertHistoryCard(file);
    // await addHistoryToFiles();
    // await selectSendFileList();
    await viewDocument(file, userid, 'N,N', '1');
  }
  document.querySelector('iframe').addEventListener('load', async function() {
    await insertHistoryCard(file);
    await addHistoryToFiles();
    await selectSendFileList();
    docloading.value = false;
  });

}

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

// Move Function
const moveToBmsDctsendCurrentList = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};

</script>