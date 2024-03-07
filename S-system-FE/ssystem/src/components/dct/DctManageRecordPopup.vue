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
            <tbody>
              <tr>
                <th>비밀등급</th>
                <td>{{ transformSeclevel(getMgmtRegiList.seclevel) }}</td>
                <th>관리번호</th>
                <td>{{ getMgmtRegiList.mgmtno }}</td>
                <th>문서번호</th>
                <td>{{ getDctAttrList.docno }}</td>
                <th>사본번호</th>
                <td>{{ getMgmtRegiList.copyno }}</td>
                <th>형태</th>
                <td>{{ getMgmtRegiList.regirecvtype == "1" ? '전자' : '비전자' }}</td>
              </tr>
              <tr>
                <th>문서 제목</th>
                <td colspan="9">{{ getMgmtRegiList.secttl }}</td>
              </tr>
              <tr>
                <th>생산/접수일자</th>
                <td colspan="2">{{ dayjs(getMgmtRegiList.regirecvdt).format('YYYY.MM.DD') }}</td>
                <th>생산처</th>
                <td colspan="2">{{ getMgmtRegiList.regirecvsendname }}</td>
                <th>수신처</th>
                <td colspan="3">{{ formatRecvList }}</td>
              </tr>
              <tr>
                <th>예고문 보호기간</th>
                <td colspan="2">{{ dayjs(getMgmtRegiList.notiprotterm).format('YYYY.MM.DD') }}</td>
                <th>예고문 보존기간</th>
                <td colspan="2">{{ transformNotiprsrvterm(getMgmtRegiList.notiprsrvterm) }}</td>
                <th>보관장소</th>
                <td colspan="3" v-if="getMgmtRegiList.regirecvtype=='2'">{{ getMgmtRegiList.locker }}</td>
                <td colspan="3" v-else></td>
              </tr>
              <tr>
                <th rowspan="4">재분류</th>
                <th>처리일자</th>
                <td colspan="8">{{ getMgmtRegiList.reclassdt != null ? dayjs(getMgmtRegiList.reclassdt).format('YYYY.MM.DD') : "" }}</td>
              </tr>
              <tr>
                <th>사유</th>
                <td colspan="8">{{ getMgmtRegiList.reclassbasis }}</td>
              </tr>
              <tr>
                <th>요청자</th>
                <td colspan="8">{{ reclassRequester }}</td>
              </tr>
              <tr>
                <th>확인자</th>
                <td colspan="8">{{ reclassApprover }}</td>
              </tr>
              <tr>
                <th rowspan="4">파기</th>
                <th>처리일자</th>
                <td colspan="8">{{ getMgmtRegiList.destructdt != null ? dayjs(getMgmtRegiList.destructdt).format('YYYY.MM.DD') : "" }}</td>
              </tr>
              <tr>
                <th>사유</th>
                <td colspan="8">{{ getMgmtRegiList.destructbasis }}</td>
              </tr>
              <tr>
                <th>요청자</th>
                <td colspan="8">{{ destructRequester }}</td>
              </tr>
              <tr>
                <th>확인자</th>
                <td colspan="8">{{ destructApprover }}</td>
              </tr>
              <tr>
                <th rowspan="4">이관</th>
                <th>처리일자</th>
                <td colspan="8">{{ getMgmtRegiList.transdt != null ? dayjs(getMgmtRegiList.transdt).format('YYYY.MM.DD') : "" }}</td>
              </tr>
              <tr>
                <th>사유</th>
                <td colspan="8">{{ getMgmtRegiList.transbasis }}</td>
              </tr>
              <tr>
                <th>요청자</th>
                <td colspan="8">{{ transRequester }}</td>
              </tr>
              <tr>
                <th>확인자</th>
                <td colspan="8">{{ transApprover }}</td>
              </tr>
              <tr>
                <th rowspan="4">존안</th>
                <th>처리일자</th>
                <td colspan="8">{{ getMgmtRegiList.cnsrvdt != null ? dayjs(getMgmtRegiList.cnsrvdt).format('YYYY.MM.DD') : "" }}</td>
              </tr>
              <tr>
                <th>사유</th>
                <td colspan="8">{{ getMgmtRegiList.cnsrvbasis }}</td>
              </tr>
              <tr>
                <th>요청자</th>
                <td colspan="8">{{ cnsrvRequester }}</td>
              </tr>
              <tr>
                <th>확인자</th>
                <td colspan="8">{{ cnsrvApprover }}</td>
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
import { transformSeclevel, transformStatus,transformState, transformAsktype, transformDate, transformNotiprsrvterm } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('DctManageRecordPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const urlPaths =  ref('')
const args = ref({})
const getMgmtRegiCondi = ref({
  mgmtid: []
})
const getMgmtRegiList = ref([])
const getDctAttrCondi = ref({
  docid: ""
})
const getDctAttrList = ref([])
const getDctRecvList = ref([])
const selectReclassPathInfoList = ref([])
const selectDestructPathInfoList = ref([])
const selectTransPathInfoList = ref([])
const selectCnsrvPathInfoList = ref([])

const reclassRequester = ref('')
const reclassApprover = ref('')
const destructRequester = ref('')
const destructApprover = ref('')
const transRequester = ref('')
const transApprover = ref('')
const cnsrvRequester = ref('')
const cnsrvApprover = ref('')

onBeforeMount(() => {
  args.value = props.args
  getMgmtRegiCondi.value.mgmtid = args.value.mgmtid;
  getDctAttrCondi.value.docid = args.value.docid;
})

onMounted(async () => {
  await getMgmtRegi();
  await getDctAttr();
  await getDctRecv();
  await selectReclassPathInfo()
  await selectDestructPathInfo()
  await selectTransPathInfo()
  await selectCnsrvPathInfo()
})


const getMgmtRegi = async () => {
  try {
    const response = await API.dctAPI.getMgmtRegi(getMgmtRegiCondi.value, urlPaths.value);
    getMgmtRegiList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const getDctAttr = async () => {
  try {
    const response = await API.dctAPI.getDctAttr(getDctAttrCondi.value, urlPaths.value);
    getDctAttrList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const getDctRecv = async () => {
  try {
    const response = await API.dctAPI.getDctRecv(getDctAttrCondi.value, urlPaths.value);
    getDctRecvList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}
const formatRecvList = computed(() => {
  const formattedRecvList = getDctRecvList.value.map((item) => {
    return item.deptname;
  });
  return formattedRecvList.join(', ');
});

const selectReclassPathInfo = async () => {
  try {
    const response = await API.useAPI.selectReclassPathInfo(getMgmtRegiCondi.value, urlPaths.value);
    selectReclassPathInfoList.value = response.data;
    const length = selectReclassPathInfoList.value.length;
    if(length>0){
      reclassRequester.value = selectReclassPathInfoList.value[0].apprname;
      reclassApprover.value = selectReclassPathInfoList.value[length-1].apprname;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectDestructPathInfo = async () => {
  try {
    const response = await API.useAPI.selectDestructPathInfo(getMgmtRegiCondi.value, urlPaths.value);
    selectDestructPathInfoList.value = response.data;
    const length = selectDestructPathInfoList.value.length;
    if(length>0){
      destructRequester.value = selectDestructPathInfoList.value[0].apprname;
      destructApprover.value = selectDestructPathInfoList.value[length-1].apprname;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectTransPathInfo = async () => {
  try {
    const response = await API.useAPI.selectTransPathInfo(getMgmtRegiCondi.value, urlPaths.value);
    selectTransPathInfoList.value = response.data; 
    const length = selectTransPathInfoList.value.length;
    if(length>0){
      transRequester.value = selectTransPathInfoList.value[0].apprname;
      transApprover.value = selectTransPathInfoList.value[length-1].apprname;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectCnsrvPathInfo = async () => {
  try {
    const response = await API.useAPI.selectCnsrvPathInfo(getMgmtRegiCondi.value, urlPaths.value);
    selectCnsrvPathInfoList.value = response.data;
    const length = selectCnsrvPathInfoList.value.length;
    if(length>0){
      cnsrvRequester.value = selectCnsrvPathInfoList.value[0].apprname;
      cnsrvApprover.value = selectCnsrvPathInfoList.value[length-1].apprname;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const closePopUp = () => {
  props.toggleFunc();
};

</script>