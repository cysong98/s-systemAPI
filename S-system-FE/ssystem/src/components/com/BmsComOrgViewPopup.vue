<template>
  <div class="modal-body">
    <div class="content" style="height: 500px;">
          <div class="row">
              <div class="col">
                  <div class="box-flex">
                      <v-select
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
                      <v-btn class="magnify-solid" @click="selectSearch(1)">
                          <v-icon></v-icon>검색
                      </v-btn>
                  </div>
              </div>
          </div><br/>
          <div class="row">
              <div class="col">
                  <div class="row">
                      <p style="font-size: 17px; font-weight: bold; padding-bottom: 9px;">부서목록</p>
                  </div>
                  <Tree
                      class="tree"
                      :nodes="treeData"
                      :use-checkbox="useCheckbox"
                      :use-icon="true"
                      :search-text="searchText"
                      style="height: 388px;"
                      @nodeClick="onNodeClick">
                  </Tree>
              </div>
              <div class="col">
                <div class="row" style="margin-bottom: 0px; justify-content: space-between;">
                  <div style="display: flex; gap: 10px;">
                    <p style="font-size: 17px; font-weight: bold;">사용자목록</p>
                    <p>전체: {{ totalItem }}개</p>
                  </div>
                  <v-pagination
                    v-model="currentPageSelectUserList"
                    :length="totalPagesSelectUserList"
                    class="pagination"
                    total-visible="5"
                    prev-icon="mdi-menu-left"
                    next-icon="mdi-menu-right"
                    @click="handlePageChangeSelectUserList"
                  ></v-pagination>
                </div>

                <v-data-table
                  :headers="staticColumnsSelectUserList"
                  :items="selectUserListList"
                  :items-per-page="pageSizeSelectUserList"
                  :loading="userLoader"
                  :no-data-text="noDataText"
                  fixed-header
                  hover
                  class="table-type-02"
                  height="385px"
                >
                  <template v-slot:item.vgroupname="{item}">
                    <div v-for="icon in transVgroupIcon(item.raw)" class="vgroup-icon">
                      <div v-if="icon.content !== '부서장'" class="content">{{ icon.vgroupname }}</div>
                      <v-chip
                        class="density"
                        :text="icon.content"
                        :color="icon.color"
                        variant="flat"
                        style="font-weight: 600; font-size: 12px; color: rgb(255, 255, 255); padding: 0px 4px; height: 15px;"
                        label
                      ></v-chip>
                    </div>
                  </template>
                  <template v-slot:bottom />
                </v-data-table>
            </div>
        </div>
    </div>
    <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp" style="margin-top: 0px;">닫기</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import { ref, onBeforeMount, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api'; 

import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, validCheckSearchInput } from "@/utils/Utils.js";
import { transVgroupIcon } from "@/utils/TransFormLabelDataUtil.js"

const route = useRoute()
const urlPaths =  ref('')
const props = defineProps({
toggleFunc: Function,
})

// for column combo
const comboSelected = ref("orgname")
const comboInputData = ref("")
const comboData = [
  { key: "orgname", view: "부서명" },
  { key: "username", view: "사용자명" }
];
// for selectOrganizationList
const organizationLoader = ref(true)
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

const staticColumnsSelectUserList = [
  { title: "아이디", key: "userid", width: "26%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이름", key: "username", width: "13%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서", key: "deptname", width: "21%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직위", key: "position", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직급", key: "grade", width: "10%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "권한", key: "vgroupname", width: "20%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];


/* ================================ pagination ================================ */
const totalPagesSelectOrganizationList = ref(0)
const totalPagesSelectUserList = ref(0)
const currentPageSelectUserList = ref(1)
const pageSizeSelectUserList = ref(1000)

const handlePageChangeSelectUserList = () => {
  checkedListSelectUserList.value = [];
  selectUserList(currentPageSelectUserList.value);
}
/* ============================================================================ */
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

/* ============================================================================= */
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
  await selectUserList(1);
  
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

  try {
    const response = await API.comAPI.selectUserList({ ...selectUserListCondi.value }, urlPaths.value);
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

// Popup Function
const closePopUp = () => {
  props.toggleFunc();
};
</script>

<style scoped>
.pagination {
  height: 37px;
  padding-top: 0px;
  padding-bottom: 0px;
}
.table-type-02 {
  width: 820px;
}
</style>