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
      <h4 class="h4">기안</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">임시저장함</h5>
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
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectTempList(1)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>저장기간</th>
              <td>
                <v-text-field
                  v-model="selectTempListCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
                ~
                <v-text-field
                  v-model="selectTempListCondi.endDt"
                  type="date"
                  :min="selectTempListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectTempList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ selectTempListCnt }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="deleteTempList()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectTempList"
              :items="pageSizesSelectTempList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectTempList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          v-model="checkedListSelectTempList"
          :headers="staticColumnsSelectTempList"
          :items="selectTempListList"
          :items-per-page="pageSizeSelectTempList"
          :loading="selectTempListLoader"
          :item-value="selectTempListIdentifier"
          show-select
          @click:row="(event, item, pathname) => moveDetailPage(event, item.item.raw.docid, 'BmsDcttempcard')"
          class="table-type-04"
          hover
        >
          <template v-slot:item.docttl="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectTempList"
          :length="totalPagesSelectTempList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectTempList"
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
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { storeToRefs } from 'pinia';
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsDctTempList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("docttl")
const comboInputData = ref("")
const comboData = [
  { view: "제목", key: "docttl"},
];
// for selectTempList
const checkedListSelectTempList = ref([])
const selectTempListList = ref([])
const selectTempListCnt = ref(0)
const selectTempListDefCondi = {
    docid: "",
    docttl: "",
    startDt: "",
    endDt: "",
    authorid: "",
    pageNum: 1,
    pageSize: 10,
    sortDirection: "ASC",
    sortItem: "docid"
  }
const selectTempListCondi = ref({ ...selectTempListDefCondi })
const selectTempListCondiCheck = ref({ value: { ...selectTempListDefCondi }, flag: { ...selectTempListDefCondi } })
/* ================ pagination ================ */
const selectTempListLoader = ref(true)
const totalPagesSelectTempList = ref(0)
const currentPageSelectTempList = ref(1)
const pageSizeSelectTempList = ref(10)
const pageSizesSelectTempList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectTempList = () => {
  selectTempListCondi.value.pageSize = pageSizeSelectTempList.value;
  currentPageSelectTempList.value = 1;
  selectTempList(currentPageSelectTempList.value);
}
const handlePageChangeSelectTempList = () => {
  selectTempList(currentPageSelectTempList.value);
}
/* ============================================== */


// for deleteTempList
const deleteTempListDefCondi = [{}]
const deleteTempListCondi = ref({ ...deleteTempListDefCondi })

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectTempListCondiCheck.value, selectTempListCondi.value);
  setQueries(route, selectTempListCondi.value, selectTempListDefCondi);
  await selectTempList(selectTempListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectTempListCondi.value, selectTempListDefCondi);
  await selectTempList(selectTempListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectTempListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectTempListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectTempListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectTempListCondiCheck.value, selectTempListCondi.value, selectTempListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectTempListCondi.value["docttl"] = "";
    selectTempListCondi.value["indt"] = "";
  setCondiChanged(selectTempListCondiCheck.value, selectTempListCondi.value, selectTempListCondi.value);
})

watchEffect(() => {
})


const staticColumnsSelectTempList = [
  { key: "indt", title: "저장일시", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "작성자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordeptname", title: "작성부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

/* ======================= multi selection ======================= */
const selectTempListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectTempListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectTempListList.value.filter((item) =>
    checkedListSelectTempList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */


const selectTempList = async (pageNum) => {

  selectTempListLoader.value = true;
  selectTempListCondi.value.pageNum = parseInt(pageNum);

  selectTempListCondi.value.authorid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectTempListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectTempListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectTempList({ ...selectTempListCondi.value }, urlPaths.value);
    
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm') };
      return { ...u};
    });

    selectTempListList.value = response.data.list;
    selectTempListCnt.value = response.data.total;
    totalPagesSelectTempList.value = response.data.pages;
    const allOption = pageSizesSelectTempList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectTempListLoader.value = false;

    setCondiClear(selectTempListCondiCheck.value, selectTempListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// delete tempList
const deleteTempList = async () => {

  if (checkedListSelectTempList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  } 

  if (!confirm("삭제하시겠습니까?")) {
    return;
  }

  deleteTempListCondi.value = checkedListSelectTempList.value.map(item => ({docid: item}));

  await API.dctAPI
    .deleteTempList(deleteTempListCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 삭제되었습니다");
        selectTempList(1);
        checkedListSelectTempList.value = [];
      }
    })
    .catch(error => {
      const statusCode = error.response.status;
      const errorMsg = error.response.data;
      alert("Server Error");
      selectTempList(1);
    });
}


// 로우클릭
const moveDetailPage = (event, row, pathname) => {
  const id = event.target.id;
  if (id != "") { 
    return;
  }

  const param = {docid: row, frompage: "BmsDcttemplist"};
  router.push({
    name: pathname,
    query: {
      ...param
    }
  });
}

</script>