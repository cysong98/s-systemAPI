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
          <h5 class="h5">FAQ 상세</h5>
          <div class="action-buttons">
            <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="moveToBmsComfaqupdate({faqid: getFaqDetailData.faqid})">수정</v-btn>
            <v-btn outlined @click="moveToBmsComfaqlist()">목록</v-btn>
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
              <th class="text-center">공개대상</th>
              <td>
                <v-text-field
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                > {{ deptlist }} </v-text-field>
              </td>
            </tr>
            <tr>
              <th class="text-center">제목</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getFaqDetailData.title"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="text-center">내용</th>
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
import { ref, onBeforeMount, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import { setQuillEditorForDetail } from '@/utils/QuillEditor.js';

const name = ref('BmsComFaqDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref()
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getFaqDetail
const getFaqDetailData = ref({})
const getFaqDetailDefCondi = {
  faqid: "",
  role: "user",
}
const getFaqDetailCondi = ref({ ...getFaqDetailDefCondi })
const deptlist = ref("");
const manageflag = ref(false);
onBeforeMount(() => {
  query.value = route.query;
  getFaqDetailCondi.value.faqid = query.value.faqid;
})

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  await getFaqDetail();
})


const getFaqDetail = async () => {
  isloading.value = true;
  try {
    const response = await API.comAPI.getFaqDetail(getFaqDetailCondi.value, urlPaths.value);
    getFaqDetailData.value = response.data;
    response.data.deptlist.map((item) => {
      deptlist.value = deptlist.value + " " + item.deptname;
    });
    if(response.data.cont !== undefined) {
      setQuillEditorForDetail(getFaqDetailData.value);
    }
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsComfaqlist = () => {
  router.push({
    name: "BmsComfaqlist"
  });
}

// Move Function
const moveToBmsComfaqupdate = (item) => {
  router.push({
    name: "BmsComfaqupdate",
    query: {
      ...{faqid: item.faqid},
    },
  });
};
</script>


<style scoped>
  #editor-container { 
    height: 500px;
    padding-bottom: 10px;
    padding-top: 10px;
  }
</style>