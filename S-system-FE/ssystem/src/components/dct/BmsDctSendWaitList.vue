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
      <h4 class="h4">발송</h4>
    </div>
    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">발송대기함</h5>
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
                  @keyup.enter="selectSendWaitListRouterPush(1)"  
                />
              </td>
              <th>보고기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectSendWaitListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                  ~
                <v-text-field
                  type="date"
                  v-model="selectSendWaitListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectSendWaitListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{totalPagesSelectSendWaitListCnt}}개</span>
          <v-select
            v-model="pageSizeSelectSendWaitList"
            :items="pageSizesSelectSendWaitList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectSendWaitList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectSendWaitList"
          :headers="staticColumnsSelectSendWaitList"
          :items="selectSendWaitListList"
          :items-per-page="pageSizeSelectSendWaitList"
          :loading="selectSendWaitListLoader"
          :item-value="selectSendWaitListIdentifier"
          class="table-type-02"
          @click:row="(event, item) => moveToPageFunc(item.item.raw)"
          hover
          >
          <template v-slot:item.docttl="{ item }">
            <div class="text-left-02" >
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:item.gubun="{ item }">
            {{ transformRdocGubun(item.raw.gubun) }}
          </template>
          <template v-slot:item.indt="{ item }">
            {{ transformDate(item.raw.indt) }}
          </template>
          <template v-slot:item.reportdt="{ item }">
            {{ transformDate(item.raw.reportdt) }}
          </template>
          <template v-slot:item.updt="{ item }">
            {{ transformDate(item.raw.updt) }}
          </template>
          <template v-slot:item.seclevel="{ item }">
            {{ transformSeclevel(item.raw.seclevel) }}
          </template>
          <template v-slot:item.recvdeptnames="{ item }">
            <div class="text-left-04" >
              {{ item.raw.recvdeptnames }}
            </div>
          </template>
          <!-- <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template> -->
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageSelectSendWaitList"
          :length="totalPagesSelectSendWaitList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectSendWaitList"
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
import { API } from "@/api";
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { transformDate,transformRdocGubun,transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref("BmsDctSendWaitList");
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
const props = defineProps({
  args: Object,
});
const args = ref({});
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// for column combo
const comboSelected = ref("docttl");
const comboInputData = ref("");
const comboData = [
  {key:"제목", value:"docttl"},
  {key:"수신부서", value:"recvdeptnames"},
]
const moveToPageParamDefCondi = { docid: "", docttl: "" };
const moveToPageParamCondi = ref({ ...moveToPageParamDefCondi });
const checkedListSelectSendWaitList = ref([])
const selectSendWaitListList = ref([]);
const selectSendWaitListDefCondi = {
  docid: "",
  docttl: "",
  authorname: "",
  recvdeptnames: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
};
const selectSendWaitListCondi = ref({ ...selectSendWaitListDefCondi })
const selectSendWaitListCondiCheck = ref({ value: { ...selectSendWaitListDefCondi }, flag: { ...selectSendWaitListDefCondi } })
/* ================ pagination ================ */
const selectSendWaitListLoader = ref(true)
const totalPagesSelectSendWaitList = ref(0)
const currentPageSelectSendWaitList = ref(1)
const pageSizeSelectSendWaitList = ref(10)
const pageSizesSelectSendWaitList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectSendWaitList = () => {
  selectSendWaitListCondi.value.pageSize = pageSizeSelectSendWaitList.value;
  currentPageSelectSendWaitList.value = 1;
  selectSendWaitList(currentPageSelectSendWaitList.value);
}
const handlePageChangeSelectSendWaitList = () => {
  selectSendWaitList(currentPageSelectSendWaitList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectSendWaitListCondiCheck.value, selectSendWaitListCondi.value);
  setQueries(route, selectSendWaitListCondi.value, selectSendWaitListDefCondi);

  selectSendWaitListCondi.value.objectid = getUserLoginData.value.userid;

  await selectSendWaitList(selectSendWaitListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectSendWaitListCondi.value, selectSendWaitListDefCondi);
  await selectSendWaitList(selectSendWaitListCondi.value.pageNum);
})

watch(() => _.cloneDeep(selectSendWaitListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectSendWaitListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectSendWaitListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectSendWaitListCondiCheck.value, selectSendWaitListCondi.value, selectSendWaitListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectSendWaitListCondi.value["docttl"] = "";
    selectSendWaitListCondi.value["authorname"] = "";
    selectSendWaitListCondi.value["recvdeptnames"] = "";
  setCondiChanged(selectSendWaitListCondiCheck.value, selectSendWaitListCondi.value, selectSendWaitListCondi.value);
})

watchEffect(() => {
  args.value = props.args;
})


const staticColumnsSelectSendWaitList = [
  { key: "reportdt", title: "보고일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "objname", title: "보고자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "gubun", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "deptname", title: "수신부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "recvdeptnames", title: "수신부서", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

/* ======================= multi selection ======================= */
const selectSendWaitListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.docid}-${item.receiverid}-${item.tempflag}-${item.mgmtid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectSendWaitListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectSendWaitListList.value.filter((item) =>
    checkedListSelectSendWaitList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectSendWaitListRouterPush = (pageNum) => {
  selectSendWaitListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectSendWaitListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}

const totalPagesSelectSendWaitListCnt = ref(0);

const selectSendWaitList = async (pageNum) => {
  selectSendWaitListLoader.value = true;
  selectSendWaitListCondi.value.pageNum = parseInt(pageNum);
  selectSendWaitListCondi.value.userid = getUserLoginData.value.userid;
  selectSendWaitListCondi.value.objid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectSendWaitListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectSendWaitListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectSendWaitList({ ...selectSendWaitListCondi.value }, urlPaths.value);
    selectSendWaitListList.value = response.data.list;
    totalPagesSelectSendWaitList.value = response.data.pages;
    totalPagesSelectSendWaitListCnt.value = response.data.total;
    const allOption = pageSizesSelectSendWaitList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectSendWaitListLoader.value = false;
    setCondiClear(selectSendWaitListCondiCheck.value, selectSendWaitListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const moveToPageFunc = (item) => {
  moveToBmsDctSendWaitCard(item);
};

// Move Function
const moveToBmsDctSendWaitCard = (item) => {
  router.push({
    name: "BmsDctsendwaitcard",
    query: {
      ...item,
    },
  });
};

</script>