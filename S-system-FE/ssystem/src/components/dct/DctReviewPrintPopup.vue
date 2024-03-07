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
      <v-table class="table-type-03">
        <colgroup>
          <col width="120">
          <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th>문서제목</th>
            <td>{{args.docttl}}</td>
          </tr>
          <tr>
            <th>기안문</th>
            <td>
              <div class="box-flex">
                <v-icon color="#35aee7">mdi-syllabary-hangul</v-icon>
                <span @click="clickFile(draftFile, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ draftFile.sfilename }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>붙임</th>
            <td>
              <div style="overflow-y: auto; max-height: 100px; margin-right: -16px;">
                <div class="box-flex" v-for="(fileData, idx) in fileList" :key="idx">
                  <v-icon :color="getFileIcon(getExtension(fileData.sfilename)).color">{{ getFileIcon(getExtension(fileData.sfilename)).icon }}</v-icon>
                  <span @click="clickFile(fileData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ fileData.sfilename }}</span>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th>담당자이름</th>
            <td>{{args.authorname}}</td>
          </tr>
          <tr>
            <th>담당자부서</th>
            <td>{{args.authordeptname}}</td>
          </tr>
          <tr>
            <th>담당자직위</th>
            <td>{{args.authordutyname}}</td>
          </tr>
        </tbody>
        
      </v-table>
      <div class="box-flex font-red">
        <span>* 첨부된 파일을 클릭 하여 인쇄 하여 주십시오.</span>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>

  </div>

  <!-- 문서 조회 팝업 -->
  <v-dialog v-model="visibleViewerPopup">
    <v-card>
      <div v-if="docloading" class="overlay_02">
        <div class="spinner_02"></div>
        <div class="progress_02"></div>
      </div>
      <v-toolbar>
        <v-toolbar-title>문서 조회</v-toolbar-title>
        <v-btn class="close" icon @click="visibleViewerPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <iframe :src="url" width="1200" height="840" style="padding: 10px;"></iframe>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { getExtension, getFileIcon, viewDocument, url } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';

const name = ref('DctReviewPrintPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})


const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
// for getReviewPrint
const getReviewPrintData = ref({})
const getReviewPrintDefCondi = {
   docid: ""
  }
const getReviewPrintCondi = ref({ ...getReviewPrintDefCondi })
// for selectDctFile
const checkedListSelectDctFile = ref([])
const selectDctFileLoader = ref(true)
const selectDctFileList = ref([])
const selectDctFileData = ref({})
const selectDctFileDefCondi = {
  docid: "",
  sortDirection: "ASC",
  sortItem: "docid"
}
const selectDctFileCondi = ref({ ...selectDctFileDefCondi })
const selectDctFileCondiCheck = ref({ value: { ...selectDctFileDefCondi }, flag: { ...selectDctFileDefCondi } })

/* ================ loginUser ================ */
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
/* ============================================== */
onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args

  await getReviewPrint();
  await selectDctFile();
})

watchEffect(() => {
  args.value = props.args
})

// eslint-disable-next-line
const getReviewPrint = async () => {

  try {
    getReviewPrintCondi.value.docid = args.value.docid;
    const response = await API.dctAPI.getReviewPrint(getReviewPrintCondi.value, urlPaths.value);
    getReviewPrintData.value = response.data;
    getReviewPrintData.value = response.data

  } catch (error) {
    console.log(error);
  }
}

// 파일 변수
const fileList = ref([]); // 붙임파일
const draftFile = ref({}); // 기안문파일

const selectDctFile = async () => {
  selectDctFileLoader.value = true;
  try {
    // selectDctFileCondi.value.docid = args.value.value.docid;
    selectDctFileCondi.value.docid = args.value.docid;
    const response = await API.dctAPI.selectDctFile({ ...selectDctFileCondi.value }, urlPaths.value);
    selectDctFileList.value = response.data.list;
    selectDctFileLoader.value = false;
    selectDctFileData.value = response.data;
    setCondiClear(selectDctFileCondiCheck.value, selectDctFileCondi.value)

    for (let i = 0; i < response.data.length; i++) {
      if (response.data[i].gubun == 'DFT02') { // 붙임파일만
        fileList.value.push(response.data[i]);
      } else {
        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if(response.data[i].gubun == 'DFT01') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          draftFile.value = response.data[i];
        }
      }
    }

  } catch (error) {
    console.log(error);
  }
}


// Popup
const closePopUp = () => {
  props.toggleFunc();
}

const clickFile = (file, id) => {
  toggleViewerPopup(file, id);
  args.value.printflag = "Y";
}


// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  docloading.value = true;
  visibleViewerPopup.value = !visibleViewerPopup.value;
  await viewDocument(file, userid, 'N,N', 'Y');
  document.querySelector('iframe').addEventListener('load', function() {
    docloading.value = false;
  });
}

</script>

<style lang="scss" scoped>
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-cly1{text-align:left;vertical-align:middle}
.tg .tg-ceoq{background-color:#DDEBF7;text-align:left;vertical-align:middle}
.tg .tg-m6kf{background-color:#DDEBF7;border-color:inherit;text-align:left;vertical-align:middle}
.tg .tg-nrix{text-align:center;vertical-align:middle}
</style>
