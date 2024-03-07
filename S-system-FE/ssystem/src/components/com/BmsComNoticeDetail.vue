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
    <div v-if="isloading === true" class="overlay">
      <div class="spinner"></div>
    </div>
    <div class="box-title">
      <h4 class="h4">게시판{{manageflag === true ? '관리' : ''}}</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">공지사항 상세</h5>
          <div class="action-buttons">
            <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="moveToBmsComnoticeupdate({ntcid: getNoticeDetailData.ntcid})">수정</v-btn>
            <v-btn outlined @click="moveToBmsComnoticelist()">목록</v-btn>
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
              <th>공지기간</th>
              <td :readonly="true">
                <v-text-field
                  :readonly="true"
                  v-model="noticelist"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
            </tr>
            <tr>
              <th>공개대상</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="deptlist"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>제목</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getNoticeDetailData.title"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>내용</th>
              <td>
                <div id="editor-container">
                  <div id="quill-editor"></div>
                </div>
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
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import { setQuillEditorForDetail } from '@/utils/QuillEditor.js'

const name = ref('BmsComNoticeDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref()
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getNoticeDetail
const getNoticeDetailData = ref({})
const getNoticeDetailDefCondi = {
  ntcid: "",
  role: "user",
}
const getNoticeDetailCondi = ref({ ...getNoticeDetailDefCondi })
const deptlist = ref("");
const noticelist = ref("");
const manageflag = ref(false);
onBeforeMount(() => {
  query.value = route.query;
  getNoticeDetailCondi.value.ntcid = query.value.ntcid;
})

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  await getNoticeDetail();
})


const getNoticeDetail = async () => {
  isloading.value = true;
  try {
    const response = await API.comAPI.getNoticeDetail({...getNoticeDetailCondi.value}, urlPaths.value);
    getNoticeDetailData.value = response.data;
    response.data.deptlist.map((item) => {
      deptlist.value = deptlist.value + " " + item.deptname;
    });

    noticelist.value = dayjs(getNoticeDetailData.value.sdate).format('YYYY-MM-DD') + " ~ ";
    if(getNoticeDetailData.value.edate !== undefined)
      noticelist.value += dayjs(getNoticeDetailData.value.edate).format('YYYY-MM-DD');

    if(response.data.cont !== undefined) {
      getNoticeDetailData.value.cont = response.data.cont;
      setQuillEditorForDetail(getNoticeDetailData.value);
    }                        
  } catch (error) {
    console.log(error);
  }
  isloading.value = false;
}


// Move Function
const moveToBmsComnoticelist = () => {
  router.push({
    name: "BmsComnoticelist"
  });
}

// Move Function
const moveToBmsComnoticeupdate = (item) => {
  router.push({
    name: "BmsComnoticeupdate",
      query: {
        ...{ntcid: item.ntcid}
      }
    });
}

</script>


<style scoped>
  #editor-container { 
    height: 470px;
    padding-bottom: 10px;
    padding-top: 10px;
  }
</style>