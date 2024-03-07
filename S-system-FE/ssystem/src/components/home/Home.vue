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
  <div class="content-wrap">
    <div id="content">
      <div class="title-main">
        <h2>비밀관리시스템</h2>
        <p>Secret management system</p>
      </div>
      <div class="layout-main">
        <section class="section">
          <div class="shortcut">
            <button type="button" class="button-icon i11" @click="moveListPage('BmsDctcreatecard', 'B', '기안, 비밀작성')">
              <strong class="title">비밀문서 작성</strong>
            </button>
            <button type="button" class="button-icon i10" @click="moveListPage('BmsDctmgmtregilist', 'B', '비밀관리기록부')">
              <strong class="title">비밀관리기록부</strong>
            </button>
            <v-divider :thickness="2" style="margin-left:15px; margin-right:15px;"></v-divider>
            <button type="button" class="button-icon i01" @click="moveListPage('BmsDctwaitlist', 'B', '결재, 결재대기함')">
              <strong class="title">결재대기함</strong>
              <span class="badge">{{ selectWaitListCnt }}</span>
            </button>
            <button type="button" class="button-icon i02" @click="moveListPage('BmsUsereqlist', 'C', '비밀문서사용처리, 처리할 요청서')">
              <strong class="title">처리할 요청서</strong>
              <span class="badge">{{ selectUseReqListReqCnt }}</span>
            </button>
            <button type="button" class="button-icon i03" @click="moveListPage('BmsDctsendwaitlist', 'B', '발송, 발송대기함')">
              <strong class="title">발송대기함</strong>
              <span class="badge">{{ selectSendWaitListCnt }}</span>
            </button>
            <button type="button" class="button-icon i04" @click="moveListPage('BmsUsecompletelist', 'C', '비밀문서사용처리, 요청서 완료처리')">
              <strong class="title">요청서 완료처리</strong>
              <span class="badge">{{ selectUseReqListCompleteCnt }}</span>
            </button>
            <button type="button" class="button-icon i05" @click="moveListPage('BmsDctreceiptlist', 'B', '접수, 접수대기함')">
              <strong class="title">접수대기함</strong>
              <span class="badge">{{ selectReceiptListCnt }}</span>
            </button>
            <button type="button" class="button-icon i08" @click="moveListPage('BmsCominglist', 'C', '예고문도래목록')">
              <strong class="title">예고문도래목록</strong>
              <span class="badge">{{ selectComingListCnt }}</span>
            </button>
            <button type="button" class="button-icon i07" @click="moveListPage('BmsDcttodolistread', 'B', '비밀열람함')">
              <strong class="title">비밀열람함</strong>
              <span class="badge">{{ selectdctRdocCnt }}</span>
            </button>
            <button type="button" class="button-icon i06" @click="moveListPage('BmsComtransproctodolist', 'C', '인계인수, 처리할 인계인수서')">
              <strong class="title">처리할 인계인수서</strong>
              <span class="badge">{{ selectTransProcToDoListCnt }}</span>
            </button>
            <!-- <button type="button" class="button-icon i09" @click="moveListPage('BmsReprocesslist')">
              <strong class="title">재지정함</strong>
              <span class="badge">999</span>
            </button> -->
          </div>
        </section>
        <section class="section">
          <div class="tabs-type-01">
            <button
              v-for="button in buttons"
              :key="button.id"
              @click="btnClick(button.id)"
              :class="{ 'button': true, 'active': button.id === activeButtonId }"
            >
              <strong class="title">{{ button.title }}</strong>
            </button>
          </div>

          <div class="table-wrapper">
            <!-- 결재대기함 -->
            <div v-if="activeButtonId == 0">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsDctwaitlist', 'B', '결재, 결재대기함')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectWaitList"
                :items="selectWaitListList"
                :items-per-page="pageSizeSelectWaitList"
                :loading="selectWaitListLoader"
                @click:row="(event, item) => moveApproveDetailPage(item, 'BmsDctwaitcard', 'B', '결재, 결재대기함')"
                item-value="docid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.docttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.docttl }}
                  </div>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>

            <!-- 발송대기함 -->
            <div v-if="activeButtonId == 1">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsDctsendwaitlist', 'B', '발송, 발송대기함')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectSendWaitList"
                :items="selectSendWaitListList"
                :items-per-page="pageSizeSelectSendWaitList"
                :loading="selectSendWaitListLoader"
                @click:row="(event, item) => moveDetailPage(item, 'BmsDctsendwaitcard', 'B', '발송, 발송대기함')"
                item-value="docid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.docttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.docttl }}
                  </div>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>

            <!-- 접수대기함 -->
            <div v-if="activeButtonId == 2">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsDctreceiptlist', 'B', '접수, 접수대기함')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectReceiptList"
                :items="selectReceiptListList"
                :items-per-page="pageSizeSelectReceiptList"
                :loading="selectReceiptListLoader"
                @click:row="(event, item) => moveDetailPage(item, 'BmsReceiptdetailcard', 'B', '접수, 접수대기함')"
                item-value="enfdocid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.doctitle="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.doctitle }}
                  </div>
                </template>
                <template v-slot:item.chargerid="{ item }">
                  {{ item.raw.chargerid? 'O' : 'X' }}
                </template>
                <template v-slot:bottom></template>
              </v-data-table>
            </div>

            <!-- 비밀열람함 -->
            <div v-if="activeButtonId == 3">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsDcttodolistread', 'B', '비밀열람함')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectdctRdoc"
                :items="selectdctRdocList"
                :items-per-page="pageSizeSelectdctRdoc"
                :loading="selectdctRdocLoader"
                @click:row="(event, item) => moveReadDetailPage(item.item.raw, 'BmsDctmgmtregidetailcard', 'B', '비밀열람함')"
                item-value="docid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.secttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.secttl }}
                  </div>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>
          </div>
        </section>
        <section class="section">
          <div class="tabs-type-01">
            <button
              v-for="button in buttons2"
              :key="button.id"
              @click="btnClick2(button.id)"
              :class="{ 'button': true, 'active': button.id === activeButtonId2 }"
            >
              <strong class="title">{{ button.title }}</strong>
            </button>
          </div>

          <div class="table-wrapper">
            <!-- 처리할 요청서 -->
            <div v-if="activeButtonId2 == 0">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsUsereqlist', 'C', '비밀문서사용처리, 처리할 요청서')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectUseReqList"
                :items="selectUseReqLisReqList"
                :items-per-page="pageSizeSelectUseReqList"
                :loading="selectUseReqListLoader"
                @click:row="(event, item) => moveToBmsUseReqdetail(item, 'C', '비밀문서사용처리, 처리할 요청서')"
                item-value="apprreqid"
                class="table-type-01"
                hover
              >
              <template v-slot:item.reqttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.reqttl }}
                  </div>
                </template>
              <template v-slot:bottom />
              </v-data-table>
            </div>

            <!-- 요청서 완료처리 -->
            <div v-if="activeButtonId2 == 1">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsUsecompletelist', 'C', '비밀문서사용처리, 요청서 완료처리')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectUseReqList"
                :items="selectUseReqLisCompleteList"
                :items-per-page="pageSizeSelectUseReqList"
                :loading="selectUseReqListLoader"
                @click:row="(event, item) => moveToBmsUseReqdetail(item, 'C', '비밀문서사용처리, 요청서 완료처리')"
                item-value="apprreqid"
                class="table-type-01"
                hover
              >
              <template v-slot:item.reqttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.reqttl }}
                  </div>
                </template>
              <template v-slot:bottom />
              </v-data-table>
            </div>

            <!-- 예고문도래목록 -->
            <div v-if="activeButtonId2 == 2">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsCominglist', 'C', '예고문도래목록')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsselectComing"
                :items="selectComingListList"
                :items-per-page="pageSizeSelectComingList"
                :loading="selectComingListLoader"
                @click:row="(event, item) => moveComingDetailPage(item.item.raw, 'BmsDctmgmtregidetailcard', 'C', '예고문도래목록')"
                item-value="mgmtid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.secttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.secttl }}
                  </div>
                </template>
                <template v-slot:item.etc="{ item }">
                  <span>
                      {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').isBefore(dayjs(), 'day') ? '만료' : '도래' }}
                      {{ dayjs(item.raw.protdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') }}
                      {{ item.raw.copyno.includes("원") ? transReclassInfo(item.raw.orgnreclass) : transReclassInfo(item.raw.copyreclass) }}
                  </span>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>


            <!-- 처리할 인계인수서 -->
            <div v-if="activeButtonId2 == 3">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsComtransproctodolist', 'C', '인계인수, 처리할 인계인수서')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsSelectTransProcToDoList"
                :items="selectTransProcToDoListList"
                :items-per-page="pageSizeSelectTransProcToDoList"
                :loading="selectTransProcToDoListLoader"
                @click:row="(event, item) => moveDetailPage(item, 'BmsTrndetailcard', 'C', '인계인수, 처리할 인계인수서')"
                item-value="transferid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.title="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.title }}
                  </div>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div>

            <!-- 인수현황함 -->
            <!-- <div v-if="activeButtonId2 == 4">
              <div class="box-flex justify-end mb-3">
                <button type="button" class="button-all" @click="moveListPage('BmsTrncurrentlist', 'C', '인계인수, 인수현황함')">더보기</button>
              </div>
              <v-data-table
                :headers="staticColumnsBmsTrnCurrentList"
                :items="bmsTrnCurrentListList"
                :items-per-page="pageSizeBmsTrnCurrentList"
                :loading="bmsTrnCurrentListLoader"
                @click:row="(event, item) => moveDetailPage(item, 'BmsTrndetailcard', 'C', '인계인수, 인수현황함')"
                item-value="transferid"
                class="table-type-01"
                hover
                >
                <template v-slot:item.title="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.title }}
                  </div>
                </template>
                <template v-slot:bottom></template>
              </v-data-table> 
            </div> -->
          </div>
        </section>
        <section class="section">
          <div class="customer faq">
            <strong class="title">FAQ</strong>
            <p class="paragraph">비밀문서 작성하기 메뉴얼</p>
            <button class="link" @click="moveListPage('BmsComfaqlist','D','게시판관리, FAQ 관리')">더보기</button>
          </div>
        </section>
        <section class="section">
          <div class="customer qna">
            <strong class="title">Q&A</strong>
            <p class="paragraph">비밀관리 시스템 이용 중<br>오류 발생 해결 방법 문의</p>
            <button class="link" @click="moveListPage('BmsComqnalist','D','게시판관리, Q&A 관리')">더보기</button>
          </div>
        </section>
        <section class="section">
          <div class="notice-report-wrapper">
            <div class="notice">
              <v-carousel
                height="50px"
                direction="vertical"
                :cycle=true
                :interval=3000
                :hide-delimiters=true
                :show-arrows=false
                :continuous=true
              >
                <v-carousel-item v-for="(item, index) in noticeData" :key="index">
                  <RouterLink :to="`/com/BmsComnoticedetail?ntcid=${item.ntcid}`" class="link" @click="changeStoreData('D', '게시판관리, 공지사항 관리')">
                    <strong class="title" :class="item.new ? 'new' : ''">{{ item.title }}</strong>
                    <span class="arrow"></span>
                  </RouterLink>
                </v-carousel-item>
              </v-carousel>
            </div>
            <div class="report">
              <button type="button" class="button qna" @click="moveListPage('BmsComqnainsert', 'D', '게시판관리, Q&A 관리')">Q&A 작성하기</button>
              <!-- <button type="button" class="button secret" @click="moveListPage('BmsDctcreatecard', 'B', '기안, 비밀작성')">비밀문서 작성하기</button> -->
            </div>
          </div>
        </section>
      </div>
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
import { storeToRefs } from 'pinia';
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import HomeTop from "@/components/main/HomeTop.vue";

const name = ref('HomeThing')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi, alertCnt } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
import { transformSecstatus, transformDate, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
onBeforeMount(() => {
})

onMounted(async () => {
  if (getUserLoginData.value.username != undefined && getUserLoginData.value.username != null && getUserLoginData.value.username != "") {
    await selectFuncs1();
    await selectFuncs2();
    await selectNoticeList(selectNoticeListCondi.value.pageNum); // 공지사항
    await checkAbsenceAndUpdate();
  }
})

const selectFuncs1 = async()=>{
  await selectWaitList(selectWaitListCondi.value.pageNum); // 결재대기함
  await selectSendWaitList(selectSendWaitListCondi.value.pageNum); // 발송대기함
  await selectReceiptList(selectReceiptListCondi.value.pageNum); // 접수대기함
  await selectdctRdoc(selectdctRdocCondi.value.pageNum); // 비밀열람함
};

const selectFuncs2 = async()=>{
  await selectUseReqListReq(selectUseReqListCondi.value.pageNum); // 처리할 요청서
  await selectUseReqListComplete(selectUseReqListCondi.value.pageNum); // 요청서 완료처리
  await selectComingList(selectComingListCondi.value.pageNum); // 예고문도래목록
  await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum); // 처리할 인계인수서
}


const buttons = [
  { id: 0, title: "결재대기함" },
  { id: 1, title: "발송대기함" },
  { id: 2, title: "접수대기함" },
  { id: 3, title: "비밀열람함" }
];

const buttons2 = [
  { id: 0, title: "처리할 요청서" },
  { id: 1, title: "요청서 완료처리" },
  { id: 2, title: "예고문도래목록" },
  { id: 3, title: "처리할 인계인수서" },
  // { id: 4, title: "인수현황함" }
];

const activeButtonId = ref(0);
const activeButtonId2 = ref(0);


watch(() => activeButtonId.value, () => {
  selectFuncs1();
});
watch(() => activeButtonId2.value, () => {
  selectFuncs2();
});

const btnClick = (id) => {
  activeButtonId.value = id;
};
const btnClick2 = (id) => {
  activeButtonId2.value = id;
  if(id==0){ // 처리할 요청서
    selectUseReqListReq(1);
  }else if(id==1){ // 요청서 완료처리
    selectUseReqListComplete(1);
  }
};

const changeStoreData = (data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;
};

const moveListPage = (pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  router.push({
    name: pathname,
  });
};

const moveDetailPage = (row, pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  row.page = 'Home';
  if(pathname === 'BmsTrndetailcard'){
    row.item.raw.proc = "proc"
    row.item.raw.parentPage = "Home";
  }
  if(pathname === 'BmsReceiptdetailcard'){
    pathname = row.item.raw.regirecvtype == '1' ? 'BmsReceiptdetailcard' : 'BmsDctReceivedOfflinesharecard';
    row.item.raw.page = "BmsDctreceiptlist";
    row.item.raw.frompage = "BmsDctreceiptlist";
  }
  router.push({
    name: pathname,
    params: {
      ...row.item.raw,
    },
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
  { key: "reportdt", title: "둥록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "요청자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectWaitList = async (pageNum) => {
  selectWaitListLoader.value = true;
  selectWaitListCondi.value.pageNum = parseInt(pageNum);
  selectWaitListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectWaitList({ ...selectWaitListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reportdt: dayjs(u.reportdt, 'YYYYMMDD').format('YYYY.MM.DD') };
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
const moveApproveDetailPage = (row, pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
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
  { key: "reportdt", title: "둥록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "objname", title: "발신자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];

const selectSendWaitList = async (pageNum) => {
  selectSendWaitListLoader.value = true;
  selectSendWaitListCondi.value.pageNum = parseInt(pageNum);
  selectSendWaitListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectSendWaitList({ ...selectSendWaitListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reportdt: dayjs(u.reportdt, 'YYYYMMDD').format('YYYY.MM.DD') };
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
  { key: "indt", title: "둥록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "doctitle", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "me", title: "접수자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
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



/* ====================== 4. 비밀열람함 ====================== */
const selectdctRdocLoader = ref(true);
const currentPageSelectdctRdoc = ref(1);
const totalPagesSelectdctRdoc = ref(0);
const pageSizeSelectdctRdoc = ref(10);
const handlePageChangeSelectdctRdoc = () => {
  selectdctRdoc(currentPageSelectdctRdoc.value);
};
const selectdctRdocCondi = ref({ 
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 5,
  sortDirection: "ASC",
  sortItem: "docid"
});
const selectdctRdocList = ref([]);
const selectdctRdocCnt = ref(0);
const staticColumnsSelectdctRdoc = [
  { key: "indt", title: "둥록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectdctRdoc = async (pageNum) => {
  selectdctRdocLoader.value = true;
  selectdctRdocCondi.value.pageNum = parseInt(pageNum);
  selectdctRdocCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectdctRdoc({ ...selectdctRdocCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectdctRdocList.value = response.data.list;
    selectdctRdocCnt.value = response.data.total;
    totalPagesSelectdctRdoc.value = response.data.pages;
    selectdctRdocLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};

const moveReadDetailPage = (row, pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
  activeLink = breadcrumbs.value.topMenu + ', ' + activeLink;

  let arr = [];
  arr = activeLink.split(',');
  breadcrumbs.value.activeLink = arr;

  const param = {docid: row.docid, frompage: "BmsDcttodolistread"};
  if(row.regirecvtype == '2'){
    if(row.paperrecgubun == '접수'){
      pathname = 'BmsDctReceivedOfflinecompletecard';
    }else{
      pathname = 'BmsDctProductOfflinecompletecard';
    }
  }
  router.push({
    name: pathname,
    params: {
      ...param,
    },
    query: {
      ...param,
    },
  });
};
/* =========================================================== */



/* ====================== 5. 처리할 요청서 && 6. 요청서 완료처리 ====================== */
const selectUseReqListLoader = ref(true);
const currentPageSelectUseReqList = ref(1);
const totalPagesSelectUseReqList = ref(0);
const pageSizeSelectUseReqList = ref(10);
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
  { key: "reqdt", title: "요청일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqtypenm", title: "구분", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "reqname", title: "요청자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
];
const selectUseReqListReq = async (pageNum) => {
  selectUseReqListLoader.value = true;    
  selectUseReqListCondi.value.pageNum = parseInt(pageNum);
  selectUseReqListCondi.value.reqid = "";
  selectUseReqListCondi.value.apprid = getUserLoginData.value.userid;
  selectUseReqListCondi.value.pagereqtype = "req";
  try {
    const response = await API.useAPI.selectUseReqList({ ...selectUseReqListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reqdt: dayjs(u.reqdt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectUseReqLisReqList.value = response.data.list;
    selectUseReqListReqCnt.value = response.data.total;
    totalPagesSelectUseReqList.value = response.data.pages;
    selectUseReqListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};

const selectUseReqListComplete = async (pageNum) => {
  selectUseReqListLoader.value = true;    
  selectUseReqListCondi.value.pageNum = parseInt(pageNum);
  selectUseReqListCondi.value.reqid = getUserLoginData.value.userid;
  selectUseReqListCondi.value.apprid = "";
  selectUseReqListCondi.value.pagereqtype = "complete";
  try {
    const response = await API.useAPI.selectUseReqList({ ...selectUseReqListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, reqdt: dayjs(u.reqdt, 'YYYYMMDD').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectUseReqLisCompleteList.value = response.data.list;
    selectUseReqListCompleteCnt.value = response.data.total;
    totalPagesSelectUseReqList.value = response.data.pages;
    selectUseReqListLoader.value = false;
  } catch (error) {
    alert("Server Error");
  }
};

const moveToBmsUseReqdetail = (row, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
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
  const param = {apprreqid: "",reqtype:"",frompage: activeButtonId2.value == 0 ? "BmsUsereqlist" : "BmsUsecompletelist"};
  param.apprreqid = row.item.raw.apprreqid;
  param.reqtype = row.item.raw.reqtype;
  router.push({
    name: moveName,
    params: {
       ...param
      },
      query: {
        ...param
      }
  })
};
/* =========================================================== */


/* ====================== 7. 예고문도래목록 ====================== */
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
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "mgmtid"
});
const selectComingListList = ref([]);
const selectComingListCnt = ref(0);
const staticColumnsselectComing = [
  { key: "indt", title: "등록일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "secttl", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "etc", title: "도래일", width: "180px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "담당자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
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
  selectComingListloader.value = false;
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
const moveComingDetailPage = (row, pathname, data, activeLink) => {
  topMenu.value.data = data;

  if (data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (data == 'D') {
    breadcrumbs.value.topMenu = '관리자';
  }
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
    params: {
      ...param,
    },
    query: {
      ...param,
    },
  });
};
/* =========================================================== */

/* ====================== 8. 처리할 인계인수서 ====================== */
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
  { key: "reqdate", title: "요청일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "title", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "requsername", title: "인계자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprusername", title: "인수자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
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
//   { key: "reqdate", title: "요청일자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
//   { key: "title", title: "제목", width: "300px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
//   { key: "requsername", title: "요청자", width: "100px", sortable: false, filterable: true, align: "center", divider: true, class: "custom-class" },
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


/* ====================== etc. 공지사항 ====================== */
const selectNoticeListCondi = ref({
  pageNum: 1,
  pageSize: 5,
  sortDirection: "DESC",
  sortItem: "indt"
});
const selectNoticeListList = ref([]);
const noticeData = ref([]);
const selectNoticeList = async (pageNum) => {
  selectNoticeListCondi.value.pageNum = parseInt(pageNum);
  try {
    selectNoticeListCondi.value.userid = getUserLoginData.value.userid;
    // 시스템관리자의 경우 userid를 입력하지 않는다.
    if(getUserLoginData.value.vgroupid.includes('4001'))
      selectNoticeListCondi.value.userid = "";

    const response = await API.comAPI.selectNoticeList({ ...selectNoticeListCondi.value }, urlPaths.value);
    noticeData.value = response.data.list;
    // selectNoticeListList.value = response.data.list;
    // noticeData.value = selectNoticeListList.value.map(noti=>noti.title);
  } catch (error) {
    alert("Server Error");
  }
};
/* =========================================================== */

// 부재 등록 후 복귀시 업데이트
const checkAbsenceAndUpdate = async () => {
  if(getUserLoginData.value.absenceyn != undefined && getUserLoginData.value.absenceyn == 'Y'){
    updateAbsence();
  }
}

const updateAbsenceCondi = ref({
  userid: "",
  absenceyn: "",
  absenceid: "",
  absencedt: "",
});

const updateAbsence = async () => {
  updateAbsenceCondi.value.userid = getUserLoginData.value.userid;
  updateAbsenceCondi.value.absenceyn = 'N';
  updateAbsenceCondi.value.absenceid = null;
  updateAbsenceCondi.value.absencedt = null;
  API.loginAPI
    .updateAbsence(updateAbsenceCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200 && alertCnt.value == 1) {
        alert(`${getUserLoginData.value.username}님의 부재 등록이 해제되었습니다.`);
        alertCnt.value++;
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}

</script>

<style lang="scss" scoped></style>
