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
            :items="selectOrganizationInfoList"
            :item-value="selectOrganizationInfoIdentifier"
            :items-length="selectOrganizationInfoList.length"
            select-strategy="single"
            :no-data-text="noDataText"
            :loading="selectOrganizationInfoLoader"
            @click:row="(event, item) => openTree(item.item.raw)"
            class="table-type-02 narrow"
            height="200px"
            show-select
            fixed-header
            hover
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
import { API } from "@/api";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
import { transVgroupIcon } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('DctReaderPopup')
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

// for selectOrganizationInfo
const checkedListSelectOrganizationInfo = ref([])
const selectOrganizationInfoLoader = ref(true)
const selectOrganizationInfoList = ref([])
const selectOrganizationInfoDefCondi = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "userid"
}
const selectOrganizationInfoCondi = ref({ ...selectOrganizationInfoDefCondi })
const selectOrganizationInfoCondiCheck = ref({ value: { ...selectOrganizationInfoDefCondi }, flag: { ...selectOrganizationInfoDefCondi } })

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

const onNodeClick = async(node) => {
  initialClick.value = true;
  selectOrganizationInfoCondi.value.orgid = node.orgid;
  selectOrganizationInfoCondi.value.userid = "";
  selectOrganizationInfoCondi.value.username = "";
  comboInputData.value = "";
  searchText.value = "";
  await selectOrganizationInfo();
  treeNodeColor(node);
  isSearch.value = false;
};

/* ============================================== */

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  setCondiClear(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value);
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganization();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  await selectOrganizationInfo();
  selectedData.value = args.value;
})
watch(route, async (route) => {
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganizationInfo(); 
})

watchEffect(() => {
  args.value = props.args
})
watch(() => _.cloneDeep(selectOrganizationInfoCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectOrganizationInfoCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectOrganizationInfoCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value, selectOrganizationInfoCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectOrganizationInfoCondi.value["username"] = "";
  selectOrganizationInfoCondi.value["userid"] = "";
  setCondiChanged(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value, selectOrganizationInfoCondi.value);
})

const staticColumnsSelectOrganizationInfo = [
  { key: "grade", title: "직위", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "username", title: "성명", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "vgroupname", title: "권한", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

// userinfo identifier
const selectOrganizationInfoIdentifier = computed(() => (item) => {
  item.identifier = `${item.orgid}-${item.userid}`;
 return item.identifier;
});

// userinfo single select
const selectOrganizationInfoFinalData = computed(() => {
  const filteredData = selectOrganizationInfoList.value.filter((item) => 
    checkedListSelectOrganizationInfo.value.includes(item.identifier))
  return filteredData.map((item) => {
    return item;
  });
});

// 검색 기능
const isSearch = ref(false);
const searchUser = () => {
  checkedListSelectOrganizationInfo.value = "";
  selectOrganizationInfoCondi.value.orgid = "";
  selectOrganizationInfoCondi.value[comboSelected.value] = comboInputData.value;
  if(comboInputData.value == ""){
    searchText.value = "";
  }
  selectOrganizationInfo();
  isSearch.value = true;
}

// 검색된 유저 조회 테이블 row 클릭시
const openTree = (item) => {
  if(isSearch.value){
    searchText.value = item.orgname;
    item.label = item.orgname;
    nextTick(()=>{
      treeNodeColor(item);
    })
  }
}

watch(
  () => checkedListSelectOrganizationInfo.value,
  (newVal, oldVal) => {
    if(oldVal != newVal){
      if(newVal != ""){
        const item = selectOrganizationInfoList.value.find(obj=>obj.orgid == checkedListSelectOrganizationInfo.value[0].split("-")[0])
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
];

const moveDataDown = () => {
  if(checkedListSelectOrganizationInfo.value == ""){
    alert("담당자를 선택해주세요")
  }
  if(selectOrganizationInfoFinalData.value.length != 0){
    const newData = {...selectOrganizationInfoFinalData.value[0]};
    const isDuplicate = selectedData.value.some(item => item.identifier === newData.identifier);
    if (isDuplicate) {
      alert('이미 지정된 담당자입니다');
      chkClear();
    }else if(selectedData.value.length >= 1){
      alert('담당자는 한명만 선택하실 수 있습니다');
      chkClear();
    }else {
      selectedData.value.push(newData);
    }
  }
};

const chkClear = () => {
  checkedListSelectOrganizationInfo.value = [];
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
    alert("삭제하실 담당자를 선택해주세요")
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
    console.log(error);
    alert("Server Error");
  }
};

// tree에서 선택된 부서의 user 데이터 조회용
const selectOrganizationInfo = async () => {
  selectOrganizationInfoLoader.value = true;
  try {
    const response = await API.dctAPI.selectManagerInfo({ ...selectOrganizationInfoCondi.value }, urlPaths.value);
    selectOrganizationInfoList.value = response.data;
    selectOrganizationInfoLoader.value = false;

    setCondiClear(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value)
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}


// Popup
const closePopUp = () => {
  props.toggleFunc();
}

const confirmPopUp = () => {
  childReturnValue.value = makeFinalData.value;
  props.returnFunc(childReturnValue.value);
}
</script>


<style scoped>
.density {
  padding: 0px 4px;
  height: 15px;
}
</style>