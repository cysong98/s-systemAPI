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
            <h5 class="h5">심볼/로고 등록</h5>
            <div class="action-buttons">
              <v-btn outlined @click="insertLogoBIA()">저장</v-btn>
              <v-btn outlined @click="moveToBmsComlogolist()">목록</v-btn>
            </div>
          </div>
        </section>
  
        <!-- 서명인 이미지 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">심볼/로고 이미지</h6>
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
  
        <!-- 사용자 정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">사용자 정보</h6>
          </div>
  
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th class="required">분류</th>
                <td>
                  <v-select
                    label="분류"
                    v-model="insertLogoBIAData.managegubun"
                    :items="comboData"
                    item-title="view"
                    item-value="key"
                    variant="outlined"
                    hide-details="auto"
                  />
                </td>
              </tr>
              <tr>
                <th class="required">부서</th>
                <td>
                  <div class="box-flex">
                    <v-text-field :readonly="true" v-model="orgname" variant="outlined" hide-details="auto"></v-text-field>
                    <v-btn @click="toggleOrg()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>등록일</th>
                <td>
                  <div>
                    <v-text-field :readonly="true" v-model="insertLogoBIAData.indt" variant="outlined" hide-details="auto"></v-text-field>
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
          :args="logoArgs"
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
  import { ref, watch} from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { createFile } from "@/utils/File.js";
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';
  import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';
  
  const name = ref('BmsComLogoInsert')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  const isloading = ref(false);
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for insertLogoBIA
  const insertLogoBIAData = ref({
    orgid: "",
    seq: "",
    managegubun: "ORGE4",
    fileid: "",
    etc: "로고",
    isuse: "1",
    isdefault: "1",
    inid: loginStore.getUserLoginData.userid ,
    indt: dayjs(new Date()).format('YYYY-MM-DD'),
    fileInfo: [],
    indexComment: []
  })
  
  
  const fileinfo = ref([]);
  watch(fileinfo, () => {
    if(fileinfo.value.length == 0) {
      let preview = document.getElementById("img");
      preview.setAttribute("src", "");
    }
  })
  
  // combo box
  const comboData = [
    { key: "ORGE4", view: "로고"},
    { key: "ORGE5", view: "심볼"},
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
        insertLogoBIAData.value.fileInfo = [];
        insertLogoBIAData.value.fileInfo.push(createdFile);
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
    }
  }
  
  /* =================================================================== */
  
  
  const checkAlreadySaved = ref({
    orgid: "",
    managegubun: "",
  });
  const insertLogoBIA = async () => {
    if (checkRVComOrgDetailInfo() == false) {
      return;
    }
  
    isloading.value = true;
    // insertLogoBIAData 입력 정보
    insertLogoBIAData.value.indt = dayjs(new Date()).format('YYYY-MM-DD');
    if(insertLogoBIAData.value.managegubun === "ORGE4")
      insertLogoBIAData.value.etc = "로고";
    else
      insertLogoBIAData.value.etc = "심볼";

    // 이미 저장한 적이 있는지 체크
    checkAlreadySaved.value.orgid = insertLogoBIAData.value.orgid;
    checkAlreadySaved.value.managegubun = insertLogoBIAData.value.managegubun;
    const response = await API.comAPI.getSignDetail(checkAlreadySaved.value, urlPaths.value);
    
    if(response.data !== "") {
      alert(`${orgname.value}의 ${insertLogoBIAData.value.etc}에는 이미 등록된 이미지가 존재합니다.`);
      isloading.value = false;
      return;
    }
  
    /* ===================== 파일 입력 ===================== */
    var insertLogoBIAFormData = new FormData();
  
    // 추가된 것
    insertLogoBIAData.value.indexComment = [];
    let fIdx = 0;
  
    for await(const f of insertLogoBIAData.value.fileInfo) {
      insertLogoBIAData.value.indexComment.push({
        fileIndex: fIdx++,
      });
      insertLogoBIAFormData.append("files", f.rawFile);
    }
  
    insertLogoBIAFormData.append(
      "requiredInsertLogoBIA",
      new Blob([JSON.stringify(insertLogoBIAData.value)], {
        type: "application/json",
      })
    );
    /* ==================================================== */
  
    
    API.comAPI
      .insertLogoBIA(insertLogoBIAFormData, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // alert("정상 등록되었습니다");
          isloading.value = false;
          moveToBmsComlogolist();
        }
      })
      .catch(error => {
        console.log(error);
      });
  }
  
  
  /* ======================== submenu ======================== */
  const userid = ref("");
  const username = ref("");
  const orgname = ref("");
  
  const logoArgs = ref("");
  const visibleOrg = ref(false);
  const toggleOrg = ref(() => {
    logoArgs.value = "singular";
    visibleOrg.value = !visibleOrg.value;
  });
  
  // return function
  const selectOrgList = ref(async (retValue) => {
    const response = await API.comAPI.getUserDetail({ userid: retValue.chiefid }, urlPaths.value);
    if(retValue.chiefid !== undefined){
      username.value = response.data[0].username;
      userid.value = retValue.chiefid;
    }
    else {
      username.value = "부서장이 존재하지 않습니다.";
      userid.value = "부서장이 존재하지 않습니다.";
    }
  
    toggleOrg.value();
    insertLogoBIAData.value.orgid = retValue.orgid;
    orgname.value = retValue.orgname;
  });
  /* ========================================================= */
  
  
  // Move Function
  const moveToBmsComlogolist = () => {
    router.push({
      name: "BmsComlogolist"
    });
  }
  

  // Check Validation
  const checkRVComOrgDetailInfo = () => {
    if(fileinfo.value.length == 0) {
      alert("이미지를 등록하세요");
      return false;
    }

    if (insertLogoBIAData.value.orgid == ''){
      alert("부서를 선택해주세요.");
      return false;
    }
  
    return true;
  }
  
  </script>