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
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="insertReuseDraft">상신</v-btn>
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
import { ref, onBeforeMount, onMounted, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { connectSSE, initializeSSE } from '@/utils/progressCheck.js'
import { API } from "@/api";

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('UseProcPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
  saveFunc: Function
})
const args = ref({})
const isloading = ref(false);
const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
const progress = ref("0%");

onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
})


// 재사용 문서처리
const insertReuseDraft = async () => {

  let mgs = "";
  if(args.value.pathList.length == 1){
    mgs = "1인결재를 진행하시겠습니까?";
  }else{
    mgs = "처리하시겠습니까?";
  }

  if(!confirm(mgs)){
    return;
  }

  isloading.value = true;

  const sse = connectSSE();
  sse.addEventListener('progress', (e) => {
    progress.value = e.data;
  });
  sse.onerror = () => {
    progress.value = "0%";
    sse.close();
  };

  // bms_dct_rdoc table
  args.value.tempflag = "N";
  args.value.state = "DRDS2";

  // bms_dct_path table, bms_dct_hist_card table
  if(args.value.pathList.length == 1){
    await createNo();
    
    args.value.isOneApprove = "Y";
    args.value.pathList[0].pathstate = "DCST5";
    args.value.pathList[0].askTemp = "1인결재"; 
    args.value.pathList[0].asktype = "DEA45";
    args.value.pathList[0].acttype = "DEA45";
    args.value.resasktype = "DEA45";
    if(args.value.recvList.every(recv=>recv.apprrecvdeptname == "")){
      args.value.state = "DRDS3"; // 종료
    }else{
      args.value.state = "DRDS4"; // 발송대기
    }
    args.value.histgubun = "02";
    args.value.histstatus = "011";
  }else{
    args.value.isOneApprove = "N";
    args.value.pathList.map(item=>
      {if(item.pathorder=="1"){
        item.pathstate = "DCST4"
      }});
    args.value.histgubun = "01";
    args.value.histstatus = "010";
  }
  
  // bms_dct_appr_recv table
  args.value.recvList.map(item=>item.apprrecvtempflag = "N");
  var insertReuseDraftForm = new FormData(); 
  args.value.files.forEach((file) => {
    insertReuseDraftForm.append('draftAttach', file);
  });
  
  args.value.refList.forEach((ref)=>{
    if(ref.file){
      insertReuseDraftForm.append('refAttach', ref.file);
    }
  });
  const {files, ...rest} = args.value;
  const textData = Object.assign({}, rest);

  insertReuseDraftForm.append(
    "reqInsertReuseDraft",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  
  if(errFlag.value == "N") {
    await API.dctAPI
      .insertReuseDraft(insertReuseDraftForm, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          isloading.value = false;
          moveToBmsDctreqcompletelist();
          window.sessionStorage.removeItem('waitFlag');
        }
      })
      .catch(error => {
        isloading.value = false;
        window.sessionStorage.removeItem('waitFlag');
        console.log(error);
        alert("Server Error");
      });
  } else {
    isloading.value = false;
  }

  initializeSSE();
};

const closePopUp = () => {
  props.toggleFunc();
}

// 기안완료함 이동
const moveToBmsDctreqcompletelist = (item) => {
  let arr = ['비밀문서', '기안', '기안완료함']

  breadcrumbs.value.activeLink = arr;
  topMenu.value.data = "B";

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