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
      <h4 class="h4">이관비밀관리기록부</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">이관비밀관리기록부</h5>
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
                  @keyup.enter="selectUseTransMgmtList(1)"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>이관일자</th>
              <td>
                <v-text-field
                  v-model="selectUseTransMgmtCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectUseTransMgmtCondi.endDt"
                  type="date"
                  :min="selectUseTransMgmtCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectUseTransMgmtList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>
        
        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectUseTransMgmtListCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveReq(1)">열람요청</v-btn>
              <v-btn class="button" @click="moveReq(4)">재사용요청</v-btn>
              <v-btn class="button" @click="moveReq(2)">인쇄요청</v-btn>
              <v-btn class="button" @click="moveReq(3)">반출요청</v-btn>
            </div>
          <v-select
            v-model="pageSizeselectUseTransMgmt"
            :items="pageSizesselectUseTransMgmt"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeselectUseTransMgmt"
            variant="outlined"
            hide-details="auto"
          ></v-select>
          </div>
        </div>

        <v-data-table
          :headers="staticColumnsselectUseTransMgmt"
          :items="selectUseTransMgmtListList"
          :items-per-page="pageSizeselectUseTransMgmt"
          :loading="selectUseTransMgmtLoader"
          show-select
          item-value="mgmtid"
          :no-data-text="noDataText"
          @click:row="(event, item) => moveDetailPage(event, item.item.raw, 'BmsDctmgmtregidetailcard')"
          class="table-type-02 hide-header-checkbox"
          hover
          >
          <template v-slot:item.data-table-select="{ item}">
            <v-checkbox
              v-if="item.raw.disabled !== true"
              :model-value="isChecked(item)"
              @update:model-value="toggleSelection(item)"
              hide-details="auto"
              class="custom-v-checkbox"
            ></v-checkbox>
            <v-checkbox
              v-else
              disabled
              indeterminate
              hide-details="auto"
              class="custom-v-checkbox"
            ></v-checkbox>
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
          v-model="currentPageselectUseTransMgmt"
          :length="totalPagesselectUseTransMgmt"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeselectUseTransMgmt"
        ></v-pagination>
      </section>  
    </div>
  </div>

  <!-- 비밀관리기록부 팝업 -->
  <v-dialog v-model="bVisibleDctManageRecordPopup" max-width="1000px" max-height="850px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>비밀관리기록부</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctManageRecordPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctManageRecordPopup
        v-if="bVisibleDctManageRecordPopup"
        :args="dctManageRecordPopupArgs"
        :toggleFunc="dctManageRecordPopupToggleFunc"
        :returnFunc="dctManageRecordPopupReturnFunc"
      ></DctManageRecordPopup>
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
import { transformDate, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "@/components/dct/DctHistoryCardPopup.vue";
import DctManageRecordPopup from "@/components/dct/DctManageRecordPopup.vue";

const name = ref('BmsUseTransMgmtRegiList')
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

/* ======================== 비밀관리기록부 팝업 ======================== */
const bVisibleDctManageRecordPopup = ref(false)
const dctManageRecordPopupArgs = ref({})
const dctManageRecordPopupToggleFunc = ref(() => {
  bVisibleDctManageRecordPopup.value = !bVisibleDctManageRecordPopup.value; 
})
const dctManageRecordPopupReturnFunc = ref((retValue) => {
  dctManageRecordPopupToggleFunc.value()
})
/* ============================================================ */

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("secttl")
const comboInputData = ref("")
const comboData = [
  { view: '유형', key: 'kind'},
  { view: "종류", key: "doccate"},
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
  selectUseTransMgmtCondi.value.kind = dockinde.value;
  selectUseTransMgmtList(1);
  currentPageselectUseTransMgmt.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectUseTransMgmtCondi.value.regirecvtype = doccate.value;
  selectUseTransMgmtList(1);
  currentPageselectUseTransMgmt.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectUseTransMgmtCondi.value.seclevel = docseclevel.value;
  selectUseTransMgmtList(1);
  currentPageselectUseTransMgmt.value = 1;
}

const checkedList = ref([]);
const isChecked = (item) => {
  return checkedList.value.some((i) => i.mgmtid === item.raw.mgmtid);
};
let selectedRegirecvtype = null;
const toggleSelection = (item) => {
  if (checkedList.value.length === 0) {
    selectedRegirecvtype = null;
  }else{
    if (item.raw.regirecvtype === '전자') {
      if (selectedRegirecvtype === '비전자') {
        alert("이미 비전자 항목을 선택한 후에는 전자 항목을 선택할 수 없습니다.");
        return;
      }
    } else if (item.raw.regirecvtype === '비전자') {
      if (selectedRegirecvtype === '전자') {
        alert("이미 전자 항목을 선택한 후에는 비전자 항목을 선택할 수 없습니다.");
        return;
      }
    }
  };

  const index = checkedList.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedList.value.splice(index, 1);
  } else {
    checkedList.value.push({ 
      mgmtid: item.raw.mgmtid,
      auth: item.raw.auth,
      regirecvtype: item.raw.regirecvtype,
      paperrecgubun: item.raw.paperrecgubun,
    });
    selectedRegirecvtype = item.raw.regirecvtype;
  }
};
const selectUseTransMgmtListList = ref([])
const selectUseTransMgmtListCnt = ref(0)
const selectUseTransMgmtDefCondi = {
  secttl: "",
  authorname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "DESC",
  sortItem: "indt"
}
const selectUseTransMgmtCondi = ref({ ...selectUseTransMgmtDefCondi })
const selectUseTransMgmtCondiCheck = ref({ value: { ...selectUseTransMgmtDefCondi }, flag: { ...selectUseTransMgmtDefCondi } })
/* ================ pagination ================ */
const selectUseTransMgmtLoader = ref(true)
const totalPagesselectUseTransMgmt = ref(0)
const currentPageselectUseTransMgmt = ref(1)
const pageSizeselectUseTransMgmt = ref(10)
const pageSizesselectUseTransMgmt = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeselectUseTransMgmt = () => {
  selectUseTransMgmtCondi.value.pageSize = pageSizeselectUseTransMgmt.value;
  currentPageselectUseTransMgmt.value = 1;
  selectUseTransMgmtList(currentPageselectUseTransMgmt.value);
}
const handlePageChangeselectUseTransMgmt = () => {
  selectUseTransMgmtList(currentPageselectUseTransMgmt.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)


onMounted(async () => {
  setCondiClear(selectUseTransMgmtCondiCheck.value, selectUseTransMgmtCondi.value);
  setQueries(route, selectUseTransMgmtCondi.value, selectUseTransMgmtDefCondi);
  await selectFindManager();
  if (await isTransManager()) {
    await selectUseTransMgmtList(selectUseTransMgmtCondi.value.pageNum);
  }
})

watch(route, async (route) => {
  setQueries(route, selectUseTransMgmtCondi.value, selectUseTransMgmtDefCondi);
  await selectUseTransMgmtList(selectUseTransMgmtCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUseTransMgmtCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUseTransMgmtCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUseTransMgmtCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUseTransMgmtCondiCheck.value, selectUseTransMgmtCondi.value, selectUseTransMgmtCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectUseTransMgmtCondi.value["secttl"] = "";
    selectUseTransMgmtCondi.value["authorname"] = "";
    selectUseTransMgmtCondi.value["mgmtno"] = "";
    selectUseTransMgmtCondi.value["copyno"] = "";
    setCondiChanged(selectUseTransMgmtCondiCheck.value, selectUseTransMgmtCondi.value, selectUseTransMgmtCondi.value);
})

const selectFindManagerList = ref([]);
const selectFindManagerCondi = ref({
  vgroupid: ['8050','8255']
}) // 8050 : 이관관리담당자, 8255: 이관관리부서장

const selectFindManager = async () => {
  try {
    const response = await API.comAPI.selectFindManager(selectFindManagerCondi.value, urlPaths.value);
    selectFindManagerList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const isTransManager = async () => {
  const myid = getUserLoginData.value.userid;
  const manager = selectFindManagerList.value.find((data) => data.userid == myid);
  if (!manager) {
    alert("이관관리담당자, 이관관리부서장만 조회할 수 있는 페이지입니다.");
    router.push({ name: "ManageTodolist" });
    return false;
  }
  return true;
};

const staticColumnsselectUseTransMgmt = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "transdt", title: "이관일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "담당부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "readauthexpiredt", title: "열람권한", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secstatus", title: "상태", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]


const noDataText = ref("");
const selectUseTransMgmtList = async (pageNum) => {

  selectUseTransMgmtLoader.value = true;
  selectUseTransMgmtCondi.value.pageNum = parseInt(pageNum);
  selectUseTransMgmtCondi.value.loginuserid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectUseTransMgmtCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectUseTransMgmtLoader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectUseTransMgmtList({ ...selectUseTransMgmtCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u.transdt = transformDate(u.transdt);
      u.seclevel = transformSeclevel(u.seclevel);
      u.secstatus = transformSecstatus(u.secstatus);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      return { ...u };
    });

    selectUseTransMgmtListList.value = response.data.list;
    selectUseTransMgmtListCnt.value = response.data.total;
    totalPagesselectUseTransMgmt.value = response.data.pages;
    const allOption = pageSizesselectUseTransMgmt.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectUseTransMgmtLoader.value = false;

    setCondiClear(selectUseTransMgmtCondiCheck.value, selectUseTransMgmtCondi.value)
  } catch (error) {
    alert("Server Error");
  }
}

const isIncludeNoAuth = () => {
  const hasNoAuth = checkedList.value.some(item => item.auth === '미보유');
  if (hasNoAuth) {
    alert("열람권한이 없는 문서가 포함되어있습니다.");
    return true; 
  }
  return false;
};

const isIncludeAuth = () => {
  const hasAuth = checkedList.value.some(item => item.auth === '보유');
  if (hasAuth) {
    alert("이미 열람권한이 있는 문서가 포함되어있습니다.");
    return true;
  }
};

const isIncludedOffline = () => {
  const hasOffline = checkedList.value.some(item=>item.regirecvtype === '비전자');
  if(hasOffline){
    return true;
  }
};

const moveReq = (type) => {
  if(checkedList.value.length < 1){
    alert("요청하실 항목을 선택해주세요");
    return;
  }
  if ((type == 2 || type == 3 || type == 4) && isIncludeNoAuth()) {
    checkedList.value = [];
    return;
  }

  if(type == 1 && isIncludeAuth()){
    checkedList.value = [];
    return;
  }

  if((type == 4 || type == 2) && isIncludedOffline()){
    alert("비전자 문서는 요청이 불가합니다.");
    checkedList.value = [];
    return;
  }
  let moveName = '';
  if(type == 1){
    moveName = isIncludedOffline() ? "BmsUsenondigitalviewreq" : 'BmsUsedigitalviewreq'; // 열람
  }else if(type == 2){
    moveName = 'BmsUsedigitalprintreq'; // 인쇄
  }else if(type == 3){
    moveName = isIncludedOffline() ? "BmsUsenondigitaltakeoutreq" : 'BmsUsetakeoutreq'; // 반출
  }else if(type == 4){
    moveName = 'BmsReusereq'; // 재사용
  }else{
    return;
  }

  const param = {mgmtid: "",frompage: "BmsUseTransMgmtRegiList"};
  const mgmtIdsString = checkedList.value.map(item => item.mgmtid).join(',');
  param.mgmtid = mgmtIdsString;

  router.push({
    name: moveName,
    params: {
       ...param
      },
      query: {
        ...param
      }
  })
};

// Move Function
const moveDetailPage = (event, row, pathname) => {
  const id = event.target.id;
  if (id != "") { 
    return;
  }

  if(row.auth == '미보유'){
    dctManageRecordPopupArgs.value.docid = row.docid;
    dctManageRecordPopupArgs.value.mgmtid = row.mgmtid;
    dctManageRecordPopupToggleFunc.value();
  }else{
    if(row.regirecvtype == '비전자'){
      if(row.paperrecgubun == '접수'){
        pathname = 'BmsDctReceivedOfflinecompletecard';
      }else{
        pathname = 'BmsDctProductOfflinecompletecard';
      }
    }
    const param = {docid: row.docid, frompage: "BmsUseTransMgmtRegiList"};
    router.push({
      name: pathname,
      params: {
          ...param
        },
        query: {
          ...param
        }
      });
  }

}
</script>