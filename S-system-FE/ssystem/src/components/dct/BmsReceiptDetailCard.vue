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
  <div class="page" :style="{width: contentWidth}">
    <BmsDctCreateCardTemp :data="getDctRdocData" />
  </div>
</template>

<script setup>
  import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)
  
  import dayjs from 'dayjs';
  import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { VDatePicker } from 'vuetify/labs/VDatePicker';
  import { API } from "@/api";
  import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
  import { getValueByKey } from "@/utils/Utils.js";
  import {
    setQueries,
    setCondiChanged,
    setCondiClear,
    changeCondi,
    checkCondiWithoutPageNum,
  } from "@/utils/Query.js";
  import BmsDctCreateCardTemp from "./BmsDctCreateCardTemp.vue";
  import { storeToRefs } from 'pinia';
  import { useLoginStore } from '/src/store/Login';
  import DctPathPopup from "./DctPathPopup.vue";

const name = ref("BmsReceiptDetailCard");
const params = ref({});
const query = ref({});

const props = defineProps({
  args: Object,
  // args: String,
  toggleFunc: Function,
  returnFunc: Function,
});
const args = ref({});
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// docid를 넘기기 위한 변수 선언
const docidForProps = ref("");

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(async() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  docidForProps.value = query.value.docid;
  await getDctRdoc();
});

onMounted(async () => {
  // await getDctRdoc();
});


const getDctRdocData = ref({});
const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getTempCreateCardList(
      {docid: query.value.docid,
      loginuserid: loginStore.getUserLoginData.userid,
      enfdocid: route.query.enfdocid
      },
      urlPaths.value
    );
    getDctRdocData.value = response.data;
    getDctRdocData.value.enfdocid = query.value.enfdocid;
    getDctRdocData.value.page = query.value.page;
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};

watchEffect(() => {});

</script>

<style lang="scss" scoped></style>