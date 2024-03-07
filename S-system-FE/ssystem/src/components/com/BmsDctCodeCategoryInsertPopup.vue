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
              <col width="130">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th class="required">코드분류아이디</th>
                <td>
                  <v-text-field
                    v-model="insertCodeCategoryData.tcodeid"
                    maxlength="6"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>코드분류명</th>
                <td>
                  <v-text-field
                    v-model="insertCodeCategoryData.tcodename"
                    maxlength="100"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>코드설명</th>
                <td>
                  <v-textarea
                    v-model="insertCodeCategoryData.description"
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
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="insertCodeCategory()">저장</v-btn>
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

const name = ref('BmsDctCodeCategoryInsertPopup')
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
// for insertCodeCategory
const insertCodeCategoryData = ref({
  tcodeid: "",
  tcodename: "",
  description: "",
  inid: loginStore.getUserLoginData.userid,
  indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
})


onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
})


const insertCodeCategory = async () => {
  if (checkRVBmsTcode() == false) {
    return;
  }

  API.comAPI
    .insertCodeCategory(insertCodeCategoryData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        props.returnFunc();
      }
    })
    .catch(error => {
      console.log(error);
    });
}

// Popup
const closePopUp = () => {
  props.toggleFunc();
}


// Check Validation
const checkRVBmsTcode = () => {
  if (insertCodeCategoryData.value.tcodeid.length == 0) {
    alert("코드분류아이디를 입력해주세요.");
    return false;
  }
  if (insertCodeCategoryData.value.tcodeid.length > 6) {
    alert("코드분류아이디의 최대 길이는 6자입니다.");
    return false;
  }
  if (insertCodeCategoryData.value.tcodename.length > 100) {
    alert("코드분류명의 최대 길이는 100자입니다.");
    return false;
  }
  if (insertCodeCategoryData.value.description.length > 500) {
    alert("코드설명의 최대 길이는 500자입니다.");
    return false;
  }

  return true;
}

</script>