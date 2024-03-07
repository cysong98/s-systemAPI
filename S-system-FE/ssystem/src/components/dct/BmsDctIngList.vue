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
      <h4 class="h4">결재</h4>
    </div>
    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">진행비밀함</h5>
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
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-select
                  v-if="comboSelected === 'kind'"
                  v-model="dockinde"
                  :items="dockindData"
                  @update:modelValue="kindSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-select
                  v-else-if="comboSelected === 'asktype'"
                  v-model="docasktype"
                  :items="docasktypeData"
                  @update:modelValue="asktypeSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-select
                  v-else-if="comboSelected === 'seclevel'"
                  v-model="docseclevel"
                  :items="docseclevelData"
                  @update:modelValue="seclevelSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field v-else
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectIngList(selectIngListCondi.pageNum)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>보고기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectIngListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
                ~
                <v-text-field
                  type="date"
                  v-model="selectIngListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectIngList(selectIngListCondi.pageNum)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ selectIngListCnt }}개</span>
          <v-select
            v-model="pageSizeSelectIngList"
            :items="pageSizesSelectIngList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectIngList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectIngList"
          :headers="staticColumnsSelectIngList"
          :items="selectIngListList"
          :items-per-page="pageSizeSelectIngList"
          :loading="selectIngListLoader"
          :item-value="selectIngListIdentifier"
          class="table-type-02"
          hover
          >
          <template v-slot:item="{ item }">
            <tr @click="moveToPageFunc(item.raw)" style="cursor: pointer;">
              <td> {{ item.raw.copyno != undefined ? !item.raw.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서' }} </td>
              <td> {{ transformAsktype(item.raw.asktype) }} </td>
              <td> {{ item.raw.docno }} </td>
              <td> {{ item.raw.mgmtno }} </td>
              <td> {{ transformDate(item.raw.indt) }} </td>
              <td :class="{ 'text-left': item }" > {{ item.raw.docttl }} </td>
              <td> {{ transformSeclevel(item.raw.seclevel) }} </td>
              <td> {{ item.raw.authorname }} </td>
              <td> {{ item.raw.authordutyname }} </td>
              <td> {{ item.raw.authordeptname }} </td>
              <td> {{ transformDate(item.raw.reportdt) }} </td>
              <td> 
                <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
                  <v-icon></v-icon>
                </v-btn>
              </td>
            </tr>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectIngList"
          :length="totalPagesSelectIngList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectIngList"
        ></v-pagination>
      </section>
    </div>
  </div>

  <!-- 비밀이력카드 팝업 -->
  <v-dialog v-model="bVisibleDctHistoryCardPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀이력카드</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctHistoryCardPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctHistoryCardPopup
         v-if="bVisibleDctHistoryCardPopup"
        :args="dctHistoryCardPopupArgs"
        :toggleFunc="dctHistoryCardPopupToggleFunc"
        :returnFunc="dctHistoryCardPopupReturnFunc"
      ></DctHistoryCardPopup>
    </v-card>
  </v-dialog>

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
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctIngList')
/* ======================== 비밀이력카드 팝업 ======================== */
const bVisibleDctHistoryCardPopup = ref(false);
const dctHistoryCardPopupArgs = ref({});
const dctHistoryCardPopupToggleFunc = ref((docid) => {
  dctHistoryCardPopupArgs.value.docid = docid;
  bVisibleDctHistoryCardPopup.value = !bVisibleDctHistoryCardPopup.value;
});
const dctHistoryCardPopupReturnFunc = ref((retValue) => {
  dctHistoryCardPopupToggleFunc.value();
});
const histBtnClick = (docid) => {
  dctHistoryCardPopupToggleFunc.value(docid);
}
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("docttl")
const comboInputData = ref("")
const comboData = [
  {key:"유형", value:"kind"},
  {key:"구분", value:"asktype"},
  {key:"문서번호", value:"docno"},
  {key:"관리번호", value:"mgmtno"},
  {key:"제목", value:"docttl"},
  {key:"등급", value:"seclevel"},
  {key:"보고자", value:"authorname"},
  {key:"부서", value:"authordeptname"},
]
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectIngListCondi.value.kind = dockinde.value;
  selectIngList(1);
  currentPageSelectIngList.value = 1;
}
const docasktype = ref("")
const docasktypeData = ref([
  { view: '전체', key: ''},
  { view: '협조', key: 'DEA31'},
  { view: '검토', key: 'DEA21'},
  { view: '전결', key: 'DEA51'},
  { view: '결재', key: 'DEA41'},
  { view: '전대결', key: 'DEA71'},
  { view: '대결', key: 'DEA61'},
])
const asktypeSearch = () => {
  selectIngListCondi.value.asktype = docasktype.value;
  selectIngList(1);
  currentPageSelectIngList.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectIngListCondi.value.seclevel = docseclevel.value;
  selectIngList(1);
  currentPageSelectIngList.value = 1;
}
// for selectIngList
const checkedListSelectIngList = ref([])
const selectIngListList = ref([])
const selectIngListDefCondi = {
  docttl: "",
  authorname: "",
  authordeptname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
  userid: ""
}
const selectIngListCondi = ref({ ...selectIngListDefCondi })
const selectIngListCondiCheck = ref({ value: { ...selectIngListDefCondi }, flag: { ...selectIngListDefCondi } })
/* ================ pagination ================ */
const selectIngListLoader = ref(true)
const totalPagesSelectIngList = ref(0)
const currentPageSelectIngList = ref(1)
const pageSizeSelectIngList = ref(10)
const pageSizesSelectIngList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectIngList = () => {
  selectIngListCondi.value.pageSize = pageSizeSelectIngList.value;
  currentPageSelectIngList.value = 1;
  selectIngList(currentPageSelectIngList.value);
}
const handlePageChangeSelectIngList = () => {
  selectIngList(currentPageSelectIngList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectIngListCondiCheck.value, selectIngListCondi.value);
  setQueries(route, selectIngListCondi.value, selectIngListDefCondi);
  await selectIngList(selectIngListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectIngListCondi.value, selectIngListDefCondi);
  await selectIngList(selectIngListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectIngListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectIngListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectIngListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectIngListCondiCheck.value, selectIngListCondi.value, selectIngListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectIngListCondi.value["docttl"] = "";
    selectIngListCondi.value["authorname"] = "";
    selectIngListCondi.value["authordeptname"] = "";
  setCondiChanged(selectIngListCondiCheck.value, selectIngListCondi.value, selectIngListCondi.value);
})

watchEffect(() => {
})


const staticColumnsSelectIngList = [
  { key: "kind", title: "유형", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "asktype", title: "구분", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "보고일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "보고자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },  
  { key: "authordeptname", title: "부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reportdt", title: "처리일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이력", key: "histBtn", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

const responseData = ref([]);

const moveToPageParamDefCondi = { docid: "" };
const moveToPageParamCondi = ref({ ...moveToPageParamDefCondi });

const clickedItem = ref();
const clickedItemId = ref();
const paramDocId = ref();
const moveToPageFunc = (item) => {
  clickedItem.value = responseData.value.indexOf(item);
  clickedItemId.value = item.docid;
  paramDocId.value = item.docid;
  if(item.paperflag == 'Y'){
    let path = '';
    if(item.paperrecgubun == '접수'){
      path = 'BmsDctReceivedOfflinecompletecard';
    }else{
      path = 'BmsDctProductOfflinecompletecard';
    }
    const param = {frompage: 'BmsDctinglist', docid: clickedItemId.value}
    router.push({
      name: path,
      query: {
        ...param,
      },
    });
  }else{
    moveToBmsDctIngCard(clickedItemId.value);
  }
};

// Move Function
const moveToBmsDctIngCard = (paramDocId) => {
  moveToPageParamCondi.value.docid = paramDocId;
  router.push({
    name: "BmsDctingcard",
    query: {
      ...moveToPageParamCondi.value,
    },
  });
};

/* ======================= multi selection ======================= */
const selectIngListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.docid}-${item.pathorder}-${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectIngListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectIngListList.value.filter((item) => 
    checkedListSelectIngList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectIngListRouterPush = (pageNum) => {
  selectIngListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectIngListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}

const selectIngListCnt = ref(0);

const selectIngList = async (pageNum) => {

  selectIngListLoader.value = true;
  selectIngListCondi.value.pageNum = parseInt(pageNum);
  selectIngListCondi.value.userid = getUserLoginData.value.userid;

  if (!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '') {
    if (validCheckSearchInput(comboInputData.value)) {
      selectIngListCondi.value[comboSelected.value] = comboInputData.value;
    } else {
      comboInputData.value = "";
      selectIngListLoader.value = false;
      return;
    }
  }
  try {
    const response = await API.dctAPI.selectIngList({ ...selectIngListCondi.value }, urlPaths.value);
    selectIngListList.value = response.data.list;
    selectIngListCnt.value = response.data.total;
    totalPagesSelectIngList.value = response.data.pages;
    const allOption = pageSizesSelectIngList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectIngListLoader.value = false;
    setCondiClear(selectIngListCondiCheck.value, selectIngListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

</script>