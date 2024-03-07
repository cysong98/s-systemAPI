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
          <h5 class="h5">접수대기함</h5>
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
                  hide-details="auto" />
                <v-text-field
                  v-model="comboInputData"
                  required
                  clearable
                  variant="outlined"
                  hide-details="auto" 
                  @keyup.enter="selectReceiptListRouterPush(1)"/>
              </td>
              <th>접수기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectReceiptListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  type="date"
                  v-model="selectReceiptListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectReceiptListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>
        
        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{totalPagesSelectReceiptListCnt}}개</span>
          <v-select
            v-model="pageSizeSelectReceiptList"
            :items="pageSizesSelectReceiptList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectReceiptList"
            variant="outlined"
            hide-details="auto"></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsReceiptdetailcard(item)"
          v-model="checkedListSelectReceiptList"
          :headers="staticColumnsSelectReceiptList"
          :items="selectReceiptListList"
          :items-per-page="pageSizeSelectReceiptList"
          :loading="selectReceiptListLoader"
          :item-value="selectReceiptListIdentifier"
          class="table-type-02-copydoc"
          hover>
          <template v-slot:item.doctitle="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.doctitle }}</div>
          </template>
          <template v-slot:item.sendopinion="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.sendopinion }}</div>
          </template>
          <template v-slot:item.regirecvgubun="{ item }">
            {{ item.raw.regirecvgubun === "1" ? '생산' : '접수' }}
          </template>
          <template v-slot:item.docstatus="{ item }">
            {{ transformStatus(item.raw.docstatus) }}
          </template>
          <template v-slot:item.state="{ item }">
            {{ transformState(item.raw.state) }}
          </template>
          <template v-slot:item.gubun="{ item }">
            {{ item.raw.gubun === "RDR08" ? '원내' : '외부' }}
          </template>
          <template v-slot:item.indt="{ item }">
            {{ transformDate(item.raw.indt).slice(0,10) }}
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
          <template v-slot:item.copyno="{ item }">
            {{ item.raw.copyno }}
          </template>
          <template v-slot:item.docno2="{ item }">
            {{ trimAfterDash(item.raw.docno) }}
          </template>
          <template v-slot:item.chargerid="{ item }">
            {{ item.raw.chargerid? '기안' : '접수' }}
          </template>
          <template v-slot:item.regirecvtype="{ item }">
            {{ item.raw.regirecvtype=== '1'? '전자' : '비전자' }}
          </template>
          <!-- <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template> -->
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectReceiptList"
          :length="totalPagesSelectReceiptList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectReceiptList"></v-pagination>
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

import dayjs from "dayjs";
import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { VDatePicker } from "vuetify/labs/VDatePicker";
import { API } from "@/api";
import { getValueByKey, trimAfterDash, validCheckSearchInput } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref("BmsDctReceiptList");
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
const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// for column combo
const comboSelected = ref("doctitle");
const comboInputData = ref("");
const comboData = [
  {key:"제목", value:"doctitle"},
  {key:"배포처", value:"docno"},
];
// for selectReceiptList
const checkedListSelectReceiptList = ref([]);
const selectReceiptListList = ref([]);
const selectReceiptListDefCondi = {
  docid: "",
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
  sortItem: "docid",
};
const selectReceiptListCondi = ref({ ...selectReceiptListDefCondi });
const selectReceiptListCondiCheck = ref({
  value: { ...selectReceiptListDefCondi },
  flag: { ...selectReceiptListDefCondi },
});
/* ================ pagination ================ */
const selectReceiptListLoader = ref(true);
const totalPagesSelectReceiptList = ref(0);
const currentPageSelectReceiptList = ref(1);
const pageSizeSelectReceiptList = ref(10);
const pageSizesSelectReceiptList = ref([
  { view: "10개씩 보기", key: 10 },
  { view: "25개씩 보기", key: 25 },
  { view: "50개씩 보기", key: 50 },
]);

const handlePageSizeChangeSelectReceiptList = () => {
  selectReceiptListCondi.value.pageSize = pageSizeSelectReceiptList.value;
  currentPageSelectReceiptList.value = 1;
  selectReceiptList(currentPageSelectReceiptList.value);
};
const handlePageChangeSelectReceiptList = () => {
  selectReceiptList(currentPageSelectReceiptList.value);
};
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(
    selectReceiptListCondiCheck.value,
    selectReceiptListCondi.value
  );
  setQueries(route, selectReceiptListCondi.value, selectReceiptListDefCondi);
  await selectReceiptList(selectReceiptListCondi.value.pageNum);
});
onBeforeUnmount(() => {});

watch(route, async (route) => {
  setQueries(route, selectReceiptListCondi.value, selectReceiptListDefCondi);
  await selectReceiptList(selectReceiptListCondi.value.pageNum);
});

watch(
  () => _.cloneDeep(selectReceiptListCondi.value),
  (newVal, oldVal) => {
    setCondiChanged(selectReceiptListCondiCheck.value, newVal, oldVal);
  }
);

watch(comboInputData, async (comboInputData) => {
  if (!(comboSelected.value == "" || comboSelected.value == undefined))
    selectReceiptListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(
    selectReceiptListCondiCheck.value,
    selectReceiptListCondi.value,
    selectReceiptListCondi.value
  );
});

watch(comboSelected, async (comboSelected) => {
  selectReceiptListCondi.value["title"] = "";
  selectReceiptListCondi.value["recvdt"] = "";
  selectReceiptListCondi.value["docno"] = "";
  selectReceiptListCondi.value["seclevel"] = "";
  selectReceiptListCondi.value["doctitle"] = "";
  selectReceiptListCondi.value["enfgubun"] = "";
  setCondiChanged(
    selectReceiptListCondiCheck.value,
    selectReceiptListCondi.value,
    selectReceiptListCondi.value
  );
});

watchEffect(() => {});

const staticColumnsSelectReceiptList = [
  { key: "indt", title: "접수일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "chargerid", title: "구분", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "sendopinion", title: "발송의견", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno2", title: "배포처", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "gubun", title: "수신구분", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

/* ======================= multi selection ======================= */
const selectReceiptListIdentifier = computed(() => (item) => {
  item.identifier = `${item.enfdocid}-${item.docid}`;
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectReceiptListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectReceiptListList.value.filter((item) =>
    checkedListSelectReceiptList.value.includes(item.identifier)
  );

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
const docId = computed(() => {
  // return selectSendCurrentListFinalData.value.map((item) => item.docid);
  return selectReceiptListFinalData.value.map((item) => {
    return { docid: item.docid };
  });
});
/* =========================================================== */


const selectReceiptListRouterPush = (pageNum) => {
  selectReceiptListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectReceiptListCondi.value,
    })
    .catch((error) => {
      console.log(error);
    });
};


const totalPagesSelectReceiptListCnt = ref(0);

const selectReceiptList = async (pageNum) => {
  selectReceiptListLoader.value = true;
  selectReceiptListCondi.value.pageNum = parseInt(pageNum);

  selectReceiptListCondi.value.userid = getUserLoginData.value.userid;
  selectReceiptListCondi.value.deptid = getUserLoginData.value.deptid;
  selectReceiptListCondi.value.tempflag = 'N';
  if(getUserLoginData.value.vgroupid.includes('5220') ){
    selectReceiptListCondi.value.vgroupid = '5220';
  }

  // selectReceiptListCondi.value.docstatus = "EDS09";
  if (!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectReceiptListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectReceiptListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectReceiptList(
      { ...selectReceiptListCondi.value},
      urlPaths.value
    );
    selectReceiptListList.value = response.data.list;
    totalPagesSelectReceiptList.value = response.data.pages;
    totalPagesSelectReceiptListCnt.value = response.data.total;
    const allOption = pageSizesSelectReceiptList.value.find(
      (option) => option.view === "ALL"
    );
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectReceiptListLoader.value = false;

    setCondiClear(
      selectReceiptListCondiCheck.value,
      selectReceiptListCondi.value
    );
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// Move Function
const moveToBmsReceiptdetailcard = (item) => {
  item.item.raw.page = 'BmsDctreceiptlist';
  item.item.raw.frompage = 'BmsDctreceiptlist';
  const routeName = item.item.raw.regirecvtype == '1' ? "BmsReceiptdetailcard" : "BmsDctReceivedOfflinesharecard"
  router.push({
    name: routeName,
    query: {
      ...item.item.raw,
    },
  });
};
</script>