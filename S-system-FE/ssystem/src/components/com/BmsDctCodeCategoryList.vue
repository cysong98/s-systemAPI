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
          <h5 class="h5">코드분류관리</h5>
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
                  @keyup.enter="selectCodeCategoryList(1)"
                  v-model="comboInputData"
                  :maxlength="maxLengthFunc()"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
              </td>
              <th></th>
              <td></td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectCodeCategoryListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn outlined @click="bmsDctCodeCategoryInsertPopupToggleFunc()">등록</v-btn>
              <v-btn outlined @click="bmsDctCodeCategoryUpdatePopupToggleFunc(checkedListSelectCodeCategoryList)">수정</v-btn>
              <v-btn outlined @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectCodeCategoryList"
              :items="pageSizesSelectCodeCategoryList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectCodeCategoryList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsDctcodelist(item.item)"
          v-model="checkedListSelectCodeCategoryList"
          :headers="staticColumnsSelectCodeCategoryList"
          :items="selectCodeCategoryListList"
          :items-per-page="pageSizeSelectCodeCategoryList"
          :loading="selectCodeCategoryListLoader"
          :item-value="selectCodeCategoryListIdentifier"
          show-select
          hover
          class="table-type-05"
        >
          <template v-slot:item.description="{ item }">
            <td style="background-color: #FFFFFF00;">
              <p :class="{'text-left-03': item}">{{ item.raw.description }}</p>
            </td>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectCodeCategoryList"
          :length="totalPagesSelectCodeCategoryList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectCodeCategoryList"
        ></v-pagination>
      </section>
    </div>
  </div>

  <v-dialog v-model="bVisibleBmsDctCodeCategoryInsertPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>코드분류 등록</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleBmsDctCodeCategoryInsertPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctCodeCategoryInsertPopup
        v-if="bVisibleBmsDctCodeCategoryInsertPopup"
        :args="bmsDctCodeCategoryInsertPopupArgs"
        :toggleFunc="bmsDctCodeCategoryInsertPopupToggleFunc"
        :returnFunc="bmsDctCodeCategoryInsertPopupReturnFunc"
      ></BmsDctCodeCategoryInsertPopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="bVisibleBmsDctCodeCategoryUpdatePopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>코드분류 수정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleBmsDctCodeCategoryUpdatePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctCodeCategoryUpdatePopup
        v-if="bVisibleBmsDctCodeCategoryUpdatePopup"
        :args="bmsDctCodeCategoryUpdatePopupArgs"
        :toggleFunc="bmsDctCodeCategoryUpdatePopupToggleFunc"
        :returnFunc="bmsDctCodeCategoryUpdatePopupReturnFunc"
      ></BmsDctCodeCategoryUpdatePopup>
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
        :alertMsg="'하위 코드까지 모두 삭제됩니다.'"
        :returnFunc="deleteCodeCategory"
      ></BmsComDeletePopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import { ref, onMounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";

import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';
import { popups, togglePopup } from "@/utils/Popup.js";
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';

import BmsDctCodeCategoryInsertPopup from "./BmsDctCodeCategoryInsertPopup.vue";
import BmsDctCodeCategoryUpdatePopup from "./BmsDctCodeCategoryUpdatePopup.vue";
import BmsComDeletePopup from "./BmsComDeletePopup.vue";

const name = ref('BmsDctCodeCategoryList')/* =======  popup props variables & functions for parent ======= */
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
const props = defineProps({
  args: Object
})
const args = ref({})
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("tcodename");
const comboInputData = ref("");
const comboData = [
  { key: "tcodeid", view: "코드분류아이디" },
  { key: "tcodename", view: "코드분류명" },
];
// for selectCodeCategoryList
const checkedListSelectCodeCategoryList = ref([])
const selectCodeCategoryListList = ref([])
const selectCodeCategoryListDefCondi = {
  tcodeid: "",
  tcodename: "",
  pageNum: 1,
  pageSize: 10,
}
const selectCodeCategoryListCondi = ref({ ...selectCodeCategoryListDefCondi })
const selectCodeCategoryListCondiCheck = ref({ value: { ...selectCodeCategoryListDefCondi }, flag: { ...selectCodeCategoryListDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "tcodename")
    return 100;
  else
    return 6;
};

/* ================ pagination ================ */
const selectCodeCategoryListLoader = ref(true)
const totalPagesSelectCodeCategoryList = ref(0)
const currentPageSelectCodeCategoryList = ref(1)
const pageSizeSelectCodeCategoryList = ref(10)
const pageSizesSelectCodeCategoryList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectCodeCategoryList = () => {
  selectCodeCategoryListCondi.value.pageSize = pageSizeSelectCodeCategoryList.value;
  currentPageSelectCodeCategoryList.value = 1;
  selectCodeCategoryList(currentPageSelectCodeCategoryList.value);
}
const handlePageChangeSelectCodeCategoryList = () => {
  selectCodeCategoryList(currentPageSelectCodeCategoryList.value);
  checkedListSelectCodeCategoryList.value = [];
}
/* ============================================== */


onMounted(async () => {
  setCondiClear(selectCodeCategoryListCondiCheck.value, selectCodeCategoryListCondi.value);
  setQueries(route, selectCodeCategoryListCondi.value, selectCodeCategoryListDefCondi);
  await selectCodeCategoryList(selectCodeCategoryListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectCodeCategoryListCondi.value, selectCodeCategoryListDefCondi);
  await selectCodeCategoryList(selectCodeCategoryListCondi.value.pageNum);
})

watch(() => _.cloneDeep(selectCodeCategoryListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectCodeCategoryListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectCodeCategoryListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectCodeCategoryListCondiCheck.value, selectCodeCategoryListCondi.value, selectCodeCategoryListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectCodeCategoryListCondi.value["tcodeid"] = "";
  selectCodeCategoryListCondi.value["tcodename"] = "";
  setCondiChanged(selectCodeCategoryListCondiCheck.value, selectCodeCategoryListCondi.value, selectCodeCategoryListCondi.value);
})

watchEffect(() => {
  args.value = props.args
})


const staticColumnsSelectCodeCategoryList = [
  { key: "tcodeid", title: "코드분류아이디", width: "13%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "tcodename", title: "코드분류명", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "description", title: "코드설명", width: "57%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "modifyflag", title: "수정여부", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

const bVisibleBmsDctCodeCategoryInsertPopup = ref(false)
const bmsDctCodeCategoryInsertPopupReuturn = ref({})
const bmsDctCodeCategoryInsertPopupArgs = ref({})
const bmsDctCodeCategoryInsertPopupToggleFunc = ref(() => {
  bVisibleBmsDctCodeCategoryInsertPopup.value = !bVisibleBmsDctCodeCategoryInsertPopup.value;
  togglePopup('BmsDctCodeCategoryInsertPopup');
})
const bmsDctCodeCategoryInsertPopupReturnFunc = ref(async (retValue) => {
  bmsDctCodeCategoryInsertPopupToggleFunc.value()
  bmsDctCodeCategoryInsertPopupReuturn.value = retValue;
  await selectCodeCategoryList(1);
})

const bVisibleBmsDctCodeCategoryUpdatePopup = ref(false);
const bmsDctCodeCategoryUpdatePopupReuturn = ref({});
const bmsDctCodeCategoryUpdatePopupArgs = ref({});
const bmsDctCodeCategoryUpdatePopupToggleFunc = ref((paramValue) => {
  if (paramValue.length > 1) {
    alert("수정할 항목을 하나만 선택해주세요.");
    bVisibleBmsDctCodeCategoryUpdatePopup.value = false;
  }
  else if (paramValue.length < 1) {
    alert("수정할 항목을 선택해주세요.");
    bVisibleBmsDctCodeCategoryUpdatePopup.value = false;
  }
  else {
    bVisibleBmsDctCodeCategoryUpdatePopup.value = !bVisibleBmsDctCodeCategoryUpdatePopup.value;
    bmsDctCodeCategoryUpdatePopupArgs.value = paramValue;
  }
});

const bmsDctCodeCategoryUpdatePopupReturnFunc = ref(async () => {
  bVisibleBmsDctCodeCategoryUpdatePopup.value = false;
  // bmsDctCodeCategoryUpdatePopupReuturn.value = retValue;
  currentPageSelectCodeCategoryList.value = 1;
  selectCodeCategoryListCondi.value.pageNum = 1;
  await selectCodeCategoryList(1);
});

const visibleBmsComDeletePopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectCodeCategoryList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }

  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});

/* ======================= multi selection ======================= */
const selectCodeCategoryListIdentifier = computed(() => (item) => {
  item.identifier = `${item.tcodeid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectCodeCategoryListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectCodeCategoryListList.value.filter((item) =>
    checkedListSelectCodeCategoryList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectCodeCategoryListRouterPush = (pageNum) => {
  selectCodeCategoryListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectCodeCategoryListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


let totalItem = 0;
const selectCodeCategoryList = async (pageNum) => {
  selectCodeCategoryListLoader.value = true;
  selectCodeCategoryListCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectCodeCategoryListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectCodeCategoryListLoader.value = false;
      return;
    }
  }
  try {
    const response = await API.comAPI.selectCodeCategoryList({ ...selectCodeCategoryListCondi.value }, urlPaths.value);
    selectCodeCategoryListList.value = response.data.list;
    totalPagesSelectCodeCategoryList.value = response.data.pages;
    selectCodeCategoryListLoader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectCodeCategoryListCondiCheck.value, selectCodeCategoryListCondi.value)
  } catch (error) {
    console.log(error);
  }
}


const deleteCodeCategory = async (retValue) => {
  toggleDeletePopup.value();
  if (retValue == false) {
    return;
  }

  await API.comAPI
    .deleteCodeCategory(selectCodeCategoryListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
    .catch(error => {
      console.log(error);
    });

  comboInputData.value = "";
  setCondiClear(selectCodeCategoryListCondiCheck.value, selectCodeCategoryListCondi.value);
  await selectCodeCategoryList(1);
  checkedListSelectCodeCategoryList.value = [];
};


// Move Function
const moveToBmsDctcodelist = (paramTcodeid) => {
  router.push({
  name: "BmsDctcodelist",
    query: {
      ...{tcodeid: paramTcodeid.value}
    }
  });
};
</script>