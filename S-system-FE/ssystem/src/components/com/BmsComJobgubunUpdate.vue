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
            <h5 class="h5">직위 수정</h5>
            <div class="action-buttons">
              <v-btn outlined @click="updateJobgubun()">저장</v-btn>
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
                <th>직류코드</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="updateJobgubunData.jobgubun"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직위코드</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="updateJobgubunData.code"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">직위명</th>
                <td>
                  <v-text-field
                    v-model="updateJobgubunData.codename"
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
  import { ref, onBeforeMount, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';
    
  const name = ref('BmsComJobgubunUpdate')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  const query = ref({jobgubun: "", code: ""});
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for updateJobgubunBIA
  const updateJobgubunData = ref({})
  // for getJobgubunDetailBIA
  const getJobgubunDetailDefCondi = {
    jobgubun: "",
    code: ""
  }
  const getJobgubunDetailCondi = ref({ ...getJobgubunDetailDefCondi })
  

  onBeforeMount(() => {
    query.value = route.query;
  })
  
  onMounted(async () => {
    await getJobgubunDetail();
  })
  
  
  const getJobgubunDetail = async () => {
    try {
      getJobgubunDetailCondi.value.jobgubun = query.value.jobgubun;
      getJobgubunDetailCondi.value.code = query.value.code;
      const response = await API.comAPI.getJobgubunDetail(getJobgubunDetailCondi.value, urlPaths.value);
      updateJobgubunData.value = response.data;
    } catch (error) {
      console.log(error);
    }
  }
  
  
  const updateJobgubun = async () => {
    if (checkRVComJobgubun() == false) {
      return;
    }
  
    updateJobgubunData.value.upid = loginStore.getUserLoginData.userid;
    updateJobgubunData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');
  
    API.comAPI
      .updateJobgubun
      (updateJobgubunData.value, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // alert("정상 갱신되었습니다");
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
    if(updateJobgubunData.value.codename.length == 0) {
      alert("직위명을 입력해주세요.");
      return false;
    }
    if(updateJobgubunData.value.codename.length > 100) {
      alert("직위명의 최대 길이는 100자입니다.");
      return false;
    }
  
    return true;
  }
  
  </script>  