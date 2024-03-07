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
              @keyup.enter="selectUserList(1)"
              v-model="comboInputData"
              :maxlength="maxLengthFunc()"
              variant="outlined"
              hide-details="auto"
              required
              clearable
            />
            <v-btn class="magnify-solid" @click="selectUserList(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
        </div>
      </div>

      <div class="row" style="display:block;">
        <div class="col" style="height: 442px;">
          <v-data-table
            @click:row="(event, item) => selectChief(item)"
            v-model="checkedSelectUserList"
            :headers="staticColumnsSelectList"
            :items="selectUserListList"
            :items-per-page="pageSizeSelectUserList"
            :item-value="selectUserListIdentifier"
            :no-data-text="noDataText"
            hover
            class="table-type-02"
          >
            <template v-slot:bottom></template>
          </v-data-table>
        </div>
        <v-pagination
          v-model="currentPageSelectUserList"
          :length="totalPagesSelectUserList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectUserList"
        ></v-pagination>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>
  </div>
</template>
    
<script setup>
import console from "console";

import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import { validCheckSearchInput } from "@/utils/Utils.js";
import _ from 'lodash';
import { API } from '@/api'; 

const name = ref('BmsComUserList')
const props = defineProps({
  orgid: String,
  toggleFunc: Function,
  returnFunc: Function
})

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for column combo
const comboSelected = ref("username")
const comboInputData = ref("")
const comboData = ref([
  { key: "username", view: "이름" },
  { key: "deptname", view: "부서명" }
]);
// for selectUserList
const checkedSelectUserList = ref([]);
const selectUserListList = ref([])
const selectUserListDefCondi = {
  username: "",
  deptname: "",
  grade: "",
  pageNum: 1,
  pageSize: 10,
}
const selectUserListCondi = ref({ ...selectUserListDefCondi })
const selectUserListCondiCheck = ref({ value: { ...selectUserListDefCondi }, flag: { ...selectUserListDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value === "username")
    return 99;
  else
    return 255;
}

/* ================== pagination ================== */
const totalPagesSelectUserList = ref(0)
const currentPageSelectUserList = ref(1)
const pageSizeSelectUserList = ref(10)

const handlePageChangeSelectUserList = () => {
  selectUserList(currentPageSelectUserList.value);
}
/* =============================================== */


onMounted(async () => {
  if(props.orgid != undefined) {
    comboData.value = [{ key: "username", view: "이름" }];
    selectUserListCondi.value.deptid = props.orgid;
  }
  setCondiClear(selectUserListCondiCheck.value, selectUserListCondi.value);
  await selectUserList(1);
})

watch(route, async (route) => {
  setQueries(route, selectUserListCondi.value, selectUserListDefCondi);
  await selectUserList(selectUserListCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUserListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUserListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectUserListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUserListCondiCheck.value, selectUserListCondi.value, selectUserListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectUserListCondi.value["deptname"] = "";
  selectUserListCondi.value["username"] = "";
  setCondiChanged(selectUserListCondiCheck.value, selectUserListCondi.value, selectUserListCondi.value);
})


const staticColumnsSelectList = [
  { title: "부서명", key: "deptname", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "직급", key: "grade", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "이름", key: "username", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

/* ======================= multi selection ======================= */
const selectUserListIdentifier = computed(() => (item) => {
  item.identifier = {
    userid: item.userid,
    username: item.username
  };
  
  return item.identifier;
});
/* =============================================================== */


const selectChief = (item) => {
  props.returnFunc(item.item.value);
};

const closePopUp = () => {
  props.toggleFunc();
};

const noDataText = ref("조건에 맞는 데이터가 없습니다.");
const selectUserList = async (pageNum) => {
  selectUserListCondi.value.pageNum = parseInt(pageNum);

  if(!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectUserListCondi.value[comboSelected.value] = comboInputData.value;
    else return;
  }
  try {
    const response = await API.comAPI.selectUserList({ ...selectUserListCondi.value }, urlPaths.value);
    selectUserListList.value = response.data.list;
    totalPagesSelectUserList.value = response.data.pages;

    setCondiClear(selectUserListCondiCheck.value, selectUserListCondi.value)
  } catch (error) {
    console.log(error);
  }
}
  
</script>