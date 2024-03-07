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
          <h5 class="h5">반송함</h5>
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
                  @keyup.enter="selectReturnListRouterPush(1)"  
                />
              </td>
              <th>반송기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectReturnListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                  ~
                <v-text-field
                  type="date"
                  v-model="selectReturnListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectReturnListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{totalPagesSelectReturnListCnt}}개</span>
          <v-select
            v-model="pageSizeSelectReturnList"
            :items="pageSizesSelectReturnList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectReturnList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => bmsDctDetailPopupToggleFunc(item)"
          v-model="checkedListSelectReturnList"
          :headers="staticColumnsSelectReturnList"
          :items="selectReturnListList"
          :items-per-page="pageSizeSelectReturnList"
          :loading="selectReturnListLoader"
          :item-value="selectReturnListIdentifier"
          class="table-type-02"
          hover
          >
          <template v-slot:item.doctitle="{ item }">
            <div :class="{ 'text-left-02': item }">{{ item.raw.doctitle }}</div>
          </template>
          <template v-slot:item.actopinion="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.actopinion }}</div>
          </template>
          <template v-slot:item.docstatus="{ item }">
            {{ transformStatus(item.raw.docstatus) }}
          </template>
          <template v-slot:item.state="{ item }">
            {{ transformState(item.raw.state) }}
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
          <!-- <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template> -->
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectReturnList"
          :length="totalPagesSelectReturnList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectReturnList"
        ></v-pagination>
      </section>
    </div>
  </div>

  <v-dialog v-model="bVisibleBmsDctReturnPopup">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>반송 상세</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleBmsDctReturnPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctReturnPopup
        v-if="bVisibleBmsDctReturnPopup"
        :args="bmsDctReturnPopupArgs"
        :toggleFunc="bmsDctReturnPopupToggleFunc"
        :returnFunc="bmsDctReturnPopupReturnFunc"
      ></BmsDctReturnPopup>
    </v-card>
  </v-dialog>

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

  <v-dialog v-model="vUpdatePopupDialog">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀배포카드</v-toolbar-title>
        <v-btn class="close" icon @click="vUpdatePopupDialog = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctDistributeCardPopup
        v-if="vUpdatePopupDialog"
        :args="bmsDctDetailPopupArgs"
        :toggleFunc="bmsDctDetailPopupToggleFunc"
        :returnFunc="
          BmsDctDistributeCardPopupReturnFunc
        "></BmsDctDistributeCardPopup>
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
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import BmsDctDistributeCardPopup from "./BmsDctDistributeCardPopup.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import BmsDctReturnPopup from "./BmsDctReturnPopup.vue";
import { API } from '@/api';
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js";
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js" //레이블링을 위한 임포트.for labeling..
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctReturnList')
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

const bVisibleBmsDctReturnPopup = ref(false)
const bmsDctReturnPopupReuturn = ref({})
const bmsDctReturnPopupArgs = ref({})
const bmsDctReturnPopupToggleFunc = ref((row) => { 
  if(!row) {
    bVisibleBmsDctReturnPopup.value = false;
    return;
  }
  bVisibleBmsDctReturnPopup.value = !bVisibleBmsDctReturnPopup.value; 
  bmsDctReturnPopupArgs.value = row.item.raw;
});

const bmsDctReturnPopupReturnFunc = ref((retValue) => {
  // selectReturnList(1);
  moveToBmsDctsendCurrentList();
  bmsDctReturnPopupToggleFunc.value()
  bmsDctReturnPopupReuturn.value = retValue;
})

//반송처리 상태 관리를 위한 ref
const isReturned = ref(false);
/* ================== 비밀배포카드 팝업 ================= */
const bVisibleBmsDctDistributeCardPopup = ref(false);
const BmsDctDistributeCardPopupReuturn = ref({});
const bmsDctDetailPopupArgs = ref({});
const bmsDctDetailPopupToggleFunc = ref((row) => {
  if(!row) {
    vUpdatePopupDialog.value = false;
    return;
  }
  if(row.item.raw.docstatus === 'EDS06'){
    if(confirm("반송처리를 하시겠습니까?")){
      row.item.raw.isReturned = true;
      isReturned.value = true;
    } else {
      // 선택하지 않았거나 '아니오'를 선택한 경우, 상태 변경 없이 함수 종료
      return;
    }
  }
  
  const rowData = row.item.raw;
  rowData.fromPage = 'BmsDctreturnlist';
  vUpdatePopupDialog.value = !vUpdatePopupDialog.value;

  const selectedDocId = { docid: row.item.raw.docid};
  bmsDctDetailPopupArgs.value = row.item.raw;
});

const BmsDctDistributeCardPopupReturnFunc = ref((retValue) => {
  // selectReturnList(1);
  moveToBmsDctsendCurrentList();
  bmsDctDetailPopupToggleFunc.value();
  BmsDctDistributeCardPopupReuturn.value = retValue;
});
/* ================== 비밀배포카드 팝업 ================= */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("doctitle")
const comboInputData = ref("")
const comboData = [
  {key:"제목", value:"doctitle"},
  // {key:"부서명", value:"recvdeptname"},
]
// for selectReturnList
const loader = ref(true)
const checkedListSelectReturnList = ref([])
const selectReturnListList = ref([])
const selectReturnListDefCondi = {
  doctitle: "",
  recvdeptname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "enfdocid"
}
const selectReturnListCondi = ref({ ...selectReturnListDefCondi })
const selectReturnListCondiCheck = ref({ value: { ...selectReturnListDefCondi }, flag: { ...selectReturnListDefCondi } })
/* ================ pagination ================ */
const selectReturnListLoader = ref(true)
const totalPagesSelectReturnList = ref(0)
const currentPageSelectReturnList = ref(1)
const pageSizeSelectReturnList = ref(10)
const pageSizesSelectReturnList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectReturnList = () => {
  selectReturnListCondi.value.pageSize = pageSizeSelectReturnList.value;
  currentPageSelectReturnList.value = 1;
  selectReturnList(currentPageSelectReturnList.value);
}
const handlePageChangeSelectReturnList = () => {
  selectReturnList(currentPageSelectReturnList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectReturnListCondiCheck.value, selectReturnListCondi.value);
  setQueries(route, selectReturnListCondi.value, selectReturnListDefCondi);
  await selectReturnList(selectReturnListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

// 상태 변경 감지 및 리스트 업데이트 로직
watch(isReturned, (newValue) => {
  if(newValue) {
    selectReturnList(selectReturnListCondi.value.pageNum);
  }
});

watch(route, async (route) => {
  setQueries(route, selectReturnListCondi.value, selectReturnListDefCondi);
  await selectReturnList(selectReturnListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectReturnListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectReturnListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectReturnListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectReturnListCondiCheck.value, selectReturnListCondi.value, selectReturnListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectReturnListCondi.value["doctitle"] = "";
    selectReturnListCondi.value["recvdeptname"] = "";
  setCondiChanged(selectReturnListCondiCheck.value, selectReturnListCondi.value, selectReturnListCondi.value);
})

const staticColumnsSelectReturnList = [
  { key: "updt", title: "반송일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acterdeptname", title: "반송부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "actopinion", title: "반송의견", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "deptname", title: "수신부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "displayDocstatus", title: "접수증", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docstatus", title: "수신구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

/* ======================= multi selection ======================= */
const selectReturnListIdentifier = computed(() => (item) => {
  item.identifier = `${item.enfdocid}-${item.docid}-${item.docid}-${item.docgubun}-${item.tempflag}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectReturnListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectReturnListList.value.filter((item) => 
    checkedListSelectReturnList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectReturnListRouterPush = (pageNum) => {
  selectReturnListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectReturnListCondi.value
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}


const totalPagesSelectReturnListCnt = ref(0);

const selectReturnList = async (pageNum) => {

  selectReturnListLoader.value = true;
  selectReturnListCondi.value.pageNum = parseInt(pageNum);
  loader.value = true;
  selectReturnListCondi.value.userid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectReturnListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectReturnListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectReturnList({ ...selectReturnListCondi.value }, urlPaths.value);
    selectReturnListList.value = response.data.list;
    totalPagesSelectReturnList.value = response.data.pages;
    totalPagesSelectReturnListCnt.value = response.data.total;

    selectReturnListList.value.forEach(item=>{
      if(item.docstatus == 'EDS05'){ //접수
        item.displayDocstatus = "○"
      }else{
        item.displayDocstatus = ""
      }
    });

    const allOption = pageSizesSelectReturnList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectReturnListLoader.value = false;
    loader.value = false;

    setCondiClear(selectReturnListCondiCheck.value, selectReturnListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
  loader.value = false;
}

const vUpdatePopupDialog = ref(false);


// // Move Function
const moveToBmsDctsendCurrentList = () => {
  router.push({
    name: "BmsDctsendcurrentlist"
    });
};
</script>