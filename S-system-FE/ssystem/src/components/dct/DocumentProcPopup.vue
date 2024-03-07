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
          <!-- {{args}} -->
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="">
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
                <th v-if="args.docstatus !== 'EDS06'">처리 의견</th>
                <th v-if="args.docstatus === 'EDS06'">반송 의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea v-model="actopinion"></textarea>
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
      <v-btn v-if="args.docstatus !== 'EDS06'" variant="flat" color="indigo-darken-3" rounded="xl" @click="documentProcBIA()">저장</v-btn>
      <v-btn v-if="args.docstatus === 'EDS06'" variant="flat" color="indigo-darken-3" rounded="xl" @click="documentProcBIA()">반송</v-btn>
    </div>

  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from '@/api'; 
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js"; 

const name = ref('DocumentProcPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})


const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
// for documentProcBIA
const documentProcBIAData = ref({
    docid: "",
    enfdocid: "",
    actseq: "",
    acterid: "",
    actername: "",
    acterdutyname: "",
    acterpositionid: "",
    actergradeid: "",
    acterdeptid: "",
    acterdeptname: "",
    acterdeptnamedesc: "",
    actgubun: "",
    actdt: "",
    actopinion: "",
    recid: "",
    recname: "",
    recdutyname: "",
    recpositionid: "",
    recgradeid: "",
    recdeptid: "",
    recdeptname: "",
    recdeptnamedesc: "",
    pathhisid: "",
    electsignid: ""
})
const actopinion = ref("");

onBeforeMount(() => {
})

onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
  documentProcBIAData.value = args.value;
  if(args.value.chargerList && args.value.chargerList.length > 1){
    args.value.newChargerList = [...args.value.chargerList];
    args.value.newChargerList.splice(0,1);
  }
})


const documentProcBIA = async () => {
  documentProcBIAData.value = args.value;
  documentProcBIAData.value.actopinion = actopinion.value;

  if(args.value.page !=null && args.value.page == 'BmsReprocesslist'){
    documentProcBIAData.value.chargerList = args.value.newChargerList;
    if (documentProcBIAData.value.chargerList && documentProcBIAData.value.chargerList.length > 0) {
      documentProcBIAData.value.chargerList[0].chargerorder = 1;
    }
  }

  documentProcBIAData.value.loginuserid = args.value.loginuserid;

  API.dctAPI
    .documentProcBIA(documentProcBIAData.value, urlPaths.value)
    .then((response) => {
      if (response.status == 200) {
        switch(documentProcBIAData.value.docstatus) { // bms_dct_enf_doc.docstatus 비밀상태
          case 'EDS10':// 재지정요청
              alert("재지정 되었습니다");
              moveToBmsDctreceiptlist(); // 접수대기함 이동
              break;
          case 'EDS05': //접수
              alert("접수 되었습니다");
              moveToBmsDctreceiptcurrentlist();
              break;
          case 'EDS06': //반송
              alert("반송 되었습니다");
              moveToBmsDctreceiptcurrentlist(); //접수,반송은 접수현황함 이동
              break;
          case 'EDS04': //재배부요청
              moveToBmsDctreceiptcurrentlist();
              break;
          case 'EDS12': //담당변경접수
              moveToBmsDctreceiptcurrentlist();
              break;
          default: //'EDS09' //접수대기
              alert("정상 등록되었습니다");
              moveToBmsDctreceiptcurrentlist();
              break;
        }
      }
    })
    .catch(error => {
      console.log(error);
      alert("Sever_Error");
    });
}

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}


// Move Function 접수현황함
const moveToBmsDctreceiptcurrentlist = () => {
  router.push({
    name: "BmsDctreceiptcurrentlist",
  });
};

// Move Function 접수대기함
const moveToBmsDctreceiptlist = () => {
  router.push({
    name: "BmsDctreceiptlist",
  });
};

</script>

<style lang="scss"></style>
