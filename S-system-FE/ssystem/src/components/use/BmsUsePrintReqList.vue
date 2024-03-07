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
      <h4 class="h4">비밀문서 사용요청</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">인쇄요청</h5>
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
                <v-text-field v-else
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectUsePrint(1)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectUsePrintCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectUsePrintCondi.endDt"
                  type="date"
                  :min="selectUsePrintCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>구분</th>
              <td class="width-150">
                <v-radio-group  v-model="selectUsePrintCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
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
          <v-btn class="magnify-solid" @click="selectUsePrint(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectUsePrintCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="moveToPagePrintReqs()">일괄요청</v-btn>
            </div>
            <v-select
              v-model="pageSizeselectUsePrint"
              :items="pageSizesselectUsePrint"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeselectUsePrint"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          :headers="staticColumnsselectUsePrint"
          :items="selectUsePrintList"
          :items-per-page="pageSizeselectUsePrint"
          :loading="selectUsePrintLoader"
          :item-value="selectUsePrintIdentifier"
          show-select
          :no-data-text="noDataText"
          class="table-type-02 hide-header-checkbox"
          @click:row="(event, item) => moveDetailPageOrRereq(event, item.item.raw, 'BmsDctmgmtregidetailcard')"
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
          <template v-slot:item.reqState="{ item }">
            <v-btn
              v-if="item.raw.reqState == '요청가능'"
              type="button"
              small
              solo
              @click.stop="moveToPagePrintReq(item.raw.mgmtid, item.raw.regirecvtype)"
              color="#35447F"
              >요청</v-btn
            >
            <span v-else>{{item.raw.reqState}}</span>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageselectUsePrint"
          :length="totalPagesselectUsePrint"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeselectUsePrint"
        ></v-pagination>
      </section>
    </div>
  </div>
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
import { transformDate, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsUsePrintReqList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("secttl")
const comboInputData = ref("")
const comboData = [
  { view: '유형', key: 'kind'},
  { view: "종류", key: "doccate"},
  { view: "관리번호", key: "mgmtno"},
  { view: "제목", key: "secttl"},
  { view: "사본번호", key: "copyno"},
  { view: "담당자", key: "authorname"},
];
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectUsePrintCondi.value.kind = dockinde.value;
  selectUsePrint(1);
  currentPageselectUsePrint.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectUsePrintCondi.value.regirecvtype = doccate.value;
  selectUsePrint(1);
  currentPageselectUsePrint.value = 1;
}
// for selectUsePrint
const checkedListselectUsePrint = ref([])
const isChecked = (item) => {
  return checkedListselectUsePrint.value.some((i) => i.mgmtid === item.raw.mgmtid);
};

let selectedRegirecvtype = null;
const toggleSelection = (item) => {

  if (checkedListselectUsePrint.value.length === 0) {
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

  const index = checkedListselectUsePrint.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedListselectUsePrint.value.splice(index, 1);
  } else {
    checkedListselectUsePrint.value.push({ mgmtid: item.raw.mgmtid, regirecvtype: item.raw.regirecvtype });
    selectedRegirecvtype = item.raw.regirecvtype;
  }
};

const selectUsePrintList = ref([])
const selectUsePrintCnt = ref(0)
const selectUsePrintDefCondi = {
  userreqtype: "개인",
  secttl: "",
  authorname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "mgmtid"
}
const selectUsePrintCondi = ref({ ...selectUsePrintDefCondi })
const selectUsePrintCondiCheck = ref({ value: { ...selectUsePrintDefCondi }, flag: { ...selectUsePrintDefCondi } })
/* ================ pagination ================ */
const selectUsePrintLoader = ref(true)
const totalPagesselectUsePrint = ref(0)
const currentPageselectUsePrint = ref(1)
const pageSizeselectUsePrint = ref(10)
const pageSizesselectUsePrint = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeselectUsePrint = () => {
  selectUsePrintCondi.value.pageSize = pageSizeselectUsePrint.value;
  currentPageselectUsePrint.value = 1;
  selectUsePrint(currentPageselectUsePrint.value);
}
const handlePageChangeselectUsePrint = () => {
  selectUsePrint(currentPageselectUsePrint.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectUsePrintCondiCheck.value, selectUsePrintCondi.value);
  setQueries(route, selectUsePrintCondi.value, selectUsePrintDefCondi);
  await selectOtherDept();
  await selectUsePrint(selectUsePrintCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectUsePrintCondi.value, selectUsePrintDefCondi);
  await selectUsePrint(selectUsePrintCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUsePrintCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUsePrintCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUsePrintCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUsePrintCondiCheck.value, selectUsePrintCondi.value, selectUsePrintCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectUsePrintCondi.value["secttl"] = "";
  selectUsePrintCondi.value["authorname"] = "";
  selectUsePrintCondi.value["mgmtno"] = "";
  selectUsePrintCondi.value["copyno"] = "";
  setCondiChanged(selectUsePrintCondiCheck.value, selectUsePrintCondi.value, selectUsePrintCondi.value);
})


const staticColumnsselectUsePrint = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "130px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "deptname", title: "담당부서", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "readPossible", title: "열람권한", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "readPossibleDate", title: "열람만료일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "printPossibleDate", title: "파쇄만료일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqState", title: "인쇄요청", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ======================= multi selection ======================= */
const selectUsePrintIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.mgmtid}-${item.docid}-${item.userid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectUsePrintFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectUsePrintList.value.filter((item) => 
    checkedListselectUsePrint.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const prevUserReqType = ref('');
const updateCondi = () => {
  choicedDeptKey.value = "";
  choicedDeptView.value = "";
  if(prevUserReqType.value == '타부서' && ['개인','부서'].includes(selectUsePrintCondi.value.userreqtype)){
    checkedListselectUsePrint.value = [];
  }
  selectUsePrint(1);
  currentPageselectUsePrint.value = 1;
  prevUserReqType.value = "";
}

const noDataText = ref("");
const selectUsePrint = async (pageNum) => {

  selectUsePrintLoader.value = true;
  selectUsePrintCondi.value.pageNum = parseInt(pageNum);
  selectUsePrintCondi.value.loginuserid = getUserLoginData.value.userid;

  if(selectUsePrintCondi.value.userreqtype == '부서'){
    selectUsePrintCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectUsePrintCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectUsePrintLoader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectUsePrint({ ...selectUsePrintCondi.value }, urlPaths.value);

    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      selectUsePrintLoader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u.indt = transformDate(u.indt);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      return { ...u};
    });

    selectUsePrintList.value = response.data.list;
    selectUsePrintCnt.value = response.data.total;

    selectUsePrintList.value.forEach(item=>{
      if(item.readauthexpiredt != null && item.reader != 'x'){ // 열람권한O
        const isReadAuthExpired  = dayjs(item.readauthexpiredt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
        if(isReadAuthExpired){ // 열람권한만료
          item.readPossible = "미보유";
          item.disabled = true;
        }else{
          item.readPossible = "보유";
          item.printPossibleDate = item.grantdestructdt;
          if(item.print.includes('승인')){
            const isDestructAuthExpired  = dayjs(item.grantdestructdt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
            if(isDestructAuthExpired){ // 인쇄권한만료
              item.reqState = '요청가능';
            }else{
              item.reqState = item.print;
              item.disabled = true;
            }
          }else{
            item.reqState = '요청가능';
          }
        }
        item.readPossibleDate = item.readauthexpiredt;
      }else{ // 열람권한X
        item.readPossible = "미보유";
        item.disabled = true;
      }
    
      if(item.regirecvtype == '비전자'){
        item.reqState = '요청불가';
        item.disabled = true;
      }
    });

    totalPagesselectUsePrint.value = response.data.pages;
    const allOption = pageSizesselectUsePrint.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectUsePrintLoader.value = false;

    setCondiClear(selectUsePrintCondiCheck.value, selectUsePrintCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// 구분 라디오 버튼
const radioData = ['개인','부서']

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
    selectUsePrintCondi.value.userreqtype = '타부서';
    selectUsePrintCondi.value.deptid = choicedDeptKey.value;
    prevUserReqType.value = selectUsePrintCondi.value.userreqtype;
    selectUsePrint(1);
    currentPageselectUsePrint.value = 1;
    checkedListselectUsePrint.value = [];
  }else{
    selectUsePrintCondi.value.userreqtype = '개인';
    updateCondi();
  }
};


/* ============ 요청  버튼 Move FN ================ */

// 일괄 요청
const moveToPagePrintReqs = () => {
  
  if (checkedListselectUsePrint.value.length == 0) {
    confirm("요청하실 항목을 선택해주세요.")
    return;
  }

  const mgmtIdsString = checkedListselectUsePrint.value
    .map(item => item.mgmtid)
    .join(',');
  const queryString = {mgmtid: ""};
  queryString.mgmtid = mgmtIdsString;

  if(checkedListselectUsePrint.value.some((i) => i.regirecvtype === '전자')){
    moveToBmsUsedigitalprintreq(queryString);
  }
  // }else if (checkedListselectUsePrint.value.some((i) => i.regirecvtype === '비전자')){
  //   moveToBmsUsenondigitalprintreq(queryString);
  // }

}

// 낱개 요청
const moveToPagePrintReq = (paramMgmtid, paramRegirecvtype) => {
  const param = {mgmtid: ""};
  param.mgmtid = paramMgmtid;

  if(paramRegirecvtype == '전자'){
    moveToBmsUsedigitalprintreq(param);
  }
  // }else if(paramRegirecvtype == '비전자'){
  //   moveToBmsUsenondigitalprintreq(param);
  // }
};

// 전자인쇄 요청서
const moveToBmsUsedigitalprintreq = (item) => {
  router.push({
    name: "BmsUsedigitalprintreq",
    query: {
      ...item,
    },
  });
};

// 비전자인쇄 요청서
const moveToBmsUsenondigitalprintreq = (item) => {
  router.push({
    name: "BmsUsenondigitalprintreq",
    query: {
      ...item,
    },
  });
};

/* =========================================================== */

const moveDetailPageOrRereq = (event, row, pathname) => {
  const id = event.target.id;
  if (id != "") {
    return;
  }

  if(row.readPossible == '미보유'){
    alert("해당 문서의 열람권한이 없습니다. \n[비밀관리> 비밀문서사용요청> 열람요청] 에서 열람을 요청하세요.");
    return
  }else{
    if(row.regirecvtype == '비전자'){
      if(row.paperrecgubun == '접수'){
        pathname = 'BmsDctReceivedOfflinecompletecard';
      }else{
        pathname = 'BmsDctProductOfflinecompletecard';
      }
    }
    const param = {docid: row.docid, frompage: 'BmsUsePrintReqList'};
    router.push({
      name: pathname,
      query: {
        ...param
      }
    });
  }
}


</script>