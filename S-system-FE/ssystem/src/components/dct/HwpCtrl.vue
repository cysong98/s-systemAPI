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
  <!-- <div class="box-content"> -->
  <!-- <section class="section">
      <div class="box-title">
        <h5 class="h5">기안문작성</h5>
        <div class="action-buttons">
          <v-btn class="button" @click="draftToggle()">비밀카드</v-btn>
          <v-btn class="button" @click="clearFile()">초기화</v-btn>
        </div>
      </div>
    </section> -->
  <div v-if="props.view.data != 'Y' && props.received != true"
    style="text-align: right; margin-bottom: 10px; margin-right: 1.7%;">
    <div class="action-buttons2">
      <v-btn class="button" @click="draftToggle('N')">비밀카드</v-btn>
      <v-btn class="button" @click="standardDraft()">표준기안</v-btn>
      <v-btn class="button" @click="dctFormPopupToggleFunc()">서식변경</v-btn>
    </div>
  </div>

  <div v-if="props.view.data != 'Y' && props.received != true" id="hwpctrl" style="width:99.6%; height:87%;">
    <!-- ADD IFRAME -->
  </div>
  <div v-else id="hwpctrl" style="width:99.6%; height:92%;">
    <!-- ADD IFRAME -->
  </div>

  <div id="temp"></div>

  <!-- </div> -->
  <div v-if="isloading" class="overlay">
    <div class="spinner"></div>
  </div>

  <!-- 서식변경 팝업 -->
  <v-dialog v-model="bVisibleDctFormPopup" style="width: 650px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>서식변경</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctFormPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctFormPopup v-if="bVisibleDctFormPopup" :args="dctFormPopupArgs" :toggleFunc="dctFormPopupToggleFunc"
        :returnFunc="dctFormPopupReturnFunc"></DctFormPopup>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";

import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
  isReactive,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { API } from "@/api";
import { getValueByKey } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { transformStatus, transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js";
import DctFormPopup from "./DctFormPopup.vue";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore)

const isloading = ref(false);

const params = ref({});
const query = ref({});

const route = useRoute();
const router = useRouter();
const urlPaths = ref("");

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

const props = defineProps({
  data: Object,
  toggle: String,
  view: String,
  received: Boolean
});

// 웹한글기안기 객체
var hwpCtrl = {};

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
});

onMounted(async () => {
  window.sessionStorage.setItem('changeFlag', 'N');
  createHwpCtrl();
});

const createHwpCtrl = () => {
  // console.log(">>>>>>>>>>>>>>>>>>>>>>>>>createHwpCtrl");
  // 스피너 true
  if (props.data.saveLoading != true) {
    isloading.value = true;
  }

  try {
    // location.href 가져와서 url 생성
    let href = location.href;
    let url = "";
    if ((href.match(/:/g) || []).length > 1) {
      url = href.split(':')[0] + ':' + href.split(':')[1] + ':' + href.split(':')[2].split('/')[0] + '/webhwpctrl/';
    } else {
      url = href.split(':')[0] + '://' + href.split(':')[1].split('/')[2] + '/webhwpctrl/';
    }

    // 웹한글기안기 객체 생성
    hwpCtrl = BuildWebHwpCtrl("hwpctrl", /*"http://localhost:8080/webhwpctrl/"*/url, function () { fileOpen(props.data); });
  } catch (error) {
    // 스피너 false
    isloading.value = false;
    alert("웹한글기안기 객체 생성 오류");
    props.toggle.data = "N";
  }
}

watchEffect(() => { });

//비밀카드 버튼 클릭시
const draftToggle = (toggle, apprSave) => {
  saveFile(props.data.docttl, toggle, apprSave);
}


//File Open Function
const onChangeByOpen = (e) => {
  var maintextfile = e.target.files[0];
  hwpCtrl.Open(maintextfile, "", "lock:false", function () {
    hwpCtrl.MoveToField(maintextfile.name, true, true, true);
  });
}

const fileOpen = (data) => {
  try {
    if (data.drafturl == "") { // 초기세팅, 나중에 기본 서식으로 변경필요
      // hwpCtrl.Open("https://172.18.18.25:8080/webhwpctrl/temp/standard.hwp", "HWP", "lock:false", function () {
      hwpCtrl.Open("https://hwp.s-system.kinac:8080/webhwpctrl/temp/standard.hwp", "HWP", "lock:false", function () {
        hwpCtrl.EditMode = 2; // 양식모드
        // editableField();

        hwpCtrl.MoveToField("본문", true, true, true);
        // syncData(data);
        saveFile(data.docttl, "Y");

        // 스피너 false
        isloading.value = false;

        // 저장로딩 false
        data.saveLoading = false;
        // console.log("파일open1");
      });
    } else if (data.drafturl.includes("/public/fileUpload")) {
      hwpCtrl.Open(data.drafturl, "HWP", "lock:false", function () {
        hwpCtrl.EditMode = 2; // 양식모드
        // editableField();
        if (props.view.data == "Y" || props.received == true) {
          hwpCtrl.EditMode = 0;
          hwpCtrl.ShowToolBar(false);
          hwpCtrl.ShowStatusBar(false);
          hwpCtrl.ShowRibbon(false);
        }
        // else {
        //   if (data.newFileList.length > 0) {
        //     syncData(data);
        //   }
        // }

        hwpCtrl.MoveToField("본문", true, true, true);
        // syncData(data);

        // 변경여부 초기화
        hwpCtrl.IsModified = false;
        saveFile(data.docttl, "Y");

        // 스피너 false
        isloading.value = false;

        // 저장로딩 false
        data.saveLoading = false;
        // console.log("파일open2");
      });
    } else {
      hwpCtrl.Open(data.drafturl, "HWP", "lock:false", function () {
        hwpCtrl.EditMode = 2; // 양식모드
        // editableField();

        if (props.view.data == "Y" || props.received == true) {
          hwpCtrl.EditMode = 0;
          hwpCtrl.ShowToolBar(false);
          hwpCtrl.ShowStatusBar(false);
          hwpCtrl.ShowRibbon(false);
        }

        hwpCtrl.MoveToField("본문", true, true, true);
        // syncData(data);

        saveFile(data.docttl, "Y");

        // 스피너 false
        isloading.value = false;

        // 저장로딩 false
        data.saveLoading = false;
        // console.log("파일open3");
      });
    }
  } catch (error) {
    // 스피너 false
    isloading.value = false;
    alert("Server Error");
  }
};

//Clear
const clearFile = () => {
  const confirmClear = confirm("초기화하시겠습니까?");
  if (confirmClear == false) {
    return;
  }
  // hwpCtrl.Clear(1);
  // props.data.drafturl = "";
  // props.data.draftdocttl = "";

}

//SaveAs
const apprFlag = ref("N");
const saveFile = async (title, toggle, apprSave) => {
  // 저장로딩 true
  if(apprSave != "Y") {
    props.data.saveLoading = true;
  }
  
  // console.log("hwpCtrl.IsModified1: ", hwpCtrl.IsModified);
  // 데이터 동기화
  // 접수문서가 아닌 경우에만 데이터 동기화 진행
  if (props.received == false) {
    syncData(props.data);
  } else {
    // 관리번호
    if (props.data.hasOwnProperty('mgmtno')) {
      if (props.data.mgmtno != '' && props.data.mgmtno != undefined) {
        hwpCtrl.PutFieldText('관리번호', props.data.mgmtno);
        // console.log("관리번호 넣음");
      }
    }

    // 사본번호
    if (props.data.hasOwnProperty('copyno')) {
      if (props.data.copyno != '' && props.data.copyno != undefined) {
        let copyno = "";
        // console.log("copyno: ", props.data.copyno);
        let copyArr = props.data.copyno.split('/');
        if (copyArr[0].length == 1) {
          copyno = ' ' + copyArr[0] + '    ' + copyArr[1];
        } else {
          copyno = copyArr[0] + '    ' + copyArr[1];
        }
        hwpCtrl.PutFieldText('사본번호', copyno);
        // console.log("사본번호 넣음");
      }
    }

    hwpCtrl.IsModified = false;
  }

  // console.log("hwpCtrl.IsModified2: ", hwpCtrl.IsModified);
  // 변경 안된 경우엔 저장 안함
  if (hwpCtrl.IsModified == true) {
    // console.log("IsModified");

    title = title + ".hwp";
    await new Promise((resolve) => {
      hwpCtrl.SaveAs(title, "Hwp", "", (response) => {
        // console.log("Save");
        //서버로 downloadUrl 넘겨서 파일 객체 만들고 bms_dct_file에 저장하는 방식으로 가야함
        props.data.drafturl = response.downloadUrl;
        // props.toggle.data = "N";
        props.toggle.data = toggle;

        // 제목 등 내용 전달
        props.data.docttl = hwpCtrl.GetFieldText('결재제목');
        props.data.draftdocttl = hwpCtrl.GetFieldText('결재제목') + ".hwp";
        props.data.draftcontent = hwpCtrl.GetFieldText('본문');


        if (apprFlag.value == "N") {
          // 문서 버전 증가
          if (props.data.hasOwnProperty("pathList")) {
            for (let i = 0; i < props.data.pathList.length; i++) {
              if (props.data.pathList[i].objid == getUserLoginData.value.userid) {
                props.data.pathList[i].mainver = i == 0 ? 1 : props.data.pathList[i - 1].mainver + 1;
                props.data.mainver = props.data.pathList[i].mainver;
              }
            }
          }
        } else {
          if(window.sessionStorage.getItem('changeFlag') == "N") {
            // 문서 버전 유지
            if (props.data.hasOwnProperty("pathList")) {
              for (let i = 0; i < props.data.pathList.length; i++) {
                if (props.data.pathList[i].objid == getUserLoginData.value.userid) {
                  props.data.pathList[i].mainver = i == 0 ? 1 : props.data.pathList[i - 1].mainver;
                  props.data.mainver = props.data.pathList[i].mainver;
                }
              }
            }
          } else {
           // 문서 버전 증가
            if (props.data.hasOwnProperty("pathList")) {
              for (let i = 0; i < props.data.pathList.length; i++) {
                if (props.data.pathList[i].objid == getUserLoginData.value.userid) {
                  props.data.pathList[i].mainver = i == 0 ? 1 : props.data.pathList[i - 1].mainver + 1;
                  props.data.mainver = props.data.pathList[i].mainver;
                }
              }
            } 
          }
        }

        // 저장로딩 false
        props.data.saveLoading = false;
        if (apprSave == "Y") {
          window.sessionStorage.setItem('waitFlag', 'Y');
        }

        resolve();
      });
    })
  } else {
    // console.log("IsNotModified");
    props.toggle.data = toggle;

    // 변경 안된 경우엔 이전 버전 따라감
    if (props.data.hasOwnProperty("pathList")) {
      for (let i = 0; i < props.data.pathList.length; i++) {
        if (props.data.pathList[i].objid == getUserLoginData.value.userid) {
          props.data.pathList[i].mainver = i == 0 ? 1 : props.data.pathList[i - 1].mainver;

          if (props.data.hasOwnProperty("mainver")) {
            if (i != 0 && props.data.mainver != props.data.pathList[i - 1].mainver) {
              props.data.mainver = props.data.mainver;
            } else {
              props.data.mainver = props.data.pathList[i].mainver;
            }
          } else {
            props.data.mainver = props.data.pathList[i].mainver;
          }
        }
      }
    }

    // 저장로딩 false
    props.data.saveLoading = false;
  }
}

// 데이터 동기화
const syncData = (data) => {
  try {
    // 관리번호
    if (data.hasOwnProperty('mgmtno')) {
      if (data.mgmtno != '' && data.mgmtno != undefined) {
        hwpCtrl.PutFieldText('관리번호', data.mgmtno);
        // console.log("관리번호 넣음");
        apprFlag.value = "Y";
      }
    }

    // 사본번호
    if (data.hasOwnProperty('copyno')) {
      if (data.copyno != '' && data.copyno != undefined) {
        let copyno = "";
        // console.log("copyno: ", data.copyno);
        let copyArr = data.copyno.split('/');
        if (copyArr[0].length == 1) {
          copyno = ' ' + copyArr[0] + '    ' + copyArr[1];
        } else {
          copyno = copyArr[0] + '    ' + copyArr[1];
        }
        hwpCtrl.PutFieldText('사본번호', copyno);
        // console.log("사본번호 넣음");
        apprFlag.value = "Y";
      }
    }

    //본문
    data.draftcontent = hwpCtrl.GetFieldText('본문');

    //제목
    // console.log("docttl : ", data.docttl)
    // console.log("draftdocttl : ", data.draftdocttl.substring(0, data.draftdocttl.length-4));
    // console.log("hwpCtrl.GetFieldText('결재제목'): ", hwpCtrl.GetFieldText('결재제목'));

    // let title = hwpCtrl.GetFieldText('결재제목') != ''?(hwpCtrl.GetFieldText('결재제목') == data.docttl ? hwpCtrl.GetFieldText('결재제목') : data.docttl):data.docttl;
    if (hwpCtrl.GetFieldText('결재제목') == '') {
      // hwpCtrl.PutFieldText('결재제목', title);
      hwpCtrl.PutFieldText('결재제목', data.docttl);
      // console.log("제목 넣음1");
      window.sessionStorage.setItem('changeFlag', 'Y');
    } else {
      if (hwpCtrl.GetFieldText('결재제목') != data.docttl) {
        if (((hwpCtrl.GetFieldText('결재제목') != data.draftdocttl.substring(0, data.draftdocttl.length - 4)))
          && (data.docttl == data.draftdocttl.substring(0, data.draftdocttl.length - 4))) {
          let title = hwpCtrl.GetFieldText('결재제목');
          hwpCtrl.PutFieldText('결재제목', title);
          // console.log("제목 넣음2");
          window.sessionStorage.setItem('changeFlag', 'Y');
        }

        if (((data.docttl != data.draftdocttl.substring(0, data.draftdocttl.length - 4)))
          && (hwpCtrl.GetFieldText('결재제목') == data.draftdocttl.substring(0, data.draftdocttl.length - 4))) {
          let title = data.docttl;
          hwpCtrl.PutFieldText('결재제목', title);
          // console.log("제목 넣음3");
          window.sessionStorage.setItem('changeFlag', 'Y');
        }
      }
    }

    data.draftdocttl = hwpCtrl.GetFieldText('결재제목') + '.hwp';

    //수신
    let recv = hwpCtrl.GetFieldText('수신') != '' ? (hwpCtrl.GetFieldText('수신') == formatRecvList.value ? hwpCtrl.GetFieldText('수신') : formatRecvList.value) : formatRecvList.value;
    if (hwpCtrl.GetFieldText('수신') == '') {
      if (data.gubun == 'DOPT2' && data.recvList.length > 0) {
        hwpCtrl.PutFieldText('수신', recv);
        // console.log("수신 넣음1");
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    } else {
      // console.log("formatRecvList.value: ", formatRecvList.value)

      if (hwpCtrl.GetFieldText('수신') != formatRecvList.value) {
        hwpCtrl.PutFieldText('수신', recv);
        // console.log("수신 넣음2");
        window.sessionStorage.setItem('changeFlag', 'Y');
      }

      if (formatRecvList.value == undefined) {
        hwpCtrl.PutFieldText('수신', ' ');
        // console.log("수신 넣음3");
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    }

    //로고
    if (data.logourl != "") {
      // undefined이면 지워줌
      if (data.logurl == undefined || data.symbolurl == undefined) {
        hwpCtrl.MoveToField("logo", true, true, true);
        hwpCtrl.Run("Delete");

        hwpCtrl.MoveToField("symbol", true, true, true);
        hwpCtrl.Run("Delete");
      }

      hwpCtrl.MoveToField("logo", true, true, true);
      hwpCtrl.Run("Delete");

      hwpCtrl.InsertPicture(data.logourl, true, 1, false, false, 0, 17, 17, (ctrl) => {
        if (ctrl) {
          // console.log("로고 성공");
          window.sessionStorage.setItem('changeFlag', 'Y');

          //심볼
          hwpCtrl.MoveToField("symbol", true, true, true);
          hwpCtrl.Run("Delete");
          hwpCtrl.InsertPicture(data.symbolurl, true, 1, false, false, 0, 17, 17, (ctrl) => {
            if (ctrl) {
              // console.log("심볼 성공");
              window.sessionStorage.setItem('changeFlag', 'Y');
            } else {
              // console.log("심볼 실패");
            }
          })
        } else {
          // console.log("로고 실패");
        }
      })
    }

    // 결재경로
    if (data.hasOwnProperty("pathList")) {
      if (data.pathList.length > 1) {
        // 경로가 삭제된 경우 체크해서 해당 데이터는 삭제해줌
        // console.log('hwpCtrl.GetFieldList(0, 1): ', hwpCtrl.GetFieldList(0, 1).split('\x02'));
        let fieldList = hwpCtrl.GetFieldList(0, 1).split('\x02');
        let pathFieldList = [];
        for (let i = 0; i < fieldList.length; i++) {
          if (fieldList[i].includes('직위')) {
            pathFieldList.push(fieldList[i]);
          }
        }

        for (let i = pathFieldList.length - 1; i >= 0; i--) {
          if (hwpCtrl.GetFieldText('직위.' + (i + 1)) == '') {
            pathFieldList.splice(i, 1);
          }
        }

        // console.log('pathFieldList: ', pathFieldList);
        // console.log('길이 비교: ', data.pathList.length - 1 +'!='+ pathFieldList.length)
        if (data.pathList.length - 1 != pathFieldList.length) {
          for (let i = 0; i < pathFieldList.length; i++) {
            // console.log('직위/서명 삭제');
            hwpCtrl.PutFieldText('직위.' + (i + 1), ' ');
            hwpCtrl.PutFieldText('서명.' + (i + 1), ' ');
          }
        }

        // console.log('data.pathList: ', data.pathList);

        for (let i = 1; i < data.pathList.length; i++) {
          if (hwpCtrl.GetFieldText('직위.' + i) == '' && hwpCtrl.GetFieldText('서명.' + i) == '') {
            // hwpCtrl.PutFieldText('구분.' + i, data.pathList[i].asktype);
            hwpCtrl.PutFieldText('직위.' + i, data.pathList[i].dutyname);
            hwpCtrl.PutFieldText('서명.' + i, data.pathList[i].objname);
            // console.log("직위/서명 넣음1");
            window.sessionStorage.setItem('changeFlag', 'Y');
          } else {
            if (hwpCtrl.GetFieldText('직위.' + i) != data.pathList[i].dutyname && hwpCtrl.GetFieldText('서명.' + i) != data.pathList[i].objname) {
              hwpCtrl.PutFieldText('직위.' + i, data.pathList[i].dutyname);
              hwpCtrl.PutFieldText('서명.' + i, data.pathList[i].objname);
              // console.log("직위/서명 넣음2");
              window.sessionStorage.setItem('changeFlag', 'Y');
            }
          }
        }
      }
    }

    /*--------------------------- 붙임 시작 ---------------------------*/
    if (data.hasOwnProperty('newFileList')) {
      if (data.newFileList.length > 0 || (data.fileList.length > 0 && data.fileList[0].fleid != '')) {
        hwpCtrl.MoveToField("붙임", true, true, true);
        hwpCtrl.EditMode = 1;
        hwpCtrl.Run("Delete");

        // 테이블생성
        var tbact = hwpCtrl.CreateAction("TableCreate");
        var tbset = tbact.CreateSet();
        tbact.GetDefault(tbset);
        let rows = (data.fileList.length == 0 ? 0 : (data.fileList[0].fleid == '' ? 0 : data.fileList.length)) + (data.newFileList.length == 0 ? 0 : data.newFileList.length);
        tbset.SetItem("Rows", rows);
        tbset.SetItem("Cols", 2);
        tbact.Execute(tbset);

        // 테이블전체선택
        hwpCtrl.Run("TableCellBlock");
        hwpCtrl.Run("TableCellBlockExtend");
        hwpCtrl.Run("TableCellBlockExtend");

        // 테이블 border set
        var tbact2 = hwpCtrl.CreateAction("CellBorderFill");
        var tbset2 = tbact2.CreateSet();
        tbact2.GetDefault(tbset2);
        tbset2.SetItem("TypeVert", 0);
        tbset2.SetItem("TypeHorz", 0);
        tbset2.SetItem("BorderTypeLeft", 0);
        tbset2.SetItem("BorderTypeRight", 0);
        tbset2.SetItem("BorderTypeTop", 0);
        tbset2.SetItem("BorderTypeBottom", 0);
        tbact2.Execute(tbset2);

        // 테이블선택취소
        hwpCtrl.Run("Cancel");

        // 테이블셀이동
        hwpCtrl.Run("TableLeftCell");
        for (let row = 0; row < rows; row++) {
          hwpCtrl.Run("TableUpperCell");
        }

        // 텍스트입력
        var tbact3 = hwpCtrl.CreateAction("InsertText");
        var tbset3 = tbact3.CreateSet();
        tbact3.GetDefault(tbset3);
        tbset3.SetItem("Text", "붙임 :");
        tbact3.Execute(tbset3);

        // for문 돌면서 파일명 텍스트 넣어줌
        // 기존 파일이 존재하는 경우
        hwpCtrl.Run("Cancel");
        let fileListLength = data.fileList.length;
        if (fileListLength > 0 && data.fileList[0].fleid != '') {
          for (let i = 0; i < fileListLength; i++) {
            if (i > 0) {
              hwpCtrl.Run("TableRightCell");
              hwpCtrl.Run("TableRightCell");
            } else {
              hwpCtrl.Run("TableRightCell");
            }
            var tbact4 = hwpCtrl.CreateAction("InsertText");
            var tbset4 = tbact4.CreateSet();
            tbact4.GetDefault(tbset4);
            tbset4.SetItem("Text", i + 1 + ". " + data.fileList[i].sfilename);
            tbact4.Execute(tbset4);
            if (data.newFileList.length == 0 && i == fileListLength - 1) {
              break;
            }
          }
          if (data.newFileList.length == 0) {
            var tbact5 = hwpCtrl.CreateAction("InsertText");
            var tbset5 = tbact5.CreateSet();
            tbact5.GetDefault(tbset5);
            tbset5.SetItem("Text", '  끝.');
            tbact5.Execute(tbset5);
          }
        } else {
          fileListLength = 0;
        }

        for (let j = 0; j < data.newFileList.length; j++) {
          if (j > 0) {
            hwpCtrl.Run("TableRightCell");
            hwpCtrl.Run("TableRightCell");
          } else {
            if (data.fileList.length > 0 && data.fileList[0].fleid != '') {
              hwpCtrl.Run("TableRightCell");
              hwpCtrl.Run("TableRightCell");
            } else {
              hwpCtrl.Run("TableRightCell");
            }
          }
          var tbact4 = hwpCtrl.CreateAction("InsertText");
          var tbset4 = tbact4.CreateSet();
          tbact4.GetDefault(tbset4);
          tbset4.SetItem("Text", (j + 1 + fileListLength) + '. ' + data.newFileList[j].file.name);
          tbact4.Execute(tbset4);
          if (j == data.newFileList.length - 1) {
            var tbact5 = hwpCtrl.CreateAction("InsertText");
            var tbset5 = tbact5.CreateSet();
            tbact5.GetDefault(tbset5);
            tbset5.SetItem("Text", '  끝.');
            tbact5.Execute(tbset5);
          }
        }

        // 셀 width set
        hwpCtrl.Run("TableLeftCell");
        hwpCtrl.Run("TableCellBlock");
        hwpCtrl.Run("TableCellBlockCol");
        for (let i = 0; i < 68; i++) {
          hwpCtrl.Run("TableResizeCellLeft");
        }

        if(props.view.data == "N") {
          hwpCtrl.EditMode = 2;
        } else {
          hwpCtrl.EditMode = 0;
        }
        
        // console.log("붙임 넣음");
        window.sessionStorage.setItem('changeFlag', 'Y');
      } else {
        if (data.hasOwnProperty('delFileList')) {
          if (data.delFileList.length > 0) {
            hwpCtrl.EditMode = 1;
            hwpCtrl.MoveToField("붙임", true, true, true);
            hwpCtrl.Run("Delete");
            if (props.view.data == "N") {
              hwpCtrl.EditMode = 2;
            } else {
              hwpCtrl.EditMode = 0;
            }
          }
        }
      }

      if (data.hasOwnProperty('delFileList') || data.hasOwnProperty('newFileList')) {
        if (data.delFileList.length > 0 || data.newFileList.length > 0) {
          hwpCtrl.IsModified = true;
        } else {
          if(apprFlag.value == 'N') {
            hwpCtrl.IsModified = false;
          }
          window.sessionStorage.setItem('changeFlag', 'N');
        }
      }
    }
    hwpCtrl.Run("Cancel");
    /*--------------------------- 붙임 끝 ---------------------------*/

    // 발신기관명
    // if(data.sendOrgNameData != '') {
    //   hwpCtrl.MoveToField('발신기관명', true, true, true);
    //   hwpCtrl.Run("Delete");
    //   hwpCtrl.PutFieldText('발신기관명', data.sendOrgNameData);
    // }

    // 발신명의
    if (data.sendNameData != '') {
      // hwpCtrl.MoveToField('발신명의', true, true, true);
      // hwpCtrl.Run("Delete");
      if (hwpCtrl.GetFieldText('발신명의') != data.sendNameData) {
        hwpCtrl.PutFieldText('발신명의', data.sendNameData);
        // console.log("발신명의 넣음");
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    }

    // 비밀등급
    let secretText = ""
    if (data.seclevel == '2') { // 2등급
      // hwpCtrl.MoveToField('등급', true, true, true);
      // hwpCtrl.Run("Delete");
      secretText = `II 급 비 밀
    CONFIDENTIAL`;
    } else if (data.seclevel == '3') { // 3등급
      // hwpCtrl.MoveToField('등급', true, true, true);
      // hwpCtrl.Run("Delete");
      secretText = `III 급 비 밀
    CONFIDENTIAL`;
    } else { // 일반 or 선택 안한 경우
      // hwpCtrl.MoveToField('등급', true, true, true);
      // hwpCtrl.Run("Delete");
      secretText = `일 반 비 밀
    CONFIDENTIAL`;
    }

    if (hwpCtrl.GetFieldText('등급') == '') {
      hwpCtrl.PutFieldText('등급', secretText);
      // console.log("등급 넣음");
      window.sessionStorage.setItem('changeFlag', 'Y');
    } else {
      // console.log("hwpCtrl.GetFieldText('등급'): ", hwpCtrl.GetFieldText('등급'));
      // console.log("secretText: ", secretText);
      if (hwpCtrl.GetFieldText('등급').substring(0, 7) != secretText.substring(0, 7)) {
        hwpCtrl.PutFieldText('등급', secretText);
        // console.log("등급 넣음");
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    }

    // 관인/서명인
    if (data.hasOwnProperty('imageFileData')) {
      if (data.imageFileData.fileurl != '') {
        hwpCtrl.EditMode = 1;
        hwpCtrl.MoveToField('관인서명인', true, true, true);
        hwpCtrl.Run("Delete");

        hwpCtrl.InsertPicture(data.imageFileData.fileurl/*"http://172.18.18.2:8581/public/fileUpload/com/f55ba95e-e911-4006-b4e8-f8b0007fcf71.png"*/, true, 1, false, false, 0, 17, 17, (ctrl) => {
          if (ctrl) {
            // console.log("관인/서명인 성공");
            hwpCtrl.EditMode = 2;
            window.sessionStorage.setItem('changeFlag', 'Y');
          } else {
            // console.log("관인/서명인 실패");
            hwpCtrl.EditMode = 2;
          }
        })
      }
    }

    // 본문으로 이동
    // hwpCtrl.MoveToField('본문', true, true, true);

    // 전화번호, 이메일
    if (data.hasOwnProperty('email')) {
      if (data.email != null && data.email != undefined) {
        hwpCtrl.PutFieldText('%이메일', data.email);
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    }

    if (data.hasOwnProperty('phone')) {
      if (data.phone != null && data.phone != undefined) {
        hwpCtrl.PutFieldText('%전화', data.phone);
        window.sessionStorage.setItem('changeFlag', 'Y');
      }
    }

  } catch (error) {
    console.log("error: ", error);
    // 저장로딩 false
    data.saveLoading = false;

    alert("데이터 동기화 Error");
  }
}

// 편집가능 필드 설정
const editableField = () => {
  // 제목
  hwpCtrl.MoveToField("결재제목");
  var tableAction = hwpCtrl.CreateAction("TablePropertyDialog");
  var tableSet = tableAction.CreateSet();
  if (tableSet == null) {
    return false;
  }
  tableAction.GetDefault(tableSet);
  var cellSet = tableSet.Item("ShapeTableCell");
  cellSet.SetItem("Editable", 1);
  tableAction.Execute(tableSet);

  // 본문
  hwpCtrl.MoveToField("본문");
  var action = hwpCtrl.CreateAction("ModifyFieldClickhere");
  var set = action.CreateSet();
  action.GetDefault(set);
  set.SetItem("Editable", 1);
  action.Execute(set);
}

// 수신처
const formatRecvList = computed(() => {
  // console.log("props.received: ", props.received);
  if(props.received == false) {
    if (props.data.hasOwnProperty('recvList')) {
      if (props.data.recvList.length > 0) {
        if (props.data.recvList.length == 0 || props.data.recvList[0].apprrecvdeptname == "") {
          return
        } else {
          const formattedRecvList = props.data.recvList.map((item) => {
            const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
            return `${item.apprrecvdeptname}${refDeptName}`;
          });
          return formattedRecvList.join(', ');
        }
      }
    }
  }
});



//표준기안 버튼 클릭
const standardDraft = () => {
  // 스피너 true
  isloading.value = true;

  props.data.drafturl = "";

  fileOpen(props.data);
}

// 서식변경 팝업
const bVisibleDctFormPopup = ref(false)
const dctFormPopupReuturn = ref({})
const dctFormPopupArgs = ref({})
const dctFormPopupToggleFunc = ref(() => {
  bVisibleDctFormPopup.value = !bVisibleDctFormPopup.value;
})
const dctFormPopupReturnFunc = ref((retValue) => {
  dctFormPopupToggleFunc.value()
  dctFormPopupReuturn.value = retValue;

  if (retValue != "") {
    // 스피너 true
    isloading.value = true;

    props.data.drafturl = retValue.fileurl;
    fileOpen(props.data);
  } else {
    // 스피너 false
    isloading.value = false;
  }
})

// watch(() => contentWidth.value, (newWidth) => {
//   console.log("Width 변경");

//   hwpCtrl = {};
//   console.log("hwpCtrl: ", hwpCtrl);

//   // 기존의 div 태그를 찾아 제거
//   var existingDiv = document.getElementById("hwpctrl");
//   if (existingDiv) {
//     existingDiv.parentNode.removeChild(existingDiv);
//   }

//   // 새로운 div 태그 생성
//   var newDiv = document.createElement("div");
//   newDiv.id = "hwpctrl";

//   if(props.view.data == 'N') {
//     newDiv.style = "width:99.6%; height:87%;";
//   } else {
//     newDiv.style = "width:99.6%; height:92%;";
//   }

//   var targetDiv = document.getElementById("temp");

//   console.log("targetDiv: ", targetDiv)

//   if(targetDiv) {
//     targetDiv.parentNode.insertBefore(newDiv, targetDiv.nextSibling);
//     console.log(">>>>>>>Div 생성")
//   }

//   createHwpCtrl();
// });

// watch(() => storeContentHeight.value, (newHeight) => {
//   console.log("Height 변경");
//   console.log("storeContentHeight.value: ", storeContentHeight.value);
//   console.log("newHeight: ", newHeight);

//   hwpCtrl = {};

//   // 기존의 div 태그를 찾아 제거
//   var existingDiv = document.getElementById("hwpctrl");
//   if (existingDiv) {
//     existingDiv.parentNode.removeChild(existingDiv);
//   }

//   // 새로운 div 태그 생성
//   var newDiv = document.createElement("div");
//   newDiv.id = "hwpctrl";

//   if (props.view.data == 'N') {
//     newDiv.style = "width:99.6%; height:87%;";
//   } else {
//     newDiv.style = "width:99.6%; height:92%;";
//   }
//   console.log("newDiv: ", newDiv);

//   var targetDiv = document.getElementById("temp");

//   console.log("targetDiv: ", targetDiv)

//   if (targetDiv) {
//     targetDiv.parentNode.insertBefore(newDiv, targetDiv);
//   }

//   createHwpCtrl();
// });

defineExpose({ draftToggle, saveFile });

</script>