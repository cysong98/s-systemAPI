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
          <h5 class="h5">처리할 인계인수서</h5>
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
                  @keyup.enter="selectTransProcToDoListRouterPush(1)"
                />
              </td>
              <th>요청기간</th>
              <td>
                  <v-text-field
                    type="date"
                    v-model="selectTransProcToDoListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" />
                  ~
                  <v-text-field
                    type="date"
                    v-model="selectTransProcToDoListCondi.endDt"
                    :min="selectTransProcToDoListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" 
                  />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectTransProcToDoListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <v-select
            v-model="pageSizeSelectTransProcToDoList"
            :items="pageSizesSelectTransProcToDoList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectTransProcToDoList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsTrndetailcard(item)"
          v-model="checkedListSelectTransProcToDoList"
          :headers="staticColumnsSelectTransProcToDoList"
          :items="selectTransProcToDoListList"
          :items-per-page="pageSizeSelectTransProcToDoList"
          :loading="selectTransProcToDoListLoader"
          :item-value="selectTransProcToDoListIdentifier"
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
          v-model="currentPageSelectTransProcToDoList"
          :length="totalPagesSelectTransProcToDoList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectTransProcToDoList"
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

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { validCheckSearchInput } from "@/utils/Utils.js";
import { transformObjStatus, transformStatus, transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsComTransProcToDoList')
const params = ref({});
const query = ref({});
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
// for selectTransProcToDoList
const checkedListSelectTransProcToDoList = ref([])
const selectTransProcToDoListList = ref([])
const selectTransProcToDoListDefCondi = {
  title: "",
  reqdeptname: "",
  requsername: "",
  apprdeptname: "",
  apprusername: "",
  transferid: "",
  status: "",
  apprstatus: "",
  appruserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "transferid"
}
const selectTransProcToDoListCondi = ref({ ...selectTransProcToDoListDefCondi })
const selectTransProcToDoListCondiCheck = ref({ value: { ...selectTransProcToDoListDefCondi }, flag: { ...selectTransProcToDoListDefCondi } })
/* ================ pagination ================ */
const selectTransProcToDoListLoader = ref(true)
const totalPagesSelectTransProcToDoList = ref(0)
const currentPageSelectTransProcToDoList = ref(1)
const pageSizeSelectTransProcToDoList = ref(10)
const pageSizesSelectTransProcToDoList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectTransProcToDoList = () => {
  selectTransProcToDoListCondi.value.pageSize = pageSizeSelectTransProcToDoList.value;
  currentPageSelectTransProcToDoList.value = 1;
  selectTransProcToDoList(currentPageSelectTransProcToDoList.value);
}
const handlePageChangeSelectTransProcToDoList = () => {
  selectTransProcToDoList(currentPageSelectTransProcToDoList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onMounted(async () => {
  setCondiClear(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value);
  setQueries(route, selectTransProcToDoListCondi.value, selectTransProcToDoListDefCondi);
  await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectTransProcToDoListCondi.value, selectTransProcToDoListDefCondi);
  await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectTransProcToDoListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectTransProcToDoListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectTransProcToDoListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value, selectTransProcToDoListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectTransProcToDoListCondi.value["title"] = "";
    selectTransProcToDoListCondi.value["reqdeptname"] = "";
    selectTransProcToDoListCondi.value["requsername"] = "";
    selectTransProcToDoListCondi.value["apprdeptname"] = "";
    selectTransProcToDoListCondi.value["apprusername"] = "";
  setCondiChanged(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value, selectTransProcToDoListCondi.value);
})


const staticColumnsSelectTransProcToDoList = [
  { key: "reportdt", title: "요청일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reason", title: "인계사유", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "finalTakeOver", title: "인수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "status", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

/* ======================= multi selection ======================= */
const selectTransProcToDoListIdentifier = computed(() => (item) => {
  item.identifier = `${item.transferid}-${item.transferid}-${item.objectid}-${item.transferid}-${item.apprseq}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectTransProcToDoListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectTransProcToDoListList.value.filter((item) => 
    checkedListSelectTransProcToDoList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectTransProcToDoListRouterPush = (pageNum) => {
  selectTransProcToDoListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectTransProcToDoListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const selectTransProcToDoList = async (pageNum) => {

  selectTransProcToDoListLoader.value = true;
  selectTransProcToDoListCondi.value.pageNum = parseInt(pageNum);
  selectTransProcToDoListCondi.value.appruserid = getUserLoginData.value.userid;

  if(selectTransProcToDoListCondi.value.startDt > selectTransProcToDoListCondi.value.endDt)
    selectTransProcToDoListCondi.value.endDt = "";

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectTransProcToDoListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectTransProcToDoListLoader.value = false;
      return;
    }
  try {
    const response = await API.trnAPI.selectTransProcToDoList({ ...selectTransProcToDoListCondi.value }, urlPaths.value);
    selectTransProcToDoListList.value = response.data.list;
    totalPagesSelectTransProcToDoList.value = response.data.pages;
    const allOption = pageSizesSelectTransProcToDoList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectTransProcToDoListLoader.value = false;
    totalItem = response.data.total;
    setCondiClear(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value)
  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsTrndetailcard = (row) => {
  row.item.raw.proc = "proc"
  row.item.raw.parentPage = "BmsComtransproctodolist";
  router.push({
    name: "BmsTrndetailcard",
    query: {
      ...row.item.raw
    }
  });
}

</script>