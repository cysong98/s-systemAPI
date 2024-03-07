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
      <h4 class="h4">시스템관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">서식 등록</h5>
          <div class="action-buttons">
            <v-btn @click="insertFormBIA(insertFormBIAData.userid)" outlined>저장</v-btn>
            <v-btn @click="moveToBmsDctformlist()" outlined>목록</v-btn>
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
              <th class="required">서식명</th>
              <td>
                <v-text-field v-model="insertFormBIAData.formtitle" maxlength="500" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">부서</th>
              <td>
                <div class="box-flex">
                  <v-text-field :readonly="true" v-model="insertFormBIAData.deptname" variant="outlined" hide-details="auto"></v-text-field>
                  <v-btn @click="toggleOrg()" class="magnify-solid" >
                    <v-icon></v-icon>검색
                  </v-btn>
                </div>
              </td>
            </tr>
            <tr>
              <th>서식순서</th>
              <td>
                <v-text-field v-model="insertFormBIAData.formorder" maxlength="5" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td>
                <v-text-field
                  v-model="insertFormBIAData.formcomment"
                  maxlength="4000"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">서식파일</th>
              <td>
                <v-file-input
                  @change="(e) => addFile(e)"
                  v-model="fileinfo"
                  label="첨부파일"
                  variant="outlined"
                  chips
                  show-size
                  hide-details="auto"
                ></v-file-input>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>
  </div>
  
  <v-dialog v-model="visibleOrg" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부서 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleOrg = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleOrg"
        :args="orgArgs"
        :toggleFunc="toggleOrg"
        :returnFunc="selectOrgList"
      ></BmsComOrgSelect>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFile } from "@/utils/File.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';

const name = ref('BmsDctFormInsert')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for insertFormBIA
const insertFormBIAData = ref({
    fileid: "",
    formid: "",
    formtitle: "",
    formname: "",
    formsize: "",
    formorder: "",
    inid: getUserLoginData.value.userid,
    indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
    deptid: "",
    deptname: "",
    formcomment: "",
    categoryid: null,
    fileInfo: [],
    indexComment: []
})


const visibleOrg = ref(false);
const orgArgs = ref("");
const toggleOrg = ref(() => {
  orgArgs.value = "singular";
  visibleOrg.value = !visibleOrg.value;
});

// return function
const selectOrgList = ref((retValue) => {
  toggleOrg.value();
  insertFormBIAData.value.deptid = retValue.orgid;
  insertFormBIAData.value.deptname = retValue.orgname;
});


/* ============================= 첨부파일 ============================= */

const addFile = async (e, idx) => {
  try {
    const files = e.target.files;
    for (const file of files) {
      if(!file.type.match(".hwp")){
        alert("한글 확장자만 업로드 가능합니다");
        fileinfo.value = [];
        break;
      };

      var newFile = {
        rawFile: file,
        file: "",
        fileOriginName: file.name,
      };

      const createdFile = await createFile(newFile.rawFile, newFile);
      insertFormBIAData.value.fileInfo = [];
      insertFormBIAData.value.fileInfo.push(createdFile);
      insertFormBIAData.value.formname = file.name;
    }
  } catch {
    return;
  }
}

/* =================================================================== */

const disable = ref(false);
const fileinfo = ref([]);
const insertFormBIA = async () => {
  if(disable.value == true) return;
  else disable.value = true;

  if (checkRVBmsDctForm() == false) 
    return;

  isloading.value = true;

  /* ===================== 파일 입력 ===================== */
  var insertSignBIAFormData = new FormData();

  // 추가된 것
  insertFormBIAData.value.indexComment = [];
  let fIdx = 0;

  for await(const f of insertFormBIAData.value.fileInfo) {
    insertFormBIAData.value.indexComment.push({
      fileIndex: fIdx++,
    });
    insertSignBIAFormData.append("files", f.rawFile);
  }

  insertSignBIAFormData.append(
    "requiredInsertFormBIA",
    new Blob([JSON.stringify(insertFormBIAData.value)], {
      type: "application/json",
    })
  );
  /* ==================================================== */

  API.comAPI
    .insertFormBIA(insertSignBIAFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        isloading.value = false;
        moveToBmsDctformlist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


// Move Function
const moveToBmsDctformlist = () => {
  router.push({
    name: "BmsDctformlist"
  });
}

// Check Validation
const checkRVBmsDctForm = () => {
  if(fileinfo.value.length == 0) {
    alert("서식 파일을 등록하세요");
    return false;
  } 
  if(insertFormBIAData.value.formtitle.length == 0) {
    alert("서식명을 입력하세요.");
    return false;
  }
  if(insertFormBIAData.value.formtitle.length > 500) {
    alert("서식명의 최대길이는 500자입니다.");
    return false;
  }
  if(insertFormBIAData.value.formname.length > 500) {
    alert("파일명의 최대길이는 500자입니다.");
    return false;
  }
  if(insertFormBIAData.value.formorder.length > 5) {
    alert("서식순서의 최대길이는 5자입니다.");
    return false;
  }
  if (insertFormBIAData.value.deptid.length == 0) {
    alert("부서를 선택해주세요.");
    return false;
  }
  if (insertFormBIAData.value.formcomment.length > 4000) {
    alert("비고의 최대 길이는 4000자입니다.");
    return false;
  }
  return true;
}

</script>