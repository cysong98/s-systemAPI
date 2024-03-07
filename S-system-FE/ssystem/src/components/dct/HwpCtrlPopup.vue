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
          <div id="hwpctrl" style="width:100%; height:642px;">
              <!-- ADD IFRAME -->
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-table class="table-type-03-hist" >
            <colgroup>
              <col width="120">
            </colgroup>
            <tbody>
              <tr>
                <th>붙임</th>
                <td>
                  <div v-if="fileHist != []" style="overflow-y: auto; max-height: 50px; margin-right: -16px;">
                    <div class="box-flex" v-for="(fileData, idx) in fileHist" :key="idx">
                      <v-icon :color="getFileIcon(getExtension(fileData.sfilename)).color">{{ getFileIcon(getExtension(fileData.sfilename)).icon }}</v-icon>
                      <span @click="toggleViewerPopup(fileData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ fileData.sfilename }}</span>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="buttons-bottom">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      </div>

      <div v-if="isloading" class="overlay">
        <div class="spinner"></div>
      </div>
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
      <iframe id="shieldViewer" :src="url" width="1200" height="840" style="padding: 10px;"></iframe>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";

import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";
import { getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
const name = ref('DctProcPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
  received: Boolean
})
const args = ref({})
const fileHist = ref([]);

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')

const isloading = ref(false);

// 웹한글기안기 객체
var hwpCtrl = {};

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args
  // 스피너 true
  isloading.value = true;

  // 붙임파일 이력
  let indtArr = [];
  for (let i = 0; i < args.value.data.fileListAll.length; i++) {
    if (args.value.objid == args.value.data.fileListAll[i].inid) {
      indtArr.push(Number(args.value.data.fileListAll[i].indt));
    }
  }

  let maxIndt = Math.max(...indtArr);

  for (let i = 0; i < args.value.data.fileListAll.length; i++) {
    if (args.value.objid == args.value.data.fileListAll[i].inid) {
      if(maxIndt == Number(args.value.data.fileListAll[i].indt)) {
        fileHist.value.push(args.value.data.fileListAll[i]);
      }
    }
  }

  // location.href 가져와서 url 생성
  let href = location.href;
  let url = "";
  if ((href.match(/:/g) || []).length > 1) {
    url = href.split(':')[0] + ':' + href.split(':')[1] + ':' + href.split(':')[2].split('/')[0] + '/webhwpctrl/';
  } else {
    url = href.split(':')[0] + '://' + href.split(':')[1].split('/')[2] + '/webhwpctrl/';
  }

  // 웹한글기안기 객체 생성
  hwpCtrl = BuildWebHwpCtrl("hwpctrl", /*"http://localhost:8080/webhwpctrl/"*/url, function () { fileOpen(args.value); });
})

watchEffect(() => {
  args.value = props.args
})

const fileOpen = (data) => {
  try {
    hwpCtrl.Open(data.url, "HWP", "lock:false", function () {
      hwpCtrl.EditMode = 0;
      hwpCtrl.ShowToolBar(false);
      hwpCtrl.ShowStatusBar(false);
      hwpCtrl.ShowRibbon(false);
      
      if(data.asktype == 'DEA41' || data.asktype == 'DEA45' || data.asktype == 'DEA51' || props.received == true) {
        // 관리번호
        if (data.data.hasOwnProperty('mgmtno')) {
          if (data.data.mgmtno != '') {
            hwpCtrl.PutFieldText('관리번호', data.data.mgmtno);
          }
        }

        // 사본번호
        if (data.data.hasOwnProperty('copyno')) {
          if (data.data.copyno != '' && data.data.copyno != undefined) {
            let copyno = "";
            let copyArr = data.data.copyno.split('/');
            if (copyArr[0].length == 1) {
              copyno = ' ' + copyArr[0] + '    ' + copyArr[1];
            } else {
              copyno = copyArr[0] + '    ' + copyArr[1];
            }
            hwpCtrl.PutFieldText('사본번호', copyno);
          }
        }
      }

      // 스피너 false
      isloading.value = false;
    });
  } catch (error) {
    // 스피너 false
    isloading.value = false;
    alert("Server Error");
  }
};

const closePopUp = () => {
  // to do
  props.toggleFunc();
};

// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  docloading.value = true;
  visibleViewerPopup.value = !visibleViewerPopup.value;
  await viewDocument(file, userid, 'N,N', '0');
  document.querySelector('#shieldViewer').addEventListener('load', function () {
    docloading.value = false;
  });
}

</script>

<style lang="scss" scoped></style>
