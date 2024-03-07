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
        <v-tab :value="1">직접입력</v-tab>
        <v-tab :value="2">비밀문서</v-tab>
      </v-tabs>

      <v-window v-if="selectedTab == 1">
        <v-window-item>
          <div class="row">
            <div class="col">
              <v-table class="table-type-03">
                <colgroup>
                  <col width="120px">
                  <col>
                </colgroup>
                <tbody>
                  <tr>
                    <th>관련근거</th>
                    <td>
                      <v-text-field
                        v-model="refName"
                        variant="outlined"
                        hide-details="auto"
                      ></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <th>
                      <!-- <div class="box-flex"> -->
                        첨부파일
                      <!-- </div> -->
                    </th>
                    <td colspan="3">
                      <div class="box-flex">
                        <v-file-input
                          v-model="file"
                          @change="(e) => addFiles(e, idx)"
                          variant="outlined"
                          hide-details="auto"
                          :chips="true"
                          show-size
                        ></v-file-input>
                        <v-btn @click="deleteFile">삭제</v-btn>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </v-table>
            </div>
          </div>
        </v-window-item>
      </v-window>

      <v-window v-if="selectedTab == 2">
        <v-window-item>
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
                  @keyup.enter="searchReference"
                  clearable
                />
                <v-btn class="magnify" @click="searchReference">
                  <v-icon></v-icon>
                </v-btn>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <v-data-table-server
                v-model="checkedListselectReferenceList"
                :headers="staticColumnsSelectReferenceList"
                :items="selectReferenceListList"
                :loading="selectReferenceListLoader"
                item-value="docid"
                select-strategy="single"
                show-select
                :no-data-text="noDataText"
                class="table-type-02 narrow"
                height="200px"
                fixed-header
                >
                <template v-slot:item.docttl="{ item }">
                  <div :class="{ 'text-left': item }">
                    {{ item.raw.docttl }}
                  </div>
                </template>
                <template v-slot:item.state="{ item }">
                  {{ transformState(item.raw.state) }}
                </template>
                <template v-slot:bottom></template>
              </v-data-table-server>
            </div>
          </div>
        </v-window-item>
      </v-window>


      <div class="row center">
        <v-icon size="x-large" color="blue" @click="moveDataDown">mdi-chevron-down-box</v-icon>
        <v-icon size="x-large" color="blue" @click="moveDataUp">mdi-chevron-up-box</v-icon>
      </div>
      
      <div class="row">
        <v-data-table
          v-model="checkedList"
          :headers="selectedHeaders"
          :items="selectedData"
          item-value="docid"
          select-strategy="single"
          show-select
          :no-data-text="noSelectedText"
          class="table-type-02 narrow"
        >                
          <template v-slot:item.docttl="{ item }">
            <div :class="{ 'text-left': item }">{{ item.raw.docttl }}</div>
          </template>
          <template v-slot:item.file="{ item }">
            <div v-if="item.raw.file && item.raw.file.name != undefined">
              <v-icon size="x-small" color="indigo-lighten-2">mdi-paperclip</v-icon>
              {{ item.raw.file.name }}
            </div>
            <div v-else-if="item.raw.sfilename">
              <v-icon size="x-small" color="indigo-lighten-2">mdi-paperclip</v-icon>
              {{ item.raw.sfilename }}
            </div>
          </template>
          <template v-slot:bottom></template>
        </v-data-table>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirmPopUp">확인</v-btn>
    </div>

  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import { setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum } from "@/utils/Query.js";
import _ from 'lodash';
import { transformState} from "@/utils/TransFormLabelDataUtil.js"
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const name = ref('DctReferencePopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
// for selectReferenceList
const checkedListselectReferenceList = ref([])
const selectReferenceListLoader = ref(true)
const selectReferenceListList = ref([]);
const selectReferenceListDefCondi = {
  docid: "",
  docttl: "",
  authorname: "",
  authordeptname: "",
  sortDirection: "ASC",
  sortItem: "orgid"
}
const selectReferenceListCondi = ref({ ...selectReferenceListDefCondi })
const selectReferenceListCondiCheck = ref({ value: { ...selectReferenceListDefCondi }, flag: { ...selectReferenceListDefCondi } })


// tab
const selectedTab = ref(null);

// 직접입력
const refName = ref("");

// search
const comboSelected = ref("docttl")
const comboInputData = ref("")
const comboData = [
  { view: "제목", key: "docttl"},
  { view: "작성자", key: "authorname"},
  { view: "작성부서", key: "authordeptname"},
];

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  if(args.value.length > 0) {
    if (args.value[0].infotitle != '') {
      for (let i = 0; i < args.value.length; i++) {
        const setRdocData = {
          docid: cnt,
          gubunTemp: "직접입력",
          docttl: "",
          file: [],
          flepath: "",
        };
        const selectedRdocData = {
          docid: "",
          gubunTemp: "비밀문서",
          docttl: "",
        };
        const existingData = {
          flepath: "",
          gubunTemp: "",
          infomationid: "",
          infotext: "",
          infotitle: "",
          infotype: "",
          resultid: "",
          sfilename: "",
          docttl: "",
          docid: "",
        };
        if (args.value[i].hasOwnProperty('infomationid')) {
          existingData.flepath = args.value[i].flepath;
          existingData.gubunTemp = args.value[i].gubunTemp;
          existingData.infomationid = args.value[i].infomationid;
          existingData.infotext = args.value[i].infotext;
          existingData.infotitle = args.value[i].infotitle;
          existingData.infotype = args.value[i].infotype;
          existingData.resultid = args.value[i].resultid;
          existingData.sfilename = args.value[i].sfilename;
          existingData.docttl = args.value[i].infotitle;
          existingData.docid = cnt;
          selectedData.value.push(existingData);
          cnt++;
        } else {
          if (args.value[i].gubunTemp == '직접입력') {
            setRdocData.docttl = args.value[i].infotitle;
            setRdocData.file = args.value[i].file;
            selectedData.value.push(setRdocData);
            cnt++;
          } else {
            selectedRdocData.docid = args.value[i].infotext;
            selectedRdocData.docttl = args.value[i].infotitle;
            selectedData.value.push(selectedRdocData);
          }
        }
      }
    }
  }

  await selectReferenceList();
})

watchEffect(() => {
  args.value = props.args
})

/* ======================== 첨부파일 ======================== */
const attachFileData = ref([]);
const file = ref([]);

const addFiles = (e, idx) => {
  const fileSize = e.target.files[0].size;
  if(fileSize == 0){
    alert("크기가 0인 파일은 등록이 불가능합니다.");
    file.value = [];
    attachFileData.value = [];
  }else if(fileSize < 2000000000) {
    attachFileData.value = e.target.files[0];
  }else {
    alert("붙임 파일은 2GB까지 등록 가능합니다.");
    file.value = [];
    attachFileData.value = [];
  }
};
const deleteFile = () => {
  attachFileData.value = [];
  file.value = [];
}
/* ============================================================ */

// 위쪽 테이블
const noDataText = "조회된 데이터가 없습니다.";

const staticColumnsSelectReferenceList = [
  { key: "indt", title: "작성일시", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "docttl", title: "제목", width: "360px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authorname", title: "작성자", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "authordeptname", title: "작성부서", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
  { key: "state", title: "상태", width: "50px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
];

const makeRowData = computed(() => {
  const filteredData = selectReferenceListList.value.filter((item) => 
    item.docid == checkedListselectReferenceList.value)
    return filteredData.map((item) => {
      return item;
  });
});


// 아래쪽 테이블
const noSelectedText = "선택된 데이터가 없습니다."
const checkedList = ref([]);
const selectedData = ref([]);
const selectedHeaders = [
  {key: "gubunTemp", title: "구분", width: "15%"},
  {key: "docttl", title: "제목", width: "50%"},
  {key: "file", title: "첨부파일", width: "35%"},
];

let cnt = 0;
const moveDataDown = () => {
  if(makeRowData.value.length !== 0 && refName.value == ""){
    const newData = {...makeRowData.value[0]};
    const isDuplicate = selectedData.value.some(item => item.docid === newData.docid);
    if (isDuplicate) {
      alert('이미 지정된 문서입니다');
    } else {
      const selectedRdocData = {
        docid: "",
        gubunTemp: "비밀문서",
        docttl: "",
      };
      selectedRdocData.docid = makeRowData.value[0].docid;
      selectedRdocData.docttl = makeRowData.value[0].docttl;
      selectedData.value.push(selectedRdocData);
      checkedListselectReferenceList.value = [];
    }
  }else if(refName.value != ""){
    const setRdocData = {
      docid: cnt,
      gubunTemp: "직접입력",
      docttl: "",
      file: [],
    };
    setRdocData.docttl = refName.value;
    if(attachFileData.value != []){
      setRdocData.file = attachFileData.value;
    }
    selectedData.value.push(setRdocData);
    attachFileData.value = [];
    file.value = [];
    refName.value = "";
    cnt++;
  }else{
    alert("관련근거 문서를 선택 또는 입력해주세요")
  }
};

const makeRowDataUnderTable = computed(() => {
  const filteredData = selectedData.value.filter((item) => 
    item.docid == checkedList.value)
    return filteredData.map((item) => {
      return item;
  });
});

const delRef = ref([]);
const moveDataUp = () => {
  if(checkedList.value != ""){
    if(makeRowDataUnderTable.value.length !== 0){
      const index = selectedData.value.findIndex
        (item => item.docid === checkedList.value[0]);
      if(index !== -1){
        delRef.value.push(selectedData.value[index].infomationid);
        selectedData.value.splice(index,1);
        checkedList.value = "";
      }
    }
  }else{
    alert("삭제하실 문서를 선택해주세요")
  }
};

// rdoc 조회
const selectReferenceList = async () => {
  selectReferenceListCondi.value.userid = getUserLoginData.value.userid;
  try {
    const response = await API.dctAPI.selectReferenceList({...selectReferenceListCondi.value}, urlPaths.value);
    response.data = response.data.map((u) => {
      u = { ...u, indt: dayjs(u.indt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD') };
      return { ...u};
    });
    selectReferenceListList.value = response.data;
    selectReferenceListLoader.value = false;
  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
};

// 검색 기능
const searchReference = () => {
  comboData.forEach(item => {selectReferenceListCondi.value[item.key] = "";});
  if(comboInputData.value !== ""){
    selectReferenceListCondi.value[comboSelected.value] = comboInputData.value;
  }
  selectReferenceList();
}


// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  childReturnValue.value.ref = selectedData.value;
  childReturnValue.value.delRef = delRef.value;
  props.returnFunc(childReturnValue.value);
}


</script>