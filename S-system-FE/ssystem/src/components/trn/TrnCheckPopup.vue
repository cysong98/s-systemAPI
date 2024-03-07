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
          <v-table class="table-type-04" height="400" fixed-header>
            <thead>
              <tr>
                <th>NO</th>
                <th>관리번호</th>
                <th>보고일자</th>
                <th>제목</th>
                <th>미완료사유</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(mgmtRegi, idx) in selectMgmtRegiList" :key="idx">
                <td>{{idx+1}}</td>
                <td>{{mgmtRegi.mgmtno}}</td>
                <td>{{transformDate(mgmtRegi.regirecvdt)}}</td>
                <td style="text-align: left;">{{mgmtRegi.secttl}}</td>
                <td>{{}}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom" style="padding-top: 0px;">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>
  </div>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>

</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import { transformDate } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('TrnCheckPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const selectMgmtRegiList = ref([])

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
const isloading = ref(false)

onBeforeMount(() => {
})

onMounted(async() => {
  args.value = props.args;
  await selectMgmtRegiNonPageList();
})

watchEffect(() => {
  args.value = props.args
})


// bms_dct_mgmt_regi table
const selectMgmtRegiNonPageList = async () => {
  isloading.value = true;
  try {
    const response = await API.dctAPI.selectMgmtRegiNonPageList({...args.value},urlPaths.value);
    selectMgmtRegiList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }finally{
    isloading.value = false;
  }
};


const closePopUp = () => {
  props.toggleFunc();
}

</script>