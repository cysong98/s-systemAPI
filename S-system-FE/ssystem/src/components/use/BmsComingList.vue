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
      <h4 class="h4">예고문도래목록</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">예고문도래목록</h5>
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
                  @keyup.enter="selectComingList(1)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>도래일</th>
              <td>
                <v-select
                  v-model="choicedDay"
                  :items="comingDays"
                  item-title="view"
                  item-value="key"
                  @update:modelValue="updateChoicedDay"
                  variant="outlined"
                  hide-details="auto"
                ></v-select>
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectComingList(1)">
          <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{selectComingListCnt}}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="moveReqs(1)">재분류,예고문변경요청</v-btn>
              <v-btn class="button" @click="moveReqs(2)">파기요청</v-btn>
              <v-btn class="button" @click="moveReqs(3)">이관대기요청</v-btn>
              <v-btn v-if="getUserLoginData.vgroupid.includes('5220')" class="button" @click="moveReqs(4)">이관연기요청</v-btn>
              <v-btn v-if="getUserLoginData.vgroupid.includes('5220')" class="button" @click="moveReqs(5)">내부이관요청</v-btn>
              <v-btn class="button" @click="moveReqs(6)">존안요청</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectComingList"
              :items="pageSizesSelectComingList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectComingList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          v-model="checkedListSelectComingList"
          :headers="staticColumnsselectComing"
          :items="selectComingListList"
          :items-per-page="pageSizeSelectComingList"
          :loading="loader"
          :item-value="selectComingIdentifier"
          show-select
          :no-data-text="noDataText"
          @click:row="(event, item, pathname) => moveDetailPage(event, item.item.raw, 'BmsDctmgmtregidetailcard')"
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
          <template v-slot:item.regirecvtype="{ item }">
            <span v-if="item.raw.regirecvtype == '1'">전자</span>
            <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
          </template>
          <template v-slot:item.etc="{ item }">
            <v-icon :color="dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').isBefore(dayjs(), 'day') ? 'red' : 'blue'">mdi-file-cancel-outline</v-icon>
            <span>
                {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').isBefore(dayjs(), 'day') ? '만료' : '도래' }}
                {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') }}
                {{ item.raw.copyno.includes("원") ? transReclassInfo(item.raw.orgnreclass) : transReclassInfo(item.raw.copyreclass) }}
            </span>
          </template>
          <!-- <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template> -->
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectComingList"
          :length="totalPagesSelectComingList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectComingList"
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
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
import { transformSeclevel, transformDate, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "@/components/dct/DctHistoryCardPopup.vue";

const name = ref('BmsComingList')
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
];
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectComingListCondi.value.kind = dockinde.value;
  selectComingList(1);
  currentPageSelectComingList.value = 1;
}
const doccate = ref("")
const doccateData = ref([
  { view: '전체', key: ''},
  { view: '전자', key: '1'},
  { view: '비전자', key: '2'},
])
const cateSearch = () => {
  selectComingListCondi.value.regirecvtype = doccate.value;
  selectComingList(1);
  currentPageSelectComingList.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectComingListCondi.value.seclevel = docseclevel.value;
  selectComingList(1);
  currentPageSelectComingList.value = 1;
}
// for selectComingList
const loader = ref(true)
const checkedListSelectComingList = ref([])
const isChecked = (item) => {
  return checkedListSelectComingList.value.some((i) => i.mgmtid === item.raw.mgmtid);
};
const toggleSelection = (item) => {
  const index = checkedListSelectComingList.value.findIndex((i) => i.mgmtid === item.raw.mgmtid);
  if (index !== -1) {
    checkedListSelectComingList.value.splice(index, 1);
  } else {
    checkedListSelectComingList.value.push({ 
      mgmtid: item.raw.mgmtid,
      copyno: item.raw.copyno,
      reqState: item.raw.reqState,
      orgnreclass: item.raw.orgnreclass,
      copyreclass: item.raw.copyreclass,
      indt: item.raw.indt,
      protdt: item.raw.protdt,
      seclevel: item.raw.seclevel
    });
  }
};

const selectComingListList = ref([])
const selectComingListCnt = ref(0)
const selectComingListDefCondi = {
  mgmtno: "",
  secttl: "",
  copyno: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "mgmtid"
}
const selectComingListCondi = ref({ ...selectComingListDefCondi })
const selectComingListCondiCheck = ref({ value: { ...selectComingListDefCondi }, flag: { ...selectComingListDefCondi } })
/* ================ pagination ================ */
const totalPagesSelectComingList = ref(0)
const currentPageSelectComingList = ref(1)
const pageSizeSelectComingList = ref(10)
const pageSizesSelectComingList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectComingList = () => {
  selectComingListCondi.value.pageSize = pageSizeSelectComingList.value;
  currentPageSelectComingList.value = 1;
  selectComingList(currentPageSelectComingList.value);
}
const handlePageChangeSelectComingList = () => {
  selectComingList(currentPageSelectComingList.value);
}
/* ============================================== */

/* ================= 날짜 선택 ================= */
const choicedDay = ref("3");
const comingDays = ref([
  {view:"30일 이내 도래", key: "3"},
  {view:"15일 이내 도래", key: "2"},
  {view:"7일 이내 도래", key: "1"},
  {view:"만료된 예고문", key: "0"},
  {view:"전체", key: "4"},
]);
const updateChoicedDay = () => {
  selectComingListCondi.value.choicedDay = choicedDay.value;
  selectComingList(1);
  currentPageSelectComingList.value = 1;
};
/* ============================================== */

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectComingListCondiCheck.value, selectComingListCondi.value);
  setQueries(route, selectComingListCondi.value, selectComingListDefCondi);
  await selectComingList(selectComingListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectComingListCondi.value, selectComingListDefCondi);
  await selectComingList(selectComingListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectComingListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectComingListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectComingListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectComingListCondiCheck.value, selectComingListCondi.value, selectComingListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectComingListCondi.value["mgmtno"] = "";
    selectComingListCondi.value["secttl"] = "";
    selectComingListCondi.value["copyno"] = "";
  setCondiChanged(selectComingListCondiCheck.value, selectComingListCondi.value, selectComingListCondi.value);
})

watchEffect(() => {
})


const staticColumnsselectComing = [
  { key: "kind", title: "유형", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvtype", title: "종류", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "비밀등급", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "250px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "140px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "etc", title: "도래일", width: "180px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqState", title: "상태", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  // { title: "이력", key: "histBtn", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

/* ======================= multi selection ======================= */
const selectComingIdentifier = computed(() => (item) => {
  item.identifier = `${item.mgmtid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectComingFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectComingListList.value.filter((item) => 
  checkedListSelectComingList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */


const noDataText = ref("");
const selectComingList = async (pageNum) => {

  selectComingListCondi.value.pageNum = parseInt(pageNum);
  selectComingListCondi.value.loginuserid = getUserLoginData.value.userid;
  selectComingListCondi.value.loginuserdeptid = getUserLoginData.value.deptid;
  selectComingListCondi.value.choicedDay = choicedDay.value;
  const vgroupidArr = getUserLoginData.value.vgroupid.split(',').map(value => value.trim());
  if (vgroupidArr.length > 0 && vgroupidArr.includes('5220')) {
    selectComingListCondi.value.vgroupid = '5220';
  }else{
    selectComingListCondi.value.vgroupid = getUserLoginData.value.vgroupid;
  }
  loader.value = true;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectComingListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  try {
    const response = await API.useAPI.selectComingList({ ...selectComingListCondi.value }, urlPaths.value);
    
    if(response.data.list.length == 0){
      noDataText.value = "조건에 맞는 데이터가 없습니다."
      loader.value = false;
    }

    response.data.list = response.data.list.map((u) => {
      u.indt = transformDate(u.indt);
      u.seclevel = transformSeclevel(u.seclevel);
      u.kind = u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서';
      const reqState = transformSecstatus(u.secstatus);
      u.reqState = reqState;
      if (reqState === "" || reqState === '인수완료') {
        u.disabled = false;
      } else {
        const isTransferManager  = getUserLoginData.value.vgroupid.includes('5220');
        const isTransferringState = ['이관대기', '이관연기', '이관연기요청중', '이관연기승인', '내부이관요청중', '내부이관승인'].includes(reqState);

        if (isTransferringState) {
          if (['이관대기', '이관연기'].includes(reqState) && isTransferManager) {
            u.disabled = false;
          }else{
            u.disabled = true;
          }
        } else {
          u.disabled = true;
        }
  }
      return { ...u };
    });

    selectComingListList.value = response.data.list;
    selectComingListCnt.value = response.data.total;
    totalPagesSelectComingList.value = response.data.pages;
    const allOption = pageSizesSelectComingList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    loader.value = false;

    setCondiClear(selectComingListCondiCheck.value, selectComingListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
  loader.value = false;
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


// 파기요청 가능여부 체크
const isPossibleDestruct = () => {
  const isIncludeOrign = checkedListSelectComingList.value.some(item => item.copyno.includes("원"));
  if (isIncludeOrign) {
    alert("원본 문서는 파기요청이 불가능합니다.");
    clearCheckedList();
    return false;
  }
  const isAllReclassDestruct = checkedListSelectComingList.value.every(item => item.copyreclass == '23');
  if (!isAllReclassDestruct) {
    alert("예고문이 파기인 경우에만 파기요청이 가능합니다. \n재분류를 검토하여주십시오.");
    clearCheckedList();
    return false;
  }
  return true;
};


// 존안요청 가능여부 체크
const isPossibleCnsrv = () => {
  const isIncludeOrign = checkedListSelectComingList.value.some(item => item.copyno.includes("원"));
  if (isIncludeOrign) {
    alert("원본 문서는 존안요청이 불가능합니다.");
    clearCheckedList();
    return false;
  }
  const isAllReclassCnsrv = checkedListSelectComingList.value.every(item => item.copyreclass == '20');
  if (!isAllReclassCnsrv) {
    alert("예고문이 존안인 경우에만 존안요청이 가능합니다. \n재분류를 검토하여주십시오.");
    clearCheckedList();
    return false;
  }
  return true;
};


// 이관대기요청 가능여부 체크
const isPossibleTransWait = () => {
  // 일반문서인지
  const isAllGeneral = checkedListSelectComingList.value.every(item => item.seclevel == '일반');
  // 비밀생산 후 30년이 경과됐는지
  const isAll30YearsAgo = checkedListSelectComingList.value.every(item => {
    const indt = dayjs(item.indt, { format: 'YYYYMMDDHHmmss' });
    const thirtyYearsAgo = dayjs().diff(indt, 'year') >= 30;
    return thirtyYearsAgo;
  });
  // 보호기간 만료 & 비밀문서의 예고문이 이관인지
  const isAllReclassTrans = checkedListSelectComingList.value.every(item => 
    item.orgnreclass === '22' && 
    ['II급', 'III급'].includes(item.seclevel) &&
    dayjs(item.protdt, { format: 'YYYYMMDDHHmmss' }).isBefore(dayjs(), 'day')
  );

  if (isAllGeneral || isAll30YearsAgo || isAllReclassTrans) {
    return true;
  }

  alert("이관대기요청은 다음과 같은 경우에 가능합니다. \n\n1. 비밀이 일반등급인 경우 \n2. 비밀 생산 후 30년 경과된 경우 \n3. 예고문이 이관이며 보호기간이 만료된 경우");
  clearCheckedList();
  return false;
};


const isTransWaitInclude = () => {
  const include = checkedListSelectComingList.value.some(item=>item.reqState.includes("이관대기"));
  return include;
};

const isTransDelayInclude = () => {
  const include = checkedListSelectComingList.value.some(item=>item.reqState.includes("이관연기"));
  return include;
};

const isTransWaitApprovedAll = () => {
  const approved = checkedListSelectComingList.value.every(item=>item.reqState === '이관대기');
  return approved;
};

const isTransDelayApprovedAll = () => {
  const approved = checkedListSelectComingList.value.every(item=>item.reqState === '이관연기');
  return approved;
};

const clearCheckedList = () => {
  checkedListSelectComingList.value = []
};

const moveReqs = async (val) => {
  
  if (checkedListSelectComingList.value.length == 0) {
    confirm("요청하실 항목을 선택해주세요.")
    return;
  }

  const mgmtIdsString = checkedListSelectComingList.value
    .map(item => item.mgmtid)
    .join(',');
  const queryString = {mgmtid: ""};
  queryString.mgmtid = mgmtIdsString;

  switch(val){
    case 1: // 재분류
      if(isTransWaitApprovedAll()){
        alert("이관연기 또는 내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isTransDelayApprovedAll()){
        alert("내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isTransWaitInclude() || isTransDelayInclude()){
        alert("이관대기 또는 이관연기 상태의 문서는 재분류요청을 하실 수 없습니다.");
        clearCheckedList();
        return;
      }else{
        moveToBmsComingreclassdetail(queryString);
      }
      break;
    case 2: // 파기
      if(isTransWaitApprovedAll()){
        alert("이관연기 또는 내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isTransDelayApprovedAll()){
        alert("내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isPossibleDestruct()){
        moveToBmsComingdestructdetail(queryString);
      }
      break;
    case 3: // 이관대기
      if(isTransWaitInclude() || isTransDelayInclude()){
        alert("이미 이관대기요청을 하신 문서가 포함되어 있습니다.");
        clearCheckedList();
        return;
      }else if(isPossibleTransWait()){
        moveToBmsComingtranswait(queryString);
      }
      break;
    case 4: // 이관연기
      if(isTransWaitApprovedAll()){
        moveToBmsComingtransdelay(queryString);
      }else if(isTransDelayInclude()){
        alert("이미 이관연기요청을 하신 문서가 포함되어 있습니다.");
        clearCheckedList();
        return;
      }else{
        alert("이관대기요청이 승인완료된 문서만 이관연기요청이 가능합니다.");
        clearCheckedList();
        return;
      }
      break;
    case 5: // 내부이관
      if(isTransWaitApprovedAll() || isTransDelayApprovedAll()){
        moveToBmsComingtransdetail(queryString);
      }else{
        alert("이관대기요청이 승인완료된 문서만 내부이관요청이 가능합니다.");
        clearCheckedList();
        return;
      }
      break;
    case 6: // 존안
    if(isTransWaitApprovedAll()){
        alert("이관연기 또는 내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isTransDelayApprovedAll()){
        alert("내부이관 요청을 해주세요.");
        clearCheckedList();
        return;
      }else if(isPossibleCnsrv()){
        moveToBmsComingcnsrvdetail(queryString);
      }
      break;
  }

}

// 재분류,예고문 요청
const moveToBmsComingreclassdetail = (row) => {
  router.push({
    name: "BmsComingreclassdetail",
    query: {
      ...row
    }
  });
}

// 파기요청
const moveToBmsComingdestructdetail = (row) => {
  router.push({
    name: "BmsComingdestructdetail",
    query: {
      ...row
    }
  });
}

// 이관대기요청
const moveToBmsComingtranswait = (row) => {
  router.push({
    name: "BmsComingtranswait",
    query: {
      ...row
    }
  });
}

// 이관연기요청
const moveToBmsComingtransdelay = (row) => {
  router.push({
    name: "BmsComingtransdelay",
    query: {
      ...row
    }
  });
}

// 이관요청
const moveToBmsComingtransdetail = (row) => {
  router.push({
    name: "BmsComingtransdetail",
    query: {
      ...row
    }
  });
}

// 존안요청
const moveToBmsComingcnsrvdetail = (row) => {
  router.push({
    name: "BmsComingcnsrvdetail",
    query: {
      ...row
    }
  });
}

// 로우클릭
const moveDetailPage = (event, row, pathname) => {
  const id = event.target.id;
  if (id != "") {
    return;
  }
  if(row.regirecvtype == '2'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  const param = {docid: row.docid, frompage: "BmsCominglist"};
  router.push({
    name: pathname,
    query: {
      ...param
    }
  });
}

</script>


<style lang="scss">
</style>

