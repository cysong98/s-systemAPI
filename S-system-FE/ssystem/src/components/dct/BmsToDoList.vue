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
      <h4 class="h4">처리할 일</h4>
    </div>

    <div class="tabs-type-02">
      <button type="button" class="button" @click="moveListPage('BmsDctwaitlist', '결재, 결재대기함')">
        <strong class="title">결재대기함</strong>
        <span class="badge">{{ selectWaitListCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsDctsendwaitlist', '발송, 발송대기함')">
        <strong class="title">발송대기함</strong>
        <span class="badge">{{ selectSendWaitListCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsDctreceiptlist', '접수, 접수대기함')">
        <strong class="title">접수대기함</strong>
        <span class="badge">{{ selectReceiptListCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsReprocesslist', '접수, 재지정함')">
        <strong class="title">재지정함</strong>
        <span class="badge">{{ selectReprocessListCnt }}</span>
      </button>
    </div>

    <div class="box-content">
      <!-- 결재대기함 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">결재대기함</h5>
          <button class="more" @click="moveListPage('BmsDctwaitlist', '결재, 결재대기함')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectWaitList"
          :items="selectWaitListList"
          :items-per-page="pageSizeSelectWaitList"
          :loading="selectWaitListLoader"
          @click:row="(event, item) => moveApproveDetailPage(item, 'BmsDctwaitcard', '결재, 결재대기함')"
          item-value="docid"
          class="table-type-02"
          hover
          >
          <template v-slot:item.docttl="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
      </section>


      <!-- 발송대기함 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">발송대기함</h5>
          <button type="button" class="more" @click="moveListPage('BmsDctsendwaitlist', '발송, 발송대기함')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectSendWaitList"
          :items="selectSendWaitListList"
          :items-per-page="pageSizeSelectSendWaitList"
          :loading="selectSendWaitListLoader"
          @click:row="(event, item) => moveDetailPage(item, 'BmsDctsendwaitcard', '발송, 발송대기함')"
          item-value="docid"
          class="table-type-02"
          hover
          >
          <template v-slot:item.docttl="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.docttl }}
            </div>
          </template>
          <template v-slot:item.recvdeptnames="{ item }">
            <div class="text-left-05" >
              {{ item.raw.recvdeptnames }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
      </section>

      <!-- 접수대기함 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">접수대기함</h5>
          <button type="button" class="more" @click="moveListPage('BmsDctreceiptlist', '접수, 접수대기함')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectReceiptList"
          :items="selectReceiptListList"
          :items-per-page="pageSizeSelectReceiptList"
          :loading="selectReceiptListLoader"
          @click:row="(event, item) => moveDetailPage(item, 'BmsReceiptdetailcard', '접수, 접수대기함')"
          item-value="enfdocid"
          class="table-type-02-copydoc"
          hover
          >
          <template v-slot:item.doctitle="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.doctitle }}
            </div>
          </template>
          <template v-slot:item.docno2="{ item }">
            {{ trimAfterDash(item.raw.docno) }}
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
      </section>

      <!-- 재지정함 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">재지정함</h5>
          <button type="button" class="more" @click="moveListPage('BmsReprocesslist', '접수, 재지정함')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectReprocessList"
          :items="selectReprocessListList"
          :items-per-page="pageSizeSelectReprocessList"
          :loading="selectReprocessListLoader"
          @click:row="(event, item) => moveDetailPage(item, 'BmsReprocessdetailcard', '접수, 재지정함')"
          item-value="enfdocid"
          class="table-type-02-copydoc"
          hover
          >
          <template v-slot:item.doctitle="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.doctitle }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
      </section>
    </div>
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
import { getValueByKey, trimAfterDash } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';

const name = ref('BmsToDoList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
import { transformDate, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  await selectWaitList(selectWaitListCondi.value.pageNum);
  await selectSendWaitList(selectSendWaitListCondi.value.pageNum);
  await selectReceiptList(selectReceiptListCondi.value.pageNum);
  await selectReprocessList(selectReprocessListCondi.value.pageNum);
})

const moveListPage = (pathname, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  router.push({
    name: pathname,
  });
};

const moveDetailPage = (row, pathname, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  if(pathname === 'BmsTrndetailcard'){
    row.item.raw.proc = "proc"
    row.item.raw.parentPage = "BmsComtransproctodolist";
  }
  if(pathname === 'BmsTrndetailcard'){
    row.item.raw.parentPage = "BmsTrncurrentlist";
  }
  if(pathname === 'BmsReceiptdetailcard'){
    pathname = row.item.raw.regirecvtype == '1' ? 'BmsReceiptdetailcard' : 'BmsDctReceivedOfflinesharecard';
    row.item.raw.page = "BmsDctreceiptlist";
    row.item.raw.frompage = "BmsDctreceiptlist";
  }
  if(pathname === 'BmsReprocessdetailcard'){
    pathname = row.item.raw.regirecvtype == '1' ? 'BmsReceiptdetailcard' : 'BmsDctReceivedOfflinesharecard';
    row.item.raw.page = "BmsReprocesslist";
    row.item.raw.frompage = "BmsReprocesslist";
  }
  router.push({
    name: pathname,
    query: {
      ...row.item.raw,
    },
  });
};

/* ====================== 1. 결재대기함 ====================== */
const selectWaitListLoader = ref(true);
const currentPageSelectWaitList = ref(1);
const totalPagesSelectWaitList = ref(0);
const pageSizeSelectWaitList = ref(10);
const handlePageChangeSelectWaitList = () => {
  selectWaitList(currentPageSelectWaitList.value);
};
const selectWaitListCondi = ref({
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "docid",
  userid : "", 
});
const selectWaitListList = ref([]);
const selectWaitListCnt = ref(0);
const staticColumnsSelectWaitList = [
  { key: "indt", title: "둥록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "요청자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordeptname", title: "요청부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectWaitList = async (pageNum) => {
  selectWaitListLoader.value = true;
  selectWaitListCondi.value.pageNum = parseInt(pageNum);
  selectWaitListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectWaitList({ ...selectWaitListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectWaitListList.value = response.data.list;
    selectWaitListCnt.value = response.data.total;
    totalPagesSelectWaitList.value = response.data.pages;
    selectWaitListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};
const moveApproveDetailPage = (row, pathname, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  const param = {docid: row.item.raw.docid, frompage: "BmsDctwaitlist"};
  let path = row.item.raw.paperflag == 'Y' && row.item.raw.paperrecgubun == '접수' ? "BmsDctReceivedOfflineupdatecard" : pathname;
  
  router.push({
    name: path,
    params: {
      ...param,
    },
    query: {
      ...param,
    },
  });
};
/* =========================================================== */



/* ====================== 2. 발송대기함 ====================== */
const selectSendWaitListLoader = ref(true);
const currentPageSelectSendWaitList = ref(1);
const totalPagesSelectSendWaitList = ref(0);
const pageSizeSelectSendWaitList = ref(10);
const handlePageChangeSelectSendWaitList = () => {
  selectSendWaitList(currentPageSelectSendWaitList.value);
};
const selectSendWaitListCondi = ref({
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "docid",
  userid : "",
 });
const selectSendWaitListList = ref([]);
const selectSendWaitListCnt = ref(0);
const staticColumnsSelectSendWaitList = [
  { key: "indt", title: "둥록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "objname", title: "발신자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "recvdeptnames", title: "수신부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

const selectSendWaitList = async (pageNum) => {
  selectSendWaitListLoader.value = true;
  selectSendWaitListCondi.value.pageNum = parseInt(pageNum);
  selectSendWaitListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectSendWaitList({ ...selectSendWaitListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectSendWaitListList.value = response.data.list;
    selectSendWaitListCnt.value = response.data.total;
    totalPagesSelectSendWaitList.value = response.data.pages;
    selectSendWaitListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};
/* =========================================================== */



/* ====================== 3. 접수대기함 ====================== */
const selectReceiptListLoader = ref(true);
const currentPageSelectReceiptList = ref(1);
const totalPagesSelectReceiptList = ref(0);
const pageSizeSelectReceiptList = ref(10);
const handlePageChangeSelectReceiptList = () => {
  selectReceiptList(currentPageSelectReceiptList.value);
};
const selectReceiptListCondi = ref({
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "docid"
});
const selectReceiptListList = ref([]);
const selectReceiptListCnt = ref(0);
const staticColumnsSelectReceiptList = [
  { key: "indt", title: "둥록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "me", title: "접수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docno2", title: "발신부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectReceiptList = async (pageNum) => {
  selectReceiptListLoader.value = true;
  selectReceiptListCondi.value.pageNum = parseInt(pageNum);
  selectReceiptListCondi.value.userid = getUserLoginData.value.userid;
  selectReceiptListCondi.value.deptid = getUserLoginData.value.deptid;
  selectReceiptListCondi.value.tempflag = 'N';
  if(getUserLoginData.value.vgroupid.includes('5220') ){
    selectReceiptListCondi.value.vgroupid = '5220';
  }
  try {
    const response = await API.dctAPI.selectReceiptList({ ...selectReceiptListCondi.value}, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD') };
      u = { ...u, me: getUserLoginData.value.username };
      return { ...u};
    });
    selectReceiptListList.value = response.data.list;
    selectReceiptListCnt.value = response.data.total;
    totalPagesSelectReceiptList.value = response.data.pages;
    selectReceiptListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};
/* =========================================================== */



/* ====================== 4. 재지정함 ====================== */
const selectReprocessListLoader = ref(true);
const currentPageSelectReprocessList = ref(1);
const totalPagesSelectReprocessList = ref(0);
const pageSizeSelectReprocessList = ref(10);
const handlePageChangeSelectReprocessList = () => {
  selectReprocessList(currentPageSelectReprocessList.value);
};
const selectReprocessListCondi = ref({ 
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "enfcodid",
});
const selectReprocessListList = ref([]);
const selectReprocessListCnt = ref(0);
const staticColumnsSelectReprocessList = [
  { key: "indt", title: "둥록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "actername", title: "요청자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "acterdeptname", title: "요청부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectReprocessList = async (pageNum) => {
  selectReprocessListLoader.value = true;
  selectReprocessListCondi.value.pageNum = parseInt(pageNum);
  selectReprocessListCondi.value.userid = getUserLoginData.value.userid;

  try {
    const response = await API.dctAPI.selectReprocessList({ ...selectReprocessListCondi.value },urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectReprocessListList.value = response.data.list;
    selectReprocessListCnt.value = response.data.total;
    totalPagesSelectReprocessList.value = response.data.pages;
    selectReprocessListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};
/* =========================================================== */




</script>

<style lang="scss" scoped>
.title {
  display: flex;
  justify-content: space-between;
}
.more {
  margin-top: 10px;
  margin-right: 10px;
}
</style>
