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
          <h5 class="h5">서식관리</h5>
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
                  @keyup.enter="selectFormList(1)"
                  v-model="comboInputData"
                  maxlength="499"
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
          <v-btn class="magnify-solid" @click="selectFormList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveToBmsDctforminsert()">등록</v-btn>
              <v-btn class="button" @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectFormList"
              :items="pageSizesSelectFormList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectFormList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsDctformdetail(item.item)"
          v-model="checkedListSelectFormList"
          :headers="staticColumnsSelectFormList"
          :items="selectFormListList"
          :items-per-page="pageSizeSelectFormList"
          :item-value="selectFormListIdentifier"
          :loading="loader"
          :no-data-text="noDataText"
          show-select
          hover
          class="table-type-05"
        >
          <template v-slot:item.btn="{ item }">
            <v-btn
              type="button"
              border="0"
              color="#FFFFFF00"
              class="h-100"
            >
              <v-icon size="x-large" color="#35aee7">mdi-syllabary-hangul</v-icon>
            </v-btn>
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectFormList"
          :length="totalPagesSelectFormList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectFormList"
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
        :alertMsg="'서식 파일이 영구히 삭제됩니다.'"
        :returnFunc="deleteForm"
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
// import { makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';
import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name = ref('BmsDctFormList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("formtitle")
const comboInputData = ref("")
const comboData = [
  { key: "formtitle", view: "서식명" },
]
// for selectFormList
const loader = ref(true)
const checkedListSelectFormList = ref([])
const selectFormListList = ref([])
const selectFormListDefCondi = {
  // categoryname: "",
  formtitle: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}
const selectFormListCondi = ref({ ...selectFormListDefCondi })
const selectFormListCondiCheck = ref({ value: { ...selectFormListDefCondi }, flag: { ...selectFormListDefCondi } })
/* ================ pagination ================ */
const totalPagesSelectFormList = ref(0)
const currentPageSelectFormList = ref(1)
const pageSizeSelectFormList = ref(10)
const pageSizesSelectFormList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectFormList = () => {
  selectFormListCondi.value.pageSize = pageSizeSelectFormList.value;
  currentPageSelectFormList.value = 1;
  selectFormList(currentPageSelectFormList.value);
}
const handlePageChangeSelectFormList = () => {
  selectFormList(currentPageSelectFormList.value);
}
/* ============================================== */


onMounted(async () => {
  setCondiClear(selectFormListCondiCheck.value, selectFormListCondi.value);
  setQueries(route, selectFormListCondi.value, selectFormListDefCondi);
  await selectFormList(selectFormListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectFormListCondi.value, selectFormListDefCondi);
  await selectFormList(selectFormListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectFormListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectFormListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectFormListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectFormListCondiCheck.value, selectFormListCondi.value, selectFormListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectFormListCondi.value["formtitle"] = "";
  setCondiChanged(selectFormListCondiCheck.value, selectFormListCondi.value, selectFormListCondi.value);
})


const staticColumnsSelectFormList = [
  // { title: "", key: "data-table-select", width: "30px"},
  { title: "서식명", key: "formtitle", width: "25%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "서식파일", key: "formname", width: "30%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "서식", key: "btn", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "username", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];



/* ======================= multi selection ======================= */
const selectFormListIdentifier = computed(() => (item) => {
  item.identifier = {
    fileid: item.fileid,
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectFormListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectFormListList.value.filter((item) =>
    checkedListSelectFormList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
/* ============================================================== */

let totalItem = 0;
const noDataText = ref("조건에 맞는 데이터가 없습니다.");
const dateFormat = ref({startDt: "", endDt: ""});
const selectFormList = async (pageNum) => {
  selectFormListCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectFormListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt !== "")
      selectFormListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else
      selectFormListCondi.value.startDt = "";
    if(dateFormat.value.endDt !== "")
      selectFormListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectFormListCondi.value.endDt = "";

    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectFormListCondi.value.endDt = "";
    }

    const response = await API.comAPI.selectFormList({ ...selectFormListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')};
      return { ...u};
    });

    selectFormListList.value = response.data.list;
    totalPagesSelectFormList.value = response.data.pages;
    loader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectFormListCondiCheck.value, selectFormListCondi.value)
    if(selectFormListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectFormListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectFormListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectFormListCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}


const deleteForm = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteForm(selectFormListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });

  await selectFormList(selectFormListCondi.value.pageNum);
  checkedListSelectFormList.value = [];
}


const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectFormList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


// Move Function
const moveToBmsDctformdetail = (item) => {
  router.push({
    name: "BmsDctformdetail",
    query: {
      ...{fileid: item.value.fileid}
    }
  });
}

// Move Function
const moveToBmsDctforminsert = () => {
  router.push({
    name: "BmsDctforminsert"
  });
}

</script>