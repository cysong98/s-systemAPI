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
  <div class="lnb" :class="{'hidden': !leftVisible}">
    <div class="box-title">
      <h2 class="h2">비밀관리시스템</h2>
      <h3 class="h3" v-if="topMenu.data == 'B'">비밀문서</h3>
      <h3 class="h3" v-else-if="topMenu.data == 'C'">비밀관리</h3>
      <div v-else>
        <h3 class="h3" v-if="manageflag">관리자</h3>
        <h3 class="h3" v-else>게시판</h3>
      </div>
    </div>
    <div class="accordion-menu">
        <v-list v-model:opened="open">
          
          <!-- 비밀문서 -->
          <!-- <div v-if="topMenu.data == 'B'" v-for="(group, index) in docGroupWithNonSub" :key="index">
            <router-link :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
              <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
            </router-link>
          </div> -->

          <div v-if="topMenu.data == 'B'" v-for="(group, index) in docGroup" :key="index">
            <v-list-group v-if="group.items.length > 0" :value="group.value">
              <template v-slot:activator="{ props }">
                <v-list-item v-bind="props" :prepend-icon="group.icon" :title="group.title"></v-list-item>
              </template>
                <router-link
                  v-for="item in group.items"
                  :key="item.title"
                  :to="item.path"
                  :title="item.title"
                  :prepend-icon="item.icon"
                  class="link"
                  @click="clickMenu(breadcrumbs.topMenu + ',' + group.title + ',' + item.title)"
                  ><v-list-item v-if="(item.title === '접수현황함' && docmanageflag) || item.title !== '접수현황함'"  :class="{ 'active-link': activeLink === item.title }">{{ item.title }}</v-list-item>
                </router-link>
            </v-list-group>
            <router-link v-else :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
              <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
            </router-link>
          </div>


          <!-- 비밀관리 -->
          <!-- <div v-if="topMenu.data == 'C'" v-for="(group, index) in mngGroupWithNonSub" :key="index">
            <router-link :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
              <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
            </router-link>
          </div> -->

          <div v-if="topMenu.data == 'C'" v-for="(group, index) in mngGroup" :key="index">
            <v-list-group v-if="group.items.length > 0" :value="group.value">
              <template v-slot:activator="{ props }">
                <v-list-item v-bind="props" :prepend-icon="group.icon" :title="group.title"></v-list-item>
              </template>
              <router-link
                v-for="item in group.items"
                :key="item.title"
                :to="item.path"
                :title="item.title"
                :prepend-icon="item.icon"
                class="link"
                @click="clickMenu(breadcrumbs.topMenu + ',' + group.title + ',' + item.title)"
                ><v-list-item :class="{ 'active-link': activeLink === item.title }">{{ item.title }}</v-list-item>
              </router-link>
            </v-list-group>
            <router-link v-else :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
              <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
            </router-link>
          </div>


            <!-- 관리자 -->
            <!-- <div v-if="topMenu.data == 'D'" v-for="(group, index) in sysGroupWithNonSub" :key="index">
              <router-link :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
                <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
              </router-link>
            </div> -->

            <div v-if="topMenu.data == 'D'">
              <div v-if="manageflag" v-for="(group, index) in sysGroup" :key="index">
                <v-list-group v-if="group.items.length > 0" :value="group.value">
                  <template v-slot:activator="{ props }">
                    <v-list-item v-bind="props" :prepend-icon="group.icon" :title="group.title"></v-list-item>
                  </template>
                  <router-link
                    v-for="item in group.items"
                    :key="item.title"
                    :to="item.path"
                    :title="item.title"
                    :prepend-icon="item.icon"
                    class="link"
                    @click="clickMenu(breadcrumbs.topMenu + ',' + group.title + ',' + item.title)"
                    ><v-list-item :class="{ 'active-link': activeLink === item.title }">{{ item.title }}</v-list-item>
                  </router-link>
                </v-list-group>
                <router-link v-else :to="group.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + group.title)">
                  <v-list-item :prepend-icon="group.icon" :class="{ 'active-link': activeLink === group.title }">{{ group.title }}</v-list-item>
                </router-link>
              </div>
              <div v-else>
                <div v-for="(item, index) in sysGroupForUser" :key="index">
                  <router-link :to="item.path" class="link" @click="clickMenu(breadcrumbs.topMenu + ',' + item.title)">
                    <v-list-item :prepend-icon="item.icon" :class="{ 'active-link': activeLink === item.title }">{{ item.title.replace('관리', '') }}</v-list-item>
                  </router-link>
                </div>
              </div>
            </div>


          <!-- <router-link v-if="topMenu.data == 'B'" :to="docTodos[0].path" class="link" @click="clickMenu(docTodos[0].title)">
            <v-list-item :prepend-icon="docTodos[0].icon" :class="{ 'active-link': activeLink === docTodos[0].title }">{{ docTodos[0].title }}</v-list-item>
          </router-link>
          <router-link v-else :to="manageTodos[0].path" class="link" @click="clickMenu(manageTodos[0].title)">
            <v-list-item :prepend-icon="manageTodos[0].icon" :class="{ 'active-link': activeLink === manageTodos[0].title }">{{ manageTodos[0].title }}</v-list-item>
          </router-link>

          <v-list-group v-if="topMenu.data == 'B'" v-for="(group, index) in groups" :key="index" :value="group.value">
            <template v-slot:activator="{ props }">
              <v-list-item v-bind="props" :prepend-icon="group.icon" :title="group.title"></v-list-item>
            </template>
            <router-link
              v-for="item in group.items"
              :key="item.title"
              :to="item.path"
              :title="item.title"
              :prepend-icon="item.icon"
              class="link"
              @click="clickMenu(item.title)"
              ><v-list-item :class="{ 'active-link': activeLink === item.title }">{{ item.title }}</v-list-item>
            </router-link>
          </v-list-group>
          <v-list-group v-else v-for="(group, index2) in groups2" :key="index2" :value="group.value">
              <template v-slot:activator="{ props }">
                <v-list-item v-bind="props" :prepend-icon="group.icon" :title="group.title"></v-list-item>
              </template>
              <router-link
                v-for="item in group.items"
                :key="item.title"
                :to="item.path"
                :title="item.title"
                :prepend-icon="item.icon"
                class="link"
                @click="clickMenu(item.title)"
                ><v-list-item :class="{ 'active-link': activeLink === item.title }">{{ item.title }}</v-list-item>
              </router-link>
            </v-list-group>

          <router-link v-if="topMenu.data == 'B'" :to="secretViews[0].path" class="link" @click="activeLink = secretViews[0].title">
            <v-list-item :prepend-icon="secretViews[0].icon" :class="{ 'active-link': activeLink === secretViews[0].title }">{{ secretViews[0].title }}</v-list-item>
          </router-link>
          <router-link v-if="topMenu.data == 'B'" :to="secretManages[0].path" class="link" @click="activeLink = secretManages[0].title">
            <v-list-item :prepend-icon="secretManages[0].icon" :class="{ 'active-link': activeLink === secretManages[0].title }">{{ secretManages[0].title }}</v-list-item>
          </router-link> -->
        </v-list>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount, onBeforeUnmount, onMounted, watchEffect, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { useMainStore } from '/src/store/Main';


const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi, getUserLogin } = storeToRefs(loginStore)

const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('DetailLeft')
const route = useRoute()
const router = useRouter()

const activeLink = ref(null);
const open = ref([]);

const test = ref(true);
const manageflag = ref(false);
const docmanageflag = ref(false);

onMounted(async() => {
  if(getUserLoginData.value.hasOwnProperty('vgroupid')) {
    if (getUserLoginData.value.vgroupid.includes('5220')) {
      docmanageflag.value = true;
    }

    if (getUserLoginData.value.vgroupid.includes('4001')) {
      manageflag.value = true;
    }
  } 

  await selectLeftMenu();
})

// const docTodos = [{ title: '처리할일', path: '/dct/BmsTodolist', icon: 'icon-lnb-document-check' }];
// const manageTodos = [{ title: '처리할일', path: '/dct/ManageTodolist', icon: 'icon-lnb-document-check' }];
// const secretViews = [{ title: '비밀열람함', path: '/dct/BmsDcttodolistread', icon: 'icon-lnb-open-book' }];
// const secretManages = [{ title: '비밀관리기록부', path: '/dct/BmsDctmgmtregilist', icon: 'icon-lnb-info-box' }];

// const drafts = [
//   { title: '비밀작성', path: '/dct/BmsDctcreatecard' },
//   { title: '임시저장함', path: '/dct/BmsDcttemplist' },
//   { title: '기안완료함', path: '/dct/BmsDctreqcompletelist' }
// ]

// const approvals = [
//   { title: '결재대기함', path: '/dct/BmsDctwaitlist' },
//   { title: '진행비밀함', path: '/dct/BmsDctinglist' },
//   { title: '결재완료함', path: '/dct/BmsDctapprovalcompletelist' },
// ]

// const sends = [
//   { title: '발송대기함', path: '/dct/BmsDctsendwaitlist' },
//   { title: '발송현황함', path: '/dct/BmsDctsendcurrentlist' },
//   { title: '반송함', path: '/dct/BmsDctreturnlist' },
// ]

// const receipts = [
//   { title: '배부함', path: '/dct/BmsDctdistributelist' },
//   { title: '재배부함', path: '' },
//   { title: '접수대기함', path: '/dct/BmsDctreceiptlist' },
//   { title: '접수현황함', path: '' },
//   { title: '재지정함', path: '/dct/BmsReprocesslist' },
//   { title: '반송대기함', path: '/dct/BmsDctsendbackreadylist' },
//   { title: '비전자비밀등록', path: '' },
// ]

// const handovers = [
//   { title: '인계인수서 작성', path: '/trn/BmsDctreqadd' },
//   { title: '처리할 인계인수서(개인)', path: '/trn/BmsComtransproctodolist' },
//   { title: '처리할 인계인수서(부서)', path: '/trn/BmsComtransprocdepttodolist' },
//   { title: '요청한 인계인수서', path: '/trn/BmsTrnreqlist' },
//   { title: '처리한 인계인수서', path: '/trn/BmsTrncompletelist' },
//   { title: '임시저장함', path: '/trn/BmsTrntempsavelist' },
//   { title: '인수현황함', path: '/trn/BmsTrncurrentlist' },
// ]

// const usereqs = [
//   { title: '열람 요청', path: '/use/BmsUseViewReqList' },
//   { title: '재사용 요청', path: '/use/BmsReUseReqList' },
//   { title: '인쇄 요청', path: '/use/BmsUsePrintReqList' },
//   { title: '반출 요청', path: '/use/BmsUseTakeOutReqList' },
// ]

// const uses = [
//   { title: '처리할 요청서', path: '/use/BmsUsereqlist' },
//   { title: '기안한 요청서', path: '/use/BmsUsedraftlist' },
//   { title: '처리한 요청서', path: '/use/BmsUseproclist' },
//   { title: '임시저장함', path: '/use/BmsUsetemplist' },
//   { title: '요청서 완료처리', path: '/use/BmsUsecompletelist' },
// ]

// const comings = [
//   { title: '예고문도래목록', path: '/use/BmsCominglist' },
// ]

// const manageboards = [
//   { title: '공지사항 관리', path: '/com/BmsComnoticelist' },
//   { title: 'FAQ 관리', path: '/com/BmsComfaqlist' },
//   { title: 'Q&A 관리', path: '/com/BmsComqnalist' },
// ]

// const manageorgs = [
//   { title: '부서관리', path: '/com/BmsComorganizationlist' },
//   { title: '사용자관리', path: '/com/BmsComuserlist' },
//   { title: '관인관리', path: '/com/BmsComgwanlist' },
//   { title: '서명인관리', path: '/com/BmsComsignlist' },
// ]

// const managesys = [
//   { title: '메뉴관리', path: '/com/BmsCommenulist' },
//   { title: '코드관리', path: '/com/BmsDctcodelist' },
//   { title: '로그관리-실시간접속현황', path: '/com/BmsComrealtimelog' },
//   { title: '로그관리-시스템접속로그', path: '/com/BmsComsystemloginlog' },
//   { title: '서식관리', path: '/com/BmsDctformlist' },
// ]
// const conserve = [
//   { title: '존안비밀관리기록부', path: '/use/BmsUseConserveMgmtRegiList' }
// ]


// const groups = [
//   { value: '기안', title: '기안', icon: 'icon-lnb-cilp-file', items: drafts },
//   { value: '결재', title: '결재', icon: 'icon-lnb-sharp-pencil', items: approvals },
//   { value: '발송', title: '발송', icon: 'icon-lnb-paper-plane', items: sends },
//   { value: '접수', title: '접수', icon: 'icon-lnb-paper-plane', items: receipts },
//   { value: '게시판관리', title: '게시판관리', icon: 'mdi-table-cog', items: manageboards },
//   { value: '조직관리', title: '조직관리', icon: 'mdi-table-cog', items: manageorgs },
//   { value: '시스템관리', title: '시스템관리', icon: 'mdi-table-cog', items: managesys },
// ];

// const groups2 = [
//   { value: '인계인수', title: '인계인수', icon: 'icon-lnb-open-book', items: handovers },
//   { value: '비밀문서사용요청', title: '비밀문서사용요청', icon: 'icon-lnb-paper-plane', items: usereqs },
//   { value: '비밀문서사용', title: '비밀문서사용', icon: 'icon-lnb-paper-plane', items: uses },
//   { value: '예고문도래목록', title: '예고문도래목록', icon: 'icon-lnb-paper-plane', items: comings },
//   { value: '존안비밀관리기록부', title: '존안비밀관리기록부', icon: 'icon-lnb-paper-plane', items: conserve },
//   { value: '게시판관리', title: '게시판관리', icon: 'mdi-table-cog', items: manageboards },
//   { value: '조직관리', title: '조직관리', icon: 'mdi-table-cog', items: manageorgs },
//   { value: '시스템관리', title: '시스템관리', icon: 'mdi-table-cog', items: managesys },
// ];

const clickMenu = (data) => {
  let arr = [];
  activeLink.value = data;
  arr = activeLink.value.split(','); 
  breadcrumbs.value.activeLink = arr;
  sessionStorage.setItem("breadcrumbs.activeLink", arr);
  
  if(topMenu.data == 'B') {
    breadcrumbs.value.topMenu = '비밀문서';
  } else if (topMenu.data == 'C') {
    breadcrumbs.value.topMenu = '비밀관리';
  } else if (topMenu.data == 'D'){
    breadcrumbs.value.topMenu = '관리자';
  }
}

const menuList = ref([]);
const selectLeftMenu = async () => {
  try {
    const response = await API.comAPI.selectLeftMenu();
    menuList.value = response.data;
    makeGroup();
  } catch (error) {
    alert("Server Error");
  }
};

const docGroup = ref([]);
const mngGroup = ref([]);
const sysGroup = ref([]);
const sysGroupForUser = ref([]);
// const docGroupWithNonSub = ref([]);
// const mngGroupWithNonSub = ref([]);
// const sysGroupWithNonSub = ref([]);

const makeGroup = () => {
  menuList.value.parent.forEach(parent => {
    const data = {};
    data.value = parent.parentname;
    data.title = parent.parentname;
    data.path = parent.parenturl != '/' ? parent.parenturl : '';
    data.icon = parent.icon;
    data.items = data.items || [];
    
    menuList.value.sub.forEach(sub => {   
      if(parent.parentid == sub.parentid){
        const subData = {};
        subData.title = sub.subname;
        subData.path = sub.suburl;
        data.items.push(subData);
      }
    })

    if(parent.parentid.includes("DOC")){
      if(data.title === '접수') {
        let itemArr = [];
        data.items.map((item) => {
          // 배부함, 비전자비밀접수
          if(item.path === '/dct/BmsDctdistributelist' || item.path === "/dct/BmsDctReceivedOfflinecreatecard") {
            if (getUserLoginData.value.vgroupid != undefined) {
              if (getUserLoginData.value.vgroupid.includes('5210')) {
                itemArr.push(item);
              }
            }
          }
          // 재지정함
          if(item.path === '/dct/BmsReprocesslist') {
            if (getUserLoginData.value.vgroupid != undefined) {
              if (getUserLoginData.value.vgroupid.includes('5220')) {
                itemArr.push(item);
              }
            }
          }
          // 배부함, 재지정함, 비전자비밀접수 아닌 경우
          if(item.path !== '/dct/BmsDctdistributelist' && item.path !== '/dct/BmsReprocesslist' && item.path !== "/dct/BmsDctReceivedOfflinecreatecard")
            itemArr.push(item);
        })
        data.items = [...itemArr];
        docGroup.value.push(data);
      }
      else
        docGroup.value.push(data);
    }else if(parent.parentid.includes("MNG")){
      // 존안비밀관리기록부
      if(data.path == '/use/BmsUseConserveMgmtRegiList') {
        if(getUserLoginData.value.vgroupid != undefined) {
          if (getUserLoginData.value.vgroupid.includes('8010') || getUserLoginData.value.vgroupid.includes('8015')) {
            mngGroup.value.push(data);
          }
        }
      }
      // 이관비밀관리기록부
      if(data.path == '/use/BmsUseTransMgmtRegiList') {
        if (getUserLoginData.value.vgroupid != undefined) {
          if (getUserLoginData.value.vgroupid.includes('8050') || getUserLoginData.value.vgroupid.includes('8255')) {
            mngGroup.value.push(data);
          }
        }
      }
      // 존안비밀관리기록부, 이관비밀관리기록부 아닌 경우
      if(data.path != '/use/BmsUseConserveMgmtRegiList' && data.path != '/use/BmsUseTransMgmtRegiList') {
        mngGroup.value.push(data);
      }
    } else if(parent.parentid.includes("SYS")){ 
      sysGroup.value.push(data);
      // 일반 사용자를 위한 LeftMenu Group
      const community = 'SYS1000';
      if(parent.parentid === community) {
        data.items.forEach(item => {
          item.icon = 'mdi-table-cog';
          sysGroupForUser.value.push(item);
        });
      }
    }
  })
};

// // 변경 감지해서 left 메뉴 open
// let leftOpen = ref(breadcrumbs.value.activeLink);

// watchEffect(() => {
//   leftOpen.value = breadcrumbs.value.activeLink;
//   open.value.splice(0, open.value.length-1);

//   if(open.value.length < 3 && leftOpen.value.length > 1){
//     if (!open.value.includes(leftOpen.value[1].replace(" ", ""))) {
//       open.value.push(leftOpen.value[1].replace(" ", ""));
//     }
//   }
// });

// let leftOpen2 = ref(open.value);

// watchEffect(() => {
//   leftOpen2.value = open.value;

//   if(leftOpen2.value.length == 2) {
//     open.value.splice(1, 1);
//   }
// });



let leftOpen = ref(breadcrumbs.value.activeLink);

watch(() => breadcrumbs.value.activeLink, (newLeftOpen) => {
  if (JSON.stringify(newLeftOpen) !== JSON.stringify(leftOpen.value)) {
    leftOpen.value = newLeftOpen;
    open.value.splice(0, open.value.length);

    if (open.value.length < 3 && leftOpen.value.length > 1) {
      if (!open.value.includes(leftOpen.value[1].replace(" ", ""))) {
        open.value.push(leftOpen.value[1].replace(" ", ""));
      }
    }
  }
});

let leftOpen2 = ref(open.value);

watch(() => open.value, (newLeftOpen2) => {
  if (JSON.stringify(newLeftOpen2) !== JSON.stringify(leftOpen2.value)) {
    leftOpen2.value = newLeftOpen2;

    if (leftOpen2.value.length == 2) {
      open.value.splice(0, 1);
    }
  }
});

let leftOpen3 = ref(breadcrumbs.value.activeLink);

watchEffect(() => {
  leftOpen3.value = breadcrumbs.value.activeLink;
  open.value.splice(0, open.value.length-1);

  if(/*open.value.length < 3 && leftOpen3.value.length > 1*/ open.value.length == 0){
    if (leftOpen3.value[1] !== undefined && !open.value.includes(leftOpen3.value[1].replace(" ", ""))) {
      open.value.push(leftOpen3.value[1].replace(" ", ""));
    }
  }
});


const props = defineProps({
  leftVisible: Boolean,
})


</script>

<style lang="scss" scoped>
.hidden {
  width: 0;
  overflow: hidden;
}
</style>