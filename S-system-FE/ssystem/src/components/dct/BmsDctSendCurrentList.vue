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
          <h5 class="h5">발송현황함</h5>
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
                  @keyup.enter="selectSendCurrentListRouterPush(1)"  
                />
              </td>
              <th>발송기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectSendCurrentListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
                  ~
                  <v-text-field
                  type="date"
                  v-model="selectSendCurrentListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectSendCurrentListRouterPush(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{totalPagesSelectSendCurrentListCnt}}개</span>
          <v-select
            v-model="pageSizeSelectSendCurrentList"
            :items="pageSizesSelectSendCurrentList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectSendCurrentList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          @click:row="(event, item) => bmsDctDetailPopupToggleFunc(item)"
          v-model="checkedListSelectSendCurrentList"
          :headers="staticColumnsSelectSendCurrentList"
          :items="selectSendCurrentListList"
          :items-per-page="pageSizeSelectSendCurrentList"
          :loading="selectSendCurrentListLoader"
          :item-value="selectSendCurrentListIdentifier"
          class="table-type-02"
          hover>
          <template v-slot:item.docttl="{ item }">
            <div class="text-left-02" >
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:item.state="{ item }">
            {{ transformState(displayState(item)) }}
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
          <!-- <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template> -->
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectSendCurrentList"
          :length="totalPagesSelectSendCurrentList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectSendCurrentList"></v-pagination>
      </section>
    </div>

  </div>
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

  <v-dialog v-model="vReceiptPopupDialog" width="700px" height="900px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title> 송증/영수증</v-toolbar-title>
        <v-btn class="close" icon @click="vReceiptPopupDialog = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsDctReceipt
        v-if="vReceiptPopupDialog"
        :args="bmsDctDetailPopupArgs"
        :toggleFunc="bmsDctDetailPopupToggleFunc"
        :returnFunc="
          BmsDctDistributeCardPopupReturnFunc
        "></BmsDctReceipt>
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
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js" //레이블링을 위한 임포트.for labeling..
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import BmsDctDistributeCardPopup from "./BmsDctDistributeCardPopup.vue";
import BmsDctReceipt from "./BmsDctReceipt.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref("BmsDctSendCurrentList");
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

//반송처리 상태 관리를 위한 ref
const isReturned = ref(false);

const bVisibleBmsDctDistributeCardPopup = ref(false);
const BmsDctDistributeCardPopupReuturn = ref({});
const bmsDctDetailPopupArgs = ref({});
const bmsDctDetailPopupToggleFunc = ref((row) => {
  if(!row) {
    vUpdatePopupDialog.value = false;
    vReceiptPopupDialog.value = false;
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
  rowData.fromPage = 'BmsDctSendCurrentList';
    if(['EDS05','EDS11','EDS10'].includes(row.item.raw.docstatus)){ //접수,종료,재지정시 송증/영수증 출력(재지정과 무관하게 최초 접수하면 접수증 뜨도록함.)
      vReceiptPopupDialog.value = !vReceiptPopupDialog.value;//송증/영수증 출력
    }else if(['DRDS0','DRDS5'].includes(row.item.raw.state) && row.item.raw.copyno && row.item.raw.copyno.includes('원본')){ //발송보류,발송안함
      moveToBmsDctSendWaitCard(rowData) //비밀조회 페이지(상세)로 이동.
    }else{
      vUpdatePopupDialog.value = !vUpdatePopupDialog.value;
    }
    const selectedDocId = { docid: row.item.raw.docid};
    bmsDctDetailPopupArgs.value = row.item.raw;
});

const BmsDctDistributeCardPopupReturnFunc = ref((retValue) => {
  selectSendCurrentList(1);
  bmsDctDetailPopupToggleFunc.value();
  BmsDctDistributeCardPopupReuturn.value = retValue;
});

const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
// for column combo
const comboSelected = ref("docttl");
const comboInputData = ref("");
const comboData = [
  {key:"제목", value:"docttl"},
  {key:"접수부서", value:"orgname"},
]
// for selectSendCurrentList
const checkedListSelectSendCurrentList = ref([]);
const selectSendCurrentListList = ref([]);
const selectSendCurrentListDefCondi = {
  docid: "",
  docttl: "",
  authorname: "",
  orgname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
};
const selectSendCurrentListCondi = ref({ ...selectSendCurrentListDefCondi });
const selectSendCurrentListCondiCheck = ref({
  value: { ...selectSendCurrentListDefCondi },
  flag: { ...selectSendCurrentListDefCondi },
});
/* ================ pagination ================ */
const selectSendCurrentListLoader = ref(true);
const totalPagesSelectSendCurrentList = ref(0);
const currentPageSelectSendCurrentList = ref(1);
const pageSizeSelectSendCurrentList = ref(10);
const pageSizesSelectSendCurrentList = ref([
  { view: "10개씩 보기", key: 10 },
  { view: "25개씩 보기", key: 25 },
  { view: "50개씩 보기", key: 50 },
]);

const handlePageSizeChangeSelectSendCurrentList = () => {
  selectSendCurrentListCondi.value.pageSize =
    pageSizeSelectSendCurrentList.value;
  currentPageSelectSendCurrentList.value = 1;
  selectSendCurrentList(currentPageSelectSendCurrentList.value);
};
const handlePageChangeSelectSendCurrentList = () => {
  selectSendCurrentList(currentPageSelectSendCurrentList.value);
};
/* ============================================== */

const responseData = ref([]);
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
});

onMounted(async () => {
  setCondiClear(
    selectSendCurrentListCondiCheck.value,
    selectSendCurrentListCondi.value
  );
  setQueries(
    route,
    selectSendCurrentListCondi.value,
    selectSendCurrentListDefCondi
  );
  await selectSendCurrentList(selectSendCurrentListCondi.value.pageNum);
});
onBeforeUnmount(() => {});

// 상태 변경 감지 및 리스트 업데이트 로직
watch(isReturned, (newValue) => {
  if(newValue) {
    selectSendCurrentList(selectSendCurrentListCondi.value.pageNum);
  }
});

watch(route, async (route) => {
  setQueries(
    route,
    selectSendCurrentListCondi.value,
    selectSendCurrentListDefCondi
  );
  await selectSendCurrentList(selectSendCurrentListCondi.value.pageNum);
});

watch(
  () => _.cloneDeep(selectSendCurrentListCondi.value),
  (newVal, oldVal) => {
    setCondiChanged(selectSendCurrentListCondiCheck.value, newVal, oldVal);
  }
);

watch(comboInputData, async (comboInputData) => {
  if (!(comboSelected.value == "" || comboSelected.value == undefined))
    selectSendCurrentListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(
    selectSendCurrentListCondiCheck.value,
    selectSendCurrentListCondi.value,
    selectSendCurrentListCondi.value
  );
});

watch(comboSelected, async (comboSelected) => {
  selectSendCurrentListCondi.value["docttl"] = "";
  selectSendCurrentListCondi.value["authorname"] = "";
  selectSendCurrentListCondi.value["orgname"] = "";
  setCondiChanged(
    selectSendCurrentListCondiCheck.value,
    selectSendCurrentListCondi.value,
    selectSendCurrentListCondi.value
  );
});

watchEffect(() => {});


const staticColumnsSelectSendCurrentList = [
  { key: "indt", title: "발송일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "state", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "inid", title: "접수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "username", title: "접수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "orgname", title: "접수부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "접수부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "displayDocstatus", title: "접수증", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { key: "docstatus", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
];


/* ======================= multi selection ======================= */
const selectSendCurrentListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.enfdocid}-${item.docid}-${item.sendhisid}`;
  return item.identifier;
});
// 서버에 전송할 체크된 리스트 (row 반환)
const selectSendCurrentListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectSendCurrentListList.value.filter((item) =>
    checkedListSelectSendCurrentList.value.includes(item.identifier)
  );

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
const docId = computed(() => {
  return selectSendCurrentListFinalData.value.map((item) => {
    return { docid: item.docid };
  });
});
/* =========================================================== */
function displayState(item) {
  if (item.raw.state === 'DRDS8' && item.raw.docstatus === 'EDS06') {
    return 'DRDS6'; //반송
  }
  if (item.raw.state === 'DRDS8' && ( item.raw.docstatus === 'EDS05' || item.raw.docstatus === 'EDS10' || item.raw.docstatus === 'EDS11') ) {
    return '접수완료';
  }
  if (item.raw.state === 'DRDS8' && item.raw.docstatus === 'EDS40') {
    return '반송종료'; //반송
  }
  if (item.raw.state === 'DRDS8' && !item.raw.docstatus) { //온라인 정상발송은 DRDS8, EDS09 의 값을 갖음.
    return '오프발송완료';
  }
  return item.raw.state;
}

const selectSendCurrentListRouterPush = (pageNum) => {
  selectSendCurrentListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectSendCurrentListCondi.value,
    })
    .catch((error) => {
      console.log(error);
    });
};


const totalPagesSelectSendCurrentListCnt = ref(0);

const selectSendCurrentList = async (pageNum) => {
  selectSendCurrentListLoader.value = true;
  selectSendCurrentListCondi.value.pageNum = parseInt(pageNum);
  selectSendCurrentListCondi.value.userid = getUserLoginData.value.userid;

  if (!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectSendCurrentListCondi.value[comboSelected.value] = comboInputData.value;
    else{
      comboInputData.value = "";
      selectSendCurrentListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectSendCurrentList(
      { ...selectSendCurrentListCondi.value },
      urlPaths.value
    );
    selectSendCurrentListList.value = response.data.list;
    responseData.value = response.data;
    totalPagesSelectSendCurrentList.value = response.data.pages;
    totalPagesSelectSendCurrentListCnt.value = response.data.total

    selectSendCurrentListList.value.forEach(item=>{
      if(['EDS05','EDS11', 'EDS10'].includes(item.docstatus)){ //[접수,종료,재지정]
        item.displayDocstatus = "○" //접수증 있음표기
      }else{
        item.displayDocstatus = ""
      }
    });

    // 오프라인접수자 등록한 이름.
    selectSendCurrentListList.value.forEach(item=>{
      if(item.acceptname){
        item.username = item.acceptname
      }
    })

    const allOption = pageSizesSelectSendCurrentList.value.find(
      (option) => option.view === "ALL"
    );
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectSendCurrentListLoader.value = false;

    setCondiClear(
      selectSendCurrentListCondiCheck.value,
      selectSendCurrentListCondi.value
    );
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// Move Function
const moveToBmsDctSendWaitCard = (item) => {
  router.push({
    name: "BmsDctsendwaitcard",
    // params: {
    //   ...item,
    // },
    query: {
      ...item,
    },
  });
};

const vUpdatePopupDialog = ref(false);
const vReceiptPopupDialog = ref(false);
</script>