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
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>비밀등급</th>
                <td>{{ transformSeclevel(docData.seclevel) }}</td>
                <th>관리번호</th>
                <td>{{ docData.mgmtno }}</td>
                <th>사본번호</th>
                <td>{{ docData.copyno }}</td>
              </tr>
              <tr>
                <th>문서 제목</th>
                <td colspan="5">{{ docData.docttl }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-table class="table-type-04" height="380" fixed-header>
            <thead>
              <tr>
                <th>구분</th>
                <th>일시</th>
                <th>성명</th>
                <th>소속</th>
                <th>서명</th>
                <th>상태</th>
                <th>기타</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(hist, idx) in getHistCardInfoList" :key="idx">
                <td>{{ hist.histgubun }}</td>
                <td>{{ hist.histdt }}</td>
                <td>{{ hist.username }}</td>
                <td>{{ hist.deptname }}</td>
                <td class="signature">{{ hist.username }}</td>
                <td>{{ hist.histstatus }}</td>
                <td>{{ hist.etc }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import _ from 'lodash';
import { transformSeclevel, transHistStatus, transHistGubun, transformAsktype } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('DctHistoryCardPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const urlPaths =  ref('')
const args = ref({})
const docid = ref({docid:""})
const docData = ref({})
const getHistCardInfoList = ref([])

onBeforeMount(() => {
  args.value = props.args
  docid.value.docid = args.value.docid;
})

onMounted(async () => {
  await getDctRdoc();
  await getDctAttr();
  await getHistCardInfo();
  await selectDctPath();
})


const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({...docid.value }, urlPaths.value);
    const data = response.data;
    docData.value.docttl = data.docttl;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getDctAttr = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({...docid.value }, urlPaths.value);
    const data = response.data;
    docData.value.seclevel = data.seclevel;
    docData.value.mgmtno = data.mgmtno;
    docData.value.copyno = data.copyno;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getHistCardInfo = async () => {
  try {
    const response = await API.dctAPI.getHistCardInfo(docid.value, urlPaths.value);
    response.data = response.data.map((u, idx) => {
      u = { ...u, histdt: dayjs(u.histdt, 'YYYYMMDDHHmmss').format('YYYY.MM.DD HH:mm') };
      u = { ...u, histstatus: transHistStatus(u.histstatus) };
      u = { ...u, histgubun: transHistGubun(u.histgubun) };
      return { ...u};
    });
    getHistCardInfoList.value = response.data;
    
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}


const selectDctPath = async () => {
  try {
    const response = await API.dctAPI.selectDctPath({
      ...docid.value
    },
      urlPaths.value
    );
    docData.value.pathList = [];
    for (let i = 0; i < response.data.length; i++) {
      const upd = {};
      upd.asktype = response.data[i].asktype;
      upd.deptid = response.data[i].deptid;
      upd.deptname = response.data[i].deptname;
      upd.dutyname = response.data[i].dutyname;
      upd.dutynamedesc = response.data[i].dutynamedesc;
      upd.objid = response.data[i].objid;
      upd.objname = response.data[i].objname;

      docData.value.pathList.push(upd);
    }

    for(let j=0; j<getHistCardInfoList.value.length; j++) {
      for(let k=0; k<docData.value.pathList.length; k++) {
        if(getHistCardInfoList.value[j].userid == docData.value.pathList[k].objid) {
          if(getHistCardInfoList.value[j].histgubun == '결재') {
            getHistCardInfoList.value[j].histgubun = transformAsktype(docData.value.pathList[k].asktype);
          }
          if (getHistCardInfoList.value[j].histstatus == '결재') {
            getHistCardInfoList.value[j].histstatus = '승인';
          }
        }
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};



const closePopUp = () => {
  props.toggleFunc();
};

const confirmPopUp = () => {
  props.returnFunc();
};

</script>