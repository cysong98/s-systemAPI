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
                <th v-if="props.args.judgeParam && props.args.judgeParam ==='distribute'">배부 의견</th>
                <th v-else>발송 의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea v-model="sendopinion"></textarea>
                    <!--테스트 끌나면 지워야함!!!-->
                    <v-btn color="white" style="border: none;" @click="toggleexstatusText()" class="test-button">(테스트스위치)송수신 상태 : {{ exstatusText }}</v-btn>
                    <!--테스트 끌나면 지워야함!!!-->
                  </span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA()">확인</v-btn>
      </div>
    </div>

  </div>
  <div v-if="isloading" class="overlay_02">
    <div v-if="route.path === '/dct/BmsDctReceivedOfflinesharecard'">
      <div class="spinner_02"></div>
      <div class="progress">{{ progress }}</div>
    </div>
    <div v-else>
      <div class="spinner_02"></div>
      <div class="progress">수신처 {{ progressCount }} / {{ args.recvList.length }}</div>
      <div class="progress" style="padding-top: 1%;">{{ progress }}</div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from "dayjs";
import { ref, onBeforeMount, onMounted, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { connectSSE, initializeSSE } from '@/utils/progressCheck.js'
import { API } from "@/api";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

const name = ref("DctSendProcPopup");
const props = defineProps({
  // args: String,
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
});
const args = ref({});
// const data = ref("");

const route = useRoute();
const router = useRouter();
// for popup
const childReturnValue = ref({});
const urlPaths = ref("");
const progress = ref("0%");
const progressCount = ref(0);

// for updateCardBIA
const updateCardBIAData = ref({});
const sendopinion = ref("");

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  args.value = props.args;
  updateCardBIAData.value = args.value;
});

onMounted(() => {
  args.value = props.args;
  updateCardBIAData.value = args.value;
});

watchEffect(() => {
  args.value = props.args;
  updateCardBIAData.value = args.value;
  if(args.recvList && args.recvList.length > 0 && args.recvlist[0] && args.recvlist[0].distdeptname){
    updateCardBIAData.value.recvMultiList = args.value.recvList
    updateCardBIAData.value.recvList.forEach((item, index) => {
      item.distdeptname = args.value.recvlist[index].distdeptname;
      item.distname = args.value.recvlist[index].distname;
    })
  }
});

const isloading = ref(false);

const exstatusText = ref('arrive');
// const exstatusText = ref('fail');

function toggleexstatusText() {
  exstatusText.value = exstatusText.value === 'arrive' ? 'fail' : 'arrive';
}

const updateCardBIA = async () => {
  isloading.value = true;

  let init = true;
  const sse = connectSSE();
  sse.addEventListener('progress', (e) => {
    if(init) {
      init = false;
      return;
    }
    progress.value = e.data;
    if(e.data === '100%')
      progressCount.value += 1;
  });
  sse.onerror = () => {
    progress.value = "0%";
    sse.close();
  };

// 1) bms_dct_rdoc table
args.value.tempflag = "N";
    args.value.state = "DRDS8"; // 발송완료
    // 4) bms_dct_path table
    args.value.pathList.map(item=>
      {if(item.pathorder=="1"){
        item.pathstate = "DCST5" //처리완료. // DCST4: 처리중
      }}); 
    // 6) bms_dct_appr_recv table
    args.value.recvList.map(item=>item.apprrecvtempflag = "N");
    args.value.recvList.map(item=>item.apprrecvgubun = "RDR08"); //원내

    updateCardBIAData.value = args.value;
    updateCardBIAData.value.refList = args.value.refList;
    updateCardBIAData.value.recvMultiList = args.value.recvList
    updateCardBIAData.value.sendopinion = sendopinion.value;
    updateCardBIAData.value.loginuserid = updateCardBIAData.value.loginUserId;
    updateCardBIAData.value.authorid = getUserLoginData.value.userid;
    updateCardBIAData.value.authorname = getUserLoginData.value.username;
    updateCardBIAData.value.authordutyname = getUserLoginData.value.grade;
    updateCardBIAData.value.authordeptid = getUserLoginData.value.deptid;
    updateCardBIAData.value.authordeptname = getUserLoginData.value.deptname;

    updateCardBIAData.value.draftfile = args.value.draftfile;

    updateCardBIAData.value.actgubun = "EPA09"; //EPA09 업무관리지정 //bms_dct_enf_history.actgubun 처리구분

    // ############ 송수신 테스트용 임시 파라미터 ############ //
    // updateCardBIAData.value.exstatus = "arrive"; //수신양호
    // updateCardBIAData.value.exstatus = "fail"; //수신불량
    updateCardBIAData.value.exstatus = exstatusText.value;
    // ############ 송수신 테스트용 임시 파라미터 ############ //

    var insertCardBIAFormData = new FormData(); 
    args.value.fileList.forEach((sfilename) => {
    insertCardBIAFormData.append('draftAttach', sfilename);
  });

    if(args.refList && args.refList.length > 0){
      args.value.refList.forEach((ref)=>{
        if(ref.sfilename){
          insertCardBIAFormData.append('refAttach', ref.sfilename);
        }
      });
    }
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
    await API.dctAPI
      .updateCardBIA(insertCardBIAFormData, urlPaths.value)
      .then((response) => {
        if (response.status == 200) {
          isloading.value = false;
          if(response.data > 10000){ //발송실패 존재.
            const failedDocs = Math.floor(response.data / 10000); // 실패한 문서 수 계산
            const totalDocs = args.value.recvList.length; // 전체 문서 수
            const successDocs = totalDocs - failedDocs; // 성공한 문서 수
            alert(`발송 실패한 문서가 존재합니다. 발송현황함에서 확인해주십시오. \n전체 문서: ${totalDocs}개, \n실패 문서: ${failedDocs}개`);
          }else{
            alert("요청하신 문서가 처리되었습니다.");
          }
          if(props.args.judgeParam && props.args.judgeParam ==='distribute'){
            moveToBmsDctdistributelist();
          }else{
            moveToBmsDctsendCurrentList();
          }
        }
      })
      .catch((error) => {
        isloading.value = false;
        console.log(error);
        alert("Server Error");
      });
  
  initializeSSE();
};

// Move Function
const moveToBmsDctsendCurrentList = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};
const moveToBmsDctdistributelist = () => {
  router.push({
    name: "BmsDctdistributelist"
    });
};

// Popup
const closePopUp = () => {
  props.toggleFunc();
};


</script>

<style lang="scss" scoped>
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
