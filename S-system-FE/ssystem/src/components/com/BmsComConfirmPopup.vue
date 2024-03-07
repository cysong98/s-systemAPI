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
          <br/><br/>
          <pre style="font-size: 25px;">작성 내역이 저장되지 않았습니다.</pre>
          <p style="font-size: 25px;">목록으로 이동하시겠습니까?</p>
        </div>
      </div>
    </div>
  </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopup()">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="moveToArgs()">확인</v-btn>
    </div>
  </div>
</template>
    
<script setup>
import console from "console";
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';

const name = ref('BmsComOrgSelectt')
const props = defineProps({
args: String,
toggleFunc: Function
})  
const route = useRoute()
const router = useRouter()
const urlPaths =  ref("");


const closePopup = () => {
  props.toggleFunc();
};

// Move Function
const moveToArgs = () => {
  closePopup();
  router.push({
    name: props.args
  });
}
  
</script>