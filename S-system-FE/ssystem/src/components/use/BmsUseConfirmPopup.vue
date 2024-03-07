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
                  <v-text-field
                    readonly
                    v-model="selectedData.title"
                    variant="solo"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>          
              <tr>
                <th>의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea
                      v-model="selectedData.appropinion"
                      :rules="[rules.counter]"
                      maxlength="1000"
                    ></textarea>
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
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
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
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';

const name = ref('BmsUseConfirmPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const route = useRoute()
const router = useRouter()
// for popup
const returnValue = ref({})
//const urlPaths =  ref('')
const selectBmsUseConfirmDefCondi = {
  appropinion: "",
  type: "",
  title: ""
}
const selectedData = ref({ ...selectBmsUseConfirmDefCondi })

onBeforeMount(() => {
})

onMounted(async () => {
  selectedData.value.type = props.args.type
  selectedData.value.title = props.args.title
})
onBeforeUnmount(() => {
})

watchEffect(() => {
  selectedData.value.type = props.args.type
  selectedData.value.title = props.args.title
})

const rules = {
  counter: value => value.length < 1000 || '최대 1000자 까지 입력하실 수 있습니다.',
};

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  if(!confirm("처리하시겠습니까?")){
    return
  }
  returnValue.value = selectedData.value;
  props.returnFunc(returnValue.value);
}




</script>