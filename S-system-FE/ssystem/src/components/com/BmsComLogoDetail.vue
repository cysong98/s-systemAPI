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
            <h5 class="h5">심볼/로고 상세</h5>
            <div class="action-buttons">
              <v-btn outlined @click="moveToBmsComlogoupdate(getLogoDetailData.orgid, getLogoDetailData.seq)">수정</v-btn>
              <v-btn outlined @click="moveToBmsComlogolist()">목록</v-btn>
            </div>
          </div>
        </section>
  
        <!-- 서명인 이미지 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">심볼/로고 이미지</h6>
          </div>
  
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>인영</th>
                <td>
                  <img id="img" :src="fileurl" alt="img" height="200" />
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>
  
        <!-- 사용자 정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">사용자 정보</h6>
          </div>
  
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>분류</th>
                <td>
                  <v-text-field :readonly="true" v-model="managegubun" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>부서</th>
                <td>
                  <v-text-field :readonly="true" v-model="getLogoDetailData.orgname" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <!-- <tr>
                <th>아이디</th>
                <td>
                  <v-text-field :readonly="true" v-model="getLogoDetailData.chiefid" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>이름</th>
                <td>
                  <v-text-field :readonly="true" v-model="getLogoDetailData.username" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr> -->
              <tr>
                <th>등록일</th>
                <td>
                  <v-text-field :readonly="true" v-model="getLogoDetailData.indt" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>수정일</th>
                <td>
                  <v-text-field :readonly="true" v-model="getLogoDetailData.updt" variant="outlined" hide-details="auto"></v-text-field>
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
  
  const name = ref('BmsComLogoDetail')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  const query = ref({});
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for getLogoDetail
  const getLogoDetailData = ref({})
  const getLogoDetailDefCondi = {
    orgid: "",
    seq: ""
  }
  const getLogoDetailCondi = ref({ ...getLogoDetailDefCondi })
  
  onBeforeMount(() => {
    query.value = route.query;
  })
  
  onMounted(async () => {
    await getLogoDetail();
  })

  
  const fileurl = ref("");
  const managegubun = ref("");
  const getLogoDetail = async () => {
  
    try {
      getLogoDetailCondi.value.orgid = query.value.orgid;
      getLogoDetailCondi.value.seq = query.value.seq;
      
      const response = await API.comAPI.getLogoDetail(getLogoDetailCondi.value, urlPaths.value);
      getLogoDetailData.value = response.data;
  
      if(getLogoDetailData.value.managegubun === "ORGE4")
        managegubun.value = "로고";
      else
        managegubun.value = "심볼";
  
      getLogoDetailData.value.indt = dayjs(response.data.indt).format('YYYY-MM-DD');
      if(response.data.updt === undefined || response.data.updt === "" || response.data.updt === null)
        getLogoDetailData.value.updt = "";
      else
        getLogoDetailData.value.updt = dayjs(response.data.updt).format('YYYY-MM-DD');
  
      /* Upload Image File */
      fileurl.value = response.data.filelist[0].fileurl;
  
    } catch (error) {
      console.log(error);
    }
  }
  
  
  /* ================== Move Function ================== */
  const moveToBmsComlogoupdate = (orgid, seq) => {
    router.push({
      name: "BmsComlogoupdate",
      query: {
        ...{ orgid: orgid },
        ...{ seq: seq }
      }
    });
  }
  
  // Move Function
  const moveToBmsComlogolist = () => {
    router.push({
      name: "BmsComlogolist"
    });
  }
  /* =================================================== */
  </script>