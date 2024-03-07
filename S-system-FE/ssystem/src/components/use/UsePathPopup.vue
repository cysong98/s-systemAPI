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
            :items-length="selectOrganizationInfoList.length"
            :loading="selectOrganizationInfoLoader"
            :item-value="selectOrganizationInfoIdentifier"
            select-strategy="single"
            show-select
            :no-data-text="noDataText"
            @click:row="(event, item) => openTree(item.item.raw)"
            class="table-type-02 narrow"
            height="200px"
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
            height="200px"
            fixed-header
          >
          <template v-slot:bottom></template>
          </v-data-table-server> 
        </div>
        <div class="col one-third-fix border px-3">
          <h3 class="h3">구분</h3>
          <v-radio-group  v-model="selectedAsktype" class="category" hide-details="true" style="padding-top: 10px;">
            <div v-for="(data,idx) in radioData" :key="idx">
              <v-radio :label="data.view" color="indigo" :value="data.key"></v-radio>
            </div>
          </v-radio-group>
        </div>
      </div>


      <div class="row center">
        <v-icon size="x-large" color="blue" @click="moveDataDown">mdi-chevron-down-box</v-icon>
        <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
      </div>

      <div class="row">
        <div class="col flex1">
          <v-data-table
            v-model="checkedList"
            :headers="selectedHeaders"
            :items="selectedData"
            :item-value="makeIdentifier"
            select-strategy="single"
            show-select
            :no-data-text="noSelectedText"
            class="table-type-02 narrow"
            fixed-header
            height="184"
          >
          <template v-slot:bottom></template>
          </v-data-table>
        </div>
        <div class="col flex0">
          <div class="change-order">
            <v-icon size="x-large" color="blue" @click="rowUp">mdi-arrow-up-thick</v-icon>
            <v-icon size="x-large" color="blue" @click="rowDown">mdi-arrow-down-thick</v-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>

  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { treeNodeColor, handleMouseOver } from "@/utils/Utils.js";
import { transVgroupIcon } from "@/utils/TransFormLabelDataUtil.js"
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)

const name = ref('UsePathPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})
const managerids = ref({});

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
const isCnsrv = ref(false);
const isTrans = ref(false);
const initialNode = ref({});

const initialDeptid = ref('');
const initialDeptname = ref('');

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
  selectedData.value = args.value.path;
  managerids.value = args.value.managerids;
  if(args.value.cnsrv != undefined){
    isCnsrv.value = true;
  }else if(args.value.trans != undefined){
    isTrans.value = true;
  }
  initialDeptid.value = args.value.initialDeptid;
  initialDeptname.value = args.value.initialDeptname;
  await initialSearch();
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

// 초기 본인 부서 검색
const initialSearch = async () => {
  initialNode.value.orgid = initialDeptid.value;
  initialNode.value.label = initialDeptname.value;
  searchText.value = initialDeptname.value;
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
  item.identifier = `${item.orgid}-${item.userid}`;
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

// 구분 라디오 버튼
const selectedAsktype = ref("3");
const radioData = [
  {view: "검토", key:"2"},
  {view: "승인", key:"3"},
]

// 아래쪽 멀티 select
const makeIdentifier = computed(() => (item) => {
  item.identifier = `${item.orgid}-${item.userid}`;
 return item.identifier;
});

const noSelectedText = "선택된 데이터가 없습니다."
const checkedList = ref([]);
const selectedData = ref([]);
const selectedHeaders = [
  {key: "orgname", title: "부서"},
  {key: "grade", title: "직위(직급)"},
  {key: "username", title: "성명"},
  {key: "askTemp", title: "구분"},
];

// 부재 여부 체크
const absenceid = ref(null);
const isAbsence = async () => {
  const absenceyn =  selectOrganizationInfoFinalData.value[0].absenceyn;
  if(absenceyn != undefined && absenceyn == 'Y'){
    absenceid.value = selectOrganizationInfoFinalData.value[0].absenceid;
    await findReplaceApproverInfo();
  }else{
    absenceid.value = null;
  }
};

// 대리 결재자 정보 조회
const findReplaceApproverInfoCondi = ref({userid: ""})
const replaceApproverInfo = ref([]);
const findReplaceApproverInfo = async () => {
  if(absenceid.value){
    findReplaceApproverInfoCondi.value.userid = absenceid.value;
      try {
        const response = await API.dctAPI.selectManagerInfo({ ...findReplaceApproverInfoCondi.value },urlPaths.value);
        response.data.map((u)=>{
          u.identifier = `${u.orgid}-${u.userid}`;
        });
        replaceApproverInfo.value = response.data[0];
      } catch (error) {
        console.log(error);
        alert("Server Error");
      }
  }
};

// 대리 결재자 등록
const absenceUsersIds = ref([]);
const pushReplaceApprover = () => {
  const user = selectOrganizationInfoFinalData.value[0].username;
  const date = dayjs(selectOrganizationInfoFinalData.value[0].absencedt).format('YYYY-MM-DD');
  const replacer = replaceApproverInfo.value.username;
  alert(`${user}님은 ~${date} 까지 부재중입니다. \n대리결재자인 ${replacer}님으로 등록됩니다.`);
  const selectedAsktype = selectOrganizationInfoFinalData.value[0].asktype;
  let askTemp = "";
  let asktype = "";
  switch(selectedAsktype){
    case '2': 
      askTemp = '검토';
      asktype = '2';
      break;
    case '3':
      askTemp = '승인';
      asktype = '3';
      break;
  }
  replaceApproverInfo.value.asktype = asktype;
  replaceApproverInfo.value.askTemp = askTemp;
  selectedData.value.push(replaceApproverInfo.value);
  absenceUsersIds.value.push({orgnuserid: selectOrganizationInfoFinalData.value[0].userid, replacerid: selectOrganizationInfoFinalData.value[0].absenceid});
};

// 대리 결재자 중복 등록 체크
// (user1의 대리결재자인 user2가 등록된 후 다시 user1을 선택한 경우)
const isAbsenceDupli = () => {
  return selectedData.value.some(data => {
    return absenceUsersIds.value.some(ids => 
      selectOrganizationInfoFinalData.value[0].userid === ids.orgnuserid && data.userid === ids.replacerid
    );
  });
};

// 대리 결재자 중복 등록 체크2
// (user2가 이미 등록되어 있고 user1(user1의 대리결재자는 user2)을 선택한 경우)
const isAbsenceDupli2 = () => {
  return selectedData.value.some(data => data.userid == absenceid.value);
};


// 대리 결재자 삭제
const deleteReplaceApprover = (row) => {
  const index = absenceUsersIds.value.findIndex(item => item.replacerid === row.userid);
  if (index !== -1) {
    absenceUsersIds.value.splice(index, 1);
  }
};

const moveDataDown = async () => {
  if (selectOrganizationInfoFinalData.value.length != 0) {
    const newData = { ...selectOrganizationInfoFinalData.value[0] };
    const isDuplicate = selectedData.value.some(
      item => item.identifier === newData.identifier
    );
    const isInitialDept = selectOrganizationInfoFinalData.value[0].deptid == initialDeptid.value ? true : false;
    const isMe = selectOrganizationInfoFinalData.value[0].userid == getUserLoginData.value.userid ? true : false;
    const isManager = managerids.value.includes(selectOrganizationInfoFinalData.value[0].userid) ? true : false;
    let possibleCnsrv = false;
    if(isCnsrv.value){
      let vgroupid = '';
      if(selectOrganizationInfoFinalData.value[0].vgroupid != undefined){
        const vgroupidArr = selectOrganizationInfoFinalData.value[0].vgroupid.split(',').map(value => value.trim());
        if (vgroupidArr.length > 0 && vgroupidArr.includes('8010')) {
          vgroupid = '8010';
        }else if(vgroupidArr.length > 0 && vgroupidArr.includes('8015')){
          vgroupid = '8015';
        }else{
          vgroupid = selectOrganizationInfoFinalData.value[0].vgroupid;
        }
      }
      possibleCnsrv = vgroupid !== undefined && (vgroupid === "8010" || vgroupid === "8015") || isInitialDept;
    }
    let possibleTrans = false;
    if(isTrans.value){
      let vgroupid = '';
      if(selectOrganizationInfoFinalData.value[0].vgroupid != undefined){
        const vgroupidArr = selectOrganizationInfoFinalData.value[0].vgroupid.split(',').map(value => value.trim());
        if (vgroupidArr.length > 0 && vgroupidArr.includes('8050')) {
          vgroupid = '8050';
        }else if(vgroupidArr.length > 0 && vgroupidArr.includes('8255')){
          vgroupid = '8255';
        }else{
          vgroupid = selectOrganizationInfoFinalData.value[0].vgroupid;
        }
      }
      possibleTrans = vgroupid !== undefined && (vgroupid === "8050" || vgroupid === "8255") || isInitialDept;
    }
    await isAbsence();
    if (isDuplicate) {
      alert("이미 지정된 결재선지정자입니다");
      chkClear();
    } else if(isAbsenceDupli()){
      alert("이미 대리 결재자가 지정되어있습니다.");
      chkClear();
    } else if(isAbsenceDupli2()){
      alert(`${selectOrganizationInfoFinalData.value[0].username}님의 대리결재자인 ${replaceApproverInfo.value.username}님이 이미 지정되어있습니다.`);
      chkClear(); 
    } else if(isMe){
      alert("본인은 선택하실 수 없습니다.");
      chkClear();
    } else if(isManager){
      alert("이미 지정된 담당자입니다.");
      chkClear();
    } else if(isCnsrv.value && !possibleCnsrv){
      alert(`${initialDeptname.value}의 비밀관리자, 부서장 또는 존안관리담당자, 존안관리부서장만 선택할 수 있습니다.`);
      chkClear();
    } else if(isTrans.value && !possibleTrans){
      alert(`${initialDeptname.value}의 비밀관리자, 부서장 또는 이관관리담당자, 이관관리부서장만 선택할 수 있습니다.`);
      chkClear();
    } else if(!isInitialDept && isCnsrv.value == false && isTrans.value == false){
      alert(`${initialDeptname.value}의 비밀관리자, 부서장만 선택할 수 있습니다.`);
      chkClear();
    } else {
      if(isCnsrv.value || isTrans.value){
        const grade = newData.vgroupname ? `(${newData.vgroupname})${newData.grade}` : `${newData.grade}`;
        newData.grade = grade;
      }
      if(absenceid.value != null){
        pushReplaceApprover();
      }else{
        selectedData.value.push(newData);
      }
      chkClear();
    }
  } else {
    alert("결재선지정자를 선택해주세요");
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
        deleteReplaceApprover(row);
      }
    })
  }else{
    alert("삭제하실 결재선지정자를 선택해주세요")
  }
};

const rowUp = () => {
  if(makeRowData.value.length !== 0){
    const index = selectedData.value.indexOf(makeRowData.value[0]);
    if (index > 0) {
      const temp = selectedData.value[index];
      selectedData.value[index] = selectedData.value[index - 1];
      selectedData.value[index - 1] = temp;
    }
  }
};
const rowDown = () => {
  if(makeRowData.value.length !== 0){
    const index = selectedData.value.indexOf(makeRowData.value[0]);
    if (index < selectedData.value.length - 1 && index !== -1) {
      const temp = selectedData.value[index];
      selectedData.value[index] = selectedData.value[index + 1];
      selectedData.value[index + 1] = temp;
    }
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


const closePopUp = () => {
  props.toggleFunc();
}

const confirmPopUp = () => {
  if(!selectedData.value.some(item => ['3'].includes(item.asktype))) {
    alert("승인자를 선택해주세요");
    return;
  }
  childReturnValue.value = selectedData.value;
  props.returnFunc(childReturnValue.value);
}
</script>

<style scoped>
.density {
  padding: 0px 4px;
  height: 15px;
}
</style>