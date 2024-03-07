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
      <div class="col" style="text-align: center;">
        <div style="font-size: 20px;">
          <br/>
          <p>임시저장 하시겠습니까?</p>
        </div>
      </div>
    </div>
  </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopup">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="insertReuseDraft">확인</v-btn>
    </div>
  </div>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>
    
<script setup>
import console from "console";
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import _ from 'lodash';

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('UseTempPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function
})  
const route = useRoute()
const router = useRouter()
const urlPaths =  ref("");
const isloading = ref(false);
const args = ref({})

onMounted(() => {
  args.value = props.args
})

watchEffect(() => {
  args.value = props.args
})

const closePopup = () => {
  props.toggleFunc();
};

// 재사용 임시저장
const insertReuseDraft = async () => {

  isloading.value = true;

  // bms_dct_rdoc table
  args.value.tempflag = "Y";
  args.value.state = "DRDS1";

  // bms_dct_path table
  args.value.pathList.map(item=> item.pathstate = "DCST1");

  // bms_dct_appr_recv table
  args.value.recvList.map(item=>item.apprrecvtempflag = "Y");

  var insertReuseDraftForm = new FormData(); 
  args.value.files.forEach((file) => {
    insertReuseDraftForm.append('draftAttach', file);
  });
  
  args.value.refList.forEach((ref)=>{
    if(ref.file){
      insertReuseDraftForm.append('refAttach', ref.file);
    }
  });
  const {files, ...rest} = args.value;
  const textData = Object.assign({}, rest);

  insertReuseDraftForm.append(
    "reqInsertReuseDraft",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );

  API.dctAPI
    .insertReuseDraft(insertReuseDraftForm, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("임시 저장되었습니다");
          moveToBmsDctTemplist();
      }
    })
    .catch(error => {
      isloading.value = false;
      console.log(error);
      alert("Server Error");
    });

};
  
const moveToBmsDctTemplist = () => {
  let arr = ['비밀문서', '기안', '임시저장함'];

  breadcrumbs.value.activeLink = arr;
  topMenu.value.data = "B";

  router.push({
    name: "BmsDcttemplist",
  });
};

</script>