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
          <h5 class="h5">처리한 인계인수서</h5>
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
                  @keyup.enter="bmsTrnCompleteListRouterPush(1)"
                />
              </td>
              <th>요청기간</th>
              <td>
                  <v-text-field
                    type="date"
                    v-model="bmsTrnCompleteListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" />
                  ~
                  <v-text-field
                    type="date"
                    v-model="bmsTrnCompleteListCondi.endDt"
                    :min="bmsTrnCompleteListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" 
                  />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="bmsTrnCompleteListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <v-select
            v-model="pageSizeBmsTrnCompleteList"
            :items="pageSizesBmsTrnCompleteList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeBmsTrnCompleteList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsTrndetailcard(item)"
          v-model="checkedListBmsTrnCompleteList"
          :headers="staticColumnsBmsTrnCompleteList"
          :items="bmsTrnCompleteListList"
          :items-per-page="pageSizeBmsTrnCompleteList"
          :loading="bmsTrnCompleteListLoader"
          :item-value="bmsTrnCompleteListIdentifier"
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
          v-model="currentPageBmsTrnCompleteList"
          :length="totalPagesBmsTrnCompleteList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeBmsTrnCompleteList"
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
import { transformObjStatus, transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsTrnCompleteList')
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
// for bmsTrnCompleteList
const checkedListBmsTrnCompleteList = ref([])
const bmsTrnCompleteListList = ref([])
const bmsTrnCompleteListDefCondi = {
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
const bmsTrnCompleteListCondi = ref({ ...bmsTrnCompleteListDefCondi })
const bmsTrnCompleteListCondiCheck = ref({ value: { ...bmsTrnCompleteListDefCondi }, flag: { ...bmsTrnCompleteListDefCondi } })
/* ================ pagination ================ */
const bmsTrnCompleteListLoader = ref(true)
const totalPagesBmsTrnCompleteList = ref(0)
const currentPageBmsTrnCompleteList = ref(1)
const pageSizeBmsTrnCompleteList = ref(10)
const pageSizesBmsTrnCompleteList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeBmsTrnCompleteList = () => {
  bmsTrnCompleteListCondi.value.pageSize = pageSizeBmsTrnCompleteList.value;
  currentPageBmsTrnCompleteList.value = 1;
  bmsTrnCompleteList(currentPageBmsTrnCompleteList.value);
}
const handlePageChangeBmsTrnCompleteList = () => {
  bmsTrnCompleteList(currentPageBmsTrnCompleteList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)

onMounted(async () => {
  setCondiClear(bmsTrnCompleteListCondiCheck.value, bmsTrnCompleteListCondi.value);
  setQueries(route, bmsTrnCompleteListCondi.value, bmsTrnCompleteListDefCondi);
  await bmsTrnCompleteList(bmsTrnCompleteListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, bmsTrnCompleteListCondi.value, bmsTrnCompleteListDefCondi);
  await bmsTrnCompleteList(bmsTrnCompleteListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(bmsTrnCompleteListCondi.value), (newVal, oldVal) => {
  setCondiChanged(bmsTrnCompleteListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    bmsTrnCompleteListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(bmsTrnCompleteListCondiCheck.value, bmsTrnCompleteListCondi.value, bmsTrnCompleteListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    bmsTrnCompleteListCondi.value["title"] = "";
    bmsTrnCompleteListCondi.value["reqdeptname"] = "";
    bmsTrnCompleteListCondi.value["requsername"] = "";
    bmsTrnCompleteListCondi.value["apprdeptname"] = "";
    bmsTrnCompleteListCondi.value["apprusername"] = "";
  setCondiChanged(bmsTrnCompleteListCondiCheck.value, bmsTrnCompleteListCondi.value, bmsTrnCompleteListCondi.value);
})

const staticColumnsBmsTrnCompleteList = [
  { key: "reportdt", title: "요청일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reason", title: "인계사유", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "finalTakeOver", title: "인수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "status", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

/* ======================= multi selection ======================= */
const bmsTrnCompleteListIdentifier = computed(() => (item) => {
  item.identifier = `${item.transferid}-${item.transferid}-${item.objectid}-${item.transferid}-${item.apprseq}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const bmsTrnCompleteListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = bmsTrnCompleteListList.value.filter((item) => 
    checkedListBmsTrnCompleteList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const bmsTrnCompleteListRouterPush = (pageNum) => {
  bmsTrnCompleteListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: bmsTrnCompleteListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const bmsTrnCompleteList = async (pageNum) => {

  bmsTrnCompleteListLoader.value = true;
  bmsTrnCompleteListCondi.value.pageNum = parseInt(pageNum);
  bmsTrnCompleteListCondi.value.appruserid = getUserLoginData.value.userid;
  const apprstatusValues = ['APP02', 'APP03', 'APP04']; // 결재중, 결재완료, 결재반려
  bmsTrnCompleteListCondi.value.apprstatus = apprstatusValues;
  bmsTrnCompleteListCondi.value.state = 'DCST3'; //bms_trn_step.state DCST3:처리완료
  
  if(bmsTrnCompleteListCondi.value.startDt > bmsTrnCompleteListCondi.value.endDt)
    bmsTrnCompleteListCondi.value.endDt = "";

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      bmsTrnCompleteListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      bmsTrnCompleteListLoader.value = false;
      return;
    }
  try {
    const response = await API.trnAPI.bmsTrnCompleteList({ ...bmsTrnCompleteListCondi.value }, urlPaths.value);
    bmsTrnCompleteListList.value = response.data.list;
    totalPagesBmsTrnCompleteList.value = response.data.pages;
    const allOption = pageSizesBmsTrnCompleteList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    bmsTrnCompleteListLoader.value = false;
    totalItem = response.data.total;
    setCondiClear(bmsTrnCompleteListCondiCheck.value, bmsTrnCompleteListCondi.value)
  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsTrndetailcard = (row) => {
  row.item.raw.parentPage = 'BmsTrncompletelist';
  router.push({
    name: "BmsTrndetailcard",
    query: {
      ...row.item.raw
    }
  });
}

</script>