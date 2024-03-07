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
          <h5 class="h5">결재완료함</h5>
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
                  v-else-if="comboSelected === 'acttype'"
                  v-model="docacttype"
                  :items="docacttypeData"
                  @update:modelValue="acttypeSearch"
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
                <v-select
                  v-else-if="comboSelected === 'docstate'"
                  v-model="docstate"
                  :items="docstateData"
                  @update:modelValue="statusSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field v-else
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectApprovalCompleteList(selectApprovalCompleteListCondi.pageNum)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>보고기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectApprovalCompleteListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
                ~
                <v-text-field
                  type="date"
                  v-model="selectApprovalCompleteListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectApprovalCompleteList(selectApprovalCompleteListCondi.pageNum)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ selectApprovalCompleteListCnt }}개</span>
          <v-select
            v-model="pageSizeSelectApprovalCompleteList"
            :items="pageSizesSelectApprovalCompleteList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectApprovalCompleteList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectApprovalCompleteList"
          :headers="staticColumnsSelectApprovalCompleteList"
          :items="selectApprovalCompleteListList"
          :items-per-page="pageSizeSelectApprovalCompleteList"
          :loading="selectApprovalCompleteListLoader"
          :item-value="selectApprovalCompleteListIdentifier"
          class="table-type-02"
          hover
          >
          <template v-slot:item="{ item }">
            <tr  @click="moveToPageFunc(item.raw)" style="cursor: pointer;">
              <td> {{ item.raw.copyno != undefined ? !item.raw.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서' }} </td>
              <td> {{ transformAsktype(item.raw.acttype) }} </td>
              <td> {{ item.raw.docno }} </td>
              <td> {{ item.raw.mgmtno }} </td>
              <td> {{ transformDate(item.raw.indt) }} </td>
              <td :class="{ 'text-left': item }"> {{ item.raw.docttl }} </td>
              <td> {{ transformSeclevel(item.raw.seclevel) }} </td>
              <td> {{ item.raw.authorname }} </td>
              <td> {{ item.raw.authordutyname }} </td>
              <td> {{ item.raw.authordeptname }} </td>
              <td> {{ transformState(item.raw.state) }} </td>
              <td> {{ transformDate(item.raw.reportdt) }} </td>
              <!-- <td> 
                <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
                  <v-icon></v-icon>
                </v-btn>
              </td> -->
            </tr>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectApprovalCompleteList"
          :length="totalPagesSelectApprovalCompleteList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectApprovalCompleteList"
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
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel, transformState } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctApprovalCompleteList')
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
  {key:"구분", value:"acttype"},
  {key:"문서번호", value:"docno"},
  {key:"관리번호", value:"mgmtno"},
  {key:"제목", value:"docttl"},
  {key:"등급", value:"seclevel"},
  {key:"보고자", value:"authorname"},
  {key:"부서", value:"authordeptname"},
  {key:"상태", value:"docstate"},
]
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectApprovalCompleteListCondi.value.kind = dockinde.value;
  selectApprovalCompleteList(1);
  currentPageSelectApprovalCompleteList.value = 1;
}
const docacttype = ref("")
const docacttypeData = ref([
  { view: '전체', key: ''},
  { view: '결재', key: 'DEA41'},
  { view: '1인결재', key: 'DEA45'},
])
const acttypeSearch = () => {
  selectApprovalCompleteListCondi.value.acttype = docacttype.value;
  selectApprovalCompleteList(1);
  currentPageSelectApprovalCompleteList.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectApprovalCompleteListCondi.value.seclevel = docseclevel.value;
  selectApprovalCompleteList(1);
  currentPageSelectApprovalCompleteList.value = 1;
}
const docstate = ref("")
const docstateData = ref([
  { view: '전체', key: ''},
  { view: '종료', key: 'DRDS3'},
  { view: '발송대기', key: 'DRDS4'},
  { view: '발송안함', key: 'DRDS5'},
  { view: '반송', key: 'DRDS6'},
  { view: '발송완료', key: 'DRDS8'},
])
const statusSearch = () => {
  selectApprovalCompleteListCondi.value.state = docstate.value;
  selectApprovalCompleteList(1);
  currentPageSelectApprovalCompleteList.value = 1;
}
// for selectApprovalCompleteList
const checkedListSelectApprovalCompleteList = ref([])
const selectApprovalCompleteListList = ref([])
const selectApprovalCompleteListDefCondi = {
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
const selectApprovalCompleteListCondi = ref({ ...selectApprovalCompleteListDefCondi })
const selectApprovalCompleteListCondiCheck = ref({ value: { ...selectApprovalCompleteListDefCondi }, flag: { ...selectApprovalCompleteListDefCondi } })
/* ================ pagination ================ */
const selectApprovalCompleteListLoader = ref(true)
const totalPagesSelectApprovalCompleteList = ref(0)
const currentPageSelectApprovalCompleteList = ref(1)
const pageSizeSelectApprovalCompleteList = ref(10)
const pageSizesSelectApprovalCompleteList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectApprovalCompleteList = () => {
  selectApprovalCompleteListCondi.value.pageSize = pageSizeSelectApprovalCompleteList.value;
  currentPageSelectApprovalCompleteList.value = 1;
  selectApprovalCompleteList(currentPageSelectApprovalCompleteList.value);
}
const handlePageChangeSelectApprovalCompleteList = () => {
  selectApprovalCompleteList(currentPageSelectApprovalCompleteList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectApprovalCompleteListCondiCheck.value, selectApprovalCompleteListCondi.value);
  setQueries(route, selectApprovalCompleteListCondi.value, selectApprovalCompleteListDefCondi);
  await selectApprovalCompleteList(selectApprovalCompleteListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectApprovalCompleteListCondi.value, selectApprovalCompleteListDefCondi);
  await selectApprovalCompleteList(selectApprovalCompleteListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectApprovalCompleteListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectApprovalCompleteListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectApprovalCompleteListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectApprovalCompleteListCondiCheck.value, selectApprovalCompleteListCondi.value, selectApprovalCompleteListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectApprovalCompleteListCondi.value["docttl"] = "";
    selectApprovalCompleteListCondi.value["authorname"] = "";
    selectApprovalCompleteListCondi.value["authordeptname"] = "";
  setCondiChanged(selectApprovalCompleteListCondiCheck.value, selectApprovalCompleteListCondi.value, selectApprovalCompleteListCondi.value);
})

watchEffect(() => {
})

const staticColumnsSelectApprovalCompleteList = [
  { key: "kind", title: "유형", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acttype", title: "구분", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "보고일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "보고자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordeptname", title: "부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "state", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reportdt", title: "처리일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]


const responseData = ref([]);

const moveToPageParamDefCondi = { docid: "" };
const moveToPageParamCondi = ref({ ...moveToPageParamDefCondi });

const clickedItem = ref();
const clickedItemId = ref();
const paramDocId = ref();
const moveToPageFunc = async(item) => {
  clickedItem.value = responseData.value.indexOf(item);
  clickedItemId.value = item.docid;
  paramDocId.value = item.docid;
  // 열람권자 조회하여 있으면 상세로 이동
  docidData.value.docid = item.docid;
  await selectDctReader();
  let flag = "N";
  for(let i=0; i<selectDctReaderList.value.length; i++) {
    if(selectDctReaderList.value[i].userid == getUserLoginData.value.userid) {
      flag = "Y";
      break;
    }
  }

  // 반려 문서인 경우 확인 가능하도록
  if(item.acttype == "DEA43") {
    flag = "Y";
  }

  if(flag == "Y") {
    if(item.paperflag == 'Y'){
      let path = '';
      if(item.paperrecgubun == '접수'){
        path = 'BmsDctReceivedOfflinecompletecard';
      }else{
        path = 'BmsDctProductOfflinecompletecard';
      }
      const param = {frompage: 'BmsDctapprovalcompletelist', docid: clickedItemId.value}
      router.push({
        name: path,
        query: {
          ...param,
        },
      });
    }else{
      moveToBmsDctApprovalCompleteCard(clickedItemId.value);
    }
  } else {
    alert("해당 문서의 열람권한이 없습니다. \n[비밀관리> 비밀문서사용요청> 열람요청] 에서 열람을 요청하세요.");
  }
};

// Move Function
const moveToBmsDctApprovalCompleteCard = (paramDocId) => {
  moveToPageParamCondi.value.docid = paramDocId;
  router.push({
    name: "BmsDctapprovalcompletecard",
    query: {
      ...moveToPageParamCondi.value,
    },
  });
};

/* ======================= multi selection ======================= */
const selectApprovalCompleteListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.docid}-${item.pathorder}-${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectApprovalCompleteListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectApprovalCompleteListList.value.filter((item) => 
    checkedListSelectApprovalCompleteList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectApprovalCompleteListRouterPush = (pageNum) => {
  selectApprovalCompleteListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectApprovalCompleteListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}

const selectApprovalCompleteListCnt = ref(0);

const selectApprovalCompleteList = async (pageNum) => {

  selectApprovalCompleteListLoader.value = true;
  selectApprovalCompleteListCondi.value.pageNum = parseInt(pageNum);
  selectApprovalCompleteListCondi.value.userid = getUserLoginData.value.userid;

  if (!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '') {
    if (validCheckSearchInput(comboInputData.value)) {
      selectApprovalCompleteListCondi.value[comboSelected.value] = comboInputData.value;
    } else {
      comboInputData.value = "";
      selectApprovalCompleteListLoader.value = false;
      return;
    }
  }
  try {
    const response = await API.dctAPI.selectApprovalCompleteList({ ...selectApprovalCompleteListCondi.value }, urlPaths.value);
    selectApprovalCompleteListList.value = response.data.list;
    totalPagesSelectApprovalCompleteList.value = response.data.pages;
    selectApprovalCompleteListCnt.value = response.data.total;
    const allOption = pageSizesSelectApprovalCompleteList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectApprovalCompleteListLoader.value = false;
    setCondiClear(selectApprovalCompleteListCondiCheck.value, selectApprovalCompleteListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}


// 열람권자 조회
const docidData = ref({ docid: "" });
const selectDctReaderList = ref([]);

const selectDctReader = async () => {
  try {
    const response = await API.dctAPI.selectDctReader({
      ...docidData.value
    },
      urlPaths.value
    );
    selectDctReaderList.value = response.data;
    
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

</script>