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
    <div class="box-title">
      <h4 class="h4">게시판{{manageflag === true ? '관리' : ''}}</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">공지사항{{manageflag === true ? '관리' : ''}}</h5>
        </div>
      </section>

      <section class="section">
        <div class="box-condition">
          <table>
            <tr>
              <th>검색어</th>
              <td>
                <v-select
                  label=""
                  v-model="comboSelected"
                  :items="comboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field
                  @keyup.enter="selectNoticeList(1)"
                  v-model="comboInputData"
                  maxlength="100"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  type="date"
                  label=""
                  v-model="dateFormat.startDt"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
                ~
                <v-text-field
                  type="date"
                  label=""
                  v-model="dateFormat.endDt"
                  required
                  variant="outlined"
                  :min="dateFormat.startDt"
                  hide-details="auto"
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectNoticeList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="moveToBmsComnoticeinsert()">등록</v-btn>
              <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectNoticeList"
              :items="pageSizesSelectNoticeList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectNoticeList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsComnoticedetail(item)"
          v-model="checkedListSelectNoticeList"
          :headers="staticColumnsSelectNoticeList"
          :items="selectNoticeListList"
          :items-per-page="pageSizeSelectNoticeList"
          :loading="selectNoticeListLoader"
          :item-value="selectNoticeListIdentifier"
          :no-data-text="noDataText"
          :show-select="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001') ? true : false"
          hover
          class="table-type-05"
        >
          <template v-slot:item.title="{ item }">
            <td style="padding-left: 20px; background-color: #FFFFFF00;">
              <p :class="{ 'text-left-03': item }">{{ item.raw.title }}</p>              
            </td>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectNoticeList"
          :length="totalPagesSelectNoticeList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectNoticeList"
        ></v-pagination>
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
        :alertMsg="''"
        :returnFunc="deleteNoticeBIA"
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
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name = ref('BmsComNoticeList')
const route = useRoute()
const router = useRouter()
const urlPaths = ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("title")
const comboInputData = ref("")
const comboData = [
  { key: "title", view: "제목"}, 
  { key: "inname", view: "등록자"}
]
// for selectNoticeList
const checkedListSelectNoticeList = ref([])
const selectNoticeListList = ref([])
const selectNoticeListDefCondi = {
  title: "",
  inname: "",
  startDt: "",
  endDt: "",
  userid: getUserLoginData.value.userid,   // 사용자의 소속 부서를 조회하기 위함
  pageNum: 1,
  pageSize: 10,
}
const selectNoticeListCondi = ref({ ...selectNoticeListDefCondi })
const selectNoticeListCondiCheck = ref({ value: { ...selectNoticeListDefCondi }, flag: { ...selectNoticeListDefCondi } })

/* ================ pagination ================ */
const selectNoticeListLoader = ref(true);
const totalPagesSelectNoticeList = ref(0)
const currentPageSelectNoticeList = ref(1)
const pageSizeSelectNoticeList = ref(10)
const pageSizesSelectNoticeList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectNoticeList = () => {
  selectNoticeListCondi.value.pageSize = pageSizeSelectNoticeList.value;
  currentPageSelectNoticeList.value = 1;
  selectNoticeList(currentPageSelectNoticeList.value);
}
const handlePageChangeSelectNoticeList = () => {
  selectNoticeList(currentPageSelectNoticeList.value);
}
/* ============================================== */
const manageflag = ref(false);

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  setCondiClear(selectNoticeListCondiCheck.value, selectNoticeListCondi.value);
  setQueries(route, selectNoticeListCondi.value, selectNoticeListDefCondi);
  await selectNoticeList(selectNoticeListCondi.value.pageNum);
});

watch(route, async (route) => {
  setQueries(route, selectNoticeListCondi.value, selectNoticeListDefCondi);
  await selectNoticeList(selectNoticeListCondi.value.pageNum); 
});

watch(() => _.cloneDeep(selectNoticeListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectNoticeListCondiCheck.value, newVal, oldVal);
});

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectNoticeListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectNoticeListCondiCheck.value, selectNoticeListCondi.value, selectNoticeListCondi.value);
});

watch(comboSelected, async (comboSelected) => {
    selectNoticeListCondi.value["title"] = "";
    selectNoticeListCondi.value["inname"] = "";
  setCondiChanged(selectNoticeListCondiCheck.value, selectNoticeListCondi.value, selectNoticeListCondi.value);
});


const staticColumnsSelectNoticeList = [
  { title: "제목", key: "title", width: "60%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "조회수", key: "selcnt", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "inname", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];


/* ======================= multi selection ======================= */
const selectNoticeListIdentifier = computed(() => (item) => {
  item.identifier = {
    ntcid: item.ntcid,
  };
  return item.identifier;
});
/* ============================================================== */

let totalItem = 0;
const noDataText = ref("");
const dateFormat = ref({startDt: "", endDt: ""});
const selectNoticeList = async (pageNum) => {

  selectNoticeListLoader.value = true;
  selectNoticeListCondi.value.pageNum = parseInt(pageNum);

  if(!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectNoticeListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
  selectNoticeListLoader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt !== "")
      selectNoticeListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else
      selectNoticeListCondi.value.startDt = "";
    if(dateFormat.value.endDt !== "")
      selectNoticeListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectNoticeListCondi.value.endDt = "";

    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectNoticeListCondi.value.endDt = "";
    }

    // 시스템관리자의 경우 userid를 입력하지 않는다.
    if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001'))
      selectNoticeListCondi.value.userid = "";

    const response = await API.comAPI.selectNoticeList({ ...selectNoticeListCondi.value }, urlPaths.value);
    if (response.data.list.length == 0) {
      noDataText.value = "조건에 맞는 데이터가 없습니다.";
      selectNoticeListLoader.value = false;
    }    
    
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      return { ...u};
    });

    selectNoticeListList.value = response.data.list;
    totalPagesSelectNoticeList.value = response.data.pages;
    selectNoticeListLoader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectNoticeListCondiCheck.value, selectNoticeListCondi.value);
    if(selectNoticeListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectNoticeListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectNoticeListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectNoticeListCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}


const deleteNoticeBIA = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteNoticeBIA(checkedListSelectNoticeList.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });
  await selectNoticeList(selectNoticeListCondi.value.pageNum);
  checkedListSelectNoticeList.value = [];
}


const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectNoticeList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }

  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


// Move Function
const moveToBmsComnoticeinsert = () => {
  router.push({
    name: "BmsComnoticeinsert"
    });
}

// Move Function
const moveToBmsComnoticedetail = (ntcid) => {
  router.push({
    name: "BmsComnoticedetail",
    query: {
      ...{ntcid: ntcid.item.value.ntcid},
    }
  });
}

</script>
