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
                    v-model="args.reqttl"
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
                      v-model="args.appropiniontemp"
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
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
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

const name = ref('UseApprovalPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
  insertFunc: Function,
  addManagerPath: Function
})
const args = ref({})
const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')

onBeforeMount(() => {
})

onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
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
  if(!confirm("요청하시겠습니까?"))
  return;
  if(args.value.addManager == 'Y'){
    props.addManagerPath();
  }
  props.insertFunc(args.value);
  props.toggleFunc();
}


</script>