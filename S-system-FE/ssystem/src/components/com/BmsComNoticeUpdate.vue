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
          <h5 class="h5">공지사항 수정</h5>
          <div class="action-buttons">
            <v-btn outlined @click="updateNoticeBIA()">저장</v-btn>
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
              <th>공지기간</th>
              <td>
                <div class="box-flex">
                  <v-text-field
                    type="date"
                    v-model="updateNoticeBIAData.sdate"
                    variant="outlined"
                    :min="firstSdate"
                    hide-details="auto"
                    required
                  />
                  <v-text-field
                    type="date"
                    v-model="updateNoticeBIAData.edate"
                    variant="outlined"
                    :min="updateNoticeBIAData.sdate"
                    hide-details="auto"
                    required
                  />
                </div>
              </td>
            </tr>
            <tr>
              <th class="required">공개대상</th>
              <td>
                <div class="box-flex2">
                  <v-checkbox
                    v-model="allDeptChecked"
                    label="전체"
                    hide-details="auto"
                  ></v-checkbox>
                  <div class="box-select">
                    <v-text-field
                      v-model="deptlist"
                      :readonly="true"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn class="magnify-solid" @click="toggleNoticeOrgList()">
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th class="required">제목</th>
              <td>
                <v-text-field
                  v-model="getNoticeDetailData.title"
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

  <v-dialog v-model="visibleBmsComNoticeOrgList" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>공개대상 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComNoticeOrgList = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleBmsComNoticeOrgList"
        :args="noticeArgs"
        :toggleFunc="toggleNoticeOrgList"
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
import { ref, onBeforeMount, onMounted, onBeforeUnmount, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';
import BmsComConfirmPopup from '@/components/com/BmsComConfirmPopup.vue';
import { textChanged, setQuillEditor } from "@/utils/QuillEditor.js";

const name = ref('BmsComNoticeUpdate')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref()
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateNoticeBIA
const updateNoticeBIAData = ref({
    ntcid: "",
    title: "",
    cont: "",
    sdate: "",
    edate: "",
    deptlist: [],
    upid: loginStore.getUserLoginData.userid,
    upname: loginStore.getUserLoginData.username,
    updt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
})
// for getNoticeDetail
const getNoticeDetailData = ref({})
const getNoticeDetailDefCondi = {
  ntcid: "",
  role: "admin",
}
const getNoticeDetailCondi = ref({ ...getNoticeDetailDefCondi })
const deptlist = ref("");

const allDeptChecked = ref(false);
const visibleBmsComNoticeOrgList = ref(false);
const visibleBmsComConfirmPopup = ref(false);

onBeforeMount(() => {
  query.value = route.query;
  getNoticeDetailCondi.value.ntcid = query.value.ntcid;
})

onMounted(async () => {
  await getNoticeDetail();
  updateNoticeBIAData.value.sdate = dayjs(getNoticeDetailData.value.sdate).format('YYYY-MM-DD');
  if(getNoticeDetailData.value.edate !== undefined)
    updateNoticeBIAData.value.edate = dayjs().format('YYYY-MM-DD');
})

onBeforeUnmount(() => {
  textChanged.value = false;
})

watch(allDeptChecked, async (allDeptChecked) => {
  if(allDeptChecked == true) {
    visibleBmsComNoticeOrgList.value = false;
    deptlist.value = "전체";
    updateNoticeBIAData.value.deptlist = [{deptid: "전체", deptname: "전체"}];
  }
  else {
    deptlist.value = "";
    updateNoticeBIAData.value.deptlist = [];
  }
});


// toggle Function
const noticeArgs = ref(""), urlArgs = ref("BmsComnoticelist");
const toggleNoticeOrgList = ref(() => {
  visibleBmsComNoticeOrgList.value = !visibleBmsComNoticeOrgList.value;
  noticeArgs.value = "plural";
  allDeptChecked.value = false;
});
const toggleConfirmPopup = ref(() => {
  visibleBmsComConfirmPopup.value = !visibleBmsComConfirmPopup.value;
});

const selectDeptList = ref((retValue) => {
  toggleNoticeOrgList.value();
  updateNoticeBIAData.value.deptlist = retValue;
  deptlist.value = "";
  retValue.map((item) => {
    deptlist.value = deptlist.value + " " + item.deptname;
  });
})


const firstSdate = ref("");
const getNoticeDetail = async () => {
  isloading.value = true;
  try {
    const response = await API.comAPI.getNoticeDetail({...getNoticeDetailCondi.value}, urlPaths.value);
    getNoticeDetailData.value = response.data;
    firstSdate.value = dayjs(getNoticeDetailData.value.sdate).format('YYYY-MM-DD');

    response.data.deptlist.map((item) => {
      deptlist.value = deptlist.value + " " + item.deptname;
    });

    updateNoticeBIAData.value.deptlist = response.data.deptlist;
    if(response.data.cont !== undefined) {
      updateNoticeBIAData.value.cont = response.data.cont;
      setQuillEditor(updateNoticeBIAData.value, true);
    }
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}

const updateNoticeBIA = async () => {
  isloading.value = true;
  if(updateNoticeBIAData.value.cont !== "") {
    if(textChanged.value === false)
      updateNoticeBIAData.value.cont = JSON.parse(updateNoticeBIAData.value.cont);
    updateNoticeBIAData.value.cont = JSON.stringify(updateNoticeBIAData.value.cont);
  }
  updateNoticeBIAData.value.ntcid = getNoticeDetailData.value.ntcid;
  updateNoticeBIAData.value.title = getNoticeDetailData.value.title;
  if(updateNoticeBIAData.value.sdate === "")
    updateNoticeBIAData.value.sdate = dayjs(new Date()).format('YYYY-MM-DD');
  if(updateNoticeBIAData.value.edate === "")
    updateNoticeBIAData.value.edate = null;

  if (checkRVBmsComNotice() == false) {
    isloading.value = false;
    return;
  }
  
  API.comAPI
    .updateNoticeBIA(updateNoticeBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
        isloading.value = false;
        moveToBmsComnoticelist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


// Move Function
const moveToBmsComnoticelist = () => {
  router.push({
    name: "BmsComnoticelist"
  });
}


// Check Validation
const checkRVBmsComNotice = () => {
  if (updateNoticeBIAData.value.title.length == 0) {
    alert("제목을 입력하세요");
    return false;
  }
  if (updateNoticeBIAData.value.title.length > 100){
    alert("제목의 최대 길이는 100자입니다.");
    return false;
  }
  if(updateNoticeBIAData.value.deptlist.length == 0) {
    alert("공개대상을 입력하세요.");
    return false;
  }
  if(updateNoticeBIAData.value.cont !== undefined && updateNoticeBIAData.value.cont.length == 0) {
    alert("내용을 입력하세요.");
    return false;
  }

  return true;
}
</script>