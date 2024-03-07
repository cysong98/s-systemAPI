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
      <h4 class="h4">인계인수</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">요청한 인계인수서</h5>
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
                  v-model="comboInputData"
                  required
                  clearable
                  variant="outlined"
                  hide-details="auto"
                  @keyup.enter="selectTrnReqListRouterPush(1)"
                />
              </td>
              <th>요청기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectTrnReqListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                  ~
                <v-text-field
                  type="date"
                  v-model="selectTrnReqListCondi.endDt"
                  :min="selectTrnReqListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectTrnReqListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem  }}개</span>
          <v-select
            v-model="pageSizeSelectTrnReqList"
            :items="pageSizesSelectTrnReqList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectTrnReqList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsDctreqadd(item)"
          v-model="checkedListSelectTrnReqList"
          :headers="staticColumnsSelectTrnReqList"
          :items="selectTrnReqListList"
          :items-per-page="pageSizeSelectTrnReqList"
          :loading="selectTrnReqListLoader"
          :item-value="selectTrnReqListIdentifier"
          class="table-type-02"
          >
          <template v-slot:item.reportdt="{ item }">
            <div>{{ transformDate(item.raw.reportdt) }}</div>
          </template>
          <template v-slot:item.title="{ item }">
            <div class="text-left">{{ item.raw.title }}</div>
          </template>
          <template v-slot:item.reason="{ item }">
            <td style="padding-left: 20px; background-color: #FFFFFF00;">
              <p :class="{ 'text-left-02': item }">{{ item.raw.reason }}</p>              
            </td>
          </template>
          <template v-slot:item.status="{ item }">
            <div>{{ transformObjStatus(item.raw.status) }}</div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageSelectTrnReqList"
          :length="totalPagesSelectTrnReqList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectTrnReqList"
        ></v-pagination>
      </section>
    </div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { validCheckSearchInput } from "@/utils/Utils.js";
import { transformObjStatus, transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsTrnReqList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("title")
const comboInputData = ref("")
const comboData = [
  { view: "제목", key: "title"},
  { view: "인계자", key: "requsername"},
  // { view: "인계부서", key: "reqdeptname"},
  // { view: "인수자", key: "apprusername"},
  // { view: "인수부서", key: "apprdeptname"},
];
// for selectTrnReqList
const checkedListSelectTrnReqList = ref([])
const selectTrnReqListList = ref([])
const selectTrnReqListDefCondi = {
  title: "",
  reqdeptname: "",
  requsername: "",
  apprdeptname: "",
  apprusername: "",
  transferid: "",
  status: "",
  apprstatus: "",
  appuserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "transferid"
}
const selectTrnReqListCondi = ref({ ...selectTrnReqListDefCondi })
const selectTrnReqListCondiCheck = ref({ value: { ...selectTrnReqListDefCondi }, flag: { ...selectTrnReqListDefCondi } })
/* ================ pagination ================ */
const selectTrnReqListLoader = ref(true)
const totalPagesSelectTrnReqList = ref(0)
const currentPageSelectTrnReqList = ref(1)
const pageSizeSelectTrnReqList = ref(10)
const pageSizesSelectTrnReqList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectTrnReqList = () => {
  selectTrnReqListCondi.value.pageSize = pageSizeSelectTrnReqList.value;
  currentPageSelectTrnReqList.value = 1;
  selectTrnReqList(currentPageSelectTrnReqList.value);
}
const handlePageChangeSelectTrnReqList = () => {
  selectTrnReqList(currentPageSelectTrnReqList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)


onMounted(async () => {
  setCondiClear(selectTrnReqListCondiCheck.value, selectTrnReqListCondi.value);
  setQueries(route, selectTrnReqListCondi.value, selectTrnReqListDefCondi);
  await selectTrnReqList(selectTrnReqListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectTrnReqListCondi.value, selectTrnReqListDefCondi);
  await selectTrnReqList(selectTrnReqListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectTrnReqListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectTrnReqListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectTrnReqListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectTrnReqListCondiCheck.value, selectTrnReqListCondi.value, selectTrnReqListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectTrnReqListCondi.value["title"] = "";
    selectTrnReqListCondi.value["reqdeptname"] = "";
    selectTrnReqListCondi.value["requsername"] = "";
    selectTrnReqListCondi.value["apprdeptname"] = "";
    selectTrnReqListCondi.value["apprusername"] = "";
  setCondiChanged(selectTrnReqListCondiCheck.value, selectTrnReqListCondi.value, selectTrnReqListCondi.value);
})


const staticColumnsSelectTrnReqList = [
  { key: "reportdt", title: "요청일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reason", title: "인계사유", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "finalTakeOver", title: "인수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "status", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

/* ======================= multi selection ======================= */
const selectTrnReqListIdentifier = computed(() => (item) => {
  item.identifier = `${item.transferid}-${item.transferid}-${item.objectid}-${item.transferid}-${item.apprseq}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectTrnReqListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectTrnReqListList.value.filter((item) => 
    checkedListSelectTrnReqList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */
const selectTrnReqListRouterPush = (pageNum) => {
  selectTrnReqListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectTrnReqListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const selectTrnReqList = async (pageNum) => {

  selectTrnReqListLoader.value = true;
  selectTrnReqListCondi.value.pageNum = parseInt(pageNum);
  selectTrnReqListCondi.value.requserid = getUserLoginData.value.userid;

  if(selectTrnReqListCondi.value.startDt > selectTrnReqListCondi.value.endDt)
    selectTrnReqListCondi.value.endDt = "";

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectTrnReqListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectTrnReqListLoader.value = false;
      return;
    }
  try {
    selectTrnReqListCondi.value.apprstatus  = 'APP01'; //'APP01' //상신 //BMS_TRN_REPORT.apprstatus(최종결재완료여부) APP09 임시저장
    const response = await API.trnAPI.selectTrnReqList({ ...selectTrnReqListCondi.value }, urlPaths.value);
    selectTrnReqListList.value = response.data.list;
    totalPagesSelectTrnReqList.value = response.data.pages;
    const allOption = pageSizesSelectTrnReqList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectTrnReqListLoader.value = false;
    totalItem = response.data.total;
    setCondiClear(selectTrnReqListCondiCheck.value, selectTrnReqListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}


// Move Function
const moveToBmsDctreqadd = (row) => {
  row.item.raw.parentPage = "BmsTrnreqlist";
  router.push({
    name: "BmsTrndetailcard",
    query: {
      ...row.item.raw
    }
  });
}

</script>