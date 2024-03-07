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
          <v-table class="table-type-03">
            <colgroup>
              <col width="120px">
              <col>
            </colgroup>
            <tbody>
              <tr>
                <th>문서 제목</th>
                <td>
                  <v-text-field
                    readonly
                    v-model="args.docttl"
                    variant="solo"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>처리 의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea v-model="args.opinion"></textarea>
                  </span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if = "args.frompage && args.frompage == 'BmsDctReceivedOfflineSharecard'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="insertOfflineRdoc">상신</v-btn>
      <v-btn v-else variant="flat" color="indigo-darken-3" rounded="xl" @click="draftConfirm">상신</v-btn>
    </div>

    <div v-if="isloading" class="overlay_02">
      <div class="spinner_02"></div>
      <div class="progress">{{ progress }}</div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { connectSSE, initializeSSE } from '@/utils/progressCheck.js'
import { API } from "@/api";

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('DctReqProcPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
  saveFunc: Function,
  received: Boolean
})
const args = ref({})
const isloading = ref(false);
const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
const progress = ref("0%");
// for insertCardBIA
const insertCardBIAData = ref({})

onBeforeMount(() => {
  if(props.args.page === 'BmsDctreceiptlist'){
    getDctRdoc();
    args.value.docstatus = 'EDS11'; //EDS11: 종료 (bms_dct_enf_doc 테이블 docstatus 컬럼)
  }else{
  }
})

onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
  if(args.value.chargerid && args.value.frompage != 'BmsDctReceivedOfflineSharecard'){
    args.value.recvList = null;
  }
  /* 접수문서를 기안할때는 해당사본을 원본으로 갖도록 한다. */
  if(props && props.args && props.args.page && props.args.page === 'BmsDctreceiptlist' && args.value.recvlist && args.value.recvlist.length > 0){
    args.value.createoridocid = args.value.oridocid //생산문서 원본 docid
    // args.value.oridocid = props.args.recvlist[0].docid //기안문서 원본 docid (해당 사본을 원본으로 가지면서 새로운 docid 생성.)
  }
})


const draftConfirm = () => {
  if(args.value.pathList.length == 1){
    if(confirm("1인결재를 진행하시겠습니까?")){
        updateCardBIA1();
    }else{
      return
    }
  }else{
    insertCardBIA();
  }
};

// 기안 문서처리
const insertCardBIA = async () => {

  if(!confirm("처리하시겠습니까?")){
    return;
  }

  isloading.value = true;

  const sse = connectSSE();
  sse.addEventListener('progress', (e) => {
    progress.value = e.data;
  });
  sse.onerror = async() => {
    progress.value = "0%";
    sse.close();
  };

  // 1) bms_dct_rdoc table
  args.value.tempflag = "N";
  args.value.state = "DRDS2"; // 처리중
  // 4) bms_dct_path table
  args.value.pathList.map(item=>
    {if(item.pathorder=="1"){
      item.pathstate = "DCST4"
    }}); // 처리중
  // 6) bms_dct_appr_recv table
  if(!args.value.chargerid){
    args.value.recvList.map(item=>item.apprrecvtempflag = "N");
  }


  var insertCardBIAFormData = new FormData(); 
  // args.value.files가 존재하면 해당 배열을 사용하고, 그렇지 않으면 빈 배열을 사용
  (args.value.files || []).forEach((file) => {
    insertCardBIAFormData.append('draftAttach', file);
  });

  (args.value.refList || []).forEach((ref) => {
    if(ref.file){
      insertCardBIAFormData.append('refAttach', ref.file);
    }
  });
  const {files, ...rest} = args.value;
  const textData = Object.assign({}, rest);

  insertCardBIAFormData.append(
    "reqInsertCardBIA",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  
  // if(props.args.page === 'BmsDctreceiptlist'){
  //   updateEnfDocRepath(); //접수한 비밀문서를 '문서처리'시 상태값을 업데이트하는 로직 : bms_dct_enf_doc 테이블 docstatus 컬럼을 EDS11 (종료) 처리
  // }

  await API.dctAPI
    .insertCardBIA(insertCardBIAFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        if(props.args.page === 'BmsDctreceiptlist'){
          updateEnfDocRepath(); //접수한 비밀문서를 '문서처리'시 상태값을 업데이트하는 로직 : bms_dct_enf_doc 테이블 docstatus 컬럼을 EDS11 (종료) 처리
        }else{
          isloading.value = false;
          // alert("정상 등록되었습니다");
        }
        moveToBmsDctreqcompletelist();
      }
    })
    .catch(error => {
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });

    initializeSSE();
};


// 1인결재 승인
const updateCardBIA1 = async () => {
  if(props.received != true) {
    await createNo();
  }

  const sse = connectSSE();
  sse.addEventListener('progress', (e) => {
    progress.value = e.data;
  });
  sse.onerror = () => {
    progress.value = "0%";
    sse.close();
  };

  if(errFlag.value == "N") {
    isloading.value = true;

    // 1) bms_dct_rdoc table
    args.value.tempflag = "N";
    // 4) bms_dct_path table
    args.value.pathList[0].pathstate = "DCST5" // 처리완료
    args.value.pathList[0].askTemp = "1인결재";
    args.value.pathList[0].asktype = "DEA45"; // 1인결재
    args.value.pathList[0].acttype = "DEA45"; // 1인결재
    // 6) bms_dct_appr_recv table
    if (!args.value.chargerid) {
      args.value.recvList.map(item => item.apprrecvtempflag = "N");
    }

    args.value.resasktype = "DEA45"; // 1인결재

    args.value.histgubun = "02"; // 결재
    args.value.histstatus = "011"; // 결재

    var updateCardBIA1FormData = new FormData();

    if (args.value.files && args.value.files.length > 0) {
      args.value.files.forEach((file) => {
        updateCardBIA1FormData.append('draftAttach', file);
      });
    }
    if (args.value.refList && args.value.refList.length > 0) {
      args.value.refList.forEach((ref) => {
        if (ref.file) {
          updateCardBIA1FormData.append('refAttach', ref.file);
        }
      });
    }
    args.value.summary = "";

    const { files, ...rest } = args.value;
    const textData = Object.assign({}, rest);

    updateCardBIA1FormData.append(
      "reqUpdateCardBIA1",
      new Blob([JSON.stringify(textData)], {
        type: "application/json",
      })
    );

    await API.dctAPI
      .updateCardBIA1(updateCardBIA1FormData, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          if (props.args.page === 'BmsDctreceiptlist') {
            updateEnfDocRepath(); //접수한 비밀문서를 '문서처리'시 상태값을 업데이트하는 로직 : bms_dct_enf_doc 테이블 docstatus 컬럼을 EDS11 (종료) 처리
          } else {
            isloading.value = false;
            // alert("정상 등록되었습니다");
          }
          moveToBmsDctreqcompletelist();
        }
        window.sessionStorage.removeItem('waitFlag');
      })
      .catch(error => {
        console.log(error);
        isloading.value = false;
        window.sessionStorage.removeItem('waitFlag');
        alert("Server Error");
      });

    isloading.value = false;
  }

  initializeSSE();
};


// 문서처리
const insertOfflineRdoc = async () => {

if(args.value.pathList.length == 1){
  if(!confirm("1인결재를 진행하시겠습니까?")){
    return
  }else{
    args.value.isOneApprove = "Y"; //1인결재
    args.value.pathList[0].pathstate = "DCST5";
    args.value.pathList[0].askTemp = "1인결재"; 
    args.value.pathList[0].asktype = "DEA45";
    args.value.pathList[0].acttype = "DEA45";
    args.value.resasktype = "DEA45";
    args.value.state = "DRDS3"; // 종료
    args.value.histgubun = "02";
    args.value.histstatus = "011";
    const getUserLoginData = ref({getUserLoginData: loginStore.getUserLoginData});
    args.value.authordeptid = getUserLoginData.value.getUserLoginData.deptid;
    args.value.authorid = getUserLoginData.value.getUserLoginData.userid;
    args.value.authorname = getUserLoginData.value.getUserLoginData.username;
    args.value.authordutyname = getUserLoginData.value.getUserLoginData.grade;
    args.value.authordeptname = getUserLoginData.value.getUserLoginData.orgname;
    args.value.deptid = getUserLoginData.value.getUserLoginData.deptid;
    await createMgmtno();
  }
}else{
  args.value.isOneApprove = "N"; // 경로다건
  args.value.state = "DRDS2"; // 처리중
  args.value.resasktype = "";
  args.value.mgmtno = "";
}

args.value.pathList.map(item=>
  {if(item.pathorder=="1"){
    item.pathstate = "DCST5" //처리완료
  }});
args.value.histgubun = "01";
args.value.histstatus = "010";
args.value.paperrecgubun = "접수";
args.value.judgeParam = "offReceipt";

isloading.value = true;

const sse = connectSSE();
sse.addEventListener('progress', (e) => {
  progress.value = e.data;
});
sse.onerror = () => {
  progress.value = "0%";
  sse.close();
};

var insertOfflineRdocForm = new FormData(); 
 // args.value.files가 존재하면 해당 배열을 사용하고, 그렇지 않으면 빈 배열을 사용
(args.value.files || []).forEach((file) => {
  args.value.files.forEach((file) => {
    insertOfflineRdocForm.append('draftAttach', file);
  });
});

const {files, ...rest} = args.value;
const textData = Object.assign({}, rest);

insertOfflineRdocForm.append(
  "reqInsertOfflineRdoc",
  new Blob([JSON.stringify(textData)], {
    type: "application/json",
  })
);

API.dctAPI
  .insertOfflineRdoc(insertOfflineRdocForm, urlPaths.value)
  .then(response => {
    if (response.status == 200) {
      isloading.value = false;
      moveToBmsDctreqcompletelist();
    }
  })
  .catch(error => {
    isloading.value = false;
    console.log(error);
    alert("Server Error");
  });

  initializeSSE();
};

// 관리번호 생성
const createMgmtno = async () => {
  try {
    // API 호출
    let tempSeq = await API.dctAPI.getTempSeq({},urlPaths.value);
    let recentMgmt = await API.dctAPI.getRecentMgmt({},urlPaths.value);

    // 년도
    let curDate = new Date();
    let curYear = curDate.getFullYear();

    // 가장 최근 비밀관리기록부 데이터의 등록일 년도보다 현재 년도가 크면 시퀀스 reset
    if (recentMgmt.data != null && recentMgmt.data != undefined && recentMgmt.data != "") {
      if (Number(curYear) > Number(recentMgmt.data.toString().substring(0, 4))) {
        let resetTempSeq = await API.dctAPI.resetTempSeq({},urlPaths.value);
        tempSeq = resetTempSeq;
      }
    }

    // 관리번호 생성
    let seclevel = ""
    if (args.value.seclevel == "2") {
      seclevel = "II";
    }
    if (args.value.seclevel == "3") {
      seclevel = "III";
    }
    if (args.value.seclevel == "4") {
      seclevel = "대외비";
    }
    if (args.value.seclevel == "5") {
      seclevel = "일반";
    }
    let mgmtno = seclevel + "-" + curYear + "-" + tempSeq.data;
    args.value.mgmtno = mgmtno;

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 1) bms_dct_rdoc table
const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc(
      {docid: props.args.docid},
      urlPaths.value
    );
    const data = response.data;
    if(data.oridocid){
      args.value.oridocid = data.oridocid;
    }else{
      args.value.oridocid = props.args.docid;
    }
    
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const updateEnfDocRepath = async () => {
  try {
    const response = await API.dctAPI.updateEnfDocRepath(
      {enfdocid: props.args.enfdocid, docstatus: 'EDS11'},
      urlPaths.value
    );
    const data = response.data;
    // alert("정상 등록되었습니다");
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  alert("확인되었습니다.");
  props.returnFunc(childReturnValue.value);
}

// 기안완료함 이동
const moveToBmsDctreqcompletelist = (item) => {
  let arr = ['비밀문서', '기안', '기안완료함']

  breadcrumbs.value.activeLink = arr;

  router.push({
    name: "BmsDctreqcompletelist",
  });
};

// 관리번호, 사본번호 생성
const errFlag = ref("N");
const createNo = async () => {
  try {
    // API 호출
    let tempSeq = await API.dctAPI.getTempSeq({

    },
      urlPaths.value
    );

    let recentMgmt = await API.dctAPI.getRecentMgmt({

    },
      urlPaths.value
    );

    // 비밀등급 변환
    let seclevel = ""
    if (args.value.seclevel == "2") {
      seclevel = "II";
    }
    if (args.value.seclevel == "3") {
      seclevel = "III";
    }
    if (args.value.seclevel == "4") {
      seclevel = "대외비";
    }
    if (args.value.seclevel == "5") {
      seclevel = "일반";
    }

    // 년도
    let curDate = new Date();
    let curYear = curDate.getFullYear();

    // 가장 최근 비밀관리기록부 데이터의 등록일 년도보다 현재 년도가 크면 시퀀스 reset
    if (recentMgmt.data != null && recentMgmt.data != undefined && recentMgmt.data != "") {
      if (Number(curYear) > Number(recentMgmt.data.toString().substring(0, 4))) {
        let resetTempSeq = await API.dctAPI.resetTempSeq({

        },
          urlPaths.value
        );

        tempSeq = resetTempSeq;
      }
    }

    // 관리번호 생성
    let mgmtno = seclevel + "-" + curYear + "-" + tempSeq.data;
    args.value.mgmtno = mgmtno;

    // 사본번호 생성
    let copyno = "";
    if (args.value.recvList != null && args.value.recvList.length > 0) {
      if (args.value.recvList[0].receiverid != "") {
        copyno = "원본/" + args.value.recvList.length;
      } else {
        copyno = "원/본";
      }
    } else {
      copyno = "원/본";
    }
    args.value.copyno = copyno;

    // 웹한글기안기 저장
    isloading.value = true;
    await props.saveFunc('Y', 'Y');

    // 세션 스토리지 값이 Y로 변경될 때까지 대기
    let waitCnt = 0;
    while (window.sessionStorage.getItem('waitFlag') !== 'Y') {
      // 1초 대기 후 다시 확인
      await new Promise(resolve => setTimeout(resolve, 1000));

      if (window.sessionStorage.getItem('waitFlag') == null) {
        alert("웹한글기안기 오류");
        errFlag.value = "Y";
        break;
      }

      if (waitCnt > 60) {
        alert("Timeout Error");
        errFlag.value = "Y";
        break;
      }

      waitCnt++;
    }

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

</script>