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
      <h4 class="h4">시스템관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">실시간접속현황</h5>
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
                  @keyup.enter="selectRealtimeLog(1)"
                  v-model="comboInputData"
                  :maxlength="maxLengthFunc()"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
              </td>
              <th>접속기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="dateFormat.startDt"
                  variant="outlined"
                  hide-details="auto"
                  required
                />
                ~
                <v-text-field
                  type="date"
                  v-model="dateFormat.endDt"
                  :min="dateFormat.startDt"
                  variant="outlined"
                  hide-details="auto"
                  required
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectRealtimeLog(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <v-select
            v-model="pageSizeSelectRealtimeLog"
            :items="pageSizesSelectRealtimeLog"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectRealtimeLog"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectRealtimeLog"
          :headers="staticColumnsSelectRealtimeLog"
          :items="selectRealtimeLogList"
          :items-per-page="pageSizeSelectRealtimeLog"
          :loading="loader"
          :no-data-text="noDataText"
          class="table-type-02"
          hover
        >
        <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectRealtimeLog"
          :length="totalPagesSelectRealtimeLog"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectRealtimeLog"
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
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";

const name = ref('BmsComRealtimeLog')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("deptname")
const comboInputData = ref("")
const comboData = [
  { key: "deptname", view: "부서" },
  { key: "userid", view: "아이디" },
  { key: "username", view: "이름" }
]
// for selectRealtimeLog
const loader = ref(true)
const checkedListSelectRealtimeLog = ref([])
const selectRealtimeLogList = ref([])
const selectRealtimeLogDefCondi = {
  deptname: "",
  userid: "",
  username: "",
  startDt: "",
  endDt: "",
  flag: "Y",
  pageNum: 1,
  pageSize: 10,
}
const selectRealtimeLogCondi = ref({ ...selectRealtimeLogDefCondi })
const selectRealtimeLogCondiCheck = ref({ value: { ...selectRealtimeLogDefCondi }, flag: { ...selectRealtimeLogDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "deptname")
    return 256;
  else if(comboSelected.value === "userid")
    return 35;
  else
    return 100;
};

/* ================ pagination ================ */
const totalPagesSelectRealtimeLog = ref(0)
const currentPageSelectRealtimeLog = ref(1)
const pageSizeSelectRealtimeLog = ref(10)
const pageSizesSelectRealtimeLog = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectRealtimeLog = () => {
  selectRealtimeLogCondi.value.pageSize = pageSizeSelectRealtimeLog.value;
  currentPageSelectRealtimeLog.value = 1;
  selectRealtimeLog(currentPageSelectRealtimeLog.value);
}
const handlePageChangeSelectRealtimeLog = () => {
  selectRealtimeLog(currentPageSelectRealtimeLog.value);
}
/* ============================================== */


onMounted(async () => {
  setCondiClear(selectRealtimeLogCondiCheck.value, selectRealtimeLogCondi.value);
  setQueries(route, selectRealtimeLogCondi.value, selectRealtimeLogDefCondi);
  await selectRealtimeLog(selectRealtimeLogCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectRealtimeLogCondi.value, selectRealtimeLogDefCondi);
  await selectRealtimeLog(selectRealtimeLogCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectRealtimeLogCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectRealtimeLogCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectRealtimeLogCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectRealtimeLogCondiCheck.value, selectRealtimeLogCondi.value, selectRealtimeLogCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectRealtimeLogCondi.value["deptname"] = "";
  selectRealtimeLogCondi.value["userid"] = "";
  selectRealtimeLogCondi.value["username"] = "";
  setCondiChanged(selectRealtimeLogCondiCheck.value, selectRealtimeLogCondi.value, selectRealtimeLogCondi.value);
})


const staticColumnsSelectRealtimeLog = [
  { title: "접속시간", key: "indt", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서", key: "deptname", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "아이디", key: "userid", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이름", key: "username", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "작업내용", key: "", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];


let totalItem = 0;
const noDataText = ref("");
const dateFormat = ref({startDt: "", endDt: ""});
const selectRealtimeLog = async (pageNum) => {

  selectRealtimeLogCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectRealtimeLogCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt !== "")
      selectRealtimeLogCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else
      selectRealtimeLogCondi.value.startDt = "";
    if(dateFormat.value.endDt !== "")
      selectRealtimeLogCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectRealtimeLogCondi.value.endDt = "";

    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectRealtimeLogCondi.value.endDt = "";
    }

    const response = await API.comAPI.selectRealtimeLog({ ...selectRealtimeLogCondi.value }, urlPaths.value);
    if (response.data.list.length == 0) {
      noDataText.value = "조건에 맞는 데이터가 없습니다.";
      loader.value = false;
    }    

    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm:ss') };
      return { ...u};
    });

    selectRealtimeLogList.value = response.data.list;
    totalPagesSelectRealtimeLog.value = response.data.pages;
    loader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectRealtimeLogCondiCheck.value, selectRealtimeLogCondi.value)
    if(selectRealtimeLogCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectRealtimeLogCondi.value.startDt).format('YYYY-MM-DD');
    if(selectRealtimeLogCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectRealtimeLogCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}


</script>