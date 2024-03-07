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
      <div class="row1">
        <div class="col">
          <h3 class="h3">송증 정보</h3>
          <!-- {{selectReceiptData}} -->
          <!-- {{props.args}} -->
          <!-- {{args}} -->
          <!-- {{selectReceiptData}} -->
          <v-table class="table-type-03">
            <colgroup>
              <col width="60">
              <col width="60">
              <col width="240">
              <col width="80">
              <col width="180">
            </colgroup>
            <tbody>
              <tr>
                <th colspan="2">발신일자</th>
                <td>{{ transformDate(selectReceiptData.snddt) }}</td>
                <th>비밀등급</th>
                <td>{{ transformSeclevel(selectReceiptData.seclevel) }}</td>
              </tr>    
              <tr>
                <th colspan="2">비밀제목</th>
                <td colspan="3">{{ selectReceiptData.invrcpttl }}</td>
              </tr>           
              <tr>
                <th colspan="2">관리번호</th>
                <td>{{ selectReceiptData.mgmtno }}</td>
                <th>문서번호</th>
                <td>{{ selectReceiptData.docno }}</td>
              </tr>
              <tr>
                <th colspan="2">사본번호</th>
                <td>{{ selectReceiptData.copyno }}</td>
                <th>붙임파일 건수</th>
                <td>{{ selectReceiptData.qty }}</td>
              </tr>
              <tr>
                <th colspan="2">수신</th>
                <td>{{ selectReceiptData.rcvmininame }}</td>
                <th>참조(참조부서명)</th>
                <td>{{ selectReceiptData.refdeptname }}</td>
              </tr>
              <tr>
                <th rowspan="2">발송자</th>
                <th>소속</th>
                <td>{{ selectReceiptData.snddeptname }}</td>
                <th>직위</th>
                <td>{{ selectReceiptData.snddutyname }}</td>
              </tr>
              <tr>
                <th>성명</th>
                <td>{{ selectReceiptData.sndname }}</td>
                <th>서명</th>
                <td class="signature">{{ selectReceiptData.sndname }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="row2">
        <div class="col">
          <h3 class="h3">영수증 정보</h3>
          <!-- {{selectedData}}
          {{attachFileData}} -->
          <v-table class="table-type-03">
            <colgroup>
              <col width="60">
              <col width="60">
              <col width="240">
              <col width="80">
              <col width="180">
            </colgroup>
            <tbody>
              <tr>
                <th colspan="2">접수일자</th>
                <td>{{ transformDate(selectReceiptData.rcvdt) }}</td>
                <th>비밀등급</th>
                <td>{{ transformSeclevel(selectReceiptData.seclevel) }}</td>
              </tr>
              <tr>
                <th colspan="2">비밀제목</th>
                <td colspan="3">{{ selectReceiptData.invrcpttl }}</td>
              </tr>
              <tr>
                <th colspan="2">사본번호</th>
                <td>{{ selectReceiptData.copyno }}</td>
                <th>붙임파일 건수</th>
                <td>{{ selectReceiptData.qty }}</td>
              </tr>
              <tr>
                <th colspan="2">수신</th>
                <td>{{ selectReceiptData.rcvmininame }}</td>
                <th>참조(참조부서명)</th>
                <td>{{ selectReceiptData.refdeptname }}</td>
              </tr>
              <tr>
                <th rowspan="2">수신자</th>
                <th>소속</th>
                <td>{{ selectReceiptData.acceptdeptname }}</td>
                <th>직위</th>
                <td v-if = "args.fromPage !='BmsDctDistributeCardPopup'">{{ selectReceiptData.acceptdutyname }}</td>
                <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='1'">{{ selectReceiptData.acceptdutyname }}</td>
                <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'">
                  <v-text-field
                    v-model="selectReceiptData.acceptdutyname"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>성명</th>
                <td v-if = "args.fromPage !='BmsDctDistributeCardPopup'">{{ selectReceiptData.acceptname }}</td>
                <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='1'">{{ selectReceiptData.acceptname }}</td>
                <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'">
                  <v-text-field
                    v-model="selectReceiptData.acceptname"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td>
                <th>서명</th>
                <td class="signature">{{ selectReceiptData.acceptname }}</td>
                <!-- <td v-if = "args.fromPage !='BmsDctDistributeCardPopup'" class="signature">{{ selectReceiptData.acceptname }}</td> -->
                <!-- <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='1'" class="signature">{{ selectReceiptData.acceptname }}</td> -->
                <!-- <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'">
                  <v-text-field
                    v-model="selectReceiptData.acceptname"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td> -->
              </tr>
              <tr>
                <th v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'" colspan="2">접수증 사진(첨부파일)</th>
                <td v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'" colspan="3">
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
              <!-- <tr>
                <th>접수증 파일</th>
                <td>
                  <div v-for="refData, idx in updateCardData.refList" :key="idx">
                    <div class="box-flex">
                      <span>{{ refData.gubunTemp }}</span>
                      <v-text-field
                        v-model="refData.infotitle"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto">
                      </v-text-field>
                    </div>
                    <div v-if="refData.sfilename != undefined && refData.sfilename != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.sfilename)).color">{{ getFileIcon(getExtension(refData.sfilename)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.sfilename }}</span>
                        </li>
                      </ul>
                    </div>
                    <div v-else-if="refData.file != undefined && refData.file != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.file.name)).color">{{ getFileIcon(getExtension(refData.file.name)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.file.name }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </td>
              </tr> -->
              <tr>
                <th v-if = "getReceiptData && getReceiptData.length > 0" colspan="2">접수증 사진</th>
                <!-- <td v-if = "getReceiptData && getReceiptData.length > 0" colspan="2">{{getReceiptData[0].sfilename}}</td> -->
                <td v-if = "getReceiptData && getReceiptData.length > 0" colspan="2">
                  <!-- {{getReceiptData[0].sfilename}} -->
                  <div class="box-flex" v-if="getReceiptData && getReceiptData.length > 0">
                    <v-icon :color="getFileIcon(getExtension(getReceiptData[0].sfilename)).color">{{ getFileIcon(getExtension(getReceiptData[0].sfilename)).icon }}</v-icon>
                    <span @click="toggleViewerPopup(getReceiptData[0], getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ getReceiptData[0].sfilename }}</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>
  
    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn v-if = "args.fromPage !='BmsDctDistributeCardPopup'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="printReceipt">인쇄
      </v-btn>
      <v-btn v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='1'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="printReceipt">인쇄
      </v-btn>
      <v-btn v-if = "args.fromPage =='BmsDctDistributeCardPopup' && args.actionType =='2'"
        variant="flat" color="indigo-darken-3" rounded="xl" @click="insertRecvInvrcpt">저장
      </v-btn>
    </div>
  </div>

    <!-- 문서 조회 팝업 -->
    <v-dialog v-model="visibleViewerPopup">
      <v-card>
        <div v-if="docloading" class="overlay_02">
          <div class="spinner_02"></div>
          <div class="progress_02"></div>
        </div>
          <v-toolbar>
            <v-toolbar-title>문서 조회</v-toolbar-title>
            <v-btn class="close" icon @click="visibleViewerPopup = false">
              <v-icon></v-icon>
            </v-btn>
          </v-toolbar>
          <iframe :src="url" width="1200" height="840" style="padding: 10px;"></iframe>
      </v-card>
    </v-dialog>
  <!-- {{"args.docid >>>>>>>> : "}} -->
  <!-- {{args.docid}} -->
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getFileIcon, viewDocument, getExtension, url } from "@/utils/Utils.js";
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from "@/api";
import { transformStatus,transformState, transformAsktype, transformDate, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js"
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const name = ref('BmsDctReceipt')
const props = defineProps({
  args: Object,
  // args: String,
  toggleFunc: Function,
  returnFunc: Function,
});
const args = ref({});
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for selectReceipt
const selectReceiptData = ref({})
const getReceiptData = ref([])
const selectReceiptDefCondi = {
   docid: "",
   copyno: ""
  }
const selectReceiptCondi = ref({ ...selectReceiptDefCondi })

/* 현재 시간 가져오는 함수 관련 */
const setCurrentTime = (args) => {
  console.log('args')
  console.log(args.value.fromPage)
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;
  const date = now.getDate();
  const hours = now.getHours();
  const minutes = now.getMinutes();
  const formattedMonth = month < 10 ? `0${month}` : month.toString();
  const formattedDate = date < 10 ? `0${date}` : date.toString();
  const formattedHours = hours < 10 ? `0${hours}` : hours.toString();
  const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes.toString();
  const formattedTime = `${year}${formattedMonth}${formattedDate}${formattedHours}${formattedMinutes}`;

  if(args.value.fromPage && args.value.fromPage =='BmsDctDistributeCardPopup'){
    selectReceiptData.value.snddt = formattedTime;
  }
}

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args;
  selectReceiptCondi.value.docid = args.value.docid;
  selectReceiptCondi.value = args.value;
  // console.log("selectReceiptCondi.value >>> ");
  // console.log(selectReceiptCondi.value);
  await selectReceipt(selectReceiptCondi.value);
  setCurrentTime(args);
})

watchEffect(() => {
  // args.value = props.args;
});

const selectReceipt = async (args) => {
  try {
    if(args.fromPage && args.fromPage =='BmsDctDistributeCardPopup'){
      selectReceiptData.value = args;
      selectReceiptData.value.seclevel = args.seclevel;
      // selectReceiptData.value.snddt
      selectReceiptData.value.invrcpttl = args.docttl;
      selectReceiptData.value.mgmtno = args.mgmtno;
      // selectReceiptData.value.docno = args. //문서번호 {부서명}-S{시퀀스}
      selectReceiptData.value.docno = args.authordeptname + 'S-' + '123'; //수정해야하는 부분
      selectReceiptData.value.qty = args.addsendcnt;
      selectReceiptData.value.rcvmininame = args.recvdeptname;
      // selectReceiptData.value.refdeptname = '0';//참조부서명
      selectReceiptData.value.snddeptname = args.authordeptname;
      selectReceiptData.value.snddutyname = args.authordutyname;
      selectReceiptData.value.sndname = args.authorname;

      selectReceiptData.value.acceptdeptname = args.recvdeptname;
      
    }else{
      //접수증 정보
      const response = await API.dctAPI.selectReceipt(selectReceiptCondi.value, urlPaths.value);
      selectReceiptData.value = response.data; 
      
      //접수증 이미지파일 정보
      const getCommonDctFile = await API.dctAPI.selectCommonDctFile(
      {
        docid : args.docid,
        gubun : 'DFT23',
      }  
      , urlPaths.value);
      getReceiptData.value = getCommonDctFile.data;
      // console.log('getReceiptData >>>', getReceiptData.value)
    }

  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
}

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
};


const printReceipt = () => {
  
  // .content 요소의 HTML을 가져옴.
  const contentHtml1 = document.querySelector('.modal-body .content .row1').outerHTML;
  const contentHtml2 = document.querySelector('.modal-body .content .row2').outerHTML;

  const width = 1000;
  const height = 700;

  const left = (window.screen.width / 2) - (width / 2);
  const top = (window.screen.height / 2) - (height / 2);

  const printWindow = window.open('', '_blank', `width=${width},height=${height},top=${top},left=${left}`);

  // 인쇄할 스타일 정의.
  const printStyles = `
    <style>
      .v-toolbar {
        position: sticky;
        z-index: 2;
        top: 0;
        .v-toolbar__content {
          height: 70px !important;
          border-bottom: 2px solid #35447F;
          background: #fff;
          .v-toolbar-title {
            font-size: 20px;
            font-weight: 500;
            margin: 0 40px;
          }
        }
      }

      .table-type-03.v-table {
        border-radius: 0;
        .v-table__wrapper {
          border-top: 1px solid #d3d7e3;
          border-bottom: 1px solid #d3d7e3;
          table {
            tbody {
              tr {
                th {
                  vertical-align: middle;
                  word-break: keep-all;
                  background: #e3ecfa;
                  text-align: center;
                }
                td {
                  height: 50px;
                  padding-top: 3px;
                  padding-bottom: 3px;
                  border-color: #000;
                  text-align: left;
                  vertical-align: middle;
                  color: rgba(0, 0, 0, 0.7);
                  .link {
                    color: rgba(0, 0, 0, 0.7);
                    &:hover {
                      color: #000;
                      text-decoration: underline
                    }
                  }
                  .ellipsis{
                    @include ellipsis(1);
                  }
                }
              }
            }
          }
        }
      }

      .table-type-03.v-table {
        border-radius: 0;
        border: 1px solid #000; /* 외곽선 */
      }
      .v-table__wrapper {
        border-top: none; /* 상단 보더 제거 */
        border-bottom: none;
      }
      .v-table__wrapper table {
        width: 100%;
        border-collapse: collapse;
      }
      .v-table__wrapper table th,
      .v-table__wrapper table td {
        padding: 10px;
        border: 1px solid #000;
        vertical-align: middle;
        word-break: keep-all;
        text-align: center;
      }
      .v-table__wrapper table tbody tr td {
        text-align: left;
        color: rgba(0, 0, 0, 0.7);
      }
      .v-toolbar-title {
        font-size: 20px;
        font-weight: 600;
        margin: 0 40px;
      }
      .full-width-line { /* 절취선 */
        width: 100%;
        margin-top: 20px;
        margin-bottom: 20px;
        border-top: 1px dashed #000; /* 점선 스타일 */
      }      
    </style>
  `;

  // 새 창에 HTML 구조 작성.
  printWindow.document.write(`
    <html>
      <head>
        <title>송증/영수증 인쇄</title>
        ${printStyles}
      </head>
      <body>
        <div class="v-toolbar">
          <div class="v-toolbar__content">
            <div class="v-toolbar-title">송증/영수증</div>
          </div>
        </div>
        ${contentHtml1}
        <br/>
        <div class="full-width-line"></div>
        ${contentHtml2}
      </body>
    </html>
  `);

  // 문서 로딩을 기다린 후 인쇄.
  printWindow.document.close();
  printWindow.onload = () => {
    printWindow.focus();
    printWindow.print();
    printWindow.close();
  };
};

const insertRecvInvrcpt = async () => {
  selectReceiptData.value.docstatus = 'EDS05' //접수완료

  selectReceiptData.value.authorid = getUserLoginData.value.userid;
  selectReceiptData.value.authorname = getUserLoginData.value.username;
  selectReceiptData.value.authordutyname = getUserLoginData.value.grade;
  selectReceiptData.value.authordeptid = getUserLoginData.value.deptid;
  selectReceiptData.value.authordeptname = getUserLoginData.value.deptname;

  selectReceiptData.value.deptid = getUserLoginData.value.deptid;
  selectReceiptData.value.deptname = getUserLoginData.value.deptname;
  selectReceiptData.value.dutyname = getUserLoginData.value.grade;
  selectReceiptData.value.username = getUserLoginData.value.username;

  selectReceiptData.value.resuserid = getUserLoginData.value.userid;

  var insertReceiptFormData = new FormData();

  // 파일 데이터 추가
  attachFileData.value.forEach((file) => {
    if (file) { // file 객체 확인
      insertReceiptFormData.append('refAttach', file);
    }
  });
  
  const textData = { ...selectReceiptData.value };
  insertReceiptFormData.append(
    "reqInsertRecvInvrcpt",
    new Blob([JSON.stringify(textData)], {
      type: "application/json",
    })
  );

  API.dctAPI.insertRecvInvrcpt(insertReceiptFormData, urlPaths.value).then((response) => {
    if (response.status == 200) {
      confirmPopUp();
    }
  })
  .catch(error => {
    console.log(error);
    alert("Sever_Error");
  });
}

const confirmPopUp = () => {
  const confirmItem  = ref({});
  alert("정상 갱신되었습니다");
  confirmItem.value.state = 'DRDS8';
  props.returnFunc(confirmItem);
}

/* ======================== 첨부파일 추가 ======================== */
const file = ref([]);
const refName = ref("");
const setRdocData = {
  gubunTemp: "접수증",
  docttl: "",
  file: [],
};

file.value = [];
const selectedData = ref([]);
const attachFileData = ref([]);

const addFiles = (e, idx) => {
  const fileSize = e.target.files[0].size;
  if(fileSize == 0){
    alert("크기가 0인 파일은 등록이 불가능합니다.");
    file.value = [];
    attachFileData.value = [];
  }else if(fileSize < 2000000000) {
    attachFileData.value = [e.target.files[0]];
    filePush(); //파일객체 푸쉬.
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


const isReset = ref("N");
const filePush = () => {
  if(attachFileData.value.length > 0){
    setRdocData.file = [...attachFileData.value];
    setRdocData.docttl = selectReceiptData.value.invrcpttl;
  }
  selectedData.value.push(setRdocData);

  if(isReset.value === 'Y'){
    attachFileData.value = [];
    file.value = [];
  }
}
/* ============================================================ */




/* ======================== 첨부파일 조회 ======================== */
// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  docloading.value = true;
  visibleViewerPopup.value = !visibleViewerPopup.value;
  await viewDocument(file, userid, 'N,N', 'Y');
  document.querySelector('iframe').addEventListener('load', function() {
    docloading.value = false;
  });
}
/* ============================================================ */


</script>
