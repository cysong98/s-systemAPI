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
      <h4 class="h4">인계인수</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">인계인수서 작성</h5>
        </div>
      </section>

      <section class="section">
        <div class="row">
          <div class="col">
            <div class="group-container">
              <v-label style="margin-right: 1%;">미완료 문서가 존재할 경우 [전체문서]로 인계인수서를 작성할 수 없습니다.</v-label>
              <v-radio-group v-model="selectedType" hide-details="auto" inline>
                <v-radio v-for="(data, idx) in radioData" :key="idx" :label="data.view" color="indigo" :value="data.key"></v-radio>
              </v-radio-group>
              <v-btn class="magnify-solid" style="margin-left: 2%; margin-right: 2%;" @click="moveToBmsDctreqadd(param)">작성</v-btn>
            </div>
            <v-table class="table-type-03">
              <colgroup>
                <col width="">
                <col width="">
                <col width="">
                <col width="">
              </colgroup>
              <tbody>
                <tr>
                  <th style="padding-left: 0%;">미완료건수</th>
                  <th style="padding-left: 0%;">생산 건수</th>
                  <th style="padding-left: 0%;">접수 건수</th>
                  <th style="padding-left: 0%;">일반 건수</th>
                </tr>
              </tbody>
              <tbody>
                <tr>
                  <td style="padding-left: 10%;"><v-btn class="magnify-solid">0건</v-btn></td>
                  <td style="padding-left: 10%;"><v-btn class="magnify-solid" @click="trnCheckPopupToggleFunc(1)">{{createOtherCount}}건</v-btn></td>
                  <td style="padding-left: 10%;"><v-btn class="magnify-solid" @click="trnCheckPopupToggleFunc(2)">{{receiptCount}}건</v-btn></td>
                  <td style="padding-left: 10%;"><v-btn class="magnify-solid" @click="trnCheckPopupToggleFunc(3)">{{create5LevelCount}}건</v-btn></td>
                </tr>
                <tr></tr>
              </tbody>
              
            </v-table>
            <div style="text-align: right; padding-right: 1%;">
              <v-label>전체 {{totalCount}}건</v-label>
            </div>
          </div>
        </div>

        <div class="box-flex justify-space-between pb-2">
        </div>

        

        <div class="box-sort">
        </div>
      </section>
    </div>
  </div>


  <!-- 인계인수 건수별 팝업(미완료건수, 생산건수, 접수건수, 일반건수) -->
  <v-dialog v-model="bVisibleTrnCheckPopup" >
    <v-card>
      <v-toolbar>
        <v-toolbar-title>{{ toolbarTitle }}</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleTrnCheckPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <TrnCheckPopup
        v-if="bVisibleTrnCheckPopup"
        :args="trnCheckPopupArgs"
        :toggleFunc="trnCheckPopupToggleFunc"
        :returnFunc="trnCheckPopupReturnFunc"
      ></TrnCheckPopup>
    </v-card>
  </v-dialog>

  
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { setQueries, setCondiChanged, setCondiClear } from "@/utils/Query.js";
import _ from 'lodash';
import { API } from '@/api';
import { useLoginStore } from '/src/store/Login';
import TrnCheckPopup from "./TrnCheckPopup.vue";

const name = ref('BmsComTransProcToDoList')
const params = ref({});
const query = ref({});
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')

const param = ref({})

// for column combo
const comboSelected = ref("")
const comboInputData = ref("")
const comboData = ["title", "reqdeptname", "requsername", "apprdeptname", "apprusername"]
// for selectTransProcToDoList
const checkedListSelectTransProcToDoList = ref([])
const selectTransProcToDoListList = ref([])
const selectTransProcToDoListDefCondi = {
  title: "",
  reqdeptname: "",
  requsername: "",
  apprdeptname: "",
  apprusername: "",
  transferid: "",
  status: "",
  apprstatus: "",
  appuserid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
  sortDirection: "ASC",
  sortItem: "transferid"
}
const selectTransProcToDoListCondi = ref({ ...selectTransProcToDoListDefCondi })
const selectTransProcToDoListCondiCheck = ref({ value: { ...selectTransProcToDoListDefCondi }, flag: { ...selectTransProcToDoListDefCondi } })
/* ================ pagination ================ */
const selectTransProcToDoListLoader = ref(true)
const totalPagesSelectTransProcToDoList = ref(0)
const currentPageSelectTransProcToDoList = ref(1)
const pageSizeSelectTransProcToDoList = ref(10)
const pageSizesSelectTransProcToDoList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])

/* ============================================== */

const loginStore = useLoginStore()
const { getUserLoginData } = storeToRefs(loginStore);


onMounted(async () => {
  setCondiClear(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value);
  setQueries(route, selectTransProcToDoListCondi.value, selectTransProcToDoListDefCondi);
  // await selectTransProcToDoList(selectTransProcToDoListCondi.value.pageNum);
  await selectMgmtRegiCnt(1);
  if(responseData){
    param.value.responseData = responseData;
  }
  if(selectedType){
    param.value.selectedType = selectedType;
  }
})

watch(route, async (route) => {
  setQueries(route, selectTransProcToDoListCondi.value, selectTransProcToDoListDefCondi);
})

watch(() => _.cloneDeep(selectTransProcToDoListCondi.value), (newVal, oldVal) => {
  setCondiChanged(selectTransProcToDoListCondiCheck.value, newVal, oldVal);
})

watch(comboInputData, async (comboInputData) => {
  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectTransProcToDoListCondi.value[comboSelected.value] = comboInputData;
  setCondiChanged(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value, selectTransProcToDoListCondi.value);
})

watch(comboSelected, async (comboSelected) => {
    selectTransProcToDoListCondi.value["title"] = "";
    selectTransProcToDoListCondi.value["reqdeptname"] = "";
    selectTransProcToDoListCondi.value["requsername"] = "";
    selectTransProcToDoListCondi.value["apprdeptname"] = "";
    selectTransProcToDoListCondi.value["apprusername"] = "";
  setCondiChanged(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value, selectTransProcToDoListCondi.value);
})


// 구분 라디오 버튼
// const radioData = ['전체문서','일부문서']
const selectedType = ref("all");
const radioData = [
  {view: '전체문서', key:'all'},
  {view: '일부문서', key:'partial'}
]

const selectTransProcToDoList = async (pageNum) => {

  selectTransProcToDoListLoader.value = true;
  selectTransProcToDoListCondi.value.pageNum = parseInt(pageNum);
  selectTransProcToDoListCondi.value.appruserid = getUserLoginData.value.userid;

  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectTransProcToDoListCondi.value[comboSelected.value] = comboInputData.value;
  try {
    const response = await API.trnAPI.selectTransProcToDoList({ ...selectTransProcToDoListCondi.value }, urlPaths.value);
    selectTransProcToDoListList.value = response.data.list;
    totalPagesSelectTransProcToDoList.value = response.data.pages;
    const allOption = pageSizesSelectTransProcToDoList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectTransProcToDoListLoader.value = false;
    setCondiClear(selectTransProcToDoListCondiCheck.value, selectTransProcToDoListCondi.value)
  } catch (error) {
    console.log(error);
  }
}

const totalPagesSelectMgmtRegiList = ref(0)
const selectMgmtRegiListList = ref([])
const selectMgmtRegiListCnt = ref(0)
const pageSizesSelectMgmtRegiList = ref([
  {view: "10개씩 보기", key: 10},
  {view: "25개씩 보기", key: 25},
  {view: "50개씩 보기", key: 50},
])
const selectMgmtRegiListDefCondi = {
  userreqtype: "개인",
  secttl: "",
  authorname: "",
  mgmtno: "",
  mgmtid: "",
  startDt: "",
  endDt: "",
  pageNum: 1,
  pageSize: 10,
}

const selectMgmtRegiListCondi = ref({ ...selectMgmtRegiListDefCondi })
const selectMgmtRegiListCondiCheck = ref({ value: { ...selectMgmtRegiListDefCondi }, flag: { ...selectMgmtRegiListDefCondi } })
const selectMgmtRegiList = async (pageNum) => {

  selectTransProcToDoListLoader.value = true;
  selectMgmtRegiListCondi.value.pageNum = parseInt(pageNum);

  if(selectMgmtRegiListCondi.value.userreqtype == '개인'){
    selectMgmtRegiListCondi.value.authorid = getUserLoginData.value.userid;
  }else if(selectMgmtRegiListCondi.value.userreqtype == '부서'){
    selectMgmtRegiListCondi.value.deptid = getUserLoginData.value.deptid;
  }

  if(!(comboSelected.value == "" || comboSelected.value == undefined))
    selectMgmtRegiListCondi.value[comboSelected.value] = comboInputData.value;
  try {
    const response = await API.dctAPI.selectMgmtRegiList({ ...selectMgmtRegiListCondi.value }, urlPaths.value);
    selectMgmtRegiListList.value = response.data.list;
    selectMgmtRegiListCnt.value = response.data.total;
    totalPagesSelectMgmtRegiList.value = response.data.pages;
    const allOption = pageSizesSelectMgmtRegiList.value.find(option => option.view === 'ALL');
    if (allOption) {
      allOption.key = response.data.total;
    }
    selectTransProcToDoListLoader.value = false;

    setCondiClear(selectMgmtRegiListCondiCheck.value, selectMgmtRegiListCondi.value)
  } catch (error) {
    alert("Server Error")
  }
}

const responseData = ref({});
const createOtherCount = ref(0);
const create5LevelCount = ref(0);
const receiptCount = ref(0);
const totalCount = ref(0);
const selectMgmtRegiCnt = async () => {

  selectTransProcToDoListLoader.value = true;

  try {
  const response = await API.dctAPI.selectMgmtRegiCnt({ 
      authorid: getUserLoginData.value.userid,
    }, 
    urlPaths.value);

  responseData.value = response.data
  createOtherCount.value = response.data.createOtherCount
  create5LevelCount.value = response.data.create5LevelCount
  receiptCount.value = response.data.receiptCount
  totalCount.value = response.data.totalCount;
  selectTransProcToDoListLoader.value = false;
  } catch (error) {
  alert("Server Error")
  }
}

const toolbarTitle = ref('');
/* ======================== 건수 팝업 ======================== */
const bVisibleTrnCheckPopup = ref(false)
const trnCheckPopupReuturn = ref({})
const trnCheckPopupArgs = ref({})
const trnCheckPopupToggleFunc = ref((buttonValue) => {
    
  trnCheckPopupArgs.value = {
    authorid: getUserLoginData.value.userid,
  }

  if (buttonValue === 1) { //생산 건수.
    toolbarTitle.value = '생산 건수';
    trnCheckPopupArgs.value.regirecvgubun = '1' //생산
    trnCheckPopupArgs.value.seclevel = ['2','3','4'];
  } else if (buttonValue === 2){
    toolbarTitle.value = '접수 건수';
    trnCheckPopupArgs.value.regirecvgubun = '2'
    trnCheckPopupArgs.value.seclevel = ['2','3','4'];
  } else if (buttonValue === 3){
    toolbarTitle.value = '일반 건수';
    trnCheckPopupArgs.value.seclevel = ['5']
  } else{
    //미완료용
  }

  // param.value.

  bVisibleTrnCheckPopup.value = !bVisibleTrnCheckPopup.value; 

})
const trnCheckPopupReturnFunc = ref((retValue) => {
  trnCheckPopupToggleFunc.value()
  trnCheckPopupReuturn.value = retValue;
})
/* ============================================================ */

// Move Function (인계인수서 작성 페이지)
const moveToBmsDctreqadd = (param) => {
  router.push({
    name: "BmsDctreqadd",
    query: {
      data: JSON.stringify(param),
    }
  });
};

</script>

<style lang="scss" scoped>
  .group-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-left: 49%;
  }
</style>