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
            <div>
                <br/><br/><br/>
                <pre style="font-size: 20px;">{{props.alertMsg}}</pre>
                <p style="font-size: 20px;">삭제를 원하시면 확인 버튼을 클릭하여 주십시오.</p>
            </div>
          </div>
        </div>
      </div>
  
      <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="deleteConfirm(false)">취소</v-btn>
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="deleteConfirm(true)">확인</v-btn>
      </div>
    </div>
  </template>
    
  <script setup>
  import console from "console";
  
  import dayjs from 'dayjs';
  import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect, resolveComponent } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { VDatePicker } from 'vuetify/labs/VDatePicker';
  import _ from 'lodash';
  
  const name = ref('BmsComOrgSelectt')
  const props = defineProps({
    alertMsg: String,
    returnFunc: Function
  })  
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
    
  // Return Function 
  const deleteConfirm = (isConfirm) => {
    props.returnFunc(isConfirm);
  };  
  
  </script>