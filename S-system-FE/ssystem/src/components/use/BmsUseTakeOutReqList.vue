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
          <h5 class="h5">반출요청</h5>
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
                  @keyup.enter="selectUseTakeOut(1)"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectUseTakeOutCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectUseTakeOutCondi.endDt"
                  type="date"
                  :min="selectUseTakeOutCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>구분</th>
              <td class="width-150">
                <v-radio-group  v-model="selectUseTakeOutCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
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
          <v-btn class="magnify-solid" @click="selectUseTakeOut(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectUseTakeOutCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="moveToPageTakeoutReqs()">일괄요청</v-btn>
            </div>
            <v-select
              v-model="pageSizeselectUseTakeOut"
              :items="pageSizesselectUseTakeOut"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeselectUseTakeOut"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          :headers="staticColumnsselectUseTakeOut"
          :items="selectUseTakeOutList"
          :items-per-page="pageSizeselectUseTakeOut"
          :loading="selectUseTakeOutLoader"
          :item-value="selectUseTakeOutIdentifier"
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
              @click.stop="moveToPageTakeoutReq(item.raw.mgmtid, item.raw.regirecvtype)"
              color="#35447F"
              >요청</v-btn
            >
            <span v-else>{{item.raw.reqState}}</span>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageselectUseTakeOut"
          :length="totalPagesselectUseTakeOut"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeselectUseTakeOut"
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
import { getValueByKey, validCheckSearchInput} from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { transformDate, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsUseTakeOutReqList')
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
  selectUseTakeOutCondi.value.kind = dockinde.value;
  selectUseTakeOut(1);
  currentPageselectUseTakeOut.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectUseTakeOutCondi.value.regirecvtype = doccate.value;
  selectUseTakeOut(1);
  currentPageselectUseTakeOut.value = 1;
}
// for selectUseTakeOut
const checkedListselectUseTakeOut = ref([])
const isChecked = (item) => {
  return checkedListselectUseTakeOut.value.some((i) => i.mgmtid === item.raw.mgmtid);
};


let selectedRegirecvtype = null;
const toggleSelection = (item) => {

  if (checkedListselectUseTakeOut.value.length === 0) {
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

  const index = checkedListselectUseTakeOut.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedListselectUseTakeOut.value.splice(index, 1);
  } else {
    checkedListselectUseTakeOut.value.push({ mgmtid: item.raw.mgmtid, regirecvtype: item.raw.regirecvtype });
    selectedRegirecvtype = item.raw.regirecvtype;
  }
};

const selectUseTakeOutList = ref([])
const selectUseTakeOutCnt = ref(0)
const selectUseTakeOutDefCondi = {
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
const selectUseTakeOutCondi = ref({ ...selectUseTakeOutDefCondi })
const selectUseTakeOutCondiCheck = ref({ value: { ...selectUseTakeOutDefCondi }, flag: { ...selectUseTakeOutDefCondi } })
/* ================ pagination ================ */
const selectUseTakeOutLoader = ref(true)
const totalPagesselectUseTakeOut = ref(0)
const currentPageselectUseTakeOut = ref(1)
const pageSizeselectUseTakeOut = ref(10)
const pageSizesselectUseTakeOut = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeselectUseTakeOut = () => {
  selectUseTakeOutCondi.value.pageSize = pageSizeselectUseTakeOut.value;
  currentPageselectUseTakeOut.value = 1;
  selectUseTakeOut(currentPageselectUseTakeOut.value);
}
const handlePageChangeselectUseTakeOut = () => {
  selectUseTakeOut(currentPageselectUseTakeOut.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectUseTakeOutCondiCheck.value, selectUseTakeOutCondi.value);
  setQueries(route, selectUseTakeOutCondi.value, selectUseTakeOutDefCondi);
  await selectOtherDept();
  await selectUseTakeOut(selectUseTakeOutCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectUseTakeOutCondi.value, selectUseTakeOutDefCondi);
  await selectUseTakeOut(selectUseTakeOutCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUseTakeOutCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUseTakeOutCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUseTakeOutCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUseTakeOutCondiCheck.value, selectUseTakeOutCondi.value, selectUseTakeOutCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectUseTakeOutCondi.value["secttl"] = "";
  selectUseTakeOutCondi.value["authorname"] = "";
  selectUseTakeOutCondi.value["mgmtno"] = "";
  selectUseTakeOutCondi.value["copyno"] = "";
  setCondiChanged(selectUseTakeOutCondiCheck.value, selectUseTakeOutCondi.value, selectUseTakeOutCondi.value);
})

const staticColumnsselectUseTakeOut = [
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
  { key: "lendBackPossibleDate", title: "반납만료일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqState", title: "반출요청", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

/* ======================= multi selection ======================= */
const selectUseTakeOutIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.mgmtid}-${item.docid}-${item.userid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectUseTakeOutFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectUseTakeOutList.value.filter((item) => 
    checkedListselectUseTakeOut.value.includes(item.identifier))
  
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
  if(prevUserReqType.value == '타부서' && ['개인','부서'].includes(selectUseTakeOutCondi.value.userreqtype)){
    checkedListselectUseTakeOut.value = [];
  }
  selectUseTakeOut(1);
  currentPageselectUseTakeOut.value = 1;
}

const noDataText = ref("");
const selectUseTakeOut = async (pageNum) => {

  selectUseTakeOutLoader.value = true;
  selectUseTakeOutCondi.value.pageNum = parseInt(pageNum);
  selectUseTakeOutCondi.value.loginuserid = getUserLoginData.value.userid;

  if(selectUseTakeOutCondi.value.userreqtype == '부서'){
    selectUseTakeOutCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectUseTakeOutCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectUseTakeOutLoader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectUseTakeOut({ ...selectUseTakeOutCondi.value }, urlPaths.value);

    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      selectUseTakeOutLoader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u.indt = transformDate(u.indt);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      return { ...u};
    });

    selectUseTakeOutList.value = response.data.list;
    selectUseTakeOutCnt.value = response.data.total;

    selectUseTakeOutList.value.forEach(item=>{
      if(item.readauthexpiredt != null && item.reader != 'x'){ // 열람권한O
        const isReadAuthExpired  = dayjs(item.readauthexpiredt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
        if(isReadAuthExpired){ // 열람권한만료
          item.readPossible = "미보유";
          item.disabled = true;
        }else{
          item.readPossible = "보유";
          item.lendBackPossibleDate = item.grantbackdt;
          if(item.lend.includes('승인')){
            const isLendBackAuthExpired  = dayjs(item.grantbackdt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
            if(isLendBackAuthExpired){ // 반출권한만료
              item.reqState = '요청가능';
            }else{
              item.reqState = item.lend;
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
    });

    totalPagesselectUseTakeOut.value = response.data.pages;
    const allOption = pageSizesselectUseTakeOut.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectUseTakeOutLoader.value = false;

    setCondiClear(selectUseTakeOutCondiCheck.value, selectUseTakeOutCondi.value)
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
    selectUseTakeOutCondi.value.userreqtype = '타부서';
    selectUseTakeOutCondi.value.deptid = choicedDeptKey.value;
    prevUserReqType.value = selectUseTakeOutCondi.value.userreqtype;
    selectUseTakeOut(1);
    currentPageselectUseTakeOut.value = 1;
    checkedListselectUseTakeOut.value = [];
  }else{
    selectUseTakeOutCondi.value.userreqtype = '개인';
    updateCondi();
  }
};


/* ============ 요청  버튼 Move FN ================ */

// 일괄 요청
const moveToPageTakeoutReqs = () => {
  
  if (checkedListselectUseTakeOut.value.length == 0) {
    confirm("요청하실 항목을 선택해주세요.")
    return;
  }

  const mgmtIdsString = checkedListselectUseTakeOut.value
    .map(item => item.mgmtid)
    .join(',');
  const queryString = {mgmtid: ""};
  queryString.mgmtid = mgmtIdsString;

  if(checkedListselectUseTakeOut.value.some((i) => i.regirecvtype === '전자')){
    moveToBmsUsetakeoutreq(queryString);
  }else if (checkedListselectUseTakeOut.value.some((i) => i.regirecvtype === '비전자')){
    moveToBmsUsenondigitaltakeoutreq(queryString);
  }
}

// 낱개 요청
const moveToPageTakeoutReq = (paramMgmtid, paramRegirecvtype) => {
  const param = {mgmtid: ""};
  param.mgmtid = paramMgmtid;
  
  if(paramRegirecvtype == '전자'){
    moveToBmsUsetakeoutreq(param);
  }else if(paramRegirecvtype == '비전자'){
    moveToBmsUsenondigitaltakeoutreq(param);
  }
};


// 전자반출 요청서
const moveToBmsUsetakeoutreq = (item) => {
  router.push({
    name: "BmsUsetakeoutreq",
    query: {
      ...item,
    },
  });
};

// 비전자반출 요청서
const moveToBmsUsenondigitaltakeoutreq = (item) => {
  router.push({
    name: "BmsUsenondigitaltakeoutreq",
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
    const param = {docid: row.docid, frompage: 'BmsUseTakeOutReqList'};
    router.push({
      name: pathname,
      query: {
        ...param
      }
    });
  }
}

</script>