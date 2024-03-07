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
      <button type="button" class="button" @click="moveListPage('BmsUsereqlist', '비밀문서사용처리, 처리할 요청서')">
        <strong class="title">처리할 요청서</strong>
        <span class="badge">{{ selectUseReqListReqCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsUsecompletelist', '비밀문서사용처리, 요청서 완료처리')">
        <strong class="title">요청서 완료처리</strong>
        <span class="badge">{{ selectUseReqListCompleteCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsCominglist', '예고문도래목록')">
        <strong class="title">예고문도래목록</strong>
        <span class="badge">{{ selectComingListCnt }}</span>
      </button>
      <button type="button" class="button" @click="moveListPage('BmsComtransproctodolist', '인계인수, 처리할 인계인수서')">
        <strong class="title">처리할 인계인수서</strong>
        <span class="badge">{{ selectTransProcToDoListCnt }}</span>
      </button>
    </div>

    <div class="box-content">

      <!-- 처리할 요청서 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">처리할 요청서</h5>
          <button type="button" class="more" @click="moveListPage('BmsUsereqlist', '비밀문서사용처리, 처리할 요청서')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectUseReqList"
          :items="selectUseReqLisReqList"
          :items-per-page="pageSizeSelectUseReqList"
          :loading="selectUseReqListLoader"
          @click:row="(event, item) => moveToBmsUseReqdetail(item, 'req', '비밀문서사용처리, 처리할 요청서')"
          item-value="apprreqid"
          class="table-type-02"
          hover
        >
        <template v-slot:item.reqttl="{ item }">
          <div :class="{ 'text-left-02': item }">
            {{ item.raw.reqttl }}
          </div>
        </template>
        <template v-slot:bottom />
        </v-data-table>
      </section>

      <!-- 요청서 완료처리 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">요청서 완료처리</h5>
          <button type="button" class="more" @click="moveListPage('BmsUsecompletelist', '비밀문서사용처리, 요청서 완료처리')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectUseReqList"
          :items="selectUseReqLisCompleteList"
          :items-per-page="pageSizeSelectUseReqList"
          :loading="selectUseReqListLoader"
          @click:row="(event, item) => moveToBmsUseReqdetail(item, 'complete', '비밀문서사용처리, 요청서 완료처리')"
          item-value="apprreqid"
          class="table-type-02"
          hover
        >
        <template v-slot:item.reqttl="{ item }">
          <div :class="{ 'text-left-02': item }">
            {{ item.raw.reqttl }}
          </div>
        </template>
        <template v-slot:bottom />
        </v-data-table>
      </section>

      <!-- 예고문도래목록 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">예고문도래목록</h5>
          <button type="button" class="more" @click="moveListPage('BmsCominglist', '예고문도래목록')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsselectComing"
          :items="selectComingListList"
          :items-per-page="pageSizeSelectComingList"
          :loading="selectComingListloader"
          @click:row="(event, item) => moveComingDetailPage(item.item.raw, 'BmsDctmgmtregidetailcard', '예고문도래목록')"
          item-value="apprreqid"
          class="table-type-02"
          hover
        >
        <template v-slot:item.secttl="{ item }">
          <div :class="{ 'text-left-02': item }">
            {{ item.raw.secttl }}
          </div>
        </template>
        <template v-slot:item.etc="{ item }">
          <v-icon :color="dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').isBefore(dayjs(), 'day') ? 'red' : 'blue'">mdi-file-cancel-outline</v-icon>
          <span>
              {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').isBefore(dayjs(), 'day') ? '만료' : '도래' }}
              {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') }}
              {{ item.raw.copyno.includes("원") ? transReclassInfo(item.raw.orgnreclass) : transReclassInfo(item.raw.copyreclass) }}
          </span>
        </template>
        <template v-slot:bottom />
        </v-data-table>
      </section>


      <!-- 처리할 인계인수서 -->
      <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">처리할 인계인수서</h5>
          <button type="button" class="more" @click="moveListPage('BmsComtransproctodolist', '인계인수, 처리할 인계인수서')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsSelectTransProcToDoList"
          :items="selectTransProcToDoListList"
          :items-per-page="pageSizeSelectTransProcToDoList"
          :loading="selectTransProcToDoListLoader"
          @click:row="(event, item) => moveTransProcDetailPage(item, 'BmsTrndetailcard', '인계인수, 처리할 인계인수서')"
          item-value="transferid"
          class="table-type-02"
          hover
          >
          <template v-slot:item.title="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.title }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
      </section>


      <!-- 인수현황함 -->
      <!-- <section class="section sticky">
        <div class="box-title title">
          <h5 class="h5">인수현황함</h5>
          <button type="button" class="more" @click="moveListPage('BmsTrncurrentlist')">더보기</button>
        </div>
      </section>
      <section class="section">
        <v-data-table
          :headers="staticColumnsBmsTrnCurrentList"
          :items="bmsTrnCurrentListList"
          :items-per-page="pageSizeBmsTrnCurrentList"
          :loading="bmsTrnCurrentListLoader"
          @click:row="(event, item) => moveDetailPage(item, 'BmsTrndetailcard')"
          item-value="transferid"
          class="table-type-02"
          hover
          >
          <template v-slot:item.title="{ item }">
            <div :class="{ 'text-left-02': item }">
              {{ item.raw.title }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table> 
      </section> -->
    </div>
    <br>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, topMenu, breadcrumbs } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import { transformSecstatus, transformDate, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('ManageToDoList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
})

onMounted(async () => {
  await selectUseReqList(selectUseReqListCondi.value.pageNum, "req"); // 처리할 요청서
  await selectUseReqList(selectUseReqListCondi.value.pageNum, "complete"); // 요청서 완료처리
  await selectComingList(selectComingListCondi.value.pageNum); // 예고문도래목록
  await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum); // 처리할 인계인수서
  // await bmsTrnCurrentList(bmsTrnCurrentListCondi.value.pageNum); // 인수현황함
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

/* =============== 1. 처리할 요청서 && 2. 요청서 완료처리 =============== */
const selectUseReqListLoader = ref(true);
const currentPageSelectUseReqList = ref(1);
const totalPagesSelectUseReqList = ref(0);
const pageSizeSelectUseReqList = ref(10);
const handlePageChangeSelectUseReqList = () => {
  selectUseReqList(currentPageSelectUseReqList.value);
};
const selectUseReqListCondi = ref({ 
  pageNum: 1,
  pageSize: 5,
  sortDirection: "DESC",
  sortItem: "reqdt",
});
const selectUseReqLisReqList = ref([]);
const selectUseReqListReqCnt = ref(0);
const selectUseReqLisCompleteList = ref([]);
const selectUseReqListCompleteCnt = ref(0);
const staticColumnsSelectUseReqList = [
  { key: "reqdt", title: "요청일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqtypenm", title: "구분", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqname", title: "요청자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectUseReqList = async (pageNum, type) => {
  selectUseReqListLoader.value = true;    
  selectUseReqListCondi.value.pageNum = parseInt(pageNum);

  if(type == "req"){
    selectUseReqListCondi.value.reqid = "";
    selectUseReqListCondi.value.apprid = getUserLoginData.value.userid;
    selectUseReqListCondi.value.pagereqtype = "req";
  }else if(type == "complete"){
    selectUseReqListCondi.value.reqid = getUserLoginData.value.userid;
    selectUseReqListCondi.value.apprid = "";
    selectUseReqListCondi.value.pagereqtype = "complete";
  }

  try {
    const response = await API.useAPI.selectUseReqList({ ...selectUseReqListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reqdt: dayjs(u.reqdt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    if(type == "req"){
      selectUseReqLisReqList.value = response.data.list;
      selectUseReqListReqCnt.value = response.data.total;
    }else if(type == "complete"){
      selectUseReqLisCompleteList.value = response.data.list;
      selectUseReqListCompleteCnt.value = response.data.total;
    }
    totalPagesSelectUseReqList.value = response.data.pages;
    selectUseReqListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};

const moveToBmsUseReqdetail = (row, type, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  let reqtype = row.item.raw.reqtype;
  let reqstatus = row.item.raw.reqstatus;
  let moveName = '';
  if(reqtype === '01'){
    moveName = 'BmsUsereqviewdetail'; // 열람
  }else if(reqtype === '02'){
    moveName = 'BmsUsereqrecycledetail'; // 재사용
  }else if(reqtype === '03'){
    moveName = 'BmsUsereqprintdetail'; // 인쇄
  }else if(reqtype === '04' || reqtype === '05'){ // 반출(대출, 지출)
    if(reqstatus == "8"){
      moveName = 'BmsUsereqlendbackdetail'; 
    }else{
      moveName = 'BmsUsereqlenddetail'; 
    }
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
  const param = {apprreqid: "",reqtype:"",frompage: type == "req" ? "BmsUsereqlist" : "BmsUsecompletelist"};
  param.apprreqid = row.item.raw.apprreqid;
  param.reqtype = row.item.raw.reqtype;
  router.push({
    name: moveName,
      query: {
        ...param
      }
  })
};
/* =========================================================== */

/* ====================== 3. 예고문도래목록 ====================== */
const selectComingListloader = ref(true);
const currentPageSelectComingList = ref(1);
const totalPagesSelectComingList = ref(0);
const pageSizeSelectComingList = ref(10);
const handlePageChangeSelectComingList = () => {
  selectComingList(currentPageSelectComingList.value);
};
const selectComingListCondi = ref({
  mgmtno: "",
  secttl: "",
  copyno: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "mgmtid"
});
const selectComingListList = ref([]);
const selectComingListCnt = ref(0);
const staticColumnsselectComing = [
  { key: "indt", title: "등록일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "etc", title: "도래일", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectComingList = async (pageNum) => {
  selectComingListCondi.value.pageNum = parseInt(pageNum);
  selectComingListCondi.value.loginuserid = getUserLoginData.value.userid;
  selectComingListCondi.value.loginuserdeptid = getUserLoginData.value.deptid;
  selectComingListCondi.value.choicedDay = '3';
  selectComingListCondi.value.vgroupid = getUserLoginData.value.vgroupid;
  selectComingListloader.value = true;
  try {
    const response = await API.useAPI.selectComingList({ ...selectComingListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u.indt = dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD');
      u.reqState = transformSecstatus(u.secstatus);
      return { ...u };
    });
    selectComingListList.value = response.data.list;
    selectComingListCnt.value = response.data.total;
    totalPagesSelectComingList.value = response.data.pages;
    selectComingListloader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};
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
};
const moveComingDetailPage = (row, pathname, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  const param = {docid: row.docid, frompage: "BmsCominglist"};
  if(row.regirecvtype == '2'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  router.push({
    name: pathname,
    query: {
      ...param,
    },
  });
};
/* =========================================================== */


/* =============== 4. 처리할 인계인수서 =============== */
const selectTransProcToDoListLoader = ref(true);
const currentPageSelectTransProcToDoList = ref(1);
const totalPagesSelectTransProcToDoList = ref(0);
const pageSizeSelectTransProcToDoList = ref(10);
const handlePageChangeSelectTransProcToDoList = () => {
  selectTransProcToDoList(currentPageSelectTransProcToDoList.value);
};
const selectTransProcToDoListCondi = ref({ 
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "transferid"
});
const selectTransProcToDoListList = ref([]);
const selectTransProcToDoListCnt = ref(0);
const staticColumnsSelectTransProcToDoList = [
  { key: "reqdate", title: "요청일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprusername", title: "인수자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectTransProcToDoList = async (pageNum) => {
  selectTransProcToDoListLoader.value = true;
  selectTransProcToDoListCondi.value.pageNum = parseInt(pageNum);
  selectTransProcToDoListCondi.value.appruserid = getUserLoginData.value.userid;
  try {
    const response = await API.trnAPI.selectTransProcToDoList({ ...selectTransProcToDoListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reqdate: dayjs(u.reqdate, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectTransProcToDoListList.value = response.data.list;
    selectTransProcToDoListCnt.value = response.data.total;
    totalPagesSelectTransProcToDoList.value = response.data.pages;
    selectTransProcToDoListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};

const moveTransProcDetailPage = (row, pathname, activeLink) => {
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  if(pathname === 'BmsTrndetailcard'){
    row.item.raw.proc = "proc"
    row.item.raw.parentPage = "BmsComtransproctodolist";
  }
  if(pathname === 'BmsReceiptdetailcard'){
    row.item.raw.page = "BmsDctreceiptlist";
  }
  router.push({
    name: pathname,
    query: {
      ...row.item.raw,
    },
  });
};
/* =========================================================== */




/* ====================== 인수현황함 ====================== */
// const bmsTrnCurrentListLoader = ref(true);
// const currentPageBmsTrnCurrentList = ref(1);
// const totalPagesBmsTrnCurrentList = ref(0);
// const pageSizeBmsTrnCurrentList = ref(10);
// const handlePageChangeBmsTrnCurrentList = () => {
//   bmsTrnCurrentList(currentPageBmsTrnCurrentList.value);
// };
// const bmsTrnCurrentListCondi = ref({
//   pageNum: 1,
//   pageSize: 5,
// });
// const bmsTrnCurrentListList = ref([]);
// const bmsTrnCurrentListCnt = ref(0);
// const staticColumnsBmsTrnCurrentList = [
//   { key: "reqdate", title: "요청일자", width: "150px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
//   { key: "title", title: "제목", width: "500px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
//   { key: "requsername", title: "요청자", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
//   { key: "reqdeptname", title: "요청부서", width: "100px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
// ];
// const bmsTrnCurrentList = async (pageNum) => {
//   bmsTrnCurrentListLoader.value = true;
//   bmsTrnCurrentListCondi.value.pageNum = parseInt(pageNum);
//   bmsTrnCurrentListCondi.value.appruserid = getUserLoginData.value.userid;

//   try {
//     const response = await API.trnAPI.bmsTrnCurrentList({ ...bmsTrnCurrentListCondi.value }, urlPaths.value);
//     response.data.list = response.data.list.map((u) => {
//       u = { ...u, reqdate: dayjs(u.reqdate, 'YYYYMMDD').format('YYYY.MM.DD') };
//       return { ...u};
//     });
//     bmsTrnCurrentListList.value = response.data.list;
//     bmsTrnCurrentListCnt.value = response.data.total;
//     totalPagesBmsTrnCurrentList.value = response.data.pages;
//     bmsTrnCurrentListLoader.value = false;
//   } catch (error) {
//     alert("Server Error");
//   }
// };
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
