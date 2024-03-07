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
      <h4 class="h4">비밀열람함</h4>
    </div>
    
    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비밀열람함</h5>
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
                  v-else-if="comboSelected === 'doccate'"
                  v-model="doccate"
                  :items="doccateData"
                  @update:modelValue="cateSearch"
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
                  @keyup.enter="selectdctRdoc(1)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectdctRdocCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                  ~
                <v-text-field
                  v-model="selectdctRdocCondi.endDt"
                  type="date"
                  :min="selectdctRdocCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectdctRdoc(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectdctRdocCnt}}개</span>
          <v-select
            v-model="pageSizeSelectdctRdoc"
            :items="pageSizesSelectdctRdoc"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectdctRdoc"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          :headers="staticColumnsSelectdctRdoc"
          :items="selectdctRdocList"
          :items-per-page="pageSizeSelectdctRdoc"
          :loading="selectdctRdocLoader"
          item-value="mgmtid"
          :no-data-text="noDataText"
          @click:row="(event, item) => moveDetailPage(item.item.raw, 'BmsDctmgmtregidetailcard')"
          class="table-type-02"
          hover
          >
          <template v-slot:item.regirecvtype="{ item }">
            <span v-if="item.raw.regirecvtype == '1'">전자</span>
            <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
          </template>
          <template v-slot:item.seclevel="{ item }">
            <span>{{ transformSeclevel(item.raw.seclevel) }}</span>
          </template>
          <template v-slot:item.secttl="{ item }">
            <div :class="{ 'text-left': item }">
              {{ item.raw.secttl }}
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
          v-model="currentPageSelectdctRdoc"
          :length="totalPagesSelectdctRdoc"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectdctRdoc"
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
import { transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctToDoListRead')
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
const comboSelected = ref("secttl")
const comboInputData = ref("")
const comboData = [
  { view: '유형', key: 'kind'},
  { view: '종류', key: 'doccate'},
  { view: '등급', key: 'seclevel'},
  { view: "문서번호", key: "docno"},
  { view: "관리번호", key: "mgmtno"},
  { view: "제목", key: "secttl"},
  { view: "사본번호", key: "copyno"},
  { view: "담당자", key: "authorname"},
  { view: "담당부서", key: "deptname"},
];
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectdctRdocCondi.value.kind = dockinde.value;
  selectdctRdoc(1);
  currentPageSelectdctRdoc.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectdctRdocCondi.value.regirecvtype = doccate.value;
  selectdctRdoc(1);
  currentPageSelectdctRdoc.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectdctRdocCondi.value.seclevel = docseclevel.value;
  selectdctRdoc(1);
  currentPageSelectdctRdoc.value = 1;
}
// for selectdctRdoc
const selectdctRdocList = ref([])
const selectdctRdocCnt = ref(0)
const selectdctRdocDefCondi = {
  secttl: "",
  authorname: "",
  mgmtno: "",
  mgmtno: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid"
}
const selectdctRdocCondi = ref({ ...selectdctRdocDefCondi })
const selectdctRdocCondiCheck = ref({ value: { ...selectdctRdocDefCondi }, flag: { ...selectdctRdocDefCondi } })
/* ================ pagination ================ */
const selectdctRdocLoader = ref(true)
const totalPagesSelectdctRdoc = ref(0)
const currentPageSelectdctRdoc = ref(1)
const pageSizeSelectdctRdoc = ref(10)
const pageSizesSelectdctRdoc = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectdctRdoc = () => {
  selectdctRdocCondi.value.pageSize = pageSizeSelectdctRdoc.value;
  currentPageSelectdctRdoc.value = 1;
  selectdctRdoc(currentPageSelectdctRdoc.value);
}
const handlePageChangeSelectdctRdoc = () => {
  selectdctRdoc(currentPageSelectdctRdoc.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectdctRdocCondiCheck.value, selectdctRdocCondi.value);
  setQueries(route, selectdctRdocCondi.value, selectdctRdocDefCondi);
  await selectdctRdoc(selectdctRdocCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectdctRdocCondi.value, selectdctRdocDefCondi);
  await selectdctRdoc(selectdctRdocCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectdctRdocCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectdctRdocCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectdctRdocCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectdctRdocCondiCheck.value, selectdctRdocCondi.value, selectdctRdocCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectdctRdocCondi.value["secttl"] = "";
    selectdctRdocCondi.value["authorname"] = "";
    selectdctRdocCondi.value["mgmtno"] = "";
    setCondiChanged(selectdctRdocCondiCheck.value, selectdctRdocCondi.value, selectdctRdocCondi.value);
  })

  watchEffect(() => {
})


const staticColumnsSelectdctRdoc = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류",  width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급",  width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호",  width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호",  width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px",sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "담당부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "readauthexpiredt", title: "열람만료일", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]


const noDataText = ref("");
const selectdctRdoc = async (pageNum) => {

  selectdctRdocLoader.value = true;
  selectdctRdocCondi.value.pageNum = parseInt(pageNum);
  selectdctRdocCondi.value.userid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectdctRdocCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectdctRdocLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectdctRdoc({ ...selectdctRdocCondi.value }, urlPaths.value);

    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      selectdctRdocLoader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm') };
      u = { ...u, kind: u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서'};
      return { ...u};
    });

    selectdctRdocList.value = response.data.list;
    selectdctRdocCnt.value = response.data.total;

    totalPagesSelectdctRdoc.value = response.data.pages;
    const allOption = pageSizesSelectdctRdoc.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectdctRdocLoader.value = false;

    setCondiClear(selectdctRdocCondiCheck.value, selectdctRdocCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// Move Function
const moveDetailPage = (row, pathname) => {
  const isReadAuthExpired  = dayjs(row.readauthexpiredt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
  if(isReadAuthExpired){
    alert("열람기간이 만료되었습니다. \n[비밀관리> 비밀문서사용요청> 열람요청] 에서 열람을 요청하세요.")
    return;
  }

  if(row.regirecvtype == '2'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  const param = {docid: row.docid, frompage: "BmsDcttodolistread"};
  router.push({
    name: pathname,
    query: {
      ...param
    }
  });
}

</script>