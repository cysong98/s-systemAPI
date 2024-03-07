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
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th class="required">코드아이디</th>
                <td>
                  <v-text-field
                    v-model="insertCodeData.codeid"
                    maxlength="6"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>코드명</th>
                <td>
                  <v-text-field
                    v-model="insertCodeData.codename"
                    maxlength="100"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>코드분류아이디</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="insertCodeData.tcodeid"
                    maxlength="6"
                    variant="outlined"
                    hide-details="auto"
                    ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>설명</th>
                <td>
                  <v-textarea
                    v-model="insertCodeData.description"
                    maxlength="500"
                    variant="outlined"
                    hide-details="auto"
                    auto-grow
                    style="margin-top: 10px; margin-bottom: 10px;"
                  ></v-textarea>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="insertCode()">등록</v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onMounted, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

const name = ref('BmsDctCodeInsertPopup')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for insertCode
const insertCodeData = ref({
  codeid: "",
  codename: "",
  tcodeid: "",
  description: "",
  inid: loginStore.getUserLoginData.userid,
  indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
})


onMounted(() => {
  args.value = props.args
  insertCodeData.value.tcodeid = args.value.tcodeid
})

watchEffect(() => {
  args.value = props.args
})


const insertCode = async () => {
  if (checkRVBmsCode() == false) {
    return;
  }

  if(insertCodeData.value.codeid === insertCodeData.value.tcodeid) {
    alert("코드 아이디는 코드분류 아이디와 동일할 수 없습니다.");
    insertCodeData.value.codeid = "";
    return;
  }

  API.comAPI
    .insertCode(insertCodeData.value, urlPaths.value)
    .then((response) => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        props.returnFunc();
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

// Popup
const closePopUp = () => {
  props.toggleFunc();
};


// Check Validation
const checkRVBmsCode = () => {
  if (insertCodeData.value.codeid.length == 0) {
    alert("코드아이디를 입력해주세요.");
    return false;
  }
  if (insertCodeData.value.codeid.length > 6) {
    alert("코드아이디의 최대 길이는 6자입니다.");
    return false;
  }
  if (insertCodeData.value.codename.length > 100) {
    alert("코드명의 최대 길이는 100자입니다.");
    return false;
  }
  if (insertCodeData.value.tcodeid.length == 0) {
    alert("코드아이디를 입력해주세요.");
    return false;
  }
  if (insertCodeData.value.tcodeid.length > 6) {
    alert("코드아이디의 최대 길이는 6자입니다.");
    return false;
  }
  if (insertCodeData.value.description.length > 500) {
    alert("설명의 최대 길이는 500자입니다.");
    return false;
  }

  return true;
}

</script>