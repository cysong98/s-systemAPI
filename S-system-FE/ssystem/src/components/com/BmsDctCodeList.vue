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
          <h5 class="h5">코드관리</h5>
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
                  @keyup.enter="selectCodeList(1)"
                  v-model="comboInputData"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
              </td>
              <th>코드분류 ID</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="selectCodeListCondi.tcodeid"
                  maxlength="6"
                  variant="outlined"
                  hide-details="auto"
                  required
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectCodeListRouterPush()">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="moveToBmsDctcodecategorylist()" outlined>&lt; 코드분류</v-btn>
              <v-btn @click="bmsDctCodeInsertPopupToggleFunc()" outlined>등록</v-btn>
              <v-btn @click="bmsDctCodeUpdatePopupToggleFunc(checkedListSelectCodeList)" outlined>수정</v-btn>
              <v-btn @click="toggleDeletePopup()" outlined>삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectCodeList"
              :items="pageSizesSelectCodeList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectCodeList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          v-model="checkedListSelectCodeList"
          :headers="staticColumnsSelectCodeList"
          :items="selectCodeListList"
          :items-per-page="pageSizeSelectCodeList"
          :loading="selectCodeListLoader"
          :item-value="selectCodeListIdentifier"
          show-select
          hover
          class="table-type-05"
        >
          <template v-slot:item.description="{ item }">
            <td style="background-color: #FFFFFF00;">
              <p class="text-left-02">{{ item.raw.description }}</p>
            </td>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectCodeList"
          :length="totalPagesSelectCodeList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectCodeList"
        ></v-pagination>
      </section>
    </div>
  </div>

  <v-dialog v-model="bVisibleBmsDctCodeInsertPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>코드 등록</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleBmsDctCodeInsertPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctCodeInsertPopup
        v-if="bVisibleBmsDctCodeInsertPopup"
        :args="bmsDctCodeInsertPopupArgs"
        :toggleFunc="bmsDctCodeInsertPopupToggleFunc"
        :returnFunc="bmsDctCodeInsertPopupReturnFunc"
      ></BmsDctCodeInsertPopup>
    </v-card>
  </v-dialog>


  <v-dialog v-model="bVisibleBmsDctCodeUpdatePopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>코드 수정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleBmsDctCodeUpdatePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctCodeUpdatePopup
        v-if="bVisibleBmsDctCodeUpdatePopup"
        :args="bmsDctCodeUpdatePopupArgs"
        :toggleFunc="bmsDctCodeUpdatePopupToggleFunc"
        :returnFunc="bmsDctCodeUpdatePopupReturnFunc"
      ></BmsDctCodeUpdatePopup>
    </v-card>
  </v-dialog>
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
        :returnFunc="deleteCode"
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
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';
import _ from 'lodash';

import BmsDctCodeInsertPopup from "./BmsDctCodeInsertPopup.vue";
import BmsDctCodeUpdatePopup from "./BmsDctCodeUpdatePopup.vue";
import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name =ref("BmsDctCodeList"); /* =======  popup props variables & functions for parent ======= */
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("codename");
const comboInputData = ref("");
const comboData = [
  { key: "codename", view: "코드명" },
  { key: "codeid", view: "코드아이디" },
];
// for selectCodeList
const checkedListSelectCodeList = ref([])
const selectCodeListList = ref([])
const selectCodeListDefCondi = {
  codeid: "",
  codename: "",
  tcodeid: "",
  pageNum: 1,
  pageSize: 10,
}
const selectCodeListCondi = ref({ ...selectCodeListDefCondi })
const selectCodeListCondiCheck = ref({ value: { ...selectCodeListDefCondi }, flag: { ...selectCodeListDefCondi } })

/* ================ pagination ================ */
const selectCodeListLoader = ref(true)
const totalPagesSelectCodeList = ref(0)
const currentPageSelectCodeList = ref(1)
const pageSizeSelectCodeList = ref(10)
const pageSizesSelectCodeList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectCodeList = () => {
  selectCodeListCondi.value.pageSize = pageSizeSelectCodeList.value;
  currentPageSelectCodeList.value = 1;
  selectCodeList(currentPageSelectCodeList.value);
}
const handlePageChangeSelectCodeList = () => {
  selectCodeList(currentPageSelectCodeList.value);
  checkedListSelectCodeList.value = [];
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectCodeListCondiCheck.value, selectCodeListCondi.value);
  setQueries(route, selectCodeListCondi.value, selectCodeListDefCondi);
  await selectCodeList(1);
});

watch(route, async (route) => {
  setQueries(route, selectCodeListCondi.value, selectCodeListDefCondi);
  await selectCodeList(selectCodeListCondi.value.pageNum);
})

watch(() => _.cloneDeep(selectCodeListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectCodeListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
  selectCodeListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectCodeListCondiCheck.value, selectCodeListCondi.value, selectCodeListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectCodeListCondi.value["codeid"] = "";
  selectCodeListCondi.value["codename"] = "";
  setCondiChanged(selectCodeListCondiCheck.value, selectCodeListCondi.value, selectCodeListCondi.value);
})


const staticColumnsSelectCodeList = [
  { key: "codeid", title: "코드 ID", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "codename", title: "코드명", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "description", title: "설명", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]


const bVisibleBmsDctCodeInsertPopup = ref(false);
const bmsDctCodeInsertPopupReuturn = ref({});
const bmsDctCodeInsertPopupArgs = ref({});
const bmsDctCodeInsertPopupToggleFunc = ref(() => {
  bVisibleBmsDctCodeInsertPopup.value = !bVisibleBmsDctCodeInsertPopup.value;
  bmsDctCodeInsertPopupArgs.value = selectCodeListCondi.value;
});
const bmsDctCodeInsertPopupReturnFunc = ref(async (retValue) => {
  bmsDctCodeInsertPopupToggleFunc.value();
  bmsDctCodeInsertPopupReuturn.value = retValue;
  await selectCodeList(1);
});

const bVisibleBmsDctCodeUpdatePopup = ref(false);
const bmsDctCodeUpdatePopupReuturn = ref({});
const bmsDctCodeUpdatePopupArgs = ref({});
const bmsDctCodeUpdatePopupToggleFunc = ref((paramValue) => {
  if (paramValue.length > 1) {
    alert("수정할 항목을 하나만 선택해주세요.");
    bVisibleBmsDctCodeUpdatePopup.value = false;
  } 
  else if (paramValue.length < 1) {
    alert("수정할 항목을 선택해주세요.");
    bVisibleBmsDctCodeUpdatePopup.value = false;
  }
  else {
    bVisibleBmsDctCodeUpdatePopup.value = !bVisibleBmsDctCodeUpdatePopup.value;
    bmsDctCodeUpdatePopupArgs.value = paramValue;
  }
});
const bmsDctCodeUpdatePopupReturnFunc = ref(async () => {
  bVisibleBmsDctCodeUpdatePopup.value = false;
  // bmsDctCodeUpdatePopupReuturn.value = retValue;
  currentPageSelectCodeList.value = 1;
  selectCodeListCondi.value.pageNum = 1;
  await selectCodeList(1);
});

const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectCodeList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});

/* ======================= multi selection ======================= */
const selectCodeListIdentifier = computed(() => (item) => {
  item.identifier = `${item.codeid}`;
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectCodeListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectCodeListList.value.filter((item) =>
    checkedListSelectCodeList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectCodeListRouterPush = () => {
  router
    .push({
      query: selectCodeListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const selectCodeList = async (pageNum) => {
  selectCodeListLoader.value = true;
  selectCodeListCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectCodeListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectCodeListLoader.value = false;
      return;
    }
  }
  try {
    const response = await API.comAPI.selectCodeList({ ...selectCodeListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      return { ...u};
    });

    selectCodeListList.value = response.data.list;
    totalPagesSelectCodeList.value = response.data.pages;
    selectCodeListLoader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectCodeListCondiCheck.value, selectCodeListCondi.value)
  } catch (error) {
    console.log(error);
  }
}


const deleteCode = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteCode(selectCodeListFinalData.value, urlPaths.value)
    .then((response) => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
    .catch((error) => {
      console.log(error);
    });

  comboInputData.value = "";
  setCondiClear(selectCodeListCondiCheck.value, selectCodeListCondi.value);
  await selectCodeList(1);
  checkedListSelectCodeList.value = [];
}


// Move Function
const moveToBmsDctcodecategorylist = () => {
  router.push({
    name: "BmsDctcodecategorylist"
  });
}

</script>