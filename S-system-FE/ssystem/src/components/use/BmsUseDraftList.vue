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
      <h4 class="h4">비밀문서사용</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">기안한요청서</h5>
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
                  v-if="comboSelected === 'reqgubun'"
                  v-model="reqGubun"
                  :items="reqGubunData"
                  @update:modelValue="gubunSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-select
                  v-else-if="comboSelected === 'reqstate'"
                  v-model="reqState"
                  :items="reqStateData"
                  @update:modelValue="stateSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field v-else
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectUseReqList(1)"
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th>요청기간</th>
              <td>
                <v-text-field
                  v-model="selectUseReqListCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
                ~
                <v-text-field
                  v-model="selectUseReqListCondi.endDt"
                  type="date"
                  :min="selectUseReqListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectUseReqList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectUseReqListCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="deleteUseReqList()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectUseReqList"
              :items="pageSizesSelectUseReqList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectUseReqList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          :headers="staticColumnsSelectUseReqList"
          :items="selectUseReqListList"
          :items-per-page="pageSizeSelectUseReqList"
          :loading="loader"
          :item-value="listIdentifier"
          class="table-type-02"
          hover
        >
        <template v-slot:item="{ item }">
          <tr :style="{ 'text-decoration': item.raw.reqstatus === '4' || item.raw.reqstatus === '5' || item.raw.reqstatusnm === '반려'? 'line-through red' : 'none' }" 
          @click="moveToBmsUseReqdetail($event, item.raw)" style="cursor: pointer;">
            <td><v-checkbox class="req-del-chk" :disabled="!['4', '5'].includes(item.raw.reqstatus) && !(item.raw.reqstatus === '2' && item.raw.reqstatusnm === '반려')"
                  v-model="checkedItems[item.raw.apprreqid]" @update:modelValue="newValue => chkForDelete(newValue, item.raw)"></v-checkbox></td>
            <td>{{ item.raw.reqtypenm }}</td>
            <td>{{ item.raw.reqdt }}</td>
            <td :class="{ 'text-left': item }">{{ item.raw.reqttl }}</td>
            <td>{{ item.raw.apprname }}</td>
            <td>{{ item.raw.apprdutyname }}</td>
            <td>{{ item.raw.apprdeptname }}</td>
            <td :class="{ 'text-left': item }">{{ item.raw.appropinion }}</td>
            <td>{{ item.raw.updtdt }}</td>
            <td>{{ item.raw.reqstatusnm }}</td>
          </tr>
        </template>
        <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectUseReqList"
          :length="totalPagesSelectUseReqList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectUseReqList"
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
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api'; 

import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('BmsUseDraftList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("reqttl")
const comboInputData = ref("")
const comboData = [
  { view: "구분", key: "reqgubun"},
  { view: "제목", key: "reqttl"},
  { view: "상태", key: "reqstate"}
]
const reqGubun = ref("")
const reqGubunData = ref([
  { view: '전체', key: ''},
  { view: '열람', key: '01'},
  { view: '재사용', key: '02'},
  { view: '인쇄', key: '03'},
  { view: '대출', key: '04'},
  { view: '지출', key: '05'},
  { view: '재분류', key: '06'},
  { view: '파기', key: '07'},
  { view: '존안', key: '08'},
  { view: '이관대기', key: '10'},
  { view: '이관연기', key: '11'},
  { view: '내부이관', key: '12'},
])
const gubunSearch = () => {
  selectUseReqListCondi.value.reqtype = reqGubun.value;
  selectUseReqList(1);
}
const reqState = ref("")
const reqStateData = ref([
  { view: '전체', key: ''},
  { view: '결재중', key: '1'},
  { view: '결재완료(승인/반려)', key: '2'},
  { view: '실행완료', key: '3'},
  { view: '권한회수', key: '4'},
  { view: '요청서회수', key: '5'},
  { view: '반출실행', key: '7'},
  { view: '반납요청', key: '8'},
  { view: '반납완료', key: '9'},
])
const stateSearch = () => {
  selectUseReqListCondi.value.reqstatus = reqState.value;
  selectUseReqList(1);
}
// for selectUseReqList
const loader = ref(true)
const listIdentifier = computed(() => (item) => {
  item.identifier = `${item.apprreqid}`;
  return item.identifier;
});
const checkedListDeleteUseReqList = ref([])
const selectUseReqListList = ref([])
const selectUseReqListCnt = ref(0)
const selectUseReqListDefCondi = {
  apprgubun: "",
  reqttl: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "DESC",
  sortItem: "reqdt"
}
const selectUseReqListCondi = ref({ ...selectUseReqListDefCondi })
const selectUseReqListCondiCheck = ref({ value: { ...selectUseReqListDefCondi }, flag: { ...selectUseReqListDefCondi } })
/* ================ pagination ================ */
const totalPagesSelectUseReqList = ref(0)
const currentPageSelectUseReqList = ref(1)
const pageSizeSelectUseReqList = ref(10)
const pageSizesSelectUseReqList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])
//콤보 페이징갯수 선택 후 리스트 재조회
const handlePageSizeChangeSelectUseReqList = () => {
  selectUseReqListCondi.value.pageSize = pageSizeSelectUseReqList.value;
  currentPageSelectUseReqList.value = 1;
  selectUseReqList(currentPageSelectUseReqList.value);
}
//페이징 번호 선택 후 리스트 재조회
const handlePageChangeSelectUseReqList = () => {
  selectUseReqList(currentPageSelectUseReqList.value);
}
/* ============================================== */


onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectUseReqListCondiCheck.value, selectUseReqListCondi.value);
  setQueries(route, selectUseReqListCondi.value, selectUseReqListDefCondi);
  await selectUseReqList(selectUseReqListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectUseReqListCondi.value, selectUseReqListDefCondi);
  await selectUseReqList(selectUseReqListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUseReqListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUseReqListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUseReqListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUseReqListCondiCheck.value, selectUseReqListCondi.value, selectUseReqListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectUseReqListCondi.value["apprgubun"] = "";
    selectUseReqListCondi.value["reqttl"] = "";
  setCondiChanged(selectUseReqListCondiCheck.value, selectUseReqListCondi.value, selectUseReqListCondi.value);
})

watchEffect(() => {
})

const checkedItems = ref({});
const chkForDelete = (newValue, item) => {
  const data = { apprreqid: "", reqtype: "" };
  if (newValue === true) {
    data.apprreqid = item.apprreqid;
    data.reqtype = item.reqtype;
    checkedListDeleteUseReqList.value.push(data);
  } else {
    const idx = checkedListDeleteUseReqList.value.findIndex((chked) => chked.apprreqid === item.apprreqid);
    if (idx !== -1) {
      checkedListDeleteUseReqList.value.splice(idx, 1);
    }
  }
};

const deleteUseReqList = async() => {
  if(checkedListDeleteUseReqList.value.length < 1){
    alert("삭제하실 요청서를 선택해주세요.");
    return;
  }

  if (!confirm("삭제하시겠습니까?")) {
    return;
  }

  await API.useAPI
    .deleteUseReqList(checkedListDeleteUseReqList.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("정상 삭제되었습니다");
        checkedListDeleteUseReqList.value = [];
        selectUseReqList(1);
      }
    })
    .catch(error => {
      console.log(error)
      alert("Server Error");
    });
}

const staticColumnsSelectUseReqList = [
  { key: "checkedItem", title: "선택", width: "20px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqtypenm", title: "구분", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqdt", title: "요청일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprname", title: "결재자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprdutyname", title: "직위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprdeptname", title: "부서", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "appropinion", title: "처리의견",  idth: "150px",sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "updtdt", title: "처리일시",  width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqstatusnm", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


const selectUseReqList = async (pageNum) => {

  selectUseReqListCondi.value.reqid = getUserLoginData.value.userid;
  
  selectUseReqListCondi.value.pageNum = parseInt(pageNum);
  selectUseReqListCondi.value.pagereqtype = "draft";
  loader.value = true;    

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectUseReqListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectUseReqList({ ...selectUseReqListCondi.value }, urlPaths.value);
    selectUseReqListList.value = response.data.list;
    selectUseReqListCnt.value = response.data.total;
    totalPagesSelectUseReqList.value = response.data.pages;
    const allOption = pageSizesSelectUseReqList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    loader.value = false;

    setCondiClear(selectUseReqListCondiCheck.value, selectUseReqListCondi.value)
  } catch (error) {
    alert("Server Error");
    console.log(error);
  }
  loader.value = false;
}

// Move Function
const moveToBmsUseReqdetail = ($event, row) => {
  const id = event.target.id;
  if (id != "") { 
    return;
  }

  let reqtype = row.reqtype;
  let moveName = '';
  if(reqtype === '01'){
    moveName = 'BmsUsereqviewdetail'; // 열람
  }else if(reqtype === '02'){
    moveName = 'BmsUsereqrecycledetail'; // 재사용
  }else if(reqtype === '03'){
    moveName = 'BmsUsereqprintdetail'; // 인쇄
  }else if(reqtype === '04' || reqtype === '05'){ // 반출(대출, 지출)
    moveName = 'BmsUsereqlenddetail'; 
  }else if(reqtype === '06'){ // 재분류
    moveName = 'BmsUsereqreclassdetail';
  }else if(reqtype === '07'){ // 파기
    moveName = 'BmsUsereqdestructdetail';
  }else if(reqtype === '08'){ // 존안
    moveName = 'BmsUsereqcnsrvdetail';
  }else if(reqtype === '10'){ // 이관대기
    moveName = 'BmsUsereqtranswaitdetail';
  }else if(reqtype === '11' || reqtype === '12' ){ // 이관연기, 내부이관
    moveName = 'BmsUsereqtransdetail';
  }else{
    return;
  }
  const param = {apprreqid: "",reqtype:"",frompage:"BmsUsedraftlist", reqstatusnm: ""};
  param.apprreqid = row.apprreqid;
  param.reqtype = row.reqtype;
  param.reqstatusnm = row.reqstatusnm;
  
  router.push({
    name: moveName,
    query: {
      ...param
    }
  });
}
  
  </script>

<style lang="scss" scoped>
.req-del-chk {
  display: flex;
  justify-content: center;
  margin-left: -30px;
  margin-top: 5px;
  // height: 20px;
}
</style>