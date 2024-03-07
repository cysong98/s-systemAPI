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
      <h4 class="h4">조직관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">서명인관리</h5>
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
                  v-if="comboSelected == 'orgname'"
                  @keyup.enter="selectSignList(1)"
                  v-model="comboInputData"
                  maxlength="255"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
                <v-select
                  v-else
                  v-model="mgComboSelected"
                  :items="mgComboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>등록기간</th>
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
          <v-btn class="magnify-solid" @click="selectSignList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveToBmsComsigninsert()">등록</v-btn>
              <v-btn class="button" @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectSignList"
              :items="pageSizesSelectSignList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectSignList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsComsigndetail(item.item)"
          v-model="checkedListSelectSignList"
          :headers="staticColumnsSelectSignList"
          :items="selectSignListList"
          :items-per-page="pageSizeSelectSignList"
          :item-value="selectSignListIdentifier"
          :loading="loader"
          :no-data-text="noDataText"
          show-select
          hover
          class="table-type-05"
        >
          <template v-slot:item.managegubun="{ item }">
              {{ displayManagegubun(item.raw.managegubun) }}
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectSignList"
          :length="totalPagesSelectSignList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectSignList"
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
        :alertMsg="'서명인 정보가 영구 삭제됩니다.'"
        :returnFunc="deleteSignBIA"
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

const name = ref('BmsComSignList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("orgname")
const mgComboSelected = ref("")
const comboInputData = ref("")
const comboData = [
  { key: "managegubun", view: "분류" },
  { key: "orgname", view: "부서명"},
]
const mgComboData = [
  { key: "", view: "선택"},
  { key: "ORGEA", view: "서명인"},
  { key: "ORGEB", view: "생략인" }
]
// for selectSignList
const loader = ref(true)
const checkedListSelectSignList = ref([])
const selectSignListList = ref([])
const selectSignListDefCondi = {
  userid: "",
  username: "",
  orgname: "",
  managegubun: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}
const selectSignListCondi = ref({ ...selectSignListDefCondi })
const selectSignListCondiCheck = ref({ value: { ...selectSignListDefCondi }, flag: { ...selectSignListDefCondi } })
/* ================ pagination ================ */
const totalPagesSelectSignList = ref(0)
const currentPageSelectSignList = ref(1)
const pageSizeSelectSignList = ref(10)
const pageSizesSelectSignList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectSignList = () => {
  selectSignListCondi.value.pageSize = pageSizeSelectSignList.value;
  currentPageSelectSignList.value = 1;
  selectSignList(currentPageSelectSignList.value);
}
const handlePageChangeSelectSignList = () => {
  selectSignList(currentPageSelectSignList.value);
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectSignListCondiCheck.value, selectSignListCondi.value);
  setQueries(route, selectSignListCondi.value, selectSignListDefCondi);
  await selectSignList(selectSignListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectSignListCondi.value, selectSignListDefCondi);
  await selectSignList(selectSignListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectSignListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectSignListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectSignListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectSignListCondiCheck.value, selectSignListCondi.value, selectSignListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  mgComboSelected.value = "";
  selectSignListCondi.value["orgname"] = "";
  selectSignListCondi.value["managegubun"] = "";
  setCondiChanged(selectSignListCondiCheck.value, selectSignListCondi.value, selectSignListCondi.value);
})

watch(mgComboSelected, async (mgComboSelected) => {
  await selectSignList(1);
})


const staticColumnsSelectSignList = [
  { title: "분류", key: "managegubun", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서명", key: "orgname", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

const displayManagegubun = (item) => {
  if(item === "ORGEA")
    return "서명인";
  else
    return "생략인";
}

/* ======================= multi selection ======================= */
const selectSignListIdentifier = computed(() => (item) => {
  item.identifier = {
    orgid: item.orgid,
    seq: item.seq
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const checkedSignListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectSignListList.value.filter((item) =>
    checkedListSelectSignList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
/* ============================================================== */


let totalItem = 0;
const length = ref(0);
const noDataText = ref("검색된 조건에 맞는 데이터가 없습니다.");
const dateFormat = ref({startDt: "", endDt: ""});

const selectSignList = async (pageNum) => {
  selectSignListCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectSignListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  selectSignListCondi.value.managegubun = mgComboSelected.value;
  
  try {
    if(dateFormat.value.startDt != "")
      selectSignListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else 
      selectSignListCondi.value.startDt = "";
    if(dateFormat.value.endDt != "")
      selectSignListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectSignListCondi.value.endDt = "";
        
    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectSignListCondi.value.endDt = "";
    }

    const response = await API.comAPI.selectSignList({ ...selectSignListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')};
      return { ...u};
    });

    selectSignListList.value = response.data.list;
    totalPagesSelectSignList.value = response.data.pages;
    loader.value = false;
    length.value = response.data.list.length;
    totalItem = response.data.total;

    setCondiClear(selectSignListCondiCheck.value, selectSignListCondi.value);
    if(selectSignListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectSignListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectSignListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectSignListCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}


const deleteSignBIA = async (retValue) => {
  toggleDeletePopup.value();
  if(retValue == false) {
    return;
  }
  
  // 마지막 페이지의 모든 항목을 삭제한 경우
  if(checkedListSelectSignList.value.length == length.value) {
    selectSignListCondi.value.pageNum = 1;
  }

  await API.comAPI
    .deleteSignBIA(checkedSignListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });
  await selectSignList(selectSignListCondi.value.pageNum);
  checkedListSelectSignList.value = [];
}


const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectSignList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


// Move Function
const moveToBmsComsigndetail = (item) => {
  router.push({
    name: "BmsComsigndetail",
    query: {
      ...{ orgid: item.value.orgid },
      ...{ seq: item.value.seq }
    }
  });
}

// Move Function
const moveToBmsComsigninsert = () => {
  router.push({
    name: "BmsComsigninsert"
  });
}

</script>