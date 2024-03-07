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
            <h5 class="h5">직급 등록</h5>
            <div class="action-buttons">
              <v-btn outlined @click="insertGrade()">저장</v-btn>
              <v-btn outlined @click="moveToBmsComgradelist()">목록</v-btn>
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
                <th class="required">직급코드</th>
                <td>
                  <v-text-field
                    v-model="insertGradeData.grade"
                    maxlength="35"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">직급명</th>
                <td>
                  <v-text-field
                    v-model="insertGradeData.gradename"
                    maxlength="100"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직종</th>
                <td>
                  <v-text-field
                    v-model="insertGradeData.jobtype"
                    maxlength="50"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직류</th>
                <td>
                  <v-text-field
                    v-model="insertGradeData.jobgubun"
                    maxlength="50"
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
  import { ref, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';
  
  const name = ref('BmsComGradeInsert')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for insertGradeBIA
  const insertGradeData = ref({
      grade: "",
      gradename: "",
      jobtype: "",
      jobgubun: "",
      inid: loginStore.getUserLoginData.userid,
      indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
  })
  // for checkDupId
  const getGradeDetailCondi = ref({
    grade: ""
  })


  const checkDupId = async () => {
    try {
      getGradeDetailCondi.value.grade = insertGradeData.value.grade;
      const response = await API.comAPI.getGradeDetail(getGradeDetailCondi.value, urlPaths.value);
      if(response.data.grade == undefined)
        return true;
      else
        return false;

    } catch (error) {
      console.log(error);
    }
  }


  const insertGrade = async () => {
    if (checkRVComGrade() == false)
      return;
    if(await checkDupId() == false) {
      alert("이미 사용중인 직급코드입니다.");
      insertGradeData.value.grade = "";
      return;
    }

    API.comAPI
      .insertGrade(insertGradeData.value, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // alert("정상 등록되었습니다");
          moveToBmsComgradelist();
        }
      })
      .catch(error => {
        console.log(error);
      });
  }
  
  
  // Move Function
  const moveToBmsComgradelist = () => {
    router.push({
      name: "BmsComgradelist"
    });
  }
  
  // Check Validation
  const checkRVComGrade = () => {
    if(insertGradeData.value.grade.length == 0) {
      alert("직급코드를 입력해주세요.");
      return false;
    }
    if(insertGradeData.value.grade.length > 35) {
      alert("직급코드의 최대 길이는 35자입니다.");
      return false;
    }
    if(insertGradeData.value.gradename.length == 0) {
      alert("직급명을 입력해주세요.");
      return false;
    }
    if(insertGradeData.value.gradename.length > 100) {
      alert("직급명의 최대 길이는 100자입니다.");
      return false;
    }

    return true;
  }
  
  </script>  