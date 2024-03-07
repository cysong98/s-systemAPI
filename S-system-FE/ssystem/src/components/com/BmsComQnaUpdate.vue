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
          <h5 class="h5">Q&A 수정</h5>
          <div class="action-buttons">
            <v-btn outlined @click="updateQna()">저장</v-btn>
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
              <th class="required">제목</th>
              <td>
                <v-text-field
                  v-model="updateQnaData.title"
                  maxlength="100"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">내용</th>
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
import { ref, onBeforeMount, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import { textChanged, setQuillEditor } from "@/utils/QuillEditor.js";
import BmsComConfirmPopup from '@/components/com/BmsComConfirmPopup.vue';

const name = ref('BmsComQnaUpdate')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref()
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateQna
const updateQnaData = ref({})
// for getQnaDetail
const getQnaDetailDefCondi = {
  qnaid: "",
  role: "admin",
}
const getQnaDetailCondi = ref({ ...getQnaDetailDefCondi })
const manageflag = ref(false);

onBeforeMount(() => {
  query.value = route.query; 
  getQnaDetailCondi.value.qnaid = query.value.qnaid;
})

onMounted(async () => {
  await getQnaDetail();
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
})

onBeforeUnmount(() => {
  textChanged.value = false;
})

const getQnaDetail = async () => {
  isloading.value = true;
  try {
    const response = await API.comAPI.getQnaDetail(getQnaDetailCondi.value, urlPaths.value);
    updateQnaData.value = response.data;

    if(response.data.cont !== undefined) {
      setQuillEditor(updateQnaData.value, true);
    }
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}

const updateQna = async () => {
  isloading.value = true;
  updateQnaData.value.upid = loginStore.getUserLoginData.userid;
  updateQnaData.value.upname = loginStore.getUserLoginData.username;
  updateQnaData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');
  if(updateQnaData.value.cont !== "") {
    if(textChanged.value === false)
      updateQnaData.value.cont = JSON.parse(updateQnaData.value.cont);
    updateQnaData.value.cont = JSON.stringify(updateQnaData.value.cont);
  }

  if (checkRVBmsComQna() == false) {
    return;
  }
  
  API.comAPI
    .updateQna(updateQnaData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
        isloading.value = false;
        moveToBmsComqnadetail(updateQnaData.value.qnaid);
      }
    })
    .catch(error => {
      console.log(error);
    });
}


const urlArgs = ref("BmsComqnalist");
const visibleBmsComConfirmPopup = ref(false);
const toggleConfirmPopup = ref(() => {
  visibleBmsComConfirmPopup.value = !visibleBmsComConfirmPopup.value;
});


// Move Function
const moveToBmsComqnadetail = (qnaid) => {
  router.push({
    name: "BmsComqnadetail",
    query: {
      ...{qnaid: qnaid}
    }
  });
}

// Check Validation
const checkRVBmsComQna = () => {
  if (updateQnaData.value.title.length == 0) {
    alert("제목을 입력하세요");
    return false;
  }
  if (updateQnaData.value.title.length > 100){
    alert("제목의 최대 길이는 100자입니다.");
    return false;
  }
  if (
    !checkRV.checkRV(
      updateQnaData.value.fileyn,
      false,
      "checkStrLen",
      "첨부파일여부 형식을 확인하세요.",
      1
    )
  )
    return false;
  
  return true;
}

</script>

<style scoped>
  #editor-container { 
    height: 550px;
  }
</style>