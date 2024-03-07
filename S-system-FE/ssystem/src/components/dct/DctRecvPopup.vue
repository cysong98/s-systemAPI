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
      <v-tabs v-model="selectedTab" align-tabs="end" class="tabs">
        <v-tab :value="1">원내</v-tab>
        <v-tab :value="2">대내</v-tab>
        <v-tab :value="3">외부</v-tab>
      </v-tabs>

      <!-- 원내 -->
      <v-window v-if="selectedTab == 1 ">
        <v-window-item>
          <div class="row">
            <div class="col half-fix">
              <div class="box-flex">
                <v-label>부서명</v-label>
                <v-text-field
                  v-model="selectOrganizationInfoCondi.orgname"
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
          </div>
          <div class="row center">
            <v-icon size="x-large" color="blue" @click="moveInternalDataDown">mdi-chevron-down-box</v-icon>
            <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
          </div>
        </v-window-item>
      </v-window>

      <!-- 대내 -->
      <v-window v-else-if="selectedTab == 2">
        <v-window-item>
          <div class="row">
              <div class="col half-fix">
                <div class="box-flex">
                  <v-label>부서명</v-label>
                  <v-text-field
                    v-model="selectOrganizationInfoCondiLDAP.orgname"
                    variant="outlined"
                    hide-details="auto"
                    @keyup.enter="selectOrgnameLDAP"
                    clearable
                  />
                  <v-btn class="magnify" @click="selectOrgnameLDAP">
                    <v-icon></v-icon>
                  </v-btn>
                </div>
              </div> 
            </div>
            <div class="row">
              <div class="col">
                <Tree
                  class="tree"
                  :nodes="treeDataLDAP"
                  :use-checkbox="false"
                  :use-icon="true"
                  :search-text="searchTextLDAP"
                  @nodeClick="onNodeClickLDAP"
                  @mouseover="handleMouseOver"
                />
              </div>
            </div>
            <div class="row center">
              <v-icon size="x-large" color="blue" @click="moveDomesticDataDown">mdi-chevron-down-box</v-icon>
              <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
            </div>
        </v-window-item>
      </v-window>

      <!-- 외부 -->
      <v-window v-else-if="selectedTab == 3">
        <v-window-item>
          <div class="row">
            <div class="col">
              <section class="section">
                <v-table class="table-type-03">
                  <colgroup>
                    <col width="120">
                    <col width="">
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>수신 기관
                        <v-btn size="xsmall" class="magnify" @click="dctRecvExternalPopupToggleFunc">
                          <v-icon></v-icon>
                        </v-btn>
                      </th>
                      <td>
                        <v-text-field
                          :readonly="true"
                          v-model="recvExternalData.apprrecvdeptname"
                          variant="outlined"
                          hide-details="auto"></v-text-field>
                      </td>
                    </tr>
                    <tr>
                      <th>참조 기관</th>
                      <td>
                        <v-text-field 
                          v-model="recvExternalData.apprrecvrefdeptname"
                          variant="outlined"
                          hide-details="auto"></v-text-field>
                      </td>
                    </tr>
                    <tr>
                      <th>수신자 기호</th>
                      <td>
                        <v-text-field 
                          :readonly="true"
                          v-model="recvExternalData.receiverSymbol"
                          variant="outlined"
                          hide-details="auto"></v-text-field>
                      </td>
                    </tr>
                  </tbody>
                </v-table>
              </section>
            </div>
          </div>
          <div class="row center">
            <v-icon size="x-large" color="blue" @click="moveExternalDataDown">mdi-chevron-down-box</v-icon>
            <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
          </div>
        </v-window-item>
      </v-window>

      <div class="row">
        <v-data-table-server
          v-model="checkedList"
          :headers="selectedOrgHeaders"
          :items="selectedOrgList"
          :items-length="selectedOrgList.length"
          item-value="receiverid"
          select-strategy="single"
          :no-data-text="noDataText"
          class="table-type-02 narrow"
          height="123px"
          show-select
        >
        <template v-slot:bottom></template>
        </v-data-table-server>
      </div>
      <v-table class="table-type-03" v-if="selectedOrgList.length>0">
        <colgroup>
          <col width="120px">
          <col>
        </colgroup>
        <tbody>
          <tr>
            <th>
              <v-label>수신처표시명</v-label>
            </th>
            <td>
              <div style="display: flex;">
                <v-text-field
                  v-model="recvDisplayName"
                  variant="outlined"
                  hide-details="auto"
                  style="margin-right: 5px;"
                ></v-text-field>
                <v-btn @click="updateRecvDisplayName">변경</v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>
  </div>

    <!-- 외부 수신기관 지정 팝업 -->
    <v-dialog v-model="bVisibleDctRecvExternalPopup" style="width: 800px; min-height:500px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>외부 수신기관 지정</v-toolbar-title>
          <v-btn class="close" icon @click="bVisibleDctRecvExternalPopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <DctRecvExternalPopup
          v-if="bVisibleDctRecvExternalPopup"
          :args="dctRecvExternalPopupArgs"
          :toggleFunc="dctRecvExternalPopupToggleFunc"
          :returnFunc="dctRecvExternalPopupReturnFunc"
        ></DctRecvExternalPopup>
      </v-card>
    </v-dialog>

  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
import DctRecvExternalPopup from "./DctRecvExternalPopup.vue";

const name = ref('DctRecvPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const route = useRoute()
const router = useRouter()
const isloading = ref(false)
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
// for selectOrganization
const checkedList = ref([])
const selectOrganizationDefCondi = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectOrganizationCondi = ref({ ...selectOrganizationDefCondi })
const selectOrganizationDefCondiLDAP = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectOrganizationCondiLDAP = ref({ ...selectOrganizationDefCondi })
const noDataText = "선택된 데이터가 없습니다.";

/* ======================== 외부 수신기관 지정 팝업 ======================== */
const bVisibleDctRecvExternalPopup = ref(false)
const dctRecvExternalPopupArgs = ref({})
const dctRecvExternalPopupToggleFunc = ref(() => { 
  bVisibleDctRecvExternalPopup.value = !bVisibleDctRecvExternalPopup.value;
})
const dctRecvExternalPopupReturnFunc = ref((retValue) => {
  if(retValue != ''){
    recvExternalData.value.apprrecvdeptname = retValue.description;
    recvExternalData.value.receiverSymbol = retValue.codename;
    recvExternalData.value.receiverid = retValue.codeid;
  }
  dctRecvExternalPopupToggleFunc.value()
})
/* ============================================================ */

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
  selectOrganizationInfoCondi.value.orgname = "";
  selectOrganizationInfoCondi.value.orgid = node.orgid;
  if(clear.value){
    searchText.value = "";
  }
  await selectOrganizationInfo();
  treeNodeColor(node);
  clear.value = true;
};

/* ============================================== */

/* ================ tree - LDAP ========================== */
const makeTreeLDAP = computed(() => {
  const treeNodes = [];

  const buildTree = (orgData, idPrefix) => {
    const treeNode = {
      id: idPrefix,
      orgid: orgData.orgid,
      label: orgData.orgname,
      nodes: []
    };
    const childOrgs = selectOrganizationListLDAP.value.filter(item => item.parentorgid === orgData.orgid);
    childOrgs.forEach((childOrg, index) => {
      const childIdPrefix = idPrefix + (index + 1);
      treeNode.nodes.push(buildTree(childOrg, childIdPrefix));
    });
    return treeNode;
  };

  const rootOrgs = selectOrganizationListLDAP.value.filter(item => item.orgdepth === 1);
  rootOrgs.forEach((rootOrg, index) => {
    const rootIdPrefix = (index + 1).toString();
    const rootNode = buildTree(rootOrg, rootIdPrefix);
    treeNodes.push(rootNode);
  });
  return treeNodes;
});

const treeDataLDAP = ref([]);
const initialClickLDAP = ref(false);
const searchTextLDAP = ref("");
const clearLDAP = ref(true);

const onNodeClickLDAP = async(node) => {
  initialClickLDAP.value = true;
  selectOrganizationInfoCondiLDAP.value.orgname = "";
  selectOrganizationInfoCondiLDAP.value.orgid = node.orgid;
  if(clearLDAP.value){
    searchTextLDAP.value = "";
  }
  await selectOrganizationInfoLDAP();
  treeNodeColor(node);
  clearLDAP.value = true;
};

/* ============================================== */

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  await selectOrganization();
  await selectOrganizationLDAP();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  treeDataLDAP.value = makeTreeLDAP.value;
  if (treeDataLDAP.value.length > 0) {
    treeDataLDAP.value[0].expanded = true;
  }
  if(args.value != ""){
    if(args.value[0].receiverid !== ""){
      selectedOrgList.value = args.value;
    }
  }
})

watchEffect(() => {
  args.value = props.args
})

// 조직도 검색
const selectOrgname = async () => {
  selectOrganizationInfoCondi.value.orgid = "";
  searchText.value = selectOrganizationInfoCondi.value.orgname;
  const node = selectOrganizationList.value.find(obj => obj.orgname.includes(searchText.value));
  if(node){
    node.label = node.orgname;
    clear.value = false;
    await onNodeClick(node);
  }
};

// 조직도 검색 - LDAP(대내)
const selectOrgnameLDAP = async () => {
  selectOrganizationInfoCondiLDAP.value.orgid = "";
  searchTextLDAP.value = selectOrganizationInfoCondiLDAP.value.orgname;
  const node = selectOrganizationListLDAP.value.find(obj => obj.orgname.includes(searchTextLDAP.value));
  if(node){
    node.label = node.orgname;
    clearLDAP.value = false;
    await onNodeClickLDAP(node);
  }
};

// tree 데이터 조회용
const selectOrganizationList = ref([]);
const selectOrganization = async () => {
  isloading.value = true;
  try {
    const response = await API.dctAPI.selectOrganization({...selectOrganizationCondi.value}, urlPaths.value);
    selectOrganizationList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }finally{
    isloading.value = false;
  }
};

// tree 데이터 조회용 - LDAP(대내)
const selectOrganizationListLDAP = ref([]);
const selectOrganizationLDAP = async () => {
  isloading.value = true;
  try {
    const response = await API.dctAPI.selectOrganizationLDAP({...selectOrganizationCondiLDAP.value}, urlPaths.value);
    selectOrganizationListLDAP.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }finally{
    isloading.value = false;
  }
};

// 수신처명 검색, 노드클릭용
const selectOrganizationInfoCondi = ref({orgname:"", orgid: ""});
const searchResult = ref([]);
const selectOrganizationInfo = async () => {
  try {
    const response = await API.dctAPI.selectOrganization({ ...selectOrganizationInfoCondi.value }, urlPaths.value);
    searchResult.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 수신처명 검색, 노드클릭용 - LDAP(대내)
const selectOrganizationInfoCondiLDAP = ref({orgname:"", orgid: ""});
const searchResultLDAP = ref([]);
const selectOrganizationInfoLDAP = async () => {
  try {
    const response = await API.dctAPI.selectOrganizationLDAP({ ...selectOrganizationInfoCondiLDAP.value }, urlPaths.value);
    searchResultLDAP.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const selectedTab = ref(1);
const selectedOrgList = ref([]);
const selectedOrgHeaders = [
  { key: "apprrecvdeptname", title: "수신기관", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "apprrecvrefdeptname", title: "참조기관", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "receiverSymbol", title: "수신자기호", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "gubun", title: "구분", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

// 원내 - 등록
const moveInternalDataDown = () => {
  const isDuplicate = selectedOrgList.value.some((list) => list.receiverid === searchResult.value[0].orgid);
  if(!isDuplicate){
    const internalData = {
      receiverid: "",
      apprrecvtempflag: "",
      apprrecvdeptname: "",
      apprrecvrefdeptname: "",
      receiverSymbol: "",
      apprrecvgubun: "",
      gubun: "",
    };
    internalData.receiverid = searchResult.value[0].orgid;
    internalData.apprrecvtempflag = 'Y';
    internalData.apprrecvdeptname = searchResult.value[0].orgname;
    internalData.apprrecvrefdeptname = '';
    internalData.receiverSymbol = ''; 
    internalData.apprrecvgubun = 'RDR08';
    internalData.gubun = '원내'; 
    selectedOrgList.value.push(internalData);
  }else{
    alert("이미 지정된 수신처입니다.")
  }
};

// 원내 - 수신처명 변경
const recvDisplayName = ref("");
const updateRecvDisplayName = () => {
  if(checkedList.value.length == 0){
    alert("표기명을 변경하실 수신기관을 선택해주세요")
    return;
  }else{
    if(recvDisplayName.value == ""){
      alert("변경하실 표기명을 입력해주세요")
    }else{
      const index = selectedOrgList.value.findIndex(item => item.receiverid === checkedList.value[0]);
      if(index !== -1){
        selectedOrgList.value[index].apprrecvdeptname = recvDisplayName.value;
        recvDisplayName.value = "";
      }
    }
  }
};

// 대내 - 등록
const moveDomesticDataDown = () => {
  const isDuplicate = selectedOrgList.value.some((list) => list.receiverid === searchResultLDAP.value[0].orgid);
  if(!isDuplicate){
    const domesticData = {
      receiverid: "",
      apprrecvtempflag: "",
      apprrecvdeptname: "",
      apprrecvrefdeptname: "",
      receiverSymbol: "",
      apprrecvgubun: "",
      gubun: "",
      relaytype: "", //유통망 정보
    };
    domesticData.receiverid = searchResultLDAP.value[0].orgid;
    domesticData.apprrecvtempflag = 'Y';
    domesticData.apprrecvdeptname = searchResultLDAP.value[0].orgname;
    domesticData.apprrecvrefdeptname = '';
    domesticData.receiverSymbol = ''; 
    domesticData.apprrecvgubun = 'RDR01';
    domesticData.gubun = '대내'; 
    domesticData.relaytype = searchResultLDAP.value[0].relaytype; //유통망 정보
    selectedOrgList.value.push(domesticData);
  }else{
    alert("이미 지정된 수신처입니다.")
  }
};

// 외부 - 데이터
const recvExternalData = ref({
  apprrecvtempflag: "Y",
  receiverid: "",
  apprrecvdeptname: "",
  apprrecvrefdeptname: "",
  receiverSymbol: "",
  apprrecvgubun: "RDR02",
  gubun: "외부",
});

// 외부 - 등록
const moveExternalDataDown = () => {
  if(recvExternalData.value.apprrecvdeptname != ""){
    const isDuplicate = selectedOrgList.value.some((list) => list.receiverid === recvExternalData.value.receiverid);
    if(isDuplicate){
      alert("이미 지정된 수신처입니다.")
    }else{
      selectedOrgList.value.push({...recvExternalData.value});
    }
    recvExternalData.value.apprrecvdeptname = "";
    recvExternalData.value.apprrecvrefdeptname = "";
    recvExternalData.value.receiverSymbol = "";
  }else{
    alert("외부 수신기관을 선택해주세요")
  }
};

// 삭제
const moveDataUp = () => {
  if(checkedList.value.length !== 0){
    const index = selectedOrgList.value.findIndex(item => item.receiverid === checkedList.value[0]);
    if(index !== -1){
      selectedOrgList.value.splice(index,1);
      checkedList.value = [];
    }
  }else{
    alert("삭제하실 수신처를 선택해주세요")
  }
};


const closePopUp = () => {
  props.toggleFunc();
}

const confirmPopUp = () => {
  childReturnValue.value = selectedOrgList.value;
  props.returnFunc(childReturnValue.value);
}

</script>