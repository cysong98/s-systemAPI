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
      <h4 class="h4">발송</h4>
    </div>
      <div class="box-content">
        <section class="section sticky">
          <div class="box-title">
            <h5 class="h5">비밀조회</h5>
            <div class="action-buttons">
              <v-btn v-if="draftToggle.data == 'N'" class="button" @click="readDraftDoc()">기안문조회</v-btn>
              <v-btn v-else class="button" @click="readDraftDoc()">비밀카드</v-btn>
              <v-btn v-if="getDctRdocData && getDctRdocData.state !=='DRDS5'" class="button" @click="dctGwaninPopupToggleFunc()">
                <v-dialog v-model="bVisibleDctGwaninPopup" style="width: 900px">
                  <v-card>
                    <v-toolbar>
                      <v-toolbar-title>관인 날인</v-toolbar-title>
                      <v-btn class="close" icon @click="bVisibleDctGwaninPopup = false">
                        <v-icon></v-icon>
                      </v-btn>
                    </v-toolbar>
                    <DctGwaninPopup
                      v-if="bVisibleDctGwaninPopup"
                      :args="dctGwaninPopupArgs"
                      :toggleFunc="dctGwaninPopupToggleFunc"
                      :returnFunc="dctGwaninPopupReturnFunc"></DctGwaninPopup>
                  </v-card>
                </v-dialog>
                관인
              </v-btn>

              <v-btn v-if="getDctRdocData && getDctRdocData.state !=='DRDS5'" class="button" @click="dctSignPopupToggleFunc()">
                <v-dialog v-model="bVisibleDctSignPopup" style="width: 900px">
                  <v-card>
                    <v-toolbar>
                      <v-toolbar-title>서명인 날인</v-toolbar-title>
                      <v-btn class="close" icon @click="bVisibleDctSignPopup = false">
                        <v-icon></v-icon>
                      </v-btn>
                    </v-toolbar>
                    <DctSignPopup
                      v-if="bVisibleDctSignPopup"
                      :args="dctSignPopupArgs"
                      :toggleFunc="dctSignPopupToggleFunc"
                      :returnFunc="dctSignPopupReturnFunc"></DctSignPopup>
                  </v-card>
                </v-dialog>
                서명인
              </v-btn>

              <v-btn v-if="getDctRdocData && getDctRdocData.state !=='DRDS5' && !getDctRdocData.saveLoading" class="button" @click="dctSendProcPopupToggleFunc(docidForProps)">
                <v-dialog v-model="bVisibleDctSendProcPopup" style="width: 800px;">
                  <v-card>
                    <v-toolbar>
                      <v-toolbar-title>발송</v-toolbar-title>
                      <v-btn class="close" icon @click="bVisibleDctSendProcPopup = false">
                        <v-icon></v-icon>
                      </v-btn>
                    </v-toolbar>
                    <DctSendProcPopup
                      v-if="bVisibleDctSendProcPopup"
                      :args="dctSendProcPopupArgs"
                      :toggleFunc="dctSendProcPopupToggleFunc"
                      :returnFunc="dctSendProcPopupReturnFunc"></DctSendProcPopup>
                  </v-card>
                </v-dialog>
                발송
              </v-btn>

              <v-btn v-if="getDctRdocData && getDctRdocData.state !=='DRDS0' && query && query.fromPage !=='BmsDctSendCurrentList'" class="button" @click="dctSendHoldPopupToggleFunc()">
                <v-dialog v-model="bVisibleDctSendHoldPopup" style="width: 800px;">
                  <v-card>
                    <v-toolbar>
                      <v-toolbar-title>발송보류</v-toolbar-title>
                      <v-btn class="close" icon @click="bVisibleDctSendHoldPopup = false">
                        <v-icon></v-icon>
                      </v-btn>
                    </v-toolbar>
                    <DctSendHoldPopup
                      v-if="bVisibleDctSendHoldPopup"
                      :args="dctSendHoldPopupArgs"
                      :toggleFunc="dctSendHoldPopupToggleFunc"
                      :returnFunc="dctSendHoldPopupReturnFunc"></DctSendHoldPopup>
                  </v-card>
                </v-dialog>
                발송보류
              </v-btn>

              <v-btn v-if="getDctRdocData && getDctRdocData.state ==='DRDS0'" @click="notSend()">발송안함</v-btn>

              <v-btn v-if="query && query.fromPage !=='BmsDctSendCurrentList'" class="button" @click="moveToBmsDctsendwaitlist()">닫기</v-btn>
              <v-btn v-if="query && query.fromPage ==='BmsDctSendCurrentList'" class="button" @click="moveToBmsDctsendcurrentlist()">닫기</v-btn>
            </div>
          </div>
        </section>
      
        <div v-if="draftToggle.data == 'N'">
          <BmsDctCard :data="getDctRdocData" :draftToggle="draftToggle" @updateToggle="handleToggleUpdate"/>
        </div>
        <HwpCtrl v-if="draftToggle.data == 'Y'" :data="getDctRdocData" :toggle="draftToggle" :view="draftView" ref="childRef" :key="compKey"/>
      </div>
      
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import { getValueByKey } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import DctGwaninPopup from "./DctGwaninPopup.vue";
import DctSignPopup from "./DctSignPopup.vue";
import DctSendProcPopup from "./DctSendProcPopup.vue";
import DctSendHoldPopup from "./DctSendHoldPopup.vue";
import BmsDctCard from "./BmsDctCard.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

const name =
  ref(
    "BmsDctSendWaitCard"
  ); 

const params = ref({});
const query = ref({});

// 기안문 관련
import HwpCtrlPopup from "./HwpCtrlPopup.vue";
import HwpCtrl from "./HwpCtrl.vue";
const childRef = ref(null);
const draftToggle = ref({ data: "N" });
const draftView = ref({ data: "Y" });
const handleToggleUpdate = (newValue) => {
  draftToggle.value.data = newValue;
};
const readDraftDoc = ref(() => {
  if (getDctRdocData.value.drafturl != "") {
    if (draftToggle.value.data == 'Y') {
      draftToggle.value.data = "N";
    } else {
      draftToggle.value.data = "Y";
    }
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
});
const callDraftToggle = (data) => {
  // draftToggle.value.data = data;
  if (childRef.value) {
    childRef.value.draftToggle(data);
  }
}

const bVisibleDctGwaninPopup = ref(false);
const dctGwaninPopupArgs = ref({});
const dctGwaninPopupToggleFunc = ref(() => {
  bVisibleDctGwaninPopup.value = !bVisibleDctGwaninPopup.value;
});
const dctGwaninPopupReturnFunc = ref((retValue) => {
  dctGwaninPopupToggleFunc.value();
  getDctRdocData.value.imageFileData = {
    managegubun: retValue.managegubun,
    fileurl: retValue.fileurl,
    filepath: retValue.filepath
  };
  callDraftToggle("Y");
});

const bVisibleDctSignPopup = ref(false);
const dctSignPopupArgs = ref({});
const dctSignPopupToggleFunc = ref(() => {
  bVisibleDctSignPopup.value = !bVisibleDctSignPopup.value;
});
const dctSignPopupReturnFunc = ref((retValue) => {
  dctSignPopupToggleFunc.value();
  getDctRdocData.value.imageFileData = {
    managegubun: retValue.managegubun,
    fileurl: retValue.fileurl,
    filepath: retValue.filepath
  };
  callDraftToggle("Y");
});

const bVisibleDctSendProcPopup = ref(false);
const dctSendProcPopupReturn = ref({});
const dctSendProcPopupArgs = ref({});
const dctSendProcPopupToggleFunc = ref(() => {
  // 저장로딩 true
  getDctRdocData.value.saveLoading = true;

  if (draftToggle.value.data == 'N') {

    draftToggle.value.data = 'Y';
  } else {
    callDraftToggle('Y');
  }

  dctSendProcPopupArgs.value = getDctRdocData.value
  dctSendProcPopupArgs.value.loginUserId =loginStore.getUserLoginData.userid;
  dctSendProcPopupArgs.value.loginUserName =loginStore.getUserLoginData.username;
  bVisibleDctSendProcPopup.value = !bVisibleDctSendProcPopup.value;
});
const dctSendProcPopupReturnFunc = ref((retValue) => {
  dctSendProcPopupToggleFunc.value();
  dctSendProcPopupReturn.value = retValue;
});

const bVisibleDctSendHoldPopup = ref(false);
const dctSendHoldPopupReturn = ref({});
const dctSendHoldPopupArgs = ref({});
const dctSendHoldPopupToggleFunc = ref(() => {
  dctSendHoldPopupArgs.value = query.value
  bVisibleDctSendHoldPopup.value = !bVisibleDctSendHoldPopup.value;
});
const dctSendHoldPopupReturnFunc = ref((retValue) => {
  dctSendHoldPopupToggleFunc.value();
  dctSendHoldPopupReturn.value = retValue;
});

const route = useRoute();
const router = useRouter();
const urlPaths = ref("");

const responseData = ref([]);
const docidForProps = ref("");

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(async() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  docidForProps.value = query.value.docid;
});
onMounted(async () => {
  await selectTaskInfo();
  await getDctRdoc();
});


const selectTaskInfo  = async () => {
  try {
    const response = await API.dctAPI.selectTaskInfo ({
      docid: query.value.docid },
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
const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.selectTempCreateCardList(
      {docid: query.value.docid,
        loginuserid: loginStore.getUserLoginData.userid
      },
      urlPaths.value
    );
    getDctRdocData.value = response.data;
    getDctRdocData.value.refList = responseData.value.refList;
    getDctRdocData.value.saveLoading = false;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const notSend = async() => {
  if(!confirm("처리하시겠습니까?")){
    return;
  }
  getDctRdocData.value;
  // 1) bms_dct_rdoc table
  getDctRdocData.value.tempflag = "N";
  getDctRdocData.value.state = "DRDS5"; // 발송안함
  // 4) bms_dct_path table
  getDctRdocData.value.pathList.map(item=>
    {if(item.pathorder=="1"){
      item.pathstate = "DCST5" //처리완료. // DCST4: 처리중
    }}); 
  // 6) bms_dct_appr_recv table
  getDctRdocData.value.recvList.map(item=>item.apprrecvtempflag = "N");
  getDctRdocData.value.recvList.map(item=>item.apprrecvgubun = "RDR08"); //원내
  getDctRdocData.value.recvMultiList = getDctRdocData.value.recvList
  // getDctRdocData.value.sendopinion;
  getDctRdocData.value.loginuserid = loginStore.getUserLoginData.userid;
  getDctRdocData.value.authorid = getUserLoginData.value.userid;
  getDctRdocData.value.authorname = getUserLoginData.value.username;
  getDctRdocData.value.authordutyname = getUserLoginData.value.grade;
  getDctRdocData.value.authordeptid = getUserLoginData.value.deptid;
  getDctRdocData.value.authordeptname = getUserLoginData.value.deptname;

  getDctRdocData.value.actgubun = "EPA09"; //EPA09 업무관리지정 //bms_dct_enf_history.actgubun 처리구분
  getDctRdocData.value.isNotSend = 'NotSend';

  var insertCardBIAFormData = new FormData(); 
  getDctRdocData.value.fileList.forEach((sfilename) => {
    insertCardBIAFormData.append('draftAttach', sfilename);
  });

  getDctRdocData.value.refList.forEach((ref)=>{
    if(ref.sfilename){
      insertCardBIAFormData.append('refAttach', ref.sfilename);
    }
  });     
  const {files, ...rest} = getDctRdocData.value;
  const textData = Object.assign({}, rest);
  insertCardBIAFormData.append(
    "reqUpdateCardBIA",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );
  for (let [key, value] of insertCardBIAFormData.entries()) {
  }

  try {
    const response = await API.dctAPI.updateCardBIA(insertCardBIAFormData, urlPaths.value);
    if (response.status == 200) {
      alert("발송안함 처리 되었습니다.")
      moveToBmsDctsendcurrentlist();
    }
  }catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// Move Function
const moveToBmsDctsendwaitlist = () => {
  router.push({
    name: "BmsDctsendwaitlist"
    });
};

// Move Function
const moveToBmsDctsendcurrentlist = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};

// left, top 숨김/펼침 버튼 클릭 시
// 기안문 width, height 변경되도록
const compKey = ref(0);

watch(() => contentWidth.value, (newWidth) => {
  compKey.value++;
});

watch(() => storeContentHeight.value, (newHeight) => {
  compKey.value++;
});
</script>