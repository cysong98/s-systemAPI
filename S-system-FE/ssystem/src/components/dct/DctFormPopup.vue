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
  <div class="modal-body">
    <div class="content">
      <div class="row">
        <div class="col">
          <div class="box-flex">
            <v-select
              v-model="comboSelected"
              label=""
              :items="comboData"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto" />
            <v-text-field
              v-model="comboInputData"
              variant="outlined"
              hide-details="auto"
              clearable
              @keyup.enter="selectFormList(1)" />
            <v-btn class="magnify" @click="selectFormList(1)">
              <v-icon></v-icon>
            </v-btn>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-data-table
            v-model="checkedListSelectFormList"
            :headers="staticColumnsSelectFormList"
            :items="selectFormListList"
            :item-value="selectFormListIdentifier"
            :loading="loader"
            :no-data-text="noDataText"
            select-strategy="single"
            show-select
            hover
            class="table-type-02 narrow"
            fixed-header
          >
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
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopup()">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp()">확인</v-btn>
    </div>
  </div>
</template>
    
<script setup>
import console from "console";
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { API } from '@/api';
import _ from 'lodash';

const name = ref('BmsComOrgSelectt')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
})  
const route = useRoute()
const router = useRouter()
const urlPaths =  ref("");

// for column combo
const comboSelected = ref("formtitle")
const comboInputData = ref("")
const comboData = [
  { key: "formtitle", view: "서식명" },
  // { key: "categoryname", view: "카테고리명"}
]

/* ================ pagination ================ */
const totalPagesSelectFormList = ref(0)
const currentPageSelectFormList = ref(1)
const pageSizeSelectFormList = ref(10)
const pageSizesSelectFormList = ref([
  { view: "10개씩 보기", key: 10 },
  { view: "25개씩 보기", key: 25 },
  { view: "50개씩 보기", key: 50 },
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
const closePopup = () => {
  props.toggleFunc();
};

const childReturnValue = ref({});
const confirmPopUp = () => {
  childReturnValue.value = checkedListSelectFormList.value[0];

  if(childReturnValue.value == undefined) {
    childReturnValue.value = "";
  }
  props.returnFunc(childReturnValue.value);
};

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
  pageSize: 5,
}
const selectFormListCondi = ref({ ...selectFormListDefCondi })
const selectFormListCondiCheck = ref({ value: { ...selectFormListDefCondi }, flag: { ...selectFormListDefCondi } })


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


const staticColumnsSelectFormList = [
  // { title: "카테고리명", key: "categoryname", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "선택", key: "data-table-select", width: "30px" },
  { title: "서식명", key: "formtitle", width: "120px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "서식파일", key: "formname", width: "200px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];



/* ======================= multi selection ======================= */
const selectFormListIdentifier = computed(() => (item) => {
  item.identifier = {
    fileid: item.fileid,
    fileurl: item.fileurl,
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
const dateFormat = ref({ startDt: "", endDt: "" });
const selectFormList = async (pageNum) => {

  selectFormListCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;

  if (!(comboSelected.value == "" || comboSelected.value == undefined))
    selectFormListCondi.value[comboSelected.value] = comboInputData.value;
  try {
    const response = await API.comAPI.selectFormList({ ...selectFormListCondi.value }, urlPaths.value);

    selectFormListList.value = response.data.list;
    totalPagesSelectFormList.value = response.data.pages;
    loader.value = false;
    totalItem = response.data.total;
  } catch (error) {
    console.log(error);
  }
}

</script>