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
            <h5 class="h5" style="padding-right: 0px;">직급관리</h5>
            <h5 class="h5" style="padding-left: 0px; padding-right: 0px;">|</h5>
            <h5
              class="h5"
              style="color: #AAAAAA; cursor: pointer;padding-left: 0px;"
              @click="moveToBmsComjobgubunlist()"
            >직위관리</h5>
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
                    @keyup.enter="selectGradeList(1)"
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
            <v-btn class="magnify-solid" @click="selectGradeList(1)">
              <v-icon></v-icon>검색
            </v-btn>
          </div>
  
          <div class="box-flex justify-space-between pb-2">
            <span>전체: {{ totalItem }}개</span>
            <div class="box-flex flex-end">
              <div class="action-buttons">
                <v-btn outlined @click="moveToBmsComgradeinsert()">등록</v-btn>
                <v-btn outlined @click="toggleDeletePopup()">삭제</v-btn>
              </div>
              <v-select
                v-model="pageSizeSelectGradeList"
                :items="pageSizesSelectGradeList"
                item-title="view"
                item-value="key"
                @update:modelValue="handlePageSizeChangeSelectGradeList"
                variant="outlined"
                hide-details="auto"
              ></v-select>
            </div>
          </div>

          <v-data-table
            @click:row="(event, item) => moveToBmsComgradedetail(item.item)"
            v-model="checkedListSelectGradeList"
            :headers="staticColumnsSelectGradeList"
            :items="selectGradeListList"
            :items-per-page="pageSizeSelectGradeList"
            :loading="selectGradeListLoader"
            :item-value="selectGradeListIdentifier"
            :no-data-text="noDataText"
            show-select
            hover
            class="table-type-05"
          >
          <template v-slot:bottom></template>
          </v-data-table>
          <v-pagination
            v-model="currentPageSelectGradeList"
            :length="totalPagesSelectGradeList"
            total-visible="5"
            prev-icon="mdi-menu-left"
            next-icon="mdi-menu-right"
            @click="handlePageChangeSelectGradeList"
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
          :returnFunc="deleteGrade"
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
  
  const name = ref('BmsComGradeList')
  const route = useRoute()
  const router = useRouter()
  const urlPaths = ref('')
  // Pinia
  const loginStore = useLoginStore()
  const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
  const { getUserLogin } = loginStore
  // for column combo
  const comboSelected = ref("gradename")
  const comboInputData = ref("")
  const comboData = [
    { key: "grade", view: "직급코드"},
    { key: "gradename", view: "직급명"},
  ]
  // for selectGradeList
  const checkedListSelectGradeList = ref([])
  const selectGradeListList = ref([])
  const selectGradeListDefCondi = {
    grade: "",
    gradename: "",
    startDt: "",
    endDt: "",
    pageNum: 1,
    pageSize: 10,
  }
  const selectGradeListCondi = ref({ ...selectGradeListDefCondi })
  const selectGradeListCondiCheck = ref({ value: { ...selectGradeListDefCondi }, flag: { ...selectGradeListDefCondi } })

  const maxLengthFunc = () => {
    if(comboSelected.value === "gradename")
      return 99;
    else
      return 34;
  };

  /* ================ pagination ================ */
  const selectGradeListLoader = ref(true);
  const totalPagesSelectGradeList = ref(0)
  const currentPageSelectGradeList = ref(1)
  const pageSizeSelectGradeList = ref(10)
  const pageSizesSelectGradeList = ref([
    {view: "10개씩 보기", key: 10},
    {view: "25개씩 보기", key: 25},
    {view: "50개씩 보기", key: 50},
  ])
  
  const handlePageSizeChangeSelectGradeList = () => {
    selectGradeListCondi.value.pageSize = pageSizeSelectGradeList.value;
    currentPageSelectGradeList.value = 1;
    selectGradeList(currentPageSelectGradeList.value);
  }
  const handlePageChangeSelectGradeList = () => {
    selectGradeList(currentPageSelectGradeList.value);
  }
  /* ============================================== */
  
  
  onMounted(async () => {
    setCondiClear(selectGradeListCondiCheck.value, selectGradeListCondi.value);
    setQueries(route, selectGradeListCondi.value, selectGradeListDefCondi);
    await selectGradeList(selectGradeListCondi.value.pageNum);
  });
  
  watch(route, async (route) => {
    setQueries(route, selectGradeListCondi.value, selectGradeListDefCondi);
    await selectGradeList(selectGradeListCondi.value.pageNum); 
  });
  
  watch(() => _.cloneDeep(selectGradeListCondi.value), (newVal, oldVal) => {
    setCondiChanged(selectGradeListCondiCheck.value, newVal, oldVal);
  });
  
  watch(comboInputData, async (comboInputData) => {
    if(!(comboSelected.value == "" || comboSelected.value == undefined))
      selectGradeListCondi.value[comboSelected.value] = comboInputData;
    setCondiChanged(selectGradeListCondiCheck.value, selectGradeListCondi.value, selectGradeListCondi.value);
  });
  
  watch(comboSelected, async (comboSelected) => {
    selectGradeListCondi.value["grade"] = "";
    selectGradeListCondi.value["gradename"] = "";
    setCondiChanged(selectGradeListCondiCheck.value, selectGradeListCondi.value, selectGradeListCondi.value);
  });


  const staticColumnsSelectGradeList = [
    { title: "직급코드", key: "grade", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직급명", key: "gradename", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직종명", key: "jobtype", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "직류명", key: "jobgubun", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "등록일", key: "indt", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" }
  ];
  
  /* ======================= multi selection ======================= */
  const selectGradeListIdentifier = computed(() => (item) => {
    item.identifier = {
      grade: item.grade,
    };
    return item.identifier;
  });
  
  // 서버에 전송할 체크된 리스트 (row 반환)
  const selectGradeListFinalData = computed(() => {
    // 체크된 항목만 필터링
      const filteredData = selectGradeListList.value.filter((item) =>
      checkedListSelectGradeList.value.includes(item.identifier))
  
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
  const selectGradeList = async (pageNum) => {
    selectGradeListLoader.value = true;
    selectGradeListCondi.value.pageNum = parseInt(pageNum);
  

    if (!(comboSelected.value == "" || comboSelected.value == undefined)) {
      if(validCheckSearchInput(comboInputData.value))
        selectGradeListCondi.value[comboSelected.value] = comboInputData.value;
      else {
        comboInputData.value = "";
        selectGradeListLoader.value = false;
        return;
      }
    }
    try {    
      if(dateFormat.value.startDt != "")
        selectGradeListCondi.value.startDt = dayjs(dateFormat.value.startDt).format('YYYYMMDDHHmmss');
      else 
        selectGradeListCondi.value.startDt = "";
      if(dateFormat.value.endDt != "")
        selectGradeListCondi.value.endDt = dayjs(dateFormat.value.endDt).format('YYYYMMDD') + "235959";
      else
        selectGradeListCondi.value.endDt = "";
          
      if(dateFormat.value.startDt > dateFormat.value.endDt) {
        dateFormat.value.endDt = "";
        selectGradeListCondi.value.endDt = "";
      }

      const response = await API.comAPI.selectGradeList({ ...selectGradeListCondi.value }, urlPaths.value);
      if (response.data.list.length == 0) {
        noDataText.value = "조건에 맞는 데이터가 없습니다.";
        selectGradeListLoader.value = false;
      }    
      
      response.data.list = response.data.list.map((u) => {
        u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
        return { ...u};
      });
  
      selectGradeListList.value = response.data.list;
      totalPagesSelectGradeList.value = response.data.pages;
      selectGradeListLoader.value = false;
      totalItem = response.data.total;
  
      setCondiClear(selectGradeListCondiCheck.value, selectGradeListCondi.value);
      if(selectGradeListCondi.value.startDt != "")
        dateFormat.value.startDt = dayjs(selectGradeListCondi.value.startDt).format('YYYY-MM-DD');
      if(selectGradeListCondi.value.endDt != "") 
        dateFormat.value.endDt = dayjs(selectGradeListCondi.value.endDt).format('YYYY-MM-DD');
    } catch (error) {
      console.log(error);
    }
  }
  
  
  const deleteGrade = async (retValue) => {
    toggleDeletePopup.value();
    if (retValue == false) {
      return;
    }
  
    await API.comAPI
      .deleteGrade(selectGradeListFinalData.value, urlPaths.value)
      .then(response => {
        // if (response.status == 200) {
        //   alert("정상 삭제되었습니다");
        // }
      })
    .catch(error => {
      console.log(error);
    });
    await selectGradeList(selectGradeListCondi.value.pageNum);
    checkedListSelectGradeList.value = [];
  }
  
  
  const visibleBmsComDeletePopup = ref(false);
  const toggleDeletePopup = ref(() => {
    if (checkedListSelectGradeList.value.length < 1) {
      alert("삭제할 항목을 선택해주세요.");
      return;
    }
    visibleBmsComDeletePopup.value = !visibleBmsComDeletePopup.value;
  });
  
  
  // Move Function
  const moveToBmsComgradeinsert = () => {
    router.push({
      name: "BmsComgradeinsert"
    });
  }
  
  // Move Function
  const moveToBmsComgradedetail = (item) => {
    router.push({
      name: "BmsComgradedetail",
      query: {
        ...{grade: item.value.grade},
      }
    });
  }

  // Move Function
  const moveToBmsComjobgubunlist = () => {
    router.push({
      name: "BmsComjobgubunlist"
    });
  };
  
</script>