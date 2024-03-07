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
      <h4 class="h4">접수</h4>
    </div>
    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">배부함</h5>
        </div>
      </section>

      <section class="section">
        <div class="box-condition-copydoc">
          <table>
            <tr>
              <th>검색어</th>
              <td>
                <v-select
                  label=""
                  v-model="comboSelected"
                  :items="comboData"
                  item-title="key"
                  item-value="value"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field
                  v-model="comboInputData"
                  required
                  clearable
                  variant="outlined"
                  hide-details="auto" 
                  @keyup.enter="selectDctDistributeListRouterPush(1)"  
                />
              </td>
              <th>접수기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectDctDistributeListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  type="date"
                  v-model="selectDctDistributeListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectDctDistributeListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{totalPagesSelectDctDistributeListCnt}}개</span>
          <v-select
            v-model="pageSizeSelectDctDistributeList"
            :items="pageSizesSelectDctDistributeList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectDctDistributeList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

          <v-data-table
          @click:row="(event, item) => moveToBmsDctReceivedOfflinesharecard(event, item.item.raw)"
          v-model="checkedListSelectDctDistributeList"
          :headers="staticColumnsSelectDctDistributeList"
          :items="selectDctDistributeListList"
          :items-per-page="pageSizeSelectDctDistributeList"
          :loading="selectDctDistributeListLoader"
          :item-value="selectDctDistributeListIdentifier"
          class="table-type-02-copydoc"
          hover
          >
          <template v-slot:item.indt="{ item }">
            {{ transformDate(item.raw.indt).slice(0,10) }}
          </template>
          <template v-slot:item.seclevel="{ item }">
            {{ transformSeclevel(item.raw.seclevel) }}
          </template>
          <template v-slot:item.doctitle="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.doctitle }}</div>
          </template>
          <template v-slot:item.sendopinion="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.sendopinion }}</div>
          </template>
          <template v-slot:item.gubun="{ item }">
            {{ item.raw.gubun === "RDR08" ? '원내' : '대내' }}
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageSelectDctDistributeList"
          :length="totalPagesSelectDctDistributeList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectDctDistributeList"
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
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctDistributeList')

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
const comboSelected = ref("doctitle")
const comboInputData = ref("")
const comboData = [
  {key:"제목", value:"doctitle"},
]
// for selectDctDistributeList
const checkedListSelectDctDistributeList = ref([])
const selectDctDistributeListList = ref([])
const selectDctDistributeListDefCondi = {
  docid: "",
  docstatus: "",
  title: "",
  recvdt: "",
  docno: "",
  seclevel: "",
  doctitle: "",
  createdeptname: "",
  enfgubun: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid"
}
const selectDctDistributeListCondi = ref({ ...selectDctDistributeListDefCondi })
const selectDctDistributeListCondiCheck = ref({ value: { ...selectDctDistributeListDefCondi }, flag: { ...selectDctDistributeListDefCondi } })
/* ================ pagination ================ */
const selectDctDistributeListLoader = ref(true)
const totalPagesSelectDctDistributeList = ref(0)
const currentPageSelectDctDistributeList = ref(1)
const pageSizeSelectDctDistributeList = ref(10)
const pageSizesSelectDctDistributeList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectDctDistributeList = () => {
  selectDctDistributeListCondi.value.pageSize = pageSizeSelectDctDistributeList.value;
  currentPageSelectDctDistributeList.value = 1;
  selectDctDistributeList(currentPageSelectDctDistributeList.value);
}
const handlePageChangeSelectDctDistributeList = () => {
  selectDctDistributeList(currentPageSelectDctDistributeList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectDctDistributeListCondiCheck.value, selectDctDistributeListCondi.value);
  setQueries(route, selectDctDistributeListCondi.value, selectDctDistributeListDefCondi);
  await selectDctDistributeList(selectDctDistributeListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectDctDistributeListCondi.value, selectDctDistributeListDefCondi);
  await selectDctDistributeList(selectDctDistributeListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectDctDistributeListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectDctDistributeListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectDctDistributeListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectDctDistributeListCondiCheck.value, selectDctDistributeListCondi.value, selectDctDistributeListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectDctDistributeListCondi.value["title"] = "";
    selectDctDistributeListCondi.value["recvdt"] = "";
    selectDctDistributeListCondi.value["docno"] = "";
    selectDctDistributeListCondi.value["seclevel"] = "";
    selectDctDistributeListCondi.value["doctitle"] = "";
    selectDctDistributeListCondi.value["createdeptname"] = "";
    selectDctDistributeListCondi.value["enfgubun"] = "";
  setCondiChanged(selectDctDistributeListCondiCheck.value, selectDctDistributeListCondi.value, selectDctDistributeListCondi.value);
})

watchEffect(() => {
})


const staticColumnsSelectDctDistributeList = [
  { key: "indt", title: "접수일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acceptdocnum", title: "접수번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acceptname", title: "담당자", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acceptdeptname", title: "담당부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "gubun", title: "구분", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "kind", title: "종류", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

/* ======================= multi selection ======================= */
const selectDctDistributeListIdentifier = computed(() => (item) => {
  item.identifier = `${item.enfdocid}-${item.docid}-${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectDctDistributeListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectDctDistributeListList.value.filter((item) => 
    checkedListSelectDctDistributeList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectDctDistributeListRouterPush = (pageNum) => {
  selectDctDistributeListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectDctDistributeListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}

const totalPagesSelectDctDistributeListCnt = ref(0);

const selectDctDistributeList = async (pageNum) => {

  selectDctDistributeListLoader.value = true;
  selectDctDistributeListCondi.value.pageNum = parseInt(pageNum);
  selectDctDistributeListCondi.value.userid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectDctDistributeListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectDctDistributeListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectDctDistributeList({ ...selectDctDistributeListCondi.value }, urlPaths.value);
    response.data.list.map((u) => {
      u.acceptdocnum = u.acceptdeptname + '-S'+u.acceptdocseq;
      u.kind = u.paperflag == 'Y' ? '비전자' : '전자';
    });
    selectDctDistributeListList.value = response.data.list;
    totalPagesSelectDctDistributeList.value = response.data.pages;
    totalPagesSelectDctDistributeListCnt.value = response.data.total;
    const allOption = pageSizesSelectDctDistributeList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectDctDistributeListLoader.value = false;

    setCondiClear(selectDctDistributeListCondiCheck.value, selectDctDistributeListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// Move Function
const moveToBmsDctReceivedOfflinesharecard = (event, row) => {
  const param = {docid: row.docid, frompage : 'BmsDctdistributelist', enfdocid: row.enfdocid}
  router.push({
    name: "BmsDctReceivedOfflinesharecard",
      query: {
        ...param
      }
    });
}

</script>