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
          <h5 class="h5">관인 수정</h5>
          <div class="action-buttons">
            <v-btn outlined @click="updateGwanBIA()">저장</v-btn>
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
                <img v-if="fileurl !== ''" id='img' :src="fileurl" alt="previewImg" height="200" />
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
                <v-text-field v-model="updateGwanBIAData.gwaninname" maxlength="100" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">관인종류</th>
              <td>
                <v-select
                  label="관인"
                  v-model="updateGwanBIAData.managegubun"
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
                    v-model="updateGwanBIAData.indt"
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
                    v-model="updateGwanBIAData.firstdt"
                    :min="updateGwanBIAData.indt"
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
                <v-text-field v-model="updateGwanBIAData.regreason" maxlength="200" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">관리부서</th>
              <td>
                <div class="box-flex">
                  <v-text-field :readonly="true" v-model="updateGwanBIAData.orgname" variant="outlined" hide-details="auto"></v-text-field>
                  <v-btn
                    @click="selectOrg"
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
                    v-model="width"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                  <v-label>px / </v-label>
                  <v-label>세로</v-label>
                  <v-text-field
                    :readonly="true"
                    v-model="height"
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
                <v-text-field v-model="updateGwanBIAData.regetc" maxlength="200" variant="outlined" hide-details="auto"></v-text-field>
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
import { ref, onBeforeMount, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';
import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';

const name = ref('BmsComGwanUpdate')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref({orgid: ""});
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateGwanBIA
const updateGwanBIAData = ref({})
// for getGwanDetail
const getGwanDetailDefCondi = {
  orgid: "",
  seq: ""
}
const getGwanDetailCondi = ref({ ...getGwanDetailDefCondi })

onBeforeMount(() => {
  query.value = route.query;
})

onMounted(async () => {
  await getGwanDetail();
})

const fileinfo = ref([]);
watch(fileinfo, () => {
  if(fileinfo.value.length == 0) {
    let preview = document.getElementById("img");
    preview.setAttribute("src", fileurl.value);
  }
})

// combo box
const comboData = [
  { key: "ORGE1", view: "직인"},
  { key: "ORGE3", view: "특수관인"},
];


/* ============================= 첨부파일 ============================= */
const addFile = async (e) => {
  try {
    const files = e.target.files;
    for (const file of files) {
      var newFile = {
        rawFile: file,
        file: "",
        fileOriginName: file.name,
      };
      const createdFile = await createFile(newFile.rawFile, newFile);
      updateGwanBIAData.value.fileInfo = [];
      updateGwanBIAData.value.fileInfo.push(createdFile);
      previewImage(file);
    }
  } catch {
    return;
  }
}

const previewImage = (item) => {
  let reader = new FileReader();
  reader.readAsDataURL(item);
  reader.onload = e => {
    let preview = document.getElementById("img");
    preview.setAttribute("src", e.target.result);

    var img = new Image();
    img.src = e.target.result;

    img.onload = () => {
      width.value = img.width;
      height.value = img.height;
}
  }
}

const downloadFile = async(file) => {
  try {
    var fileInfo = {
      uploadPath: file.filePath,
      orgFilename: file.fileOriginName,
    };

    const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
    SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
  } catch (error) {
    console.log(error);
  }
}
/* =================================================================== */


const fileurl = ref("");
const firstdt = ref("");
const width = ref(""), height = ref("");
const firstManagegubun = ref("");
const getGwanDetail = async () => {
  try {
    getGwanDetailCondi.value.orgid = query.value.orgid;
    getGwanDetailCondi.value.seq = query.value.seq;

    const response = await API.comAPI.getGwanDetail(getGwanDetailCondi.value, urlPaths.value);
    updateGwanBIAData.value = response.data;
    firstManagegubun.value = response.data.managegubun;

    width.value = response.data.filelist[0].filewidth;
    height.value = response.data.filelist[0].fileheight;

    updateGwanBIAData.value.indt = dayjs(updateGwanBIAData.value.indt).format('YYYY-MM-DD')
    if(updateGwanBIAData.value.firstdt === "" || updateGwanBIAData.value.firstdt === undefined)
      updateGwanBIAData.value.firstdt = "";
    else
      updateGwanBIAData.value.firstdt = dayjs(updateGwanBIAData.value.firstdt).format('YYYY-MM-DD');

    updateGwanBIAData.value.fileInfo = [];
    updateGwanBIAData.value.indexComment = [];

    /* Upload Image File */
    fileurl.value = response.data.filelist[0].fileurl;
    
  } catch (error) {
    console.log(error);
  }
}


const checkAlreadySaved = ref({
  orgid: "",
  managegubun: "",
});
const updateGwanBIA = async () => {
  if(fileinfo.value.length == 0)
    updateGwanBIAData.value.fileInfo = [];
  if(checkRVComOrgGwaninfo() == false) 
    return;
    
  isloading.value = true;
  if(updateGwanBIAData.value.managegubun === "ORGE1")
    updateGwanBIAData.value.etc = "관인(직인)";
  else
    updateGwanBIAData.value.etc = "특수관인";

  // 이미 저장한 적이 있는지 체크
  if(firstManagegubun.value !== updateGwanBIAData.value.managegubun) {
    checkAlreadySaved.value.orgid = updateGwanBIAData.value.orgid;
    checkAlreadySaved.value.managegubun = updateGwanBIAData.value.managegubun;
    const response = await API.comAPI.getGwanDetail(checkAlreadySaved.value, urlPaths.value);
    
    if(response.data !== "") {
      alert(`${updateGwanBIAData.value.orgname}의 ${updateGwanBIAData.value.etc}에는 이미 등록된 이미지가 존재합니다.`);
      isloading.value = false;
      return;
    }
  }
  
  // updateGwanBIAData 입력 정보
  updateGwanBIAData.value.filewidth = width;
  updateGwanBIAData.value.fileheight = height;

  updateGwanBIAData.value.upid = loginStore.getUserLoginData.userid;
  if(firstdt.value !== updateGwanBIAData.value.firstdt)
    updateGwanBIAData.value.firstdt = dayjs(updateGwanBIAData.value.firstdt).format('YYYY-MM-DD');


  /* ===================== 파일 입력 ===================== */
  var updateGwanBIAFormData = new FormData();

  // 추가된 것
  updateGwanBIAData.value.indexComment = [];
  let fIdx = 0;

  for await(const f of updateGwanBIAData.value.fileInfo) {
    updateGwanBIAData.value.indexComment.push({
      // comment: f.comment,
      fileIndex: fIdx++,
    });
    updateGwanBIAFormData.append("files", f.rawFile);
  }

  updateGwanBIAFormData.append(
    "requiredUpdateGwanBIA",
    new Blob([JSON.stringify(updateGwanBIAData.value)], {
      type: "application/json",
    })
  );
  /* ==================================================== */

  API.comAPI
    .updateGwanBIA(updateGwanBIAFormData, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
        isloading.value = false;
        moveToBmsComgwanlist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


/* =========================== submenu =========================== */
const gwanArgs = ref("");
const visibleOrg = ref(false);
const toggleOrg = ref(() => {
  visibleOrg.value = !visibleOrg.value;
});

// return function
const selectOrg = ref((retValue) => {
  gwanArgs.value = "singular";
  toggleOrg.value();
  updateGwanBIAData.value.orgid = retValue.orgid;
  updateGwanBIAData.value.manageorgid = retValue.orgid;
  updateGwanBIAData.value.orgname = retValue.orgname;
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
  if (updateGwanBIAData.value.gwaninname.length == 0){
    alert("관인명을 입력하세요.");
    return false;
  }
  if (updateGwanBIAData.value.gwaninname.length > 100){
    alert("관인명의 최대 길이는 100자입니다.");
    return false;
  }
  if (updateGwanBIAData.value.regreason !== undefined && updateGwanBIAData.value.regreason.length > 200){
    alert("등록사유의 최대 길이는 200자입니다.");
    return false;
  }
  if (updateGwanBIAData.value.orgid == ''){
    alert("관리부서를 선택해주세요.");
    return false;
  }
  if (updateGwanBIAData.value.regetc !== undefined && updateGwanBIAData.value.regetc.length > 200){
    alert("비고의 최대 길이는 200자입니다.");
    return false;
  }

  return true;
}

</script>