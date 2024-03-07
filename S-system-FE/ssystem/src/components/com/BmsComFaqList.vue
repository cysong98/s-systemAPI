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
          <h5 class="h5">FAQ{{manageflag === true ? '관리' : ''}}</h5>
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
                  @keyup.enter="selectFaqList(1)"
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
                  label=""
                  v-model="dateFormat.startDt"
                  variant="outlined"
                  hide-details="auto"
                  required
                />
                ~
                <v-text-field
                  type="date"
                  label=""
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
          <v-btn class="magnify-solid" @click="selectFaqList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="moveToBmsComfaqinsert()">등록</v-btn>
              <v-btn v-if="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001')" outlined @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectFaqList"
              :items="pageSizesSelectFaqList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectFaqList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsComfaqdetail(item.item)"
          v-model="checkedListSelectFaqList"
          :headers="staticColumnsSelectFaqList"
          :items="selectFaqListList"
          :items-per-page="pageSizeSelectFaqList"
          :loading="selectFaqListLoader"
          :item-value="selectFaqListIdentifier"
          :no-data-text="noDataText"
          :show-select="getUserLoginData.hasOwnProperty('vgroupid') && getUserLoginData.vgroupid.includes('4001') ? true : false"
          hover
          class="table-type-05"
        >
          <template v-slot:item.title="{ item }">
            <td style="padding-left: 20px; background-color: #FFFFFF00;">
              <p :class="{ 'text-left-03': item }" >{{ item.raw.title }}</p>
            </td>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectFaqList"
          :length="totalPagesSelectFaqList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectFaqList"
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
        :alertMsg="''"
        :returnFunc="deleteFaqBIA"
      ></BmsComDeletePopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from "dayjs";
import { ref, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from "lodash";
import { API } from "@/api";
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name = ref("BmsComFaqList");
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("title");
const comboInputData = ref("");
const comboData = [
  { view: '제목', key: 'title'},
  { view: '등록자', key: 'inname'},
];
// for selectFaqList
const checkedListSelectFaqList = ref([]);
const selectFaqListList = ref([]);
const selectFaqListDefCondi = {
  title: "",
  inname: "",
  userid: getUserLoginData.value.userid,   // 사용자의 소속 부서를 조회하기 위함
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10
};
const selectFaqListCondi = ref({ ...selectFaqListDefCondi });
const selectFaqListCondiCheck = ref({ value: { ...selectFaqListDefCondi }, flag: { ...selectFaqListDefCondi } });
/* ================ pagination ================ */
const selectFaqListLoader = ref(true);
const totalPagesSelectFaqList = ref(0);
const currentPageSelectFaqList = ref(1);
const pageSizeSelectFaqList = ref(10);
const pageSizesSelectFaqList = ref([
  { view: "10개씩 보기", key: 10 },
  { view: "25개씩 보기", key: 25 },
  { view: "50개씩 보기", key: 50 },
]);

const handlePageSizeChangeSelectFaqList = () => {
  selectFaqListCondi.value.pageSize = pageSizeSelectFaqList.value;
  currentPageSelectFaqList.value = 1;
  selectFaqList(currentPageSelectFaqList.value);
};
const handlePageChangeSelectFaqList = () => {
  selectFaqList(currentPageSelectFaqList.value);
};
/* ============================================== */
const manageflag = ref(false);

onMounted(async () => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001')){
    manageflag.value = true;
  }
  setCondiClear(selectFaqListCondiCheck.value, selectFaqListCondi.value);
  setQueries(route, selectFaqListCondi.value, selectFaqListDefCondi);
  await selectFaqList(selectFaqListCondi.value.pageNum);
});

watch(route, async (route) => {
  setQueries(route, selectFaqListCondi.value, selectFaqListDefCondi);
  await selectFaqList(selectFaqListCondi.value.pageNum);
});

watch(() => _.cloneDeep(selectFaqListCondi.value), (newVal, oldVal) => {
    setCondiChanged(selectFaqListCondiCheck.value, newVal, oldVal);
});

watch(comboInputData, async (comboInputData) => {
  if (!(comboSelected.value == "" || comboSelected.value == undefined))
    selectFaqListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectFaqListCondiCheck.value, selectFaqListCondi.value, selectFaqListCondi.value);
});

watch(comboSelected, async (comboSelected) => {
  selectFaqListCondi.value["title"] = "";
  selectFaqListCondi.value["inname"] = "";
  setCondiChanged(selectFaqListCondiCheck.value, selectFaqListCondi.value, selectFaqListCondi.value);
});


const staticColumnsSelectFaqList = [
  { title: "제목", key: "title", width: "60%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "조회수", key: "selcnt", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록자", key: "inname", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "등록일", key: "indt", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

/* ======================= multi selection ======================= */
const selectFaqListIdentifier = computed(() => (item) => {
  item.identifier = {
    faqid: item.faqid,
    deptid: item.deptid,
  };
  return item.identifier;
});

/* =========================================================== */

let totalItem = 0;
const noDataText = ref("조건에 맞는 데이터가 없습니다.");
const dateFormat = ref({startDt: "", endDt: ""});
const selectFaqList = async (pageNum) => {

  selectFaqListLoader.value = true;
  selectFaqListCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectFaqListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectFaqListLoader.value = false;
      return;
    }
  }
  try {
    if(dateFormat.value.startDt !== "")
      selectFaqListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
    else
      selectFaqListCondi.value.startDt = "";
    if(dateFormat.value.endDt !== "") 
      selectFaqListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
    else 
      selectFaqListCondi.value.endDt = "";
    
    if(dateFormat.value.startDt > dateFormat.value.endDt) {
      dateFormat.value.endDt = "";
      selectFaqListCondi.value.endDt = "";
    }

    // 시스템관리자의 경우 userid를 입력하지 않는다.
    if(getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001'))
      selectFaqListCondi.value.userid = "";

    const response = await API.comAPI.selectFaqList({ ...selectFaqListCondi.value }, urlPaths.value);
    if (response.data.length == 0) {
      selectFaqListLoader.value = false;
    }
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      return { ...u};
    });

    selectFaqListList.value = response.data.list;
    totalPagesSelectFaqList.value = response.data.pages;
    selectFaqListLoader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectFaqListCondiCheck.value, selectFaqListCondi.value);
    if(selectFaqListCondi.value.startDt != "")
      dateFormat.value.startDt = dayjs(selectFaqListCondi.value.startDt).format('YYYY-MM-DD');
    if(selectFaqListCondi.value.endDt != "") 
      dateFormat.value.endDt = dayjs(selectFaqListCondi.value.endDt).format('YYYY-MM-DD');
    
  } catch (error) {
    console.log(error);
  }
};

const deleteFaqBIA = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteFaqBIA(checkedListSelectFaqList.value, urlPaths.value)
    .then((response) => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
    .catch((error) => {
      console.log(error);
  });
  await selectFaqList(selectFaqListCondi.value.pageNum);
  checkedListSelectFaqList.value = [];
};


// toggleDeletePopup
const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectFaqList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }

  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});


// Move Function
const moveToBmsComfaqinsert = () => {
  router.push({
    name: "BmsComfaqinsert"
  });
};

// Move Function
const moveToBmsComfaqdetail = (item) => {
  router.push({
    name: "BmsComfaqdetail",
    query: {
      ...{faqid: item.value.faqid},
    },
  });
};

</script>
