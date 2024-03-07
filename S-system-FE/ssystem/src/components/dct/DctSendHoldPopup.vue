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
                <th>보류 의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea v-model="sendopinion"></textarea>
                  </span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
      
      <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA()">보류</v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from "dayjs";
import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { VDatePicker } from "vuetify/labs/VDatePicker";
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

const name = ref("DctSendHoldPopup");
const props = defineProps({
  // args: String,
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
});
const args = ref({});
// const data = ref("");

const route = useRoute();
const router = useRouter();
// for popup
const childReturnValue = ref({});
const urlPaths = ref("");
// for updateCardBIA
const updateCardBIAData = ref({});
const sendopinion = ref("");

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {});

onMounted(() => {
  args.value = props.args;
  updateCardBIAData.value.docid = args.value.docid;
});

watchEffect(() => {
  args.value = props.args;
});

const updateCardBIA = async () => {
  updateCardBIAData.value = args.value;
  updateCardBIAData.value.sendopinion = sendopinion.value;
  updateCardBIAData.value.state = 'DRDS5'; //발송안함(발송보류)
  updateCardBIAData.value.userid = getUserLoginData.value.userid;
  updateCardBIAData.value.docid = args.value.docid;

  API.dctAPI
    .updateCardHoldBIA(updateCardBIAData.value, urlPaths.value)
    .then((response) => {
      if (response.status == 200) {
        alert("요청하신 문서가 보류처리 되었습니다.");
        // closePopUp();
        moveToBmsDctsendCurrentList();
      }
    })
    .catch((error) => {
      alert("보류처리 실패");
      console.log(error);
    });
};

// // Move Function
const moveToBmsDctsendCurrentList = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
};

const confirmPopUp = () => {
  alert("확인되었습니다.");
  props.returnFunc(childReturnValue.value);
};

const zIndex = ref(1000);
const handlePopupClick = (event) => {
  if (event.target.closest(".window-modal")) {
    zIndex.value = bringPopupToFront("DctSendHoldPopup");
  }
};
</script>

<style lang="scss" scoped></style>
