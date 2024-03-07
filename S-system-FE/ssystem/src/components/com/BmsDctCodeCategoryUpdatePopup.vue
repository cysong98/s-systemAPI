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
                    :readonly="true"
                    v-model="updateCodeCategoryData.tcodeid"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>코드분류명</th>
                <td>
                  <v-text-field
                    v-model="updateCodeCategoryData.tcodename"
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
                    v-model="updateCodeCategoryData.description"
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
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCodeCategory()">저장</v-btn>
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

const name = ref('BmsDctCodeCategoryUpdatePopup')
const route = useRoute()
const router = useRouter()
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
// for popup
const urlPaths =  ref('')
// for updateCodeCategory
const updateCodeCategoryData = ref({})
// for getCodeCategoryDetail
const getCodeCategoryDetailData = ref({})
const getCodeCategoryDetailDefCondi = {
  tcodeid: ''
}
const getCodeCategoryDetailCondi = ref({ ...getCodeCategoryDetailDefCondi })
const getCodeCategoryDetailDefCondiStr = ref('')


onMounted(async () => {
  args.value = props.args
  getCodeCategoryDetailCondi.value.tcodeid = props.args[0];
  await getCodeCategoryDetail(getCodeCategoryDetailDefCondiStr.value);
})

watchEffect(() => {
  args.value = props.args
})


const getCodeCategoryDetail = async (paramValue) => {
  try {
    const response = await API.comAPI.getCodeCategoryDetail(getCodeCategoryDetailCondi.value, urlPaths.value);
    updateCodeCategoryData.value = response.data;

  } catch (error) {
    console.log(error);
  }
}

const updateCodeCategory = async () => {
  if (checkRVBmsTcode() == false) {
    return;
  }
  updateCodeCategoryData.value.upid = loginStore.getUserLoginData.userid;
  updateCodeCategoryData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');
  API.comAPI
    .updateCodeCategory(updateCodeCategoryData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
        props.returnFunc();
      }
    })
    .catch(error => {
      console.log(error);
    });
    props.returnFunc();
}


// Popup
const closePopUp = () => {
  props.toggleFunc(props.args);
}

// Check Validation
const checkRVBmsTcode = () => {
  if (updateCodeCategoryData.value.tcodeid.length == 0) {
    alert("코드분류아이디를 입력해주세요.");
    return false;
  }
  if (updateCodeCategoryData.value.tcodeid.length > 6) {
    alert("코드분류아이디의 최대 길이는 6자입니다.");
    return false;
  }
  if (updateCodeCategoryData.value.tcodename != undefined && updateCodeCategoryData.value.tcodename.length > 100) {
    alert("코드분류명의 최대 길이는 100자입니다.");
    return false;
  }
  if (updateCodeCategoryData.value.description != undefined && updateCodeCategoryData.value.description.length > 500) {
    alert("코드설명의 최대 길이는 500자입니다.");
    return false;
  }

  return true;
}

</script>
