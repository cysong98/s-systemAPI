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
      <v-tabs v-model="selectedTab" align-tabs="end" class="tabs">
        <v-tab :value="1">중앙행정기관 및 이에 준하는 기관</v-tab>
        <v-tab :value="2">각 시/도</v-tab>
        <v-tab :value="3">각 시/도 교육청</v-tab>
        <v-tab :value="4">이북 5도</v-tab>
        <v-tab :value="5">공공기관</v-tab>
      </v-tabs>

      <v-window>
        <v-window-item>
          <div class="row">
            <div class="col">
              <v-data-table
                @click:row="(event, item) => selectedRow(item.item.raw)"
                :headers="headers"
                :items="receiveMarkList"
                item-value="codeid"
                hover
                class="table-type-02"
                height="400px"
              >
                <template v-slot:bottom></template>
              </v-data-table>
            </div>
          </div>
        </v-window-item>
      </v-window>
    </div>

    <div class="buttons-bottom">
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
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';

const name = ref('DctRecvExternalPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const route = useRoute()
const router = useRouter()
const isloading = ref(false)
// for popup
const urlPaths =  ref('')
const selectedTab = ref(1)

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  await selectReceiveMarkCodeList();
})

watchEffect(() => {
  args.value = props.args
})

const receiveMarkCondi = ref({tcodeid: "", description: ""})
const receiveMarkList = ref([]);
const selectReceiveMarkCodeList = async () => {
  isloading.value = true;
  switch(selectedTab.value){
    case 1:
      receiveMarkCondi.value.tcodeid = 'SYM00';
      break;
    case 2:
      receiveMarkCondi.value.tcodeid = 'SYM01';
      break;
    case 3:
      receiveMarkCondi.value.tcodeid = 'SYM02';
      break;
    case 4:
      receiveMarkCondi.value.tcodeid = 'SYM03';
      break;
    case 5:
      receiveMarkCondi.value.tcodeid = 'SYM04';
      break;
  }
  try {
    const response = await API.comAPI.selectReceiveMarkCodeList({...receiveMarkCondi.value}, urlPaths.value);
    receiveMarkList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }finally{
    isloading.value = false;
  }
};

watchEffect(async () => {
  await selectReceiveMarkCodeList();
});

const headers = [
  { title: "수신기관명", key: "description", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "수신자기호", key: "codename", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

const closePopUp = () => {
  props.toggleFunc();
}

const selectedRow = (value) => {
  if(value !== undefined) {
    props.returnFunc(value);
    return;
  }
};
</script>