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
                <th>제목</th>
                <td>
                  <v-text-field readonly v-model="args.reqttl" variant="solo" hide-details="auto"></v-text-field>
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
      <v-btn v-if="type==1" variant="flat" color="indigo-darken-3" rounded="xl" @click="updateTrnRepotBIA1">{{ namingGubun() }}</v-btn>
      <v-btn v-else-if="type==2" variant="flat" color="indigo-darken-3" rounded="xl" @click="updateTrnRepotBIA2">반려</v-btn>
      <v-btn v-else-if="type==3" variant="flat" color="indigo-darken-3" rounded="xl" @click="updateTrnRepotBIA3">인수</v-btn>
      <v-btn v-else variant="flat" color="indigo-darken-3" rounded="xl" @click="reqAddConfirm()">승인요청</v-btn>
    </div>
  </div>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";

import { ref, onMounted, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
import { useLoginStore } from '/src/store/Login';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)
const loginStore = useLoginStore();
const { getUserLoginData } = storeToRefs(loginStore);
const name = ref('TrnApprovalPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
const type = ref(null);
const namingGubun = () => {
  if(args.value.apprcode === "ARC05")
    return '인수';
  else 
    return '승인';
}

onMounted(() => {
  args.value = props.args
  type.value = props.args.type
})

watchEffect(() => {
  args.value = props.args
})

// 승인
const updateTrnRepotBIA1Param = ref(args.value)
const updateTrnRepotBIA1 = async () => {
  updateTrnRepotBIA1Param.value.transferid = args.value.transferid;
  updateTrnRepotBIA1Param.value.apprstatus = 'APP03' //결재완료 //BMS_TRN_REPORT.apprstatus(최종결재완료여부)
  if(args.value.apprcode === "ARC05")
    updateTrnRepotBIA1Param.value.status = 'TRS04' //인수완료 //BMS_TRN_REPORT.status(진행상태)
  else
    updateTrnRepotBIA1Param.value.status = 'TRS03' //인수중 //BMS_TRN_REPORT.status(진행상태)
  updateTrnRepotBIA1Param.value.gojudge = 'go'
  updateTrnRepotBIA1Param.value.appruserid = getUserLoginData.value.userid;
  updateTrnRepotBIA1Param.value.apprreason = args.value.opinion;
  API.trnAPI
    .updateTrnRepotBIA1(updateTrnRepotBIA1Param.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("승인 처리 되었습니다");
        moveToBmsTrncompletelist();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
};


// 반려
const updateTrnRepotBIA2Param = ref(args.value)
const updateTrnRepotBIA2 = async () => {
  updateTrnRepotBIA2Param.value.transferid = args.value.transferid;
  updateTrnRepotBIA2Param.value.appruserid = getUserLoginData.value.userid;
  updateTrnRepotBIA2Param.value.apprreason = args.value.opinion;
  API.trnAPI
    .updateTrnRepotBIA2(updateTrnRepotBIA2Param.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("반려 처리 되었습니다");
        moveToBmsTrncompletelist();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
};

// 인수
const updateTrnRepotBIA3Param = ref(args.value);
const updateTrnRepotBIA3 = async () => {
  updateTrnRepotBIA3Param.value.transferid = args.value.transferid;
  updateTrnRepotBIA3Param.value.apprstatus = 'APP03' //결재완료 //BMS_TRN_REPORT.apprstatus(최종결재완료여부)
  updateTrnRepotBIA3Param.value.status = 'TRS04' //인수완료 //BMS_TRN_REPORT.status(진행상태)
  updateTrnRepotBIA3Param.value.gojudge = 'go'
  updateTrnRepotBIA3Param.value.appruserid = getUserLoginData.value.userid;
  updateTrnRepotBIA3Param.value.apprreason = args.value.opinion;
  API.trnAPI
    .updateTrnRepotBIA1(updateTrnRepotBIA3Param.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("승인 처리 되었습니다");
        moveToBmsTrncompletelist();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}


// 승인요청
const reqAddConfirm = () => {
  props.returnFunc(args.value.opinion);
};

const closePopUp = () => {
  props.toggleFunc();
}

// 처리한 인계인수서
const moveToBmsTrncompletelist = () => {
  let arr = ['비밀관리', '인계인수', '처리한 인계인수서'];
  breadcrumbs.value.activeLink = arr;
  router.push({
    name: "BmsTrncompletelist",
  });
};

</script>

<style lang="scss" scoped>
.opinion{
  height: 200px;
  border: 1px solid lightgray;
  border-radius: 5px;
  width: 500px;
  resize: none;
}
</style>
