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
                label=""
                v-model="comboSelected"
                :items="comboData"
                item-title="view"
                item-value="key"
                variant="outlined"
                hide-details="auto"
              />
              <v-text-field
                @keyup.enter="selectGradeList(1)"
                v-model="comboInputData"
                :maxlength="maxLengthFunc()"
                variant="outlined"
                hide-details="auto"
                required
                clearable
              />
              <v-btn class="magnify-solid" @click="selectGradeList(1)">
                <v-icon></v-icon>검색
              </v-btn>
            </div>
          </div>
        </div>
        <div class="row" style="display:block;">
          <div class="col" style="height: 442px;">
            <v-data-table
                @click:row="(event, item) => selectGrade(item.item.raw)"
                v-model="checkedSelectGradeList"
                :headers="staticColumnsSelectList"
                :items="selectGradeListList"
                :items-per-page="pageSizeSelectGradeList"
                hover
                class="table-type-02"
            >
                <template v-slot:bottom></template>
            </v-data-table>
          </div>
          <v-pagination
            v-model="currentPageSelectGradeList"
            :length="totalPagesSelectGradeList"
            total-visible="5"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            @click="handlePageChangeSelectGradeList"
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
  
  import { ref, onMounted, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
  import { validCheckSearchInput } from "@/utils/Utils.js";
  import _ from 'lodash';
  import { API } from '@/api'; 
  
  const name = ref('BmsComGradePopup')
  const props = defineProps({
    args: String,
    toggleFunc: Function,
    returnFunc: Function
  })
  const gubun = ref("");
  
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  // for column combo
  const comboSelected = ref("")
  const comboInputData = ref("")
  const comboData = ref([]);
  const positionCombo = [
    { key: "codename", view: "직위명" },
    { key: "code", view: "직위코드" }
  ];
  const gradeCombo = [
    { key: "gradename", view: "직급명" },
    { key: "grade", view: "직급코드" }
  ];

  // for selectGradeList
  const checkedSelectGradeList = ref([]);
  const selectGradeListList = ref([])
  const selectGradeListDefCondi = ref({});
  const positionDefCondi = {
    code: "",
    codename: "",
    pageNum: 1,
    pageSize: 10,
  };
  const gradeDefCondi = {
    grade: "",
    gradename: "",
    pageNum: 1,
    pageSize: 10,
  };
  const selectGradeListCondi = ref({});
  const selectGradeListCondiCheck = ref({})

  const maxLengthFunc = () => {
    if(comboSelected.value === "grade" || comboSelected.value === "code")
      return 35;
    else
      return 99;
  };

  /* ================== pagination ================== */
  const totalPagesSelectGradeList = ref(0)
  const currentPageSelectGradeList = ref(1)
  const pageSizeSelectGradeList = ref(10)
  
  const handlePageChangeSelectGradeList = () => {
    selectGradeList(currentPageSelectGradeList.value);
  }
  /* =============================================== */
  
    
  onMounted(async () => {
    gubun.value = props.args;
    if(gubun.value == "position") {
        comboData.value = positionCombo;
        selectGradeListDefCondi.value = positionDefCondi;
        staticColumnsSelectList.value = staticColumnsPositionList;
        comboSelected.value = "codename";
    }
    else {
        comboData.value = gradeCombo;
        selectGradeListDefCondi.value = gradeDefCondi;
        staticColumnsSelectList.value = staticColumnsGradeList;
        comboSelected.value = "gradename";
    }
    selectGradeListCondi.value = { ...selectGradeListDefCondi.value };
    selectGradeListCondiCheck.value = { value: { ...selectGradeListDefCondi.value }, flag: { ...selectGradeListDefCondi.value } };

    setCondiClear(selectGradeListCondiCheck.value, selectGradeListCondi.value);
    await selectGradeList(selectGradeListCondi.value.pageNum);
  })
  

  watch(route, async (route) => {
    setQueries(route, selectGradeListCondi.value, selectGradeListDefCondi);
    await selectGradeList(selectGradeListCondi.value.pageNum); 
  })
  
  watch(() => _.cloneDeep(selectGradeListCondi.value), (newVal, oldVal) => {
    setCondiChanged(selectGradeListCondiCheck.value, newVal, oldVal);
  })
  
  watch(comboInputData, async (comboInputData) => {
    if(!(comboSelected.value == "" || comboSelected.value == undefined))
      selectGradeListCondi.value[comboSelected.value] = comboInputData;
    setCondiChanged(selectGradeListCondiCheck.value, selectGradeListCondi.value, selectGradeListCondi.value);
  })
  
  watch(comboSelected, async (comboSelected) => {
      selectGradeListCondi.value[comboData.value[0].key] = "";
      selectGradeListCondi.value[comboData.value[1].key] = "";
    setCondiChanged(selectGradeListCondiCheck.value, selectGradeListCondi.value, selectGradeListCondi.value);
  })
    
  
  const staticColumnsSelectList = ref([]);
  const staticColumnsPositionList = [
    { title: "직위코드", key: "code", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직위명", key: "codename", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
  ];
  const staticColumnsGradeList = [
    { title: "직급코드", key: "grade", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직급명", key: "gradename", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
  ];
    
  
  // Return Function 
  const selectGrade = (item) => {
    props.returnFunc(item);
  };
  
  // Popup Function
  const closePopUp = () => {
    props.toggleFunc();
  };
  
  
  const selectGradeList = async (pageNum) => {
  
    selectGradeListCondi.value.pageNum = parseInt(pageNum);
  
    if(!(comboSelected.value == "" || comboSelected.value == undefined)) {
      if(validCheckSearchInput(comboInputData.value))
        selectGradeListCondi.value[comboSelected.value] = comboInputData.value;
      else return;
    }
    try {
      var response = null;
      if(gubun.value == "position"){
        response = await API.comAPI.selectJobgubunList({ ...selectGradeListCondi.value }, urlPaths.value);
    }
      else{
        response = await API.comAPI.selectGradeList({ ...selectGradeListCondi.value }, urlPaths.value);
    }
      selectGradeListList.value = response.data.list;
      totalPagesSelectGradeList.value = response.data.pages;
  
      setCondiClear(selectGradeListCondiCheck.value, selectGradeListCondi.value)
    } catch (error) {
      console.log(error);
    }
  }
  
  </script>