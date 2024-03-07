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
          <h5 class="h5">Q&A 상세</h5>
          <div class="action-buttons">
            <v-btn
              v-if="writerId == getUserLoginData.userid || (getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001'))"
              @click="moveToBmsComqnaupdate(query.qnaid)"
              outlined
            >
              수정
            </v-btn>
            <v-btn
              v-if="(getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001'))"
              @click="moveToBmsComqnainsert(query.qnaid)"
              outlined
            >
              답변
            </v-btn>
            <v-btn
              v-if="writerId == getUserLoginData.userid || (getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001'))"
              @click="toggleDeletePopup()"
              outlined
            >
              삭제
            </v-btn>
            <v-btn @click="moveToBmsComqnalist()" outlined>목록</v-btn>
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
              <th>제목</th>
              <td>
                <v-text-field
                  v-model="getQnaDetailData.title"
                  :readonly="true"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>내용</th>
              <td>
                <div id="editor-container">
                  <div id="quill-editor"></div>
                </div>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>

      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">Q&A 답변 목록</h5>
        </div>
      </section>

      <section class="section">
        <v-data-table
          @click:row="(event, item) => moveToBmsComqnadetail(item.item.raw.qnaid)"
          :headers="staticColumnsSelectRepList"
          :items="selectRepListData"
          :items-per-page="100"
          :no-data-text="noDataText"
          hover
          class="table-type-04"
        >
          <template v-slot:item.title="{ item }">
            <td style="width: 100px; background-color: #FFFFFF00;">
              <p class="text-left-02">{{ item.raw.title }}</p>              
            </td>
          </template>
          <template v-slot:item.cont="{ item }">
            <td style="width: 700px; background-color: #FFFFFF00;">
              <p class="text-left-02">{{ item.raw.cont }}</p>              
            </td>
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <div id="get-text" style="display: none;"></div>
      </section>
    </div>
  </div>

  <v-dialog v-model="visibleBmsComDeletePopup" style="width: 700px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComDeletePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComDeletePopup
        v-if="visibleBmsComDeletePopup"
        :alertMsg="'Q&A 삭제 시 답변글도 모두 삭제됩니다.'"
        :returnFunc="deleteQna"
      ></BmsComDeletePopup>
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
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";

import { setQuillEditorForDetail } from '@/utils/QuillEditor.js'
import { Quill } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

const name = ref('BmsComQnaDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref()
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getQnaDetail
const getQnaDetailData = ref({})
const getQnaDetailDefCondi = {
  qnaid: "",
}
const getQnaDetailCondi = ref({ ...getQnaDetailDefCondi })
// for selectRepList
const selectRepListData = ref([]);
const selectRepListDefCondi = {
  title: "",
  inname: "",
  startDt: "",
  endDt: "",
  refid: "",
  qnaid: "",
  pageNum: 1,
  pageSize: 100,
};
const selectRepListCondi = ref({ ...selectRepListDefCondi });
const selectRepListCondiCheck = ref({ value: { ...selectRepListDefCondi }, flag: { ...selectRepListDefCondi } })
// for deleteQna
const deleteQnaCondi = ref([]);

const writerId = ref("");
const manageflag = ref(false);

onBeforeMount(async() => {
  query.value = route.query;
  getQnaDetailCondi.value.qnaid = query.value.qnaid;
  const response = await API.comAPI.getQnaDetail(getQnaDetailCondi.value, urlPaths.value);
  writerId.value = response.data.inid;
})

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  deleteQnaCondi.value.push({qnaid: route.query.qnaid});
  setCondiClear(selectRepListCondiCheck.value, selectRepListCondi.value);
  setQueries(route, selectRepListCondi.value, selectRepListDefCondi);
  await getQnaDetail(query.value.qnaid);
  await addSelCnt(getQnaDetailData.value.qnaid);
})

watch(route, async (route) => {
  query.value = route.query;
  setQueries(route, selectRepListCondi.value, selectRepListDefCondi);
  await selectRepList(1); 
})

watch(() => _.cloneDeep(selectRepListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectRepListCondiCheck.value, newVal, oldVal);
})


const staticColumnsSelectRepList = [
  { title: "제목", key: "title", width: "120px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "내용", key: "cont", width: "550px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "inname", width: "80px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "조회수", key: "selcnt", width: "80px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" }
];


const addSelCnt = async (qnaid) => {
  try {
    getQnaDetailData.value.selcnt += 1;
    await API.comAPI.updateQna(getQnaDetailData.value, urlPaths.value);

  } catch(error) {
    console.log(error);
  }
}

const getQnaDetail = async (qnaid) => {
  isloading.value = true;
  try {
    getQnaDetailCondi.value.qnaid = qnaid;
    const response = await API.comAPI.getQnaDetail(getQnaDetailCondi.value, urlPaths.value);

    getQnaDetailData.value.qnaid = response.data.qnaid;
    getQnaDetailData.value.thread = response.data.thread;
    getQnaDetailData.value.pos = response.data.pos;
    getQnaDetailData.value.depth = response.data.depth;
    getQnaDetailData.value.selcnt = response.data.selcnt;
    getQnaDetailData.value.repcnt = response.data.repcnt;
    getQnaDetailData.value.title = response.data.title;
    if(response.data.cont !== undefined) {
      getQnaDetailData.value.cont = response.data.cont;
      setQuillEditorForDetail(getQnaDetailData.value);
    }

    await selectRepList(1);
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}

const noDataText = ref("");
const selectRepList = async (pageNum) => {
  isloading.value = true;
  try {
    selectRepListCondi.value.refid = getQnaDetailData.value.qnaid;
    if(selectRepListCondi.value.refid === "" || selectRepListCondi.value.refid === undefined || selectRepListCondi.value.refid === null) {
      noDataText.value = "답변이 존재하지 않습니다.";
      return;
    }
    selectRepListCondi.value.pageNum = pageNum;
    const response = await API.comAPI.selectQnaList(selectRepListCondi.value, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      var quill = new Quill('#get-text');
      quill.setContents(JSON.parse(u.cont).ops);
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD'), cont: quill.getText()};
      return { ...u};
    });

    selectRepListData.value = response.data.list;

    if(response.data.size === 0)
      noDataText.value = "답변이 존재하지 않습니다.";

    setCondiClear(selectRepListCondiCheck.value, selectRepListCondi.value);
    selectRepListCondi.value.refid = "";
    isloading.value = false;
  } catch (error) {
    console.log(error);
  }
}


const deleteQna = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }
  isloading.value = true;

  await API.comAPI
    .deleteQna(deleteQnaCondi.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
      isloading.value = false;
      moveToBmsComqnalist();
    })
  .catch(error => {
    console.log(error);
  });
}

const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});

/* ====================== Move Function ====================== */
const moveToBmsComqnalist = () => {
  router.push({
    name: "BmsComqnalist"
  });
}

const moveToBmsComqnaupdate = (qnaid) => {
  router.push({
    name: "BmsComqnaupdate",
    query: {
      qnaid,
    }
  });
}

const moveToBmsComqnainsert = (qnaid) => {
  router.push({
    name: "BmsComqnainsert",
    query: {
      ...{refid: qnaid}
    }
  });
}

const moveToBmsComqnadetail = (item) => {
  router.push({
    name: "BmsComqnadetail",
    query: {
      ...{qnaid: item}
    }
  });
  getQnaDetail(item);
}
/* =========================================================== */
</script>


<style scoped>
  #editor-container { 
    height: 450px;
    padding-bottom: 10px;
    padding-top: 10px;
  }
</style>