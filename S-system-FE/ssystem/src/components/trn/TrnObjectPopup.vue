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
  <div class="modal-body">
    <div class="content" style="padding-top: 15px;">
      
      <v-tabs v-model="selectedTab" align-tabs="end">
        <v-tab :value="1">생산</v-tab>
        <v-tab :value="2">접수</v-tab>
        <v-tab :value="3">일반</v-tab>
      </v-tabs>
      
      <!-- 생산 -->
      <v-window v-if="selectedTab == 1">
        <v-window-item>
          <div class="countSpan"> 전체 : {{args.dataObject.createOtherCount}} 개</div>
          <div class="row">
            <div class="col">
              <v-data-table
                v-model="checkedListSelectMgmtRegiReturnList"
                :headers="staticColumnsSelectMgmtRegiList"
                :items="selectMgmtRegiListList"
                :items-per-page="selectMgmtRegiListList.length"
                :loading="selectMgmtRegiListLoader"
                :item-value="selectMgmtRegiListIdentifier"
                show-select
                class="table-type-05"
                hover
                height="240px"
                fixed-header
                >
                <template v-slot:item.regirecvtype="{ item}">
                  {{ item.raw.regirecvtype == "1" ? '전자' : '비전자' }}
                </template>
                <template v-slot:item.seclevel="{ item}">
                  {{ transformSeclevel(item.raw.seclevel) }}
                </template>
                <template v-slot:item.secttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.secttl }}
                  </div>
                </template>
                <template v-slot:item.indt="{ item}">
                  {{ transformDate(item.raw.indt) }}
                </template>
                <template v-slot:item.secstatus="{ item}">
                  {{ transformSecstatus(item.raw.secstatus) }}
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>
          </div>
        </v-window-item>
      </v-window>

      <!-- 접수 -->
      <v-window v-if="selectedTab == 2">
        <v-window-item>
          <div class="countSpan"> 전체 : {{args.dataObject.receiptCount}} 개</div>
          <div class="row">
            <div class="col">
              <v-data-table
                v-model="checkedListSelectMgmtRegiReturnList"
                :headers="staticColumnsSelectMgmtRegiList"
                :items="selectMgmtRegiListList"
                :items-per-page="selectMgmtRegiListList.length"
                :loading="selectMgmtRegiListLoader"
                :item-value="selectMgmtRegiListIdentifier"
                show-select
                class="table-type-05"
                hover
                height="240px"
                fixed-header
                >
                <template v-slot:item.regirecvtype="{ item}">
                  {{ item.raw.regirecvtype == "1" ? '전자' : '비전자' }}
                </template>
                <template v-slot:item.seclevel="{ item}">
                  {{ transformSeclevel(item.raw.seclevel) }}
                </template>
                <template v-slot:item.secttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.secttl }}
                  </div>
                </template>
                <template v-slot:item.indt="{ item}">
                  {{ transformDate(item.raw.indt) }}
                </template>
                <template v-slot:item.secstatus="{ item}">
                  {{ transformSecstatus(item.raw.secstatus) }}
                </template>
                <template v-slot:bottom></template>
              </v-data-table>
            </div>
          </div>
        </v-window-item>
      </v-window>

      <!-- 일반 -->
      <v-window v-if="selectedTab == 3">
        <v-window-item>
          <div class="countSpan"> 전체 : {{args.dataObject.create5LevelCount}} 개</div>
          <div class="row">
            <div class="col">
              <v-data-table
                v-model="checkedListSelectMgmtRegiReturnList"
                :headers="staticColumnsSelectMgmtRegiList"
                :items="selectMgmtRegiListList"
                :items-per-page="selectMgmtRegiListList.length"
                :loading="selectMgmtRegiListLoader"
                :item-value="selectMgmtRegiListIdentifier"
                show-select
                class="table-type-05"
                hover
                height="240px"
                fixed-header
                >
                <template v-slot:item.regirecvtype="{ item}">
                  {{ item.raw.regirecvtype == "1" ? '전자' : '비전자' }}
                </template>
                <template v-slot:item.seclevel="{ item}">
                  {{ transformSeclevel(item.raw.seclevel) }}
                </template>
                <template v-slot:item.secttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.secttl }}
                  </div>
                </template>
                <template v-slot:item.indt="{ item}">
                  {{ transformDate(item.raw.indt) }}
                </template>
                <template v-slot:item.secstatus="{ item}">
                  {{ transformSecstatus(item.raw.secstatus) }}
                </template>
                <template v-slot:bottom></template>
              </v-data-table>
            </div>
          </div>
        </v-window-item>
      </v-window>

      <div class="row center">
        <v-icon size="x-large" color="blue" @click="moveDataDown">mdi-chevron-down-box</v-icon>
        <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
      </div>

      <div class="row">
        <v-data-table
          v-model="checkedListselectData"
          :headers="selectedDataHeaders"
          :items="selectedDataList"
          :items-per-page="99999999"
          item-value="docid"
          select-strategy="single"
          show-select
          :no-data-text="noDataText"
          height="190px"
          fixed-header
          class="table-type-05 narrow">
          <template v-slot:item.regirecvtype="{ item }">
            <span v-if="item.raw.regirecvtype == '1'">전자</span>
            <span v-else-if="item.raw.regirecvtype == '2'">비전자</span>
            <span v-else>전자</span>
          </template>
          <template v-slot:item.seclevel="{ item}">
            {{ transformSeclevel(item.raw.seclevel) }}
          </template>
          <template v-slot:item.secttl="{ item }">
            <div :class="{ 'text-left': item }">
              {{ item.raw.secttl }}
            </div>
          </template>
          <template v-slot:item.indt="{ item}">
            {{ transformDate(item.raw.indt) }}
          </template>
          <template v-slot:item.secstatus="{ item}">
            {{ transformSecstatus(item.raw.secstatus) }}
          </template>
          <template v-slot:item.regirecvgubun="{ item }">
            <span v-if="item.raw.regirecvgubun == '1'">생산</span>
            <span v-else-if="item.raw.regirecvgubun == '2'">접수</span>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
      </div>
    </div>

    <div class="buttons-bottom" style="padding-top: 0px;">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import _ from 'lodash';

const name = ref('TrnObjectPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const noObjectDataText = "데이터가 없습니다. 인계할 대상을 선택해 주세요.";

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
const selectedTab = ref(1); //생산탭(1번탭) 기본

// for selectMgmtRegiList
const checkedListSelectMgmtRegiReturnList = ref([])
const selectMgmtRegiListList = ref([])
const selectMgmtRegiListDefCondi = {
  userreqtype: "개인",
  // secttl: "",
  // authorname: "",
  // mgmtno: "",
  // mgmtid: "",
  // startDt: "",
  // endDt: "",
  // pageNum: 1,
  // pageSize: 10,
  // regirecvgubun: '', //생산, 접수 구분용도
  // seclevel: [],
  regirecvgubun: '1', //생산, 접수 구분용도
  seclevel: ['2','3','4'],
}
const selectMgmtRegiListCondi = ref({ ...selectMgmtRegiListDefCondi })
const selectMgmtRegiListLoader = ref(true)

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const noDataText = "선택한 데이터가 없습니다.";
const selectedData = ref([]);

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  selectedDataList.value = args.value.selectObject;
  await selectMgmtRegiNonPageList(selectMgmtRegiListCondi.value);
})

watch(selectedTab, async (newVal) => {
  switch (newVal) {
    case 1:
      selectMgmtRegiListCondi.value.regirecvgubun = '1';
      selectMgmtRegiListCondi.value.seclevel = ['2', '3', '4'];
      await selectMgmtRegiNonPageList(selectMgmtRegiListCondi.value); 
      break;
    case 2:
      selectMgmtRegiListCondi.value.regirecvgubun = '2';
      selectMgmtRegiListCondi.value.seclevel = null;
      await selectMgmtRegiNonPageList(selectMgmtRegiListCondi.value);
      break;
    case 3:
      selectMgmtRegiListCondi.value.regirecvgubun = '1';
      selectMgmtRegiListCondi.value.seclevel = ['5'];
      await selectMgmtRegiNonPageList(selectMgmtRegiListCondi.value);
      break;
  }
});


watchEffect(() => {
  args.value = props.args
})

const staticColumnsSelectMgmtRegiList = [
  { key: "regirecvtype", title: "종류", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

const dupliCheck = () => {
  return selectedDataList.value.some(data => {
    return checkedListSelectMgmtRegiReturnList.value.some(checked => 
      checked.docid == data.docid
    );
  });
}

const selectedDataList = ref([]);
const moveDataDown = () => {
  if(checkedListSelectMgmtRegiReturnList.value.length != 0){
    if(dupliCheck()){
      alert("이미 지정된 대상이 포함되어있습니다.");
      checkedListSelectMgmtRegiReturnList.value = [];
      return;
    }
    for(let i = 0; i < checkedListSelectMgmtRegiReturnList.value.length; i++){
      const selectedData = {};
      selectedData.regirecvtype = checkedListSelectMgmtRegiReturnList.value.map(item=>item.regirecvtype)[i];
      selectedData.seclevel = checkedListSelectMgmtRegiReturnList.value.map(item=>item.seclevel)[i];
      selectedData.docid = checkedListSelectMgmtRegiReturnList.value.map(item=>item.docid)[i];
      selectedData.docno = checkedListSelectMgmtRegiReturnList.value.map(item=>item.docno)[i];
      selectedData.mgmtid = checkedListSelectMgmtRegiReturnList.value.map(item=>item.mgmtid)[i];
      selectedData.mgmtno = checkedListSelectMgmtRegiReturnList.value.map(item=>item.mgmtno)[i];
      selectedData.secttl = checkedListSelectMgmtRegiReturnList.value.map(item=>item.secttl)[i];
      selectedData.indt = checkedListSelectMgmtRegiReturnList.value.map(item=>item.indt)[i];
      selectedData.copyno = checkedListSelectMgmtRegiReturnList.value.map(item=>item.copyno)[i];
      selectedData.authorname = checkedListSelectMgmtRegiReturnList.value.map(item=>item.authorname)[i];
      selectedData.deptname = checkedListSelectMgmtRegiReturnList.value.map(item=>item.deptname)[i];
      selectedData.secstatus = checkedListSelectMgmtRegiReturnList.value.map(item=>item.secstatus)[i];
      selectedData.regirecvgubun = checkedListSelectMgmtRegiReturnList.value.map(item=>item.regirecvgubun)[i];
      // selectedData.value = selectMgmtRegiListFinalData.value.map(item=>item)[i];
      selectedDataList.value.push(selectedData);
    }
    // selectedData.apprrecvtempflag = 'Y';
    // selectedData.apprrecvdeptname = selectOrganizationList.value[0].orgname;
    // selectedData.apprrecvrefdeptname = '개발팀';
    // selectedData.receiverSymbol = '개발00'; 
    // selectedData.apprrecvgubun = 'RDR08'; // 원내 코드값
    // selectedData.gubun = '원내'; 
    // selectedDataList.value.push(selectedData);
    checkedListSelectMgmtRegiReturnList.value = [];
  }else{
    alert("대상을 선택해주세요.")
  }
};

const checkedListselectData = ref([]);
const moveDataUp = () => {
  if (checkedListselectData.value.length !== 0) {
    const docid = checkedListselectData.value[0];
    const index = selectedDataList.value.findIndex(data => data.docid === docid);
    if (index !== -1) {
      selectedDataList.value.splice(index, 1);
    }
  } else {
    alert("삭제하실 대상을 선택해주세요");
  }
};

const makeFinalData = computed(() => {
  return selectedDataList.value.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});

const selectedDataHeaders = [
  { key: "regirecvtype", title: "종류", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "mgmtno", title: "관리번호", width: "14%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "indt", title: "등록일자", width: "16%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "35%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno", title: "문서번호", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "regirecvgubun", title: "구분", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

/* ======================= identifier ======================= */
const selectMgmtRegiListIdentifier = computed(() => (item) => {
  // item.identifier = `${item.mgmtid}-${item.docid}-${item.secttl}`;
  item.identifier = {
    regirecvtype: item.regirecvtype,
    regirecvgubun: item.regirecvgubun,
    seclevel: item.seclevel,
    mgmtno: item.mgmtno,
    indt: item.indt,
    copyno: item.copyno,
    authorname: item.authorname,
    deptname: item.deptname,
    secstatus: item.secstatus,
    mgmtid: item.mgmtid,
    docid: item.docid,
    docno: item.docno,
    secttl: item.secttl,
  }
 return item.identifier;
});
/* =========================================================== */

const selectMgmtRegiNonPageList = async (pageNum) => {
  selectMgmtRegiListLoader.value = true;
  selectMgmtRegiListCondi.value.pageNum = parseInt(pageNum);
  selectMgmtRegiListCondi.value.authorid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectMgmtRegiNonPageList({ ...selectMgmtRegiListCondi.value }, urlPaths.value);
    selectMgmtRegiListList.value = response.data;
    selectMgmtRegiListLoader.value = false;
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
}

const closePopUp = () => {
  props.toggleFunc();
}

const confirmPopUp = () => {
  childReturnValue.value = makeFinalData.value;
  childReturnValue.value.temp = args.value;
  props.returnFunc(childReturnValue.value);
}

</script>

<style lang="scss" scoped>
  .countSpan {
    margin-bottom: 10px;
  }
</style>