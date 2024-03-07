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
      <h4 class="h4">게시판관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">FAQ 등록</h5>
          <div class="action-buttons">
            <v-btn outlined @click="insertFaqBIA()">저장</v-btn>
            <v-btn outlined @click="toggleConfirmPopup()">목록</v-btn>
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
              <th class="text-center required">공개대상</th>
              <td>
                <div class="box-flex2">
                  <v-checkbox
                    v-model="allDeptChecked"
                    label="전체"
                    hide-details="auto"
                  ></v-checkbox>
                  <div class="box-select">
                    <v-text-field
                      :readonly="true"
                      v-model="deptlist"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn class="magnify-solid" @click="toggleFaqOrgList()">
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th class="text-center required">제목</th>
              <td>
                <v-text-field
                  v-model="insertFaqBIAData.title"
                  maxlength="100"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="text-center required">내용</th>
              <td>
                <div id="editor-container">
                  <div id="quill-editor"> </div>
                </div>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>
  </div>
  
  <v-dialog v-model="visibleBmsComFaqOrgList" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>공개대상 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComFaqOrgList = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleBmsComFaqOrgList"
        :args="faqArgs"
        :toggleFunc="toggleFaqOrgList"
        :returnFunc="selectDeptList"
      ></BmsComOrgSelect>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleBmsComConfirmPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title></v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComConfirmPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComConfirmPopup
        v-if="visibleBmsComConfirmPopup"
        :args="urlArgs"
        :toggleFunc="toggleConfirmPopup"
      ></BmsComConfirmPopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';
import { API } from '@/api';
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';
import BmsComConfirmPopup from '@/components/com/BmsComConfirmPopup.vue';
import { setQuillEditor } from '@/utils/QuillEditor.js';

const name = ref('BmsComFaqInsert')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for insertFaqBIA
const insertFaqBIAData = ref({
  faqid: "",
  title: "",
  cont: "",
  deptlist: [],
  inid: loginStore.getUserLoginData.userid,
  inname: loginStore.getUserLoginData.username,
  indt: "",
})

const allDeptChecked = ref(false);
const visibleBmsComFaqOrgList = ref(false);
const visibleBmsComConfirmPopup = ref(false);
const deptlist = ref("");


onMounted(() => {
  setQuillEditor(insertFaqBIAData.value);
  allDeptChecked.value = true;
})

watch(allDeptChecked, async (allDeptChecked) => {
  if(allDeptChecked == true) {
    deptlist.value = "전체";
    insertFaqBIAData.value.deptlist = [{deptid: "전체", deptname: "전체"}];
  }
  else {
    deptlist.value = "";
    insertFaqBIAData.value.deptlist = [];
  }
});


// Toggle Function
const faqArgs = ref(""), urlArgs = ref("BmsComfaqlist");
const toggleFaqOrgList = ref(() => {
  visibleBmsComFaqOrgList.value = !visibleBmsComFaqOrgList.value;
  faqArgs.value = "plural";
  allDeptChecked.value = false;
});
const toggleConfirmPopup = ref(() => {
  visibleBmsComConfirmPopup.value = !visibleBmsComConfirmPopup.value;
});

const selectDeptList = ref((retValue) => {
  toggleFaqOrgList.value();
  insertFaqBIAData.value.deptlist = retValue;
  deptlist.value = "";
  retValue.map((item) => {
    deptlist.value = deptlist.value + " " + item.deptname;
  });
})


const insertFaqBIA = async () => {
  isloading.value = true;
  if(insertFaqBIAData.value.cont !== "")
    insertFaqBIAData.value.cont = JSON.stringify(insertFaqBIAData.value.cont);

  if (checkRVBmsComFaq() == false) {
    isloading.value = false;
    return;
  }

  const now = dayjs(new Date()).format('YYYYMMDDHHmmss');
  insertFaqBIAData.value.indt = now;
  API.comAPI
    .insertFaqBIA(insertFaqBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        isloading.value = false;
        moveToBmsComfaqlist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


// Move Function
const moveToBmsComfaqlist = () => {
  router.push({
    name: "BmsComfaqlist"
  });
}


// Check Validation
const checkRVBmsComFaq = () => {
  if(insertFaqBIAData.value.deptlist.length == 0) {
    alert("공개대상을 입력하세요.");
    return false;
  }

  if(insertFaqBIAData.value.title.length == 0) {
    alert("제목을 입력하세요.");
    return false;
  }if (insertFaqBIAData.value.title.length > 100){
    alert("제목의 최대 길이는 100자입니다.");
    return false;
  }

  if(insertFaqBIAData.value.cont.length == 0) {
    alert("내용을 입력하세요.");
    return false;
  }

  return true;
}

</script>

<style scoped>
  #editor-container { 
    height: 520px;
  }
</style>