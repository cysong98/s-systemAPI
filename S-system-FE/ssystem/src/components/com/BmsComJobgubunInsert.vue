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
      <div class="box-title">
        <h4 class="h4">조직관리</h4>
      </div>
  
      <div class="box-content">
        <section class="section sticky">
          <div class="box-title">
            <h5 class="h5">직위 등록</h5>
            <div class="action-buttons">
              <v-btn outlined @click="insertJobgubun()">저장</v-btn>
              <v-btn outlined @click="moveToBmsComjobgubunlist()">목록</v-btn>
            </div>
          </div>
        </section>
  
        <section class="section">
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th class="required">직류코드</th>
                <td>
                  <v-text-field
                    v-model="insertJobgubunData.jobgubun"
                    maxlength="50"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">직위코드</th>
                <td>
                  <v-text-field
                    v-model="insertJobgubunData.code"
                    maxlength="35"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">직위명</th>
                <td>
                  <v-text-field
                    v-model="insertJobgubunData.codename"
                    maxlength="100"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>
      </div>
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
  import { checkRV } from "@/utils/CheckRV.js";
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';
  
  const name = ref('BmsComJobgubunInsert')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for insertJobgubun
  const insertJobgubunData = ref({
      jobgubun: "",
      code: "",
      codename: "",
      inid: loginStore.getUserLoginData.userid,
      indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
  })
  // for checkDupId
  const getJobgubunDetailCondi = ref({
    jobgubun: "",
    code: "",
  })


  const checkDupId = async () => {
    try {
      getJobgubunDetailCondi.value.jobgubun = insertJobgubunData.value.jobgubun;
      getJobgubunDetailCondi.value.code = insertJobgubunData.value.code;
      const response = await API.comAPI.getJobgubunDetail(getJobgubunDetailCondi.value, urlPaths.value);
      if(response.data.code == undefined)
        return true;
      else
        return false;

    } catch (error) {
      console.log(error);
    }
  }


  const insertJobgubun = async () => {
    if (checkRVComJobgubun() == false)
      return;
    if(await checkDupId() == false) {
      alert("이미 사용중인 직위코드입니다.");
      insertJobgubunData.value.code = "";
      return;
    }

    API.comAPI
      .insertJobgubun(insertJobgubunData.value, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // alert("정상 등록되었습니다");
          moveToBmsComjobgubunlist();
        }
      })
      .catch(error => {
        console.log(error);
      });
  }
  
  
  // Move Function
  const moveToBmsComjobgubunlist = () => {
    router.push({
      name: "BmsComjobgubunlist"
    });
  }
  
  // Check Validation
  const checkRVComJobgubun = () => {
    if(insertJobgubunData.value.jobgubun.length == 0) {
      alert("직류코드를 입력해주세요.");
      return false;
    }
    if(insertJobgubunData.value.jobgubun.length > 50) {
      alert("직류코드의 최대 길이는 50자입니다.");
      return false;
    }
    if(insertJobgubunData.value.code.length == 0) {
      alert("직위코드을 입력해주세요.");
      return false;
    }
    if(insertJobgubunData.value.code.length > 35) {
      alert("직위코드의 최대 길이는 35자입니다.");
      return false;
    }
    if(insertJobgubunData.value.codename.length == 0) {
      alert("직위명을 입력해주세요.");
      return false;
    }
    if(insertJobgubunData.value.codename.length > 100) {
      alert("직위명의 최대 길이는 100자입니다.");
      return false;
    }

    return true;
  }
  
  </script>  