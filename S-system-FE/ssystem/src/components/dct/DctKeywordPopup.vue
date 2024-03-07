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
    <div class="content">

      <div class="row">
        <div class="col">
          <div class="box-flex">
            <v-label>키워드</v-label>
            <v-text-field style="width: 500px; margin-right: 50px;"
              v-model="selectRdocForKeywordSearchCondi.keyword"
              variant="outlined"
              hide-details="auto"
              clearable
              @keyup.enter="selectRdocForKeywordSearch(1)"
            />
            <v-label>구분</v-label>
            <div class="dept-select">
              <v-radio-group
                v-model="selectRdocForKeywordSearchCondi.userreqtype" @update:modelValue="updateCondi" hide-details="auto" inline>
                <v-radio v-for="(data,idx) in radioData" :key="idx" :label="data" color="indigo" :value="data"></v-radio>
              </v-radio-group>
              <v-select
                v-model="choicedDeptKey"
                :items="depts"
                item-title="view"
                item-value="key"
                @update:modelValue="updatechoicedDept"
                variant="outlined"
                hide-details="auto"
              ></v-select>
            </div>
            <v-btn class="magnify-solid" @click="selectRdocForKeywordSearch(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <div class="row" style="margin-bottom: 0px; margin-top: 20px; justify-content: space-between;">
            <div style="display: flex; gap: 10px;">
              <p style="font-size: 17px; font-weight: bold;">비밀문서 {{ choicedDeptView ? ` (${choicedDeptView})` : `(${getUserLoginData.deptname})` }}</p>
              <p>전체: {{ selectRdocForKeywordSearchCnt }}개</p>
            </div>
            <v-pagination 
            v-model="currentPageselectRdocForKeywordSearch"
            :length="totalPagesselectRdocForKeywordSearch"
            class="pagination"
            total-visible="3"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            @click="handlePageChangeselectRdocForKeywordSearch"
            ></v-pagination>
          </div>
          <v-data-table
            :headers="columns"
            :items="selectRdocForKeywordSearchList"
            :items-per-page="pageSizeselectRdocForKeywordSearch"
            item-value="docid"
            class="table-type-02"
            @click:row="(event, item) => moveDetailPage(item.item.raw, 'BmsDctmgmtregidetailcard', '비밀관리기록부')"
            hover
            height="400px"
            fixed-header
          >
            <template v-slot:item.docttl="{ item }">
              <div :class="{ 'text-left': item }">
                {{ item.raw.docttl }}
              </div>
            </template>
            <template v-slot:bottom></template>
          </v-data-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>
  </div>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, topMenu, breadcrumbs } = storeToRefs(mainStore)
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { API } from "@/api";
import { getValueByKey, validCheckSearchInput } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import { transformDate, transformRdocGubun, transformAsktype, transformSeclevel, transformSecstatus } from "@/utils/TransFormLabelDataUtil.js"
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const name = ref('DctKeywordPopup')
const isloading = ref(false);
const selectRdocForKeywordSearchList = ref([])
const selectRdocForKeywordSearchCnt = ref(0)
const selectRdocForKeywordSearchDefCondi = {
  userreqtype: "개인",
  keyword: "",
  pageNum: 1,
  pageSize: 10,
}
const selectRdocForKeywordSearchCondi = ref({ ...selectRdocForKeywordSearchDefCondi })
/* ================ pagination ================ */
const totalPagesselectRdocForKeywordSearch = ref(0)
const currentPageselectRdocForKeywordSearch = ref(1)
const pageSizeselectRdocForKeywordSearch = ref(10)
const handlePageChangeselectRdocForKeywordSearch = () => {
  selectRdocForKeywordSearch(currentPageselectRdocForKeywordSearch.value);
}
/* ============================================== */
const radioData = ['개인','부서']

const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')

onMounted(async () => {
  args.value = props.args;
  await selectOtherDept();
  await selectRdocForKeywordSearch(selectRdocForKeywordSearchCondi.value.pageNum)
})

watchEffect(() => {
  args.value = props.args
})


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

const updateCondi = () => {
  choicedDeptKey.value = "";
  choicedDeptView.value = "";
  selectRdocForKeywordSearchCondi.value.deptid = getUserLoginData.value.deptid;
  selectRdocForKeywordSearch(1);
  currentPageselectRdocForKeywordSearch.value = 1;
};

const updatechoicedDept = () => {
  const selectedDept = depts.value.find(dept => dept.key === choicedDeptKey.value);
  if (selectedDept) {
    choicedDeptView.value = selectedDept.view;
  }
  if(choicedDeptKey.value != ""){
    selectRdocForKeywordSearchCondi.value.userreqtype = '타부서';
    selectRdocForKeywordSearchCondi.value.deptid = choicedDeptKey.value;
    selectRdocForKeywordSearch(1);
    currentPageselectRdocForKeywordSearch.value = 1;
  }else{
    selectRdocForKeywordSearchCondi.value.userreqtype = '개인';
    updateCondi();
  }
};

const columns = [
  { title: "등급", key: "seclevel", width:"50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "문서번호", key: "docno", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "관리번호", key: "mgmtno", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "보고일자", key: "reportdt", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "제목", key: "docttl", width:"300px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "작성자", key: "authorname", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직위", key: "authordutyname", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "작성부서", key: "authordeptname", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "키워드", key: "keyword", width:"100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

const selectRdocForKeywordSearch = async (pageNum) => {
  if(selectRdocForKeywordSearchCondi.value.keyword != ""){
    const isValid = validCheckSearchInput(selectRdocForKeywordSearchCondi.value.keyword);
    if (!isValid) {
      selectRdocForKeywordSearchCondi.value.keyword = "";
      return
    }
  }
  isloading.value = true;
  selectRdocForKeywordSearchCondi.value.pageNum = parseInt(pageNum);
  selectRdocForKeywordSearchCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectRdocForKeywordSearch({ ...selectRdocForKeywordSearchCondi.value }, urlPaths.value);
    response.data.list.map((u) => {
      u.reportdt = dayjs(u.reportdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD');
      u.seclevel = transformSeclevel(u.seclevel);
    });
    selectRdocForKeywordSearchList.value = response.data.list;
    totalPagesselectRdocForKeywordSearch.value = response.data.pages;
    selectRdocForKeywordSearchCnt.value = response.data.total;
    isloading.value = false;
  } catch (error) {
    isloading.value = false;
    console.log(error);
    alert("Server Error");
  }
  isloading.value = false;
}

const closePopUp = () => {
  props.toggleFunc();
}


const moveDetailPage = (row, pathname, activeLink) => {
  if(row.auth == '미보유'){
    alert("해당 문서의 열람권한이 없습니다. \n[비밀관리> 비밀문서사용요청> 열람요청] 에서 열람을 요청하세요.");
    return
  }
  topMenu.value.data = 'B';
  breadcrumbs.value.topMenu = '비밀문서';
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;
  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  const param = {docid: row.docid, frompage: 'BmsDctmgmtregilist'};

  let path = pathname;
  if(row.paperflag == 'Y'){
    if(row.paperrecgubun == '접수'){
      path = 'BmsDctReceivedOfflinecompletecard';
    }else{
      path = 'BmsDctProductOfflinecompletecard';
    }
  }

  router.push({
    name: path,
    params: {
        ...param
      },
      query: {
        ...param
      }
  });
  closePopUp();
}

</script>

<style scoped>
.pagination {
  height: 37px;
  padding-top: 0px;
  padding-bottom: 0px;
}
.dept-select {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

</style>