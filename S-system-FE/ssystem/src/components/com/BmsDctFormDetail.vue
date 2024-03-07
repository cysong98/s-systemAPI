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
      <h4 class="h4">시스템관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">서식 상세</h5>
          <div class="action-buttons">
            <v-btn outlined @click="moveToBmsDctformupdate(getFormDetailData.fileid)">수정</v-btn>
            <v-btn outlined @click="moveToBmsDctformlist()">목록</v-btn>
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
            <!-- <tr>
              <th>카테고리명</th>
              <td>
                <v-text-field :readonly="true" v-model="getFormDetailData.categoryname" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr> -->
            <tr>
              <th>서식명</th>
              <td>
                <v-text-field :readonly="true" v-model="getFormDetailData.formtitle" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>부서</th>
              <td>
                <v-text-field :readonly="true" v-model="getFormDetailData.deptname" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>서식순서</th>
              <td>
                <v-text-field :readonly="true" v-model="getFormDetailData.formorder" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td>
                <v-text-field :readonly="true" v-model="getFormDetailData.formcomment" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>서식파일</th>
              <td>
                <div class="box-flex">
                  <p style="font-size: 15px; font-weight: bold;">{{getFormDetailData.formname}} [{{ fileSize }} KB]</p>
                  <v-btn @click="downloadFile()" large >
                    <v-icon>mdi-download</v-icon>다운로드
                  </v-btn>
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

import { ref, onBeforeMount, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

const name = ref('BmsDctFormDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref();
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getFormDetail
const getFormDetailData = ref({})
const getFormDetailDefCondi = {
  fileid: ""
}
const getFormDetailCondi = ref({ ...getFormDetailDefCondi })

onBeforeMount(() => {
  query.value = route.query;
})

onMounted(async () => {
  await getFormDetail();
})


/* ============================= 첨부파일 ============================= */
const downloadFile = async() => {
  try {
    var fileInfo = {
      uploadPath: getFormDetailData.value.filelist[0].filepath,
      orgFilename: getFormDetailData.value.formname,
    };

    const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
    SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
  } catch (error) {
    console.log(error);
  }
}
/* =================================================================== */


const fileSize = ref("");
const getFormDetail = async () => {

  try {
    getFormDetailCondi.value.fileid = query.value.fileid;
    const response = await API.comAPI.getFormDetail(getFormDetailCondi.value, urlPaths.value);
    getFormDetailData.value = response.data;
    fileSize.value = (response.data.formsize / 1024).toFixed(2);
  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsDctformupdate = (item) => {
  router.push({
    name: "BmsDctformupdate",
    query: {
      ...{fileid: item}
    }
  });
}

// Move Function
const moveToBmsDctformlist = () => {
  router.push({
    name: "BmsDctformlist"
  });
}

</script>
