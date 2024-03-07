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
      <h4 class="h4">결재</h4>
    </div>
    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">결재대기함</h5>
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
                  item-title="key"
                  item-value="value"
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
                  v-else-if="comboSelected === 'asktype'"
                  v-model="docasktype"
                  :items="docasktypeData"
                  @update:modelValue="asktypeSearch"
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
                  @keyup.enter="selectWaitList(selectWaitListCondi.pageNum)"
                  variant="outlined"
                  hide-details="auto" />
              </td>
              <th>보고기간</th>
              <td>
                <v-text-field
                  type="date"
                  v-model="selectWaitListCondi.startDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
                ~
                <v-text-field
                  type="date"
                  v-model="selectWaitListCondi.endDt"
                  required
                  variant="outlined"
                  hide-details="auto" 
                />
              </td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectWaitList(selectWaitListCondi.pageNum)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ selectWaitListCnt }}개</span>
          <v-select
            v-model="pageSizeSelectWaitList"
            :items="pageSizesSelectWaitList"
            item-title="view"
            item-value="key"
            @update:modelValue="handlePageSizeChangeSelectWaitList"
            variant="outlined"
            hide-details="auto"
          ></v-select>
        </div>

        <v-data-table
          v-model="checkedListSelectWaitList"
          :headers="staticColumnsSelectWaitList"
          :items="selectWaitListList"
          :items-per-page="pageSizeSelectWaitList"
          :loading="selectWaitListLoader"
          :item-value="selectWaitListIdentifier"
          class="table-type-02"
          hover
          >
          <template v-slot:item="{ item }">
            <tr @click="moveToPageFunc(item.raw)" style="cursor: pointer;">
              <td> {{ item.raw.copyno != undefined ? !item.raw.copyno.includes('원') ? '접수문서' : '생산문서' : '생산문서' }} </td>
              <td> {{ transformAsktype(item.raw.asktype) }} </td>
              <td> {{ item.raw.docno }} </td>
              <td> {{ item.raw.copyno }} </td>
              <td> {{ transformDate(item.raw.indt) }} </td>
              <td :class="{ 'text-left': item }" > 
                <v-chip class="density" v-if="transformEmergency(item.raw.emergency) === '지급'" color="green">{{ transformEmergency(item.raw.emergency) }}</v-chip>
                <v-chip class="density" v-if="transformEmergency(item.raw.emergency) === '긴급'" color="orange">{{ transformEmergency(item.raw.emergency) }}</v-chip>
                <v-chip class="density" v-if="transformEmergency(item.raw.emergency) === '초긴급'" color="red">{{ transformEmergency(item.raw.emergency) }}</v-chip>
                {{ item.raw.docttl }} 
              </td>
              <td> {{ transformSeclevel(item.raw.seclevel) }} </td>
              <td> {{ item.raw.authorname }} </td>
              <td> {{ item.raw.authordutyname }} </td>
              <td> {{ item.raw.authordeptname }} </td>
              <td> 
                <v-btn class="magnify"  @click.stop="histBtnClick(item.raw.docid)">
                  <v-icon></v-icon>
                </v-btn>
              </td>
            </tr>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
        <v-pagination
          v-model="currentPageSelectWaitList"
          :length="totalPagesSelectWaitList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectWaitList"
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
import { loadRouteLocation, useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { storeToRefs } from 'pinia';
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel, transformEmergency } from "@/utils/TransFormLabelDataUtil.js"
import DctHistoryCardPopup from "./DctHistoryCardPopup.vue";

const name = ref('BmsDctWaitList')
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
  {key:"유형", value:"kind"},
  {key:"구분", value:"asktype"},
  {key:"문서번호", value:"docno"},
  {key:"사본번호", value:"copyno"},
  {key:"제목", value:"docttl"},
  {key:"등급", value:"seclevel"},
  {key:"보고자", value:"authorname"},
  {key:"부서", value:"authordeptname"},
]
const dockinde = ref("")
const dockindData = ref([
  { view: '전체', key: ''},
  { view: '생산문서', key: '1'},
  { view: '접수문서', key: '2'},
])
const kindSearch = () => {
  selectWaitListCondi.value.kind = dockinde.value;
  selectWaitList(1);
  currentPageSelectWaitList.value = 1;
}
const docasktype = ref("")
const docasktypeData = ref([
  { view: '전체', key: ''},
  { view: '협조', key: 'DEA31'},
  { view: '검토', key: 'DEA21'},
  { view: '전결', key: 'DEA51'},
  { view: '결재', key: 'DEA41'},
  { view: '전대결', key: 'DEA71'},
  { view: '대결', key: 'DEA61'},
])
const asktypeSearch = () => {
  selectWaitListCondi.value.asktype = docasktype.value;
  selectWaitList(1);
  currentPageSelectWaitList.value = 1;
}
const docseclevel = ref("")
const docseclevelData = ref([
  { view: '전체', key: ''},
  { view: 'II급', key: '2'},
  { view: 'III급', key: '3'},
  { view: '일반', key: '5'},
])
const seclevelSearch = () => {
  selectWaitListCondi.value.seclevel = docseclevel.value;
  selectWaitList(1);
  currentPageSelectWaitList.value = 1;
}
// for selectWaitList
const checkedListSelectWaitList = ref([])
const selectWaitListList = ref([])
const selectWaitListDefCondi = {
  docttl: "",
  authorname: "",
  authordeptname: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "docid",
  userid : ""
}
const selectWaitListCondi = ref({ ...selectWaitListDefCondi })
const selectWaitListCondiCheck = ref({ value: { ...selectWaitListDefCondi }, flag: { ...selectWaitListDefCondi } })
/* ================ pagination ================ */
const selectWaitListLoader = ref(true)
const totalPagesSelectWaitList = ref(0)
const currentPageSelectWaitList = ref(1)
const pageSizeSelectWaitList = ref(10)
const pageSizesSelectWaitList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectWaitList = () => {
  selectWaitListCondi.value.pageSize = pageSizeSelectWaitList.value;
  currentPageSelectWaitList.value = 1;
  selectWaitList(currentPageSelectWaitList.value);
}
const handlePageChangeSelectWaitList = () => {
  selectWaitList(currentPageSelectWaitList.value);
}
/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  setCondiClear(selectWaitListCondiCheck.value, selectWaitListCondi.value);
  setQueries(route, selectWaitListCondi.value, selectWaitListDefCondi);
  await selectWaitList(selectWaitListCondi.value.pageNum);
})
onBeforeUnmount(() => {
})

watch(route, async (route) => {
  setQueries(route, selectWaitListCondi.value, selectWaitListDefCondi);
  await selectWaitList(selectWaitListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectWaitListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectWaitListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectWaitListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectWaitListCondiCheck.value, selectWaitListCondi.value, selectWaitListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectWaitListCondi.value["docttl"] = "";
    selectWaitListCondi.value["authorname"] = "";
    selectWaitListCondi.value["authordeptname"] = "";
  setCondiChanged(selectWaitListCondiCheck.value, selectWaitListCondi.value, selectWaitListCondi.value);
})

watchEffect(() => {
})


const staticColumnsSelectWaitList = [
  { key: "kind", title: "유형", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "asktype", title: "구분", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "copyno", title: "사본번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "보고일시", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "seclevel", title: "등급", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "보고자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordutyname", title: "직위", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordeptname", title: "부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },  
  { title: "이력", key: "histBtn", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class"  },
]

const responseData = ref([]);

const moveToPageParamDefCondi = { docid: "" };
const moveToPageParamCondi = ref({ ...moveToPageParamDefCondi });

const clickedItem = ref();
const clickedItemId = ref();
const paramDocId = ref();

const moveToPageFunc = async(item) => {
  clickedItem.value = responseData.value.indexOf(item);
  clickedItemId.value = item.docid;
  paramDocId.value = item.docid;
  if(item.paperflag == 'Y' && item.paperrecgubun == '접수'){
    moveToBmsDctOfflineUpdateCard(clickedItemId.value);
  }else{
    moveToBmsDctWaitCard(clickedItemId.value);
  }
};

// Move Function
const moveToBmsDctWaitCard = (paramDocId) => {
  moveToPageParamCondi.value.docid = paramDocId;
  router.push({
    name: "BmsDctwaitcard",
    query: {
      ...moveToPageParamCondi.value,
    },
  });
};

const moveToBmsDctOfflineUpdateCard = (paramDocId) => {
  const param = {frompage: 'BmsDctwaitlist', docid: paramDocId}
  router.push({
    name: "BmsDctReceivedOfflineupdatecard",
    query: {
      ...param,
    },
  });
};

/* ======================= multi selection ======================= */
const selectWaitListIdentifier = computed(() => (item) => {
  item.identifier = `${item.docid}-${item.docid}-${item.pathorder}-${item.docid}`;
 return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectWaitListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectWaitListList.value.filter((item) => 
    checkedListSelectWaitList.value.includes(item.identifier))
  
  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });

});
/* =========================================================== */

const selectWaitListRouterPush = (pageNum) => {
  selectWaitListCondi.value.pageNum = parseInt(pageNum);
  router
    .push({
      query: selectWaitListCondi.value
    })
    .catch(error => {
      console.log(error);
    });
}


const selectWaitListCnt = ref(0);

const selectWaitList = async (pageNum) => {

  selectWaitListLoader.value = true;
  selectWaitListCondi.value.pageNum = parseInt(pageNum);
  selectWaitListCondi.value.userid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined) && comboInputData.value != '') {
    if(validCheckSearchInput(comboInputData.value)) {
      selectWaitListCondi.value[comboSelected.value] = comboInputData.value;
    } else {
      comboInputData.value = "";
      selectWaitListLoader.value = false;
      return;
    }
  }
  try {
    const response = await API.dctAPI.selectWaitList({ ...selectWaitListCondi.value }, urlPaths.value);
    selectWaitListList.value = response.data.list;
    selectWaitListCnt.value = response.data.total;
    totalPagesSelectWaitList.value = response.data.pages;
    const allOption = pageSizesSelectWaitList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectWaitListLoader.value = false;
    setCondiClear(selectWaitListCondiCheck.value, selectWaitListCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

</script>


<style scoped>
.density {
  padding: 0px 11px;
  height: 21px;
}
</style>