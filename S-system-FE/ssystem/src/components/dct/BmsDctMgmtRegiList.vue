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
      <h4 class="h4">비밀관리기록부{{ choicedDeptView ? ` (${choicedDeptView})` : '' }}</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">비밀관리기록부</h5>
        </div>
      </section>
      
      <section class="section">
        <div class="box-condition-02">
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
                  @keyup.enter="selectMgmtRegiList(1)"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectMgmtRegiListCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectMgmtRegiListCondi.endDt"
                  type="date"
                  :min="selectMgmtRegiListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>구분</th>
              <td class="width-150">
                <v-radio-group  v-model="selectMgmtRegiListCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
                  <v-radio v-for="(data,idx) in radioData" :key="idx" :label="data" color="indigo" :value="data"></v-radio>
                </v-radio-group>
              </td>
              <td class="width-150">
                <v-select
                v-model="choicedDeptKey"
                :items="depts"
                item-title="view"
                item-value="key"
                @update:modelValue="updatechoicedDept"
                variant="outlined"
                hide-details="auto"
                ></v-select>
              </td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectMgmtRegiList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>
  
        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectMgmtRegiListCnt}}개</span>
          <div class="box-flex">
            <div class="action-buttons">
              <v-btn class="button" @click="moveProductOfflineCreate()">생산비전자비밀등록</v-btn>
            </div>
            <v-select
              v-model="delline"
              :items="dellineData"
              @update:modelValue="dellineSearch"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            />
            <v-select
              v-model="pageSizeSelectMgmtRegiList"
              :items="pageSizesSelectMgmtRegiList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectMgmtRegiList"
              variant="outlined"
              hide-details="auto"></v-select>
          </div>
        </div>

        <v-data-table
          :headers="getDynamicHeaders"
          :items="selectMgmtRegiListList"
          :items-per-page="pageSizeSelectMgmtRegiList"
          :loading="selectMgmtRegiListLoader"
          item-value="mgmtid"
          class="table-type-02"
          hover
          >
          <template v-slot:item="{ item }">
            <tr :style="{
                'text-decoration': item.raw.delflag === 'Y' || item.raw.cnsrvflag === 'Y' || ['MGT72', 'MGT80', 'MGT81'].includes(item.raw.secstatus) ? 'line-through red' : 'none',
              }" @click="moveDetailPage(item.raw, 'BmsDctmgmtregidetailcard')" style="cursor: pointer;">
              <td> {{ item.raw.copyno != undefined ? !item.raw.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서' }} </td>
              <td v-if="item.raw.regirecvtype == '1'">전자</td>
              <td v-else-if="item.raw.regirecvtype == '2'">비전자</td>
              <td>{{ transformSeclevel(item.raw.seclevel) }}</td>
              <td>{{ item.raw.docno }}</td>
              <td>{{ item.raw.mgmtno }}</td>
              <td :class="{ 'text-left': item }">{{ item.raw.secttl }}</td>
              <td>{{ transformDate(item.raw.indt) }}</td>
              <td>{{ item.raw.copyno }}</td>
              <td>{{ item.raw.noti }}</td>
              <td>{{ item.raw.authorname }}</td>
              <td>{{ item.raw.deptname }}</td>
              <td>{{ transformSecstatus(item.raw.secstatus) }}</td>
              <td v-if="selectMgmtRegiListCondi.userreqtype.includes('부서')">{{ item.raw.auth }}</td>
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
          v-model="currentPageSelectMgmtRegiList"
          :length="totalPagesSelectMgmtRegiList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectMgmtRegiList"
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
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import DctManageRecordPopup from "./DctManageRecordPopup.vue";
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctMgmtRegiList')

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
  selectMgmtRegiListCondi.value.kind = dockinde.value;
  selectMgmtRegiList(1);
  currentPageSelectMgmtRegiList.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectMgmtRegiListCondi.value.regirecvtype = doccate.value;
  selectMgmtRegiList(1);
  currentPageSelectMgmtRegiList.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectMgmtRegiListCondi.value.seclevel = docseclevel.value;
  selectMgmtRegiList(1);
  currentPageSelectMgmtRegiList.value = 1;
}
const delline = ref("")
const dellineData = ref([
  { view: '삭선여부', key: ''},
  { view: '삭선', key: '1'},
  { view: '삭선없음', key: '2'},
])
const dellineSearch = () => {
  selectMgmtRegiListCondi.value.delline = delline.value;
  selectMgmtRegiList(1);
  currentPageSelectMgmtRegiList.value = 1;
}
// for selectMgmtRegiList
const selectMgmtRegiListList = ref([])
const selectMgmtRegiListCnt = ref(0)
const selectMgmtRegiListDefCondi = {
  userreqtype: "개인",
  secttl: "",
  authorname: "",
  mgmtno: "",
  mgmtid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}
const selectMgmtRegiListCondi = ref({ ...selectMgmtRegiListDefCondi })
const selectMgmtRegiListCondiCheck = ref({ value: { ...selectMgmtRegiListDefCondi }, flag: { ...selectMgmtRegiListDefCondi } })
/* ================ pagination ================ */
const selectMgmtRegiListLoader = ref(true)
const totalPagesSelectMgmtRegiList = ref(0)
const currentPageSelectMgmtRegiList = ref(1)
const pageSizeSelectMgmtRegiList = ref(10)
const pageSizesSelectMgmtRegiList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectMgmtRegiList = () => {
  selectMgmtRegiListCondi.value.pageSize = pageSizeSelectMgmtRegiList.value;
  currentPageSelectMgmtRegiList.value = 1;
  selectMgmtRegiList(currentPageSelectMgmtRegiList.value);
}
const handlePageChangeSelectMgmtRegiList = () => {
  selectMgmtRegiList(currentPageSelectMgmtRegiList.value);
}
/* ============================================== */


// 구분 라디오 버튼
const radioData = ['개인','부서']

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectMgmtRegiListCondiCheck.value, selectMgmtRegiListCondi.value);
  setQueries(route, selectMgmtRegiListCondi.value, selectMgmtRegiListDefCondi);
  await selectOtherDept();
  await selectMgmtRegiList(selectMgmtRegiListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectMgmtRegiListCondi.value, selectMgmtRegiListDefCondi);
  await selectMgmtRegiList(selectMgmtRegiListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectMgmtRegiListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectMgmtRegiListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectMgmtRegiListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectMgmtRegiListCondiCheck.value, selectMgmtRegiListCondi.value, selectMgmtRegiListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectMgmtRegiListCondi.value["secttl"] = "";
    selectMgmtRegiListCondi.value["authorname"] = "";
    selectMgmtRegiListCondi.value["mgmtno"] = "";
    selectMgmtRegiListCondi.value["copyno"] = "";
  setCondiChanged(selectMgmtRegiListCondiCheck.value, selectMgmtRegiListCondi.value, selectMgmtRegiListCondi.value);
})

const getDynamicHeaders = computed(() => {
  const columns = selectMgmtRegiListCondi.value.userreqtype.includes('부서') ? staticColumnsDept : staticColumnsMine;
  return [...columns];
});

const staticColumnsMine = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "140px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "noti", title: "예고문", width: "130px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "담당부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secstatus", title: "상태", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

const staticColumnsDept = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "140px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "noti", title: "예고문", width: "130px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "담당부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secstatus", title: "상태", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "auth", title: "열람권한", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]


const updateCondi = () => {
  choicedDeptKey.value = "";
  choicedDeptView.value = "";
  selectMgmtRegiList(1);
  currentPageSelectMgmtRegiList.value = 1;
}

const selectMgmtRegiList = async (pageNum) => {

  selectMgmtRegiListLoader.value = true;
  selectMgmtRegiListCondi.value.pageNum = parseInt(pageNum);

  if(selectMgmtRegiListCondi.value.userreqtype == '개인'){
    selectMgmtRegiListCondi.value.authorid = getUserLoginData.value.userid;
  }else if(selectMgmtRegiListCondi.value.userreqtype == '부서'){
    selectMgmtRegiListCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectMgmtRegiListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectMgmtRegiListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectMgmtRegiList({ ...selectMgmtRegiListCondi.value }, urlPaths.value);
    response.data.list.map((u) => {
      u.noti = dayjs(u.notiprotterm, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') + (u.copyno.includes("원") ? transReclassInfo(u.orgnreclass) : transReclassInfo(u.copyreclass));
    });
    selectMgmtRegiListList.value = response.data.list;
    selectMgmtRegiListCnt.value = response.data.total;
    totalPagesSelectMgmtRegiList.value = response.data.pages;
    const allOption = pageSizesSelectMgmtRegiList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectMgmtRegiListLoader.value = false;

    setCondiClear(selectMgmtRegiListCondiCheck.value, selectMgmtRegiListCondi.value)
  } catch (error) {
    alert("Server Error")
  }
}

const transReclassInfo = (value) => {
  switch (value) {
    case '20':
      return ' 존안'
    case '21':
      return ' 재분류'
    case '22':
      return ' 이관'
    case '23':
      return ' 파기'
    default:
      return ''
  }
}

// 타부서 조회
const selectOtherDeptCondi = ref({
  mydeptid : getUserLoginData.value.deptid
});
const choicedDeptKey = ref("");
const choicedDeptView = ref("");
const depts =  ref([{ view: "타부서 선택", key: ""}]);
const selectOtherDept = async () => {
  try {
    const response = await API.dctAPI.selectOtherDept({ ...selectOtherDeptCondi.value }, urlPaths.value);
    depts.value = [...depts.value, ...response.data.map(item => ({ view: item.orgname, key: item.orgid }))];
  } catch (error) {
    alert("Server Error")
  }
};

const updatechoicedDept = () => {
  const selectedDept = depts.value.find(dept => dept.key === choicedDeptKey.value);
  if (selectedDept) {
    choicedDeptView.value = selectedDept.view;
  }
  if(choicedDeptKey.value != ""){
    selectMgmtRegiListCondi.value.userreqtype = '타부서';
    selectMgmtRegiListCondi.value.deptid = choicedDeptKey.value;
    selectMgmtRegiList(1);
    currentPageSelectMgmtRegiList.value = 1;
  }else{
    selectMgmtRegiListCondi.value.userreqtype = '개인';
    updateCondi();
  }
};


// Move Function
const moveDetailPage = (row, pathname) => {
  if(row.delflag == 'Y' || row.cnsrvflag == 'Y' || ['MGT72', 'MGT80', 'MGT81'].includes(row.secstatus)){
    dctManageRecordPopupArgs.value.mgmtid = row.mgmtid;
    dctManageRecordPopupArgs.value.docid = row.docid;
    dctManageRecordPopupToggleFunc.value();
    return
  }

  if(row.auth == '미보유'){
    alert("해당 문서의 열람권한이 없습니다. \n[비밀관리> 비밀문서사용요청> 열람요청] 에서 열람을 요청하세요.");
    return
  }

  if(row.regirecvtype == '2'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  const param = {docid: row.docid, frompage: 'BmsDctmgmtregilist'};
  router.push({
    name: pathname,
    query: {
      ...param
    }
  });
}

const moveProductOfflineCreate = () => {
  router.push({
    name: 'BmsDctProductOfflinecreatecard',
  });
};

</script>