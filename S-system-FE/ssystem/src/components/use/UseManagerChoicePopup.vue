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
        <div class="col half-fix">
          <div class="box-flex">
            <v-select
              v-model="comboSelected"
              label=""
              :items="comboData"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            /> 
            <v-text-field
              v-model="comboInputData"
              variant="outlined"
              hide-details="auto"
              @keyup.enter="searchUser"
              clearable
            />
            <v-btn class="magnify" @click="searchUser">
              <v-icon></v-icon>
            </v-btn>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <Tree
            class="tree"
            :nodes="treeData"
            :use-checkbox="false"
            :use-icon="true"
            :search-text="searchText"
            @nodeClick="onNodeClick"
            @mouseover="handleMouseOver"
          />
        </div>
        <div class="col">
          <v-data-table-server
            v-if="initialClick || isSearch"
            v-model="checkedListSelectOrganizationInfo"
            :headers="staticColumnsSelectOrganizationInfo"
            :items="selectManagerInfoList"
            :item-value="selectManagerInfoIdentifier"
            :items-length="selectManagerInfoList.length"
            :no-data-text="noDataText"
            :loading="selectManagerInfoLoader"
            select-strategy="single"
            @click:row="(event, item) => openTree(item.item.raw)"
            class="table-type-02 narrow"
            height="200px"
            show-select
            fixed-header
            hover
          >
            <template v-slot:bottom></template>
          </v-data-table-server> 
          <v-data-table-server
            v-else
            :headers="staticColumnsSelectOrganizationInfo"
            :items-length="100"
            :no-data-text="noDataText"
            class="table-type-02 narrow"
          >
            <template v-slot:bottom></template>
          </v-data-table-server> 
        </div>
      </div>
      <div class="row center">
        <v-icon size="x-large" color="blue" @click="moveDataDown">mdi-chevron-down-box</v-icon>
        <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
      </div>
      <div class="row">
        <div class="col">
          <v-data-table
            v-model="checkedList"
            :headers="selectedHeaders"
            :items="selectedData"
            :item-value="makeIdentifier"
            select-strategy="single"
            show-select
            :no-data-text="noSelectedText"
            class="table-type-02 narrow"
          >
            <template v-slot:bottom></template>
          </v-data-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">저장</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";

const name = ref('UseManagerChoicePopup')
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

// for selectManagerInfo
const checkedListSelectOrganizationInfo = ref([])
const selectManagerInfoLoader = ref(true)
const selectManagerInfoList = ref([])
const selectManagerInfoDefCondi = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "userid"
}
const selectManagerInfoCondi = ref({ ...selectManagerInfoDefCondi })
const selectManagerInfoCondiCheck = ref({ value: { ...selectManagerInfoDefCondi }, flag: { ...selectManagerInfoDefCondi } })

// search
const comboSelected = ref("username")
const comboInputData = ref("")
const comboData = [
  { view: "이름", key: "username"},
  { view: "ID", key: "userid"},
];

/* ================ tree ========================== */
const makeTree = computed(() => {
  const treeNodes = [];

  const buildTree = (orgData, idPrefix) => {
    const treeNode = {
      id: idPrefix,
      orgid: orgData.orgid,
      label: orgData.orgname,
      nodes: []
    };
    const childOrgs = selectOrganizationList.value.filter(item => item.parentorgid === orgData.orgid);
    childOrgs.forEach((childOrg, index) => {
      const childIdPrefix = idPrefix + (index + 1);
      treeNode.nodes.push(buildTree(childOrg, childIdPrefix));
    });
    return treeNode;
  };

  const rootOrgs = selectOrganizationList.value.filter(item => item.orgdepth === 1);
  rootOrgs.forEach((rootOrg, index) => {
    const rootIdPrefix = (index + 1).toString();
    const rootNode = buildTree(rootOrg, rootIdPrefix);
    treeNodes.push(rootNode);
  });
  return treeNodes;
});

const treeData = ref([]);
const initialClick = ref(false);
const noDataText = "조회된 데이터가 없습니다.";
const searchText = ref("");

const onNodeClick = async (node) => {
  initialClick.value = true;
  selectManagerInfoCondi.value.orgid = node.orgid;
  selectManagerInfoCondi.value.userid = "";
  selectManagerInfoCondi.value.username = "";
  comboInputData.value = "";
  searchText.value = ""
  await selectManagerInfo();
  treeNodeColor(node);
};

/* ============================================== */

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  setCondiClear(selectManagerInfoCondiCheck.value, selectManagerInfoCondi.value);
  setQueries(route, selectManagerInfoCondi.value, selectManagerInfoDefCondi);
  await selectOrganization();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  await selectManagerInfo();
  selectedData.value = args.value
})
watch(route, async (route) => {
  setQueries(route, selectManagerInfoCondi.value, selectManagerInfoDefCondi);
  await selectManagerInfo(); 
})

watchEffect(() => {
  args.value = props.args
})
watch(() => _.cloneDeep(selectManagerInfoCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectManagerInfoCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectManagerInfoCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectManagerInfoCondiCheck.value, selectManagerInfoCondi.value, selectManagerInfoCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectManagerInfoCondi.value["username"] = "";
  selectManagerInfoCondi.value["userid"] = "";
  setCondiChanged(selectManagerInfoCondiCheck.value, selectManagerInfoCondi.value, selectManagerInfoCondi.value);
})

const staticColumnsSelectOrganizationInfo = [
  { key: "grade", title: "직위", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "username", title: "성명", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "vgroupname", title: "권한", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

// userinfo identifier
const selectManagerInfoIdentifier = computed(() => (item) => {
  item.identifier = `${item.orgid}-${item.userid}`;
 return item.identifier;
});

// userinfo single select
const selectManagerInfoFinalData = computed(() => {
  const filteredData = selectManagerInfoList.value.filter((item) => 
    checkedListSelectOrganizationInfo.value.includes(item.identifier))
  return filteredData.map((item) => {
    item.askTemp = '최종승인';
    item.asktype = '3';
    return item;
  });
});

// 검색 기능
const isSearch = ref(false);
const searchUser = () => {
  checkedListSelectOrganizationInfo.value = "";
  selectManagerInfoCondi.value.orgid = "";
  selectManagerInfoCondi.value[comboSelected.value] = comboInputData.value;
  if(comboInputData.value == ""){
    searchText.value = "";
  }
  selectManagerInfo();
  isSearch.value = true;
}


// 검색된 유저 조회 테이블 row 클릭시
const openTree = (item) => {
  searchText.value = item.orgname;
  item.label = item.orgname;
  nextTick(()=>{
    treeNodeColor(item);
  })
}

watch(
  () => checkedListSelectOrganizationInfo.value,
  (newVal, oldVal) => {
    if(oldVal != newVal){
      if(newVal != ""){
        const item = selectManagerInfoList.value.find(obj=>obj.orgid == checkedListSelectOrganizationInfo.value[0].split("-")[0])
        openTree(item);
      }
    }
  }
);

// 아래쪽 멀티 select
const makeIdentifier = computed(() => (item) => {
  item.identifier = `${item.orgid}-${item.userid}`;
 return item.identifier;
});

const noSelectedText = "선택된 데이터가 없습니다."
const checkedList = ref([]);
const selectedData = ref([]);
const selectedHeaders = [
  {key: "orgname", title: "부서", width: "100px"},
  {key: "grade", title: "직위(직급)", width: "100px"},
  {key: "username", title: "성명", width: "100px"},
  {key: "askTemp", title: "구분", width: "100px"},
];

const moveDataDown = () => {
  if(checkedListSelectOrganizationInfo.value == ""){
    alert("관리자를 선택해주세요")
  }
  if(selectManagerInfoFinalData.value.length != 0){
    const newData = {...selectManagerInfoFinalData.value[0]};
    const isDuplicate = selectedData.value.some(item => item.identifier === newData.identifier);
    const isFileManager = selectManagerInfoFinalData.value[0].vgroupid !== undefined && selectManagerInfoFinalData.value[0].vgroupid === "6010";
    if (isDuplicate) {
      alert('이미 지정된 관리자입니다');
    }else if(!isFileManager){
      alert('파일반출담당자만 선택하실 수 있습니다');
    }else if(selectedData.value.length >= 1){
      alert('한명만 선택하실 수 있습니다');
    }else {
      selectedData.value.push(newData);
    }
  }
};

const makeRowData = computed(() => {
  const filteredData = selectedData.value.filter((item) => 
  checkedList.value.includes(item.identifier))
  return filteredData.map((item) => {
    return item;
  });
});

const moveDataUp = () => {
  if(makeRowData.value.length != 0){
    makeRowData.value.forEach(row => {
      const index = selectedData.value.indexOf(row);
      if(index !== -1){
        selectedData.value.splice(index,1);
      }
    })
  }else{
    alert("삭제하실 관리자를 선택해주세요")
  }
};

const makeFinalData = computed(() => {
  return selectedData.value.map((item) => {
    const { identifier, ...newItem } = item;
    return newItem;
  });
});

// tree 데이터 조회용
const selectOrganizationList = ref([]);
const selectOrganization = async () => {
  try {
    const response = await API.dctAPI.selectOrganization("", urlPaths.value);
    selectOrganizationList.value = response.data;
  } catch (error) {
    alert("Server Error");
  }
};

// tree에서 선택된 부서의 user 데이터 조회용
const selectManagerInfo = async () => {
  selectManagerInfoLoader.value = true;
  try {
    const response = await API.dctAPI.selectManagerInfo({ ...selectManagerInfoCondi.value }, urlPaths.value);
    selectManagerInfoList.value = response.data;
    selectManagerInfoLoader.value = false;
    setCondiClear(selectManagerInfoCondiCheck.value, selectManagerInfoCondi.value)
  } catch (error) {
    alert("Server Error");
  }
}


// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  childReturnValue.value = makeFinalData.value[0];
  props.returnFunc(childReturnValue.value);
}



</script>