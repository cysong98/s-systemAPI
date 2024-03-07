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
    <div class="content" style="height: 570px;">
      <div class="row">
        <div class="col">
          <div class="box-flex">
            <v-select
            label="선택"
            v-model="comboSelected"
            :items="comboData"
            item-title="view"
            item-value="key"
            variant="outlined"
            hide-details="auto"
          />
          <v-text-field
            @keyup.enter="selectUserVgroup(1)"
            v-model="comboInputData"
            :maxlength="maxLengthFunc()"
            variant="outlined"
            hide-details="auto"
            required
            clearable
          />
          <v-btn class="magnify-solid" @click="selectUserVgroup(1)">
            <v-icon></v-icon>검색
          </v-btn>
          </div>
        </div>
      </div>
      <div class="row" style="display:block;">
        <div class="col">
          <v-data-table
            v-model="checkedListSelectUserVgroup"
            :headers="staticColumnsSelectList"
            :items="selectUserVgroupList"
            :items-per-page="pageSizeSelectUserVgroup"
            :item-value="selectUserVgroupIdentifier"
            :no-data-text="noDataText"
            height="200"
            show-select
            hover
            class="table-type-05"
          >
            <template v-slot:bottom></template>
          </v-data-table>
        </div>
        <v-pagination
          v-model="currentPageSelectUserVgroup"
          :length="totalPagesSelectUserVgroup"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectUserVgroup"
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

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="selectVgroup()">저장</v-btn>
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

const name = ref('BmsComUserVgroupList')
const props = defineProps({
  args: Array,
  toggleFunc: Function,
  returnFunc: Function
})

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
  // for column combo
  const comboSelected = ref("vgroupname")
  const comboInputData = ref("")
  const comboData = [
    { key: "vgroupname", view: "코드명" },
    { key: "vgroupid", view: "코드아이디" }
  ]
// for selectUserVgroup
const loader = ref(true)
const noSelectedText = ref("선택된 데이터가 없습니다.");
const checkedListSelectUserVgroup = ref([])
const selectUserVgroupList = ref([])
const selectUserVgroupDefCondi = {
  vgroupid: "",
  vgroupname: "",
  pageNum: 1,
  pageSize: 10,
}
const selectUserVgroupCondi = ref({ ...selectUserVgroupDefCondi })
const selectUserVgroupCondiCheck = ref({ value: { ...selectUserVgroupDefCondi }, flag: { ...selectUserVgroupDefCondi } })

const maxLengthFunc = () => {
  if(comboSelected.value == "vgroupname")
    return 199;
  else
    return 35;
};

/* ================ pagination ================ */
const totalPagesSelectUserVgroup = ref(0)
const currentPageSelectUserVgroup = ref(1)
const pageSizeSelectUserVgroup = ref(10)

const handlePageChangeSelectUserVgroup = () => {
  selectUserVgroup(currentPageSelectUserVgroup.value);
}
/* ============================================== */

onMounted(async () => {
  setCondiClear(selectUserVgroupCondiCheck.value, selectUserVgroupCondi.value);
  setQueries(route, selectUserVgroupCondi.value, selectUserVgroupDefCondi);
  await selectUserVgroup(selectUserVgroupCondi.value.pageNum);
  selectedData.value = props.args;
})

watch(route, async (route) => {
  setQueries(route, selectUserVgroupCondi.value, selectUserVgroupDefCondi);
  await selectUserVgroup(selectUserVgroupCondi.value.pageNum); 
})

watch(() => _.cloneDeep(selectUserVgroupCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectUserVgroupCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
  selectUserVgroupCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectUserVgroupCondiCheck.value, selectUserVgroupCondi.value, selectUserVgroupCondi.value);
})

watch(comboSelected, async (comboSelected) => {
  selectUserVgroupCondi.value["vgroupid"] = "";
    selectUserVgroupCondi.value["vgroupname"] = "";
  setCondiChanged(selectUserVgroupCondiCheck.value, selectUserVgroupCondi.value, selectUserVgroupCondi.value);
})


const staticColumnsSelectList = [
  { title: "코드아이디", key: "vgroupid", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "코드명", key: "vgroupname", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];

const selectedHeaders = [
  { title: "코드아이디", key: "vgroupid", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { title: "코드명", key: "vgroupname", width: "50%", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
];
/* ======================= multi selection ======================= */
const selectUserVgroupIdentifier = computed(() => (item) => {
  item.identifier = {
    vgroupid: item.vgroupid,
    vgroupname: item.vgroupname
  };
  
  return item.identifier;
});

// 아래쪽 멀티 select
const makeIdentifier = computed(() => item => {
  item.identifier = item.vgroupid;
  return item.identifier;
});
/* =============================================================== */

const noDataText = ref("조건에 맞는 데이터가 없습니다.");
const selectUserVgroup = async (pageNum) => {

  loader.value = true;
  selectUserVgroupCondi.value.pageNum = parseInt(pageNum);
  if(!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectUserVgroupCondi.value[comboSelected.value] = comboInputData.value;
    else return;
  }
  try {
    const response = await API.comAPI.selectUserVgroup({ ...selectUserVgroupCondi.value }, urlPaths.value);
    selectUserVgroupList.value = response.data.list;
    totalPagesSelectUserVgroup.value = response.data.pages;
    loader.value = false;

    setCondiClear(selectUserVgroupCondiCheck.value, selectUserVgroupCondi.value)
  } catch (error) {
    console.log(error);
  }
}

const selectedList = ref([]);
const selectedData = ref([]);
const selectVgroup = () => {
  let item = [];
  if(selectedData.value.length !== 0) {
    selectedData.value.map((it) => {
      item.push(it);
    })
  }
  else if(checkedListSelectUserVgroup.value.length !== 0) {
    checkedListSelectUserVgroup.value.map((it) => {
      item.push(it);
    })
  }

  props.returnFunc(item);
};

const closePopUp = () => {
  props.toggleFunc();
}

const moveDataDown = () => {
  if(checkedListSelectUserVgroup.value.length !== 0) {
    checkedListSelectUserVgroup.value.map((item) => {
      const isDuplicate = selectedData.value.some((el) => el.vgroupid == item.vgroupid);
      if(isDuplicate === false) {
        selectedData.value.push(item);
      }
    })
    checkedListSelectUserVgroup.value = [];
  }
};

const moveDataUp = () => {
  if(selectedList.value.length !== 0) {
    selectedList.value.forEach(row => {
      const index = selectedData.value.findIndex((el) => {
        if(el.vgroupid == row)
          return true;
      });
      if (index !== -1) {
        selectedData.value.splice(index, 1);
      }
    });
    selectedList.value = [];
  }
};

</script>
