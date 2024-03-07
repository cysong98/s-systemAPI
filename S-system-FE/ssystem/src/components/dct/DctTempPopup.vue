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
          <p v-if="props.args.flag == '1'" style="color: red;"><br>* 임시저장시 문서가 결재대기함에 유지됩니다.</p>
        </div>
      </div>
    </div>
  </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopup()">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="tempSave()">확인</v-btn>
    </div>
  </div>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>
    
<script setup>
import console from "console";
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import _ from 'lodash';

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('DctTempPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function
})  
const route = useRoute()
const router = useRouter()
const urlPaths =  ref("");
const isloading = ref(false);

const closePopup = () => {
  props.toggleFunc();
};

const tempSave = ref(() => {
  isloading.value = true;
  var updateTempFormData = new FormData();
  props.args.file.forEach((file) => {
    updateTempFormData.append('draftAttach', file);
  });

  props.args.data.refList.forEach((ref) => {
    if (ref.file) {
      updateTempFormData.append('refAttach', ref.file);
    }
  });

  updateTempFormData.append(
    "reqInsertTempList",
    new Blob([JSON.stringify(props.args.data)], {
      type: "application/json",
    })
  );

  API.dctAPI
    .insertTempList(updateTempFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        // alert("임시 저장되었습니다");
        if(props.args.flag == '0') {
          moveToBmsDctTemplist();
        } else {
          moveToBmsDctwaitlist();
        }
      }
    })
    .catch(error => {
      console.log(error);
      isloading.value = false;
      alert("Server Error");
    });

});
  
const moveToBmsDctTemplist = () => {
  let arr = ['비밀문서', '기안', '임시저장함'];

  breadcrumbs.value.activeLink = arr;

  router.push({
    name: "BmsDcttemplist",
  });
};
const moveToBmsDctwaitlist = () => {
  let arr = ['비밀문서', '결재', '결재대기함'];

  breadcrumbs.value.activeLink = arr;

  router.push({
    name: "BmsDctwaitlist",
  });
};

</script>