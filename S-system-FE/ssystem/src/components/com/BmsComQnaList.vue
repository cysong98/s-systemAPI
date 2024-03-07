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
      <h4 class="h4">게시판{{manageflag === true ? '관리' : ''}}</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">Q&A{{manageflag === true ? '관리' : ''}}</h5>
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
                  @keyup.enter="selectQnaList(1)"
                  v-model="comboInputData"
                  maxlength="100"
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
                  variant="outlined"
                  :min="dateFormat.startDt"
                  hide-details="auto"
                  required
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectQnaList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
            <v-btn class="button" @click="moveToBmsComqnainsert()">등록</v-btn>
            <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" class="button" @click="toggleDeletePopup">삭제</v-btn>
          </div>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsComqnadetail(item.item)"
          v-model="checkedListSelectQnaList"
          :headers="staticColumnsSelectQnaList"
          :items="selectQnaListList"
          :items-per-page="pageSizeSelectQnaList"
          :item-value="selectQnaListIdentifier"
          :loading="loader"
          :no-data-text="noDataText"
          :show-select="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001') ? true : false"
          hover
          class="table-type-05"
        >
          <template v-slot:item.title="{ item }">
            <td style="display: flex; background-color: #FFFFFF00; padding-top: 10px; padding-left: 20px;">
              <pre v-if="item.raw.depth > 1" v-for="i in item.raw.depth-1">          </pre>
              <p v-if="item.raw.depth === 0" class="text-left-03">{{ item.raw.title }}</p>
              <pre v-else class="branch-down"></pre>
              <p v-if="item.raw.depth !== 0" class="text-left-03">{{ item.raw.title }}</p>
            </td>
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectQnaList"
          :length="totalPagesSelectQnaList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectQnaList"
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
        :alertMsg="'Q&A 삭제 시 답변글도 모두 삭제됩니다.'"
        :returnFunc="deleteQna"
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
import { ref, onBeforeMount, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api';
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name = ref('BmsComQnaList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("title")
const comboInputData = ref("")
const comboData = [
  { key: "title", view: "제목"}, 
  { key: "inname", view: "등록자"}
]
// for selectQnaList
const loader = ref(true)
const checkedListSelectQnaList = ref([])
const selectQnaListList = ref([])
const selectQnaListDefCondi = {
  title: "",
  inname: "",
  startDt: "",
  endDt: "",
  refid: "",
  qnaid: "",
  pageNum: 1,
  pageSize: 100,
}
const selectQnaListCondi = ref({ ...selectQnaListDefCondi })
const selectQnaListCondiCheck = ref({ value: { ...selectQnaListDefCondi }, flag: { ...selectQnaListDefCondi } })
/* ================ pagination ================ */
const totalPagesSelectQnaList = ref(0)
const currentPageSelectQnaList = ref(1)
const pageSizeSelectQnaList = ref(0)

const handlePageChangeSelectQnaList = () => {
  selectQnaList(currentPageSelectQnaList.value);
}
/* ============================================== */
const manageflag = ref(false);

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  setCondiClear(selectQnaListCondiCheck.value, selectQnaListCondi.value);
  setQueries(route, selectQnaListCondi.value, selectQnaListDefCondi);
  await selectQnaList(selectQnaListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectQnaListCondi.value, selectQnaListDefCondi);
  await selectQnaList(selectQnaListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectQnaListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectQnaListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectQnaListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectQnaListCondiCheck.value, selectQnaListCondi.value, selectQnaListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectQnaListCondi.value["title"] = "";
  selectQnaListCondi.value["inname"] = "";
  setCondiChanged(selectQnaListCondiCheck.value, selectQnaListCondi.value, selectQnaListCondi.value);
})


const staticColumnsSelectQnaList = [
  { title: "제목", key: "title", width: "60%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "조회수", key: "selcnt", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "inname", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

/* ======================= multi selection ======================= */
const selectQnaListIdentifier = computed(() => (item) => {
  item.identifier = {
    qnaid: item.qnaid,
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectQnaListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectQnaListList.value.filter((item) =>
    checkedListSelectQnaList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});


let totalItem = 0;
const noDataText = ref("검색된 조건에 맞는 데이터가 없습니다.");
const dateFormat = ref({startDt: "", endDt: ""});
const selectQnaList = async (pageNum) => {

  loader.value = true;
  selectQnaListCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value)) {
      selectQnaListCondi.value[comboSelected.value] = comboInputData.value;
      if(selectQnaListCondi.value[comboSelected.value] === null)
        selectQnaListCondi.value[comboSelected.value] = "";
    }
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }

    // if()
  }
  try {
    if(dateFormat.value.startDt != "")
      selectQnaListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else 
      selectQnaListCondi.value.startDt = "";
    if(dateFormat.value.endDt != "")
      selectQnaListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else
      selectQnaListCondi.value.endDt = "";
        
    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectQnaListCondi.value.endDt = "";
    }
      
    const response = await API.comAPI.selectQnaList({ ...selectQnaListCondi.value }, urlPaths.value);
    if (response.data.length == 0) {
      noDataText.value = "조건에 맞는 데이터가 없습니다.";
      loader.value = false;
    }    

    response.data.list = response.data.list.map((u) => {
      let title = "";
      if(selectQnaListCondi.value.inname == "" && selectQnaListCondi.value.title == "" &&
            selectQnaListCondi.value.startDt == "" && selectQnaListCondi.value.endDt == "") {
      }

      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD'), title: title + u.title };
      return { ...u};
    });

    totalPagesSelectQnaList.value = response.data.pages;
    pageSizeSelectQnaList.value = response.data.pageSize;
    totalItem = response.data.total;
    
    selectQnaListList.value = [];
    if(totalItem !== 0) {
      for(let i=response.data.startRow; i<=response.data.endRow; i++) {
        selectQnaListList.value.push(response.data.list[i-1]);
      }
    }
    loader.value = false;
    
    setCondiClear(selectQnaListCondiCheck.value, selectQnaListCondi.value);
    if(selectQnaListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectQnaListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectQnaListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectQnaListCondi.value.endDt).format('YYYY-MM-DD');
  } catch (error) {
    console.log(error);
  }
}


const deleteQna = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteQna(selectQnaListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });
  await selectQnaList(1);
  checkedListSelectQnaList.value = [];
}


const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectQnaList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }

  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


// Move Function
const moveToBmsComqnadetail = (item) => {
  router.push({
    name: "BmsComqnadetail",
    query: {
      ...{qnaid: item.value.qnaid},
    }
  });
}

// Move Function
const moveToBmsComqnainsert = () => {
  router.push({
    name: "BmsComqnainsert"
  });
}

</script>

<style scoped>
.branch-down {
  width: 23px;
  height: 20px;
}
</style>