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
    <div class="page" :style="{width: contentWidth}">
      <div class="box-title">
        <h4 class="h4">조직관리</h4>
      </div>
  
      <div class="box-content">
        <section class="section sticky">
          <div class="box-title">
            <h5
              class="h5"
              style="color: #AAAAAA; cursor: pointer; padding-right: 0px;"
              @click="moveToBmsComgradelist()"
            >직급관리</h5>
            <h5 class="h5" style="padding-left: 0px; padding-right: 0px;">|</h5>
            <h5 class="h5" style="padding-left: 0px;">직위관리</h5>
          </div>
        </section>
  
        <section class="section">
          <div class="box-condition">
            <table>
              <tr>
                <th>검색어</th>
                <td>
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
                    @keyup.enter="selectJobgubunList(1)"
                    v-model="comboInputData"
                    :maxlength="maxLengthFunc()"
                    variant="outlined"
                    hide-details="auto"
                    required
                    clearable
                  />
                </td>
                <th>등록기간</th>
                <td>
                  <v-text-field
                    type="date"
                    v-model="dateFormat.startDt"
                    variant="outlined"
                    hide-details="auto"
                    required
                  />
                  ~
                  <v-text-field
                    type="date"
                    v-model="dateFormat.endDt"
                    :min="dateFormat.startDt"
                    variant="outlined"
                    hide-details="auto"
                    required
                  />
                </td>
                <th></th>
                <td></td>
              </tr>
            </table>
            <v-btn class="magnify-solid" @click="selectJobgubunList(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
  
          <div class="box-flex justify-space-between pb-2">
            <span>전체: {{ totalItem }}개</span>
            <div class="box-flex flex-end">
              <div class="action-buttons">
                <v-btn outlined @click="moveToBmsComjobgubuninsert()">등록</v-btn>
                <v-btn outlined @click="toggleDeletePopup()">삭제</v-btn>
              </div>
              <v-select
                v-model="pageSizeSelectJobgubunList"
                :items="pageSizesSelectJobgubunList"
                item-title="view"
                item-value="key"
                @update:modelValue="handlePageSizeChangeSelectJobgubunList"
                variant="outlined"
                hide-details="auto"
              ></v-select>
            </div>
          </div>

          <v-data-table
            @click:row="(event, item) => moveToBmsComjobgubundetail(item.item)"
            v-model="checkedListSelectJobgubunList"
            :headers="staticColumnsSelectJobgubunList"
            :items="selectJobgubunListList"
            :items-per-page="pageSizeSelectJobgubunList"
            :loading="selectJobgubunListLoader"
            :item-value="selectJobgubunListIdentifier"
            :no-data-text="noDataText"
            show-select
            hover
            class="table-type-05"
          >
          <template v-slot:bottom></template>
          </v-data-table>
          <v-pagination
            v-model="currentPageSelectJobgubunList"
            :length="totalPagesSelectJobgubunList"
            total-visible="5"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            @click="handlePageChangeSelectJobgubunList"
          ></v-pagination>
        </section>
      </div>
    </div>
  
    <v-dialog v-model="visibleBmsComDeletePopup" style="width: 700px;">
      <v-card>
        <v-toolbar>
          <v-toolbar-title>알림</v-toolbar-title>
          <v-btn class="close" icon @click="visibleBmsComDeletePopup = false">
            <v-icon></v-icon>
          </v-btn>
        </v-toolbar>
        <BmsComDeletePopup
          v-if="visibleBmsComDeletePopup"
          :alertMsg="''"
          :returnFunc="deleteJobgubun"
        ></BmsComDeletePopup>
      </v-card>
    </v-dialog>
  </template>
  
  <script setup>
  import console from "console";
  import { useMainStore } from '/src/store/Main';
  const mainStore = useMainStore()
  const { contentWidth } = storeToRefs(mainStore)
  
  import dayjs from 'dayjs';
  import { ref, onMounted, computed, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
  import { validCheckSearchInput } from "@/utils/Utils.js";
  import _ from 'lodash';
  import { API } from '@/api'; 
  import { storeToRefs } from "pinia";
  import { useLoginStore } from '@/store/Login.js';

  import BmsComDeletePopup from "./BmsComDeletePopup.vue";
  
  const name = ref('BmsComJobgubunList')
  const route = useRoute()
  const router = useRouter()
  const urlPaths = ref('')
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for column combo
  const comboSelected = ref("codename")
  const comboInputData = ref("")
  const comboData = [
    { key: "code", view: "직위코드"},
    { key: "codename", view: "직위명"},
  ]
  // for selectJobgubunList
  const checkedListSelectJobgubunList = ref([])
  const selectJobgubunListList = ref([])
  const selectJobgubunListDefCondi = {
    code: "",
    codename: "",
    startDt: "",
    endDt: "",
    pageNum: 1,
    pageSize: 10,
  }
  const selectJobgubunListCondi = ref({ ...selectJobgubunListDefCondi })
  const selectJobgubunListCondiCheck = ref({ value: { ...selectJobgubunListDefCondi }, flag: { ...selectJobgubunListDefCondi } })

  const maxLengthFunc = () => {
    if(comboSelected.value === "codename")
      return 99;
    else
      return 34;
  };

  /* ================ pagination ================ */
  const selectJobgubunListLoader = ref(true);
  const totalPagesSelectJobgubunList = ref(0)
  const currentPageSelectJobgubunList = ref(1)
  const pageSizeSelectJobgubunList = ref(10)
  const pageSizesSelectJobgubunList = ref([
    {view: "10개씩 보기", key: 10},
    {view: "25개씩 보기", key: 25},
    {view: "50개씩 보기", key: 50},
  ])
  
  const handlePageSizeChangeSelectJobgubunList = () => {
    selectJobgubunListCondi.value.pageSize = pageSizeSelectJobgubunList.value;
    currentPageSelectJobgubunList.value = 1;
    selectJobgubunList(currentPageSelectJobgubunList.value);
  }
  const handlePageChangeSelectJobgubunList = () => {
    selectJobgubunList(currentPageSelectJobgubunList.value);
  }
  /* ============================================== */
  
  
  onMounted(async () => {
    setCondiClear(selectJobgubunListCondiCheck.value, selectJobgubunListCondi.value);
    setQueries(route, selectJobgubunListCondi.value, selectJobgubunListDefCondi);
    await selectJobgubunList(selectJobgubunListCondi.value.pageNum);
  });
  
  watch(route, async (route) => {
    setQueries(route, selectJobgubunListCondi.value, selectJobgubunListDefCondi);
    await selectJobgubunList(selectJobgubunListCondi.value.pageNum); 
  });
  
  watch(() => _.cloneDeep(selectJobgubunListCondi.value), (newVal, oldVal) => {
    setCondiChanged(selectJobgubunListCondiCheck.value, newVal, oldVal);
  });
  
  watch(comboInputData, async (comboInputData) => {
    if(!(comboSelected.value == "" || comboSelected.value == undefined))
      selectJobgubunListCondi.value[comboSelected.value] = comboInputData;
    setCondiChanged(selectJobgubunListCondiCheck.value, selectJobgubunListCondi.value, selectJobgubunListCondi.value);
  });
  
  watch(comboSelected, async (comboSelected) => {
      selectJobgubunListCondi.value["code"] = "";
      selectJobgubunListCondi.value["codename"] = "";
    setCondiChanged(selectJobgubunListCondiCheck.value, selectJobgubunListCondi.value, selectJobgubunListCondi.value);
  });


  const staticColumnsSelectJobgubunList = [
    { title: "직류코드", key: "jobgubun", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직위코드", key: "code", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직위명", key: "codename", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "등록일", key: "indt", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
  ];
  
  /* ======================= multi selection ======================= */
  const selectJobgubunListIdentifier = computed(() => (item) => {
    item.identifier = {
      jobgubun: item.jobgubun,
      code: item.code,
    };
    return item.identifier;
  });
  
  // 서버에 전송할 체크된 리스트 (row 반환)
  const selectJobgubunListFinalData = computed(() => {
    // 체크된 항목만 필터링
      const filteredData = selectJobgubunListList.value.filter((item) =>
      checkedListSelectJobgubunList.value.includes(item.identifier))
  
    // identifier 속성을 제거한 복사본을 생성하여 반환
    return filteredData.map((item) => {
      const { identifier, ...newItem } = item;
      return newItem;
    });
  
  });
  /* ============================================================== */
  
  let totalItem = 0;
  const noDataText = ref("");
  const dateFormat = ref({startDt: "", endDt: ""});
  const selectJobgubunList = async (pageNum) => {
    selectJobgubunListLoader.value = true;
    selectJobgubunListCondi.value.pageNum = parseInt(pageNum);
  
    if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
      if(validCheckSearchInput(comboInputData.value))
        selectJobgubunListCondi.value[comboSelected.value] = comboInputData.value;
      else {
        comboInputData.value = "";
        selectJobgubunListLoader.value = false;
        return;
      }
    }
    try {  
      if(dateFormat.value.startDt != "")
        selectJobgubunListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
      else 
        selectJobgubunListCondi.value.startDt = "";
      if(dateFormat.value.endDt != "")
        selectJobgubunListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
      else
        selectJobgubunListCondi.value.endDt = "";
          
      if(dateFormat.value.startDt > dateFormat.value.endDt) {
        dateFormat.value.endDt = "";
        selectJobgubunListCondi.value.endDt = "";
      }

      const response = await API.comAPI.selectJobgubunList({ ...selectJobgubunListCondi.value }, urlPaths.value);
      if (response.data.list.length == 0) {
        noDataText.value = "조건에 맞는 데이터가 없습니다.";
        selectJobgubunListLoader.value = false;
      }    
      
      response.data.list = response.data.list.map((u) => {
        u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
        return { ...u};
      });
  
      selectJobgubunListList.value = response.data.list;
      totalPagesSelectJobgubunList.value = response.data.pages;
      selectJobgubunListLoader.value = false;
      totalItem = response.data.total;
  
      setCondiClear(selectJobgubunListCondiCheck.value, selectJobgubunListCondi.value);
      if(selectJobgubunListCondi.value.startDt != "")
        dateFormat.value.startDt = dayjs(selectJobgubunListCondi.value.startDt).format('YYYY-MM-DD');
      if(selectJobgubunListCondi.value.endDt != "") 
        dateFormat.value.endDt = dayjs(selectJobgubunListCondi.value.endDt).format('YYYY-MM-DD');
    } catch (error) {
      console.log(error);
    }
  }
  
  
  const deleteJobgubun = async (retValue) => {
    toggleDeletePopup.value();
    if (retValue == false) {
      return;
    }
  
    await API.comAPI
      .deleteJobgubun(selectJobgubunListFinalData.value, urlPaths.value)
      .then(response => {
        // if (response.status == 200) {
        //   alert("정상 삭제되었습니다");
        // }
      })
    .catch(error => {
      console.log(error);
    });
    await selectJobgubunList(selectJobgubunListCondi.value.pageNum);
    checkedListSelectJobgubunList.value = [];
  }
  
  
  const visibleBmsComDeletePopup = ref(false);
  const toggleDeletePopup = ref(() => {
    if (checkedListSelectJobgubunList.value.length < 1) {
      alert("삭제할 항목을 선택해주세요.");
      return;
    }
    visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
  });
  
  
  // Move Function
  const moveToBmsComjobgubuninsert = () => {
    router.push({
      name: "BmsComjobgubuninsert"
      });
  }
  
  // Move Function
  const moveToBmsComjobgubundetail = (item) => {
    router.push({
      name: "BmsComjobgubundetail",
      query: {
        ...{jobgubun: item.value.jobgubun, code: item.value.code},
      }
    });
  }
  
  // Move Function
  const moveToBmsComgradelist = () => {
    router.push({
      name: "BmsComgradelist"
    });
  };

  </script>
  