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
      <h4 class="h4">시스템관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">메뉴관리</h5>
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
                  @keyup.enter="selectMenuList(1)"
                  v-model="comboInputData"
                  :maxlength="maxLengthFunc()"
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
          <v-btn class="magnify-solid" @click="selectMenuList(1)">
            <v-icon></v-icon>검색
          </v-btn>
        </div>
        
        <div class="box-flex justify-space-between pb-2">
          <span>전체: {{ totalItem }}개</span>
          <div class="box-flex flex-end">
            <div class="action-buttons">
              <v-btn class="button" @click="toggleOrderPopup()">순서변경</v-btn>
              <v-btn class="button" @click="moveToBmsCommenuinsert()">등록</v-btn>
              <v-btn class="button" @click="toggleDeletePopup()">삭제</v-btn>
            </div>
            <v-select
              v-model="pageSizeSelectMenuList"
              :items="pageSizesSelectMenuList"
              item-title="view"
              item-value="key"
              @update:modelValue="handlePageSizeChangeSelectMenuList"
              variant="outlined"
              hide-details="auto"
            ></v-select>
          </div>
        </div>

        <v-data-table
          @click:row="(event, item) => moveToBmsCommenudetail(item.item.value)"
          v-model="checkedListSelectMenuList"
          v-model:expanded="expanded"
          :headers="staticColumnsSelectMenuList"
          :items="selectedTreeData"
          :items-per-page="pageSizeSelectMenuList"
          :item-value="selectMenuListIdentifier"
          :loading="loader"
          :no-data-text="noDataText"
          fixed-header
          show-select
          show-expand
          hover
          height="520px"
          class="table-type-05"
        >
          <template v-slot:item.name="{ item }">
            <tr>
              <td :class="{ 'text-left': item }" style="background-color: #FFFFFF00;">
                {{ item.raw.name }}
              </td>
            </tr>
          </template>
          <template v-slot:item.url="{ item }">
            <tr>
              <td :class="{ 'text-left': item }" style="background-color: #FFFFFF00;">
                {{ item.raw.url }}
              </td>
            </tr>
          </template>
          <template v-slot:item.description="{ item }">
            <tr>
              <td :class="{ 'text-left': item }" style="background-color: #FFFFFF00;">
                {{ item.raw.description }}
              </td>
            </tr>
          </template>
          <template v-slot:expanded-row="{ item }">
            <tr
              v-for="node in item.raw.nodes"
              @click="moveToBmsCommenudetail(node)"
              style="cursor: pointer;"
            >
              <td>
                <v-checkbox
                  @click="(e) => { e.stopPropagation() }"
                  v-model="checkedListSelectMenuList"
                  :value="{id: node.menuid}"
                  hide-details="auto"
                />
              </td>
              <td>{{ node.menuid }}</td>
              <td :class="{ 'text-left': item }">
                <pre class="branch-down">          {{ node.name }}</pre>
              </td>
              <td :class="{ 'text-left': item }">
                <pre class="branch-down">          {{ node.url }}</pre>
              </td>
              <td :class="{ 'text-left': item }">
                <pre v-if="node.description !== undefined" class="branch-down">          {{ node.description }}</pre>
              </td>
              <td></td>
            </tr>
          </template>
          <template v-slot:item.data-table-expand="{ item }">
            <tr @click="(e) => e.stopPropagation()">
              <td style="background-color: #FFFFFF00;">
                <v-btn
                  v-if="item.raw.nodes.length !== 0"
                  @click="(e) => { clickExpandBtn(e, item) }"
                  :icon="expanded.includes(item.raw.identifier) === false ? 'mdi-chevron-down' : 'mdi-chevron-up'"
                  :ripple="false"
                  size="x-large"
                  color="#FFFFFF00"
                  border="0"
                  rounded
                ></v-btn>
              </td>
            </tr>
          </template>
          <template v-slot:bottom />
        </v-data-table>
        <v-pagination
          v-model="currentPageSelectMenuList"
          :length="Math.ceil(totalPagesSelectMenuList/pageSizeSelectMenuList)"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="paginationTreeData()"
        ></v-pagination>
      </section>
    </div>
  </div>

  <v-dialog v-model="visibleBmsComDeletePopup" style="width: 700px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComDeletePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComDeletePopup
        v-if="visibleBmsComDeletePopup"
        :alertMsg="'하위 메뉴까지 모두 삭제됩니다.'"
        :returnFunc="deleteMenuBIA"
      ></BmsComDeletePopup>
    </v-card>
  </v-dialog>
  <v-dialog v-model="visibleBmsComOrderPopup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>메뉴 순서 변경</v-toolbar-title>
        <v-btn class="close" icon @click="visibleBmsComOrderPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComMenuOrderPopup
        v-if="visibleBmsComOrderPopup"
        :args="treeData"
        :toggleFunc="toggleOrderPopup"
      ></BmsComMenuOrderPopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComDeletePopup from "./BmsComDeletePopup.vue";
import BmsComMenuOrderPopup from "./BmsComMenuOrderPopup.vue";

const name = ref('BmsComMenuList')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for column combo
const comboSelected = ref("name")
const comboInputData = ref("")
const comboData = [
  { key : "id", view : "메뉴아이디" },
  { key : "name", view : "메뉴명" },
]
// for selectMenuList
const loader = ref(true)
const checkedListSelectMenuList = ref([])
const selectMenuListList = ref([])
const selectMenuListDefCondi = {
  id: "",
  name: "",
  pageNum: 1,
  pageSize: 1000,
}
const selectMenuListCondi = ref({ ...selectMenuListDefCondi })
const selectMenuListCondiCheck = ref({ value: { ...selectMenuListDefCondi }, flag: { ...selectMenuListDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value == "name")
    return 256;
  else
    return 35;
};


/* ================ pagination ================ */
const totalPagesSelectMenuList = ref(0)
const currentPageSelectMenuList = ref(1)
const pageSizeSelectMenuList = ref(10)
const pageSizesSelectMenuList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

const handlePageSizeChangeSelectMenuList = () => {
  currentPageSelectMenuList.value = 1;
  selectedTreeData.value = treeData.value;
  expanded.value = [];
}
const paginationTreeData = () => {
  selectedTreeData.value = [];
  for(var i=pageSizeSelectMenuList.value*(currentPageSelectMenuList.value-1); i<treeData.value.length; i++) {
    selectedTreeData.value.push(treeData.value[i]);
  }
  expanded.value = [];
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value);
  setQueries(route, selectMenuListCondi.value, selectMenuListDefCondi);
  await selectMenuList(1);
})

watch(route, async (route) => {
  setQueries(route, selectMenuListCondi.value, selectMenuListDefCondi);
  await selectMenuList(selectMenuListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectMenuListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectMenuListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectMenuListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectMenuListCondiCheck.value, selectMenuListCondi.value, selectMenuListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  comboInputData.value = "";
  selectMenuListCondi.value["id"] = "";
  selectMenuListCondi.value["name"] = "";
  setCondiChanged(selectMenuListCondiCheck.value, selectMenuListCondi.value, selectMenuListCondi.value);
})


const staticColumnsSelectMenuList = [
  { title: "메뉴아이디", key: "menuid", width: "13%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "메뉴명", key: "name", width: "27%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "메뉴URL", key: "url", width: "25%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "설명", key: "description", width: "30%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "하위메뉴", key: "data-table-expand", width: "5%" },
];


/* ============================= Expandable Table ============================= */
const treeData = ref([]), selectedTreeData = ref([]);
const expanded = ref([]);
const makeTree = computed(() => {
  const treeNodes = [];

  const buildTree = (menuData, idPrefix) => {
    const treeNode = {
      id: idPrefix,
      menuid: menuData.id,
      name: menuData.name,
      url: menuData.url,
      description: menuData.description,
      menuorder: menuData.menuorder,
      nodes: []
    };

    const childMenus = selectSubMenuList.value.filter(item => item.menuid === treeNode.menuid);
    childMenus.forEach((childMenu, index) => {
      let childData = selectMenuListList.value.find((el) => el.id === childMenu.submenuid);
      if(childData !== undefined) {
        const childIdPrefix = idPrefix + (index + 1);
        childData.menuorder = childMenu.submenuorder;
        treeNode.nodes.push(buildTree(childData, childIdPrefix));
      }
    });
    return treeNode;
  };

  const rootMenus = selectMenuListList.value.filter(item => item.type === "a");
  rootMenus.forEach((rootMenu, index) => {
    const rootIdPrefix = (index + 1).toString();
    const rootNode = buildTree(rootMenu, rootIdPrefix);
    treeNodes.push(rootNode);
  });
  return treeNodes;
});

const clickExpandBtn = (e, item) => {
  e.stopPropagation();
  const index = expanded.value.findIndex((i) => i.id === item.raw.menuid);
  if (index !== -1) {
    expanded.value.splice(index, 1);
  } else {
    expanded.value.push(item.raw.identifier);
  }
};

/* =========================================================================== */

/* ======================= multi selection ======================= */
const selectMenuListIdentifier = computed(() => (item) => {
  item.identifier = {
    id: item.menuid,
  };
  return item.identifier;
});

// 서버에 전송할 체크된 리스트 (row 반환)
const selectMenuListFinalData = computed(() => (item) =>{
    checkedListSelectMenuList.value.includes(item);
});
/* =========================================================== */

let totalItem = 0;
const noDataText = ref("");
const selectMenuList = async (pageNum) => {
  loader.value = true;
  selectMenuListCondi.value.pageNum = parseInt(pageNum);

  if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectMenuListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      loader.value = false;
      return;
    }
  }
  try {
    const response = await API.comAPI.selectMenuList({ ...selectMenuListCondi.value }, urlPaths.value);
    response.data.list = response.data.list.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD')};
      return { ...u};
    });

    selectMenuListList.value = response.data.list;
    loader.value = false;
    noDataText.value = "검색된 조건에 맞는 데이터가 없습니다.";
    totalItem = response.data.total;

    setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value)
    await selectSubMenu("");

    treeData.value = makeTree.value;
    selectedTreeData.value = treeData.value;
    totalPagesSelectMenuList.value = treeData.value.length;
    expanded.value = [];
  } catch (error) {
    console.log(error);
  }
}

const selectSubMenuList = ref([]);
const selectSubMenu = async (menuid) => {
  const response = await API.comAPI.selectSubMenu({ menuid: menuid }, urlPaths.value);
  selectSubMenuList.value = response.data;
};

const deleteMenuBIA = async (reValue) => {
  toggleDeletePopup.value();
  if (reValue == false) {
    return;
  }
  isloading.value = true;

  await API.comAPI
    .deleteMenuBIA(checkedListSelectMenuList.value, urlPaths.value)
    .then(response => {
      // if (response.status == 200) {
      //   alert("정상 삭제되었습니다");
      // }
    })
  .catch(error => {
    console.log(error);
  });

  comboInputData.value = "";
  setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value)
  await selectMenuList(1);

  checkedListSelectMenuList.value = [];
  expanded.value = [];
  currentPageSelectMenuList.value = 1;
  isloading.value = false;
}


const visibleBmsComDeletePopup = ref(false);
const visibleBmsComOrderPopup = ref(false);
const toggleDeletePopup = ref(() => {
  if (checkedListSelectMenuList.value.length < 1) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }
  visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
});
const toggleOrderPopup = ref(async() => {
  visibleBmsComOrderPopup.value = !visibleBmsComOrderPopup.value;
  comboInputData.value = "";
  setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value)
  await selectMenuList(1);
});


// Move Function
const moveToBmsCommenudetail = (item) => {
  if(item.menuid == undefined)
    item.menuid = item.id;

  router.push({
    name: "BmsCommenudetail",
    query: {
      ...{id: item.menuid}
    }
  });
}

// Move Function
const moveToBmsCommenuinsert = () => {
  router.push({
    name: "BmsCommenuinsert"
  });
};

</script>