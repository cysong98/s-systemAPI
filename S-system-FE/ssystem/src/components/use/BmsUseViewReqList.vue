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
          <h5 class="h5">열람요청</h5>
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
                  @keyup.enter="selectUseDigitalView(1)"
                  variant="outlined"
                  hide-details="auto"
                  />
              </td>
              <th>등록기간</th>
              <td>
                <v-text-field
                  v-model="selectUseDigitalViewCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto" />
                ~
                <v-text-field
                  v-model="selectUseDigitalViewCondi.endDt"
                  type="date"
                  :min="selectUseDigitalViewCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>구분</th>
              <td class="width-150">
                <v-radio-group  v-model="selectUseDigitalViewCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
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
          <v-btn class="magnify-solid" @click="selectUseDigitalView(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectUseDigitalViewCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveToPageViewReqs()">일괄요청</v-btn>
            </div>
            <v-select
              v-model="pageSizeselectUseDigitalView"
              :items="pageSizesselectUseDigitalView"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeselectUseDigitalView"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          :headers="staticColumnsselectUseDigitalView"
          :items="selectUseDigitalViewList"
          :items-per-page="pageSizeselectUseDigitalView"
          :loading="selectUseDigitalViewLoader"
          :item-value="selectUseDigitalViewIdentifier"
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
            @click.stop="moveToPageViewReq(item.raw.mgmtid, item.raw.regirecvtype)"
            color="#35447F"
            >요청</v-btn
            >
            <span v-else>{{ item.raw.reqState }}</span>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageselectUseDigitalView"
          :length="totalPagesselectUseDigitalView"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeselectUseDigitalView"
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

const name = ref('BmsUseViewReqList')
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
  selectUseDigitalViewCondi.value.kind = dockinde.value;
  selectUseDigitalView(1);
  currentPageselectUseDigitalView.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectUseDigitalViewCondi.value.regirecvtype = doccate.value;
  selectUseDigitalView(1);
  currentPageselectUseDigitalView.value = 1;
}
// for selectUseDigitalView
const checkedListselectUseDigitalView = ref([])
const isChecked = (item) => {
  return checkedListselectUseDigitalView.value.some((i) => i.mgmtid === item.raw.mgmtid);
};


let selectedRegirecvtype = null;
const toggleSelection = (item) => {

  if (checkedListselectUseDigitalView.value.length === 0) {
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

  const index = checkedListselectUseDigitalView.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedListselectUseDigitalView.value.splice(index, 1);
  } else {
    checkedListselectUseDigitalView.value.push({ mgmtid: item.raw.mgmtid, regirecvtype: item.raw.regirecvtype });
    selectedRegirecvtype = item.raw.regirecvtype;
  }
};

const selectUseDigitalViewList = ref([])
const selectUseDigitalViewCnt = ref(0)
const selectUseDigitalViewDefCondi = {
  userreqtype: "부서",
  secttl: "",
  authorname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "mgmtid"
}
const selectUseDigitalViewCondi = ref({ ...selectUseDigitalViewDefCondi })
const selectUseDigitalViewCondiCheck = ref({ value: { ...selectUseDigitalViewDefCondi }, flag: { ...selectUseDigitalViewDefCondi } })
/* ================ pagination ================ */
const selectUseDigitalViewLoader = ref(true)
const totalPagesselectUseDigitalView = ref(0)
const currentPageselectUseDigitalView = ref(1)
const pageSizeselectUseDigitalView = ref(10)
const pageSizesselectUseDigitalView = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeselectUseDigitalView = () => {
  selectUseDigitalViewCondi.value.pageSize = pageSizeselectUseDigitalView.value;
  currentPageselectUseDigitalView.value = 1;
  selectUseDigitalView(currentPageselectUseDigitalView.value);
}
const handlePageChangeselectUseDigitalView = () => {
  selectUseDigitalView(currentPageselectUseDigitalView.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectUseDigitalViewCondiCheck.value, selectUseDigitalViewCondi.value);
  setQueries(route, selectUseDigitalViewCondi.value, selectUseDigitalViewDefCondi);
  await selectOtherDept();
  await selectUseDigitalView(selectUseDigitalViewCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectUseDigitalViewCondi.value, selectUseDigitalViewDefCondi);
  await selectUseDigitalView(selectUseDigitalViewCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUseDigitalViewCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUseDigitalViewCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUseDigitalViewCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUseDigitalViewCondiCheck.value, selectUseDigitalViewCondi.value, selectUseDigitalViewCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectUseDigitalViewCondi.value["secttl"] = "";
  selectUseDigitalViewCondi.value["authorname"] = "";
  selectUseDigitalViewCondi.value["mgmtno"] = "";
  selectUseDigitalViewCondi.value["copyno"] = "";
  setCondiChanged(selectUseDigitalViewCondiCheck.value, selectUseDigitalViewCondi.value, selectUseDigitalViewCondi.value);
})



const staticColumnsselectUseDigitalView = [
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
  { key: "reqState", title: "열람요청", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ======================= multi selection ======================= */
const selectUseDigitalViewIdentifier = computed(() => (item) => {
  item.identifier = `${item.mgmtid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectUseDigitalViewFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectUseDigitalViewList.value.filter((item) => 
    checkedListselectUseDigitalView.value.includes(item.identifier))
  
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
  if(prevUserReqType.value == '타부서' && ['개인','부서'].includes(selectUseDigitalViewCondi.value.userreqtype)){
    checkedListselectUseDigitalView.value = [];
  }
  selectUseDigitalView(1);
  currentPageselectUseDigitalView.value = 1;
}

const noDataText = ref("");
const selectUseDigitalView = async (pageNum) => {

  selectUseDigitalViewLoader.value = true;
  selectUseDigitalViewCondi.value.pageNum = parseInt(pageNum);
  selectUseDigitalViewCondi.value.loginuserid = getUserLoginData.value.userid;

  if(selectUseDigitalViewCondi.value.userreqtype == '부서'){
    selectUseDigitalViewCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
  if(validCheckSearchInput(comboInputData.value))
    selectUseDigitalViewCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectUseDigitalViewLoader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectUseDigitalView({ ...selectUseDigitalViewCondi.value }, urlPaths.value);

    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      selectUseDigitalViewLoader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u.indt = transformDate(u.indt);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      return { ...u};
    });

    selectUseDigitalViewList.value = response.data.list;
    selectUseDigitalViewCnt.value = response.data.total;

    selectUseDigitalViewList.value.forEach(item=>{
      if(item.readauthexpiredt != null && item.reader != 'x'){ // 권한O
        const isReadAuthExpired  = dayjs(item.readauthexpiredt, { format: 'YYYY.MM.DD' }).isBefore(dayjs(), 'day');
        if(isReadAuthExpired){ // 권한만료
          item.readPossible = "미보유";
          if(item.read == '승인대기'){
            item.reqState = item.read;
            item.disabled = true;
          }else{
            item.reqState = '요청가능';
          }
        }else{
          item.readPossible = "보유";
          item.readPossibleDate = item.readauthexpiredt;
          item.disabled = true;
          item.reqState = '승인';
        }
        item.readPossibleDate = item.readauthexpiredt;
      }else{ // 권한X
        item.readPossible = "미보유";
        if(item.read.includes('승인')){
          item.reqState = item.read;
          item.disabled = true;
        }else{
          item.reqState = '요청가능';
        }
      }
    });

    totalPagesselectUseDigitalView.value = response.data.pages;
    const allOption = pageSizesselectUseDigitalView.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectUseDigitalViewLoader.value = false;

    setCondiClear(selectUseDigitalViewCondiCheck.value, selectUseDigitalViewCondi.value)
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
    selectUseDigitalViewCondi.value.userreqtype = '타부서';
    selectUseDigitalViewCondi.value.deptid = choicedDeptKey.value;
    prevUserReqType.value = selectUseDigitalViewCondi.value.userreqtype;
    selectUseDigitalView(1);
    currentPageselectUseDigitalView.value = 1;
    checkedListselectUseDigitalView.value = [];
  }else{
    selectUseDigitalViewCondi.value.userreqtype = '부서';
    updateCondi();
  }
};

/* ============ 요청  버튼 Move FN ================ */

// 일괄 요청
const moveToPageViewReqs = () => {
  
  if (checkedListselectUseDigitalView.value.length == 0) {
    confirm("요청하실 항목을 선택해주세요.")
    return;
  }

  const mgmtIdsString = checkedListselectUseDigitalView.value
    .map(item => item.mgmtid)
    .join(',');
  const queryString = {mgmtid: ""};
  queryString.mgmtid = mgmtIdsString;

  if(checkedListselectUseDigitalView.value.some((i) => i.regirecvtype === '전자')){
    moveToBmsUsedigitalviewreq(queryString);
  }else if (checkedListselectUseDigitalView.value.some((i) => i.regirecvtype === '비전자')){
    moveToBmsUsenondigitalviewreq(queryString);
  }
  
}

// 낱개 요청
const moveToPageViewReq = (paramMgmtid, paramRegirecvtype) => {
  const param = {mgmtid: ""};
  param.mgmtid = paramMgmtid;

  if(paramRegirecvtype == '전자'){
    moveToBmsUsedigitalviewreq(param);
  }else if(paramRegirecvtype == '비전자'){
    moveToBmsUsenondigitalviewreq(param);
  }
};


// 전자열람 요청서
const moveToBmsUsedigitalviewreq = (item) => {
  router.push({
    name: "BmsUsedigitalviewreq",
    query: {
      ...item,
    },
  });
};

// 비전자열람 요청서
const moveToBmsUsenondigitalviewreq = (item) => {
  router.push({
    name: "BmsUsenondigitalviewreq",
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
  }else if(row.readPossible == '보유'){
    if(row.regirecvtype == '비전자'){
      if(row.paperrecgubun == '접수'){
        pathname = 'BmsDctReceivedOfflinecompletecard';
      }else{
        pathname = 'BmsDctProductOfflinecompletecard';
      }
    }
    const param = {docid: row.docid, frompage: 'BmsUseViewReqList'};
    router.push({
      name: pathname,
      query: {
        ...param
      }
    });
  }
}
</script>