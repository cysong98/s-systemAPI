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
        <v-tab :value="1">부재 등록</v-tab>
        <v-tab :value="2">등록내역/해제</v-tab>
      </v-tabs>

      <v-window v-if="selectedTab==1">
        <v-window-item>
          <div class="row">
            <div class="col">
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
                <v-btn class="magnify-solid" @click="searchUser">
                  <v-icon></v-icon>검색
                </v-btn>
                <span style="margin-left:180px;">복귀일</span>
                <v-text-field :min="today" type="date" v-model="absencedt" variant="outlined" hide-details="auto"></v-text-field>
              </div>
            </div>
          </div>
          
          <div class="row">
            <div class="col half-fix">
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
                :no-data-text="noDataText"
                :loading="selectOrganizationInfoLoader"
                @click:row="(event, item) => openTree(item.item.raw)"
                select-strategy="single"
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
                height="200px"
                fixed-header
              >
              <template v-slot:bottom></template>
              </v-data-table-server> 
            </div>
          </div>
        </v-window-item>
      </v-window>

      <v-window v-if="selectedTab==2">
        <v-window-item>
          <div class="row">
            <div class="col">
              <v-table class="table-type-03 narrow" v-if="isAbsence">
                <colgroup>
                  <col width="250px">
                  <col width="">
                </colgroup>
                <tbody>
                  <tr>
                    <th>성명</th>
                    <td>{{ getUserLoginData.username }}</td>
                  </tr>
                  <tr>
                    <th>대리 결재자</th>
                    <td>{{ `${proxyApprover.username} (${proxyApprover.orgname}/${proxyApprover.grade})` }}</td>
                  </tr>
                  <tr>
                    <th>복귀일</th>
                    <td>{{ dayjs(absenceData.absencedt).format('YYYY-MM-DD') }}</td>
                  </tr>
                  <tr>
                    <th>등록해제</th>
                    <td><v-checkbox class="v-checkbox-square-btn" v-model="chkbox" hide-details="auto"></v-checkbox></td>
                  </tr>
                </tbody>
              </v-table>
              <span v-if="!isAbsence">등록된 내역이 없습니다.</span>
            </div>
          </div>
        </v-window-item>
      </v-window>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" v-if="selectedTab == 1" @click="absenceRegist">등록</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" v-if="selectedTab == 2 && chkbox" @click="absenceCancel">해제</v-btn>
    </div>

  </div>
  
  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>

</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('AbsencePopup')
const props = defineProps({
  toggleFunc: Function,
})
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for selectOrganization
const selectOrganizationDefCondi = {
  orgid: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectOrganizationCondi = ref({ ...selectOrganizationDefCondi })
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

const absencedt = ref('');
const today = ref(dayjs().format('YYYY-MM-DD'))
const tomorrow = ref(dayjs().add(1,'day').format('YYYY-MM-DD'));
const isloading = ref(false)

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
const clear = ref(true);

const onNodeClick = async (node) => {
  initialClick.value = true;
  selectOrganizationInfoCondi.value.orgid = node.orgid;
  selectOrganizationInfoCondi.value.userid = "";
  selectOrganizationInfoCondi.value.username = "";
  comboInputData.value = "";
  if(clear.value){
    searchText.value = "";
  }
  await selectOrganizationInfo();
  treeNodeColor(node);
  isSearch.value = false;
  clear.value = true;
};

/* ============================================== */
const initialNode = ref({});
const selectedTab = ref(1)

onBeforeMount(() => {
})

onMounted(async () => {
  absencedt.value = tomorrow.value;
  setCondiClear(selectOrganizationInfoCondiCheck.value, selectOrganizationInfoCondi.value);
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganization();
  treeData.value = makeTree.value;
  if (treeData.value.length > 0) {
    treeData.value[0].expanded = true;
  }
  await selectOrganizationInfo();
  await initialSearch();
  await checkAbsence();
})
watch(route, async (route) => {
  setQueries(route, selectOrganizationInfoCondi.value, selectOrganizationInfoDefCondi);
  await selectOrganizationInfo(); 
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

// 초기 본인 부서 검색
const initialSearch = async () => {
  initialNode.value.orgid = getUserLoginData.value.deptid;
  initialNode.value.label = getUserLoginData.value.deptname;
  searchText.value = getUserLoginData.value.deptname;
  clear.value = false;
  await onNodeClick(initialNode.value);
}

const staticColumnsSelectOrganizationInfo = [
  { key: "grade", title: "직위", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "username", title: "성명", width: "40px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "vgroupname", title: "권한", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
]

// userinfo identifier
const selectOrganizationInfoIdentifier = computed(() => (item) => {
  item.identifier = `${item.orgid}-${item.userid}-${item.username}`;
 return item.identifier;
});

// userinfo single select
const selectOrganizationInfoFinalData = computed(() => {
  const filteredData = selectOrganizationInfoList.value.filter((item) => 
    checkedListSelectOrganizationInfo.value.includes(item.identifier))
  return filteredData.map((item) => {
    const ask = radioData.find(radioItem => radioItem.key === selectedAsktype.value);
    item.askTemp = ask.view;
    item.asktype = ask.key;
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


const closePopUp = () => {
  props.toggleFunc();
}

const absenceRegist = async () => {
  if(isAbsence.value){
    if(!confirm("이미 등록된 내역이 존재합니다. 수정하시겠습니까?")){
      return;
    }
  }
  if(absencedt.value == ''){
    alert("복귀일을 선택해주세요");
    return;
  }

  if(checkedListSelectOrganizationInfo.value.length == 0){
    alert("대리 결재자를 선택해주세요");
  }else if(checkedListSelectOrganizationInfo.value[0].split("-")[1] == getUserLoginData.value.userid){
    alert("본인은 선택하실 수 없습니다.");
    checkedListSelectOrganizationInfo.value = [];
  }else{
    if(!confirm(`${getUserLoginData.value.username}님의 부재 등록을 하시겠습니까? \n대리결재자 : ${checkedListSelectOrganizationInfo.value[0].split("-")[2]}님 \n부재기간 : ${today.value} ~ ${absencedt.value}`)){
      return;
    }else{
      await updateAbsence();
    }
  }
}

const updateAbsenceCondi = ref({
  userid: "",
  absenceyn: "",
  absenceid: "",
  absencedt: "",
});

// 부재 등록
const updateAbsence = async () => {
  updateAbsenceCondi.value.userid = getUserLoginData.value.userid;
  updateAbsenceCondi.value.absenceyn = 'Y';
  updateAbsenceCondi.value.absenceid = checkedListSelectOrganizationInfo.value[0].split("-")[1];
  updateAbsenceCondi.value.absencedt = absencedt.value;
  API.loginAPI
    .updateAbsence(updateAbsenceCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        isloading.value = false;
        alert("등록을 완료하였습니다.");
        props.toggleFunc();
      }
    })
    .catch(error => {
      console.log(error);
      isloading.value = false;
      alert("Server Error");
    });
}

// 부재 등록 내용 확인
const isAbsence = ref(false);
const absenceData = ref([]);
const checkAbsence = async () => {
  try {
    const response = await API.loginAPI.getUserLogin({userid: getUserLoginData.value.userid}, urlPaths.value);
    absenceData.value = response.data;
    isAbsence.value = absenceData.value.absenceyn == "Y" ? true : false;
    if (isAbsence.value) {
      await processAfterAbsenceCheck();
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};
const proxyApprover = ref([]);
const processAfterAbsenceCheck = async () => {
  try {
    const response = await API.loginAPI.getUserLogin({ userid: absenceData.value.absenceid }, urlPaths.value);
    proxyApprover.value = response.data;
  } catch (error) {
    console.error(error);
    alert("Server Error");
  }
};
watch(selectedTab, (newValue, oldValue) => {
  if (newValue === 2) {
    checkAbsence();
  }
});

// 부재 해제
const cancelAbsenceCondi = ref({
  userid: "",
  absenceyn: "",
  absenceid: "",
  absencedt: "",
});
const chkbox = ref(false);
const absenceCancel = async () => {
  if(!confirm("해제하시겠습니까?")){
    return;
  }
  cancelAbsenceCondi.value.userid = getUserLoginData.value.userid;
  cancelAbsenceCondi.value.absenceyn = 'N';
  cancelAbsenceCondi.value.absenceid = null;
  cancelAbsenceCondi.value.absencedt = null;
  API.loginAPI
    .updateAbsence(cancelAbsenceCondi.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert(`${getUserLoginData.value.username}님의 부재 등록이 해제되었습니다.`);
        props.toggleFunc();
      }
    })
    .catch(error => {
      console.log(error);
      alert("Server Error");
    });
}

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

</script>