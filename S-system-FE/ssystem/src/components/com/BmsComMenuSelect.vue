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
              v-model="comboSelected"
              :items="comboData"
              item-title="view"
              item-value="key"
              variant="outlined"
              hide-details="auto"
            />
            <v-text-field
              @keyup.enter="selectMenuList(1)"
              v-model="comboInputData"
              :maxlength="maxLengthFunc()"
              variant="outlined"
              hide-details="auto"
              required
              clearable
            />
            <v-btn class="magnify-solid" @click="selectMenuList(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
        </div>
      </div>
      
      <div class="row" style="display: block;">
        <div class="col" style="height: 442px;">
          <v-data-table
            @click:row="(event, item) => selectMenu(item.item.raw)"
            v-model="checkedSelectMenuList"
            :headers="staticColumnsSelectList"
            :items="selectMenuListList"
            :items-per-page="pageSizeSelectMenuList"
            :item-value="selectMenuListIdentifier"
            class="table-type-02"
            hover
          >
            <template v-slot:bottom></template>
          </v-data-table>
        </div>

        <v-pagination
          v-model="currentPageSelectMenuList"
          :length="totalPagesSelectMenuList"
          total-visible="5"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @click="handlePageChangeSelectMenuList"
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
  
  const name = ref('BmsComMenuSelect')
  const props = defineProps({
    toggleFunc: Function,
    returnFunc: Function
  })
  
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')
  // for column combo
  const comboSelected = ref("name")
  const comboInputData = ref("")
  const comboData = [
    { key: "name", view: "메뉴명" },
    { key: "id", view: "메뉴아이디" },
  ]
  // for selectMenuList
  const checkedSelectMenuList = ref([]);
  const selectMenuListList = ref([])
  const selectMenuListDefCondi = {
    id: "",
    name: "",
    type: "a",
    pageNum: 1,
    pageSize: 10,
  }
  const selectMenuListCondi = ref({ ...selectMenuListDefCondi })
  const selectMenuListCondiCheck = ref({ value: { ...selectMenuListDefCondi }, flag: { ...selectMenuListDefCondi } })

  const maxLengthFunc = () => {
    if(comboSelected.value === "name")
      return 256;
    else
      return 35;
  };

  /* ================== pagination ================== */
  const totalPagesSelectMenuList = ref(0)
  const currentPageSelectMenuList = ref(1)
  const pageSizeSelectMenuList = ref(10)
  
  const handlePageChangeSelectMenuList = () => {
    selectMenuList(currentPageSelectMenuList.value);
  }
  /* =============================================== */
  
  
  onMounted(async () => {
    setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value);
    await selectMenuList(selectMenuListCondi.value.pageNum);
  })
  
  watch(route, async (route) => {
    setQueries(route, selectMenuListCondi.value, selectMenuListDefCondi);
    await selectMenuList(selectMenuListCondi.value.pageNum); 
  })
  
  watch(() => _.cloneDeep(selectMenuListCondi.value), (newVal, oldVal) => {
    setCondiChanged(selectMenuListCondiCheck.value, newVal, oldVal);
  })
  
  watch(comboInputData, async (comboInputData) => {
    if(!(comboSelected.value == "" || comboSelected.value == undefined))
      selectMenuListCondi.value[comboSelected.value] = comboInputData;
    setCondiChanged(selectMenuListCondiCheck.value, selectMenuListCondi.value, selectMenuListCondi.value);
  })
  
  watch(comboSelected, async (comboSelected) => {
    comboInputData.value = "";
    selectMenuListCondi.value["id"] = "";
    selectMenuListCondi.value["name"] = "";
    setCondiChanged(selectMenuListCondiCheck.value, selectMenuListCondi.value, selectMenuListCondi.value);
  })
  
  
  const staticColumnsSelectList = [
    { title: "메뉴아이디", key: "id", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "메뉴명", key: "name", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
  ];
  
  /* ======================= multi selection ======================= */
  const selectMenuListIdentifier = computed(() => (item) => {
    item.identifier = {
      id: item.id,
    };
    
    return item.identifier;
  });
  /* =============================================================== */
  
  
  // Return Function
  const selectMenu = (item) => {
    props.returnFunc(item);
  };

  // Toggle Function
  const closePopUp = () => {
    props.toggleFunc();
  };
  
  
  const selectMenuList = async (pageNum) => {
    selectMenuListCondi.value.pageNum = parseInt(pageNum);
    
    if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
    if(validCheckSearchInput(comboInputData.value))
      selectMenuListCondi.value[comboSelected.value] = comboInputData.value;
    else {
      comboInputData.value = "";
      return;
    }
  }
    try {
      const response = await API.comAPI.selectMenuList({ ...selectMenuListCondi.value }, urlPaths.value);
      selectMenuListList.value = response.data.list;
      totalPagesSelectMenuList.value = response.data.pages;
  
      setCondiClear(selectMenuListCondiCheck.value, selectMenuListCondi.value)
    } catch (error) {
      console.log(error);
    }
  }
  
</script>