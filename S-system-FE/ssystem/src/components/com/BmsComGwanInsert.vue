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
      <h4 class="h4">조직관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">관인 등록</h5>
          <div class="action-buttons">
            <v-btn outlined @click="insertGwanBIA()">저장</v-btn>
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
              <th class="required">인영</th>
              <td>
                <img v-if="fileinfo.length !== 0" id="img" src="" alt="previewImg" height="200" />
              </td>
            </tr>
            <tr>
              <th>첨부파일</th>
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
              <th class="required">관인명</th>
              <td>
                <v-text-field
                  v-model="insertGwanBIAData.gwaninname"
                  maxlength="100"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">관인종류</th>
              <td>
                <v-select
                  label="카테고리"
                  v-model="insertGwanBIAData.managegubun"
                  :items="comboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
            </tr>
            <tr>
              <th>등록일</th>
              <td>
                <div>
                  <v-text-field
                    :readonly="true"
                    v-model="insertGwanBIAData.indt"
                    variant="outlined"
                    hide-details="auto"
                    style="width: 150px;"
                    required
                  />
                </div>
              </td>
            </tr>
            <tr>
              <th>최초사용일</th>
              <td>
                <div>
                  <v-text-field
                    type="date"
                    v-model="insertGwanBIAData.firstdt"
                    :min="insertGwanBIAData.indt"
                    variant="outlined"
                    hide-details="auto"
                    required
                  />
                </div>
              </td>
            </tr>
            <tr>
              <th>등록사유</th>
              <td>
                <v-text-field
                  v-model="insertGwanBIAData.regreason"
                  maxlength="200"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">관리부서</th>
              <td>
                <div class="box-flex">
                  <v-text-field
                    :readonly="true"
                    v-model="orgname"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  <v-btn
                    @click="toggleOrg()"
                    class="magnify-solid"
                  >
                    <v-icon></v-icon>검색
                  </v-btn>
                </div>
              </td>
            </tr>
            <tr>
              <th>관인크기</th>
              <td>
                <div class="box-flex">
                  <v-label>가로</v-label>
                  <v-text-field
                    :readonly="true"
                    v-model="insertGwanBIAData.filewidth"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  <v-label>px / </v-label>
                  <v-label>세로</v-label>
                  <v-text-field
                    :readonly="true"
                    v-model="insertGwanBIAData.fileheight"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  <v-label>px</v-label>
                </div>
              </td>
            </tr>
            <tr>
              <th>비고</th>
              <td>
                <v-text-field
                  v-model="insertGwanBIAData.regetc"
                  maxlength="200"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
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
        <v-toolbar-title>관리부서 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleOrg = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleOrg"
        :args="gwanArgs"
        :toggleFunc="toggleOrg"
        :returnFunc="selectOrg"
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
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFile } from "@/utils/File.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';

const name = ref('BmsComGwanInsert')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for insertGwanBIA
const insertGwanBIAData = ref({
    orgid: "",
    seq: "",
    managegubun: "ORGE1",
    fileid: "",
    gwaninname: "",
    regreason: "",
    regetc: "",
    manageorgid: "",
    inid: loginStore.getUserLoginData.userid,
    indt: dayjs(new Date()).format('YYYY-MM-DD'),
    etc: "",
    isuse: "1",
    isdefault: "1",
    filewidth: "",
    fileheight: "",
    fileInfo: [],
    indexComment: []
})

const checkAlreadySaved = ref({
  orgid: "",
  managegubun: "",
});


const fileinfo = ref([]);
watch(fileinfo, () => {
  if(fileinfo.value.length == 0) {
    let preview = document.getElementById("img");
    preview.setAttribute("src", "");
  }
})

// combo box
const comboData = [
  { key: "ORGE1", view: "직인"},
  { key: "ORGE3", view: "특수관인"},
  { key: "ORGEB", view: "생략인"},
];

/* ============================= 첨부파일 ============================= */
const addFile = async (e) => {
  try {
    const files = e.target.files;
    for (const file of files) {
      if(!file.type.match("image/.*")){
        alert("이미지 확장자만 업로드 가능합니다");
        fileinfo.value = [];
        break;
      };

      var newFile = {
        rawFile: file,
        file: "",
        fileOriginName: file.name,
      };
      const createdFile = await createFile(newFile.rawFile, newFile);
      insertGwanBIAData.value.fileInfo = [];
      insertGwanBIAData.value.fileInfo.push(createdFile);
      previewImage(file);
    }
  } catch {
    return;
  }
}

const previewImage = (file) => {
  let reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = (e) => {
    let preview = document.getElementById("img");
    preview.setAttribute("src", e.target.result);

    var img = new Image();
    img.src = e.target.result;

    img.onload = () => {
      insertGwanBIAData.value.filewidth = img.width;
      insertGwanBIAData.value.fileheight = img.height;
    }
  }
}

/* =================================================================== */


const insertGwanBIA = async () => {
  if (checkRVComOrgGwaninfo() == false) {
    return;
  }

  isloading.value = true;
  if(insertGwanBIAData.value.managegubun === "ORGE1")
    insertGwanBIAData.value.etc = "관인(직인)";
  else
    insertGwanBIAData.value.etc = "특수관인";

  // 이미 저장한 적이 있는지 체크
  checkAlreadySaved.value.orgid = insertGwanBIAData.value.orgid;
  checkAlreadySaved.value.managegubun = insertGwanBIAData.value.managegubun;
  const response = await API.comAPI.getGwanDetail(checkAlreadySaved.value, urlPaths.value);
  
  if(response.data !== "") {
    alert(`${orgname.value}의 ${insertGwanBIAData.value.etc}에는 이미 등록된 이미지가 존재합니다.`);
    isloading.value = false;
    return;
  }

  // insertGwanBIAData 입력 정보
  if(insertGwanBIAData.value.firstdt !== "" && insertGwanBIAData.value.firstdt !== null)
    insertGwanBIAData.value.firstdt = dayjs(insertGwanBIAData.value.firstdt).format('YYYY-MM-DD');

  insertGwanBIAData.value.isuse = "1";
  insertGwanBIAData.value.isdefault = "1";

  /* ===================== 파일 입력 ===================== */
  var insertGwanBIAFormData = new FormData();

  // 추가된 것
  insertGwanBIAData.value.indexComment = [];
  let fIdx = 0;

  for await(const f of insertGwanBIAData.value.fileInfo) {
    insertGwanBIAData.value.indexComment.push({
      // comment: f.comment,
      fileIndex: fIdx++,
    });
    insertGwanBIAFormData.append("files", f.rawFile);
  }

  insertGwanBIAFormData.append(
    "requiredInsertGwanBIA",
    new Blob([JSON.stringify(insertGwanBIAData.value)], {
      type: "application/json",
    })
  );
  /* ==================================================== */

  API.comAPI
    .insertGwanBIA(insertGwanBIAFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        isloading.value = false;
        moveToBmsComgwanlist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


/* =========================== submenu =========================== */
const orgname = ref("");
const gwanArgs = ref("");
const visibleOrg = ref(false);

const toggleOrg = ref(() => {
  gwanArgs.value = "singular";
  visibleOrg.value = !visibleOrg.value;
});

// return function
const selectOrg = ref((retValue) => {
  toggleOrg.value();
  insertGwanBIAData.value.orgid = retValue.orgid;
  insertGwanBIAData.value.manageorgid = retValue.orgid;
  orgname.value = retValue.orgname;
});
/* ============================================================== */


// Move Function
const moveToBmsComgwanlist = () => {
  router.push({
    name: "BmsComgwanlist"
  });
}

// Check Validation
const checkRVComOrgGwaninfo = () => {
  if(fileinfo.value.length === 0) {
    alert("관인 이미지를 등록하세요");
    return;
  }
  
  if (insertGwanBIAData.value.gwaninname.length === 0){
    alert("관인명을 입력하세요.");
    return false;
  }
  if (insertGwanBIAData.value.gwaninname.length > 100){
    alert("관인명의 최대 길이는 100자입니다.");
    return false;
  }
  if (insertGwanBIAData.value.regreason.length > 200){
    alert("등록사유의 최대 길이는 200자입니다.");
    return false;
  }
  if (insertGwanBIAData.value.orgid.length === 0){
    alert("관리부서를 선택해주세요.");
    return false;
  }
  if (insertGwanBIAData.value.regetc.length > 200){
    alert("비고의 최대 길이는 200자입니다.");
    return false;
  }

  return true;
}

</script>