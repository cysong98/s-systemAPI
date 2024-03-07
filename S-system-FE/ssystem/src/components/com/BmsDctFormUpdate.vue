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
          <h5 class="h5">서식 수정</h5>
          <div class="action-buttons">
            <v-btn @click="updateFormBIA(updateFormBIAData.userid)" outlined>저장</v-btn>
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
                <v-text-field v-model="updateFormBIAData.formtitle" maxlength="500" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">부서</th>
              <td>
                <div class="box-flex">
                  <v-text-field :readonly="true" v-model="updateFormBIAData.deptname" variant="outlined" hide-details="auto"></v-text-field>
                  <v-btn @click="toggleOrg()" class="magnify-solid" >
                    <v-icon></v-icon>검색
                  </v-btn>
                </div>
              </td>
            </tr>
            <tr>
              <th>서식순서</th>
              <td>
                <v-text-field v-model="updateFormBIAData.formorder" maxlength="5" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td>
                <v-text-field
                  v-model="updateFormBIAData.formcomment"
                  maxlength="4000"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">서식파일</th>
              <td>
                <div v-if="hasNewFile == false" class="box-flex">
                  <v-text-field
                    :readonly="true"
                    v-model="updateFormBIAData.formname"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  <v-btn @click="toggleBtn()" class="magnify-solid">변경</v-btn>
                </div>
                <div v-else class="box-flex">
                  <v-file-input
                    @change="(e) => addFile(e)"
                    v-model="fileinfo"
                    label="첨부파일"
                    variant="outlined"
                    chips
                    show-size
                    hide-details="auto"
                  ></v-file-input>
                  <v-btn @click="toggleBtn()" class="magnify-solid">취소</v-btn>
                </div>
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
import { ref, onBeforeMount, onMounted, watch} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFile } from "@/utils/File.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';

const name = ref('BmsDctFormUpdate')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref();
const isloading = ref(false);
const hasNewFile = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateFormBIA
const updateFormBIAData = ref({})
// for getFormDetail
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
const addFile = async (e) => {
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
      updateFormBIAData.value.fileInfo = [];
      updateFormBIAData.value.fileInfo.push(createdFile);
      updateFormBIAData.value.formname = file.name;
    }
  } catch {
    return;
  }
}


const toggleBtn = ref(() => {
  hasNewFile.value = !hasNewFile.value;
  if(hasNewFile.value == false) {
    fileinfo.value = [];
  }
});

/* =================================================================== */

const getFormDetail = async () => {
  isloading.value = true;
  try {
    getFormDetailCondi.value.fileid = query.value.fileid;
    const response = await API.comAPI.getFormDetail(getFormDetailCondi.value, urlPaths.value);
    updateFormBIAData.value = response.data;

    if(response.data.formorder == undefined)
      updateFormBIAData.value.formorder = "";
    if(response.data.formcomment == undefined)
      updateFormBIAData.value.formcomment = "";

    updateFormBIAData.value.fileInfo = [];
    updateFormBIAData.value.indexComment = [];
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}


const fileinfo = ref([]);
const updateFormBIA = async () => {
  if(checkRVBmsDctForm() == false) {
    return;
  }
  isloading.value = true;

  // updateFormBIAData 정보 입력
  updateFormBIAData.value.upid = loginStore.getUserLoginData.userid;
  updateFormBIAData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');


  /* ===================== 파일 입력 ===================== */
  var updateFormBIAFormData = new FormData();

  // 추가된 것
  updateFormBIAData.value.indexComment = [];
  let fIdx = 0;

  for await(const f of updateFormBIAData.value.fileInfo) {
    updateFormBIAData.value.indexComment.push({
      // comment: f.comment,
      fileIndex: fIdx++,
    });
    updateFormBIAFormData.append("files", f.rawFile);
  }

  updateFormBIAFormData.append(
    "requiredUpdateFormBIA",
    new Blob([JSON.stringify(updateFormBIAData.value)], {
      type: "application/json",
    })
  );
  /* ==================================================== */

  API.comAPI
    .updateFormBIA(updateFormBIAFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
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
  if(updateFormBIAData.value.formtitle.length == 0) {
    alert("서식명을 입력하세요.");
    return false;
  }
  if(updateFormBIAData.value.formtitle.length > 500) {
    alert("서식명의 최대길이는 500자입니다.");
    return false;
  }
  if(updateFormBIAData.value.formname.length > 500) {
    alert("파일명의 최대길이는 500자입니다.");
    return false;
  }
  if(updateFormBIAData.value.formorder !== undefined && updateFormBIAData.value.formorder.length > 5) {
    alert("서식순서의 최대길이는 5자입니다.");
    return false;
  }
  if (updateFormBIAData.value.formcomment !== undefined && updateFormBIAData.value.formcomment.length > 4000) {
    alert("비고의 최대 길이는 4000자입니다.");
    return false;
  }
  return true;
}

</script>