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
              v-model="selectSymbolCondi.orgname"
              variant="outlined"
              hide-details="auto"
              @keyup.enter="selectOrgname"
              clearable
            />
            <v-btn class="magnify" @click="selectOrgname">
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
        <div class="col border px-3">
          <h3 class="h3">로고</h3>
          <div class="box-image">
            <img v-if="logo.fileurl != null" :src="logo.fileurl" alt="" class="image">
            <span v-else>{{ logo }}</span>
          </div>
        </div>
        <div class="col border px-3">
          <h3 class="h3">심볼</h3>
          <div class="box-image">
            <img v-if="symbol.fileurl != null" :src="symbol.fileurl" alt="" class="image">
            <span v-else>{{ symbol }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const name = ref('DctSymbolPopup')
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
// for selectOrganization
const selectOrganizationDefCondi = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectOrganizationCondi = ref({ ...selectOrganizationDefCondi })
// for selectSymbol
const selectSymbolList = ref([])
const selectSymbolDefCondi = {
  orgid: "",
  orgname: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectSymbolCondi = ref({ ...selectSymbolDefCondi })
const selectSymbolCondiCheck = ref({ value: { ...selectSymbolDefCondi }, flag: { ...selectSymbolDefCondi } })

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
const searchText = ref("");
const clear = ref(true);

const onNodeClick = async(node) => {
  initialClick.value = true;
  selectSymbolCondi.value.orgname = "";
  selectSymbolCondi.value.orgid = node.orgid;
  if(clear.value){
    searchText.value = "";
  }
  await selectSymbol();
  treeNodeColor(node);
  clear.value = true;
};

/* ============================================== */

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args;
  await selectOrganization();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  selectSymbolCondi.value.orgname = getUserLoginData.value.deptname;
  await selectOrgname();
})

watchEffect(() => {
  args.value = props.args
})


// 조직도 검색
const selectOrgname = async () => {
  selectSymbolCondi.value.orgid = "";
  searchText.value = selectSymbolCondi.value.orgname;
  const node = selectOrganizationList.value.find(obj => obj.orgname.includes(searchText.value));
  if(node){
    node.label = node.orgname;
    clear.value = false;
    await onNodeClick(node);
  }
};

// tree 데이터 조회용
const selectOrganizationList = ref([]);
const selectOrganization = async () => {
  try {
    const response = await API.dctAPI.selectOrganization({...selectOrganizationCondi.value}, urlPaths.value);
    selectOrganizationList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};


// 로고, 심볼 조회용
const logo = ref({});
const symbol = ref({});
const selectSymbol = async () => {
  try {
    const response = await API.dctAPI.selectSymbol({ ...selectSymbolCondi.value }, urlPaths.value);

    if(response.data.length > 0){
      selectSymbolList.value = response.data;

      const logoValue = selectSymbolList.value.find(obj => obj.managegubun === "ORGE4");
      const symbolValue = selectSymbolList.value.find(obj => obj.managegubun === "ORGE5");

      if(logoValue){
        if(logoValue.fileurl != null){
          logo.value = {};
          logo.value.orgname = logoValue.orgname;
          logo.value.fileid = logoValue.fileid;
          logo.value.cfilename = logoValue.cfilename;
          logo.value.fileurl = logoValue.fileurl;
          logo.value.filepath = logoValue.filepath;
        }else{
          noLogo();
        }
      }else{
        noLogo();
      }

      if(symbolValue){
        if(symbolValue.fileurl != null){
          symbol.value = {};
          symbol.value.orgname = symbolValue.orgname;
          symbol.value.fileid = symbolValue.fileid;
          symbol.value.cfilename = symbolValue.cfilename;
          symbol.value.fileurl = symbolValue.fileurl;
          symbol.value.filepath = symbolValue.filepath;
        }else{
          noSymbol();
        }
      }else{
        noSymbol();
      }
    }else{
      noLogo();
      noSymbol();
    }

    
  } catch (error) {
    alert("Server Error")
  }
}

const noLogo = () => {
  logo.value = "등록된 로고가 없습니다.";
};

const noSymbol = () => {
  symbol.value = "등록된 심볼이 없습니다.";
};

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  // alert("확인되었습니다.");

  // 리턴값 세팅
  childReturnValue.value.symbolurl = symbol.value.fileurl
  childReturnValue.value.symbolpath = symbol.value.filepath
  childReturnValue.value.logourl = logo.value.fileurl
  childReturnValue.value.logopath = logo.value.filepath

  props.returnFunc(childReturnValue.value);
}



</script>