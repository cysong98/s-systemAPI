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
          <h5 class="h5">관인 상세</h5>
          <div class="action-buttons">
            <v-btn outlined @click="moveToBmsComgwanupdate(getGwanDetailData.orgid, getGwanDetailData.seq)">수정</v-btn>
            <v-btn outlined @click="moveToBmsComgwanlist()">목록</v-btn>
        </div>
        </div>
      </section>

      <!-- 관인 이미지 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">관인 이미지</h6>
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
                <img v-if="fileurl !== ''" id="img" :src="fileurl" alt="img" height="200" />
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <!-- 관인 정보 -->
      <section class="section">
        <div class="box-title">
          <h6 class="h6">관인 정보</h6>
        </div>

        <v-table class="table-type-03">
          <colgroup>
            <col width="240">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th>관인명</th>
              <td>
                <v-text-field :readonly="true" v-model="getGwanDetailData.gwaninname" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>관인종류</th>
              <td>
                <v-select
                  :readonly="true"
                  v-model="gawningubun"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
            </tr>
            <tr>
              <th>등록일</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getGwanDetailData.indt"
                  variant="outlined"
                  hide-details="auto"
                  style="width: 150px;"
                />
              </td>
            </tr>
            <tr>
              <th>최초사용일</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getGwanDetailData.firstdt"
                  variant="outlined"
                  hide-details="auto"
                  style="width: 150px;"
                />
              </td>
            </tr>
            <tr>
              <th>등록사유</th>
              <td>
                <v-text-field :readonly="true" v-model="getGwanDetailData.regreason" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>관리부서</th>
              <td>
                <v-text-field :readonly="true" v-model="getGwanDetailData.orgname" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>관인크기</th>
              <td>
                <div class="box-flex">
                  <v-label>가로</v-label>
                  <v-text-field :readonly="true" v-model="width" variant="outlined" hide-details="auto"></v-text-field>
                  <v-label>px / </v-label>
                  <v-label>세로</v-label>
                  <v-text-field :readonly="true" v-model="height" variant="outlined" hide-details="auto"></v-text-field>
                  <v-label>px</v-label>
                </div>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td>
                <v-text-field :readonly="true" v-model="getGwanDetailData.regetc" variant="outlined" hide-details="auto"></v-text-field>
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

const name = ref('BmsComGwanDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref();
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getGwanDetail
const getGwanDetailData = ref({})
const getGwanDetailDefCondi = {
  orgid: "",
  seq: "",
}
const getGwanDetailCondi = ref({ ...getGwanDetailDefCondi })

onBeforeMount(() => {
  query.value = route.query;
})

onMounted(async () => {
  await getGwanDetail();
})


const fileurl = ref("");
const gawningubun = ref("");
const width = ref(""), height = ref("");
const getGwanDetail = async () => {
  try {
    getGwanDetailCondi.value.orgid = query.value.orgid;
    getGwanDetailCondi.value.seq = query.value.seq;

    const response = await API.comAPI.getGwanDetail(getGwanDetailCondi.value, urlPaths.value);
    getGwanDetailData.value = response.data;

    if(response.data.managegubun === "ORGE1")
      gawningubun.value = "직인";
    else
      gawningubun.value = "특수관인";

    getGwanDetailData.value.indt = dayjs(getGwanDetailData.value.indt).format('YYYY-MM-DD');
    if(getGwanDetailData.value.firstdt === "" || getGwanDetailData.value.firstdt === undefined)
      getGwanDetailData.value.firstdt = "";
    else
      getGwanDetailData.value.firstdt = dayjs(getGwanDetailData.value.firstdt).format('YYYY-MM-DD');

    width.value = response.data.filelist[0].filewidth;
    height.value = response.data.filelist[0].fileheight;

    /* Upload Image File */
    fileurl.value = response.data.filelist[0].fileurl;
    
  } catch (error) {
    console.log(error);
  }
}


/* ================== Move Function ================== */
const moveToBmsComgwanupdate = (orgid, seq) => {
  router.push({
    name: "BmsComgwanupdate",
    query: {
      ...{ orgid: orgid },
      ...{ seq: seq }
    }
  });
}

// Move Function
const moveToBmsComgwanlist = () => {
  router.push({
    name: "BmsComgwanlist"
  });
}
/* =================================================== */

</script>