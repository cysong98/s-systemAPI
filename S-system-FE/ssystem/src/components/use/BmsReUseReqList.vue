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
          <h5 class="h5">재사용요청</h5>
        </div>

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
                  @keyup.enter="selectReUse(1)"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectReUseCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectReUseCondi.endDt"
                  type="date"
                  :min="selectReUseCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>구분</th>
              <td class="width-150">
                <v-radio-group  v-model="selectReUseCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
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
          <v-btn class="magnify-solid" @click="selectReUse(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectReUseCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="moveToPageReuseReqs()">일괄요청</v-btn>
            </div>
            <v-select
              v-model="pageSizeselectReUse"
              :items="pageSizesselectReUse"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeselectReUse"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          v-model="selectAll"
          :headers="staticColumnsselectReUse"
          :items="selectReUseList"
          :items-per-page="pageSizeselectReUse"
          :loading="selectReUseLoader"
          :item-value="selectReUseIdentifier"
          show-select
          :no-data-text="noDataText"
          @update:modelValue="selectAllToggle"
          class="table-type-02 hide-header-checkbox"
          @click:row="(event, item) => moveDetailPageOrRereq(event, item.item.raw, 'BmsDctmgmtregidetailcard')"
          hover
          >
          <template v-slot:item.secttl="{ item }">
            <div :class="{ 'text-left': item }">
              {{ item.raw.secttl }}
            </div>
          </template>
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
          <template v-slot:item.reqState="{ item }">
            <v-btn
              v-if="item.raw.reqState == '요청가능'"
              type="button"
              small
              solo
              @click.stop="moveToPageReuseReq(item.raw)"
              color="#35447F"
              >요청</v-btn
            >
            <span v-else>{{item.raw.reqState}}</span>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageselectReUse"
          :length="totalPagesselectReUse"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeselectReUse"
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

const name = ref('BmsReUseReqList')
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
  selectReUseCondi.value.kind = dockinde.value;
  selectReUse(1);
  currentPageselectReUse.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectReUseCondi.value.regirecvtype = doccate.value;
  selectReUse(1);
  currentPageselectReUse.value = 1;
}
// for selectReUse
const checkedListselectReUse = ref([])
const isChecked = (item) => {
  return checkedListselectReUse.value.some((i) => i.mgmtid === item.raw.mgmtid);
};

const toggleSelection = (item) => {
  const index = checkedListselectReUse.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedListselectReUse.value.splice(index, 1);
  } else {
    checkedListselectReUse.value.push({ mgmtid: item.raw.mgmtid, copyno: item.raw.copyno });
  }
};

const selectAll = ref([]);
const selectAllToggle = () => {
  if (selectAll.value.length >0) {
    checkedListselectReUse.value = selectReUseList.value
      .filter(item => !item.disabled)
      .map((item) => ({ mgmtid: item.mgmtid, copyno: item.copyno }));
  } else {
    checkedListselectReUse.value = [];
  }
};

const selectReUseList = ref([])
const selectReUseCnt = ref(0)
const selectReUseDefCondi = {
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
const selectReUseCondi = ref({ ...selectReUseDefCondi })
const selectReUseCondiCheck = ref({ value: { ...selectReUseDefCondi }, flag: { ...selectReUseDefCondi } })
/* ================ pagination ================ */
const selectReUseLoader = ref(true)
const totalPagesselectReUse = ref(0)
const currentPageselectReUse = ref(1)
const pageSizeselectReUse = ref(10)
const pageSizesselectReUse = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeselectReUse = () => {
  selectReUseCondi.value.pageSize = pageSizeselectReUse.value;
  currentPageselectReUse.value = 1;
  selectReUse(currentPageselectReUse.value);
}
const handlePageChangeselectReUse = () => {
  selectReUse(currentPageselectReUse.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)


onMounted(async () => {
  setCondiClear(selectReUseCondiCheck.value, selectReUseCondi.value);
  setQueries(route, selectReUseCondi.value, selectReUseDefCondi);
  await selectOtherDept();
  await selectReUse(selectReUseCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectReUseCondi.value, selectReUseDefCondi);
  await selectReUse(selectReUseCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectReUseCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectReUseCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectReUseCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectReUseCondiCheck.value, selectReUseCondi.value, selectReUseCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectReUseCondi.value["secttl"] = "";
  selectReUseCondi.value["authorname"] = "";
  selectReUseCondi.value["mgmtno"] = "";
  selectReUseCondi.value["copyno"] = "";
  setCondiChanged(selectReUseCondiCheck.value, selectReUseCondi.value, selectReUseCondi.value);
})

const staticColumnsselectReUse = [
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
  { key: "reusePossibleDate", title: "재사용만료일자", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqState", title: "재사용요청", width: "80px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ======================= multi selection ======================= */
const selectReUseIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.mgmtid}-${item.docid}-${item.userid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectReUseFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectReUseList.value.filter((item) => 
    checkedListselectReUse.value.includes(item.identifier))
  
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
  if(prevUserReqType.value == '타부서' && ['개인','부서'].includes(selectReUseCondi.value.userreqtype)){
    checkedListselectReUse.value = [];
  }
  selectReUse(1);
  currentPageselectReUse.value = 1;
}

const noDataText = ref("");
const selectReUse = async (pageNum) => {

  selectReUseLoader.value = true;
  selectReUseCondi.value.pageNum = parseInt(pageNum);
  selectReUseCondi.value.loginuserid = getUserLoginData.value.userid;

  if(selectReUseCondi.value.userreqtype == '부서'){
    selectReUseCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectReUseCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectReUseLoader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectReUse({ ...selectReUseCondi.value }, urlPaths.value);

    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      selectReUseLoader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u.indt = transformDate(u.indt);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      return { ...u};
    });

    selectReUseList.value = response.data.list;
    selectReUseCnt.value = response.data.total;

    selectReUseList.value.forEach(item=>{
      if(item.readauthexpiredt != null && item.reader != 'x'){ // 열람권한O
        const isReadAuthExpired  = dayjs(item.readauthexpiredt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
        if(isReadAuthExpired){ // 열람권한만료
          item.readPossible = "미보유";
          item.disabled = true;
        }else{
          item.readPossible = "보유";
          item.reusePossibleDate = item.grantreuseenddt;
          if(item.reuse.includes('승인')){
            const isReuseAuthExpired  = dayjs(item.grantreuseenddt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
            if(isReuseAuthExpired){ // 재사용권한만료
              item.reqState = '요청가능';
            }else{
              item.reqState = item.reuse;
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

    totalPagesselectReUse.value = response.data.pages;
    const allOption = pageSizesselectReUse.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectReUseLoader.value = false;

    setCondiClear(selectReUseCondiCheck.value, selectReUseCondi.value)
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
    selectReUseCondi.value.userreqtype = '타부서';
    selectReUseCondi.value.deptid = choicedDeptKey.value;
    prevUserReqType.value = selectReUseCondi.value.userreqtype;
    selectReUse(1);
    currentPageselectReUse.value = 1;
    checkedListselectReUse.value = [];
  }else{
    selectReUseCondi.value.userreqtype = '개인';
    updateCondi();
  }
};

const hasCopy = () => {
  const copy = checkedListselectReUse.value.some(item=>!item.copyno.includes("원"));
  return copy;
};

/* ============ 요청  버튼 Move FN ================ */
const moveToPageReuseReqs = () => {
  
  if (checkedListselectReUse.value.length == 0) {
    confirm("요청하실 항목을 선택해주세요.")
    return;
  }

  if(hasCopy()){
    alert("사본 문서는 재사용 요청이 불가능합니다.")
    return
  }
  const mgmtIdsString = checkedListselectReUse.value
    .map(item => item.mgmtid)
    .join(',');
  const queryString = {mgmtid: ""};
  queryString.mgmtid = mgmtIdsString;

  moveToBmsReusereq(queryString)
}

const moveToPageReuseReq = (row) => {
  if(!row.copyno.includes("원")){
    alert("사본 문서는 재사용 요청이 불가능합니다.")
    return
  }
  const param = {mgmtid: ""};
  param.mgmtid = row.mgmtid;
  moveToBmsReusereq(param);
};

const moveToBmsReusereq = (item) => {
  router.push({
    name: "BmsReusereq",
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
    const param = {docid: row.docid, frompage: 'BmsReUseReqList'};
    router.push({
      name: pathname,
      query: {
        ...param
      }
    });
  }
}
</script>