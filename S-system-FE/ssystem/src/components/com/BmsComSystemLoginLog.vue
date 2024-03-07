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
          <h5 class="h5">시스템접속로그</h5>
        </div>
      </section>

      <section class="section">
        <div class="box-condition">
          <table>
            <tr>
              <th>검색어</th>
              <td>
                <v-select
                  v-model="comboSelected"
                  :items="comboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field
                  @keyup.enter="selectSystemLoginLog(1)"
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
          <v-btn class="magnify-solid" @click="selectSystemLoginLog(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <v-select
            v-model="pageSizeSelectSystemLoginLog"
            :items="pageSizesSelectSystemLoginLog"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectSystemLoginLog"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectSystemLoginLog"
          :headers="staticColumnsSelectSystemLoginLog"
          :items="selectSystemLoginLogList"
          :items-per-page="pageSizeSelectSystemLoginLog"
          :loading="loader"
          :no-data-text="noDataText"
          hover
          class="table-type-02"
        >
        <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectSystemLoginLog"
          :length="totalPagesSelectSystemLoginLog"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectSystemLoginLog"
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

const name = ref('BmsComSystemLoginLog')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("deptname")
const comboInputData = ref("")
const comboData = [
  { key: "deptname", view: "부서" },
  { key: "userid", view: "아이디" },
  { key: "username", view: "이름" },
]
// for selectSystemLoginLog
const loader = ref(true)
const checkedListSelectSystemLoginLog = ref([])
const selectSystemLoginLogList = ref([])
const selectSystemLoginLogDefCondi = {
  userid: "",
  username: "",
  deptid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}
const selectSystemLoginLogCondi = ref({ ...selectSystemLoginLogDefCondi })
const selectSystemLoginLogCondiCheck = ref({ value: { ...selectSystemLoginLogDefCondi }, flag: { ...selectSystemLoginLogDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "deptname")
    return 256;
  else if(comboSelected.value === "userid")
    return 35;
  else
    return 100;
};

/* ================ pagination ================ */
const totalPagesSelectSystemLoginLog = ref(0)
const currentPageSelectSystemLoginLog = ref(1)
const pageSizeSelectSystemLoginLog = ref(10)
const pageSizesSelectSystemLoginLog = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectSystemLoginLog = () => {
  selectSystemLoginLogCondi.value.pageSize = pageSizeSelectSystemLoginLog.value;
  currentPageSelectSystemLoginLog.value = 1;
  selectSystemLoginLog(currentPageSelectSystemLoginLog.value);
}
const handlePageChangeSelectSystemLoginLog = () => {
  selectSystemLoginLog(currentPageSelectSystemLoginLog.value);
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectSystemLoginLogCondiCheck.value, selectSystemLoginLogCondi.value);
  setQueries(route, selectSystemLoginLogCondi.value, selectSystemLoginLogDefCondi);
  await selectSystemLoginLog(selectSystemLoginLogCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectSystemLoginLogCondi.value, selectSystemLoginLogDefCondi);
  await selectSystemLoginLog(selectSystemLoginLogCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectSystemLoginLogCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectSystemLoginLogCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectSystemLoginLogCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectSystemLoginLogCondiCheck.value, selectSystemLoginLogCondi.value, selectSystemLoginLogCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectSystemLoginLogCondi.value["userid"] = "";
  selectSystemLoginLogCondi.value["username"] = "";
  selectSystemLoginLogCondi.value["deptid"] = "";
  setCondiChanged(selectSystemLoginLogCondiCheck.value, selectSystemLoginLogCondi.value, selectSystemLoginLogCondi.value);
})


const staticColumnsSelectSystemLoginLog = [
  { title: "접속시간", key: "logtime", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서", key: "deptname", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "아이디", key: "userid", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이름", key: "username", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "유형", key: "gubun", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


let totalItem = 0;
const noDataText = ref("");
const dateFormat = ref({startDt: "", endDt: ""});
const selectSystemLoginLog = async (pageNum) => {
  loader.value = true;
  selectSystemLoginLogCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectSystemLoginLogCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt !== "")
      selectSystemLoginLogCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else
      selectSystemLoginLogCondi.value.startDt = "";
    if(dateFormat.value.endDt !== "")
      selectSystemLoginLogCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectSystemLoginLogCondi.value.endDt = "";

    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectSystemLoginLogCondi.value.endDt = "";
    }

    const response = await API.comAPI.selectSystemLoginLog({ ...selectSystemLoginLogCondi.value }, urlPaths.value);
    if (response.data.list.length == 0) {
      noDataText.value = "조건에 맞는 데이터가 없습니다.";
      loader.value = false;
    }    

    response.data.list = response.data.list.map((u) => {
      u = { ...u, logtime: dayjs(u.logtime, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm:ss') };
      return { ...u};
    });
    
    selectSystemLoginLogList.value = response.data.list;
    totalPagesSelectSystemLoginLog.value = response.data.pages;
    loader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectSystemLoginLogCondiCheck.value, selectSystemLoginLogCondi.value)
    if(selectSystemLoginLogCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectSystemLoginLogCondi.value.startDt).format('YYYY-MM-DD');
    if(selectSystemLoginLogCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectSystemLoginLogCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}

</script>