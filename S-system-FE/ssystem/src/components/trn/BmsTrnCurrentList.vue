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
          <h5 class="h5">인수현황함</h5>
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
                  @keyup.enter="bmsTrnCurrentListRouterPush(1)"
                  />
              </td>
              <th>요청기간</th>
              <td>
                <v-text-field
                    type="date"
                    v-model="bmsTrnCurrentListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" />
                  ~
                  <v-text-field
                    type="date"
                    v-model="bmsTrnCurrentListCondi.endDt"
                    :min="bmsTrnCurrentListCondi.startDt"
                    required
                    variant="outlined"
                    hide-details="auto" 
                  />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="bmsTrnCurrentListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <v-select
            v-model="pageSizeBmsTrnCurrentList"
            :items="pageSizesBmsTrnCurrentList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeBmsTrnCurrentList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsTrndetailcard(item)"
          v-model="checkedListBmsTrnCurrentList"
          :headers="staticColumnsBmsTrnCurrentList"
          :items="bmsTrnCurrentListList"
          :items-per-page="pageSizeBmsTrnCurrentList"
          :loading="bmsTrnCurrentListLoader"
          :item-value="bmsTrnCurrentListIdentifier"
          class="table-type-05"
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
          v-model="currentPageBmsTrnCurrentList"
          :length="totalPagesBmsTrnCurrentList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeBmsTrnCurrentList"
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
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { transformObjStatus, transformStatus, transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsTrnCurrentList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("title")
const comboInputData = ref("")
const comboData = [
  { view: "제목", key: "title"},
  { view: "인계자", key: "requsername"},
]
// for bmsTrnCurrentList
const checkedListBmsTrnCurrentList = ref([])
const bmsTrnCurrentListList = ref([])
const bmsTrnCurrentListDefCondi = {
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
}
const bmsTrnCurrentListCondi = ref({ ...bmsTrnCurrentListDefCondi })
const bmsTrnCurrentListCondiCheck = ref({ value: { ...bmsTrnCurrentListDefCondi }, flag: { ...bmsTrnCurrentListDefCondi } })
/* ================ pagination ================ */
const bmsTrnCurrentListLoader = ref(true)
const totalPagesBmsTrnCurrentList = ref(0)
const currentPageBmsTrnCurrentList = ref(1)
const pageSizeBmsTrnCurrentList = ref(10)
const pageSizesBmsTrnCurrentList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeBmsTrnCurrentList = () => {
  bmsTrnCurrentListCondi.value.pageSize = pageSizeBmsTrnCurrentList.value;
  currentPageBmsTrnCurrentList.value = 1;
  bmsTrnCurrentList(currentPageBmsTrnCurrentList.value);
}
const handlePageChangeBmsTrnCurrentList = () => {
  bmsTrnCurrentList(currentPageBmsTrnCurrentList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)


onMounted(async () => {
  setCondiClear(bmsTrnCurrentListCondiCheck.value, bmsTrnCurrentListCondi.value);
  setQueries(route, bmsTrnCurrentListCondi.value, bmsTrnCurrentListDefCondi);
  await bmsTrnCurrentList(bmsTrnCurrentListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, bmsTrnCurrentListCondi.value, bmsTrnCurrentListDefCondi);
  await bmsTrnCurrentList(bmsTrnCurrentListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(bmsTrnCurrentListCondi.value), (newVal, oldVal) => {
  setCondiChanged(bmsTrnCurrentListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    bmsTrnCurrentListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(bmsTrnCurrentListCondiCheck.value, bmsTrnCurrentListCondi.value, bmsTrnCurrentListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    bmsTrnCurrentListCondi.value["title"] = "";
    bmsTrnCurrentListCondi.value["reqdeptname"] = "";
    bmsTrnCurrentListCondi.value["requsername"] = "";
    bmsTrnCurrentListCondi.value["apprdeptname"] = "";
    bmsTrnCurrentListCondi.value["apprusername"] = "";
  setCondiChanged(bmsTrnCurrentListCondiCheck.value, bmsTrnCurrentListCondi.value, bmsTrnCurrentListCondi.value);
})

const staticColumnsBmsTrnCurrentList = [
  { key: "reportdt", title: "요청일자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reason", title: "인계사유", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprusername", title: "인수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "status", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ======================= multi selection ======================= */
const bmsTrnCurrentListIdentifier = computed(() => (item) => {
  item.identifier = `${item.transferid}-${item.transferid}-${item.objectid}-${item.transferid}-${item.apprseq}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const bmsTrnCurrentListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = bmsTrnCurrentListList.value.filter((item) => 
    checkedListBmsTrnCurrentList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */


const bmsTrnCurrentListRouterPush = (pageNum) => {
  bmsTrnCurrentListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: bmsTrnCurrentListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const bmsTrnCurrentList = async (pageNum) => {

  bmsTrnCurrentListLoader.value = true;
  bmsTrnCurrentListCondi.value.pageNum = parseInt(pageNum);
  bmsTrnCurrentListCondi.value.appruserid = getUserLoginData.value.userid;

  if(bmsTrnCurrentListCondi.value.startDt > bmsTrnCurrentListCondi.value.endDt)
    bmsTrnCurrentListCondi.value.endDt = "";

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
    bmsTrnCurrentListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      bmsTrnCurrentListLoader.value = false;
      return;
    }
  try {
    const response = await API.trnAPI.bmsTrnCurrentList({ ...bmsTrnCurrentListCondi.value }, urlPaths.value);
    bmsTrnCurrentListList.value = response.data.list;
    totalPagesBmsTrnCurrentList.value = response.data.pages;
    const allOption = pageSizesBmsTrnCurrentList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    bmsTrnCurrentListLoader.value = false;
    totalItem = response.data.total;
    setCondiClear(bmsTrnCurrentListCondiCheck.value, bmsTrnCurrentListCondi.value)
  } catch (error) {
    console.log(error);
  }
}

// Move Function
const moveToBmsTrndetailcard = (row) => {
  row.item.raw.parentPage = "BmsTrncurrentlist";
  router.push({
    name: "BmsTrndetailcard",
    query: {
      ...row.item.raw
    }
  });
}

</script>