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
            <h5 class="h5">심볼/로고 수정</h5>
            <div class="action-buttons">
              <v-btn outlined @click="updateLogoBIA()">저장</v-btn>
              <v-btn outlined @click="moveToBmsComlogolist()">목록</v-btn>
            </div>
          </div>
        </section>
  
        <!-- 심볼/로고 이미지 -->
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
                    v-model="updateLogoBIAData.managegubun"
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
                    <v-text-field :readonly="true" v-model="updateLogoBIAData.orgname" variant="outlined" hide-details="auto"></v-text-field>
                  </div>
                </td>
              </tr>
              <tr>
                <th>수정일</th>
                <td>
                  <div>
                    <v-text-field :readonly="true" v-model="updateLogoBIAData.updt" variant="outlined" hide-details="auto"></v-text-field>
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
  import { ref, onBeforeMount, onMounted, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';
  
  const name = ref('BmsComLogoUpdate')
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  const query = ref({orgid: ""});
  const isloading = ref(false);
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for updateLogoBIA
  const updateLogoBIAData = ref({})
  // for getLogoDetail
  const getLogoDetailData = ref({})
  const getLogoDetailDefCondi = {
    orgid: "",
    seq: ""
  }
  const getLogoDetailCondi = ref({ ...getLogoDetailDefCondi })
  
  onBeforeMount(() => {
    query.value = route.query;
  })
  
  onMounted(async () => {
    await getLogoDetail();
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
    { key: "ORGE4", view: "로고"},
    { key: "ORGE5", view: "심볼"},
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
        updateLogoBIAData.value.fileInfo = [];
        updateLogoBIAData.value.fileInfo.push(createdFile);
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
  const firstManagegubun = ref("");
  const getLogoDetail = async () => {
  
    try {
      getLogoDetailCondi.value.orgid = query.value.orgid;
      getLogoDetailCondi.value.seq = query.value.seq;
  
      const response = await API.comAPI.getLogoDetail(getLogoDetailCondi.value, urlPaths.value);
      updateLogoBIAData.value = response.data;
      firstManagegubun.value = response.data.managegubun;

      updateLogoBIAData.value.updt = dayjs(new Date()).format('YYYY-MM-DD');
      updateLogoBIAData.value.fileInfo = [];
      updateLogoBIAData.value.indexComment = [];
  
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
  const updateLogoBIA = async () => {
    if(fileinfo.value.length == 0)
      updateLogoBIAData.value.fileInfo = [];
    if (checkRVComOrgDetailInfo() == false)
      return;
  
    isloading.value = true;

    // updateLogoBIAData 입력 정보
    if(updateLogoBIAData.value.managegubun === "ORGE4")
      updateLogoBIAData.value.etc = "로고";
    else
      updateLogoBIAData.value.etc = "심볼";
    updateLogoBIAData.value.upid = loginStore.getUserLoginData.userid;
    
    // 이미 저장한 적이 있는지 체크
    if(firstManagegubun.value !== updateLogoBIAData.value.managegubun) {
      checkAlreadySaved.value.orgid = updateLogoBIAData.value.orgid;
      checkAlreadySaved.value.managegubun = updateLogoBIAData.value.managegubun;
      const response = await API.comAPI.getSignDetail(checkAlreadySaved.value, urlPaths.value);
      
      if(response.data !== "") {
        alert(`${updateLogoBIAData.value.orgname}의 ${updateLogoBIAData.value.etc}에는 이미 등록된 이미지가 존재합니다.`);
        isloading.value = false;
        return;
      }
    }

    /* ===================== 파일 입력 ===================== */
    var updateLogoBIAFormData = new FormData();
  
    // 추가된 것
    updateLogoBIAData.value.indexComment = [];
    let fIdx = 0;
  
    for await(const f of updateLogoBIAData.value.fileInfo) {
      updateLogoBIAData.value.indexComment.push({
        fileIndex: fIdx++,
      });
      updateLogoBIAFormData.append("files", f.rawFile);
    }
  
    updateLogoBIAFormData.append(
      "requiredUpdateLogoBIA",
      new Blob([JSON.stringify(updateLogoBIAData.value)], {
        type: "application/json",
      })
    );
    /* ==================================================== */
  
    
    API.comAPI
      .updateLogoBIA(updateLogoBIAFormData, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // alert("정상 갱신되었습니다");
          isloading.value = false;
          moveToBmsComlogolist();
        }
      })
      .catch(error => {
        console.log(error);
      });
  }
  
  
  // Move Function
  const moveToBmsComlogolist = () => {
    router.push({
      name: "BmsComlogolist"
    });
  }
  
  
  // Check Validation
  const checkRVComOrgDetailInfo = () => {
    
    return true;
  }
  
  </script>