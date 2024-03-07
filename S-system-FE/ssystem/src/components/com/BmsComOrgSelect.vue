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
    <div class="content"  style="height: 570px;">
      <div class="row">
        <div class="col">
          <div class="box-flex">
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
              @keyup.enter="selectOrgList(1)"
              v-model="comboInputData"
              :maxlength="maxLengthFunc()"
              variant="outlined"
              hide-details="auto"
              required
              clearable
            />
            <v-btn class="magnify-solid" @click="selectOrgList(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
        </div>
      </div>
      <div class="row" style="display:block;">
        <div class="col">
          <!-- 단일 선택 -->
          <v-data-table
            v-if="visibleCheckBox === 'singular'"
            @click:row="(event, item) => selectDept(item.item.raw)"
            v-model="checkedSelectOrgList"
            :headers="staticColumnsSelectList"
            :items="selectOrgListList"
            :items-per-page="pageSizeSelectOrgList"
            :item-value="selectOrgListIdentifier"
            hover
            class="table-type-02"
          >
            <template v-slot:bottom></template>
          </v-data-table>
          <v-pagination
            v-if="visibleCheckBox === 'singular'"
            v-model="currentPageSelectOrgList"
            :length="totalPagesSelectOrgList"
            class="pagination"
            total-visible="5"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            @click="handlePageChangeSelectOrgList"
          ></v-pagination>
            
          <!-- 복수 선택 -->
          <v-data-table
            v-if="visibleCheckBox === 'plural'"
            v-model="checkedSelectOrgList"
            :headers="staticColumnsSelectList"
            :items="selectOrgListList"
            :items-per-page="pageSizeSelectOrgList"
            :item-value="selectOrgListIdentifier"
            height="200"
            show-select
            fixed-header
            hover
            class="table-type-05"
          >
            <template v-slot:bottom></template>
          </v-data-table>
        </div>
        <div v-if="visibleCheckBox === 'plural'">
          <v-pagination
            v-model="currentPageSelectOrgList"
            :length="totalPagesSelectOrgList"
            total-visible="5"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            style="padding-bottom: 5px;"
            @click="handlePageChangeSelectOrgList"
          ></v-pagination>
          <div class="row center">
            <v-icon size="x-large" color="blue" @click="moveDataDown"
              >mdi-chevron-down-box</v-icon
            >
            <v-icon size="x-large" color="blue" @click="moveDataUp"
              >mdi-chevron-up-box</v-icon
            >
          </div>
          <div class="col flex1">
            <v-data-table
              v-model="selectedList"
              :headers="selectedHeaders"
              :items="selectedData"
              :item-value="makeIdentifier"
              items-per-page="100"
              show-select
              :no-data-text="noSelectedText"
              class="table-type-05 narrow"
              fixed-header
              height="184">
              <template v-slot:bottom></template>
            </v-data-table>
          </div>
        </div>
      </div>
    </div>

    <div class="buttons-bottom" style="padding-top: 0px;">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if="visibleCheckBox === 'plural'" variant="flat" color="indigo-darken-3" rounded="xl" @click="selectDept()">저장</v-btn>
    </div>
  </div>
</template>
  
<script setup>
import console from "console";

import { ref, onBeforeMount, onMounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 

const name = ref('BmsComOrgSelectt')
const props = defineProps({
  args: String,
  toggleFunc: Function,
  returnFunc: Function
})
const visibleCheckBox = ref("");

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("orgname")
const comboInputData = ref("")
const comboData = [
  { key: "orgname", view: "부서명" },
  { key: "orgid", view: "부서아이디" }
]
// for selectOrgList
const noSelectedText = ref("선택된 데이터가 없습니다.");
const checkedSelectOrgList = ref([]);
const selectedData = ref([]);
const selectedList = ref([]);
const selectOrgListList = ref([])
const selectOrgListDefCondi = {
  orgid: "",
  orgname: "",
  toporgid: "",
  pageNum: 1,
  pageSize: 10,
}
const selectOrgListCondi = ref({ ...selectOrgListDefCondi })
const selectOrgListCondiCheck = ref({ value: { ...selectOrgListDefCondi }, flag: { ...selectOrgListDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "orgname")
    return 255;
  else
    return 35;
};

/* ================== pagination ================== */
const totalPagesSelectOrgList = ref(0)
const currentPageSelectOrgList = ref(1)
const pageSizeSelectOrgList = ref(10)

const handlePageChangeSelectOrgList = () => {
  checkedSelectOrgList.value = [];
  selectOrgList(currentPageSelectOrgList.value);
}
/* =============================================== */


onMounted(async () => {
  visibleCheckBox.value = props.args;
  setCondiClear(selectOrgListCondiCheck.value, selectOrgListCondi.value);
  await selectOrgList(selectOrgListCondi.value.pageNum);
})

watch(route, async (route) => {
  setQueries(route, selectOrgListCondi.value, selectOrgListDefCondi);
  await selectOrgList(selectOrgListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectOrgListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectOrgListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectOrgListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectOrgListCondiCheck.value, selectOrgListCondi.value, selectOrgListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectOrgListCondi.value["orgid"] = "";
    selectOrgListCondi.value["orgname"] = "";
  setCondiChanged(selectOrgListCondiCheck.value, selectOrgListCondi.value, selectOrgListCondi.value);
})

watchEffect(() => {
  visibleCheckBox.value = props.args;
})


const staticColumnsSelectList = [
  { title: "부서아이디", key: "orgid", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서명", key: "orgname", width: "50%",sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

const selectedHeaders = [
  { title: "부서아이디", key: "deptid", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "부서명", key: "deptname", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];
/* ======================= multi selection ======================= */
const selectOrgListIdentifier = computed(() => (item) => {
  item.identifier = {
    deptid: item.orgid,
    deptname: item.orgname,
  };
  
  return item.identifier;
});

// 아래쪽 멀티 select
const makeIdentifier = computed(() => item => {
  item.identifier = item.deptid;
  return item.identifier;
});
/* =============================================================== */


// Return Function 
const selectDept = (value) => {
  if(value !== undefined) {
    props.returnFunc(value);
    return;
  }

  let item = [];
  if(selectedData.value.length !== 0) {
    selectedData.value.map((it) => {
      item.push(it);
    })
  }
  else if(checkedSelectOrgList.value.length !== 0) {
    checkedSelectOrgList.value.map((it) => {
      item.push(it);
    })
  }
  props.returnFunc(item);
};

// Popup Function
const closePopUp = () => {
  props.toggleFunc();
};

const moveDataDown = () => {
  if(checkedSelectOrgList.value.length !== 0) {
    checkedSelectOrgList.value.map((item) => {
      const isDuplicate = selectedData.value.some((el) => el.deptid == item.deptid);
      if(isDuplicate === false) {
        selectedData.value.push(item);
      }
    })
    checkedSelectOrgList.value = [];
  }
};

const moveDataUp = () => {
  if(selectedList.value.length !== 0) {
    selectedList.value.forEach(row => {
      const index = selectedData.value.findIndex((el) => {
        if(el.deptid == row)
          return true;
      });
      if (index !== -1) {
        selectedData.value.splice(index, 1);
      }
    });
    selectedList.value = [];
  }
};


const selectOrgList = async (pageNum) => {
  selectOrgListCondi.value.pageNum = parseInt(pageNum);

  if(!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectOrgListCondi.value[comboSelected.value] = comboInputData.value;
    else return;
  }
  try {
    const response = await API.comAPI.selectOrganizationList({ ...selectOrgListCondi.value }, urlPaths.value);
    selectOrgListList.value = response.data.list;
    totalPagesSelectOrgList.value = response.data.pages;

    setCondiClear(selectOrgListCondiCheck.value, selectOrgListCondi.value)
  } catch (error) {
    console.log(error);
  }
}

</script>