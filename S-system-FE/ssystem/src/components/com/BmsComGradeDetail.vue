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
            <h5 class="h5">직급 상세</h5>
            <div class="action-buttons">
              <v-btn outlined @click="moveToBmsComgradeupdate(getGradeDetailData.grade)">수정</v-btn>
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
                <th>코드</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="getGradeDetailData.grade"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직급명</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="getGradeDetailData.gradename"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직종</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="getGradeDetailData.jobtype"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직류</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="getGradeDetailData.jobgubun"
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
  
  import { ref, onBeforeMount, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { API } from '@/api';
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js'; 
    
  const name = ref('BmsComGradeDetail')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  const query = ref({});
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for getGradeDetailBIA
  const getGradeDetailData = ref({})
  const getGradeDetailDefCondi = {
    grade: ""
  }
  const getGradeDetailCondi = ref({ ...getGradeDetailDefCondi })
  
  onBeforeMount(() => {
    query.value = route.query;
  })
  
  onMounted(async () => {
    await getGradeDetail();
  })
  
  
  const getGradeDetail = async () => {
    try {
      getGradeDetailCondi.value.grade = query.value.grade;
      const response = await API.comAPI.getGradeDetail(getGradeDetailCondi.value, urlPaths.value);
      getGradeDetailData.value = response.data;
  
    } catch (error) {
      console.log(error);
    }
  }
  
  
  // Move Function
  const moveToBmsComgradeupdate = (grade) => {
    router.push({
      name: "BmsComgradeupdate",
      query: {
        ...{grade: grade}
      },
    });
  }
  
  // Move Function
  const moveToBmsComgradelist = () => {
    router.push({
      name: "BmsComgradelist"
    });
  }
  
  </script>