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
    <div v-if="isloading === true" class="overlay">
      <div class="spinner"></div>
    </div>
    <div class="box-title">
      <h4 class="h4">조직관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">부서 / 사용자관리</h5>
        </div>
      </section>

      <section class="section">
        <div class="box-condition">
          <table>
            <tr>
              <th>검색어</th>
              <td>
                <v-select
                  label=""
                  v-model="comboSelected"
                  :items="comboData"
                  item-title="view"
                  item-value="key"
                  variant="outlined"
                  hide-details="auto"
                />
                <v-text-field
                  @keyup.enter="selectSearch(1)"
                  v-model="comboInputData"
                  maxlength="255"
                  variant="outlined"
                  hide-details="auto"
                  required
                  clearable
                />
              </td>
              <th></th>
              <td></td>
              <th></th>
              <td></td>
            </tr>
          </table>
          <v-btn class="magnify-solid" @click="selectSearch(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>

        <div class="box-flex justify-space-between flex-start">
          <div class="bordered" style="width: 29%;"><br/>
            <div class="box-title">
              <h6 class="h6">부서관리</h6>
              <div class="action-buttons">
                <v-btn class="button" @click="toggleOrgInsertPopup()">등록</v-btn>
                <v-btn class="button" @click="toggleOrgUpdatePopup()">수정</v-btn>
                <v-btn class="button" @click="toggleOrgDeletePopup()">삭제</v-btn>
                <div></div>
              </div>
            </div><br/>
            <Tree
              :nodes="treeData"
              :use-checkbox="useCheckbox"
              :use-icon="true"
              :search-text="searchText"
              style="height: 539px; border-color: #ffffff00;"
              @nodeClick="onNodeClick"
            />
          </div>

          <div class="bordered w70 px-5"><br/>
            <div class="box-title">
              <h6 class="h6">사용자관리</h6>
              <div class="action-buttons">
                <v-btn class="button" @click="toggleUserInsertPopup()">등록</v-btn>
                <v-btn class="button" @click="toggleUserUpdatePopup()">수정</v-btn>
                <v-btn class="button" @click="toggleUserDeletePopup()">삭제</v-btn>
              </div>
            </div>
            
            <div class="box-flex justify-space-between pb-2">
              <span>전체: {{ totalItem }}개</span>
            </div>

            <v-data-table
              @click:row="(event, item) => toggleUserDetailPopup(item)"
              v-model="checkedListSelectUserList"
              :headers="staticColumnsSelectUserList"
              :items="selectUserListList"
              :items-per-page="pageSizeSelectUserList"
              :item-value="selectUserListIdentifier"
              :loading="userLoader"
              :no-data-text="noDataText"
              fixed-header
              show-select
              hover
              class="table-type-05"
              height="450px"
            ><template v-slot:bottom />
            </v-data-table>
            <v-pagination
              v-model="currentPageSelectUserList"
              :length="totalPagesSelectUserList"
              total-visible="5"
              prev-icon="mdi-menu-left"
              next-icon="mdi-menu-right"
              @click="handlePageChangeSelectUserList"
            ></v-pagination>
          </div>
        </div>
      </section>
    </div>
  </div>

  <!-- delete task -->
  <v-dialog v-model="visibleBmsComOrgDeletePopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComOrgDeletePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComDeletePopup
        v-if="visibleBmsComOrgDeletePopup"
        :alertMsg="`삭제 대상: ${orgArgs.label}(${orgArgs.orgid})\n\n대상 부서코드와 모든 하위 부서코드가 영구 삭제됩니다.\n사용자가 존재하면 삭제되지 않습니다.`"
        :returnFunc="deleteOrganizationBIA"
      ></BmsComDeletePopup>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComUserDeletePopup" style="width: 700px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComUserDeletePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComDeletePopup
        v-if="visibleBmsComUserDeletePopup"
        :alertMsg="'사용자가 영구 삭제됩니다.'"
        :returnFunc="deleteUserBIA"
      ></BmsComDeletePopup>
    </v-card>
  </v-dialog>


  <!-- insert task -->
  <v-dialog v-model="visibleBmsComOrgInsertPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title> 부서 등록
        </v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComOrgInsertPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrganizationInsert
        v-if="visibleBmsComOrgInsertPopup"
        :toggleFunc="toggleOrgInsertPopup"
        :returnFunc="toggleToporgInsertPopup"
      ></BmsComOrganizationInsert>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComToporgInsertPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>최상위부서 관리</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComToporgInsertPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComToporgPopup
        v-if="visibleBmsComToporgInsertPopup"
        :toggleFunc="toggleToporgInsertPopup"
      ></BmsComToporgPopup>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComUserInsertPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>사용자 등록</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComUserInsertPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComUserInsert
        v-if="visibleBmsComUserInsertPopup"
        :args="orgArgs"
        :toggleFunc="toggleUserInsertPopup"
        :returnFunc="returnUserInsertPopup"
      ></BmsComUserInsert>
    </v-card>
  </v-dialog>

  <!-- update task -->
  <v-dialog v-model="visibleBmsComOrgUpdatePopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부서 수정</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComOrgUpdatePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrganizationUpdate
        v-if="visibleBmsComOrgUpdatePopup"
        :args="orgArgs"
        :toggleFunc="toggleOrgUpdatePopup"
        :returnFunc="toggleToporgInsertPopup"
      ></BmsComOrganizationUpdate>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComUserUpdatePopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>사용자 수정</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComUserUpdatePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComUserUpdate
        v-if="visibleBmsComUserUpdatePopup"
        :args="userArgs"
        :toggleFunc="toggleUserUpdatePopup"
        :returnFunc="returnUserUpdatePopup"
      ></BmsComUserUpdate>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComUserDetailPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>사용자 상세</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComUserDetailPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComUserDetail
        v-if="visibleBmsComUserDetailPopup"
        :args="userArgs"
        :toggleFunc="toggleUserDetailPopup"
      ></BmsComUserDetail>
    </v-card>
  </v-dialog>

</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";
import BmsComOrganizationInsert from "./BmsComOrganizationInsert.vue";
import BmsComOrganizationUpdate from "./BmsComOrganizationUpdate.vue";
import BmsComUserInsert from "./BmsComUserInsert.vue";
import BmsComUserUpdate from "./BmsComUserUpdate.vue";
import BmsComUserDetail from "./BmsComUserDetail.vue";
import BmsComToporgPopup from "./BmsComToporgPopup.vue";

import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, validCheckSearchInput } from "@/utils/Utils.js";

const name = ref('BmsComOrganizationList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("orgname")
const comboInputData = ref("")
const comboData = [
  { key: "orgname", view: "부서명" },
  { key: "username", view: "사용자명" }
]
const userComboSelected = ref("")
const userComboInputData = ref("")

// for selectOrganizationList
const organizationLoader = ref(true)
const checkedListSelectOrganizationList = ref([])
const selectOrganizationListList = ref([])
const selectOrganizationListDefCondi = {
  orgid: "",
  orgname: "",
  pageNum: 1,
  pageSize: 1000,
}
const selectOrganizationListCondi = ref({ ...selectOrganizationListDefCondi })
const selectOrganizationListCondiCheck = ref({ value: { ...selectOrganizationListDefCondi }, flag: { ...selectOrganizationListDefCondi } })
// for selectUserList
const userLoader = ref(true)
const checkedListSelectUserList = ref([])
const selectUserListList = ref([])
const selectUserListDefCondi = {
  userid: "",
  username: "",
  deptname: "",
  deptid: "",
  pageNum: 1,
  pageSize: 10,
}
const selectUserListCondi = ref({ ...selectUserListDefCondi })
const selectUserListCondiCheck = ref({ value: { ...selectUserListDefCondi }, flag: { ...selectUserListDefCondi } })


/* ========================= pagination ========================= */
const totalPagesSelectOrganizationList = ref(0)
const currentPageSelectOrganizationList = ref(1)
const pageSizeSelectOrganizationList = ref(1000)

const totalPagesSelectUserList = ref(0)
const currentPageSelectUserList = ref(1)
const pageSizeSelectUserList = ref(10)

const handlePageSizeChangeSelectUserList = () => {
  selectUserListCondi.value.pageSize = pageSizeSelectUserList.value;
  currentPageSelectUserList.value = 1;
  selectUserList(currentPageSelectUserList.value);
}
const handlePageChangeSelectUserList = () => {
  checkedListSelectUserList.value = [];
  selectUserList(currentPageSelectUserList.value);
}
/* ============================================================ */


/* ============================== selectOrganizationList ============================== */
onMounted(async () => {
  setCondiClear(selectOrganizationListCondiCheck.value, selectOrganizationListCondi.value);
  setQueries(route, selectOrganizationListCondi.value, selectOrganizationListDefCondi);
  await selectOrganizationList(1);
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  
  setCondiClear(selectUserListCondiCheck.value, selectUserListCondi.value);
  setQueries(route, selectUserListCondi.value, selectUserListDefCondi);
  await selectUserList(selectUserListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectOrganizationListCondi.value, selectOrganizationListDefCondi);
  await selectOrganizationList(selectOrganizationListCondi.value.pageNum);
})

watch(() => _.cloneDeep(selectOrganizationListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectOrganizationListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectOrganizationListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectOrganizationListCondiCheck.value, selectOrganizationListCondi.value, selectOrganizationListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectOrganizationListCondi.value["orgname"] = "";
  selectUserListCondi.value["username"] = "";
  if(comboSelected === 'orgname') {
    setCondiClear(selectUserListCondiCheck.value, selectUserListCondi.value);
    setQueries(route, selectUserListCondi.value, selectUserListDefCondi);
    await selectUserList(1);
  }
  else {
    setCondiClear(selectOrganizationListCondiCheck.value, selectOrganizationListCondi.value);
    setQueries(route, selectOrganizationListCondi.value, selectOrganizationListDefCondi);
    await selectOrganizationList(1);

    searchText.value = "";
    treeData.value = makeTree.value;
    if (treeData.value.length > 0) {
      treeData.value[0].expanded = true;
    }
  }
  setCondiChanged(selectOrganizationListCondiCheck.value, selectOrganizationListCondi.value, selectOrganizationListCondi.value);
  setCondiChanged(selectUserListCondiCheck.value, selectUserListCondi.value, selectUserListCondi.value);
})

/* ==================================================================================== */

/* ============================== selectUserList ============================== */

watch(route, async (route) => {
  setQueries(route, selectUserListCondi.value, selectUserListDefCondi);
  await selectUserList(selectUserListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUserListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUserListCondiCheck.value, newVal, oldVal);
})

watch(userComboInputData, async (userComboInputData) => {
  if(!(userComboSelected.value == "" || userComboSelected.value == undefined)) {
    selectUserListCondi.value[userComboSelected.value] = userComboInputData;
    selectUserListCondi.value.deptid = "";
  }
  setCondiChanged(selectUserListCondiCheck.value, selectUserListCondi.value, selectUserListCondi.value);
})

/* ============================================================================= */

const staticColumnsSelectUserList = [
  { title: "아이디", key: "userid", width: "30%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이름", key: "username", width: "15%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서", key: "deptname", width: "25%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직위", key: "position", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직급", key: "grade", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ======================= multi selection ======================= */

const selectUserListIdentifier = computed(() => (item) => {
  item.identifier = {
    userid: item.userid,
    username: item.username,
    deptname: item.deptname
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectUserListFinalData = computed(() => {
  // 체크된 항목만 필터링
  const filteredData = selectUserListList.value.filter((item) =>
    checkedListSelectUserList.value.includes(item.identifier))

  // identifier 속성을 제거한 복사본을 생성하여 반환
  return filteredData.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});
/* ============================================================== */

/* ============================= tree ============================= */
const makeTree = computed(() => {
  const treeNodes = [];

  const buildTree = (orgData, idPrefix) => {
    const treeNode = {
      id: idPrefix,
      orgid: orgData.orgid,
      label: orgData.orgname,
      toporgid: orgData.toporgid,
      orgdepth: orgData.orgdepth,
      nodes: []
    };
    const childOrgs = selectOrganizationListList.value.filter(item => item.parentorgid === orgData.orgid);
    childOrgs.forEach((childOrg, index) => {
      const childIdPrefix = idPrefix + (index + 1);
      treeNode.nodes.push(buildTree(childOrg, childIdPrefix));
    });
    return treeNode;
  };

  const rootOrgs = selectOrganizationListList.value.filter(item => item.orgdepth === 1);
  rootOrgs.forEach((rootOrg, index) => {
    const rootIdPrefix = (index + 1).toString();
    const rootNode = buildTree(rootOrg, rootIdPrefix);
    treeNodes.push(rootNode);
  });
  return treeNodes;
});

// option
const treeData = ref([]);
const useCheckbox = ref(false);
const searchText = ref("");

const onNodeClick = async (node) => {
  selectUserListCondi.value.deptid = node.orgid;
  userComboInputData.value = "";
  await selectUserList(1);
  
  orgArgs.value = node;
  checkedListSelectUserList.value = [];
  treeNodeColor(node);
};

/* =========================================================== */

/* Select Function */
const noDataText = ref("조회된 데이터가 없습니다.");
const selectOrganizationList = async (pageNum) => {

  organizationLoader.value = true;
  selectOrganizationListCondi.value.pageNum = parseInt(pageNum);

  if(!(comboSelected.value === "" || comboSelected.value === undefined)){
    if(validCheckSearchInput(comboInputData.value)) {
      searchText.value = comboInputData.value;
    }
    else
      return;
  }
  try {
    const response = await API.comAPI.selectOrganizationList({ ...selectOrganizationListCondi.value }, urlPaths.value);

    selectOrganizationListList.value = response.data.list;
    totalPagesSelectOrganizationList.value = response.data.pages;
    organizationLoader.value = false;
    
    setCondiClear(selectOrganizationListCondiCheck.value, selectOrganizationListCondi.value)
  } catch (error) {
    console.log(error);
  }
}

let totalItem = 0;
const selectUserList = async (pageNum) => {
  selectUserListCondi.value.pageNum = parseInt(pageNum);
  userLoader.value = true;

  if(!(userComboSelected.value === "" || userComboSelected.value === undefined))
    selectUserListCondi.value[userComboSelected.value] = userComboInputData.value;
  try {
    const response = await API.comAPI.selectUserList({ ...selectUserListCondi.value }, urlPaths.value);
    
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')};
      return { ...u};
    });

    selectUserListList.value = response.data.list;
    totalPagesSelectUserList.value = response.data.pages;
    userLoader.value = false;
    totalItem = response.data.total;

    setCondiClear(selectUserListCondiCheck.value, selectUserListCondi.value)
  } catch (error) {
    console.log(error);
  }
}


const selectSearch = (pageNum) => {
  if(comboSelected.value === 'orgname') {
    selectUserListCondi.value.username = "";
    selectOrganizationListCondi.value.orgname = comboInputData.value;
    selectOrganizationList(pageNum);
  }
  else {
    selectUserListCondi.value.deptid = "";
    selectUserListCondi.value.username = comboInputData.value;
    selectUserList(pageNum);
  }
};

/* Delete Function */
// deleteOrganizationBIA
const deleteOrganizationBIA = async (retValue) => {
  toggleOrgDeletePopup.value();
  if (retValue == false) {
    return;
  }

  if(selectUserListList.value.length > 0) {
    alert("조직에 사용자가 존재하여 삭제를 진행할 수 없습니다.");
    return;
  }

  isloading.value = true;
  /* =================== 삭제 리스트 탐색 ================== */
  let treeNode = [];
  treeNode.push({
    orgid: orgArgs.value.orgid,
    orgname: orgArgs.value.label
  });

  const searchTree = (node) => {
    node.nodes.forEach((n) => {
      treeNode.push({orgid: n.orgid});
      searchTree(n);
    });
  };
  searchTree(orgArgs.value);
  /* ===================================================== */

  const response = await API.comAPI.deleteOrganizationBIA(treeNode, urlPaths.value)
                                  .catch(error => { console.log(error); });
  if(response.data[0] == -1) {
    alert("하위 부서에 사용자가 존재합니다.");
    return;
  }
  // else if(response.status == 200) {
  //   alert("정상 삭제되었습니다");
  // }

  selectUserListCondi.value.deptid = "";
  await selectOrganizationList(1);
  await selectUserList(1);
  treeData.value = makeTree.value;
  if (treeData.value.length > 0)
    treeData.value[0].expanded = true;

  orgArgs.value = {};
  const treeRowItem = document.getElementsByClassName("tree-row-item");
  for (let i = 0; i < treeRowItem.length; i++) {
    treeRowItem[i].style.backgroundColor = "";
  }
  isloading.value = false;
}

// deleteUserBIA
const deleteUserBIA = async (retValue) => {
  toggleUserDeletePopup.value();
  if (retValue == false) {
    return;
  }

  isloading.value = true;
  await API.comAPI
    .deleteUserBIA(selectUserListFinalData.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });
  selectUserListCondi.value.pageNum = 1;
  currentPageSelectUserList.value = 1;
  await selectUserList(1);
  checkedListSelectUserList.value = [];
  isloading.value = false;
}


/* =============================== Popup Function =============================== */
// toggleDeletePopup
const visibleBmsComOrgDeletePopup = ref(false);
const visibleBmsComUserDeletePopup = ref(false);
const toggleOrgDeletePopup = ref(async () => {
  if (orgArgs.value.orgid === undefined) {
    alert("삭제할 부서 항목을 선택해주세요.");
    return;
  }

  visibleBmsComUserDeletePopup.value = false;
  visibleBmsComOrgDeletePopup.value = !visibleBmsComOrgDeletePopup.value;
});
const toggleUserDeletePopup = ref(() => {
  if (checkedListSelectUserList.value.length < 1) {
    alert("삭제할 사용자 항목을 선택해주세요.");
    checkedListSelectUserList.value = [];
    return;
  }
  visibleBmsComOrgDeletePopup.value = false;
  visibleBmsComUserDeletePopup.value = !visibleBmsComUserDeletePopup.value;
});

// toggleInsertPopup
const visibleBmsComOrgInsertPopup = ref(false);
const toggleOrgInsertPopup = ref(async () => {
  comboInputData.value = "";
  await selectOrganizationList(1);
  treeData.value = makeTree.value;
  if (treeData.value.length > 0)
    treeData.value[0].expanded = true;

  visibleBmsComToporgInsertPopup.value = false;
  visibleBmsComOrgInsertPopup.value = !visibleBmsComOrgInsertPopup.value;

  const treeRowItem = document.getElementsByClassName("tree-row-item");
  for (let i = 0; i < treeRowItem.length; i++) {
    treeRowItem[i].style.backgroundColor = "";
  }
  orgArgs.value = {};
});

const visibleBmsComToporgInsertPopup = ref(false);
const toggleToporgInsertPopup = ref(async () => {
  visibleBmsComOrgInsertPopup.value = false;
  visibleBmsComOrgUpdatePopup.value = false;
  visibleBmsComToporgInsertPopup.value = !visibleBmsComToporgInsertPopup.value;
});

const visibleBmsComUserInsertPopup = ref(false);
const toggleUserInsertPopup = ref(() => {
  visibleBmsComOrgInsertPopup.value = false;
  visibleBmsComUserInsertPopup.value = !visibleBmsComUserInsertPopup.value;
});
const returnUserInsertPopup = ref(async () => {
  toggleUserInsertPopup.value();
  checkedListSelectUserList.value = [];
  selectUserListCondi.value.pageNum = 1;
  currentPageSelectUserList.value = 1;
  await selectUserList(1);
});

// toggleUpdatePopup
const visibleBmsComOrgUpdatePopup = ref(false);
const visibleBmsComUserUpdatePopup = ref(false);
const visibleBmsComUserDetailPopup = ref(false);
const orgArgs = ref({});
const userArgs = ref({});

const toggleOrgUpdatePopup = ref(async () => {
  if(orgArgs.value.orgid == undefined) {
    alert("정보를 수정할 부서를 선택해주세요.")
    return;
  }
  
  comboInputData.value = "";
  await selectOrganizationList(1);
  treeData.value = makeTree.value;
  if (treeData.value.length > 0)
    treeData.value[0].expanded = true;

  visibleBmsComUserUpdatePopup.value = false;
  visibleBmsComOrgUpdatePopup.value = !visibleBmsComOrgUpdatePopup.value;
  await selectUserList(1);
});
const toggleUserUpdatePopup = ref(() => {
  if(checkedListSelectUserList.value.length == 0) {
    alert("정보를 수정할 대상을 선택해주세요.")
    return;
  }
  if(checkedListSelectUserList.value.length > 1) {
    alert("사용자 정보 수정은 한 항목씩 가능합니다.")
    return;
  }
  userArgs.value = checkedListSelectUserList.value[0];
  visibleBmsComOrgUpdatePopup.value = false;
  visibleBmsComUserUpdatePopup.value = !visibleBmsComUserUpdatePopup.value;
});
const returnUserUpdatePopup = ref(async() => {
  toggleUserUpdatePopup.value();
  selectUserListCondi.value.pageNum = 1;
  currentPageSelectUserList.value = 1;
  checkedListSelectUserList.value = [];
  await selectUserList(1);
});
const toggleUserDetailPopup = ref((item) => {
  if(item !== undefined)
    userArgs.value = item.item.raw;
  visibleBmsComUserDetailPopup.value = !visibleBmsComUserDetailPopup.value;
});
/* ============================================================================== */

</script>
