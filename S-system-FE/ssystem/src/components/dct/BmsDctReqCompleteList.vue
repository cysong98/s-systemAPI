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
      <h4 class="h4">기안</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">기안완료함</h5>
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
                  v-if="comboSelected === 'docstate'"
                  v-model="docstate"
                  :items="docstateData"
                  @update:modelValue="statusSearch"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-select
                  v-else-if="comboSelected === 'kind'"
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
                <v-text-field v-else
                  v-model="comboInputData"
                  required
                  clearable
                  @keyup.enter="selectReqCompleteList(1)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>보고기간</th>
              <td>
                <v-text-field
                  v-model="selectReqCompleteListCondi.startDt"
                  type="date"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
                ~
                <v-text-field
                  v-model="selectReqCompleteListCondi.endDt"
                  type="date"
                  :min="selectReqCompleteListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto"
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectReqCompleteList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ selectReqCompleteListCnt }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn @click="rejectedDocumentToTempSave()">일괄반려삭제</v-btn>
              <!-- <v-btn @click="deleteDraftCompleteList()">삭제</v-btn> -->
            </div>
            <v-select
              v-model="pageSizeSelectReqCompleteList"
              :items="pageSizesSelectReqCompleteList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectReqCompleteList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          v-model="checkedListSelectReqCompleteList"
          :headers="staticColumnsSelectReqCompleteList"
          :items="selectReqCompleteListList"
          :items-per-page="pageSizeSelectReqCompleteList"
          :loading="selectReqCompleteListLoader"
          :item-value="selectReqCompleteListIdentifier"
          class="table-type-02 hide-header-checkbox"
          show-select
          @click:row="(event, item) => moveDetailPage(event, item.item.raw, 'BmsDctreqcompletecard')"
          hover
          >
          <template v-slot:item.data-table-select="{ item}">
            <v-checkbox
              v-if="item.raw.disabled == true"
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
          <template v-slot:item.docttl="{ item }">
            <div :class="{ 'text-left-02': item }">
              <v-chip density="comfort" v-if="transformEmergency(item.raw.emergency) === '지급'" color="rgba(194, 188, 16, 1)">{{ transformEmergency(item.raw.emergency) }}</v-chip>
              <v-chip density="comfort" v-if="transformEmergency(item.raw.emergency) === '긴급'" color="orange">{{ transformEmergency(item.raw.emergency) }}</v-chip>
              <v-chip density="comfort" v-if="transformEmergency(item.raw.emergency) === '초긴급'" color="red">{{ transformEmergency(item.raw.emergency) }}</v-chip>
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:item.histBtn="{ item }">
            <v-btn class="magnify" @click.stop="histBtnClick(item.raw.docid)">
              <v-icon></v-icon>
            </v-btn>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectReqCompleteList"
          :length="totalPagesSelectReqCompleteList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectReqCompleteList"
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
import { transformStatus,transformState, transformAsktype, transformEmergency } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctReqCompleteList')
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
  { view: '유형', key: 'kind'},
  { view: '구분', key: 'acttype'},
  { view: '문서번호', key: 'docno' },
  { view: '제목', key: 'docttl'},
  { view: '상태', key: 'docstate'},
]
const docstate = ref("")
const docstateData = ref([
  { view: '전체', key: ''},
  { view: '처리중', key: 'DRDS2'},
  { view: '종료', key: 'DRDS3'},
  { view: '발송대기', key: 'DRDS4'},
  { view: '발송안함', key: 'DRDS5'},
  { view: '반송', key: 'DRDS6'},
  // { view: '예약발송', key: 'DRDS7'},
  { view: '발송완료', key: 'DRDS8'},
])
const statusSearch = () => {
  selectReqCompleteListCondi.value.state = docstate.value;
  selectReqCompleteList(1);
  currentPageSelectReqCompleteList.value = 1;
}
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectReqCompleteListCondi.value.kind = dockinde.value;
  selectReqCompleteList(1);
  currentPageSelectReqCompleteList.value = 1;
}
const docacttype = ref("")
const docacttypeData = ref([
  { view: '전체', key: ''},
  { view: '결재', key: 'DEA41'},
  { view: '1인결재', key: 'DEA45'},
])
const acttypeSearch = () => {
  selectReqCompleteListCondi.value.acttype = docacttype.value;
  selectReqCompleteList(1);
  currentPageSelectReqCompleteList.value = 1;
}
// for selectReqCompleteList
const checkedListSelectReqCompleteList = ref([])
const isChecked = (item) => {
  return checkedListSelectReqCompleteList.value.some((i) => i.docid === item.raw.docid);
};
const toggleSelection = (item) => {
  const index = checkedListSelectReqCompleteList.value.findIndex((i) => i.docid === item.raw.docid);
  if (index !== -1) {
    checkedListSelectReqCompleteList.value.splice(index, 1);
  } else {
    checkedListSelectReqCompleteList.value.push({ docid: item.raw.docid });
  }
};
const selectReqCompleteListList = ref([])
const selectReqCompleteListCnt = ref(0)
const selectReqCompleteListDefCondi = {
    docid: "",
    authorid: "",
    docttl: "",
    startDt: "",
    endDt: "",
    pageNum: 1,
    pageSize: 10,
    sortDirection: "ASC",
    sortItem: "docid"
  }
const selectReqCompleteListCondi = ref({ ...selectReqCompleteListDefCondi })
const selectReqCompleteListCondiCheck = ref({ value: { ...selectReqCompleteListDefCondi }, flag: { ...selectReqCompleteListDefCondi } })
/* ================ pagination ================ */
const selectReqCompleteListLoader = ref(true)
const totalPagesSelectReqCompleteList = ref(0)
const currentPageSelectReqCompleteList = ref(1)
const pageSizeSelectReqCompleteList = ref(10)
const pageSizesSelectReqCompleteList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectReqCompleteList = () => {
  selectReqCompleteListCondi.value.pageSize = pageSizeSelectReqCompleteList.value;
  currentPageSelectReqCompleteList.value = 1;
  selectReqCompleteList(currentPageSelectReqCompleteList.value);
}
const handlePageChangeSelectReqCompleteList = () => {
  selectReqCompleteList(currentPageSelectReqCompleteList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectReqCompleteListCondiCheck.value, selectReqCompleteListCondi.value);
  setQueries(route, selectReqCompleteListCondi.value, selectReqCompleteListDefCondi);
  await selectReqCompleteList(selectReqCompleteListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectReqCompleteListCondi.value, selectReqCompleteListDefCondi);
  await selectReqCompleteList(selectReqCompleteListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectReqCompleteListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectReqCompleteListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectReqCompleteListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectReqCompleteListCondiCheck.value, selectReqCompleteListCondi.value, selectReqCompleteListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectReqCompleteListCondi.value["docttl"] = "";
  setCondiChanged(selectReqCompleteListCondiCheck.value, selectReqCompleteListCondi.value, selectReqCompleteListCondi.value);
})

watchEffect(() => {
})


const staticColumnsSelectReqCompleteList = [
  { title: "유형", key: "kind", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "구분", key: "acttype", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "문서번호", key: "docno", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "보고일자", key: "reportdt", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "제목", key: "docttl", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "작성자", key: "authorname", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직위", key: "authordutyname", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "작성부서", key: "authordeptname", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "처리일자", key: "updt", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "상태", key: "state", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이력", key: "histBtn", width: "30px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

/* ======================= multi selection ======================= */
const selectReqCompleteListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectReqCompleteListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectReqCompleteListList.value.filter((item) =>
    checkedListSelectReqCompleteList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */


const selectReqCompleteList = async (pageNum) => {

  selectReqCompleteListLoader.value = true;
  selectReqCompleteListCondi.value.pageNum = parseInt(pageNum);

  selectReqCompleteListCondi.value.authorid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '')
    if(validCheckSearchInput(comboInputData.value))
      selectReqCompleteListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      selectReqCompleteListLoader.value = false;
      return;
    }
  try {
    const response = await API.dctAPI.selectReqCompleteList({ ...selectReqCompleteListCondi.value }, urlPaths.value);

    response.data.list = response.data.list.map((u) => {
      // u = { ...u, disabled: true}; // 잘못된기안 삭제용(임시)
      u = { ...u, disabled: u.acttype == 'DEA43' ? true : false }; // 반려삭제
      u = { ...u, acttype: transformAsktype(u.acttype) };
      u = { ...u, state: transformState(u.state) };
      u = { ...u, reportdt: dayjs(u.reportdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      u = { ...u, updt: dayjs(u.updt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      u = { ...u, kind: u.copyno != undefined ? !u.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서'};
      return { ...u};
    });

    selectReqCompleteListList.value = response.data.list;
    selectReqCompleteListCnt.value = response.data.total;
    totalPagesSelectReqCompleteList.value = response.data.pages;
    const allOption = pageSizesSelectReqCompleteList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectReqCompleteListLoader.value = false;

    setCondiClear(selectReqCompleteListCondiCheck.value, selectReqCompleteListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const rejectedDocumentToTempSave = async () => {
  if (checkedListSelectReqCompleteList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  } 
  if (!confirm("삭제하시겠습니까?\n반려삭제된 기안문은 임시저장함으로 이동됩니다.")) {
    return;
  }
  await API.dctAPI
  .rejectedDocumentToTempSave(checkedListSelectReqCompleteList.value, urlPaths.value)
  .then(response => {
    if (response.status == 200) {
      alert("정상 삭제되었습니다");
      moveToBmsDctTemplist();
    }
  })
  .catch(error => {
    console.log(error)
    alert("Server Error");
  });
};


const deleteDraftCompleteList = async () => {
  if (checkedListSelectReqCompleteList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  } 
  if (!confirm("삭제하시겠습니까?")) {
    return;
  }
  await API.dctAPI
  .deleteDraftCompleteList(checkedListSelectReqCompleteList.value, urlPaths.value)
  .then(response => {
    if (response.status == 200) {
      alert("정상 삭제되었습니다");
      selectReqCompleteList(1);
    }
  })
  .catch(error => {
    console.log(error)
    alert("Server Error");
  });

}


// 로우클릭
const moveDetailPage = (event, row, pathname) => {
  const id = event.target.id;
  if (id != "") {
    return;
  }

  if(row.paperflag == 'Y'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  const param = {docid: row.docid, frompage: "BmsDctreqcompletelist", acttype: row.acttype};
  router.push({
    name: pathname,
    query: {
      ...param
    }
  });
};


const moveToBmsDctTemplist = () => {
  router.push({
    name: "BmsDcttemplist",
  });
};
</script>