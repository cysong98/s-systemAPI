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
          <h5 class="h5">관인관리</h5>
        </div>
      </section>

      <section class="section">
        <div class="box-condition">
          <table>
            <tr>
              <th>검색어</th>
              <td>
                <v-select
                  label="선택"
                  v-model="comboSelected"
                  :items="comboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field
                  @keyup.enter="selectGwanList(1)"
                  v-model="comboInputData"
                  :maxlength="maxLengthFunc()"
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
          <v-btn class="magnify-solid" @click="selectGwanList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveToBmsComgwaninsert()">등록</v-btn>
              <v-btn class="button" @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectGwanList"
              :items="pageSizesSelectGwanList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectGwanList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsComgwandetail(item.item)"
          v-model="checkedListSelectGwanList"
          :headers="staticColumnsSelectGwanList"
          :items="selectGwanListList"
          :items-per-page="pageSizeSelectGwanList"
          :item-value="selectGwanListIdentifier"
          :loading="loader"
          :no-data-text="noDataText"
          show-select
          hover
          class="table-type-05"
        >
          <template v-slot:item.gwaningubun="{ item }">
            {{ displayGwaningubun(item.raw.managegubun) }}
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectGwanList"
          :length="totalPagesSelectGwanList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectGwanList"
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
        :alertMsg="'관인정보가 영구 삭제됩니다.'"
        :returnFunc="deleteGwanBIA"
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

const name = ref('BmsComGwanList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("gwaninname")
const comboInputData = ref("")
const comboData = [
  { key: "gwaninname", view: "관인명" },
  { key: "etc", view: "관인종류"},
  { key: "orgname", view: "관리부서"}
]
// for selectGwanList
const loader = ref(true)
const checkedListSelectGwanList = ref([])
const selectGwanListList = ref([])
const selectGwanListDefCondi = {
  gwaninname: "",
  etc: "",
  orgname: "",
  managegubun: "ORGE1",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}
const selectGwanListCondi = ref({ ...selectGwanListDefCondi })
const selectGwanListCondiCheck = ref({ value: { ...selectGwanListDefCondi }, flag: { ...selectGwanListDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "orgname")
    return 255;
  else
    return 100;
};

/* ================ pagination ================ */
const totalPagesSelectGwanList = ref(0)
const currentPageSelectGwanList = ref(1)
const pageSizeSelectGwanList = ref(10)
const pageSizesSelectGwanList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectGwanList = () => {
  selectGwanListCondi.value.pageSize = pageSizeSelectGwanList.value;
  currentPageSelectGwanList.value = 1;
  selectGwanList(currentPageSelectGwanList.value);
}
const handlePageChangeSelectGwanList = () => {
  selectGwanList(currentPageSelectGwanList.value);
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectGwanListCondiCheck.value, selectGwanListCondi.value);
  setQueries(route, selectGwanListCondi.value, selectGwanListDefCondi);
  await selectGwanList(selectGwanListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectGwanListCondi.value, selectGwanListDefCondi);
  await selectGwanList(selectGwanListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectGwanListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectGwanListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectGwanListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectGwanListCondiCheck.value, selectGwanListCondi.value, selectGwanListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectGwanListCondi.value["gwaninname"] = "";
  selectGwanListCondi.value["etc"] = "";
  selectGwanListCondi.value["orgname"] = "";
  setCondiChanged(selectGwanListCondiCheck.value, selectGwanListCondi.value, selectGwanListCondi.value);
})


const staticColumnsSelectGwanList = [
  { title: "관인명", key: "gwaninname", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "관인종류", key: "gwaningubun", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "관리부서", key: "orgname", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "username", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

/* ======================= multi selection ======================= */
const selectGwanListIdentifier = computed(() => (item) => {
  item.identifier = {
    orgid: item.orgid,
    seq: item.seq
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const checkedGwanListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectGwanListList.value.filter((item) =>
    checkedListSelectGwanList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
/* ============================================================== */


const displayGwaningubun = (item) => {
  if(item === "ORGE1")
    return "직인";
  else
    return "특수관인";
}


let totalItem = 0;
const length = ref(0);
const noDataText = ref("조건에 맞는 데이터가 없습니다.");
const dateFormat = ref({startDt: "", endDt: ""});
const selectGwanList = async (pageNum) => {

  selectGwanListCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectGwanListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt != "")
      selectGwanListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else 
      selectGwanListCondi.value.startDt = "";
    if(dateFormat.value.endDt != "")
      selectGwanListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectGwanListCondi.value.endDt = "";
        
    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectGwanListCondi.value.endDt = "";
    }

    const response = await API.comAPI.selectGwanList({ ...selectGwanListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')};
      return { ...u};
    });

    selectGwanListList.value = response.data.list;
    totalPagesSelectGwanList.value = response.data.pages;
    totalItem = response.data.total;
    
    loader.value = false;
    length.value = response.data.list.length;

    setCondiClear(selectGwanListCondiCheck.value, selectGwanListCondi.value)
    if(selectGwanListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectGwanListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectGwanListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectGwanListCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}

const deleteGwanBIA = async (retValue) => {
  toggleDeletePopup.value();
  // 마지막 페이지의 모든 항목을 삭제한 경우
  if(checkedListSelectGwanList.value.length == length.value) {
    selectGwanListCondi.value.pageNum = 1;
  }

  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteGwanBIA(checkedGwanListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });

  await selectGwanList(selectGwanListCondi.value.pageNum);
  checkedListSelectGwanList.value = [];
}


const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectGwanList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


/* ================== Move Function ================== */
const moveToBmsComgwandetail = (item) => {
  router.push({
    name: "BmsComgwandetail",
    query: {
      ...{orgid: item.value.orgid},
      ...{seq: item.value.seq}
    }
  });
}

// Move Function
const moveToBmsComgwaninsert = () => {
  router.push({
    name: "BmsComgwaninsert"
  });
}
/* =================================================== */

</script>