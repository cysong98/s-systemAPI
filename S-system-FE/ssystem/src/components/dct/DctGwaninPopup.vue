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
            <v-label>부서명</v-label>
            <v-text-field
              @keyup.enter="searchUser"
              v-model="comboInputData"
              variant="outlined"
              hide-details="auto"
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
            style="height: 250px;"
          />
        </div>
        <div class="col border half-of-half-fix px-3">
          <h3 class="h3">구분</h3>
          <v-radio-group v-model="selectedAsktype">
            <div v-for="(data,idx) in radioData" :key="idx">
              <v-radio :label="data.view" color="indigo" :value="data.key"></v-radio>
            </div>
          </v-radio-group>
        </div>
        <div class="col border px-3">
          <template v-if="selectedAsktype === 'ORGE1'">
            <h3 class="h3">직인</h3>
            <div class="box-image">
              <img
                v-if="fileurl !== null && fileurl !== ''"
                style="width: 200px; height: auto;"
                :src="fileurl"
                class="image"
                alt=""
              >
              <span v-else>{{ noDataText }}</span>
            </div>
          </template>
          <template v-if="selectedAsktype === 'ORGE3'">
            <h3 class="h3">특수관인</h3>
            <div class="box-image">
              <img
                v-if="fileurl !== null && fileurl !== ''"
                style="width: 200px; height: auto;"
                :src="fileurl"
                class="image"
                alt=""
              >
              <span v-else>{{ noDataText }}</span>
            </div>
          </template>
          <template v-if="selectedAsktype === 'ORGEB'">
            <h3 class="h3">관인 생략</h3>
            <div class="box-image">
              <img
                v-if="fileurl !== null && fileurl !== ''"
                style="width: 200px; height: auto;"
                :src="fileurl"
                class="image"
                alt=""
              >
              <span v-else>{{ noDataText }}</span>
            </div>
          </template>
        </div>
      </div>
    </div>

    <div class="buttons-bottom" style="padding-top: 0px;">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import { ref, onBeforeMount, onMounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";

const name = ref('DctGwaninPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})

const route = useRoute();
const router = useRouter();
// for popup
const urlPaths = ref("");
// pinia
const loginStore = useLoginStore()
const { getUserLoginData } = storeToRefs(loginStore)
// for selectOrganizationInfo
const checkedListSelectOrganizationInfo = ref([])
const selectOrganizationInfoLoader = ref(true)
const selectOrganizationInfoList = ref([])
const selectOrganizationInfoDefCondi = {
  managegubun: "ORGE1",
  orgid: "",
}
const selectOrganizationInfoCondi = ref({ ...selectOrganizationInfoDefCondi })
const selectOrganizationInfoCondiCheck = ref({ value: { ...selectOrganizationInfoDefCondi }, flag: { ...selectOrganizationInfoDefCondi } })

// search
const comboSelected = ref("orgname")
const comboInputData = ref("")

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
const searchText = ref(getUserLoginData.value.deptname);
const clear = ref(true);

const onNodeClick = async(node) => {
  selectOrganizationInfoCondi.value.orgid = node.orgid;
  comboInputData.value = "";
  if(clear.value){
    searchText.value = "";
  }
  await selectOrganizationInfo();
  treeNodeColor(node);

  if(selectOrganizationInfoList.value[0] !== undefined)
    fileurl.value = selectOrganizationInfoList.value[0].fileurl;
  isSearch.value = false;
  clear.value = true;
};

/* ============================================== */

onMounted(async () => {
  args.value = props.args
  setCondiClear(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value);
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganization();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
})
watch(route, async (route) => {
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganizationInfo(); 
})

watchEffect(() => {
  args.value = props.args;
})

watch(() => _.cloneDeep(selectOrganizationInfoCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectOrganizationInfoCondiCheck.value, newVal, oldVal);
})

/* =========================================================== */

// 검색 기능
const isSearch = ref(false);
const searchUser = () => {
  checkedListSelectOrganizationInfo.value = "";
  selectOrganizationInfoCondi.value.orgid = "";
  selectOrganizationInfoCondi.value[comboSelected.value] = comboInputData.value;
  searchText.value = selectOrganizationInfoCondi.value.orgname;
  if(comboInputData.value == ""){
    searchText.value = "";
  }
  selectOrganizationInfo();
  clear.value = false;
  isSearch.value = true;
}

// 구분 라디오 버튼
const selectedAsktype = ref("ORGE1");
const radioData = [
  {view: "직인", key:"ORGE1"},
  {view: "특수관인", key:"ORGE3"},
  {view: "생략인", key:"ORGEB"},
]
// 라디오버튼 선택했을때의 펑션
watch(selectedAsktype, async(newVal) => {
  selectOrganizationInfoCondi.value.managegubun = newVal; // key값을 선택한 값으로.
  if(selectOrganizationInfoCondi.value.orgid !== '') {
    await selectOrganizationInfo(); // 라디오버튼 선택해도 쿼리 출력하도록.
    if(selectOrganizationInfoList.value[0] !== undefined)
      fileurl.value = selectOrganizationInfoList.value[0].fileurl;
  }
  else
    fileurl.value = "";
});

/* =========================================================== */

// tree 데이터 조회용
const selectOrganizationList = ref([]);
const noDataText = ref("");
const fileurl = ref("");
const selectOrganization = async () => {
  try {
    const response = await API.dctAPI.selectOrganization("", urlPaths.value);
    selectOrganizationList.value = response.data;

    const node = selectOrganizationList.value.find(obj => obj.orgname.includes(searchText.value));
    if(node){
      node.label = node.orgname;
      clear.value = false;
      onNodeClick(node);
    }
  } catch (error) {
    console.log(error);
  }
};

// tree에서 선택된 부서의 user 데이터 조회용
const selectOrganizationInfo = async () => {
  selectOrganizationInfoLoader.value = true;

  try {
    const response = await API.dctAPI.selectGawnin({ ...selectOrganizationInfoCondi.value }, urlPaths.value);
    selectOrganizationInfoList.value = response.data;
    selectOrganizationInfoLoader.value = false;

    if(response.data.length === 0) {
      fileurl.value = "";
      noDataText.value = "등록된 이미지가 없습니다.";
    }      
    setCondiClear(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value);
  } catch (error) {
    console.log(error);
  }
}


// Popup
const closePopUp = () => {
  props.toggleFunc();
}

const confirmPopUp = () => {
  if(fileurl.value !== '')
    props.returnFunc(selectOrganizationInfoList.value[0]);
  else
    alert("이미지를 선택해주세요.");
}

</script>